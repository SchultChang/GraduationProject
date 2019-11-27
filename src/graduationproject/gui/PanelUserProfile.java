/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.gui;

import graduationproject.controllers.UserManagementController;
import graduationproject.controllers.UserManagementController.DataOrders;
import graduationproject.data.DataManager;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

/**
 *
 * @author cloud
 */
public class PanelUserProfile extends JPanel {

    private JButton buttonCancel;
    private JButton buttonSave;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JLabel label6;
    private JLabel label7;
    private JLabel labelAccount;
    private JLabel labelAvatar;
    private JLabel labelChangePassword;
    private JLabel labelDisableAutoLogin;
    private JPanel panelAccountInformation;
    private JPanel panelOverlay;
    private JPanel panelPersonalInformation;
    private JSeparator separator1;
    private JTextField tfieldAge;
    private JTextField tfieldEmail;
    private JTextField tfieldName;
    private JTextField tfieldPhone;
    private JTextField tfieldPosition;

    private PanelChangePassword panelChangePassword;
    
    private MouseAdapter listenerLabel;
    private ActionListener listenerButton;

    private final String LABEL_AUTOLOGIN_ENABLE = "Save Password";
    private final String LABEL_AUTOLOGIN_DISABLE = "Remove Saved Password";

    public PanelUserProfile() {
        initComponents();
        initListeners();
    }

