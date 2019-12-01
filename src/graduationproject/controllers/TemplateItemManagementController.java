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

}
