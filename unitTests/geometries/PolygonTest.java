package geometries;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

/**
 * class to implement test of Polygon class
 */
class PolygonTest {

    //four reused points for the class
    Point p1=new Point(0, 0, 1);
    Point p2 = new Point(1, 0, 0);
    Point p3 = new Point(0, 1, 0);
    Point p4 =  new Point(-1, 1, 1);
    /**
     * Test method for {@link geometries.Polygon#Polygon(primitives.Point...)}.
     */
    @Test
    public void testConstructor() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Correct concave quadrangular with vertices in correct order
        try {
            new Polygon(p1, p2, p3, p4);
        } catch (IllegalArgumentException e) {
            fail("Failed constructing a correct polygon");
        }

        // TC02: Wrong vertices order
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(p1, p2, p3,p1), //
                "Constructed a polygon with wrong order of vertices");

        // TC03: Not in the same plane
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(p1, p2, p3, new Point(0, 2, 2)), //
                "Constructed a polygon with vertices that are not in the same plane");

        // TC04: Concave quadrangular
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(p1, p2, p3,
                        new Point(0.5, 0.25, 0.5)), //
                "Constructed a concave polygon");

        // =============== Boundary Values Tests ==================

        // TC10: Vertex on a side of a quadrangular
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(p1, p2, p3,new Point(0, 0.5, 0.5)),
                "Constructed a polygon with vertice on a side");

        // TC11: Last point = first point
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(p1, p2, p3, p1),
                "Constructed a polygon with vertice on a side");

        // TC12: Co-located points
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(p1, p2, p3, p3),
                "Constructed a polygon with vertice on a side");

    }

    /**
     * Test method for {@link geometries.Polygon#getNormal(primitives.Point)}.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here
        Polygon pl = new Polygon(p1, p2, p3,p4);
        double sqrt3 = Math.sqrt(1d / 3);
        assertEquals(new Vector(sqrt3, sqrt3, sqrt3), pl.getNormal(new Point(0, 0, 1)), "Bad normal to polygon");
    }

    /**
     * Test method for {@link geometries.Polygon#findIntersections(primitives.Ray)}.
     */
    @Test
    public void testFindIntersections(){
        //reusable vector for the tests
        Vector v1= new Vector(0, 0, 1);
        //reusable polygon for the tests
        Polygon pol = new Polygon(new Point(0, 0, 1), new Point(2, 0, 1), new Point(2, 2, 1),new Point(0, 2, 1));
        //reusable plane for the tests
        Plane pl = new Plane(new Point(0, 0, 1), new Point(1, 0, 1), new Point(0, 1, 1));
        //reusable ray for the tests
        Ray ray;
        //two type of error:
                //or the plane intersection incorrect
                //or the polygon intersection incorrect
        String errorInIntersectionPlane = "Wrong intersection with plane";
        String errorBadIntersection = "Bad intersection";


        // ============ Equivalence Partitions Tests ==============
        // TC01: The Point inside polygon
        ray = new Ray(new Point(1, 1, 0), v1);
        assertEquals(List.of(new Point(1, 1, 1)), pol.findIntersections(ray), errorBadIntersection);

        // TC02:Point in on edge
        ray = new Ray(new Point(-1, 1, 0), v1);
        assertEquals(List.of(new Point(-1, 1, 1)), pl.findIntersections(ray), errorInIntersectionPlane);
        assertNull(pol.findIntersections(ray), errorBadIntersection);

        // TC03: Point in on vertex
        ray = new Ray(new Point(-1, -1, 0), v1);
        assertEquals(List.of(new Point(-1, -1, 1)), pl.findIntersections(ray), errorInIntersectionPlane);
        assertNull(pol.findIntersections(ray), errorBadIntersection);


        // =============== Boundary Values Tests ==================
        // TC04: In vertex
        ray = new Ray(new Point(0, 2, 0), v1);
        assertEquals(List.of(new Point(0, 2, 1)), pl.findIntersections(ray), errorInIntersectionPlane);
        assertNull(pol.findIntersections(ray), errorBadIntersection);

        // TC05: On edge
        ray = new Ray(new Point(0, 1, 0), v1);
        assertEquals(List.of(new Point(0, 1, 1)), pl.findIntersections(ray), errorInIntersectionPlane);
        assertNull(pol.findIntersections(ray), errorBadIntersection);

        // TC06: On edge continuation
        ray = new Ray(new Point(0, 3, 0), v1);
        assertEquals(List.of(new Point(0, 3, 1)), pl.findIntersections(ray), errorInIntersectionPlane);
        assertNull(pol.findIntersections(ray), errorBadIntersection);

    }
}