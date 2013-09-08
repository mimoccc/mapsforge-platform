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
package de.fub.maps.project.detector.ui;

import de.fub.maps.project.detector.utils.DetectorUtils;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import org.openide.explorer.ExplorerManager;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.WeakListeners;

/**
 *
 * @author Serdar
 */
public class FolderExplorer extends javax.swing.JPanel implements ExplorerManager.Provider, PropertyChangeListener {

    private static final long serialVersionUID = 1L;
    private final ExplorerManager explorerManager = new ExplorerManager();
    private Node[] selectedNodes = new Node[0];
    private boolean componentAdded = false;

    /**
     * Creates new form FolderExplorer
     */
    public FolderExplorer() {
        initComponents();
        outlineView1.getOutline().setRootVisible(false);
        init();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        outlineView1 = new org.openide.explorer.view.OutlineView();

        setLayout(new java.awt.BorderLayout());
        add(outlineView1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.openide.explorer.view.OutlineView outlineView1;
    // End of variables declaration//GEN-END:variables

    @Override
    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }

    public Node[] getSelectedFiles() {
        return selectedNodes;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (ExplorerManager.PROP_SELECTED_NODES.equals(evt.getPropertyName()) && componentAdded) {
            selectedNodes = getExplorerManager().getSelectedNodes();
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        componentAdded = true;
    }

    @Override
    public void removeNotify() {
        componentAdded = false;
        super.removeNotify();
    }

    private void init() {
        try {
            FileObject fileObject = DetectorUtils.getDatasourceFileObject();
            if (fileObject != null
                    && fileObject.isValid()
                    && fileObject.isFolder()) {
                DataObject dataObject = DataObject.find(fileObject);
                explorerManager.setRootContext(new FilterNode(dataObject.getNodeDelegate()));
                explorerManager.addPropertyChangeListener(WeakListeners.propertyChange(FolderExplorer.this, explorerManager));
            } else {
                throw new IOException("Couldn'T find datasource folder!");
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
