/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data;

import graduationproject.data.DataManager;
import graduationproject.data.models.Device;
import graduationproject.helpers.AddressParser;
import graduationproject.helpers.DataConverter;
import graduationproject.helpers.TopoDrawer;
import static graduationproject.helpers.TopoDrawer.VS_DEVICE_ID;
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

    public boolean newDataForTopo;

    public static final int MANAGER_DEVICE_ID = -2;
    public static final int UNKNOWN_DEVICE_ID = -1;

    private final String UNKNOWN_DEVICE_VALUE = "Unknown";
    private final String SNMP_MANAGER_VALUE = "SNMP Manager";

    private ActiveDeviceData managerDevice;
    private List<ActiveDeviceData> importedDevices;
    private List<ActiveDeviceData> unknownDevices;
//    private List<ActiveDeviceData> virtualSwitches;

    private static ActiveDeviceDataCollector instance;

    private ActiveDeviceDataCollector() {
        this.importedDevices = new ArrayList<ActiveDeviceData>();
        this.unknownDevices = new ArrayList<ActiveDeviceData>();
//        this.virtualSwitches = new ArrayList<ActiveDeviceData>();
        this.newDataForTopo = false;

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
                short prefixLength = 0;
                for (InterfaceAddress address : addresses) {
                    String temp = address.getAddress().getHostAddress();
                    if (!temp.contains(":")) {
                        ipAddress = temp;
                        prefixLength = address.getNetworkPrefixLength();
                    }
                }
                this.managerDevice.interfaces.add(new InterfaceData(MANAGER_DEVICE_ID, intf.getName(), ipAddress,
                        new AddressParser().getNetworkIp(ipAddress, prefixLength), macAddress));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized ActiveDeviceData getManagerDevice() {
        return managerDevice;
    }

    public synchronized List<ActiveDeviceData> getImportedDevices() {
        return importedDevices;
    }

    public synchronized List<ActiveDeviceData> getUnknownDevices() {
        return unknownDevices;
    }

    public int[] findNextNodeId(List<String> ipAddresses, List<String> macAddresses) {
        int tempSize = macAddresses.size();
        int[] result = new int[tempSize];
        for (int i = 0; i < tempSize; i++) {
            //Device device = DataManager.getInstance().getDeviceManager().getDevice(macAddresses.get(i));
            result[i] = ActiveDeviceDataCollector.UNKNOWN_DEVICE_ID;
            if (ActiveDeviceDataCollector.getInstance().isConnectedToManager(ipAddresses.get(i), macAddresses.get(i))) {
                result[i] = ActiveDeviceDataCollector.MANAGER_DEVICE_ID;
            } else {
                synchronized (this.importedDevices) {
                    for (ActiveDeviceData device : this.importedDevices) {
                        if (device.containInterface(ipAddresses.get(i), macAddresses.get(i))) {
                            result[i] = device.id;
                        }
                    }

                }
            }
        }
        return result;
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

    public synchronized void addUnknownDevice(String ip, String networkIp, String mac,
            int nextNodeId, String nextNodeIp, String nextNodeMac) {
        synchronized (this.unknownDevices) {
            boolean isExisted = false;
            for (ActiveDeviceData deviceData : this.unknownDevices) {
                if (deviceData.containInterface(ip, mac)) {
//                    deviceData.unknownLinkCount++;
                    deviceData.updateInterface(null, ip, networkIp, mac,
                            new int[]{nextNodeId},
                            Arrays.asList(nextNodeIp),
                            Arrays.asList(nextNodeMac));
                    isExisted = true;
//                    System.out.println("ADDING UNKNOWN NODE");
                    break;
                }
            }
            if (!isExisted) {
//                System.out.println("ADDING UNKNOWN NODE");
                this.newDataForTopo = true;
                ActiveDeviceData unknownDevice = new ActiveDeviceData(UNKNOWN_DEVICE_ID, null, ip, networkIp, mac);
                unknownDevice.updateInterface(null, ip, networkIp, mac,
                        new int[]{nextNodeId},
                        Arrays.asList(nextNodeIp),
                        Arrays.asList(nextNodeMac));
                this.unknownDevices.add(unknownDevice);
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
                this.newDataForTopo = true;
                this.importedDevices.add(new ActiveDeviceData(deviceId));
            }
        }
    }

    public synchronized void removeImportedDevice(int deviceId) {
        synchronized (this.importedDevices) {
            int i = 0;
            for (ActiveDeviceData deviceData : this.importedDevices) {
                if (deviceData.id == deviceId) {
                    deviceData.onNodeRemoved();
                    this.importedDevices.remove(deviceData);

                    this.newDataForTopo = true;
                }
            }
        }
    }

    public void removeUnknownDevice(String ipAddress, String macAddress,
            int nextNodeId, String nextNodeIp, String nextNodeMac) {
        synchronized (this.unknownDevices) {
//            System.out.println("REMOVING UNKNOWN NODE 2");
            for (ActiveDeviceData device : this.unknownDevices) {
                if (device.containInterface(ipAddress, macAddress)) {
                    device.removeNextNode(nextNodeId, nextNodeMac);
                    if (device.interfaces.get(0).nextNodes.isEmpty()) {
                        this.unknownDevices.remove(device);
                        this.newDataForTopo = true;
                        System.out.println("REMOVE UNKNOWN DEVICE");
                        break;
                    }
                }
            }
        }
    }

    public synchronized void updateInterfaceData(int deviceId, String interfaceName, String interfaceIp, String networkIp, String interfaceMac,
            int[] nextNodeIds, List<String> nextNodeIps, List<String> nextNodeMacs) {
        synchronized (this.importedDevices) {
            for (ActiveDeviceData deviceData : this.importedDevices) {
                if (deviceData.id == deviceId) {
                    deviceData.updateInterface(interfaceName, interfaceIp, networkIp, interfaceMac, nextNodeIds, nextNodeIps, nextNodeMacs);
                    break;
                }
            }
        }
    }

    public synchronized void mergeNewInterfaceData(int deviceId) {
        synchronized (this.importedDevices) {
            for (ActiveDeviceData deviceData : this.importedDevices) {
                if (deviceData.id == deviceId) {
                    deviceData.mergeNewInterfaces();
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

    public class ActiveDeviceData {

        private int id;
        List<InterfaceData> interfaces;
        List<InterfaceData> newInterfaces;
//        private int unknownLinkCount;

        public ActiveDeviceData(int id) {
            this.id = id;
            this.interfaces = new ArrayList<InterfaceData>();
            if (this.id != MANAGER_DEVICE_ID && this.id != UNKNOWN_DEVICE_ID) {
                this.newInterfaces = new ArrayList<InterfaceData>();
            } else {
                this.newInterfaces = this.interfaces;
            }
//            this.unknownLinkCount = 1;
        }

        public ActiveDeviceData(int id, String name, String ip, String networkIp, String mac) {
            this.id = id;
            this.interfaces = new ArrayList<InterfaceData>();
            if (this.id != MANAGER_DEVICE_ID && this.id != UNKNOWN_DEVICE_ID) {
                this.newInterfaces = new ArrayList<InterfaceData>();
            } else {
                this.newInterfaces = this.interfaces;
            }
            this.interfaces.add(new InterfaceData(this.id, name, ip, networkIp, mac));
//            this.unknownLinkCount = 1;
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

        public void updateInterface(String name, String ip, String networkIp, String mac, int[] nextNodeIds, List<String> nextNodeIps, List<String> nextNodeMacs) {
            boolean isExisted = false;
            for (InterfaceData interfaceData : this.newInterfaces) {
                if (interfaceData.macAddress.equalsIgnoreCase(mac)) {
                    if (name != null) {
                        interfaceData.name = name;
                    }
                    interfaceData.ipAddress = ip;
                    interfaceData.macAddress = mac;
                    interfaceData.updateNextNodes(nextNodeIds, nextNodeIps, nextNodeMacs);
                    isExisted = true;
                    break;
                }
            }
            if (!isExisted) {
                InterfaceData newInterface = new InterfaceData(this.id, name, ip, networkIp, mac);
                newInterface.updateNextNodes(nextNodeIds, nextNodeIps, nextNodeMacs);
                this.newInterfaces.add(newInterface);
            }
        }

        public void mergeNewInterfaces() {
            synchronized (this.interfaces) {
                synchronized (this.newInterfaces) {
//                    List<NextNodeData> currentNextNodes = this.getAllNextNodes();
//                    List<NextNodeData> removedNextNodes = new ArrayList<NextNodeData>();
                    boolean isExisted = false;

                    for (InterfaceData oldInterface : this.interfaces) {
                        for (NextNodeData currentNextNode : oldInterface.nextNodes) {
                            isExisted = false;
                            for (InterfaceData newInterface : this.newInterfaces) {
                                NextNodeData temp = newInterface.getNextNode(currentNextNode.getId(), currentNextNode.getMacAddress());
                                if (temp != null && temp.getId() == currentNextNode.getId()) {
                                    isExisted = true;
                                    break;
                                }
                            }

                            if (!isExisted) {
                                newDataForTopo = true;                                  //some connection is lost
                                if (currentNextNode.id == MANAGER_DEVICE_ID) {
                                    System.out.println("WHEN MERGING " + this.id);
                                    managerDevice.removeNextNode(this.id, new String());
                                }
                                if (currentNextNode.id == UNKNOWN_DEVICE_ID) {
                                    ActiveDeviceDataCollector.getInstance().removeUnknownDevice(
                                            currentNextNode.ipAddress, currentNextNode.macAddress,
                                            this.id, oldInterface.ipAddress, oldInterface.macAddress);
                                }
                            }
                        }
                    }

                    this.interfaces = this.newInterfaces;
                    this.newInterfaces = new ArrayList<InterfaceData>();
                    System.gc();
                }
            }
        }

        public boolean containInterface(String ip, String mac) {
            for (InterfaceData interfaceData : this.interfaces) {
                if (interfaceData.macAddress != null && interfaceData.macAddress.equalsIgnoreCase(mac)) {
                    return true;
                }
            }
            return false;
        }

        public List<Object> getNextNodesForView(String mac, String nextNodeIp) {
            for (InterfaceData interfaceData : this.interfaces) {
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

        public void onNodeRemoved() {
            if (this.id == UNKNOWN_DEVICE_ID || this.id == MANAGER_DEVICE_ID) {
                return;
            }
            for (InterfaceData interfaceData : this.interfaces) {
                for (NextNodeData nextNode : interfaceData.nextNodes) {
                    if (nextNode.id == MANAGER_DEVICE_ID) {
                        System.out.println("REMOVE MANAGER's NEXT NODE " + this.id);
                        managerDevice.removeNextNode(this.id, interfaceData.macAddress);
                    }
                    if (nextNode.id == UNKNOWN_DEVICE_ID) {
                        ActiveDeviceDataCollector.getInstance().removeUnknownDevice(nextNode.ipAddress, nextNode.macAddress,
                                this.id, interfaceData.ipAddress, interfaceData.macAddress);
                    }
                }
            }
        }

        public void removeNextNode(int nextNodeId, String mac) {
            for (InterfaceData interfaceData : this.interfaces) {
//                interfaceData.displayAllNextNodes();
                NextNodeData nextNode = interfaceData.getNextNode(nextNodeId, mac);
                if (nextNode != null) {
                    if (nextNode.id == MANAGER_DEVICE_ID) {                     //this next node will have to be ensured to not to connect to other interface
                        managerDevice.removeNextNode(id, interfaceData.macAddress);
                    }
                    if (nextNode.id == UNKNOWN_DEVICE_ID) {
                        ActiveDeviceDataCollector.getInstance().removeUnknownDevice(nextNode.ipAddress, nextNode.macAddress,
                                this.id, interfaceData.ipAddress, interfaceData.macAddress);
                    }
//                    System.out.println("DEVICE IS REMOVING NEXT NODE: " + this.id);
//                    System.out.println("REMOVED NEXT NODE: " + nextNode.id);
                    interfaceData.nextNodes.remove(nextNode);
                    newDataForTopo = true;
                    break;
                }
            }
        }

        public synchronized List<NextNodeData> getAllNextNodes() {
            List<NextNodeData> result = new ArrayList<NextNodeData>();
            for (InterfaceData interfaceData : this.interfaces) {
                if (interfaceData.nextNodes.size() == 1) {
                    if (this.id == MANAGER_DEVICE_ID || this.id == UNKNOWN_DEVICE_ID) {
                        if (TopoDrawer.getInstance().isConnectedToVs(interfaceData.networkIp)) {
                            result.add(new NextNodeData(VS_DEVICE_ID, new String(), interfaceData.networkIp, new String()));
                            continue;
                        }
                    }
                    result.add(interfaceData.nextNodes.get(0));
                } else if (interfaceData.nextNodes.size() > 1) {
                    result.add(new NextNodeData(VS_DEVICE_ID, new String(), interfaceData.networkIp, new String()));
                }
            }
            return result;
        }

        public List<String> getNetworkWithManyNextNodes() {
            List<String> result = new ArrayList<String>();

            for (InterfaceData interfaceData : this.interfaces) {
                if (interfaceData.nextNodes.size() > 1) {
//                    System.out.println("DEVICE + " + this.id + " NETWORK IP : " + interfaceData.networkIp);
                    result.add(interfaceData.networkIp);
                }
            }

            return result;
        }

        public boolean belongToNetwork(String networkAddress) {
            for (InterfaceData interfaceData : this.interfaces) {
                if (interfaceData.networkIp.equalsIgnoreCase(networkAddress)) {
                    return true;
                }
            }
            return false;
        }
        
        public String getDefaultAddress() {
            if (this.id == MANAGER_DEVICE_ID) {
                return "127.0.0.1";
            }
            return this.interfaces.get(0).ipAddress;
        }

    }

    public class InterfaceData {

        private int deviceId;
        private String name;
        private String networkIp;
        private String ipAddress;
        private String macAddress;

        private List<NextNodeData> nextNodes;

        public InterfaceData(int deviceId, String name, String ipAddress, String networkIp, String macAddress) {
            this.deviceId = deviceId;
            this.name = name;
            this.ipAddress = ipAddress;
            this.networkIp = networkIp;
            this.macAddress = macAddress;
            this.nextNodes = new ArrayList<NextNodeData>();
        }

        public void updateNextNodes(int[] nextNodeIds, List<String> nextNodeIps, List<String> nextNodeMacs) {
            int tempSize = nextNodeIds.length;
            NextNodeData temp;
            for (int i = 0; i < tempSize; i++) {
                temp = this.getNextNode(nextNodeIds[i], nextNodeMacs.get(i));

                if (nextNodeIds[i] == MANAGER_DEVICE_ID) {
//                    updateNextNodeToManager(nextNodeIps.get(i), nextNodeMacs.get(i), this.deviceId, this.ipAddress, this.macAddress);
                    System.out.println("ADDING NEXT NODE TO MANAGER " + this.deviceId);
                    synchronized (managerDevice) {
                        managerDevice.updateInterface(
                                null, nextNodeIps.get(i), this.networkIp, nextNodeMacs.get(i),
                                new int[]{this.deviceId},
                                Arrays.asList(this.ipAddress),
                                Arrays.asList(this.macAddress));
                    }

                } else if (nextNodeIds[i] == UNKNOWN_DEVICE_ID) {
                    ActiveDeviceDataCollector.this.addUnknownDevice(nextNodeIps.get(i), this.networkIp, nextNodeMacs.get(i),
                            this.deviceId, this.ipAddress, this.macAddress);
                }

                if (temp == null) {
                    this.nextNodes.add(new NextNodeData(nextNodeIds[i], nextNodeIps.get(i), this.networkIp, nextNodeMacs.get(i)));
                } else {
                    temp.id = nextNodeIds[i];
                    temp.ipAddress = nextNodeIps.get(i);
                    temp.macAddress = nextNodeMacs.get(i);
                }
            }
        }

        private NextNodeData getNextNode(int nextNodeId, String mac) {
            for (NextNodeData nextNode : nextNodes) {
                if (nextNode.id == nextNodeId && nextNode.macAddress.equalsIgnoreCase(mac)) {
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

                NextNodeData firstNode = this.nextNodes.get(0);
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
            for (NextNodeData nextNode : this.nextNodes) {
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
        
        public void displayAllNextNodes() {
            for (NextNodeData nextNode : this.nextNodes) {
                System.out.println("ID " + nextNode.id + " IP " + nextNode.ipAddress + " MAC " + nextNode.macAddress);
            }
        }

    }

    public class NextNodeData {

        private int id;
        private String ipAddress;
        private String networkIp;
        private String macAddress;

        public NextNodeData(int id, String ipAddress, String networkIp, String macAddress) {
            this.id = id;
            this.ipAddress = ipAddress;
            this.networkIp = networkIp;
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

        public String getNetworkIp() {
            return networkIp;
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