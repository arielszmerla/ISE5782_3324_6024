package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Geometries class
 * @authors Ariel&Gal
 *
 */
class GeometriesTest {

    /**
     * Test method for {@link Geometries#findIntersections(Ray)}.
     */
    @Test
    void findIntersectionsTest() {
        List<Point> resultPointIntersectections;
        Geometries geometries = new Geometries(
                new Plane(new Point(2, 0, 0), new Vector(-1, 1, 0)),
                new Sphere( new Point(5,0,0) , 1),
                new Triangle(new Point(8.5, -1, 0), new Point(7.5, 1.5, 1), new Point(7.5, 1.5, -1))
        );

        // ============ Equivalence Partitions Tests ==============
        // TC01: Some geo intersect
        resultPointIntersectections = geometries.findIntersections(new Ray(new Point(1, 0, 0), new Vector(7, 3, 0)));
        assertNotNull(resultPointIntersectections, "Should not be empty!");
        assertEquals(1, resultPointIntersectections.size(), "Bad intersection number of point");

        // =============== Boundary Values Tests ==================
        // TC11: Empty collection of result
        resultPointIntersectections = new Geometries().findIntersections(new Ray(new Point(1, 0, 0), new Vector(1, 0, 0)));
        assertNull(resultPointIntersectections, "Should be empty!");

        // TC12: No intersections
        resultPointIntersectections = geometries.findIntersections(new Ray(new Point(1, 0, 0), new Vector(1, 3, 0)));
        assertNull(resultPointIntersectections, "Should be empty!");

        // TC13: One intersection
        resultPointIntersectections = geometries.findIntersections(new Ray(new Point(1, 0, 0), new Vector(4, 3, 0)));
        assertNotNull(resultPointIntersectections, "Should not be empty!");
        assertEquals(1, resultPointIntersectections.size(), "Bad intersection number of point");

        // TC14: Many intersections
        resultPointIntersectections = geometries.findIntersections(new Ray(new Point(1, 0, 0), new Vector(7, 1, 0)));
        assertNotNull(resultPointIntersectections, "Should not be empty!");
        assertEquals(4, resultPointIntersectections.size(), "Bad intersection number of point");

    }
}