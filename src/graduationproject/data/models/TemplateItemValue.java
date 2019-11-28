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
public class TemplateItemValue {
    private int id;
    private String value;
    private Date updatedTime;
    
    private TemplateItem templateItem;
    private Device device;

    public TemplateItemValue() {
    }

    public TemplateItemValue(String value, Date updatedTime, TemplateItem templateItem, Device device) {
        this.value = value;
        this.updatedTime = updatedTime;
        this.templateItem = templateItem;
        this.device = device;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public TemplateItem getTemplateItem() {
        return templateItem;
    }

    public void setTemplateItem(TemplateItem templateItem) {
        this.templateItem = templateItem;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
    
    
}
