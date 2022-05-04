package primitives;
import geometries.Intersectable.GeoPoint;


import java.util.List;

import static primitives.Util.isZero;

/**
 * Sphere class represents 3D ray in 3D Cartesian coordinate system
 * @author Gal&Ariel
 */
public class Ray {
    private static final double EPS = 0.1;
    /**
     * Ray's start point
     */
    final private Point _p0;
    /**
     * Ray's direction vector
     */
    final private Vector _dir;

    /**
     * Ray constructor based on start point p0 and direction vector
     * @param p0 start point
     * @param dir direction vector
     */
    public Ray(Point p0, Vector dir) {
        _p0 = p0;
        _dir = dir.normalize();
    }

    /**
     * Ray constructor based on start point p0 and direction vector
     * @param p0 start point
     * @param dir direction vector
     */
    public Ray(Point p0, Vector n, Vector dir) {
        double eps= dir.dotProduct(n)>=0?EPS:-EPS;
        _p0 = p0.add(n.scale(eps));
        _dir = dir.normalize();
    }

    /**
     * get the start point of ray
     * @return Rays's start point
     */
    public Point getP0() {
        return _p0;
    }
    /**
     * get the direction vector of ray
     * @return Ray's direction vector
     */
    public Vector getDir() {
        return _dir.normalize();
    }
    @Override
    public String toString() {
        return "_p0=" + _p0.toString() +
                ", _dir=" + _dir.toString();
    }

    /**
     * check equality between two rays
     * @param o
     * @return if ray o compare to this ray
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ray ray = (Ray) o;
        return _p0.equals(ray._p0) && _dir.equals( ray._dir);
    }

    /**
     *
     * get Point on specific distance on the ray
     *
     * @param t scalar - distance from p0 on the ray
     * @return new {@link Point} on the ray
     */
    public Point getPoint(double t) {
        if(isZero(t))
            throw new IllegalArgumentException("t equals 0 causes illegal Vector Zero");
        return _p0.add(_dir.scale(t));
    }


   public Point findClosestPoint(List<Point> points){
        if(points.isEmpty())
            return null;
        Point p0= this.getP0();
        Point shortestDistance= points.get(0);
        for(Point pt : points){
            if (p0.distance(pt) < p0.distance(shortestDistance)) {
                shortestDistance = pt;
            }
        }

        return shortestDistance;
   }
    /**
     * get the closest GeoPoint in the list of points
     * @param points list of intersection points
     * @return the closest point
     */
    public GeoPoint getClosestGeoPoint(List<GeoPoint> points) {
        if (points == null || points.size()==0)
            return null;
        GeoPoint myPoint = points.get(0);
        for (var point : points
        ) {
            if (_p0.distance(myPoint._point) > _p0.distance(point._point)) {
                myPoint = point;
            }
        }

        return myPoint;
    }

}
