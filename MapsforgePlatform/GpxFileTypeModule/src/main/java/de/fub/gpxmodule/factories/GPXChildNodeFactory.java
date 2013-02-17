/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fub.gpxmodule.factories;

import de.fub.gpxmodule.nodes.RteNode;
import de.fub.gpxmodule.nodes.TrkNode;
import de.fub.gpxmodule.xml.Gpx;
import de.fub.gpxmodule.xml.Rte;
import de.fub.gpxmodule.xml.Trk;
import de.fub.gpxmodule.xml.Wpt;
import java.beans.IntrospectionException;
import java.util.List;
import org.openide.nodes.BeanNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author Serdar
 */
public class GPXChildNodeFactory extends ChildFactory<Node> {

    private final Gpx gpx;

    public GPXChildNodeFactory(Gpx gpx) {
        assert gpx != null;
        this.gpx = gpx;
    }

    @Override
    protected boolean createKeys(List<Node> list) {

        if (gpx.getTrk() != null) {
            for (Trk trk : gpx.getTrk()) {
                try {
                    list.add(
                            new TrkNode(
                            trk,
                            ((trk.getTrkseg() == null || trk.getTrkseg().isEmpty()) ? Children.LEAF : Children.create(new TrackSegmentNodeFactory(trk.getTrkseg()), true)),
                            Lookup.EMPTY));
                } catch (IntrospectionException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }

        if (gpx.getWpt() != null) {
            for (Wpt wpt : gpx.getWpt()) {
                try {
                    list.add(new BeanNode<Wpt>(wpt));
                } catch (IntrospectionException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }

        if (gpx.getRte() != null) {
            for (Rte rte : gpx.getRte()) {
                try {
                    list.add(new RteNode(rte, rte.getRtept() == null || rte.getRtept().isEmpty() ? Children.LEAF : Children.create(new RteptNodeFactory(rte.getRtept()), true)));
                } catch (IntrospectionException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }

        return true;
    }

    @Override
    protected Node createNodeForKey(Node t) {
        return t;
    }
}