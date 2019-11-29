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
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import org.netbeans.lib.awtextra.AbsoluteConstraints;

/**
 *
 * @author cloud
 */
public class PanelImportedDevices extends JPanel {

    private JButton buttonImport;
    private JLabel labelSearch;
    private JPanel panelDeviceList;
    private JPanel panelDevices;
    private JScrollPane scrollpane1;
    private JTextField tfieldSearch;

    private JPanel currentDisplayedPanel;
    private PanelDeviceInfo panelDeviceInfo;

    private List<LabelDevice> labelDevices;
    private List<LabelInterface> labelInterfaces;

    private ActionListener listenerButton;
    private KeyAdapter listenerField;
    private MouseAdapter listenerListDevices;

    private final String ICON_ACTIVE_PATH = "/resources/icon_active_30.png";
    private final String ICON_DEACTIVE_PATH = "/resources/icon_deactive_30.png";

    //some action constants
    private final int LABEL_HOVER = 1;
    private final int LABEL_CLICK = 2;

    private LabelDevice currentChosenLabelDevice;
    private DataOrders currentDataOrder;

    public enum PANELS {
        PANEL_DEVICE_INFO
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
        labelSearch = new JLabel();
        buttonImport = new JButton();

        setPreferredSize(new java.awt.Dimension(1600, 940));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        setBackground(Color.white);

        panelDevices.setBackground(new java.awt.Color(20, 51, 125));
        panelDevices.setPreferredSize(new java.awt.Dimension(280, 940));
        panelDevices.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tfieldSearch.setFont(new java.awt.Font("SansSerif", 0, 15)); // NOI18N
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
        labelSearch.setIcon(new ImageIcon(getClass().getResource("/resources/icon_search_40.png"))); // NOI18N
        panelDevices.add(labelSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 40, 50, 30));

        buttonImport.setBackground(new java.awt.Color(38, 56, 163));
        buttonImport.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        buttonImport.setForeground(java.awt.Color.white);
        buttonImport.setIcon(new ImageIcon(getClass().getResource("/resources/icon_plus_40.png"))); // NOI18N
        buttonImport.setText("Import");

