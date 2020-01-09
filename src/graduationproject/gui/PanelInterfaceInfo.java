/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.gui;

import graduationproject.controllers.InterfaceManagementController;
import graduationproject.controllers.InterfaceManagementController.DataOrders;
import graduationproject.data.ActiveDeviceDataCollector;
import graduationproject.data.ActiveDeviceDataCollector.ConnectedNodeDataOrders;
import graduationproject.snmpd.SnmpManager;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author cloud
 */
public class PanelInterfaceInfo extends JPanel {

    private final String DEFAULT_VALUE = ". . .";

    private JButton buttonStop;
    private JButton buttonChange;
    private JLabel label1;
    private JLabel label11;
    private JLabel label12;
    private JLabel label13;
    private JLabel label14;
    private JLabel label15;
    private JLabel label16;
    private JLabel label17;
    private JLabel label18;
    private JLabel label2;
    private JLabel label20;
    private JLabel label23;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JLabel label6;
    private JLabel label7;
    private JLabel label8;
    private JLabel label9;
    private JLabel labelCurrentBandwidth;
    private JLabel labelIPAddress;
    private JLabel labelInDiscardCount;
    private JLabel labelInPackAmount;
    private JLabel labelInboundBytes;
    private JLabel labelMTU;
    private JLabel labelMacAddress;
    private JLabel labelName;
    private JLabel labelNetmask;
    private JComboBox cboxConnectedNodeIpAddresses;
    private JLabel labelConnectedNodeLabel;
    private JLabel labelConnectedNodeMacAddress;
    private JLabel labelConnectedNodeName;
    private JLabel labelOutDiscardCount;
    private JLabel labelOutPackAmount;
    private JLabel labelOutboundBytes;
    private JLabel labelType;
    private JLabel labelUpdatedTime;
    private JPanel panelAdditionalInformation;
    private JPanel panelBasicInformation;
    private JPanel panelConnectedNode;
    private JSeparator separator1;
    private JSeparator separator2;
    private JSeparator separator3;
    private JTextField tfieldUpdatePeriod;

    private ActionListener listenerButton;
    private ItemListener listenerComboBox;

    private boolean firstTime;
    private int currentIpChoiceId;
    private int interfaceListId;
    private int deviceId;

    public PanelInterfaceInfo() {
        initComponents();
        initListeners();
        initOtherComponents();
    }

    private void initComponents() {
        panelAdditionalInformation = new JPanel();
        label11 = new JLabel();
        labelInPackAmount = new JLabel();
        label12 = new JLabel();
        labelOutPackAmount = new JLabel();
        label13 = new JLabel();
        labelInboundBytes = new JLabel();
        label14 = new JLabel();
        labelOutboundBytes = new JLabel();
        label15 = new JLabel();
        labelInDiscardCount = new JLabel();
        label16 = new JLabel();
        labelOutDiscardCount = new JLabel();
        labelMTU = new JLabel();
        label17 = new JLabel();
        label18 = new JLabel();
        labelCurrentBandwidth = new JLabel();
        separator1 = new JSeparator();
        separator2 = new JSeparator();
        separator3 = new JSeparator();
        panelBasicInformation = new JPanel();
        label1 = new JLabel();
        labelName = new JLabel();
        label3 = new JLabel();
        labelIPAddress = new JLabel();
        label4 = new JLabel();
        labelNetmask = new JLabel();
        label2 = new JLabel();
        labelType = new JLabel();
        panelConnectedNode = new JPanel();
        label5 = new JLabel();
        cboxConnectedNodeIpAddresses = new JComboBox();
        label6 = new JLabel();
        labelConnectedNodeMacAddress = new JLabel();
        label8 = new JLabel();
        labelConnectedNodeLabel = new JLabel();
        label9 = new JLabel();
        labelConnectedNodeName = new JLabel();
        label7 = new JLabel();
        labelMacAddress = new JLabel();
        label23 = new JLabel();
        tfieldUpdatePeriod = new JTextField();
        buttonChange = new JButton();
        buttonStop = new JButton();
        label20 = new JLabel();
        labelUpdatedTime = new JLabel();

        setMaximumSize(new java.awt.Dimension(1160, 940));
        setMinimumSize(new java.awt.Dimension(1160, 940));
        setPreferredSize(new java.awt.Dimension(1160, 940));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        setBackground(Color.white);

        panelAdditionalInformation.setBackground(java.awt.Color.white);
        panelAdditionalInformation.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Additional Information", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new java.awt.Font("SansSerif", 1, 14)));
        panelAdditionalInformation.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label11.setFont(new java.awt.Font("SansSerif", 1, 16));
        label11.setText("Number Of Inbound Packets:");
        panelAdditionalInformation.add(label11, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 280, -1, 30));

