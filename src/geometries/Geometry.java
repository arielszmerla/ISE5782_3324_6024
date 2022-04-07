package geometries;

import primitives.*;

/**
 * This interface will serve all geometric class
 *
 * @author Gal Gabay & Ariel Szmerla
 */
public abstract class Geometry extends Intersectable {
    protected Color _emission=Color.BLACK;
    public abstract Vector getNormal(Point point);
    private Material _material=new Material();

    public Color getEmission() {
        return _emission;
    }

    public Geometry setEmission(Color emission) {
        _emission = emission;
        return this;
    }

    public Material getMaterial() {
        return _material;
    }

    public Geometry setMaterial(Material material) {
        _material = material;
        return this;
    }
}
