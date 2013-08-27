/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fub.maps.project.detector.factories.inference;

import de.fub.maps.project.detector.factories.nodes.inference.FeatureRootNode;
import de.fub.maps.project.detector.model.Detector;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;

/**
 *
 * @author Serdar
 */
public class InferenceNodeChildFactory extends ChildFactory<Node> {

    private final Detector detector;

    public InferenceNodeChildFactory(Detector detector) {
        this.detector = detector;;
    }

    @Override
    protected boolean createKeys(List<Node> toPopulate) {
        if (detector != null) {
            toPopulate.add(new FeatureRootNode(detector));
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(Node node) {
        return new FilterNode(node);
    }
}