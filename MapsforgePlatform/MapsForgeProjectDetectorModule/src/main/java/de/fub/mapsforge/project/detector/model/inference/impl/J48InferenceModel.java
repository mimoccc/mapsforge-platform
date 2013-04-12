/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fub.mapsforge.project.detector.model.inference.impl;

import de.fub.mapsforge.project.detector.model.Detector;
import de.fub.mapsforge.project.detector.model.inference.AbstractInferenceModel;
import de.fub.mapsforge.project.detector.model.xmls.InferenceModelDescriptor;
import de.fub.mapsforge.project.detector.model.xmls.Property;
import de.fub.mapsforge.project.detector.model.xmls.Section;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JToolBar;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;

/**
 *
 * @author Serdar
 */
@NbBundle.Messages({
    "CLT_J48InferenceModel_Name=J48 Inference Model",
    "CLT_J48InferenceModel_Description=Inference Model which uses a J48 classifier that generates an unpruned or a pruned C4.5 decision tree."
})
@ServiceProvider(service = AbstractInferenceModel.class)
public class J48InferenceModel extends AbstractInferenceModel {

    private static final Logger LOG = Logger.getLogger(J48InferenceModel.class.getName());
    protected static final String PROPERTY_UNPRUNED = "j48.unpruned";
    protected static final String PROPERTY_CONFIDENCE = "j48.confidence";
    protected static final String PROPERTY_MINIMUM_INSTANCES = "j48.minimum.instances.per.leaf";
    protected static final String PROPERTY_REDUCED_ERROR_PRUNING = "j48.reduced.error.pruning";
    protected static final String PROPERTY_FOLDS = "j48.folds";
    protected static final String PROPERTY_BINARY_SPLITS = "j48.binary.splits";
    protected static final String PROPERTY_NO_SUBTREE_RAISING = "j48.no.subtree.raising";
    protected static final String PROPERTY_NO_CLEAN_UP = "j48.no.clean.up";
    protected static final String PROPERTY_SEED = "j48.seed";
    private JToolBar toolbar = null;
    private J48 classifierJ48 = null;

    public J48InferenceModel() {
        this(null);
    }

    public J48InferenceModel(Detector detector) {
        super(detector);
    }

    private void initToolBar() {
        toolbar.setFloatable(false);
        toolbar.add(new JButton(new ShowGraphAction(J48InferenceModel.this)));
    }

    @Override
    public JToolBar getToolbarRepresenter() {
        if (toolbar == null) {
            toolbar = new JToolBar();
            initToolBar();
        }
        return toolbar;
    }

    @Override
    protected final synchronized Classifier createClassifier() {
        classifierJ48 = new J48();
        configureClassifier();
        return classifierJ48;
    }

    protected void configureClassifier() {
        InferenceModelDescriptor inferenceModelDescriptor = getInferenceModelDescriptor();
        if (inferenceModelDescriptor != null) {
            for (Section propertySection : inferenceModelDescriptor.getPropertySection().getSectionList()) {
                if (OPTIONS_PROPERTY_SECTION.equals(propertySection.getId())) {
                    ArrayList<String> optionList = new ArrayList<String>();
                    for (Property property : propertySection.getPropertyList()) {
                        try {
                            if (PROPERTY_BINARY_SPLITS.equals(property.getId())) {
                                boolean splits = Boolean.parseBoolean(property.getValue());
                                if (splits) {
                                    optionList.add("B");
                                }
                            } else if (PROPERTY_CONFIDENCE.equals(property.getId())) {
                                optionList.add(String.format(Locale.ENGLISH, "-C {0}", property.getValue()));
                            } else if (PROPERTY_FOLDS.equals(property.getId())) {
                                optionList.add(MessageFormat.format("-N {0}", property.getValue()));
                            } else if (PROPERTY_MINIMUM_INSTANCES.equals(property.getId())) {
                                optionList.add(MessageFormat.format("-M {0}", property.getValue()));
                            } else if (PROPERTY_NO_CLEAN_UP.equals(property.getId())) {
                                boolean parseBoolean = Boolean.parseBoolean(property.getValue());
                                if (parseBoolean) {
                                    optionList.add("-L");
                                }
                            } else if (PROPERTY_NO_SUBTREE_RAISING.equals(property.getId())) {
                                boolean parseBoolean = Boolean.parseBoolean(property.getValue());
                                if (parseBoolean) {
                                    optionList.add("-S");
                                }
                            } else if (PROPERTY_REDUCED_ERROR_PRUNING.equals(property.getId())) {
                                boolean parseBoolean = Boolean.parseBoolean(property.getValue());
                                if (parseBoolean) {
                                    optionList.add("-R");
                                }
                            } else if (PROPERTY_SEED.equals(property.getId())) {
                                optionList.add(MessageFormat.format("-Q {0}", property.getValue()));
                            } else if (PROPERTY_UNPRUNED.equals(property.getId())) {
                                boolean parseBoolean = Boolean.parseBoolean(property.getValue());
                                if (parseBoolean) {
                                    optionList.add("-U");
                                }
                            }
                        } catch (IllegalArgumentException ex) {
                            LOG.log(Level.FINE, ex.getMessage(), ex);
                        }
                    }
                    try {
                        classifierJ48.setOptions(optionList.toArray(new String[optionList.size()]));
                    } catch (Exception ex) {
                        LOG.log(Level.SEVERE, ex.getMessage(), ex);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public String getName() {
        if (getInferenceModelDescriptor() != null) {
            return getInferenceModelDescriptor().getName();
        }
        return Bundle.CLT_J48InferenceModel_Name();
    }

    @Override
    public String getDescription() {
        if (getInferenceModelDescriptor() != null) {
            return getInferenceModelDescriptor().getDescription();
        }
        return Bundle.CLT_J48InferenceModel_Description();
    }
}
