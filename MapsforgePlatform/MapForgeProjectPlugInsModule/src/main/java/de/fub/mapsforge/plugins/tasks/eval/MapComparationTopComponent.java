/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fub.mapsforge.plugins.tasks.eval;

import java.util.List;
import javax.swing.Box;
import org.openide.windows.TopComponent;

/**
 *
 * @author Serdar
 */
public class MapComparationTopComponent extends TopComponent {

    private static final long serialVersionUID = 1L;

    /**
     * Creates new form MapComparationModel
     */
    public MapComparationTopComponent() {
        initComponents();
    }

    public MapComparationTopComponent(List<AggregatorRoadNetworkStatisticPair> roadNetworkStatisticList) {
        this();

        for (AggregatorRoadNetworkStatisticPair pair : roadNetworkStatisticList) {
            RoadNetworkStatisticComparationPanel panel = new RoadNetworkStatisticComparationPanel(pair);
            contentPanel.add(panel);
            contentPanel.add(Box.createVerticalStrut(8));
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        contentPanel = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        contentPanel.setBackground(new java.awt.Color(255, 255, 255));
        contentPanel.setLayout(new javax.swing.BoxLayout(contentPanel, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(contentPanel);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel contentPanel;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
