package renderer;

import lighting.LightSource;
import primitives.*;
import primitives.Vector;
import renderer.scene.Scene;
import geometries.Intersectable.GeoPoint;
import static java.lang.Math.random;

import java.util.*;

import static primitives.Util.*;

/**
 * The type Ray tracer basic.
 */
public class RayTracerBasic extends RayTracer {
    private static final double DELTA = 0.1;
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final double INITIAL_K = 1.0;
    private int _glossinessRays = 10;

    /**
     * scene setter
     *
     * @param scene {@link Scene}
     */
    protected RayTracerBasic(Scene scene) {
        super(scene);
    }

    /**
     * Get color of the intersection of the ray with the scene
     *
     * @param ray Ray to trace
     * @return Color of intersection
     */
    @Override
    public Color traceRay(Ray ray) {
          if (this.findClosestIntersection(ray) != null)
               return calcColor(this.findClosestIntersection(ray), ray);
            return _scene._background;

    }
    public RayTracerBasic setGlossinessRays(int glossinessRays) {
        if (glossinessRays <= 0) {
            throw new IllegalArgumentException("number of glossiness rays should be greater than 0");
        }
        _glossinessRays = glossinessRays;
        return this;
    }

    private Color calcColor(GeoPoint intersection, Ray ray,int level,Double3 k) {
        if(intersection==null)
            return _scene._background;
        Color color = intersection._geometry.getEmission()
                .add(calcLocalEffects(intersection,ray,k));
        return 1 == level ? color : color.add(calcGlobalEffects2(intersection, ray, level, k));

    }
    /**
     * Calculate color using recursive function
     *
     * @param geopoint the point of intersection
     * @param ray      the ray
     * @return the color
     */
    private Color calcColor(GeoPoint geopoint, Ray ray) {
        return calcColor(geopoint, ray, MAX_CALC_COLOR_LEVEL,new Double3(INITIAL_K))
                .add(_scene._ambientLight.getIntensity());
    }

    private Color calcGlobalEffects(GeoPoint intersection, Ray ray, int level, Double3 k) {
        Color color = Color.BLACK;
        Double3 kr= intersection._geometry.getMaterial()._kR;Double3 kkr=kr.product(k);
        Vector n = intersection._geometry.getNormal(intersection._point);
        if(kkr.biggerThan(MIN_CALC_COLOR_K)){
            Ray reflectedRay = constructReflectedRay(n, intersection._point, ray);
            GeoPoint reflectedPoint = findClosestIntersection(reflectedRay);
            if (reflectedPoint != null)
                color= color.add(calcColor(reflectedPoint,reflectedRay,level-1,kkr).scale(kr));
        }
        Double3 kt=intersection._geometry.getMaterial()._kT;Double3 kkt=kt.product(k);
        if(kkt.biggerThan(MIN_CALC_COLOR_K)){
            Ray refractedRay = constructRefractedRay(n, intersection._point, ray);
            GeoPoint refractedPoint = findClosestIntersection(refractedRay);
            if (refractedPoint != null)
                color= color.add(calcColor(refractedPoint,refractedRay,level-1,kkt).scale(kt));
        }
        return color;
    }
    /**
     *help function to the recursion
     * @param ray from the geometry
     * @param level of recursion
     * @param kx parameter of the recursion
     * @param kkx parameter of the recursion
     * @return the calculate color
     */
    private Color calcGlobalEffect(Ray ray, int level, Double3 kx, Double3 kkx) {
        GeoPoint gp = findClosestIntersection(ray);
        return (gp == null ? _scene._background :  calcColor(gp, ray, level - 1, kkx)).scale(kx);
    }
    /**
     * Calculates the reflection and the refraction
     * at a given intersection point.
     *
     * @param intersection    the intersection point
     * @param ray   the ray that caused the intersection
     * @param level the number of the recursive calls
     *              to calculate the next reflections and
     *              refractions
     * @param k     the effect's strength by the reflection and refraction
     * @return the color on the intersection point
     */
    private Color calcGlobalEffects2(GeoPoint intersection, Ray ray, int level, Double3 k) {
        Color color = Color.BLACK;
        Material material=intersection._geometry.getMaterial();
        Double3 kr= material._kR;Double3 kkr=kr.product(k);
        Vector n = intersection._geometry.getNormal(intersection._point);
        Vector v = ray.getDir();
        if (v.dotProduct(n) > 0) {
            n = n.scale(-1);
        }
        // Calculating the reflected rays and adding them to the color.
        if(kkr.biggerThan(MIN_CALC_COLOR_K)){
            Ray[] reflectedRays = constructReflectedRays(intersection._point, v, n, material._kG);
            for (Ray reflectedRay : reflectedRays) {
                color = color.add(calcGlobalEffect(reflectedRay, level, material._kR, kkr)
                        .scale(1d / reflectedRays.length));
            }
        }

        // adds the refraction effect
        Double3 kt=intersection._geometry.getMaterial()._kT;Double3 kkt=kt.product(k);
        if(kkt.biggerThan(MIN_CALC_COLOR_K)){

            Ray[] refractedRays = constructRefractedRays(intersection._point, v, n.scale(-1), material._kG);
            for (Ray refractedRay : refractedRays) {
                color = color.add(calcGlobalEffect(refractedRay, level, material._kT, kkt)
                        .scale(1d / refractedRays.length));
            }
        }

        return color;
    }
    private Color calcLocalEffects(GeoPoint intersection, Ray ray,Double3 k) {
        Vector v = ray.getDir();
        Vector n = intersection._geometry.getNormal(intersection._point);
        double nv= alignZero(n.dotProduct(v));
        if (nv== 0) return Color.BLACK;
        int nShininess = intersection._geometry.getMaterial().getnShininess();
        Double3 kd = intersection._geometry.getMaterial().getkD();
        Double3 ks = intersection._geometry.getMaterial().getkS();

        Color color= Color.BLACK;
        for (LightSource lightSource: _scene.lights) {
            Vector l = lightSource.getL(intersection._point);
            double nl= alignZero(n.dotProduct(l));
            if (nl * nv> 0) { // sign(nl) == sing(nv)\
                Double3 ktr = transparency(intersection,l, n,lightSource);
                if (ktr.product(k).biggerThan(MIN_CALC_COLOR_K)) {
                    Color lightIntensity = lightSource.getIntensity(intersection._point).scale(ktr);
                    color = color.add(calcDiffusive(kd, l, n, lightIntensity),
                            calcSpecular(ks, l, n, v, nShininess, lightIntensity));
                }

            }
        }
        return color;
    }

