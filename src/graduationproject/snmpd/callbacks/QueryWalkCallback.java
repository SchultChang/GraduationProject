/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.snmpd.callbacks;

import graduationproject.snmpd.helpers.DeviceQueryHelper.ResponseDataProcessor;
import graduationproject.snmpd.helpers.DeviceQueryHelper.TemplateQuery;
import java.util.ArrayList;
import java.util.List;
import org.soulwing.snmp.SnmpAsyncWalker;
import org.soulwing.snmp.SnmpCallback;
import org.soulwing.snmp.SnmpEvent;
import org.soulwing.snmp.SnmpResponse;
import org.soulwing.snmp.VarbindCollection;

/**
 *
 * @author cloud
 */
public class QueryWalkCallback implements SnmpCallback<SnmpAsyncWalker<VarbindCollection>>{

    private TemplateQuery templateQuery;

    public QueryWalkCallback(TemplateQuery templateQuery) {
        this.templateQuery = templateQuery;
    }

    @Override
    public void onSnmpResponse(SnmpEvent<SnmpAsyncWalker<VarbindCollection>> se) {
        try {
            SnmpResponse<SnmpAsyncWalker<VarbindCollection>> response = se.getResponse();
            SnmpAsyncWalker<VarbindCollection> walker = response.get();
            
            VarbindCollection entry = walker.next().get();
            List<VarbindCollection> varbindsList = new ArrayList<VarbindCollection>();
            
            while (entry != null) {
                varbindsList.add(entry);
                try {
                    entry = walker.next().get();
                } catch (Exception e) {
                    walker.invoke();
                    entry = walker.next().get();
                }
            }            
            
            ResponseDataProcessor dataProcessor = new ResponseDataProcessor();
            dataProcessor.processWalkData(templateQuery, varbindsList);
        } catch(Exception e) {
//            e.printStackTrace();
            se.getContext().close();
        } finally {
            se.getContext().close();
        }
        
    }
    
}
