/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.models;

/**
 *
 * @author cloud
 */
public class DeviceNetworkInterface {
    private int id;
    private String name;
    private String macAddress;
    private String type;
    
    public DeviceNetworkInterface() {
    }

    public DeviceNetworkInterface(String name, String macAddress, String type) {
        this.name = name;
        this.macAddress = macAddress;
        this.type = type;
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

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
}
