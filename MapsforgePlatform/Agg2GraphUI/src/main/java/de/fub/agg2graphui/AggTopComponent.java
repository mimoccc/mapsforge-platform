/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fub.agg2graphui;

import de.fub.agg2graph.osm.OsmPanel.OSMMapRect;
import de.fub.agg2graph.structs.DoubleRect;
import de.fub.agg2graphui.controller.AbstractLayer;
import de.fub.agg2graphui.controller.LayerManager;
import java.awt.Point;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JPanel;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;
import org.openstreetmap.gui.jmapviewer.interfaces.MapRectangle;

/**
 * Top component which displays something.
 */
@Messages({
    "CTL_AggAction=Agg",
    "CTL_AggTopComponent=Agg Window",
    "HINT_AggTopComponent=This is a Agg window"
})
public final class AggTopComponent extends JPanel {

    private static final long serialVersionUID = 1L;
    private MapViewListener mouseListener = new MapViewListener(this);
    // explorerManager for nodes that represent the current
    // state of the dataflow step

    public AggTopComponent() {
        initComponents();
        setName(Bundle.CTL_AggTopComponent());
        setToolTipText(Bundle.HINT_AggTopComponent());
        init();
    }

    private void init() {
        mapViewer.addMouseListener(mouseListener);
        mapViewer.addMouseMotionListener(mouseListener);
        mapViewer.addMouseWheelListener(mouseListener);
        mapViewer.addComponentListener(mouseListener);
        mapViewer.setZoomContolsVisible(false);
    }

    public void showArea(DoubleRect area) {
        OSMMapRect mapRect = new OSMMapRect(area);
//        mapViewer.addMapRectangle(mapRect);
//        mapViewer.setDisplayToFitMapRectangle();
//        mapViewer.removeMapRectangle(mapRect);
        mapViewer.addMapMarker(new MapMarkerDot(area.getX(), area.getY()));
        mapViewer.addMapMarker(new MapMarkerDot(area.getWidth(), area.getHeight()));
        mapViewer.setDisplayToFitMapMarkers();
        removeAllMarkers();
    }

    public void setDisplayToFitMapMarkers() {
        mapViewer.setDisplayToFitMapMarkers();
    }

    public void setDisplayToFitMapRectangle() {
        mapViewer.setDisplayToFitMapRectangle();
    }

    public synchronized void addMapMarker(MapMarker marker) {
        mapViewer.addMapMarker(marker);
    }

    public synchronized void removeMapMarker(MapMarker marker) {
        mapViewer.removeMapMarker(marker);
    }

    public synchronized void addMapRectangle(MapRectangle rectangle) {
        mapViewer.addMapRectangle(rectangle);
    }

    public synchronized void removeMapRectangle(MapRectangle rectangle) {
        mapViewer.removeMapRectangle(rectangle);
    }

    public synchronized void removeAllMarkers() {
        mapViewer.getMapMarkerList().clear();
        List<MapMarker> mapMarkerList = new ArrayList<MapMarker>(mapViewer.getMapMarkerList());
        for (MapMarker marker : mapMarkerList) {
            mapViewer.removeMapMarker(marker);
        }
    }

    public void updateLonLat(Point point) {
        Coordinate position = mapViewer.getPosition(point);
        setLatitude(position.getLat());
        setLongitude(position.getLon());
    }

    public void updateBoundingBox() {
        Coordinate leftLongBottomLat = mapViewer.getPosition(getLocation().x, getLocation().y + getHeight());
        Coordinate rigtLongTopLat = mapViewer.getPosition(getLocation().x + getWidth(), getLocation().y);
        boundingBox.setText(MessageFormat.format(
                NbBundle.getMessage(this.getClass(), "AggTopComponent.boundingBox.text"),
                leftLongBottomLat.getLon(),
                rigtLongTopLat.getLat(),
                rigtLongTopLat.getLon(),
                leftLongBottomLat.getLat()));
    }

