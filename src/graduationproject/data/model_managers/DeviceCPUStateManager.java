/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.model_managers;

import graduationproject.data.models.Device;
import graduationproject.data.models.DeviceCPUState;
import graduationproject.data.models.DeviceInterfaceDynamicData;
import java.util.Calendar;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author cloud
 */
public class DeviceCPUStateManager {

    private SessionFactory sessionFactory;

    public DeviceCPUStateManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public int saveDeviceCPUState(DeviceCPUState cpuState) {
        Session session = null;
        Transaction tx = null;
        int result = -1;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

            result = Integer.parseInt(session.save(cpuState).toString());

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
            
            Criteria cri = session.createCriteria(DeviceCPUState.class);
            cri.setProjection(Projections.distinct(Projections.property("hrDeviceId")))
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
    
    public DeviceCPUState getDeviceCPUState(int hrDeviceId, Device device) {
        Session session = null;
        Transaction tx = null;
        DeviceCPUState result = null;
        
        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();
            
            DetachedCriteria maxId = DetachedCriteria.forClass(DeviceCPUState.class)
                    .setProjection(Projections.max("id"))
                    .add(Restrictions.eq("device", device))
                    .add(Restrictions.eq("hrDeviceId", hrDeviceId));
            
            Criteria cri = session.createCriteria(DeviceCPUState.class)
                    .add(Property.forName("id").eq(maxId));

            List<Object> temp = cri.list();
            if (!temp.isEmpty())            {
                result = (DeviceCPUState) temp.get(0);
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
    
    public List<Integer> getDeviceCPULoad(Device device, Calendar startTime, Calendar endTime, Integer choice) {
        Session session = null;
        Transaction tx = null;
        List<Integer> result = null;
        
        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

            Criteria cri = session.createCriteria(DeviceCPUState.class);
            cri.setProjection(Projections.property("cpuLoad"))
                    .add(Restrictions.eq("device", device))
                    .add(Restrictions.ge("updatedTime", startTime))
                    .add(Restrictions.le("updatedTime", endTime))
                    .add(Restrictions.eq("hrDeviceId", choice.intValue()));

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
    
    public double getAverageCpuLoad(Device device, Calendar startTime, Calendar endTime) {
        Session session = null;
        Transaction tx = null;
        double result = 0.0;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

            Criteria cri = session.createCriteria(DeviceCPUState.class);
            cri.setProjection(Projections.avg("cpuLoad"))
                    .add(Restrictions.eq("device", device))
                    .add(Restrictions.ge("updatedTime", startTime))
                    .add(Restrictions.le("updatedTime", endTime));

            Object temp = cri.uniqueResult();
            if (temp != null) {
                result = (double) temp;
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
}
