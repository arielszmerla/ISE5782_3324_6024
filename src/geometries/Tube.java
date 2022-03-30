package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.*;

/**
 * Sphere class represents 3D tube in 3D Cartesian coordinate system
 * @author Gal&Ariel
 */
public class Tube implements Geometry {
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
     * @param point point where the normal
     * @return normal vector
     */
    @Override
    public Vector getNormal(Point point) {
        Point P0 = _axisRay.getP0();
        Vector v = _axisRay.getDir();
        Vector P0_P = point.substract(P0);
        double t = alignZero(v.dotProduct(P0_P));

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

    @Override
    public String toString() {
        return "Tube{" +
                "_axisRay=" + _axisRay +
                ", _radius=" + _radius +
                '}';
    }

    /**
     *  Get ray in the center of the tube
     * @return Ray in the center of the tube
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
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
