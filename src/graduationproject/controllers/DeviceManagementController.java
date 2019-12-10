/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.controllers;

import com.opencsv.CSVReader;
import graduationproject.data.DataConverter;
import graduationproject.data.DataManager;
import graduationproject.data.models.ContactNetworkInterface;
import graduationproject.data.models.Device;
import graduationproject.data.models.DeviceCPUState;
import graduationproject.data.models.DeviceMemoryState;
import graduationproject.data.models.DeviceNetworkInterface;
import graduationproject.data.models.Setting;
import graduationproject.data.models.Template;
import graduationproject.data.models.TemplateItem;
import graduationproject.data.models.User;
import graduationproject.gui.ApplicationWindow;
import graduationproject.snmpd.callbacks.DeviceActiveCheckingCallback;
import graduationproject.snmpd.SnmpManager;
import graduationproject.snmpd.helpers.DeviceQueryHelper;
import graduationproject.snmpd.helpers.DeviceQueryHelper.DeviceCpuData;
import graduationproject.snmpd.helpers.DeviceQueryHelper.DeviceMemoryData;
import graduationproject.snmpd.helpers.DeviceQueryHelper.MemoryType;
import graduationproject.snmpd.helpers.DeviceQueryHelper.TemplateQuery;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import org.soulwing.snmp.SimpleSnmpV2cTarget;
import org.soulwing.snmp.SnmpContext;
import org.soulwing.snmp.SnmpTarget;

/**
 *
 * @author cloud
 */
public class DeviceManagementController {

    private String resultMessage;
    private String extraStringData;
    private int[] deviceIds;
    private Device checkingDevice;

    public static final String[] DEVICE_TYPES = {"Router", "Switch", "End Host"};
    public static final String UNKNOWN_DEVICE_INFO = "UNKNOWN";

    public enum DeviceStates {
        ACTIVE,
        DEACTIVE
    }

    public enum DataOrders {
        NAME(0),
        LABEL(1),
        TYPE(2),
        DESCRIPTION(3),
        LOCATION(4),
        SNMP_VERSION(5),
        LAST_ACCESS(6),
        IMPORTED_TIME(7),
        CI_IP_ADDRESS(8),
        CI_COMMUNITY(9),
        //based on positions in memory data list of a resource data collector
        MEMORY_RAM(0),
        MEMORY_VIRTUAL(1),
        MEMORY_OTHER(2),
        MEMORY_DISK(3),
        MEMORY_TOTAL(1),
        MEMORY_USED(2);

        private final int value;

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

    public int[] getDeviceIds() {
        return deviceIds;
    }

    public String getExtraStringData() {
        return extraStringData;
    }

    private String normalizeDeviceType(String inputType) {
        for (int i = 0; i < DEVICE_TYPES.length; i++) {
            if (DEVICE_TYPES[i].equalsIgnoreCase(inputType)) {
                return DEVICE_TYPES[i];
            }
        }
        return null;
    }

    public boolean processImportingDevicesFromFile(File file) {
        CSVReader reader = null;
        boolean result = false;
        try {
            reader = new CSVReader(new FileReader(file));
            String[] line = reader.readNext();

            int countLine = 0, countSaving = 0;
            Date importedTime = new Date();

            while ((line = reader.readNext()) != null) {
                ContactNetworkInterface networkInterface = new ContactNetworkInterface(
                        line[DataOrders.SNMP_VERSION.getValue() + 1],
                        line[DataOrders.SNMP_VERSION.getValue() + 2],
                        importedTime);

                Device device = new Device(
                        line[DataOrders.NAME.getValue()],
                        line[DataOrders.LABEL.getValue()],
                        this.normalizeDeviceType(line[DataOrders.TYPE.getValue()]),
                        line[DataOrders.DESCRIPTION.getValue()],
                        line[DataOrders.LOCATION.getValue()],
                        SnmpManager.getInstance().parseVersionString(line[DataOrders.SNMP_VERSION.getValue()]).getValue(),
                        importedTime,
                        networkInterface);

                if (DataManager.getInstance().getDeviceManager().saveDevice(device) >= 0) {
                    countSaving++;
                    //if (this.checkDeviceState(device))
                }

                countLine++;
            }

            if (countLine != 0) {
                if (countLine == countSaving) {
                    result = true;
                } else {
                    this.resultMessage = new ResultMessageGenerator().IMPORTING_FAILED_OTHER;
                }
            } else {
                this.resultMessage = new ResultMessageGenerator().IMPORTING_FAILED_FILE_NON_CONTENT;

            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            this.resultMessage = new ResultMessageGenerator().IMPORTING_FAILED_FILE_NOT_FOUND;
        } catch (Exception ex) {
            ex.printStackTrace();
            this.resultMessage = new ResultMessageGenerator().IMPORTING_FAILED_FILE_IO;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    this.resultMessage = new ResultMessageGenerator().IMPORTING_FAILED_FILE_IO;
                    result = false;
                }
            }
        }

