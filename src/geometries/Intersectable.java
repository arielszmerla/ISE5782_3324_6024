package geometries;

import primitives.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * interface for finding intersestions points
 */
public abstract class Intersectable {


    public static class GeoPoint{

        public  Geometry _geometry;
        public  Point _point;

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

        @Override
        public String toString() {
            return "GeoPoint{" +
                    "_geometry=" + _geometry +
                    ", _point=" + _point +
                    '}';
        }
    }

    /**
     *
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
     *
     * @param ray {@link Ray} pointing toward the objects
     * @return List of intersections {@link GeoPoint}
     */
    public final List<GeoPoint> findGeoIntersections(Ray ray){
        return findGeoIntersectionsHelper(ray);
    }

    protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray);


}
