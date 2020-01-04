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
public class InterfaceQueryCallback implements SnmpCallback<SnmpAsyncWalker<VarbindCollection>> {

    private InterfaceDataCollector dataCollector;
    private int stage;

    public InterfaceQueryCallback(InterfaceDataCollector dataCollector) {
        this.dataCollector = dataCollector;
        this.stage = 1;
    }

    @Override
    public void onSnmpResponse(SnmpEvent<SnmpAsyncWalker<VarbindCollection>> se) {
        try {
            SnmpResponse<SnmpAsyncWalker<VarbindCollection>> response = se.getResponse();
            SnmpAsyncWalker<VarbindCollection> walker = response.get();

            VarbindCollection entry = walker.next().get();
            while (entry != null) {
                switch (this.stage) {
                    case 1:
                        this.dataCollector.addIfTableEntryData(entry);
                        break;
                    case 2:
                        this.dataCollector.addIpAddrTableEntryData(entry);
                        break;
                    case 3:
                        this.dataCollector.addIpNetToMediaTableEntryData(entry);
                        break;
                    default:
                        break;
                }
                
                try {
                    entry = walker.next().get();
                } catch (Exception e) {
                    walker.invoke();
                    entry = walker.next().get();
                }
            }

            this.stage++;
            switch (this.stage) {
                case 2:
                    se.getContext().asyncWalk(this, 1, InterfaceQueryHelper.ipAddrTable);
                    break;
                case 3:
                    se.getContext().asyncWalk(this, 1, InterfaceQueryHelper.ipNetToMediaTable);
                    break;
                default:
                    this.dataCollector.processCollectedData();
                    se.getContext().close();
                    break;
            }
//            InterfaceQueryCallbackStage2 stage2Callback = new InterfaceQueryCallbackStage2(this.dataCollector);
//            se.getContext().asyncWalk(stage2Callback, 1, InterfaceQueryHelper.ipAddrTable);
        } catch (Exception e) {
            e.printStackTrace();
            se.getContext().close();
        }
    }

}
