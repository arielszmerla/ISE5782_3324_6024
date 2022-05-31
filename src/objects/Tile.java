package objects;

import geometries.Polygon;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;

public class Tile {
    public Polygon _pol;

    public Tile(Point _leftback, double _size, Color _color, Material _material) {
        this._pol =(Polygon) new Polygon(
                _leftback,
                _leftback.add(new Vector(_size, 0, 0)),
                _leftback.add(new Vector(_size, 0, _size)),
                _leftback.add(new Vector(0, 0, _size)))
                .setEmission(_color).setMaterial(_material);
    }
}
