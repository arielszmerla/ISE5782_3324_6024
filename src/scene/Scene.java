package scene;

import lighting.AmbientLight;
import geometries.Geometries;
import primitives.Color;

public class Scene {

    private final String _name;
    private final Color _background;
    private final AmbientLight _ambientLight;
    private final Geometries _geometries;


    private Scene(SceneBuilder builder){
        _name = builder._name;
        _background = builder._background;
        _ambientLight = builder._ambientLight;
        _geometries = builder._geometries;
    }

    public String getName() {
        return _name;
    }

    public Color getBackground() {
        return _background;
    }

    public AmbientLight getAmbientLight() {
        return _ambientLight;
    }

    public Geometries getGeometries() {
        return _geometries;
    }

    public static class SceneBuilder{
        private  final String _name;
        private  Color _background;
        private AmbientLight _ambientLight;
        private Geometries _geometries;



        public SceneBuilder(String name){
            _name =name;
            _geometries = new Geometries();
        }
        //chaining methods

        public SceneBuilder setBackground(Color background) {
            _background = background;
            return this;
        }

        public SceneBuilder setAmbientLight(AmbientLight ambientLight) {
            _ambientLight = ambientLight;
            return this;
        }

        public SceneBuilder setGeometries(Geometries geometries) {
            _geometries = geometries;
            return this;
        }


    }
}
