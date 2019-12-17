/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.gui;

import graduationproject.controllers.DeviceManagementController;
import graduationproject.controllers.DeviceManagementController.DataOrders;
import graduationproject.controllers.DeviceManagementController.DeviceStates;
import graduationproject.controllers.InterfaceManagementController;
import graduationproject.controllers.InterfaceManagementController.InterfaceStates;
import graduationproject.helpers.TopoDrawer;
import graduationproject.snmpd.SnmpManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

/**
 *
 * @author cloud
 */
public class PanelImportedDevices extends JPanel {

    private JButton buttonImport;
    private JButton buttonTopology;
    private JLabel labelHideDeviceList;
    private JLabel labelShowDeviceList;
    private JLabel labelSearch;
    private JPanel panelDeviceList;
    private JPanel panelDevices;
    private JScrollPane scrollpane1;
    private JTextField tfieldSearch;

    private JPanel currentDisplayedPanel;
    private PanelBasicTopology panelTopology;
    private PanelDeviceInfo panelDeviceInfo;
    private PanelInterfaceInfo panelInterfaceInfo;
    private PanelMonitoringDevice panelMonitoringDevice;
    private PanelDeviceResources panelDeviceResources;
    private PanelDeviceSummary panelDeviceSummary;

    private List<LabelDevice> labelDevices;
    private List<LabelInterface> labelInterfaces;

    private JPopupMenu pmenuDevices;
    private JMenuItem mitemDelete;

    private ActionListener listenerButton;
    private KeyAdapter listenerField;
    private MouseAdapter listenerListDevices;
    private MouseAdapter listenerListInterfaces;
    private MouseAdapter listenerLabel;
    private ActionListener listenerItems;

    private final String ICON_DEVICE_ACTIVE_PATH = "/resources/icon_active_30.png";
    private final String ICON_DEVICE_DEACTIVE_PATH = "/resources/icon_deactive_30.png";
    private final String ICON_INTERFACE_ACTIVE_PATH = "/resources/icon_active_20.png";
    private final String ICON_INTERFACE_DEACTIVE_PATH = "/resources/icon_deactive_20.png";

    //some action constants
    private final int LABEL_HOVER = 1;
    private final int LABEL_CLICK = 2;

    private LabelInterface currentChosenLabelInterface;
    private LabelDevice pendingLabelToDelete;
    private LabelDevice currentChosenLabelDevice;
    private DataOrders currentDataOrder;

    private boolean enableInterfaces = true;

    public enum PANELS {
        PANEL_TOPOLOGY,
        PANEL_DEVICE_INFO,
        PANEL_INTERFACE_INFO,
        PANEL_MONITORING_DEVICE,
        PANEL_DEVICE_RESOURCES,
        PANEL_DEVICE_SUMMARY
    }

//    private List<DeviceStates> deviceStates;
    public PanelImportedDevices() {
        initComponents();
        initListeners();
        initOtherComponents();
    }

    private void initComponents() {

        panelDevices = new JPanel();
        tfieldSearch = new JTextField();
        scrollpane1 = new JScrollPane();
        panelDeviceList = new JPanel();
        labelHideDeviceList = new JLabel();
        labelShowDeviceList = new JLabel();
        labelSearch = new JLabel();
        buttonImport = new JButton();
        buttonTopology = new JButton();

        setPreferredSize(new java.awt.Dimension(1600, 940));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        setBackground(Color.white);

        labelShowDeviceList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icon_double_right_blue_40.png"))); 
        add(labelShowDeviceList, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, 40, 190));
        labelShowDeviceList.setVisible(false);
        labelShowDeviceList.setEnabled(false);
        
