package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.isZero;

/**
 * Sphere class represents 3D sphere in 3D Cartesian coordinate system
 *
 * @author Gal&Ariel
 */
public class Sphere implements Geometry {
    /**
     * Sphere's center
     */
    final private Point _center;
    /**
     * Sphere's radius
     */
    final private double _radius;

    /**
     * Sphere constructor based on center and radius.
     *
     * @param center
     * @param radius
     */
    public Sphere(Point center, double radius) {
        if (radius <= 0)
            throw new IllegalArgumentException("Radius cannot be lower than 0");
        _center = center;
        _radius = radius;
    }

    /**
     * Get the normal vector on sphere based-on point
     *
     * @param point point where the normal
     * @return normal vector {@link Vector}
     */
    @Override
    public Vector getNormal(Point point) {
        return point.subtract(_center).normalize();
    }

    /**
     * get the center of sphere
     *
     * @return Sphere's center point
     */
    public Point getCenter() {
        return _center;
    }

    /**
     * get the radius of sphere
     *
     * @return Sphere's radius
     */
    public double getRadius() {
        return _radius;
    }


    @Override
    public String toString() {
        return "_center=" + _center +
                ", _radius=" + _radius;
    }

    /**
     * @param ray {@link Ray} pointing toward the objects
     * @return List of intersections {@link Point}
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        if (ray.getP0().equals(_center))
            throw new IllegalArgumentException("can't start from center");
        Vector u = _center.subtract(ray.getP0());
        double tm = ray.getDir().dotProduct(u);
        double d = Math.sqrt(u.lengthSquared() - (tm * tm));
        if (d >= _radius || isZero(d - _radius))
            return null;
        double th = Math.sqrt(_radius * _radius - d * d);
        if (tm - th > 0 && tm + th > 0)
            return List.of(ray.getP0().add(ray.getDir().scale(tm - th)), ray.getP0().add(ray.getDir().scale(tm + th)));
        if (tm - th > 0 && !(tm + th > 0))
            return List.of(ray.getP0().add(ray.getDir().scale(tm - th)));
        if (!(tm - th > 0) && tm + th > 0)
            return List.of(ray.getP0().add(ray.getDir().scale(tm + th)));
        return null;

    }
}
