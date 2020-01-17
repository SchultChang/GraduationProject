/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.gui;

import graduationproject.controllers.DeviceManagementController;
import graduationproject.controllers.DeviceResourceManagementController;
import graduationproject.controllers.InterfaceManagementController;
import graduationproject.controllers.UserManagementController;
import graduationproject.helpers.SSHClient;
import graduationproject.snmpd.SnmpManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

/**
 *
 * @author cloud
 */
public class PanelMain extends JPanel {

    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JLabel label6;
    private JLabel label7;
    private JLabel labelCloseBoard;
    private JLabel labelIconAccount;
    private JLabel labelIconSettings;
    private JLabel labelImportedDevices;
    private JLabel labelProfile;
    private JLabel labelLogout;
    private JLabel labelScannedDevices;
    private JLabel labelSingularTemplates;
    private JLabel labelTabularTemplates;
    private JPanel panelAccountMenu;
//    private JPanel panelDeviceMenu;
    private JPanel panelTemplateMenu;
    private JPanel panelNotificationBoard;
    private JPanel panelNotifications;
    private JScrollPane scrollpane1;

    private JPanel panelOptionDevices;
    private JPanel panelOptionTemplates;
    private JPanel panelOptionNotifications;
    private JPanel panelOptionsBar;
    private JSeparator separator1;
    private JSeparator separator2;
    private JSeparator separator3;

    private JPanel currentDisplayedPanel;
    private PanelNotificationInfo panelNotificationInfo;
    private PanelUserProfile panelUserProfile;
    private PanelImportedDevices panelImportedDevices;
    private PanelImportedTemplates panelImportedTemplates;
    private PanelNotificationList panelNotificationList;

    private MouseAdapter listenerPanel;
    private MouseAdapter listenerPanelMotion;
    private MouseAdapter listenerLabel;

    private final Color HOVER_COLOR = new Color(134, 189, 240);

    private final int NOTIFICATION_BOARD_LIVE_TIME = 500000; //millisecond
    private Timer timerNotificationBoard;
    private List<PanelNotification> listNotifications;

    public enum PANELS {
        PANEL_USER_PROFILE,
        PANEL_IMPORTED_DEVICES,
        PANEL_IMPORTED_TEMPLATES,
        PANEL_NOTIFICATION_INFO,
        PANEL_NOTIFICATION_LIST
    }

    public PanelMain() {
        initComponents();
        initListeners();
        initChildPanels();
        initOtherComponents();
    }

    private void initComponents() {
//        panelDeviceMenu = new JPanel();
        labelImportedDevices = new JLabel();
        labelScannedDevices = new JLabel();
        separator1 = new JSeparator();
        panelTemplateMenu = new JPanel();
        labelTabularTemplates = new JLabel();
        labelSingularTemplates = new JLabel();
        separator2 = new JSeparator();
        panelAccountMenu = new JPanel();
        labelLogout = new JLabel();
        labelProfile = new JLabel();
        separator3 = new JSeparator();
        panelOptionsBar = new JPanel();
        panelOptionNotifications = new JPanel();
        label5 = new JLabel();
        label6 = new JLabel();
        label7 = new JLabel();
        panelOptionDevices = new JPanel();
        label1 = new JLabel();
        label2 = new JLabel();
        panelOptionTemplates = new JPanel();
        label3 = new JLabel();
        label4 = new JLabel();
        labelIconAccount = new JLabel();
        labelIconSettings = new JLabel();
        panelNotificationBoard = new JPanel();
        labelCloseBoard = new JLabel();
        scrollpane1 = new JScrollPane();
        panelNotifications = new JPanel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        setBackground(java.awt.Color.white);
        setPreferredSize(new java.awt.Dimension(1600, 1000));

//        panelDeviceMenu.setBackground(new java.awt.Color(39, 87, 159));
//        panelDeviceMenu.setBorder(null);
//        panelDeviceMenu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
//
//        labelImportedDevices.setFont(new java.awt.Font("SansSerif", 1, 15));
//        labelImportedDevices.setForeground(java.awt.Color.white);
//        labelImportedDevices.setBackground(HOVER_COLOR);
//        labelImportedDevices.setHorizontalAlignment(SwingConstants.CENTER);
//        labelImportedDevices.setText("Imported Devices");
//        labelImportedDevices.setBorder(null);
//        panelDeviceMenu.add(labelImportedDevices, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 50));
//
//        labelScannedDevices.setFont(new java.awt.Font("SansSerif", 1, 15));
//        labelScannedDevices.setForeground(java.awt.Color.white);
//        labelScannedDevices.setBackground(HOVER_COLOR);
//        labelScannedDevices.setHorizontalAlignment(SwingConstants.CENTER);
//        labelScannedDevices.setText("Scanned Devices");
//        labelScannedDevices.setBorder(null);
//        panelDeviceMenu.add(labelScannedDevices, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 200, 50));
//        panelDeviceMenu.add(separator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 54, 200, 2));
//
//        add(panelDeviceMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 200, 110));
//        panelDeviceMenu.setVisible(false);
//        panelDeviceMenu.setEnabled(false);
        panelTemplateMenu.setBackground(new java.awt.Color(39, 87, 159));
        panelTemplateMenu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelTabularTemplates.setFont(new java.awt.Font("SansSerif", 1, 15));
        labelTabularTemplates.setForeground(java.awt.Color.white);
        labelTabularTemplates.setBackground(HOVER_COLOR);
        labelTabularTemplates.setHorizontalAlignment(SwingConstants.CENTER);
        labelTabularTemplates.setText("Tabular Templates");
        labelTabularTemplates.setBorder(null);
        panelTemplateMenu.add(labelTabularTemplates, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 200, 50));