    /**
     * The specular component is the light intensity multiplied by the specular coefficient (ks) multiplied by the max of 0
     * and the dot product of the negative view vector and the reflection vector raised to the power of the shininess
     * coefficient
     *
     * @param ks the specular coefficient of the material
     * @param l the vector from the point to the light source
     * @param n normal vector
     * @param v the vector from the point to the camera
     * @param nShininess the shininess of the material
     * @param lightIntensity the intensity of the light source
     * @return The color of the point.
     */
    private Color calcSpecular(Double3 ks, Vector l, Vector n, Vector v, int nShininess, Color lightIntensity) {

        // Calculating the reflection vector.
        Vector r = new Vector(l.add(n.scale(n.dotProduct(l) * -2)).get_xyz());
        double vR;
        try {
            // Calculating the dot product of the vector -v and the vector r.
            vR = v.scale(-1).dotProduct(r);
        } catch (Exception exception) {
            //if vR is 0 vector
            return lightIntensity.scale(1);
        }
        //color = ks * max(0, -v.r)^nSh @ppt 7 theoretical course
        // Calculating the specular component of the light intensity.
        return lightIntensity.scale(ks.scale( Math.pow(Math.max(0, vR), nShininess)));
    }

    /**
     * Calculate color of the diffusive effects of the light
     *
     * @param kd diffusive ratio
     * @param l light's direction vector
     * @param n normal vector
     * @param lightIntensity intensity of the light
     * @return color of the diffusive effect
     */
    private Color calcDiffusive(Double3 kd, Vector l, Vector n, Color lightIntensity) {
        double lN;
        try {
            lN = l.normalize().dotProduct(n.normalize());
        } catch (Exception exception) {
            return lightIntensity.scale(0);
        }
        //color = light * |l.n| * kd
        return lightIntensity.scale(kd.scale(Math.abs(lN)));
    }

