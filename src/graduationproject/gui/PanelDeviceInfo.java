/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.gui;

import graduationproject.controllers.DeviceManagementController;
import graduationproject.controllers.DeviceManagementController.DataOrders;
import graduationproject.controllers.DeviceManagementController.DeviceStates;
import graduationproject.snmpd.SnmpManager;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author cloud
 */
public class PanelDeviceInfo extends JPanel {

    private JButton buttonCancel;
    private JButton buttonResources;
    private JButton buttonAdvance;
    private JButton buttonStatistics;
    private JButton buttonSave;

    private JComboBox<String> cboxSNMPVersion;
    private JComboBox<String> cboxType;
    private JLabel label1;
    private JLabel label10;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JLabel label6;
    private JLabel label8;
    private JLabel label9;
    private JLabel labelHidePanel;
    private JLabel labelLastAccess;
    private JPanel panelBasicInfo;
    private JPanel panelSNMPInfo;
    private JScrollPane scrollpane1;
    private JTextArea tareaDescription;
    private JTextField tfieldCommunity;
    private JTextField tfieldIpAddress;
    private JTextField tfieldLabel;
    private JTextField tfieldLocation;
    private JTextField tfieldName;

    private DialogChoosingTemplates dialogChoosingTemplates;

    private ActionListener listenerButton;
    private MouseAdapter listenerLabel;

    private int deviceId;

    public PanelDeviceInfo() {
        initComponents();
        initListeners();
    }

    public void initComponents() {

        panelBasicInfo = new JPanel();
        label1 = new JLabel();
        tfieldName = new JTextField();
        label2 = new JLabel();
        label3 = new JLabel();
        tfieldLocation = new JTextField();
        label4 = new JLabel();
        scrollpane1 = new JScrollPane();
        tareaDescription = new JTextArea();
        cboxType = new JComboBox<>();
        label5 = new JLabel();
        labelLastAccess = new JLabel();
        label6 = new JLabel();
        tfieldLabel = new JTextField();
        panelSNMPInfo = new JPanel();
        label8 = new JLabel();
        label9 = new JLabel();
        tfieldCommunity = new JTextField();
        cboxSNMPVersion = new JComboBox<>();
        label10 = new JLabel();
        tfieldIpAddress = new JTextField();
        buttonCancel = new JButton();
        buttonSave = new JButton();
        buttonResources = new JButton();
        buttonStatistics = new JButton();
        buttonAdvance = new JButton();
        labelHidePanel = new JLabel();

        setPreferredSize(new java.awt.Dimension(1160, 940));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        setBackground(Color.white);

        panelBasicInfo.setBackground(java.awt.Color.white);
        panelBasicInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Basic Information", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new java.awt.Font("SansSerif", 1, 14))); // NOI18N
        panelBasicInfo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label1.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        label1.setText("Name:");
        panelBasicInfo.add(label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, -1, 30));

