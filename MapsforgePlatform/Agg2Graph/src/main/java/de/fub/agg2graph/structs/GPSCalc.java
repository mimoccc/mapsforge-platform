/**
 * *****************************************************************************
 * Copyright 2013 Johannes Mitlmeier
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * ****************************************************************************
 */
package de.fub.agg2graph.structs;

import de.fub.agg2graph.agg.AggContainer;
import de.fub.agg2graph.agg.AggNode;
import de.fub.agg2graph.gpseval.data.Waypoint;
import de.fub.agg2graph.structs.frechet.Interval;
import de.fub.agg2graph.utils.MathUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;
import org.jscience.mathematics.number.Float64;
import org.jscience.mathematics.vector.Float64Vector;

/**
 * Static methods for calculations on gps data like distance and angles.
 *
 * @author Johannes Mitlmeier
 *
 */
public class GPSCalc {

    /**
     * ****************
     * TODO: MARTINUS * *****************
     */
    private static Double precision = 100000000.0;

    /**
     * computes the heading / angle of the gps point <code>lastWaypoint</code>
     * with the help of its predecessor and successor point.
     *
     * The method shifts the <code>lastWaypoint</code> and <code>waypoint</code>
     * to the zero vector and measures the angle between both of those vectors,
     * according to
     * {@link http://www.mathe-online.at/materialien/Andreas.Pester/files/Vectors/winkel_zwischen_vektoren.htm}
     *
     * @param secondLasWaypoint
     * @param lastWaypoint
     * @param waypoint
     * @return degree between 0° and 359°
     */
    public static double computeHeading(Waypoint secondLasWaypoint, Waypoint lastWaypoint, Waypoint waypoint) {
        // get vectors and shift to origin
        //        MutableWaypoint vector1 = new MutableWaypoint();
        //        vector1.setLat(lastWaypoint.getLat() - secondLasWaypoint.getLat());
        //        vector1.setLon(lastWaypoint.getLon() - secondLasWaypoint.getLon());
        //        MutableWaypoint vector2 = new MutableWaypoint();
        //        vector2.setLat(waypoint.getLat() - lastWaypoint.getLat());
        //        vector2.setLon(waypoint.getLon() - lastWaypoint.getLon());
        //
        //        double x = vector1.getLat() * vector2.getLat() + vector1.getLon() * vector2.getLon();
        //        double y = StrictMath.sqrt(StrictMath.pow(vector1.getLat(), 2) + StrictMath.pow(vector1.getLon(), 2)) * StrictMath.sqrt(Math.pow(vector2.getLat(), 2) + Math.pow(vector2.getLon(), 2));
        //        double header = StrictMath.acos(x / y) * 180 / Math.PI;
        ILocation previous = new GPSPoint(secondLasWaypoint.getLat(), secondLasWaypoint.getLon());
        ILocation current = new GPSPoint(lastWaypoint.getLat(), lastWaypoint.getLon());
        ILocation next = new GPSPoint(waypoint.getLat(), waypoint.getLon());
        return getAngleBetweenEdges(previous, current, current, next);
    }

