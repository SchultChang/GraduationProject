/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.model_managers;

import graduationproject.data.models.Setting;
import graduationproject.data.models.TemplateItem;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author cloud
 */
public class TemplateItemManager {

    private SessionFactory sessionFactory;

    public TemplateItemManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public boolean updateTemplateItem(TemplateItem item) {
        Session session = null;
        Transaction transaction = null;
        boolean result = false;

        try {
            session = this.sessionFactory.openSession();
            transaction = session.beginTransaction();

            session.update(item);

            transaction.commit();
            result = true;
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
