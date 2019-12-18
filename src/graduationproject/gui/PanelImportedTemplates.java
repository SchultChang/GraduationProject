/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.gui;

import graduationproject.controllers.TemplateManagementController;
import graduationproject.controllers.TemplateManagementController.DataOrders;
import graduationproject.controllers.TemplateManagementController.TemplateType;
import graduationproject.snmpd.SnmpManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import org.netbeans.lib.awtextra.AbsoluteConstraints;

/**
 *
 * @author cloud
 */
public class PanelImportedTemplates extends JPanel {

    private JButton buttonImport;
    private JLabel label1;
    private JLabel labelSearch;
    private JLabel labelTemplatesType;
    private JPanel panelTemplates;
    private JPanel panelTemplateList;
    private JScrollPane scrollpane1;
    private JTextField tfieldSearch;

    private PanelItemInfo panelItemInfo;
    private PanelTemplateInfo panelTemplateInfo;

    private ActionListener listenerButton;
    private KeyAdapter listenerField;
    private MouseAdapter listenerListTemplates;

    private LabelTemplate chosenLabelTemplate;
    private List<LabelTemplate> labelTemplates;

    private DataOrders currentDataOrder;
    private boolean isSingular;

    private final int LABEL_HOVER = 1;
    private final int LABEL_CLICK = 2;

    public PanelImportedTemplates() {
        initComponents();
        initListeners();
        initOtherComponents();
    }

