/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fub.maps.project.detector.model.inference.features;

import de.fub.agg2graph.gpseval.data.Waypoint;
import de.fub.agg2graph.gpseval.features.MaxNVelocityFeature;
import de.fub.maps.project.detector.model.gpx.TrackSegment;
import de.fub.maps.project.detector.model.xmls.ProcessDescriptor;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Serdar
 */
@NbBundle.Messages({
    "CLT_Max3rdVelocityFeature_Name=Third Maximal Velocity",
    "CLT_Max3rdVelocityFeature_Description=Feature that computes the third highest Velocity value which a track contains."
})
@ServiceProvider(service = FeatureProcess.class)
public class Max3rdVelocityFeatureProcess extends FeatureProcess {

    private final MaxNVelocityFeature feature = new MaxNVelocityFeature(3);
    private TrackSegment gpsTrack;

    public Max3rdVelocityFeatureProcess() {
    }

    @Override
    protected void start() {
        feature.reset();
        if (gpsTrack != null) {
            for (Waypoint waypoint : gpsTrack.getWayPointList()) {
                feature.addWaypoint(waypoint);
            }
        }
    }

    @Override
    public String getName() {
        return Bundle.CLT_Max3rdVelocityFeature_Name();
    }

    @Override
    public String getDescription() {
        return Bundle.CLT_Max3rdVelocityFeature_Description();
    }

    @Override
    public void setInput(TrackSegment gpsTrack) {
        this.gpsTrack = gpsTrack;
    }

    @Override
    public Double getResult() {
        double result = feature.getResult();
        feature.reset();
        gpsTrack = null;
        return result;
    }

    @Override
    protected ProcessDescriptor createProcessDescriptor() {
        ProcessDescriptor descriptor = new ProcessDescriptor();
        descriptor.setJavaType(Max3rdVelocityFeatureProcess.class.getName());
        descriptor.setName(Bundle.CLT_Max3rdVelocityFeature_Name());
        descriptor.setDescription(Bundle.CLT_Max3rdVelocityFeature_Description());
        return descriptor;
    }
}