/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fub.mapsforge.project.detector.model.inference.ui;

import de.fub.mapsforge.project.detector.model.inference.processhandler.InferenceModelProcessHandler;
import de.fub.utilsmodule.Collections.ObservableArrayList;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.ListView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.WeakListeners;

/**
 *
 * @author Serdar
 */
public class ProcessSelectionComponent extends javax.swing.JPanel implements ExplorerManager.Provider, PropertyChangeListener {

    private static final long serialVersionUID = 1L;
    private final ExplorerManager explorerManager = new ExplorerManager();
    private final ObservableArrayList<InferenceModelProcessHandler> processHandlerList = new ObservableArrayList<InferenceModelProcessHandler>();
    private InferenceModelProcessHandler selectedProcessHandler;

    /**
     * Creates new form ProcessSelectionComponent
     */
    public ProcessSelectionComponent() {
        initComponents();
        explorerManager.setRootContext(new AbstractNode(Children.create(new ProcessHandlerFactory(processHandlerList), true)));
        explorerManager.addPropertyChangeListener(WeakListeners.propertyChange(ProcessSelectionComponent.this, explorerManager));
        listView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
        listView = new org.openide.explorer.view.ListView();
        currentProcessHandlerName = new javax.swing.JTextField();
        selectButton = new javax.swing.JButton();
        title = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        listView.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        currentProcessHandlerName.setEditable(false);
        currentProcessHandlerName.setText(org.openide.util.NbBundle.getMessage(ProcessSelectionComponent.class, "ProcessSelectionComponent.currentProcessHandlerName.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(selectButton, org.openide.util.NbBundle.getMessage(ProcessSelectionComponent.class, "ProcessSelectionComponent.selectButton.text")); // NOI18N
        selectButton.setEnabled(false);
        selectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(title, org.openide.util.NbBundle.getMessage(ProcessSelectionComponent.class, "ProcessSelectionComponent.title.text")); // NOI18N
        title.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(currentProcessHandlerName)
            .addComponent(selectButton, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addComponent(listView, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(currentProcessHandlerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(selectButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(listView, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE))
        );

        add(jPanel3, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void selectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectButtonActionPerformed
        Node[] selectedNodes = getExplorerManager().getSelectedNodes();
        if (selectedNodes.length == 1) {
            selectedProcessHandler = selectedNodes[0].getLookup().lookup(InferenceModelProcessHandler.class);
            if (selectedProcessHandler != null) {
                setSelectedProcessHandler(selectedProcessHandler);
            }
        }
    }//GEN-LAST:event_selectButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField currentProcessHandlerName;
    private javax.swing.JPanel jPanel3;
    private org.openide.explorer.view.ListView listView;
    private javax.swing.JButton selectButton;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables

    @Override
    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }

    public InferenceModelProcessHandler getSelectedProcessHandler() {
        return selectedProcessHandler;
    }

    public void setSelectedProcessHandler(InferenceModelProcessHandler selectedProcessHandler) {
        this.selectedProcessHandler = selectedProcessHandler;
        if (this.selectedProcessHandler != null) {
            currentProcessHandlerName.setText(this.selectedProcessHandler.getDescriptor().getName());
        } else {
            currentProcessHandlerName.setText(null);
        }
    }

    public JButton getSelectButton() {
        return selectButton;
    }

    public ObservableArrayList<InferenceModelProcessHandler> getProcessHandlerList() {
        return processHandlerList;
    }

    public ListView getListView() {
        return listView;
    }

    public JLabel getTitle() {
        return title;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (ExplorerManager.PROP_SELECTED_NODES.equals(evt.getPropertyName())) {
            Node[] selectedNodes = getExplorerManager().getSelectedNodes();
            if (selectedNodes.length == 1) {
                selectButton.setEnabled(true);
            } else {
                selectButton.setEnabled(false);
            }
        }
    }

    private static class ProcessHandlerFactory extends ChildFactory<InferenceModelProcessHandler> implements ChangeListener {

        private final ObservableArrayList<InferenceModelProcessHandler> list;

        public ProcessHandlerFactory(ObservableArrayList<InferenceModelProcessHandler> list) {
            this.list = list;
        }

        @Override
        protected boolean createKeys(List<InferenceModelProcessHandler> toPopulate) {
            toPopulate.addAll(list);
            return true;
        }

        @Override
        protected Node createNodeForKey(InferenceModelProcessHandler key) {
            return new FilterNode(key.getNodeDelegate());
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            refresh(true);
        }
    }
}
