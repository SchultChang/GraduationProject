/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.snmpd.helpers;

import graduationproject.controllers.DeviceManagementController;
import java.util.ArrayList;
import java.util.List;
import org.soulwing.snmp.SimpleSnmpV2cTarget;
import org.soulwing.snmp.SnmpTarget;
import org.soulwing.snmp.Varbind;
import org.soulwing.snmp.VarbindCollection;

/**
 *
 * @author cloud
 */
public class NotificationParser {

    private final String typeName = "snmpTrapOID";

    public List<Object> parseNotification(SimpleSnmpV2cTarget target, VarbindCollection varbinds) {
        List<Object> result = new ArrayList<Object>();

//        result.add(DataOrders.IP_ADDRESS.getValue(), target.getAddress());
//        result.add(DataOrders.COMMUNITY.getValue(), target.getCommunity());
        String typeOid = varbinds.get(typeName).asString();

        if (typeOid.equals(NotificationType.COLD_START.getOid())) {
            this.parseColdStartNotification(target, varbinds, result);
        } else if (typeOid.equals(NotificationType.WARM_START.getOid())) {
            this.parseWarmStartNotification(target, varbinds, result);
        } else if (typeOid.equals(NotificationType.LINK_DOWN.getOid())) {
            this.parseLinkDownNotification(target, varbinds, result);
        } else if (typeOid.equals(NotificationType.LINK_UP.getOid())) {
            this.parseLinkUpNotification(target, varbinds, result);
        } else if (typeOid.equals((NotificationType.AUTHENTICATION_FAILURE.getOid()))) {
            this.parseAuthenticationFailureNotification(target, varbinds, result);
        } else {
            this.parseEnterpriseNotification(target, varbinds, result);
        }

        return result;
    }

    private void parseColdStartNotification(SnmpTarget target, VarbindCollection varbinds, List<Object> result) {
        DeviceManagementController deviceController = new DeviceManagementController();
        int deviceId = deviceController.processDeviceInfoWithStartNotification(target, true);
        result.add(DataOrders.DEVICE_ID.getValue(), deviceId);
        result.add(DataOrders.DEVICE_ADDRESS.getValue(), target.getAddress());

        result.add(DataOrders.TYPE.getValue(), NotificationType.COLD_START.getName());
        result.add(DataOrders.CONTENT.getValue(), NotificationType.COLD_START.getMessage());

        this.getExtraData(varbinds, result);
    }

    private void parseWarmStartNotification(SnmpTarget target, VarbindCollection varbinds, List<Object> result) {
        DeviceManagementController deviceController = new DeviceManagementController();
        int deviceId = deviceController.processDeviceInfoWithStartNotification(target, true);
        result.add(DataOrders.DEVICE_ID.getValue(), deviceId);
        result.add(DataOrders.DEVICE_ADDRESS.getValue(), target.getAddress());

        result.add(DataOrders.TYPE.getValue(), NotificationType.WARM_START.getName());
        result.add(DataOrders.CONTENT.getValue(), NotificationType.WARM_START.getMessage());

        this.getExtraData(varbinds, result);
    }

    private void parseLinkDownNotification(SnmpTarget target, VarbindCollection varbinds, List<Object> result) {
        DeviceManagementController deviceController = new DeviceManagementController();
        int interfaceId = varbinds.get("ifIndex").asInt();
        int deviceId = deviceController.processDeviceInfoWithLinkNOtification(target, interfaceId, false, true);
        result.add(DataOrders.DEVICE_ID.getValue(), deviceId);
        result.add(DataOrders.DEVICE_ADDRESS.getValue(), target.getAddress());

        result.add(DataOrders.TYPE.getValue(), NotificationType.LINK_DOWN.getName());
        if (deviceId >= 0) {
            String interfaceName = deviceController.getCheckingDeviceInterfaceName(interfaceId);
            result.add(DataOrders.CONTENT.getValue(), "Interface " + interfaceName + " is down.");
        } else {
            result.add(DataOrders.CONTENT.getValue(), "Interface " + interfaceId + " is down.");
        }

        this.getExtraData(varbinds, result);
    }

    private void parseLinkUpNotification(SnmpTarget target, VarbindCollection varbinds, List<Object> result) {
        DeviceManagementController deviceController = new DeviceManagementController();
        int interfaceId = varbinds.get("ifIndex").asInt();
        int deviceId = deviceController.processDeviceInfoWithLinkNOtification(target, interfaceId, true, true);
        result.add(DataOrders.DEVICE_ID.getValue(), deviceId);
        result.add(DataOrders.DEVICE_ADDRESS.getValue(), target.getAddress());

        result.add(DataOrders.TYPE.getValue(), NotificationType.LINK_UP.getName());
        if (deviceId >= 0) {
            String interfaceName = deviceController.getCheckingDeviceInterfaceName(interfaceId);
            result.add(DataOrders.CONTENT.getValue(), "Interface " + interfaceName + " is up.");
        } else {
            result.add(DataOrders.CONTENT.getValue(), "Interface " + interfaceId + " is up.");
        }

        this.getExtraData(varbinds, result);
    }

