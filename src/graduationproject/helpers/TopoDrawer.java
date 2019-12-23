/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.helpers;

import graduationproject.data.ActiveDeviceDataCollector;
import graduationproject.gui.ApplicationWindow;
import graduationproject.data.ActiveDeviceDataCollector.ActiveDeviceData;
import graduationproject.data.ActiveDeviceDataCollector.ConnectedNodeData;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author cloud
 */
public class TopoDrawer {

    public enum NodeTypes {
        MANAGER,
        IMPORTED,
        UNKNOWN,
        VS
    }

    public int redrawTimerCounter;

    public static final int VS_DEVICE_ID = -3;
    private final String VS_DEVICE_LABEL = "vs";

    private final int MAX_REDRAWING_TIMER_COUNT_VALUE = 4;
    private final int MANAGER_X = 800;
    private final int MANAGER_Y = 100;
    private final int MANAGER_ANGLE = 90;
    private final int DEFAULT_ANGLE_LIMIT = 180;
//    private final int MANAGER_ANGLE_LIMIT = 360;
    private final int DISTANCE_BETWEEN_NODE = 100;
    private final int RADIUS_OF_NODE = 20;

    private List<TopoNodeData> topoNodes;
    private List<int[]> connectionList;                                         //each element of it is a pair of node list ids

    private ActiveDeviceData managerDevice;                                     //topo id = 0
    private List<ActiveDeviceData> importedDevices;                             //topo id = 1 + list id
    private List<ActiveDeviceData> unknownDevices;                              //topo id = 1 + size of imported list + list id
    private List<VirtualSwitch> vsDevices;                                      //topo id = 1 + size of imported list + unknown size + list id

    private int importedSize, unknownSize, vsSize;
    private static TopoDrawer instance;

    public static TopoDrawer getInstance() {
        if (instance == null) {
            instance = new TopoDrawer();
        }
        return instance;
    }

    private TopoDrawer() {
        this.connectionList = new ArrayList<int[]>();
        this.topoNodes = new ArrayList<TopoNodeData>();
        this.vsDevices = new ArrayList<VirtualSwitch>();

        this.redrawTimerCounter = 0;
    }

    public void checkRedrawingTopo() {
        if (this.redrawTimerCounter == MAX_REDRAWING_TIMER_COUNT_VALUE) {
            if (ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices().getPanelTopology().checkRedrawing()) {
                ActiveDeviceDataCollector.getInstance().newDataForTopo = false;
            }
            this.redrawTimerCounter = 0;
        } else if (ActiveDeviceDataCollector.getInstance().newDataForTopo) {
            this.redrawTimerCounter++;
        }
    }

    private void prepareTopoData() {                                            //merge connections into a network and create virtual switch
        this.connectionList.clear();
        this.topoNodes.clear();

        this.managerDevice = ActiveDeviceDataCollector.getInstance().getManagerDevice();
        this.importedDevices = ActiveDeviceDataCollector.getInstance().getImportedDevices();
        this.unknownDevices = ActiveDeviceDataCollector.getInstance().getUnknownDevices();

        this.importedSize = this.importedDevices.size();
        this.unknownSize = this.unknownDevices.size();

        this.vsDevices.clear();
        this.createVirtualSwitchForDevice(0, this.managerDevice.getNetworkWithManyConnectedNodes());
        for (int i = 0; i < this.importedSize; i++) {
            this.createVirtualSwitchForDevice(1 + i, this.importedDevices.get(i).getNetworkWithManyConnectedNodes());
        }
        this.vsSize = this.vsDevices.size();

        //add unknown node
        for (int i = 0; i < this.unknownSize; i++) {
            for (VirtualSwitch vs : this.vsDevices) {
                if (this.unknownDevices.get(i).belongToNetwork(vs.networkAddress)) {
                    vs.nextNodeTopoIds.add(i + this.importedSize + 1);
                }
            }
        }

        for (VirtualSwitch vs : this.vsDevices) {
            vs.displayConnectedNodes();
        }
    }

    private void createVirtualSwitchForDevice(int topoId, List<String> networkAddresses) {
        int vsListId;
        for (String networkAddress : networkAddresses) {
            vsListId = this.findVsListId(networkAddress);
            if (vsListId == -1) {
                this.vsDevices.add(new VirtualSwitch(networkAddress, topoId));
            } else {
                this.vsDevices.get(vsListId).nextNodeTopoIds.add(topoId);
            }
        }
    }

    private int findVsListId(String networkAddress) {
        int tempSize = this.vsDevices.size();
        for (int i = 0; i < tempSize; i++) {
            if (this.vsDevices.get(i).networkAddress.equalsIgnoreCase(networkAddress)) {
                return i;
            }
        }
        return -1;
    }

    public boolean isConnectedToVs(String networkAddress) {
        for (VirtualSwitch vs : this.vsDevices) {
            if (vs.networkAddress.equals(networkAddress)) {
                return true;
            }
        }
        return false;
    }

