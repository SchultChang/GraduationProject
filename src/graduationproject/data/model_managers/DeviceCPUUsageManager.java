/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.model_managers;

import graduationproject.data.models.Device;
import graduationproject.data.models.DeviceAvgCpuUsage;
import graduationproject.data.models.DeviceAvgMemoryUsage;
import java.util.Calendar;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author cloud
 */
public class DeviceCPUUsageManager {

    private SessionFactory sessionFactory;

    public DeviceCPUUsageManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public int saveAvgCPUUsage(DeviceAvgCpuUsage cpuUsage) {
        Session session = null;
        Transaction tx = null;
        int result = -1;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

            result = Integer.parseInt(session.save(cpuUsage).toString());

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

    public List<DeviceAvgCpuUsage> getAvgCPUUsages(Device device,
            Calendar startTime, Calendar endTime, int cpuId) {
        Session session = null;
        Transaction tx = null;
        List<DeviceAvgCpuUsage> result = null;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

            Criteria cri = session.createCriteria(DeviceAvgCpuUsage.class);
            cri.add(Restrictions.ge("time", startTime))
                    .add(Restrictions.eq("device", device))
                    .add(Restrictions.le("time", endTime))
                    .add(Restrictions.eq("cpuId", cpuId));

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

    public List<Integer> getDeviceCpuDeviceIds(Device device) {
        Session session = null;
        Transaction tx = null;
        List<Integer> result = null;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

            Criteria cri = session.createCriteria(DeviceAvgCpuUsage.class);
            cri.setProjection(Projections.distinct(Projections.property("cpuId")))
                    .add(Restrictions.eq("device", device));

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
