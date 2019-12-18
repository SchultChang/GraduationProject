/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.gui;

import graduationproject.controllers.NotificationManagementController;
import graduationproject.controllers.TemplateManagementController;
import graduationproject.data.DataManager;
import graduationproject.snmpd.helpers.NotificationParser.NotificationType;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author cloud
 */
public class PanelNotificationList extends JPanel {

    private String[] tableColumns = {"Device", "Type", "Content", "Time"};

    private JCheckBox cboxAuthenticationFailure;
    private JCheckBox cboxColdStart;
    private JCheckBox cboxEGPLoss;
    private JCheckBox cboxEnterprise;
    private JCheckBox cboxLinkDown;
    private JCheckBox cboxLinkUp;
    private JCheckBox cboxWarmStart;
    private JLabel label1;
    private JLabel label2;
    private JLabel labelIconSearch;
    private JPanel panel1;
    private JScrollPane scrollpane1;
    private JTable tableNotifications;
    private JTextField tfieldSearchDevice;

    private MouseAdapter listenerTable;
    private KeyAdapter listenerField;
    private ActionListener listenerCheckbox;

    int[] notificationIds;
    int clickedRow;

    public PanelNotificationList() {
        initComponents();
        initListeners();
    }

    private void initComponents() {

        scrollpane1 = new JScrollPane();
        tableNotifications = new JTable();
        cboxColdStart = new JCheckBox();
        cboxWarmStart = new JCheckBox();
        cboxLinkUp = new JCheckBox();
        cboxLinkDown = new JCheckBox();
        cboxAuthenticationFailure = new JCheckBox();
        cboxEGPLoss = new JCheckBox();
        cboxEnterprise = new JCheckBox();
        label1 = new JLabel();
        tfieldSearchDevice = new JTextField();
        labelIconSearch = new JLabel();
        label2 = new JLabel();
        panel1 = new JPanel();

        setMaximumSize(new java.awt.Dimension(1600, 940));
        setMinimumSize(new java.awt.Dimension(1600, 940));
        setPreferredSize(new java.awt.Dimension(1600, 940));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        this.setBackground(Color.white);

        scrollpane1.setBackground(java.awt.Color.white);
        scrollpane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        tableNotifications.setBorder(null);
        tableNotifications.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        tableNotifications.setBackground(Color.white);
        tableNotifications.setModel(new DefaultTableModel(
                new Object[][]{},
                this.tableColumns
        ));
        scrollpane1.setViewportView(tableNotifications);

        add(scrollpane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(91, 142, 1395, 701));

        cboxColdStart.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        cboxColdStart.setText("Cold Start");
        cboxColdStart.setOpaque(false);
        add(cboxColdStart, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, -1, -1));

