package renderer;

import primitives.Color;
import primitives.Ray;
import renderer.scene.Scene;

import java.util.LinkedList;

/**
 * abstract class for ray tracer
 */
public abstract class RayTracer {
    protected Scene _scene;

    /**
     * scene setter
     * @param scene {@link Scene}
     */
    protected RayTracer (Scene scene){
        _scene=scene;
    }
    public abstract Color traceRay(Ray ray);
    public abstract Color averageColor(LinkedList<Ray> rays);
}
