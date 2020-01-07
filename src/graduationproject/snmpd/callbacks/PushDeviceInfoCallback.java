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
public class PushDeviceInfoCallback implements SnmpCallback<VarbindCollection> {      //get info stage

    private int deviceId;
    private int stage;
    String name;
    String location;
    String contact;

    public PushDeviceInfoCallback(int deviceId, String name, String location, String contact) {
        this.deviceId = deviceId;
        this.name = name;
        this.location = location;
        this.contact = contact;
        this.stage = 1;
    }

    @Override
    public void onSnmpResponse(SnmpEvent<VarbindCollection> se) {
        try {
            VarbindCollection varbinds = se.getResponse().get();
            DeviceManagementController deviceController = new DeviceManagementController();

            String[] ids = DeviceQueryHelper.parseDeviceIdentification(varbinds.get("sysName").asString());

            if (stage == 1) {
                this.stage++;
                if (deviceController.processUpdatingDeviceInfo(deviceId,
                        ids[0], ids[1],
                        varbinds.get("sysDescr").asString(),
                        varbinds.get("sysLocation").asString())) {

                    deviceController.processUpdateDeviceLabel(this.deviceId,
                            DeviceQueryHelper.parseDeviceIdentification(this.name)[1]);

                    varbinds.get("sysName").set(this.name);
                    varbinds.get("sysLocation").set(this.location);
                    varbinds.get("sysContact").set(this.contact);

                    se.getContext().asyncSet(this,
                            varbinds.get("sysName"),
                            varbinds.get("sysLocation"),
                            varbinds.get("sysContact"));
                } else {
                    System.out.println("PUSHING DATA INTO DEVICE ENDED");
                    deviceController.processUpdateDeviceLabel(this.deviceId, ids[1]);
                    se.getContext().close();
                }
            } else {
                System.out.println("PUSHING DATA INTO DEVICE ENDED");
                se.getContext().close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            se.getContext().close();
        }
    }

}
