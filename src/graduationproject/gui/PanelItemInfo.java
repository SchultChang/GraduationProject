/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.gui;

import graduationproject.controllers.TemplateItemManagementController;
import graduationproject.controllers.TemplateItemManagementController.DataOrders;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author cloud
 */
public class PanelItemInfo extends JPanel{

    private JButton buttonCancel;
    private JButton buttonSave;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JLabel label6;
    private JLabel labelAccessType;
    private JLabel labelIconBack;
    private JLabel labelMIBName;
    private JLabel labelOID;
    private JLabel labelValueType;
    private JPanel panelItemInfo;
    private JPanel panelOverlay;
    private JScrollPane scrollpane1;
    private JTextArea tareaDescription;
    private JTextField tfieldDisplayName;

    private ActionListener listenerButton;
    private MouseAdapter listenerLabel;
    
    private int templateId;
    private int itemListId;
    
    public PanelItemInfo() {
        initComponents();
        initListeners();
    }
    
    private void initComponents() {

        panelOverlay = new JPanel();
        panelItemInfo = new JPanel();
        label1 = new JLabel();
        tfieldDisplayName = new JTextField();
        label2 = new JLabel();
        label3 = new JLabel();
        labelValueType = new JLabel();
        label4 = new JLabel();
        labelAccessType = new JLabel();
        label5 = new JLabel();
        label6 = new JLabel();
        scrollpane1 = new JScrollPane();
        tareaDescription = new JTextArea();
        buttonCancel = new JButton();
        buttonSave = new JButton();
        labelIconBack = new JLabel();
        labelMIBName = new JLabel();
        labelOID = new JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        setPreferredSize(new Dimension(1160, 940));
        setBackground(Color.white);
        
        panelOverlay.setBackground(new Color(0, 0, 0, 150));
        panelOverlay.setPreferredSize(new java.awt.Dimension(1160, 940));
        panelOverlay.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelItemInfo.setBackground(java.awt.Color.white);
        panelItemInfo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label1.setFont(new java.awt.Font("SansSerif", 1, 14)); 
        label1.setText("Display Name:");
        panelItemInfo.add(label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 100, -1, 30));

