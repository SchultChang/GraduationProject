/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.snmpd.callbacks;

import org.soulwing.snmp.SnmpCallback;
import org.soulwing.snmp.SnmpEvent;
import org.soulwing.snmp.VarbindCollection;

/**
 *
 * @author cloud
 */
public class PushDeviceInfoCallbackStage2 implements SnmpCallback<VarbindCollection>{
    
    @Override
    public void onSnmpResponse(SnmpEvent<VarbindCollection> se) {
        try {
            se.getResponse().get().get("sysName");
        } catch (Exception e) {
            e.printStackTrace();
        }
        se.getContext().close();
        System.out.println("PUSHING DATA INTO DEVICE ENDED");
    }
    
}