        cboxWarmStart.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        cboxWarmStart.setText("Warm Start");
        cboxWarmStart.setOpaque(false);
        add(cboxWarmStart, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, -1, -1));

        cboxLinkUp.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        cboxLinkUp.setText("Link Up");
        cboxLinkUp.setOpaque(false);
        add(cboxLinkUp, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 80, -1, -1));

        cboxLinkDown.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        cboxLinkDown.setText("Link Down");
        cboxLinkDown.setOpaque(false);
        add(cboxLinkDown, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 80, -1, -1));

        cboxAuthenticationFailure.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        cboxAuthenticationFailure.setText("Authentication Failure");
        cboxAuthenticationFailure.setOpaque(false);
        add(cboxAuthenticationFailure, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 80, -1, -1));

        cboxEGPLoss.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        cboxEGPLoss.setText("EGP Loss");
        cboxEGPLoss.setOpaque(false);
        add(cboxEGPLoss, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 80, -1, -1));

        cboxEnterprise.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        cboxEnterprise.setText("Enterprise");
        cboxEnterprise.setOpaque(false);
        add(cboxEnterprise, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 80, -1, -1));

        label1.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        label1.setForeground(java.awt.Color.blue);
        label1.setText("Device:");
        add(label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 50, 80, 30));

        tfieldSearchDevice.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        add(tfieldSearchDevice, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 50, 370, 30));

        labelIconSearch.setFont(new java.awt.Font("SansSerif", 0, 15)); // NOI18N
        labelIconSearch.setIcon(new ImageIcon(getClass().getResource("/resources/icon_search2_30.png"))); // NOI18N
        add(labelIconSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(1450, 50, 30, 30));

        label2.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        label2.setForeground(java.awt.Color.blue);
        label2.setText("Notification Types:");
        add(label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 50, -1, 30));

        panel1.setBackground(new Color(178, 205, 247));
        add(panel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1600, 130));
    }

    public void initViewData() {
        NotificationManagementController notificationController = new NotificationManagementController();
        List<Object> data = notificationController.getNotificationsOfDay(DataManager.getInstance().getStartTime());

        if (data == null) {
            JOptionPane.showMessageDialog(null, notificationController.getResultMessage());
            return;
        }

        this.cboxAuthenticationFailure.setSelected(true);
        this.cboxColdStart.setSelected(true);
        this.cboxWarmStart.setSelected(true);
        this.cboxLinkDown.setSelected(true);
        this.cboxLinkUp.setSelected(true);
        this.cboxEGPLoss.setSelected(true);
        this.cboxEnterprise.setSelected(true);

        this.updateNotificationList(notificationController.getNotificationIds(), data);
    }

    public void updateNotificationList(int[] notificationIds, List<Object> data) {
        this.notificationIds = notificationIds;

        DefaultTableModel tableModel = (DefaultTableModel) this.tableNotifications.getModel();
        int tempSize = tableModel.getRowCount();
        for (int i = tempSize - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }

        tempSize = data.size();
        for (int i = 0; i < tempSize; i++) {
            tableModel.addRow((Object[]) data.get(i));
        }

        this.tableNotifications.revalidate();
        this.tableNotifications.repaint();
        System.gc();
    }

    private void initListeners() {
        this.listenerCheckbox = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NotificationManagementController notificationController = new NotificationManagementController();
                List<Object> data = notificationController.getNotificationsOfDay(
                        DataManager.getInstance().getStartTime(),
                        tfieldSearchDevice.getText(), getSelectedNotificationTypes());
                if (data != null) {
                    updateNotificationList(notificationController.getNotificationIds(), data);
                }
            }
        };

        this.cboxAuthenticationFailure.addActionListener(listenerCheckbox);
        this.cboxColdStart.addActionListener(listenerCheckbox);
        this.cboxEGPLoss.addActionListener(listenerCheckbox);
        this.cboxWarmStart.addActionListener(listenerCheckbox);
        this.cboxEnterprise.addActionListener(listenerCheckbox);
        this.cboxLinkDown.addActionListener(listenerCheckbox);
        this.cboxLinkUp.addActionListener(listenerCheckbox);

        this.listenerField = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                JTextField source = (JTextField) e.getSource();
                if (source == tfieldSearchDevice) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        NotificationManagementController notificationController = new NotificationManagementController();
                        List<Object> data = notificationController.getNotificationsOfDay(
                                DataManager.getInstance().getStartTime(),
                                tfieldSearchDevice.getText(), getSelectedNotificationTypes());
                        if (data != null) {
                            updateNotificationList(notificationController.getNotificationIds(), data);
                        }

                    } else {
                        super.keyReleased(e);
                    }
                }
            }
        };
        
        this.tfieldSearchDevice.addKeyListener(listenerField);
        
        this.listenerTable = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                JTable source = (JTable) e.getSource();
                if (source == tableNotifications) {
                    if (clickedRow ==  source.getSelectedRow() &&  clickedRow > -1) {
                        ApplicationWindow.getInstance().getPanelMain().showPanelNotificationInfo(notificationIds[clickedRow]);
                        clickedRow = -1;
                    } else {
                        clickedRow = source.getSelectedRow();
                    }
                }
            }
        };
        this.tableNotifications.addMouseListener(listenerTable);
    }

    public List<Object> getSelectedNotificationTypes() {
        List<Object> result = new ArrayList<Object>();
        if (this.cboxAuthenticationFailure.isSelected()) {
            result.add(NotificationType.AUTHENTICATION_FAILURE.getName());
        }
        if (this.cboxColdStart.isSelected()) {
            result.add(NotificationType.COLD_START.getName());
        }
        if (this.cboxWarmStart.isSelected()) {
            result.add(NotificationType.WARM_START.getName());
        }
        if (this.cboxLinkDown.isSelected()) {
            result.add(NotificationType.LINK_DOWN.getName());
        }
        if (this.cboxLinkUp.isSelected()) {
            result.add(NotificationType.LINK_UP.getName());
        }
        if (this.cboxEGPLoss.isSelected()) {
            result.add(NotificationType.EGP_LOSS.getName());
        }
        if (this.cboxEnterprise.isSelected()) {
            result.add(NotificationType.ENTERPRISE.getName());
        }
        return result;
    }
}
