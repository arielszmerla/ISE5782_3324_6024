package geometries;

import primitives.*;

/**
 * This interface will serve all geometric class
 *
 * @author Gal Gabay & Ariel Szmerla
 */
public interface Geometry extends Intersectable {
    Vector getNormal(Point point);
}
