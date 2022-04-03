package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

public class RayTracerBasic extends RayTracer {
    public RayTracerBasic(Scene scene) {
        super(scene);
    }
    /**
     * Get color of the intersection of the ray with the scene
     *
     * @param ray Ray to trace
     * @param isSoftShadows is rendering with soft shadows improvement
     * @return Color of intersection
     */
    @Override
    public Color traceRay(Ray ray) {

        List<Point> myPoints = _scene.getGeometries().findIntersections(ray);
        if (myPoints != null) {
            Point myPoint = ray.findClosestPoint(myPoints);
            return calcColor(myPoint, ray);
        }
        return _scene.getBackground();
    }

    private Color calcColor(Point myPoint, Ray ray) {
        return _scene.getBackground();
    }
}
