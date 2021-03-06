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
package de.fub.maps.gpx.analysis.ui;

import de.fub.gpxmodule.GPXDataObject;
import de.fub.gpxmodule.service.GPXProvider;
import de.fub.gpxmodule.xml.Gpx;
import de.fub.maps.gpx.analysis.models.GpxTrackSegmentStatistic;
import de.fub.maps.gpx.analysis.models.nodes.GpxRootNode;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.BeanInfo;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.TreeSelectionModel;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.openide.awt.UndoRedo;
import org.openide.explorer.ExplorerManager;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.TopComponent;

/**
 *
 * @author Serdar
 */
@MultiViewElement.Registration(
        displayName = "#LBL_GPX_ANALYSIS_VISUAL",
        //        iconBase = "de/fub/gpxmodule/gpx.png",
        mimeType = "text/gpx+xml",
        persistenceType = TopComponent.PERSISTENCE_NEVER,
        preferredID = "GPXAnalysisVisual",
        position = 550)
@NbBundle.Messages({"LBL_GPX_ANALYSIS_VISUAL=Analysis"})
public class GpxAnalysisVisualElement extends javax.swing.JPanel implements MultiViewElement, ExplorerManager.Provider, PropertyChangeListener, ChangeListener {

    private static final long serialVersionUID = 1L;
    private GPXDataObject obj;
    private MultiViewElementCallback callback;
    private final JToolBar toolbar = new JToolBar();
    private final ExplorerManager explorerManager = new ExplorerManager();
    private GPXProvider gpxProvide;
    private Lookup lookup;

    /**
     * Creates new form GpxAnalysisVisualElement
     */
    public GpxAnalysisVisualElement() {
        initComponents();

        beanTreeView1.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        explorerManager.addPropertyChangeListener(WeakListeners.propertyChange(GpxAnalysisVisualElement.this, explorerManager));

        toolbar.add(new JToolBar.Separator());
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = (int) (getSize().width * .33);
                beanTreeView1.setMaximumSize(new Dimension(width, 0));
                if (width > jSplitPane1.getDividerLocation()) {
                    jSplitPane1.setDividerLocation(width);
                }
            }
        });
    }

    public GpxAnalysisVisualElement(Lookup lkp) {
        this();
        obj = lkp.lookup(GPXDataObject.class);
        assert obj != null;
        lookup = new ProxyLookup(Lookups.fixed(gpxTrkSegAnalysizerTopComponent1), lkp);
        init();
    }

    private void init() {
        gpxProvide = obj.getLookup().lookup(GPXProvider.class);
        gpxProvide.addChangeListener(WeakListeners.change(GpxAnalysisVisualElement.this, gpxProvide));
        updateView();
    }

    private void updateView() {
        Gpx gpx = obj.getGpx();
        GpxRootNode gpxRootNode = new GpxRootNode(gpx);
        explorerManager.setRootContext(gpxRootNode);
    }

    @Override
    public String getName() {
        return "GPXAnalysisVisualElement";
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        beanTreeView1 = new org.openide.explorer.view.BeanTreeView();
        jPanel1 = new javax.swing.JPanel();
        gpxTrkSegAnalysizerTopComponent1 = new de.fub.maps.gpx.analysis.ui.GpxTrkSegAnalysizerTopComponent();

        setLayout(new java.awt.BorderLayout());

        jPanel2.setPreferredSize(new java.awt.Dimension(470, 584));
        jPanel2.setLayout(new java.awt.BorderLayout());

        beanTreeView1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        beanTreeView1.setRootVisible(false);
        jPanel2.add(beanTreeView1, java.awt.BorderLayout.CENTER);

        jSplitPane1.setLeftComponent(jPanel2);

        jPanel1.setLayout(new java.awt.BorderLayout());
        jPanel1.add(gpxTrkSegAnalysizerTopComponent1, java.awt.BorderLayout.CENTER);

        jSplitPane1.setRightComponent(jPanel1);

        add(jSplitPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.openide.explorer.view.BeanTreeView beanTreeView1;
    private de.fub.maps.gpx.analysis.ui.GpxTrkSegAnalysizerTopComponent gpxTrkSegAnalysizerTopComponent1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSplitPane jSplitPane1;
    // End of variables declaration//GEN-END:variables

    @Override
    public JComponent getVisualRepresentation() {
        return this;
    }

    @Override
    public JComponent getToolbarRepresentation() {
        return toolbar;
    }

    @Override
    public Action[] getActions() {
        Action[] retValue;
        // the multiviewObserver was passed to the element in setMultiViewCallback() method.
        if (callback != null) {
            retValue = callback.createDefaultActions();
            // add you own custom actions here..
        } else {
            // fallback..
            retValue = new Action[0];
        }
        return retValue;
    }

    @Override
    public Lookup getLookup() {
        return lookup != null ? lookup : Lookup.EMPTY;
    }

    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
    }

    @Override
    public void componentShowing() {
    }

    @Override
    public void componentHidden() {
    }

    @Override
    public void componentActivated() {
    }

    @Override
    public void componentDeactivated() {
    }

    @Override
    public UndoRedo getUndoRedo() {
        return UndoRedo.NONE;
    }

    @Override
    public void setMultiViewCallback(MultiViewElementCallback callback) {
        this.callback = callback;
        if (callback != null) {
            Image icon = obj.getNodeDelegate().getIcon(BeanInfo.ICON_COLOR_16x16);
            if (icon != null) {
                callback.getTopComponent().setIcon(icon);
            }
            callback.getTopComponent().setDisplayName(obj.getName());
        }
    }

    @Override
    public CloseOperationState canCloseElement() {
        return CloseOperationState.STATE_OK;
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (ExplorerManager.PROP_SELECTED_NODES.equals(evt.getPropertyName())) {
            Node[] selectedNodes = explorerManager.getSelectedNodes();
            GpxTrackSegmentStatistic statistic = null;

            if (selectedNodes.length == 1) {
                statistic = selectedNodes[0].getLookup().lookup(GpxTrackSegmentStatistic.class);
            }

            gpxTrkSegAnalysizerTopComponent1.setStatistic(statistic);

        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                updateView();
            }
        });
    }
}
