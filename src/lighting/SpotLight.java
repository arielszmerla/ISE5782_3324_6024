package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class SpotLight extends PointLight{
    private Vector _direction;
    protected SpotLight(Color intensity, Point position, Vector direction,double kC,double kL,double kQ) {
        super(intensity, position,kC,kL,kQ);
        _direction=direction;
    }
}