        return result;
    }

    public void processPushingDeviceInfo(int deviceId, Device device, SnmpContext snmpContext) {
        System.out.println("START PUSING DEVICE INFO");
        boolean result = false;

        if (device == null) {
            device = DataManager.getInstance().getDeviceManager().getDevice(deviceId);
        }

        User user = DataManager.getInstance().getUserManager().getUser(DataManager.getInstance().getActiveAccountId());
        String userInfo = "";
        try {
            userInfo = (user.getEmail() != null) ? user.getEmail() : userInfo;
        } catch (Exception e) {
        }

        DeviceQueryHelper queryHelper = new DeviceQueryHelper();
        List<Object> pushingResult = null;
        if (snmpContext == null) {
            queryHelper.pushInfoIntoDevice(
                    device.getContactInterface().getIpAddress(), device.getContactInterface().getCommunity(),
                    deviceId, device.getName(), device.getLabel(), device.getLocation(), userInfo);
        } else {
            queryHelper.pushInfoIntoDevice(snmpContext, deviceId, device.getName(), device.getLabel(), device.getLocation(), userInfo);
        }
    }

    public void processUpdatingDeviceDescription(int deviceId, String description) {
        Device device = DataManager.getInstance().getDeviceManager().getDevice(deviceId);
        if (device != null) {
            device.setDescription(description);
            DataManager.getInstance().getDeviceManager().updateDevice(device);
            ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices().getPanelDeviceInfo()
                    .updateDeviceDescription(deviceId, device.getDescription());
        }
    }

    public List<String> processGettingImportedDevices(DataOrders order) {
        List<Device> devices = DataManager.getInstance().getDeviceManager().getDevices(order);

        if (devices == null) {
            this.resultMessage = new ResultMessageGenerator().GETTING_FAILED_OTHER;
            return null;
        }

        if (devices.isEmpty()) {
            this.resultMessage = new ResultMessageGenerator().GETTING_FAILED_NO_RECORD;
        }

        List<String> result = new ArrayList<String>();
        int tempSize = devices.size();
        this.deviceIds = new int[tempSize];

        for (int i = 0; i < tempSize; i++) {
            result.add((String) devices.get(i).getData(order));
            this.deviceIds[i] = devices.get(i).getId();
        }

        return result;
    }

    public void processCheckingStateOfDevices(int[] deviceIds) {
        System.out.println("START CHECKING DEVICE STATES");
        TimerTask checkingTask = new TimerTask() {
            @Override
            public void run() {
                DeviceManagementController deviceController = new DeviceManagementController();
                deviceController.startCheckingStateOfDevices(deviceIds);
            }
        };

        User currentUser = DataManager.getInstance().getUserManager().getUser(DataManager.getInstance().getActiveAccountId());
        Setting setting = currentUser.getSetting();
        SnmpManager.getInstance().getQueryTimerManager().startDeviceActiveTimer(checkingTask, 0, setting.getNormalizedTime(setting.getDeviceCheckingPeriod()));
    }

