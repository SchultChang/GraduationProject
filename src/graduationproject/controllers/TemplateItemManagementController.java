/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.controllers;

import com.opencsv.CSVReader;
import graduationproject.data.DataManager;
import graduationproject.data.models.ContactNetworkInterface;
import graduationproject.data.models.Device;
import graduationproject.data.models.Template;
import graduationproject.data.models.TemplateItem;
import graduationproject.snmpd.SnmpManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author cloud
 */
public class TemplateItemManagementController {

    private String resultMessage;

    public static final String[] VALUE_TYPES = {"String", "Number"};
    public static final String[] ACCESS_TYPES = {"Read-Only", "Read-Write", "Not-Accessible"};

    public enum DataOrders {
        MIB_NAME(0),
        OID(1),
        DISPLAY_NAME(2),
        IS_ENABLED(3),
        DESCRIPTION(3),
        VALUE_TYPE(4),
        ACCESS_TYPE(5);

        private int value;

        private DataOrders(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

    }

    public String getResultMessage() {
        return resultMessage;
    }
    
    public String normalizeValueType(String input) {
        System.out.println("VALUE TYPE:" + input);
        for (int i = 0; i < VALUE_TYPES.length; i++) {
            if (VALUE_TYPES[i].equalsIgnoreCase(input)) {
                return VALUE_TYPES[i];
            }
        }
        return null;
    }

    public String normalizeAccessType(String input) {
        System.out.println("ACCESS-TYPE:" + input);
        for (int i = 0; i < ACCESS_TYPES.length; i++) {
            if (ACCESS_TYPES[i].equalsIgnoreCase(input)) {
                return ACCESS_TYPES[i];
            }
        }
        return null;
    }

    public List<Object> processGettingTemplateItemInfo(int templateId, int itemListId) {
        Template template = DataManager.getInstance().getTemplateManager().getTemplate(templateId);
        if (template == null) {
            this.resultMessage = new ResultMessageGenerator().GETTING_FAILED_OTHER;
            return null;
        }
        
        List<TemplateItem> templateItems = template.getTemplateItems();
        if (templateItems == null || itemListId >= templateItems.size()) {
            this.resultMessage = new ResultMessageGenerator().GETTING_FAILED_OTHER;
            return null;
        }
        
        return this.convertDataForView(true, templateItems.get(itemListId));
    }

    public boolean processSavingTemplateItemInfo(int templateId, int itemListId, List<Object> data) {
        Template template = DataManager.getInstance().getTemplateManager().getTemplate(templateId);
        if (template == null) {
            this.resultMessage = new ResultMessageGenerator().GETTING_FAILED_OTHER;
            return false;
        }
        
        List<TemplateItem> templateItems = template.getTemplateItems();
        if (templateItems == null || itemListId >= templateItems.size()) {
            this.resultMessage = new ResultMessageGenerator().GETTING_FAILED_OTHER;
            return false;
        }
        
        TemplateItem item = templateItems.get(itemListId);
        item.setDisplayName((String) data.get(DataOrders.DISPLAY_NAME.getValue()));
        item.setDescription((String) data.get(DataOrders.DESCRIPTION.getValue()));
        
        if (!DataManager.getInstance().getTemplateItemManager().updateTemplateItem(item)) {
            this.resultMessage = new ResultMessageGenerator().UPDATING_FAILED_OTHER;
            return false;
        }
        
        return true;
    }
    
    public List<Object> convertDataForView(boolean full, TemplateItem templateItem) {
        List<Object> result = new ArrayList<Object>();
        result.add(DataOrders.MIB_NAME.getValue(), templateItem.getMibName());
        result.add(DataOrders.OID.getValue(), templateItem.getOid());
        result.add(DataOrders.DISPLAY_NAME.getValue(), templateItem.getDisplayName());
        if (full) {
            result.add(DataOrders.DESCRIPTION.getValue(), templateItem.getDescription());
            result.add(DataOrders.VALUE_TYPE.getValue(), templateItem.getValueType());
            result.add(DataOrders.ACCESS_TYPE.getValue(), templateItem.getAccessType());
        } else {
            result.add(DataOrders.IS_ENABLED.getValue(), templateItem.isIsEnabled());
        }
        return result;
    }

    public class ResultMessageGenerator {
        public String GETTING_FAILED_NO_RECORD = "We haven't imported any template yet.";
        public String GETTING_FAILED_OTHER = "Some errors happened when getting template data from database. Please try again later";

        public String UPDATING_FAILED_OTHER = "Some errors happened when updaing template info into database.";
    }

}
