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
package de.fub.maps.project.aggregator.actions.wizards.aggregator;

import de.fub.maps.project.MapsProject;
import de.fub.maps.project.datasource.MapsDatasourceNodeFactory;
import de.fub.maps.project.utils.AggregatorUtils;
import de.fub.maps.project.xml.Maps;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.bind.JAXBException;
import org.netbeans.api.project.Project;
import org.openide.explorer.ExplorerManager;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.nodes.FilterNode;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;

/**
 *
 * @author Serdar
 */
public class AggregatorVisualPanel3 extends javax.swing.JPanel implements ExplorerManager.Provider {

    private static final long serialVersionUID = 1L;
    private final ExplorerManager explorerManager = new ExplorerManager();
    private static final String FILE_NOT_FOUND_MESSAGE = "Datasource file path not found in module preference. Datasource module needs to register its path";

    /**
     * Creates new form AggregatorVisualPanel3
     */
    public AggregatorVisualPanel3() {
        initComponents();
        String datasourcefileObjectPath = alternativeMethod();
//        try {
//            // this way depends on the abailablity of the necessary data in the
//            // NbPreference module.
//            ClassLoader classLoader = Lookup.getDefault().lookup(ClassLoader.class);
//            if (classLoader != null) {
//                Preferences preferences = NbPreferences.forModule(classLoader.loadClass("de.fub.mapsforge.project.datasource.MapsDatasourceNodeFactory"));
//                datasourcefileObjectPath = preferences.get("GPX Datasource", null);
//                if (datasourcefileObjectPath == null) {
//                    throw new FileNotFoundException(FILE_NOT_FOUND_MESSAGE);
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            datasourcefileObjectPath = alternativeMethod();
//        } catch (FileNotFoundException ex) {
//            datasourcefileObjectPath = alternativeMethod();
//        }
        try {
            if (datasourcefileObjectPath != null) {
                File file = new File(datasourcefileObjectPath);
                if (file.exists()) {
                    FileObject fileObject = FileUtil.toFileObject(file);
                    DataObject dataObject1 = DataObject.find(fileObject);
                    explorerManager.setRootContext(new FilterNode(dataObject1.getNodeDelegate(), new FilterNode.Children(dataObject1.getNodeDelegate())));
                }
            } else {
                throw new FileNotFoundException(FILE_NOT_FOUND_MESSAGE);
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * A fall back method for the case that the datasource file path is not
     * registered in the module preference.
     *
     * This way is more complicated but doesn't need pre requirements like
     * registering the needed info in the NbPreference.
     */
    private String alternativeMethod() {
        String folderPath = null;
        DataObject dataObject = Utilities.actionsGlobalContext().lookup(DataObject.class);
        if (dataObject != null) {
            FileObject primaryFile = dataObject.getPrimaryFile();
            Project project = AggregatorUtils.findProject(primaryFile);
            if (project instanceof MapsProject) {
                MapsProject mapsForgeProject = (MapsProject) project;
                try {
                    Maps projectData = mapsForgeProject.getProjectData();
                    if (projectData != null) {
                        folderPath = projectData.getProjectFolders().getFolderPath(MapsDatasourceNodeFactory.DATASOURCE_FILENAME);
                        folderPath = mapsForgeProject.getProjectDirectory().getFileObject(folderPath).getPath();
                    }
                } catch (JAXBException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
        return folderPath;
    }

    @NbBundle.Messages("CLT_Choose_Datasources=Choose Datasources")
    @Override
    public String getName() {
        return Bundle.CLT_Choose_Datasources();
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

        setPreferredSize(new java.awt.Dimension(400, 300));
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
}