    public void startCheckingStateOfDevices(int deviceIds[]) {
        try {
            Date checkingTime = new Date();
            for (int deviceId : deviceIds) {
                Device device = DataManager.getInstance().getDeviceManager().getDevice(deviceId);
                device.setLastAccess(checkingTime);
                DataManager.getInstance().getDeviceManager().updateDevice(device);

                if (device != null) {
                    this.startCheckingDeviceState(device);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startCheckingDeviceState(Device device) {
        String objectToCheck = "sysUpTime";
        SnmpContext snmpContext = SnmpManager.getInstance().createContext(
                device.getSnmpVersion(),
                device.getContactInterface().getIpAddress(),
                device.getContactInterface().getCommunity());

        try {
            DeviceActiveCheckingCallback checkingCallback = new DeviceActiveCheckingCallback(device.getId(), objectToCheck);
            snmpContext.asyncGetNext(checkingCallback, objectToCheck);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> processSearchingDevices(DataOrders order, String prefix) {
        List<Device> devices = DataManager.getInstance().getDeviceManager().getDevices(order, prefix);
        if (devices == null || devices.isEmpty()) {
            this.resultMessage = new ResultMessageGenerator().GETTING_FAILED_OTHER;
            return null;
        }

        List<String> result = new ArrayList<String>();
        this.deviceIds = new int[devices.size()];
        int tempSize = devices.size();

        for (int i = 0; i < tempSize; i++) {
            result.add((String) devices.get(i).getData(order));
            this.deviceIds[i] = devices.get(i).getId();
        }

        return result;
    }

    public List<String> processGettingDeviceInfo(int deviceId) {
        Device device = DataManager.getInstance().getDeviceManager().getDevice(deviceId);
        if (device == null) {
            this.resultMessage = new ResultMessageGenerator().GETTING_FAILED_OTHER;
            return null;
        }

        List<String> result = new ArrayList<String>();
        result.add(DataOrders.NAME.getValue(), device.getName());
        result.add(DataOrders.LABEL.getValue(), device.getLabel());
        result.add(DataOrders.TYPE.getValue(), device.getType());
        result.add(DataOrders.DESCRIPTION.getValue(), device.getDescription());
        result.add(DataOrders.LOCATION.getValue(), device.getLocation());
        result.add(DataOrders.SNMP_VERSION.getValue(), device.getSnmpVersion());

        DataConverter dataConverter = new DataConverter();
        result.add(DataOrders.LAST_ACCESS.getValue(), dataConverter.convertDateToString(device.getLastAccess()));
        result.add(DataOrders.IMPORTED_TIME.getValue(), dataConverter.convertDateToString(device.getImportedTime()));

        result.add(DataOrders.CI_IP_ADDRESS.getValue(), device.getContactInterface().getIpAddress());
        result.add(DataOrders.CI_COMMUNITY.getValue(), device.getContactInterface().getCommunity());

        return result;
    }

    public boolean processSavingDeviceInfo(int deviceId, List<String> data) {
        Device device = DataManager.getInstance().getDeviceManager().getDevice(deviceId);
        if (device == null) {
            this.resultMessage = new ResultMessageGenerator().UPDATING_FAILED_OTHER;
            return false;
        }

        if (data.get(DataOrders.LABEL.getValue()).trim().isEmpty()) {
            this.resultMessage = new ResultMessageGenerator().UPDATING_FAILED_NON_LABEL;
            return false;
        }

        device.setName(data.get(DataOrders.NAME.getValue()));
        device.setLabel(data.get(DataOrders.LABEL.getValue()));
        device.setType(data.get(DataOrders.TYPE.getValue()));
//        device.setDescription(data.get(DataOrders.DESCRIPTION.getValue()));
        device.setLocation(data.get(DataOrders.LOCATION.getValue()));
        device.setSnmpVersion(data.get(DataOrders.SNMP_VERSION.getValue()));
        device.getContactInterface().setCommunity(data.get(DataOrders.CI_COMMUNITY.getValue()));
        device.getContactInterface().setIpAddress(data.get(DataOrders.CI_IP_ADDRESS.getValue()));
        device.getContactInterface().setUpdatedTime(new Date());

        //update now is called in processPushingDeviceInfo
        if (!DataManager.getInstance().getDeviceManager().updateDevice(device)) {
            this.resultMessage = new ResultMessageGenerator().UPDATING_FAILED_OTHER;
            return false;
        }

        this.extraStringData = (String) device.getData(
                ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices().getCurrentDataOrder());

        this.processPushingDeviceInfo(deviceId, device, null);

        return true;
    }

    public boolean processDeletingDevice(int deviceId) {
        if (!DataManager.getInstance().getDeviceManager().deleteDevice(deviceId)) {
            this.resultMessage = new ResultMessageGenerator().DELETING_FAILED_OTHER;
            return false;
        }
        return true;
    }

    public int processDeviceInfoWithStartNotification(SnmpTarget snmpTarget, boolean merge) {
        this.getCheckingDeviceWithNotificationData(snmpTarget, merge);
        if (checkingDevice != null) {
            ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices().updateLabelDeviceState(checkingDevice.getId(), DeviceStates.ACTIVE);
            return checkingDevice.getId();
        }
        return -1;
    }

    public int processDeviceInfoWithLinkNotification(SnmpTarget snmpTarget, int interfaceId, boolean isUp, boolean merge) {
        this.getCheckingDeviceWithNotificationData(snmpTarget, merge);
        if (checkingDevice != null) {
            if (isUp) {
                ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices()
                        .updateLabelInterfaceState(checkingDevice.getId(), interfaceId - 1, null, InterfaceManagementController.InterfaceStates.UP);
            } else {
                ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices()
                        .updateLabelInterfaceState(checkingDevice.getId(), interfaceId - 1, null, InterfaceManagementController.InterfaceStates.DOWN);
            }

            ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices().updateLabelDeviceState(checkingDevice.getId(), DeviceStates.ACTIVE);
            return checkingDevice.getId();
        }
        return -1;
    }

    public int processDeviceInfoWithAuthenticationNotification(SnmpTarget snmpTarget, boolean merge) {
        this.getCheckingDeviceWithNotificationData(snmpTarget, merge);
        if (checkingDevice != null) {
            return checkingDevice.getId();
        }
        return -1;
    }

    public int processDeviceInfoWithEgpNotification(SnmpTarget snmpTarget, boolean merge) {
        this.getCheckingDeviceWithNotificationData(snmpTarget, merge);
        if (checkingDevice != null) {
            return checkingDevice.getId();
        }
        return -1;
    }

    public int processDeviceInfoWithEnterpriseNotification(SnmpTarget snmpTarget, boolean merge) {
        this.getCheckingDeviceWithNotificationData(snmpTarget, merge);
        if (checkingDevice != null) {
            return checkingDevice.getId();
        }
        return -1;
    }

    private void getCheckingDeviceWithNotificationData(SnmpTarget target, boolean merge) {
        String community = ((SimpleSnmpV2cTarget) target).getCommunity();
        String[] liveData;

        checkingDevice = DataManager.getInstance().getDeviceManager().getDevice(DataOrders.CI_IP_ADDRESS, target.getAddress());
        if (checkingDevice == null) {
            return;
        }

        if (community == null) {
            community = checkingDevice.getContactInterface().getCommunity();
            liveData = new DeviceQueryHelper().getDeviceIdentification(target, community);
        } else {
            liveData = new DeviceQueryHelper().getDeviceIdentification(target);
        }

        if (liveData == null || !checkingDevice.getName().equals(liveData[DeviceQueryHelper.DataOrders.DEVICE_NAME.getValue()])) {
            this.checkingDevice = null;
            return;
        }

        if (liveData == null || !checkingDevice.getLabel().equals(liveData[DeviceQueryHelper.DataOrders.DEVICE_LABEL.getValue()])) {
//            checkingDevice = DataManager.getInstance().getDeviceManager().getDevice(
//                    DataOrders.LABEL,
//                    liveData[MergingDataHelper.DataOrders.DEVICE_LABEL.getValue()]);
            checkingDevice = null;
            return;
        }

//        if (checkingDevice != null && merge) {
//            checkingDevice.setName(mergingData[MergingDataHelper.DataOrders.DEVICE_NAME.getValue()]);
//            checkingDevice.setLabel(mergingData[MergingDataHelper.DataOrders.DEVICE_LABEL.getValue()]);
//            checkingDevice.getContactInterface().setCommunity(community);
//            checkingDevice.getContactInterface().setIpAddress(target.getAddress());
//            DataManager.getInstance().getDeviceManager().updateDevice(checkingDevice);
//        }
    }

    public String getCheckingDeviceInterfaceName(int interfaceId) {
        if (checkingDevice != null) {
            List<DeviceNetworkInterface> networkInterfaces = (List<DeviceNetworkInterface>) this.checkingDevice.getNetworkInterfaces();
            if (networkInterfaces != null) {
                return networkInterfaces.get(interfaceId - 1).getName();
            }
        }
        return null;
    }

    public String getCheckingDeviceLabel() {
        if (checkingDevice != null) {
            return checkingDevice.getLabel();
        }
        return UNKNOWN_DEVICE_INFO;
    }

    public void processSendingQueryBasedOnTemplate(boolean first, int deviceId, int templateId) {
        Device device = DataManager.getInstance().getDeviceManager().getDevice(deviceId);
        if (device == null) {
            this.resultMessage = new ResultMessageGenerator().QUERYING_FAILED_IN_PREPARING;
            return;
        }

        Template template = DataManager.getInstance().getTemplateManager().getTemplate(templateId);
        if (template == null) {
            this.resultMessage = new ResultMessageGenerator().QUERYING_FAILED_IN_PREPARING;
            return;
        }

        List<TemplateItem> templateItems = template.getTemplateItems();
        List<String> queryItems = new ArrayList<String>();
        List<String> displayNames = new ArrayList<String>();
        for (TemplateItem templateItem : templateItems) {
            if (templateItem.isIsEnabled()) {
                queryItems.add(templateItem.getMibName());
                displayNames.add(templateItem.getDisplayName());
            }
        }

        if (!queryItems.isEmpty()) {
            if (first) {
                ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices().getPanelMonitoringDevice().updateViewStage1(
                        device.getLabel(), template.getName(), displayNames, template.isIsTable());
            }

            TemplateQuery templateQuery = new TemplateQuery(
                    deviceId, device.getContactInterface().getIpAddress(), device.getContactInterface().getCommunity(),
                    templateId, queryItems, template.isIsTable());

            DeviceQueryHelper queryHelper = new DeviceQueryHelper();
            queryHelper.startTemplateQuery(templateQuery);
        }
    }

    public void processCompletedTemplateQuery(TemplateQuery completedQuery) {
        ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices().getPanelMonitoringDevice().updateViewStage2(
                completedQuery.getDeviceId(), completedQuery.getTemplateId(),
                new DataConverter().convertCalendarToString(completedQuery.getReceivedTime()),
                completedQuery.getResult(), completedQuery.isIsTable());
    }

    public class ResultMessageGenerator {

        public String IMPORTING_FAILED_FILE_NOT_FOUND = "The chosen file is not found. Please try again.";
        public String IMPORTING_FAILED_FILE_IO = "Some errors happened when reading data from that specified file. Please try again laster.";
        public String IMPORTING_FAILED_FILE_NON_CONTENT = "The chosen file doesn't have any content in it. Please check it then try again later.";
        public String IMPORTING_FAILED_OTHER = "Some errors happened when saving imported data to the database. Please try again later.";

        public String GETTING_FAILED_NO_RECORD = "We have not imported any record into database.";
        public String GETTING_FAILED_OTHER = "Some errors happened when getting device data from database. Please try again later";

        public String UPDATING_FAILED_OTHER = "Some errors happened when updaing device info into database.";
        public String UPDATING_FAILED_NON_LABEL = "Label field should not be left empty, otherwise you can not find that device later.";

        public String DELETING_FAILED_OTHER = "Some errors happened when deleting device data in database.";

        public String QUERYING_FAILED_IN_PREPARING = "Some errors happened when preparing query to send to the client.";

        public String PUSHING_FAILED_OTHER = "Some errors happened when saving device info onto device. Please try again later";
    }

}
