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

    private JPanel contentPanel;    
    private PanelInitial panelInitial;    
    
    private static ApplicationWindow instance = null;    
    
    private ApplicationWindow() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(WINDOW_TITLE);
        setSize(new java.awt.Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setResizable(false);
        
        this.contentPanel = (JPanel) this.getContentPane();
        this.contentPanel.setLayout(new AbsoluteLayout());
        
        this.initChildPanels();
        this.revalidate();
        this.repaint();
    }

    private void initChildPanels() {
        this.panelInitial = new PanelInitial();
        this.contentPanel.add(this.panelInitial, new AbsoluteConstraints(0, 0, -1, -1));
    }
    
    public static ApplicationWindow getInstance() {
        if (instance == null) {
            instance = new ApplicationWindow();
        }
        return instance;
    }
    
    

}
