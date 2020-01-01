/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.snmpd.helpers;

import graduationproject.controllers.DeviceManagementController;
import graduationproject.snmpd.SnmpManager;
import graduationproject.snmpd.callbacks.DeviceResourceQueryCallbackStage1;
import graduationproject.snmpd.callbacks.PushDeviceInfoCallbackStage1;
import graduationproject.snmpd.callbacks.QueryGetNextCallback;
import graduationproject.snmpd.callbacks.QueryWalkCallback;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.soulwing.snmp.SnmpContext;
import org.soulwing.snmp.SnmpTarget;
import org.soulwing.snmp.TimeoutException;
import org.soulwing.snmp.VarbindCollection;

/**
 *
 * @author cloud
 */
public class DeviceQueryHelper {

    public static final String DEVICE_ID_SEP = ":_:";
    public static final String[] processorLoadTable = {"sysUpTime", "hrProcessorFrwID", "hrProcessorLoad"};
    public static final String[] deviceTable = {"sysUpTime", "hrDeviceType", "hrDeviceDescr", "hrDeviceID"};
    private static final String CPU_DEVICE_TYPEOID = "1.3.6.1.2.1.25.3.1.3";
    public static final String[] memoryTable = {"sysUpTime", "hrStorageType", "hrStorageDescr", "hrStorageSize", "hrStorageUsed"};

    public void startTemplateQuery(TemplateQuery query) {
        if (query.isTable) {
            this.startTemplateWalkQuery(query);
        } else {
            this.startTemplateGetNextQuery(query);
        }
    }

    private void startTemplateGetNextQuery(TemplateQuery query) {
        QueryGetNextCallback queryCallback = new QueryGetNextCallback(query);
        SnmpContext context = SnmpManager.getInstance().createContext(
                SnmpManager.SnmpVersion.VERSION_2_COMMUNITY.getValue(), query.ipAddress, query.port, query.community);
        context.asyncGetNext(queryCallback, query.itemList);
    }

    private void startTemplateWalkQuery(TemplateQuery query) {
        QueryWalkCallback queryCallback = new QueryWalkCallback(query);
        SnmpContext context = SnmpManager.getInstance().createContext(
                SnmpManager.SnmpVersion.VERSION_2_COMMUNITY.getValue(), query.ipAddress, query.port, query.community);

        List<String> nonRepeater = new ArrayList<String>();
        nonRepeater.add("sysUpTime");
        context.asyncWalk(queryCallback, nonRepeater, query.itemList);
    }

    //NOTE: identification = name + label    
    public String[] getDeviceIdentification(SnmpTarget target) throws TimeoutException {
        String objName = "sysName";
        String retrievedData = null;

        SnmpContext queryContext = SnmpManager.getInstance().createContext(target);
        VarbindCollection varbind = queryContext.getNext(objName).get();
        retrievedData = varbind.get(objName).asString();

        String[] result = new String[DataOrders.END.getValue()];
        int sepPosition = retrievedData.lastIndexOf(this.DEVICE_ID_SEP);
        if (sepPosition >= 0) {
            result[DataOrders.DEVICE_NAME.getValue()] = retrievedData.substring(0, sepPosition);
            result[DataOrders.DEVICE_LABEL.getValue()] = retrievedData.substring(sepPosition + this.DEVICE_ID_SEP.length());
        } else {
            result[DataOrders.DEVICE_NAME.getValue()] = retrievedData;
            result[DataOrders.DEVICE_LABEL.getValue()] = retrievedData;
        }

        return result;
    }

    //push other device info and get device description
    public void pushInfoIntoDevice(String ipAddress, int port, String community, int deviceId, String name, String label, String location, String userInfo) {
        SnmpContext snmpContext = SnmpManager.getInstance()
                .createContext(SnmpManager.SnmpVersion.VERSION_2_COMMUNITY.getValue(), ipAddress, port, community);
        this.pushInfoIntoDevice(snmpContext, deviceId, name, label, location, userInfo);
    }

