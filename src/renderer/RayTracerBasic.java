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
            return calcColor(myPoint,ray);
        }
        return _scene._background;
    }
    private Color calcColor(GeoPoint geoPoint, Ray ray) {
        return geoPoint._geometry.getEmission().add(calcLocalEffects(geoPoint,ray));
    }
    private Color calcLocalEffects(GeoPoint intersection, Ray ray) {
        Vector v = ray.getDir();
        Vector n = intersection._geometry.getNormal(intersection._point);
        double nv= alignZero(n.dotProduct(v));
        if (nv== 0) return Color.BLACK;
        int nShininess= intersection._geometry.getMaterial().getnShininess();
        Double3 kd= intersection._geometry.getMaterial().getkD(), ks= intersection._geometry.getMaterial().getkS();
        Color color= Color.BLACK;
        for (LightSource lightSource: _scene.lights) {
            Vector l = lightSource.getL(intersection._point);
            double nl= alignZero(n.dotProduct(l));
            if (nl * nv> 0) { // sign(nl) == sing(nv)
                Color lightIntensity= lightSource.getIntensity(intersection._point);
                color = color.add(calcDiffusive(kd, l, n, lightIntensity),
                        calcSpecular(ks, l, n, v, nShininess, lightIntensity));
            }
        }
        return color;
    }

    private Color calcSpecular(Double3 ks, Vector l, Vector n, Vector v, int nShininess, Color lightIntensity) {
        return null;
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
}