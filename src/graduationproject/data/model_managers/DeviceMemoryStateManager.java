/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.model_managers;

import graduationproject.data.models.Device;
import graduationproject.data.models.DeviceMemoryState;
import graduationproject.data.models.DeviceMemoryState;
import java.util.Calendar;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
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

    public List<String> getDeviceMemoryInfo(Device device) {
        Session session = null;
        Transaction tx = null;
        List<String> result = null;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

            Criteria cri = session.createCriteria(DeviceMemoryState.class);
            cri.setProjection(Projections.distinct(Projections.property("description")))
                    .add(Restrictions.eq("device", device));

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

    public DeviceMemoryState getDeviceMemoryState(String info, Device device) {
        Session session = null;
        Transaction tx = null;
        DeviceMemoryState result = null;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

            DetachedCriteria maxId = DetachedCriteria.forClass(DeviceMemoryState.class)
                    .setProjection(Projections.max("id"))
                    .add(Restrictions.eq("device", device))
                    .add(Restrictions.eq("description", info));

            Criteria cri = session.createCriteria(DeviceMemoryState.class)
                    .add(Property.forName("id").eq(maxId));

            List<Object> temp = cri.list();
            if (!temp.isEmpty()) {
                result = (DeviceMemoryState) temp.get(0);
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

    public List<DeviceMemoryState> getDeviceMemoryStates(Device device, Calendar startTime, Calendar endTime, String type) {
        Session session = null;
        Transaction tx = null;
        List<DeviceMemoryState> result = null;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

            Criteria cri = session.createCriteria(DeviceMemoryState.class);
            cri.add(Restrictions.eq("device", device))
                    .add(Restrictions.ge("updatedTime", startTime))
                    .add(Restrictions.le("updatedTime", endTime))
                    .add(Restrictions.eq("type", type));
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

    public List<DeviceMemoryState> getDeviceMemoryStates(Calendar startTime, Calendar endTime, String type) {
        Session session = null;
        Transaction tx = null;
        List<DeviceMemoryState> result = null;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

            Criteria cri = session.createCriteria(DeviceMemoryState.class);
            cri.add(Restrictions.ge("updatedTime", startTime))
                    .add(Restrictions.le("updatedTime", endTime));
            if (type != null) {
                cri.add(Restrictions.eq("type", type));
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

    public void renewDeviceMemoryStateTable(List<DeviceMemoryState> memoryStates) {
        Session session = null;
        Transaction tx = null;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

            NativeQuery query = session.createNativeQuery("TRUNCATE DEVICE_MEMORY_STATES");
            query.executeUpdate();

            for (DeviceMemoryState memoryState : memoryStates) {
                try {
                    session.save(memoryState);
                } catch (Exception e) {
                    memoryState.displayInfo();
                }
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
    }

}
