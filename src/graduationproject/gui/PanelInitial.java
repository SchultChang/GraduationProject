/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.gui;

import graduationproject.controllers.UserManagementController;
import graduationproject.data.ActiveDeviceDataCollector;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import org.netbeans.lib.awtextra.AbsoluteConstraints;

/**
 *
 * @author cloud
 */
public class PanelInitial extends JPanel {

    private JButton buttonCancelRegistration;
    private JButton buttonCancelSubmission;
    private JButton buttonLogin;
    private JButton buttonRegister;
    private JButton buttonSubmitAnswers;
    private JCheckBox checkboxTerms;
    private JCheckBox checkboxRememberPassword;
    private JLabel label1;
    private JLabel label10;
    private JLabel label11;
    private JLabel label12;
    private JLabel label13;
    private JLabel label14;
    private JLabel label15;
    private JLabel label16;
    private JLabel label17;
    private JLabel label18;
    private JLabel label19;
    private JLabel label2;
    private JLabel label20;
    private JLabel label21;
    private JLabel label22;
    private JLabel label23;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JLabel label6;
    private JLabel label7;
    private JLabel label8;
    private JLabel label9;
    private JLabel labelAddQuestions;
    private JLabel labelBackground;
    private JLabel labelIconAccount;
    private JLabel labelBackAddingQuestions;
    private JLabel labelBackSubmittingQuestions;
    private JLabel labelOpenAccountList;
    private JLabel labelRecoverPassword;
    private JLabel labelRegister;
    private JLabel labelSubmitQuestion1;
    private JLabel labelSubmitQuestion2;
    private JLabel labelSubmitQuestion3;
    private JLabel labelTerms;
    private JList<String> listAccounts;

    private JPasswordField pfieldPassword;
    private JPasswordField pfieldRegisterConfirm;
    private JPasswordField pfieldRegisterPassword;
    private JScrollPane scrollpane1;
    private JScrollPane scrollpane2;
    private JScrollPane scrollpane3;
    private JScrollPane scrollpane4;
    private JScrollPane scrollpane5;
    private JScrollPane scrollpane6;
    private JScrollPane scrollpaneAccountList;
    private JTextArea tareaAddAnswer1;
    private JTextArea tareaAddAnswer2;
    private JTextArea tareaAddAnswer3;
    private JTextArea tareaSubmitAnswer1;
    private JTextArea tareaSubmitAnswer2;
    private JTextArea tareaSubmitAnswer3;
    private JTextField tfieldAccount;
    private JTextField tfieldAddQuestion1;
    private JTextField tfieldAddQuestion2;
    private JTextField tfieldAddQuestion3;
    private JTextField tfieldRegisterAccount;
    private JFormattedTextField tfieldRegisterAge;
    private JTextField tfieldRegisterEmail;
    private JTextField tfieldRegisterName;
    private JFormattedTextField tfieldRegisterPhone;
    private JTextField tfieldRegisterPosition;

    private JPanel panelAddQuestions;
    private JPanel panelLogin;
    private JPanel panelOverlay;
    private JPanel panelRegister;
    private JPanel panelSubmitQuestions;

    private JPanel currentDisplayedPanel;
   
    private MouseAdapter listenerLabel;
    private ActionListener listenerButton;
    private MouseAdapter listenerListHover, listenerListClick;

    private int mouseHoverListId = -1;     
    private int accountId;
    private int[] rememberedAccountIds;

    public enum PANELS {
        PANEL_LOGIN,
        PANEL_REGISTER,
        PANEL_ADDQUESTIONS,
        PANEL_SUBMITQUESTIONS
    }

    public PanelInitial() {
        initComponents();
        initListeners();
        initViewData();
    }