        labelSingularTemplates.setFont(new java.awt.Font("SansSerif", 1, 15));
        labelSingularTemplates.setForeground(java.awt.Color.white);
        labelSingularTemplates.setBackground(HOVER_COLOR);
        labelSingularTemplates.setHorizontalAlignment(SwingConstants.CENTER);
        labelSingularTemplates.setText("Singular Templates");
        labelSingularTemplates.setBorder(null);
        panelTemplateMenu.add(labelSingularTemplates, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 0, 200, 50));
        panelTemplateMenu.add(separator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 54, 200, 2));

        add(panelTemplateMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 60, 200, 110));
        panelTemplateMenu.setVisible(false);
        panelTemplateMenu.setEnabled(false);

        panelAccountMenu.setBackground(new java.awt.Color(39, 87, 159));
        panelAccountMenu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelLogout.setFont(new java.awt.Font("SansSerif", 1, 15));
        labelLogout.setForeground(java.awt.Color.white);
        labelLogout.setBackground(HOVER_COLOR);
        labelLogout.setHorizontalAlignment(SwingConstants.CENTER);
        labelLogout.setText("Logout");
        labelLogout.setBorder(null);
        panelAccountMenu.add(labelLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 200, 50));

        labelProfile.setFont(new java.awt.Font("SansSerif", 1, 15));
        labelProfile.setForeground(java.awt.Color.white);
        labelProfile.setBackground(HOVER_COLOR);
        labelProfile.setHorizontalAlignment(SwingConstants.CENTER);
        labelProfile.setText("Profile");
        labelProfile.setBorder(null);
        panelAccountMenu.add(labelProfile, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 0, 200, 50));
        panelAccountMenu.add(separator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 54, 200, 2));

        panelAccountMenu.setPreferredSize(new Dimension(200, 110));
        add(panelAccountMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(1400, 60, 200, 110));
        panelAccountMenu.setVisible(false);
        panelAccountMenu.setEnabled(false);

        panelOptionsBar.setBackground(new java.awt.Color(20, 62, 125));
        panelOptionsBar.setForeground(new java.awt.Color(34, 52, 143));
        panelOptionsBar.setPreferredSize(new java.awt.Dimension(1600, 60));
        panelOptionsBar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelOptionNotifications.setBackground(HOVER_COLOR);
        panelOptionNotifications.setOpaque(false);
        panelOptionNotifications.setPreferredSize(new java.awt.Dimension(200, 60));
        panelOptionNotifications.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label5.setHorizontalAlignment(SwingConstants.CENTER);
        label5.setIcon(new ImageIcon(getClass().getResource("/resources/icon_traps_40.png")));
        label5.setFocusable(false);
        panelOptionNotifications.add(label5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 70, 60));

        label6.setFont(new java.awt.Font("SansSerif", 1, 18));
        label6.setForeground(java.awt.Color.white);
        label6.setText("Notifications");
        label6.setFocusable(false);
        panelOptionNotifications.add(label6, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 0, 130, 60));

        label7.setBackground(java.awt.Color.red);
        label7.setFont(new java.awt.Font("SansSerif", 1, 15));
        label7.setForeground(java.awt.Color.red);
        label7.setText("News");
        panelOptionNotifications.add(label7, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 40, -1, -1));
        label7.setVisible(false);

        panelOptionsBar.add(panelOptionNotifications, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 0, -1, -1));

        panelOptionDevices.setBackground(HOVER_COLOR);
        panelOptionDevices.setFocusable(false);
        panelOptionDevices.setOpaque(false);
        panelOptionDevices.setPreferredSize(new java.awt.Dimension(200, 60));
        panelOptionDevices.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label1.setHorizontalAlignment(SwingConstants.CENTER);
        label1.setIcon(new ImageIcon(getClass().getResource("/resources/icon_network_40.png")));
        label1.setFocusable(false);
        panelOptionDevices.add(label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 70, 60));

        label2.setFont(new java.awt.Font("SansSerif", 1, 18));
        label2.setForeground(java.awt.Color.white);
        label2.setText("Devices");
        label2.setFocusable(false);
        panelOptionDevices.add(label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 0, 130, 60));

        panelOptionsBar.add(panelOptionDevices, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelOptionTemplates.setBackground(HOVER_COLOR);
        panelOptionTemplates.setOpaque(false);
        panelOptionTemplates.setPreferredSize(new java.awt.Dimension(200, 60));
        panelOptionTemplates.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label3.setFont(new java.awt.Font("SansSerif", 1, 18));
        label3.setForeground(java.awt.Color.white);
        label3.setText("Templates");
        label3.setFocusable(false);
        panelOptionTemplates.add(label3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 0, 130, 60));

        label4.setHorizontalAlignment(SwingConstants.CENTER);
        label4.setIcon(new ImageIcon(getClass().getResource("/resources/icon_template2_40.png")));
        label4.setFocusable(false);
        panelOptionTemplates.add(label4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 70, 60));

        panelOptionsBar.add(panelOptionTemplates, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 0, -1, -1));

        labelIconAccount.setBackground(HOVER_COLOR);
        labelIconAccount.setHorizontalAlignment(SwingConstants.CENTER);
        labelIconAccount.setIcon(new ImageIcon(getClass().getResource("/resources/icon_account_40.png")));
        labelIconAccount.setPreferredSize(new Dimension(60, 60));
        panelOptionsBar.add(labelIconAccount, new org.netbeans.lib.awtextra.AbsoluteConstraints(1540, 0, 60, 60));

        labelIconSettings.setBackground(HOVER_COLOR);
        labelIconSettings.setHorizontalAlignment(SwingConstants.CENTER);
        labelIconSettings.setIcon(new ImageIcon(getClass().getResource("/resources/icon_settings_40.png")));
        labelIconSettings.setPreferredSize(new Dimension(60, 60));
        panelOptionsBar.add(labelIconSettings, new org.netbeans.lib.awtextra.AbsoluteConstraints(1480, 0, 60, 60));

        add(panelOptionsBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelNotificationBoard.setOpaque(false);
        panelNotificationBoard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelCloseBoard.setBackground(java.awt.Color.white);
        labelCloseBoard.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelCloseBoard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icon_double_up_30.png")));
        labelCloseBoard.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 1, new java.awt.Color(0, 0, 0)));
        labelCloseBoard.setOpaque(true);
        panelNotificationBoard.add(labelCloseBoard, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 420, 400, 30));

        scrollpane1.setBackground(new Color(0, 0, 0, 100));
        scrollpane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollpane1.setOpaque(false);

        panelNotifications.setBackground(new Color(0, 0, 0, 100));
        panelNotifications.setOpaque(false);
        panelNotifications.setPreferredSize(new java.awt.Dimension(100, 1000));
        panelNotifications.setLayout(new javax.swing.BoxLayout(panelNotifications, javax.swing.BoxLayout.PAGE_AXIS));
        scrollpane1.setViewportView(panelNotifications);

        panelNotificationBoard.add(scrollpane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 400, 420));

        add(panelNotificationBoard, new org.netbeans.lib.awtextra.AbsoluteConstraints(1200, 60, 400, 450));
        hideMenu(panelNotificationBoard);
    }

    private void initListeners() {
        this.listenerLabel = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                JLabel source = (JLabel) e.getSource();
                if (source != labelCloseBoard) {
                    source.setOpaque(true);
                    source.repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                JLabel source = (JLabel) e.getSource();
                if (source == labelIconAccount) {
                    showMenu(panelAccountMenu);
                }
                if (source == labelIconSettings) {
                }
                if (source == labelCloseBoard) {
                    timerNotificationBoard.cancel();
                    timerNotificationBoard.purge();
                    hideMenu(panelNotificationBoard);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                JLabel source = (JLabel) e.getSource();
                if (source != labelCloseBoard) {
                    source.setOpaque(false);
                    source.repaint();
                }
            }
        };

        this.labelIconAccount.addMouseListener(this.listenerLabel);
        this.labelIconSettings.addMouseListener(this.listenerLabel);
        this.labelCloseBoard.addMouseListener(this.listenerLabel);

        this.listenerPanel = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                JPanel source = (JPanel) e.getSource();
                if (source == panelOptionDevices) {
                    panelOptionDevices.setOpaque(true);
                    if (panelTemplateMenu.isVisible()) {
                        hideMenu(panelTemplateMenu);
                    }
                }
                if (source == panelOptionTemplates) {
                    panelOptionTemplates.setOpaque(true);
//                    if (panelDeviceMenu.isVisible()) {
//                        hideMenu(panelDeviceMenu);
//                    }
                }
                if (source == panelOptionNotifications) {
                    panelOptionNotifications.setOpaque(true);
                    if (panelTemplateMenu.isVisible()) {
                        hideMenu(panelTemplateMenu);
                    }
                }
                PanelMain.this.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                System.gc();
                JPanel source = (JPanel) e.getSource();
                if (source == panelOptionDevices) {
//                    showMenu(panelDeviceMenu);
                    switchDisplayedPanel(PANELS.PANEL_IMPORTED_DEVICES);
                    return;
                }
                if (source == panelOptionTemplates) {
//                    showMenu(panelTemplateMenu);
                    switchDisplayedPanel(PANELS.PANEL_IMPORTED_TEMPLATES);
                    return;
                }
                if (source == panelOptionNotifications) {
                    switchDisplayedPanel(PANELS.PANEL_NOTIFICATION_LIST);
                }
                if (source == panelAccountMenu) {
                    processMousePressedOnPanelAccountMenu(e.getX(), e.getY());
                    return;
                }
//                if (source == panelDeviceMenu) {
//                    processMousePressedOnPanelDeviceMenu(e.getX(), e.getY());
//                    return;
//                }
//                if (source == panelTemplateMenu) {
//                    processMousePressedOnPanelTemplateMenu(e.getX(), e.getY());
//                    return;
//                }
//
                try {
                    PanelNotification notification = (PanelNotification) source;
                    showPanelNotificationInfo(notification.getNotificationId());
                    notification.setIsRead(true);
                } catch (Exception ex) {
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                JPanel source = (JPanel) e.getSource();
                if (source == panelOptionDevices) {
                    panelOptionDevices.setOpaque(false);
                }
                if (source == panelOptionTemplates) {
                    panelOptionTemplates.setOpaque(false);
                }
                if (source == panelOptionNotifications) {
                    panelOptionNotifications.setOpaque(false);
                }
                PanelMain.this.repaint();
//                if (panelDeviceMenu.isVisible() && source == panelDeviceMenu) {
//                    hideMenu(panelDeviceMenu);
//                    return;
//                }
                if (panelTemplateMenu.isVisible() && source == panelTemplateMenu) {
                    hideMenu(panelTemplateMenu);
                    return;
                }
                if (panelAccountMenu.isVisible() && source == panelAccountMenu) {
                    hideMenu(panelAccountMenu);
                    return;
                }
            }
        };

        this.panelOptionDevices.addMouseListener(this.listenerPanel);
        this.panelOptionTemplates.addMouseListener(this.listenerPanel);
        this.panelOptionNotifications.addMouseListener(this.listenerPanel);
