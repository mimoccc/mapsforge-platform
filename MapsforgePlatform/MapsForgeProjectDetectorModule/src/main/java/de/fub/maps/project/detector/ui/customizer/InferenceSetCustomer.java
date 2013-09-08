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
package de.fub.maps.project.detector.ui.customizer;

import de.fub.maps.project.detector.ui.InferenceDatasetComponent;
import javax.swing.JComponent;
import org.netbeans.spi.project.ui.support.ProjectCustomizer;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

/**
 *
 * @author Serdar
 */
public class InferenceSetCustomer implements ProjectCustomizer.CompositeCategoryProvider {

    private static final String GENERAL = "Inference Dataset";

    @ProjectCustomizer.CompositeCategoryProvider.Registration(
            projectType = "org-maps-project/Detector", position = 100)
    public static InferenceSetCustomer createGeneral() {
        return new InferenceSetCustomer();
    }

    @NbBundle.Messages("LBL_Config_inferenceSet=Inference Dataset")
    @Override
    public ProjectCustomizer.Category createCategory(Lookup context) {
        return ProjectCustomizer.Category.create(
                GENERAL,
                Bundle.LBL_Config_inferenceSet(),
                null);
    }

    @Override
    public JComponent createComponent(ProjectCustomizer.Category category, Lookup context) {
        return new InferenceDatasetComponent(context);
    }
}
