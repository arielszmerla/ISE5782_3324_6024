package primitives;

/**
 * Vector class represents vector in 3D Cartesian coordinate system
 *
 * @author Gal&Ariel
 */
public class Vector extends Point {
    /**
     * Vector constructor based on Double3D parameter
     *
     * @param xyz
     */
    public Vector(Double3 xyz) {
        super(xyz);
    }

    /**
     * Vector constructor based on three doubles
     *
     * @param x
     * @param y
     * @param z
     */
    public Vector(double x, double y, double z) {
        super(x, y, z);
        if (_xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector(0,0,0) is not valid");
    }

    @Override
    public String toString() {
        return "Vector " + _xyz;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * dot product between 2 vectors
     *
     * @param v
     * @return
     */
    public double dotProduct(Vector v) {
        double u1 = _xyz._d1;
        double u2 = _xyz._d2;
        double u3 = _xyz._d3;

        double v1 = v._xyz._d1;
        double v2 = v._xyz._d2;
        double v3 = v._xyz._d3;

        return (u1 * v1 + u2 * v2 + u3 * v3);

    }

    /**
     * cross product between 2 vectors
     *
     * @param v
     * @return
     */
    public Vector crossProduct(Vector v) {
        double u1 = _xyz._d1;
        double u2 = _xyz._d2;
        double u3 = _xyz._d3;

        double v1 = v._xyz._d1;
        double v2 = v._xyz._d2;
        double v3 = v._xyz._d3;
        return new Vector((u2 * v3 - v2 * u3), -(u1 * v3 - v1 * u3), (u1 * v2 - v1 * u2));
    }

    /**
     * calculate the vector's length squared
     *
     * @return vector's length squared
     */
    public double lengthSquared() {
        double u1 = _xyz._d1;
        double u2 = _xyz._d2;
        double u3 = _xyz._d3;
        return u1 * u1 + u2 * u2 + u3 * u3;
    }

    /**
     * calculate the vector's length
     *
     * @return vector's length
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * Change vector to normalized vector
     *
     * @return normalized vector
     */
    public Vector normalize() {
        double size = length();
        return new Vector(_xyz.reduce(size));
    }
}
