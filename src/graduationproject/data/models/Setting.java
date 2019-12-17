/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.models;

import graduationproject.controllers.TemplateManagementController.DataOrders;

/**
 *
 * @author cloud
 */
public class Setting {
    private int id;
    private int deviceCheckingPeriod;
    private int resourceCheckingPeriod;
    private int interfaceCheckingPeriod;
    private int monitoringQueryPeriod;
    private int maxTableIndex;    
    private boolean hasPasswordRemembered;
    
    public Setting() {
        this.deviceCheckingPeriod = 5;
        this.resourceCheckingPeriod = 5;
        this.interfaceCheckingPeriod = 5;
        this.monitoringQueryPeriod = 10;        
        this.maxTableIndex = 10;               
        
        this.hasPasswordRemembered = false;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDeviceCheckingPeriod() {
        return deviceCheckingPeriod;
    }

    public void setDeviceCheckingPeriod(int deviceCheckingPeriod) {
        this.deviceCheckingPeriod = deviceCheckingPeriod;
    }

    public int getResourceCheckingPeriod() {
        return resourceCheckingPeriod;
    }

    public void setResourceCheckingPeriod(int resourceCheckingPeriod) {
        this.resourceCheckingPeriod = resourceCheckingPeriod;
    }

    public int getInterfaceCheckingPeriod() {
        return interfaceCheckingPeriod;
    }

    public void setInterfaceCheckingPeriod(int interfaceCheckingPeriod) {
        this.interfaceCheckingPeriod = interfaceCheckingPeriod;
    }

    public int getMonitoringQueryPeriod() {
        return monitoringQueryPeriod;
    }

    public void setMonitoringQueryPeriod(int monitoringQueryPeriod) {
        this.monitoringQueryPeriod = monitoringQueryPeriod;
    }

    public int getMaxTableIndex() {
        return maxTableIndex;
    }

    public void setMaxTableIndex(int maxTableIndex) {
        this.maxTableIndex = maxTableIndex;
    }

    public boolean isHasPasswordRemembered() {
        return hasPasswordRemembered;
    }

    public void setHasPasswordRemembered(boolean hasPasswordRemembered) {
        this.hasPasswordRemembered = hasPasswordRemembered;
    }
    
    public int getNormalizedTime(int time) { //each time period of this class is in milliseconds state
        return time * 1000;
    }
    
}
