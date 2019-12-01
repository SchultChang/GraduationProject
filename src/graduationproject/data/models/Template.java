/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.models;

import graduationproject.controllers.TemplateManagementController;
import graduationproject.controllers.TemplateManagementController.DataOrders;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author cloud
 */
public class Template {

    private int id;
    private String name;
    private String description;
    private String snmpVersion;
    private boolean isTable;
    private Date importedTime;

    private List<TemplateItem> templateItems;

    public Template() {
        this.isTable = false;
        this.templateItems = new ArrayList<TemplateItem>();
    }

    public Template(String name, String description, String snmpVersion, boolean isTable, Date importedTime) {
        this.name = name;
        this.description = description;
        this.snmpVersion = snmpVersion;
        this.isTable = isTable;
        this.importedTime = importedTime;
        this.templateItems = new ArrayList<TemplateItem>();
    }

    public List<TemplateItem> getTemplateItems() {
        return templateItems;
    }

    public void setTemplateItems(List<TemplateItem> templateItems) {
        this.templateItems = templateItems;
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

    public boolean isIsTable() {
        return isTable;
    }

    public void setIsTable(boolean isTable) {
        this.isTable = isTable;
    }

    public Date getImportedTime() {
        return importedTime;
    }

    public void setImportedTime(Date importedTime) {
        this.importedTime = importedTime;
    }
    
    public Object getData(DataOrders order ) {
        if (order == DataOrders.NAME) {
            return this.name;
        }
        if (order == DataOrders.DESCRIPTION) {
            return this.description;
        }
        return null;
    }

    public static String getColumnName(DataOrders order) {
        String[] columnNames = {"name", "description"};
        return columnNames[order.getValue()];
    }

}
