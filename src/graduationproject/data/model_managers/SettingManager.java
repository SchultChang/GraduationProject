/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.model_managers;

import graduationproject.data.models.Setting;
import graduationproject.data.models.User;
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
public class SettingManager {

    private SessionFactory sessionFactory;

    public boolean updateSetting(Setting setting, boolean hasPasswordRemembered) {
        Session session = null;
        Transaction transaction = null;
        boolean result = false;
        
        try {
            session = this.sessionFactory.openSession();
            transaction = session.beginTransaction();

            setting.setHasPasswordRemembered(hasPasswordRemembered);
            session.update(setting);
            
            result = true;
            transaction.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            result = false;
            transaction.rollback();            
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return result;
    }
}
