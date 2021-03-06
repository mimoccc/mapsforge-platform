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
package de.fub.maps.project.detector.model.inference.processhandler;

import de.fub.maps.project.detector.model.gpx.TrackSegment;
import de.fub.maps.project.detector.model.inference.AbstractInferenceModel;
import de.fub.maps.project.detector.model.inference.InferenceMode;
import de.fub.maps.project.detector.model.inference.InferenceModelInputDataSet;
import de.fub.maps.project.detector.model.inference.features.FeatureProcess;
import de.fub.maps.project.detector.model.inference.ui.InferenceResultPanel;
import de.fub.maps.project.detector.model.xmls.ProcessHandlerDescriptor;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

/**
 * This is a special implementation of the clustering processhander. Instead of
 * a specified inference set it will use the trainingsset and labels each
 * tracksegment with its transportation mode.
 *
 * This processhandler is only for the use of computing the the accuracy for
 * each transport mode
 *
 * @author Serdar
 */
@NbBundle.Messages({
    "LBL_Detector_special_clustering_Title=Clustering",
    "CLT_Special_InferenceDataProcessHandler_Name=Inference ProcessHandler",
    "CLT_Special_InferenceDataProcessHandler_Description=No description available"
})
@ServiceProvider(service = InferenceModelProcessHandler.class)
public class SpecialInferenceDataProcessHandler extends InferenceModelProcessHandler {

    private InferenceResultPanel inferenceResultPanel = null;
    // key = class/transport mode name, value = list of instance whose label is the key;
    // don't acces this member directly. use the respective methods
    private final HashMap<String, List<Instance>> resultMap = new HashMap<String, List<Instance>>();
    // helper map to map instances to the original dataset
    private final HashMap<Instance, TrackSegment> instanceToTrackSegmentMap = new HashMap<Instance, TrackSegment>();

    public SpecialInferenceDataProcessHandler() {
        super(null);
    }

    public SpecialInferenceDataProcessHandler(AbstractInferenceModel inferenceModel) {
        super(inferenceModel);
    }

    private void setClassesToView(Collection<String> classes) {
        ArrayList<String> arrayList = new ArrayList<String>(classes);
        Collections.sort(arrayList);
        for (String string : arrayList) {
            resultMap.put(string, new ArrayList<Instance>());
        }
        updateVisualRepresentation();
    }

    @Override
    protected void handle() {
        clearResults();

        Classifier classifier = getInferenceModel().getClassifier();
        Collection<Attribute> attributeList = getInferenceModel().getAttributes();

        if (!attributeList.isEmpty()) {
            Set<String> keySet = getInferenceModel().getInput().getTrainingsSet().keySet();
            setClassesToView(keySet);

            Instances unlabeledInstances = new Instances("Unlabeld Tracks", new ArrayList<Attribute>(attributeList), 0); //NO18N
            unlabeledInstances.setClassIndex(0);

            ArrayList<TrackSegment> segmentList = new ArrayList<TrackSegment>();
            for (Entry<String, HashSet<TrackSegment>> entry : getInferenceModel().getInput().getTrainingsSet().entrySet()) {
                for (TrackSegment segment : entry.getValue()) {
                    segment.setLabel(entry.getKey());
                    Instance instance = getInstance(segment);
                    unlabeledInstances.add(instance);
                    segmentList.add(segment);
                }
            }

            // create copy
            Instances labeledInstances = new Instances(unlabeledInstances);

            for (int index = 0; index < labeledInstances.numInstances(); index++) {
                try {
                    Instance instance = labeledInstances.instance(index);

                    // classify instance
                    double classifyed = classifier.classifyInstance(instance);
                    instance.setClassValue(classifyed);

                    // get class label
                    String value = unlabeledInstances.classAttribute().value((int) classifyed);

                    if (index < segmentList.size()) {
                        instanceToTrackSegmentMap.put(instance, segmentList.get(index));
                    }

                    // put label and instance to result map
                    put(value, instance);

                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                }
            }

            // update visw
            updateVisualRepresentation();

            // update result set of the inferenceModel
            for (Map.Entry<String, List<Instance>> entry : resultMap.entrySet()) {
                HashSet<TrackSegment> trackSegmentList = new HashSet<TrackSegment>();
                for (Instance instance : entry.getValue()) {
                    TrackSegment trackSegment = instanceToTrackSegmentMap.get(instance);
                    if (trackSegment != null) {
                        trackSegmentList.add(trackSegment);
                    }
                }

                // only those classes are put into  the result data set, which are not empty
                if (!trackSegmentList.isEmpty()) {
                    getInferenceModel().getResult().put(entry.getKey(), trackSegmentList);
                }
            }
        } else {
            throw new InferenceModelClassifyException(MessageFormat.format("No attributes available. Attribute list lengeth == {0}", attributeList.size()));
        }
        resultMap.clear();
        instanceToTrackSegmentMap.clear();
    }

