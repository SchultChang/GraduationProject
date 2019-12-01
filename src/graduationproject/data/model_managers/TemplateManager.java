/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.model_managers;

import graduationproject.controllers.TemplateManagementController;
import graduationproject.controllers.TemplateManagementController.DataOrders;
import graduationproject.data.models.Template;
import graduationproject.data.models.Template;
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
public class TemplateManager {

    private SessionFactory sessionFactory;

    public TemplateManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public int saveTemplate(Template template) {
        Session session = null;
        Transaction tx = null;
        int result = -1;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

            result = Integer.parseInt(session.save(template).toString());

            session.save(template);
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

    public List<Template> getTemplates(DataOrders order, boolean isTable) {
        Session session = null;
        Transaction tx = null;
        List<Template> result = null;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

            Criteria cri = session.createCriteria(Template.class)
                    .setProjection(
                            Projections.projectionList()
                            .add(Projections.property("id"), "id")
                            .add(Projections.property(Template.getColumnName(order)), Template.getColumnName(order)))
                    .add(Restrictions.eq("isTable", isTable));
            cri.setResultTransformer(Transformers.aliasToBean(Template.class));
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

    public List<Template> getTemplates(DataOrders order, Object prefix, boolean isTable) {
        Session session = null;
        Transaction tx = null;
        List<Template> result = null;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

            Criteria cri = session.createCriteria(Template.class)
                    .setProjection(Projections.projectionList().add(Projections.property("id"), "id")
                            .add(Projections.property(Template.getColumnName(order)), Template.getColumnName(order)))
                    .add(Restrictions.eq("isTable", isTable));
            cri.setResultTransformer(Transformers.aliasToBean(Template.class));
            if (order != DataOrders.DESCRIPTION) {
                cri.add(Restrictions.like(Template.getColumnName(order), prefix + "%"));
            }

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
    
    public Template getTemplate(int templateId) {
        Session session = null;
        Transaction tx = null;
        Template result = null;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

            Criteria cri = session.createCriteria(Template.class)
                    .add(Restrictions.eq("id", templateId));

            List<Template> resultList = cri.list();
            if (resultList.isEmpty()) {
                return result;
            }            
            result = resultList.get(0);

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
    
    public boolean updateTemplate(Template template) {
        Session session = null;
        Transaction tx = null;
        boolean result = false;
        
        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();
            
            session.update(template);
            
            tx.commit();
            result = true;
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