    /**
     * in meters
     *
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return
     */
    public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
        return getSimpleDistance(lat1, lon1, lat2, lon2);
        //  former use of jcoord library removed because of licensing incompatibilities
        //	return new LatLng(lat1, lon1).distance(new LatLng(lat2, lon2)) * 1000;
    }

    public static double getDistance(ILocation a, ILocation b) {
        double result = getDistance(a.getLat(), a.getLon(), b.getLat(),
                b.getLon());
        if (Double.isNaN(result)) {
            if (a.getLat() == b.getLat() && a.getLon() == b.getLon()) {
                return 0.0;
            }
            return Double.NaN;
        }
        return result;
    }

    public static double getSimpleDistance(double lat1, double lon1,
            double lat2, double lon2) {
        double lat = (lat1 + lat2) / 2 * 0.01745;
        double dx = 111.3 * StrictMath.cos(lat) * (lon1 - lon2);
        double dy = 111.3 * (lat1 - lat2);
        double distance = StrictMath.sqrt(dx * dx + dy * dy);
        return distance * 1000;
    }

    public static double getPreciseDistance(double lat1, double lon1,
            double lat2, double lon2) {

        double lat = new BigDecimal(lat1 + lat2).divide(new BigDecimal(2), RoundingMode.HALF_UP).doubleValue() * 0.01745;
        double dx = 111.3 * Math.cos(lat) * (lon1 - lon2);
        double dy = 111.3 * (lat1 - lat2);
        double distance = MathUtil.sqrt(new BigDecimal(dx * dx + dy * dy), RoundingMode.HALF_UP).doubleValue();
        return distance * 1000;
    }

    public static double getSimpleDistance(ILocation a, ILocation b) {
        return getSimpleDistance(a.getLat(), a.getLon(), b.getLat(), b.getLon());
    }

    /**
     * Calculates geodetic distance between two points specified by
     * latitude/longitude using Vincenty inverse formula for ellipsoids
     *
     * @param lat1 first point latitude in decimal degrees
     * @param lon1 first point longitude in decimal degrees
     * @param lat2 second point latitude in decimal degrees
     * @param lon2 second point longitude in decimal degrees
     * @return
     * @returns distance in meters between points with 5.10<sup>-4</sup>
     * precision
     * @see <a
     * href="http://www.movable-type.co.uk/scripts/latlong-vincenty.html"></a>
     */
    public static double getDistVincentyFast(double lat1, double lon1, double lat2, double lon2) {
        double a = 6378137, b = 6356752.314245, f = 1 / 298.257223563; // WGS-84 ellipsoid params
        double L = Math.toRadians(lon2 - lon1);
        double U1 = Math.atan((1 - f) * Math.tan(Math.toRadians(lat1)));
        double U2 = Math.atan((1 - f) * Math.tan(Math.toRadians(lat2)));
        double sinU1 = Math.sin(U1), cosU1 = Math.cos(U1);
        double sinU2 = Math.sin(U2), cosU2 = Math.cos(U2);

        double sinLambda, cosLambda, sinSigma, cosSigma, sigma, sinAlpha, cosSqAlpha, cos2SigmaM;
        double lambda = L, lambdaP, iterLimit = 100;
        do {
            sinLambda = Math.sin(lambda);
            cosLambda = Math.cos(lambda);
            sinSigma = Math.sqrt((cosU2 * sinLambda) * (cosU2 * sinLambda)
                    + (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda) * (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda));
            if (sinSigma == 0) {
                return 0; // co-incident points
            }
            cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * cosLambda;
            sigma = Math.atan2(sinSigma, cosSigma);
            sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
            cosSqAlpha = 1 - sinAlpha * sinAlpha;
            cos2SigmaM = cosSigma - 2 * sinU1 * sinU2 / cosSqAlpha;
            if (Double.isNaN(cos2SigmaM)) {
                cos2SigmaM = 0; // equatorial line: cosSqAlpha=0 (§6)
            }
            double C = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));
            lambdaP = lambda;
            lambda = L + (1 - C) * f * sinAlpha
                    * (sigma + C * sinSigma * (cos2SigmaM + C * cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)));
        } while (Math.abs(lambda - lambdaP) > 1e-12 && --iterLimit > 0);

        if (iterLimit == 0) {
            return Double.NaN; // formula failed to converge
        }
        double uSq = cosSqAlpha * (a * a - b * b) / (b * b);
        double A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
        double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));
        double deltaSigma = B
                * sinSigma
                * (cos2SigmaM + B
                / 4
                * (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM) - B / 6 * cos2SigmaM
                * (-3 + 4 * sinSigma * sinSigma) * (-3 + 4 * cos2SigmaM * cos2SigmaM)));
        double dist = b * A * (sigma - deltaSigma);

        return dist;
    }

    public static double getDistVincentySlow(double lat1, double lon1, double lat2, double lon2) {
        double a = 6378137, b = 6356752.314245, f = 1 / 298.257223563; // WGS-84 ellipsoid params
        double L = Math.toRadians(lon2 - lon1);
        double U1 = StrictMath.atan((1 - f) * StrictMath.tan(Math.toRadians(lat1)));
        double U2 = StrictMath.atan((1 - f) * StrictMath.tan(Math.toRadians(lat2)));
        double sinU1 = StrictMath.sin(U1), cosU1 = StrictMath.cos(U1);
        double sinU2 = StrictMath.sin(U2), cosU2 = StrictMath.cos(U2);

        double sinLambda, cosLambda, sinSigma, cosSigma, sigma, sinAlpha, cosSqAlpha, cos2SigmaM;
        double lambda = L, lambdaP, iterLimit = 100;
        do {
            sinLambda = StrictMath.sin(lambda);
            cosLambda = StrictMath.cos(lambda);
            sinSigma = StrictMath.sqrt((cosU2 * sinLambda) * (cosU2 * sinLambda)
                    + (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda) * (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda));
            if (sinSigma == 0) {
                return 0; // co-incident points
            }
            cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * cosLambda;
            sigma = StrictMath.atan2(sinSigma, cosSigma);
            sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
            cosSqAlpha = 1 - sinAlpha * sinAlpha;
            cos2SigmaM = cosSigma - 2 * sinU1 * sinU2 / cosSqAlpha;
            if (Double.isNaN(cos2SigmaM)) {
                cos2SigmaM = 0; // equatorial line: cosSqAlpha=0 (§6)
            }
            double C = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));
            lambdaP = lambda;
            lambda = L + (1 - C) * f * sinAlpha
                    * (sigma + C * sinSigma * (cos2SigmaM + C * cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)));
        } while (StrictMath.abs(lambda - lambdaP) > 1e-12 && --iterLimit > 0);

        if (iterLimit == 0) {
            return Double.NaN; // formula failed to converge
        }
        double uSq = cosSqAlpha * (a * a - b * b) / (b * b);
        double A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
        double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));
        double deltaSigma = B
                * sinSigma
                * (cos2SigmaM + B
                / 4
                * (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM) - B / 6 * cos2SigmaM
                * (-3 + 4 * sinSigma * sinSigma) * (-3 + 4 * cos2SigmaM * cos2SigmaM)));
        double dist = b * A * (sigma - deltaSigma);

        return dist;
    }

    /**
     * Only usable for very short distances.
     *
     * @param a
     * @param b
     * @return
     */
    public static ILocation getMidwayLocation(ILocation a, ILocation b) {
        BigDecimal TWO = new BigDecimal(2);
        double newLat = a.getLat() + new BigDecimal(b.getLat() - a.getLat()).divide(TWO, RoundingMode.HALF_UP).doubleValue();
        double newLon = a.getLon() + new BigDecimal(b.getLon() - a.getLon()).divide(TWO, RoundingMode.HALF_UP).doubleValue();
        return new GPSPoint(newLat, newLon);
    }

    public static double getGradient(double lat1, double lon1, double lat2,
            double lon2) {
        // m = (delta_y / delta_x)

        // border cases
        if (Math.abs(lon2 - lon1) < 10e-4) {
            if (lat2 - lat1 == 0) {
                return 0;
            } else if (lat2 - lat1 > 0) {
                return Double.POSITIVE_INFINITY;
            } else {
                return Double.NEGATIVE_INFINITY;
            }
        }
        return (lat2 - lat1) / (lon2 - lon1);
    }

    public static double getGradient(ILocation a, ILocation b) {
        return getGradient(a.getLat(), a.getLon(), b.getLat(), b.getLon());
    }

    public static double getDistancePointToEdge(ILocation point,
            ILocation start, ILocation end) {
        // project to edge and get distance to projection
        ILocation projection = getProjectionPoint(point, start, end);
        if (projection != null) {
            return getDistance(point, projection);
        }

        double pointToA = getDistance(start, point);
        double pointToB = getDistance(end, point);
        return Math.min(pointToA, pointToB);
    }

    public static boolean isDistancePointToTraceBelowLimit(ILocation point,
            List<? extends ILocation> list, double limit) {
        double distHere = 0;
        if (list.size() == 1) {
            return getDistance(point, list.get(0)) < limit;
        }
        for (int j = 0; j < list.size() - 1; j++) {
            distHere = GPSCalc.getDistancePointToEdge(point, list.get(j),
                    list.get(j + 1));
            if (distHere < limit) {
                return true;
            }
        }
        return false;
    }

    public static double[] distancePointToTrace(ILocation point,
            List<? extends ILocation> list) {
        int minPos = 0;
        double distHere = 0;
        double minDist = Double.MAX_VALUE;
        if (list.size() == 1) {
            return new double[]{getDistance(point, list.get(0)), 0};
        }
        for (int j = minPos; j < list.size() - 1; j++) {
            distHere = GPSCalc.getDistancePointToEdge(point, list.get(j),
                    list.get(j + 1));
            if (distHere < minDist) {
                minDist = distHere;
                minPos = j;
            }
            if (Double.isNaN(minDist)) {
                minDist = Double.MAX_VALUE;
            }
        }
        return new double[]{minDist, minPos};
    }

    public static double getAngleBetweenEdges(ILocation pointA1,
            ILocation pointA2, ILocation pointB1, ILocation pointB2) {
        Float64Vector vecA = getVector(pointA1, pointA2);
        Float64Vector vecB = getVector(pointB1, pointB2);
        return Math.toDegrees(Math.acos((vecA.times(vecB).divide(vecA
                .normValue() * vecB.normValue())).doubleValue()));
    }

    public static double getAngleBetweenEdges(IEdge<? extends ILocation> edge1,
            IEdge<? extends ILocation> edge2) {
        return getAngleBetweenEdges(edge1.getFrom(), edge1.getTo(),
                edge2.getFrom(), edge2.getTo());
    }

    public static double getSmallGradientFromEdges(
            IEdge<? extends ILocation> edgeA, IEdge<? extends ILocation> edgeB) {
        return getSmallGradientFromEdges(edgeA.getFrom(), edgeA.getTo(),
                edgeB.getFrom(), edgeB.getTo());
    }

    /**
     * This method calculates a mismatch in two gradients.
     *
     * @param pointA1
     * @param pointA2
     * @param pointB1
     * @param pointB2
     * @return 0 for identical gradients, 2 for opposite gradients and in
     * between.
     * @author Sebastian Müller
     */
    public static double getSmallGradientFromEdges(ILocation pointA1,
            ILocation pointA2, ILocation pointB1, ILocation pointB2) {
        double mA = Double.MAX_VALUE;
        boolean diff1 = false;
        boolean diff2 = false;
        if (!(pointA1.getY() == pointA2.getY())) {
            double d1 = pointA2.getX() - pointA1.getX();
            double d2 = pointA2.getY() - pointA1.getY();
            mA = d2 / d1;
            if (d1 < 0 && d2 < 0) {
                diff1 = true;
            }
            if (d1 < 0 && d2 > 0) {
                diff2 = true;
            }
        }
        double mB = Double.MAX_VALUE;
        if (!(pointB1.getY() == pointB2.getY())) {
            double d1 = pointB2.getX() - pointB1.getX();
            double d2 = pointB2.getY() - pointB1.getY();
            mB = d2 / d1;
            if (d1 < 0 && d2 < 0 && diff1 == true) {
                diff1 = false;
            } else if (d1 < 0 && d2 < 0 && diff1 == false) {
                diff1 = true;
            }
            if (d1 < 0 && d2 > 0 && diff2 == true) {
                diff2 = false;
            } else if (d1 < 0 && d2 > 0 && diff2 == false) {
                diff2 = true;
            }
        }

        if (mA > 1 || mA < -1) {
            mA = (2 - (1 / Math.abs(mA))) * Math.signum(mA);
        }
        if (mB > 1 || mB < -1) {
            mB = (2 - (1 / Math.abs(mB))) * Math.signum(mB);
        }

        if (Math.signum(mA) != Math.signum(mB) && diff1 == diff2) {
            return Math.abs(mA) + Math.abs(mB);
        } else if (Math.signum(mA) == Math.signum(mB) && !diff1 && !diff2) {
            return Math.abs(mA - mB);
        } else if (Math.signum(mA) == Math.signum(mB) && (diff1 || diff2)) {
            return 4 - Math.abs(mA - mB);
        } else {
            return 4 - (Math.abs(mA) + Math.abs(mB));
        }

    }

    public static ILocation getPointAverage(List<ILocation> locations) {
        if (locations.isEmpty()) {
            return null;
        }
        GPSPoint zero = new GPSPoint(0, 0);
        Float64Vector sum = getVector(zero);
        for (ILocation point : locations) {
            sum = sum.plus(getVector(point));
        }
        Float64Vector result = sum.times(1.0 / locations.size());
        return new GPSPoint(result.getValue(0), result.getValue(1));
    }

    public static Float64Vector getVector(ILocation a) {
        GPSPoint zero = new GPSPoint(0, 0);
        return getVector(zero, a);
    }

    public static Float64Vector getVector(ILocation a, ILocation b) {
        return Float64Vector.valueOf(b.getLat() - a.getLat(),
                b.getLon() - a.getLon());
    }

    public static double getDistancePointToEdge(ILocation point,
            IEdge<? extends ILocation> edge) {
        return getDistancePointToEdge(point, edge.getFrom(), edge.getTo());
    }

    public static ILocation getProjectionPoint(ILocation point,
            ILocation start, ILocation end) {
        ILocation result = new GPSPoint();
        Float64Vector w = getVector(start, point);
        Float64Vector a = getVector(start, end);
        Float64 factor = (w.times(a).divide(Math.pow(a.normValue(), 2)));
        Float64Vector proj = getVector(new GPSPoint(0, 0), start).plus(
                a.times(factor));
        if (!((proj.get(0).doubleValue() >= start.getLat() && proj.get(0)
                .doubleValue() <= end.getLat()) || (proj.get(0).doubleValue() <= start
                .getLat() && proj.get(0).doubleValue() >= end.getLat()))) {
            return null;
        }
        if (!((proj.get(1).doubleValue() >= start.getLon() && proj.get(1)
                .doubleValue() <= end.getLon()) || (proj.get(1).doubleValue() <= start
                .getLon() && proj.get(1).doubleValue() >= end.getLon()))) {
            return null;
        }
        result.setLat(proj.get(0).doubleValue());
        result.setLon(proj.get(1).doubleValue());
        return result;
    }

    public static ILocation getProjectedPointToEdge(ILocation point, ILocation edgeStart, ILocation edgeEnd) {
        ILocation result = new GPSPoint(0, 0);

        Float64Vector vectorA = getVector(edgeStart, point);
        Float64Vector vectorB = getVector(edgeStart, edgeEnd);

        Float64 numeriator = vectorA.times(vectorB);
        Float64 denumerator = vectorB.times(vectorB);

        Float64Vector projection = vectorB.times(numeriator.divide(denumerator));
        if (projection.getDimension() == 2) {
            result.setLatLon(projection.getValue(0), projection.getValue(1));
        }
        return result;
    }

    public static ILocation getProjectionPoint(ILocation point,
            IEdge<? extends ILocation> edge) {
        return getProjectionPoint(point, edge.getFrom(), edge.getTo());
    }

    /**
     * Measure the distance between two points in meter
     *
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return
     */
    public static double getDistanceTwoPointsMeter(double lat1, double lon1,
            double lat2, double lon2) {
        double er = 6366.707;

        double latFrom = Math.toRadians(lat1);
        double latTo = Math.toRadians(lat2);
        double lngFrom = Math.toRadians(lon1);
        double lngTo = Math.toRadians(lon2);

        double d = Math.acos(Math.sin(latFrom) * Math.sin(latTo) + Math.cos(latFrom)
                * Math.cos(latTo) * Math.cos(lngTo - lngFrom)) * er;
        d = d * 1000;
        return d;
    }

    /**
     * Measure the distance between two points in meter
     *
     * @param a
     * @param b
     * @return
     */
    public static double getDistanceTwoPointsMeter(ILocation a, ILocation b) {
        return getDistanceTwoPointsMeter(a.getLat(), a.getLon(), b.getLat(), b.getLon());
    }

    /**
     * Measure the (simple) distance between two points
     *
     * @param a
     * @param b
     * @return
     */
    public static double getDistanceTwoPoints(ILocation a, ILocation b) {
        return getDistanceTwoPoints(a.getLat(), a.getLon(), b.getLat(),
                b.getLon());
    }

    /**
     * Measure the (simple) distance between two points
     *
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return
     */
    public static double getDistanceTwoPoints(double lat1, double lon1,
            double lat2, double lon2) {
        double lat = (lat1 + lat2) / 2 * 0.01745;
        double dx = 111.3 * Math.cos(lat) * (lon1 - lon2);
        double dy = 111.3 * (lat1 - lat2);
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance * 1000;
    }

    /**
     * Measure the distance between a point and (0,0) in Float 64 bit
     *
     * @param a
     * @return
     */
    public static Float64Vector getDistanceTwoPointsFloat64(ILocation a) {
        GPSPoint zero = new GPSPoint(0, 0);
        return getDistanceTwoPointsFloat64(zero, a);
    }

    /**
     * Measure the distance between two points in Float 64 bit
     *
     * @param a
     * @param b
     * @return
     */
    public static Float64Vector getDistanceTwoPointsFloat64(ILocation a,
            ILocation b) {
        return Float64Vector.valueOf(b.getLat() - a.getLat(),
                b.getLon() - a.getLon());
    }

    /**
     * Distance between two Points (WARNING: Fischer's Works) SquaredEuclidian
     *
     * @param from
     * @param to
     * @return
     */
    public static double getDistanceTwoPointsDouble(ILocation from, ILocation to) {
        double deltaLat = from.getLat() - to.getLat();
        double deltaLong = from.getLon() - to.getLon();
        return Math.sqrt(deltaLat * deltaLat + deltaLong * deltaLong);
    }

    /**
     * Measure the distance between a point and edge in Double
     *
     * @param q
     * @param s1
     * @param s2
     * @return
     */
    public static double getDistancePointToEdgeDouble(GPSPoint q, GPSPoint s1,
            GPSPoint s2) {
        double vx = -(s2.getLat() - s1.getLat());
        double vy = s2.getLon() - s1.getLon();

        double rx = s1.getLon() - q.getLon();
        double ry = s1.getLat() - q.getLat();

        // Calculate |v dot r|
        double f1 = Math.abs(vx * rx + vy * ry);
        double f2 = Math.sqrt(vx * vx + vy * vy);
        if (f2 == 0) {
            return Math.sqrt(rx * rx + ry * ry);
        }
        return (f1 / f2);
    }

    /**
     * Measure the distance between point and edge in Meter
     *
     * @param point
     * @param start
     * @param end
     * @return
     */
    public static double getDistancePointToEdgeMeter(ILocation point,
            ILocation start, ILocation end) {
        // project to edge and get distance to projection
        // if(!point.isRelevant() || !start.isRelevant() || !end.isRelevant())
        // return Double.MAX_VALUE;
        ILocation projection = getProjectionPoint(point, start, end);
        if (projection != null) {
            return getDistanceTwoPointsMeter(point, projection);
        }

        // double angleWithStart = getAngleBetweenEdges(start, point, start,
        // end);
        // double angleWithEnd = getAngleBetweenEdges(point, end, start,
        // end);
        // if ((angleWithStart > 90 && angleWithEnd < 90)
        // || (angleWithStart < 90 && angleWithEnd > 90)) {
        double pointToA = getDistanceTwoPointsMeter(start, point);
        double pointToB = getDistanceTwoPointsMeter(end, point);
        return Math.min(pointToA, pointToB);
        // }
    }

    /**
     * Measure the distance between point and edge
     *
     * @param point
     * @param edge
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static double getDistancePointToEdgeMeter(ILocation point, IEdge edge) {
        return getDistancePointToEdgeMeter(point, edge.getFrom(), edge.getTo());
    }

    /**
     * Measure the distance between point and traces (or List of edge)
     *
     * @param point
     * @param list
     * @return the distance and the position
     */
    public static double[] getDistancePointToTraceMeter(ILocation point,
            List<? extends ILocation> list) {
        int minPos = 0;
        double distHere = 0;
        double minDist = Double.MAX_VALUE;
        if (list.size() == 1) {
            return new double[]{
                getDistanceTwoPointsMeter(point, list.get(0)), 0};
        }
        for (int j = minPos; j < list.size() - 1; j++) {

            distHere = GPSCalc.getDistancePointToEdgeMeter(point, list.get(j),
                    list.get(j + 1));

            if (distHere < minDist) {
                minDist = distHere;
                minPos = j;
            }
            if (Double.isNaN(minDist)) {
                minDist = Double.MAX_VALUE;
            }
        }
        return new double[]{minDist, minPos};
    }

    public static double traceLengthMeter(List<? extends ILocation> trace) {
        double sum = 0;
        for (int i = 0; i < trace.size() - 1; i++) {
            sum += getDistanceTwoPointsMeter(trace.get(i), trace.get(i + 1));
        }
        return sum;
    }

    public static double traceLengthMeter(GPSSegment segment) {
        double sum = 0;
        for (int i = 0; i < segment.size() - 1; i++) {
            sum += getDistanceTwoPointsMeter(segment.get(i), segment.get(i + 1));

        }
        return sum;
    }

    /**
     * Get Point between Edge with given proportional (WARNING: Fischer's Works)
     *
     * @param t
     * @param from
     * @param to
     * @return
     */
    public static ILocation getPointAt(double t, ILocation from, ILocation to) {
        return new GPSPoint((1 - t) * from.getLat() + t * to.getLat(), (1 - t)
                * from.getLon() + t * to.getLon());
    }

    /**
     * Comparing two double
     *
     * @param d1
     * @param d2
     * @return
     */
    public static int compareDouble(double d1, double d2) {
        Double r1 = Math.round(d1 * precision) / precision;
        Double r2 = Math.round(d2 * precision) / precision;
        return (Double.compare(r1, r2));
    }

    /**
     * TODO in meter? Circle-Segment intersection
     *
     * @http://mathworld.wolfram.com/Circle-LineIntersection.html
     * @param ax
     * @param ay
     * @param bx
     * @param by
     * @param cx
     * @param cy
     * @param radius
     * @return
     */
    public static Interval getSegmentCircleIntersection2(double ax, double ay,
            double bx, double by, // Segment
            double cx, double cy, // Circle center
            double radius) {
        final double vx = bx - ax;
        final double vy = by - ay;
        final double sx = ax - cx;
        final double sy = ay - cy;

        // polygon
        final double a = vx * vx + vy * vy; // V^2
        final double b = 2 * (sx * vx + sy * vy); // 2(S dot V)
        final double c = (sx * sx + sy * sy) - radius * radius; // S^2 - r^2

        Interval result = new Interval();

        if (a == 0.) { // Input is a line degraded to a point
            final double incircle = Math.sqrt(sx * sx + sy * sy);
            if (incircle <= radius) {
                result.start = 0.;
                result.end = 1.;
            }
        } else {

            // Discriminant
            final double D = b * b - 4 * a * c;

            if (D >= 0) {
                final double Dsq = Math.sqrt(D);

                final double r1 = (-b - Dsq) / (2 * a);

                final double r2 = (D == 0) ? r1 : (-b + Dsq) / (2 * a);

                if (r1 < r2 && r1 < 1. && r2 > 0.) {
                    result.start = (r1 < 0.) ? 0. : r1;
                    result.end = (r2 > 1.) ? 1. : r2;
                }
            }
        }

        return result;
    }

    /**
     * Intersection (WARNING: Fischer's Works)
     *
     * @param p1
     * @param p2
     * @param q
     * @param r1
     * @param r2
     * @return
     */
    public static ILocation IntersectionOfPerpendicularWithLine(ILocation p1,
            ILocation p2, ILocation q, ILocation r1, ILocation r2) {
        final double p1x = p1.getLon();
        final double p1y = p1.getLat();
        final double p2x = p2.getLon();
        final double p2y = p2.getLat();

        final double qx = q.getLon();
        final double qy = q.getLat();

        final double a = p1y - p2y;
        final double b = p2x - p1x;

        final double pa = -b;
        final double pb = a;
        final double pc = pa * qx + pb * qy;

        final double r1x = r1.getLon();
        final double r1y = r1.getLat();
        final double r2x = r2.getLon();
        final double r2y = r2.getLat();

        final double ra = r1y - r2y;
        final double rb = r2x - r1x;
        final double rc = r2x * r1y - r2y * r1x;

        final double D = (ra * pb) - (pa * rb);

        if (D != 0.) {
            return new GPSPoint((ra * pc - pa * rc) / D, (pb * rc - rb * pc)
                    / D);
        } else {
            return null;
        }
    }

    public static ILocation intersectionWithPerpendicularThrough(
            GPSPoint p1, GPSPoint p2, GPSPoint q) {
        final double p1x = p1.getLon();
        final double p1y = p1.getLat();
        final double p2x = p2.getLon();
        final double p2y = p2.getLat();

        final double qx = q.getLon();
        final double qy = q.getLat();

        final double a = p1y - p2y;
        final double b = p2x - p1x;
        final double c = p2x * p1y - p2y * p1x;

        final double pa = -b;
        final double pb = a;
        final double pc = pa * qx + pb * qy;

        final double D = (a * pb) - (pa * b);

        if (D != 0.) {
            return new AbstractLocation((a * pc - pa * c) / D, (pb * c - b * pc) / D);
        } else {
            return null;
        }
    }

    public static ILocation intersection(ILocation p1, ILocation p2, ILocation q1, ILocation q2) {
        final double p1x = p1.getLon();
        final double p1y = p1.getLat();
        final double p2x = p2.getLon();
        final double p2y = p2.getLat();

        final double q1x = q1.getLon();
        final double q1y = q1.getLat();
        final double q2x = q2.getLon();
        final double q2y = q2.getLat();

        final double a1 = p1y - p2y;
        final double b1 = p2x - p1x;
        final double c1 = p2x * p1y - p2y * p1x;

        final double a2 = q1y - q2y;
        final double b2 = q2x - q1x;
        final double c2 = q2x * q1y - q2y * q1x;

        final double D = (a1 * b2) - (a2 * b1);

        if (D != 0.) {
            return new AbstractLocation((a1 * c2 - a2 * c1) / D, (b2 * c1 - b1 * c2) / D);
        } else {
            return null;
        }
    }

