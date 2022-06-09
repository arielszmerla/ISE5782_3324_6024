package primitives;

public class Material {

    // A field of the class.
    public Double3 _kD=new Double3(0d);
    // A field of the class.
    public Double3 _kS=new Double3(0d);
    public Double3 _kT=new Double3(0d);
    public Double3 _kR=new Double3(0d);
    /**
     *glossiness factor
     */
    public Double3 _kG= Double3.ONE;

    /**
     *shininess factor
     */
    public int _nShininess=0;

    /**
     * Set the diffuse coefficient to the given value.
     *
     * @param kD The diffuse coefficient.
     * @return The material itself.
     */
    public Material setKd(double kD) {
        _kD = new Double3(kD);
        return this;
    }


    /**
     * Set the specular reflectance of the material to the given value.
     *
     * @param kS specular coefficient
     * @return The material itself.
     */
    public Material setKs(double kS) {
        _kS = new Double3(kS);
        return this;
    }
    /**
     * Set the material's kG value to the square root of the given value.
     *
     * @param kG The absorption coefficient of the material.
     * @return The material object itself.
     */
    public Material setKg(double kG) {
        _kG = new Double3(kG);
        return this;
    }


    /**
     * Set the shininess of the material.
     *
     * @param nShininess The shininess of the material.
     * @return The material object itself.
     */
    public Material setnShininess(int nShininess) {
        _nShininess = nShininess;
        return this;
    }

    public Double3 getkD() {
        return _kD;
    }

    public Double3 getkS() {
        return _kS;
    }

    /**
     * > This function returns the value of the private variable _nShininess
     *
     * @return The value of the private variable _nShininess.
    */
    public int getnShininess() {
        return _nShininess;
    }
    /**
     * Set the red component of the reflection coefficient to the given value.
     *
     * @param kR The amount of light reflected by the material.
     * @return The material itself.
     */
    public Material setKr(double kR) {
        _kR = new Double3(kR);
        return this;
    }
    /**
     * "Set the thermal conductivity of the material to the given value."
     *
     * The first line of the function is a comment. Comments are ignored by the compiler. They are used to explain what the
     * code does
     *
     * @param kT The transmission coefficient.
     * @return The material itself.
     */
    public Material setKt(double kT) {
        _kT = new Double3(kT);
        return this;
    }
}
