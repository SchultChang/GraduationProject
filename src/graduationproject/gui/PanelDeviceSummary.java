/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.gui;

import graduationproject.controllers.ChartManagementController;
import graduationproject.controllers.ChartManagementController.QueryPeriod;
import graduationproject.controllers.InterfaceManagementController;
import graduationproject.snmpd.helpers.DeviceQueryHelper;
import graduationproject.snmpd.helpers.DeviceQueryHelper.MemoryType;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.netbeans.lib.awtextra.AbsoluteConstraints;

/**
 *
 * @author cloud
 */
public class PanelDeviceSummary extends JPanel {

    private final AbsoluteConstraints PANEL_CPU_POSITION = new AbsoluteConstraints(80, 120, 990, 190);
    private final AbsoluteConstraints PANEL_RAM_POSITION = new AbsoluteConstraints(80, 380, 990, 190);
    private final AbsoluteConstraints PANEL_BANDWIDTH_POSITION = new AbsoluteConstraints(80, 650, 990, 190);
    private final String DEFAULT_CHOICE_VALUE = "All";
    private String[] memoryChoices = {DEFAULT_CHOICE_VALUE,
        MemoryType.RAM.getDisplayType(), MemoryType.VIRTUAL.getDisplayType(), MemoryType.OTHER.getDisplayType()};
//    private String[] interfaceChoices;

    private ButtonGroup buttonGroup;
    private JRadioButton button3days;
    private JRadioButton button7days;
    private JRadioButton buttonToday;
    private JRadioButton buttonYesterday;
    private JComboBox<String> cboxInterfaces;
    private JComboBox<String> cboxMemory;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JLabel label6;

    private XChartPanel currentCPUPanel;
    private XChartPanel currentRamPanel;
    private XChartPanel currentBandwidthPanel;

    private ActionListener listenerCheckBox;

    private int deviceId;

    public PanelDeviceSummary() {
        initComponents();
        initListeners();
    }

    private void initComponents() {

        label1 = new JLabel();
        buttonGroup = new ButtonGroup();
        button7days = new JRadioButton();
        buttonToday = new JRadioButton();
        buttonYesterday = new JRadioButton();
        button3days = new JRadioButton();
        label2 = new JLabel();
        label3 = new JLabel();
        label4 = new JLabel();
        label5 = new JLabel();
        label6 = new JLabel();
        cboxInterfaces = new JComboBox<>();
        cboxMemory = new JComboBox<>();

        setPreferredSize(new java.awt.Dimension(1160, 940));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        setBackground(Color.white);

        label1.setFont(new java.awt.Font("SansSerif", 1, 16));
        label1.setText("CPU Load:");
        add(label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 80, -1, 30));

        button7days.setFont(new java.awt.Font("SansSerif", 0, 16));
        button7days.setText("Last 7 days");
        button7days.setActionCommand(QueryPeriod.LAST_7_DAYS.getValue());
        button7days.setOpaque(false);
        add(button7days, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 50, -1, 30));

        buttonToday.setFont(new java.awt.Font("SansSerif", 0, 16));
        buttonToday.setText("Today");
        buttonToday.setActionCommand(QueryPeriod.TODAY.getValue());
        buttonToday.setOpaque(false);
        add(buttonToday, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 50, -1, 30));

        buttonYesterday.setFont(new java.awt.Font("SansSerif", 0, 16));
        buttonYesterday.setText("Yesterday");
        buttonYesterday.setActionCommand(QueryPeriod.YESTERDAY.getValue());
        buttonYesterday.setOpaque(false);
        add(buttonYesterday, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 50, -1, 30));

        button3days.setFont(new java.awt.Font("SansSerif", 0, 16));
        button3days.setText("Last 3 days");
        button3days.setActionCommand(QueryPeriod.LAST_3_DAYS.getValue());
        button3days.setOpaque(false);
        add(button3days, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 50, -1, 30));

        buttonGroup.add(buttonToday);
        buttonGroup.add(buttonYesterday);
        buttonGroup.add(button3days);
        buttonGroup.add(button7days);
        buttonToday.setSelected(true);

        label2.setFont(new java.awt.Font("SansSerif", 1, 16));
        label2.setText("Period:");
        add(label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 50, -1, 30));

        label3.setFont(new java.awt.Font("SansSerif", 1, 16));
        label3.setText("RAM Usage:");
        add(label3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 340, -1, 30));

        label4.setFont(new java.awt.Font("SansSerif", 1, 16));
        label4.setText("Interface:");
        add(label4, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 610, -1, 30));

        label5.setFont(new java.awt.Font("SansSerif", 1, 16));
        label5.setText("Bandwidth Utilization:");
        add(label5, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 610, -1, 30));

        cboxInterfaces.setFont(new java.awt.Font("SansSerif", 0, 16));
        cboxInterfaces.setModel(new DefaultComboBoxModel<>(new String[]{DEFAULT_CHOICE_VALUE}));
        add(cboxInterfaces, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 610, 290, 30));

        label6.setFont(new java.awt.Font("SansSerif", 1, 16));
        label6.setText("Memory:");
        add(label6, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 340, -1, 30));

        cboxMemory.setFont(new java.awt.Font("SansSerif", 0, 16));
        cboxMemory.setModel(new javax.swing.DefaultComboBoxModel<>(this.memoryChoices));
        add(cboxMemory, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 340, 290, 30));

    }

    private void initListeners() {
        listenerCheckBox = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JRadioButton source = (JRadioButton) e.getSource();
                ChartManagementController chartController = new ChartManagementController();
                displayAllCharts(deviceId, source.getActionCommand());
            }
        };

        this.buttonToday.addActionListener(listenerCheckBox);
        this.buttonYesterday.addActionListener(listenerCheckBox);
        this.button3days.addActionListener(listenerCheckBox);
        this.button7days.addActionListener(listenerCheckBox);
    }

    public void initData(int deviceId) {
        this.deviceId = deviceId;
        this.buttonToday.setSelected(true);

        {
            List<String> interfaceNames = new InterfaceManagementController().processGettingInterfaceNames(this.deviceId);
            
            if (interfaceNames != null && !interfaceNames.isEmpty()) {
                DefaultComboBoxModel cboxModel = (DefaultComboBoxModel) this.cboxInterfaces.getModel();
                cboxModel.removeAllElements();
                cboxModel.addElement(DEFAULT_CHOICE_VALUE);
                
                for (String interfaceName : interfaceNames) {
                    cboxModel.addElement(interfaceName);
                }
            }
        }

        this.cboxInterfaces.setSelectedIndex(0);
        this.cboxMemory.setSelectedIndex(0);
        
        this.displayAllCharts(deviceId, this.buttonToday.getActionCommand());
    }

    private void displayAllCharts(int deviceId, String period) {
        ChartManagementController chartController = new ChartManagementController();
        XYChart chart = chartController.processGettingChart(deviceId, ChartManagementController.DataType.CPU_LOAD, period);
        if (chart == null) {
            JOptionPane.showMessageDialog(null, chartController.getResultMessage());
            return;
        }

        this.currentCPUPanel = this.displayChart(this.currentCPUPanel, chart, PANEL_CPU_POSITION);

        this.revalidate();
        this.repaint();

        System.gc();
    }

    private XChartPanel<XYChart> displayChart(XChartPanel removableChart, XYChart newChart, AbsoluteConstraints position) {
        if (removableChart != null) {
            this.remove(removableChart);
        }

        XChartPanel<XYChart> newPanel = new XChartPanel<XYChart>(newChart);
        this.add(newPanel, position);
        return newPanel;
    }
}
