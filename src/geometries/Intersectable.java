package geometries;

import primitives.*;
import java.util.List;

/**
 * interface for finding intersestions points
 */
public interface Intersectable {
    /**
     *
     * @param ray {@link Ray} pointing toward the objects
     * @return List of intersections {@link Point}
     */
    List<Point> findIntersections(Ray ray);
}
