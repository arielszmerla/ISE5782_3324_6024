package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * class that tests Plane class
 */
class PlaneTest {
    //three reused points for the class
    Point p1 = new Point(0, 0, 1);
    Point p2 = new Point(1, 0, 0);
    Point p3 = new Point(0, 1, 0);
    /**
     * Test method for {@link geometries.Plane#Plane(Point, Point, Point)}.
     */
    @Test
    public void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here
        Plane pl = new Plane(p1, p2,p3);
        //create a vector to be compared to constructored vector normal to a plane
        Point p0 = p1;
        Vector v = p2.subtract(p1);
        Vector u = p3.subtract(p1);
        Vector n = v.crossProduct(u);
        Vector normal = n.normalize();
        assertEquals(normal, pl.getNormal(), "Bad constructor to plane");
    }


    /**
     * test normal
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here
        Plane pl = new Plane(p1, p2,p3);
        //create a vector with 1 for length for the check
        double sqrt3 = Math.sqrt(1d / 3);
        assertEquals(
                new Vector(sqrt3, sqrt3, sqrt3),
                pl.getNormal(p1),
                "Bad normal to Plane");
    }

}