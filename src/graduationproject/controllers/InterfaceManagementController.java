/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.controllers;

import graduationproject.data.DataManager;
import graduationproject.data.models.Device;
import graduationproject.data.models.DeviceInterfaceDynamicData;
import graduationproject.data.models.DeviceNetworkInterface;
import graduationproject.data.models.Setting;
import graduationproject.data.models.User;
import graduationproject.gui.ApplicationWindow;
import graduationproject.snmpd.SnmpManager;
import graduationproject.snmpd.helpers.InterfaceQueryHelper;
import graduationproject.snmpd.helpers.InterfaceQueryHelper.InterfaceRawData;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import org.soulwing.snmp.SnmpContext;

/**
 *
 * @author cloud
 */
public class InterfaceManagementController {

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
        NEXT_NODE_NAME(10),
        NEXT_NODE_LABEL(11),
        NEXT_NODE_IP_ADDRESS(12),
        NEXT_NODE_MAC_ADDRESS(13),
        UPDATED_TIME(14);

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

    public void processGettingInterfacesOfDevice(int deviceId) {
        System.out.println("START CHECKING INTERFACE STATES");
        TimerTask queryTask = new TimerTask() {
            @Override
            public void run() {
                Device device = DataManager.getInstance().getDeviceManager().getDevice(deviceId);
                if (device == null) {
                    return;
                }
                SnmpContext snmpContext = SnmpManager.getInstance().createContext(
                        device.getSnmpVersion(), device.getContactInterface().getIpAddress(),
                        device.getContactInterface().getCommunity());
                InterfaceQueryHelper helper = new InterfaceQueryHelper();
                helper.startQueryAllInterfaces(deviceId, snmpContext);
            }
        };

        User currentUser = DataManager.getInstance().getUserManager().getUser(DataManager.getInstance().getActiveAccountId());
        Setting setting = currentUser.getSetting();
        SnmpManager.getInstance().getQueryTimerManager().startInterfaceTimer(queryTask, 0, setting.getNormalizedTime(setting.getInterfaceCheckingPeriod()));
    }

    public void processCollectedData(int deviceId, List<InterfaceRawData> interfaceList) {
        if (interfaceList.isEmpty()) {
            return;
        }

        //for database
        Device device = DataManager.getInstance().getDeviceManager().getDevice(deviceId);
        if (device == null) {
            return;
        }
        //public DeviceNetworkInterface(String name, String macAddress, String type) {

        System.out.println("START PROCESSING INTERFACE DATA");
        
        int tempSize = device.getNetworkInterfaces().size();
        for (int i = 0; i < tempSize; i++) {
            device.getNetworkInterfaces().get(i).setName(interfaceList.get(i).getName());
            device.getNetworkInterfaces().get(i).setMacAddress(interfaceList.get(i).getMacAddress());
            device.getNetworkInterfaces().get(i).setType(interfaceList.get(i).getType());
        }

        int tempSize2 = interfaceList.size();
        if (tempSize < tempSize2) {
            for (int i = tempSize; i < tempSize2; i++) {
                device.getNetworkInterfaces().add(new DeviceNetworkInterface(interfaceList.get(i).getName(),
                        interfaceList.get(i).getMacAddress(),
                        interfaceList.get(i).getType()));
            }
            tempSize = tempSize2;
        }
        DataManager.getInstance().getDeviceManager().updateDevice(device);
        
        Date importedTime = new Date();
//        System.out.println("INSERTING INTERFACE DYNAMIC DATA");
        for (int i = 0; i < tempSize; i++) {
            DeviceInterfaceDynamicData dynamicData = new DeviceInterfaceDynamicData(
                    interfaceList.get(i).getDynamicData(), importedTime, null);
            DataManager.getInstance().getInterfaceDynamicDataManager().insertDynamicData(
                    device.getNetworkInterfaces().get(i).getId(),
                    dynamicData);
        }
        
        //for display
        String[] names = new String[tempSize];
        int[] interfaceIds = new int[tempSize];
        InterfaceStates[] interfaceStates = new InterfaceStates[tempSize];

        for (int i = 0; i < tempSize; i++) {
            names[i] = interfaceList.get(i).getName();
            interfaceIds[i] = interfaceList.get(i).getIndex();
//            interfaceIds[i] = i;
            interfaceStates[i] = (interfaceList.get(i).getOperStatus() != 1) ? InterfaceStates.DOWN : InterfaceStates.UP;
        }

        ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices().updateLabelInterfaces(deviceId, interfaceIds, names, interfaceStates);
    }

}
