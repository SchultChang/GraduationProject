/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.models;

import graduationproject.controllers.DeviceManagementController.DataOrders;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author cloud
 */
public class Device {
    
    private int id;
    private String label;
    private String name;
    private String type;
    private String location;
    private String description;
    private String snmpVersion;
    private Date importedTime;
    private Date lastAccess;
    
    private ContactNetworkInterface contactInterface;
    private List<DeviceNetworkInterface> networkInterfaces;
    
    public Device() {
    }
    
    public Device(String name, String label, String type, String description,
            String location, String snmpVersion, Date importedTime, ContactNetworkInterface contactInterface) {
        this.label = label;
        this.name = name;
        this.type = type;
        this.location = location;
        this.description = description;
        this.snmpVersion = snmpVersion;
        this.importedTime = importedTime;
        this.lastAccess = null;
        this.contactInterface = contactInterface;
    }
    
    public ContactNetworkInterface getContactInterface() {
        return contactInterface;
    }
    
    public void setContactInterface(ContactNetworkInterface contactInterface) {
        this.contactInterface = contactInterface;
    }
    
    public String getLabel() {
        return label;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getSnmpVersion() {
        return snmpVersion;
    }
    
    public void setSnmpVersion(String snmpVersion) {
        this.snmpVersion = snmpVersion;
    }
    
    public Date getImportedTime() {
        return importedTime;
    }
    
    public void setImportedTime(Date importedTime) {
        this.importedTime = importedTime;
    }
    
    public Date getLastAccess() {
        return lastAccess;
    }
    
    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }

    public List<DeviceNetworkInterface> getNetworkInterfaces() {
        return networkInterfaces;
    }

    public void setNetworkInterfaces(List<DeviceNetworkInterface> networkInterfaces) {
        this.networkInterfaces = networkInterfaces;
    }

    public Object getData(DataOrders order) {
        if (order == DataOrders.NAME) {
            return this.name;
        }
        if (order == DataOrders.LABEL) {
            return this.label;
        }
        if (order == DataOrders.TYPE) {
            return this.type;
        }
        if (order == DataOrders.LOCATION) {
            return this.location;
        }
        return null;
    }

    public void setData(DataOrders order, Object value) {
        if (order == DataOrders.NAME) {
            this.setName((String) value);
        }
        if (order == DataOrders.LABEL) {
            this.setLabel((String) value);
        }
        if (order == DataOrders.TYPE) {
            this.setType((String) value);
        }
        if (order == DataOrders.LOCATION) {
            this.setType((String) value);
        }
    }
    
    public static String getColumnName(DataOrders order) {
        String[] columnNames = {"name", "label", "type", "description", "location",
            "snmpVersion", "lastAccess", "importedTime", "contactInterface"};
        return columnNames[order.getValue()];
    }
    
    
}
