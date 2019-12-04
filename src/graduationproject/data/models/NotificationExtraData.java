/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.models;

import graduationproject.controllers.NotificationManagementController.DataOrders;
import graduationproject.snmpd.helpers.NotificationParser;

/**
 *
 * @author cloud
 */
public class NotificationExtraData {
    private int id;
    private String value;
    private String name;
    
    public NotificationExtraData() {
    }

    public NotificationExtraData(String value, String name) {
        this.value = value;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getData() {
        String[] result = new String[2];
        result[DataOrders.EXTRA_NAME.getValue()] = this.name;
        result[DataOrders.EXTRA_VALUE.getValue()] = this.value;
        return result;
    }
}
