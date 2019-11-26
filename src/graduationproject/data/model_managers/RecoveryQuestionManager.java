/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.model_managers;

import graduationproject.data.models.RecoveryQuestion;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author cloud
 */
public class RecoveryQuestionManager {
    private SessionFactory sessionFactory;

    public RecoveryQuestionManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    public int countQuestions(int id, String answer) {
        Session session = null;
        Transaction transaction = null;
        Integer result = -1;
        
        try {
            session = this.sessionFactory.openSession();
            transaction = session.beginTransaction();
            
            Criteria cri = session.createCriteria(RecoveryQuestion.class);
            cri.add(Restrictions.eq("id", id));
            cri.add(Restrictions.eq("answer", answer));
            result = (Integer) cri.uniqueResult(); 
            
            transaction.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            transaction.rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        
        return result.intValue();
    }
}
