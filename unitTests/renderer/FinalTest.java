package renderer;

import objects.Table;
import objects.Tile;
import objects.Wall;
import org.junit.jupiter.api.Test;

import lighting.*;
import geometries.*;
import primitives.*;
import renderer.scene.Scene;

import java.util.Arrays;
import java.util.Random;

import static java.awt.Color.*;


public class FinalTest {
    Random rand = new Random();
    private Scene scene1 = new Scene.SceneBuilder("Test scene").build();
    private Camera camera1 = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
            .setVPSize(150, 150) //
            .setVPDistance(1000);

    @Test
    // A test for the scene of the spheres.
    public void sphereSpot() {
        Point center = new Point(-60,0,0);
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                Sphere sphere = (Sphere) new Sphere(center.add(new Vector(30*(j+0.001),0,-40*(i+0.001))),10d)
                        .setMaterial(new Material().setKs(0.5).setKd(0.5).setKg(1));
                sphere.setEmission(new Color(rand.nextInt(256),rand.nextInt(256),rand.nextInt(256)));
                scene1._geometries.add(sphere);

        }
           /* scene1._geometries.add(
                    new Sphere(new Point(-40,55,-150), 20)
                            .setEmission(new Color(136,139,141))
                            .setMaterial(new Material().setKs(0.5).setKd(0.5).setKr(1).setKg(1))
            );*/
        }
        scene1._geometries.add(new Polygon(
                new Point(-70,-10,40),
                new Point(70,-10,40),
                new Point(70,-10,-160),
                new Point(-70,-10,-160))
                .setEmission(new Color(BLACK))
                .setMaterial(new Material().setKs(0.2).setKd(0.5).setKr(0.7).setKg(0.9))

        );
        scene1._lights.add(new SpotLight(new Color(WHITE), new Point(-100,200,-800), new Vector(0.1,-0.2,1)).set_shorten(0.1));
        ImageWriter imageWriter = new ImageWriter("balls", 500, 500);
        camera1.setFocusField(1800);
        Camera camera1 = new Camera(new Point(0, 500, 950), new Vector(0, -0.432, -0.901), new Vector(0, 0.901, -0.432)) //
                .setVPSize(150, 150) //
                .setVPDistance(1000);
        camera1.moveCamera(new Vector(400, 0, 0));
        camera1.rotateCamera(new Vector(0, 0.901, -0.432), 19.45);
        camera1.setImageWriter(imageWriter) //
                .setRayTracer(new RayTracerBasic(scene1)) //
                .renderImage(); //
        camera1.writeToImage();//
    }
}