    private void initComponents() {
        scrollpaneAccountList = new JScrollPane();
        listAccounts = new JList<>();
        panelRegister = new JPanel();
        label4 = new JLabel();
        tfieldRegisterName = new JTextField();
        label5 = new JLabel();
        tfieldRegisterPosition = new JTextField();
        label6 = new JLabel();
        tfieldRegisterEmail = new JTextField();
        label7 = new JLabel();
        label8 = new JLabel();
        label9 = new JLabel();
        tfieldRegisterAccount = new JTextField();
        label10 = new JLabel();
        label11 = new JLabel();
        buttonRegister = new JButton();
        buttonCancelRegistration = new JButton();
        labelAddQuestions = new JLabel();
        checkboxTerms = new JCheckBox();
        labelTerms = new JLabel();
        tfieldRegisterAge = new JFormattedTextField();
        tfieldRegisterPhone = new JFormattedTextField();
        pfieldRegisterPassword = new JPasswordField();
        pfieldRegisterConfirm = new JPasswordField();
        panelAddQuestions = new JPanel();
        label12 = new JLabel();
        tfieldAddQuestion1 = new JTextField();
        label13 = new JLabel();
        scrollpane1 = new JScrollPane();
        tareaAddAnswer1 = new JTextArea();
        label14 = new JLabel();
        tfieldAddQuestion2 = new JTextField();
        label15 = new JLabel();
        scrollpane2 = new JScrollPane();
        tareaAddAnswer2 = new JTextArea();
        label16 = new JLabel();
        tfieldAddQuestion3 = new JTextField();
        label17 = new JLabel();
        scrollpane3 = new JScrollPane();
        tareaAddAnswer3 = new JTextArea();
        labelBackAddingQuestions = new JLabel();
        panelSubmitQuestions = new JPanel();
        label18 = new JLabel();
        label19 = new JLabel();
        scrollpane4 = new JScrollPane();
        tareaSubmitAnswer1 = new JTextArea();
        label20 = new JLabel();
        label21 = new JLabel();
        scrollpane5 = new JScrollPane();
        tareaSubmitAnswer2 = new JTextArea();
        label22 = new JLabel();
        label23 = new JLabel();
        scrollpane6 = new JScrollPane();
        tareaSubmitAnswer3 = new JTextArea();
        buttonSubmitAnswers = new JButton();
        buttonCancelSubmission = new JButton();
        labelSubmitQuestion3 = new JLabel();
        labelSubmitQuestion1 = new JLabel();
        labelSubmitQuestion2 = new JLabel();
        labelBackSubmittingQuestions = new JLabel();
        panelOverlay = new JPanel();
        panelLogin = new JPanel();
        labelIconAccount = new JLabel();
        label1 = new JLabel();
        labelRecoverPassword = new JLabel();
        buttonLogin = new JButton();
        label2 = new JLabel();
        label3 = new JLabel();
        labelRegister = new JLabel();
        labelOpenAccountList = new JLabel();
        pfieldPassword = new JPasswordField();
        tfieldAccount = new JTextField();
        checkboxRememberPassword = new JCheckBox();
        labelBackground = new JLabel();

        scrollpaneAccountList.setBorder(null);
        scrollpaneAccountList.setOpaque(false);

        listAccounts.setFont(new java.awt.Font("SansSerif", 0, 16));
        listAccounts.setModel(new DefaultListModel());
        listAccounts.setBorder(null);
        listAccounts.setOpaque(false);
        scrollpaneAccountList.setViewportView(listAccounts);

        setBackground(new java.awt.Color(0, 0, 0));
        setPreferredSize(new java.awt.Dimension(1600, 1000));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelOverlay.setBackground(new Color(0, 0, 0, 130));
        panelOverlay.setPreferredSize(new java.awt.Dimension(1600, 1000));
        panelOverlay.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        this.initChildPanels();

        add(panelOverlay, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        labelBackground.setBackground(java.awt.Color.black);
        labelBackground.setIcon(new ImageIcon(getClass().getResource("/resources/login_background.jpg"))); 
        add(labelBackground, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

    }

    private void initChildPanels() {
        initPanelLogin();
        initPanelRegister();
        initPanelAddQuestions();
        initPanelSubmitQuestions();

        panelOverlay.add(panelLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 250, 420, 560));
        this.currentDisplayedPanel = panelLogin;

        panelRegister.setVisible(false);
        panelRegister.setEnabled(false);

        panelAddQuestions.setVisible(false);
        panelAddQuestions.setEnabled(false);

        panelSubmitQuestions.setVisible(false);
        panelSubmitQuestions.setEnabled(false);
    }

    private void initPanelLogin() {
        panelLogin.setBackground(new Color(255, 255, 255, 200));
        panelLogin.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        panelLogin.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelLogin.add(scrollpaneAccountList, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 290, 330, 230));
        scrollpaneAccountList.setVisible(false);
        scrollpaneAccountList.setEnabled(false);

        labelIconAccount.setIcon(new ImageIcon(getClass().getResource("/resources/account_icon.png"))); 
        panelLogin.add(labelIconAccount, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 80, -1, -1));