        tfieldName.setBackground(java.awt.Color.white);
        tfieldName.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        panelBasicInfo.add(tfieldName, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 50, 350, 30));

        label2.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        label2.setText("Type:");
        panelBasicInfo.add(label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 160, -1, 30));

        label3.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        label3.setText("Location:");
        panelBasicInfo.add(label3, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 220, -1, 30));

        tfieldLocation.setBackground(java.awt.Color.white);
        tfieldLocation.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        panelBasicInfo.add(tfieldLocation, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 220, 350, 30));

        label4.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        label4.setText("Description:");
        panelBasicInfo.add(label4, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 280, -1, 30));

        scrollpane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollpane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        tareaDescription.setBackground(java.awt.Color.white);
        tareaDescription.setColumns(20);
        tareaDescription.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        tareaDescription.setRows(5);
        tareaDescription.setEditable(false);
        tareaDescription.setLineWrap(true);
        scrollpane1.setViewportView(tareaDescription);

        panelBasicInfo.add(scrollpane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 280, 350, 120));

        cboxType.setBackground(java.awt.Color.white);
        cboxType.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        cboxType.setModel(new DefaultComboBoxModel<>(DeviceManagementController.DEVICE_TYPES));
        panelBasicInfo.add(cboxType, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 160, 180, -1));

        label5.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        label5.setText("Last Access:");
        panelBasicInfo.add(label5, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 430, -1, 30));

        labelLastAccess.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        labelLastAccess.setText(". . .");
        panelBasicInfo.add(labelLastAccess, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 430, 350, 30));

        label6.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        label6.setText("Label:");
        panelBasicInfo.add(label6, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 100, -1, 30));

        tfieldLabel.setBackground(java.awt.Color.white);
        tfieldLabel.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        panelBasicInfo.add(tfieldLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 100, 350, 30));

        add(panelBasicInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 70, 920, 500));

        panelSNMPInfo.setBackground(java.awt.Color.white);
        panelSNMPInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)),
                "SNMP Information", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new java.awt.Font("SansSerif", 1, 14))); // NOI18N
        panelSNMPInfo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label8.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        label8.setText("SNMP Version:");
        panelSNMPInfo.add(label8, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 40, -1, 30));

        label9.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        label9.setText("Community:");
        panelSNMPInfo.add(label9, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 140, -1, 30));

        tfieldCommunity.setBackground(java.awt.Color.white);
        tfieldCommunity.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        panelSNMPInfo.add(tfieldCommunity, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 140, 350, 30));

        cboxSNMPVersion.setBackground(java.awt.Color.white);
        cboxSNMPVersion.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        cboxSNMPVersion.setModel(new DefaultComboBoxModel<>(SnmpManager.getInstance().getVersionStrings()));
        panelSNMPInfo.add(cboxSNMPVersion, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 40, 180, -1));

        label10.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        label10.setText("IP Address:");
        panelSNMPInfo.add(label10, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 90, -1, 30));

        tfieldIpAddress.setBackground(java.awt.Color.white);
        tfieldIpAddress.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        panelSNMPInfo.add(tfieldIpAddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 90, 350, 30));

        add(panelSNMPInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 590, 920, 210));

        buttonCancel.setBackground(new java.awt.Color(38, 56, 163));
        buttonCancel.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        buttonCancel.setForeground(java.awt.Color.white);
        buttonCancel.setText("Cancel");
        buttonCancel.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        buttonCancel.setOpaque(true);
        add(buttonCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 830, 90, 40));

        buttonSave.setBackground(new java.awt.Color(38, 56, 163));
        buttonSave.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        buttonSave.setForeground(java.awt.Color.white);
        buttonSave.setText("Save");
        buttonSave.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        buttonSave.setOpaque(true);
        add(buttonSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 830, 100, 40));

        buttonResources.setBackground(new java.awt.Color(207, 62, 69));
        buttonResources.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        buttonResources.setForeground(java.awt.Color.white);
        buttonResources.setText("Resources");
        buttonResources.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        buttonResources.setOpaque(true);
        add(buttonResources, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 830, 100, 40));

        buttonStatistics.setBackground(new java.awt.Color(207, 62, 69));
        buttonStatistics.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        buttonStatistics.setForeground(java.awt.Color.white);
        buttonStatistics.setText("Statistics");
        buttonStatistics.setBorder(new SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buttonStatistics.setOpaque(true);
        add(buttonStatistics, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 830, 100, 40));

        buttonAdvance.setBackground(new java.awt.Color(207, 62, 69));
        buttonAdvance.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        buttonAdvance.setForeground(java.awt.Color.white);
        buttonAdvance.setText("Advance");
        buttonAdvance.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buttonAdvance.setOpaque(true);
        add(buttonAdvance, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 830, 100, 40));

        labelHidePanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icon_double_right_40.png")));
        add(labelHidePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 335, -1, 180));

        dialogChoosingTemplates = new DialogChoosingTemplates();
        dialogChoosingTemplates.dispose();
    }

    private void initListeners() {
        this.listenerButton = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton source = (JButton) e.getSource();
                if (source == buttonSave) {
                    DeviceManagementController deviceController = new DeviceManagementController();
                    if (!deviceController.processSavingDeviceInfo(deviceId, getDeviceDataForUpdate())) {
                        JOptionPane.showMessageDialog(null, deviceController.getResultMessage());
                    } else {
                        ApplicationWindow.getInstance().getPanelMain()
                                .getPanelImportedDevices().updateLabelDeviceText(deviceId, deviceController.getExtraStringData());
                    }
                }
                if (source == buttonCancel) {
                    initData(deviceId);
                }
                if (source == buttonResources) {
                    ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices()
                            .switchDisplayedPanel(PanelImportedDevices.PANELS.PANEL_DEVICE_RESOURCES);
                    ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices().getPanelDeviceResources().initData(deviceId);
                }
                if (source == buttonStatistics) {
                    try {
                        ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices()
                                .switchDisplayedPanel(PanelImportedDevices.PANELS.PANEL_DEVICE_SUMMARY);
                        ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices().getPanelDeviceSummary().initData(deviceId);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                if (source == buttonAdvance) {
                    dialogChoosingTemplates.setVisible(true);
                }

            }

        };
        this.buttonSave.addActionListener(this.listenerButton);
        this.buttonCancel.addActionListener(this.listenerButton);
        this.buttonResources.addActionListener(this.listenerButton);
        this.buttonStatistics.addActionListener((this.listenerButton));
        this.buttonAdvance.addActionListener(this.listenerButton);
        
        this.listenerLabel = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                JLabel source = (JLabel) e.getSource();
                if (source == labelHidePanel) {
                    ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices().hideDisplayedPanel();
                }
            }
        };
        this.labelHidePanel.addMouseListener(this.listenerLabel);
    }

    public void initData(int deviceId) {
        this.deviceId = deviceId;

        DeviceManagementController deviceController = new DeviceManagementController();
        List<String> data = deviceController.processGettingDeviceInfo(deviceId);

        if (data == null) {
            JOptionPane.showMessageDialog(null, deviceController.getResultMessage());
            return;
        }

        this.tfieldName.setText(data.get(DataOrders.NAME.getValue()));
        this.tfieldLabel.setText(data.get(DataOrders.LABEL.getValue()));
        this.cboxType.setSelectedItem(data.get(DataOrders.TYPE.getValue()));
        this.tareaDescription.setText(data.get(DataOrders.DESCRIPTION.getValue()));
        this.tfieldLocation.setText(data.get(DataOrders.LOCATION.getValue()));
        this.labelLastAccess.setText(data.get(DataOrders.LAST_ACCESS.getValue()));
        this.cboxSNMPVersion.setSelectedItem(data.get(DataOrders.SNMP_VERSION.getValue()));
        this.tfieldIpAddress.setText(data.get(DataOrders.CI_IP_ADDRESS.getValue()));
        this.tfieldCommunity.setText(data.get(DataOrders.CI_COMMUNITY.getValue()));
    }

    public List<String> getDeviceDataForUpdate() {
        List<String> result = new ArrayList<String>();
        result.add(DataOrders.NAME.getValue(), this.tfieldName.getText());
        result.add(DataOrders.LABEL.getValue(), this.tfieldLabel.getText());
        result.add(DataOrders.TYPE.getValue(), (String) this.cboxType.getSelectedItem());
        result.add(DataOrders.DESCRIPTION.getValue(), this.tareaDescription.getText());
        result.add(DataOrders.LOCATION.getValue(), this.tfieldLocation.getText());
        result.add(DataOrders.SNMP_VERSION.getValue(), (String) this.cboxSNMPVersion.getSelectedItem());
        result.add(DataOrders.LAST_ACCESS.getValue(), this.labelLastAccess.getText());
        result.add(DataOrders.IMPORTED_TIME.getValue(), new String());
        result.add(DataOrders.CI_IP_ADDRESS.getValue(), this.tfieldIpAddress.getText());
        result.add(DataOrders.CI_COMMUNITY.getValue(), this.tfieldCommunity.getText());
        return result;
    }

    public synchronized void updateDeviceDescription(int deviceId, String description) {
        if (deviceId == this.deviceId) {
            this.tareaDescription.setText(description);
        }
    }

    public int getDeviceId() {
        return deviceId;
    }

}
