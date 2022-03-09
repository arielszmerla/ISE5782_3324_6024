package geometries;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;
/**
 * * class for tube class tests
 */
class TubeTest {

    /**
     * test for get normal
     */
    @Test
    void testGetNormal() {
        Tube tube= new Tube(new Ray(new Point(0,0,0), new Vector(0,0,1)), 1 );
        Point pt1= new Point(0,1,2);
        assertEquals(new Vector(0,1,0), tube.getNormal(pt1), "that is not a tube proper normal vector!");


        //---------------------BVA if the ankle between the point and the start of ray do a square-------------
        Point pt2 = new Point(0,1,0);
        assertEquals(new Vector(0,1,0), tube.getNormal(pt2), "that is not a tube proper normal vector!");
        //---------------------BVA if the ankle between the point and the start of ray do a square-------------
        Point pt3 = new Point(0,0,1);
        assertThrows(IllegalArgumentException.class, () -> tube.getNormal(pt3),
                "getNormal() for parallel inner point does not throw an exception");

    }
}