    private void initComponents() {
        labelAvatar = new JLabel();
        panelOverlay = new JPanel();
        panelAccountInformation = new JPanel();
        labelAccount = new JLabel();
        label1 = new JLabel();
        separator1 = new JSeparator();
        label2 = new JLabel();
        labelDisableAutoLogin = new JLabel();
        labelChangePassword = new JLabel();
        panelPersonalInformation = new JPanel();
        label3 = new JLabel();
        tfieldName = new JTextField();
        tfieldAge = new JTextField();
        label4 = new JLabel();
        tfieldPosition = new JTextField();
        label5 = new JLabel();
        label6 = new JLabel();
        tfieldPhone = new JTextField();
        tfieldEmail = new JTextField();
        label7 = new JLabel();
        buttonCancel = new JButton();
        buttonSave = new JButton();

        setPreferredSize(new java.awt.Dimension(1600, 940));
        setLayout(new AbsoluteLayout());
        setBackground(Color.white);
        
        initPanelChangePassword();

        labelAvatar.setBackground(java.awt.Color.white);
        labelAvatar.setHorizontalAlignment(SwingConstants.CENTER);
        labelAvatar.setText("Avatar (40  x 20)");
        labelAvatar.setOpaque(true);
        labelAvatar.setPreferredSize(new java.awt.Dimension(40, 20));
        add(labelAvatar, new AbsoluteConstraints(150, 20, 230, 280));

        panelOverlay.setBackground(new java.awt.Color(73, 125, 222));

        GroupLayout panelOverlayLayout = new GroupLayout(panelOverlay);
        panelOverlay.setLayout(panelOverlayLayout);
        panelOverlayLayout.setHorizontalGroup(
                panelOverlayLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGap(0, 1600, Short.MAX_VALUE)
        );
        panelOverlayLayout.setVerticalGroup(
                panelOverlayLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGap(0, 120, Short.MAX_VALUE)
        );

        add(panelOverlay, new AbsoluteConstraints(0, 0, 1600, -1));

        panelAccountInformation.setBackground(java.awt.Color.white);
        panelAccountInformation.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Account Information", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new java.awt.Font("SansSerif", 1, 14)));
        panelAccountInformation.setLayout(new AbsoluteLayout());

        labelAccount.setFont(new java.awt.Font("SansSerif", 0, 18));
        labelAccount.setText(". . .");
        panelAccountInformation.add(labelAccount, new AbsoluteConstraints(220, 60, 220, 40));

        label1.setFont(new java.awt.Font("SansSerif", 0, 18));
        label1.setForeground(new java.awt.Color(73, 125, 222));
        label1.setText("Account");
        panelAccountInformation.add(label1, new AbsoluteConstraints(60, 60, 80, 40));
        panelAccountInformation.add(separator1, new AbsoluteConstraints(0, 110, 470, 10));

        label2.setFont(new java.awt.Font("SansSerif", 0, 18));
        label2.setForeground(new java.awt.Color(73, 125, 222));
        label2.setText("Password");
        panelAccountInformation.add(label2, new AbsoluteConstraints(60, 140, 130, 40));

        labelDisableAutoLogin.setFont(new java.awt.Font("SansSerif", 1, 14));
        labelDisableAutoLogin.setForeground(new java.awt.Color(222, 73, 88));
        labelDisableAutoLogin.setText(LABEL_AUTOLOGIN_DISABLE);
        panelAccountInformation.add(labelDisableAutoLogin, new AbsoluteConstraints(220, 180, 220, 40));

        labelChangePassword.setFont(new java.awt.Font("SansSerif", 0, 18));
        labelChangePassword.setForeground(java.awt.Color.blue);
        labelChangePassword.setText("Change password");
        panelAccountInformation.add(labelChangePassword, new AbsoluteConstraints(220, 140, 220, 40));

        add(panelAccountInformation, new AbsoluteConstraints(150, 340, 470, 260));

        panelPersonalInformation.setBackground(java.awt.Color.white);
        panelPersonalInformation.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Personal Information",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new java.awt.Font("SansSerif", 1, 14)));
        panelPersonalInformation.setLayout(new AbsoluteLayout());

        label3.setBackground(new java.awt.Color(73, 125, 222));
        label3.setFont(new java.awt.Font("SansSerif", 1, 16));
        label3.setForeground(new java.awt.Color(73, 125, 222));
        label3.setText("Name:");
        panelPersonalInformation.add(label3, new AbsoluteConstraints(150, 90, -1, 20));

        tfieldName.setFont(new java.awt.Font("SansSerif", 0, 16));
        tfieldName.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        tfieldName.setOpaque(false);
        panelPersonalInformation.add(tfieldName, new AbsoluteConstraints(270, 90, 342, 30));

        tfieldAge.setFont(new java.awt.Font("SansSerif", 0, 16));
        tfieldAge.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        tfieldAge.setOpaque(false);
        panelPersonalInformation.add(tfieldAge, new AbsoluteConstraints(270, 160, 342, 30));

        label4.setBackground(new java.awt.Color(73, 125, 222));
        label4.setFont(new java.awt.Font("SansSerif", 1, 16));
        label4.setForeground(new java.awt.Color(73, 125, 222));
        label4.setText("Age:");
        panelPersonalInformation.add(label4, new AbsoluteConstraints(150, 160, -1, 20));

        tfieldPosition.setFont(new java.awt.Font("SansSerif", 0, 16));
        tfieldPosition.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        tfieldPosition.setOpaque(false);
        panelPersonalInformation.add(tfieldPosition, new AbsoluteConstraints(270, 230, 342, 30));

        label5.setBackground(new java.awt.Color(73, 125, 222));
        label5.setFont(new java.awt.Font("SansSerif", 1, 16));
        label5.setForeground(new java.awt.Color(73, 125, 222));
        label5.setText("Position:");
        panelPersonalInformation.add(label5, new AbsoluteConstraints(150, 230, -1, 20));

        label6.setBackground(new java.awt.Color(73, 125, 222));
        label6.setFont(new java.awt.Font("SansSerif", 1, 16));
        label6.setForeground(new java.awt.Color(73, 125, 222));
        label6.setText("Phone:");
        panelPersonalInformation.add(label6, new AbsoluteConstraints(150, 300, -1, 20));

        tfieldPhone.setFont(new java.awt.Font("SansSerif", 0, 16));
        tfieldPhone.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        tfieldPhone.setOpaque(false);
        panelPersonalInformation.add(tfieldPhone, new AbsoluteConstraints(270, 300, 342, 30));

        tfieldEmail.setFont(new java.awt.Font("SansSerif", 0, 16));
        tfieldEmail.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        tfieldEmail.setOpaque(false);
        panelPersonalInformation.add(tfieldEmail, new AbsoluteConstraints(270, 370, 342, 30));

        label7.setBackground(new java.awt.Color(73, 125, 222));
        label7.setFont(new java.awt.Font("SansSerif", 1, 16));
        label7.setForeground(new java.awt.Color(73, 125, 222));
        label7.setText("Email:");
        panelPersonalInformation.add(label7, new AbsoluteConstraints(150, 380, -1, 20));

        add(panelPersonalInformation, new AbsoluteConstraints(680, 340, 760, 460));

        buttonCancel.setBackground(new java.awt.Color(73, 125, 222));
        buttonCancel.setFont(new java.awt.Font("SansSerif", 1, 15));
        buttonCancel.setForeground(java.awt.Color.white);
        buttonCancel.setText("Cancel");
        buttonCancel.setBorder(null);
        buttonCancel.setBorderPainted(false);
        add(buttonCancel, new AbsoluteConstraints(1350, 830, 90, 40));

        buttonSave.setBackground(new java.awt.Color(73, 125, 222));
        buttonSave.setFont(new java.awt.Font("SansSerif", 1, 15));
        buttonSave.setForeground(java.awt.Color.white);
        buttonSave.setText("Save");
        buttonSave.setBorder(null);
        buttonSave.setBorderPainted(false);
        add(buttonSave, new AbsoluteConstraints(1250, 830, 90, 40));

    }
    
