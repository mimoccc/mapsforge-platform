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

import de.fub.maps.project.detector.model.inference.features.FeatureProcess;
import de.fub.maps.project.detector.model.process.DetectorProcess;
import de.fub.utilsmodule.Collections.ObservableList;
import javax.swing.JPanel;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

@NbBundle.Messages({
    "CLT_InferenceModelFeatures_Name=Features",
    "CLT_All_Features_Title=All Available Features",
    "CLT_Selected_Features_Title=Selected Features"
})
public final class InferenceModelFeatureSelectionVisualPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * Creates new form InferenceModelFeatureSelectionVisualPanel
     */
    public InferenceModelFeatureSelectionVisualPanel() {
        initComponents();
        getAllFeatures().addAll(Lookup.getDefault().lookupResult(FeatureProcess.class).allInstances());
        selectionComponent.getAllItemListTitle().setText(Bundle.CLT_All_Features_Title());
        selectionComponent.getSelectedItemListTitle().setText(Bundle.CLT_Selected_Features_Title());
    }

    @Override
    public String getName() {
        return Bundle.CLT_InferenceModelFeatures_Name();
    }

    public ObservableList<DetectorProcess> getAllFeatures() {
        return selectionComponent.getAllItems();
    }

    public ObservableList<DetectorProcess> getSelectedFeatures() {
        return selectionComponent.getSelectedItems();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        selectionComponent = new de.fub.maps.project.detector.ui.SelectionComponent();

        setPreferredSize(new java.awt.Dimension(400, 300));
        setLayout(new java.awt.BorderLayout());
        add(selectionComponent, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private de.fub.maps.project.detector.ui.SelectionComponent selectionComponent;
    // End of variables declaration//GEN-END:variables
}
