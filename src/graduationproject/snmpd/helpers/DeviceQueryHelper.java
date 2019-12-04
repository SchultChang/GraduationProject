/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.snmpd.helpers;

import graduationproject.controllers.DeviceManagementController;
import graduationproject.snmpd.SnmpManager;
import graduationproject.snmpd.callbacks.QueryGetNextCallback;
import graduationproject.snmpd.callbacks.QueryWalkCallback;
import java.util.ArrayList;
import java.util.List;
import org.soulwing.snmp.SnmpContext;
import org.soulwing.snmp.VarbindCollection;

/**
 *
 * @author cloud
 */
public class DeviceQueryHelper {

    public void startTemplateQuery(TemplateQuery query) {
        if (query.isTable) {
            this.startWalkQuery(query);
        } else {
            this.startGetNextQuery(query);
        }
    }

    private void startGetNextQuery(TemplateQuery query) {
        QueryGetNextCallback queryCallback = new QueryGetNextCallback(query);
        SnmpContext context = SnmpManager.getInstance().createContext(
                SnmpManager.SnmpVersion.VERSION_2_COMMUNITY.getValue(), query.ipAddress, query.community);
        context.asyncGetNext(queryCallback, query.itemList);
    }

    private void startWalkQuery(TemplateQuery query) {
        QueryWalkCallback queryCallback = new QueryWalkCallback(query);
        SnmpContext context = SnmpManager.getInstance().createContext(
                SnmpManager.SnmpVersion.VERSION_2_COMMUNITY.getValue(), query.ipAddress, query.community);

        List<String> nonRepeater = new ArrayList<String>();
        nonRepeater.add("sysUpTime");
        context.asyncWalk(queryCallback, nonRepeater, query.itemList);
    }

    public static class ResponseDataProcessor {

        public void processGetNextData(TemplateQuery templateQuery, VarbindCollection varbinds) {
            try {
                int tempSize = templateQuery.itemList.size();

                for (int i = 0; i < tempSize; i++) {
                    templateQuery.result.add(varbinds.get(templateQuery.itemList.get(i)).asString());
                }

                DeviceManagementController deviceController = new DeviceManagementController();
                deviceController.processCompletedTemplateQuery(templateQuery);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void processWalkData(TemplateQuery templateQuery, List<VarbindCollection> varbindsList) {
            try {
                int tempSize = templateQuery.itemList.size();

                for (VarbindCollection varbinds : varbindsList) {
                    System.out.println(varbinds.toString());
                    Object[] objects = new Object[tempSize];
                    for (int i = 0; i < tempSize; i++) {
                        objects[i] = varbinds.get(templateQuery.itemList.get(i)).asString();
                    }
                    templateQuery.result.add(objects);
                }
                DeviceManagementController deviceController = new DeviceManagementController();
                deviceController.processCompletedTemplateQuery(templateQuery);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class TemplateQuery {

        private int deviceId;
        private String ipAddress;
        private String community;
        private int templateId;
        private List<String> itemList;
        private boolean isTable;

        private List<Object> result;

        public TemplateQuery(int deviceId, String ipAddress, String community, int templateId, List<String> itemList, boolean isTable) {
            this.deviceId = deviceId;
            this.ipAddress = ipAddress;
            this.community = community;
            this.templateId = templateId;
            this.itemList = itemList;
            this.isTable = isTable;

            this.result = new ArrayList<Object>();
        }

        public int getDeviceId() {
            return deviceId;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public String getCommunity() {
            return community;
        }

        public int getTemplateId() {
            return templateId;
        }

        public List<String> getItemList() {
            return itemList;
        }

        public boolean isIsTable() {
            return isTable;
        }

        public List<Object> getResult() {
            return result;
        }

    }
}
