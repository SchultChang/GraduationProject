/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.gui;

import graduationproject.controllers.DeviceManagementController;
import graduationproject.controllers.DeviceManagementController.DataOrders;
import graduationproject.controllers.DeviceResourceManagementController;
import graduationproject.snmpd.SnmpManager;
import java.awt.Color;
import java.util.Calendar;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author cloud
 */
public class PanelDeviceResources extends JPanel {

    private String[] tableCPUHeaders = {"Firmware Id", "Description", "Load(%)"};
    private String[] tableDiskHeaders = {"Name", "Total Size", "Used Size"};

    private JButton buttonStart;
    private JButton buttonStop;
    private JLabel label1;
    private JLabel label10;
    private JLabel label11;
    private JLabel label12;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JLabel label6;
    private JLabel label7;
    private JLabel label8;
    private JLabel label9;
    private JLabel labelTotalOther;
    private JLabel labelTotalRam;
    private JLabel labelTotalVirtual;
    private JLabel labelUpdatedTime;
    private JLabel labelUsedOther;
    private JLabel labelUsedRam;
    private JLabel labelUsedVirtual;
    private JPanel panelCPU;
    private JPanel panelMemory;
    private JScrollPane scrollpane1;
    private JScrollPane scrollpane2;
    private JTable tableCPU;
    private JTable tableDisks;
    private JTextField tfieldUpdatePeirod;

    private int deviceId;

    public PanelDeviceResources() {
        initComponents();
    }

