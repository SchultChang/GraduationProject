/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.gui;

import graduationproject.controllers.DeviceManagementController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

/**
 *
 * @author cloud
 */
public class PanelImportedDevices extends JPanel {

    private JButton buttonImport;
    private JLabel labelSearch;
    private JPanel panelDeviceList;
    private JPanel panelDevices;
    private JScrollPane scrollpane1;
    private JTextField tfieldSearch;

    private ActionListener listenerButton;
    
    public PanelImportedDevices() {
        initComponents();
        initListeners();
    }

    private void initComponents() {

        panelDevices = new JPanel();
        tfieldSearch = new JTextField();
        scrollpane1 = new JScrollPane();
        panelDeviceList = new JPanel();
        labelSearch = new JLabel();
        buttonImport = new JButton();

        setPreferredSize(new java.awt.Dimension(1600, 940));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelDevices.setBackground(new java.awt.Color(20, 51, 125));
        panelDevices.setPreferredSize(new java.awt.Dimension(280, 940));
        panelDevices.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tfieldSearch.setFont(new java.awt.Font("SansSerif", 0, 15)); // NOI18N
        tfieldSearch.setForeground(java.awt.Color.white);
        tfieldSearch.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, java.awt.Color.white));
        tfieldSearch.setCaretColor(java.awt.Color.white);
        tfieldSearch.setOpaque(false);
        panelDevices.add(tfieldSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 310, 30));

        scrollpane1.setBackground(java.awt.Color.white);
        scrollpane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panelDeviceList.setBackground(java.awt.Color.white);
        panelDeviceList.setPreferredSize(new java.awt.Dimension(370, 738));
        panelDeviceList.setLayout(new BoxLayout(panelDeviceList, BoxLayout.PAGE_AXIS));
        scrollpane1.setViewportView(panelDeviceList);

        panelDevices.add(scrollpane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 360, 690));

        labelSearch.setHorizontalAlignment(SwingConstants.CENTER);
        labelSearch.setIcon(new ImageIcon(getClass().getResource("/resources/icon_search_40.png"))); // NOI18N
        panelDevices.add(labelSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 40, 50, 30));

        buttonImport.setBackground(new java.awt.Color(38, 56, 163));
        buttonImport.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        buttonImport.setForeground(java.awt.Color.white);
        buttonImport.setIcon(new ImageIcon(getClass().getResource("/resources/icon_plus_40.png"))); // NOI18N
        buttonImport.setText("Import");

        buttonImport.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        buttonImport.setBorderPainted(false);
        panelDevices.add(buttonImport, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 830, 120, 40));

        add(panelDevices, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 440, -1));

    }
    
    private void initListeners() {
        this.listenerButton = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                
                if (result == JFileChooser.APPROVE_OPTION) {
                    DeviceManagementController deviceController = new DeviceManagementController();
                    File file = fileChooser.getSelectedFile();

                    if (!deviceController.processImportingDevicesFromFile(file)) {
                        JOptionPane.showMessageDialog(null, deviceController.getResultMessage());
                    }
                }
            }
            
        };
        this.buttonImport.addActionListener(this.listenerButton);
                
    }
}

