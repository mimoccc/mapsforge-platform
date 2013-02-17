/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fub.mapsforge.project.aggregator.graph;

import de.fub.mapsforge.project.aggregator.pipeline.AbstractAggregationProcess;
import de.fub.mapsforge.project.aggregator.xml.ProcessDescriptor;
import de.fub.mapsforge.project.models.Aggregator;
import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.netbeans.api.visual.graph.layout.GraphLayout;
import org.netbeans.api.visual.graph.layout.GraphLayoutFactory;
import org.netbeans.api.visual.graph.layout.GraphLayoutSupport;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.layout.SceneLayout;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.ChangeSupport;

/**
 *
 * @author Serdar
 */
public class GraphPanel extends javax.swing.JPanel implements PropertyChangeListener {

    private static final long serialVersionUID = 1L;
    private final ProcessGraph graph = new ProcessGraph();
    private Aggregator aggregator = null;
    private final ChangeSupport cs = new ChangeSupport(this);

    /**
     * Creates new form GraphPanel
     */
    public GraphPanel() {
        initComponents();
        jScrollPane1.setViewportView(graph.createView());
        jScrollPane1.setDropTarget(new DropTarget(this, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
            }
        }));
        graph.addPropertyChangeListener(GraphPanel.this);
    }

    public void addChangeListener(ChangeListener listener) {
        cs.addChangeListener(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        cs.removeChangeListener(listener);
    }

    public Aggregator getAggregator() {
        return aggregator;
    }

    public void setAggregator(Aggregator aggregator) {
        graph.removePropertyChangeListener(GraphPanel.this);
        this.aggregator = aggregator;
        graph.clearGraph();
        graph.validate();
        reinitGraph();
        graph.addPropertyChangeListener(GraphPanel.this);
    }

    protected Widget attachNodeWidget(AbstractAggregationProcess<?, ?> node) {
        return graph.attachNodeWidget(node);
    }

    protected Widget attachEdgeWidget(String edge) {
        return graph.attachEdgeWidget(edge);
    }

    protected void attachEdgeSourceAnchor(String edge, AbstractAggregationProcess<?, ?> oldSourceNode, AbstractAggregationProcess<?, ?> sourceNode) {
        graph.attachEdgeSourceAnchor(edge, oldSourceNode, sourceNode);
    }

    protected void attachEdgeTargetAnchor(String edge, AbstractAggregationProcess<?, ?> oldTargetNode, AbstractAggregationProcess<?, ?> targetNode) {
        graph.attachEdgeTargetAnchor(edge, oldTargetNode, targetNode);
    }

    public void layoutGraph() {
        Collection<AbstractAggregationProcess<?, ?>> processes = aggregator.getPipeline().getProcesses();
        final GraphLayout<AbstractAggregationProcess<?, ?>, String> layout = GraphLayoutFactory.createTreeGraphLayout(5, 5, 200, 10, true);
        AbstractAggregationProcess rootProcess = processes.iterator().next();
        GraphLayoutSupport.setTreeGraphLayoutRootNode(layout, rootProcess);
        SceneLayout sceneLayout = LayoutFactory.createSceneGraphLayout(graph, layout);
        layout.setAnimated(false);
        sceneLayout.invokeLayoutImmediately();
        graph.revalidate();
        repaint();
    }

    private void reinitGraph() {
        graph.revalidate();
        Collection<AbstractAggregationProcess<?, ?>> processes = aggregator.getPipeline().getProcesses();
        Widget lastNodeWidget = null;
        AbstractAggregationProcess<?, ?> lastProcess = null;
        int i = 1;
        for (AbstractAggregationProcess<?, ?> process : processes) {
            if (process != null) {
                Widget nodeWidget = graph.addNode(process);
                nodeWidget.setPreferredLocation(new Point(20 + (i * 200), 50));
                if (lastNodeWidget != null && lastProcess != null) {
                    String edgeID = "edge" + ProcessGraph.edgeCount++;
                    graph.addEdge(edgeID);
                    graph.setEdgeSource(edgeID, lastProcess);
                    graph.setEdgeTarget(edgeID, process);
                }
                i++;
                lastNodeWidget = nodeWidget;
                lastProcess = process;
            } else {
                System.out.println("Error");
            }
        }
        layoutGraph();
        graph.validate();
        graph.revalidate();
        graph.repaint();
        repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();

        setLayout(new java.awt.BorderLayout());
        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (ProcessGraph.PROP_NAME_PIPELINE.equals(evt.getPropertyName())) {
            Object newValue = evt.getNewValue();
            if (newValue instanceof List) {
                @SuppressWarnings("unchecked")
                List<AbstractAggregationProcess<?, ?>> pipeline = (List<AbstractAggregationProcess<?, ?>>) newValue;
                if (aggregator != null) {
                    aggregator.getDescriptor().getPipeline().getList().clear();

                    for (AbstractAggregationProcess<?, ?> process : pipeline) {
                        if (process.getDescriptor() != null) {
                            if (process.getDescriptor().getJavatype() == null
                                    || process.getDescriptor().getDisplayName() == null
                                    || process.getDescriptor().getDescription() == null) {
                                process.setDescriptor(new ProcessDescriptor(process));
                            }
                            aggregator.getDescriptor().getPipeline().getList().add(process.getDescriptor());
                        }
                    }
                    aggregator.getDataObject().save();
                }
            }
        }
    }
}