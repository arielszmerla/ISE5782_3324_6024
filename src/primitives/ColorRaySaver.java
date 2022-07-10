package primitives;

/**
 * It's a wrapper for a ray and a color
 */
public class ColorRaySaver {
    // It's a variable that holds the ray.
    private Ray ray;
    // It's a that holds the color of the object.
    private Color color;

    public ColorRaySaver(Ray ray, Color color) {
        this.ray = ray;
        this.color = color;
    }

    /**
     * Returns the ray that was used to create this intersection.
     *
     * @return The ray object is being returned.
     */
    public Ray getRay() {
        return ray;
    }

    /**
     * This function returns the color of the object.
     *
     * @return The color of the car.
     */
    public Color getColor() {
        return color;
    }
}