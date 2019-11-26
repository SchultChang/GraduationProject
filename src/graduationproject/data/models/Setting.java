/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.models;

/**
 *
 * @author cloud
 */
public class Setting {
    private int id;
    private String language;
    private String theme;
    private int deviceCheckingPeriod;
    private int interfaceCheckingPeriod;
    private int monitoringQueryPeriod;
    private int maxTableIndex;    
    private boolean hasPasswordRemembered;
    
    public Setting() {
        this.language = Language.EN;
        this.theme = Theme.MT;
        
        this.deviceCheckingPeriod = 5;
        this.interfaceCheckingPeriod = 5;
        this.monitoringQueryPeriod = 10;        
        this.maxTableIndex = 10;               
        
        this.hasPasswordRemembered = false;
    }
    
    public class Language {
        public static final String EN = "English";
        public static final String VN = "Vietnamese";
        public static final String RU = "Rusia";
    }    
    
    public class Theme {
        public static final String LINUX_DEFAULT = "GTK+";
        public static final String MT = "Metal";
        public static final String NIM = "Nimbus";
        public static final String WIN_DEFAULT = "Classic";        
    }

    public int getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public String getTheme() {
        return theme;
    }

    public int getDeviceCheckingPeriod() {
        return deviceCheckingPeriod;
    }

    public int getInterfaceCheckingPeriod() {
        return interfaceCheckingPeriod;
    }

    public int getMonitoringQueryPeriod() {
        return monitoringQueryPeriod;
    }

    public int getMaxTableIndex() {
        return maxTableIndex;
    }

    public boolean isHasPasswordRemembered() {
        return hasPasswordRemembered;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setDeviceCheckingPeriod(int deviceCheckingPeriod) {
        this.deviceCheckingPeriod = deviceCheckingPeriod;
    }

    public void setInterfaceCheckingPeriod(int interfaceCheckingPeriod) {
        this.interfaceCheckingPeriod = interfaceCheckingPeriod;
    }

    public void setMonitoringQueryPeriod(int monitoringQueryPeriod) {
        this.monitoringQueryPeriod = monitoringQueryPeriod;
    }

    public void setMaxTableIndex(int maxTableIndex) {
        this.maxTableIndex = maxTableIndex;
    }

    public void setHasPasswordRemembered(boolean hasPasswordRemembered) {
        this.hasPasswordRemembered = hasPasswordRemembered;
    }
    
    
}
