package objects;

import geometries.Intersectable;
import geometries.Polygon;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Wall {
    public List<Intersectable> _parts= new LinkedList<Intersectable>();

    public Wall(Point c1,Point c2,Point c3,Point c4, double height, Color _color, Material _material) {
        Polygon s1 = (Polygon) new Polygon(c1, c2, c2.add(new Vector(0,height,0)),c1.add(new Vector(0,height,0)))
                .setEmission(_color).setMaterial(_material);
        Polygon s2 = (Polygon) new Polygon(c3, c4, c4.add(new Vector(0,height,0)),c3.add(new Vector(0,height,0)))
                .setEmission(_color).setMaterial(_material);
        Polygon s3 = (Polygon) new Polygon(c2, c3, c3.add(new Vector(0,height,0)),c2.add(new Vector(0,height,0)))
                .setEmission(_color).setMaterial(_material);
        Polygon s4 = (Polygon) new Polygon(c1, c4, c4.add(new Vector(0,height,0)),c1.add(new Vector(0,height,0)))
                .setEmission(_color).setMaterial(_material);
        Polygon up = (Polygon) new Polygon(c1.add(new Vector(0,height,0)),c2.add(new Vector(0,height,0)),
                c3.add(new Vector(0,height,0)),c4.add(new Vector(0,height,0)))
                .setEmission(_color).setMaterial(_material);
        Collections.addAll(_parts, s1,s2,s3,s4,up);
    }
}
