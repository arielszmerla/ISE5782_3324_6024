package lighting;

import primitives.Color;
import primitives.*;

/**
 * interface will serve all light source type

 */
public interface LightSource {
    /**
     *
     * @param p {@link Point}
     * @return {@link Color} on it
     */
    public Color getIntensity(Point p);

    /**
     *
     * @param p {@link Point}
     * @return {@link Vector} light ray from it
     */
    public Vector getL(Point p);

    /**
     * get distance from light to point
     * @param point {@link Point}
     * @return distance( double )
     */
    double getDistance(Point point);
}
