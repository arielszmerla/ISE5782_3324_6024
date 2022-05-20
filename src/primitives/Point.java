package primitives;

import java.util.Objects;
/**
 * Point class represents point in 3D Cartesian coordinate system
 * @author Gal&Ariel
 */
public class Point {
    public static final Point ZERO = new Point(0,0,0);
    final Double3 _xyz;

    /**
     * Point constructor based on double3D parameter
     * @param xyz
     */
    public Point(Double3 xyz) {
        _xyz = xyz;
    }

    /**
     * Point constructor based on three doubles
     * @param x
     * @param y
     * @param z
     */
    public Point(double x,double y,double z) {
        _xyz =new Double3 (x,y,z);
    }

    public Double3 get_xyz() {
        return _xyz;
    }

    @Override
    public String toString() {
        return "Point"+_xyz ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return _xyz.equals(point._xyz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_xyz);
    }

    /**
     * adding vector to point
     * @param vector
     * @return
     */
    public Point add(Vector vector) {
        return new Point(_xyz.add(vector._xyz));
    }

    /**
     * get a vector between 2 points by subtracting
     * @param p1
     * @return
     */
    public Vector subtract(Point p1) {
        return new Vector(_xyz.subtract(p1._xyz));
    }

    /**
     * @param other
     * @return (x2 - x1)^2 + (y2-y1)^2 + (z2-z1)^2
     */
    public double distanceSquared(Point other) {
        final double x1 = _xyz._d1;
        final double y1 = _xyz._d2;
        final double z1 = _xyz._d3;

        final double x2 = other._xyz._d1;
        final double y2 = other._xyz._d2;
        final double z2 = other._xyz._d3;

        return ((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1)) + ((z2 - z1) * (z2 - z1));
    }

    /**
     * @param p
     * @return euclidean distance between 2  3D points using the Pythagorean theorem
     */
    public double distance(Point p) {
        return Math.sqrt(distanceSquared(p));
    }

    public double getX() {
        return _xyz._d1;
    }
    public double getY() {
        return _xyz._d2;
    }
    public double getZ() {
        return _xyz._d3;
    }
}
