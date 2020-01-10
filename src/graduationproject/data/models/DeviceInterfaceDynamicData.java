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
    private float bandwidth;
    private float inboundBytes;
    private float outboundBytes;
    private Calendar updatedTime;
    private boolean isSummarized;
    
    private DeviceNetworkInterface networkInterface; 
    
    public DeviceInterfaceDynamicData() {
    }

    public DeviceInterfaceDynamicData(float bandwidth, float inboundBytes, float outboundBytes, Calendar updatedTime, DeviceNetworkInterface networkInterface) {
        this.bandwidth = bandwidth;
        this.inboundBytes = inboundBytes;
        this.outboundBytes = outboundBytes;
        this.updatedTime = (Calendar) updatedTime.clone();
        this.networkInterface = networkInterface;
        
        this.isSummarized = false;
    }
    
    public DeviceInterfaceDynamicData(float bandwidth, float inboundBytes, float outboundBytes, Calendar updatedTime, boolean isSummarized, DeviceNetworkInterface networkInterface) {
        this.bandwidth = bandwidth;
        this.inboundBytes = inboundBytes;
        this.outboundBytes = outboundBytes;
        this.updatedTime = (Calendar) updatedTime.clone();
        this.networkInterface = networkInterface;
        
        this.isSummarized = isSummarized;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(float bandwidth) {
        this.bandwidth = bandwidth;
    }

    public float getInboundBytes() {
        return inboundBytes;
    }

    public void setInboundBytes(float inboundBytes) {
        this.inboundBytes = inboundBytes;
    }

    public float getOutboundBytes() {
        return outboundBytes;
    }

    public void setOutboundBytes(float outboundBytes) {
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

    public boolean isIsSummarized() {
        return isSummarized;
    }

    public void setIsSummarized(boolean isSummarized) {
        this.isSummarized = isSummarized;
    }
    
    public void displayInfo() {
        System.out.println(this.networkInterface.getId());
        System.out.println(this.outboundBytes);
    }
    
}
