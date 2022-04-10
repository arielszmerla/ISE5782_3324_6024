package scene;

import lighting.AmbientLight;
import geometries.Geometries;
import lighting.LightSource;
import primitives.Color;
import primitives.Double3;

import java.util.LinkedList;
import java.util.List;

public class Scene {

    /**
     * name of the scene
     */
    public String _name;

    /**
     * background color of the scene
     */
    public Color _background= Color.BLACK;

    /**
     * Ambient light of the scene
     */
    public AmbientLight _ambientLight= new AmbientLight(Color.BLACK,new Double3(1,1,1));

    /**
     * Geometries figures of the scene
     */
    public Geometries _geometries;
    public List<LightSource> lights=new LinkedList<>();


    private Scene(SceneBuilder builder) {
        this._name = builder._name;
        this._background = builder._background;
        this._ambientLight = builder._ambientLight;
        this._geometries = builder._geometries;
    }

    public Scene setLights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }


    /**
     * Builder of the scene
     */
    public static class SceneBuilder {
        public String _name;
        public Color _background = Color.BLACK;
        public AmbientLight _ambientLight;
        public Geometries _geometries = new Geometries();

        public SceneBuilder(String name) {
            this._name = name;
        }
        public SceneBuilder(Scene scene){
            _name = scene._name;
            _background = scene._background;
            _ambientLight = scene._ambientLight;
            _geometries = scene._geometries;
        }

        public SceneBuilder setBackground(Color background) {
            this._background = background;
            return this;
        }

        public SceneBuilder setAmbientLight(AmbientLight ambientLight) {
            this._ambientLight = ambientLight;
            return this;
        }

        public SceneBuilder setGeometries(Geometries geometries) {
            this._geometries = geometries;
            return this;
        }

        public Scene build() {
            return new Scene(this);
        }
    }
}