/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fub.mapsforge.project.detector.model.pipeline.preprocessors.filters;

import de.fub.agg2graph.gpseval.data.Waypoint;
import de.fub.mapsforge.project.detector.model.gpx.TrackSegment;
import de.fub.mapsforge.project.detector.model.pipeline.preprocessors.FilterProcess;
import de.fub.mapsforge.project.detector.model.xmls.ProcessDescriptor;
import de.fub.mapsforge.project.detector.model.xmls.Property;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Serdar
 */
@Messages({"CLT_UniformDurationFilter_Name=Uniform Duration Segmentation",
    "CLT_UniformDurationFilter_Description=GPS tracks will be segmented into tracks whose duration time does not exceeds the specified duration (seconds) limit property.",
    "CLT_UniformDurationFilter_Property_MaxDuration_Name=Maximum Duration",
    "CLT_UniformDurationFilter_Property_MaxDuration_Description=Specifies the maximum duration for each gps track."
})
@ServiceProvider(service = FilterProcess.class)
public class UniformDurationSegmentationFilterProcess extends FilterProcess {

    private static final String PROP_NAME_DURATION = "uniform.duration.filter.duration";
    private List<TrackSegment> result;
    private List<TrackSegment> gpsTracks;
    // duration in seconds;
    private double duration = -1;

    public UniformDurationSegmentationFilterProcess() {
    }

    @Override
    protected void start() {
        if (gpsTracks != null) {
            if (result == null) {
                result = new ArrayList<TrackSegment>(gpsTracks.size());
            }
            result.clear();

            for (TrackSegment trackSegment : gpsTracks) {
                Waypoint lastWaypoint = null;
                long currentDuration = 0;
                TrackSegment currentSegment = new TrackSegment();

                for (Waypoint waypoint : trackSegment.getWayPointList()) {
                    if (lastWaypoint != null) {
                        // get time difference in seconds and add to current duration
                        currentDuration += (waypoint.getTimestamp().getTime() - lastWaypoint.getTimestamp().getTime()) / 1000;

                        // if currenDuration exceeds the duration limit
                        // then add current segment to result and reset
                        // variables, otherwise add waypoint to current segment
                        if (currentDuration > getDurationProperty()) {
                            result.add(currentSegment);
                            currentDuration = 0;
                            currentSegment = new TrackSegment();
                        } else {
                            currentSegment.add(waypoint);
                        }
                    }
                    lastWaypoint = waypoint;
                }

                if (!result.contains(currentSegment)) {
                    result.add(currentSegment);
                }
            }

        }
    }

    @Override
    public String getName() {
        if (getProcessDescriptor() != null && getProcessDescriptor().getName() != null) {
            return getProcessDescriptor().getName();
        }
        return Bundle.CLT_UniformDurationFilter_Name();
    }

    @Override
    public String getDescription() {
        if (getProcessDescriptor() != null && getProcessDescriptor().getName() != null) {
            return getProcessDescriptor().getName();
        }
        return Bundle.CLT_UniformDurationFilter_Description();
    }

    @Override
    public void setInput(List<TrackSegment> input) {
        this.gpsTracks = input;
    }

    @Override
    public List<TrackSegment> getResult() {
        List<TrackSegment> arrayList = this.result;
        this.result = null;
        this.gpsTracks = null;
        return arrayList;

    }

    private double getDurationProperty() {
        if (duration < 0) {
            List<Property> propertyList = getProcessDescriptor().getProperties().getPropertyList();
            for (Property property : propertyList) {
                if (PROP_NAME_DURATION.equals(property.getId())) {
                    if (property.getValue() != null) {
                        duration = Double.valueOf(property.getValue());
                    } else {
                        // duration in seconds (5 min default)
                        duration = 300;
                    }
                    break;
                }
            }
        }
        return duration;
    }

    @Override
    protected ProcessDescriptor createProcessDescriptor() {
        ProcessDescriptor descriptor = new ProcessDescriptor();
        descriptor.setJavaType(UniformDurationSegmentationFilterProcess.class.getName());
        descriptor.setName(Bundle.CLT_UniformDurationFilter_Name());
        descriptor.setDescription(Bundle.CLT_UniformDurationFilter_Description());

        // <!-- duration value in seconds -->
        Property property = new Property();
        property.setId(PROP_NAME_DURATION);
        property.setJavaType(Double.class.getName());
        property.setValue("300");
        property.setName(Bundle.CLT_UniformDurationFilter_Property_MaxDuration_Name());
        property.setDescription(Bundle.CLT_UniformDurationFilter_Property_MaxDuration_Description());
        descriptor.getProperties().getPropertyList().add(property);

        return descriptor;
    }
}
