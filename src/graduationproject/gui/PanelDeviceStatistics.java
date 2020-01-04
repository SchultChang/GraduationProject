/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.gui;

import graduationproject.controllers.ChartManagementController;
import graduationproject.controllers.ChartManagementController.QueryPeriod;
import graduationproject.controllers.DeviceResourceManagementController;
import graduationproject.controllers.InterfaceManagementController;
import graduationproject.snmpd.helpers.DeviceQueryHelper.MemoryType;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.internal.chartpart.Chart;
import org.netbeans.lib.awtextra.AbsoluteConstraints;

/**
 *
 * @author cloud
 */
public class PanelDeviceStatistics extends JPanel {

    private final AbsoluteConstraints PANEL_CPU_POSITION = new AbsoluteConstraints(80, 120, 990, 190);
    private final AbsoluteConstraints PANEL_MEMORY_POSITION = new AbsoluteConstraints(80, 380, 990, 190);
    private final AbsoluteConstraints PANEL_BANDWIDTH_POSITION = new AbsoluteConstraints(80, 650, 990, 190);
    private final String DEFAULT_CHOICE_VALUE = "All";
    private Integer[] cpuChoices;
    private String[] memoryChoices = {MemoryType.RAM.getDisplayType(), MemoryType.VIRTUAL.getDisplayType(), MemoryType.OTHER.getDisplayType()};
    private String[] interfaceChoices;

    private ButtonGroup buttonGroup;
    private JRadioButton button3days;
    private JRadioButton button7days;
    private JRadioButton buttonToday;
    private JRadioButton buttonYesterday;
    private JComboBox<String> cboxInterfaces;
    private JComboBox<String> cboxMemory;
    private JComboBox<String> cboxCPU;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JLabel label6;
    private JLabel label7;

    private ChartViewer chartViewer;
    private XChartPanel currentCPUPanel;
    private XChartPanel currentMemoryPanel;
    private XChartPanel currentBandwidthPanel;

    private MouseAdapter listenerPanel;
    private ActionListener listenerRadioButton;
    private ItemListener listenerComboBox;

    private int currentCpuChoiceId; 
    private int currentMemoryChoiceId;
    private int currentInterfaceChoiceId;
    private int deviceId;

    public PanelDeviceStatistics() {
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
        label7 = new JLabel();
        cboxInterfaces = new JComboBox<>();
        cboxMemory = new JComboBox<>();
        cboxCPU = new JComboBox<>();

        setPreferredSize(new java.awt.Dimension(1160, 940));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        setBackground(Color.white);

        chartViewer = new ChartViewer();
        add(chartViewer, new AbsoluteConstraints(70, 40, 1000, 810));
        switchChartViewerVisibility(false, null);

        label1.setFont(new java.awt.Font("SansSerif", 1, 16));
        label1.setText("CPU Load:");
        add(label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 80, -1, 30));

