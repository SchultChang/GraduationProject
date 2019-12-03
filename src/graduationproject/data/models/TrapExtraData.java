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
public class TrapExtraData {
    private int id;
    private String value;
    private String name;
    
    private Trap trap;
    private TemplateItem templateItem;

    public TrapExtraData() {
    }

    public TrapExtraData(String value, String name, Trap trap) {
        this.value = value;
        this.name = name;
        this.trap = trap;
        this.templateItem = null;
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
