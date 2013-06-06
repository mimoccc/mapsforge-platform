/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fub.mapsforge.project.detector.model.pipeline.preprocessors.filters;

import de.fub.mapsforge.project.detector.model.gpx.TrackSegment;
import de.fub.mapsforge.project.detector.model.inference.features.TrackLengthFeatureProcess;
import de.fub.mapsforge.project.detector.model.pipeline.preprocessors.FilterProcess;
import de.fub.mapsforge.project.detector.model.xmls.ProcessDescriptor;
import de.fub.mapsforge.project.detector.model.xmls.Property;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Serdar
 */
@NbBundle.Messages({
    "CLT_MinimumTrackLengthFilter_Name=Minimum Track Length Filter",
    "CLT_MinimumTrackLengthFilter_Description=This process filters all track segments whose length is shorter then the specified min track length parameter.",
    "CLT_MinimumTrackLengthFilter_Property_TrackLength_Name=Minimum track length",
    "CLT_MinimumTrackLengthFilter_Property_TrackLength_Description=Specifies the minimum length (in meters) that a track must have."
})
@ServiceProvider(service = FilterProcess.class)
public class MinimumTrackLengthFilterProcess extends FilterProcess {

    private static final String PROP_NAME_MIN_TRACK_LENGTH = "min.track.length";
    private Double minTrackLength = null;
    private List<TrackSegment> trackSegments;
    private TrackLengthFeatureProcess trackLengthfeature = new TrackLengthFeatureProcess();

    public MinimumTrackLengthFilterProcess() {
    }

    @Override
    protected void start() {
        for (TrackSegment trackSegment : new ArrayList<TrackSegment>(trackSegments)) {
            trackLengthfeature.setInput(trackSegment);
            trackLengthfeature.run();
            Double result = trackLengthfeature.getResult();
            if (result <= getMinTrackLengthParameter()) {
                trackSegments.remove(trackSegment);
            }
        }
        trackLengthfeature.setInput(null);

    }

    @Override
    public String getName() {
        if (getProcessDescriptor() != null && getProcessDescriptor().getName() != null) {
            return getProcessDescriptor().getName();
        }
        return Bundle.CLT_MinimumTrackLengthFilter_Name();
    }

    @Override
    public String getDescription() {
        if (getProcessDescriptor() != null && getProcessDescriptor().getDescription() != null) {
            return getProcessDescriptor().getDescription();
        }
        return Bundle.CLT_MinimumTrackLengthFilter_Description();
    }

    @Override
    public void setInput(List<TrackSegment> input) {
        this.trackSegments = input;
    }

    @Override
    public List<TrackSegment> getResult() {
        List<TrackSegment> list = trackSegments;
        trackSegments = null;
        return list;
    }

    private double getMinTrackLengthParameter() {
        if (minTrackLength == null) {
            List<Property> propertyList = getProcessDescriptor().getProperties().getPropertyList();
            for (Property property : propertyList) {
                if (PROP_NAME_MIN_TRACK_LENGTH.equals(property.getId()) && property.getValue() != null) {
                    try {
                        minTrackLength = Double.valueOf(property.getValue());
                    } catch (NumberFormatException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                    break;
                }
            }
            if (minTrackLength == null) {
                minTrackLength = 150d;
            }
        }
        return minTrackLength;
    }

    @Override
    protected ProcessDescriptor createProcessDescriptor() {
        ProcessDescriptor descriptor = new ProcessDescriptor();
        descriptor.setJavaType(MinimumTrackLengthFilterProcess.class.getName());
        descriptor.setName(Bundle.CLT_MinimumTrackLengthFilter_Name());
        descriptor.setDescription(Bundle.CLT_MinimumTrackLengthFilter_Description());

        Property property = new Property();
        property.setId(PROP_NAME_PROCESS_STATE);
        property.setJavaType(Double.class.getName());
        property.setValue("150");
        property.setName(Bundle.CLT_MinimumTrackLengthFilter_Property_TrackLength_Name());
        property.setDescription(Bundle.CLT_MinimumTrackLengthFilter_Property_TrackLength_Description());

        descriptor.getProperties().getPropertyList().add(property);

        return descriptor;
    }
}
