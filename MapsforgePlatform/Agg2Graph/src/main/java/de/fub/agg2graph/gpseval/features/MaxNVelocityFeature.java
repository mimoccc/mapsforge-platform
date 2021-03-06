/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fub.agg2graph.gpseval.features;

import de.fub.agg2graph.gpseval.data.Waypoint;
import de.fub.agg2graph.structs.GPSCalc;
import java.util.PriorityQueue;

/**
 * Feature that computes the n-th max value of gps tracks. Inital n has the
 * default value of 1.
 *
 * @author Serdar
 */
public class MaxNVelocityFeature extends Feature {

    private PriorityQueue<Double> priorityQueue = new PriorityQueue<Double>();
    private int maxN = 1;
    private Waypoint lastWaypoint;

    public MaxNVelocityFeature() {
        this(1);
    }

    public MaxNVelocityFeature(int nthMaxValue) {
        setMaxN(nthMaxValue);
    }

    public int getMaxN() {
        return maxN;
    }

    /**
     * sets the n max value that should be computed from the gps track.
     *
     * @param n > 0
     */
    public final void setMaxN(int n) {
        assert n > 0;
        this.maxN = n;
        reset();
    }

    @Override
    public void reset() {
        lastWaypoint = null;
        priorityQueue.clear();
    }

    @Override
    public void addWaypoint(Waypoint waypoint) {
        double velocity = 0;
        if (lastWaypoint != null
                && lastWaypoint.getTimestamp() != null
                && waypoint.getTimestamp() != null) {
            long seconds = Math.max(0, (waypoint.getTimestamp().getTime() - lastWaypoint.getTimestamp().getTime()) / 1000);
            if (seconds > 0) {
                // meter per seconds
                velocity = GPSCalc.getDistVincentyFast(lastWaypoint.getLat(), lastWaypoint.getLon(), waypoint.getLat(), waypoint.getLon()) / seconds;
            }
            priorityQueue.add(velocity);
            if (priorityQueue.size() > maxN) {
                priorityQueue.poll();
                assert priorityQueue.size() == maxN;
            }
        }
        lastWaypoint = waypoint;
    }

    @Override
    public double getResult() {
        Double peek = priorityQueue.peek();
        return peek != null ? peek : Math.random();  // only for the propose to not have duplicate nominal value labels.
    }
}
