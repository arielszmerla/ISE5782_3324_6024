package geometries;

import primitives.*;

/**
 * This interface will serve all geometric class
 *
 * @author Gal Gabay & Ariel Szmerla
 */
public abstract class Geometry extends Intersectable {
    public abstract Vector getNormal(Point point);
}
