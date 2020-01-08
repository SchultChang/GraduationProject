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
public class DeviceCPUState {
    private int id;
    private int hrDeviceId;
    private String firmwareId;
    private String description;
    private float cpuLoad;
    private Calendar updatedTime;
    private boolean isSummarized;
    
    private Device device;

    public DeviceCPUState() {
    }
    
    public DeviceCPUState(int hrDeviceId, String firmwareId, String description, float cpuLoad, Calendar updatedTime, Device device) {
        this.hrDeviceId = hrDeviceId;
        this.firmwareId = firmwareId;
        this.description = description;
        this.cpuLoad = cpuLoad;
        this.updatedTime = updatedTime;
        this.device = device;
        
        this.isSummarized = false;
    }

    public DeviceCPUState(int hrDeviceId, String firmwareId, String description, float cpuLoad, Calendar updatedTime, boolean isSummarized, Device device) {
        this.hrDeviceId = hrDeviceId;
        this.firmwareId = firmwareId;
        this.description = description;
        this.cpuLoad = cpuLoad;
        this.updatedTime = updatedTime;
        this.device = device;
        
        this.isSummarized = isSummarized;
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public int getHrDeviceId() {
        return hrDeviceId;
    }

    public void setHrDeviceId(int hrDeviceId) {
        this.hrDeviceId = hrDeviceId;
    }
    
    public String getFirmwareId() {
        return firmwareId;
    }

    public void setFirmwareId(String firmwareId) {
        this.firmwareId = firmwareId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getCpuLoad() {
        return cpuLoad;
    }

    public void setCpuLoad(float cpuLoad) {
        this.cpuLoad = cpuLoad;
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

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
