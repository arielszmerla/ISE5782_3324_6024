package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Pointlight class
 */
public class PointLight extends Light implements LightSource{
    /**
     * position of the start point
     */
   private Point _position;

  // A constant that is used to calculate the intensity of the light.
    private double _kC=1d;
   // A constant that is used to calculate the intensity descending due to distance of the light.
   private double _kL=0d;
    // A constant that is used to calculate the intensity descending due to cubic loose of strength of the light.

    private double _kQ=0d;

  // A constant that is used to calculate the intensity of the light.
     /**
     * ctor with values
     * @param c {@link Color} of the light
     * @param pos {@link Point} start position
     */

    public PointLight(Color c, Point pos) {
        super(c);
        _position = pos;
    }

    public PointLight setKc(double kC) {
        _kC = kC;
        return this;
    }

    public PointLight setKl(double kL) {
        _kL = kL;
        return this;
    }

    public PointLight setKq(double kQ) {
        _kQ = kQ;
        return this;
    }

    @Override
    // Returning the color of the point p.
    public Color getIntensity(Point p) {
        double factor = _kC;
        double distance;
        try {
            distance = _position.distance(p);
            factor += _kL * distance + _kQ * distance * distance;
        } catch (Exception exception) {
        }

        return getIntensity().scale(1 / factor);
    }

    @Override
    // Calculating the vector from the light source to the point.
    public Vector getL(Point p) {
        try {
            return p.subtract(_position).normalize();
        } catch (Exception exception) {
            return null;
        }
    }

    @Override
    public double getDistance(Point point) {
        return point.distance(_position);
    }
}