        panelDevices.setBackground(new java.awt.Color(20, 51, 125));
        panelDevices.setPreferredSize(new java.awt.Dimension(280, 940));
        panelDevices.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tfieldSearch.setFont(new java.awt.Font("SansSerif", 0, 15));
        tfieldSearch.setForeground(java.awt.Color.white);
        tfieldSearch.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, java.awt.Color.white));
        tfieldSearch.setCaretColor(java.awt.Color.white);
        tfieldSearch.setOpaque(false);
        panelDevices.add(tfieldSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 310, 30));

        scrollpane1.setBackground(java.awt.Color.white);
        scrollpane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panelDeviceList.setBackground(java.awt.Color.white);
        panelDeviceList.setPreferredSize(new java.awt.Dimension(370, 2000));
        panelDeviceList.setLayout(new BoxLayout(panelDeviceList, BoxLayout.PAGE_AXIS));
        scrollpane1.setViewportView(panelDeviceList);

        panelDevices.add(scrollpane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 360, 690));

        labelSearch.setHorizontalAlignment(SwingConstants.CENTER);
        labelSearch.setIcon(new ImageIcon(getClass().getResource("/resources/icon_search_40.png")));
        panelDevices.add(labelSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 40, 50, 30));

        buttonImport.setBackground(new java.awt.Color(38, 56, 163));
        buttonImport.setFont(new java.awt.Font("SansSerif", 1, 15));
        buttonImport.setForeground(java.awt.Color.white);
        buttonImport.setIcon(new ImageIcon(getClass().getResource("/resources/icon_plus_40.png")));
        buttonImport.setText("Import");
        buttonImport.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        buttonImport.setBorderPainted(false);
        panelDevices.add(buttonImport, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 830, 120, 40));

        buttonTopology.setBackground(new java.awt.Color(38, 56, 163));
        buttonTopology.setFont(new java.awt.Font("SansSerif", 1, 15));
        buttonTopology.setForeground(java.awt.Color.white);
        buttonTopology.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icon_network2_40.png")));
        buttonTopology.setText("Topology");
        buttonTopology.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        buttonTopology.setBorderPainted(false);
        panelDevices.add(buttonTopology, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 830, 150, 40));

        labelHideDeviceList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icon_double_left_white_40.png"))); 
        panelDevices.add(labelHideDeviceList, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 330, 30, 190));

        add(panelDevices, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 440, -1));

        initChildPanels();
        initMenu();
    }

    public void initChildPanels() {
        this.panelDeviceInfo = new PanelDeviceInfo();
        this.panelDeviceInfo.setVisible(false);
        this.panelDeviceInfo.setEnabled(false);

        this.panelInterfaceInfo = new PanelInterfaceInfo();
        this.panelInterfaceInfo.setVisible(false);
        this.panelInterfaceInfo.setEnabled(false);

        this.panelMonitoringDevice = new PanelMonitoringDevice();
        this.panelMonitoringDevice.setVisible(false);
        this.panelMonitoringDevice.setEnabled(false);

        this.panelDeviceResources = new PanelDeviceResources();
        this.panelDeviceResources.setVisible(false);
        this.panelDeviceResources.setEnabled(false);

        this.panelDeviceSummary = new PanelDeviceSummary();
        this.panelDeviceSummary.setVisible(false);
        this.panelDeviceSummary.setEnabled(false);

        this.panelTopology = new PanelBasicTopology();
        this.panelTopology.setVisible(false);
        this.panelTopology.setEnabled(false);
    }

    private void initMenu() {
        this.pmenuDevices = new JPopupMenu();

        this.mitemDelete = new JMenuItem("Delete");
        this.pmenuDevices.add(this.mitemDelete);
    }

    private void initListeners() {
        this.listenerButton = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton source = (JButton) e.getSource();

                if (source == buttonImport) {
                    JFileChooser fileChooser = new JFileChooser();
                    int result = fileChooser.showOpenDialog(null);

                    if (result == JFileChooser.APPROVE_OPTION) {
                        DeviceManagementController deviceController = new DeviceManagementController();
                        File file = fileChooser.getSelectedFile();

                        if (!deviceController.processImportingDevicesFromFile(file)) {
                            JOptionPane.showMessageDialog(null, deviceController.getResultMessage());
                        } else {
//                            SnmpManager.getInstance().getQueryTimerManager().cancelDeviceActiveTimer();
//                            SnmpManager.getInstance().getQueryTimerManager().cancelInterfaceTimer();
                            initData();
                        }
                    }
                }
                if (source == buttonTopology) {
                    switchDeviceListVisibility();
                    switchDisplayedPanel(PANELS.PANEL_TOPOLOGY);
                    panelTopology.initTopo();
                }
            }

        };
        this.buttonImport.addActionListener(this.listenerButton);
        this.buttonTopology.addActionListener(this.listenerButton);

        this.listenerField = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                JTextField source = (JTextField) e.getSource();
                if (source == tfieldSearch) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        DeviceManagementController deviceController = new DeviceManagementController();
                        List<String> data = deviceController.processSearchingDevices(currentDataOrder, tfieldSearch.getText());

                        updateDeviceList(deviceController.getDeviceIds(), data);
