package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.*;

/**
 * Tube class represents 3D tube in 3D Cartesian coordinate system
 * inherits Geometry class
 * @author Gal&Ariel
 */
public class Tube extends Geometry {
    /**
     * Ray in the center of the tube
     */
    final protected Ray _axisRay;
    /**
     * Tube's radius
     */
    final protected double _radius;

    /**
     * Tube constructor based on axis ray and radius
     * @param axisRay Ray in the center of the tube
     * @param radius Tube's radius
     */
    public Tube(Ray axisRay, double radius) {
        _axisRay = axisRay;
        if (radius <= 0)
            throw new IllegalArgumentException("Radius cannot be lower than 0");
        _radius = radius;
    }

    /**
     * Get the normal vector on tube based-on point
     * @param point {@link Point} where the requested normal
     * @return normal vector {@link Vector}
     */
    @Override
    public Vector getNormal(Point point) {
        Point P0 = _axisRay.getP0();
        Vector v = _axisRay.getDir();
        Vector P0_P = point.substract(P0);
        double t = alignZero(v.dotProduct(P0_P));

        //if the point given is located on the tube bottom
        if (isZero(t)) {
            return P0_P.normalize();
        }

        Point o = P0.add(v.scale(t));
        //if the point given is located on the tube axis
        if (point.equals(o)) {
            throw new IllegalArgumentException("point cannot be on the tube axis");
        }
        Vector n = point.substract(o).normalize();
        return n;
    }

    /** textual description of tube
     * @return textual description of tube
     */
    @Override
    public String toString() {
        return "Tube{" +
                "_axisRay=" + _axisRay +
                ", _radius=" + _radius +
                '}';
    }

    /**
     *  Get ray in the center of the tube
     * @return {@link Ray} Ray in the center of the tube
     */
    public Ray getAxisRay() {
        return _axisRay;
    }

    /**
     * Get tube's radius
     * @return tube's radius
     */
    public double getRadius() {
        return _radius;
    }

    /**
     * @param ray {@link Ray} pointing toward the objects
     * @return List of intersections {@link Point}
     */

    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray ) {
        /*
        The equation for a tube of radius r oriented along a line pa + vat:
        (q - pa - (va,q - pa)va)2 - r2 = 0
        get intersections using formula : (p - pa + vt - (va,p - pa + vt)va)^2 - r^2 = 0
        reduces to at^2 + bt + c = 0
        with a = (v - (v,va)va)^2
             b = 2 * (v - (v,va)va,∆p - (∆p,va)va)
             c = (∆p - (∆p,va)va)^2 - r^2
        where  ∆p = p - pa
        */

        Vector v = ray.getDir();
        Vector va = this.getAxisRay().getDir();

        //if vectors are parallel then there is no intersections possible
        if (v.normalize().equals(va.normalize()))
            return null;

        //use of calculated variables to avoid vector ZERO
        double vva;
        double pva;
        double a;
        double b = 0;
        double c = 0;

        //check every variables to avoid ZERO vector
        if (ray.getP0().equals(_axisRay.getP0())){
            vva = v.dotProduct(va);
            if (vva == 0){
                a = v.dotProduct(v);
            }
            else{
                a = (v.substract(va.scale(vva))).dotProduct(v.substract(va.scale(vva)));
            }
            b = 0;
            c = - _radius * _radius;
        }
        else{
            Vector deltaP = ray.getP0().substract(_axisRay.getP0());
            vva = v.dotProduct(va);
            pva = deltaP.dotProduct(va);

            if (vva == 0 && pva == 0){
                a = v.dotProduct(v);
                b = 2 * v.dotProduct(deltaP);
                c = deltaP.dotProduct(deltaP) - _radius * _radius;
            }
            else if (vva == 0){
                a = v.dotProduct(v);
                Vector scale;
                try {
                    scale = va.scale(deltaP.dotProduct(va));
                    if (deltaP.equals(scale)){
                        b = 0;
                        c = - _radius * _radius;
                    }
                    else{
                        b = 2 * v.dotProduct(deltaP.substract(scale));
                        c = (deltaP.substract(scale).dotProduct(deltaP.substract(scale))) - _radius * _radius;
                    }
                } catch (Exception e) {
                    b = 2 * v.dotProduct(deltaP);
                    c = (deltaP).dotProduct(deltaP) - this._radius * this._radius;
                }
            }
            else if (pva == 0){
                a = (v.substract(va.scale(vva))).dotProduct(v.substract(va.scale(vva)));
                b = 2 * v.substract(va.scale(vva)).dotProduct(deltaP);
                c = (deltaP.dotProduct(deltaP)) - this._radius * this._radius;
            }
            else {
                Vector vSubstractScaleVa;
                Vector scale;
                try {
                    vSubstractScaleVa = v.substract(va.scale(vva));

                }
                catch (Exception e){
                    vSubstractScaleVa = v;
                }
                a = vSubstractScaleVa.dotProduct(vSubstractScaleVa);
                try {
                    scale = va.scale(deltaP.dotProduct(va));
                    if (deltaP.equals(scale)){
                        b = 0;
                        c = - _radius * _radius;
                    }
                    else{
                        b = 2 * vSubstractScaleVa.dotProduct(deltaP.substract(scale));
                        c = (deltaP.substract(scale).dotProduct(deltaP.substract(scale))) - this._radius * this._radius;
                    }
                } catch (Exception e) {
                    b = 2 * vSubstractScaleVa.dotProduct(deltaP);
                    c = (deltaP.dotProduct(deltaP)) - this._radius * this._radius;
                }

            }
        }

        //calculate delta for result of equation
        double delta = b * b - 4 * a * c;

        if (delta <= 0){
            return null; // no intersections
        }
        else{
            //calculate points taking only those with t > 0
            double t1 = alignZero((- b - Math.sqrt(delta)) / (2 * a));
            double t2 = alignZero((- b + Math.sqrt(delta)) / (2 * a));
            if (t1 > 0 && t2 > 0){
                Point p1 = new Point(ray.getPoint(t1).get_xyz());
                double distance1 = ray.getP0().distance(p1);
                Point p2 = new Point(ray.getPoint(t2).get_xyz());
                double distance2 = ray.getP0().distance(p2);
               /* if (distance1 <= maxDistance && distance2 <= maxDistance){*/
                    return List.of(new GeoPoint(this, p1), new GeoPoint(this, p2));
              /*  }
                else if (distance1 <= maxDistance){
                    return List.of(new GeoPoint(this, p1));
                }
                else if (distance2 <= maxDistance){
                    return List.of(new GeoPoint(this, p2));
                }
                else{
                    return null;
                }*/
            }
            else if (t1 > 0){
                Point p1 = new Point(ray.getPoint(t1).get_xyz());
                double distance1 = ray.getP0().distance(p1);
               // if (distance1 <= maxDistance)
                    return List.of(new GeoPoint(this, p1));

            }
            else if (t2 > 0){
                Point p2 = new Point(ray.getPoint(t2).get_xyz());
                double distance2 = ray.getP0().distance(p2);
                //if (distance2 <= maxDistance)
                    return List.of(new GeoPoint(this, p2));

            }
        }
        return null;
    }
}
