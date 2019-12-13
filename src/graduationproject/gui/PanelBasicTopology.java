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
import java.awt.Graphics;
import java.awt.Graphics2D;
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

    public PanelBasicTopology() {
        initComponents();
    }

    private void initComponents() {
        this.setPreferredSize(new Dimension(1600, 940));
        this.setBackground(Color.white);
        this.setLayout(new AbsoluteLayout());
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
//            System.out.println("TOPO - DRAW NODE : " +  data.getDeviceId());
                LabelNode temp = new LabelNode(i, data.getDeviceLabel(), data.getNodeType());
//            System.out.println("X: " + data.getX() + " AND Y: " + data.getY());
                this.add(temp, new AbsoluteConstraints(data.getX() - 20, data.getY() - 20, -1, -1));
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
            g2.setStroke(new BasicStroke(2));
            
            for (int[] connection : connectionList) {
                coordinates = TopoDrawer.getInstance().getConnectionCoordinates(connection);
                g2.drawLine(coordinates[0], coordinates[1], coordinates[2], coordinates[3]);
            }
        }
        
        System.gc();
    }

    public class LabelNode extends JLabel {

        private int nodeListId;

        public LabelNode(int nodeListId, String label, NodeTypes type) {
//            this.setPreferredSize(new Dimension(100, 20));
            this.nodeListId = nodeListId;
//            this.setText(String.valueOf(deviceId));
            this.setText(label);
            if (type == NodeTypes.MANAGER) {
                this.setIcon(new ImageIcon(getClass().getResource("/resources/icon_manager_node_40.png")));
            } else if (type == NodeTypes.UNKNOWN) {
                this.setIcon(new ImageIcon(getClass().getResource("/resources/icon_unknown_node_40.png")));
            } else {
                this.setIcon(new ImageIcon(getClass().getResource("/resources/icon_imported_node_40.png")));
            }
            this.repaint();
        }
    }
}
