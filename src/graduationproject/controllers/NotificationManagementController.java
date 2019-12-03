/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.controllers;

import graduationproject.data.DataManager;
import graduationproject.data.models.Device;
import graduationproject.data.models.Notification;
import graduationproject.data.models.NotificationExtraData;
import graduationproject.gui.ApplicationWindow;
import graduationproject.snmpd.helpers.NotificationParser;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author cloud
 */
public class NotificationManagementController {

    private String resultMessage;
//        DEVICE_ID(2),
//        DEVICE_INFO(3),
//        TYPE(4),
//        CONTENT(5),
//        EXTRA(6);

    public void processPushingDeviceNotifications(Calendar receivedTime, List<Object> notificationData) {
        int notificationId = -1;
        Device device = DataManager.getInstance().getDeviceManager()
                .getDevice((int) notificationData.get(NotificationParser.DataOrders.DEVICE_ID.getValue()));
        if (device != null) {
            List<NotificationExtraData> extraDataList = new ArrayList<NotificationExtraData>();
            int dataSize = notificationData.size();
            for (int i = NotificationParser.DataOrders.EXTRA.getValue(); i < dataSize; i++) {
                String[] extraData = (String[]) notificationData.get(i);
                extraDataList.add(new NotificationExtraData(
                        extraData[NotificationParser.DataOrders.EXTRA_VALUE.getValue()],
                        extraData[NotificationParser.DataOrders.EXTRA_NAME.getValue()]
                ));
            }

            Notification notification = new Notification(
                    String.valueOf(notificationData.get(NotificationParser.DataOrders.TYPE.getValue())),
                    String.valueOf(notificationData.get(NotificationParser.DataOrders.CONTENT.getValue())),
                    receivedTime,
                    device,
                    extraDataList
            );

            notificationId = DataManager.getInstance().getNotificationManager().saveNotification(notification);
        }
        ApplicationWindow.getInstance().getPanelMain().showNotification(notificationId,
                (String) notificationData.get(NotificationParser.DataOrders.DEVICE_DISPLAY_INFO.getValue()),
                (String) notificationData.get(NotificationParser.DataOrders.CONTENT.getValue()));

    }

    public class ResultMessageGenerator {

        public String SAVING_FAILED_OTHER = "Some errors happened when saving notification data into system.";

        public String GETTING_FAILED_OTHER = "Getting notification data is failed. Try again later.";
    }
}
