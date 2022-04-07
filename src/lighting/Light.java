package lighting;

import primitives.Color;

abstract class Light {
    private Color _intensity;
    protected Light(Color intensity){
        _intensity=intensity;
    }

    public Color getIntensity() {
        return _intensity;
    }

}