    private void parseAuthenticationFailureNotification(SnmpTarget target, VarbindCollection varbinds, List<Object> result) {
        DeviceManagementController deviceController = new DeviceManagementController();
        int deviceId = deviceController.processDeviceInfoWithAuthenticationNotification(target, true);
        result.add(DataOrders.DEVICE_ID.getValue(), deviceId);
        result.add(DataOrders.DEVICE_ADDRESS.getValue(), target.getAddress());

        result.add(DataOrders.TYPE.getValue(), NotificationType.AUTHENTICATION_FAILURE.getName());
        result.add(DataOrders.CONTENT.getValue(), NotificationType.AUTHENTICATION_FAILURE.getMessage());

        this.getExtraData(varbinds, result);
    }

    private void parseEgpNeighborLossNotification(SnmpTarget target, VarbindCollection varbinds, List<Object> result) {
        DeviceManagementController deviceController = new DeviceManagementController();
        int deviceId = deviceController.processDeviceInfoWithEgpNotification(target, true);
        result.add(DataOrders.DEVICE_ID.getValue(), deviceId);
        result.add(DataOrders.DEVICE_ADDRESS.getValue(), target.getAddress());

        result.add(DataOrders.TYPE.getValue(), NotificationType.EGP_LOSS.getName());
        result.add(DataOrders.CONTENT.getValue(), NotificationType.EGP_LOSS.getMessage());

        this.getExtraData(varbinds, result);
    }

    private void parseEnterpriseNotification(SnmpTarget target, VarbindCollection varbinds, List<Object> result) {
        DeviceManagementController deviceController = new DeviceManagementController();
        int deviceId = deviceController.processDeviceInfoWithEnterpriseNotification(target, true);
        result.add(DataOrders.DEVICE_ID.getValue(), deviceId);
        result.add(DataOrders.DEVICE_ADDRESS.getValue(), target.getAddress());

        result.add(DataOrders.TYPE.getValue(), NotificationType.ENTERPRISE.getName());
        result.add(DataOrders.CONTENT.getValue(), NotificationType.ENTERPRISE.getMessage());

        this.getExtraData(varbinds, result);
    }

    private void getExtraData(VarbindCollection varbinds, List<Object> result) {
        int count = 0;
        String[] pair;
        
        for (Varbind varbind : varbinds) {
            if (!varbind.getName().contains(typeName)) {
                pair = new String[DataOrders.EXTRA_SIZE.getValue()];
                pair[DataOrders.EXTRA_NAME.getValue()] = varbind.getName();
                pair[DataOrders.EXTRA_VALUE.getValue()] = varbind.asString();
                result.add(DataOrders.EXTRA.getValue() + count, pair);
                count++;
            }
        }
    }

    public enum NotificationType {
        COLD_START("Cold Start", "1.3.6.1.6.3.1.1.5.1", "Device has restarted"),
        WARM_START("Warm Start", "1.3.6.1.6.3.1.1.5.2", "Device has restarted without changing its configuration"),
        LINK_DOWN("Link Down", "1.3.6.1.6.3.1.1.5.3"),
        LINK_UP("Link Up", "1.3.6.1.6.3.1.1.5.4"),
        AUTHENTICATION_FAILURE("Authentication Failure", "1.3.6.1.6.3.1.1.5.5", "Someone is trying to access the device with wrong community."),
        EGP_LOSS("EGP Neighbor Loss", "1.3.6.1.6.3.1.1.5.6", "Device has lost one of its EGP neighbor"),
        ENTERPRISE("Enterprise", "", "A specific event has happened on device");

        private String name;
        private String oid;
        private String message;

        private NotificationType(String name, String oid) {
            this.name = name;
            this.oid = oid;
        }

        private NotificationType(String name, String oid, String message) {
            this.name = name;
            this.oid = oid;
            this.message = message;
        }

        public String getName() {
            return this.name;
        }

        public String getOid() {
            return this.oid;
        }

        public String getMessage() {
            return this.message;
        }
    }

    public enum DataOrders {
//        IP_ADDRESS(0),
//        COMMUNITY(1),
        DEVICE_ID(0),
        DEVICE_ADDRESS(1),
        TYPE(2),
        CONTENT(3),
        EXTRA(4),
        
        EXTRA_SIZE(2),
        EXTRA_NAME(0),
        EXTRA_VALUE(1);
        

        private int value;

        private DataOrders(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }
}    
