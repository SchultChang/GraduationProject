/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.gui;

import graduationproject.controllers.DeviceManagementController;
import graduationproject.controllers.TemplateItemManagementController;
import graduationproject.controllers.TemplateManagementController;
import graduationproject.controllers.TemplateManagementController.DataOrders;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author cloud
 */
public class PanelTemplateInfo extends JPanel {

    private JButton buttonCancel;
    private JButton buttonSave;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel labelImportedTime;
    private JPanel panelTemplateInfo;
    private JScrollPane scrollPane1;
    private JScrollPane scrollpane1;
    private JTable tableItems;
    private JTextArea tareaDescription;
    private JTextField tfieldTemplateName;

    private String[] defaultColumns = {"Display Name", "MIB Name", "Object Id",  "Enable"};
    
    private ActionListener listenerButton;
    private MouseAdapter listenerTable;
    
    private int clickedRow = 0;
    private int templateId;
    
    public PanelTemplateInfo() {
        initComponents();
        initListeners();
        initOtherComponents();
    }
    
    private void initComponents() {
        panelTemplateInfo = new JPanel();
        label1 = new JLabel();
        tfieldTemplateName = new JTextField();
        label2 = new JLabel();
        label3 = new JLabel();
        labelImportedTime = new JLabel();
        scrollPane1 = new JScrollPane();
        tareaDescription = new JTextArea();
        label4 = new JLabel();
        scrollpane1 = new JScrollPane();
        tableItems = new JTable();
        buttonCancel = new JButton();
        buttonSave = new JButton();

        setBackground(java.awt.Color.white);
        setPreferredSize(new java.awt.Dimension(1160, 940));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelTemplateInfo.setBackground(new java.awt.Color(140, 198, 237));
        panelTemplateInfo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label1.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        label1.setText("Template Name:");
        panelTemplateInfo.add(label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 40, -1, 40));

