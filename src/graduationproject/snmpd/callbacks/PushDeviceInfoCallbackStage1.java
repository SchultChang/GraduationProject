/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.snmpd.callbacks;

import graduationproject.controllers.DeviceManagementController;
import graduationproject.snmpd.helpers.DeviceQueryHelper;
import java.util.List;
import org.soulwing.snmp.SnmpCallback;
import org.soulwing.snmp.SnmpEvent;
import org.soulwing.snmp.VarbindCollection;

/**
 *
 * @author cloud
 */
public class PushDeviceInfoCallbackStage1 implements SnmpCallback<VarbindCollection> {      //get info stage

    private int deviceId;
    String name;
    String location;
    String contact;

    public PushDeviceInfoCallbackStage1(int deviceId, String name, String location, String contact) {
        this.deviceId = deviceId;
        this.name = name;
        this.location = location;
        this.contact = contact;
    }

    @Override
    public void onSnmpResponse(SnmpEvent<VarbindCollection> se) {
        try {
            VarbindCollection varbinds = se.getResponse().get();
            DeviceManagementController deviceController = new DeviceManagementController();

            String[] ids = DeviceQueryHelper.parseDeviceIdentification(varbinds.get("sysName").asString());
            
            if (deviceController.processUpdatingDeviceInfo(deviceId,
                    ids[0], ids[1],
                    varbinds.get("sysDescr").asString(),
                    varbinds.get("sysLocation").asString())) {
                
                deviceController.processUpdateDeviceLabel(this.deviceId, 
                        DeviceQueryHelper.parseDeviceIdentification(this.name)[1]);
                varbinds.get("sysName").set(this.name);
                varbinds.get("sysLocation").set(this.location);
                varbinds.get("sysContact").set(this.contact);
                
                PushDeviceInfoCallbackStage2 stage2Callback = new PushDeviceInfoCallbackStage2();
                se.getContext().asyncSet(stage2Callback,
                        varbinds.get("sysName"),
                        varbinds.get("sysLocation"),
                        varbinds.get("sysContact"));
            } else {
                deviceController.processUpdateDeviceLabel(this.deviceId, ids[1]);
                se.getContext().close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            se.getContext().close();
        }
    }

}
