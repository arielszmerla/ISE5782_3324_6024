package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

/**
 * Triangle class represents 2D triangle (flat-shape) in 3D Cartesian coordinate system
 * inheritance from polygon
 * @author Gal&Ariel
 */
public class Triangle extends Polygon{
    /**
     * Triangle constructor based on three points
     * @param p0 {@link Point #1}
     * @param p1 {@link Point #2}
     * @param p2 {@link Point #3}
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
        List<Point> l = _vertices;
        Vector v1= l.get(0).subtract(ray.getP0());
        Vector v2= l.get(1).subtract(ray.getP0());
        Vector v3= l.get(2).subtract(ray.getP0());
        Vector n1=(v1.crossProduct(v2)).normalize();//new Vector(0.001,0.001,0.001);
        Vector n2=(v2.crossProduct(v3)).normalize();//new Vector(0.001,0.001,0.001);
        Vector n3=(v3.crossProduct(v1)).normalize();//new Vector(0.001,0.001,0.001);
     /*  try {
            n1= (v1.crossProduct(v2)).normalize();
            n2= (v2.crossProduct(v3)).normalize();
            n3= (v3.crossProduct(v1)).normalize();
        }
        catch (IllegalArgumentException e){}*/
        double num1 = n1.dotProduct(ray.getDir());
        double num2 = n2.dotProduct(ray.getDir());
        double num3 = n3.dotProduct(ray.getDir());

    // if there is an intersection point inside the triangle
        if((num1>0&&num2>0&&num3>0)||(num1<0&&num2<0&&num3<0)) {
            Plane pl = new Plane(l.get(0), l.get(1), l.get(2));
            List<GeoPoint> geoPoints =new LinkedList<>();
            if(pl.findIntersections(ray) != null)
                for ( Point pt : pl.findIntersections(ray) ) {
                    geoPoints.add( new GeoPoint(this,pt));
            }
            return  geoPoints;
        }
        return null; // there isn't an intersection point inside the triangle


    }

    /** textual description of triangle
     * @return textual description of triangle
     */
    @Override
    public String toString() {
        return "Triangle{ " + super.toString() + "}" ;
    }


}
