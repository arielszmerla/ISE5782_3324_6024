package geometries;

import primitives.Point;
import primitives.Vector;
/**
 * Sphere class represents 3D sphere in 3D Cartesian coordinate system
 * @author Gal&Ariel
 */
public class Sphere implements Geometry{
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
     * @param point point where the normal
     * @return normal vector
     */
    @Override
    public Vector getNormal(Point point) {
        return null;
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
    public double getRadius() {
        return _radius;
    }


    @Override
    public String toString() {
        return "_center=" + _center +
                ", _radius=" + _radius  ;
    }
}
