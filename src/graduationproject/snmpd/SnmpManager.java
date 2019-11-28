/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.snmpd;

import java.io.IOException;
import org.soulwing.snmp.Mib;
import org.soulwing.snmp.MibFactory;
import org.soulwing.snmp.ModuleParseException;
import org.soulwing.snmp.SimpleSnmpV2cTarget;
import org.soulwing.snmp.SnmpContext;
import org.soulwing.snmp.SnmpFactory;
import org.soulwing.snmp.SnmpTarget;

/**
 *
 * @author cloud
 */
public class SnmpManager {

    private final String[] MIB_MODULES = {"SNMPv2-MIB", "IP-MIB", "IF-MIB", "RFC1213-MIB"};
    private Mib mib;

    private static SnmpManager instance;

    private SnmpManager() {
        initMib();
    }

    private void initMib() {
        this.mib = MibFactory.getInstance().newMib();
        for (String module : MIB_MODULES) {
            try {
                this.mib.load(module);
            } catch (ModuleParseException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static SnmpManager getInstance() {
        if (instance == null) {
            instance = new SnmpManager();
        }
        return instance;
    }

    public SnmpTarget createTarget(SnmpVersion version, String ipAddress, String community) {
//        if (version == SnmpVersion.VERSION_1) {
//            return null;
//        }
//        if (version == SnmpVersion.VERSION_2) {
//            return null;
//        }
//        if (version == SnmpVersion.VERSION_2_COMMUNITY) {
            SimpleSnmpV2cTarget target = new SimpleSnmpV2cTarget();
            target.setAddress(ipAddress);
            target.setCommunity(community);
            return target;
//        }
//        if (version == SnmpVersion.VERSION_3) {
//            return null;
//        }
//        return null;
    }
    
    public SnmpContext createContext(String version, String ipAddress, String community) {
        SnmpVersion normalizedVersion = this.parseVersionString(version);
        SnmpTarget target = this.createTarget(normalizedVersion, ipAddress, community);
        return SnmpFactory.getInstance().newContext(target, mib);
    }

    public SnmpVersion parseVersionString(String input) {
        SnmpVersion[] temp = {SnmpVersion.VERSION_1, SnmpVersion.VERSION_2, SnmpVersion.VERSION_2_COMMUNITY, SnmpVersion.VERSION_3};
        for (int i = 0; i < temp.length; i++) {
            if (temp[i].getValue().equalsIgnoreCase(input)) {
                return temp[i];
            }
        }
        return null;
    }

    public enum SnmpVersion {
        VERSION_1("v1"),
        VERSION_2("v2"),
        VERSION_2_COMMUNITY("v2c"),
        VERSION_3("v3");

        private String value;

        private SnmpVersion(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }
}
