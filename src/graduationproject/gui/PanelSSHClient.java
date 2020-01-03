/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.gui;

import graduationproject.controllers.DeviceManagementController;
import graduationproject.controllers.TemplateManagementController;
import graduationproject.helpers.SSHClient;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import org.netbeans.lib.awtextra.AbsoluteConstraints;

/**
 *
 * @author cloud
 */
public class PanelSSHClient extends JPanel {

    private final String MESSAGE_FOR_CONNECTION_FAILED = "The system could not connect to remote host. Please try again later.";

    private JButton buttonConnect;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel labelClose1;
    private JLabel labelClose2;
    private JLabel labelHostInfo;
    private JLabel labelPort;
    private JPanel panelCommand;
    private JPanel panelInformation;
    private JScrollPane scrollpane1;
    private JTextArea tareaResult;
    private JTextField tfieldCommand;
    private JTextField tfieldHostAddress;
    private JPasswordField tfieldPassword;
    private JTextField tfieldPort;
    private JTextField tfieldUserName;

    private ActionListener listenerButton;
    private MouseAdapter listenerLabel;
    private KeyAdapter listenerField;

    private AreaDisplayTextController areaTextDisplayer;

    public PanelSSHClient() {
        initComponents();
        initListeners();

        this.areaTextDisplayer = new AreaDisplayTextController();
    }