    private void initComponents() {

        panelCPU = new JPanel();
        scrollpane1 = new JScrollPane();
        tableCPU = new JTable();
        panelMemory = new JPanel();
        label2 = new JLabel();
        label3 = new JLabel();
        labelTotalRam = new JLabel();
        label4 = new JLabel();
        labelUsedRam = new JLabel();
        label5 = new JLabel();
        label6 = new JLabel();
        labelTotalVirtual = new JLabel();
        label7 = new JLabel();
        labelUsedVirtual = new JLabel();
        label8 = new JLabel();
        label9 = new JLabel();
        label10 = new JLabel();
        labelUsedOther = new JLabel();
        labelTotalOther = new JLabel();
        label1 = new JLabel();
        scrollpane2 = new JScrollPane();
        tableDisks = new JTable();
        label11 = new JLabel();
        tfieldUpdatePeirod = new JTextField();
        buttonStart = new JButton();
        buttonStop = new JButton();
        labelUpdatedTime = new JLabel();
        label12 = new JLabel();

        setPreferredSize(new java.awt.Dimension(1160, 940));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        setBackground(Color.white);

        panelCPU.setBackground(java.awt.Color.white);
        panelCPU.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)),
                "CPU", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new java.awt.Font("SansSerif", 1, 14)));
        panelCPU.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scrollpane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollpane1.setViewportBorder(null);

        tableCPU.setBorder(null);
        tableCPU.setFont(new java.awt.Font("SansSerif", 0, 16));
        tableCPU.setModel(new DefaultTableModel(
                new Object[][]{},
                this.tableCPUHeaders
        ));
        scrollpane1.setViewportView(tableCPU);

        panelCPU.add(scrollpane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 50, 921, 120));

        add(panelCPU, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 60, 1060, 220));

        panelMemory.setBackground(java.awt.Color.white);
        panelMemory.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)),
                "Memory", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new java.awt.Font("SansSerif", 1, 14)));
        panelMemory.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label2.setFont(new java.awt.Font("SansSerif", 1, 16));
        label2.setText("Disks:");
        panelMemory.add(label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 60, 70, 30));

        label3.setFont(new java.awt.Font("SansSerif", 1, 16));
        label3.setText("Total Size (bytes):");
        panelMemory.add(label3, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 90, 170, 20));

        labelTotalRam.setFont(new java.awt.Font("SansSerif", 0, 16));
        labelTotalRam.setText(". . .");
        panelMemory.add(labelTotalRam, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 90, 131, 20));

        label4.setFont(new java.awt.Font("SansSerif", 1, 16));
        label4.setText("Used Size (bytes):");
        panelMemory.add(label4, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 120, 170, 20));

        labelUsedRam.setFont(new java.awt.Font("SansSerif", 0, 16));
        labelUsedRam.setText(". . .");
        panelMemory.add(labelUsedRam, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 120, 131, 20));

        label5.setFont(new java.awt.Font("SansSerif", 1, 16));
        label5.setText("Virtual:");
        panelMemory.add(label5, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 210, -1, -1));

        label6.setFont(new java.awt.Font("SansSerif", 1, 16));
        label6.setText("Total Size (bytes):");
        panelMemory.add(label6, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 240, 170, 20));

        labelTotalVirtual.setFont(new java.awt.Font("SansSerif", 0, 16));
        labelTotalVirtual.setText(". . .");
        panelMemory.add(labelTotalVirtual, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 240, 131, 20));

        label7.setFont(new java.awt.Font("SansSerif", 1, 16));
        label7.setText("Used Size (bytes):");
        panelMemory.add(label7, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 270, 170, 20));

        labelUsedVirtual.setFont(new java.awt.Font("SansSerif", 0, 16));
        labelUsedVirtual.setText(". . .");
        panelMemory.add(labelUsedVirtual, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 270, 131, 20));

        label8.setFont(new java.awt.Font("SansSerif", 1, 16));
        label8.setText("Other:");
        panelMemory.add(label8, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 350, -1, -1));

        label9.setFont(new java.awt.Font("SansSerif", 1, 16));
        label9.setText("Total Size (bytes):");
        panelMemory.add(label9, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 380, 170, 20));

        label10.setFont(new java.awt.Font("SansSerif", 1, 16));
        label10.setText("Used Size (bytes):");
        panelMemory.add(label10, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 410, 170, 20));

        labelUsedOther.setFont(new java.awt.Font("SansSerif", 0, 16));
        labelUsedOther.setText(". . .");
        panelMemory.add(labelUsedOther, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 410, 131, 20));

        labelTotalOther.setFont(new java.awt.Font("SansSerif", 0, 16));
        labelTotalOther.setText(". . .");
        panelMemory.add(labelTotalOther, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 380, 131, 20));

        label1.setFont(new java.awt.Font("SansSerif", 1, 16));
        label1.setText("RAM:");
        panelMemory.add(label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 60, -1, -1));

        scrollpane2.setBorder(null);
        scrollpane2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        tableDisks.setBorder(null);
        tableDisks.setFont(new java.awt.Font("SansSerif", 0, 16));
        tableDisks.setModel(new DefaultTableModel(
                new Object[][]{},
                this.tableDiskHeaders
        ));
        scrollpane2.setViewportView(tableDisks);

        panelMemory.add(scrollpane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 100, 520, 340));

        add(panelMemory, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 320, 1060, 540));

        label11.setFont(new java.awt.Font("SansSerif", 1, 16));
        label11.setText("Update Period (s):");
        add(label11, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 880, -1, 30));

        tfieldUpdatePeirod.setFont(new java.awt.Font("SansSerif", 1, 16));
        add(tfieldUpdatePeirod, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 880, 90, 30));

        buttonStart.setBackground(new java.awt.Color(70, 120, 227));
        buttonStart.setFont(new java.awt.Font("SansSerif", 1, 16));
        buttonStart.setForeground(java.awt.Color.white);
        buttonStart.setText("Start");
        buttonStart.setBorderPainted(false);
        add(buttonStart, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 880, 80, -1));

        buttonStop.setBackground(new java.awt.Color(70, 120, 227));
        buttonStop.setFont(new java.awt.Font("SansSerif", 1, 16));
        buttonStop.setForeground(java.awt.Color.white);
        buttonStop.setText("Stop");
        buttonStop.setBorderPainted(false);
        add(buttonStop, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 880, 80, -1));

        labelUpdatedTime.setFont(new java.awt.Font("SansSerif", 0, 16));
        labelUpdatedTime.setText(". . .");
        add(labelUpdatedTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(211, 890, 280, 20));

        label12.setFont(new java.awt.Font("SansSerif", 1, 16));
        label12.setText("Updated Time:");
        add(label12, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 890, 130, 20));

    }

    public synchronized void initData(int deviceId) {
        this.deviceId = deviceId;
        DeviceResourceManagementController resourceController = new DeviceResourceManagementController();
        if (!resourceController.processGettingDeviceResource(deviceId, 
                ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices().getSelectedDeviceStates())) {
            JOptionPane.showMessageDialog(null, resourceController.getResultMessage());
        }
    }

    public synchronized void updateView(int deviceId, List<Object> cpuData, List<Object> memoryData, String updatedTime) {
        DefaultTableModel tableModel = (DefaultTableModel) this.tableCPU.getModel();
        this.clearTable(tableModel);
        for (Object cpuState : cpuData) {
            tableModel.addRow((Object[]) cpuState);
        }

        Object[] memoryState = (Object[]) memoryData.get(DataOrders.MEMORY_RAM.getValue());
        this.labelTotalRam.setText(String.valueOf(memoryState[DataOrders.MEMORY_TOTAL.getValue()]));
        this.labelUsedRam.setText(String.valueOf(memoryState[DataOrders.MEMORY_USED.getValue()]));

        memoryState = (Object[]) memoryData.get(DataOrders.MEMORY_VIRTUAL.getValue());
        this.labelTotalVirtual.setText(String.valueOf(memoryState[DataOrders.MEMORY_TOTAL.getValue()]));
        this.labelUsedVirtual.setText(String.valueOf(memoryState[DataOrders.MEMORY_USED.getValue()]));

        memoryState = (Object[]) memoryData.get(DataOrders.MEMORY_OTHER.getValue());
        this.labelTotalOther.setText(String.valueOf(memoryState[DataOrders.MEMORY_TOTAL.getValue()]));
        this.labelUsedOther.setText(String.valueOf(memoryState[DataOrders.MEMORY_USED.getValue()]));

        tableModel = (DefaultTableModel) this.tableDisks.getModel();

        this.clearTable(tableModel);
        int tempSize = memoryData.size();
        for (int i = DataOrders.MEMORY_DISK.getValue(); i < tempSize; i++) {
            tableModel.addRow((Object[]) (memoryData.get(i)));
        }

        this.labelUpdatedTime.setText(updatedTime);

        this.revalidate();
        this.repaint();
    }

    private void clearTable(DefaultTableModel tableModel) {
        int rowCount = tableModel.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        if (!enabled) {
            SnmpManager.getInstance().getQueryTimerManager().cancelDeviceResourceTimer();
        }
    }
}
