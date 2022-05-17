package primitives;

public class Material {
   public Double3 _kD=new Double3(0d);
   public Double3 _kS=new Double3(0d);
   public Double3 _kT=new Double3(0d);
   public Double3 _kR=new Double3(0d);
   public Double3 _kG=new Double3(1d);
    public int nShininess=0;

    public Material setKd(double kD) {
        _kD = new Double3(kD);
        return this;
    }


    public Material setKs(double kS) {
        _kS = new Double3(kS);
        return this;
    }
    public Material setKg(double kG) {
        _kG = new Double3(Math.pow(kG, 0.5));
        return this;
    }

    public Material setnShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }

    public Double3 getkD() {
        return _kD;
    }

    public Double3 getkS() {
        return _kS;
    }

    public int getnShininess() {
        return nShininess;
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
