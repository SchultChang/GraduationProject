/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.model_managers;

import graduationproject.data.models.DeviceMemoryState;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

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
   
}
