/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.models;

import java.util.Date;

/**
 *
 * @author cloud
 */
public class Trap {
    private int id;
    private String trapType;
    private String content;
    private Date receivedTime;
    
    private Device device;

    public Trap() {
    }

    public Trap(String trapType, String content, Date receivedTime) {
        this.trapType = trapType;
        this.content = content;
        this.receivedTime = receivedTime;
    }

    public Trap(String trapType, Date receivedTime, Device device) {
        this.trapType = trapType;
        this.receivedTime = receivedTime;
        this.device = device;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrapType() {
        return trapType;
    }

    public void setTrapType(String trapType) {
        this.trapType = trapType;
    }

    public Date getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(Date receivedTime) {
        this.receivedTime = receivedTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

}
