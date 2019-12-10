/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.model_managers;

import graduationproject.data.models.Device;
import graduationproject.data.models.DeviceCPUState;
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