    private void initComponents() {
        panelCommand = new JPanel();
        scrollpane1 = new JScrollPane();
        tareaResult = new JTextArea();
        tfieldCommand = new JTextField();
        labelClose1 = new JLabel();
        labelHostInfo = new JLabel();
        panelInformation = new JPanel();
        label1 = new JLabel();
        tfieldHostAddress = new JTextField();
        label3 = new JLabel();
        label2 = new JLabel();
        tfieldPassword = new JPasswordField();
        tfieldUserName = new JTextField();
        labelPort = new JLabel();
        tfieldPort = new JTextField();
        buttonConnect = new JButton();
        labelClose2 = new JLabel();

        panelCommand.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelCommand.setBackground(Color.white);
        panelCommand.setVisible(false);
        panelCommand.setEnabled(false);

        tareaResult.setEditable(false);
        tareaResult.setBackground(new java.awt.Color(58, 58, 87));
        tareaResult.setColumns(20);
        tareaResult.setFont(new java.awt.Font("SansSerif", 0, 12));
        tareaResult.setForeground(java.awt.Color.white);
        tareaResult.setLineWrap(true);
        tareaResult.setRows(500);
        tareaResult.setPreferredSize(new java.awt.Dimension(500, 2000));
        scrollpane1.setViewportView(tareaResult);

        panelCommand.add(scrollpane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 500, 420));
        panelCommand.setPreferredSize(new Dimension(500, 500));
        panelCommand.setBackground(Color.white);
        panelCommand.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        tfieldCommand.setBackground(new java.awt.Color(59, 59, 99));
        tfieldCommand.setFont(new java.awt.Font("SansSerif", 0, 14));
        tfieldCommand.setForeground(java.awt.Color.white);
        tfieldCommand.setText("");
        tfieldCommand.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        tfieldCommand.setCaretColor(java.awt.Color.white);
        panelCommand.add(tfieldCommand, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 450, 500, 50));

        labelClose1.setIcon(new ImageIcon(getClass().getResource("/resources/icon_close_30.png")));
        panelCommand.add(labelClose1, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 0, -1, 30));

        labelHostInfo.setText("Host");
        panelCommand.add(labelHostInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 420, 30));

        panelInformation.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelInformation.setPreferredSize(new Dimension(500, 500));
        panelInformation.setBackground(Color.white);
        panelInformation.setVisible(false);
        panelInformation.setEnabled(false);
        panelInformation.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        label1.setFont(new java.awt.Font("SansSerif", 1, 15));
        label1.setText("User Name:");
        panelInformation.add(label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 100, -1, -1));

        tfieldHostAddress.setFont(new java.awt.Font("SansSerif", 0, 15));
        panelInformation.add(tfieldHostAddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 300, 298, 40));

        label3.setFont(new java.awt.Font("SansSerif", 1, 15));
        label3.setText("Host Address:");
        panelInformation.add(label3, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 270, -1, -1));

        label2.setFont(new java.awt.Font("SansSerif", 1, 15));
        label2.setText("Password:");
        panelInformation.add(label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 180, -1, -1));

        tfieldPassword.setFont(new java.awt.Font("SansSerif", 0, 15));
        panelInformation.add(tfieldPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 210, 298, 40));

        tfieldUserName.setFont(new java.awt.Font("SansSerif", 0, 15));
        panelInformation.add(tfieldUserName, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 130, 298, 40));

        labelPort.setFont(new java.awt.Font("SansSerif", 1, 15));
        labelPort.setText("Port:");
        panelInformation.add(labelPort, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 360, -1, 40));

        tfieldPort.setFont(new java.awt.Font("SansSerif", 0, 15));
        tfieldPort.setHorizontalAlignment(JTextField.CENTER);
        panelInformation.add(tfieldPort, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 360, 100, 40));

        buttonConnect.setBackground(new java.awt.Color(38, 56, 163));
        buttonConnect.setFont(new java.awt.Font("SansSerif", 1, 15));
        buttonConnect.setForeground(java.awt.Color.white);
        buttonConnect.setText("Connect");
        buttonConnect.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        panelInformation.add(buttonConnect, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 360, 80, 40));

        labelClose2.setIcon(new ImageIcon(getClass().getResource("/resources/icon_close_30.png")));
        panelInformation.add(labelClose2, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 0, -1, 30));

        setPreferredSize(new java.awt.Dimension(500, 500));
        setOpaque(false);
        setBackground(Color.white);
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
    }

    private void initListeners() {
        this.listenerButton = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == buttonConnect) {
                    if (SSHClient.getInstance().createSession(tfieldUserName.getText(), String.valueOf(tfieldPassword.getPassword()),
                            tfieldHostAddress.getText(), tfieldPort.getText())) {
                        switchDisplayedPanel(false);
                    } else {
                        JOptionPane.showMessageDialog(null, MESSAGE_FOR_CONNECTION_FAILED);
                    }
                }
            }

        };
        this.buttonConnect.addActionListener(listenerButton);

        this.listenerLabel = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getSource() == labelClose1 || e.getSource() == labelClose2) {
                    ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices().switchSSHClientVisibility(false);
                    SSHClient.getInstance().close();
                }
            }
        };
        this.labelClose1.addMouseListener(listenerLabel);
        this.labelClose2.addMouseListener(listenerLabel);

        this.listenerField = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getSource() == tfieldCommand) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//                        areaTextDisplayer.addText(tfieldUserName.getText() + "@" + labelHostInfo.getText() + ":" + tfieldCommand.getText());
                        String commandResult = SSHClient.getInstance().sendCommand(tfieldCommand.getText());
                        tfieldCommand.setText("");
                        if (commandResult != null) {
                            areaTextDisplayer.addText(commandResult);
                            tareaResult.setText(areaTextDisplayer.getString());
                            tareaResult.repaint();
                            System.gc();
                        }
                    } else {
                        super.keyReleased(e);
                    }
                }

            }
        };
        this.tfieldCommand.addKeyListener(listenerField);
    }

    public void initViewData(int deviceId) {
        DeviceManagementController deviceController = new DeviceManagementController();
        List<String> data = deviceController.processGettingDeviceInfo(deviceId);
        if (data != null) {
            this.initViewData(data.get(DeviceManagementController.DataOrders.CI_IP_ADDRESS.getValue()));
        } else {
            this.initViewData("");
        }
    }

    public void initViewData(String address) {
        if (!this.panelInformation.isVisible()) {
            this.switchDisplayedPanel(true);
        }

        this.tfieldUserName.setText("root");
        this.tfieldPassword.setText("");
        this.tfieldPort.setText("22");

        if (address != null) {
            this.tfieldHostAddress.setText(address);
            this.labelHostInfo.setText(address);
        }

        this.tareaResult.setText("");
        this.tfieldCommand.setText("");
        this.areaTextDisplayer.lines.clear();
        System.gc();
    }

    public void switchDisplayedPanel(boolean displayInfo) {
        if (displayInfo) {
            this.hidePanel(panelCommand);
            this.showPanel(panelInformation, 0, 0, -1, -1);
        } else {
            this.hidePanel(panelInformation);
            this.showPanel(panelCommand, 0, 0, -1, -1);
        }
        this.revalidate();
        this.repaint();
    }

    private void hidePanel(JPanel panel) {
        this.remove(panel);
        panel.setVisible(false);
        panel.setEnabled(false);
    }

    private void showPanel(JPanel panel, int x, int y, int width, int height) {
        if (panel == panelInformation) {
            System.out.println("DISPLAY PANEL INFORMATION");
        }
        this.add(panel, new AbsoluteConstraints(x, y, width, height));
        panel.setVisible(true);
        panel.setEnabled(true);
    }

    public class AreaDisplayTextController {

        private int listLimit = 300;
        private List<String> lines;

        public AreaDisplayTextController() {
            this.lines = new LinkedList<String>();
        }

        public void addText(String text) {
            String[] newLines = text.split("\\n?\\r");
            int sizeToRemove = this.lines.size() + newLines.length - this.listLimit;

            for (int i = 0; i < sizeToRemove; i++) {
                this.lines.remove(0);
            }

            int tempSize = this.lines.size();
            String lastRow = null;
            if (tempSize > 0) {
                lastRow = this.lines.get(tempSize - 1);
            }
            if (lastRow != null) {
                this.lines.set(tempSize - 1, lastRow + newLines[0]);
            }

            for (int i = 1; i < newLines.length; i++) {
                this.lines.add(newLines[i]);
            }
        }

        public String getString() {
            StringBuilder builder = new StringBuilder();
            for (String line : this.lines) {
                builder.append(line);
            }
            return builder.toString();
        }
    }
}
