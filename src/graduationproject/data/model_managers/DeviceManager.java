/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.model_managers;

import graduationproject.controllers.DeviceManagementController.DataOrders;
import graduationproject.data.DataManager;
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
                            .add(Projections.property("id"), "id")
                            .add(Projections.property(Device.getColumnName(order)), Device.getColumnName(order)));
            cri.setResultTransformer(Transformers.aliasToBean(Device.class));
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

    public Device getDevice(int deviceId) {
        Session session = null;
        Transaction transaction = null;
        Device result = null;

        try {
            session = this.sessionFactory.openSession();
            transaction = session.beginTransaction();

            Criteria criteria = session.createCriteria(Device.class);
            criteria.add(Restrictions.eq("id", deviceId));
            
            List<Device> resultList = criteria.list();
            if (!resultList.isEmpty()) {
                result = (Device) criteria.list().get(0);
            }

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

    public List<Device> getDevices(DataOrders order, Object value) {
        Session session = null;
        Transaction tx = null;
        List<Device> result = null;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

            Criteria cri = session.createCriteria(Device.class)
                    .setProjection(Projections.projectionList().add(Projections.property("id"), "id")
                    .add(Projections.property(Device.getColumnName(order)), Device.getColumnName(order)));                    
            cri.setResultTransformer(Transformers.aliasToBean(Device.class));
            if (order != DataOrders.CI_IP_ADDRESS && order != DataOrders.CI_COMMUNITY) {
                cri.add(Restrictions.like(Device.getColumnName(order), value + "%"));
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
    
    public synchronized boolean updateDevice(Device device) {
        Session session = null;
        Transaction tx = null;
        boolean result = false;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();
            
            session.update(device);
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
    
    public boolean deleteDevice(int deviceId) {
        Session session = null;
        Transaction tx = null;
        boolean result = false;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();
            
            Device device = session.load(Device.class, deviceId);
            int tempSize = device.getNetworkInterfaces().size();
            for (int i = 0; i < tempSize; i++) {
                DataManager.getInstance().getInterfaceDynamicDataManager().deleteDynamicData(device.getNetworkInterfaces().get(i));
            }
            session.delete(device);
            
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