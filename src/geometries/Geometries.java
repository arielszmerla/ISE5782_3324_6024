package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.*;


public class Geometries extends Intersectable {
    private List<Intersectable> _intersectables= new LinkedList<>();

    public Geometries(Intersectable ... geometries) {
        add(geometries);
    }
   public Geometries() {
       _intersectables= new LinkedList<>();
   }
   public void add(Intersectable... geometries) {

           Collections.addAll(_intersectables,geometries);
   }



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
