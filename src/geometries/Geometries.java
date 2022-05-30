package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.*;


public class Geometries extends Intersectable {
    /**
     * intersectable geometries list
     */
    private List<Intersectable> _intersectables= new LinkedList<>();

    /**
     * Geometries constructor
     * @param  geometries {@link Intersectable}
     */
    public Geometries(Intersectable ... geometries) {
        add(geometries);
    }

    /**
     * default Geometries constructor
     */
    public Geometries() {
       _intersectables= new LinkedList<>();
   }

    /**
     * add geometries to _intesectables
     * @param geometries {@link Intersectable}
     */
   public void add(Intersectable... geometries) {
           Collections.addAll(_intersectables,geometries);
   }

   /**
     * add geometries to _intesectables
     * @param geometries {@link Intersectable}
     */
   public void addAll(List <Intersectable> geometries) {
           _intersectables.addAll(geometries);
   }


    /**
     * find intersections (GeoPoints) on geometry based-on given ray
     * @param ray {@link Ray} pointing toward the objects
     * @return
     */
    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
            List<GeoPoint> intersectionPoints = null;

            //gets list of intersections of all elements with the ray
            for (Intersectable value : _intersectables) {
                List<GeoPoint> itemPoints = value.findGeoIntersections(ray);
                if (itemPoints != null){
                    if(intersectionPoints == null){
                        intersectionPoints = new LinkedList<>();
                    }
                    intersectionPoints.addAll(itemPoints);
                }
            }
            return intersectionPoints;
        }
}
