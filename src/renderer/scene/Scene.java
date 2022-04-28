package renderer.scene;

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
    public AmbientLight _ambientLight;
    /**
     * Geometries figures of the scene
     */
    public Geometries _geometries;
    /**
     * light sources list
     */
    public List<LightSource> lights=new LinkedList<>();

    /**
     * Scene Constructor based-on SceneBuilder
     * @param builder {@link SceneBuilder}
     */
    private Scene(SceneBuilder builder) {
        this._name = builder._name;
        this._background = builder._background;
        this._ambientLight = builder._ambientLight;
        this._geometries = builder._geometries;
    }

    /**
     * lights setter
     * @param lights list {@link LightSource}
     * @return updated scene {@link Scene}
     */
    public Scene setLights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }


    /**
     * inner static class - Builder of the scene
     */
    public static class SceneBuilder {
        /**
         * name of the scene
         */
        public String _name;
        /**
         * background color of the scene
         */
        public Color _background = Color.BLACK;
        /**
         * Ambient light of the scene
         */
        public AmbientLight _ambientLight=new AmbientLight(Color.BLACK,new Double3(1,1,1));
        /**
         * geometries in the scene
         */
        public Geometries _geometries = new Geometries();

        /**
         * ctor based-on name
         * @param name name of scene
         */
        public SceneBuilder(String name) {
            this._name = name;
        }

        /**
         * ctor based-on scene
         * @param scene {@link Scene}
         */
        public SceneBuilder(Scene scene){
            _name = scene._name;
            _background = scene._background;
            _ambientLight = scene._ambientLight;
            _geometries = scene._geometries;
        }

        /**
         * set scene background
         * @param background
         * @return updated {@link SceneBuilder}
         */
        public SceneBuilder setBackground(Color background) {
            this._background = background;
            return this;
        }

        /**
         * set ambient light of scene
         * @param ambientLight
         * @return updated {@link SceneBuilder}
         */
        public SceneBuilder setAmbientLight(AmbientLight ambientLight) {
            this._ambientLight = ambientLight;
            return this;
        }

        /**
         * set geometries of scene
         * @param geometries
         * @return updated {@link SceneBuilder}
         */
        public SceneBuilder setGeometries(Geometries geometries) {
            this._geometries = geometries;
            return this;
        }

        /**
         * Scene builder
         * @return new {@link Scene} based-on SceneBuilder
         */
        public Scene build() {
            return new Scene(this);
        }
    }
}