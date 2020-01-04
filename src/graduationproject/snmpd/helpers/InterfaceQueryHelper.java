/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.snmpd.helpers;

import graduationproject.controllers.InterfaceManagementController;
import graduationproject.controllers.InterfaceManagementController.DataOrders;
import graduationproject.helpers.AddressParser;
import graduationproject.snmpd.callbacks.InterfaceQueryCallbackStage1;
import java.util.ArrayList;
import java.util.List;
import org.soulwing.snmp.SnmpContext;
import org.soulwing.snmp.VarbindCollection;

/**
 *
 * @author cloud
 */
public class InterfaceQueryHelper {

    public static final String[] ifTable = {"sysUpTime", "ifIndex", "ifOperStatus",
        "ifDescr", "ifPhysAddress", "ifType", "ifMtu", "ifSpeed",
        "ifInUcastPkts", "ifInNUcastPkts", "ifOutUcastPkts", "ifOutNUcastPkts",
        "ifInOctets", "ifOutOctets", "ifInDiscards", "ifOutDiscards"};
    public static final String[] ipAddrTable = {"sysUpTime", "ipAdEntAddr", "ipAdEntIfIndex", "ipAdEntNetMask"};
    public static final String[] ipNetToMediaTable = {"sysUpTime", "ipNetToMediaIfIndex", "ipNetToMediaPhysAddress", "ipNetToMediaNetAddress"};

    public void startQueryAllInterfaces(int deviceId, SnmpContext context) {
        InterfaceDataCollector dataCollector = new InterfaceDataCollector(deviceId);
        InterfaceQueryCallbackStage1 stage1Callback = new InterfaceQueryCallbackStage1(dataCollector);
        context.asyncWalk(stage1Callback, 1, ifTable);
    }

    public class InterfaceDataCollector {

        private int deviceId;
        private List<InterfaceRawData> interfaces;

        public InterfaceDataCollector(int deviceId) {
            this.deviceId = deviceId;
            this.interfaces = new ArrayList<InterfaceRawData>();
        }

        public void addIfTableEntryData(VarbindCollection varbinds) {
            InterfaceRawData newInterface = new InterfaceRawData();
            newInterface.parseIfTable(varbinds);
            this.interfaces.add(newInterface);
        }

        public void addIpAddrTableEntryData(VarbindCollection varbinds) {
            int tempSize = this.interfaces.size();
            for (int i = 0; i < tempSize; i++) {
                if (this.interfaces.get(i).parseIpAddrTable(varbinds)) {
                    break;
                }
            }
        }

        public void addIpNetToMediaTableEntryData(VarbindCollection varbinds) {
            int tempSize = this.interfaces.size();
            for (int i = 0; i < tempSize; i++) {
                if (this.interfaces.get(i).parseIpNetToMediaTable(varbinds)) {
                    break;
                }
            }
        }

        public void processCollectedData() {
            InterfaceManagementController interfaceController = new InterfaceManagementController();
            interfaceController.processCollectedData(deviceId, interfaces);
        }
    }

    public class InterfaceRawData {

        private int index;
        private int operStatus;
        private String name;
        private String macAddress;
        private String type;
        private long mtu;
        private long speed;
        private long inUCast;
        private long inNUCast;
        private long outUCast;
        private long outNUCast;
        private long inBytes;
        private long outBytes;
        private long inDiscard;
        private long outDiscard;

        private String ipAddress;
        private String netmask;

        private List<String> connectedNodeMacs;
        private List<String> connectedNodeIPs;

        public InterfaceRawData() {
            this.connectedNodeIPs = new ArrayList<String>();
            this.connectedNodeMacs = new ArrayList<String>();
        }

        public void parseIfTable(VarbindCollection varbind) {
            this.index = varbind.get("ifIndex").asInt();
            this.operStatus = varbind.get("ifOperStatus").asInt();
            this.name = varbind.get("ifDescr").asString();
            this.macAddress = AddressParser.normalizeMac(varbind.get("ifPhysAddress").asString());
            this.type = varbind.get("ifType").asString();
            this.mtu = varbind.get("ifMtu").asLong();
            this.speed = varbind.get("ifSpeed").asLong();
            this.inUCast = varbind.get("ifInUcastPkts").asLong();
            this.inNUCast = varbind.get("ifInNUcastPkts").asLong();
            this.outUCast = varbind.get("ifOutUcastPkts").asLong();
            this.outNUCast = varbind.get("ifOutNUcastPkts").asLong();
            this.inBytes = varbind.get("ifInOctets").asLong();
            this.outBytes = varbind.get("ifOutOctets").asLong();
            this.inDiscard = varbind.get("ifInDiscards").asLong();
            this.outDiscard = varbind.get("ifOutDiscards").asLong();
        }

        public boolean parseIpAddrTable(VarbindCollection varbind) {
            if (this.index != varbind.get("ipAdEntIfIndex").asInt()) {
                return false;
            }

            this.ipAddress = varbind.get("ipAdEntAddr").asString();
            this.netmask = varbind.get("ipAdEntNetMask").asString();
            return true;
        }

        public boolean parseIpNetToMediaTable(VarbindCollection varbind) {
            if (this.index != varbind.get("ipNetToMediaIfIndex").asInt()) {
                return false;
            }

            this.connectedNodeMacs.add(AddressParser.normalizeMac(varbind.get("ipNetToMediaPhysAddress").asString()));
            this.connectedNodeIPs.add(varbind.get("ipNetToMediaNetAddress").asString());
            return true;
        }

        public int getIndex() {
            return index;
        }

        public int getOperStatus() {
            return operStatus;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return this.type;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public String getNetmask() {
            return netmask;
        }

        public long getMtu() {
            return mtu;
        }

        public long getInDiscards() {
            return inDiscard;
        }

        public long getOutDiscards() {
            return outDiscard;
        }

        public long getInPackets() {
            return this.inNUCast + this.inUCast;
        }
        
        public long getOutPackets() {
            return this.outNUCast + this.outUCast;
        }
        
        public long getBandwidth() {
            return this.speed;
        }
        
        public long getInBytes() {
            return this.inBytes;
        }
        
        public long getOutBytes() {
            return this.outBytes;
        }
        
        public String getMacAddress() {
            return this.macAddress;
        }

        public List<String> getConnectedNodeMacs() {
            return connectedNodeMacs;
        }

        public List<String> getConnectedNodeIPs() {
            return connectedNodeIPs;
        }
        
        public List<Object> getDynamicData() {
            List<Object> result = new ArrayList<Object>();
            result.add(DataOrders.IP_ADDRESS.getValue(), this.ipAddress);
            result.add(DataOrders.NETMASK.getValue(), this.netmask);
            result.add(DataOrders.MTU.getValue(), this.mtu);
            result.add(DataOrders.BANDWIDTH.getValue(), this.speed);
            result.add(DataOrders.IN_PACK_NUMBER.getValue(), this.inUCast + this.inNUCast);
            result.add(DataOrders.OUT_PACK_NUMBER.getValue(), this.outUCast + this.outNUCast);
            result.add(DataOrders.IN_BYTES.getValue(), this.inBytes);
            result.add(DataOrders.OUT_BYTES.getValue(), this.outBytes);
            result.add(DataOrders.IN_DISCARD_PACK_NUMBER.getValue(), this.inDiscard);
            result.add(DataOrders.OUT_DISCARD_PACK_NUMBER.getValue(), this.outDiscard);
            return result;
        }
    }
}