//	public static void main(String[] args) {
//		ILocation p1 = new GPSPoint(3, 1);
//		ILocation p2 = new GPSPoint(5, 1);
//		ILocation q1 = new GPSPoint(2, 6);
//		ILocation q2 = new GPSPoint(2, 7);
//		System.out.println(Intersection(p1, p2, q1, q2));
//	}
    public static boolean PntOnLine(ILocation p, ILocation q, ILocation t) {
        /*
         * given a line through P:(px,py) Q:(qx,qy) and T:(tx,ty)
         * return 0 if T is not on the line through      <--P--Q-->
         *        1 if T is on the open ray ending at P: <--P
         *        2 if T is on the closed interior along:   P--Q
         *        3 if T is on the open ray beginning at Q:    Q-->
         */
        final double px = p.getLon();
        final double py = p.getLat();
        final double qx = q.getLon();
        final double qy = q.getLat();
        final double tx = t.getLon();
        final double ty = t.getLat();

        if ((px == qx) && (py == qy)) {
            if ((tx == px) && (ty == py)) {
                return true;
            } else {
                return false;
            }
        }

        if (Math.abs((qy - py) * (tx - px) - (ty - py) * (qx - px))
                >= (Math.max(Math.abs(qx - px), Math.abs(qy - py)))) {
            return false;
        }
        if (((qx < px) && (px < tx)) || ((qy < py) && (py < ty))) {
            return false;
        }
        if (((tx < px) && (px < qx)) || ((ty < py) && (py < qy))) {
            return false;
        }
        if (((px < qx) && (qx < tx)) || ((py < qy) && (qy < ty))) {
            return false;
        }
        if (((tx < qx) && (qx < px)) || ((ty < qy) && (qy < py))) {
            return false;
        }

        return true;
    }

    public static AggNode calculateMean(AggNode locationToMove, Collection<GPSPoint> affectedTraceLocations,
            double epsilon, AggContainer aggContainer, boolean dampFactor) {
        final double alon = locationToMove.getLon();
        final double alat = locationToMove.getLat();

        double slon = 0;
        double slat = 0;

        int div = 0;

        for (ILocation ti : affectedTraceLocations) {
            double dist = ((GPSPoint) locationToMove).getDistanceTo((GPSPoint) ti);
//			System.out.println("dist = " + dist);
            if (dist > epsilon) {
                continue;
            }

//			double damp = damp(dist, epsilon);
            double damp;
            if (dampFactor) {
                if (locationToMove.getK() >= 4) {
                    damp = damp(dist, epsilon) / (Math.log10(locationToMove.getK()) / Math.log10(2));
                } else {
                    damp = damp(dist, epsilon);
                }
            } else {
                damp = damp(dist, epsilon);
            }

            slon += damp * (ti.getLon() - alon);
            slat += damp * (ti.getLat() - alat);
            ++div;
        }

        if (div == 0) {
            return locationToMove;
        }

        slon /= div;
        slat /= div;

//		return null;
        return new AggNode(slat + alat, slon + alon, aggContainer);
    }

    public static AggNode moveLocation(AggNode fix, AggNode toMove, AggContainer aggContainer) {
        // along To Perpendicular from Trace
        final double alon = toMove.getLon();
        final double alat = toMove.getLat();
        final double elon = fix.getLon();
        final double elat = fix.getLat();

        final double n = 0.5;

        return new AggNode((alat * n + elat) / (n + 1.), (alon * n + elon) / (n + 1.), aggContainer);
    }

    private static double damp(double distance, double epsilon) {
        final double d = distance / (4 * epsilon);
        final double fval = Math.exp(-(5 * d * d)); // clamp into [0..1] approx.

        if (fval <= 0) {
            return 0.;
        } else if (fval >= 1) {
            return 1.;
        } else {
            return fval;
        }
    }
}
