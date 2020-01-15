/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data;

import graduationproject.data.models.Device;
import graduationproject.data.models.DeviceCPUState;
import graduationproject.data.models.DeviceInterfaceDynamicData;
import graduationproject.data.models.DeviceMemoryState;
import graduationproject.data.models.DeviceNetworkInterface;
import graduationproject.helpers.DataConverter;
import graduationproject.snmpd.helpers.DeviceQueryHelper;
import graduationproject.snmpd.helpers.DeviceQueryHelper.MemoryType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author danh.nguyentranbao
 */
public class DataCompressor {

    private final int DAY_LIMIT = 7;

    public void compressDataOnInit() {
        List<Device> devices = DataManager.getInstance().getDeviceManager().getDevices();
        this.compressCPUData(devices);
        this.compressMemoryData(devices);
        this.compressInterfaceDynamicData(devices);
    }

    private void compressCPUData(List<Device> devices) {
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.SECOND, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.add(Calendar.DAY_OF_YEAR, -DAY_LIMIT);

        Calendar endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.SECOND, 59);
        endTime.set(Calendar.MINUTE, 59);
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        endTime.add(Calendar.DAY_OF_YEAR, (DAY_LIMIT - 2));
//        System.out.println(startTime.get(Calendar.DAY_OF_MONTH));
//        System.out.println(endTime.get(Calendar.DAY_OF_MONTH));

        List<DeviceCPUState> newDeviceCPUStates = new ArrayList<DeviceCPUState>();
        List<DeviceCPUState> oldCPUStates = DataManager.getInstance().getDeviceCpuManager().getDeviceCPUStates(startTime, endTime);
        for (DeviceCPUState oldState : oldCPUStates) {
            newDeviceCPUStates.add(new DeviceCPUState(oldState));
        }
//        System.out.println("OLD SIZE " + newDeviceCPUStates.size());

        startTime.add(Calendar.DAY_OF_YEAR, (DAY_LIMIT - 1));
        endTime.add(Calendar.DAY_OF_YEAR, 1);

        //compress data from yesterday
        for (Device device : devices) {
            List<Integer> cpuIds = DataManager.getInstance().getDeviceCpuManager().getDeviceCpuDeviceIds(device);

            for (int cpuId : cpuIds) {
                DeviceCPUState modelState = DataManager.getInstance().getDeviceCpuManager().getDeviceCPUState(cpuId, device);

                for (int i = 0; i < 24; i++) {
                    startTime.set(Calendar.HOUR_OF_DAY, i);
                    endTime.set(Calendar.HOUR_OF_DAY, i);

                    List<Float> cpuLoads = DataManager.getInstance().getDeviceCpuManager()
                            .getDeviceCPULoads(device, startTime, endTime, "cpuLoad", cpuId);
                    DeviceCPUState deviceCPUState = null;
                    if (cpuLoads != null && !cpuLoads.isEmpty()) {
                        float loadSum = 0;
                        for (float cpuLoad : cpuLoads) {
                            loadSum += cpuLoad;
                        }

                        deviceCPUState = new DeviceCPUState(cpuId, modelState.getFirmwareId(), modelState.getDescription(),
                                loadSum / cpuLoads.size(), startTime, true, device);
                    }

                    if (deviceCPUState != null) {
                        newDeviceCPUStates.add(deviceCPUState);
                    }
                }
            }
        }
//        System.out.println("COMPRESSED SIZE " + newDeviceCPUStates.size());
//        System.out.println(startTime.get(Calendar.DAY_OF_YEAR));
//        System.out.println(endTime.get(Calendar.DAY_OF_YEAR));

