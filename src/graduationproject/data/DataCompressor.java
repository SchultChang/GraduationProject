/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data;

import graduationproject.data.models.Device;
import graduationproject.data.models.DeviceCPUState;
import graduationproject.data.models.DeviceMemoryState;
import graduationproject.snmpd.helpers.DeviceQueryHelper;
import graduationproject.snmpd.helpers.DeviceQueryHelper.MemoryType;
import java.util.ArrayList;
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
    }

    private void compressCPUData(List<Device> devices) {
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.SECOND, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.add(Calendar.DAY_OF_YEAR, -DAY_LIMIT - 1);

        Calendar endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.SECOND, 59);
        endTime.set(Calendar.MINUTE, 59);
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        endTime.add(Calendar.DAY_OF_YEAR, DAY_LIMIT - 2);

        List<DeviceCPUState> newDeviceCPUStates = DataManager.getInstance().getDeviceCpuManager().getDeviceCPUStates(startTime, endTime);
        if (newDeviceCPUStates == null) {
            newDeviceCPUStates = new ArrayList<DeviceCPUState>();
        }

        startTime.add(Calendar.DAY_OF_YEAR, DAY_LIMIT - 1);
        endTime.add(Calendar.DAY_OF_YEAR, 1);

        //compress data from yesterday
        for (Device device : devices) {
            List<Integer> cpuIds = DataManager.getInstance().getDeviceCpuManager().getDeviceCpuDeviceIds(device);

            for (int cpuId : cpuIds) {
                DeviceCPUState modelState = DataManager.getInstance().getDeviceCpuManager().getDeviceCPUState(cpuId, device);

                for (int i = 0; i < 24; i++) {
                    startTime.set(Calendar.HOUR_OF_DAY, i);
                    endTime.set(Calendar.HOUR_OF_DAY, i);

                    List<Float> cpuLoads = DataManager.getInstance().getDeviceCpuManager().getDeviceCPULoads(device, startTime, endTime, "cpuLoad", cpuId);
                    DeviceCPUState deviceCPUState = null;
                    if (cpuLoads != null && cpuLoads.isEmpty()) {
                        float loadSum = 0;
                        for (float cpuLoad : cpuLoads) {
                            loadSum += cpuLoad;
                        }

                        deviceCPUState = new DeviceCPUState(cpuId, modelState.getFirmwareId(), modelState.getDescription(),
                                loadSum / cpuLoads.size(), startTime, true, device);
                    } else {
                        deviceCPUState = new DeviceCPUState(cpuId, modelState.getFirmwareId(), modelState.getDescription(),
                                0.0f, startTime, true, device);
                    }

                    newDeviceCPUStates.add(deviceCPUState);
                }
            }
        }

        //add data today
        startTime.add(Calendar.DAY_OF_YEAR, 1);
        endTime.add(Calendar.DAY_OF_YEAR, 1);
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        newDeviceCPUStates.addAll(DataManager.getInstance().getDeviceCpuManager().getDeviceCPUStates(startTime, endTime));

        DataManager.getInstance().getDeviceCpuManager().renewDeviceCPUStateTable(newDeviceCPUStates);
    }

    private void compressMemoryData(List<Device> devices) {
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.SECOND, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.add(Calendar.DAY_OF_YEAR, -DAY_LIMIT - 1);

        Calendar endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.SECOND, 59);
        endTime.set(Calendar.MINUTE, 59);
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        endTime.add(Calendar.DAY_OF_YEAR, DAY_LIMIT - 2);

        String[] memoryTypes = {MemoryType.RAM.getDisplayType(), MemoryType.VIRTUAL.getDisplayType(), MemoryType.OTHER.getDisplayType()};
               
        List<DeviceMemoryState> newDeviceMemoryStates = new ArrayList<DeviceMemoryState>();
        for (String memoryType : memoryTypes) {
            List<DeviceMemoryState> states = DataManager.getInstance().getDeviceMemoryManager().getDeviceMemoryStates(startTime, endTime, memoryType);
            if (states != null) {
                newDeviceMemoryStates.addAll(states);
            }
        }
        
        startTime.add(Calendar.DAY_OF_YEAR, DAY_LIMIT - 1);
        endTime.add(Calendar.DAY_OF_YEAR, 1);

        //compress data from yesterday
        for (Device device : devices) {
            List<Integer> cpuIds = DataManager.getInstance().getDeviceCpuManager().getDeviceCpuDeviceIds(device);

            for (int cpuId : cpuIds) {
                DeviceCPUState modelState = DataManager.getInstance().getDeviceCpuManager().getDeviceCPUState(cpuId, device);

                for (int i = 0; i < 24; i++) {
                    startTime.set(Calendar.HOUR_OF_DAY, i);
                    endTime.set(Calendar.HOUR_OF_DAY, i);

                    List<Float> cpuLoads = DataManager.getInstance().getDeviceCpuManager().getDeviceCPULoads(device, startTime, endTime, "cpuLoad", cpuId);
                    DeviceCPUState deviceCPUState = null;
                    if (cpuLoads != null && cpuLoads.isEmpty()) {
                        float loadSum = 0;
                        for (float cpuLoad : cpuLoads) {
                            loadSum += cpuLoad;
                        }

                        deviceCPUState = new DeviceCPUState(cpuId, modelState.getFirmwareId(), modelState.getDescription(),
                                loadSum / cpuLoads.size(), startTime, true, device);
                    } else {
                        deviceCPUState = new DeviceCPUState(cpuId, modelState.getFirmwareId(), modelState.getDescription(),
                                0.0f, startTime, true, device);
                    }

                    newDeviceCPUStates.add(deviceCPUState);
                }
            }
        }

        //add data today
        startTime.add(Calendar.DAY_OF_YEAR, 1);
        endTime.add(Calendar.DAY_OF_YEAR, 1);
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        newDeviceCPUStates.addAll(DataManager.getInstance().getDeviceCpuManager().getDeviceCPUStates(startTime, endTime));

        DataManager.getInstance().getDeviceCpuManager().renewDeviceCPUStateTable(newDeviceCPUStates);
    }
    
}
