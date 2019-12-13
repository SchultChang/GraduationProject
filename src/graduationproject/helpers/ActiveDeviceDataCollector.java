/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.helpers;

import graduationproject.data.DataConverter;
import graduationproject.data.DataManager;
import graduationproject.data.models.Device;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 *
 * @author cloud
 */
public class ActiveDeviceDataCollector {

    private static ActiveDeviceDataCollector instance;
    public static final int MANAGER_DEVICE_ID = -2;
    public static final int UNKNOWN_DEVICE_ID = -1;
    private final String UNKNOWN_DEVICE_VALUE = "Unknown";
    private final String SNMP_MANAGER_VALUE = "SNMP Manager";

    private ActiveDeviceData managerDevice;
    private List<ActiveDeviceData> importedDevices;
    private List<ActiveDeviceData> unknownDevices;

    private ActiveDeviceDataCollector() {
        this.importedDevices = new ArrayList<ActiveDeviceData>();
        this.unknownDevices = new ArrayList<ActiveDeviceData>();
        this.initManagerData();
    }

    private void initManagerData() {
        this.managerDevice = new ActiveDeviceData(MANAGER_DEVICE_ID);
//        System.out.println("MANAGER:");
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            DataConverter dataConverter = new DataConverter();
            while (en.hasMoreElements()) {
                NetworkInterface intf = en.nextElement();

                String macAddress = dataConverter.convertBytesToStringMacAddress(intf.getHardwareAddress());
                List<InterfaceAddress> addresses = intf.getInterfaceAddresses();

                String ipAddress = null;
                for (InterfaceAddress address : addresses) {
                    String temp = address.getAddress().getHostAddress();
                    if (!temp.contains(":")) {
                        ipAddress = temp;
                    }
                }
//                System.out.println("Name:" + intf.getName());
//                if (macAddress != null)
//                    System.out.println("Mac:"+ macAddress);
//                System.out.println("IP:" + ipAddress);
                this.managerDevice.interfaces.add(new InterfaceTopoData(MANAGER_DEVICE_ID, intf.getName(), ipAddress, macAddress));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ActiveDeviceData getManagerDevice() {
        return managerDevice;
    }

    public List<ActiveDeviceData> getImportedDevices() {
        return importedDevices;
    }

    public List<ActiveDeviceData> getUnknownDevices() {
        return unknownDevices;
    }

    public boolean isConnectedToManager(String ip, String mac) {
        return this.managerDevice.containInterface(ip, mac);
    }

    public static ActiveDeviceDataCollector getInstance() {
        if (instance == null) {
            instance = new ActiveDeviceDataCollector();
        }
        return instance;
    }

    public synchronized void addUnknownDevice(String ip, String mac) {
        synchronized (this.unknownDevices) {
            boolean isExisted = false;
            for (ActiveDeviceData deviceData : this.unknownDevices) {
                if (deviceData.containInterface(ip, mac)) {
                    isExisted = true;
                    break;
                }
            }
            if (!isExisted) {
                this.unknownDevices.add(new ActiveDeviceData(UNKNOWN_DEVICE_ID, null, ip, mac));
            }
        }
    }

    public synchronized void addImportedDevice(int deviceId) {
        synchronized (this.importedDevices) {
            boolean isExisted = false;
            for (ActiveDeviceData deviceData : this.importedDevices) {
                if (deviceData.id == deviceId) {
                    isExisted = true;
                    break;
                }
            }
            if (!isExisted) {
                this.importedDevices.add(new ActiveDeviceData(deviceId));
            }
        }
    }

    public synchronized void removeImportedDevice(int deviceId) {
        synchronized (this.importedDevices) {
            int i = 0;
            for (ActiveDeviceData deviceData : this.importedDevices) {
                if (deviceData.id == deviceId) {
//                    System.out.println("COLLECTOR -- REMOVING DEVICE :" + deviceId);
                    this.importedDevices.remove(deviceData);
                }
            }
        }
    }

    private void displayCurrentDevices() {
        System.out.println("COLLECTOR - CURRENT ACTIVE DEVICES:");
        for (ActiveDeviceData deviceData : this.importedDevices) {
            System.out.println(deviceData.id);
        }
    }

    public synchronized void updateInterfaceData(int deviceId, String interfaceName, String interfaceIp, String interfaceMac,
            int[] nextNodeIds, List<String> nextNodeIps, List<String> nextNodeMacs) {
        synchronized (this.importedDevices) {
            for (ActiveDeviceData deviceData : this.importedDevices) {
                if (deviceData.id == deviceId) {
                    deviceData.updateInterface(interfaceName, interfaceIp, interfaceMac, nextNodeIds, nextNodeIps, nextNodeMacs);
                    break;
                }
            }
        }
    }

    public int[] getImportedDeviceIds() {
        synchronized (this.importedDevices) {
            int tempSize = this.importedDevices.size();
            int[] result = new int[tempSize];
            for (int i = 0; i < tempSize; i++) {
                result[i] = this.importedDevices.get(i).id;
            }
            return result;
        }
    }

    public synchronized List<Object> getNextNodesForView(int deviceId, String mac, String nextNodeIp) {
        List<Object> result = new ArrayList<Object>();
        for (ActiveDeviceData deviceData : this.importedDevices) {
            if (deviceData.id == deviceId) {
                return deviceData.getNextNodesForView(mac, nextNodeIp);
            }
        }
        return null;
    }

    private void updateNextNodeToManager(String managerIp, String managerMac, int deviceId, String deviceIp, String deviceMac) {
        this.managerDevice.updateInterface(
                null, managerIp, managerMac,
                new int[]{deviceId},
                Arrays.asList(deviceIp),
                Arrays.asList(deviceMac));
    }

    public class ActiveDeviceData {

        private int id;
        List<InterfaceTopoData> interfaces;

        public ActiveDeviceData(int id) {
            this.id = id;
            this.interfaces = new ArrayList<InterfaceTopoData>();
        }

        public ActiveDeviceData(int id, String name, String ip, String mac) {
            this.id = id;
            this.interfaces = new ArrayList<InterfaceTopoData>();
            this.interfaces.add(new InterfaceTopoData(this.id, name, ip, mac));
        }

        public int getId() {
            return id;
        }

        public String getLabel() {
            if (this.id == MANAGER_DEVICE_ID) {
                return SNMP_MANAGER_VALUE;
            }
            if (this.id == UNKNOWN_DEVICE_ID) {
                return UNKNOWN_DEVICE_VALUE;
            }
            Device device = DataManager.getInstance().getDeviceManager().getDevice(this.id);
            return device.getLabel();
        }

        public void updateInterface(String name, String ip, String mac, int[] nextNodeIds, List<String> nextNodeIps, List<String> nextNodeMacs) {
            boolean isExisted = false;
            for (InterfaceTopoData interfaceData : this.interfaces) {
                if (interfaceData.macAddress.equalsIgnoreCase(mac)) {
                    if (name != null) {
                        interfaceData.name = name;
                    }
                    interfaceData.ipAddress = ip;
//                    interfaceData.macAddress = mac;
                    interfaceData.updateNextNodes(nextNodeIds, nextNodeIps, nextNodeMacs);
                    isExisted = true;
                    break;
                }
            }
            if (!isExisted) {
                InterfaceTopoData newInterface = new InterfaceTopoData(this.id, name, ip, mac);
                newInterface.updateNextNodes(nextNodeIds, nextNodeIps, nextNodeMacs);
                this.interfaces.add(newInterface);
            }
        }

        public boolean containInterface(String ip, String mac) {
            for (InterfaceTopoData interfaceData : this.interfaces) {
                if (interfaceData.macAddress != null && interfaceData.macAddress.equalsIgnoreCase(mac)) {
                    return true;
                }
            }
            return false;
        }

        public List<Object> getNextNodesForView(String mac, String nextNodeIp) {
            for (InterfaceTopoData interfaceData : this.interfaces) {
                if (interfaceData.macAddress.equalsIgnoreCase(mac)) {
                    if (nextNodeIp == null) {
                        return interfaceData.getNextNodesForView();
                    } else {
                        return interfaceData.getNextNodeForView(nextNodeIp);
                    }
                }
            }
            return null;
        }

        public List<NextNodeTopoData> getAllNextNodes() {
            List<NextNodeTopoData> result = new ArrayList<NextNodeTopoData>();
            for (InterfaceTopoData interfaceData : this.interfaces) {
                result.addAll(interfaceData.nextNodes);
            }
            return result;
        }

    }

    public class InterfaceTopoData {

        private int deviceId;
        private String name;
        private String ipAddress;
        private String macAddress;

        private List<NextNodeTopoData> nextNodes;

        public InterfaceTopoData(int deviceId, String name, String ipAddress, String macAddress) {
            this.deviceId = deviceId;
            this.name = name;
            this.ipAddress = ipAddress;
            this.macAddress = macAddress;
            this.nextNodes = new ArrayList<NextNodeTopoData>();
        }

        public void updateNextNodes(int[] nextNodeIds, List<String> nextNodeIps, List<String> nextNodeMacs) {
            int tempSize = nextNodeIds.length;
            NextNodeTopoData temp;
            for (int i = 0; i < tempSize; i++) {
                temp = this.getNextNode(nextNodeIds[i], nextNodeMacs.get(i));

                if (nextNodeIds[i] == MANAGER_DEVICE_ID) {
                    updateNextNodeToManager(nextNodeIps.get(i), nextNodeMacs.get(i), this.deviceId, this.ipAddress, this.macAddress);
                } else if (nextNodeIds[i] == UNKNOWN_DEVICE_ID) {
                    ActiveDeviceDataCollector.this.addUnknownDevice(nextNodeIps.get(i), nextNodeMacs.get(i));
                }

                if (temp == null) {
                    this.nextNodes.add(new NextNodeTopoData(nextNodeIds[i], nextNodeIps.get(i), nextNodeMacs.get(i)));
                } else {
                    temp.ipAddress = nextNodeIps.get(i);
                    temp.macAddress = nextNodeMacs.get(i);
                }
            }
        }

        private NextNodeTopoData getNextNode(int nextNodeId, String mac) {
            for (NextNodeTopoData nextNode : nextNodes) {
                if (nextNode.id == nextNodeId && nextNode.id != UNKNOWN_DEVICE_ID) {
                    return nextNode;
                }
                if (nextNode.macAddress.equalsIgnoreCase(mac)) {
                    return nextNode;
                }
            }
            return null;
        }

        public List<Object> getNextNodesForView() {
            int tempSize = this.nextNodes.size();
            if (tempSize > 0) {
                String[] ipAddresses = new String[tempSize];
                for (int i = 0; i < tempSize; i++) {
                    ipAddresses[i] = this.nextNodes.get(i).ipAddress;
                }

                NextNodeTopoData firstNode = this.nextNodes.get(0);
                if (firstNode.id == UNKNOWN_DEVICE_ID) {
                    return this.convertDataToView(ipAddresses, UNKNOWN_DEVICE_VALUE, UNKNOWN_DEVICE_VALUE, firstNode.macAddress);
                } else if (firstNode.id == MANAGER_DEVICE_ID) {
                    return this.convertDataToView(ipAddresses, SNMP_MANAGER_VALUE, SNMP_MANAGER_VALUE, firstNode.macAddress);
                } else {
                    Device device = DataManager.getInstance().getDeviceManager().getDevice(firstNode.id);
                    return this.convertDataToView(ipAddresses, device.getName(), device.getLabel(), firstNode.macAddress);
                }
            }

            return this.convertDataToView(new String[]{}, new String(), new String(), new String());
        }

        public List<Object> getNextNodeForView(String nextNodeIp) {
            for (NextNodeTopoData nextNode : this.nextNodes) {
                if (nextNode.ipAddress.equals(nextNodeIp)) {
                    if (nextNode.id == UNKNOWN_DEVICE_ID) {
                        return this.convertDataToView(new String[]{}, UNKNOWN_DEVICE_VALUE, UNKNOWN_DEVICE_VALUE, nextNode.macAddress);
                    } else if (nextNode.id == MANAGER_DEVICE_ID) {
                        return this.convertDataToView(new String[]{}, SNMP_MANAGER_VALUE, SNMP_MANAGER_VALUE, nextNode.macAddress);
                    } else {
                        Device device = DataManager.getInstance().getDeviceManager().getDevice(nextNode.id);
                        return this.convertDataToView(new String[]{}, device.getName(), device.getLabel(), nextNode.macAddress);
                    }
                }
            }
            return null;
        }

        private List<Object> convertDataToView(String[] ipAddresses, String name, String label, String macAddress) {
            List<Object> result = new ArrayList<Object>();
            result.add(NextNodeDataOrders.IP_ADDRESSES.getValue(), ipAddresses);
            result.add(NextNodeDataOrders.NAME.getValue(), name);
            result.add(NextNodeDataOrders.LABEL.getValue(), label);
            result.add(NextNodeDataOrders.MAC_ADDRESS.getValue(), macAddress);
            return result;
        }
    }

    public class NextNodeTopoData {

        private int id;
        private String ipAddress;
        private String macAddress;

        public NextNodeTopoData(int id, String ipAddress, String macAddress) {
            this.id = id;
            this.ipAddress = ipAddress;
            this.macAddress = macAddress;
        }

        public int getId() {
            return id;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public String getMacAddress() {
            return macAddress;
        }

    }

    public enum NextNodeDataOrders {
        IP_ADDRESSES(0),
        NAME(1),
        LABEL(2),
        MAC_ADDRESS(3);

        private int value;

        private NextNodeDataOrders(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }
}