    private void constructTopoModel() {
        System.gc();
        boolean[] isAdded = new boolean[1 + importedSize + unknownSize + this.vsSize];
        Arrays.fill(isAdded, false);

        isAdded[0] = true;
        this.topoNodes.add(new TopoNodeData(0, -1));
        int left = 0, right = 0, current = 0;

        List<ConnectedNodeData> nextNodes;
        List<Integer> topoIds;
        while (left <= right) {
            Object device = this.getDeviceForTopoId(this.topoNodes.get(left).topoId);
            if (this.topoNodes.get(left).topoId <= this.importedSize + this.unknownSize) {
                nextNodes = ((ActiveDeviceData) device).getAllConnectedNodes();
                topoIds = new ArrayList<Integer>();
                if (nextNodes != null) {
                    for (ConnectedNodeData nextNode : nextNodes) {
                        topoIds.add(this.getTopoIdForData(nextNode));
                    }
                }
            } else {
                topoIds = ((VirtualSwitch) device).nextNodeTopoIds;
            }
            
            for (Integer topoId : topoIds) {
                if (topoId != -1) {
                    if (!isAdded[topoId]) {
                        isAdded[topoId] = true;

                        right++;
                        current = right;
                        this.topoNodes.add(new TopoNodeData(topoId, left));
                    } else {
                        current = this.findNodeListId(topoId);
                    }
                    this.addConnection(left, current);
                }
            }
            left++;
        }

//        for (TopoNodeData topoNode : this.topoNodes) {
//            topoNode.displayInfo();
//        }
    }

    private Object getDeviceForTopoId(int nodeId) {
        if (nodeId == 0) {
            return this.managerDevice;
        }

        if (1 <= nodeId && nodeId <= this.importedSize) {
            return this.importedDevices.get(nodeId - 1);
        }

        if (this.importedSize < nodeId && nodeId <= this.importedSize + this.unknownSize) {
            return this.unknownDevices.get(nodeId - this.importedSize - 1);
        }

        if (this.importedSize + this.unknownSize < nodeId) {
            return this.vsDevices.get(nodeId - this.importedSize - this.unknownSize - 1);
        }

        return null;
    }

    private int getTopoIdForData(ConnectedNodeData nextNode) {
        if (nextNode.getId() == ActiveDeviceDataCollector.MANAGER_DEVICE_ID) {
            return 0;
        }

        if (nextNode.getId() == ActiveDeviceDataCollector.UNKNOWN_DEVICE_ID) {
            for (int i = 0; i < this.unknownSize; i++) {
                if (this.unknownDevices.get(i).containInterface(nextNode.getIpAddress(), nextNode.getMacAddress())) {
                    return this.importedSize + i + 1;
                }
            }
        }

        if (nextNode.getId() == VS_DEVICE_ID) {
            return this.findVsListId(nextNode.getNetworkIp()) + this.importedSize + this.unknownSize + 1;
        }

        for (int i = 0; i < this.importedSize; i++) {
            if (this.importedDevices.get(i).getId() == nextNode.getId()) {
                return i + 1;
            }
        }
        return -1;
    }

    private int findNodeListId(int topoId) {
        int tempSize = this.topoNodes.size();
        for (int i = 0; i < tempSize; i++) {
            if (this.topoNodes.get(i).topoId == topoId) {
                return i;
            }
        }
        return -1;
    }

    private void addConnection(int id1, int id2) {
        boolean isExisted = false;
        for (int[] connection : this.connectionList) {
            if ((this.topoNodes.get(connection[0]).topoId == this.topoNodes.get(id1).topoId
                    && this.topoNodes.get(connection[1]).topoId == this.topoNodes.get(id2).topoId)
                    || (this.topoNodes.get(connection[0]).topoId == this.topoNodes.get(id2).topoId
                    && this.topoNodes.get(connection[1]).topoId == this.topoNodes.get(id1).topoId)) {
                isExisted = true;
                break;
            }
        }
        if (!isExisted) {
            this.connectionList.add(new int[]{id1, id2});
        }
    }

    public void constructTopoView() {
        this.topoNodes.get(0).x = MANAGER_X;
        this.topoNodes.get(0).y = MANAGER_Y;
        this.topoNodes.get(0).angle = MANAGER_ANGLE;

        int left = 1;
        int right = 0;
        int tempSize = this.topoNodes.size();
        int anglePerNode = 0;

        while (left < tempSize) {
            right = left;
            while (right < tempSize && this.topoNodes.get(right).preListId == this.topoNodes.get(left).preListId) {
                right++;
            }

            if (right != left) {
//                if (this.topoNodes.get(left).preListId != 0) {
                anglePerNode = this.DEFAULT_ANGLE_LIMIT / (right - left + 1);
//                } else {
//                    anglePerNode = this.MANAGER_ANGLE_LIMIT / (right - left);
//                }
            }

            for (int i = left; i < right; i++) {
                this.calculatePosition(i - left, anglePerNode, this.topoNodes.get(i));
//                this.topoNodes.get(i).displayInfo();
            }
            left = right;
        }

    }

