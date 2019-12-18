/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.snmpd.callbacks;

import graduationproject.controllers.DeviceManagementController;
import graduationproject.controllers.DeviceResourceManagementController;
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
public class DeviceResourceQueryCallbackStage3 implements SnmpCallback<SnmpAsyncWalker<VarbindCollection>> {

    private DeviceQueryHelper.DeviceResourceDataCollector dataCollector;

    public DeviceResourceQueryCallbackStage3(DeviceQueryHelper.DeviceResourceDataCollector dataCollector) {
        this.dataCollector = dataCollector;
    }

    @Override
    public void onSnmpResponse(SnmpEvent<SnmpAsyncWalker<VarbindCollection>> se) {
        try {
            SnmpResponse<SnmpAsyncWalker<VarbindCollection>> response = se.getResponse();
            SnmpAsyncWalker<VarbindCollection> walker = response.get();

            VarbindCollection entry = walker.next().get();
            while (entry != null) {
                this.dataCollector.processMemoryData(entry);
                try {
                    entry = walker.next().get();
                } catch (Exception e) {
                    walker.invoke();
                    entry = walker.next().get();
                }
            }

            DeviceResourceManagementController resourceController = new DeviceResourceManagementController();
            resourceController.processCollectedResourceData(this.dataCollector.getDeviceId(), 
                    this.dataCollector.getDeviceCpuData(), this.dataCollector.getDeviceMemoryData());
        } catch (Exception e) {
//            e.printStackTrace();
            se.getContext().close();
        } finally {
            se.getContext().close();
        }

    }

}