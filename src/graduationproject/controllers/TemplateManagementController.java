/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.controllers;

import com.opencsv.CSVReader;
import graduationproject.helpers.DataConverter;
import graduationproject.data.DataManager;
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
public class TemplateManagementController {

    private String resultMessage;
    private int[] templateIds;

    public enum TemplateType {
        SINGULAR("Singular"),
        TABULAR("Tabular");
        
        private String value;
        private TemplateType(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return this.value;
        }
    }

    public enum DataOrders {
        NAME(0),
        DESCRIPTION(1),
        SNMP_VERSION(2),
        IMPORTED_TIME(3),
        EXTRA(4);

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

    public int[] getTemplateIds() {
        return templateIds;
    }

    public boolean processImportingTemplateFromFile(boolean isTable, File file) {
        CSVReader reader = null;
        boolean result = false;
        try {
            reader = new CSVReader(new FileReader(file));
            String[] line = null;

            Date importedTime = new Date();
            Template newTemplate = new Template();
            newTemplate.setImportedTime(importedTime);
            newTemplate.setIsTable(isTable);

            int lenConstraint = 2;
            for (int i = 0; i < DataOrders.IMPORTED_TIME.getValue(); i++) {
                line = reader.readNext();
                if (line == null || line.length < lenConstraint) {
                    this.resultMessage = new ResultMessageGenerator().IMPORTING_FAILED_FILE_NON_CONTENT;
                    break;
                }

                if (i == DataOrders.NAME.getValue()) {
                    newTemplate.setName(line[lenConstraint - 1].trim());
                } else if (i == DataOrders.DESCRIPTION.getValue()) {
                    newTemplate.setDescription(line[lenConstraint - 1].trim());
                } else if (i == DataOrders.SNMP_VERSION.getValue()) {
                    newTemplate.setSnmpVersion(SnmpManager.getInstance().parseVersionString(line[lenConstraint - 1].trim()).getValue());
                }
                
            }

            line = reader.readNext();       //read the headers
            TemplateItemManagementController itemController = new TemplateItemManagementController();

            while ((line = reader.readNext()) != null) {
                newTemplate.getTemplateItems().add(new TemplateItem(
                        line[TemplateItemManagementController.DataOrders.MIB_NAME.getValue()].trim(),
                        line[TemplateItemManagementController.DataOrders.OID.getValue()].trim(),
                        line[TemplateItemManagementController.DataOrders.DISPLAY_NAME.getValue()].trim(),
                        line[TemplateItemManagementController.DataOrders.DESCRIPTION.getValue()].trim(),
                        itemController.normalizeValueType(line[TemplateItemManagementController.DataOrders.VALUE_TYPE.getValue()].trim()),
                        itemController.normalizeAccessType(line[TemplateItemManagementController.DataOrders.ACCESS_TYPE.getValue()].trim()),
                        true
                ));
            }

            if (DataManager.getInstance().getTemplateManager().saveTemplate(newTemplate) < 0) {
                this.resultMessage = new ResultMessageGenerator().IMPORTING_FAILED_OTHER;
            } else {
                result = true;
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            this.resultMessage = new ResultMessageGenerator().IMPORTING_FAILED_FILE_NOT_FOUND;
        } catch (Exception ex) {
            ex.printStackTrace();
            this.resultMessage = new ResultMessageGenerator().IMPORTING_FAILED_FILE_IO;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    this.resultMessage = new ResultMessageGenerator().IMPORTING_FAILED_FILE_IO;
                    result = false;
                }
            }
        }

        return result;

    }

    public List<String> processGettingImportedTemplates(DataOrders order, boolean isTable) {
        List<Template> templates = DataManager.getInstance().getTemplateManager().getTemplates(order, isTable);

        if (templates == null) {
            this.resultMessage = new ResultMessageGenerator().GETTING_FAILED_OTHER;
            return null;
        }

        if (templates.isEmpty()) {
            this.resultMessage = new ResultMessageGenerator().GETTING_FAILED_NO_RECORD;
        }

        List<String> result = new ArrayList<String>();
        int tempSize = templates.size();
        this.templateIds = new int[tempSize];

        for (int i = 0; i < tempSize; i++) {
            result.add(String.valueOf(templates.get(i).getData(order)));
            this.templateIds[i] = templates.get(i).getId();
        }

        return result;
    }