    private void initComponents() {
        panelTemplates = new JPanel();
        tfieldSearch = new JTextField();
        scrollpane1 = new JScrollPane();
        panelTemplateList = new JPanel();
        labelSearch = new JLabel();
        buttonImport = new JButton();
        label1 = new JLabel();
        labelTemplatesType = new JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        setBackground(Color.white);
        this.setPreferredSize(new Dimension(1600, 940));

        panelTemplates.setBackground(new java.awt.Color(20, 51, 125));
        panelTemplates.setForeground(java.awt.Color.white);
        panelTemplates.setPreferredSize(new java.awt.Dimension(280, 940));
        panelTemplates.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tfieldSearch.setFont(new java.awt.Font("SansSerif", 0, 15)); // NOI18N
        tfieldSearch.setForeground(java.awt.Color.white);
        tfieldSearch.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, java.awt.Color.white));
        tfieldSearch.setCaretColor(java.awt.Color.white);
        tfieldSearch.setOpaque(false);
        panelTemplates.add(tfieldSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 310, 30));

        scrollpane1.setBackground(java.awt.Color.white);
        scrollpane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panelTemplateList.setBackground(java.awt.Color.white);
        panelTemplateList.setPreferredSize(new java.awt.Dimension(370, 738));
        panelTemplateList.setLayout(new BoxLayout(panelTemplateList, BoxLayout.PAGE_AXIS));
        scrollpane1.setViewportView(panelTemplateList);

        panelTemplates.add(scrollpane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 360, 690));

        labelSearch.setHorizontalAlignment(SwingConstants.CENTER);
        labelSearch.setIcon(new ImageIcon(getClass().getResource("/resources/icon_search_40.png"))); // NOI18N
        panelTemplates.add(labelSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 40, 50, 30));

        buttonImport.setBackground(new java.awt.Color(38, 56, 163));
        buttonImport.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        buttonImport.setForeground(java.awt.Color.white);
        buttonImport.setIcon(new ImageIcon(getClass().getResource("/resources/icon_plus_40.png"))); // NOI18N
        buttonImport.setText("Import");
        buttonImport.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        buttonImport.setBorderPainted(false);
        panelTemplates.add(buttonImport, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 830, 120, 40));

        label1.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        label1.setForeground(java.awt.Color.white);
        label1.setText("Templates Type:");
        panelTemplates.add(label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 830, -1, 40));

        labelTemplatesType.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        labelTemplatesType.setForeground(java.awt.Color.white);
        labelTemplatesType.setText(". . .");
        panelTemplates.add(labelTemplatesType, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 830, 100, 40));

        add(panelTemplates, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 440, -1));

        initChildPanels();
    }

    private void initChildPanels() {
        this.panelItemInfo = new PanelItemInfo();
        this.add(this.panelItemInfo, new AbsoluteConstraints(440, 0, -1, -1));

        this.panelTemplateInfo = new PanelTemplateInfo();
        this.add(this.panelTemplateInfo, new AbsoluteConstraints(440, 0, -1, -1));
        this.hidePanelTemplateInfo();
    }

    private void initListeners() {
        this.listenerButton = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton source = (JButton) e.getSource();

                if (source == buttonImport) {
                    JFileChooser fileChooser = new JFileChooser();
                    int result = fileChooser.showOpenDialog(null);

                    if (result == JFileChooser.APPROVE_OPTION) {
                        TemplateManagementController templateController = new TemplateManagementController();

                        if (!templateController.processImportingTemplateFromFile(!isSingular, fileChooser.getSelectedFile())) {
                            JOptionPane.showMessageDialog(null, templateController.getResultMessage());
                        } else {
                            initViewData();
                        }
                    }

                }
            }
        };
        this.buttonImport.addActionListener(this.listenerButton);

        this.listenerField = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                JTextField source = (JTextField) e.getSource();
                if (source == tfieldSearch) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        TemplateManagementController templateController = new TemplateManagementController();
                        List<String> data = templateController.processSearchingTemplates(!isSingular, currentDataOrder, tfieldSearch.getText());

                        hidePanelTemplateInfo();
                        updateTemplateList(templateController.getTemplateIds(), data);
                    } else {
                        super.keyReleased(e);
                    }
                }
            }
        };
        this.tfieldSearch.addKeyListener(this.listenerField);

        this.listenerListTemplates = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                LabelTemplate source = (LabelTemplate) e.getSource();
                source.switchBackground(LABEL_HOVER);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                LabelTemplate source = (LabelTemplate) e.getSource();

                hidePanelItemInfo();
                showPanelTemplateInfo();
                panelTemplateInfo.initViewData(source.getTemplateId());

                if (source != chosenLabelTemplate) {
                    if (chosenLabelTemplate != null) {
                        chosenLabelTemplate.switchBackground(-LABEL_CLICK);
                    }

                    source.switchBackground(LABEL_CLICK);
                    chosenLabelTemplate = source;
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                LabelTemplate source = (LabelTemplate) e.getSource();
                if (source != chosenLabelTemplate) {
                    source.switchBackground(-LABEL_HOVER);
                }
            }
        };
    }

    private void initOtherComponents() {
        this.labelTemplates = new ArrayList<LabelTemplate>();
    }

    public void initViewData() {
        hidePanelTemplateInfo();
        hidePanelItemInfo();
        initTemplateList();
    }

    private void initTemplateList() {
        this.currentDataOrder = DataOrders.NAME;

        TemplateManagementController templateController = new TemplateManagementController();
        List<String> data = templateController.processGettingImportedTemplates(this.currentDataOrder, !this.isSingular);

        if (data == null) {
            JOptionPane.showMessageDialog(null, templateController.getResultMessage());
            return;
        }

        this.updateTemplateList(templateController.getTemplateIds(), data);
    }

    public void updateTemplateList(int[] templateIds, List<String> data) {
        this.panelTemplateList.removeAll();
        this.labelTemplates.clear();
        System.gc();

        if (templateIds != null && data != null) {
            for (int i = 0; i < templateIds.length; i++) {
                LabelTemplate newLabel = new LabelTemplate(data.get(i), templateIds[i]);
                newLabel.addMouseListener(this.listenerListTemplates);
                this.labelTemplates.add(newLabel);
                this.panelTemplateList.add(newLabel);
            }
        }

        this.panelTemplateList.revalidate();
        this.panelTemplateList.repaint();
    }

    public void showPanelTemplateInfo() {
        if (!this.panelTemplateInfo.isVisible()) {
            this.panelTemplateInfo.setVisible(true);
            this.panelTemplateInfo.setEnabled(true);

            this.revalidate();
            this.repaint();
        }
    }

    public void showPanelItemInfo() {
        if (!this.panelItemInfo.isVisible()) {
            this.panelItemInfo.setEnabled(true);
            this.panelItemInfo.setVisible(true);

            this.revalidate();
            this.repaint();
        }
    }

    public void hidePanelTemplateInfo() {
        if (this.panelTemplateInfo.isVisible()) {
            this.panelTemplateInfo.setVisible(false);
            this.panelTemplateInfo.setEnabled(false);

            this.revalidate();
            this.repaint();
        }
    }

    public void hidePanelItemInfo() {
        if (this.panelItemInfo.isVisible()) {
            this.panelItemInfo.setVisible(false);
            this.panelItemInfo.setEnabled(false);

            this.revalidate();
            this.repaint();
        }
    }

    public void setIsSingular(boolean isSingular) {
        this.isSingular = isSingular;
        if (this.isSingular) {
            this.labelTemplatesType.setText(TemplateType.SINGULAR.getValue());
        } else {
            this.labelTemplatesType.setText(TemplateType.TABULAR.getValue());
        }
    }

    public PanelItemInfo getPanelItemInfo() {
        return panelItemInfo;
    }

    public PanelTemplateInfo getPanelTemplateInfo() {
        return panelTemplateInfo;
    }

    public class LabelTemplate extends JLabel {

        private int templateId;

        public LabelTemplate(String text, int templateId) {
            super(text);
            this.templateId = templateId;

            this.setPreferredSize(new Dimension(400, 60));
            this.setMinimumSize(new Dimension(400, 60));
            this.setMaximumSize(new Dimension(400, 60));

            this.setFont(new java.awt.Font("SansSerif", 1, 16));
            this.setIconTextGap(10);

            this.setOpaque(true);
            this.setBackground(Color.white);
            this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(60, 60, 60)));

            this.setVisible(true);
            this.setEnabled(true);
        }

        public int getTemplateId() {
            return templateId;
        }

        public void switchBackground(int action) {
            if (action == LABEL_HOVER || action == LABEL_CLICK) {
                this.setBackground(new Color(194, 217, 255));
            } else {
                this.setBackground(Color.WHITE);
            }
        }
    }
}
