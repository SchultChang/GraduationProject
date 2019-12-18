/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.snmpd.callbacks;

import graduationproject.controllers.NotificationManagementController;
import graduationproject.gui.ApplicationWindow;
import graduationproject.snmpd.helpers.NotificationParser;
import java.util.Calendar;
import org.soulwing.snmp.SimpleSnmpV2cTarget;
import org.soulwing.snmp.SnmpNotificationEvent;
import org.soulwing.snmp.SnmpNotificationHandler;
import org.soulwing.snmp.VarbindCollection;

/**
 *
 * @author cloud
 */
public class NotificationProcessor implements SnmpNotificationHandler {

    @Override
    public Boolean handleNotification(SnmpNotificationEvent sne) {
        try {
            if (ApplicationWindow.getInstance().getPanelMain().isVisible()) {
                System.out.println("RECEIVE A NOTIFICATION");
                VarbindCollection varbinds = sne.getSubject().getVarbinds();
                SimpleSnmpV2cTarget target = (SimpleSnmpV2cTarget) sne.getSubject().getPeer();
                Calendar currentTime = Calendar.getInstance();

                NotificationParser notificationParser = new NotificationParser();
                NotificationManagementController notificationController = new NotificationManagementController();
                notificationController.processPushingDeviceNotifications(currentTime, notificationParser.parseNotification(target, varbinds));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

}
