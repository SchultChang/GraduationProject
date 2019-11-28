/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.models;

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
    private List<Trap> traps;

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
        this.traps = new ArrayList<Trap>();
    }

    public List<Trap> getTraps() {
        return traps;
    }

    public void setTraps(List<Trap> traps) {
        this.traps = traps;
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

    
}
