package geometries;

import primitives.*;
import java.util.List;

/**
 * interface for finding intersestions points
 */
public abstract class Intersectable {


    public static class GeoPoint{

        public final Geometry geometry;
        public final Point point;

        public GeoPoint(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
        }
    }

    /**
     *
     * @param ray {@link Ray} pointing toward the objects
     * @return List of intersections {@link Point}
     */
    public final List<Point> findIntersections(Ray ray){
        List<GeoPoint> geoList = findGeoIntersections(ray);
        return geoList == null? null
                :geoList.stream()
                .map((gp -> gp.point))
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
