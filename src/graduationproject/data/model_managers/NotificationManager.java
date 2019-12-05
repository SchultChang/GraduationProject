/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.model_managers;

import graduationproject.data.models.Device;
import graduationproject.data.models.Notification;
import graduationproject.snmpd.helpers.NotificationParser.NotificationType;
import java.util.Calendar;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;

/**
 *
 * @author cloud
 */
public class NotificationManager {

    private SessionFactory sessionFactory;

    public NotificationManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public int saveNotification(Notification notification) {
        Session session = null;
        Transaction tx = null;
        int result = -1;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

            result = Integer.parseInt(session.save(notification).toString());

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

    public Notification getNotification(int id) {
        Session session = null;
        Transaction tx = null;
        Notification result = null;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

            result = session.find(Notification.class, id);
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

    public List<Notification> getNotifications(Calendar day) {
        Session session = null;
        Transaction tx = null;
        List<Notification> result = null;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

//            String hql = "FROM " + Notification.class.getSimpleName() + " n"
//                    + " WHERE EXTRACT(DAY FROM n.received_time) = :day"
//                    + " AND EXTRACT(MONTH FROM n.received_time) = :month"
//                    + " AND EXTRACT(YEAR FROM n.received_time) = :year";
//
//            Query query = session.createQuery(hql);
//            query.setParameter("day", day.get(Calendar.DAY_OF_MONTH));
//            query.setParameter("month", day.get(Calendar.MONTH));
//            query.setParameter("year", day.get(Calendar.YEAR));
//            result = query.list();
            Criteria cri = session.createCriteria(Notification.class)
                    .add(Restrictions.ge("receivedTime", day))
                    .addOrder(Order.desc("id"));
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

    public List<Notification> getNotifications(Device device, Calendar day, List<Object> notificationTypes) {
        Session session = null;
        Transaction tx = null;
        List<Notification> result = null;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

            Criteria cri = session.createCriteria(Notification.class);
//            cri.add(Restrictions.sqlRestriction("DAY(received_time) = ?", day.get(Calendar.DAY_OF_MONTH), IntegerType.INSTANCE))
//                    .add(Restrictions.sqlRestriction("MONTH(received_time) = ?", day.get(Calendar.MONTH), IntegerType.INSTANCE))
//                    .add(Restrictions.sqlRestriction("YEAR(received_time) = ?", day.get(Calendar.YEAR), IntegerType.INSTANCE))
//                    .add(Restrictions.eq("device", device))
//                    .add(Restrictions.in("notificationType", notificationTypes));

            cri.add(Restrictions.ge("receivedTime", day))
                    .add(Restrictions.in("notificationType", notificationTypes))
                    .addOrder(Order.desc("id"));
            if (device == null) {
                cri.add(Restrictions.isNull("device"));
            } else {
                cri.add(Restrictions.eq("device", device));
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

    public List<Notification> getNotifications(Calendar day, List<Object> notificationTypes) {
        Session session = null;
        Transaction tx = null;
        List<Notification> result = null;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

//            Criteria cri = session.createCriteria(Notification.class, "noti");
//            cri.createAlias("noti.device", "device")
//                    .add(Restrictions.sqlRestriction("DAY(received_time) = ?", day.get(Calendar.DAY_OF_MONTH), IntegerType.INSTANCE))
//                    .add(Restrictions.sqlRestriction("MONTH(received_time) = ?", day.get(Calendar.MONTH), IntegerType.INSTANCE))
//                    .add(Restrictions.sqlRestriction("YEAR(received_time) = ?", day.get(Calendar.YEAR), IntegerType.INSTANCE))
//                    .add(Restrictions.like("device.label", deviceInfo + "%"))
//                    .add(Restrictions.in("notificationType", notificationTypes));
            Criteria cri = session.createCriteria(Notification.class);
            cri.add(Restrictions.ge("receivedTime", day))
                    .add(Restrictions.in("notificationType", notificationTypes))
                    .addOrder(Order.desc("id"));
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

    public List<Notification> getNotifications(Calendar day, String deviceInfo, List<Object> notificationTypes) {
        Session session = null;
        Transaction tx = null;
        List<Notification> result = null;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

//            Criteria cri = session.createCriteria(Notification.class, "noti");
//            cri.createAlias("noti.device", "device")
//                    .add(Restrictions.sqlRestriction("DAY(received_time) = ?", day.get(Calendar.DAY_OF_MONTH), IntegerType.INSTANCE))
//                    .add(Restrictions.sqlRestriction("MONTH(received_time) = ?", day.get(Calendar.MONTH), IntegerType.INSTANCE))
//                    .add(Restrictions.sqlRestriction("YEAR(received_time) = ?", day.get(Calendar.YEAR), IntegerType.INSTANCE))
//                    .add(Restrictions.like("device.label", deviceInfo + "%"))
//                    .add(Restrictions.in("notificationType", notificationTypes));
            Criteria cri = session.createCriteria(Notification.class, "notification");
            cri.createAlias("notification.device", "device", Criteria.LEFT_JOIN);
            cri.add(Restrictions.ge("receivedTime", day))
                    .add(Restrictions.like("device.label", deviceInfo + "%"))
                    .add(Restrictions.in("notificationType", notificationTypes))
                    .addOrder(Order.desc("id"));
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
