/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.snmpd.helpers;

import graduationproject.snmpd.SnmpManager;
import org.soulwing.snmp.SnmpContext;
import org.soulwing.snmp.SnmpTarget;
import org.soulwing.snmp.VarbindCollection;

/**
 *
 * @author cloud
 */
public class MergingDataHelper {
    private final String DEVICE_ID_SEP=  ":_:";
    
    //NOTE: identification = name + label    
    public String[] getDeviceIdentification(SnmpTarget target) {
        String objName = "sysName";      
        
        SnmpContext queryContext = SnmpManager.getInstance().createContext(target);
        VarbindCollection varbind = queryContext.getNext(objName).get();
        String retrievedData = varbind.get(objName).asString();
        
        String[] result = new String[DataOrders.END.getValue()];
        int sepPosition = retrievedData.lastIndexOf(this.DEVICE_ID_SEP);
        if (sepPosition >= 0) {
            result[DataOrders.DEVICE_NAME.getValue()] = retrievedData.substring(0, sepPosition);
            result[DataOrders.DEVICE_LABEL.getValue()] = retrievedData.substring(sepPosition + this.DEVICE_ID_SEP.length());
        } else {
            result[DataOrders.DEVICE_NAME.getValue()] = retrievedData;
            result[DataOrders.DEVICE_LABEL.getValue()] = retrievedData;
        }
        
        return result;
    }
    
    public String[] getDeviceIdentification(SnmpTarget target, String community) {
        return this.getDeviceIdentification(SnmpManager.getInstance().createTarget(SnmpManager.SnmpVersion.VERSION_2_COMMUNITY, target.getAddress(), community));
    }
    
    public enum DataOrders {
        DEVICE_NAME(0),
        DEVICE_LABEL(1),
        END(2);
        
        private int value;
        private DataOrders(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return this.value;
        }
    }
}
