package geometries;

import primitives.*;

import java.util.List;

import static primitives.Util.*;

/**
 * Plane class represents plane (flatted) in 3D Cartesian coordinate system
 * @author Gal&Ariel
 */
public class Plane extends Geometry{
    /**
     * point on the plane
     */
    final private Point _q0;
    /**
     * normal vector at point p0
     */
    final private Vector _normal;

    /**
     * Plane constructor based on three points on the plane
     * @param p1 {@link Point #1}
     * @param p2 {@link Point #2}
     * @param p3 {@link Point #3}
     */
    public Plane(Point p1, Point p2, Point p3) throws IllegalArgumentException {
       Vector v = p2.subtract(p1);
       Vector u = p3.subtract(p1);
       Vector n = v.crossProduct(u);
       _q0 = p1;
       _normal = n.normalize();
    }

    /**
     * Plane constructor based on point on the plane and normal
     * @param p point {@link Point} on the plane
     * @param n normal vector {@link Vector} at point p
     */
    public Plane(Point p, Vector n){
        _q0 = p;
        _normal = n.normalize();
    }

    /**
     * Get the normal vector on plane based-on point
     * @param point {@link Point} where the normal
     * @return normal vector
     */
    @Override
    public Vector getNormal(Point point) {
        return getNormal();
    }

    /**
     * getter for normal vector
     * @return normal {@link java.util.Vector} to plane
     */
    public Vector getNormal() {
        return _normal;
    }

    /**
     * find intersections (GeoPoints) on geometry based-on given ray
     * @param ray {@link Ray} pointing toward the objects
     * @return List of intersections {@link GeoPoint}
     */
    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        Point p0 = ray.getP0();
        Vector v = ray.getDir();
        Vector n = _normal;
        if (_q0.equals(p0))
            return null;
        double nv = alignZero(n.dotProduct(v));
        if (isZero(nv))
            return null;

        Vector P0_Q0 = _q0.subtract(p0); // Q - P0
        double nP0Q0 = alignZero(n.dotProduct(P0_Q0));

        if (isZero(nP0Q0))
            return null;

        double t = alignZero(nP0Q0 / nv);
        // t should be bigger than 0
        if (t<=0)
            return null;
        return List.of(new GeoPoint(this, ray.getPoint(t)));
    }
}
