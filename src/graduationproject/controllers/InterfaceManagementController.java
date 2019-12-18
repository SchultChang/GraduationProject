/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.controllers;

import graduationproject.helpers.DataConverter;
import graduationproject.data.DataManager;
import graduationproject.data.models.Device;
import graduationproject.data.models.DeviceInterfaceDynamicData;
import graduationproject.data.models.DeviceNetworkInterface;
import graduationproject.data.models.Setting;
import graduationproject.data.models.User;
import graduationproject.gui.ApplicationWindow;
import graduationproject.gui.PanelInterfaceInfo;
import graduationproject.data.ActiveDeviceDataCollector;
import graduationproject.helpers.AddressParser;
import graduationproject.helpers.TopoDrawer;
import graduationproject.snmpd.SnmpManager;
import graduationproject.snmpd.helpers.InterfaceQueryHelper;
import graduationproject.snmpd.helpers.InterfaceQueryHelper.InterfaceRawData;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import org.soulwing.snmp.SnmpContext;

/**
 *
 * @author cloud
 */
public class InterfaceManagementController {

    private String resultMessage;

    public enum DataOrders {
        IP_ADDRESS(0),
        NETMASK(1),
        MTU(2),
        BANDWIDTH(3),
        IN_PACK_NUMBER(4),
        OUT_PACK_NUMBER(5),
        IN_BYTES(6),
        OUT_BYTES(7),
        IN_DISCARD_PACK_NUMBER(8),
        OUT_DISCARD_PACK_NUMBER(9),
        //        NEXT_NODE_NAME(10),
        //        NEXT_NODE_LABEL(11),
        //        NEXT_NODE_IP_ADDRESS(12),
        //        NEXT_NODE_MAC_ADDRESS(13),
        UPDATED_TIME(10),
        NAME(11),
        MAC_ADDRESS(12),
        TYPE(13),
        UPDATE_PERIOD(14);

        private int value;

