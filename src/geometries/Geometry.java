package geometries;

import primitives.*;

/**
 * This abstract class will serve all geometric class
 * inerits Intersectable class
 * @author Gal Gabay & Ariel Szmerla
 */
public abstract class Geometry extends Intersectable {
    /**
     * emission color with default value
     */
    protected Color _emission=Color.BLACK;
    /**
     * geometry material type
     */
    private Material _material=new Material();

    /**
     * Get the normal vector on tube based-on point
     * @param point {@link Point} where the requested normal
     * @return normal vector {@link Vector}
     */
    public abstract Vector getNormal(Point point);

    /**
     * get the emission color
     * @return emission {@link Color}
     */
    public Color getEmission() {
        return _emission;
    }

    /**
     * set geometry's emission
     * @param emission {@link Color}
     * @return updated geometry {@link Geometry}
     */
    public Geometry setEmission(Color emission) {
        _emission = emission;
        return this;
    }

    /**
     * get the geometry material
     * @return material {@link Material}
     */
    public Material getMaterial() {
        return _material;
    }

    /**
     * set geometry material
     * @param material {@link Material}
     * @return updated geometry {@link Geometry}
     */
    public Geometry setMaterial(Material material) {
        _material = material;
        return this;
    }
}
