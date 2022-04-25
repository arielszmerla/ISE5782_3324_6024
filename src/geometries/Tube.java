package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.*;

/**
 * Tube class represents 3D tube in 3D Cartesian coordinate system
 * inherits Geometry class
 * @author Gal&Ariel
 */
public class Tube extends Geometry {
    /**
     * Ray in the center of the tube
     */
    final protected Ray _axisRay;
    /**
     * Tube's radius
     */
    final protected double _radius;

    /**
     * Tube constructor based on axis ray and radius
     * @param axisRay Ray in the center of the tube
     * @param radius Tube's radius
     */
    public Tube(Ray axisRay, double radius) {
        _axisRay = axisRay;
        if (radius <= 0)
            throw new IllegalArgumentException("Radius cannot be lower than 0");
        _radius = radius;
    }

    /**
     * Get the normal vector on tube based-on point
     * @param point {@link Point} where the requested normal
     * @return normal vector {@link Vector}
     */
    @Override
    public Vector getNormal(Point point) {
        Point P0 = _axisRay.getP0();
        Vector v = _axisRay.getDir();
        Vector P0_P = point.substract(P0);
        double t = alignZero(v.dotProduct(P0_P));

        //if the point given is located on the tube bottom
        if (isZero(t)) {
            return P0_P.normalize();
        }

        Point o = P0.add(v.scale(t));
        //if the point given is located on the tube axis
        if (point.equals(o)) {
            throw new IllegalArgumentException("point cannot be on the tube axis");
        }
        Vector n = point.substract(o).normalize();
        return n;
    }

    /** textual description of tube
     * @return textual description of tube
     */
    @Override
    public String toString() {
        return "Tube{" +
                "_axisRay=" + _axisRay +
                ", _radius=" + _radius +
                '}';
    }

    /**
     *  Get ray in the center of the tube
     * @return {@link Ray} Ray in the center of the tube
     */
    public Ray getAxisRay() {
        return _axisRay;
    }

    /**
     * Get tube's radius
     * @return tube's radius
     */
    public double getRadius() {
        return _radius;
    }

    /**
     * @param ray {@link Ray} pointing toward the objects
     * @return List of intersections {@link Point}
     */
    @Override
    protected List <GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        return null;
    }

}
