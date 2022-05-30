package objects;

import geometries.Cylinder;
import geometries.Intersectable;
import primitives.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Table  {
    private double _radius;
    private double _height;
    private Point _center;
    private Color _color;
    private Material _material;
    public List<Intersectable> _parts= new LinkedList<Intersectable>();
    public Table(double _radius, double _height, Point _center, Color _color, Material _material) {
        this._radius = _radius;
        this._height = _height;
        this._center = _center;
        this._color = _color;
        this._material = _material;

        double x = Math.sqrt((_radius-5)*(_radius-5)/2);

        Collections.addAll(_parts, new Cylinder(new Ray(_center,new Vector(0,1,0)),_radius, 10d).setEmission(_color) //
                        .setMaterial(_material),
                new Cylinder(new Ray(_center.add(new Vector(x,0,x)),new Vector(0,-1,0)),5d, _height).setEmission(_color) //
                        .setMaterial(_material),
               new Cylinder(new Ray(_center.add(new Vector(x,0,-x)),new Vector(0,-1,0)),5d, _height).setEmission(_color) //
                        .setMaterial(_material),
               new Cylinder(new Ray(_center.add(new Vector(-x,0,-x)),new Vector(0,-1,0)),5d, _height).setEmission(_color) //
                        .setMaterial(_material),
               new Cylinder(new Ray(_center.add(new Vector(-x,0,x)),new Vector(0,-1,0)),5d, _height).setEmission(_color) //
                        .setMaterial(_material));
    }


}
