package lighting;

import primitives.Color;
import primitives.Double3;
import primitives.Point;
import primitives.Vector;

/**
 * Directional light class
 */
public class DirectionalLight extends Light implements LightSource {
    private Vector _direction;
    public DirectionalLight(Color intensity,Vector direction) {
        super(intensity);
        _direction=direction.normalize();
    }

    /**
     * builder direction setter
     * @param direction {@link Vector}
     * @return this
     */
    public DirectionalLight setDirection(Vector direction) {
        _direction = direction.normalize();
        return this;
    }

    /**
     * get a point on the object and return the intensity on it
     * @param point {@link Point}
     * @return {@link Color}
     */
    @Override
    public Color getIntensity(Point point) {
        return super.getIntensity();
    }
    /**
     * get a point on the object and return the direction of the light on it
     * @param point {@link Point}
     * @return direction of the light {@link Vector}
     */
    @Override
    public Vector getL(Point point) {
        return _direction;
    }
    /**
     * get a point on the object and return the direction of the light on it
     * @param point {@link Point}
     * @return default value as its not impacted by distance
     */
    @Override
    public double getDistance(Point point) {
        return Double.POSITIVE_INFINITY;
    }
}
