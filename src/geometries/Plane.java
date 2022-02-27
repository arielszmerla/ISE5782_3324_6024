package geometries;

import primitives.Point;
import primitives.Vector;
/**
 * Plane class represents plane (flatted) in 3D Cartesian coordinate system
 * @author Gal&Ariel
 */
public class Plane implements Geometry{
    /**
     * point on the plane
     */
    final private Point _p0;
    /**
     * normal vector at point p0
     */
    final private Vector _normal;

    /**
     * Plane constructor based on three points on the plane
     * @param p1
     * @param p2
     * @param p3
     */
    public Plane(Point p1, Point p2, Point p3) {
        _p0 = p1;
       Vector v = p2.subtract(p1);
       Vector u = p3.subtract(p1);
       Vector n = v.crossProduct(u);
       _normal = n.normalize();
    }
    /**
     * Get the normal vector on plane based-on point
     * @param point point where the normal
     * @return normal vector
     */
    @Override
    public Vector getNormal(Point point) {
        return _normal;
    }

    public Vector getNormal() {
        return _normal;
    }
}