    public void pushInfoIntoDevice(SnmpContext snmpContext, int deviceId, String name, String label, String location, String userInfo) {
        String[] getObjects = {"sysName", "sysLocation", "sysDescr", "sysContact"};
        try {
            System.out.println("START PUSHING DATA INTO DEVICE");
            String deviceName = name + DEVICE_ID_SEP + label;
            PushDeviceInfoCallbackStage1 stage1Callback = new PushDeviceInfoCallbackStage1(deviceId, deviceName, location, userInfo);
            snmpContext.asyncGetNext(stage1Callback, getObjects);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] getDeviceIdentification(SnmpTarget target, String community) throws TimeoutException {
        return this.getDeviceIdentification(SnmpManager.getInstance()
                .createTarget(SnmpManager.SnmpVersion.VERSION_2_COMMUNITY, target.getAddress(), target.getPort(), community));
    }

    public void startQueryDeviceResource(int deviceId, SnmpContext context) {
        DeviceResourceDataCollector dataCollector = new DeviceResourceDataCollector(deviceId);
        DeviceResourceQueryCallbackStage1 stage1Callback = new DeviceResourceQueryCallbackStage1(dataCollector);
        context.asyncWalk(stage1Callback, 1, processorLoadTable);
    }

    public enum DataOrders {
        DEVICE_NAME(0),
        DEVICE_LABEL(1),
        END(2);

        private int value;

        private DataOrders(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public enum MemoryType {
        RAM(0, "1.3.6.1.2.1.25.2.1.2", "RAM"),
        VIRTUAL(1, "1.3.6.1.2.1.25.2.1.3", "VIRTUAL"),
        OTHER(2, "1.3.6.1.2.1.25.2.1.1", "OTHER"),
        DISK(3, "1.3.6.1.2.1.25.2.1.4", "DISK");

        private String typeOid;
        private String displayType;
        private int listPosition;

        private MemoryType(int listPosition, String typeOid, String displayType) {
            this.typeOid = typeOid;
            this.listPosition = listPosition;
            this.displayType = displayType;
        }

        public int getListPosition() {
            return this.listPosition;
        }

        public String getTypeOid() {
            return typeOid;
        }

        public String getDisplayType() {
            return displayType;
        }

        public static String convertTypeOidToDisplayType(String typeOid) {
            if (RAM.typeOid.equals(typeOid)) {
                return RAM.displayType;
            }
            if (VIRTUAL.typeOid.equals(typeOid)) {
                return VIRTUAL.displayType;
            }
            if (OTHER.typeOid.equals(typeOid)) {
                return OTHER.displayType;
            }
            return DISK.displayType;
        }
    }

    public static class ResponseDataProcessor {

        public void processGetNextData(Calendar receivedTime, TemplateQuery templateQuery, VarbindCollection varbinds) {
            try {
                templateQuery.setReceivedTime(receivedTime);
                int tempSize = templateQuery.itemList.size();

                for (int i = 0; i < tempSize; i++) {
                    templateQuery.result.add(varbinds.get(templateQuery.itemList.get(i)).asString());
                }

                DeviceManagementController deviceController = new DeviceManagementController();
                deviceController.processCompletedTemplateQuery(templateQuery);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void processWalkData(Calendar receivedTime, TemplateQuery templateQuery, List<VarbindCollection> varbindsList) {
            try {
                templateQuery.setReceivedTime(receivedTime);
                int tempSize = templateQuery.itemList.size();

                for (VarbindCollection varbinds : varbindsList) {
                    System.out.println(varbinds.toString());
                    Object[] objects = new Object[tempSize];
                    for (int i = 0; i < tempSize; i++) {
                        objects[i] = varbinds.get(templateQuery.itemList.get(i)).asString();
                    }
                    templateQuery.result.add(objects);
                }
                DeviceManagementController deviceController = new DeviceManagementController();
                deviceController.processCompletedTemplateQuery(templateQuery);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class DeviceResourceDataCollector {

        private int deviceId;
        private List<DeviceCpuData> deviceCpuData;
        private List<DeviceMemoryData> deviceMemoryData;

        public DeviceResourceDataCollector(int deviceId) {
            this.deviceId = deviceId;
            this.deviceCpuData = new ArrayList<DeviceCpuData>();

            this.deviceMemoryData = new ArrayList<DeviceMemoryData>();
            this.deviceMemoryData.add(MemoryType.RAM.getListPosition(), new DeviceMemoryData(MemoryType.RAM.getDisplayType()));
            this.deviceMemoryData.add(MemoryType.VIRTUAL.getListPosition(), new DeviceMemoryData(MemoryType.VIRTUAL.getDisplayType()));
            this.deviceMemoryData.add(MemoryType.OTHER.getListPosition(), new DeviceMemoryData(MemoryType.OTHER.getDisplayType()));
        }

        public void processCPUData(VarbindCollection varbindCol) {
            this.deviceCpuData.add(new DeviceCpuData(
                    varbindCol.get("hrProcessorFrwID").asString(),
                    varbindCol.get("hrProcessorLoad").asInt())
            );
        }

        public void processDeviceData(VarbindCollection varbindCol) {
            int tempSize = this.deviceCpuData.size();
            int i = 0;

            if (varbindCol.get("hrDeviceType").asString().equals(CPU_DEVICE_TYPEOID)) {
                for (i = 0; i < tempSize; i++) {
                    DeviceCpuData temp = this.deviceCpuData.get(i);
                    if (temp.description == null && temp.firmwareId.equals(varbindCol.get("hrDeviceID").asString())) {
                        temp.description = varbindCol.get("hrDeviceDescr").asString();
                        break;
                    }
                }
            }
        }

        public void processMemoryData(VarbindCollection varbindCol) {
            int limit = MemoryType.DISK.getListPosition();
            int i = 0;
            for (i = 0; i < limit; i++) {
                DeviceMemoryData temp = this.deviceMemoryData.get(i);
                if (temp.type.equals(MemoryType.convertTypeOidToDisplayType(varbindCol.get("hrStorageType").asString()))) {
                    temp.addTotalSize(varbindCol.get("hrStorageSize").asLong());
                    temp.addUsedSize(varbindCol.get("hrStorageUsed").asLong());
                    break;
                }
            }
            if (i == limit) {
                this.deviceMemoryData.add(new DeviceMemoryData(
                        MemoryType.convertTypeOidToDisplayType(varbindCol.get("hrStorageType").asString()),
                        varbindCol.get("hrStorageDescr").asString(),
                        varbindCol.get("hrStorageSize").asLong(),
                        varbindCol.get("hrStorageUsed").asLong()));
            }
        }

        public int getDeviceId() {
            return deviceId;
        }

        public List<DeviceCpuData> getDeviceCpuData() {
            return deviceCpuData;
        }

        public List<DeviceMemoryData> getDeviceMemoryData() {
            return deviceMemoryData;
        }

    }

    public static class DeviceCpuData {

        private String firmwareId;
        private String description;
        private int load;

        public DeviceCpuData(String firmwareId, int load) {
            this.firmwareId = firmwareId;
            this.load = load;
            this.description = null;
        }

        public String getFirmwareId() {
            return firmwareId;
        }

        public String getDescription() {
            return description;
        }

        public int getLoad() {
            return load;
        }
    }

    public static class DeviceMemoryData {

        private String type;
        private String description;
        private long totalSize;
        private long usedSize;

        public DeviceMemoryData(String type) {
            this.type = type;
            this.description = type;

            this.totalSize = 0;
            this.usedSize = 0;
        }

        public DeviceMemoryData(String type, String description, long totalSize, long usedSize) {
            this.type = type;
            this.description = description;
            this.totalSize = totalSize;
            this.usedSize = usedSize;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void addTotalSize(long amount) {
            this.totalSize += amount;
        }

        public void addUsedSize(long amount) {
            this.usedSize += amount;
        }

        public String getType() {
            return type;
        }

        public String getDescription() {
            return description;
        }

        public long getTotalSize() {
            return totalSize;
        }

        public long getUsedSize() {
            return usedSize;
        }

    }

    public static class TemplateQuery {

        private int deviceId;
        private String ipAddress;
        private int port;
        private String community;
        private int templateId;
        private List<String> itemList;
        private boolean isTable;

        private Calendar receivedTime;
        private List<Object> result;

        public TemplateQuery(int deviceId, String ipAddress, int port, String community, 
                int templateId, List<String> itemList, boolean isTable) {
            this.deviceId = deviceId;
            this.ipAddress = ipAddress;
            this.port = port;
            this.community = community;
            this.templateId = templateId;
            this.itemList = itemList;
            this.isTable = isTable;

            this.receivedTime = null;
            this.result = new ArrayList<Object>();
        }
        
        public int getDeviceId() {
            return deviceId;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public String getCommunity() {
            return community;
        }

        public int getTemplateId() {
            return templateId;
        }

        public List<String> getItemList() {
            return itemList;
        }

        public boolean isIsTable() {
            return isTable;
        }

        public List<Object> getResult() {
            return result;
        }

        public Calendar getReceivedTime() {
            return receivedTime;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
        
        public void setReceivedTime(Calendar receivedTime) {
            this.receivedTime = receivedTime;
        }        
    }
}
