/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.model_managers;

import graduationproject.data.models.DeviceInterfaceDynamicData;
import graduationproject.data.models.DeviceNetworkInterface;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author cloud
 */
public class DeviceInterfaceDynamicDataManager {
    private SessionFactory sessionFactory;

    public DeviceInterfaceDynamicDataManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    public int insertDynamicData(int interfaceId, DeviceInterfaceDynamicData dynamicData) {
        Session session = null;
        Transaction tx = null;
        int result = -1;
        
        try {
           session = this.sessionFactory.openSession();
           tx = session.beginTransaction();
           
           DeviceNetworkInterface owningInterface = session.find(DeviceNetworkInterface.class, interfaceId);
           dynamicData.setNetworkInterface(owningInterface);
           session.persist(dynamicData);
           
           tx.commit();
           result = 1;
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        
        return result;
    }
}
