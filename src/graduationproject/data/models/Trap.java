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
    private Date receivedTime;

    public Trap() {
    }

    public Trap(String trapType, Date receivedTime) {
        this.trapType = trapType;
        this.receivedTime = receivedTime;
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

    
    
}
