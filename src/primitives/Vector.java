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
    /**
     * Change vector to scalized vector
     *
     * @return scalized vector
     */
    public Vector scale(double t) {
        return new Vector (_xyz.scale(t));
    }
    /**
     * Change vector to scalized vector
     *
     * @return scalized vector
     */
    public Vector scale(Double3 t) {
        return new Vector (_xyz._d1*t._d1, _xyz._d2*t._d2,_xyz._d3*t._d3);
    }
    /***
     * Rotate the vector by angle (in degrees) and axis of rotation
     * @param axis Axis of rotation
     * @param theta Angle of rotation (degrees)
     */
    public Vector rotateVector(Vector axis, double theta) {

        //Use of Rodrigues' rotation formula
        //https://en.wikipedia.org/wiki/Rodrigues%27_rotation_formula
        //v: vector to rotate
        //k: axis of rotation
        //θ: angle of rotation
        //Vrot = v * Cos θ + (k * v) * Sin θ + k(k,v) * (1 - Cos θ)

        //Variables used in computing
        double x, y, z;
        double u, v, w;
        x = this.get_xyz()._d1;
        y = this.get_xyz()._d2;
        z = this._xyz._d3;
        u = axis._xyz._d1;
        v = axis._xyz._d2;
        w = axis._xyz._d3;
        double v1 = u * x + v * y + w * z;

        //Convert degrees to Rad
        double thetaRad = Math.toRadians(theta);

        //Calculate X's new coordinates
        double xPrime = u * v1 * (1d - Math.cos(thetaRad))
                + x * Math.cos(thetaRad)
                + (-w * y + v * z) * Math.sin(thetaRad);

        //Calculate Y's new coordinates
        double yPrime = v * v1 * (1d - Math.cos(thetaRad))
                + y * Math.cos(thetaRad)
                + (w * x - u * z) * Math.sin(thetaRad);

        //Calculate Z's new coordinates
        double zPrime = w * v1 * (1d - Math.cos(thetaRad))
                + z * Math.cos(thetaRad)
                + (-v * x + u * y) * Math.sin(thetaRad);

        return new Vector( xPrime, yPrime, zPrime);
    }
}
