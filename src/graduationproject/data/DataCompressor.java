/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data;

import graduationproject.data.models.Device;
import graduationproject.data.models.DeviceAvgBandwidthUsage;
import graduationproject.data.models.DeviceAvgCpuUsage;
import graduationproject.data.models.DeviceAvgMemoryUsage;
import graduationproject.data.models.DeviceCPUState;
import graduationproject.data.models.DeviceInterfaceDynamicData;
import graduationproject.data.models.DeviceMemoryState;
import graduationproject.data.models.DeviceNetworkInterface;
import graduationproject.helpers.DataConverter;
import graduationproject.snmpd.helpers.DeviceQueryHelper.MemoryType;
import java.util.Calendar;
import java.util.List;

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
        startTime.add(Calendar.DAY_OF_YEAR, -1);

        Calendar endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.SECOND, 59);
        endTime.set(Calendar.MINUTE, 59);
        endTime.set(Calendar.HOUR_OF_DAY, 23);
//        System.out.println(startTime.get(Calendar.DAY_OF_MONTH));
//        System.out.println(endTime.get(Calendar.DAY_OF_MONTH));

        List<DeviceCPUState> oldCPUStates = DataManager.getInstance().getDeviceCpuManager().getDeviceCPUStates(startTime, endTime);
        if (oldCPUStates != null && oldCPUStates.size() != 0) {

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

                            DeviceAvgCpuUsage cpuUsage = new DeviceAvgCpuUsage(cpuId, loadSum / cpuLoads.size(), startTime, device);
                            DataManager.getInstance().getCpuUsageManager().saveAvgCPUUsage(cpuUsage);
                        }
                    }
                }
            }
//        System.out.println("COMPRESSED SIZE " + newDeviceCPUStates.size());
//        System.out.println(startTime.get(Calendar.DAY_OF_YEAR));
//        System.out.println(endTime.get(Calendar.DAY_OF_YEAR));

            DataManager.getInstance().getDeviceCpuManager().renewDeviceCPUStateTable();
        }
    }

    private void compressMemoryData(List<Device> devices) {
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.SECOND, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.add(Calendar.DAY_OF_YEAR, -1);

        Calendar endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.SECOND, 59);
        endTime.set(Calendar.MINUTE, 59);
        endTime.set(Calendar.HOUR_OF_DAY, 23);

        String[] memoryTypes = {MemoryType.RAM.getDisplayType(), MemoryType.VIRTUAL.getDisplayType(), MemoryType.OTHER.getDisplayType()};

        List<DeviceMemoryState> oldStates = DataManager.getInstance().getDeviceMemoryManager()
                .getDeviceMemoryStates(startTime, endTime, null);
        if (oldStates != null && oldStates.size() != 0) {

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
                                memorySum += memoryState.getUsagePercentage();
                            }
                            DeviceAvgMemoryUsage memoryUsage = new DeviceAvgMemoryUsage(
                                    memoryType, memorySum / memoryStates.size(), startTime, device);
                            DataManager.getInstance().getMemoryUsageManager().saveAvgMemoryUsage(memoryUsage);
                        }

                    }
                }
            }

            DataManager.getInstance().getDeviceMemoryManager().renewDeviceMemoryStateTable();
        }
    }

    public void compressInterfaceDynamicData(List<Device> devices) {
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.SECOND, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.add(Calendar.DAY_OF_YEAR, -1);

        Calendar endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.SECOND, 59);
        endTime.set(Calendar.MINUTE, 59);
        endTime.set(Calendar.HOUR_OF_DAY, 23);

        List<DeviceInterfaceDynamicData> oldStates = DataManager.getInstance()
                .getInterfaceDynamicDataManager().getDeviceDynamicData(null, startTime, endTime);
        if (oldStates != null && oldStates.size() != 0) {

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

                            for (int j = 1; j < tempSize; j++) {
                                current = interfaceDataList.get(j);
                                previous = interfaceDataList.get(j - 1);
                                upperPart = Math.max(current.getInboundBytes() - previous.getInboundBytes(),
                                        current.getOutboundBytes() - previous.getOutboundBytes()) * 8 * 100;
                                lowerPart = (float) ((dataConverter.convertCalendarTimeToSecond(current.getUpdatedTime())
                                        - dataConverter.convertCalendarTimeToSecond(previous.getUpdatedTime())) * current.getBandwidth());
                                if (lowerPart != 0) {
                                    interfaceSum += (upperPart / lowerPart);
//                                    System.out.println("INTERFACE SUM:" + interfaceSum);
                                }
                            }

                            if (tempSize - 1 > 0) {
                                DeviceAvgBandwidthUsage bandwidthUsage = new DeviceAvgBandwidthUsage(interfaceSum / (tempSize - 1), startTime, networkInterface);
                                DataManager.getInstance().getBandwidthManager().saveAvgBandwidthUsage(bandwidthUsage);
                            }
                        }
                    }
                }
            }

            //add data today
            DataManager.getInstance().getInterfaceDynamicDataManager().renewDeviceInterfaceDynamicDataTable();
        }
    }

}
