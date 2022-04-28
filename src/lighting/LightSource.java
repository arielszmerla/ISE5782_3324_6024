package lighting;

import primitives.Color;
import primitives.*;

/**
 * interface will serve all light source type
 *
 *
 */
public interface LightSource {
    public Color getIntensity(Point p);
    public Vector getL(Point p);
    double getDistance(Point point);
}