//        this.panelDeviceMenu.addMouseListener(this.listenerPanel);
//        this.panelTemplateMenu.addMouseListener(this.listenerPanel);
        this.panelAccountMenu.addMouseListener(this.listenerPanel);

        this.listenerPanelMotion = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                JPanel source = (JPanel) e.getSource();
//                if (source == panelDeviceMenu) {
//                    processMouseMovedOnPanelDeviceMenu(e.getX(), e.getY());
//                }
//                if (source == panelTemplateMenu) {
//                    processMouseMovedOnPanelTemplateMenu(e.getX(), e.getY());
//                }
                if (source == panelAccountMenu) {
                    processMouseMovedOnPanelAccountMenu(e.getX(), e.getY());
                }
            }
        };

//        this.panelDeviceMenu.addMouseMotionListener(this.listenerPanelMotion);
//        this.panelTemplateMenu.addMouseMotionListener(this.listenerPanelMotion);
        this.panelAccountMenu.addMouseMotionListener(this.listenerPanelMotion);
    }

//    private void processMouseMovedOnPanelDeviceMenu(int x, int y) {
//        JLabel[] labels = {this.labelImportedDevices, this.labelScannedDevices};
//        int temp = this.getMouseOnLabelId(labels, x, y);
//
//        if (temp < labels.length) {
//            if (labels[temp] == this.labelImportedDevices) {
//                this.labelImportedDevices.setOpaque(true);
//                this.labelScannedDevices.setOpaque(false);
//            }
//            if (labels[temp] == this.labelScannedDevices) {
//                this.labelScannedDevices.setOpaque(true);
//                this.labelImportedDevices.setOpaque(false);
//            }
//        }
//        this.panelDeviceMenu.repaint();
//    }
    private void processMouseMovedOnPanelAccountMenu(int x, int y) {
        JLabel[] labels = {this.labelProfile, this.labelLogout};
        int temp = this.getMouseOnLabelId(labels, x, y);

        if (temp < labels.length) {
            if (labels[temp] == this.labelProfile) {
                this.labelProfile.setOpaque(true);
                this.labelLogout.setOpaque(false);
            }
            if (labels[temp] == this.labelLogout) {
                this.labelLogout.setOpaque(true);
                this.labelProfile.setOpaque(false);
            }
        }
        this.panelAccountMenu.repaint();
    }

