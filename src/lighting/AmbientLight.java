package lighting;

import primitives.Color;
import primitives.Double3;

/**
 * Ambient light for all objects in 3D space
 *
 */
public class AmbientLight extends Light {
    /**
     * Intensity of ambient light
     * The ambient light's color is the color scaled by the ka factor
     *
     * @param color color at start
     * @param ka    attenuation factor
     */
    public AmbientLight(Color color, Double3 ka) {
        super(color.scale(ka));
    }

    /**
     * default constructor
     */
    public AmbientLight() {
        super(Color.BLACK);
    }
}
