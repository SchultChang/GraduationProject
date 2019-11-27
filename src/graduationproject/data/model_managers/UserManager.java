/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.model_managers;

import graduationproject.data.DataManager;
import graduationproject.data.models.RecoveryQuestion;
import graduationproject.data.models.User;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author cloud
 */
public class UserManager {
    private SessionFactory sessionFactory;

    public UserManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public int createUser(String account, String password,
            String name, int age, String position, String email, String phone, List<RecoveryQuestion> queryList) {
        Session session = null;
        Transaction transaction = null;
        int result = -1;

        try {
            session = this.sessionFactory.openSession();
            transaction = session.beginTransaction();

            result = Integer.parseInt(session.save(new User(account, password, name, age, position, email, phone, queryList)).toString());

            transaction.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            transaction.rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return result;
    }

    public List<User> getUsers(String account) {
        Session session = null;
        Transaction transaction = null;
        List<User> result = null;

        try {
            session = this.sessionFactory.openSession();
            transaction = session.beginTransaction();

            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("account", account));
            result = criteria.list();

            transaction.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            transaction.rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return result;
    }

    public User getUser(int accountId) {
        Session session = null;
        Transaction transaction = null;
        User result = null;

        try {
            session = this.sessionFactory.openSession();
            transaction = session.beginTransaction();

            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("id", accountId));
            result = (User) criteria.list().get(0);

            transaction.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            transaction.rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return result;
    }

    public List<User> getUsers(boolean hasPasswordRemembered) {
        Session session = null;
        Transaction transaction = null;
        List<User> result = null;

        try {
            session = this.sessionFactory.openSession();
            transaction = session.beginTransaction();

            Criteria cri = session.createCriteria(User.class, "user");
            cri.createAlias("user.setting", "setting");
            cri.add(Restrictions.eq("setting.hasPasswordRemembered", hasPasswordRemembered));
            result = cri.list();

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return result;
    }
    
    public boolean updateUser(User user) {
        Session session = null;
        Transaction transaction = null;
        boolean result = false;

        try {
            session = this.sessionFactory.openSession();
            transaction = session.beginTransaction();
            
            session.update(user);
            transaction.commit();
            
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return result;
        
    }

}
