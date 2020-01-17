/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.model_managers;

import graduationproject.data.models.DeviceInterfaceDynamicData;
import graduationproject.data.models.DeviceNetworkInterface;
import java.util.Calendar;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;

/**
 *
 * @author cloud
 */
public class DeviceInterfaceDynamicDataManager {

    private SessionFactory sessionFactory;

    public DeviceInterfaceDynamicDataManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public int saveDynamicData(DeviceInterfaceDynamicData dynamicData) {
        Session session = null;
        Transaction tx = null;
        int result = -1;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

//           dynamicData.setNetworkInterface(owningInterface);
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

    //getting the newest updated data    
    public DeviceInterfaceDynamicData getDynamicData(DeviceNetworkInterface networkInterface) {
        Session session = null;
        Transaction tx = null;
        DeviceInterfaceDynamicData result = null;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

            DetachedCriteria maxId = DetachedCriteria.forClass(DeviceInterfaceDynamicData.class)
                    .setProjection(Projections.max("id"))
                    .add(Restrictions.eq("networkInterface", networkInterface));
            Criteria cri = session.createCriteria(DeviceInterfaceDynamicData.class)
                    .add(Property.forName("id").eq(maxId));

            List<DeviceInterfaceDynamicData> resultList = cri.list();
            if (!resultList.isEmpty()) {
                result = (DeviceInterfaceDynamicData) cri.list().get(0);
            }

            tx.commit();
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

    public boolean deleteDynamicData(DeviceNetworkInterface networkInterface) {
        Session session = null;
        Transaction tx = null;
        boolean result = false;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

            String hql = "delete from " + DeviceInterfaceDynamicData.class.getSimpleName()
                    + " where networkInterface=:networkInterface";
            Query query = session.createQuery(hql);
            query.setParameter("networkInterface", networkInterface);
            query.executeUpdate();

            tx.commit();
            result = true;
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

    public List<DeviceInterfaceDynamicData> getDeviceDynamicData(DeviceNetworkInterface networkInterface, Calendar startTime, Calendar endTime) {
        Session session = null;
        Transaction tx = null;
        List<DeviceInterfaceDynamicData> result = null;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

            Criteria cri = session.createCriteria(DeviceInterfaceDynamicData.class);
            cri.add(Restrictions.ge("updatedTime", startTime))
                    .add(Restrictions.le("updatedTime", endTime));
            if (networkInterface != null) {
                cri.add(Restrictions.eq("networkInterface", networkInterface));
            }

            result = cri.list();
            tx.commit();
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

    public void renewDeviceInterfaceDynamicDataTable() {
        Session session = null;
//        Transaction tx = null;

        try {
            session = this.sessionFactory.openSession();
//            tx = session.beginTransaction();

            NativeQuery query = session.createSQLQuery("TRUNCATE NETWORK_INTERFACE_DYNAMIC_DATA");
            query.executeUpdate();
//            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
//            tx.rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

}
