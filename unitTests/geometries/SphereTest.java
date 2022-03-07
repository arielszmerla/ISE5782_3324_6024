package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import static org.junit.jupiter.api.Assertions.*;

class SphereTest {

    @Test
    void testGetNormal() {
        Sphere sph= new Sphere( new Point(0,0,0), 1);
        Point pt= new Point(0,0,1);
        Vector vec = new Vector (0,0,1);
        assertEquals(vec, sph.getNormal(pt),"the vector returned is not a normal to a sphere, go to...");
    }
}