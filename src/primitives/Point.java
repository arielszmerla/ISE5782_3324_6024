package primitives;

import java.util.Objects;
/**
 * Point class represents point in 3D Cartesian coordinate system
 * @author Gal&Ariel
 */
public class Point {
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
}