        tfieldTemplateName.setFont(new java.awt.Font("SansSerif", 0, 15)); 
        tfieldTemplateName.setText(". . .");
        tfieldTemplateName.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelTemplateInfo.add(tfieldTemplateName, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 40, 420, 40));

        label2.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        label2.setText("Template Description:");
        panelTemplateInfo.add(label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 100, -1, 40));

        label3.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        label3.setText("Imported Time:");
        panelTemplateInfo.add(label3, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 200, -1, 40));

        labelImportedTime.setFont(new java.awt.Font("SansSerif", 0, 15)); 
        labelImportedTime.setText(". . .");
        panelTemplateInfo.add(labelImportedTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 200, 420, 40));

        scrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        tareaDescription.setColumns(20);
        tareaDescription.setFont(new java.awt.Font("SansSerif", 0, 15)); 
        tareaDescription.setLineWrap(true);
        tareaDescription.setRows(4);
        tareaDescription.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane1.setViewportView(tareaDescription);

        panelTemplateInfo.add(scrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 100, 420, 90));

        add(panelTemplateInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1160, 280));

        label4.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        label4.setText("Items:");
        add(label4, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 290, -1, 40));

        tableItems.setBorder(null);
        tableItems.setFont(new java.awt.Font("SansSerif", 0, 15)); 
        tableItems.setModel(new DefaultTableModel(
            new Object [][] {
            },
            this.defaultColumns
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        scrollpane1.setViewportView(tableItems);

        add(scrollpane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 330, 950, 480));

        buttonCancel.setBackground(new java.awt.Color(73, 125, 222));
        buttonCancel.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        buttonCancel.setForeground(java.awt.Color.white);
        buttonCancel.setText("Cancel");
        buttonCancel.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        buttonCancel.setBorderPainted(false);
        add(buttonCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 850, 100, 40));

        buttonSave.setBackground(new java.awt.Color(73, 125, 222));
        buttonSave.setFont(new java.awt.Font("SansSerif", 1, 15)); 
        buttonSave.setForeground(java.awt.Color.white);
        buttonSave.setText("Save");
        buttonSave.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        buttonSave.setBorderPainted(false);
        add(buttonSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 850, 100, 40));

    }
    
    private void initListeners() {
        this.listenerButton = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton source = (JButton) e.getSource();
                if (source == buttonSave) {
                    TemplateManagementController templateController = new TemplateManagementController();
                    if (!templateController.processSavingTemplateInfo(templateId, getDataForUpdate())) {
                        JOptionPane.showMessageDialog(null, templateController.getResultMessage());
                    }
                }
                if (source == buttonCancel) {
                    initData(templateId);
                }
            }
            
        };
        
        this.buttonCancel.addActionListener(this.listenerButton);
        this.buttonSave.addActionListener(this.listenerButton);
        
        this.listenerTable = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                JTable source = (JTable) e.getSource();
                if (source == tableItems) {
                    if (clickedRow ==  source.getSelectedRow()) {
                        ApplicationWindow.getInstance().getPanelMain().getPanelImportedTemplates().showPanelItemInfo();
                        ApplicationWindow.getInstance().getPanelMain().getPanelImportedTemplates().getPanelItemInfo().initData(templateId, clickedRow);
                        clickedRow = -1;
                    } else {
                        clickedRow = source.getSelectedRow();
                    }
                }
            }
        };
        this.tableItems.addMouseListener(listenerTable);
    }
    
    private void initOtherComponents() { 
        this.clickedRow = -1;
    }
    
    public void initData(int templateId) {
        this.templateId = templateId;
        
        TemplateManagementController templateController = new TemplateManagementController();
        List<Object> data = templateController.processGettingTemplateInfo(templateId);
        
        if (data == null) {
            JOptionPane.showMessageDialog(null, templateController.getResultMessage());
            return;
        }
        
        this.updateView(data);
    }
    
    public void updateView(List<Object> data) {
        this.tfieldTemplateName.setText((String) data.get(DataOrders.NAME.getValue()));
        this.tareaDescription.setText((String) data.get(DataOrders.DESCRIPTION.getValue()));
        this.labelImportedTime.setText((String) data.get(DataOrders.IMPORTED_TIME.getValue()));
        
        DefaultTableModel tableModel = (DefaultTableModel) this.tableItems.getModel();
        int rowCount = tableModel.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            tableModel.removeRow(i);            
        }        
        
        int tempSize = data.size();
        for (int i = DataOrders.EXTRA.getValue(); i < tempSize; i++) {
            List<Object> item = (List<Object>) data.get(i);
            tableModel.addRow(new Object[]{
                item.get(TemplateItemManagementController.DataOrders.DISPLAY_NAME.getValue()),
                item.get(TemplateItemManagementController.DataOrders.MIB_NAME.getValue()),
                item.get(TemplateItemManagementController.DataOrders.OID.getValue()),
                item.get(TemplateItemManagementController.DataOrders.IS_ENABLED.getValue())
            });
        }            
        
        this.tableItems.revalidate();
        this.tableItems.repaint();
    }
    
    public List<Object> getDataForUpdate() {
        List<Object> result = new ArrayList<Object>();
        result.add(DataOrders.NAME.getValue(), this.tfieldTemplateName.getText());
        result.add(DataOrders.DESCRIPTION.getValue(), this.tareaDescription.getText());
        result.add(DataOrders.SNMP_VERSION.getValue(), new String());
        result.add(DataOrders.IMPORTED_TIME.getValue(), this.labelImportedTime.getText());
        
        DefaultTableModel tableModel = (DefaultTableModel) this.tableItems.getModel();
        
        int rowCount = tableModel.getRowCount();
        int[] colOrder = {1, 2, 0, 3};
        
        for (int i = 0; i < rowCount; i++) {
            List<Object> item = new ArrayList<Object>();
            for (int j = 0; j < colOrder.length; j++) {
                item.add(j, tableModel.getValueAt(i, colOrder[j]));                
            }
            
            result.add(item);
        }
        
        return result;
    }
}
