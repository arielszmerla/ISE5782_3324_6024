package primitives;

public class Material {
    // Initializing the variables.

    public Double3 _kD=new Double3(0d);
    public Double3 _kS=new Double3(0d);
    public Double3 _kT=new Double3(0d);
    public Double3 _kR=new Double3(0d);
    /**
     *glossiness factor
     */
    public Double3 _kG=new Double3(1);

    /**
     *shininess factor
     */
    public int _nShininess=0;

    public Material setKd(double kD) {
        _kD = new Double3(kD);
        return this;
    }


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

    // A getter function for the shininess factor.
    public int getnShininess() {
        return _nShininess;
    }

    public Material setKr(double kR) {
        _kR = new Double3(kR);
        return this;
    }
    public Material setKt(double kT) {
        _kT = new Double3(kT);
        return this;
    }
}
