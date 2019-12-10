/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.model_managers;

import graduationproject.data.models.Device;
import graduationproject.data.models.DeviceInterfaceDynamicData;
import graduationproject.data.models.DeviceNetworkInterface;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author cloud
 */
public class DeviceNetworkInterfaceManager {

    private SessionFactory sessionFactory;

    public DeviceNetworkInterfaceManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

//    public DeviceNetworkInterface getNetworkInterface(Device device, String name) {
//        Session session = null;
//        Transaction tx = null;
//        DeviceNetworkInterface result = null;
//
//        try {
//            session = this.sessionFactory.openSession();
//            tx = session.beginTransaction();
//
//            Criteria cri = session.createCriteria(DeviceNetworkInterface.class);
//            cri.add(Restrictions.eq("device", device))
//                    .add(Restrictions.eq("name", name));
//            result = (DeviceNetworkInterface) cri.uniqueResult();
//
//            tx.commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//            tx.rollback();
//        } finally {
//            if (session != null) {
//                session.close();
//            }
//        }
//
//        return result;
//    }
}
