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

import de.fub.maps.project.detector.model.xmls.TransportMode;
import de.fub.utilsmodule.Collections.ObservableArrayList;
import de.fub.utilsmodule.Collections.ObservableList;
import de.fub.utilsmodule.icons.IconRegister;
import java.awt.Image;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.loaders.DataObject;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

@NbBundle.Messages({
    "CLT_TransportMode_And_Trainingset=Transport modes & Trainingsset"
})
public final class TrainingSetSelectionVisualPanel extends JPanel implements ExplorerManager.Provider, Lookup.Provider {

    private static final long serialVersionUID = 1L;
    private final ExplorerManager explorerManager = new ExplorerManager();
    private Lookup lookup = ExplorerUtils.createLookup(explorerManager, getActionMap());
    private HashMap<String, List<Node>> map = new HashMap<String, List<Node>>();

    /**
     * Creates new form TrainingSetSelectionVisualPanel
     */
    public TrainingSetSelectionVisualPanel() {
        initComponents();
        beanTreeView1.setRootVisible(true);
        RootNode rootNode = new RootNode();
        getExplorerManager().setRootContext(rootNode);
    }

    @Override
    public String getName() {
        return Bundle.CLT_TransportMode_And_Trainingset();
    }

    public HashMap<String, List<Node>> getTransportModeMap() {
        map.clear();
        Node rootContext = getExplorerManager().getRootContext();
        for (Node node : rootContext.getChildren().getNodes(true)) {
            String transportMode = node.getDisplayName();
            map.put(transportMode, Arrays.asList(node.getChildren().getNodes(true)));
        }
        return map;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        beanTreeView1 = new org.openide.explorer.view.BeanTreeView();

        setPreferredSize(new java.awt.Dimension(400, 300));
        setLayout(new java.awt.BorderLayout());

        beanTreeView1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
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

    public static class RootNode extends AbstractNode {

        public static final String ACTION_PATH = "maps/Detector/Wizard/Traningsset/Actions";
        private final ObservableList<String> transportModeNameList;
        private final InstanceContent content;

        public RootNode() {
            this(new ObservableArrayList<String>(), new InstanceContent());
        }

        private RootNode(ObservableList<String> transportModeNameList, InstanceContent content) {
            super(Children.create(new TransportNodeFactory(transportModeNameList), true), new AbstractLookup(content));
            setDisplayName("Transport Modes");
            this.transportModeNameList = transportModeNameList;
            this.content = content;
            this.content.add(RootNode.this);
        }

        @Override
        public Image getIcon(int type) {
            Image image = IconRegister.getFolderIcon();
            if (image == null) {
                super.getIcon(type);
            }
            return image;
        }

        @Override
        public Image getOpenedIcon(int type) {
            return getIcon(type);
        }

        @Override
        public Action[] getActions(boolean context) {
            List<? extends Action> actionsForPath = Utilities.actionsForPath(ACTION_PATH);
            return actionsForPath.toArray(new Action[actionsForPath.size()]);
        }

        public ObservableList<String> getTransportModeNameList() {
            return transportModeNameList;
        }
    }

    public static class TransportNodeFactory extends ChildFactory<String> implements ChangeListener {

        private final ObservableList<String> transportModeNameList;

        public TransportNodeFactory(ObservableList<String> transportModeNames) {
            this.transportModeNameList = transportModeNames;
            transportModeNames.addChangeListener(TransportNodeFactory.this);
        }

        @Override
        protected boolean createKeys(List<String> list) {
            list.addAll(transportModeNameList);
            return true;
        }

        @Override
        protected Node createNodeForKey(String transportModeName) {
            TransportMode transportMode = new TransportMode(transportModeName);
            return new TransportModeFilterNode(transportMode, transportModeNameList);
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            refresh(true);
        }
    }

    private static class DataSetNodeFactory extends ChildFactory<DataObject> implements ChangeListener {

        private final ObservableList<DataObject> list;

        private DataSetNodeFactory(ObservableList<DataObject> list) {
            this.list = list;
            list.addChangeListener(WeakListeners.change(DataSetNodeFactory.this, list));
        }

        @Override
        protected boolean createKeys(List<DataObject> toPopulate) {
            toPopulate.addAll(list);
            return true;
        }

        @Override
        protected Node createNodeForKey(DataObject key) {
            return new FilterNode(key.getNodeDelegate()) {
                @Override
                public Action[] getActions(boolean context) {
                    return new Action[0];
                }
            };
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            refresh(true);
        }
    }

    public static class TransportModeFilterNode extends AbstractNode {

        public static final String ACTION_PATH = "maps/Detector/Wizard/Traningsset/Transportmode/Actions";
        private final InstanceContent content;
        private final TransportMode transportMode;
        private final ObservableList<String> transportNodeNameList;

        public TransportModeFilterNode(
                TransportMode transportMode,
                ObservableList<String> transportModeNameList) {
            this(transportMode,
                    transportModeNameList,
                    new InstanceContent(),
                    new ObservableArrayList<DataObject>());
        }

        private TransportModeFilterNode(
                TransportMode transportMode,
                ObservableList<String> transportModeNameList,
                InstanceContent content,
                ObservableList<DataObject> dataObjectList) {
            super(Children.create(new DataSetNodeFactory(dataObjectList), true), new AbstractLookup(content));
            setDisplayName(transportMode.getName());
            this.content = content;
            this.content.add(TransportModeFilterNode.this);
            this.content.add(dataObjectList);
            this.transportMode = transportMode;
            this.transportNodeNameList = transportModeNameList;
        }

        @Override
        public Action[] getActions(boolean context) {
            List<? extends Action> actionsForPath = Utilities.actionsForPath(ACTION_PATH);
            return actionsForPath.toArray(new Action[actionsForPath.size()]);
        }

        public TransportMode getTransportMode() {
            return transportMode;
        }

        public ObservableList<String> getTransportNodeNameList() {
            return transportNodeNameList;
        }

        @Override
        public Image getIcon(int type) {
            Image image = IconRegister.getFolderIcon();
            if (image == null) {
                image = super.getIcon(type);
            }
            return image;
        }

        @Override
        public Image getOpenedIcon(int type) {
            return getIcon(type);
        }
    }
}
