package primitives;

/**
 * Sphere class represents 3D ray in 3D Cartesian coordinate system
 * @author Gal&Ariel
 */
public class Ray {
    /**
     * Ray's start point
     */
    final private Point _p0;
    /**
     * Ray's direction vector
     */
    final private Vector _dir;

    /**
     * Ray constructor based on start point p0 and direction vector
     * @param p0 start point
     * @param dir direction vector
     */
    public Ray(Point p0, Vector dir) {
        _p0 = p0;
        _dir = dir.normalize();
    }
    /**
     * get the start point of ray
     * @return Rays's start point
     */
    public Point getP0() {
        return _p0;
    }
    /**
     * get the direction vector of ray
     * @return Ray's direction vector
     */
    public Vector getDir() {
        return _dir;
    }

    @Override
    public String toString() {
        return "_p0=" + _p0.toString() +
                ", _dir=" + _dir.toString();
    }

    /**
     * check equality between two rays
     * @param o
     * @return if ray o compare to this ray
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ray ray = (Ray) o;
        return _p0.equals(ray._p0) && _dir.equals( ray._dir);
    }

}
