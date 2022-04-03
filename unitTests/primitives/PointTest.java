package primitives;

import org.junit.jupiter.api.Test;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;
import java.util.List;
class PointTest {

    /**
     * test for get normal
     */
    @Test
    void testAdd() {
        fail("Not yet implemented");
    }
    /**
     * test for get normal
     */
    @Test
    void testSubtract() {

    }

    @Test
    void testDistance(){
        Point p1= new Point (0,0,0);
        Point p2= new Point(1,1,0);
        assertEquals(Math.sqrt(2),p1.distance(p2),"incorrect calculated distance");
    }

    @Test
    void testFindClosestPoint (){
        List<Point> points= new LinkedList<>();
        Ray ray =new Ray (new Point(0,0,0), new Vector(1,0,0));

        // ============ Equivalence Partitions Tests ==============
        // TC01: Some Points intersect
        points.add(new Point(1,0,0));
        points.add(new Point(1.5,0,0));
        points.add(new Point(2,0,0));
        assertEquals(new Point(1,0,0),ray.findClosestPoint(points), "this point is not the closest");

        // ============ BOUNDARY VALUES TEST ==============
        // TC11:
        // Empty points list
        points.clear();
        assertNull(ray.findClosestPoint(points),"it is an empty list");

        // TC12:
        // First point  of list is the closest

        points.add(new Point(1,0,0));
        points.add(new Point(2,0,0));
        assertEquals(new Point(1,0,0),ray.findClosestPoint(points), "the first point is the closest");

        // TC13:
        // last point  of list is the closest
        Collections.reverse(points);
        assertEquals(new Point(1,0,0),ray.findClosestPoint(points), "the last point is the closest");



    }
}