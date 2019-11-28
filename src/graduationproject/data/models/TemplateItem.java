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
public class TemplateItem {
    private int id;
    private String mibName;
    private String oid;
    private String displayName;
    private String valueType;
    private String accessType;
    private boolean isEnabled;

    public TemplateItem() {
    }

    public TemplateItem(String mibName, String oid, String displayName, String valueType, String accessType, boolean isEnabled) {
        this.mibName = mibName;
        this.oid = oid;
        this.displayName = displayName;
        this.valueType = valueType;
        this.accessType = accessType;
        this.isEnabled = isEnabled;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMibName() {
        return mibName;
    }

    public void setMibName(String mibName) {
        this.mibName = mibName;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public boolean isIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    
}
