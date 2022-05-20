package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.*;

import static primitives.Util.isZero;

/**
 * Cylinder class represents 3D cylinder in 3D Cartesian coordinate system
 * inherits Tube
 * @author Gal&Ariel
 */
public class Cylinder extends Tube {
    /**
     * height of cylinder
     */
    final private double _height;

    /**
     * {@link Cylinder} Cylinder constructor based on tube parameters (axis ray and radius) and height
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

    /** textual description of cylinder
     * @return textual description of cylinder
     */
    @Override
    public String toString() {
        return "Cylinder{" +
                "_height=" + _height +
                super.toString() +
                '}';
    }
    /**
     * Get the normal vector on Cylinder based-on point
     * @param p1 {@link Point} where the requested normal
     * @return normal vector {@link Vector}
     */
    @Override
    public Vector getNormal(Point p1) {
        // we assume point is on cylinder, so we check if it is on top bottom or side.
        Point centerBottom = _axisRay.getP0();
        Point centerTop = _axisRay.getP0().add(_axisRay.getDir().scale(_height));
        if(p1.equals(centerTop) || isZero(p1.subtract(centerTop).dotProduct(_axisRay.getDir()))) { // top
            return _axisRay.getDir();
        } else if (p1.equals(centerBottom) || isZero(p1.subtract(centerBottom).dotProduct(_axisRay.getDir()))) { // bottom
            return _axisRay.getDir().scale(-1);
        } else { // side
            return super.getNormal(p1);
        }
    }
    /**
     * Get the list of Cylinder ray intersections
     * @param ray {@link Ray} the ray entering
     * @return list of {@link GeoPoint}
     */
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {

        List<GeoPoint> result = new LinkedList<>();
        Vector va = _axisRay.getDir();
        Point p1 = _axisRay.getP0();
        Point p2 = p1.add(_axisRay.getDir().scale(_height));

        //1) get the intersections with bottom plane
        //2) get the intersections with tube that includes the cylinder
        //3) get the intersections with top plane
        Plane plane1 = new Plane(p1,_axisRay.getDir()); //get plane of bottom base
        List<GeoPoint> result2 = plane1.findGeoIntersectionsHelper(ray); //intersections with bottom's plane
        if (result2 != null){
            //Add all intersections of bottom's plane that are in the base's bounders
            for (GeoPoint point : result2) {
                if (point._point.equals(p1)){ //to avoid vector ZERO
                    //if (point._point.distance(ray.getP0()) <= maxDistance){
                        result.add(new GeoPoint(this, point._point));

                }
                //Formula that checks that point is inside the base
                else if ((point._point.subtract(p1).dotProduct(point._point.subtract(p1)) < _radius * _radius)){
                  //  if (point._point.distance(ray.getP0()) <= maxDistance){
                        result.add(new GeoPoint(this, point._point));

                }
            }
        }

        List<GeoPoint> result1 = super.findGeoIntersectionsHelper(ray); //get intersections for tube

        if (result1 != null){
            //Add all intersections of tube that are in the cylinder's bounders
            for (GeoPoint point:result1) {
                if (va.dotProduct(point._point.subtract(p1)) > 0 && va.dotProduct(point._point.subtract(p2)) < 0){
                  //  if (point._point.distance(ray.getP0()) <= maxDistance){
                        result.add(new GeoPoint(this, point._point));

                }
            }
        }


        Plane plane2 = new Plane(p2, _axisRay.getDir()); //get plane of top base
        List<GeoPoint> result3 = plane2.findGeoIntersectionsHelper(ray); //intersections with top's plane

        if (result3 != null){
            for (GeoPoint point : result3) {
                if (point._point.equals(p2)){ //to avoid vector ZERO
                  //  if (point._point.distance(ray.getP0()) <= maxDistance){
                        result.add(new GeoPoint(this, point._point));

                }
                //Formula that checks that point is inside the base
                else if ((point._point.subtract(p2).dotProduct(point._point.subtract(p2)) < _radius * _radius)){
                    //if (point._point.distance(ray.getP0()) <= maxDistance){
                        result.add(new GeoPoint(this, point._point));

                }
            }
        }
        //if any result return it
        if (result.size() > 0)
            return result;

        return null;
    }
}

