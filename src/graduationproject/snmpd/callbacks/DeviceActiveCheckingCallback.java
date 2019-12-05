/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.snmpd.callbacks;

import graduationproject.controllers.DeviceManagementController;
import graduationproject.gui.ApplicationWindow;
import org.soulwing.snmp.SimpleSnmpV2cTarget;
import org.soulwing.snmp.SnmpCallback;
import org.soulwing.snmp.SnmpEvent;
import org.soulwing.snmp.VarbindCollection;

/**
 *
 * @author cloud
 */
public class DeviceActiveCheckingCallback implements SnmpCallback<VarbindCollection> {

    private int deviceId;
    private String queryObject;

    public DeviceActiveCheckingCallback(int deviceId, String queryObject) {
        this.deviceId = deviceId;
        this.queryObject = queryObject;
    }

    @Override
    public void onSnmpResponse(SnmpEvent<VarbindCollection> se) {
        try {
            VarbindCollection varbinds = se.getResponse().get();
            Object result = varbinds.get(queryObject);

            if (ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices()
                    .getDeviceState(deviceId) == DeviceManagementController.DeviceStates.DEACTIVE) {
                DeviceManagementController deviceController = new DeviceManagementController();
                deviceController.processPushingDeviceInfo(deviceId, null, se.getContext());

                ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices()
                        .updateLabelDeviceState(deviceId, DeviceManagementController.DeviceStates.ACTIVE);
            }
        } catch (Exception e) {
            ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices()
                    .updateLabelDeviceState(deviceId, DeviceManagementController.DeviceStates.DEACTIVE);
            se.getContext().close();
        }
    }
}
