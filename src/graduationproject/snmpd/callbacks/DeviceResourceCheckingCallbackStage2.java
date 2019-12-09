/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.snmpd.callbacks;

import graduationproject.snmpd.helpers.DeviceQueryHelper;
import org.soulwing.snmp.SnmpAsyncWalker;
import org.soulwing.snmp.SnmpCallback;
import org.soulwing.snmp.SnmpEvent;
import org.soulwing.snmp.SnmpResponse;
import org.soulwing.snmp.VarbindCollection;

/**
 *
 * @author cloud
 */
public class DeviceResourceCheckingCallbackStage2 implements SnmpCallback<SnmpAsyncWalker<VarbindCollection>> {

    private DeviceQueryHelper.DeviceResourceDataCollector dataCollector;

    public DeviceResourceCheckingCallbackStage2(DeviceQueryHelper.DeviceResourceDataCollector dataCollector) {
        this.dataCollector = dataCollector;
    }

    @Override
    public void onSnmpResponse(SnmpEvent<SnmpAsyncWalker<VarbindCollection>> se) {
        try {
            SnmpResponse<SnmpAsyncWalker<VarbindCollection>> response = se.getResponse();
            SnmpAsyncWalker<VarbindCollection> walker = response.get();

            VarbindCollection entry = walker.next().get();
            while (entry != null) {
                this.dataCollector.processDeviceData(entry);
                try {
                    entry = walker.next().get();
                } catch (Exception e) {
                    walker.invoke();
                    entry = walker.next().get();
                }
            }

            DeviceResourceCheckingCallbackStage3 stage3Callback = new DeviceResourceCheckingCallbackStage3(this.dataCollector);
            se.getContext().asyncWalk(stage3Callback, 1, DeviceQueryHelper.memoryTable);
        } catch (Exception e) {
//            e.printStackTrace();
            se.getContext().close();
        }

    }

}
