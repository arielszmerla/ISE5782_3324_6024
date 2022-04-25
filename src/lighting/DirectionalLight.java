package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class DirectionalLight extends Light implements LightSource {
    private Vector _direction;
    public DirectionalLight(Color intensity,Vector direction) {
        super(intensity);
        _direction=direction.normalize();
    }

    public DirectionalLight setDirection(Vector direction) {
        _direction = direction;
        return this;
    }

    @Override
    public Color getIntensity(Point p) {
        return super.getIntensity();
    }

    @Override
    public Vector getL(Point p) {
        return _direction.normalize();
    }
}