        button7days.setFont(new java.awt.Font("SansSerif", 0, 16));
        button7days.setText("Last 7 days");
        button7days.setActionCommand(QueryPeriod.LAST_7_DAYS.getValue());
        button7days.setOpaque(false);
        add(button7days, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 40, -1, 30));

        buttonToday.setFont(new java.awt.Font("SansSerif", 0, 16));
        buttonToday.setText("Today");
        buttonToday.setActionCommand(QueryPeriod.TODAY.getValue());
        buttonToday.setOpaque(false);
        add(buttonToday, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 40, -1, 30));

        buttonYesterday.setFont(new java.awt.Font("SansSerif", 0, 16));
        buttonYesterday.setText("Yesterday");
        buttonYesterday.setActionCommand(QueryPeriod.YESTERDAY.getValue());
        buttonYesterday.setOpaque(false);
        add(buttonYesterday, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 40, -1, 30));

        button3days.setFont(new java.awt.Font("SansSerif", 0, 16));
        button3days.setText("Last 3 days");
        button3days.setActionCommand(QueryPeriod.LAST_3_DAYS.getValue());
        button3days.setOpaque(false);
        add(button3days, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 40, -1, 30));

        buttonGroup.add(buttonToday);
        buttonGroup.add(buttonYesterday);
        buttonGroup.add(button3days);
        buttonGroup.add(button7days);
        buttonToday.setSelected(true);

        label2.setFont(new java.awt.Font("SansSerif", 1, 16));
        label2.setText("Period:");
        add(label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 40, -1, 30));

        label3.setFont(new java.awt.Font("SansSerif", 1, 16));
        label3.setText("RAM Usage:");
        add(label3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 340, -1, 30));

        label4.setFont(new java.awt.Font("SansSerif", 1, 16));
        label4.setText("Interface");
        add(label4, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 610, -1, 30));

        label5.setFont(new java.awt.Font("SansSerif", 1, 16));
        label5.setText("Bandwidth Utilization:");
        add(label5, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 610, -1, 30));

        cboxInterfaces.setFont(new java.awt.Font("SansSerif", 0, 16));
        cboxInterfaces.setModel(new DefaultComboBoxModel<>(new String[]{DEFAULT_CHOICE_VALUE}));
        cboxInterfaces.setSelectedIndex(0);
        add(cboxInterfaces, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 610, 290, 30));

        label6.setFont(new java.awt.Font("SansSerif", 1, 16));
        label6.setText("Memory");
        add(label6, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 340, -1, 30));

        cboxMemory.setFont(new java.awt.Font("SansSerif", 0, 16));
        DefaultComboBoxModel memoryModel = new DefaultComboBoxModel<>();
        memoryModel.addElement(DEFAULT_CHOICE_VALUE);
        for (String memoryChoice : memoryChoices) {
            memoryModel.addElement(memoryChoice);
        }
        cboxMemory.setModel(memoryModel);
        cboxMemory.setSelectedIndex(0);
        add(cboxMemory, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 340, 290, 30));

        label7.setFont(new java.awt.Font("SansSerif", 1, 16));
        label7.setText("CPU");
        add(label7, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 80, 50, 30));

        cboxCPU.setFont(new java.awt.Font("SansSerif", 0, 16));
        cboxCPU.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        add(cboxCPU, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 80, 290, 30));

    }

    private void initListeners() {
        listenerRadioButton = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JRadioButton source = (JRadioButton) e.getSource();
                ChartManagementController chartController = new ChartManagementController();
                displayAllCharts(deviceId, source.getActionCommand());
            }
        };
        this.buttonToday.addActionListener(listenerRadioButton);
        this.buttonYesterday.addActionListener(listenerRadioButton);
        this.button3days.addActionListener(listenerRadioButton);
        this.button7days.addActionListener(listenerRadioButton);

        listenerComboBox = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    JComboBox source = (JComboBox) e.getSource();
                    if (source == cboxCPU) {
                        if (source.getSelectedIndex() != currentCpuChoiceId) {
                            currentCpuChoiceId = cboxCPU.getSelectedIndex();
                            ChartManagementController chartController = new ChartManagementController();
                            
                            Chart chart = chartController.processGettingChart(deviceId, 
                                    ChartManagementController.DataType.CPU_LOAD, 
                                    buttonGroup.getSelection().getActionCommand(), 
                                    getCPUChoices());
                            if (chart == null) {
                                JOptionPane.showMessageDialog(null, chartController.getResultMessage());
                                return;
                            }          
                            
                            currentCPUPanel = displayChart(currentCPUPanel, chart, PANEL_CPU_POSITION);
                            PanelDeviceStatistics.this.revalidate();
                            PanelDeviceStatistics.this.repaint();
                        }
                    }
                    if (source == cboxMemory) {
                        if (source.getSelectedIndex() != currentMemoryChoiceId) {
                            currentMemoryChoiceId = cboxMemory.getSelectedIndex();
                            ChartManagementController chartController = new ChartManagementController();

                            Chart chart = chartController.processGettingChart(deviceId,
                                    ChartManagementController.DataType.MEMORY_USAGE,
                                    buttonGroup.getSelection().getActionCommand(),
                                    getMemoryChoices());
                            if (chart == null) {
                                JOptionPane.showMessageDialog(null, chartController.getResultMessage());
                                return;
                            }

                            currentMemoryPanel = displayChart(currentMemoryPanel, chart, PANEL_MEMORY_POSITION);
                            PanelDeviceStatistics.this.revalidate();
                            PanelDeviceStatistics.this.repaint();
                        }
                    }
                    if (source == cboxInterfaces) {
                        if (source.getSelectedIndex() != currentInterfaceChoiceId) {
                            currentInterfaceChoiceId = cboxInterfaces.getSelectedIndex();
                            ChartManagementController chartController = new ChartManagementController();

                            Chart chart = chartController.processGettingChart(deviceId,
                                    ChartManagementController.DataType.BANDWIDTH_USAGE,
                                    buttonGroup.getSelection().getActionCommand(),
                                    getInterfaceChoices());
                            if (chart == null) {
                                JOptionPane.showMessageDialog(null, chartController.getResultMessage());
                                return;
                            }

                            currentBandwidthPanel = displayChart(currentBandwidthPanel, chart, PANEL_BANDWIDTH_POSITION);
                            PanelDeviceStatistics.this.revalidate();
                            PanelDeviceStatistics.this.repaint();
                        }
                    }
                }
            }

        };
        this.cboxCPU.addItemListener(listenerComboBox);
        this.cboxInterfaces.addItemListener(listenerComboBox);
        this.cboxMemory.addItemListener(listenerComboBox);

        this.listenerPanel = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                XChartPanel source = (XChartPanel) e.getSource();
                if (source == currentCPUPanel
                        || source == currentBandwidthPanel
                        || source == currentMemoryPanel) {
                    switchChartViewerVisibility(true, source.getChart());
                } else {
                    switchChartViewerVisibility(false, null);
                }
            }
        };
        this.chartViewer.addMouseListener(this.listenerPanel);
    }

    private void switchChartViewerVisibility(boolean visible, Chart chart) {
        if (visible) {
            this.chartViewer.displayChart(chart);
        }
        this.chartViewer.setVisible(visible);
        this.chartViewer.setEnabled(visible);

        this.revalidate();
        this.repaint();
    }

    public void initViewData(int deviceId) {
        this.deviceId = deviceId;
        this.currentCpuChoiceId = 0;
        this.currentMemoryChoiceId = 0;
        this.currentInterfaceChoiceId = 0;

        this.buttonToday.setSelected(true);
        this.switchChartViewerVisibility(false, null);

        List<String> interfaceNames = new InterfaceManagementController().processGettingInterfaceNames(this.deviceId);
        if (interfaceNames != null && !interfaceNames.isEmpty()) {
            DefaultComboBoxModel cboxModel = (DefaultComboBoxModel) this.cboxInterfaces.getModel();
            cboxModel.removeAllElements();
            cboxModel.addElement(DEFAULT_CHOICE_VALUE);

            int tempSize = interfaceNames.size();
            this.interfaceChoices = new String[tempSize];
            for (int i = 0; i < tempSize; i++) {
                this.interfaceChoices[i] = interfaceNames.get(i);
                cboxModel.addElement(this.interfaceChoices[i]);
            }
        }

        List<Integer> cpuDeviceIds = new DeviceResourceManagementController().processGettingCPUIds(deviceId);
        if (cpuDeviceIds != null && !cpuDeviceIds.isEmpty()) {
            DefaultComboBoxModel cboxModel = (DefaultComboBoxModel) this.cboxCPU.getModel();
            cboxModel.removeAllElements();
            cboxModel.addElement(DEFAULT_CHOICE_VALUE);
            
            int tempSize = cpuDeviceIds.size();
            this.cpuChoices = new Integer[tempSize];
            for (int i = 0; i < tempSize; i++) {
                this.cpuChoices[i] = cpuDeviceIds.get(i);
                cboxModel.addElement(this.cpuChoices[i] - this.cpuChoices[0] + 1);
            }
        }
        
        this.cboxCPU.setSelectedIndex(0);
        this.cboxInterfaces.setSelectedIndex(0);
        this.cboxMemory.setSelectedIndex(0);

        this.displayAllCharts(deviceId, this.buttonToday.getActionCommand());
    }

    private void displayAllCharts(int deviceId, String period) {
        ChartManagementController chartController = new ChartManagementController();

        Chart chart = chartController.processGettingChart(deviceId, ChartManagementController.DataType.CPU_LOAD, period, this.getCPUChoices());
        if (chart == null) {
            JOptionPane.showMessageDialog(null, chartController.getResultMessage());
            return;
        }
        this.currentCPUPanel = this.displayChart(this.currentCPUPanel, chart, PANEL_CPU_POSITION);

        chart = chartController.processGettingChart(deviceId, ChartManagementController.DataType.MEMORY_USAGE, period, this.getMemoryChoices());
        if (chart == null) {
            JOptionPane.showMessageDialog(null, chartController.getResultMessage());
            return;
        }
        this.currentMemoryPanel = this.displayChart(this.currentMemoryPanel, chart, PANEL_MEMORY_POSITION);

        chart = chartController.processGettingChart(deviceId, ChartManagementController.DataType.BANDWIDTH_USAGE, period, this.getInterfaceChoices());
        if (chart == null) {
            JOptionPane.showConfirmDialog(null, chartController.getResultMessage());
            return;
        }
        this.currentBandwidthPanel = this.displayChart(this.currentBandwidthPanel, chart, PANEL_BANDWIDTH_POSITION);

        this.revalidate();
        this.repaint();

        System.gc();
    }

    private XChartPanel displayChart(XChartPanel removableChart, Chart newChart, AbsoluteConstraints position) {
        if (removableChart != null) {
            this.remove(removableChart);
        }

        XChartPanel<Chart> newPanel = new XChartPanel<Chart>(newChart);
        newPanel.addMouseListener(this.listenerPanel);
        this.add(newPanel, position);
        return newPanel;
    }
    
    private Integer[] getCPUChoices() {
        if (this.cboxCPU.getSelectedItem().toString().equals(DEFAULT_CHOICE_VALUE)) {
            return this.cpuChoices;
        }
        if (this.cpuChoices != null) {
            return new Integer[] {this.cpuChoices[this.cboxCPU.getSelectedIndex() - 1]};
        }
        return null;
    }

    private String[] getMemoryChoices() {
        if (this.cboxMemory.getSelectedItem().toString().equals(DEFAULT_CHOICE_VALUE)) {
            return this.memoryChoices;
        }
        return new String[]{this.cboxMemory.getSelectedItem().toString()};
    }

    private String[] getInterfaceChoices() {
        if (this.cboxInterfaces.getSelectedItem().toString().equals(DEFAULT_CHOICE_VALUE)) {
            return this.interfaceChoices;
        }
        return new String[]{this.cboxInterfaces.getSelectedItem().toString()};
    }

    public class ChartViewer extends JPanel {

        public ChartViewer() {
            super();
            setBackground(java.awt.Color.white);
            setMaximumSize(new java.awt.Dimension(1000, 800));
            setMinimumSize(new java.awt.Dimension(1000, 800));
            setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        }

        public void displayChart(Chart chart) {
            this.removeAll();
//                System.out.println("CHART IS NULL");
            XChartPanel<Chart> chartPanel = new XChartPanel<Chart>(chart);
            add(chartPanel, new AbsoluteConstraints(0, 0, 1000, 800));
            chartPanel.addMouseListener(listenerPanel);
        }
    }
}
