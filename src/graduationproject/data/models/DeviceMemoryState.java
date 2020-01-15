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
public class DeviceMemoryState {
    private int id;
    private String type;
    private String description;
    private float totalSize;
    private float usedSize;
    private Calendar updatedTime;
    private boolean isSummarized;
    
    private Device device;

    public DeviceMemoryState() {
    }
    
    public DeviceMemoryState(String type, String description, float totalSize, float usedSize, Calendar updatedTime, Device device) {
        this.type = type;
        this.description = description;
        this.totalSize = totalSize;
        this.usedSize = usedSize;
        this.updatedTime = (Calendar) updatedTime.clone();
        this.device = device;
        
        this.isSummarized = false;
    }

    public DeviceMemoryState(String type, String description, float totalSize, float usedSize, Calendar updatedTime, boolean isSummarized, Device device) {
        this.type = type;
        this.description = description;
        this.totalSize = totalSize;
        this.usedSize = usedSize;
        this.updatedTime = (Calendar) updatedTime.clone();
        this.device = device;
        
        this.isSummarized = isSummarized;
    }
    
    public DeviceMemoryState(DeviceMemoryState other) {
        this.type = other.type;
        this.description = other.description;
        this.totalSize = other.totalSize;
        this.usedSize = other.usedSize;
        this.updatedTime = other.updatedTime;
        this.device = other.device;
        
        this.isSummarized = other.isSummarized;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(float totalSize) {
        this.totalSize = totalSize;
    }

    public float getUsedSize() {
        return usedSize;
    }

    public void setUsedSize(float usedSize) {
        this.usedSize = usedSize;
    }
    
    public Calendar getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Calendar updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
    
    public double getUsagePercentage() {
        return this.usedSize * 100.0d / this.totalSize;
    }

    public boolean isIsSummarized() {
        return isSummarized;
    }

    public void setIsSummarized(boolean isSummarized) {
        this.isSummarized = isSummarized;
    }
    
    public void displayInfo() {
//        System.out.println(this.type);
//        System.out.println(this.description);
        System.out.println(this.type);
//        System.out.println(this.totalSize);
        System.out.println(this.usedSize);
        System.out.println(this.isSummarized);
        System.out.println(this.device.getId());
//        System.out.println(this.);
    }
    
}