        //add data today
        startTime.add(Calendar.DAY_OF_YEAR, 1);
        endTime.add(Calendar.DAY_OF_YEAR, 1);
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        List<DeviceCPUState> newList = DataManager.getInstance().getDeviceCpuManager().getDeviceCPUStates(startTime, endTime);
        oldCPUStates = DataManager.getInstance().getDeviceCpuManager().getDeviceCPUStates(startTime, endTime);
        for (DeviceCPUState oldState : oldCPUStates) {
            newDeviceCPUStates.add(new DeviceCPUState(oldState));
        }

//        System.out.println("NEW SIZE " + newList.size());
//        System.out.println(startTime.get(Calendar.DAY_OF_YEAR));
//        System.out.println(endTime.get(Calendar.DAY_OF_YEAR));
        DataManager.getInstance().getDeviceCpuManager().renewDeviceCPUStateTable(newDeviceCPUStates);
    }

    private void compressMemoryData(List<Device> devices) {
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.SECOND, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.add(Calendar.DAY_OF_YEAR, -DAY_LIMIT);

        Calendar endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.SECOND, 59);
        endTime.set(Calendar.MINUTE, 59);
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        endTime.add(Calendar.DAY_OF_YEAR, (DAY_LIMIT - 2));

        String[] memoryTypes = {MemoryType.RAM.getDisplayType(), MemoryType.VIRTUAL.getDisplayType(), MemoryType.OTHER.getDisplayType()};

        List<DeviceMemoryState> newDeviceMemoryStates = new ArrayList<DeviceMemoryState>();
        List<DeviceMemoryState> oldStates = DataManager.getInstance().getDeviceMemoryManager()
                .getDeviceMemoryStates(startTime, endTime, null);
        for (DeviceMemoryState oldState : oldStates) {
            newDeviceMemoryStates.add(new DeviceMemoryState(oldState));
        }

        startTime.add(Calendar.DAY_OF_YEAR, DAY_LIMIT - 1);
        endTime.add(Calendar.DAY_OF_YEAR, 1);

        //compress data from yesterday
        for (Device device : devices) {
            for (String memoryType : memoryTypes) {
                DeviceMemoryState modelState = DataManager.getInstance().getDeviceMemoryManager().getDeviceMemoryState(memoryType, device);

                for (int i = 0; i < 24; i++) {
                    startTime.set(Calendar.HOUR_OF_DAY, i);
                    endTime.set(Calendar.HOUR_OF_DAY, i);

                    List<DeviceMemoryState> memoryStates = DataManager.getInstance().getDeviceMemoryManager()
                            .getDeviceMemoryStates(device, startTime, endTime, memoryType);
                    DeviceMemoryState deviceMemoryState = null;

                    if (memoryStates != null && !memoryStates.isEmpty()) {
                        float memorySum = 0.0f;
                        for (DeviceMemoryState memoryState : memoryStates) {
                            if (!memoryState.isIsSummarized()) {
//                                System.out.println(memorySum);
                                memorySum += memoryState.getUsagePercentage();
                            } else {
                                memorySum += memoryState.getUsedSize();
                            }
                        }
                        deviceMemoryState = new DeviceMemoryState(memoryType, memoryType,
                                memorySum / memoryStates.size(), memorySum / memoryStates.size(), startTime, true, device);
                    }

//                    deviceMemoryState.displayInfo();
                    if (deviceMemoryState != null) {
                        newDeviceMemoryStates.add(deviceMemoryState);
                    }
                }
            }
        }

        //add data today
        startTime.add(Calendar.DAY_OF_YEAR, 1);
        endTime.add(Calendar.DAY_OF_YEAR, 1);
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        oldStates = DataManager.getInstance().getDeviceMemoryManager().getDeviceMemoryStates(startTime, endTime, null);
        oldStates = DataManager.getInstance().getDeviceMemoryManager()
                .getDeviceMemoryStates(startTime, endTime, null);
        for (DeviceMemoryState oldState : oldStates) {
            newDeviceMemoryStates.add(new DeviceMemoryState(oldState));
        }

