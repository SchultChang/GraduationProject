/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data;

import graduationproject.data.model_managers.DeviceCPUStateManager;
import graduationproject.data.model_managers.DeviceInterfaceDynamicDataManager;
import graduationproject.data.model_managers.DeviceManager;
import graduationproject.data.model_managers.DeviceMemoryStateManager;
import graduationproject.data.model_managers.NotificationManager;
import graduationproject.data.model_managers.RecoveryQuestionManager;
import graduationproject.data.model_managers.SettingManager;
import graduationproject.data.model_managers.TemplateItemManager;
import graduationproject.data.model_managers.TemplateManager;
import graduationproject.data.model_managers.UserManager;
import java.util.Calendar;
import java.util.logging.Level;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


/**
 *
 * @author cloud
 */
public class DataManager {
    private final String HIBERNATE_CONFIG_FILE = "graduationproject/data/configs/hibernate.cfg.xml";
    SessionFactory sessionFactory;

    private UserManager userManager;
    private RecoveryQuestionManager recoveryQuestionManager;
    private SettingManager settingManager;
    private DeviceManager deviceManager;    
    private DeviceInterfaceDynamicDataManager interfaceDynamicDataManager;
    private TemplateManager templateManager;
    private TemplateItemManager templateItemManager;
    private NotificationManager notificationManager;
    private DeviceCPUStateManager deviceCpuManager;
    private DeviceMemoryStateManager deviceMemoryManager;
    
    private int activeAccountId;
    private Calendar startTime;
    
    private static DataManager instance;
    
    
    private DataManager() {
//        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);        
        this.sessionFactory = new Configuration().configure(HIBERNATE_CONFIG_FILE).buildSessionFactory();
        
        this.userManager = new UserManager(this.sessionFactory);
        this.settingManager = new SettingManager(this.sessionFactory);
//        this.recoveryQuestionManager = new RecoveryQuestionManager(this.sessionFactory);
        this.deviceManager = new DeviceManager(this.sessionFactory);
        this.interfaceDynamicDataManager = new DeviceInterfaceDynamicDataManager(this.sessionFactory);
        this.templateManager = new TemplateManager(this.sessionFactory);
        this.templateItemManager = new TemplateItemManager(this.sessionFactory);
        this.notificationManager = new NotificationManager(this.sessionFactory);
        this.deviceCpuManager = new DeviceCPUStateManager(this.sessionFactory);
        this.deviceMemoryManager = new DeviceMemoryStateManager(this.sessionFactory);
        
        this.startTime = Calendar.getInstance();
        this.startTime.add(Calendar.DATE, -1);
    }
    
    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void close() {
        if (this.sessionFactory != null) {
            this.sessionFactory.close();
        }
    }

    public Calendar getStartTime() {
        return startTime;
    }
    
    public UserManager getUserManager() {
        return userManager;
    }

    public RecoveryQuestionManager getRecoveryQuestionManager() {
        return recoveryQuestionManager;
    }
    
    public SettingManager getSettingManager() {
        return settingManager;
    }

    public DeviceManager getDeviceManager() {
        return deviceManager;
    }

    public DeviceInterfaceDynamicDataManager getInterfaceDynamicDataManager() {
        return interfaceDynamicDataManager;
    }

    public TemplateManager getTemplateManager() {
        return templateManager;
    }

    public TemplateItemManager getTemplateItemManager() {
        return templateItemManager;
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public DeviceCPUStateManager getDeviceCpuManager() {
        return deviceCpuManager;
    }

    public DeviceMemoryStateManager getDeviceMemoryManager() {
        return deviceMemoryManager;
    }
    
    public int getActiveAccountId() {
        return activeAccountId;
    }

    public void setActiveAccountId(int activeAccountId) {
        this.activeAccountId = activeAccountId;
    }

    public void onLogout() {
        this.activeAccountId = -1;
    }
}
