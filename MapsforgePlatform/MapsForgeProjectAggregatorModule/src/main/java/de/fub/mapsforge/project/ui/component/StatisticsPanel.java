/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fub.mapsforge.project.ui.component;

import de.fub.mapsforge.project.api.StatisticProvider;
import java.beans.PropertyVetoException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.explorer.ExplorerManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.PropertySupport.ReadOnly;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;

/**
 *
 * @author Serdar
 */
public class StatisticsPanel extends javax.swing.JPanel implements ExplorerManager.Provider {

    private static final long serialVersionUID = 1L;
    private final ExplorerManager explorerManager = new ExplorerManager();
    private transient List<StatisticProvider> statisticsProvides;
    private StatisticsNode statisticsNode;

    /**
     * Creates new form StatisticsPanel
     */
    public StatisticsPanel() {
        initComponents();
    }

    public StatisticsPanel(List<StatisticProvider> statisticsProvides) {
        this();
        this.statisticsProvides = statisticsProvides;
        statisticsNode = new StatisticsNode(this.statisticsProvides);
        explorerManager.setRootContext(statisticsNode);
        try {
            explorerManager.setSelectedNodes(new Node[]{statisticsNode});
        } catch (PropertyVetoException ex) {
            Exceptions.printStackTrace(ex);
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

        propertySheetView1 = new org.openide.explorer.propertysheet.PropertySheetView();

        setMinimumSize(new java.awt.Dimension(400, 450));
        setPreferredSize(new java.awt.Dimension(400, 450));
        setLayout(new java.awt.BorderLayout());
        add(propertySheetView1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.openide.explorer.propertysheet.PropertySheetView propertySheetView1;
    // End of variables declaration//GEN-END:variables

    @Override
    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }

    private static class StatisticsNode extends AbstractNode {

        private static final Logger LOG = Logger.getLogger(StatisticsNode.class.getName());
        private static final String TAB_NAME = "tabName";
        private final List<StatisticProvider> statisticsProvides;

        public StatisticsNode(List<StatisticProvider> statisticProviders) {
            super(Children.LEAF);
            this.statisticsProvides = statisticProviders;
        }

        @Override
        protected Sheet createSheet() {
            Sheet sheet = Sheet.createDefault();
            for (StatisticProvider provider : statisticsProvides) {

                try {
                    Sheet.Set set = null;
                    Property<?> property = null;
                    Collections.reverse(provider.getStatisticData());
                    for (StatisticProvider.StatisticSection section : provider.getStatisticData()) {
                        set = Sheet.createPropertiesSet();
                        set.setValue(TAB_NAME, provider.getName());
                        set.setName(section.getName());
                        set.setDisplayName(section.getName());
                        set.setShortDescription(section.getDescription());
                        sheet.put(set);
                        for (final StatisticProvider.StatisticItem item : section.getStatisticsItemList()) {
                            property = new ReadOnly<String>(item.getName(), String.class, item.getName(), item.getDescription()) {
                                @Override
                                public String getValue() throws IllegalAccessException, InvocationTargetException {
                                    return item.getValue();
                                }
                            };
                            set.put(property);
                        }
                    }
                    if (set != null) {
                        set.setPreferred(true);
                    }
                } catch (StatisticProvider.StatisticNotAvailableException ex) {
                    LOG.log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
            sheet.remove("properties");
            return sheet;
        }
    }
}
