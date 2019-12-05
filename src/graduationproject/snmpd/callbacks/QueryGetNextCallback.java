/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.snmpd.callbacks;

import graduationproject.snmpd.helpers.DeviceQueryHelper;
import graduationproject.snmpd.helpers.DeviceQueryHelper.TemplateQuery;
import java.util.Calendar;
import org.soulwing.snmp.SnmpCallback;
import org.soulwing.snmp.SnmpEvent;
import org.soulwing.snmp.Varbind;
import org.soulwing.snmp.VarbindCollection;

/**
 *
 * @author cloud
 */
public class QueryGetNextCallback implements SnmpCallback<VarbindCollection>{
    private TemplateQuery templateQuery;

    public QueryGetNextCallback(TemplateQuery templateQuery) {
        this.templateQuery = templateQuery;
    }
    
    @Override
    public void onSnmpResponse(SnmpEvent<VarbindCollection> se) {
        try {
            VarbindCollection varbinds = se.getResponse().get();
            DeviceQueryHelper.ResponseDataProcessor dataProcessor = new DeviceQueryHelper.ResponseDataProcessor();
            dataProcessor.processGetNextData(Calendar.getInstance(), templateQuery, varbinds);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            se.getContext().close();
        }
    }    
    
}
