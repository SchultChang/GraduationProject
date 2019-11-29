/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.snmpd.callbacks;

import graduationproject.snmpd.helpers.InterfaceQueryHelper;
import graduationproject.snmpd.helpers.InterfaceQueryHelper.InterfaceDataCollector;
import org.soulwing.snmp.SnmpAsyncWalker;
import org.soulwing.snmp.SnmpCallback;
import org.soulwing.snmp.SnmpEvent;
import org.soulwing.snmp.SnmpResponse;
import org.soulwing.snmp.VarbindCollection;

/**
 *
 * @author cloud
 */
public class InterfaceQueryCallbackStage2 implements SnmpCallback<SnmpAsyncWalker<VarbindCollection>>{
    private InterfaceDataCollector dataCollector;

    public InterfaceQueryCallbackStage2(InterfaceDataCollector dataCollector) {
        this.dataCollector = dataCollector;
    }

    @Override
    public void onSnmpResponse(SnmpEvent<SnmpAsyncWalker<VarbindCollection>> se) {
        try {
            SnmpResponse<SnmpAsyncWalker<VarbindCollection>> response = se.getResponse();
            SnmpAsyncWalker<VarbindCollection> walker = response.get();
            
            VarbindCollection entry = walker.next().get();
            while (entry != null) {
                this.dataCollector.addIpAddrTableEntryData(entry);
                try {
                    entry = walker.next().get();
                } catch (Exception e) {
                    walker.invoke();
                    entry = walker.next().get();
                }
            }            
            
            InterfaceQueryCallbackStage3 stage3Callback = new InterfaceQueryCallbackStage3(this.dataCollector);
            se.getContext().asyncWalk(stage3Callback, 1, InterfaceQueryHelper.ipNetToMediaTable);
        } catch(Exception e) {
            e.printStackTrace();
            se.getContext().close();
        }
    }
    
}
