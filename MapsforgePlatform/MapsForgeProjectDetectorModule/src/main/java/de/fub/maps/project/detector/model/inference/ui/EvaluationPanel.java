/*
 * Copyright 2013 Serdar.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.fub.maps.project.detector.model.inference.ui;

import de.fub.maps.project.detector.model.inference.EvaluationDetailPanel;
import de.fub.maps.project.detector.model.inference.processhandler.InferenceModelProcessHandler;
import de.fub.maps.project.detector.model.inference.ui.charts.PrecisionRecallBarChartPanel;
import de.fub.utilsmodule.components.CustomOutlineView;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.List;
import javax.swing.JLabel;
import org.jfree.data.category.DefaultCategoryDataset;
import org.netbeans.swing.outline.Outline;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.OutlineView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.NbBundle;
import weka.classifiers.Evaluation;
import weka.core.Instances;

/**
 *
 * @author Serdar
 */
@NbBundle.Messages({
    "CLT_TransportMode_Label_Text=Transport Mode"
})
public class EvaluationPanel extends javax.swing.JPanel implements ExplorerManager.Provider {

    private static final String NUMBER_PATTERN = "{0, number, 000.00} %";
    private static final long serialVersionUID = 1L;
    private transient final ExplorerManager explorerManager = new ExplorerManager();
    private transient Evaluation evaluation;
    private transient InferenceModelProcessHandler processHandler;

    /**
     * Creates new form EvaluationPanel
     */
    public EvaluationPanel() {
        initComponents();
        outlineView.getActionMap().clear();
        Outline outline = outlineView.getOutline();
        outline.getActionMap().clear();
        outline.setRootVisible(false);
    }

    public EvaluationPanel(InferenceModelProcessHandler processHandler) {
        this();
        this.processHandler = processHandler;
    }

    public PrecisionRecallBarChartPanel getBarChartPanel() {
        return barChartPanel;
    }

    public OutlineView getOutlineView() {
        return outlineView;
    }

    public JLabel getTitle() {
        return title;
    }

    public JLabel getCorrectClassifiedInstances() {
        return correctClassifiedInstances;
    }

