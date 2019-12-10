/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.controllers;

import graduationproject.data.DataConverter;
import graduationproject.data.DataManager;
import graduationproject.data.models.Device;
import graduationproject.data.models.DeviceCPUState;
import graduationproject.data.models.DeviceMemoryState;
import graduationproject.data.models.Setting;
import graduationproject.gui.ApplicationWindow;
import graduationproject.snmpd.SnmpManager;
import graduationproject.snmpd.helpers.DeviceQueryHelper;
import graduationproject.snmpd.helpers.DeviceQueryHelper.MemoryType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;

/**
 *
 * @author cloud
 */
public class DeviceResourceManagementController {
    private String resultMessage;

    public String getResultMessage() {
        return resultMessage;
    }
    
    public boolean processGettingDeviceResource(int deviceId, DeviceManagementController.DeviceStates deviceState) {
        if (deviceState == DeviceManagementController.DeviceStates.ACTIVE) {
            this.getLiveDeviceResource(deviceId);
            return true;
        }

        return false;
    }

    private void getLiveDeviceResource(int deviceId) {
        System.out.println("START CHECKING DEVICE RESOURCES");

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Device device = DataManager.getInstance().getDeviceManager().getDevice(deviceId);
                if (device == null) {
                    return;
                }

                DeviceQueryHelper deviceQueryHelper = new DeviceQueryHelper();
                deviceQueryHelper.startQueryDeviceResource(
                        deviceId,
                        SnmpManager.getInstance().createContext(
                                SnmpManager.SnmpVersion.VERSION_2_COMMUNITY.getValue(),
                                device.getContactInterface().getIpAddress(),
                                device.getContactInterface().getCommunity()));

            }
        };

        Setting setting = DataManager.getInstance().getUserManager().getUser(DataManager.getInstance().getActiveAccountId()).getSetting();
        if (setting != null) {
            SnmpManager.getInstance().getQueryTimerManager().startDeviceResourceTimer(
                    timerTask, 0, setting.getNormalizedTime(setting.getDeviceCheckingPeriod()));
        }
    }

    public void processCollectedResourceData(int deviceId, List<DeviceQueryHelper.DeviceCpuData> cpuDataList, List<DeviceQueryHelper.DeviceMemoryData> memoryDataList) {
        Device device = DataManager.getInstance().getDeviceManager().getDevice(deviceId);
        if (device == null) {
            return;
        }

        Calendar updatedTime = Calendar.getInstance();

        List<Object> cpuDataToView = new ArrayList<Object>();
        for (DeviceQueryHelper.DeviceCpuData cpuData : cpuDataList) {
            cpuDataToView.add(new Object[]{cpuData.getFirmwareId(), cpuData.getDescription(), cpuData.getLoad()});
            DataManager.getInstance().getDeviceCpuManager().saveDeviceCPUState(
                    new DeviceCPUState(cpuData.getFirmwareId(), cpuData.getDescription(), cpuData.getLoad(), updatedTime, device));
        }

        List<Object> memoryDataToView = new ArrayList<Object>();
        for (DeviceQueryHelper.DeviceMemoryData memoryData : memoryDataList) {
            memoryDataToView.add(new Object[]{memoryData.getDescription(), memoryData.getTotalSize(), memoryData.getUsedSize()});
            DataManager.getInstance().getDeviceMemoryManager().saveDeviceMemoryState(
                    new DeviceMemoryState(memoryData.getType(), memoryData.getDescription(), memoryData.getTotalSize(), memoryData.getUsedSize(), updatedTime, device));
        }

        ApplicationWindow.getInstance()
                .getPanelMain().getPanelImportedDevices().getPanelDeviceResources()
                .updateView(deviceId, cpuDataToView, memoryDataToView, new DataConverter().convertCalendarToString(updatedTime));
    }

}
