/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fub.mapsforge.project.detector.model.inference;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Serdar
 */
@XmlType(name = "inferenceMode")
@XmlEnum(String.class)
public enum InferenceMode {

    @XmlEnumValue("crossvalidationMode")
    CROSS_VALIDATION_MODE("Crossvalidation"),
    @XmlEnumValue("trainingsMode")
    TRAININGS_MODE("Training"),
    @XmlEnumValue("inferenceMode")
    INFERENCE_MODE("Inference"),
    ALL_MODE("All Mode");
    private String displayName;

    private InferenceMode(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    public static InferenceMode fromValue(String name) {
        InferenceMode mode = null;
        if ("crossvalidationMode".equals(name) || "Crossvalidation".equals(name)) {
            mode = InferenceMode.CROSS_VALIDATION_MODE;
        } else if ("trainingsMode".equals(name) || "Training".equals(name)) {
            mode = InferenceMode.TRAININGS_MODE;
        } else if ("inferenceMode".equals(name) || "Inference".equals(name)) {
            mode = InferenceMode.INFERENCE_MODE;
        } else {
            throw new IllegalArgumentException(name);
        }
        return mode;
    }
}
