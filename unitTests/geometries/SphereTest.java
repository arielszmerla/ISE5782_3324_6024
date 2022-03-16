package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    /**
     * test for findIntersections
     */
    @Test
    void testFindIntersections() {

        // ============ Equivalence Partitions Tests ==============

        //TC01: two intersections
        Sphere sphere = new Sphere( new Point(0,0,1), 1);
        Ray ray1 = new Ray(new Point(-2,1,0) , new Vector(8,-4,2));
        assertEquals(List.of(new Point(-0.6666666666666674,0.3333333333333337,0.33333333333333315), new Point(0.857142857142859,-0.4285714285714295,0.7142857142857147)) ,sphere.findIntersections(ray1), "Bad intersections with sphere");

        //TC02: 0ne intersections, starts from inside the sphere
        Ray ray2 = new Ray(new Point(0,0,0.5) ,new Vector(0,1,0.5) );
        assertEquals(List.of(new Point(0,1,1)), sphere.findIntersections(ray2), "Bad intersections with sphere");

        //TC03: no intersections but if ray comes from sphere
        Ray ray3 = new Ray(new Point(2,2,0.5) ,new Vector(2,2,0.5));
        assertNull( sphere.findIntersections(ray3), "Bad intersections with sphere");

        //TC04: no intersections
        Ray ray4 = new Ray(new Point(10,10,5) ,new Vector(1,1,1) );
        assertNull(  sphere.findIntersections(ray4), "Bad intersections with sphere");

        // =============== Boundary Values Tests ==================

        // **** Group: Ray's line crosses the sphere (but not the center)

        // TC1.1: Ray starts at sphere and goes inside (1 points)
        assertEquals(List.of(new Point(0,1,1)), sphere.findIntersections(new Ray(new Point(0, 0, 0), new Vector(0, 1, 1))), "Wrong number of points");

        // TC12: Ray starts at sphere and goes outside (0 points)
        assertNull( sphere.findIntersections(new Ray(new Point(0, 0, 2), new Vector(1, 1, 1))), "Wrong number of points");

        // **** Group: Ray's line goes through the center

        // TC13: Ray starts before the sphere (2 points)
        assertEquals(List.of(new Point(0, 0, 0), new Point(0, 0, 2)), sphere.findIntersections(new Ray(new Point(0, 0, -10), new Vector(0, 0, 5))), "Bad intersection points");

        // TC14: Ray starts at sphere and goes inside (1 points)
        assertEquals(List.of(new Point(0, 0, 2)), sphere.findIntersections(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1))), "Bad intersection point");

        // TC15: Ray starts inside (1 points)
        assertEquals(List.of(new Point(0, 0, 2)), sphere.findIntersections(new Ray(new Point(0, 0, 0.1), new Vector(0, 0, 1))), "Bad intersection point");

        // TC16: Ray starts at the center (1 points)
        try {
            sphere.findIntersections(new Ray(new Point(0, 0, 1), new Vector(0, 1, 0)));
            fail("Error when ray starts at center of sphere");
        } catch (IllegalArgumentException exception) {

        }

        // TC17: Ray starts at sphere and goes outside (0 points)
        assertNull( sphere.findIntersections(new Ray(new Point(0, 0, 2), new Vector(0, 0, 1))), "Wrong number of points");

        // TC18: Ray starts after sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(0, 0, 3), new Vector(0, 0, 1))), "Wrong number of points");

        // **** Group: Ray's line is tangent to the sphere (all tests 0 points)
        // TC19: Ray starts before the tangent point
        assertNull(sphere.findIntersections(new Ray(new Point(0, 1, 0), new Vector(1, 0, 0))), "Wrong number of points");

        // TC20: Ray starts at the tangent point
        assertNull(sphere.findIntersections(new Ray(new Point(0, 0, 0), new Vector(1, 0, 0))), "Wrong number of points");

        // TC21: Ray starts after the tangent point
        assertNull(sphere.findIntersections(new Ray(new Point(0,1, 2), new Vector(0, 0, 1))), "Wrong number of points");

        // **** Group: Special cases
        // TC22: Ray's line is outside, ray is orthogonal to ray start to sphere's center line
        assertNull( sphere.findIntersections(new Ray(new Point(0, 0, 4), new Vector(0, 1, 0))), "Wrong number of points");
    }
}