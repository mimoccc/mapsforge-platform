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

import de.fub.maps.project.detector.factories.nodes.datasets.DataSetNode;
import de.fub.maps.project.detector.model.Detector;
import de.fub.maps.project.detector.model.xmls.DataSet;
import de.fub.maps.project.detector.model.xmls.InferenceSet;
import de.fub.utilsmodule.Collections.ObservableArrayList;
import de.fub.utilsmodule.icons.IconRegister;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.ChangeSupport;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Serdar
 */
public class InferenceDatasetComponent extends javax.swing.JPanel implements ExplorerManager.Provider, Lookup.Provider {

    private static final long serialVersionUID = 1L;
    private final ExplorerManager explorerManager = new ExplorerManager();
    private final Lookup lookup = ExplorerUtils.createLookup(explorerManager, getActionMap());
    private Lookup context;
    private Detector detector;

    /**
     * Creates new form InferenceDatasetComponent
     */
    public InferenceDatasetComponent() {
        initComponents();
        beanTreeView1.setRootVisible(true);
    }

    public InferenceDatasetComponent(Lookup context) {
        this();
        this.context = context;
        detector = context.lookup(Detector.class);
        explorerManager.setRootContext(new RootNode(new InferenceDatasetWrapper(detector)));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        beanTreeView1 = new org.openide.explorer.view.BeanTreeView();

        setLayout(new java.awt.BorderLayout());

        beanTreeView1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        add(beanTreeView1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.openide.explorer.view.BeanTreeView beanTreeView1;
    // End of variables declaration//GEN-END:variables

    @Override
    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    private static class InferenceDatasetWrapper implements ChangeListener {

        private final ChangeSupport changeSupport = new ChangeSupport(this);
        private final ObservableArrayList<DataSet> dataset = new ObservableArrayList<DataSet>();
        private final Detector detector;
        private final InferenceSet inferenceSet;

        public InferenceDatasetWrapper(Detector detector) {
            this.detector = detector;
            this.inferenceSet = detector.getDetectorDescriptor().getDatasets().getInferenceSet();
            dataset.addAll(inferenceSet.getDatasetList());
            dataset.addChangeListener(WeakListeners.change(InferenceDatasetWrapper.this, dataset));
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            inferenceSet.getDatasetList().clear();
            inferenceSet.getDatasetList().addAll(dataset);
            fireChange();
        }

        public void fireChange() {
            changeSupport.fireChange();
        }

        public void addChangeListener(ChangeListener listener) {
            changeSupport.addChangeListener(listener);
        }

        public void removeChangeListener(ChangeListener listener) {
            changeSupport.removeChangeListener(listener);
        }
    }

    private static class RootNode extends AbstractNode {

        private final InferenceDatasetWrapper dataset;

        public RootNode(InferenceDatasetWrapper dataset) {
            super(Children.create(new DataSetNodeFactory(dataset), true), Lookups.fixed(dataset));
            this.dataset = dataset;
            setDisplayName(NbBundle.getMessage(InferenceDatasetComponent.class, "InferenceDatasetComponent.CLT_RootNode_Name"));
        }

        @Override
        public Action[] getActions(boolean context) {
            return new Action[]{new AddDatasetAction(dataset), new RemoveAllDatasetAction(dataset)};
        }

        @Override
        public Image getIcon(int type) {
            Image image = IconRegister.getFolderIcon();
            return image;
        }

        @Override
        public Image getOpenedIcon(int type) {
            return getIcon(type);
        }
    }

    private static class DataSetNodeFactory extends ChildFactory<DataSet> implements ChangeListener {

        private final InferenceDatasetWrapper dataset;

        public DataSetNodeFactory(InferenceDatasetWrapper dataset) {
            this.dataset = dataset;
            this.dataset.addChangeListener(WeakListeners.change(DataSetNodeFactory.this, this.dataset));
        }

        @Override
        protected boolean createKeys(List<DataSet> toPopulate) {
            toPopulate.addAll(this.dataset.dataset);
            return true;
        }

        @Override
        protected Node createNodeForKey(final DataSet key) {
            return new FilterNode(new DataSetNode(dataset.detector, key)) {
                @Override
                public Action[] getActions(boolean context) {
                    return new Action[]{new RemoveDatasetAction(key, dataset)};
                }
            };
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            refresh(true);
        }
    }

    private static class AddDatasetAction extends AbstractAction {

        private static final long serialVersionUID = 1L;
        private FolderExplorer folderExplorer;
        private final InferenceDatasetWrapper dataset;

        public AddDatasetAction(InferenceDatasetWrapper dataset) {
            super(NbBundle.getMessage(InferenceDatasetComponent.class, "TrainingDatasetComponent.CLT_AddDatasetAction_Name"));
            this.dataset = dataset;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            folderExplorer = new FolderExplorer();
            DialogDescriptor dd = new DialogDescriptor(folderExplorer, "Datasources");
            DialogDisplayer.getDefault().createDialog(dd).setVisible(true);
            if (DialogDescriptor.OK_OPTION.equals(dd.getValue())) {
                Node[] selectedNodes = folderExplorer.getSelectedFiles();

                for (Node node : selectedNodes) {
                    DataObject dataObject = node.getLookup().lookup(DataObject.class);
                    dataset.dataset.addAll(collectData(dataObject));
                }
            }
        }

        private List<DataSet> collectData(DataObject dataObject) {
            List<DataSet> result = new LinkedList<DataSet>();
            if (dataObject.getPrimaryFile().isFolder()) {
                for (FileObject fileObject : dataObject.getPrimaryFile().getChildren()) {
                    try {
                        result.addAll(collectData(DataObject.find(fileObject)));
                    } catch (DataObjectNotFoundException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            } else if (dataObject.getPrimaryFile().isData()) {
                result.add(new DataSet(dataObject.getPrimaryFile().getPath()));
            }
            return result;
        }
    }

    private static class RemoveDatasetAction extends AbstractAction {

        private static final long serialVersionUID = 1L;
        private final InferenceDatasetWrapper dataset;
        private final DataSet data;

        public RemoveDatasetAction(DataSet data, InferenceDatasetWrapper dataset) {
            super(NbBundle.getMessage(InferenceDatasetComponent.class, "TrainingDatasetComponent.CLT_RemoveDatasetAction_Name"));
            this.data = data;
            this.dataset = dataset;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            NotifyDescriptor nd = new NotifyDescriptor.Confirmation(
                    NbBundle.getMessage(InferenceDatasetComponent.class, "TrainingDatasetComponent.CLT_RemoveDatasetAction_Text"),
                    NbBundle.getMessage(InferenceDatasetComponent.class, "TrainingDatasetComponent.CLT_RemoveDatasetAction_Title"),
                    NotifyDescriptor.Confirmation.YES_NO_OPTION);
            Object notify = DialogDisplayer.getDefault().notify(nd);
            if (NotifyDescriptor.YES_OPTION == notify) {
                dataset.dataset.remove(data);
                dataset.fireChange();
            }
        }
    }

    private static class RemoveAllDatasetAction extends AbstractAction {

        private static final long serialVersionUID = 1L;
        private final InferenceDatasetWrapper dataset;

        public RemoveAllDatasetAction(InferenceDatasetWrapper dataset) {
            super(NbBundle.getMessage(InferenceDatasetComponent.class, "TrainingDatasetComponent.CLT_RemoveAllDatasetItemsAction_Name"));
            this.dataset = dataset;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            dataset.dataset.clear();
            dataset.fireChange();
        }
    }
}