        label1.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        label1.setForeground(new java.awt.Color(20, 99, 236));
        label1.setText("account");
        panelLogin.add(label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, -1, -1));

        labelRecoverPassword.setFont(new java.awt.Font("SansSerif", 0, 12)); 
        labelRecoverPassword.setForeground(new java.awt.Color(236, 46, 20));
        labelRecoverPassword.setText("Recover password");
        panelLogin.add(labelRecoverPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 480, -1, -1));

        buttonLogin.setBackground(new java.awt.Color(20, 99, 236));
        buttonLogin.setFont(new java.awt.Font("SansSerif", 1, 16)); 
        buttonLogin.setForeground(java.awt.Color.white);
        buttonLogin.setText("Login");
        buttonLogin.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        panelLogin.add(buttonLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 430, 170, 30));

        label2.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        label2.setForeground(new java.awt.Color(20, 99, 236));
        label2.setText("password");
        panelLogin.add(label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 320, -1, -1));

        label3.setFont(new java.awt.Font("SansSerif", 0, 12)); 
        label3.setText("or");
        panelLogin.add(label3, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 480, -1, -1));

        labelRegister.setFont(new java.awt.Font("SansSerif", 0, 12)); 
        labelRegister.setForeground(new java.awt.Color(236, 46, 20));
        labelRegister.setText("Register");
        panelLogin.add(labelRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 480, -1, -1));

        labelOpenAccountList.setBackground(java.awt.Color.white);
        labelOpenAccountList.setIcon(new ImageIcon(getClass().getResource("/resources/down_arrow_icon.png"))); 
        labelOpenAccountList.setOpaque(true);
        panelLogin.add(labelOpenAccountList, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 260, 30, 30));

        pfieldPassword.setBackground(java.awt.Color.white);
        pfieldPassword.setFont(new java.awt.Font("SansSerif", 0, 20)); 
        pfieldPassword.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        panelLogin.add(pfieldPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 340, 330, 30));

        tfieldAccount.setBackground(java.awt.Color.white);
        tfieldAccount.setFont(new java.awt.Font("SansSerif", 0, 20)); 
        tfieldAccount.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        panelLogin.add(tfieldAccount, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 260, 300, 30));

        checkboxRememberPassword.setFont(new java.awt.Font("SansSerif", 0, 14)); 
        checkboxRememberPassword.setForeground(new java.awt.Color(236, 46, 20));
        checkboxRememberPassword.setText("Remember me");
        checkboxRememberPassword.setOpaque(false);
        panelLogin.add(checkboxRememberPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 390, 130, -1));
    }

    private void initPanelRegister() {
        panelRegister.setBackground(new java.awt.Color(255, 255, 255));
        panelRegister.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label4.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        label4.setForeground(new java.awt.Color(20, 99, 236));
        label4.setText("Name");
        panelRegister.add(label4, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 290, -1, 30));

        tfieldRegisterName.setFont(new java.awt.Font("SansSerif", 0, 15)); 
        tfieldRegisterName.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(20, 99, 236)));
        panelRegister.add(tfieldRegisterName, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 290, 300, 30));

        label5.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        label5.setForeground(new java.awt.Color(20, 99, 236));
        label5.setText("Age");
        panelRegister.add(label5, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 340, -1, 30));

        tfieldRegisterPosition.setFont(new java.awt.Font("SansSerif", 0, 15)); 
        tfieldRegisterPosition.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(20, 99, 236)));
        panelRegister.add(tfieldRegisterPosition, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 390, 300, 30));

        label6.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        label6.setForeground(new java.awt.Color(20, 99, 236));
        label6.setText("Position");
        panelRegister.add(label6, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 390, -1, 30));

        tfieldRegisterEmail.setFont(new java.awt.Font("SansSerif", 0, 15)); 
        tfieldRegisterEmail.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(20, 99, 236)));
        panelRegister.add(tfieldRegisterEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 440, 300, 30));

        label7.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        label7.setForeground(new java.awt.Color(20, 99, 236));
        label7.setText("Email");
        panelRegister.add(label7, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 440, -1, 30));

        label8.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        label8.setForeground(new java.awt.Color(20, 99, 236));
        label8.setText("Phone");
        panelRegister.add(label8, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 490, -1, 30));

        label9.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        label9.setForeground(new java.awt.Color(20, 99, 236));
        label9.setText("Account");
        panelRegister.add(label9, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 100, -1, 30));

        tfieldRegisterAccount.setFont(new java.awt.Font("SansSerif", 0, 15)); 
        tfieldRegisterAccount.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(20, 99, 236)));
        panelRegister.add(tfieldRegisterAccount, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 100, 300, 30));

        label10.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        label10.setForeground(new java.awt.Color(20, 99, 236));
        label10.setText("Password");
        panelRegister.add(label10, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 150, -1, 30));

        label11.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        label11.setForeground(new java.awt.Color(20, 99, 236));
        label11.setText("Confirm");
        panelRegister.add(label11, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 200, -1, 30));

        buttonRegister.setBackground(new java.awt.Color(20, 99, 236));
        buttonRegister.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        buttonRegister.setForeground(java.awt.Color.white);
        buttonRegister.setText("Register");
        buttonRegister.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        panelRegister.add(buttonRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 670, 80, 30));

        buttonCancelRegistration.setBackground(new java.awt.Color(20, 99, 236));
        buttonCancelRegistration.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        buttonCancelRegistration.setForeground(java.awt.Color.white);
        buttonCancelRegistration.setText("Cancel");
        buttonCancelRegistration.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        panelRegister.add(buttonCancelRegistration, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 670, 80, 30));

        labelAddQuestions.setFont(new java.awt.Font("SansSerif", 1, 13)); 
        labelAddQuestions.setForeground(new java.awt.Color(254, 13, 13));
        labelAddQuestions.setText("Add questions to recover password later?");
        panelRegister.add(labelAddQuestions, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 620, 390, -1));

        checkboxTerms.setFont(new java.awt.Font("SansSerif", 0, 15)); 
        checkboxTerms.setText("I aggree to the");
        checkboxTerms.setOpaque(false);
        panelRegister.add(checkboxTerms, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 580, -1, 30));

        labelTerms.setFont(new java.awt.Font("SansSerif", 0, 15)); 
        labelTerms.setForeground(new java.awt.Color(20, 99, 236));
        labelTerms.setText("terms and conditions");
        panelRegister.add(labelTerms, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 580, -1, 30));

        tfieldRegisterAge.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(20, 99, 236)));
        tfieldRegisterAge.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        ((NumberFormatter) tfieldRegisterAge.getFormatter()).setAllowsInvalid(false);
        tfieldRegisterAge.setFont(new java.awt.Font("SansSerif", 0, 15)); 
        tfieldRegisterAge.setOpaque(false);
        panelRegister.add(tfieldRegisterAge, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 340, 300, 30));

        tfieldRegisterPhone.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(20, 99, 236)));
        tfieldRegisterPhone.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        ((NumberFormatter) tfieldRegisterPhone.getFormatter()).setAllowsInvalid(false);
        tfieldRegisterPhone.setFont(new java.awt.Font("SansSerif", 0, 15)); 
        tfieldRegisterPhone.setOpaque(false);
        panelRegister.add(tfieldRegisterPhone, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 490, 300, 30));

        pfieldRegisterPassword.setFont(new java.awt.Font("SansSerif", 0, 15)); 
        pfieldRegisterPassword.setText("");
        pfieldRegisterPassword.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(20, 99, 236)));
        pfieldRegisterPassword.setOpaque(false);
        panelRegister.add(pfieldRegisterPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 150, 300, 30));

        pfieldRegisterConfirm.setFont(new java.awt.Font("SansSerif", 0, 15)); 
        pfieldRegisterConfirm.setText("");
        pfieldRegisterConfirm.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(20, 99, 236)));
        pfieldRegisterConfirm.setOpaque(false);
        panelRegister.add(pfieldRegisterConfirm, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 200, 300, 30));
    }

    private void initPanelAddQuestions() {
        panelAddQuestions.setBackground(java.awt.Color.white);
        panelAddQuestions.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label12.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        label12.setForeground(new java.awt.Color(12, 73, 197));
        label12.setText("Question 1:");
        panelAddQuestions.add(label12, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 130, -1, 30));

        tfieldAddQuestion1.setBackground(java.awt.Color.white);
        tfieldAddQuestion1.setFont(new java.awt.Font("SansSerif", 0, 15)); 
        tfieldAddQuestion1.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelAddQuestions.add(tfieldAddQuestion1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 130, 330, 30));

        label13.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        label13.setForeground(new java.awt.Color(216, 23, 23));
        label13.setText("Answer:");
        panelAddQuestions.add(label13, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 170, -1, 30));

        scrollpane1.setForeground(new java.awt.Color(60, 60, 60));

        tareaAddAnswer1.setBackground(java.awt.Color.white);
        tareaAddAnswer1.setColumns(20);
        tareaAddAnswer1.setFont(new java.awt.Font("SansSerif", 0, 15)); 
        tareaAddAnswer1.setLineWrap(true);
        tareaAddAnswer1.setRows(5);
        tareaAddAnswer1.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollpane1.setViewportView(tareaAddAnswer1);

        panelAddQuestions.add(scrollpane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 170, 330, -1));

        label14.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        label14.setForeground(new java.awt.Color(12, 73, 197));
        label14.setText("Question 2:");
        panelAddQuestions.add(label14, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 300, -1, 30));

        tfieldAddQuestion2.setBackground(java.awt.Color.white);
        tfieldAddQuestion2.setFont(new java.awt.Font("SansSerif", 0, 15)); 
        tfieldAddQuestion2.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelAddQuestions.add(tfieldAddQuestion2, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 300, 330, 30));

        label15.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        label15.setForeground(new java.awt.Color(216, 23, 23));
        label15.setText("Answer:");
        panelAddQuestions.add(label15, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 340, -1, 30));

        scrollpane2.setForeground(new java.awt.Color(60, 60, 60));

        tareaAddAnswer2.setBackground(java.awt.Color.white);
        tareaAddAnswer2.setColumns(20);
        tareaAddAnswer2.setFont(new java.awt.Font("SansSerif", 0, 15)); 
        tareaAddAnswer2.setLineWrap(true);
        tareaAddAnswer2.setRows(5);
        tareaAddAnswer2.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollpane2.setViewportView(tareaAddAnswer2);

        panelAddQuestions.add(scrollpane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 340, 330, -1));

        label16.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        label16.setForeground(new java.awt.Color(216, 23, 23));
        label16.setText("Answer:");
        panelAddQuestions.add(label16, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 520, -1, 30));

        tfieldAddQuestion3.setBackground(java.awt.Color.white);
        tfieldAddQuestion3.setFont(new java.awt.Font("SansSerif", 0, 15)); 
        tfieldAddQuestion3.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelAddQuestions.add(tfieldAddQuestion3, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 480, 330, 30));

        label17.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        label17.setForeground(new java.awt.Color(12, 73, 197));
        label17.setText("Question 3:");
        panelAddQuestions.add(label17, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 480, -1, 30));

        scrollpane3.setForeground(new java.awt.Color(60, 60, 60));

        tareaAddAnswer3.setBackground(java.awt.Color.white);
        tareaAddAnswer3.setColumns(20);
        tareaAddAnswer3.setFont(new java.awt.Font("SansSerif", 0, 15)); 
        tareaAddAnswer3.setLineWrap(true);
        tareaAddAnswer3.setRows(5);
        tareaAddAnswer3.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollpane3.setViewportView(tareaAddAnswer3);

        panelAddQuestions.add(scrollpane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 520, 330, -1));

        labelBackAddingQuestions.setIcon(new ImageIcon(getClass().getResource("/resources/icon_arrow_back80.png"))); 
        panelAddQuestions.add(labelBackAddingQuestions, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 320, -1, -1));

    }

    private void initPanelSubmitQuestions() {
        panelSubmitQuestions.setBackground(java.awt.Color.white);
        panelSubmitQuestions.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label18.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        label18.setForeground(new java.awt.Color(12, 73, 197));
        label18.setText("Question 1:");
        panelSubmitQuestions.add(label18, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 120, -1, 30));

        label19.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        label19.setForeground(new java.awt.Color(216, 23, 23));
        label19.setText("Answer:");
        panelSubmitQuestions.add(label19, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 160, -1, 30));

        scrollpane4.setForeground(new java.awt.Color(60, 60, 60));

        tareaSubmitAnswer1.setBackground(java.awt.Color.white);
        tareaSubmitAnswer1.setColumns(20);
        tareaSubmitAnswer1.setFont(new java.awt.Font("SansSerif", 0, 15)); 
        tareaSubmitAnswer1.setLineWrap(true);
        tareaSubmitAnswer1.setRows(5);
        tareaSubmitAnswer1.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollpane4.setViewportView(tareaSubmitAnswer1);

        panelSubmitQuestions.add(scrollpane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 160, 330, -1));

        label20.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        label20.setForeground(new java.awt.Color(12, 73, 197));
        label20.setText("Question 2:");
        panelSubmitQuestions.add(label20, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 290, -1, 30));

        label21.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        label21.setForeground(new java.awt.Color(216, 23, 23));
        label21.setText("Answer:");
        panelSubmitQuestions.add(label21, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 330, -1, 30));

        scrollpane5.setForeground(new java.awt.Color(60, 60, 60));

        tareaSubmitAnswer2.setBackground(java.awt.Color.white);
        tareaSubmitAnswer2.setColumns(20);
        tareaSubmitAnswer2.setFont(new java.awt.Font("SansSerif", 0, 15)); 
        tareaSubmitAnswer2.setLineWrap(true);
        tareaSubmitAnswer2.setRows(5);
        tareaSubmitAnswer2.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollpane5.setViewportView(tareaSubmitAnswer2);

        panelSubmitQuestions.add(scrollpane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 330, 330, -1));

        label22.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        label22.setForeground(new java.awt.Color(216, 23, 23));
        label22.setText("Answer:");
        panelSubmitQuestions.add(label22, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 510, -1, 30));

        label23.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        label23.setForeground(new java.awt.Color(12, 73, 197));
        label23.setText("Question 3:");
        panelSubmitQuestions.add(label23, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 470, -1, 30));

        scrollpane6.setForeground(new java.awt.Color(60, 60, 60));

        tareaSubmitAnswer3.setBackground(java.awt.Color.white);
        tareaSubmitAnswer3.setColumns(20);
        tareaSubmitAnswer3.setFont(new java.awt.Font("SansSerif", 0, 15)); 
        tareaSubmitAnswer3.setLineWrap(true);
        tareaSubmitAnswer3.setRows(5);
        tareaSubmitAnswer3.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollpane6.setViewportView(tareaSubmitAnswer3);

        panelSubmitQuestions.add(scrollpane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 510, 330, -1));

        buttonSubmitAnswers.setBackground(new java.awt.Color(119, 119, 255));
        buttonSubmitAnswers.setFont(new java.awt.Font("SansSerif", 1, 14)); 
        buttonSubmitAnswers.setForeground(new java.awt.Color(255, 255, 255));
        buttonSubmitAnswers.setText("Submit");
        buttonSubmitAnswers.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        panelSubmitQuestions.add(buttonSubmitAnswers, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 640, 100, 40));

        buttonCancelSubmission.setBackground(new java.awt.Color(119, 119, 255));
        buttonCancelSubmission.setFont(new java.awt.Font("SansSerif", 1, 14)); 
        buttonCancelSubmission.setForeground(new java.awt.Color(255, 255, 255));
        buttonCancelSubmission.setText("Cancel");
        buttonCancelSubmission.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        panelSubmitQuestions.add(buttonCancelSubmission, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 640, 100, 40));

        labelSubmitQuestion3.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        panelSubmitQuestions.add(labelSubmitQuestion3, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 470, 330, 30));

        labelSubmitQuestion1.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        panelSubmitQuestions.add(labelSubmitQuestion1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 120, 330, 30));

        labelSubmitQuestion2.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        panelSubmitQuestions.add(labelSubmitQuestion2, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 290, 330, 30));

        labelBackSubmittingQuestions.setIcon(new ImageIcon(getClass().getResource("/resources/icon_back_arrow_16.png"))); 
        panelSubmitQuestions.add(labelBackSubmittingQuestions, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 30, 30));

    }

    private void initListeners() {
        this.listenerLabel = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                JLabel source = (JLabel) e.getSource();
                if (source == labelRegister) {
                    PanelInitial.this.switchDisplayedPanel(PANELS.PANEL_REGISTER, true);
                }
                if (source == labelRecoverPassword) {
                    UserManagementController userController = new UserManagementController();
                    List<String> inputData = new ArrayList<String>();
                    inputData.add(UserManagementController.DataOrders.ACCOUNT.getValue(), tfieldAccount.getText());

                    List<String> questions = userController.processGettingQuestionsForRecovery(inputData);
                    if (questions == null) {
                        JOptionPane.showMessageDialog(null, userController.getResultMessage());
                    } else {
                        PanelInitial.this.switchDisplayedPanel(PANELS.PANEL_SUBMITQUESTIONS, true);
                        PanelInitial.this.updatePanelRecoveryQuestions(questions);
                        PanelInitial.this.accountId = userController.getCheckingAccountId();
                    }
                }
                if (source == labelAddQuestions) {
                    PanelInitial.this.switchDisplayedPanel(PANELS.PANEL_ADDQUESTIONS, false);
                }
                if (source == labelBackAddingQuestions) {
                    PanelInitial.this.switchDisplayedPanel(PANELS.PANEL_REGISTER, false);
                }
                if (source == labelBackSubmittingQuestions) {
                    PanelInitial.this.switchDisplayedPanel(PANELS.PANEL_LOGIN, false);
                }

                if (source == labelOpenAccountList) {
                    if (!scrollpaneAccountList.isEnabled()) {
                        scrollpaneAccountList.setEnabled(true);
                        scrollpaneAccountList.setVisible(true);
                    } else {
                        scrollpaneAccountList.setVisible(false);
                        scrollpaneAccountList.setEnabled(false);
                    }

                    PanelInitial.this.revalidate();
                    PanelInitial.this.repaint();
                }
            }
        };

        this.labelOpenAccountList.addMouseListener(this.listenerLabel);
        this.labelRegister.addMouseListener(this.listenerLabel);
        this.labelAddQuestions.addMouseListener(this.listenerLabel);
        this.labelRecoverPassword.addMouseListener(this.listenerLabel);
        this.labelBackAddingQuestions.addMouseListener(this.listenerLabel);
        this.labelBackSubmittingQuestions.addMouseListener(this.listenerLabel);

        this.listenerButton = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton source = (JButton) e.getSource();
                if (source == buttonRegister) {
                    UserManagementController userController = new UserManagementController();
                    if (!userController.processCreatingUser(PanelInitial.this.getDataForRegistration())) {
                        JOptionPane.showMessageDialog(null, userController.getResultMessage());
                    } else {
                        PanelInitial.this.switchDisplayedPanel(PANELS.PANEL_LOGIN, true);
                    }
                }
                if (source == buttonCancelRegistration) {
                    PanelInitial.this.switchDisplayedPanel(PANELS.PANEL_LOGIN, true);
                }

                if (source == buttonSubmitAnswers) {
                    UserManagementController userController = new UserManagementController();
                    String inform = userController.processRecoveringPassword(accountId, getDataForRecovery());
                    if (inform != null) {
                        JOptionPane.showMessageDialog(null, inform);
                    } else {
                        JOptionPane.showMessageDialog(null, userController.getResultMessage());
                    }
                }
                if (source == buttonCancelSubmission) {
                    PanelInitial.this.switchDisplayedPanel(PANELS.PANEL_LOGIN, false);
                }

                if (source == buttonLogin) {
                    UserManagementController userController = new UserManagementController();
                    if (!userController.processLogin(
                            tfieldAccount.getText(),
                            String.valueOf(pfieldPassword.getPassword()),
                            checkboxRememberPassword.isSelected())) {
                        JOptionPane.showMessageDialog(null, userController.getResultMessage());
                    } else {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        ActiveDeviceDataCollector.getInstance();
                        ApplicationWindow.getInstance().switchPanel(ApplicationWindow.PANELS.PANEL_MAIN);
                    }
                }
            }
        };

        this.buttonRegister.addActionListener(this.listenerButton);
        this.buttonCancelRegistration.addActionListener(this.listenerButton);
        this.buttonLogin.addActionListener(this.listenerButton);
        this.buttonCancelSubmission.addActionListener(this.listenerButton);
        this.buttonSubmitAnswers.addActionListener(this.listenerButton);

        this.listenerListClick = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (listAccounts.getSelectedIndex() >= 0) {
                    accountId = rememberedAccountIds[listAccounts.getSelectedIndex()];
                    
                    tfieldAccount.setText(listAccounts.getModel().getElementAt(listAccounts.getSelectedIndex()));                    
                    scrollpaneAccountList.setVisible(false);
                    scrollpaneAccountList.setEnabled(false);
                    checkboxRememberPassword.setSelected(true);

                    UserManagementController userController = new UserManagementController();
                    String password = userController.processGettingPassword(accountId);
                    if (password != null) {
                        PanelInitial.this.pfieldPassword.setText(password);
                    } else {
                        JOptionPane.showMessageDialog(null, userController.getResultMessage());
                    }

                    listAccounts.clearSelection();
                    PanelInitial.this.revalidate();
                    PanelInitial.this.repaint();
                }
            }
        };
        this.listAccounts.addMouseListener(this.listenerListClick);
        
        this.listenerListHover = new MouseAdapter(){
            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = new Point(e.getX(), e.getY());
                int index = listAccounts.locationToIndex(p);
                if (index != mouseHoverListId) {
                    mouseHoverListId = index;
                    listAccounts.setSelectedIndex(index);
                    listAccounts.repaint();
                }
            }
        };
        this.listAccounts.addMouseMotionListener(this.listenerListHover);
    }

    private void initViewData() {
        initViewDataPanelLogin();
    }

    private void initViewDataPanelLogin() {
        UserManagementController userController = new UserManagementController();
        List<String> accounts = userController.processGettingRememberedAccounts();

        if (accounts != null && accounts.size() > 0) {
            DefaultListModel listModel = (DefaultListModel) this.listAccounts.getModel();
            listModel.removeAllElements();

            int tempSize = accounts.size();
            for (int i = 0; i < tempSize; i++) {
                listModel.addElement(accounts.get(i));
            }

            this.listAccounts.revalidate();
            this.listAccounts.repaint();

            this.rememberedAccountIds = userController.getAccountIds();
        }
    }

    public void switchDisplayedPanel(PANELS newPanel, boolean shouldRefresh) {
        this.panelOverlay.remove(this.currentDisplayedPanel);
        this.currentDisplayedPanel.setVisible(false);
        this.currentDisplayedPanel.setEnabled(false);

        switch (newPanel) {
            case PANEL_LOGIN:
                this.displayPanel(panelLogin, 620, 250, 420, 560);
                this.initViewDataPanelLogin();
                if (shouldRefresh) {
                    this.refreshPanelLogin();
                    this.refreshPanelAddQuestions();
                }
                break;
            case PANEL_REGISTER:
                this.displayPanel(panelRegister, 520, 140, 620, 750);
                if (shouldRefresh) {
                    this.refreshPanelRegister();
                }
                break;
            case PANEL_ADDQUESTIONS:
                this.displayPanel(panelAddQuestions, 520, 140, 620, 750);
                if (shouldRefresh) {
                    this.refreshPanelAddQuestions();
                }
                break;
            case PANEL_SUBMITQUESTIONS:
                this.displayPanel(panelSubmitQuestions, 520, 140, 620, 750);
                if (shouldRefresh) {
                    this.refreshPanelRecovery();
                }
                break;
        }

        this.revalidate();
        this.repaint();
    }

    private void displayPanel(JPanel panel, int x, int y, int width, int height) {
        panel.setEnabled(true);
        panel.setVisible(true);

        this.panelOverlay.add(panel, new AbsoluteConstraints(x, y, width, height));
        this.currentDisplayedPanel = panel;
    }

    public void refreshPanel() {
        this.switchDisplayedPanel(PANELS.PANEL_LOGIN, true);
    }
    
    private void refreshPanelRegister() {
        this.tfieldRegisterName.setText("");
        this.tfieldRegisterAge.setValue(null);
        this.tfieldRegisterEmail.setText("");
        this.tfieldRegisterPosition.setText("");
        this.tfieldRegisterPhone.setValue(null);

        this.tfieldRegisterAccount.setText("");
        this.pfieldRegisterPassword.setText("");
        this.pfieldRegisterConfirm.setText("");

        this.checkboxTerms.setSelected(false);
    }

    private void refreshPanelAddQuestions() {
        this.tfieldAddQuestion1.setText("");
        this.tfieldAddQuestion2.setText("");
        this.tfieldAddQuestion3.setText("");

        this.tareaAddAnswer1.setText("");
        this.tareaAddAnswer2.setText("");
        this.tareaAddAnswer3.setText("");
    }

    private void refreshPanelLogin() {
        this.initViewData();
        this.tfieldAccount.setText("");
        this.pfieldPassword.setText("");
        this.checkboxRememberPassword.setSelected(false);
    }

    private void refreshPanelRecovery() {
        this.labelSubmitQuestion1.setText("");
        this.labelSubmitQuestion2.setText("");
        this.labelSubmitQuestion3.setText("");

        this.tareaSubmitAnswer1.setText("");
        this.tareaSubmitAnswer1.setEditable(true);
        this.tareaSubmitAnswer2.setText("");
        this.tareaSubmitAnswer2.setEditable(true);
        this.tareaSubmitAnswer3.setText("");
        this.tareaSubmitAnswer3.setEditable(true);
    }

    public void updatePanelRecoveryQuestions(List<String> data) {
        JLabel[] labelQuestions = {this.labelSubmitQuestion1, this.labelSubmitQuestion2, this.labelSubmitQuestion3};
        for (int i = 0; i < data.size(); i++) {
            labelQuestions[i].setText(data.get(i));
        }

        if (data.size() < labelQuestions.length) {
            JTextArea[] answerAreas = {this.tareaSubmitAnswer1, this.tareaSubmitAnswer2, this.tareaSubmitAnswer3};
            for (int i = data.size(); i < labelQuestions.length; i++) {
                answerAreas[i].setEditable(false);
            }
        }

        this.revalidate();
        this.repaint();
    }

    public List<String> getDataForRegistration() {
        List<String> result = new ArrayList<String>();

        result.add(UserManagementController.DataOrders.ACCOUNT.getValue(), this.tfieldRegisterAccount.getText());
        result.add(UserManagementController.DataOrders.PASSWORD.getValue(), String.valueOf(this.pfieldRegisterPassword.getPassword()));
        result.add(UserManagementController.DataOrders.CONFIRM.getValue(), String.valueOf(this.pfieldRegisterConfirm.getPassword()));
        result.add(UserManagementController.DataOrders.NAME.getValue(), this.tfieldRegisterName.getText());
        result.add(UserManagementController.DataOrders.AGE.getValue(), this.tfieldRegisterAge.getText());
        result.add(UserManagementController.DataOrders.POSITION.getValue(), this.tfieldRegisterPosition.getText());
        result.add(UserManagementController.DataOrders.EMAIL.getValue(), this.tfieldRegisterEmail.getText());
        result.add(UserManagementController.DataOrders.PHONE.getValue(), this.tfieldRegisterPhone.getText());
        result.add(UserManagementController.DataOrders.CONDITION.getValue(), String.valueOf(this.checkboxTerms.isSelected()));

        int tempCount = 0;
        JTextField[] questFields = {this.tfieldAddQuestion1, this.tfieldAddQuestion2, this.tfieldAddQuestion3};
        JTextArea[] answerFields = {this.tareaAddAnswer1, this.tareaAddAnswer2, this.tareaAddAnswer3};
        for (int i = 0; i < questFields.length; i++) {
            if (!questFields[i].getText().trim().isEmpty()) {
                result.add(UserManagementController.DataOrders.QUESTION_ANSWER.getValue() + tempCount, questFields[i].getText());
                result.add(UserManagementController.DataOrders.QUESTION_ANSWER.getValue() + tempCount + 1, answerFields[i].getText());
                tempCount += 2;
            }
        }

        return result;
    }

    public String[] getDataForRecovery() {
        JTextArea[] answerAreas = {this.tareaSubmitAnswer1, this.tareaSubmitAnswer2, this.tareaSubmitAnswer3};
        int count = -1;
        for (count = 0; count < answerAreas.length; count++) {
            if (!answerAreas[count].isEditable()) {
                break;
            }
        }

        String[] result = null;
        if (count > 0) {
            result = new String[count];
            for (int i = 0; i < count; i++) {
                result[i] = answerAreas[i].getText();
            }
        }

        return result;
    }
    
}
