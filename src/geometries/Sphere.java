package geometries;

import primitives.Point;
import primitives.Vector;

public class Sphere implements Geometry{

    Point _center;
    double _radius;

    public Sphere(Point center, double radius) {
        _center = center;
        _radius = radius;
    }

    @Override
    public Vector getNormal(Point point) {
        return null;
    }

    public Point getCenter() {
        return _center;
    }

    public double getRadius() {
        return _radius;
    }

    @Override
    public String toString() {
        return "_center=" + _center +
                ", _radius=" + _radius  ;
    }
}
