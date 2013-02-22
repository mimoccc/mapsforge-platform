/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fub.mapsforge.project.aggregator.pipeline.processes;

import de.fub.agg2graph.input.GPXReader;
import de.fub.agg2graph.structs.GPSSegment;
import de.fub.agg2graph.ui.gui.RenderingOptions;
import de.fub.agg2graphui.controller.AbstractLayer;
import de.fub.agg2graphui.layers.GPSSegmentLayer;
import de.fub.mapsforge.project.aggregator.pipeline.AbstractAggregationProcess;
import de.fub.mapsforge.project.aggregator.pipeline.ProcessPipeline;
import de.fub.mapsforge.project.aggregator.xml.ProcessDescriptor;
import de.fub.mapsforge.project.aggregator.xml.Source;
import de.fub.mapsforge.project.api.StatisticProvider;
import de.fub.mapsforge.project.models.Aggregator;
import de.fub.mapsforge.project.utils.AggregateUtils;
import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JComponent;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Serdar
 */
@ServiceProvider(service = AbstractAggregationProcess.class)
public class DatasourceProcess extends AbstractAggregationProcess<Void, List<GPSSegment>> implements StatisticProvider {

    @StaticResource
    private static final String ICON_PATH = "de/fub/mapsforge/project/aggregator/pipeline/processes/datasourceProcessIcon.png";
    private static final Image IMAGE = ImageUtilities.loadImage(ICON_PATH);
    private List<GPSSegment> segments = new ArrayList<GPSSegment>();
    private final GPSSegmentLayer gPSSegmentLayer;
    private int totalSegmentCount = 0;
    private int totalGPSPoints = 0;
    private int totalGPXFiles = 0;

    public DatasourceProcess() {
        this(null);
    }

    public DatasourceProcess(Aggregator container) {
        super(container);
        RenderingOptions renderingOptions = new RenderingOptions();
        renderingOptions.setColor(new Color(97, 123, 228)); // blue
        renderingOptions.setRenderingType(RenderingOptions.RenderingType.ALL);
        renderingOptions.setzIndex(-1);
        renderingOptions.setOpacity(1);
        gPSSegmentLayer = new GPSSegmentLayer("Raw-Layer", "Displays the raw gps data.", renderingOptions);
        layers.add(gPSSegmentLayer);
    }

    @Override
    public void setDescriptor(ProcessDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    @Override
    public String getName() {
        return "Gpx Datasource";
    }

    @Override
    public String getDescription() {
        return "Gpx segment provider";
    }

    @Override
    public void setInput(Void input) {
        // do nothing we get the input from aggContainer
    }

    @Override
    public List<GPSSegment> getResult() {
        return Collections.unmodifiableList(this.segments);
    }

    @Override
    public List<AbstractLayer<?>> getLayers() {
        return Collections.unmodifiableList(layers);
    }

    @Override
    protected void start() {
        totalGPSPoints = 0;
        totalGPXFiles = 0;
        totalSegmentCount = 0;
        segments.clear();
        gPSSegmentLayer.clearRenderObjects();
        List<Source> sourceList = aggregator.getSourceList();
        int progress = 0;
        for (Source source : sourceList) {
            File file = new File(source.getUrl());
            if (file.exists()) {
                this.segments.addAll(GPXReader.getSegments(file));
                totalSegmentCount += segments.size();
                int segmentId = 0;
                for (GPSSegment segment : segments) {
                    segment.addIDs("I" + (segmentId++));
                    gPSSegmentLayer.add(segment);
                    totalGPSPoints += segment.size();
                }
            }
            totalGPXFiles++;
            fireProcessEvent(new ProcessPipeline.ProcessEvent(this, "import", (int) ((100d / sourceList.size()) * (++progress))));
        }
    }

    @Override
    public Image getIcon() {
        return IMAGE;
    }

    @Override
    public JComponent getSettingsView() {
        return null;
    }

    @Override
    protected ProcessDescriptor createProcessDescriptor() {
        ProcessDescriptor desc = null;
        try {
            desc = AggregateUtils.getProcessDescriptor(getClass());
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        return desc;
    }

    @Override
    public List<StatisticSection> getStatisticData() throws StatisticNotAvailableException {
        List<StatisticSection> statisticSections = new ArrayList<StatisticProvider.StatisticSection>();
        statisticSections.add(getPerformanceData());

        StatisticSection section = new StatisticSection("GPS Datasource Statistics", "Displays the statistical data of the raw gps data.");
        statisticSections.add(section);
        section.getStatisticsItemList().add(new StatisticItem("Total GPS Points", String.valueOf(totalGPSPoints), "Represents the total count of GPS points that the data set contains."));
        section.getStatisticsItemList().add(new StatisticItem("Total GPS Segemtns", String.valueOf(totalSegmentCount), "Represents the total count of GPS segments that the data set contains."));
        section.getStatisticsItemList().add(new StatisticItem("GPS points/segment ratio", String.valueOf(totalGPSPoints / (double) totalSegmentCount), "Displays the ratio of gps points to segments"));
        section.getStatisticsItemList().add(new StatisticItem("Total GPX File", String.valueOf(aggregator.getSourceList().size()), "Represents the total amount of GPX files as data set"));

        return statisticSections;
    }
}
