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
public class ContactNetworkInterface {
    private int id;
    private String ipAddress;
    private String community;
    private Date updatedTime;

    public ContactNetworkInterface() {
    }

    public ContactNetworkInterface(String ipAddress, String community, Date updatedTime) {
        this.ipAddress = ipAddress;
        this.community = community;
        this.updatedTime = updatedTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
    
    
}
