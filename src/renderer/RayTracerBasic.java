package renderer;

import lighting.LightSource;
import primitives.*;
import renderer.scene.Scene;
import geometries.Intersectable.GeoPoint;


import java.util.List;

import static primitives.Util.*;

public class RayTracerBasic extends RayTracer {
    private static final double DELTA = 0.1;
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final double INITIAL_K = 1.0;


    /**
     * scene setter
     *
     * @param scene {@link Scene}
     */
    protected RayTracerBasic(Scene scene) {
        super(scene);
    }

    /**
     * Get color of the intersection of the ray with the scene
     *
     * @param ray Ray to trace
     * @return Color of intersection
     */
    @Override
    public Color traceRay(Ray ray) {
        List<GeoPoint> myPoints = _scene._geometries.findGeoIntersections(ray);
           GeoPoint closestPoint = ray.getClosestGeoPoint(myPoints);
           if (closestPoint != null)
               return calcColor(closestPoint, ray);
            return _scene._background;

    }
    private Color calcColor(GeoPoint intersection, Ray ray,int level,Double3 k) {
        Color color = intersection._geometry.getEmission()
                .add(calcLocalEffects(intersection,ray,k));
        return 1 == level ? color : color.add(calcGlobalEffects(intersection, ray, level, k));

    }
    /**
     * Calculate color using recursive function
     *
     * @param geopoint the point of intersection
     * @param ray      the ray
     * @return the color
     */
    private Color calcColor(GeoPoint geopoint, Ray ray) {
        return calcColor(geopoint, ray, MAX_CALC_COLOR_LEVEL,new Double3(INITIAL_K))
                .add(_scene._ambientLight.getIntensity());
    }
    private Color calcGlobalEffects(GeoPoint intersection, Ray inRay, int level, Double3 k) {
        Color color = Color.BLACK;
        Double3 kr= intersection._geometry.getMaterial()._kR;Double3 kkr=kr.product(k);
        Vector n = intersection._geometry.getNormal(intersection._point);
        if(kkr.lowerThan(MIN_CALC_COLOR_K-DELTA)==false){
            Ray reflectedRay = constructReflectedRay(n, intersection._point, inRay);
            GeoPoint reflectedPoint = findClosestIntersection(reflectedRay);
            if (reflectedPoint != null)
                color= color.add(calcColor(reflectedPoint,reflectedRay,level-1,kkr).scale(kr));
        }
        Double3 kt=intersection._geometry.getMaterial()._kT;Double3 kkt=kt.product(k);
        if(kkt.lowerThan(MIN_CALC_COLOR_K-DELTA)==false){
            Ray refractedRay = constructRefractedRay(n, intersection._point, inRay);
            GeoPoint refractedPoint = findClosestIntersection(refractedRay);
            if (refractedPoint != null)
                color= color.add(calcColor(refractedPoint,refractedRay,level-1,kkt).scale(kt));
        }
        return color;
    }

    private Color calcLocalEffects(GeoPoint intersection, Ray ray,Double3 k) {
        Vector v = ray.getDir();
        Vector n = intersection._geometry.getNormal(intersection._point);
        double nv= alignZero(n.dotProduct(v));
        if (nv== 0) return Color.BLACK;
        int nShininess = intersection._geometry.getMaterial().getnShininess();
        Double3 kd = intersection._geometry.getMaterial().getkD();
        Double3 ks = intersection._geometry.getMaterial().getkS();
        Color color= Color.BLACK;
        for (LightSource lightSource: _scene.lights) {
            Vector l = lightSource.getL(intersection._point);
            double nl= alignZero(n.dotProduct(l));
            if (nl * nv> 0) { // sign(nl) == sing(nv)\
                if (unshaded(intersection, l, n, lightSource,nv)) {
                    Color lightIntensity = lightSource.getIntensity(intersection._point);
                    color = color.add(calcDiffusive(kd, l, n, lightIntensity),
                            calcSpecular(ks, l, n, v, nShininess, lightIntensity));
                }

            }
        }
        return color;
    }

