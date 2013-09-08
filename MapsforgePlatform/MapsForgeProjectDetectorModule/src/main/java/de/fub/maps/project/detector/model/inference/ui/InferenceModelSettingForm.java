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
package de.fub.maps.project.detector.model.inference.ui;

import de.fub.maps.project.detector.model.inference.AbstractInferenceModel;
import de.fub.maps.project.detector.model.inference.features.FeatureProcess;
import de.fub.maps.project.detector.model.process.DetectorProcess;
import de.fub.maps.project.detector.model.xmls.InferenceModelDescriptor;
import de.fub.maps.project.detector.model.xmls.ProcessDescriptor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.Lookup;
import org.openide.util.WeakListeners;

/**
 *
 * @author Serdar
 */
public class InferenceModelSettingForm extends javax.swing.JPanel implements ActionListener, ChangeListener {

    private static final long serialVersionUID = 1L;
    private final AbstractInferenceModel inferenecModel;

    /**
     * Creates new form InferenceModelSettingForm
     */
    public InferenceModelSettingForm() {
        this(null);
    }

    public InferenceModelSettingForm(AbstractInferenceModel inferenceModel) {
        assert inferenceModel != null;
        initComponents();

        this.inferenecModel = inferenceModel;
        optionPanel1.setInferenceModel(inferenceModel);

        selectionComponent1.getAllItemListTitle().setText("All Features"); //NO18N
        selectionComponent1.getSelectedItemListTitle().setText("Selected Features"); // NO18N

        selectionComponent1.getAllItems().addAll(Lookup.getDefault().lookupResult(FeatureProcess.class).allInstances());
        Collection<FeatureProcess> featureList = inferenceModel.getFeatureList();

        for (DetectorProcess feature : featureList) {
            for (DetectorProcess f : selectionComponent1.getAllItems()) {
                if (feature.getClass().equals(f.getClass())) {
                    selectionComponent1.getSelectedItems().add(f);
                }
            }
        }
        selectionComponent1.getAllItems().removeAll(selectionComponent1.getSelectedItems());
        selectionComponent1.getSelectedItems().addChangeListener(WeakListeners.change(InferenceModelSettingForm.this, selectionComponent1.getSelectedListExplorerManager()));

//        for (InferenceMode mode : InferenceMode.values()) {
//            InferenceModelProcessHandler processHandlerInstance = inferenceModel.getProcessHandlerInstance(mode);
//            if (processHandlerInstance != null) {
//                switch (mode) {
//                    case TRAININGS_MODE:
//                        processHandlerPanel1.getTrainingProcessSelectionComponent().setSelectedProcessHandler(processHandlerInstance);
//                        break;
//                    case CROSS_VALIDATION_MODE:
//                        processHandlerPanel1.getCrossvalidationProcessSelectionComponent().setSelectedProcessHandler(processHandlerInstance);
//                        break;
//                    case INFERENCE_MODE:
//                        processHandlerPanel1.getInferenceProcessSelectionComponent().setSelectedProcessHandler(processHandlerInstance);
//                        break;
//                    case ALL_MODE: // do nothing in this case
//                        break;
//                    default:
//                        throw new AssertionError();
//                }
//            }
//        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        optionPanel1 = new de.fub.maps.project.detector.model.inference.ui.OptionPanel();
        selectionComponent1 = new de.fub.maps.project.detector.ui.SelectionComponent();

        setLayout(new java.awt.BorderLayout());

        jTabbedPane1.setPreferredSize(new java.awt.Dimension(700, 450));
        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(InferenceModelSettingForm.class, "InferenceModelSettingForm.optionPanel1.TabConstraints.tabTitle"), optionPanel1); // NOI18N

        selectionComponent1.setPreferredSize(new java.awt.Dimension(400, 450));
        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(InferenceModelSettingForm.class, "InferenceModelSettingForm.selectionComponent1.TabConstraints.tabTitle"), selectionComponent1); // NOI18N

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane jTabbedPane1;
    private de.fub.maps.project.detector.model.inference.ui.OptionPanel optionPanel1;
    private de.fub.maps.project.detector.ui.SelectionComponent selectionComponent1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void stateChanged(ChangeEvent evt) {
        if (evt.getSource().equals(selectionComponent1.getSelectedItems())) {
            InferenceModelDescriptor inferenceModelDescriptor = inferenecModel.getInferenceModelDescriptor();
            List<ProcessDescriptor> featureList = inferenceModelDescriptor.getFeatures().getFeatureList();
            featureList.clear();

            for (DetectorProcess item : selectionComponent1.getSelectedItems()) {
                if (item instanceof FeatureProcess) {
                    FeatureProcess featureProcess = (FeatureProcess) item;
                    ProcessDescriptor processDescriptor = featureProcess.getProcessDescriptor();
                    if (processDescriptor != null) {
                        featureList.add(processDescriptor);
                    }
                }
            }

        }
    }
}