    protected void updateVisualRepresentation() {
        final HashMap<String, List<Instance>> transportModeToInstanceMap = new HashMap<String, List<Instance>>(resultMap);
        final HashMap<Instance, TrackSegment> instanceTrackSegmentMap = new HashMap<Instance, TrackSegment>(instanceToTrackSegmentMap);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                getInferenceResultPanel().updateView(new InferenceDataProcessHandler.ClassificationResult(transportModeToInstanceMap, instanceTrackSegmentMap)); // TODO
            }
        });
    }

    private void put(String className, Instance instance) {
        if (!resultMap.containsKey(className)) {
            resultMap.put(className, new ArrayList<Instance>());
        }
        resultMap.get(className).add(instance);
    }

    private void clearResults() {
        getInferenceModel().getResult().clear();
        resultMap.clear();
        instanceToTrackSegmentMap.clear();
    }

    public HashSet<TrackSegment> getInferenceDataSet() {
        InferenceModelInputDataSet input = getInferenceModel().getInput();
        HashSet<TrackSegment> dataset = input.getInferenceSet();
        return dataset;
    }

    @Override
    public JComponent getVisualRepresentation() {
        return getInferenceResultPanel();
    }

    private InferenceResultPanel getInferenceResultPanel() {
        if (inferenceResultPanel == null) {
            inferenceResultPanel = new InferenceResultPanel();
            inferenceResultPanel.getTitle().setText(Bundle.LBL_Detector_clustering_Title());
        }
        return inferenceResultPanel;
    }

    private Instance getInstance(TrackSegment segment) {
        Instance instance = new DenseInstance(getInferenceModel().getAttributes().size());

        for (FeatureProcess feature : getInferenceModel().getFeatureList()) {
            feature.setInput(segment);
            feature.run();
            String featureName = feature.getName();
            Attribute attribute = getInferenceModel().getAttributeMap().get(featureName);
            Double result = feature.getResult();
            instance.setValue(attribute, result);
        }
        return instance;
    }

    @Override
    protected ProcessHandlerDescriptor createDefaultDescriptor() {
        ProcessHandlerDescriptor descriptor = new ProcessHandlerDescriptor();
        descriptor.setJavaType(InferenceDataProcessHandler.class.getName());
        descriptor.setInferenceMode(InferenceMode.INFERENCE_MODE);
        descriptor.setName(Bundle.CLT_InferenceDataProcessHandler_Name());
        descriptor.setDescription(Bundle.CLT_InferenceDataProcessHandler_Description());
        return descriptor;
    }

    public static class ClassificationResult {

        private final HashMap<String, List<Instance>> resultMap;
        private final HashMap<Instance, TrackSegment> instanceToTrackSegmentMap;

        public ClassificationResult(HashMap<String, List<Instance>> resultMap, HashMap<Instance, TrackSegment> instanceToTrackSegmentMap) {
            this.instanceToTrackSegmentMap = instanceToTrackSegmentMap;
            this.resultMap = resultMap;
        }

        public Map<String, List<Instance>> getResultMap() {
            return Collections.unmodifiableMap(resultMap);
        }

        public HashMap<Instance, TrackSegment> getInstanceToTrackSegmentMap() {
            return instanceToTrackSegmentMap;
        }
    }
}
