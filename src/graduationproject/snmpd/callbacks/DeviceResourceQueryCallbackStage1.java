/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.snmpd.callbacks;

import graduationproject.snmpd.helpers.DeviceQueryHelper;
import graduationproject.snmpd.helpers.DeviceQueryHelper.DeviceResourceDataCollector;
import graduationproject.snmpd.helpers.InterfaceQueryHelper;
import java.util.ArrayList;
import java.util.List;
import org.soulwing.snmp.SnmpAsyncWalker;
import org.soulwing.snmp.SnmpCallback;
import org.soulwing.snmp.SnmpEvent;
import org.soulwing.snmp.SnmpResponse;
import org.soulwing.snmp.VarbindCollection;

/**
 *
 * @author cloud
 */
public class DeviceResourceQueryCallbackStage1 implements SnmpCallback<SnmpAsyncWalker<VarbindCollection>> {

    private DeviceResourceDataCollector dataCollector;

    public DeviceResourceQueryCallbackStage1(DeviceResourceDataCollector dataCollector) {
        this.dataCollector = dataCollector;
    }

    @Override
    public void onSnmpResponse(SnmpEvent<SnmpAsyncWalker<VarbindCollection>> se) {
        try {
            SnmpResponse<SnmpAsyncWalker<VarbindCollection>> response = se.getResponse();
            SnmpAsyncWalker<VarbindCollection> walker = response.get();

            VarbindCollection entry = walker.next().get();
            while (entry != null) {
                this.dataCollector.processCPUData(entry);
                try {
                    entry = walker.next().get();
                } catch (Exception e) {
                    walker.invoke();
                    entry = walker.next().get();
                }
            }

            DeviceResourceQueryCallbackStage2 stage2Callback = new DeviceResourceQueryCallbackStage2(this.dataCollector);
            se.getContext().asyncWalk(stage2Callback, 1, DeviceQueryHelper.deviceTable);
        } catch (Exception e) {
            e.printStackTrace();
            se.getContext().close();
        }

    }

}
