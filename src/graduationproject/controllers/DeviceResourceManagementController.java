/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.controllers;

import graduationproject.helpers.DataConverter;
import graduationproject.data.DataManager;
import graduationproject.data.models.Device;
import graduationproject.data.models.DeviceCPUState;
import graduationproject.data.models.DeviceMemoryState;
import graduationproject.data.models.Setting;
import graduationproject.data.models.User;
import graduationproject.gui.ApplicationWindow;
import graduationproject.data.ActiveDeviceDataCollector;
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
public class DeviceResourceManagementController extends ManagementController {

    public int processGettingResourceCheckingPeriod() {
        User user = DataManager.getInstance().getUserManager().getUser(DataManager.getInstance().getActiveAccountId());
        if (user == null) {
            return 0;
        }

        Setting setting = user.getSetting();
        if (setting == null) {
            return 0;
        }

        return setting.getResourceCheckingPeriod();
    }

    public void processGettingResourcesOfActiveDevices() {
        System.out.println("START CHECKING DEVICE RESOURCES");

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                int[] deviceIds = ActiveDeviceDataCollector.getInstance().getImportedDeviceIds();
                DeviceQueryHelper deviceQueryHelper = new DeviceQueryHelper();

                for (int deviceId : deviceIds) {
                    Device device = DataManager.getInstance().getDeviceManager().getDevice(deviceId);
                    if (device == null) {
                        return;
                    }

                    deviceQueryHelper.startQueryDeviceResource(
                            deviceId,
                            SnmpManager.getInstance().createContext(
                                    SnmpManager.SnmpVersion.VERSION_2_COMMUNITY.getValue(),
                                    device.getContactInterface().getIpAddress(),
                                    device.getContactInterface().getCommunity()));
                }
            }
        };

        Setting setting = DataManager.getInstance().getUserManager().getUser(DataManager.getInstance().getActiveAccountId()).getSetting();
        if (setting != null) {
            SnmpManager.getInstance().getQueryTimerManager().startDeviceResourceTimer(timerTask,
                    setting.getNormalizedTime(setting.getDeviceCheckingPeriod()), setting.getNormalizedTime(setting.getResourceCheckingPeriod()));
        }
    }

    public boolean processChangingResourceCheckingPeriod(int newPeriod) {
        User user = DataManager.getInstance().getUserManager().getUser(DataManager.getInstance().getActiveAccountId());
        if (user == null) {
            this.resultMessage = new ResultMessageGenerator().CHANGING_FAILED_GETTING;
            return false;
        }

        Setting setting = user.getSetting();
        if (setting == null) {
            this.resultMessage = new ResultMessageGenerator().CHANGING_FAILED_GETTING;
            return false;
        }

        setting.setResourceCheckingPeriod(newPeriod);
        if (!DataManager.getInstance().getSettingManager().updateSetting(setting)) {
            this.resultMessage = new ResultMessageGenerator().CHANGING_FAILED_SETTING;
            return false;
        }

        this.processGettingResourcesOfActiveDevices();
        return true;
    }

    public void processCollectedResourceData(int deviceId, List<DeviceQueryHelper.DeviceCpuData> cpuDataList,
            List<DeviceQueryHelper.DeviceMemoryData> memoryDataList) {
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
                    new DeviceMemoryState(memoryData.getType(), memoryData.getDescription(),
                            memoryData.getTotalSize(), memoryData.getUsedSize(), updatedTime, device));
        }

        ApplicationWindow.getInstance()
                .getPanelMain().getPanelImportedDevices().getPanelDeviceResources()
                .updateView(deviceId, cpuDataToView, memoryDataToView, new DataConverter().convertCalendarToString(updatedTime));
    }

    public class ResultMessageGenerator {

        public String GETTING_FAILED_OTHER = "Some errors happened when getting interface data. Please try again later.";

        public String CHANGING_FAILED_GETTING = "Some errors happened when getting setting from database.";
        public String CHANGING_FAILED_SETTING = "Some errors happened when saving your new configuration.";
    }

}
