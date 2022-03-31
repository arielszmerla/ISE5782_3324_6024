package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
}