        buttonImport.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        buttonImport.setBorderPainted(false);
        panelDevices.add(buttonImport, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 830, 120, 40));

        add(panelDevices, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 440, -1));

        initChildPanels();
    }

    public void initChildPanels() {
        this.panelDeviceInfo = new PanelDeviceInfo();
        this.panelDeviceInfo.setVisible(false);
        this.panelDeviceInfo.setEnabled(false);
    }

    private void initListeners() {
        this.listenerButton = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    DeviceManagementController deviceController = new DeviceManagementController();
                    File file = fileChooser.getSelectedFile();

                    if (!deviceController.processImportingDevicesFromFile(file)) {
                        JOptionPane.showMessageDialog(null, deviceController.getResultMessage());
                    }
                }
            }

        };
        this.buttonImport.addActionListener(this.listenerButton);

        this.listenerField = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    DeviceManagementController deviceController = new DeviceManagementController();
                    List<String> data = deviceController.processSearchingDevices(currentDataOrder, tfieldSearch.getText());

                    updateDeviceList(deviceController.getDeviceIds(), data);
                    if (data != null) {
                        deviceController.processCheckingStateOfDevices(deviceController.getDeviceIds());
                    }
                } else {
                    super.keyReleased(e);
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
                if (currentChosenLabelDevice != null) {
                    currentChosenLabelDevice.switchBackground(-LABEL_CLICK);
                }

                LabelDevice source = (LabelDevice) e.getSource();
                if (!panelDeviceInfo.isVisible()) {
                    switchDisplayedPanel(PANELS.PANEL_DEVICE_INFO);
                }
                panelDeviceInfo.initData(source.getDeviceId());
                
                InterfaceManagementController interfaceController = new InterfaceManagementController();
                interfaceController.processGettingInterfacesOfDevice(source.getDeviceId());
                
                currentChosenLabelDevice = source;
                currentChosenLabelDevice.switchBackground(LABEL_CLICK);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                LabelDevice source = (LabelDevice) e.getSource();
                if (source != currentChosenLabelDevice) {
                    source.switchBackground(-LABEL_HOVER);
                }
            }
        };
    }

    public void initOtherComponents() {
        this.labelDevices = new ArrayList<LabelDevice>();
        this.labelInterfaces = new ArrayList<LabelInterface>();
    }

    public void initData() {
        this.initDeviceList();
    }

    public void initDeviceList() {
        this.currentDataOrder = DataOrders.LABEL;

        DeviceManagementController deviceController = new DeviceManagementController();
        List<String> data = deviceController.processGettingImportedDevices(this.currentDataOrder);

        if (data == null) {
            JOptionPane.showMessageDialog(null, deviceController.getResultMessage());
            return;
        }

        this.updateDeviceList(deviceController.getDeviceIds(), data);
        if (data != null) {
            deviceController.processCheckingStateOfDevices(deviceController.getDeviceIds());
        }
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
            if (this.labelDevices.get(i).getDeviceId() == deviceId) {
                this.labelDevices.get(i).setDeviceState(deviceState);
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

    public synchronized void addLabelInterfaces(int deviceId, int[] interfaceIds, String[] names, InterfaceStates[] interfaceStates) {
        int tempSize = this.labelDevices.size();
        int listPosition = -1;
        
        for (int i = 0; i < tempSize; i++) {
            if (this.labelDevices.get(i).getDeviceId() == deviceId) {
                if (this.currentChosenLabelDevice != this.labelDevices.get(i)) {
                    return;
                }
                listPosition = i;
                break;
            }
        }

        if (listPosition == -1) {
            return;
        }

        for (int i = listPosition + 1; i < tempSize; i++) {
            this.panelDeviceList.remove(this.labelDevices.get(i));
        }

        this.labelInterfaces.clear();
        System.gc();

        for (int i = 0; i < interfaceIds.length; i++) {
            LabelInterface labelInterface = new LabelInterface(names[i], deviceId, interfaceIds[i], interfaceStates[i]);
            this.labelInterfaces.add(labelInterface);
            this.panelDeviceList.add(labelInterface);
        }

        for (int i = listPosition + 1; i < tempSize; i++) {
            this.panelDeviceList.add(this.labelDevices.get(i));
        }
        
        this.panelDeviceList.revalidate();
        this.panelDeviceList.repaint();
    }

    public void switchDisplayedPanel(PANELS panel) {
        this.hideDisplayedPanel();

        switch (panel) {
            case PANEL_DEVICE_INFO:
                this.displayPanel(panelDeviceInfo, 440, 30, -1, -1);
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
        }
    }

    private void displayPanel(JPanel panel, int x, int y, int width, int height) {
        panel.setEnabled(true);
        panel.setVisible(true);

        this.add(panel, new AbsoluteConstraints(x, y, width, height));
        this.currentDisplayedPanel = panel;
    }

    public void refreshPanel() {
        this.hideDisplayedPanel();
    }

    public DataOrders getCurrentDataOrder() {
        return currentDataOrder;
    }
    
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        
        if (!visible) {
            SnmpManager.getInstance().getQueryTimerManager().cancelDeviceTimer();
            SnmpManager.getInstance().getQueryTimerManager().cancelInterfaceTimer();
        }
    }

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
                iconImage = new ImageIcon(getClass().getResource(ICON_DEACTIVE_PATH));
            } else {
                iconImage = new ImageIcon(getClass().getResource(ICON_ACTIVE_PATH));
            }
            this.setIcon(iconImage);

            this.setOpaque(true);
            this.setBackground(Color.white);
            this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(60, 60, 60)));

            this.setVisible(true);
            this.setEnabled(true);
        }

        public int getDeviceId() {
            return deviceId;
        }

        public DeviceStates getDeviceState() {
            return deviceState;
        }

        public void setDeviceState(DeviceStates deviceState) {
            if (this.deviceState != deviceState) {
                ImageIcon newIcon = null;
                if (deviceState == DeviceStates.ACTIVE) {
                    newIcon = new ImageIcon(getClass().getResource(ICON_ACTIVE_PATH));
                } else {
                    newIcon = new ImageIcon(getClass().getResource(ICON_DEACTIVE_PATH));
                }

                this.setIcon(newIcon);
                this.revalidate();
                this.repaint();
            }
            this.deviceState = deviceState;
        }

        public void switchBackground(int action) {
            if (action == LABEL_HOVER || action == LABEL_CLICK) {
                this.setBackground(new Color(194, 217, 255));
            } else {
                this.setBackground(Color.WHITE);
            }
        }

    }

    public class LabelInterface extends JLabel {

        private int deviceId;
        private int interfaceId; //idx of table network interface
        private InterfaceStates interfaceState;

        public LabelInterface(String text, int deviceId, int interfaceId, InterfaceStates interfaceState) {
            super(text);

            this.deviceId = deviceId;
            this.interfaceId = interfaceId;
            this.interfaceState = interfaceState;

            this.setPreferredSize(new Dimension(400, 60));
            this.setMinimumSize(new Dimension(400, 60));
            this.setMaximumSize(new Dimension(400, 60));

            this.setFont(new java.awt.Font("SansSerif", 1, 16));
            this.setIconTextGap(50);

            ImageIcon iconImage;
            if (interfaceState == InterfaceStates.DOWN) {
                iconImage = new ImageIcon(getClass().getResource(ICON_DEACTIVE_PATH));
            } else {
                iconImage = new ImageIcon(getClass().getResource(ICON_ACTIVE_PATH));
            }
            this.setIcon(iconImage);

            this.setOpaque(true);
            this.setBackground(Color.white);
            this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(60, 60, 60)));

            this.setVisible(true);
            this.setEnabled(true);
        }

        public int getDeviceId() {
            return deviceId;
        }

        public InterfaceStates getDeviceState() {
            return interfaceState;
        }

        public void setDeviceState(InterfaceStates interfaceState) {
            if (this.interfaceState != interfaceState) {
                ImageIcon newIcon = null;
                if (interfaceState == InterfaceStates.UP) {
                    newIcon = new ImageIcon(getClass().getResource(ICON_ACTIVE_PATH));
                } else {
                    newIcon = new ImageIcon(getClass().getResource(ICON_DEACTIVE_PATH));
                }

                this.setIcon(newIcon);
                this.revalidate();
                this.repaint();
            }
            this.interfaceState = interfaceState;
        }

        public void switchBackground(int action) {
            if (action == LABEL_HOVER || action == LABEL_CLICK) {
                this.setBackground(new Color(194, 217, 255));
            } else {
                this.setBackground(Color.WHITE);
            }
        }

    }

}
