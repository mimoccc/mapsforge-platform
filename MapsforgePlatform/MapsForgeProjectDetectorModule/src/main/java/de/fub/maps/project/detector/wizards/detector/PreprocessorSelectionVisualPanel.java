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

import de.fub.maps.project.detector.model.pipeline.preprocessors.FilterProcess;
import de.fub.maps.project.detector.model.process.DetectorProcess;
import de.fub.utilsmodule.Collections.ObservableList;
import javax.swing.JPanel;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

@NbBundle.Messages({
    "CLT_Preprocessors_Panel_Name=Preprocessors",
    "CLT_All_Available_Preprocessors=All Preprocessor Filters",
    "CLT_Selected_Preprocessors=Selected Preprocessor Filters"
})
public final class PreprocessorSelectionVisualPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * Creates new form PreprocessorSelectionVisualPanel
     */
    public PreprocessorSelectionVisualPanel() {
        initComponents();
        init();
    }

    private void init() {
        selectionComponent.getAllItemListTitle().setText(Bundle.CLT_All_Available_Preprocessors());
        selectionComponent.getSelectedItemListTitle().setText(Bundle.CLT_Selected_Preprocessors());
        getAllFilters().addAll(Lookup.getDefault().lookupResult(FilterProcess.class).allInstances());
    }

    public ObservableList<DetectorProcess> getAllFilters() {
        return selectionComponent.getAllItems();
    }

    public ObservableList<DetectorProcess> getSelectedFilters() {
        return selectionComponent.getSelectedItems();
    }

    @Override
    public String getName() {
        return Bundle.CLT_Preprocessors_Panel_Name();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        selectionComponent = new de.fub.maps.project.detector.ui.SelectionComponent();

        setLayout(new java.awt.BorderLayout());
        add(selectionComponent, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private de.fub.maps.project.detector.ui.SelectionComponent selectionComponent;
    // End of variables declaration//GEN-END:variables
}
