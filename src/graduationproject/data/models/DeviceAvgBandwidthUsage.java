/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.models;

import java.util.Calendar;

/**
 *
 * @author cloud
 */
public class DeviceAvgBandwidthUsage {

    private int id;
    private float avgUsage;
    private Calendar time;
    private DeviceNetworkInterface networkInterface;

    public DeviceAvgBandwidthUsage() {
    }
    
    public DeviceAvgBandwidthUsage(float avgUsage, Calendar time, DeviceNetworkInterface networkInterface) {
        this.avgUsage = avgUsage;
        this.time = time;
        this.networkInterface = networkInterface;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getAvgUsage() {
        return avgUsage;
    }

    public void setAvgUsage(float avgUsage) {
        this.avgUsage = avgUsage;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public DeviceNetworkInterface getNetworkInterface() {
        return networkInterface;
    }

    public void setNetworkInterface(DeviceNetworkInterface networkInterface) {
        this.networkInterface = networkInterface;
    }

    
}
