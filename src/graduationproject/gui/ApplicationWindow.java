/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

/**
 *
 * @author cloud
 */
public class ApplicationWindow extends JFrame {

    private final String WINDOW_TITLE = "Snmp Manager";
    private final int WINDOW_WIDTH = 1600;
    private final int WINDOW_HEIGHT = 1000;

    private JPanel panelContent;    
    private JPanel currentDisplayedPanel;

    private PanelInitial panelInitial;    
    private PanelMain panelMain;

    public enum PANELS {
        PANEL_INITIAL,
        PANEL_MAIN
    }
    
    private static ApplicationWindow instance = null;    
    
    private ApplicationWindow() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(WINDOW_TITLE);
        setSize(new java.awt.Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setResizable(false);
        
        this.panelContent = (JPanel) this.getContentPane();
        this.panelContent.setLayout(new AbsoluteLayout());
        
        this.initChildPanels();
        this.revalidate();
        this.repaint();
    }

    private void initChildPanels() {
        this.panelInitial = new PanelInitial();
        this.panelContent.add(this.panelInitial, new AbsoluteConstraints(0, 0, -1, -1));
        this.currentDisplayedPanel = this.panelInitial;
        
        this.panelMain = new PanelMain();
        this.panelMain.setVisible(false);
        this.panelMain.setEnabled(false);
//        this.panelContent.add(this.panelInitial, new AbsoluteConstraints(0, 0, -1, -1));        
    }
    
    public static ApplicationWindow getInstance() {
        if (instance == null) {
            instance = new ApplicationWindow();
        }
        return instance;
    }
    
    public void switchPanel(PANELS panelId) {
        this.panelContent.remove(this.currentDisplayedPanel);
        this.currentDisplayedPanel.setVisible(false);
        this.currentDisplayedPanel.setEnabled(false);
        
        switch (panelId) {
            case PANEL_INITIAL:
                this.displayPanel(this.panelInitial, 0, 0, -1, -1);
                break;
            case PANEL_MAIN:
                this.displayPanel(this.panelMain, 0, 0, -1, -1);
                break;
        }
        
        this.revalidate();
        this.repaint();
    }

    private void displayPanel(JPanel panel, int x, int y, int width, int height) {
        panel.setEnabled(true);
        panel.setVisible(true);
        
        this.panelContent.add(panel, new AbsoluteConstraints(x, y, width, height));
        this.currentDisplayedPanel = panel;
    }
}
