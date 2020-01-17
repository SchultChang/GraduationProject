/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.model_managers;

import graduationproject.data.models.Device;
import graduationproject.data.models.DeviceAvgMemoryUsage;
import graduationproject.data.models.DeviceMemoryState;
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
public class DeviceMemoryUsageManager {

    private SessionFactory sessionFactory;

    public DeviceMemoryUsageManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public int saveAvgMemoryUsage(DeviceAvgMemoryUsage memoryUsage) {
        Session session = null;
        Transaction tx = null;
        int result = -1;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

            result = Integer.parseInt(session.save(memoryUsage).toString());

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
    
    public List<DeviceAvgMemoryUsage> getAvgMemoryUsages(Device device, Calendar startTime, Calendar endTime, String type) {
        Session session = null;
        Transaction tx = null;
        List<DeviceAvgMemoryUsage> result = null;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

            Criteria cri = session.createCriteria(DeviceAvgMemoryUsage.class);
            cri.add(Restrictions.eq("device", device))
                    .add(Restrictions.ge("time", startTime))
                    .add(Restrictions.le("time", endTime))
                    .add(Restrictions.eq("type", type));
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
        
}
