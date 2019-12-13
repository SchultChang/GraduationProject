/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.helpers;

import graduationproject.helpers.ActiveDeviceDataCollector.ActiveDeviceData;
import graduationproject.helpers.ActiveDeviceDataCollector.NextNodeTopoData;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author cloud
 */
public class TopoDrawer {

    public enum NodeTypes {
        MANAGER,
        IMPORTED,
        UNKNOWN
    }

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
    private int importedSize, unknownSize;

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
    }

    public void prepareTopo() {
        this.managerDevice = ActiveDeviceDataCollector.getInstance().getManagerDevice();
        this.importedDevices = ActiveDeviceDataCollector.getInstance().getImportedDevices();
        this.unknownDevices = ActiveDeviceDataCollector.getInstance().getUnknownDevices();
        this.importedSize = this.importedDevices.size();
        this.unknownSize = this.unknownDevices.size();

        this.connectionList.clear();
        this.topoNodes.clear();
        System.gc();
        boolean[] isAdded = new boolean[1 + importedSize + unknownSize];
        Arrays.fill(isAdded, false);

        isAdded[0] = true;
        this.topoNodes.add(new TopoNodeData(0, -1));
        int left = 0, right = 0, current = 0;

        List<NextNodeTopoData> nextNodes;
        while (left <= right) {
            nextNodes = this.getDeviceForTopoId(this.topoNodes.get(left).id).getAllNextNodes();

            for (NextNodeTopoData nextNode : nextNodes) {
                int topoId = this.getTopoIdForData(nextNode);
                if (topoId != -1 && !isAdded[topoId]) {
                    isAdded[topoId] = true;

                    right++;
                    current = right;
                    this.topoNodes.add(new TopoNodeData(topoId, left));
                } else {
                    current = this.findNodeListId(topoId);
                }
                this.addConnection(left, current);
            }

            left++;
        }
    }

    private ActiveDeviceData getDeviceForTopoId(int nodeId) {
        if (nodeId == 0) {
            return this.managerDevice;
        }

        if (1 <= nodeId && nodeId <= this.importedSize) {
            return this.importedDevices.get(nodeId - 1);
        }

        if (this.importedSize < nodeId) {
            return this.unknownDevices.get(nodeId - this.importedSize - 1);
        }

        return null;
    }

    private int getTopoIdForData(NextNodeTopoData nextNode) {
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
            if (this.topoNodes.get(i).id == topoId) {
                return i;
            }
        }
        return -1;
    }

    public void addConnection(int id1, int id2) {
        boolean isExisted = false;
        for (int[] connection : this.connectionList) {
            if ((this.topoNodes.get(connection[0]).id == this.topoNodes.get(id1).id
                    && this.topoNodes.get(connection[1]).id == this.topoNodes.get(id2).id)
                    || (this.topoNodes.get(connection[0]).id == this.topoNodes.get(id2).id
                    && this.topoNodes.get(connection[1]).id == this.topoNodes.get(id1).id)) {
                isExisted = true;
                break;
            }
        }
        if (!isExisted) {
            this.connectionList.add(new int[]{id1, id2});
        }
    }

    public void calculateTopo() {
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
        this.prepareTopo();
        this.calculateTopo();
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
            return new int[] {x1, y1, x2, y2};
        }
        return null;
    }

    public List<TopoNodeData> getTopoNodes() {
        return topoNodes;
    }

    public List<int[]> getConnectionList() {
        return connectionList;
    }

    public class TopoNodeData {

        private int id;
        private int preListId;
        private int x;
        private int y;
        private int angle;

        public TopoNodeData(int id, int preListId) {
            this.preListId = preListId;
            this.id = id;
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
            if (this.id == 0) {
                return NodeTypes.MANAGER;
            }
            if (1 <= this.id && this.id <= importedSize) {
                return NodeTypes.IMPORTED;
            }
            return NodeTypes.UNKNOWN;
        }

        public int getDeviceId() {
            ActiveDeviceData device = TopoDrawer.this.getDeviceForTopoId(id);
            return device.getId();
        }

        public String getDeviceLabel() {
            ActiveDeviceData device = TopoDrawer.this.getDeviceForTopoId(this.id);
            return device.getLabel();
        }

        public void displayInfo() {
            System.out.println("NODE TOPO ID: " + this.id + " NODE PRE LIST ID: " + this.preListId);
            System.out.println("NODE X:" + this.x + " NODE Y:" + this.y + " NODE ANGLE: " + this.angle);
        }
    }
}
