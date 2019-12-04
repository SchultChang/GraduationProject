/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.models;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author cloud
 */
public class Notification {
    private int id;
    private String notificationType;
    private String content;
    private String sourceAddress;
    private Calendar receivedTime;
    
    private Device device;
    private List<NotificationExtraData> extraData;
    
    public Notification() {
    }

    public Notification(String notificationType, String content, String sourceAddress, Calendar receivedTime, Device device, List<NotificationExtraData> extraData) {
        this.notificationType = notificationType;
        this.content = content;
        this.sourceAddress = sourceAddress;
        this.receivedTime = receivedTime;
        this.device = device;
        this.extraData = extraData;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public Calendar getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(Calendar receivedTime) {
        this.receivedTime = receivedTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }
    
    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public List<NotificationExtraData> getExtraData() {
        return extraData;
    }

    public void setExtraData(List<NotificationExtraData> extraData) {
        this.extraData = extraData;
    }

}

