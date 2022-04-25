package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static primitives.Util.isZero;

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
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        List<Point> l =  _vertices;
        Vector v1= l.get(0).substract(ray.getP0());
        Vector v2= l.get(1).substract(ray.getP0());
        Vector v3= l.get(2).substract(ray.getP0());
        Vector n1= (v1.crossProduct(v2)).normalize();
        Vector n2= (v2.crossProduct(v3)).normalize();
        Vector n3= (v3.crossProduct(v1)).normalize();

        double num1= n1.dotProduct(ray.getDir());

        double num2= n2.dotProduct(ray.getDir());
        double num3= n3.dotProduct(ray.getDir());

        if((num1>0&&num2>0&&num3>0)||(num1<0&&num2<0&&num3<0)) {
            Plane pl = new Plane(l.get(0), l.get(1), l.get(2));
            List<GeoPoint> geoPoints =new LinkedList<>();

            for ( Point pt : pl.findIntersections(ray) )
            {
                geoPoints.add( new GeoPoint(this,pt));

            }
            return  geoPoints;
        }
        return null;

    }


    @Override
    public String toString() {
        return "Triangle{ " + super.toString() + "}" ;
    }


}
