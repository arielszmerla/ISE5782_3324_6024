package renderer;

import lighting.LightSource;
import primitives.*;
import scene.Scene;
import geometries.Intersectable.GeoPoint;


import java.util.List;

import static primitives.Util.*;

public class RayTracerBasic extends RayTracer {
    public RayTracerBasic(Scene scene) {
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
        if (myPoints != null) {
            GeoPoint myPoint = ray.getClosestGeoPoint(myPoints);
            return calcColor(myPoint,ray );
        }
        return _scene._background;
    }
    private Color calcColor(GeoPoint geoPoint, Ray ray) {
        return geoPoint._geometry.getEmission();
    }
//    private Color calcLocalEffects(GeoPoint intersection, Ray ray) {
//        Vector v = ray.getDir(); Vector n = intersection._geometry.getNormal();
//        double nv= alignZero(n.dotProduct(v);
//        if (nv== 0) return Color.BLACK;
//        int nShininess= intersection._geometry.getnShininess();
//        double kd= intersection._geometry.getKd(), ks= intersection._geometry.getKs();
//        Color color= Color.BLACK;
//        for (LightSource lightSource: scene.lights) {
//            Vector l = lightSource.getL(intersection._point);
//            double nl= alignZero(n.dotProduct(l);
//            if (nl* nv> 0) { // sign(nl) == sing(nv)
//                Color lightIntensity= lightSource.getIntensity(intersection._point);
//                color = color.add(calcDiffusive(kd, l, n, lightIntensity),
//                        calcSpecular(ks, l, n, v, nShininess, lightIntensity));
//            }
//        }
//        return color;
//    }
}