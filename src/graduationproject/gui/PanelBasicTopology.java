/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.gui;

import graduationproject.helpers.TopoDrawer;
import graduationproject.helpers.TopoDrawer.NodeTypes;
import graduationproject.helpers.TopoDrawer.TopoNodeData;
import static graduationproject.helpers.TopoDrawer.VS_DEVICE_ID;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

/**
 *
 * @author cloud
 */
public class PanelBasicTopology extends JPanel {

    private final int ICON_RADIUS = 20;
    private final String MESSAGE_FOR_REDRAWING_DIALOG = "Your topology is out of sync. Would you like to redraw it?";
    private final String MESSAGE_FOR_NOT_GETTING_DEVICE_INFO = "Sorry, getting that device information is not available.";
    private final String MESSAGE_FOR_SSH_TO_VS = "This device is a virtual switch so you coulnd't access it";

    private JPopupMenu pmenuNode;
    private JMenuItem mitemInformation;
    private JMenuItem mitemConfiguration;

    private PanelSSHClient panelSSHClient;
    
    private MouseAdapter listenerNodeClick;
    private ActionListener listenerItem;
    private MouseMotionListener listenerNodeMotion;

    private Point previousPosition;
    private LabelNode chosenLabel;

    public PanelBasicTopology() {
        initComponents();
        initListeners();
    }

    private void initComponents() {
        this.setPreferredSize(new Dimension(1560, 940));
        this.setBackground(Color.white);
        this.setLayout(new AbsoluteLayout());
        
        this.panelSSHClient = new PanelSSHClient();
        this.switchSSHClientVisibility(false);

        this.initMenu();
    }

    private void initMenu() {
        this.pmenuNode = new JPopupMenu();

        this.mitemInformation = new JMenuItem("Info");
        this.pmenuNode.add(this.mitemInformation);

        this.mitemConfiguration = new JMenuItem("Configure");
        this.pmenuNode.add(this.mitemConfiguration);
    }

    private void initListeners() {
        this.listenerNodeMotion = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (chosenLabel != null) {
                    int newx = chosenLabel.getLocation().x + e.getLocationOnScreen().x - previousPosition.x;
                    int newy = chosenLabel.getLocation().y + e.getLocationOnScreen().y - previousPosition.y;

                    chosenLabel.setLocation(newx, newy);
                    previousPosition = e.getLocationOnScreen();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }

        };

