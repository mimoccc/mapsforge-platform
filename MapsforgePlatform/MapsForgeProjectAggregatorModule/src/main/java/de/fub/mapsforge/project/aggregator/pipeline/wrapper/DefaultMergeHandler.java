/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fub.mapsforge.project.aggregator.pipeline.wrapper;

import de.fub.mapsforge.project.aggregator.pipeline.processes.AggregationProcess;
import de.fub.mapsforge.project.aggregator.pipeline.wrapper.interfaces.MergeHandler;
import de.fub.mapsforge.project.aggregator.xml.ProcessDescriptor;
import de.fub.mapsforge.project.aggregator.xml.Property;
import de.fub.mapsforge.project.aggregator.xml.PropertySection;
import de.fub.mapsforge.project.aggregator.xml.PropertySet;
import de.fub.mapsforge.project.models.Aggregator;
import de.fub.utilsmodule.node.property.ProcessProperty;
import de.fub.utilsmodule.synchronizer.ModelSynchronizer;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.Sheet;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Serdar
 */
@NbBundle.Messages({
    "DefaultMergeHandler_Name=Default Merge Handler",
    "DefaultMergeHandler_Description=The default merge handler implementation",
    "DefaultMergeHandler_MaxPointGhostDistance_Name=Max Point Ghost Distance",
    "DefaultMergeHandler_MaxPointGhostDistance_Description=No description available",
    "DefaultMergeHandler_MaxLookahead_Name=Max Lookahead",
    "DefaultMergeHandler_MaxLookahead_Description=No description available",
    "DefaultMergeHandler_MinContinuationAngle_Name=Min Continuation Angle",
    "DefaultMergeHandler_MinContinuationAngle_Description=No description available",
    "DefaultMergeHandler_Setting_PropertySet_Name=Setting",
    "DefaultMergeHandler_Setting_PropertySet_Description=Parameter to configure the merge handler instance."
})
@ServiceProvider(service = MergeHandler.class)
public class DefaultMergeHandler extends de.fub.agg2graph.agg.strategy.DefaultMergeHandler implements MergeHandler {

    private static final Logger LOG = Logger.getLogger(DefaultMergeHandler.class.getName());
    private static final String PROP_NAME_MAX_POINT_GHOST_DISTANCE = "default.mergeHandler.max.point.ghost.distance";
    private static final String PROP_NAME_MAX_LOOKAHEAD = "default.mergeHandler.max.lookahead";
    private static final String PROP_NAME_MIN_CONTINUATION_ANGLE = "default.min.continuation.angle";
    private Aggregator aggregator;
    private PropertySet propertySet = null;
    private MergeHandlerNode nodeDelegate;

    public DefaultMergeHandler() {
    }

    private void reInit() {
        propertySet = null;
        propertySet = getPropertySet();
        if (propertySet != null) {
            for (Property property : propertySet.getProperties()) {
                if (property.getValue() != null) {
                    try {
                        if (PROP_NAME_MAX_LOOKAHEAD.equals(property.getId())) {
                            setMaxLookahead(Integer.parseInt(property.getValue()));
                        } else if (PROP_NAME_MAX_POINT_GHOST_DISTANCE.equals(property.getId())) {
                            setMaxPointGhostDist(Double.parseDouble(property.getValue()));
                        } else if (PROP_NAME_MIN_CONTINUATION_ANGLE.equals(property.getId())) {
                            setMinContinuationAngle(Double.parseDouble(property.getValue()));
                        }
                    } catch (NumberFormatException ex) {
                        LOG.log(Level.SEVERE, ex.getMessage(), ex);
                    }
                }
            }
        }
    }

    @Override
    public void setAggregator(Aggregator aggregator) {
        this.aggregator = aggregator;
        reInit();
    }

    @Override
    public Aggregator getAggregator() {
        return this.aggregator;
    }

