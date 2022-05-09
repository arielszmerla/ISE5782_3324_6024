package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.isZero;

public class Circle extends Geometry{
    /**
     * Sphere's center
     */
    final private Point _center;
    /**
     * Sphere's radius
     */
    final private Vector _radius;
    /**
     * Sphere constructor based on center and radius.
     *
     * @param center
     * @param radius
     */
    public Circle(Point center, Point radius) {
        if (radius == center)
            throw new IllegalArgumentException("Radius cannot be lower than 0");
        _center=center;
        _radius=new Vector(radius.substract(_center).get_xyz());
    }

    /**
     * Get the normal vector on sphere based-on point
     * @param point point where the normal
     * @return normal vector {@link Vector}
     */
    @Override
    public Vector getNormal(Point point) {
        return point.substract(_center).normalize();
    }

    /**
     * get the center of sphere
     * @return Sphere's center point
     */
    public Point getCenter() {
        return _center;
    }

    /**
     * get the radius of sphere
     * @return Sphere's radius
     */
    public Vector getRadius() {
        return _radius;
    }

    /** textual description of sphere
     * @return textual description of sphere
     */
    @Override
    public String toString() {
        return "_center=" + _center +
                ", _radius=" + _radius;
    }

    /**
     * find intersections (GeoPoints) on sphere based-on given ray
     * @param ray {@link Ray} pointing toward the objects
     * @return List of intersections {@link Point}
     */
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {

        Plane p = new Plane(_center,_radius);
        List<GeoPoint> geoIntersections= p.findGeoIntersections(ray);
        if (geoIntersections!=null) {
            double distance = _radius.length();
            for (GeoPoint point : geoIntersections) {
                if (point._point.distance(_center) == distance)
                    return List.of(new GeoPoint(this, point._point));
            }
        }
        return null;

    }
}
