/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.model_managers;

import graduationproject.data.models.DeviceAvgBandwidthUsage;
import graduationproject.data.models.DeviceAvgCpuUsage;
import graduationproject.data.models.DeviceNetworkInterface;
import java.util.Calendar;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author cloud
 */
public class DeviceBandwidthUsageManager {

    private SessionFactory sessionFactory;

    public DeviceBandwidthUsageManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public int saveAvgBandwidthUsage(DeviceAvgBandwidthUsage bandwidthUsage) {
        Session session = null;
        Transaction tx = null;
        int result = -1;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

            result = Integer.parseInt(session.save(bandwidthUsage).toString());

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

    public List<DeviceAvgBandwidthUsage> getAvgBandwidthUsages(DeviceNetworkInterface networkInterface,
            Calendar startTime, Calendar endTime) {
        Session session = null;
        Transaction tx = null;
        List<DeviceAvgBandwidthUsage> result = null;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

            Criteria cri = session.createCriteria(DeviceAvgBandwidthUsage.class);
            cri.add(Restrictions.ge("time", startTime))
                    .add(Restrictions.le("time", endTime));
            if (networkInterface != null) {
                cri.add(Restrictions.eq("networkInterface", networkInterface));
            }

            result = cri.list();
            
//            System.out.println("NETWORK TIME " + startTime.getTime());
//            System.out.println("NETWORK TIME " + startTime.get(Calendar.HOUR_OF_DAY));
//            System.out.println("NETWORK INT " + networkInterface.getId());       
//            System.out.println(result.size());
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

}
