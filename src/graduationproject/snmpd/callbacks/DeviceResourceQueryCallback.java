/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.snmpd.callbacks;

import graduationproject.controllers.DeviceResourceManagementController;
import graduationproject.snmpd.helpers.DeviceQueryHelper;
import graduationproject.snmpd.helpers.DeviceQueryHelper.DeviceResourceDataCollector;
import org.soulwing.snmp.SnmpAsyncWalker;
import org.soulwing.snmp.SnmpCallback;
import org.soulwing.snmp.SnmpEvent;
import org.soulwing.snmp.SnmpResponse;
import org.soulwing.snmp.VarbindCollection;

/**
 *
 * @author cloud
 */
public class DeviceResourceQueryCallback implements SnmpCallback<SnmpAsyncWalker<VarbindCollection>> {

    private DeviceResourceDataCollector dataCollector;
    private int stage;

    public DeviceResourceQueryCallback(DeviceResourceDataCollector dataCollector) {
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
                        this.dataCollector.processCPUData(entry);
                        break;
                    case 2:
                        this.dataCollector.processDeviceData(entry);
                        break;
                    case 3:
                        this.dataCollector.processMemoryData(entry);
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
                    se.getContext().asyncWalk(this, 1, DeviceQueryHelper.deviceTable);
                    break;
                case 3:
                    se.getContext().asyncWalk(this, 1, DeviceQueryHelper.memoryTable);
                    break;
                default:
                    DeviceResourceManagementController resourceController = new DeviceResourceManagementController();
                    resourceController.processCollectedResourceData(this.dataCollector.getDeviceId(),
                            this.dataCollector.getDeviceCpuData(), this.dataCollector.getDeviceMemoryData());
                    se.getContext().close();
                    break;
            }
//            DeviceResourceQueryCallbackStage2 stage2Callback = new DeviceResourceQueryCallbackStage2(this.dataCollector);
//            se.getContext().asyncWalk(stage2Callback, 1, DeviceQueryHelper.deviceTable);
        } catch (Exception e) {
            e.printStackTrace();
            se.getContext().close();
        }

    }

}
