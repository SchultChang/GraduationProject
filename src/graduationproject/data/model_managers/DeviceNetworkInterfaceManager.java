/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.model_managers;

import org.hibernate.SessionFactory;

/**
 *
 * @author cloud
 */
public class DeviceNetworkInterfaceManager {
    
    private SessionFactory sessionFactory;

    public DeviceNetworkInterfaceManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
}