    public void setLatitude(double lat) {
        latitude.setText(MessageFormat.format(
                NbBundle.getMessage(this.getClass(), "AggTopComponent.latitude.text"), lat));
    }

    public void setLongitude(double lon) {
        longitude.setText(MessageFormat.format(
                NbBundle.getMessage(this.getClass(), "AggTopComponent.longitude.text"), lon));
    }

    public void setLabelsVisible(boolean labelsVisible) {
        mapViewer.setLabelsVisible(labelsVisible);
    }

    public LayerManager getLayerManager() {
        return mapViewer.getLayerManager();
    }

    public void addLayer(AbstractLayer<?> layer) {
        mapViewer.addLayer(layer);
    }

    public void removeLayer(AbstractLayer<?> layer) {
        mapViewer.removeLayer(layer);
    }

    public void clear() {
        mapViewer.removeAllLayers();
    }

    /**
     *
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        statusbar = new javax.swing.JPanel();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(2, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(2, 32767));
        jLabel1 = new javax.swing.JLabel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(4, 0), new java.awt.Dimension(4, 0), new java.awt.Dimension(4, 32767));
        latitude = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(8, 0), new java.awt.Dimension(8, 0), new java.awt.Dimension(8, 32767));
        jLabel2 = new javax.swing.JLabel();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(4, 0), new java.awt.Dimension(4, 0), new java.awt.Dimension(4, 32767));
        longitude = new javax.swing.JLabel();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(8, 0), new java.awt.Dimension(8, 0), new java.awt.Dimension(8, 32767));
        jLabel3 = new javax.swing.JLabel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(4, 0), new java.awt.Dimension(4, 0), new java.awt.Dimension(4, 32767));
        boundingBox = new javax.swing.JLabel();
        filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(2, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(2, 32767));
        mapViewer = new de.fub.agg2graphui.AggContentPanel();

        setLayout(new java.awt.BorderLayout());

        statusbar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        statusbar.setLayout(new javax.swing.BoxLayout(statusbar, javax.swing.BoxLayout.X_AXIS));
        statusbar.add(filler6);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(AggTopComponent.class, "AggTopComponent.jLabel1.text")); // NOI18N
        statusbar.add(jLabel1);
        statusbar.add(filler2);

        org.openide.awt.Mnemonics.setLocalizedText(latitude, org.openide.util.NbBundle.getMessage(AggTopComponent.class, "AggTopComponent.latitude.text")); // NOI18N
        statusbar.add(latitude);
        statusbar.add(filler1);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(AggTopComponent.class, "AggTopComponent.jLabel2.text")); // NOI18N
        statusbar.add(jLabel2);
        statusbar.add(filler3);

        org.openide.awt.Mnemonics.setLocalizedText(longitude, org.openide.util.NbBundle.getMessage(AggTopComponent.class, "AggTopComponent.longitude.text")); // NOI18N
        longitude.setMaximumSize(new java.awt.Dimension(150, 14));
        statusbar.add(longitude);
        statusbar.add(filler5);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(AggTopComponent.class, "AggTopComponent.jLabel3.text")); // NOI18N
        statusbar.add(jLabel3);
        statusbar.add(filler4);

        org.openide.awt.Mnemonics.setLocalizedText(boundingBox, org.openide.util.NbBundle.getMessage(AggTopComponent.class, "AggTopComponent.boundingBox.text")); // NOI18N
        boundingBox.setMaximumSize(new java.awt.Dimension(32665, 14));
        statusbar.add(boundingBox);
        statusbar.add(filler7);

        add(statusbar, java.awt.BorderLayout.SOUTH);

        mapViewer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        add(mapViewer, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel boundingBox;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.Box.Filler filler7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel latitude;
    private javax.swing.JLabel longitude;
    private de.fub.agg2graphui.AggContentPanel mapViewer;
    private javax.swing.JPanel statusbar;
    // End of variables declaration//GEN-END:variables
}