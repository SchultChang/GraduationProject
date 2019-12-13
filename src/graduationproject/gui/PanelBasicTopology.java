/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.gui;

import graduationproject.helpers.TopoDrawer;
import graduationproject.helpers.TopoDrawer.NodeTypes;
import graduationproject.helpers.TopoDrawer.TopoNodeData;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

/**
 *
 * @author cloud
 */
public class PanelBasicTopology extends JPanel {

    private final int ICON_RADIUS = 20;

    private MouseAdapter listenerNodeClick;
    private MouseMotionListener listenerNodeMotion;

    private Point previousPosition;
    private LabelNode chosenLabel;

    public PanelBasicTopology() {
        initComponents();
        initListeners();
    }

    private void initComponents() {
        this.setPreferredSize(new Dimension(1600, 940));
        this.setBackground(Color.white);
        this.setLayout(new AbsoluteLayout());
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
//        this.addMouseMotionListener(listenerPanel);

        this.listenerNodeClick = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                chosenLabel = (LabelNode) e.getSource();
                previousPosition = e.getLocationOnScreen();
//                System.out.println("CHOOSE AN NODE");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
//                System.out.println("RELEASE AN NODE");
                chosenLabel = null;
                PanelBasicTopology.this.repaint();
            }
        };

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
            } else {
                this.setIcon(new ImageIcon(getClass().getResource("/resources/icon_imported_node_40.png")));
            }
        }

        @Override
        public void setLocation(int x, int y) {
            super.setLocation(x, y);
            TopoDrawer.getInstance().getTopoNodes().get(this.nodeListId).setPosition(x + ICON_RADIUS, y + ICON_RADIUS);
        }
    }
}
