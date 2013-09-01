/*
 * Copyright 2013 Serdar.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.fub.maps.project.plugins.mapmatcher;

import de.fub.agg2graph.structs.GPSCalc;
import de.fub.agg2graph.structs.GPSPoint;
import de.fub.agg2graph.structs.GPSSegment;
import de.fub.agg2graph.structs.ILocation;
import de.fub.maps.project.plugins.tasks.eval.GpsSegmentTree;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Serdar
 */
@ServiceProvider(service = MapMatcher.class)
public class PointToPointMapMatcher implements MapMatcher {

    private static final Logger LOG = Logger.getLogger(PointToPointMapMatcher.class.getName());
    private final GpsSegmentTree TREE = new GpsSegmentTree();

    @Override
    public List<MapMatchSegment> findMatch(Collection<GPSSegment> tobeMatchedRoadNetwork, Collection<GPSSegment> roadNetwork) {
        TREE.reset();
        TREE.addSegments(roadNetwork);

        List<MapMatchSegment> resultList = new ArrayList<MapMatchSegment>(roadNetwork.size());

        for (GPSSegment roadSegment : tobeMatchedRoadNetwork) {
            MapMatchSegment matchSegment = new MapMatchSegment();
            Collection<GPSSegment> searchSpace = TREE.getIntersectingSegment(roadSegment);

            for (GPSPoint point : roadSegment) {

                // do point to point matching
                MapMatchResult mapMatchResult = findMatchingPoint(point, searchSpace);

                if (mapMatchResult != null) {
                    matchSegment.getSegment().add(mapMatchResult);
                }
            }

            if (!matchSegment.getSegment().isEmpty()) {
                resultList.add(matchSegment);
            }
        }
        return resultList;
    }

    private MapMatchResult findMatchingPoint(GPSPoint point, Collection<GPSSegment> roadNetwork) {
        MapMatchResult mapMatchResult = null;
        double minDistance = Double.MAX_VALUE;
        ILocation matchPoint = null;
        for (GPSSegment matchGpsSegment : roadNetwork) {
            ILocation lastLocation = null;
            for (ILocation roadPoint : matchGpsSegment) {

                double distance = Double.MAX_VALUE;
                if (lastLocation == null) {
                    distance = GPSCalc
                            .getDistVincentyFast(
                                    point.getLat(),
                                    point.getLon(),
                                    roadPoint.getLat(),
                                    roadPoint.getLon());
                } else {
                    // project point to edge lastpoint - roadPoint
                    ILocation projectionPoint = GPSCalc.getProjectionPoint(point, lastLocation, roadPoint);

                    if (projectionPoint != null) {
                        distance = GPSCalc.getDistVincentyFast(point.getLat(), point.getLon(), projectionPoint.getLat(), projectionPoint.getLon());
                        roadPoint = projectionPoint;
                    } else {
//                        LOG.log(Level.SEVERE, "Point {0} has no projection to segment {1} - {2}", new Object[]{point, lastLocation, roadPoint});
                        distance = GPSCalc
                                .getDistVincentyFast(
                                        point.getLat(),
                                        point.getLon(),
                                        roadPoint.getLat(),
                                        roadPoint.getLon());
                    }
                }
                if (distance < minDistance) {
                    minDistance = distance;
                    matchPoint = roadPoint;
                }
                lastLocation = roadPoint;
            }
        }

        if (matchPoint != null) {
            mapMatchResult = new MapMatchResult(point, matchPoint, minDistance);
        }

        return mapMatchResult;
    }
}