    public JLabel getIncorrectClassifiedInstances() {
        return incorrectClassifiedInstances;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        barChartPanel = new de.fub.maps.project.detector.model.inference.ui.charts.PrecisionRecallBarChartPanel();
        outlineView = new CustomOutlineView(NbBundle.getMessage(EvaluationPanel.class, "CLT_Doman_Axis_Name"));
        jPanel3 = new javax.swing.JPanel();
        title = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        correctClassifiedInstances = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        incorrectClassifiedInstances = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        toolBar = new javax.swing.JToolBar();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        infoButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102), 2));
        setMaximumSize(new java.awt.Dimension(2147483647, 350));
        setMinimumSize(new java.awt.Dimension(0, 300));
        setPreferredSize(new java.awt.Dimension(0, 300));
        setLayout(new java.awt.BorderLayout(0, 8));

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 4, 8, 4));
        jPanel2.setMinimumSize(new java.awt.Dimension(0, 32));
        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(0, 429));

        jPanel6.setMinimumSize(new java.awt.Dimension(0, 10));
        jPanel6.setPreferredSize(new java.awt.Dimension(0, 420));
        jPanel6.setLayout(new java.awt.BorderLayout());

        barChartPanel.setPreferredSize(new java.awt.Dimension(0, 420));
        jPanel6.add(barChartPanel, java.awt.BorderLayout.CENTER);

        outlineView.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(213, 213, 213)));
        outlineView.setPropertyColumns(new String[] {"precision", "Precision", "recall", "Recall"});
        outlineView.setWheelScrollingEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 657, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(outlineView, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(outlineView, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel3.setBackground(new java.awt.Color(255, 216, 178));
        jPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(204, 204, 204)));
        jPanel3.setMinimumSize(new java.awt.Dimension(0, 32));
        jPanel3.setPreferredSize(new java.awt.Dimension(0, 32));
        jPanel3.setLayout(new java.awt.BorderLayout());

        title.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        org.openide.awt.Mnemonics.setLocalizedText(title, org.openide.util.NbBundle.getMessage(EvaluationPanel.class, "EvaluationPanel.title.text")); // NOI18N
        title.setPreferredSize(new java.awt.Dimension(0, 29));
        jPanel3.add(title, java.awt.BorderLayout.CENTER);

        add(jPanel3, java.awt.BorderLayout.NORTH);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 0, 0, 0, new java.awt.Color(204, 204, 204)));
        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 32));
        jPanel1.setMinimumSize(new java.awt.Dimension(10, 32));
        jPanel1.setPreferredSize(new java.awt.Dimension(10, 32));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel4.setBackground(new java.awt.Color(255, 216, 178));
        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 8, 1, 8));
        jPanel4.setPreferredSize(new java.awt.Dimension(0, 41));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(EvaluationPanel.class, "EvaluationPanel.jLabel1.text")); // NOI18N

        correctClassifiedInstances.setFont(new java.awt.Font("Monospaced", 0, 11)); // NOI18N
        correctClassifiedInstances.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        org.openide.awt.Mnemonics.setLocalizedText(correctClassifiedInstances, org.openide.util.NbBundle.getMessage(EvaluationPanel.class, "EvaluationPanel.correctClassifiedInstances.text")); // NOI18N
        correctClassifiedInstances.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 8));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(EvaluationPanel.class, "EvaluationPanel.jLabel2.text")); // NOI18N

        incorrectClassifiedInstances.setFont(new java.awt.Font("Monospaced", 0, 11)); // NOI18N
        incorrectClassifiedInstances.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        org.openide.awt.Mnemonics.setLocalizedText(incorrectClassifiedInstances, org.openide.util.NbBundle.getMessage(EvaluationPanel.class, "EvaluationPanel.incorrectClassifiedInstances.text")); // NOI18N
        incorrectClassifiedInstances.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 8));

        jPanel5.setOpaque(false);
        jPanel5.setLayout(new java.awt.BorderLayout());

        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        toolBar.setOpaque(false);
        toolBar.add(filler1);

        org.openide.awt.Mnemonics.setLocalizedText(infoButton, org.openide.util.NbBundle.getMessage(EvaluationPanel.class, "EvaluationPanel.infoButton.text")); // NOI18N
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
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(correctClassifiedInstances)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(incorrectClassifiedInstances)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(correctClassifiedInstances, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(incorrectClassifiedInstances, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator1)
                    .addComponent(jSeparator2))
                .addContainerGap())
        );

        jPanel1.add(jPanel4, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void infoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_infoButtonActionPerformed
        if (evaluation != null) {
            DialogDescriptor descriptor = new DialogDescriptor(new EvaluationDetailPanel(evaluation), "Detail Evaluation Statistics"); //NO18N
            DialogDisplayer.getDefault().createDialog(descriptor).setVisible(true);
        }
    }//GEN-LAST:event_infoButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private de.fub.maps.project.detector.model.inference.ui.charts.PrecisionRecallBarChartPanel barChartPanel;
    private javax.swing.JLabel correctClassifiedInstances;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel incorrectClassifiedInstances;
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
    private org.openide.explorer.view.OutlineView outlineView;
    private javax.swing.JLabel title;
    private javax.swing.JToolBar toolBar;
    // End of variables declaration//GEN-END:variables

    @Override
    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }

    public void updatePanel(Evaluation evaluation) {
        DefaultCategoryDataset dataset = getBarChartPanel().getDataset();
        dataset.clear();

        this.evaluation = evaluation;
        double correct = evaluation.pctCorrect();
        double incorrect = evaluation.pctIncorrect();

        getCorrectClassifiedInstances().setText(MessageFormat.format(NUMBER_PATTERN, correct));
        getIncorrectClassifiedInstances().setText(MessageFormat.format(NUMBER_PATTERN, incorrect));

        int numClasses = evaluation.getHeader().numClasses();
        for (int classIndex = 0; classIndex < numClasses; classIndex++) {
            double precision = evaluation.precision(classIndex) * 100;
            double recall = evaluation.recall(classIndex) * 100;
            dataset.addValue(precision, NbBundle.getMessage(EvaluationPanel.class, "EvaluationPanel.CLT_Precision_Text"), evaluation.getHeader().classAttribute().value(classIndex));
            dataset.addValue(recall, NbBundle.getMessage(EvaluationPanel.class, "EvaluationPanel.CLT_Recall_Text"), evaluation.getHeader().classAttribute().value(classIndex));
        }

        getExplorerManager().setRootContext(new AbstractNode(Children.create(new EvaluationNodeFactory(evaluation), true)));
        repaint();
    }

    private static class EvaluationNodeFactory extends ChildFactory<SimpleEvaluationNode> {

        private final Evaluation evaluation;

        public EvaluationNodeFactory(Evaluation evaluation) {
            this.evaluation = evaluation;
        }

        @Override
        protected boolean createKeys(List<SimpleEvaluationNode> toPopulate) {
            Instances header = evaluation.getHeader();
            int numClasses = header.numClasses();
            for (int index = 0; index < numClasses; index++) {
                toPopulate.add(new SimpleEvaluationNode(evaluation, index));
            }

            return true;
        }

        @Override
        protected Node createNodeForKey(SimpleEvaluationNode node) {
            return node;
        }
    }

    private static class SimpleEvaluationNode extends AbstractNode implements Comparable<SimpleEvaluationNode> {

        private final Evaluation evaluation;
        private final int classIndex;
        private static final Image EMPTY_IMAGE = getEmptyImage();

        public SimpleEvaluationNode(Evaluation evaluation, int classIndex) {
            super(Children.LEAF);
            this.evaluation = evaluation;
            this.classIndex = classIndex;
            setDisplayName(evaluation.getHeader().classAttribute().value(classIndex));
        }

        @Override
        protected Sheet createSheet() {
            Sheet sheet = Sheet.createDefault();
            Sheet.Set set = Sheet.createPropertiesSet();
            sheet.put(set);

            Property<?> property = new PropertySupport.ReadOnly<Double>("precision", Double.class, NbBundle.getMessage(EvaluationPanel.class, "EvaluationPanel.CLT_Precision_Text"), "The precision value that this classifier obtained for the given label.") {
                @Override
                public Double getValue() throws IllegalAccessException, InvocationTargetException {
                    double precision = evaluation.precision(classIndex) * 100;
                    String formatedString = MessageFormat.format("{0, number, 000.00}", precision).replaceAll(",", "\\.");
                    Double result = Double.valueOf(formatedString);
                    return result;
                }
            };
            set.put(property);

            property = new PropertySupport.ReadOnly<Double>("recall", Double.class, NbBundle.getMessage(EvaluationPanel.class, "EvaluationPanel.CLT_Recall_Text"), "The recall value that this classifier obtained for the given label.") {
                @Override
                public Double getValue() throws IllegalAccessException, InvocationTargetException {
                    double recall = evaluation.recall(classIndex) * 100;
                    String formatedString = MessageFormat.format("{0, number, 000.00}", recall).replaceAll(",", "\\.");
                    Double result = Double.valueOf(formatedString);
                    return result;
                }
            };
            set.put(property);

            return sheet;
        }

        @Override
        public Image getIcon(int type) {
            return EMPTY_IMAGE;
        }

        @Override
        public Image getOpenedIcon(int type) {
            return getIcon(type);
        }

        @Override
        public int compareTo(SimpleEvaluationNode evaluationNode) {
            return getDisplayName().compareToIgnoreCase(evaluationNode.getDisplayName());
        }

        private static Image getEmptyImage() {
            BufferedImage bufferedImage = new BufferedImage(16, 16, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2d = bufferedImage.createGraphics();
            g2d.setPaint(new Color(255, 255, 255, 0));
            g2d.fill(new Rectangle(bufferedImage.getWidth(), bufferedImage.getHeight()));
            g2d.dispose();
            return bufferedImage;
        }
    }

    private static class MyOutlineView extends OutlineView {

        private static final long serialVersionUID = 1L;

        public MyOutlineView() {
            getActionMap().clear();
        }

        public MyOutlineView(String nodesColumnLabel) {
            super(nodesColumnLabel);
            getActionMap().clear();
        }
    }
}