    // The below code is checking if the point is unshaded. If it is unshaded, it will return true. If it is shaded, it
    // will return false.
    /**
     * If the point is not shaded, return true
     *
     * @param gp the point on the geometry that we are currently shading
     * @param l the vector from the point to the light source
     * @param n the normal vector of the point
     * @param ls the light source
     * @return If the point is not shaded.
     */
    private boolean unshaded(GeoPoint gp, Vector l, Vector n, LightSource ls){
        Vector lightDir = l.scale(-1);// from point to light source
        Vector epsVector= n.scale(n.dotProduct(lightDir)>0?DELTA:-DELTA);
        Point point = gp._point.add(epsVector);
        Ray lightRay = new Ray(point, n, lightDir);
        List<GeoPoint> intersections = _scene._geometries.findGeoIntersections(lightRay);
        if (intersections != null) {
            for (GeoPoint gp2 : intersections) {
                if ( point.distance(gp2._point)<ls.getDistance(point) && gp2._geometry.getMaterial()._kT.equals(new Double3(0.0))) {
                    return false;
               }
            }
        }
        return true;
    }
    /**
     * The function returns the transparency of the point, which is the product of the transparency of the point's geometry
     * and the transparency of all the geometries between the point and the light source
     *
     * @param gp The point on the geometry that we're currently shading.
     * @param l the vector from the point to the light source
     * @param n the normal vector of the point
     * @param ls the light source
     * @return The transparency of the point.
     */
    private Double3 transparency(GeoPoint gp, Vector l, Vector n, LightSource ls) {
        Vector lightDir = l.scale(-1);// from point to light source
        Vector epsVector = n.scale(n.dotProduct(lightDir) > 0 ? DELTA : -DELTA);
        Point point = gp._point.add(epsVector);
        Ray lightRay = new Ray(point, n, lightDir);
        List<GeoPoint> intersections = _scene._geometries.findGeoIntersections(lightRay);
        Double3 ktr = new Double3(1);
        if (intersections != null) {
            for (GeoPoint gp2 : intersections) {
                if (point.distance(gp2._point) < ls.getDistance(point)) {
                    ktr=gp2._geometry.getMaterial()._kT.product(ktr);
                }
            }
        }
        return ktr;
    }
    /**
     * Construct the ray refracted by an intersection with the geometry
     * @param n normal to the geometry at intersection
     * @param point the intersection point
     * @param innerRay the ray entering
     * @return refracted ray (in our project there is no refraction incidence, so we return a new ray with the same characteristics)
     */
    private Ray constructRefractedRay(Vector n, Point point, Ray innerRay) {
        return new Ray(point,n,innerRay.getDir());
    }

    /**
     * Construct the ray getting reflected on an intersection point
     * @param n normal to the point
     * @param point the intersection point
     * @param innerRay the ray entering at the intersection
     * @return the reflected ray
     */
    private Ray constructReflectedRay(Vector n, Point point, Ray innerRay) {
        //r = v - 2 * (v*n) * n
        //r is the reflected ray
        Vector v = innerRay.getDir();
        Vector r = null;
        try {
            r = v.substract(n.scale(v.dotProduct(n)).scale(2)).normalize();
        } catch (Exception e) {
            return null;
        }
        return new Ray(point, n,r);
    }

    /**
     * Find the closest intersection point between a ray base and the scene's geometries
     * @param ray the ray
     * @return the closest point
     */
    private GeoPoint findClosestIntersection(Ray ray) {
        List<GeoPoint> geoPoints = _scene._geometries.findGeoIntersections(ray);
        return ray.getClosestGeoPoint(geoPoints);
    }

