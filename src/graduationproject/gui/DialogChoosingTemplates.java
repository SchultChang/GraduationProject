/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.gui;

import graduationproject.controllers.DeviceManagementController;
import graduationproject.controllers.DeviceManagementController.DeviceStates;
import graduationproject.controllers.TemplateManagementController;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 *
 * @author cloud
 */
public class DialogChoosingTemplates extends JDialog {

    private String DEVICE_NOT_ACTIVE_WARNING = "Device is not active so you can't monitor its data now. Please try again later.";
    
    private JButton buttonCancel;
    private JButton buttonContinue;
    private JButton buttonSingular;
    private JButton buttonTabular;
    private JLabel labelIcon1;
    private JList<String> listTemplates;
    private JScrollPane scrollpane1;
    private JTextField tfieldSearch;

    private Color CLICK_COLOR = new Color(152, 181, 245);
    private Color NORMAL_COLOR = new Color(66, 108, 245);
    private Border CLICK_BORDER = javax.swing.BorderFactory.createMatteBorder(1, 1, 0, 1, new Color(0, 0, 0));
    private Border NORMAL_BORDER = javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0, 0, 0));

    private ActionListener listenerButton;
    private KeyAdapter listenerField;

    private int[] templateIds;
    private boolean isSingular;

    public DialogChoosingTemplates() {
        initComponents();
        initListeners();
    }

    private void initComponents() {
        scrollpane1 = new javax.swing.JScrollPane();
        listTemplates = new javax.swing.JList<>();
        buttonCancel = new javax.swing.JButton();
        buttonTabular = new javax.swing.JButton();
        buttonContinue = new javax.swing.JButton();
        buttonSingular = new javax.swing.JButton();
        tfieldSearch = new javax.swing.JTextField();
        labelIcon1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(java.awt.Color.white);
        setMaximumSize(new java.awt.Dimension(444, 615));
        setMinimumSize(new java.awt.Dimension(444, 615));
        setModal(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        listTemplates.setFont(new java.awt.Font("SansSerif", 0, 15)); // NOI18N
        listTemplates.setModel(new DefaultListModel());
        scrollpane1.setViewportView(listTemplates);

        getContentPane().add(scrollpane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 130, 320, 380));

        buttonCancel.setBackground(new java.awt.Color(66, 108, 245));
        buttonCancel.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        buttonCancel.setForeground(java.awt.Color.white);
        buttonCancel.setText("Cancel");
        buttonCancel.setBorderPainted(false);
        getContentPane().add(buttonCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 540, 90, -1));

        buttonTabular.setBackground(new java.awt.Color(152, 181, 245));
        buttonTabular.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        buttonTabular.setForeground(java.awt.Color.white);
        buttonTabular.setText("Tabular");
        buttonTabular.setBorder(NORMAL_BORDER);
        getContentPane().add(buttonTabular, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, 80, 30));

        buttonContinue.setBackground(new java.awt.Color(66, 108, 245));
        buttonContinue.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        buttonContinue.setForeground(java.awt.Color.white);
        buttonContinue.setText("Continue");
        buttonContinue.setBorderPainted(false);
        getContentPane().add(buttonContinue, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 540, -1, -1));

        buttonSingular.setBackground(new java.awt.Color(66, 108, 245));
        buttonSingular.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        buttonSingular.setForeground(java.awt.Color.white);
        buttonSingular.setText("Singular");
        buttonSingular.setBorder(NORMAL_BORDER);
        getContentPane().add(buttonSingular, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 100, 80, 30));

        tfieldSearch.setFont(new java.awt.Font("SansSerif", 0, 15)); // NOI18N
        getContentPane().add(tfieldSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 280, 30));

        labelIcon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icon_search2_30.png"))); // NOI18N
        getContentPane().add(labelIcon1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 50, 40, 30));

        pack();
    }

    private void initListeners() {
        this.listenerButton = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton source = (JButton) e.getSource();
                if (source == buttonCancel) {
                    DialogChoosingTemplates.this.dispose();
                }
                if (source == buttonContinue) {
                    DialogChoosingTemplates.this.dispose();
                    if (ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices().getSelectedDeviceStates()
                            == DeviceStates.ACTIVE) {
                        ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices()
                                .switchDisplayedPanel(PanelImportedDevices.PANELS.PANEL_MONITORING_DEVICE);
                        ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices().getPanelMonitoringDevice().initData(
                                ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices().getSelectedDeviceId(),
                                DialogChoosingTemplates.this.templateIds[listTemplates.getSelectedIndex()]);
                    } else {
                        JOptionPane.showMessageDialog(null, DEVICE_NOT_ACTIVE_WARNING);
                    }
                }

                if (source == buttonSingular) {
                    isSingular = true;

                    TemplateManagementController templateController = new TemplateManagementController();
                    List<String> data = templateController.processSearchingTemplates(
                            !isSingular, TemplateManagementController.DataOrders.NAME, tfieldSearch.getText());

                    buttonSingular.setBackground(CLICK_COLOR);
                    buttonSingular.setBorder(CLICK_BORDER);
                    buttonTabular.setBackground(NORMAL_COLOR);
                    buttonTabular.setBorder(NORMAL_BORDER);

                    updateTemplateList(templateController.getTemplateIds(), data);
                }
                if (source == buttonTabular) {
                    isSingular = false;

                    TemplateManagementController templateController = new TemplateManagementController();
                    List<String> data = templateController.processSearchingTemplates(
                            !isSingular, TemplateManagementController.DataOrders.NAME, tfieldSearch.getText());

                    buttonSingular.setBackground(NORMAL_COLOR);
                    buttonSingular.setBorder(NORMAL_BORDER);
                    buttonTabular.setBackground(CLICK_COLOR);
                    buttonTabular.setBorder(CLICK_BORDER);

                    updateTemplateList(templateController.getTemplateIds(), data);
                }
            }

        };
        this.buttonSingular.addActionListener(listenerButton);
        this.buttonTabular.addActionListener(listenerButton);
        this.buttonContinue.addActionListener(listenerButton);
        this.buttonCancel.addActionListener(listenerButton);

        this.listenerField = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                JTextField source = (JTextField) e.getSource();
                if (source == tfieldSearch) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        TemplateManagementController templateController = new TemplateManagementController();
                        List<String> data = templateController.processSearchingTemplates(!isSingular, TemplateManagementController.DataOrders.NAME,
                                tfieldSearch.getText());

                        updateTemplateList(templateController.getTemplateIds(), data);
                    } else {
                        super.keyReleased(e);
                    }
                }

            }
        };
        this.tfieldSearch.addKeyListener(listenerField);
    }

    public void initData() {
        this.isSingular = true;
        buttonSingular.setBorder(CLICK_BORDER);
        buttonSingular.setBackground(CLICK_COLOR);
        buttonTabular.setBorder(NORMAL_BORDER);
        buttonTabular.setBackground(NORMAL_COLOR);

        TemplateManagementController templateController = new TemplateManagementController();
        List<String> data = templateController.processGettingImportedTemplates(TemplateManagementController.DataOrders.NAME, !isSingular);

        this.updateTemplateList(templateController.getTemplateIds(), data);
    }

    public void updateTemplateList(int[] templateIds, List<String> data) {
        this.templateIds = templateIds;

        DefaultListModel listModel = (DefaultListModel) this.listTemplates.getModel();
        listModel.removeAllElements();

        if (data != null) {
            int tempSize = data.size();
            for (int i = 0; i < tempSize; i++) {
                listModel.addElement(data.get(i));
            }
        }

        this.revalidate();
        this.repaint();
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            this.initData();
        }
        super.setVisible(visible);
    }

}
