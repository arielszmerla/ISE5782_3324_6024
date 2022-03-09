package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * class for sphere tests
 */
class SphereTest {

    /**
     * test for get normal
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here
        //create vector to compare with sphere normal
        Sphere sph= new Sphere( new Point(0,0,0), 1);
        Point pt= new Point(0,0,1);
        Vector vec = new Vector (0,0,1);
        assertEquals(vec, sph.getNormal(pt),"the vector returned is not a normal to a sphere, go to...");
    }
}