    @Override
    // Creating a method called averageColor that takes in a linked list of rays and returns a color.
    public Color averageColor(LinkedList<Ray> rays){
        Color color=Color.BLACK;
        for( Ray ray:rays){
            color=color.add(traceRay(ray));
        }
        return color.reduce(Double.valueOf(rays.size()));
    }
    /**
     * Constructs randomized reflection rays at the intersection point according to kG.
     * If kG is 1 then only one ray is returned with the specular vector
     *
     * @param point the intersection point
     * @param v     the intersection's ray direction
     * @param n     the normal at the intersection point
     * @param kG    the glossiness parameter in range of [0,1], where 0 - matte, 1 - glossy
     * @return randomized reflection rays
     */
    private Ray[] constructReflectedRays(Point point, Vector v, Vector n, Double3 kG) {
        Vector n2vn = n.scale(-2 * v.dotProduct(n));
      //  Vector r = v.add(n2vn);
        Vector r= new Vector(v.add(n2vn).get_xyz());
        // If kG is equals to 1 then return only 1 ray, the specular ray (r)
        if (Double3.ONE.equals(kG)) {
            return new Ray[]{new Ray(point, n, r)};
        }

        Vector[] randomizedVectors = createRandomVectorsOnSphere(n,_glossinessRays);

        // If kG is equals to 0 then select all the randomized vectors
        if (Double3.ZERO==kG) {
            return Arrays.stream(randomizedVectors)
                    .map(vector -> new Ray(point, n,vector))
                    .toArray(Ray[]::new);
        }

        // If kG is in range (0,1) then move the randomized vectors towards the specular vector (v)
        return Arrays.stream(randomizedVectors)
                .map(vector -> new Ray(point,n,
                        new Vector( vector.scale(Double3.ONE.subtract(kG)).add(r.scale(kG)).get_xyz())))
                .toArray(Ray[]::new);
    }

    /**
     * Constructs randomized refraction rays at the intersection point according to kG.
     * If kG is 1 then only one ray is returned with the vector v (which is the specular vector).
     *
     * @param point the intersection point
     * @param v     the intersection's ray direction
     * @param n     the normal at the intersection point
     * @param kG    the glossiness parameter in range of [0,1], where 0 - matte, 1 - glossy
     * @return randomized refraction rays
     */
    private Ray[] constructRefractedRays(Point point, Vector v, Vector n, Double3 kG) {
        // If kG is equals to 1 then return only 1 ray, the specular ray (v)
       Double3 one= new Double3(1.0,1.0,1.0);
        if (one.equals(kG)) {
            return new Ray[]{new Ray(point, n, v)};
        }

        Vector[] randomizedVectors = createRandomVectorsOnSphere(n, _glossinessRays);

        // If kG is equals to 0 then select all the randomized vectors
        if (Double3.ZERO.equals(kG)) {
            return Arrays.stream(randomizedVectors)
                    .map(vector -> new Ray(point, n,vector))
                    .toArray(Ray[]::new);
        }

        // If kG is in range (0,1) then move the randomized vectors towards the specular vector (v)
        return Arrays.stream(randomizedVectors)
                .map(vector -> new Ray(point,n,
                        vector.scale(Double3.ONE.subtract(kG).add(v.scale(kG).get_xyz()))))
                .toArray(Ray[]::new);
    }

    /**
     * We create a random vector on the hemisphere, and then we rotate it around the normal vector
     * source: https://my.eng.utah.edu/~cs6958/slides/pathtrace.pdf#page=18
     * @param n The normal vector of the surface.
     * @param numOfVectors The number of vectors to be generated.
     * @return A vector that is orthogonal to the normal vector.
     */
    private Vector[] createRandomVectorsOnSphere(Vector n, int numOfVectors) {
        // pick axis with smallest component in normal
        // in order to prevent picking an axis parallel
        // to the normal and eventually creating zero vector
        Vector axis;

        // Finding the axis that is closest to the normal vector.
        if (Math.abs(n.getX()) < Math.abs(n.getY()) && Math.abs(n.getX()) < Math.abs(n.getZ())) {
            axis = new Vector(1, 0, 0);
        } else if (Math.abs(n.getY()) < Math.abs(n.getZ())) {
            axis = new Vector(0, 1, 0);
        } else {
            axis = new Vector(0, 0, 1);
        }

        // find two vectors orthogonal to the normal
        Vector x = n.crossProduct(axis);
        Vector z = n.crossProduct(x);

        Vector[] randomVectors = new Vector[numOfVectors];
        for (int i = 0; i < numOfVectors; i++) {
            // pick a point on the hemisphere bottom
            double u, v, u2, v2;
            do {
                u = random() * 2 - 1;
                v = random() * 2 - 1;
                u2 = u * u;
                v2 = v * v;
            } while (u2 + v2 >= 1);

            // calculate the height of the point
            double w = Math.sqrt(1 - u2 - v2);

            // create the new vector according to the base (x, n, z) and the coordinates (u, w, v)
            randomVectors[i] =new Vector( x.scale(u).add(z.scale(v)).add(n.scale(w)).get_xyz());

        }

        return randomVectors;
    }
}