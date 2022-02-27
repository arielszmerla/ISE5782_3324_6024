package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
/**
 * Cylinder class represents 3D cylinder in 3D Cartesian coordinate system
 * inherit Tube
 * @author Gal&Ariel
 */
public class Cylinder extends Tube {
    /**
     * height of
     */
    final private double _height;

    /**
     * Cylinder constructor based on tube parameters (axis ray and radius) and height
     */
    public Cylinder(Ray axisRay, double radius, double height) {
        super(axisRay, radius);
        if (height <= 0)
            throw new IllegalArgumentException("Height cannot be lower than 0");
        _height = height;
    }

    /**
     * get the height of cylinder
     * @return cylinder's height
     */
    public double getHeight() {
        return _height;
    }

    @Override
    public String toString() {
        return "Cylinder{" +
                "_height=" + _height +
                super.toString() +
                '}';
    }
    /**
     * Get the normal vector on Cylinder based-on point
     * @param point point where the normal
     * @return normal vector
     */
    public Vector getNormal(Point point) {
        return null;
    }
}

