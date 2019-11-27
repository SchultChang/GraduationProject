/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.gui;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;


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
    private JLabel labelLogout;
    private JLabel labelProfile;
    private JLabel labelScannedDevices;
    private JLabel labelSingularTemplates;
    private JLabel labelTabularTemplates;
    private JPanel panelAccountMenu;
    private JPanel panelDeviceMenu;
    private JPanel panelOptionDevices;
    private JPanel panelOptionTemplates;
    private JPanel panelOptionTraps;
    private JPanel panelOptionsBar;
    private JPanel panelTemplateMenu;
    private JSeparator separator1;
    private JSeparator separator2;
    private JSeparator separator3;

    public PanelMain() {
        initComponents();
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
        labelProfile = new JLabel();
        labelLogout = new JLabel();
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

        panelDeviceMenu.setBackground(new java.awt.Color(39, 87, 159));
        panelDeviceMenu.setBorder(null);
        panelDeviceMenu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelImportedDevices.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        labelImportedDevices.setForeground(java.awt.Color.white);
        labelImportedDevices.setHorizontalAlignment(SwingConstants.CENTER);
        labelImportedDevices.setText("Imported Devices");
        labelImportedDevices.setBorder(null);
        panelDeviceMenu.add(labelImportedDevices, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 0, 198, 49));

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

        labelProfile.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        labelProfile.setForeground(java.awt.Color.white);
        labelProfile.setHorizontalAlignment(SwingConstants.CENTER);
        labelProfile.setText("Logout");
        labelProfile.setBorder(null);
        panelAccountMenu.add(labelProfile, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 198, 44));

        labelLogout.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        labelLogout.setForeground(java.awt.Color.white);
        labelLogout.setHorizontalAlignment(SwingConstants.CENTER);
        labelLogout.setText("Profile");
        labelLogout.setBorder(null);
        panelAccountMenu.add(labelLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 0, 198, 49));
        panelAccountMenu.add(separator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 200, 10));

        add(panelAccountMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(1400, 60, 200, 110));
        panelAccountMenu.setVisible(false);
        panelAccountMenu.setEnabled(false);
        
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        setBackground(java.awt.Color.white);
        setPreferredSize(new java.awt.Dimension(1600, 1000));

        panelOptionsBar.setBackground(new java.awt.Color(20, 62, 125));
        panelOptionsBar.setForeground(new java.awt.Color(34, 52, 143));
        panelOptionsBar.setPreferredSize(new java.awt.Dimension(1600, 60));
        panelOptionsBar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelOptionTraps.setBackground(java.awt.Color.red);
        panelOptionTraps.setOpaque(false);
        panelOptionTraps.setPreferredSize(new java.awt.Dimension(200, 60));
        panelOptionTraps.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label5.setHorizontalAlignment(SwingConstants.CENTER);
        label5.setIcon(new ImageIcon(getClass().getResource("/images/icon_traps_40.png"))); // NOI18N
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
        label1.setIcon(new ImageIcon(getClass().getResource("/images/icon_network_40.png"))); // NOI18N
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
        label4.setIcon(new ImageIcon(getClass().getResource("/images/icon_template2_40.png"))); // NOI18N
        label4.setFocusable(false);
        panelOptionTemplates.add(label4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 70, 60));

        panelOptionsBar.add(panelOptionTemplates, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 0, -1, -1));

        labelIconAccount.setHorizontalAlignment(SwingConstants.CENTER);
        labelIconAccount.setIcon(new ImageIcon(getClass().getResource("/images/icon_account_40.png"))); // NOI18N
        panelOptionsBar.add(labelIconAccount, new org.netbeans.lib.awtextra.AbsoluteConstraints(1540, 0, 60, 60));

        labelIconSettings.setHorizontalAlignment(SwingConstants.CENTER);
        labelIconSettings.setIcon(new ImageIcon(getClass().getResource("/images/icon_settings_40.png"))); // NOI18N
        labelIconSettings.setToolTipText("");
        panelOptionsBar.add(labelIconSettings, new org.netbeans.lib.awtextra.AbsoluteConstraints(1480, 0, 60, 60));

        add(panelOptionsBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

    }
}
