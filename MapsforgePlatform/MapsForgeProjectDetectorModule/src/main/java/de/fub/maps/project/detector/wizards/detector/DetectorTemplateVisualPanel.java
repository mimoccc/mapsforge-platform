/*
 * Copyright (C) 2013 Serdar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fub.maps.project.detector.wizards.detector;

import de.fub.maps.project.detector.model.Detector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.Action;
import javax.swing.ListSelectionModel;
import org.openide.explorer.ExplorerManager;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.util.WeakListeners;

/**
 *
 * @author Serdar
 */
@Messages({"CLT_DetectorTemplateVisualPanel_Name=Templates"})
public class DetectorTemplateVisualPanel extends javax.swing.JPanel implements ExplorerManager.Provider, PropertyChangeListener {

    private static final long serialVersionUID = 1L;
    private final ExplorerManager explorerManager = new ExplorerManager();

    /**
     * Creates new form DetectorTemplateVisualPanel
     */
    public DetectorTemplateVisualPanel() {
        initComponents();
        outlineView1.getOutline().setRootVisible(false);
        outlineView1.getOutline().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        explorerManager.setRootContext(new AbstractNode(Children.create(new TemplateNodeFactory(), true)));
        explorerManager.addPropertyChangeListener(WeakListeners.propertyChange(DetectorTemplateVisualPanel.this, explorerManager));
    }

    @Override
    public String getName() {
        return Bundle.CLT_DetectorTemplateVisualPanel_Name();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        outlineView1 = new org.openide.explorer.view.OutlineView("Templates");
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        description = new javax.swing.JTextArea();

        setLayout(new java.awt.BorderLayout());

        outlineView1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        outlineView1.setPreferredSize(new java.awt.Dimension(400, 0));
        add(outlineView1, java.awt.BorderLayout.CENTER);

        jPanel1.setMinimumSize(new java.awt.Dimension(100, 84));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(DetectorTemplateVisualPanel.class, "DetectorTemplateVisualPanel.jLabel1.text")); // NOI18N

        jScrollPane1.setBorder(null);

        description.setEditable(false);
        description.setColumns(20);
        description.setLineWrap(true);
        description.setRows(2);
        description.setWrapStyleWord(true);
        description.setOpaque(false);
        description.setPreferredSize(new java.awt.Dimension(400, 40));
        jScrollPane1.setViewportView(description);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE))
        );

        add(jPanel1, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea description;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private org.openide.explorer.view.OutlineView outlineView1;
    // End of variables declaration//GEN-END:variables

    @Override
    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (ExplorerManager.PROP_SELECTED_NODES.equals(evt.getPropertyName())) {
            Node[] selectedNodes = getExplorerManager().getSelectedNodes();
            if (selectedNodes.length == 1) {
                Detector detector = selectedNodes[0].getLookup().lookup(Detector.class);
                if (detector != null) {
                    description.setText(detector.getDetectorDescriptor().getDescription());
                }
            } else {
                description.setText(null);
            }
        }
    }

    private static class TemplateNodeFactory extends ChildFactory<DataObject> {

        @Override
        protected boolean createKeys(List<DataObject> toPopulate) {
            FileObject templateFolder = FileUtil.getConfigFile("Templates/Detectors");
            if (templateFolder != null) {
                for (FileObject templateFileObject : templateFolder.getChildren()) {
                    try {
                        toPopulate.add(DataObject.find(templateFileObject));
                    } catch (DataObjectNotFoundException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }
            return true;
        }

        @Override
        protected Node createNodeForKey(DataObject dataObject) {
            return new FilterNodeImpl(dataObject.getNodeDelegate());
        }
    }

    private static class FilterNodeImpl extends FilterNode {

        public FilterNodeImpl(Node original) {
            super(original, FilterNode.Children.LEAF);
        }

        @Override
        public Action[] getActions(boolean context) {
            return new Action[0];
        }
    }
}