//                        if (data != null) {
//                            deviceController.processCheckingStateOfDevices(deviceController.getDeviceIds());
//                            new InterfaceManagementController().processGettingInterfacesOfActiveDevices();
//                        }
                    } else {
                        super.keyReleased(e);
                    }
                }
            }
        };
        this.tfieldSearch.addKeyListener(this.listenerField);

        this.listenerListDevices = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                LabelDevice source = (LabelDevice) e.getSource();
                source.switchBackground(LABEL_HOVER);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                LabelDevice source = (LabelDevice) e.getSource();

                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (!panelDeviceInfo.isVisible()) {
                        switchDisplayedPanel(PANELS.PANEL_DEVICE_INFO);
                    }
                    panelDeviceInfo.initData(source.getDeviceId());

                    if (currentChosenLabelDevice != source) {
                        if (currentChosenLabelDevice != null) {
                            currentChosenLabelDevice.switchBackground(-LABEL_CLICK);
                        }

                        currentChosenLabelDevice = source;
                        currentChosenLabelDevice.switchBackground(LABEL_CLICK);

                        PanelImportedDevices.this.clearLabelInterfaces();
                        PanelImportedDevices.this.displaySavedInterfacesOfDevice(source.getDeviceId());
                        enableInterfaces = false;
                    } else if (!enableInterfaces) {
                        PanelImportedDevices.this.clearLabelInterfaces();
                        enableInterfaces = true;
                    } else {
                        PanelImportedDevices.this.displaySavedInterfacesOfDevice(source.getDeviceId());
                    }
                    enableInterfaces = false;
                } else {
                    pmenuDevices.show(source, e.getX(), e.getY());
                    pendingLabelToDelete = source;
                }

            }

            @Override
            public void mouseExited(MouseEvent e) {
                LabelDevice source = (LabelDevice) e.getSource();
                if (source != currentChosenLabelDevice) {
                    source.switchBackground(-LABEL_HOVER);
                }
            }
        };

        this.listenerListInterfaces = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                LabelInterface source = (LabelInterface) e.getSource();
                source.switchBackground(LABEL_HOVER);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                LabelInterface source = (LabelInterface) e.getSource();

                //for when we switching the interfaces
                if (!panelInterfaceInfo.isVisible()) {
                    PanelImportedDevices.this.switchDisplayedPanel(PANELS.PANEL_INTERFACE_INFO);
                }
                panelInterfaceInfo.initData(source.getDeviceId(), source.getInterfaceId());

                if (currentChosenLabelInterface != source) {
                    if (currentChosenLabelInterface != null) {
                        currentChosenLabelInterface.switchBackground(-LABEL_CLICK);
                    }

                    source.switchBackground(LABEL_CLICK);
                    currentChosenLabelInterface = source;
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                LabelInterface source = (LabelInterface) e.getSource();
                if (source != currentChosenLabelInterface) {
                    source.switchBackground(-LABEL_HOVER);
                }
            }
        };

        this.listenerItems = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JMenuItem source = (JMenuItem) e.getSource();
                if (source == mitemDelete) {
                    DeviceManagementController deviceController = new DeviceManagementController();
                    if (!deviceController.processDeletingDevice(pendingLabelToDelete.getDeviceId())) {
                        JOptionPane.showMessageDialog(null, deviceController.getResultMessage());
                        return;
                    }

                    initData();
                    System.gc();
                }
            }

        };
        this.mitemDelete.addActionListener(this.listenerItems);
        
        this.listenerLabel = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                JLabel source = (JLabel) e.getSource();
                if (source == labelHideDeviceList) {
                    switchDeviceListVisibility();
                }
                if (source == labelShowDeviceList) {
                    switchDeviceListVisibility();
                }
            }
        };
        
        this.labelShowDeviceList.addMouseListener(this.listenerLabel);
        this.labelHideDeviceList.addMouseListener(this.listenerLabel);
    }

    public void initOtherComponents() {
        this.labelDevices = new ArrayList<LabelDevice>();
        this.labelInterfaces = new ArrayList<LabelInterface>();
    }

    public void initData() {
        this.hideDisplayedPanel();
        this.clearLabelInterfaces();
        this.initDeviceList();
    }

    public void initDeviceList() {
        this.currentDataOrder = DataOrders.LABEL;
        this.currentChosenLabelDevice = null;
        this.currentChosenLabelInterface = null;

        DeviceManagementController deviceController = new DeviceManagementController();
        List<String> data = deviceController.processGettingImportedDevices(this.currentDataOrder);

        if (data == null) {
            JOptionPane.showMessageDialog(null, deviceController.getResultMessage());
            return;
        }

        this.updateDeviceList(deviceController.getDeviceIds(), data);
//        if (data != null) {
//            deviceController.processCheckingStateOfDevices(deviceController.getDeviceIds());
//            new InterfaceManagementController().processGettingInterfacesOfActiveDevices();
//        }
    }

    public synchronized void updateDeviceList(int[] deviceIds, List<String> data) {
        this.panelDeviceList.removeAll();
        this.labelDevices.clear();
        System.gc();

        if (data != null) {
            for (int i = 0; i < deviceIds.length; i++) {
                LabelDevice temp = new LabelDevice(data.get(i), deviceIds[i], DeviceStates.DEACTIVE);
                temp.addMouseListener(this.listenerListDevices);
                this.labelDevices.add(temp);
                this.panelDeviceList.add(temp);
            }
        }

        this.panelDeviceList.revalidate();
        this.panelDeviceList.repaint();
    }

    public synchronized void updateLabelDeviceState(int deviceId, DeviceStates deviceState) {
        int tempSize = this.labelDevices.size();
        for (int i = 0; i < tempSize; i++) {
            LabelDevice temp = this.labelDevices.get(i);
            if (temp.getDeviceId() == deviceId) {
                if (temp.setDeviceState(deviceState) && this.currentChosenLabelDevice == temp) {
                    if (deviceState == DeviceStates.DEACTIVE) {
                        int tempSize1 = this.labelInterfaces.size();
                        for (int j = 0; j < tempSize1; j++) {
                            this.labelInterfaces.get(j).setInterfaceState(InterfaceStates.DOWN);
                        }
                    }
                }
                break;
            }
        }
    }

    public synchronized void updateLabelDeviceText(int deviceId, String text) {
        if (this.currentChosenLabelDevice != null && this.currentChosenLabelDevice.getDeviceId() == deviceId) {
            this.currentChosenLabelDevice.setText(text);
        } else {
            int tempSize = this.labelDevices.size();
            for (int i = 0; i < tempSize; i++) {
                if (this.labelDevices.get(i).getDeviceId() == deviceId) {
                    this.labelDevices.get(i).setText(text);
                    break;
                }
            }
        }
    }

    public void displaySavedInterfacesOfDevice(int deviceId) {
        InterfaceManagementController interfaceController = new InterfaceManagementController();
        if (!interfaceController.processGettingSavedInterfacesOfDevice(deviceId)) {
            JOptionPane.showMessageDialog(null, interfaceController.getResultMessage());
        };
    }

    public void clearLabelInterfaces() {
        int tempSize = this.labelInterfaces.size();
        for (int i = 0; i < tempSize; i++) {
            this.panelDeviceList.remove(this.labelInterfaces.get(i));
        }
        this.panelDeviceList.revalidate();
        this.panelDeviceList.repaint();

        this.labelInterfaces.clear();
        System.gc();
    }

    public synchronized void updateLabelInterfaces(int deviceId, int[] interfaceIds, String[] names, InterfaceStates[] interfaceStates) {
        if (this.currentChosenLabelDevice.getDeviceId() != deviceId) {
            return;
        }

        int tempSize = this.labelDevices.size();
        int listPosition = -1;

        for (int i = 0; i < tempSize; i++) {
            if (this.labelDevices.get(i).getDeviceId() == deviceId) {
                listPosition = i;
                break;
            }
        }

        if (listPosition == -1) {
            return;
        }

        InterfaceStates temp;
        if (this.labelInterfaces.size() != interfaceIds.length) {
            for (int i = listPosition + 1; i < tempSize; i++) {
                this.panelDeviceList.remove(this.labelDevices.get(i));
            }
            this.clearLabelInterfaces();

            for (int i = 0; i < interfaceIds.length; i++) {
                temp = (this.currentChosenLabelDevice.getDeviceState() == DeviceStates.ACTIVE) ? interfaceStates[i] : InterfaceStates.DOWN;
                LabelInterface labelInterface = new LabelInterface(names[i], deviceId, interfaceIds[i], temp);
                labelInterface.addMouseListener(this.listenerListInterfaces);

                this.labelInterfaces.add(labelInterface);
                this.panelDeviceList.add(labelInterface);
            }

            for (int i = listPosition + 1; i < tempSize; i++) {
                this.panelDeviceList.add(this.labelDevices.get(i));
            }
        } else {
            for (int i = 0; i < interfaceIds.length; i++) {
                temp = (this.currentChosenLabelDevice.getDeviceState() == DeviceStates.ACTIVE) ? interfaceStates[i] : InterfaceStates.DOWN;
                this.updateLabelInterfaceState(deviceId, i, names[i], temp);
            }
        }

        this.panelDeviceList.revalidate();
        this.panelDeviceList.repaint();
    }

    public void updateLabelInterfaceState(int deviceId, int interfaceId, String name, InterfaceStates state) {
        if (this.currentChosenLabelDevice != null) {
            if (this.currentChosenLabelDevice.getDeviceId() == deviceId) {
                if (name != null) {
                    this.labelInterfaces.get(interfaceId).setText(name);
                }
                this.labelInterfaces.get(interfaceId).setInterfaceId(interfaceId);
                this.labelInterfaces.get(interfaceId).setInterfaceState(state);
            }
        }
    }
    
    public void switchDeviceListVisibility() {
        boolean temp = this.panelDevices.isVisible();
        this.panelDevices.setVisible(!temp);
        this.panelDevices.setEnabled(!temp);
        
        this.labelShowDeviceList.setVisible(temp);
        this.labelShowDeviceList.setEnabled(temp);
    }

    public void switchDisplayedPanel(PANELS panel) {
        this.hideDisplayedPanel();

        switch (panel) {
            case PANEL_TOPOLOGY:
                this.displayPanel(panelTopology, 0, 0, -1, -1);
                break;
            case PANEL_DEVICE_INFO:
                this.displayPanel(panelDeviceInfo, 440, 0, -1, -1);
                break;
            case PANEL_INTERFACE_INFO:
                this.displayPanel(panelInterfaceInfo, 440, 0, -1, -1);
                break;
            case PANEL_MONITORING_DEVICE:
                this.displayPanel(panelMonitoringDevice, 440, 0, -1, -1);
                break;
            case PANEL_DEVICE_RESOURCES:
                this.displayPanel(panelDeviceResources, 440, 0, -1, -1);
                break;
            case PANEL_DEVICE_SUMMARY:
                this.displayPanel(panelDeviceSummary, 440, 0, -1, -1);
                break;
        }

        this.revalidate();
        this.repaint();
    }

    private void hideDisplayedPanel() {
        if (currentDisplayedPanel != null) {
            this.remove(this.currentDisplayedPanel);
            this.currentDisplayedPanel.setVisible(false);
            this.currentDisplayedPanel.setEnabled(false);
            this.currentDisplayedPanel = null;

            this.revalidate();
            this.repaint();
        }
    }

    private void displayPanel(JPanel panel, int x, int y, int width, int height) {
        panel.setEnabled(true);
        panel.setVisible(true);

        this.add(panel, new AbsoluteConstraints(x, y, width, height));
        this.currentDisplayedPanel = panel;
    }