    private Color calcSpecular(Double3 ks, Vector l, Vector n, Vector v, int nShininess, Color lightIntensity) {

        Vector r = new Vector(l.add(n.scale(n.dotProduct(l) * -2)).get_xyz());
        double vR;
        try {
            vR = v.scale(-1).dotProduct(r);
        } catch (Exception exception) {
            //if vR is 0 vector
            return lightIntensity.scale(1);
        }
        //color = ks * max(0, -v.r)^nSh @ppt 7 theoretical course
        return lightIntensity.scale(ks.scale( Math.pow(Math.max(0, vR), nShininess)));
    }

    /**
     * Calculate color of the diffusive effects of the light
     *
     * @param kd diffusive ratio
     * @param l light's direction vector
     * @param n normal vector
     * @param lightIntensity intensity of the light
     * @return color of the diffusive effect
     */
    private Color calcDiffusive(Double3 kd, Vector l, Vector n, Color lightIntensity) {
        double lN;
        try {
            lN = l.normalize().dotProduct(n.normalize());
        } catch (Exception exception) {
            return lightIntensity.scale(0);
        }
        //color = light * |l.n| * kd
        return lightIntensity.scale(kd.scale(Math.abs(lN)));
    }

    private boolean unshaded(GeoPoint gp, Vector l, Vector n, LightSource ls,double nv){
        Vector lightDir = l.scale(-1);// from point to light source
        Vector epsVector= n.scale(nv<0?DELTA:-DELTA);
        Point point = gp._point.add(epsVector);
        Ray lightRay = new Ray(point, n, lightDir);
        List<GeoPoint> intersections = _scene._geometries.findGeoIntersections(lightRay);
        if (intersections != null) {
            for (GeoPoint gp2 : intersections) {
                if (gp2._geometry.getMaterial()._kT.equals(new Double3(0.0))) {
                    return false;
                }
            }
        }
        return true;
    }
    private double transparency(GeoPoint gp, Vector l, Vector n, LightSource ls,double nv){
        Vector lightDir = l.scale(-1);// from point to light source
        Vector epsVector= n.scale(nv<0?DELTA:-DELTA);
        Point point = gp._point.add(epsVector);
        Ray lightRay = new Ray(point, n, lightDir);
        double lightDistance = ls.getDistance(gp._point);
        var intersections = _scene._geometries.findGeoIntersections(lightRay);
        if (intersections == null) return 1.0; //no intersection
        Double3 ktr =new Double3( 1.0);
        for (GeoPoint geoPoint : intersections) {
            if (alignZero(geoPoint._point.distance(gp._point) - lightDistance) <= 0) {
             // ktr=   ktr.scale(geoPoint._geometry.getMaterial()._kT);
                if (ktr.lowerThan( MIN_CALC_COLOR_K)) return 0.0;
            }
        }
        return 0;
    }
    /**
     * Construct the ray refracted by an intersection with the geometry
     * @param n normal to the geometry at intersection
     * @param point the intersection point
     * @param innerRay the ray entering
     * @return refracted ray (in our project there is no refraction incidence, so we return a new ray with the same characteristics)
     */
    private Ray constructRefractedRay(Vector n, Point point, Ray innerRay) {
        return new Ray(point, innerRay.getDir(), n);
    }

    /**
     * Construct the ray getting reflected on an intersection point
     * @param n normal to the point
     * @param point the intersection point
     * @param innerRay the ray entering at the intersection
     * @return the reflected ray
     */
    private Ray constructReflectedRay(Vector n, Point point, Ray innerRay) {
        //r = v - 2 * (v*n) * n
        //r is the reflected ray
        Vector v = innerRay.getDir();
        Vector r = null;
        try {
            r = v.substract(n.scale(v.dotProduct(n)).scale(2)).normalize();
        } catch (Exception e) {
            return null;
        }
        return new Ray(point, r, n);
    }

    /**
     * Find the closest intersection point between a ray base and the scene's geometries
     * @param ray the ray
     * @return the closest point
     */
    private GeoPoint findClosestIntersection(Ray ray) {
        List<GeoPoint> geoPoints = _scene._geometries.findGeoIntersections(ray);
        return ray.getClosestGeoPoint(geoPoints);
    }



}