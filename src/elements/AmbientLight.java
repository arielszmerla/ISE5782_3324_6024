package elements;

import primitives.Color;
import primitives.Double3;

/**
 * Ambient light for all objects in 3D space
 *
 */
public class AmbientLight {


    private final Color _intensity; // intensity of ambient light

    /**
     * Ctor
     * @param Ia light illumination
     * @param Ka light factor
     */
    public  AmbientLight(Color Ia , Double3 Ka){
        _intensity = Ia.scale(Ka);
    }

    /**
     * dflt ctor
     */
    public AmbientLight(){
        _intensity=Color.BLACK;

    }

    /**
     * getter for intensity
     * @return intensity
     */
    public Color getIntensity() {
        return _intensity;
    }
}
