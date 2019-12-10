/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.models;

import graduationproject.controllers.InterfaceManagementController;
import graduationproject.controllers.InterfaceManagementController.DataOrders;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author cloud
 */
public class DeviceInterfaceDynamicData {
    private int id;
    private String ipAddress;
    private String netmask;
    private long mtu;
    private long bandwidth;
    private long inboundPacketNumber;
    private long outboundPacketNumber;
    private long inboundBytes;
    private long outboundBytes;
    private long inboundDiscardPacketNumber;
    private long outboundDiscardPacketNumber;
    
    private String nextNodeName;
    private String nextNodeLabel;
    private String nextNodeIPAddress;
    private String nextNodeMacAddress;
    
    private Calendar updatedTime;
    
    private DeviceNetworkInterface networkInterface; 
    
    public DeviceInterfaceDynamicData() {
    }
    
    public DeviceInterfaceDynamicData(List<Object> data, Calendar updatedTime, DeviceNetworkInterface networkInterface) {
        this.ipAddress = (String) data.get(DataOrders.IP_ADDRESS.getValue());
        this.netmask = (String) data.get(DataOrders.NETMASK.getValue());
        this.mtu = (long) data.get(DataOrders.MTU.getValue());
        this.bandwidth = (long) data.get(DataOrders.BANDWIDTH.getValue());
       
        this.inboundPacketNumber = (long) data.get(DataOrders.IN_PACK_NUMBER.getValue());
        this.outboundPacketNumber = (long) data.get(DataOrders.OUT_PACK_NUMBER.getValue());
        this.inboundBytes = (long) data.get(DataOrders.IN_BYTES.getValue());
        this.outboundBytes = (long) data.get(DataOrders.OUT_BYTES.getValue());
        this.inboundDiscardPacketNumber = (long) data.get(DataOrders.IN_DISCARD_PACK_NUMBER.getValue());
        this.outboundDiscardPacketNumber = (long) data.get(DataOrders.OUT_DISCARD_PACK_NUMBER.getValue());
        
        this.nextNodeName = (String) data.get(DataOrders.NEXT_NODE_NAME.getValue());
        this.nextNodeLabel = (String) data.get(DataOrders.NEXT_NODE_LABEL.getValue());
        this.nextNodeIPAddress = (String) data.get(DataOrders.NEXT_NODE_IP_ADDRESS.getValue());
        this.nextNodeMacAddress = (String) data.get(DataOrders.NEXT_NODE_MAC_ADDRESS.getValue());

        this.updatedTime = updatedTime;
        
        this.networkInterface = networkInterface;
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getNetmask() {
        return netmask;
    }

    public void setNetmask(String netmask) {
        this.netmask = netmask;
    }

    public long getMtu() {
        return mtu;
    }

    public void setMtu(long mtu) {
        this.mtu = mtu;
    }

    public long getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(long bandwidth) {
        this.bandwidth = bandwidth;
    }

    public long getInboundPacketNumber() {
        return inboundPacketNumber;
    }

    public void setInboundPacketNumber(long inboundPacketNumber) {
        this.inboundPacketNumber = inboundPacketNumber;
    }

    public long getOutboundPacketNumber() {
        return outboundPacketNumber;
    }

    public void setOutboundPacketNumber(long outboundPacketNumber) {
        this.outboundPacketNumber = outboundPacketNumber;
    }

    public long getInboundBytes() {
        return inboundBytes;
    }

    public void setInboundBytes(long inboundBytes) {
        this.inboundBytes = inboundBytes;
    }

    public long getOutboundBytes() {
        return outboundBytes;
    }

    public void setOutboundBytes(long outboundBytes) {
        this.outboundBytes = outboundBytes;
    }

    public long getInboundDiscardPacketNumber() {
        return inboundDiscardPacketNumber;
    }

    public void setInboundDiscardPacketNumber(long inboundDiscardPacketNumber) {
        this.inboundDiscardPacketNumber = inboundDiscardPacketNumber;
    }

    public long getOutboundDiscardPacketNumber() {
        return outboundDiscardPacketNumber;
    }

    public void setOutboundDiscardPacketNumber(long outboundDiscardPacketNumber) {
        this.outboundDiscardPacketNumber = outboundDiscardPacketNumber;
    }

    public String getNextNodeName() {
        return nextNodeName;
    }

    public void setNextNodeName(String nextNodeName) {
        this.nextNodeName = nextNodeName;
    }

    public String getNextNodeLabel() {
        return nextNodeLabel;
    }

    public void setNextNodeLabel(String nextNodeLabel) {
        this.nextNodeLabel = nextNodeLabel;
    }

    public String getNextNodeIPAddress() {
        return nextNodeIPAddress;
    }

    public void setNextNodeIPAddress(String nextNodeIPAddress) {
        this.nextNodeIPAddress = nextNodeIPAddress;
    }

    public String getNextNodeMacAddress() {
        return nextNodeMacAddress;
    }

    public void setNextNodeMacAddress(String nextNodeMacAddress) {
        this.nextNodeMacAddress = nextNodeMacAddress;
    }

    public DeviceNetworkInterface getNetworkInterface() {
        return networkInterface;
    }

    public void setNetworkInterface(DeviceNetworkInterface networkInterface) {
        this.networkInterface = networkInterface;
    }

    public Calendar getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Calendar updatedTime) {
        this.updatedTime = updatedTime;
    }
   
    
}
