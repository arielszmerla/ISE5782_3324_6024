package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;
import geometries.Intersectable.GeoPoint;


import java.util.List;

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
            return calcColor(myPoint);
        }
        return _scene._background;
    }
    private Color calcColor(GeoPoint geoPoint) {
        return  geoPoint._geometry.getEmission();
    }
    }