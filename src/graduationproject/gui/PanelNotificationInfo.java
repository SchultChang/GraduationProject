/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.gui;

import graduationproject.controllers.NotificationManagementController;
import graduationproject.controllers.NotificationManagementController.DataOrders;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author cloud
 */
public class PanelNotificationInfo extends JPanel {

    private String[] extraDataColumns = {"Name", "Value"};

    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JLabel label6;
    private JLabel labelClose;
    private JLabel labelCommunity;
    private JLabel labelDeviceLabel;
    private JLabel labelExtra;
    private JLabel labelIPAddress;
    private JLabel labelType;
    private JLabel labelTime;
    private JPanel panelNotificationData;
    private JPanel panelOverlay;
    private JScrollPane scrollpane1;
    private JScrollPane scrollpane2;
    private JTable tableExtraData;
    private JTextArea tareaContent;
    
    private MouseAdapter listenerLabel;    

    public PanelNotificationInfo() {
        initComponents();
        initListeners();
    }

    private void initComponents() {

        panelOverlay = new JPanel();
        panelNotificationData = new JPanel();
        label1 = new JLabel();
        labelIPAddress = new JLabel();
        label2 = new JLabel();
        labelDeviceLabel = new JLabel();
        label3 = new JLabel();
        labelCommunity = new JLabel();
        labelType = new JLabel();
        label4 = new JLabel();
        labelExtra = new JLabel();
        scrollpane1 = new JScrollPane();
        tareaContent = new JTextArea();
        label5 = new JLabel();
        labelClose = new JLabel();
        scrollpane2 = new JScrollPane();
        tableExtraData = new JTable();
        label6 = new JLabel();
        labelTime = new JLabel();

        setMaximumSize(new java.awt.Dimension(1600, 940));
        setMinimumSize(new java.awt.Dimension(1600, 940));
        setPreferredSize(new java.awt.Dimension(1600, 940));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelOverlay.setBackground(new Color(0, 0, 0, 50));
        panelOverlay.setPreferredSize(new java.awt.Dimension(1600, 940));
        panelOverlay.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelNotificationData.setBackground(java.awt.Color.white);
        panelNotificationData.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label1.setFont(new java.awt.Font("SansSerif", 1, 16)); 
        label1.setText("IP Address");
        panelNotificationData.add(label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 110, 110, 30));

        labelIPAddress.setFont(new java.awt.Font("SansSerif", 0, 16)); 
        labelIPAddress.setText(". . .");
        panelNotificationData.add(labelIPAddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 110, 330, 30));

        label2.setFont(new java.awt.Font("SansSerif", 1, 16)); 
        label2.setText("Device Label:");
        panelNotificationData.add(label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(78, 63, 130, 30));

        labelDeviceLabel.setFont(new java.awt.Font("SansSerif", 0, 16)); 
        labelDeviceLabel.setText(". . .");
        panelNotificationData.add(labelDeviceLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(206, 63, 340, 30));

        label3.setFont(new java.awt.Font("SansSerif", 1, 16)); 
        label3.setText("Community");
        panelNotificationData.add(label3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 160, 110, 30));

        labelCommunity.setFont(new java.awt.Font("SansSerif", 0, 16)); 
        labelCommunity.setText(". . .");
        panelNotificationData.add(labelCommunity, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 160, 330, 30));

        labelType.setFont(new java.awt.Font("SansSerif", 0, 16)); 
        labelType.setText(". . .");
        panelNotificationData.add(labelType, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 210, 330, 30));

        label4.setFont(new java.awt.Font("SansSerif", 1, 16)); 
        label4.setText("Type:");
        panelNotificationData.add(label4, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 210, 110, 30));

        labelExtra.setFont(new java.awt.Font("SansSerif", 1, 16)); 
        labelExtra.setText("Extra Data");
        panelNotificationData.add(labelExtra, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 410, 110, 30));

        scrollpane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollpane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        tareaContent.setEditable(false);
        tareaContent.setColumns(20);
        tareaContent.setFont(new java.awt.Font("SansSerif", 0, 16)); 
        tareaContent.setLineWrap(true);
        tareaContent.setRows(5);
        scrollpane1.setViewportView(tareaContent);

        panelNotificationData.add(scrollpane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 320, 330, 80));

        label5.setFont(new java.awt.Font("SansSerif", 1, 16)); 
        label5.setText("Content:");
        panelNotificationData.add(label5, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 320, 110, 30));

        labelClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icon_close_30.png"))); 
        panelNotificationData.add(labelClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 0, -1, 30));

        scrollpane2.setBackground(java.awt.Color.white);

        tableExtraData.setBorder(null);
        tableExtraData.setFont(new java.awt.Font("SansSerif", 0, 15)); 
        tableExtraData.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                this.extraDataColumns
        ));
        scrollpane2.setViewportView(tableExtraData);

        panelNotificationData.add(scrollpane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 450, 460, 160));

        label6.setFont(new java.awt.Font("SansSerif", 1, 16)); 
        label6.setText("Time:");
        panelNotificationData.add(label6, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 260, 110, 30));

        labelTime.setFont(new java.awt.Font("SansSerif", 0, 16)); 
        labelTime.setText(". . .");
        panelNotificationData.add(labelTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 260, 330, 30));

        panelOverlay.add(panelNotificationData, new org.netbeans.lib.awtextra.AbsoluteConstraints(493, 121, 610, 680));

        add(panelOverlay, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

    }
    
    private void initListeners() {
        this.listenerLabel = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                JLabel source = (JLabel) e.getSource();
                if (source == labelExtra) {
                    scrollpane2.setVisible(!scrollpane2.isVisible());
                    
                    PanelNotificationInfo.this.revalidate();
                    PanelNotificationInfo.this.repaint();
                }
                if (source == labelClose) {
                    ApplicationWindow.getInstance().getPanelMain().hidePanelNotificationInfo();
                }
                
            }
            
        };
        this.labelExtra.addMouseListener(this.listenerLabel);
        this.labelClose.addMouseListener(this.listenerLabel);
    }
    
    public void initViewData(int notificationId) {
        NotificationManagementController notificationController = new NotificationManagementController();
        List<Object> data = notificationController.processGettingNotificationInfo(notificationId);
        if (data == null) {
            JOptionPane.showMessageDialog(null, notificationController.getResultMessage());
            return;            
        }

        this.labelDeviceLabel.setText((String) data.get(DataOrders.DEVICE_LABEL.getValue()));
        this.labelIPAddress.setText((String) data.get(DataOrders.DEVICE_ADDRESS.getValue()));
        this.labelCommunity.setText((String) data.get(DataOrders.DEVICE_COMMUNITY.getValue()));
        this.labelType.setText((String) data.get(DataOrders.TYPE.getValue()));
        this.labelTime.setText((String) data.get(DataOrders.TIME.getValue()));
        this.tareaContent.setText((String) data.get(DataOrders.CONTENT.getValue()));
        
        DefaultTableModel tableModel = (DefaultTableModel) this.tableExtraData.getModel();
        int tempSize = tableModel.getRowCount();
        for (int i = tempSize - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }

        tempSize = data.size();
        for (int i = DataOrders.EXTRA_DATA.getValue(); i < tempSize; i++) {
            tableModel.addRow((Object[]) data.get(i));
        }
        
        this.revalidate();
        this.repaint();
    }
}