    public List<String> processSearchingTemplates(boolean isTable, DataOrders order, String prefix) {
        List<Template> templates = DataManager.getInstance().getTemplateManager().getTemplates(order, prefix, isTable);
        if (templates == null || templates.isEmpty()) {
            this.resultMessage = new ResultMessageGenerator().GETTING_FAILED_OTHER;
            return null;
        }

        List<String> result = new ArrayList<String>();
        this.templateIds = new int[templates.size()];
        int tempSize = templates.size();

        for (int i = 0; i < tempSize; i++) {
            result.add(String.valueOf(templates.get(i).getData(order)));
            this.templateIds[i] = templates.get(i).getId();
        }

        return result;
    }

    public List<Object> processGettingTemplateInfo(int templateId) {
        Template template = DataManager.getInstance().getTemplateManager().getTemplate(templateId);
        if (template == null) {
            this.resultMessage = new ResultMessageGenerator().GETTING_FAILED_NO_RECORD;
            return null;
        }
        
        List<TemplateItem> templateItems = template.getTemplateItems();
        if (templateItems == null) {
            this.resultMessage = new ResultMessageGenerator().GETTING_FAILED_OTHER;
            return null;
        }
        
        List<Object> result = new ArrayList<Object>();
        result.add(DataOrders.NAME.getValue(), template.getName());
        result.add(DataOrders.DESCRIPTION.getValue(), template.getDescription());
        result.add(DataOrders.SNMP_VERSION.getValue(), template.getSnmpVersion());
        result.add(DataOrders.IMPORTED_TIME.getValue(), new DataConverter().convertDateToString(template.getImportedTime()));
        
        TemplateItemManagementController itemController = new TemplateItemManagementController();
        int tempSize = templateItems.size();
        for (int i = 0; i < tempSize; i++) {
            result.add(DataOrders.EXTRA.getValue() + i, itemController.convertDataForView(false, templateItems.get(i)));
        }
        
        return result;
    }
    
    public boolean processSavingTemplateInfo(int templateId, List<Object> data) {
        Template template = DataManager.getInstance().getTemplateManager().getTemplate(templateId);
        if (template == null) {
            this.resultMessage = new ResultMessageGenerator().GETTING_FAILED_OTHER;
            return false;
        }
        template.setName((String) data.get(DataOrders.NAME.getValue()));
        template.setDescription((String) data.get(DataOrders.DESCRIPTION.getValue()));
        
        List<TemplateItem> templateItems = template.getTemplateItems();
        if (templateItems == null) {
            this.resultMessage = new ResultMessageGenerator().GETTING_FAILED_OTHER;
            return false;
        }
        
        int tempSize = templateItems.size();
        for (int i = 0; i < tempSize; i++) {
            List<Object> item = (List<Object>) data.get(DataOrders.EXTRA.getValue() + i);
            templateItems.get(i).setMibName((String) item.get(TemplateItemManagementController.DataOrders.MIB_NAME.getValue()));
            templateItems.get(i).setDisplayName((String) item.get(TemplateItemManagementController.DataOrders.DISPLAY_NAME.getValue()));
            templateItems.get(i).setOid((String) item.get(TemplateItemManagementController.DataOrders.OID.getValue()));
            templateItems.get(i).setIsEnabled((boolean) item.get(TemplateItemManagementController.DataOrders.IS_ENABLED.getValue()));
        }
        
        if (!DataManager.getInstance().getTemplateManager().updateTemplate(template)) {
            this.resultMessage = new ResultMessageGenerator().UPDATING_FAILED_OTHER;
            return false;
        }
        
        return true;
    }
    
    public class ResultMessageGenerator {
        public String IMPORTING_FAILED_FILE_NOT_FOUND = "The chosen file is not found. Please try again.";
        public String IMPORTING_FAILED_FILE_IO = "Some errors happened when reading data from that specified file. Please try again laster.";
        public String IMPORTING_FAILED_FILE_NON_CONTENT = "The chosen file doesn't have any content in it. Please check it then try again later.";
        public String IMPORTING_FAILED_OTHER = "Some errors happened when saving imported data to the database. Please try again later.";

        public String GETTING_FAILED_NO_RECORD = "We haven't imported any template yet.";
        public String GETTING_FAILED_OTHER = "Some errors happened when getting template data from database. Please try again later";

        public String UPDATING_FAILED_OTHER = "Some errors happened when updaing template info into database.";
    }

}
