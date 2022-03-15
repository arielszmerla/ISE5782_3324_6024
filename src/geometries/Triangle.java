package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

/**
 * Sphere class represents 2D triangle (flat-shape) in 3D Cartesian coordinate system
 * inheritance from polygon
 * @author Gal&Ariel
 */
public class Triangle extends Polygon{
    /**
     * Triangle constructor based on three points
     * @param p0
     * @param p1
     * @param p2
     */
    public Triangle(Point p0, Point p1, Point p2) {
        super(p0, p1, p2);
    }

    /**
     * @param ray {@link Ray} pointing toward the objects
     * @return List of intersections {@link Point}
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }

    @Override
    public String toString() {
        return "Triangle{ " + super.toString() + "}" ;
    }


}
