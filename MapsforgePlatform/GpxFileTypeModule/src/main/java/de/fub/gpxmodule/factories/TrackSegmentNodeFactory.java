/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fub.gpxmodule.factories;

import de.fub.gpxmodule.nodes.TrkSegNode;
import de.fub.gpxmodule.xml.Trkseg;
import java.beans.IntrospectionException;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

/**
 *
 * @author Serdar
 */
public class TrackSegmentNodeFactory extends ChildFactory<Trkseg> {

    private final List<Trkseg> trkSegList;

    public TrackSegmentNodeFactory(List<Trkseg> list) {
        assert list != null;
        this.trkSegList = list;
    }

    @Override
    protected boolean createKeys(List<Trkseg> list) {
        list.addAll(this.trkSegList);
        return true;
    }

    @Override
    protected Node createNodeForKey(Trkseg t) {

        Node node = null;
        try {
            node = new TrkSegNode(t, t.getTrkpt() == null || t.getTrkpt().isEmpty() ? Children.LEAF : Children.create(new TrkpointNodeFactory(t.getTrkpt()), true));
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }

        return node;
    }
}