        this.listenerNodeClick = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (chosenLabel != null) {
                    chosenLabel.switchChosenMode(false);
                }
                chosenLabel = (LabelNode) e.getSource();
                chosenLabel.switchChosenMode(true);
                previousPosition = e.getLocationOnScreen();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e) && chosenLabel.getDeviceId() != VS_DEVICE_ID) {
                    pmenuNode.show(chosenLabel, e.getX(), e.getY());
                } else {
                    chosenLabel.switchChosenMode(false);
                    chosenLabel = null;
                    PanelBasicTopology.this.repaint();
                }
            }
        };

        this.listenerItem = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JMenuItem source = (JMenuItem) e.getSource();
                if (source == mitemInformation) {
                    int deviceId = chosenLabel.getDeviceId();
                    if (deviceId >= 0) {
                        ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices().switchBetweenDeviceListAndTopology(true);
                        ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices().switchDisplayedPanel(PanelImportedDevices.PANELS.PANEL_DEVICE_INFO);
                        ApplicationWindow.getInstance().getPanelMain().getPanelImportedDevices().getPanelDeviceInfo().initViewData(deviceId);
                    } else {
                        JOptionPane.showMessageDialog(null, MESSAGE_FOR_NOT_GETTING_DEVICE_INFO);
                    }
                }
                if (source == mitemConfiguration) {
                    switchSSHClientVisibility(true);
                    int deviceId = chosenLabel.getDeviceId();
                    if (deviceId != VS_DEVICE_ID) {
                        if (deviceId >= 0) {
                            panelSSHClient.initViewData(deviceId);
                        } else {
                            panelSSHClient.initViewData(chosenLabel.getDeviceAddress());
                        }
                    }
                }
            }

        };
        this.mitemConfiguration.addActionListener(this.listenerItem);
        this.mitemInformation.addActionListener(this.listenerItem);
    }

    public void initTopo() {
        TopoDrawer.getInstance().constructTopo();
        this.drawNodes(TopoDrawer.getInstance().getTopoNodes());
    }

    public synchronized void drawNodes(List<TopoNodeData> topoNodes) {
        int tempSize = topoNodes.size();
        this.removeAll();
        try {
            for (int i = 0; i < tempSize; i++) {
                TopoNodeData data = topoNodes.get(i);
                LabelNode temp = new LabelNode(i, data.getDeviceLabel(), data.getNodeType());
                temp.addMouseListener(this.listenerNodeClick);
                temp.addMouseMotionListener(this.listenerNodeMotion);
                this.add(temp, new AbsoluteConstraints(data.getX() - ICON_RADIUS, data.getY() - ICON_RADIUS, -1, -1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.revalidate();
        this.repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        {
            Graphics2D g2 = (Graphics2D) g;
            int[] coordinates;
            List<int[]> connectionList = TopoDrawer.getInstance().getConnectionList();
            synchronized (connectionList) {
                g2.setStroke(new BasicStroke(2));

                for (int[] connection : connectionList) {
                    coordinates = TopoDrawer.getInstance().getConnectionCoordinates(connection);
                    g2.drawLine(coordinates[0], coordinates[1], coordinates[2], coordinates[3]);
                }
            }
        }

        System.gc();
    }

    public boolean checkRedrawing() {
        if (this.isVisible() && !this.panelSSHClient.isVisible()) {
            int userChoice = JOptionPane.showConfirmDialog(null, this.MESSAGE_FOR_REDRAWING_DIALOG);
            if (userChoice == JOptionPane.YES_OPTION) {
                new Thread() {
                    @Override
                    public void run() {
                        PanelBasicTopology.this.initTopo();
                    }
                }.start();
                return true;
            }
        }
        return false;
    }

    public void switchSSHClientVisibility(boolean visible) {
        System.out.println("DISPLAY SSH CLIENT WITH " + String.valueOf(visible));
        this.panelSSHClient.setVisible(visible);
        this.panelSSHClient.setEnabled(visible);
        
        if (visible) {
            this.add(this.panelSSHClient, new AbsoluteConstraints(1050, 440, -1, -1));
        } else {
            this.remove(this.panelSSHClient);
        }
        
        this.revalidate();
        this.repaint();
    }
    
    public class LabelNode extends JLabel {

        private int nodeListId;

        public LabelNode(int nodeListId, String label, NodeTypes type) {
            this.nodeListId = nodeListId;
            this.setText(label);
            this.setFont(new Font("SansSerif", 1, 14));
            this.setForeground(Color.blue);
            if (type == NodeTypes.MANAGER) {
                this.setIcon(new ImageIcon(getClass().getResource("/resources/icon_manager_node_40.png")));
            } else if (type == NodeTypes.UNKNOWN) {
                this.setIcon(new ImageIcon(getClass().getResource("/resources/icon_unknown_node_40.png")));
            } else if (type == NodeTypes.IMPORTED) {
                this.setIcon(new ImageIcon(getClass().getResource("/resources/icon_imported_node_40.png")));
            } else {
                this.setIcon(new ImageIcon(getClass().getResource("/resources/icon_vs_40.png")));
            }
        }

        @Override
        public void setLocation(int x, int y) {
            super.setLocation(x, y);
            TopoDrawer.getInstance().getTopoNodes().get(this.nodeListId).setPosition(x + ICON_RADIUS, y + ICON_RADIUS);
        }

        public int getDeviceId() {
            return TopoDrawer.getInstance().getDeviceIdForNode(nodeListId);
        }

        public String getDeviceAddress() {
            return TopoDrawer.getInstance().getDeviceDefaultAddressForNode(this.nodeListId);
        }
        
        public void switchChosenMode(boolean chosen) {
            if (this.getDeviceId() >= 0) {
                if (chosen) {
                    this.setIcon(new ImageIcon(getClass().getResource("/resources/icon_chosen_node_40.png")));
                    this.repaint();
                } else {
                    this.setIcon(new ImageIcon(getClass().getResource("/resources/icon_imported_node_40.png")));
                    this.repaint();
                }
            }
        }
    }
}
