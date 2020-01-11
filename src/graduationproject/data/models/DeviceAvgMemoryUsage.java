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
public class DeviceAvgMemoryUsage {
    
    private int id;
    private String type;
    private float avgUsage;
    private Calendar time;
    private Device device;

    public DeviceAvgMemoryUsage() {
    }
    
    public DeviceAvgMemoryUsage(String type, float avgUsage, Calendar time, Device device) {
        this.type = type;
        this.avgUsage = avgUsage;
        this.time = time;
        this.device = device;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
    
}