//    public void refreshPanel() {
//        this.hideDisplayedPanel();
//    }
    public synchronized DeviceStates getDeviceState(int deviceId) {
        int tempSize = this.labelDevices.size();
        for (int i = 0; i < tempSize; i++) {
            if (this.labelDevices.get(i).getDeviceId() == deviceId) {
                return this.labelDevices.get(i).getDeviceState();
            }
        }
        return DeviceStates.DEACTIVE;
    }

    public int getSelectedDeviceId() {
        return this.currentChosenLabelDevice.getDeviceId();
    }

    public DeviceStates getSelectedDeviceStates() {
        return this.currentChosenLabelDevice.getDeviceState();
    }

    public DataOrders getCurrentDataOrder() {
        return currentDataOrder;
    }

    public PanelInterfaceInfo getPanelInterfaceInfo() {
        return panelInterfaceInfo;
    }

    public PanelMonitoringDevice getPanelMonitoringDevice() {
        return panelMonitoringDevice;
    }

    public PanelDeviceInfo getPanelDeviceInfo() {
        return panelDeviceInfo;
    }

    public PanelDeviceResources getPanelDeviceResources() {
        return panelDeviceResources;
    }

    public PanelDeviceSummary getPanelDeviceSummary() {
        return panelDeviceSummary;
    }

    public PanelBasicTopology getPanelTopology() {
        return panelTopology;
    }