    private void calculatePosition(int childId, int anglePerNode, TopoNodeData currentNode) {
        TopoNodeData previous = this.topoNodes.get(currentNode.preListId);
        int posAngle = 0;
//        if (currentNode.preListId == 0) {
//            posAngle = childId * anglePerNode;
//            currentNode.angle = posAngle;
//        } else {
        currentNode.angle = previous.angle;
        posAngle = currentNode.angle - (this.DEFAULT_ANGLE_LIMIT / 2 - anglePerNode * (childId + 1));
//        }

        currentNode.x = (int) (previous.x + this.DISTANCE_BETWEEN_NODE * Math.cos(Math.toRadians(posAngle)));
        currentNode.y = (int) (previous.y + this.DISTANCE_BETWEEN_NODE * Math.sin(Math.toRadians(posAngle)));
    }

    public void constructTopo() {
        this.prepareTopoData();
        this.constructTopoModel();
        this.constructTopoView();
    }

    public int[] getConnectionCoordinates(int[] connection) {
//        return new int[]{this.topoNodes.get(connection[0]).x, this.topoNodes.get(connection[0]).y,
//            this.topoNodes.get(connection[1]).x, this.topoNodes.get(connection[1]).y};

        int a = this.topoNodes.get(connection[0]).x - this.topoNodes.get(connection[1]).x;
        int b = this.topoNodes.get(connection[0]).y - this.topoNodes.get(connection[1]).y;
        int value = (int) Math.sqrt(a * a + b * b);
        if (value != 0) {
            int x1 = this.topoNodes.get(connection[0]).x + this.RADIUS_OF_NODE * (-a) / value;
            int y1 = this.topoNodes.get(connection[0]).y + this.RADIUS_OF_NODE * (-b) / value;
            int x2 = this.topoNodes.get(connection[1]).x + this.RADIUS_OF_NODE * a / value;
            int y2 = this.topoNodes.get(connection[1]).y + this.RADIUS_OF_NODE * b / value;
            return new int[]{x1, y1, x2, y2};
        }
        return null;
    }

    public List<TopoNodeData> getTopoNodes() {
        return topoNodes;
    }

    public List<int[]> getConnectionList() {
        return connectionList;
    }
    
    public int getDeviceIdForNode(int nodeListId) {
        return this.topoNodes.get(nodeListId).getDeviceId();
    }

    public String getDeviceDefaultAddressForNode(int nodeListId) {
        return this.topoNodes.get(nodeListId).getDeviceDefaultAddress();
    }
    
    public class TopoNodeData {

        private int topoId;
        private int preListId;
        private int x;
        private int y;
        private int angle;

        public TopoNodeData(int topoId, int preListId) {
            this.preListId = preListId;
            this.topoId = topoId;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void setPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public NodeTypes getNodeType() {
            if (this.topoId == 0) {
                return NodeTypes.MANAGER;
            }
            if (0 < this.topoId && this.topoId <= importedSize) {
                return NodeTypes.IMPORTED;
            }
            if (importedSize < this.topoId && this.topoId <= importedSize + unknownSize) {
                return NodeTypes.UNKNOWN;
            }
            return NodeTypes.VS;
        }

        public int getDeviceId() {
            if (this.topoId <= importedSize + unknownSize) {
                ActiveDeviceData device = (ActiveDeviceData) TopoDrawer.this.getDeviceForTopoId(topoId);
                return device.getId();
            }
            return VS_DEVICE_ID;
        }

        public String getDeviceDefaultAddress() {
            if (this.topoId <= importedSize + unknownSize) {
                ActiveDeviceData device = (ActiveDeviceData) TopoDrawer.this.getDeviceForTopoId(topoId);
                return device.getDefaultAddress();
            }
            return null;
        }
        
        public String getDeviceLabel() {
            if (this.topoId <= importedSize + unknownSize) {
                ActiveDeviceData device = (ActiveDeviceData) TopoDrawer.this.getDeviceForTopoId(this.topoId);
                return device.getLabel();
            }
            return VS_DEVICE_LABEL;
        }

    }

    public class VirtualSwitch {

        private String networkAddress;
        private List<Integer> nextNodeTopoIds;

        public VirtualSwitch(String networkAddress, int nextNodeTopoId) {
            this.networkAddress = networkAddress;
            this.nextNodeTopoIds = new ArrayList<Integer>();
            this.nextNodeTopoIds.add(nextNodeTopoId);
        }

        public void displayConnectedNodes() {
            System.out.println("VS: " + this.networkAddress);
            for (Integer value : this.nextNodeTopoIds) {
                System.out.println("VS - NEXT NODE : " + value);
            }
        }
    }
}
