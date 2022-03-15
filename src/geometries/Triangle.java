package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

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

        List<Point> l =  this._vertices;
        Vector v1= l.get(0).subtract(ray.getP0());
        Vector v2= l.get(1).subtract(ray.getP0());
        Vector v3= l.get(2).subtract(ray.getP0());
        Vector n1= (v1.crossProduct(v2)).normalize();
        Vector n2= (v2.crossProduct(v3)).normalize();
        Vector n3= (v3.crossProduct(v1)).normalize();

        double num1= n1.dotProduct(ray.getDir());

        double num2= n2.dotProduct(ray.getDir());
        double num3= n3.dotProduct(ray.getDir());

       if( (num1>0&&num2>0&&num3>0)||(num1<0&&num2<0&&num3<0)) {
           Plane pl = new Plane(l.get(0), l.get(1), l.get(2));
           return pl.findIntersections(ray);
       }
       return null;
    }

    @Override
    public String toString() {
        return "Triangle{ " + super.toString() + "}" ;
    }


}
