package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.*;


public class Geometries implements Intersectable {
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
    public List<Point> findIntersections(Ray ray) {
        List<Point> intersectionPoints = null;

        //gets list of intersections of all elements with the ray
        for (Intersectable value : _intersectables) {
            List<Point> itemPoints = value.findIntersections(ray);
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
