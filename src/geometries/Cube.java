package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.isZero;

/**
 * It's a list of polygons that are connected to each other
 */
public class Cube extends Geometry {
    // It's a list of polygons that are connected to each other
    List<Polygon> _cube;

    public Cube( double length,Point ... points) {
        if (points.length != 4)
            throw new IllegalArgumentException("please enter four points");
        try {
            Polygon myTry = new Polygon(points);
        } catch (Exception e) {
            throw e;
        }
        Point p0=points[0];
        Point p1=points[1];
        Point p2=points[2];
        Point p3=points[3];
        Polygon face= new Polygon(p0,p1,p2,p3);
        double distance = p0.distance(p1); double diagonal=p1.distance(p3);
        //use pythagoras for finding 90 ankle
        if(p0.distance(p1)==distance&&p1.distance(p2)==distance&&p2.distance(p3)==distance) {
              if(Math.sqrt(distance*distance*2)==Math.sqrt(diagonal*diagonal)) {
                  Point pA = p0.add(face.getNormal(p0).scale(length));
                  Point pB = p1.add(face.getNormal(p1).scale(length));
                  Point pC = p2.add(face.getNormal(p2).scale(length));
                  Point pD = p3.add(face.getNormal(p3).scale(length));
                    try {

                        Polygon faceBack = new Polygon(pA, pB, pC, pD);
                        Polygon faceTop = new Polygon(p0, pA, pB, p1);
                        Polygon faceLeft = new Polygon(p2, pC, pD, p3);
                        Polygon faceRight = new Polygon(p1, pB, pC, p2);
                        Polygon faceBottom = new Polygon(p3, pD, pA, p0);
                        _cube = List.of(face, faceBottom, faceBack, faceRight, faceLeft, faceTop);

                    }
                    catch (Exception e) {
                        throw new IllegalArgumentException("impossible to build cube1");
                    }
              }
              else
                  throw new IllegalArgumentException("impossible to build cube");
        }

    }
    public List<Polygon> getPolygons(){
        return _cube;

    }
    @Override
    public Vector getNormal(Point point) {

        for (Polygon polygon : _cube) {

                Plane myPlane= new Plane(polygon._vertices.get(0),polygon._vertices.get(1),polygon._vertices.get(2));
                if(isZero(new Vector(point.get_xyz()).dotProduct(myPlane.getNormal()))){
                    return myPlane.getNormal();
            }

        }
return null;
    }

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        List<GeoPoint> myList= new LinkedList<>();
        for(Polygon p : _cube ){
            if (p.findGeoIntersectionsHelper(ray)!=null)
                myList.addAll(p.findGeoIntersectionsHelper(ray));
        }
        return myList;
    }
}
