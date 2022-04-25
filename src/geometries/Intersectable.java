package geometries;

import primitives.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * abstract class for finding intersestions points
 */
public abstract class Intersectable {

    /**
     * inner class represents GeoPoint
     */
    public static class GeoPoint{

        /**
         * related geometry
         */
        public  Geometry _geometry;
        /**
         * point on geometry
         */
        public  Point _point;

        /**
         * GeoPoint constructor based-on geometry and point
         * @param geometry {@link Geometry}
         * @param point {@link Point}
         */
        public GeoPoint(Geometry geometry, Point point) {
            _geometry = geometry;
            _point = point;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GeoPoint geoPoint = (GeoPoint) o;
            return this._geometry.equals(geoPoint._geometry) && _point.equals(geoPoint._point);
        }

        /** textual description of GeoPoint
         * @return textual description of GeoPoint
         */
        @Override
        public String toString() {
            return "GeoPoint{" +
                    "_geometry=" + _geometry +
                    ", _point=" + _point +
                    '}';
        }
    }

    /**
     * find intersections (regular points) on geometry based-on given ray
     * @param ray {@link Ray} pointing toward the objects
     * @return List of intersections {@link Point}
     */
    public List<Point> findIntersections(Ray ray){
        List<GeoPoint> geoList = findGeoIntersections(ray);
        return geoList == null? null
                :geoList.stream()
                .map((gp -> gp._point))
                .toList();
    }

    /**
     * find intersections (GeoPoints) on geometry based-on given ray
     * @param ray {@link Ray} pointing toward the objects
     * @return List of intersections {@link GeoPoint}
     */
    public final List<GeoPoint> findGeoIntersections(Ray ray){
        return findGeoIntersectionsHelper(ray);
    }

    /**
     * helper function that every class that inherits has to implement
     * @param ray {@link Ray} pointing toward the objects
     * @return List of intersections {@link GeoPoint}
     */
    protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray);


}
