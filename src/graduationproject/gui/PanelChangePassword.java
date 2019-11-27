/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.gui;

import graduationproject.controllers.UserManagementController;
import graduationproject.data.DataManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author cloud
 */
public class PanelChangePassword extends JPanel {

    private JButton buttonCancel;
    private JButton buttonChange;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel labelErrorInform;
    private JPanel panelChangePassword;
    private JPanel panelOverlay;
    private JPasswordField pfieldConfirmPassword;
    private JPasswordField pfieldNewPassword;
    private JPasswordField pfieldOldPassword;
    private JScrollPane scrollpane1;
    private JTextArea tarea1;

    private ActionListener listenerButton;
    
    public PanelChangePassword() {
        initComponents();
        initListeners();
    }

    public void initComponents() {
        panelOverlay = new javax.swing.JPanel();
        panelChangePassword = new javax.swing.JPanel();
        label1 = new javax.swing.JLabel();
        label2 = new javax.swing.JLabel();
        label3 = new javax.swing.JLabel();
        labelErrorInform = new javax.swing.JLabel();
        buttonCancel = new javax.swing.JButton();
        buttonChange = new javax.swing.JButton();
        scrollpane1 = new javax.swing.JScrollPane();
        tarea1 = new javax.swing.JTextArea();
        pfieldConfirmPassword = new javax.swing.JPasswordField();
        pfieldOldPassword = new javax.swing.JPasswordField();
        pfieldNewPassword = new javax.swing.JPasswordField();

        setPreferredSize(new Dimension(1600, 940));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelOverlay.setBackground(new Color(0, 0, 0, 150));
        panelOverlay.setPreferredSize(new java.awt.Dimension(1600, 940));
        panelOverlay.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelChangePassword.setBackground(java.awt.Color.white);
        panelChangePassword.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label1.setBackground(new java.awt.Color(56, 48, 130));
        label1.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        label1.setForeground(new java.awt.Color(56, 48, 130));
        label1.setText("Old Password");
        panelChangePassword.add(label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 200, -1, -1));

        label2.setBackground(new java.awt.Color(56, 48, 130));
        label2.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        label2.setForeground(new java.awt.Color(56, 48, 130));
        label2.setText("New Password");
        panelChangePassword.add(label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 290, -1, -1));

        label3.setBackground(new java.awt.Color(56, 48, 130));
        label3.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        label3.setForeground(new java.awt.Color(56, 48, 130));
        label3.setText("Confirm Password");
        panelChangePassword.add(label3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 390, -1, -1));

        labelErrorInform.setForeground(java.awt.Color.red);
        labelErrorInform.setText("");
        panelChangePassword.add(labelErrorInform, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 470, 360, -1));

        buttonCancel.setBackground(new java.awt.Color(48, 59, 130));
        buttonCancel.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        buttonCancel.setForeground(java.awt.Color.white);
        buttonCancel.setText("Cancel");
        buttonCancel.setBorderPainted(false);
        panelChangePassword.add(buttonCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 510, 100, 40));

        buttonChange.setBackground(new java.awt.Color(48, 59, 130));
        buttonChange.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        buttonChange.setForeground(java.awt.Color.white);
        buttonChange.setText("Change");
        buttonChange.setBorderPainted(false);
        panelChangePassword.add(buttonChange, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 510, 100, 40));

        scrollpane1.setBorder(null);
        scrollpane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollpane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollpane1.setFocusable(false);
        scrollpane1.setOpaque(false);

        tarea1.setEditable(false);
        tarea1.setColumns(20);
        tarea1.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        tarea1.setLineWrap(true);
        tarea1.setRows(5);
        tarea1.setText("   Changing password will log you out. Youshould re-login to check your newly updated password.");
        tarea1.setWrapStyleWord(true);
        tarea1.setBorder(null);
        scrollpane1.setViewportView(tarea1);

        panelChangePassword.add(scrollpane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 110, 350, 70));

        pfieldConfirmPassword.setFont(new java.awt.Font("SansSerif", 0, 15)); // NOI18N
        pfieldConfirmPassword.setText("jPasswordField1");
        pfieldConfirmPassword.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        pfieldConfirmPassword.setOpaque(false);
        panelChangePassword.add(pfieldConfirmPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 410, 350, 40));

        pfieldOldPassword.setFont(new java.awt.Font("SansSerif", 0, 15)); // NOI18N
        pfieldOldPassword.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        pfieldOldPassword.setOpaque(false);
        panelChangePassword.add(pfieldOldPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 220, 350, 40));

        pfieldNewPassword.setFont(new java.awt.Font("SansSerif", 0, 15)); // NOI18N
        pfieldNewPassword.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        pfieldNewPassword.setOpaque(false);
        panelChangePassword.add(pfieldNewPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 310, 350, 40));

        panelOverlay.add(panelChangePassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(553, 159, 514, 626));

        add(panelOverlay, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
    }
    
    public void initListeners() {
        this.listenerButton = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton source = (JButton) e.getSource();
                if (source == buttonChange) {
                    UserManagementController userController = new UserManagementController();
                    if (!userController.processChangingPassword(   
                            DataManager.getInstance().getActiveAccountId(),
                            String.valueOf(pfieldOldPassword.getPassword()),
                            String.valueOf(pfieldNewPassword.getPassword()),
                            String.valueOf(pfieldConfirmPassword.getPassword()))) {
                        JOptionPane.showMessageDialog(null, userController.getResultMessage());
                    } else {
                        ApplicationWindow.getInstance().switchPanel(ApplicationWindow.PANELS.PANEL_INITIAL);
//                        ApplicationWindow.getInstance().getPanelInitial().refreshPanel();  
                        new UserManagementController().processLogout();
                    }
                }
                if (source == buttonCancel) {
                    ApplicationWindow.getInstance().getPanelMain().getPanelUserProfile().switchPanelChangePassword();
                }
                
            }
            
        };
        
        this.buttonCancel.addActionListener(this.listenerButton);
        this.buttonChange.addActionListener(this.listenerButton);
    }
    
    public void refreshPanel() {
        this.pfieldOldPassword.setText("");
        this.pfieldNewPassword.setText("");
        this.pfieldConfirmPassword.setText("");
    }
}
