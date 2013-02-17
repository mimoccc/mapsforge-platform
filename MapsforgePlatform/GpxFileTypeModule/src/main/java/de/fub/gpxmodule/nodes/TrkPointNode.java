/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fub.gpxmodule.nodes;

import de.fub.gpxmodule.xml.Trkseg.Trkpt;
import java.beans.IntrospectionException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import org.openide.nodes.BeanNode;
import org.openide.util.NbBundle;

/**
 *
 * @author Serdar
 */
public class TrkPointNode extends BeanNode<Trkpt> {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    @NbBundle.Messages({"CTL_descritpion=Lat/Lon and time stamp of the track point.", "CLT_time_not_available=not available"})
    public TrkPointNode(Trkpt trackPoint) throws IntrospectionException {
        super(trackPoint);
        setDisplayName(MessageFormat.format("Track Point: {0}/{1} [{2}]", 
                trackPoint.getLat(), 
                trackPoint.getLon(), 
                trackPoint.getTime() != null ? 
                formatter.format(trackPoint.getTime()) : 
                Bundle.CLT_time_not_available()));
        setShortDescription(Bundle.CTL_descritpion());
    }
}