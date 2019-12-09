/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.model_managers;

import graduationproject.data.models.DeviceCPUState;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

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
}