//    private void processMouseMovedOnPanelTemplateMenu(int x, int y) {
//        JLabel[] labels = {this.labelSingularTemplates, this.labelTabularTemplates};
//        int temp = this.getMouseOnLabelId(labels, x, y);
//
//        if (temp < labels.length) {
//            if (labels[temp] == this.labelSingularTemplates) {
//                this.labelSingularTemplates.setOpaque(true);
//                this.labelTabularTemplates.setOpaque(false);
//            }
//            if (labels[temp] == this.labelTabularTemplates) {
//                this.labelTabularTemplates.setOpaque(true);
//                this.labelSingularTemplates.setOpaque(false);
//            }
//        }
//        this.panelTemplateMenu.repaint();
//    }

//    private void processMousePressedOnPanelDeviceMenu(int x, int y) {
//        JLabel[] labels = {this.labelImportedDevices, this.labelScannedDevices};
//        int temp = this.getMouseOnLabelId(labels, x, y);
//
//        if (temp < labels.length) {
//            if (labels[temp] == this.labelImportedDevices) {
//                this.switchDisplayedPanel(PANELS.PANEL_IMPORTED_DEVICES);
//            }
//        }
//    }
//    private void processMousePressedOnPanelTemplateMenu(int x, int y) {
//        JLabel[] labels = {this.labelSingularTemplates, this.labelTabularTemplates};
//        int temp = this.getMouseOnLabelId(labels, x, y);
//
//        if (temp < labels.length) {
//            if (labels[temp] == this.labelSingularTemplates) {
////                this.panelImportedTemplates.setIsSingular(true);
//                this.switchDisplayedPanel(PANELS.PANEL_IMPORTED_TEMPLATES);
//            }
//            if (labels[temp] == this.labelTabularTemplates) {
////                this.panelImportedTemplates.setIsSingular(false);
//                this.switchDisplayedPanel(PANELS.PANEL_IMPORTED_TEMPLATES);
//            }
//        }
//    }

    private void processMousePressedOnPanelAccountMenu(int x, int y) {
        JLabel[] labels = {this.labelProfile, this.labelLogout};
        int temp = this.getMouseOnLabelId(labels, x, y);

        if (temp < labels.length) {
            if (labels[temp] == this.labelProfile) {
                this.switchDisplayedPanel(PANELS.PANEL_USER_PROFILE);
            }
            if (labels[temp] == this.labelLogout) {
                new UserManagementController().processLogout();
                ApplicationWindow.getInstance().switchPanel(ApplicationWindow.PANELS.PANEL_INITIAL);
                System.exit(0);
            }
        }
    }

    private int getMouseOnLabelId(JLabel[] labels, int x, int y) {
        int temp = 0;
        for (temp = 0; temp < labels.length; temp++) {
            if (this.isMouseOnLabel(labels[temp], x, y)) {
                break;
            }
        }

        return temp;
    }

    private boolean isMouseOnLabel(JLabel label, int xMouse, int yMouse) {
        Dimension size = label.getSize();
        if (label.getX() <= xMouse && xMouse <= label.getX() + size.width) {
            if (label.getY() <= yMouse && yMouse <= label.getY() + size.height) {
                return true;
            }
        }
        return false;
    }

    private void showMenu(JPanel panelMenu) {
        panelMenu.setEnabled(true);
        panelMenu.setVisible(true);
    }

    private void hideMenu(JPanel panelMenu) {
        panelMenu.setVisible(false);
        panelMenu.setEnabled(false);

        if (panelMenu == this.panelAccountMenu) {
            this.labelProfile.setOpaque(false);
            this.labelLogout.setOpaque(false);
        } 
//        else             if (panelMenu == this.panelDeviceMenu) {
        //                this.labelImportedDevices.setOpaque(false);
        //                this.labelScannedDevices.setOpaque(false);
        //            }
//        {
//            if (panelMenu == this.panelTemplateMenu) {
//                this.labelSingularTemplates.setOpaque(false);
//                this.labelTabularTemplates.setOpaque(false);
//            }
//        }
    }

    private void initChildPanels() {
        this.panelNotificationInfo = new PanelNotificationInfo();
        this.add(this.panelNotificationInfo, new AbsoluteConstraints(0, 60, -1, -1));
        this.panelNotificationInfo.setVisible(false);
        this.panelNotificationInfo.setEnabled(false);

        this.panelUserProfile = new PanelUserProfile();
        this.panelUserProfile.setVisible(false);
        this.panelUserProfile.setEnabled(false);

        this.panelImportedDevices = new PanelImportedDevices();
        this.panelImportedDevices.setVisible(false);
        this.panelImportedDevices.setEnabled(false);

        this.panelImportedTemplates = new PanelImportedTemplates();
        this.panelImportedTemplates.setVisible(false);
        this.panelImportedTemplates.setEnabled(false);

        this.panelNotificationList = new PanelNotificationList();
        this.panelNotificationList.setVisible(false);
        this.panelNotificationList.setEnabled(false);
    }

    private void initOtherComponents() {
        this.listNotifications = new ArrayList<PanelNotification>();
    }

    public void switchDisplayedPanel(PANELS panel) {
        if (this.currentDisplayedPanel != null) {
            this.remove(this.currentDisplayedPanel);
            this.currentDisplayedPanel.setEnabled(false);
            this.currentDisplayedPanel.setVisible(false);
        }

        switch (panel) {
            case PANEL_USER_PROFILE:
                this.displayPanel(this.panelUserProfile, 0, 60, -1, -1);
                this.panelUserProfile.initViewData();
                break;

            case PANEL_IMPORTED_DEVICES:
                this.displayPanel(this.panelImportedDevices, 0, 60, -1, -1);
//                this.panelImportedDevices.refreshPanel();
                this.panelImportedDevices.initViewData();
                break;

            case PANEL_IMPORTED_TEMPLATES:
                this.displayPanel(this.panelImportedTemplates, 0, 60, -1, -1);
                this.panelImportedTemplates.initViewData(true);
                break;

            case PANEL_NOTIFICATION_LIST:
                this.displayPanel(this.panelNotificationList, 0, 60, -1, -1);
                this.panelNotificationList.initViewData();
                break;
        }

        this.revalidate();
        this.repaint();
    }

    private void displayPanel(JPanel panel, int x, int y, int width, int height) {
        panel.setEnabled(true);
        panel.setVisible(true);

        add(panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(x, y, width, height));
        this.currentDisplayedPanel = panel;
    }

    public PanelUserProfile getPanelUserProfile() {
        return panelUserProfile;
    }

    public PanelImportedDevices getPanelImportedDevices() {
        return panelImportedDevices;
    }

    public PanelImportedTemplates getPanelImportedTemplates() {
        return panelImportedTemplates;
    }

    public void refreshPanel() {
        if (this.currentDisplayedPanel != null) {
            this.remove(this.currentDisplayedPanel);
            this.currentDisplayedPanel.setVisible(false);
            this.currentDisplayedPanel.setEnabled(false);
            this.currentDisplayedPanel = null;
        }

        this.hideMenu(this.panelAccountMenu);
//        this.hideMenu(this.panelDeviceMenu);
        this.hideMenu(this.panelTemplateMenu);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        if (!enabled) {
            SnmpManager.getInstance().close();
            SSHClient.getInstance().close();
        } else {
            new DeviceManagementController().processCheckingStateOfDevices();
            new InterfaceManagementController().processGettingInterfacesOfActiveDevices();
            new DeviceResourceManagementController().processGettingResourcesOfActiveDevices();

        }
    }

    public synchronized void showNotification(int notificationId, String header, String content) {
        if (this.isVisible()) {
            if (!this.panelNotificationBoard.isVisible()) {
                int tempSize = this.listNotifications.size();
                for (int i = tempSize - 1; i >= 0; i--) {
                    if (this.listNotifications.get(i).isRead()) {
                        this.panelNotifications.remove(this.listNotifications.get(i));
                        this.listNotifications.remove(i);
                    }
                }
                showMenu(this.panelNotificationBoard);
                if (this.timerNotificationBoard != null) {
                    this.timerNotificationBoard.cancel();
                    this.timerNotificationBoard.purge();
                }
                TimerTask boardClosingTask = new TimerTask() {
                    @Override
                    public void run() {
                        if (panelNotificationBoard.isVisible()) {
                            hideMenu(panelNotificationBoard);
                        }
                    }

                };

                this.timerNotificationBoard = new Timer();
                this.timerNotificationBoard.schedule(boardClosingTask, this.NOTIFICATION_BOARD_LIVE_TIME);
                System.gc();
            }

            PanelNotification notification = new PanelNotification(notificationId, header, content);
            notification.addMouseListener(this.listenerPanel);
            this.listNotifications.add(notification);

            this.panelNotifications.removeAll();
            int tempSize = this.listNotifications.size();
            for (int i = tempSize - 1; i >= 0; i--) {
                this.panelNotifications.add(this.listNotifications.get(i));
            }

            this.revalidate();
            this.repaint();
        }
    }

    public void showPanelNotificationInfo(int notificationId) {
        if (!this.panelNotificationInfo.isVisible()) {
            this.panelNotificationInfo.setVisible(true);
            this.panelNotificationInfo.setEnabled(true);
        }

        this.panelNotificationInfo.initViewData(notificationId);

        this.revalidate();
        this.repaint();
    }

    public void hidePanelNotificationInfo() {
        this.panelNotificationInfo.setVisible(false);
        this.panelNotificationInfo.setEnabled(false);

        this.revalidate();
        this.repaint();
    }

    public class PanelNotification extends JPanel {

        private int notificationId;
        private JLabel labelHeader;
        private JLabel labelContent;
        private boolean isRead;

        public PanelNotification(int notificationId, String header, String content) {
            this.labelHeader = new JLabel(header);
            this.labelContent = new JLabel(content);

            setBackground(new Color(192, 215, 252));
            setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
            setMaximumSize(new java.awt.Dimension(400, 70));
            setMinimumSize(new java.awt.Dimension(400, 70));
            setPreferredSize(new java.awt.Dimension(400, 70));
            setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

            labelContent.setFont(new java.awt.Font("SansSerif", 1, 12));
            labelContent.setForeground(java.awt.Color.blue);
            labelContent.setFocusable(false);
            add(labelContent, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 360, -1));

            labelHeader.setFont(new java.awt.Font("SansSerif", 1, 15));
            labelHeader.setForeground(java.awt.Color.red);
            labelHeader.setFocusable(false);
            add(labelHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 360, 30));

            this.notificationId = notificationId;
            this.isRead = false;
        }

        public boolean isRead() {
            return this.isRead;
        }

        public int getNotificationId() {
            return notificationId;
        }

        public void setIsRead(boolean isRead) {
            this.isRead = isRead;
        }

    }
}
