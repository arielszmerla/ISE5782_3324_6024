package renderer;

import geometries.Geometry;
import geometries.Sphere;
import lighting.DirectionalLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import renderer.scene.Scene;

import static java.awt.Color.BLUE;
import static org.junit.jupiter.api.Assertions.*;


class CameraTest {
    static final Point ZERO_POINT = new Point(0, 0, 0);
    private Color spCL = new Color(800, 500, 0); // Sphere test Color of Light
    private Material material = new Material().setKd(0.5).setKs(0.5).setnShininess(300);

    private Scene scene1 = new Scene.SceneBuilder("Test scene").build();
    private Geometry sphere = new Sphere(new Point(0, 0, -50), 50d) //
            .setEmission(new Color(BLUE).reduce(2)) //
            .setMaterial(material);
    private Camera camera1 = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
            .setVPSize(150, 150) //
            .setVPDistance(1000);

    /**
     * Test method for
     * {@link Camera#constructRay(int, int, int, int)}.
     */
    @Test
    void testConstructRay() {
        Camera camera = new Camera(ZERO_POINT, new Vector(0, 0, -1), new Vector(0, -1, 0))
                .setVPDistance(10);
        String badRay = "Bad ray";

        // ============ Equivalence Partitions Tests ==============
        // EP01: 4X4 Inside (1,1)
        assertEquals(new Ray(ZERO_POINT, new Vector(1, -1, -10)),
                camera
                        .setVPSize(8, 8)
                        .constructRay(4, 4, 1, 1), badRay);

        // =============== Boundary Values Tests ==================
        // BV01: 3X3 Center (1,1)
        assertEquals(new Ray(ZERO_POINT, new Vector(0, 0, -10)),
                camera.setVPSize(6, 6).constructRay(3, 3, 1, 1), badRay);

        // BV02: 3X3 Center of Upper Side (0,1)
        assertEquals(new Ray(ZERO_POINT, new Vector(0, -2, -10)),
                camera.setVPSize(6, 6).constructRay(3, 3, 1, 0), badRay);

        // BV03: 3X3 Center of Left Side (1,0)
        assertEquals(new Ray(ZERO_POINT, new Vector(2, 0, -10)),
                camera.setVPSize(6, 6).constructRay(3, 3, 0, 1), badRay);

        // BV04: 3X3 Corner (0,0)
        assertEquals(new Ray(ZERO_POINT, new Vector(2, -2, -10)),
                camera.setVPSize(6, 6).constructRay(3, 3, 0, 0), badRay);

        // BV05: 4X4 Corner (0,0)
        assertEquals(new Ray(ZERO_POINT, new Vector(3, -3, -10)),
                camera.setVPSize(8, 8).constructRay(4, 4, 0, 0), badRay);

        // BV06: 4X4 Side (0,1)
        assertEquals(new Ray(ZERO_POINT, new Vector(1, -3, -10)),
                camera.setVPSize(8, 8).constructRay(4, 4, 1, 0), badRay);

    }



    @Test
    void testRotateCamera() {
        scene1._geometries.add(sphere);
        scene1._lights.add(new DirectionalLight(spCL, new Vector(1, 1, -0.5)));
        ImageWriter imageWriter = new ImageWriter("moveCameraSphereRotate", 500, 500);
        camera1.rotateCamera(new Vector(0.1,0,0), 20);
        camera1.setImageWriter(imageWriter) //

                .setRayTracer(new RayTracerBasic(scene1)) //
                .renderImage(); //
        camera1.writeToImage();//
        imageWriter = new ImageWriter("moveCameraSphereRotate2", 500, 500);
        camera1.rotateCamera(new Vector(0.1,0,0), -40);
        camera1.setImageWriter(imageWriter) //

                .setRayTracer(new RayTracerBasic(scene1)) //
                .renderImage(); //
        camera1.writeToImage();//
        imageWriter = new ImageWriter("moveCameraSphereRotate3", 500, 500);
        camera1.rotateCamera(new Vector(0,1,0), 20);
        camera1.setImageWriter(imageWriter) //

                .setRayTracer(new RayTracerBasic(scene1)) //
                .renderImage(); //
        camera1.writeToImage();//
    }

    @Test
    void testMoveCamera() {
        scene1._geometries.add(sphere);
        scene1._lights.add(new DirectionalLight(spCL, new Vector(1, 1, -0.5)));
        ImageWriter imageWriter = new ImageWriter("moveCameraSphereUp", 500, 500);
        camera1.moveCamera(new Vector(40,0,0));
        camera1.setImageWriter(imageWriter) //

                .setRayTracer(new RayTracerBasic(scene1)) //
                .renderImage(); //
        camera1.writeToImage();//
        imageWriter = new ImageWriter("moveCameraSphereLeft", 500, 500);
        camera1.moveCamera(new Vector(-40,40,0));
        camera1.setImageWriter(imageWriter) //

                .setRayTracer(new RayTracerBasic(scene1)) //
                .renderImage(); //
        camera1.writeToImage();//
        imageWriter = new ImageWriter("moveCameraSphereBack", 500, 500);
        camera1.moveCamera(new Vector(0,-40,1000));
        camera1.setImageWriter(imageWriter) //

                .setRayTracer(new RayTracerBasic(scene1)) //
                .renderImage(); //
        camera1.writeToImage();//

    }
}