//        for (DeviceMemoryState memoryState : newDeviceMemoryStates) {
//            memoryState.displayInfo();
//        }
        DataManager.getInstance().getDeviceMemoryManager().renewDeviceMemoryStateTable(newDeviceMemoryStates);
    }

    public void compressInterfaceDynamicData(List<Device> devices) {
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.SECOND, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.add(Calendar.DAY_OF_YEAR, -DAY_LIMIT);

        Calendar endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.SECOND, 59);
        endTime.set(Calendar.MINUTE, 59);
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        endTime.add(Calendar.DAY_OF_YEAR, (DAY_LIMIT - 2));

        List<DeviceInterfaceDynamicData> newInterfaceDynamicDataList = new ArrayList<DeviceInterfaceDynamicData>();
        List<DeviceInterfaceDynamicData> oldStates = DataManager.getInstance()
                .getInterfaceDynamicDataManager().getDeviceDynamicData(null, startTime, endTime);
        for (DeviceInterfaceDynamicData oldState : oldStates) {
            newInterfaceDynamicDataList.add(new DeviceInterfaceDynamicData(oldState));
        }
        
        startTime.add(Calendar.DAY_OF_YEAR, DAY_LIMIT - 1);
        endTime.add(Calendar.DAY_OF_YEAR, 1);

        DeviceInterfaceDynamicData current, previous;
        float upperPart, lowerPart;
        DataConverter dataConverter = new DataConverter();

        //compress data from yesterday
        for (Device device : devices) {
            List<DeviceNetworkInterface> networkInterfaces = device.getNetworkInterfaces();

            for (DeviceNetworkInterface networkInterface : networkInterfaces) {
                for (int i = 0; i < 24; i++) {
                    startTime.set(Calendar.HOUR_OF_DAY, i);
                    endTime.set(Calendar.HOUR_OF_DAY, i);

                    List<DeviceInterfaceDynamicData> interfaceDataList = DataManager.getInstance()
                            .getInterfaceDynamicDataManager().getDeviceDynamicData(networkInterface, startTime, endTime);
                    DeviceInterfaceDynamicData newInterfaceData = null;

                    if (interfaceDataList != null && !interfaceDataList.isEmpty()) {
                        float interfaceSum = 0.0f;
                        int tempSize = interfaceDataList.size();

                        if (tempSize > 0 && interfaceDataList.get(0).isIsSummarized()) {
                            interfaceSum = interfaceDataList.get(0).getInboundBytes();
                        }

                        for (int j = 1; j < tempSize; j++) {
                            current = interfaceDataList.get(j);
                            if (!current.isIsSummarized()) {
                                previous = interfaceDataList.get(j - 1);
                                upperPart = Math.max(current.getInboundBytes() - previous.getInboundBytes(),
                                        current.getOutboundBytes() - previous.getOutboundBytes()) * 8 * 100;
                                lowerPart = (float) ((dataConverter.convertCalendarTimeToSecond(current.getUpdatedTime())
                                        - dataConverter.convertCalendarTimeToSecond(previous.getUpdatedTime())) * current.getBandwidth());
                                if (lowerPart != 0) {
                                    interfaceSum += (upperPart / lowerPart);
//                                    System.out.println("INTERFACE SUM:" + interfaceSum);
                                }
                            } else {
                                interfaceSum += current.getOutboundBytes();
                            }
                        }

                        if (tempSize - 1 > 0) {
                            newInterfaceData = new DeviceInterfaceDynamicData(0.0f, interfaceSum / (tempSize - 1),
                                    interfaceSum / (tempSize - 1), startTime, true, networkInterface);
                        } else {
                            newInterfaceData = new DeviceInterfaceDynamicData(0.0f, interfaceSum,
                                    interfaceSum, startTime, true, networkInterface);
                        }
                    }

                    if (newInterfaceData != null) {
//                        newInterfaceData.displayInfo();
                        newInterfaceDynamicDataList.add(newInterfaceData);
                    }
                }
            }
        }

        //add data today
        startTime.add(Calendar.DAY_OF_YEAR, 1);
        endTime.add(Calendar.DAY_OF_YEAR, 1);
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        oldStates = DataManager.getInstance()
                .getInterfaceDynamicDataManager().getDeviceDynamicData(null, startTime, endTime);
        for (DeviceInterfaceDynamicData oldState : oldStates) {
            newInterfaceDynamicDataList.add(new DeviceInterfaceDynamicData(oldState));
        }

        DataManager.getInstance().getInterfaceDynamicDataManager().renewDeviceInterfaceDynamicDataTable(newInterfaceDynamicDataList);
    }

}