        labelInPackAmount.setFont(new java.awt.Font("SansSerif", 0, 16));
        labelInPackAmount.setText(DEFAULT_VALUE);
        panelAdditionalInformation.add(labelInPackAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 280, 90, 30));

        label12.setFont(new java.awt.Font("SansSerif", 1, 16));
        label12.setText("Number Of Outbound Packets:");
        panelAdditionalInformation.add(label12, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 340, -1, 30));

        labelOutPackAmount.setFont(new java.awt.Font("SansSerif", 0, 16));
        labelOutPackAmount.setText(DEFAULT_VALUE);
        panelAdditionalInformation.add(labelOutPackAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 340, 90, 30));

        label13.setFont(new java.awt.Font("SansSerif", 1, 16));
        label13.setText("Inbound Bytes:");
        panelAdditionalInformation.add(label13, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 400, -1, 30));

        labelInboundBytes.setFont(new java.awt.Font("SansSerif", 0, 16));
        labelInboundBytes.setText(DEFAULT_VALUE);
        panelAdditionalInformation.add(labelInboundBytes, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 400, 90, 30));

        label14.setFont(new java.awt.Font("SansSerif", 1, 16));
        label14.setText("Outbound Bytes");
        panelAdditionalInformation.add(label14, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 460, -1, 30));

        labelOutboundBytes.setFont(new java.awt.Font("SansSerif", 0, 16));
        labelOutboundBytes.setText(DEFAULT_VALUE);
        panelAdditionalInformation.add(labelOutboundBytes, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 460, 90, 30));

        label15.setFont(new java.awt.Font("SansSerif", 1, 16));
        label15.setText("Number Of Inbound Discard Packets:");
        panelAdditionalInformation.add(label15, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 520, -1, 30));

        labelInDiscardCount.setFont(new java.awt.Font("SansSerif", 0, 16));
        labelInDiscardCount.setText(DEFAULT_VALUE);
        panelAdditionalInformation.add(labelInDiscardCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 520, 90, 30));

        label16.setFont(new java.awt.Font("SansSerif", 1, 16));
        label16.setText("Number Of Outbound Discard Packets:");
        panelAdditionalInformation.add(label16, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 580, -1, 30));

        labelOutDiscardCount.setFont(new java.awt.Font("SansSerif", 0, 16));
        labelOutDiscardCount.setText(DEFAULT_VALUE);
        panelAdditionalInformation.add(labelOutDiscardCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 580, 90, 30));

        labelMTU.setFont(new java.awt.Font("SansSerif", 0, 16));
        labelMTU.setText(DEFAULT_VALUE);
        panelAdditionalInformation.add(labelMTU, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 50, 200, 30));

        label17.setFont(new java.awt.Font("SansSerif", 1, 16));
        label17.setText("MTU:");
        panelAdditionalInformation.add(label17, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, -1, 30));

        label18.setFont(new java.awt.Font("SansSerif", 1, 16));
        label18.setText("Bandwidth:");
        panelAdditionalInformation.add(label18, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, -1, 30));

        labelCurrentBandwidth.setFont(new java.awt.Font("SansSerif", 0, 16));
        labelCurrentBandwidth.setText(DEFAULT_VALUE);
        panelAdditionalInformation.add(labelCurrentBandwidth, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 110, 200, 30));
        panelAdditionalInformation.add(separator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, 520, 10));
        panelAdditionalInformation.add(separator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 220, 520, 10));
        panelAdditionalInformation.add(separator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 520, 10));

        add(panelAdditionalInformation, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 60, 520, 730));

        panelBasicInformation.setBackground(java.awt.Color.white);
        panelBasicInformation.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)),
                "Basic Information", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new java.awt.Font("SansSerif", 1, 14)));
        panelBasicInformation.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label1.setFont(new java.awt.Font("SansSerif", 1, 16));
        label1.setText("Name:");
        panelBasicInformation.add(label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, -1, 30));

        labelName.setFont(new java.awt.Font("SansSerif", 0, 16));
        labelName.setText(DEFAULT_VALUE);
        panelBasicInformation.add(labelName, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 50, 240, 30));

        label3.setFont(new java.awt.Font("SansSerif", 1, 16));
        label3.setText("IP Address:");
        panelBasicInformation.add(label3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 210, -1, 30));

        labelIPAddress.setFont(new java.awt.Font("SansSerif", 0, 16));
        labelIPAddress.setText(DEFAULT_VALUE);
        panelBasicInformation.add(labelIPAddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 210, 240, 30));

        label4.setFont(new java.awt.Font("SansSerif", 1, 16));
        label4.setText("Netmask:");
        panelBasicInformation.add(label4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 270, -1, 30));

        labelNetmask.setFont(new java.awt.Font("SansSerif", 0, 16));
        labelNetmask.setText(DEFAULT_VALUE);
        panelBasicInformation.add(labelNetmask, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 270, 240, 30));

        label2.setFont(new java.awt.Font("SansSerif", 1, 16));
        label2.setText("Type:");
        panelBasicInformation.add(label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, -1, 30));

        labelType.setFont(new java.awt.Font("SansSerif", 0, 16));
        labelType.setText(DEFAULT_VALUE);
        panelBasicInformation.add(labelType, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 150, 240, 30));

        panelConnectedNode.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Connected Node", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new java.awt.Font("SansSerif", 1, 14)));
        panelConnectedNode.setOpaque(false);
        panelConnectedNode.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label5.setFont(new java.awt.Font("SansSerif", 1, 16));
        label5.setText("IP Address:");
        panelConnectedNode.add(label5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 170, -1, 30));

        cboxConnectedNodeIpAddresses.setFont(new java.awt.Font("SansSerif", 0, 16));
        cboxConnectedNodeIpAddresses.setModel(new DefaultComboBoxModel());
