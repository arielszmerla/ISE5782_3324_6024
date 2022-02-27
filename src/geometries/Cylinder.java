package geometries;

import primitives.Point;
import primitives.Vector;

public class Cylinder extends Sphere {
double _height;

    public Cylinder(Point center, double radius, double height) {
        super(center, radius);
        _height = height;
    }

    public double get_height() {
        return _height;
    }

    @Override
    public String toString() {
        return "Cylinder{" +
                "_height=" + _height +
                super.toString() +
                '}';
    }

    public Vector getNormal(Point point) {
        return null;
    }
}

