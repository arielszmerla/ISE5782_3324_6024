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
     * @return Color of intersection
     */
    @Override
    public Color traceRay(Ray ray) {
        List<Point> intersections = _scene._geometries.findIntersections(ray);
        if (intersections != null) {
            Point closestPoint = ray.findClosestPoint(intersections);
            return calcColor(closestPoint);
        }
        //ray did not intersect any geometrical object
        return _scene._background;
    }

    private Color calcColor(Point point) {
        return _scene._ambientLight.getIntensity();
    }
}
