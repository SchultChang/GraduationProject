/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.controllers;

import graduationproject.helpers.DataConverter;
import graduationproject.data.DataManager;
import graduationproject.data.models.Device;
import graduationproject.data.models.Notification;
import graduationproject.data.models.NotificationExtraData;
import graduationproject.gui.ApplicationWindow;
import graduationproject.snmpd.helpers.NotificationParser;
import graduationproject.snmpd.helpers.NotificationParser.NotificationType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author cloud
 */
public class NotificationManagementController {

    private final String UNKNOW_DEVICE_VALUE = "UNKNOWN";
    private final String NOTIFICATION_DISPLAY_HEADER_FORMAT = "%s (%s) at %s";
    private final String NOTIFICATION_DEVICE_FORMAT = "%s (%s)";
    private String resultMessage;

    private int[] notificationIds;

//        DEVICE_ID(2),
//        DEVICE_INFO(3),
//        TYPE(4),
//        CONTENT(5),
//        EXTRA(6);
    public enum DataOrders {
        DEVICE_LABEL(0),
        DEVICE_ADDRESS(1),
        DEVICE_COMMUNITY(2),
        TYPE(3),
        CONTENT(4),
        TIME(5),
        EXTRA_DATA(6),
        EXTRA_NAME(0),
        EXTRA_VALUE(1),
        LIST_DEVICE(0),
        LIST_TYPE(1),
        LIST_CONTENT(2),
        LIST_TIME(3),
        LIST_END(4);

        private int value;

        private DataOrders(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public int[] getNotificationIds() {
        return notificationIds;
    }

    public void processPushingDeviceNotifications(Calendar receivedTime, List<Object> notificationData) {
        int notificationId = -1;
        Device device = DataManager.getInstance().getDeviceManager()
                .getDevice((int) notificationData.get(NotificationParser.DataOrders.DEVICE_ID.getValue()));
//        if (device != null) {                          //use this to discard unknown host notification from storing into our system
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
                String.valueOf(notificationData.get(NotificationParser.DataOrders.DEVICE_ADDRESS.getValue())),
                receivedTime,
                device,
                extraDataList
        );

        notificationId = DataManager.getInstance().getNotificationManager().saveNotification(notification);
//        }
        if (device != null) {
            ApplicationWindow.getInstance().getPanelMain().showNotification(notificationId,
                    String.format(NOTIFICATION_DISPLAY_HEADER_FORMAT,
                            device.getLabel(),
                            (String) notificationData.get(NotificationParser.DataOrders.DEVICE_ADDRESS.getValue()),
                            new DataConverter().convertCalendarToTimeString(receivedTime)),
                    (String) notificationData.get(NotificationParser.DataOrders.CONTENT.getValue()));
        } else {
            ApplicationWindow.getInstance().getPanelMain().showNotification(notificationId,
                    String.format(NOTIFICATION_DISPLAY_HEADER_FORMAT,
                            UNKNOW_DEVICE_VALUE,
                            (String) notificationData.get(NotificationParser.DataOrders.DEVICE_ADDRESS.getValue()),
                            new DataConverter().convertCalendarToTimeString(receivedTime)),
                    (String) notificationData.get(NotificationParser.DataOrders.CONTENT.getValue()));
        }
    }