    private void initPanelChangePassword() {
        this.panelChangePassword = new PanelChangePassword();
        this.add(this.panelChangePassword, new AbsoluteConstraints(0, 0, -1, -1));
        this.panelChangePassword.setVisible(false);
        this.panelChangePassword.setEnabled(false);
    }

    private void initListeners() {
        this.listenerButton = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton source = (JButton) e.getSource();
                if (source == buttonCancel) {
                    initData();
                }
                if (source == buttonSave) {
                    UserManagementController userController = new UserManagementController();
                    if (!userController.processUpdatingUserProfile(
                            DataManager.getInstance().getActiveAccountId(),
                            PanelUserProfile.this.getUserDataToUpdate())) {
                        JOptionPane.showMessageDialog(null, userController.getResultMessage());
                    }
                }
            }
        };

        this.buttonCancel.addActionListener(this.listenerButton);
        this.buttonSave.addActionListener(this.listenerButton);

        this.listenerLabel = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                JLabel source = (JLabel) e.getSource();
                if (source == labelDisableAutoLogin) {
                    UserManagementController userController = new UserManagementController();
                    if (!userController.processSwitchingAutoLogin(DataManager.getInstance().getActiveAccountId())) {
                        JOptionPane.showMessageDialog(null, userController.getResultMessage());
                        return;
                    }
                    if (labelDisableAutoLogin.getText().equals(LABEL_AUTOLOGIN_ENABLE)) {
                        labelDisableAutoLogin.setText(LABEL_AUTOLOGIN_DISABLE);
                    } else {
                        labelDisableAutoLogin.setText(LABEL_AUTOLOGIN_ENABLE);
                    }
                }
                
                if (source == labelChangePassword) {
                    panelChangePassword.refreshPanel();
                    switchPanelChangePassword();                    
                }
            }
        };
        
        this.labelChangePassword.addMouseListener(this.listenerLabel);
        this.labelDisableAutoLogin.addMouseListener(this.listenerLabel);
    }

    public void switchPanelChangePassword() {        
        this.panelChangePassword.setEnabled(!this.panelChangePassword.isEnabled());
        this.panelChangePassword.setVisible(!this.panelChangePassword.isVisible());
        
        this.revalidate();
        this.repaint();
    }
    
    public void initData() {
        UserManagementController userController = new UserManagementController();
        List<String> userData = userController.processGettingUserProfile(DataManager.getInstance().getActiveAccountId());

        if (userController.isAccountRemembered()) {
            this.labelDisableAutoLogin.setText(LABEL_AUTOLOGIN_DISABLE);
        } else {
            this.labelDisableAutoLogin.setText(LABEL_AUTOLOGIN_ENABLE);
        }

        this.labelAccount.setText(userData.get(DataOrders.ACCOUNT.getValue()));
        this.tfieldName.setText(userData.get(DataOrders.NAME.getValue()));
        this.tfieldAge.setText(userData.get(DataOrders.AGE.getValue()));
        this.tfieldPosition.setText(userData.get(DataOrders.POSITION.getValue()));
        this.tfieldEmail.setText(userData.get(DataOrders.EMAIL.getValue()));
        this.tfieldPhone.setText(userData.get(DataOrders.PHONE.getValue()));
    }

    public List<String> getUserDataToUpdate() {
        List<String> result = new ArrayList<String>();

        result.add(DataOrders.ACCOUNT.getValue(), new String());
        result.add(DataOrders.PASSWORD.getValue(), new String());
        result.add(DataOrders.CONFIRM.getValue(), new String());
        result.add(DataOrders.NAME.getValue(), this.tfieldName.getText());
        result.add(DataOrders.AGE.getValue(), this.tfieldAge.getText());
        result.add(DataOrders.POSITION.getValue(), this.tfieldPosition.getText());
        result.add(DataOrders.EMAIL.getValue(), this.tfieldEmail.getText());
        result.add(DataOrders.PHONE.getValue(), this.tfieldPhone.getText());

        return result;
    }

}
