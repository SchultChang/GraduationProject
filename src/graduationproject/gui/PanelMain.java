/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.gui;

import graduationproject.controllers.UserManagementController;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
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
    private JLabel labelIconAccount;
    private JLabel labelIconSettings;
    private JLabel labelImportedDevices;
    private JLabel labelProfile;
    private JLabel labelLogout;
    private JLabel labelScannedDevices;
    private JLabel labelSingularTemplates;
    private JLabel labelTabularTemplates;
    private JPanel panelAccountMenu;
    private JPanel panelDeviceMenu;
    private JPanel panelTemplateMenu;

    private JPanel panelOptionDevices;
    private JPanel panelOptionTemplates;
    private JPanel panelOptionTraps;
    private JPanel panelOptionsBar;
    private JSeparator separator1;
    private JSeparator separator2;
    private JSeparator separator3;

    private JPanel currentDisplayedPanel;
    private PanelUserProfile panelUserProfile;
    private PanelImportedDevices panelImportedDevices;
    
    private MouseAdapter listenerPanel;
    private MouseAdapter listenerLabel;

    public enum PANELS {
        PANEL_USER_PROFILE,
        PANEL_IMPORTED_DEVICES;
    }

    public PanelMain() {
        initComponents();
        initListeners();
        initChildPanels();
    }

    private void initComponents() {
        panelDeviceMenu = new JPanel();
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
        panelOptionTraps = new JPanel();
        label5 = new JLabel();
        label6 = new JLabel();
        panelOptionDevices = new JPanel();
        label1 = new JLabel();
        label2 = new JLabel();
        panelOptionTemplates = new JPanel();
        label3 = new JLabel();
        label4 = new JLabel();
        labelIconAccount = new JLabel();
        labelIconSettings = new JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        setBackground(java.awt.Color.white);
        setPreferredSize(new java.awt.Dimension(1600, 1000));

        panelDeviceMenu.setBackground(new java.awt.Color(39, 87, 159));
        panelDeviceMenu.setBorder(null);
        panelDeviceMenu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelImportedDevices.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        labelImportedDevices.setForeground(java.awt.Color.white);
        labelImportedDevices.setHorizontalAlignment(SwingConstants.CENTER);
        labelImportedDevices.setText("Imported Devices");
        labelImportedDevices.setBorder(null);
        panelDeviceMenu.add(labelImportedDevices, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 198, 49));

        labelScannedDevices.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        labelScannedDevices.setForeground(java.awt.Color.white);
        labelScannedDevices.setHorizontalAlignment(SwingConstants.CENTER);
        labelScannedDevices.setText("Scanned Devices");
        labelScannedDevices.setBorder(null);
        panelDeviceMenu.add(labelScannedDevices, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 198, 44));
        panelDeviceMenu.add(separator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 200, 10));

        add(panelDeviceMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 200, 110));
        panelDeviceMenu.setVisible(false);
        panelDeviceMenu.setEnabled(false);

        panelTemplateMenu.setBackground(new java.awt.Color(39, 87, 159));
        panelTemplateMenu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelTabularTemplates.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        labelTabularTemplates.setForeground(java.awt.Color.white);
        labelTabularTemplates.setHorizontalAlignment(SwingConstants.CENTER);
        labelTabularTemplates.setText("Tabular Templates");
        labelTabularTemplates.setBorder(null);
        panelTemplateMenu.add(labelTabularTemplates, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 198, 44));

        labelSingularTemplates.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        labelSingularTemplates.setForeground(java.awt.Color.white);
        labelSingularTemplates.setHorizontalAlignment(SwingConstants.CENTER);
        labelSingularTemplates.setText("Singular Templates");
        labelSingularTemplates.setBorder(null);
        panelTemplateMenu.add(labelSingularTemplates, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 0, 198, 49));
        panelTemplateMenu.add(separator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 200, 10));

        add(panelTemplateMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 60, 200, 110));
        panelTemplateMenu.setVisible(false);
        panelTemplateMenu.setEnabled(false);

        panelAccountMenu.setBackground(new java.awt.Color(39, 87, 159));
        panelAccountMenu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelLogout.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        labelLogout.setForeground(java.awt.Color.white);
        labelLogout.setHorizontalAlignment(SwingConstants.CENTER);
        labelLogout.setText("Logout");
        labelLogout.setBorder(null);
        panelAccountMenu.add(labelLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 198, 44));

        labelProfile.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        labelProfile.setForeground(java.awt.Color.white);
        labelProfile.setHorizontalAlignment(SwingConstants.CENTER);
        labelProfile.setText("Profile");
        labelProfile.setBorder(null);
        panelAccountMenu.add(labelProfile, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 0, 198, 49));
        panelAccountMenu.add(separator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 200, 10));

        panelAccountMenu.setPreferredSize(new Dimension(200, 110));
        add(panelAccountMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(1400, 60, 200, 110));
        panelAccountMenu.setVisible(false);
        panelAccountMenu.setEnabled(false);

        panelOptionsBar.setBackground(new java.awt.Color(20, 62, 125));
        panelOptionsBar.setForeground(new java.awt.Color(34, 52, 143));
        panelOptionsBar.setPreferredSize(new java.awt.Dimension(1600, 60));
        panelOptionsBar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelOptionTraps.setBackground(java.awt.Color.red);
        panelOptionTraps.setOpaque(false);
        panelOptionTraps.setPreferredSize(new java.awt.Dimension(200, 60));
        panelOptionTraps.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label5.setHorizontalAlignment(SwingConstants.CENTER);
        label5.setIcon(new ImageIcon(getClass().getResource("/resources/icon_traps_40.png"))); // NOI18N
        label5.setFocusable(false);
        panelOptionTraps.add(label5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 70, 60));

        label6.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        label6.setForeground(java.awt.Color.white);
        label6.setText("Traps");
        label6.setFocusable(false);
        panelOptionTraps.add(label6, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 0, 130, 60));

        panelOptionsBar.add(panelOptionTraps, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 0, -1, -1));

        panelOptionDevices.setBackground(java.awt.Color.red);
        panelOptionDevices.setFocusable(false);
        panelOptionDevices.setOpaque(false);
        panelOptionDevices.setPreferredSize(new java.awt.Dimension(200, 60));
        panelOptionDevices.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label1.setHorizontalAlignment(SwingConstants.CENTER);
        label1.setIcon(new ImageIcon(getClass().getResource("/resources/icon_network_40.png"))); // NOI18N
        label1.setFocusable(false);
        panelOptionDevices.add(label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 70, 60));

        label2.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        label2.setForeground(java.awt.Color.white);
        label2.setText("Devices");
        label2.setFocusable(false);
        panelOptionDevices.add(label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 0, 130, 60));

        panelOptionsBar.add(panelOptionDevices, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelOptionTemplates.setBackground(java.awt.Color.red);
        panelOptionTemplates.setOpaque(false);
        panelOptionTemplates.setPreferredSize(new java.awt.Dimension(200, 60));
        panelOptionTemplates.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label3.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        label3.setForeground(java.awt.Color.white);
        label3.setText("Templates");
        label3.setFocusable(false);
        panelOptionTemplates.add(label3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 0, 130, 60));

        label4.setHorizontalAlignment(SwingConstants.CENTER);
        label4.setIcon(new ImageIcon(getClass().getResource("/resources/icon_template2_40.png"))); // NOI18N
        label4.setFocusable(false);
        panelOptionTemplates.add(label4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 70, 60));

        panelOptionsBar.add(panelOptionTemplates, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 0, -1, -1));

        labelIconAccount.setHorizontalAlignment(SwingConstants.CENTER);
        labelIconAccount.setIcon(new ImageIcon(getClass().getResource("/resources/icon_account_40.png"))); // NOI18N
        labelIconAccount.setPreferredSize(new Dimension(60, 60));
        panelOptionsBar.add(labelIconAccount, new org.netbeans.lib.awtextra.AbsoluteConstraints(1540, 0, 60, 60));

        labelIconSettings.setHorizontalAlignment(SwingConstants.CENTER);
        labelIconSettings.setIcon(new ImageIcon(getClass().getResource("/resources/icon_settings_40.png"))); // NOI18N
        labelIconSettings.setPreferredSize(new Dimension(60, 60));
        panelOptionsBar.add(labelIconSettings, new org.netbeans.lib.awtextra.AbsoluteConstraints(1480, 0, 60, 60));

        add(panelOptionsBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

    }

    private void initListeners() {
        this.listenerLabel = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                JLabel source = (JLabel) e.getSource();
                if (source == labelIconAccount) {
                    showMenu(panelAccountMenu);
                }
                if (source == labelIconSettings) {
                }

            }
        };

        this.labelIconAccount.addMouseListener(this.listenerLabel);
        this.labelIconSettings.addMouseListener(this.listenerLabel);

        this.listenerPanel = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                JPanel source = (JPanel) e.getSource();
                if (source == panelOptionDevices) {
                    showMenu(panelDeviceMenu);
                }
                if (source == panelOptionTemplates) {
                    showMenu(panelTemplateMenu);
                }
                if (source == panelAccountMenu) {
                    processMouseOnPanelAccountMenu(e.getX(), e.getY());
                }
                if (source == panelDeviceMenu) {
                    processMouseOnPanelDeviceMenu(e.getX(), e.getY());
                }                
            }

            @Override
            public void mouseExited(MouseEvent e) {
                JPanel source = (JPanel) e.getSource();
                if (panelDeviceMenu.isVisible() && source == panelDeviceMenu) {
                    hideMenu(panelDeviceMenu);
                }
                if (panelTemplateMenu.isVisible() && source == panelTemplateMenu) {
                    hideMenu(panelTemplateMenu);
                }
                if (panelAccountMenu.isVisible() && source == panelAccountMenu) {
                    hideMenu(panelAccountMenu);
                }
            }
        };

        this.panelOptionDevices.addMouseListener(this.listenerPanel);
        this.panelOptionTemplates.addMouseListener(this.listenerPanel);
        this.panelDeviceMenu.addMouseListener(this.listenerPanel);
        this.panelTemplateMenu.addMouseListener(this.listenerPanel);
        this.panelAccountMenu.addMouseListener(this.listenerPanel);
    }
    
    private void processMouseOnPanelDeviceMenu(int x, int y) {
        JLabel[] labels = {this.labelImportedDevices, this.labelScannedDevices};
        int temp = this.getPressedLabelId(labels, x, y);
        
        if (temp < labels.length) {
            if (labels[temp] == this.labelImportedDevices) {
                this.switchDisplayedPanel(PANELS.PANEL_IMPORTED_DEVICES);
            }
        }
    }

    private void processMouseOnPanelAccountMenu(int x, int y) {
        JLabel[] labels = {this.labelProfile, this.labelLogout};
        int temp = this.getPressedLabelId(labels, x, y);
        
        if (temp < labels.length) {
            if (labels[temp] == this.labelProfile) {
                this.switchDisplayedPanel(PANELS.PANEL_USER_PROFILE);
            }
            if (labels[temp] == this.labelLogout) {
                ApplicationWindow.getInstance().switchPanel(ApplicationWindow.PANELS.PANEL_INITIAL);
                new UserManagementController().processLogout();
            }
        }
    }

    private int getPressedLabelId(JLabel[] labels, int x, int y) {
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
    }

    private void initChildPanels() {
        this.panelUserProfile = new PanelUserProfile();
        this.panelUserProfile.setVisible(false);
        this.panelUserProfile.setEnabled(false);
        
        this.panelImportedDevices = new PanelImportedDevices();
        this.panelImportedDevices.setVisible(false);
        this.panelImportedDevices.setEnabled(false);
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
                this.panelUserProfile.initData();
                break;
                
            case PANEL_IMPORTED_DEVICES:
                this.displayPanel(this.panelImportedDevices, 0, 60, -1, -1);
                this.panelImportedDevices.refreshPanel();
                this.panelImportedDevices.initData();
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
        
    public void refreshPanel() {
        if (this.currentDisplayedPanel != null) {
            this.remove(this.currentDisplayedPanel);
            this.currentDisplayedPanel.setVisible(false);
            this.currentDisplayedPanel.setEnabled(false);
            this.currentDisplayedPanel = null;
        }
        
        this.hideMenu(this.panelAccountMenu);
        this.hideMenu(this.panelDeviceMenu);
        this.hideMenu(this.panelTemplateMenu);
    }
}
