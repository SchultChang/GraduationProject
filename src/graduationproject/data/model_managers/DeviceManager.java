/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.model_managers;

import graduationproject.controllers.DeviceManagementController.DataOrders;
import graduationproject.data.models.Device;
import graduationproject.data.models.User;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;

/**
 *
 * @author cloud
 */
public class DeviceManager {

    private SessionFactory sessionFactory;

    public DeviceManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public int saveDevice(Device device) {
        Session session = null;
        Transaction tx = null;
        int result = -1;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

            result = Integer.parseInt(session.save(device).toString());

            session.save(device);
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

    public List<Device> getDevices(DataOrders order) {
        Session session = null;
        Transaction tx = null;
        List<Device> result = null;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

            Criteria cri = session.createCriteria(Device.class)
                    .setProjection(
                            Projections.projectionList()
                            .add(Projections.property("id"))
                            .add(Projections.property(Device.getColumnName(order))));
            //cri.setResultTransformer(Transformers.aliasToBean(Device.class));
            //result = cri.list();
            List<Object[]> values = cri.list();
            result = new ArrayList<Device>();
            for (Object[] value : values) {
                Device temp = new Device();
                temp.setId((int) value[0]);
                temp.setData(order, value[1]);
                result.add(temp);
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

    public Device getDevice(int deviceId) {
        Session session = null;
        Transaction transaction = null;
        Device result = null;

        try {
            session = this.sessionFactory.openSession();
            transaction = session.beginTransaction();

            Criteria criteria = session.createCriteria(Device.class);
            criteria.add(Restrictions.eq("id", deviceId));
            result = (Device) criteria.list().get(0);

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
}
