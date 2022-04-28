package primitives;

public class Material {
   public Double3 _kD=new Double3(0d);
   public Double3 _kS=new Double3(0d);
   public int nShininess=0;

    public Material setKd(Double3 kD) {
        _kD = kD;
        return this;
    }


    public Material setKs(Double3 kS) {
        _kS = kS;
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
}
