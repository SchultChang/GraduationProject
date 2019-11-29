/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.snmpd.callbacks;

import graduationproject.snmpd.helpers.InterfaceQueryHelper;
import org.soulwing.snmp.SnmpAsyncWalker;
import org.soulwing.snmp.SnmpCallback;
import org.soulwing.snmp.SnmpEvent;
import org.soulwing.snmp.SnmpResponse;
import org.soulwing.snmp.VarbindCollection;

/**
 *
 * @author cloud
 */
public class InterfaceQueryCallbackStage3 implements SnmpCallback<SnmpAsyncWalker<VarbindCollection>>{
    private InterfaceQueryHelper.InterfaceDataCollector dataCollector;

    public InterfaceQueryCallbackStage3(InterfaceQueryHelper.InterfaceDataCollector dataCollector) {
        this.dataCollector = dataCollector;
    }

    @Override
    public void onSnmpResponse(SnmpEvent<SnmpAsyncWalker<VarbindCollection>> se) {
        try {
            SnmpResponse<SnmpAsyncWalker<VarbindCollection>> response = se.getResponse();
            SnmpAsyncWalker<VarbindCollection> walker = response.get();
            
            VarbindCollection entry = walker.next().get();
            while (entry != null) {
                this.dataCollector.addIpNetToMediaTableEntryData(entry);
                try {
                    entry = walker.next().get();
                } catch (Exception e) {
                    walker.invoke();
                    entry = walker.next().get();
                }
            }            
            
            this.dataCollector.processCollectedData();
        } catch(Exception e) {
            e.printStackTrace();
            se.getContext().close();
        } finally {
            se.getContext().close();
        }
    }
    
}