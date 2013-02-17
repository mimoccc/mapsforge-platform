/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fub.mapsforge.project.aggregator.pipeline.processes;

import de.fub.agg2graph.agg.AggContainer;
import de.fub.agg2graph.roadgen.Intersection;
import de.fub.agg2graph.roadgen.RoadNetwork;
import de.fub.agg2graphui.layers.IntersectionLayer;
import de.fub.agg2graphui.layers.RoadNetworkLayer;
import de.fub.mapsforge.project.aggregator.pipeline.AbstractAggregationProcess;
import de.fub.mapsforge.project.aggregator.pipeline.ProcessPipeline;
import de.fub.mapsforge.project.aggregator.xml.ProcessDescriptor;
import de.fub.mapsforge.project.models.Aggregator;
import java.awt.Image;
import javax.swing.JComponent;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Serdar
 */
@ServiceProvider(service = AbstractAggregationProcess.class)
public class RoadNetworkProcess extends AbstractAggregationProcess<AggContainer, RoadNetwork> {

    @StaticResource
    private static final String ICON_PATH = "de/fub/mapsforge/project/aggregator/pipeline/processes/datasourceProcessIcon.png";
    private static final Image IMAGE = ImageUtilities.loadImage(ICON_PATH);
    private RoadNetwork roadNetwork = null;
    private final Object MUTEX = new Object();
    private IntersectionLayer intersectionLayer = new IntersectionLayer();
    private RoadNetworkLayer roadNetworkLayer = new RoadNetworkLayer();

    public RoadNetworkProcess() {
        this(null);
    }

    public RoadNetworkProcess(Aggregator container) {
        super(container);
        layers.add(intersectionLayer);
        layers.add(roadNetworkLayer);
    }

    @Override
    public void setInput(AggContainer input) {
    }

    @Override
    protected void start() {
        synchronized (MUTEX) {
            if (aggregator != null) {
                roadNetworkLayer.clearRenderObjects();
                intersectionLayer.clearRenderObjects();

                roadNetwork = new RoadNetwork();
                roadNetwork.parse(aggregator.getAggContainer(), null);

                for (Intersection intersection : roadNetwork.intersections) {
                    intersectionLayer.add(intersection);
                }

                roadNetworkLayer.add(roadNetwork);
                fireProcessEvent(new ProcessPipeline.ProcessEvent(this, "Creating Roadnetwork...", 100));
            }
        }
    }

    @Override
    public RoadNetwork getResult() {
        synchronized (MUTEX) {
            return roadNetwork;
        }
    }

    @Override
    public String getName() {
        return "Road Generator";
    }

    @Override
    public String getDescription() {
        return "Default Road Generator";
    }

    @Override
    public void setDescriptor(ProcessDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    @Override
    public Image getIcon() {
        return IMAGE;
    }

    @Override
    public JComponent getSettingsView() {
        return null;
    }
}