    public List<Object> processGettingNotificationInfo(int notificationId) {
        Notification notification = DataManager.getInstance().getNotificationManager().getNotification(notificationId);
        if (notification == null) {
            this.resultMessage = new ResultMessageGenerator().GETTING_FAILED_OTHER;
            return null;
        }

        List<Object> result = new ArrayList<Object>();

        Device device = notification.getDevice();
        if (device != null) {
            result.add(DataOrders.DEVICE_LABEL.getValue(), device.getLabel());
            result.add(DataOrders.DEVICE_ADDRESS.getValue(), device.getContactInterface().getIpAddress());
            result.add(DataOrders.DEVICE_COMMUNITY.getValue(), device.getContactInterface().getCommunity());
        } else {
            result.add(DataOrders.DEVICE_LABEL.getValue(), UNKNOW_DEVICE_VALUE);
            result.add(DataOrders.DEVICE_ADDRESS.getValue(), notification.getSourceAddress());
            result.add(DataOrders.DEVICE_COMMUNITY.getValue(), UNKNOW_DEVICE_VALUE);
        }

        result.add(DataOrders.TYPE.getValue(), notification.getNotificationType());
        result.add(DataOrders.CONTENT.getValue(), notification.getContent());
        result.add(DataOrders.TIME.getValue(), new DataConverter().convertCalendarToString(notification.getReceivedTime()));

        List<NotificationExtraData> extraData = notification.getExtraData();
        int tempSize = extraData.size();
        for (int i = 0; i < tempSize; i++) {
            result.add(DataOrders.EXTRA_DATA.getValue() + i, extraData.get(i).getData());
        }

        return result;
    }

    public List<Object> getNotificationsOfDay(Calendar day) {
        List<Notification> notifications = DataManager.getInstance().getNotificationManager().getNotifications(day);
        if (notifications == null) {
            this.resultMessage = new ResultMessageGenerator().GETTING_FAILED_OTHER;
            return null;
        }

        return this.convertDataToView(notifications);
    }

    public List<Object> getNotificationsOfDay(Calendar day, String deviceInfo, List<Object> notificationTypes) {
        if (notificationTypes.isEmpty()) {
            return new ArrayList<Object>();
        }

        List<Notification> notifications;
        if (!deviceInfo.isEmpty()) {
            if (this.UNKNOW_DEVICE_VALUE.equals(deviceInfo)) {
                notifications = DataManager.getInstance().getNotificationManager().getNotifications(
                        null, day, notificationTypes);
            } else {                       //uncomment to display only known host
                notifications = DataManager.getInstance().getNotificationManager().getNotifications(
                        day, deviceInfo, notificationTypes);
            }
        } else {
            notifications = DataManager.getInstance().getNotificationManager().getNotifications(day, notificationTypes);
        }

        if (notifications == null) {
            this.resultMessage = new ResultMessageGenerator().GETTING_FAILED_OTHER;
            return null;
        }

        return this.convertDataToView(notifications);
    }

    private List<Object> convertDataToView(List<Notification> notifications) {

        List<Object> result = new ArrayList<Object>();
        int tempSize = notifications.size();
        this.notificationIds = new int[tempSize];
        Object[] values;
        Device device;
        Notification notification;

        for (int i = 0; i < tempSize; i++) {
            notification = notifications.get(i);
            notificationIds[i] = notification.getId();

            values = new Object[DataOrders.LIST_END.getValue()];
            device = notification.getDevice();
            if (device != null) {
                values[DataOrders.LIST_DEVICE.getValue()] = String.format(this.NOTIFICATION_DEVICE_FORMAT,
                        device.getLabel(),
                        device.getContactInterface().getIpAddress());
            } else {
                values[DataOrders.LIST_DEVICE.getValue()] = String.format(this.NOTIFICATION_DEVICE_FORMAT,
                        this.UNKNOW_DEVICE_VALUE,
                        notification.getSourceAddress());
            }

            values[DataOrders.LIST_TYPE.getValue()] = notification.getNotificationType();
            values[DataOrders.LIST_CONTENT.getValue()] = notification.getContent();
            values[DataOrders.LIST_TIME.getValue()] = new DataConverter().convertCalendarToString(notification.getReceivedTime());

            result.add(values);
        }

        return result;
    }

    public class ResultMessageGenerator {

        public String SAVING_FAILED_OTHER = "Some errors happened when saving notification data into system.";

        public String GETTING_FAILED_OTHER = "Some errors happened when getting notification data. Try again later.";
    }
}
