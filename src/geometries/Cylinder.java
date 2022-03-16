package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.isZero;

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
     * @param p1 {@link Point} where the normal
     * @return normal vector {@link Vector}
     */
    @Override
    public Vector getNormal(Point p1) {
        // we assume point is on cylinder so we check if it is on top
        // bottom or side.
        Point centerBottom = _axisRay.getP0();
        Point centerTop = _axisRay.getP0().add(_axisRay.getDir().scale(_height));
        if(p1.equals(centerTop) || isZero(p1.subtract(centerTop).dotProduct(_axisRay.getDir()))) { // top
            return _axisRay.getDir();
        } else if (p1.equals(centerBottom) || isZero(p1.subtract(centerBottom).dotProduct(_axisRay.getDir()))) { // bottom
            return _axisRay.getDir().scale(-1);
        } else { // side
            return super.getNormal(p1);
        }
    }}

