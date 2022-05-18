package renderer;

import org.junit.jupiter.api.Test;

import lighting.*;
import geometries.*;
import primitives.*;
import renderer.scene.Scene;

import java.util.Random;

import static java.awt.Color.*;

public class ImprovementTest {
    private Scene scene1 = new Scene.SceneBuilder("Test scene").build();
    private Camera camera1 = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
            .setVPSize(150, 150) //
            .setVPDistance(1000);
    private DirectionalLight _directionalLight= new DirectionalLight(new Color(200,200,200),new Vector(0,0,1) );
    private Material material = new Material().setKd(0.5).setKs(0.5).setnShininess(300).setKt(0.5);

    private Geometry sphere = new Sphere(new Point(0, 0, -5000), 50d) //
            .setEmission(new Color(MAGENTA).reduce(2)) //
            .setMaterial(material);
    private  Material material2=material.setKt(0.7);
    private Geometry sphere2 = new Sphere(new Point(50, 20, -200), 25d) //
            .setEmission(new Color(RED).reduce(4)) //
            .setMaterial(material2);
    private Geometry sphere3= new Sphere(new Point(50, 20, -1200), 40d) //
            .setEmission(new Color(RED).reduce(4)) //
            .setMaterial(material2);
    private Geometry sphere4= new Sphere(new Point(-50, 20, 0), 20d) //
            .setEmission(new Color(YELLOW).reduce(100)) //
            .setMaterial(material2);
    private Geometry sphere5= new Sphere(new Point(-50, -20, 200), 30d) //
            .setEmission(new Color(YELLOW).reduce(100)) //
            .setMaterial(material2);
    private Point spPL = new Point(-50, -50, 25); // Sphere test Position of Light
    private Color spCL = new Color(800, 500, 0); // Sphere test Color of Light
    @Test
    public void sphereSpot() {
        scene1._geometries.add(    new Tube(new Ray(new Point(0, 0, -7000),new Vector(0.01,0.01,1)),4d).setEmission(new Color(BLUE)) //
                .setMaterial(new Material().setKd(0.7).setKs(0.7).setnShininess(20).setKt(0.1)),
                new Tube(new Ray(new Point(0, 0, -7000),new Vector(-0.01,0.01,1)),4d).setEmission(new Color(BLUE)) //
                        .setMaterial(new Material().setKd(0.7).setKs(0.7).setnShininess(20).setKt(0.1)),
                new Tube(new Ray(new Point(0, 0, -7000),new Vector(0.01,-0.01,1)),4d).setEmission(new Color(BLUE)) //
                        .setMaterial(new Material().setKd(0.7).setKs(0.7).setnShininess(20).setKt(0.1)),
                new Tube(new Ray(new Point(0, 0, -7000),new Vector(-0.01,-0.01,1)),4d).setEmission(new Color(BLUE)) //
                        .setMaterial(new Material().setKd(0.7).setKs(0.7).setnShininess(20).setKt(0.1)),
               sphere,sphere2,sphere3,sphere4,sphere5);

        scene1.lights.add(new SpotLight(spCL, spPL, new Vector(1, 1, -0.5)).setKl(0.001).setKq(0.0001));
        scene1.lights.add(new SpotLight(new Color(WHITE),new Point(0,0,1000),new Vector(0.1,0.1,-1) ));
        scene1.lights.add(_directionalLight);

        ImageWriter imageWriter = new ImageWriter("Cylinder depth test", 500, 500);
        camera1.setImageWriter(imageWriter) //
                .setRayTracer(new RayTracerBasic(scene1)) //
                .renderImage() ; //
        camera1.writeToImage();//
    }
}
