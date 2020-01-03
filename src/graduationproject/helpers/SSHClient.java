/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.helpers;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author cloud
 */
public class SSHClient {

    private static SSHClient instance;
    Properties sessionConfig;

    private Session session;
    private Channel channel;
    private InputStream inputStream;
    private PrintStream printStream;

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
        sessionConfig.setProperty("kex", "diffie-hellman-group1-sha1");
        sessionConfig.setProperty("StrictHostKeyChecking", "no");
    }

    public boolean createSession(String username, String password, String hostAddress, String port) {
        this.close();

        try {
            this.session = jsch.getSession(username, hostAddress, Integer.parseInt(port));
            this.session.setPassword(password);
            this.session.setConfig(this.sessionConfig);
            this.session.connect();

            this.channel = this.session.openChannel("shell");
            this.channel.connect();

            this.inputStream = this.channel.getInputStream();
            this.printStream = new PrintStream(this.channel.getOutputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;

    }

    public String sendCommand(String command) {
        StringBuilder resultCollector = new StringBuilder();

        try {
            this.printStream.println(command);
            this.printStream.flush();
            Thread.sleep(1000);        //wait for response from ssh server
            
            byte[] temp = new byte[1024];
            while (inputStream.available() > 0) {
                int j = inputStream.read(temp, 0, 1024);
                if (j < 0) {
                    break;
                }
                resultCollector.append(new String(temp, 0, j));
            }

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
            if (this.channel != null) {
                this.channel.disconnect();
            }
            this.session.disconnect();
            this.session = null;
        }
    }
}