    @Override
    public PropertySet getPropertySet() {
        if (propertySet == null) {
            if (getAggregator() != null) {
                for (ProcessDescriptor descriptor : getAggregator().getAggregatorDescriptor().getPipeline().getList()) {
                    // look for the AggregationProcess descriptor
                    if (propertySet != null
                            && AggregationProcess.class.getName().equals(descriptor.getJavaType())) {
                        List<PropertySection> sections = descriptor.getProperties().getSections();
                        for (PropertySection propertySection : sections) {
                            for (PropertySet set : propertySection.getPropertySet()) {
                                // look for the propertySet with the same name as this class
                                if (Bundle.DefaultMergeHandler_Name().equals(set.getName())) {
                                    propertySet = set;
                                    break;
                                }
                            }
                        }

                        if (propertySet == null) {
                            propertySet = createDefaultPropertySet();
                        }
                    }
                }
            } else {
                propertySet = createDefaultPropertySet();
            }
        }
        return propertySet;
    }

    private PropertySet createDefaultPropertySet() {

        PropertySet set = new PropertySet(
                Bundle.DefaultMergeHandler_Setting_PropertySet_Name(),
                Bundle.DefaultMergeHandler_Setting_PropertySet_Description());

        Property property = new Property();
        property.setId(PROP_NAME_MAX_LOOKAHEAD);
        property.setJavaType(Integer.class.getName());
        property.setName(Bundle.DefaultMergeHandler_MaxLookahead_Name());
        property.setDescription(Bundle.DefaultMergeHandler_MaxLookahead_Description());
        property.setValue(MessageFormat.format("{0}", getMaxLookahead()).replaceAll("\\,", "."));
        set.getProperties().add(property);

        property = new Property();
        property.setId(PROP_NAME_MAX_POINT_GHOST_DISTANCE);
        property.setJavaType(Double.class.getName());
        property.setName(Bundle.DefaultMergeHandler_MaxPointGhostDistance_Name());
        property.setDescription(Bundle.DefaultMergeHandler_MaxPointGhostDistance_Description());
        property.setValue(MessageFormat.format("{0}", getMaxPointGhostDist()).replaceAll("\\,", "."));
        set.getProperties().add(property);

        property = new Property();
        property.setId(PROP_NAME_MIN_CONTINUATION_ANGLE);
        property.setJavaType(Double.class.getName());
        property.setName(Bundle.DefaultMergeHandler_MinContinuationAngle_Name());
        property.setDescription(Bundle.DefaultMergeHandler_MinContinuationAngle_Description());
        property.setValue(MessageFormat.format("{0}", getMinContinuationAngle()).replaceAll("\\,", "."));
        set.getProperties().add(property);


        return set;
    }

    @Override
    public Node getNodeDelegate() {
        if (nodeDelegate == null) {
            nodeDelegate = new MergeHandlerNode(DefaultMergeHandler.this);
        }
        return nodeDelegate;
    }

    private static class MergeHandlerNode extends AbstractNode implements ChangeListener {

        private final DefaultMergeHandler mergeHandler;
        private ModelSynchronizer.ModelSynchronizerClient modelSynchornizerClient;

        public MergeHandlerNode(DefaultMergeHandler mergeHandler) {
            super(Children.LEAF);
            this.mergeHandler = mergeHandler;
            if (this.mergeHandler != null && this.mergeHandler.getAggregator() != null) {
                modelSynchornizerClient = this.mergeHandler.getAggregator().create(MergeHandlerNode.this);
            }
        }

        @Override
        protected Sheet createSheet() {
            Sheet sheet = Sheet.createDefault();

            if (mergeHandler != null) {
                de.fub.mapsforge.project.aggregator.xml.PropertySet propertySet = mergeHandler.getPropertySet();

                if (propertySet != null) {
                    Sheet.Set set = Sheet.createPropertiesSet();
                    set.setName(propertySet.getName());
                    set.setDisplayName(propertySet.getName());
                    set.setShortDescription(propertySet.getDescription());
                    sheet.put(set);

                    for (de.fub.mapsforge.project.aggregator.xml.Property property : propertySet.getProperties()) {
                        set.put(new ProcessProperty(modelSynchornizerClient, property));
                    }
                }

            }

            return sheet;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            //do nothing
        }
    }
}
