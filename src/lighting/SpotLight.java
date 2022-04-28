package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

import static java.lang.Math.pow;
import static primitives.Util.isZero;

public class SpotLight extends PointLight{
    private Vector _direction;
    public double _shorten=1;
    public SpotLight(Color intensity, Point position, Vector direction,double shorten) throws IllegalArgumentException {
        super(intensity, position);
        _direction=direction.normalize();
        if(shorten>1 || shorten<0)
            throw new IllegalArgumentException("Factor has to be between 0-1");
        _shorten=shorten;
    }
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        _direction=direction.normalize();
    }
    @Override
    public Color getIntensity(Point p) {
        double projection = _direction.dotProduct(getL(p));

        if (isZero(projection)) {
            return Color.BLACK;
        }

        double factor =(Math.max(0,projection)); //Math.pow(_shorten,Math.max(0, projection));
        Color pointLightIntensity = super.getIntensity(p);
        factor=pow(factor,1/_shorten);

        return (pointLightIntensity.scale(factor));
    }

}
