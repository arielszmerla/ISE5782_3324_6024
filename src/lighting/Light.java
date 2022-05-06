package lighting;

import primitives.Color;

/**
 * class base light
 */
abstract class Light  {
    /**
     * {@link Color} intensity of the light
     */
    private Color _intensity;

    /**
     * set intensity
     * @param intensity {@link Color}
     */
    protected Light(Color intensity){
        _intensity=intensity;
    }

    /**
     * get intensity
     * @return {@link Color}
     */
    public Color getIntensity() {
        return _intensity;
    }

}
