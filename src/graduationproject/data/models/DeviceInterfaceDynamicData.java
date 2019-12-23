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
    
    private long bandwidth;
    private long inboundBytes;
    private long outboundBytes;
    
    private Calendar updatedTime;
    
    private DeviceNetworkInterface networkInterface; 
    
    public DeviceInterfaceDynamicData() {
    }

    public DeviceInterfaceDynamicData(long bandwidth, long inboundBytes, long outboundBytes, Calendar updatedTime, DeviceNetworkInterface networkInterface) {
        this.bandwidth = bandwidth;
        this.inboundBytes = inboundBytes;
        this.outboundBytes = outboundBytes;
        this.updatedTime = updatedTime;
        this.networkInterface = networkInterface;
    }
    
//    public DeviceInterfaceDynamicData(List<Object> data, Calendar updatedTime, DeviceNetworkInterface networkInterface) {
//        this.bandwidth = (long) data.get(DataOrders.BANDWIDTH.getValue());
//       
//        this.inboundBytes = (long) data.get(DataOrders.IN_BYTES.getValue());
//        this.outboundBytes = (long) data.get(DataOrders.OUT_BYTES.getValue());
//
//        this.updatedTime = updatedTime;
//        
//        this.networkInterface = networkInterface;
//    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(long bandwidth) {
        this.bandwidth = bandwidth;
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
