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
                        new AddressParser().getNetworkIp(ipAddress, prefixLength), AddressParser.normalizeMac(macAddress)));
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

    public int[] findConnectedNodeIds(List<String> ipAddresses, List<String> macAddresses) {
        int tempSize = macAddresses.size();
        int[] result = new int[tempSize];
        for (int i = 0; i < tempSize; i++) {
            //Device device = DataManager.getInstance().getDeviceManager().getDevice(macAddresses.get(i));
            result[i] = ActiveDeviceDataCollector.UNKNOWN_DEVICE_ID;
            if (ActiveDeviceDataCollector.getInstance().isConnectedToManager(ipAddresses.get(i), macAddresses.get(i))) {
                result[i] = ActiveDeviceDataCollector.MANAGER_DEVICE_ID;
            } else {
//                synchronized (this.importedDevices) {
//                    for (ActiveDeviceData device : this.importedDevices) {
//                        if (device.containInterface(ipAddresses.get(i), macAddresses.get(i))) {
//                            result[i] = device.id;
//                        }
//                    }
//
//                }
                Device device = DataManager.getInstance().getDeviceManager().getDevice(macAddresses.get(i));
                if (device != null) {
                    result[i] = device.getId();
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
            int connectedNodeId, String connectedNodeIp, String connectedNodeMac) {
        synchronized (this.unknownDevices) {
            boolean isExisted = false;
            for (ActiveDeviceData deviceData : this.unknownDevices) {
                if (deviceData.containInterface(ip, mac)) {
//                    deviceData.unknownLinkCount++;
                    deviceData.updateInterface(null, ip, networkIp, mac,
                            new int[]{connectedNodeId},
                            Arrays.asList(connectedNodeIp),
                            Arrays.asList(connectedNodeMac));
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
                        new int[]{connectedNodeId},
                        Arrays.asList(connectedNodeIp),
                        Arrays.asList(connectedNodeMac));
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
            int connectedNodeId, String connectedNodeIp, String connectedNodeMac) {
        synchronized (this.unknownDevices) {
//            System.out.println("REMOVING UNKNOWN NODE 2");
            for (ActiveDeviceData device : this.unknownDevices) {
                if (device.containInterface(ipAddress, macAddress)) {
                    device.removeConnectedNode(connectedNodeId, connectedNodeMac);
                    if (device.interfaces.get(0).connectedNodes.isEmpty()) {
                        this.unknownDevices.remove(device);
                        this.newDataForTopo = true;
                        System.out.println("REMOVE UNKNOWN DEVICE");
                        break;
                    }
                }
            }
        }
    }

//    public synchronized void updateInterfaceData(int deviceId, String interfaceName, String interfaceIp, String networkIp, String interfaceMac,
//            int[] connectedNodeIds, List<String> connectedNodeIps, List<String> connectedNodeMacs) {
//        synchronized (this.importedDevices) {
//            for (ActiveDeviceData deviceData : this.importedDevices) {
//                if (deviceData.id == deviceId) {
//                    deviceData.updateInterface(interfaceName, interfaceIp, networkIp, interfaceMac, 
//                            connectedNodeIds, connectedNodeIps, connectedNodeMacs);
//                    break;
//                }
//            }
//        }
//    }
//                String netmask, long mtu, long inboundPackets, long outboundPackets, long inboundDiscards, long outboundDiscards) {
    public synchronized InterfaceData updateInterfaceData(int deviceId, String interfaceName, String interfaceIp, String networkIp, String interfaceMac,
            String netmask, long mtu, long inboundPackets, long outboundPackets, long inboundDiscards, long outboundDiscards,
            int[] connectedNodeIds, List<String> connectedNodeIps, List<String> connectedNodeMacs) {
        synchronized (this.importedDevices) {
            for (ActiveDeviceData deviceData : this.importedDevices) {
                if (deviceData.id == deviceId) {
                    return deviceData.updateInterface(interfaceName, interfaceIp, networkIp, interfaceMac,
                            netmask, mtu, inboundPackets, outboundPackets, inboundDiscards, outboundDiscards,
                            connectedNodeIds, connectedNodeIps, connectedNodeMacs);
                }
            }
        }
        return null;
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

    public synchronized List<Object> getInterfaceDynamicDataForView(int deviceId, String mac) {
        for (ActiveDeviceData deviceData : this.importedDevices) {
            if (deviceData.id == deviceId) {
                return deviceData.getInterfaceDynamicDataForView(mac);
            }
        }
        return null;
    }

    public synchronized List<Object> getConnectedNodesForView(int deviceId, String mac, String connectedNodeIp) {
        List<Object> result = new ArrayList<Object>();
        for (ActiveDeviceData deviceData : this.importedDevices) {
            if (deviceData.id == deviceId) {
                return deviceData.getConnectedNodesForView(mac, connectedNodeIp);
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
//                String netmask, long mtu, long inboundPackets, long outboundPackets, long inboundDiscards, long outboundDiscards) {

        public void updateInterface(String name, String ip, String networkIp, String mac,
                int[] connectedNodeIds, List<String> connectedNodeIps, List<String> connectedNodeMacs) {
            boolean isExisted = false;
            for (InterfaceData interfaceData : this.newInterfaces) {
                if (interfaceData.macAddress.equalsIgnoreCase(mac)) {
                    if (name != null) {
                        interfaceData.name = name;
                    }
                    interfaceData.ipAddress = ip;
                    interfaceData.macAddress = mac;
                    interfaceData.updateConnectedNodes(connectedNodeIds, connectedNodeIps, connectedNodeMacs);
                    isExisted = true;
                    break;
                }
            }
            if (!isExisted) {
                InterfaceData newInterface = new InterfaceData(this.id, name, ip, networkIp, mac);
                newInterface.updateConnectedNodes(connectedNodeIds, connectedNodeIps, connectedNodeMacs);
                this.newInterfaces.add(newInterface);
            }
        }

        public InterfaceData updateInterface(String name, String ip, String networkIp, String mac,
                String netmask, long mtu, long inboundPackets, long outboundPackets, long inboundDiscards, long outboundDiscards,
                int[] connectedNodeIds, List<String> connectedNodeIps, List<String> connectedNodeMacs) {
//            boolean isExisted = false;
//            if (!isExisted) {
            InterfaceData newInterface = new InterfaceData(this.id, name, ip, networkIp, mac,
                    netmask, mtu, inboundPackets, outboundPackets, inboundDiscards, outboundDiscards);
            newInterface.updateConnectedNodes(connectedNodeIds, connectedNodeIps, connectedNodeMacs);
            this.newInterfaces.add(newInterface);
            return newInterface;
//            }
//            return null;
        }

        public void mergeNewInterfaces() {
            synchronized (this.interfaces) {
                synchronized (this.newInterfaces) {
//                    List<ConnectedNodeData> currentConnectedNodes = this.getAllConnectedNodes();
//                    List<ConnectedNodeData> removedConnectedNodes = new ArrayList<ConnectedNodeData>();
                    boolean isExisted = false;

                    for (InterfaceData oldInterface : this.interfaces) {
                        for (ConnectedNodeData currentConnectedNode : oldInterface.connectedNodes) {
                            isExisted = false;
                            for (InterfaceData newInterface : this.newInterfaces) {
                                ConnectedNodeData temp = newInterface.getConnectedNode(currentConnectedNode.getId(), currentConnectedNode.getMacAddress());
                                if (temp != null && temp.getId() == currentConnectedNode.getId()) {
                                    isExisted = true;
                                    break;
                                }
                            }

                            if (!isExisted) {
                                newDataForTopo = true;                                  //some connection is lost
                                if (currentConnectedNode.id == MANAGER_DEVICE_ID) {
                                    System.out.println("WHEN MERGING " + this.id);
                                    managerDevice.removeConnectedNode(this.id, new String());
                                }
                                if (currentConnectedNode.id == UNKNOWN_DEVICE_ID) {
                                    ActiveDeviceDataCollector.getInstance().removeUnknownDevice(
                                            currentConnectedNode.ipAddress, currentConnectedNode.macAddress,
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

        public List<Object> getInterfaceDynamicDataForView(String mac) {
            for (InterfaceData interfaceData : this.interfaces) {
                if (interfaceData.macAddress.equalsIgnoreCase(mac)) {
                    return interfaceData.getInterfaceDynamicDataForView();
                }
            }
            return null;
        }

        public List<Object> getConnectedNodesForView(String mac, String connectedNodeIp) {
            for (InterfaceData interfaceData : this.interfaces) {
                if (interfaceData.macAddress.equalsIgnoreCase(mac)) {
                    if (connectedNodeIp == null) {
                        return interfaceData.getConnectedNodesForView();
                    } else {
                        return interfaceData.getConnectedNodeForView(connectedNodeIp);
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
                for (ConnectedNodeData connectedNode : interfaceData.connectedNodes) {
                    if (connectedNode.id == MANAGER_DEVICE_ID) {
                        System.out.println("REMOVE MANAGER's NEXT NODE " + this.id);
                        managerDevice.removeConnectedNode(this.id, interfaceData.macAddress);
                    }
                    if (connectedNode.id == UNKNOWN_DEVICE_ID) {
                        ActiveDeviceDataCollector.getInstance().removeUnknownDevice(connectedNode.ipAddress, connectedNode.macAddress,
                                this.id, interfaceData.ipAddress, interfaceData.macAddress);
                    }
                }
            }
        }

        public void removeConnectedNode(int connectedNodeId, String mac) {
            for (InterfaceData interfaceData : this.interfaces) {
//                interfaceData.displayAllConnectedNodes();
                ConnectedNodeData connectedNode = interfaceData.getConnectedNode(connectedNodeId, mac);
                if (connectedNode != null) {
                    if (connectedNode.id == MANAGER_DEVICE_ID) {                     //this next node will have to be ensured to not to connect to other interface
                        managerDevice.removeConnectedNode(id, interfaceData.macAddress);
                    }
                    if (connectedNode.id == UNKNOWN_DEVICE_ID) {
                        ActiveDeviceDataCollector.getInstance().removeUnknownDevice(connectedNode.ipAddress, connectedNode.macAddress,
                                this.id, interfaceData.ipAddress, interfaceData.macAddress);
                    }
//                    System.out.println("DEVICE IS REMOVING NEXT NODE: " + this.id);
//                    System.out.println("REMOVED NEXT NODE: " + connectedNode.id);
                    interfaceData.connectedNodes.remove(connectedNode);
                    newDataForTopo = true;
                    break;
                }
            }
        }

        public synchronized List<ConnectedNodeData> getAllConnectedNodes() {
            List<ConnectedNodeData> result = new ArrayList<ConnectedNodeData>();
            for (InterfaceData interfaceData : this.interfaces) {
                if (interfaceData.connectedNodes.size() == 1) {
                    if (this.id == MANAGER_DEVICE_ID || this.id == UNKNOWN_DEVICE_ID) {
                        if (TopoDrawer.getInstance().isConnectedToVs(interfaceData.networkIp)) {
                            result.add(new ConnectedNodeData(VS_DEVICE_ID, new String(), interfaceData.networkIp, new String()));
                            continue;
                        }
                    }
                    result.add(interfaceData.connectedNodes.get(0));
                } else if (interfaceData.connectedNodes.size() > 1) {
                    result.add(new ConnectedNodeData(VS_DEVICE_ID, new String(), interfaceData.networkIp, new String()));
                }
            }
            return result;
        }

        public List<String> getNetworkWithManyConnectedNodes() {
            List<String> result = new ArrayList<String>();

            for (InterfaceData interfaceData : this.interfaces) {
                if (interfaceData.connectedNodes.size() > 1) {
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
//        private String ipAddress;
        private String name;
        private String macAddress;
        private String networkIp;
        private String ipAddress;
        private String netmask;
        private long mtu;
        private long inboundPackets;
        private long outboundPackets;
        private long inboundDiscards;
        private long outboundDiscards;

        private List<ConnectedNodeData> connectedNodes;

        public InterfaceData(int deviceId, String name, String ipAddress, String networkIp, String macAddress) {
            this.deviceId = deviceId;
            this.name = name;
            this.ipAddress = ipAddress;
            this.networkIp = networkIp;
            this.macAddress = macAddress;
            this.netmask = netmask;

            this.mtu = 0;
            this.inboundPackets = 0;
            this.outboundPackets = 0;
            this.inboundDiscards = 0;
            this.outboundDiscards = 0;

            this.connectedNodes = new ArrayList<ConnectedNodeData>();
        }

        public InterfaceData(int deviceId, String name, String ipAddress, String networkIp, String macAddress,
                String netmask, long mtu, long inboundPackets, long outboundPackets, long inboundDiscards, long outboundDiscards) {
            this.deviceId = deviceId;
            this.name = name;
            this.ipAddress = ipAddress;
            this.networkIp = networkIp;
            this.macAddress = macAddress;
            this.netmask = netmask;

            this.mtu = mtu;
            this.inboundPackets = inboundPackets;
            this.outboundPackets = outboundPackets;
            this.inboundDiscards = inboundDiscards;
            this.outboundDiscards = outboundDiscards;

            this.connectedNodes = new ArrayList<ConnectedNodeData>();
        }

        public void updateConnectedNodes(int[] connectedNodeIds, List<String> connectedNodeIps, List<String> connectedNodeMacs) {
            int tempSize = connectedNodeIds.length;
            ConnectedNodeData temp;
            for (int i = 0; i < tempSize; i++) {
                temp = this.getConnectedNode(connectedNodeIds[i], connectedNodeMacs.get(i));

                if (connectedNodeIds[i] == MANAGER_DEVICE_ID) {
//                    updateConnectedNodeToManager(connectedNodeIps.get(i), connectedNodeMacs.get(i), this.deviceId, this.ipAddress, this.macAddress);
//                    System.out.println("ADDING NEXT NODE TO MANAGER " + this.deviceId);
                    synchronized (managerDevice) {
                        managerDevice.updateInterface(
                                null, connectedNodeIps.get(i), this.networkIp, connectedNodeMacs.get(i),
                                new int[]{this.deviceId},
                                Arrays.asList(this.ipAddress),
                                Arrays.asList(this.macAddress));
                    }

                } else if (connectedNodeIds[i] == UNKNOWN_DEVICE_ID) {
                    ActiveDeviceDataCollector.this.addUnknownDevice(connectedNodeIps.get(i), this.networkIp, connectedNodeMacs.get(i),
                            this.deviceId, this.ipAddress, this.macAddress);
                }

                if (temp == null) {
                    this.connectedNodes.add(new ConnectedNodeData(connectedNodeIds[i], connectedNodeIps.get(i), this.networkIp, connectedNodeMacs.get(i)));
                } else {
                    temp.id = connectedNodeIds[i];
                    temp.ipAddress = connectedNodeIps.get(i);
                    temp.macAddress = connectedNodeMacs.get(i);
                }
            }
        }

        private ConnectedNodeData getConnectedNode(int connectedNodeId, String mac) {
            for (ConnectedNodeData connectedNode : connectedNodes) {
                if (connectedNode.id == connectedNodeId && connectedNode.macAddress.equalsIgnoreCase(mac)) {
                    return connectedNode;
                }
            }
            return null;
        }

        public List<Object> getInterfaceDynamicDataForView() {
//        IP_ADDRESS(0),
//        NETMASK(1),
//        MTU(2),
//        INPACKETS(3),
//        OUTPACKETS(4),
//        INDISCARDS(5),
//        OUTDISCARDS(6);
            List<Object> result = new ArrayList<>();
            result.add(InterfaceDynamicDataOrders.IP_ADDRESS.getValue(), this.ipAddress);
            result.add(InterfaceDynamicDataOrders.NETMASK.getValue(), this.netmask);
            result.add(InterfaceDynamicDataOrders.MTU.getValue(), this.mtu);
            result.add(InterfaceDynamicDataOrders.INPACKETS.getValue(), this.inboundPackets);
            result.add(InterfaceDynamicDataOrders.OUTPACKETS.getValue(), this.outboundPackets);
            result.add(InterfaceDynamicDataOrders.INDISCARDS.getValue(), this.inboundDiscards);
            result.add(InterfaceDynamicDataOrders.OUTDISCARDS.getValue(), this.outboundDiscards);
            return result;
        }

        public List<Object> getConnectedNodesForView() {
            int tempSize = this.connectedNodes.size();
            if (tempSize > 0) {
                int interId = -1;
                for (int i = 0; i < tempSize; i++) {
                    if (this.connectedNodes.get(i).ipAddress.equalsIgnoreCase(this.ipAddress)) {
                        interId = i;
                        break;
                    }
                }

                String[] ipAddresses = null;
                if (interId != -1) {
                    ipAddresses = new String[tempSize - 1];
                    int j = 0;
                    for (int i = 0; i < tempSize; i++) {
                        if (i != interId) {
                            ipAddresses[j] = this.connectedNodes.get(i).ipAddress;
                            j++;
                        }
                    }
                } else {
                    ipAddresses = new String[tempSize];
                    for (int i = 0; i < tempSize; i++) {
                        ipAddresses[i] = this.connectedNodes.get(i).ipAddress;
                    }
                }

                ConnectedNodeData firstNode = null;
                if (interId != 0) {
                    firstNode = this.connectedNodes.get(0);
                } else if (this.connectedNodes.size() > 1) {
                    firstNode = this.connectedNodes.get(1);
                }

                if (firstNode != null) {
                    if (firstNode.id == UNKNOWN_DEVICE_ID) {
                        return this.convertDataToView(ipAddresses, UNKNOWN_DEVICE_VALUE, UNKNOWN_DEVICE_VALUE, firstNode.macAddress);
                    } else if (firstNode.id == MANAGER_DEVICE_ID) {
                        return this.convertDataToView(ipAddresses, SNMP_MANAGER_VALUE, SNMP_MANAGER_VALUE, firstNode.macAddress);
                    } else {
                        Device device = DataManager.getInstance().getDeviceManager().getDevice(firstNode.id);
                        return this.convertDataToView(ipAddresses, device.getName(), device.getLabel(), firstNode.macAddress);
                    }
                }
            }

            return this.convertDataToView(new String[]{}, new String(), new String(), new String());
        }

        public List<Object> getConnectedNodeForView(String connectedNodeIp) {
            for (ConnectedNodeData connectedNode : this.connectedNodes) {
                if (connectedNode.ipAddress.equals(connectedNodeIp)) {
                    if (connectedNode.id == UNKNOWN_DEVICE_ID) {
                        return this.convertDataToView(new String[]{}, UNKNOWN_DEVICE_VALUE, UNKNOWN_DEVICE_VALUE, connectedNode.macAddress);
                    } else if (connectedNode.id == MANAGER_DEVICE_ID) {
                        return this.convertDataToView(new String[]{}, SNMP_MANAGER_VALUE, SNMP_MANAGER_VALUE, connectedNode.macAddress);
                    } else {
                        Device device = DataManager.getInstance().getDeviceManager().getDevice(connectedNode.id);
                        return this.convertDataToView(new String[]{}, device.getName(), device.getLabel(), connectedNode.macAddress);
                    }
                }
            }
            return null;
        }

        private List<Object> convertDataToView(String[] ipAddresses, String name, String label, String macAddress) {
            List<Object> result = new ArrayList<Object>();
            result.add(ConnectedNodeDataOrders.IP_ADDRESSES.getValue(), ipAddresses);
            result.add(ConnectedNodeDataOrders.NAME.getValue(), name);
            result.add(ConnectedNodeDataOrders.LABEL.getValue(), label);
            result.add(ConnectedNodeDataOrders.MAC_ADDRESS.getValue(), macAddress);
            return result;
        }

        public void displayAllConnectedNodes() {
            for (ConnectedNodeData connectedNode : this.connectedNodes) {
                System.out.println("ID " + connectedNode.id + " IP " + connectedNode.ipAddress + " MAC " + connectedNode.macAddress);
            }
        }

    }

    public class ConnectedNodeData {

        private int id;
        private String ipAddress;
        private String networkIp;
        private String macAddress;

        public ConnectedNodeData(int id, String ipAddress, String networkIp, String macAddress) {
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

    public enum InterfaceDynamicDataOrders {
        IP_ADDRESS(0),
        NETMASK(1),
        MTU(2),
        INPACKETS(3),
        OUTPACKETS(4),
        INDISCARDS(5),
        OUTDISCARDS(6);

        private int value;

        private InterfaceDynamicDataOrders(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public enum ConnectedNodeDataOrders {
        IP_ADDRESSES(0),
        NAME(1),
        LABEL(2),
        MAC_ADDRESS(3);

        private int value;

        private ConnectedNodeDataOrders(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }
}