        private DataOrders(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public enum InterfaceStates {
        UP,
        DOWN
    }

    public String getResultMessage() {
        return resultMessage;
    }

//    public boolean processGettingSavedInterfacesOfDevice(int deviceId, boolean isActive) {
//        if (!isActive) {
//            this.getLiveInterfaces(deviceId);
//            return true;
//        } else {
//            return this.getSavedInterfaces(deviceId);
//        }
//        return false;
//    }
    public void processGettingInterfacesOfActiveDevices() {
        System.out.println("START CHECKING INTERFACE STATES");
        TimerTask queryTask = new TimerTask() {
            @Override
            public void run() {
                TopoDrawer.getInstance().checkRedrawingTopo();
                System.out.println("START CHECKING INTERFACE STATES");
                int[] deviceIds = ActiveDeviceDataCollector.getInstance().getImportedDeviceIds();
                InterfaceQueryHelper helper = new InterfaceQueryHelper();
                for (int deviceId : deviceIds) {
                    Device device = DataManager.getInstance().getDeviceManager().getDevice(deviceId);
                    if (device == null) {
                        return;
                    }
                    SnmpContext snmpContext = SnmpManager.getInstance().createContext(
                            device.getSnmpVersion(), device.getContactInterface().getIpAddress(),
                            device.getContactInterface().getCommunity());
                    helper.startQueryAllInterfaces(deviceId, snmpContext);
                }
            }
        };

        User currentUser = DataManager.getInstance().getUserManager().getUser(DataManager.getInstance().getActiveAccountId());
        Setting setting = currentUser.getSetting();
        SnmpManager.getInstance().getQueryTimerManager().startInterfaceTimer(queryTask,
                setting.getNormalizedTime(setting.getDeviceCheckingPeriod()), setting.getNormalizedTime(setting.getInterfaceCheckingPeriod()));
    }

    public boolean processGettingSavedInterfacesOfDevice(int deviceId) {
        Device device = DataManager.getInstance().getDeviceManager().getDevice(deviceId);
        if (device == null) {
            this.resultMessage = new ResultMessageGenerator().GETTING_FAILED_OTHER;
            return false;
        }

        List<DeviceNetworkInterface> interfaceList = device.getNetworkInterfaces();
        if (interfaceList == null || interfaceList.isEmpty()) {
            this.resultMessage = new ResultMessageGenerator().GETTING_FAILED_OTHER;
            return false;
        }

        int tempSize = interfaceList.size();
        String[] names = new String[tempSize];
        int[] interfaceIds = new int[tempSize];
        InterfaceStates[] interfaceStates = new InterfaceStates[tempSize];

        for (int i = 0; i < tempSize; i++) {
            names[i] = interfaceList.get(i).getName();
//            interfaceIds[i] = interfaceList.get(i).getIndex();
            interfaceIds[i] = i;
            interfaceStates[i] = InterfaceStates.DOWN;
        }

        ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices().updateLabelInterfaces(deviceId, interfaceIds, names, interfaceStates);
        return true;
    }

    public void processCollectedData(int deviceId, List<InterfaceRawData> rawDataList) {
        if (rawDataList.isEmpty()) {
            return;
        }

        //for database
        Device device = DataManager.getInstance().getDeviceManager().getDevice(deviceId);
        if (device == null) {
            return;
        }

        int tempSize = device.getNetworkInterfaces().size();
        for (int i = 0; i < tempSize; i++) {
            device.getNetworkInterfaces().get(i).setName(rawDataList.get(i).getName());
            device.getNetworkInterfaces().get(i).setMacAddress(rawDataList.get(i).getMacAddress());
            device.getNetworkInterfaces().get(i).setType(rawDataList.get(i).getType());
        }

        int tempSize2 = rawDataList.size();
        if (tempSize < tempSize2) {
            for (int i = tempSize; i < tempSize2; i++) {
                device.getNetworkInterfaces().add(new DeviceNetworkInterface(rawDataList.get(i).getName(),
                        rawDataList.get(i).getMacAddress(),
                        rawDataList.get(i).getType()));
            }
            tempSize = tempSize2;
        }
        DataManager.getInstance().getDeviceManager().updateDevice(device);

        int displayedInterfaceId = ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices()
                .getPanelInterfaceInfo().getInterfaceListId();
        DeviceInterfaceDynamicData needToViewDynamicData = null;

        Calendar updatedTime = Calendar.getInstance();
        AddressParser addressParser = new AddressParser();
        try {
            for (int i = 0; i < tempSize; i++) {
                InterfaceRawData temp = rawDataList.get(i);
                List<Object> rawData = temp.getDynamicData();

                ActiveDeviceDataCollector.getInstance().updateInterfaceData(deviceId,
                        temp.getName(),
                        temp.getIpAddress(),
                        addressParser.getNetworkIp(temp.getIpAddress(), temp.getNetmask()),
                        temp.getMacAddress(),
                        ActiveDeviceDataCollector.getInstance().findNextNodeId(temp.getNextNodeIPs(), temp.getNextNodeMacs()),
                        temp.getNextNodeIPs(),
                        temp.getNextNodeMacs());

                DeviceInterfaceDynamicData dynamicData = new DeviceInterfaceDynamicData(
                        rawData, updatedTime, device.getNetworkInterfaces().get(i));
                DataManager.getInstance().getInterfaceDynamicDataManager().insertDynamicData(dynamicData);

                if (i == displayedInterfaceId) {
                    needToViewDynamicData = dynamicData;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //for display
        String[] names = new String[tempSize];
        int[] interfaceIds = new int[tempSize];
        InterfaceStates[] interfaceStates = new InterfaceStates[tempSize];

        for (int i = 0; i < tempSize; i++) {
            names[i] = rawDataList.get(i).getName();
//            interfaceIds[i] = rawDataList.get(i).getIndex();
            interfaceIds[i] = i;
            interfaceStates[i] = (rawDataList.get(i).getOperStatus() != 1) ? InterfaceStates.DOWN : InterfaceStates.UP;
        }

        ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices().updateLabelInterfaces(deviceId, interfaceIds, names, interfaceStates);

        //to display on interface info
        PanelInterfaceInfo infoPanel = ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices().getPanelInterfaceInfo();
        if (ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices().getPanelInterfaceInfo().isVisible()) {
            if (infoPanel.getDeviceId() == deviceId && displayedInterfaceId < tempSize) {
                infoPanel.updateView(deviceId, this.convertDataForView(
                        device.getNetworkInterfaces().get(displayedInterfaceId),
                        needToViewDynamicData));
            }
        }

        ActiveDeviceDataCollector.getInstance().mergeNewInterfaceData(deviceId);

    }

    public List<Object> processGettingInterfaceFromDatabase(int deviceId, int interfaceListId) {
        Device device = DataManager.getInstance().getDeviceManager().getDevice(deviceId);
        if (device == null) {
            this.resultMessage = new ResultMessageGenerator().GETTING_FAILED_OTHER;
            return null;
        }

        DeviceNetworkInterface deviceInterface = device.getNetworkInterfaces().get(interfaceListId);
        if (deviceInterface == null) {
            this.resultMessage = new ResultMessageGenerator().GETTING_FAILED_OTHER;
            return null;
        }

        DeviceInterfaceDynamicData dynamicData = DataManager.getInstance().getInterfaceDynamicDataManager()
                .getDynamicData(deviceInterface);
        if (dynamicData == null) {
            this.resultMessage = new ResultMessageGenerator().GETTING_FAILED_OTHER;
            return null;
        }

//        List<Object> result = this.convertDataForView(deviceInterface, dynamicData);
        Setting setting = DataManager.getInstance().getUserManager().getUser(DataManager.getInstance().getActiveAccountId()).getSetting();

        return this.convertDataForView(deviceInterface, dynamicData, setting);
    }

    public boolean processChangingInterfaceCheckingPeriod(int newPeriod) {
        Setting setting;
        try {
            setting = DataManager.getInstance().getUserManager().getUser(
                    DataManager.getInstance().getActiveAccountId()).getSetting();
        } catch (Exception e) {
            this.resultMessage = new ResultMessageGenerator().CHANGING_FAILED_GETTING;
            return false;
        }

        if (setting == null) {
            this.resultMessage = new ResultMessageGenerator().CHANGING_FAILED_GETTING;
            return false;
        }

        setting.setInterfaceCheckingPeriod(newPeriod);
        if (!DataManager.getInstance().getSettingManager().updateSetting(setting)) {
            this.resultMessage = new ResultMessageGenerator().CHANGING_FAILED_SETTING;
            return false;
        }

        this.processGettingInterfacesOfActiveDevices();
        return true;
    }

    private List<Object> convertDataForView(DeviceNetworkInterface networkInterface, DeviceInterfaceDynamicData data, Setting setting) {
        List<Object> temp = this.convertDataForView(networkInterface, data);
        if (temp != null) {
            temp.add(DataOrders.UPDATE_PERIOD.getValue(), setting.getInterfaceCheckingPeriod());
        }
        return temp;
    }

    private List<Object> convertDataForView(DeviceNetworkInterface networkInterface, DeviceInterfaceDynamicData data) {
        List<Object> result = new ArrayList<Object>();

        result.add(DataOrders.IP_ADDRESS.getValue(), data.getIpAddress());
        result.add(DataOrders.NETMASK.getValue(), data.getNetmask());
        result.add(DataOrders.MTU.getValue(), data.getMtu());
        result.add(DataOrders.BANDWIDTH.getValue(), data.getBandwidth());
        result.add(DataOrders.IN_PACK_NUMBER.getValue(), data.getInboundPacketNumber());
        result.add(DataOrders.OUT_PACK_NUMBER.getValue(), data.getOutboundPacketNumber());
        result.add(DataOrders.IN_BYTES.getValue(), data.getInboundBytes());
        result.add(DataOrders.OUT_BYTES.getValue(), data.getOutboundBytes());
        result.add(DataOrders.IN_DISCARD_PACK_NUMBER.getValue(), data.getInboundDiscardPacketNumber());
        result.add(DataOrders.OUT_DISCARD_PACK_NUMBER.getValue(), data.getOutboundDiscardPacketNumber());
//        result.add(DataOrders.NEXT_NODE_NAME.getValue(), data.getNextNodeName());
//        result.add(DataOrders.NEXT_NODE_LABEL.getValue(), data.getNextNodeLabel());
//        result.add(DataOrders.NEXT_NODE_IP_ADDRESS.getValue(), data.getNextNodeIPAddress());
//        result.add(DataOrders.NEXT_NODE_MAC_ADDRESS.getValue(), data.getNextNodeMacAddress());
        result.add(DataOrders.UPDATED_TIME.getValue(),
                new DataConverter().convertCalendarToString(data.getUpdatedTime()));

        result.add(DataOrders.NAME.getValue(), networkInterface.getName());
        result.add(DataOrders.MAC_ADDRESS.getValue(), networkInterface.getMacAddress());
        result.add(DataOrders.TYPE.getValue(), networkInterface.getType());
        return result;
    }

    public List<String> processGettingInterfaceNames(int deviceId) {
        Device device = DataManager.getInstance().getDeviceManager().getDevice(deviceId);
        if (device == null) {
            return null;
        }

        List<DeviceNetworkInterface> networkInterfaces = device.getNetworkInterfaces();
        List<String> result = new ArrayList<String>();

        for (DeviceNetworkInterface networkInterface : networkInterfaces) {
            result.add(networkInterface.getName());
        }

        return result;
    }

    public class ResultMessageGenerator {

        public String GETTING_FAILED_OTHER = "Some errors happened when getting interface data. Please try again later.";

        public String CHANGING_FAILED_GETTING = "Some errors happened when getting setting from database.";
        public String CHANGING_FAILED_SETTING = "Some errors happened when saving your new configuration.";
    }
}
