package geometries;

import primitives.Point;
import primitives.Vector;

public class Plane implements Geometry{
    Point _q0;
    Vector _normal;
    public Plane(Point p1, Point p2, Point p3) {
        _q0 = p1;
       Vector v = p2.subtract(p1);
       Vector u = p3.subtract(p1);
       Vector n = v.crossProduct(u);
       _normal = n.normalize();
    }

    @Override
    public Vector getNormal() {
        return _normal;
    }
}
