/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data;

import graduationproject.data.model_managers.UserManager;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


/**
 *
 * @author cloud
 */
public class DataManager {
    private final String HIBERNATE_CONFIG_FILE = "graduationproject.data.configs.hibernate.cfg.xml";
    SessionFactory sessionFactory;

    private UserManager userManager;
    
    private static DataManager instance;
    
    
    private DataManager() {
        this.sessionFactory = new Configuration().configure(HIBERNATE_CONFIG_FILE).buildSessionFactory();
        
        this.userManager = new UserManager(this.sessionFactory);
    }
    
    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public UserManager getUserManager() {
        return userManager;
    }
    
    
}