        tfieldDisplayName.setFont(new java.awt.Font("SansSerif", 0, 14)); 
        tfieldDisplayName.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelItemInfo.add(tfieldDisplayName, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 100, 360, 30));

        label2.setFont(new java.awt.Font("SansSerif", 1, 14)); 
        label2.setText("MIB Name:");
        panelItemInfo.add(label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 160, -1, 30));

        label3.setFont(new java.awt.Font("SansSerif", 1, 14)); 
        label3.setText("Object ID:");
        panelItemInfo.add(label3, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 220, -1, 30));

        labelValueType.setFont(new java.awt.Font("SansSerif", 0, 14)); 
        labelValueType.setText(". . .");
        panelItemInfo.add(labelValueType, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 280, 320, 30));

        label4.setFont(new java.awt.Font("SansSerif", 1, 14)); 
        label4.setText("Value Type:");
        panelItemInfo.add(label4, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 280, -1, 30));

        labelAccessType.setFont(new java.awt.Font("SansSerif", 0, 14)); 
        labelAccessType.setText(". . .");
        panelItemInfo.add(labelAccessType, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 340, 320, 30));

        label5.setFont(new java.awt.Font("SansSerif", 1, 14)); 
        label5.setText("Description:");
        panelItemInfo.add(label5, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 400, -1, 30));

        label6.setFont(new java.awt.Font("SansSerif", 1, 14)); 
        label6.setText("Access Type:");
        panelItemInfo.add(label6, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 340, -1, 30));

        tareaDescription.setColumns(20);
        tareaDescription.setFont(new java.awt.Font("SansSerif", 0, 15)); 
        tareaDescription.setLineWrap(true);
        tareaDescription.setRows(5);
        tareaDescription.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollpane1.setViewportView(tareaDescription);

        panelItemInfo.add(scrollpane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 400, 360, 160));

        buttonCancel.setBackground(new java.awt.Color(73, 125, 222));
        buttonCancel.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        buttonCancel.setForeground(java.awt.Color.white);
        buttonCancel.setText("Cancel");
        panelItemInfo.add(buttonCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 590, 90, 40));

        buttonSave.setBackground(new java.awt.Color(73, 125, 222));
        buttonSave.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        buttonSave.setForeground(java.awt.Color.white);
        buttonSave.setText("Save");
        panelItemInfo.add(buttonSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 590, 90, 40));

        labelIconBack.setHorizontalAlignment(SwingConstants.CENTER);
        labelIconBack.setIcon(new ImageIcon(getClass().getResource("/resources/icon_arrow_back80.png"))); 
        panelItemInfo.add(labelIconBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 290, -1, -1));

        labelMIBName.setFont(new java.awt.Font("SansSerif", 0, 14)); 
        labelMIBName.setText(". . .");
        panelItemInfo.add(labelMIBName, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 160, 320, 30));

        labelOID.setFont(new java.awt.Font("SansSerif", 0, 14)); 
        labelOID.setText(". . .");
        panelItemInfo.add(labelOID, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 220, 320, 30));

        panelOverlay.add(panelItemInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 110, 710, 680));

        add(panelOverlay, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
        
    }
    
    public void initListeners() {
        this.listenerButton = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton source = (JButton) e.getSource();
                if (source == buttonSave) {
                    TemplateItemManagementController itemController = new TemplateItemManagementController();
                    if (!itemController.processSavingTemplateItemInfo(templateId, itemListId, getDataForUpdate()))
                        JOptionPane.showMessageDialog(null, itemController.getResultMessage());
                }
                if (source == buttonCancel) {
                    initData(templateId, itemListId);
                }
            }
            
        };
        this.buttonSave.addActionListener(this.listenerButton);
        this.buttonCancel.addActionListener(this.listenerButton);
        
        this.listenerLabel = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                JLabel source = (JLabel) e.getSource();
                if (source == labelIconBack) {
                    ApplicationWindow.getInstance().getPanelMain().getPanelImportedTemplates().hidePanelItemInfo();
                }
            }
        };
        this.labelIconBack.addMouseListener(this.listenerLabel);
    }
    
    public void initData(int templateId, int itemListId) {
        this.templateId = templateId;
        this.itemListId = itemListId;
        
        TemplateItemManagementController itemController = new TemplateItemManagementController();
        List<Object> data = itemController.processGettingTemplateItemInfo(templateId, itemListId);
        if (data == null) {
            JOptionPane.showMessageDialog(null, itemController.getResultMessage());
        }
        
        this.updateView(data);
    }
    
    public void updateView(List<Object> data) {
        this.labelMIBName.setText((String) data.get(DataOrders.MIB_NAME.getValue()));
        this.labelOID.setText((String) data.get(DataOrders.OID.getValue()));
        this.tfieldDisplayName.setText((String) data.get(DataOrders.DISPLAY_NAME.getValue())); 
        this.tareaDescription.setText((String) data.get(DataOrders.DESCRIPTION.getValue()));
        this.labelValueType.setText((String) data.get(DataOrders.VALUE_TYPE.getValue()));
        this.labelAccessType.setText((String) data.get(DataOrders.ACCESS_TYPE.getValue()));
    }
   
    public List<Object> getDataForUpdate() {
        List<Object> result = new ArrayList<Object>();
        result.add(DataOrders.MIB_NAME.getValue(), this.labelMIBName.getText());
        result.add(DataOrders.OID.getValue(), this.labelOID.getText());
        result.add(DataOrders.DISPLAY_NAME.getValue(), this.tfieldDisplayName.getText());
        result.add(DataOrders.DESCRIPTION.getValue(), this.tareaDescription.getText());
        result.add(DataOrders.VALUE_TYPE.getValue(), this.labelValueType.getText());
        result.add(DataOrders.ACCESS_TYPE.getValue(), this.labelAccessType.getText());
        return result;
    }    
}
