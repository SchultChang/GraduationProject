/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.model_managers;

import graduationproject.controllers.DeviceManagementController.DataOrders;
import graduationproject.data.DataManager;
import graduationproject.data.models.Device;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
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
    
    public List<Device> getDevices() {
        Session session = null;
        Transaction tx = null;
        List<Device> result = null;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

            Criteria cri = session.createCriteria(Device.class);
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

    public List<Object> getDeviceIds() {
        Session session = null;
        Transaction tx = null;
        List<Object> result = null;

        try {
            session = this.sessionFactory.openSession();
            tx = session.beginTransaction();

            Criteria cri = session.createCriteria(Device.class);
            cri.setProjection(Projections.property("id"));
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
            if (session.isOpen()) {
                transaction = session.beginTransaction();

                Criteria criteria = session.createCriteria(Device.class);
                criteria.add(Restrictions.eq("id", deviceId));

                List<Device> resultList = criteria.list();
                if (!resultList.isEmpty()) {
                    result = (Device) criteria.list().get(0);
                }

                transaction.commit();
            }
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
            DataManager.getInstance().getNotificationManager().deleteNotifications(device);
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

    public Device getDevice(DataOrders order, String value) {
        Session session = null;
        Transaction tx = null;
        Device result = null;
        List<Device> resultList = null;

        if (value != null) {
            try {
                session = this.sessionFactory.openSession();
                tx = session.beginTransaction();

                Criteria cri;
                if (order == DataOrders.LABEL) {
                    cri = session.createCriteria(Device.class);
                    cri.add((Restrictions.eq("label", value)));
                    resultList = cri.list();
                }

                if (resultList != null && !resultList.isEmpty()) {
                    result = resultList.get(0);
                }
                if (order == DataOrders.CI_IP_ADDRESS) {
                    cri = session.createCriteria(Device.class, "device");
                    cri.createAlias("device.contactInterface", "contactInterface");
                    cri.add(Restrictions.eq("contactInterface.ipAddress", value));
                    resultList = cri.list();

                    if (!resultList.isEmpty()) {
                        result = resultList.get(0);
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

        return result;
    }

    public Device getDevice(String macAddress) {
        Session session = null;
        Transaction tx = null;
        Device result = null;
        List<Device> resultList = null;

        if (macAddress != null) {
            try {
                session = this.sessionFactory.openSession();
                tx = session.beginTransaction();

                String hql = "SELECT d FROM " + Device.class.getSimpleName() + " d JOIN d.networkInterfaces  i WHERE i.macAddress=:macAddress";
                Query query = session.createQuery(hql);
                query.setParameter("macAddress", macAddress);

                resultList = query.list();
                if (!resultList.isEmpty()) {
                    result = resultList.get(0);
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

        return result;
    }

}
