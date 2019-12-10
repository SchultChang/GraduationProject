/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.model_managers;

import graduationproject.data.models.Device;
import graduationproject.data.models.DeviceMemoryState;
import java.util.Calendar;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

/**
 *
 * @author cloud
 */
public class DeviceMemoryStateManager {
    
    private SessionFactory sessionFactory;
    
    public DeviceMemoryStateManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    public int saveDeviceMemoryState(DeviceMemoryState memoryState) {
        Session session = null;
        Transaction tx = null;
        int result = -1;
        
        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();
            
            result = Integer.parseInt(session.save(memoryState).toString());
            
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
    
    public List<DeviceMemoryState> getDeviceMemoryStates(Device device, Calendar startTime, Calendar endTime, String type) {
        Session session = null;
        Transaction tx = null;
        List<DeviceMemoryState> result = null;
        
        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

            Criteria cri = session.createCriteria(DeviceMemoryState.class);
            cri.setProjection(Projections.projectionList()
                    .add(Projections.property("totalSize"), "totalSize")
                    .add(Projections.property("usedSize"), "usedSize"))
                    .add(Restrictions.eq("device", device))
                    .add(Restrictions.ge("updatedTime", startTime))
                    .add(Restrictions.le("updatedTime", endTime))
                    .setResultTransformer(Transformers.aliasToBean(DeviceMemoryState.class));
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
