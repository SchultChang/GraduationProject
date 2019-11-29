/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.controllers;

import graduationproject.data.DataManager;
import graduationproject.data.models.Device;
import graduationproject.data.models.Setting;
import graduationproject.data.models.User;
import graduationproject.gui.ApplicationWindow;
import graduationproject.snmpd.SnmpManager;
import graduationproject.snmpd.helpers.InterfaceQueryHelper;
import graduationproject.snmpd.helpers.InterfaceQueryHelper.InterfaceRawData;
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
        IN_ERR_PACK_NUMBER(8),
        OUT_ERR_PACK_NUMBER(9),
        NEXT_NODE_NAME(10),
        NEXT_NODE_LABEL(11),
        NEXT_NODE_IP_ADDRESS(12),
        NEXT_NODE_MAC_ADDRESS(13);

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
        SnmpManager.getInstance().getQueryTimerManager().startDeviceTimer(queryTask, 0, setting.getNormalizedTime(setting.getInterfaceCheckingPeriod()));
    }

    public void processCollectedData(int deviceId, List<InterfaceRawData> interfaceList) {
        //for database

        //for display
        int tempSize = interfaceList.size();
        String[] names = new String[tempSize];
        int[] interfaceIds = new int[tempSize];
        InterfaceStates[] interfaceStates = new InterfaceStates[tempSize];
        
        for (int i = 0; i < tempSize; i++) {
            names[i] = interfaceList.get(i).getName();
            interfaceIds[i] = interfaceList.get(i).getIndex();
            interfaceStates[i] = (interfaceList.get(i).getOperStatus() != 1) ? InterfaceStates.DOWN : InterfaceStates.UP;
        }
        
        ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices().addLabelInterfaces(deviceId, interfaceIds, names, interfaceStates);
    }

}