//    @Override
//    public void setEnabled(boolean enabled) {
//        super.setEnabled(enabled);
//
//        if (!enabled) {
//            SnmpManager.getInstance().getQueryTimerManager().cancelDeviceActiveTimer();
//            SnmpManager.getInstance().getQueryTimerManager().cancelInterfaceTimer();
//            SnmpManager.getInstance().getQueryTimerManager().cancelDeviceResourceTimer();
//        }
//    }
//
    public class LabelDevice extends JLabel {

        private int deviceId;
        private DeviceStates deviceState;

        public LabelDevice(String text, int deviceId, DeviceStates deviceState) {
            super(text);

            this.deviceId = deviceId;
            this.deviceState = deviceState;

            this.setPreferredSize(new Dimension(400, 60));
            this.setMinimumSize(new Dimension(400, 60));
            this.setMaximumSize(new Dimension(400, 60));

            this.setFont(new java.awt.Font("SansSerif", 1, 16));
            this.setIconTextGap(10);

            ImageIcon iconImage;
            if (deviceState == DeviceStates.DEACTIVE) {
                iconImage = new ImageIcon(getClass().getResource(ICON_DEVICE_DEACTIVE_PATH));
            } else {
                iconImage = new ImageIcon(getClass().getResource(ICON_DEVICE_ACTIVE_PATH));
            }
            this.setIcon(iconImage);

            this.setOpaque(true);
            this.setBackground(Color.white);
            this.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(60, 60, 60)));

            this.setVisible(true);
            this.setEnabled(true);
        }

        public int getDeviceId() {
            return deviceId;
        }

        public DeviceStates getDeviceState() {
            return deviceState;
        }

        public boolean setDeviceState(DeviceStates deviceState) {
            boolean result = false;

            if (this.deviceState != deviceState) {
                ImageIcon newIcon = null;
                if (deviceState == DeviceStates.ACTIVE) {
                    newIcon = new ImageIcon(getClass().getResource(ICON_DEVICE_ACTIVE_PATH));
                } else {
                    newIcon = new ImageIcon(getClass().getResource(ICON_DEVICE_DEACTIVE_PATH));
                }

                this.setIcon(newIcon);
                result = true;

                this.revalidate();
                this.repaint();
            }

            this.deviceState = deviceState;
            return result;
        }

        public void switchBackground(int action) {
            if (action == LABEL_HOVER || action == LABEL_CLICK) {
                this.setBackground(new Color(194, 217, 255));
            } else {
                this.setBackground(Color.WHITE);
            }
        }

    }

    public class LabelInterface extends JPanel {

        private int deviceId;
        private int interfaceId; //idx of table network interface
        private InterfaceStates interfaceState;
        private JLabel label;

        public LabelInterface(String text, int deviceId, int interfaceId, InterfaceStates interfaceState) {
            this.label = new JLabel();
            this.label.setText(text);

            this.deviceId = deviceId;
            this.interfaceId = interfaceId;
            this.interfaceState = interfaceState;

            this.setPreferredSize(new Dimension(400, 40));
            this.setMinimumSize(new Dimension(400, 40));
            this.setMaximumSize(new Dimension(400, 40));

            this.label.setFont(new java.awt.Font("SansSerif", 1, 16));
            this.label.setIconTextGap(10);

            ImageIcon iconImage;
            if (interfaceState == InterfaceStates.DOWN) {
                iconImage = new ImageIcon(getClass().getResource(ICON_INTERFACE_DEACTIVE_PATH));
            } else {
                iconImage = new ImageIcon(getClass().getResource(ICON_INTERFACE_ACTIVE_PATH));
            }
            this.label.setIcon(iconImage);
//            this.label.setEnabled(false);
            this.label.setFocusable(false);

            this.label.setOpaque(true);
            this.label.setBackground(Color.white);
            this.label.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 0, new java.awt.Color(60, 60, 60)));

            this.setBackground(new Color(204, 226, 255));
            this.setLayout(new AbsoluteLayout());
            this.add(this.label, new AbsoluteConstraints(100, 0, 300, 40));

            this.setVisible(true);
            this.setEnabled(true);
        }

        public int getDeviceId() {
            return deviceId;
        }

        public InterfaceStates getDeviceState() {
            return interfaceState;
        }

        public int getInterfaceId() {
            return interfaceId;
        }

        public void setDeviceId(int deviceId) {
            this.deviceId = deviceId;
        }

        public void setInterfaceId(int interfaceId) {
            this.interfaceId = interfaceId;
        }

        public void setInterfaceState(InterfaceStates interfaceState) {
            if (this.interfaceState != interfaceState) {
                ImageIcon newIcon = null;
                if (interfaceState == InterfaceStates.UP) {
                    newIcon = new ImageIcon(getClass().getResource(ICON_INTERFACE_ACTIVE_PATH));
                } else {
                    newIcon = new ImageIcon(getClass().getResource(ICON_INTERFACE_DEACTIVE_PATH));
                }

                this.label.setIcon(newIcon);
                this.revalidate();
                this.repaint();
            }
            this.interfaceState = interfaceState;
        }

        public void setText(String text) {
            this.label.setText(text);
        }

        public void switchBackground(int action) {
            if (action == LABEL_HOVER || action == LABEL_CLICK) {
                this.label.setBackground(new Color(194, 217, 255));
            } else {
                this.label.setBackground(Color.WHITE);
            }
        }

    }

}
