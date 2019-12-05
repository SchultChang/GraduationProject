/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.gui;

import graduationproject.controllers.DeviceManagementController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author cloud
 */
public class PanelMonitoringDevice extends JPanel {

    private String[] defaultColumns = {"Object Name", "Value"};
    private int defaultValueCol = 1;

    private JButton buttonStart;
    private JButton buttonStop;
    private JLabel label6;
    private JLabel labelTime;
    private JLabel label1;
    private JLabel label2;
    private JLabel label4;
    private JLabel label5;
    private JPanel panelTemplateInfo;
    private JScrollPane scrollpane1;
    private JTable tableItems;
    private JTextField tfieldDeviceName;
    private JTextField tfieldTemplateName;
    private JTextField tfieldUpdatePeriod;

    private ActionListener listenerButton;

    private int templateId;
    private int deviceId;

    public PanelMonitoringDevice() {
        initComponents();
    }

    private void initComponents() {

        panelTemplateInfo = new JPanel();
        label1 = new JLabel();
        tfieldTemplateName = new JTextField();
        label2 = new JLabel();
        tfieldDeviceName = new JTextField();
        label4 = new JLabel();
        scrollpane1 = new JScrollPane();
        tableItems = new JTable();
        label5 = new JLabel();
        tfieldUpdatePeriod = new JTextField();
        buttonStart = new JButton();
        buttonStop = new JButton();
        label6 = new JLabel();
        labelTime = new JLabel();

        setBackground(java.awt.Color.white);
        setMaximumSize(new java.awt.Dimension(1160, 940));
        setMinimumSize(new java.awt.Dimension(1160, 940));
        setPreferredSize(new java.awt.Dimension(1160, 940));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelTemplateInfo.setBackground(new java.awt.Color(73, 125, 222));
        panelTemplateInfo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label1.setFont(new java.awt.Font("SansSerif", 1, 15));
        label1.setText("Template Name:");
        panelTemplateInfo.add(label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 50, -1, 40));

        tfieldTemplateName.setEditable(false);
        tfieldTemplateName.setFont(new java.awt.Font("SansSerif", 1, 15));
        tfieldTemplateName.setText(". . .");
        tfieldTemplateName.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));
        tfieldTemplateName.setOpaque(false);
        panelTemplateInfo.add(tfieldTemplateName, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 50, 330, 40));

        label2.setFont(new java.awt.Font("SansSerif", 1, 15));
        label2.setText("Device Name:");
        panelTemplateInfo.add(label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 110, -1, 40));

        tfieldDeviceName.setEditable(false);
        tfieldDeviceName.setFont(new java.awt.Font("SansSerif", 1, 15));
        tfieldDeviceName.setText(". . .");
        tfieldDeviceName.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));
        tfieldDeviceName.setOpaque(false);
        panelTemplateInfo.add(tfieldDeviceName, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 110, 330, 40));

        add(panelTemplateInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1160, 220));

        label4.setFont(new java.awt.Font("SansSerif", 1, 15));
        label4.setText("Items:");
        add(label4, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 240, -1, 60));

        tableItems.setBorder(null);
        tableItems.setFont(new java.awt.Font("SansSerif", 0, 16));
        tableItems.setModel(new DefaultTableModel(
                new Object[][]{},
                this.defaultColumns
        ));
        scrollpane1.setViewportView(tableItems);

        add(scrollpane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 280, 950, 540));

        label5.setFont(new java.awt.Font("SansSerif", 1, 15));
        label5.setText("Update Period (s):");
        add(label5, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 860, -1, 30));

        tfieldUpdatePeriod.setFont(new java.awt.Font("SansSerif", 0, 15));
        tfieldUpdatePeriod.setHorizontalAlignment(JTextField.CENTER);
        tfieldUpdatePeriod.setText("0");
        add(tfieldUpdatePeriod, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 860, 90, 30));

        buttonStart.setBackground(new java.awt.Color(73, 125, 222));
        buttonStart.setForeground(java.awt.Color.white);
        buttonStart.setText("Start");
        add(buttonStart, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 860, 70, -1));

        buttonStop.setBackground(new java.awt.Color(73, 125, 222));
        buttonStop.setForeground(java.awt.Color.white);
        buttonStop.setText("Stop");
        add(buttonStop, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 860, 70, -1));

        label6.setFont(new java.awt.Font("SansSerif", 1, 15));
        label6.setText("Updated Time:");
        add(label6, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 858, 140, 30));

        labelTime.setFont(new java.awt.Font("SansSerif", 1, 15));
        labelTime.setText(". . .");
        add(labelTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 858, 180, 30));

    }

    private void initListeners() {
        this.listenerButton = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }

        };
        this.buttonStart.addActionListener(listenerButton);
        this.buttonStop.addActionListener(listenerButton);
    }

    public void initData(int deviceId, int templateId) {
        this.deviceId = deviceId;
        this.templateId = templateId;

        DeviceManagementController deviceController = new DeviceManagementController();
        deviceController.processSendingQueryBasedOnTemplate(true, deviceId, templateId);
    }

    //data is almost for column headers and label
    public void updateViewStage1(String deviceName, String templateName, List<String> objectNames, boolean isTable) {
        this.tfieldDeviceName.setText(deviceName);
        this.tfieldTemplateName.setText(templateName);

        if (isTable) {
            DefaultTableModel newModel = new DefaultTableModel(new Object[][]{}, objectNames.toArray());
            this.tableItems.setModel(newModel);
        } else {
            DefaultTableModel newModel = new DefaultTableModel(new Object[][]{}, this.defaultColumns);
            this.tableItems.setModel(newModel);

            int tempSize = objectNames.size();
            for (int i = 0; i < tempSize; i++) {
                newModel.addRow(new Object[]{objectNames.get(i), null});
            }
        }

        this.revalidate();
        this.repaint();
    }

    //data is retrieved from device
    public synchronized void updateViewStage2(int deviceId, int templateId, String receivedTime, List<Object> data, boolean isTable) {
        try {
            if (deviceId != this.deviceId || templateId != this.templateId) {
                return;
            }

            DefaultTableModel tableModel = (DefaultTableModel) this.tableItems.getModel();
            if (isTable) {
                int tempSize = tableModel.getRowCount();
                for (int i = tempSize - 1; i >= 0; i--) {
                    tableModel.removeRow(i);
                }

                tempSize = data.size();
                for (int i = 0; i < tempSize; i++) {
                    tableModel.addRow((Object[]) data.get(i));
                }
            } else {
                int tempSize = data.size();
                for (int i = 0; i < tempSize; i++) {
                    tableModel.setValueAt(data.get(i), i, this.defaultValueCol);
                }
            }
            
            this.labelTime.setText(receivedTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.revalidate();
        this.repaint();
    }
}