//        cboxConnectedNodeIpAddresses.setText(DEFAULT_VALUE);
        panelConnectedNode.add(cboxConnectedNodeIpAddresses, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 170, 240, 30));

        label6.setFont(new java.awt.Font("SansSerif", 1, 16));
        label6.setText("MAC Address:");
        panelConnectedNode.add(label6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 230, -1, 30));

        labelConnectedNodeMacAddress.setFont(new java.awt.Font("SansSerif", 0, 16));
        labelConnectedNodeMacAddress.setText(DEFAULT_VALUE);
        panelConnectedNode.add(labelConnectedNodeMacAddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 230, 240, 30));

        label8.setFont(new java.awt.Font("SansSerif", 1, 16));
        label8.setText("Label:");
        panelConnectedNode.add(label8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, -1, 30));

        labelConnectedNodeLabel.setFont(new java.awt.Font("SansSerif", 0, 16));
        labelConnectedNodeLabel.setText(DEFAULT_VALUE);
        panelConnectedNode.add(labelConnectedNodeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 110, 240, 30));

        label9.setFont(new java.awt.Font("SansSerif", 1, 16));
        label9.setText("Name:");
        panelConnectedNode.add(label9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, -1, 30));

        labelConnectedNodeName.setFont(new java.awt.Font("SansSerif", 0, 16));
        labelConnectedNodeName.setText(DEFAULT_VALUE);
        panelConnectedNode.add(labelConnectedNodeName, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 50, 240, 30));

        panelBasicInformation.add(panelConnectedNode, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 380, 460, 350));

        label7.setFont(new java.awt.Font("SansSerif", 1, 16));
        label7.setText("MAC Address:");
        panelBasicInformation.add(label7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, -1, 30));

        labelMacAddress.setFont(new java.awt.Font("SansSerif", 0, 16));
        labelMacAddress.setText(DEFAULT_VALUE);
        panelBasicInformation.add(labelMacAddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 100, 240, 30));

        add(panelBasicInformation, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, 460, 730));

        label23.setFont(new java.awt.Font("SansSerif", 1, 15));
        label23.setText("Update Period(s):");
        add(label23, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 830, 150, 30));

        tfieldUpdatePeriod.setFont(new java.awt.Font("SansSerif", 1, 15));
        tfieldUpdatePeriod.setHorizontalAlignment(JTextField.CENTER);
        tfieldUpdatePeriod.setText("0");
        tfieldUpdatePeriod.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));
        tfieldUpdatePeriod.setOpaque(false);
        add(tfieldUpdatePeriod, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 830, 90, 30));

        buttonChange.setBackground(new java.awt.Color(62, 89, 207));
        buttonChange.setFont(new java.awt.Font("SansSerif", 1, 15));
        buttonChange.setForeground(java.awt.Color.white);
        buttonChange.setText("Change");
        buttonChange.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        add(buttonChange, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 830, 70, 30));

        buttonStop.setBackground(new java.awt.Color(62, 89, 207));
        buttonStop.setFont(new java.awt.Font("SansSerif", 1, 15));
        buttonStop.setForeground(java.awt.Color.white);
        buttonStop.setText("Stop");
        buttonStop.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        add(buttonStop, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 830, 70, 30));

        label20.setFont(new java.awt.Font("SansSerif", 1, 16));
        label20.setText("Updated Time:");
        add(label20, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 830, -1, 30));

        labelUpdatedTime.setFont(new java.awt.Font("SansSerif", 0, 16));
        labelUpdatedTime.setText(DEFAULT_VALUE);
        add(labelUpdatedTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 830, 310, 30));

    }

    private void initListeners() {
        this.listenerButton = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton source = (JButton) e.getSource();
                if (source == buttonStop) {
//                    PanelInterfaceInfo.this.setStopUpdate(true);
                    SnmpManager.getInstance().getQueryTimerManager().cancelInterfaceTimer();
                }
                if (source == buttonChange) {
//                    PanelInterfaceInfo.this.setStopUpdate(false);
                    InterfaceManagementController interfaceController = new InterfaceManagementController();
                    if (!interfaceController.processChangingInterfaceCheckingPeriod(
                            Integer.parseInt(tfieldUpdatePeriod.getText()))) {
                        JOptionPane.showMessageDialog(null, interfaceController.getResultMessage());
                    }
                }
            }
        };
        this.buttonChange.addActionListener(this.listenerButton);
        this.buttonStop.addActionListener(this.listenerButton);

        this.listenerComboBox = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JComboBox source = (JComboBox) e.getSource();
                if (source == cboxConnectedNodeIpAddresses) {
                    if (source.getSelectedIndex() != currentIpChoiceId) {
                        currentIpChoiceId = source.getSelectedIndex();
//                        if (cboxConnectedNodeIpAddresses.getSelectedItem() != null) {
                        firstTime = false;
//                        }
                        PanelInterfaceInfo.this.displayConnectedNodeInformation(deviceId, firstTime, null);
//                        firstTime = false;
                    }
                }
            }
        };
        this.cboxConnectedNodeIpAddresses.addItemListener(listenerComboBox);
    }

    private void initOtherComponents() {
//        this.stopUpdate = false;
        this.deviceId = -1;
        this.interfaceListId = -1;
        this.currentIpChoiceId = -1;
    }

    public void initViewData(int deviceId, int interfaceListId) {
        this.interfaceListId = interfaceListId;
        this.deviceId = deviceId;
        this.firstTime = true;
        this.currentIpChoiceId = -1;

        InterfaceManagementController interfaceController = new InterfaceManagementController();
        List<Object> data = interfaceController.processGettingInterfaceFromDatabase(deviceId, interfaceListId);
        if (data == null) {
//            JOptionPane.showMessageDialog(null, interfaceController.getResultMessage());
            return;
        }

        this.updateView(deviceId, data);
    }

    public synchronized void updateView(int deviceId, List<Object> data) {
        if (deviceId != this.deviceId) {
            return;
        }

        try {
            if (!data.isEmpty()) {
                this.labelName.setText((String) data.get(DataOrders.NAME.getValue()));

                this.labelMacAddress.setText((String) data.get(DataOrders.MAC_ADDRESS.getValue()));
                this.labelType.setText((String) data.get(DataOrders.TYPE.getValue()));
                this.labelIPAddress.setText((String) data.get(DataOrders.IP_ADDRESS.getValue()));
                this.labelNetmask.setText((String) data.get(DataOrders.NETMASK.getValue()));

                this.labelMTU.setText(String.valueOf(data.get(DataOrders.MTU.getValue())));
                this.labelCurrentBandwidth.setText(String.valueOf((int) data.get(DataOrders.BANDWIDTH.getValue())));
                this.labelInPackAmount.setText(String.valueOf(data.get(DataOrders.IN_PACK_NUMBER.getValue())));
                this.labelOutPackAmount.setText(String.valueOf(data.get(DataOrders.OUT_PACK_NUMBER.getValue())));
                this.labelInboundBytes.setText(String.valueOf((int) data.get(DataOrders.IN_BYTES.getValue())));
                this.labelOutboundBytes.setText(String.valueOf((int) data.get(DataOrders.OUT_BYTES.getValue())));
                this.labelInDiscardCount.setText(String.valueOf(data.get(DataOrders.IN_DISCARD_PACK_NUMBER.getValue())));
                this.labelOutDiscardCount.setText(String.valueOf(data.get(DataOrders.OUT_DISCARD_PACK_NUMBER.getValue())));

                this.labelUpdatedTime.setText((String) data.get(DataOrders.UPDATED_TIME.getValue()));

                if (data.size() >= DataOrders.UPDATE_PERIOD.getValue() + 1) {
                    this.tfieldUpdatePeriod.setText(String.valueOf(data.get(DataOrders.UPDATE_PERIOD.getValue())));
                }

                if (firstTime) {
                    this.displayConnectedNodeInformation(this.deviceId, this.firstTime,
                            (List<Object>) data.get(DataOrders.CONNECTED_NODE.getValue()));
                }

                this.revalidate();
                this.repaint();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void displayConnectedNodeInformation(int deviceId, boolean firstTime, List<Object> data) {
//        List<Object> data = null;
        if (firstTime) { //the first time of updating next node info 
//            data = ActiveDeviceDataCollector.getInstance().getConnectedNodesForView(deviceId, this.labelMacAddress.getText(), null);

            if (data != null) {
                DefaultComboBoxModel boxModel = (DefaultComboBoxModel) this.cboxConnectedNodeIpAddresses.getModel();
                boxModel.removeAllElements();

                String[] ips = (String[]) data.get(ConnectedNodeDataOrders.IP_ADDRESSES.getValue());
                for (String ip : ips) {
                    boxModel.addElement(ip);
                }
                if (ips != null && ips.length > 0) {
                    this.cboxConnectedNodeIpAddresses.setSelectedIndex(0);
                }
            }
        } else {
            data = ActiveDeviceDataCollector.getInstance().getConnectedNodesForView(deviceId,
                    this.labelMacAddress.getText(), (String) this.cboxConnectedNodeIpAddresses.getSelectedItem());
        }

        if (data != null) {
            this.labelConnectedNodeName.setText((String) data.get(ConnectedNodeDataOrders.NAME.getValue()));
            this.labelConnectedNodeLabel.setText((String) data.get(ConnectedNodeDataOrders.LABEL.getValue()));
            this.labelConnectedNodeMacAddress.setText((String) data.get(ConnectedNodeDataOrders.MAC_ADDRESS.getValue()));
        } else {
            DefaultComboBoxModel boxModel = (DefaultComboBoxModel) this.cboxConnectedNodeIpAddresses.getModel();
            boxModel.removeAllElements();
            this.labelConnectedNodeName.setText(DEFAULT_VALUE);
            this.labelConnectedNodeLabel.setText(DEFAULT_VALUE);
            this.labelConnectedNodeMacAddress.setText(DEFAULT_VALUE);
        }
    }

    public int getInterfaceListId() {
        return interfaceListId;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public synchronized void setStopUpdate(boolean stopUpdate) {
//        this.stopUpdate = stopUpdate;
    }

}
