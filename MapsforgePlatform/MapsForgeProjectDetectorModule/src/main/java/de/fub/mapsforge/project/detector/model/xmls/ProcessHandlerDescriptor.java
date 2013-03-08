/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fub.mapsforge.project.detector.model.xmls;

import de.fub.mapsforge.project.detector.model.inference.InferenceMode;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Serdar
 */
@XmlType(name = "processHandler")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProcessHandlerDescriptor extends Descriptor {

    @XmlAttribute(name = "inferenceMode")
    private InferenceMode inferenceMode;

    public ProcessHandlerDescriptor() {
    }

    public ProcessHandlerDescriptor(InferenceMode inferenceMode, String javaType, String name, String description) {
        super(javaType, name, description);
        this.inferenceMode = inferenceMode;
    }

    public InferenceMode getInferenceMode() {
        return inferenceMode;
    }

    public void setInferenceMode(InferenceMode inferenceMode) {
        this.inferenceMode = inferenceMode;
    }

    @Override
    public String toString() {
        return "ProcessHandler{" + "inferenceMode=" + getInferenceMode()
                + ", javaType=" + getJavaType()
                + ", name=" + getName()
                + ", description=" + getDescription() + '}';
    }
}