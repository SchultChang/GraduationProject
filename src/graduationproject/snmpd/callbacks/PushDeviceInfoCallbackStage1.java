/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.snmpd.callbacks;

import graduationproject.controllers.DeviceManagementController;
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

//            String sysDescription = varbinds.get("sysDescr").asString();
            DeviceManagementController deviceController = new DeviceManagementController();
            if (deviceController.processUpdatingDeviceInfo(deviceId,
                    varbinds.get("sysName").asString(),
                    varbinds.get("sysDescription").asString(),
                    varbinds.get("sysLocation").asString())) {
                PushDeviceInfoCallbackStage2 stage2Callback = new PushDeviceInfoCallbackStage2();
                se.getContext().asyncSet(stage2Callback,
                        varbinds.get("sysName"),
                        varbinds.get("sysLocation"),
                        varbinds.get("sysContact"));
            } else {
                se.getContext().close();
            }

//            varbinds.get("sysName").set(this.name);
//            varbinds.get("sysLocation").set(this.location);
//            varbinds.get("sysContact").set(this.contact);
        } catch (Exception e) {
            se.getContext().close();
        }
    }

}
