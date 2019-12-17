/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.helpers;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cloud
 */
public class SSHClient {

    private static SSHClient instance;
    Properties sessionConfig;

    private Session session;
    private JSch jsch;

    public static SSHClient getInstance() {
        if (instance == null) {
            instance = new SSHClient();
        }
        return instance;
    }

    private SSHClient() {
        this.jsch = new JSch();
        try {
            jsch.setKnownHosts("");
        } catch (JSchException ex) {
            ex.printStackTrace();
        }

        sessionConfig = new Properties();
        sessionConfig.setProperty("StrictHostKeyChecking", "no");
    }

    public boolean createSession(String username, String password, String hostAddress, String port) {
        this.close();
        try {
            this.session = jsch.getSession(username, hostAddress, Integer.parseInt(port));
            this.session.setPassword(password);
            this.session.setConfig(this.sessionConfig);
            this.session.connect();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public String sendCommand(String command) {
        ChannelExec channel = null;
        StringBuilder resultCollector = new StringBuilder();

        try {
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);

            InputStream inputStream = channel.getInputStream();
            channel.connect();

            int resultByte = inputStream.read();
            while (resultByte != 0xffffffff) {
                resultCollector.append((char) resultByte);
                resultByte = inputStream.read();
            }

            channel.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
            if (channel != null) {
                channel.disconnect();
            }
            return null;
        }

        return resultCollector.toString();
    }

    public void close() {
        if (this.session != null) {
            this.session.disconnect();
            this.session = null;
        }
    }
}
