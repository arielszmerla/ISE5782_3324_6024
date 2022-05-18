package geometries;

import primitives.*;
import primitives.Vector;

import java.util.*;


import static java.awt.Color.*;

public class Spring extends Geometry {
    public Point _start;
    public List<Geometry> _points=new LinkedList<>();
public double height;
    public Spring(Point start,double height,double radius ) {
        _start = start;
        for(double t=0; t<height ;t+=0.01)
            _points.add( new Circle(new Point(t,Math.cos(t),Math.sin(t)),new Point(t,Math.cos(t),Math.sin(t)).add( new Vector(new Double3(radius)))).setEmission(new Color(PINK)).setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(30)));
    }

    @Override
    public Vector getNormal(Point point) {
return null;
    }

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        for (Geometry g : _points){
          if(  g.findGeoIntersectionsHelper(ray)!=null)
              return  g.findGeoIntersectionsHelper(ray);
        }
        return null;
    }
}
