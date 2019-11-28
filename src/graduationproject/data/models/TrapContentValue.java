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
public class TrapContentValue {
    private int id;
    private String value;
    private String oid;
    
    private Trap trap;
    private TemplateItem templateItem;

    public TrapContentValue() {        
    }

    public TrapContentValue(String value, String oid, Trap trap, TemplateItem templateItem) {
        this.value = value;
        this.oid = oid;
        this.trap = trap;
        this.templateItem = templateItem;
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

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Trap getTrap() {
        return trap;
    }

    public void setTrap(Trap trap) {
        this.trap = trap;
    }

    public TemplateItem getTemplateItem() {
        return templateItem;
    }

    public void setTemplateItem(TemplateItem templateItem) {
        this.templateItem = templateItem;
    }
    
}
