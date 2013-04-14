/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fub.mapsforge.project.datasource.spi.nodes;

import de.fub.gpxmodule.xml.Wpt;
import de.fub.mapsforge.project.datasource.spi.TrackSegmentBehaviour;
import de.fub.mapsforge.project.datasource.spi.TrksegWrapper;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.PropertySupport.ReadWrite;
import org.openide.nodes.Sheet;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Serdar
 */
public class TrackSegmentNode extends AbstractNode implements TrackSegmentBehaviour {

    private static final String NAME_PATTERN = "Tracksegment: [{0, number, 000.00000000}°, {1, number, 000.00000000}°, {2, number, 000.00000000}°, {3, number, 000.00000000}°]";
    private InstanceContent instanceContent = null;
    private boolean selected = false;
    private boolean visible = true;
    private BufferedImage image = null;
    private final TrksegWrapper trkseg;

    public TrackSegmentNode(TrksegWrapper trkseg) {
        this(trkseg, new InstanceContent());
    }

    private TrackSegmentNode(TrksegWrapper trkseg, InstanceContent contenct) {
        super(Children.LEAF, new AbstractLookup(contenct));
        this.instanceContent = contenct;
        instanceContent.add(trkseg);
        this.trkseg = trkseg;
        updateNode();
    }

    private void updateNode() {
        if (trkseg.getTrackName() == null && trkseg.getTrackDescription() == null) {
            double latMax = -90;
            double lonMin = 180;
            double latMin = 90;
            double lonMax = -180;
            for (Wpt wpt : trkseg.getTrkseg().getTrkpt()) {
                latMax = Math.max(wpt.getLat().doubleValue(), latMax);
                latMin = Math.min(wpt.getLat().doubleValue(), latMin);
                lonMax = Math.max(wpt.getLon().doubleValue(), lonMax);
                lonMin = Math.min(wpt.getLon().doubleValue(), lonMin);
            }
            setDisplayName(MessageFormat.format(NAME_PATTERN, latMax, lonMin, latMin, lonMax));
        } else {
            setDisplayName(MessageFormat.format("TrackName: {0}, Description: {1}", trkseg.getTrackName(), trkseg.getTrackDescription()));
        }
    }

    @Override
    public Image getIcon(int type) {
        if (image == null) {
            image = new BufferedImage(16, 16, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2d = null;
            try {
                g2d = image.createGraphics();
                g2d.setColor(trkseg.getColor());
                g2d.fillRect(1, 1, 14, 14);
                g2d.setColor(Color.black);
                g2d.drawRect(1, 1, 14, 14);
            } finally {
                if (g2d != null) {
                    g2d.dispose();
                }
            }
        }
        return image;
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        sheet.put(set);
        Property<?> property = new PropertySupport.ReadWrite<Boolean>("visible", Boolean.class, "Visible On / Off", "") {
            @Override
            public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
                return isVisible();
            }

            @Override
            public void setValue(Boolean val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                setVisible(val);
            }
        };
        set.put(property);
        property = new ReadWrite<Boolean>("selected", Boolean.class, "Select On / Off", "") {
            @Override
            public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
                return isSelected();
            }

            @Override
            public void setValue(Boolean val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                setSelected(val);
            }
        };
        set.put(property);
        return sheet;
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }

    @Override
    public boolean isCheckable() {
        return true;
    }

    @Override
    public boolean isCheckEnabled() {
        return true;
    }

    @Override
    public Boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
