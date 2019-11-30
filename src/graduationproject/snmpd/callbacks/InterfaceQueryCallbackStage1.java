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
public class InterfaceQueryCallbackStage1 implements SnmpCallback<SnmpAsyncWalker<VarbindCollection>>{
    private InterfaceDataCollector dataCollector;

    public InterfaceQueryCallbackStage1(InterfaceDataCollector dataCollector) {
        this.dataCollector = dataCollector;
    }

    @Override
    public void onSnmpResponse(SnmpEvent<SnmpAsyncWalker<VarbindCollection>> se) {
        try {
            SnmpResponse<SnmpAsyncWalker<VarbindCollection>> response = se.getResponse();
            SnmpAsyncWalker<VarbindCollection> walker = response.get();
            
            VarbindCollection entry = walker.next().get();
            while (entry != null) {
                this.dataCollector.addIfTableEntryData(entry);
                try {
                    entry = walker.next().get();
                } catch (Exception e) {
                    walker.invoke();
                    entry = walker.next().get();
                }
            }            
            
            InterfaceQueryCallbackStage2 stage2Callback = new InterfaceQueryCallbackStage2(this.dataCollector);
            se.getContext().asyncWalk(stage2Callback, 1, InterfaceQueryHelper.ipAddrTable);
        } catch(Exception e) {
//            e.printStackTrace();
            se.getContext().close();
        }
    }
    
}
