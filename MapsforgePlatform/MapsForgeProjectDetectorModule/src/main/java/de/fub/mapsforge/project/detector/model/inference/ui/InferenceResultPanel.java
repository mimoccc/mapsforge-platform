/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fub.mapsforge.project.detector.model.inference.ui;

import de.fub.mapsforge.project.detector.model.inference.ui.charts.ClassificationBarChart;
import de.fub.mapsforge.project.detector.model.inference.processhandler.InferenceDataProcessHandler;
import de.fub.utilsmodule.Collections.ObservableArrayList;
import de.fub.utilsmodule.Collections.ObservableList;
import de.fub.utilsmodule.components.CustomOutlineView;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.OutlineView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.Node.Property;
import org.openide.nodes.PropertySupport.ReadOnly;
import org.openide.nodes.Sheet;
import org.openide.util.NbBundle;
import org.openide.util.WeakListeners;
import weka.core.Instance;

/**
 *
 * @author Serdar
 */
public class InferenceResultPanel extends javax.swing.JPanel implements ExplorerManager.Provider {

    private static final long serialVersionUID = 1L;
    private final ExplorerManager explorerManager = new ExplorerManager();
    private final ObservableList<DataItem> dataItemList = new ObservableArrayList<DataItem>();

    /**
     * Creates new form InferenceResultPanel
     */
    public InferenceResultPanel() {
        initComponents();
        explorerManager.setRootContext(new AbstractNode(Children.create(new NodeFactory(dataItemList), true)));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        title = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        classificationBarChart = new de.fub.mapsforge.project.detector.model.inference.ui.charts.ClassificationBarChart();
        jPanel6 = new javax.swing.JPanel();
        outlineView = new CustomOutlineView(NbBundle.getMessage(InferenceResultPanel.class, "CLT_Doman_Axis_Name"));
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        classifiedInstances = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        notClassifiedInstances = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        toolBar = new javax.swing.JToolBar();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        infoButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        setMinimumSize(new java.awt.Dimension(54, 300));
        setPreferredSize(new java.awt.Dimension(849, 300));
        setLayout(new java.awt.BorderLayout(0, 8));

        jPanel3.setBackground(new java.awt.Color(255, 216, 178));
        jPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(204, 204, 204)));
        jPanel3.setMinimumSize(new java.awt.Dimension(0, 32));
        jPanel3.setPreferredSize(new java.awt.Dimension(100, 32));
        jPanel3.setLayout(new java.awt.BorderLayout());

        title.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        org.openide.awt.Mnemonics.setLocalizedText(title, org.openide.util.NbBundle.getMessage(InferenceResultPanel.class, "InferenceResultPanel.title.text")); // NOI18N
        jPanel3.add(title, java.awt.BorderLayout.CENTER);

        add(jPanel3, java.awt.BorderLayout.NORTH);

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 4, 8, 4));
        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(1368, 0));
        jPanel2.setLayout(new java.awt.GridLayout(1, 2));
        jPanel2.add(classificationBarChart);

        jPanel6.setOpaque(false);
        jPanel6.setLayout(new java.awt.BorderLayout());

        outlineView.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        outlineView.setPropertyColumns(new String[] {"instancesAbs", "Instances (abs.)", "instancesRel", "Instances (rel.)"});
        jPanel6.add(outlineView, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel6);

        add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 0, 0, 0, new java.awt.Color(204, 204, 204)));
        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 32));
        jPanel1.setMinimumSize(new java.awt.Dimension(10, 32));
        jPanel1.setPreferredSize(new java.awt.Dimension(10, 32));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel4.setBackground(new java.awt.Color(255, 216, 178));
        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 8, 1, 8));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(InferenceResultPanel.class, "InferenceResultPanel.jLabel1.text")); // NOI18N

        classifiedInstances.setFont(new java.awt.Font("Monospaced", 0, 11)); // NOI18N
        classifiedInstances.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        org.openide.awt.Mnemonics.setLocalizedText(classifiedInstances, org.openide.util.NbBundle.getMessage(InferenceResultPanel.class, "InferenceResultPanel.classifiedInstances.text")); // NOI18N
        classifiedInstances.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 8));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(InferenceResultPanel.class, "InferenceResultPanel.jLabel2.text")); // NOI18N

        notClassifiedInstances.setFont(new java.awt.Font("Monospaced", 0, 11)); // NOI18N
        notClassifiedInstances.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        org.openide.awt.Mnemonics.setLocalizedText(notClassifiedInstances, org.openide.util.NbBundle.getMessage(InferenceResultPanel.class, "InferenceResultPanel.notClassifiedInstances.text")); // NOI18N
        notClassifiedInstances.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 8));

        jPanel5.setOpaque(false);
        jPanel5.setLayout(new java.awt.BorderLayout());

        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        toolBar.setOpaque(false);
        toolBar.add(filler1);

        org.openide.awt.Mnemonics.setLocalizedText(infoButton, org.openide.util.NbBundle.getMessage(InferenceResultPanel.class, "InferenceResultPanel.infoButton.text")); // NOI18N
        infoButton.setFocusable(false);
        infoButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        infoButton.setMaximumSize(new java.awt.Dimension(24, 30));
        infoButton.setOpaque(false);
        infoButton.setPreferredSize(new java.awt.Dimension(32, 21));
        infoButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        infoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                infoButtonActionPerformed(evt);
            }
        });
        toolBar.add(infoButton);

        jPanel5.add(toolBar, java.awt.BorderLayout.CENTER);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(classifiedInstances)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(notClassifiedInstances)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(classifiedInstances, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(notClassifiedInstances, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator1)
                    .addComponent(jSeparator2))
                .addContainerGap())
        );

        jPanel1.add(jPanel4, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void infoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_infoButtonActionPerformed
        // TODO
    }//GEN-LAST:event_infoButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private de.fub.mapsforge.project.detector.model.inference.ui.charts.ClassificationBarChart classificationBarChart;
    private javax.swing.JLabel classifiedInstances;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JButton infoButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel notClassifiedInstances;
    private org.openide.explorer.view.OutlineView outlineView;
    private javax.swing.JLabel title;
    private javax.swing.JToolBar toolBar;
    // End of variables declaration//GEN-END:variables

    @Override
    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }

    public ClassificationBarChart getClassificationBarChart() {
        return classificationBarChart;
    }

    public JLabel getClassifiedInstances() {
        return classifiedInstances;
    }

    public JButton getInfoButton() {
        return infoButton;
    }

    public JLabel getNotClassifiedInstances() {
        return notClassifiedInstances;
    }

    public OutlineView getOutlineView() {
        return outlineView;
    }

    public JLabel getTitle() {
        return title;
    }

    public JToolBar getToolBar() {
        return toolBar;
    }

    public void updateView(InferenceDataProcessHandler.ClassificationResult classificationResult) {
        DefaultCategoryDataset relDataset = getClassificationBarChart().getRelDataset();
        DefaultCategoryDataset absDataset = getClassificationBarChart().getAbsDataset();
        relDataset.clear();
        absDataset.clear();
        dataItemList.clear();
        Map<String, List<Instance>> resultMap = classificationResult.getResultMap();

        double sum = 0;

        for (Entry<String, List<Instance>> entry : resultMap.entrySet()) {
            sum += entry.getValue().size();
        }
        CategoryPlot plot = getClassificationBarChart().getPlot();
        CategoryItemRenderer relRenderer = plot.getRenderer(0);
        CategoryItemRenderer absRenderer = plot.getRenderer(1);


        for (Entry<String, List<Instance>> entry : resultMap.entrySet()) {
            double abs = entry.getValue().size();
            double rel = entry.getValue().size() / sum * 100;
            absDataset.addValue(null, "Instances (rel.)", entry.getKey());
            absDataset.addValue(abs, "Instances (abs.)", entry.getKey());
            relDataset.addValue(rel, "Instances (rel.)", entry.getKey());
            relDataset.addValue(null, "Instances (abs.)", entry.getKey());
            dataItemList.add(new DataItem(entry.getKey(), rel, abs));
        }
        final LegendItemCollection result = new LegendItemCollection();
        result.add(relRenderer.getLegendItem(0, 0));
        result.add(absRenderer.getLegendItem(1, 1));

        double classified = (sum / classificationResult.getInstanceToTrackSegmentMap().size() * 100);
        double notClassified = ((classificationResult.getInstanceToTrackSegmentMap().size() - sum) / classificationResult.getInstanceToTrackSegmentMap().size() * 100);
        getClassifiedInstances().setText(MessageFormat.format("{0, number, 000.00} %", classified));
        getNotClassifiedInstances().setText(MessageFormat.format("{0, number, 000.00} %", notClassified));

        repaint();
    }

    private static class NodeFactory extends ChildFactory<DataItem> implements ChangeListener {

        private final ObservableList<DataItem> list;

        public NodeFactory(ObservableList<DataItem> list) {
            this.list = list;
            this.list.addChangeListener(WeakListeners.change(NodeFactory.this, list));
        }

        @Override
        protected boolean createKeys(List<DataItem> toPopulate) {
            toPopulate.addAll(list);
            return true;
        }

        @Override
        protected Node createNodeForKey(DataItem dataItem) {
            return new DataItemNode(dataItem);
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            refresh(true);
        }
    }

    private static class DataItem implements Comparable<DataItem> {

        private final double absData;
        private final double relData;
        private final String className;

        public DataItem(String className, double relData, double absData) {
            this.className = className;
            this.relData = relData;
            this.absData = absData;
        }

        public double getAbsData() {
            return absData;
        }

        public double getRelData() {
            return relData;
        }

        public String getClassName() {
            return className;
        }

        @Override
        public int compareTo(DataItem dataItem) {
            return getClassName().compareToIgnoreCase(dataItem.getClassName());
        }
    }

    private static class DataItemNode extends AbstractNode {

        private final DataItem dataItem;

        public DataItemNode(DataItem dataItem) {
            super(Children.LEAF);
            this.dataItem = dataItem;
            setDisplayName(dataItem.getClassName());
        }

        @Override
        protected Sheet createSheet() {
            Sheet sheet = Sheet.createDefault();
            Sheet.Set set = Sheet.createPropertiesSet();
            sheet.put(set);

            Property<?> property = new ReadOnly<Double>("instancesAbs", Double.class, "Instances (abs.)", "") {
                @Override
                public Double getValue() throws IllegalAccessException, InvocationTargetException {
                    return dataItem.getAbsData();
                }
            };
            set.put(property);

            property = new ReadOnly<Double>("instancesRel", Double.class, "Instances (rel.)", "") {
                @Override
                public Double getValue() throws IllegalAccessException, InvocationTargetException {
                    return dataItem.getRelData();
                }
            };
            set.put(property);

            return sheet;
        }
    }
}
