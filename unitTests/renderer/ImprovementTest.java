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


public class ImprovementTest {
    private Scene scene1 = new Scene.SceneBuilder("Test scene").build();
    private Camera camera1 = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
            .setVPSize(150, 150) //
            .setVPDistance(1000);
    private DirectionalLight _directionalLight = new DirectionalLight(new Color(200, 200, 200), new Vector(0.1, -0.1, 1));
    private DirectionalLight _directionalLight1 = new DirectionalLight(new Color(200, 200, 200), new Vector(0.1, -0.2, 0));
    private Material material = new Material().setKd(0.5).setKs(0.5).setnShininess(300).setKt(0.5).setKr(0.1).setKg(0.3);
    private Material material3 = new Material().setKd(0).setKs(0).setnShininess(0).setKt(0);
    private Geometry backBone = new Polygon(new Point(-1000, 1000, -9000),
            new Point(1000, 1000, -9000),
            new Point(1000, -1000, -3500),
            new Point(-1000, -1000, -3500)).setEmission(new Color(24, 100, 100).reduce(2)) //
            .setMaterial(material3);
    private Geometry sphere = new Sphere(new Point(0, 0, -6000), 50d) //
            .setEmission(new Color(MAGENTA).reduce(2)) //
            .setMaterial(material);
    private Material material2 = material.setKt(0.7);
    private Geometry sphere2 = new Sphere(new Point(50, 20, -200), 25d) //
            .setEmission(new Color(RED).reduce(4)) //
            .setMaterial(material2);
    private Geometry sphere3 = new Sphere(new Point(50, 20, -1200), 40d) //
            .setEmission(new Color(RED).reduce(4)) //
            .setMaterial(material3);
    private Geometry sphere4 = new Sphere(new Point(-50, 20, 0), 20d) //
            .setEmission(new Color(YELLOW).reduce(100)) //
            .setMaterial(material);
    private Geometry sphere5 = new Sphere(new Point(-50, -20, 200), 30d) //
            .setEmission(new Color(YELLOW).reduce(100)) //
            .setMaterial(material);
    private Point spPL = new Point(-50, -50, 25); // Sphere test Position of Light
    private Color spCL = new Color(800, 500, 0); // Sphere test Color of Light
    @Test
    // A test for the scene of the spheres.
    public void sphereSpot() {
        scene1._geometries.add(new Tube(new Ray(new Point(0, 0, -7000), new Vector(0.01, 0.01, 1)), 4d).setEmission(new Color(BLUE)) //
                        .setMaterial(new Material().setKd(0.7).setKs(0.7).setnShininess(20).setKt(0.1)),
                new Tube(new Ray(new Point(0, 0, -7000), new Vector(-0.01, 0.01, 1)), 4d).setEmission(new Color(BLUE)) //
                        .setMaterial(new Material().setKd(0.7).setKs(0.7).setnShininess(20).setKt(0.1)),
                new Tube(new Ray(new Point(0, 0, -7000), new Vector(0.01, -0.01, 1)), 4d).setEmission(new Color(BLUE)) //
                        .setMaterial(new Material().setKd(0.7).setKs(0.7).setnShininess(20).setKt(0.1)),
                new Tube(new Ray(new Point(0, 0, -7000), new Vector(-0.01, -0.01, 1)), 4d).setEmission(new Color(BLUE)) //
                        .setMaterial(new Material().setKd(0.7).setKs(0.7).setnShininess(20).setKt(0.1)),
                sphere, sphere2, sphere3, sphere4, sphere5, backBone);

        scene1._lights.add(new SpotLight(spCL, spPL, new Vector(1, 1, -0.5)).setKl(0.001).setKq(0.0001));
        scene1._lights.add(new SpotLight(new Color(WHITE), new Point(0, 0, 1000), new Vector(0.1, 0.1, -1)));
        scene1._lights.add(_directionalLight);
        scene1._lights.add(_directionalLight1);
        scene1._lights.add(new PointLight(spCL, spPL).setKl(0.001).setKq(0.0002));
        ImageWriter imageWriter = new ImageWriter("Cylinder depth test", 500, 500);
        camera1.setImageWriter(imageWriter) //
                .setRayTracer(new RayTracerBasic(scene1)) //
                .renderImage(); //
        camera1.writeToImage();//
    }


    @Test
    // A test for the table scene.
    public void table() {
        Random rand = new Random();
        Color mirrorColor = new Color(100,100,100);
        Color tableColor = Color.BLACK;
        //Color lightColor = new Color(255,250,205);
        Color lightColor = new Color(255,252,50);
        Material tableMaterial = new Material().setnShininess(40).setKs(0.5).setKd(0.5).setKg(1);
        Table table1 = new Table(150, 80d, new Point(0, 0, -200), tableColor, tableMaterial);
        scene1._geometries.addAll(table1._parts);
        Point center = new Point(-75,20,-100);
        for(int i=0;i<5;i++) {
            for (int j = 0; j < 5; j++) {
                Sphere sphere = (Sphere) new Sphere(center.add(new Vector(40 * (j + 0.001), 0, -40 * (i + 0.001))), 10d)
                        .setMaterial(new Material().setKs(0.5).setKd(0.5).setKg(1));
                sphere.setEmission(new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
                scene1._geometries.add(sphere);

            }
        }


        scene1._geometries.add(
                new Sphere(new Point(-80, 150, -200), 15).setEmission(lightColor) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(20).setKt(0.5).setKg(1)),
                new Sphere(new Point(0, 150, -200), 15).setEmission(lightColor) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(20).setKt(0.5).setKg(1)),
                new Sphere(new Point(80, 150, -200), 15).setEmission(lightColor) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(20).setKt(0.5).setKg(1)),
                new Cylinder(new Ray(new Point(-80, 165, -200), new Vector(0, 1, 0)), 1d, 50)
                        .setEmission(new Color(20,20,20)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(20).setKt(0.8).setKg(1)),
                new Cylinder(new Ray(new Point(0, 165, -200), new Vector(0, 1, 0)), 1d, 50)
                        .setEmission(new Color(20,20,20)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(20).setKt(0.5).setKg(1)),
                new Cylinder(new Ray(new Point(80, 165, -200), new Vector(0, 1, 0)), 1d, 50)
                        .setEmission(new Color(20,20,20)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(20).setKt(0.5).setKg(1)),
                new Cylinder(new Ray(new Point(-80, 215, -200), new Vector(1, 0, 0)), 3d, 160)
                        .setEmission(new Color(20,20,20)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(20).setKt(0.5).setKg(1)));

/*
                new Cylinder(new Ray(new Point(0, 10, -145), new Vector(0, 1, 0)), 15d, 1d).setEmission(new Color(GRAY)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(20).setKt(0.3)),
                new Cylinder(new Ray(new Point(20, 10, -165), new Vector(0, 1, 0)), 3d, 10d).setEmission(new Color(GRAY)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(20).setKt(0.3)),
                new Polygon(new Point(20, 10.01, -160), new Point(18, 10.01, -150), new Point(20, 10.01, -125)).setEmission(new Color(BLACK))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(20).setKt(0.01)),
                new Polygon(new Point(-19.5, 10.01, -160), new Point(-19.5, 10.01, -145), new Point(-20, 10.01, -150), new Point(-20, 10.01, -160)).setEmission(new Color(BLACK))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(20).setKt(0.01)),
                new Polygon(new Point(-20.5, 10.01, -160), new Point(-20.5, 10.01, -150), new Point(-21, 10.01, -150), new Point(-21, 10.01, -160)).setEmission(new Color(BLACK))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(20).setKt(0.01)),
                new Polygon(new Point(-21.5, 10.01, -160), new Point(-21.5, 10.01, -150), new Point(-22, 10.01, -150), new Point(-22, 10.01, -160)).setEmission(new Color(BLACK))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(20).setKt(0.01)),
                new Polygon(new Point(-19.5, 10.01, -148), new Point(-19.5, 10.01, -150), new Point(-22, 10.01, -150), new Point(-22, 10.01, -148)).setEmission(new Color(BLACK))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(20).setKt(0.01)),
                new Polygon(new Point(-20, 10.01, -148), new Point(-20, 10.01, -130), new Point(-21.5, 10.01, -130), new Point(-21.5, 10.01, -148)).setEmission(new Color(BLACK))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(20).setKt(0.01))
        );*/

        // left
        Point w1 = new Point(-260, -80, 260);
        Point w2 = new Point(-260, -80, -600);
        Point w3 = new Point(-250, -80, -600);
        Point w4 = new Point(-250, -80, 260);
        scene1._geometries.addAll(new Wall(w1, w2, w3, w4, 295, new Color(WHITE), new Material().setKd(0.7))._parts);

        //back
        Point w11 = new Point(260, -80, -600);
        Point w22 = new Point(-260, -80, -600);
        Point w33 = new Point(-260, -80, -590);
        Point w44 = new Point(260, -80, -590);
        scene1._geometries.addAll(new Wall(w11, w22, w33, w44, 295, new Color(WHITE), new Material().setKd(0.7))._parts);

        //front
        Point wf51 = new Point(260, -80, 260);
        Point wf52 = new Point(-260, -80, 260);
        Point wf53 = new Point(-260, 215, 260);
        Point wf54 = new Point(260, 215, 260);
        scene1._geometries.add(new Polygon(wf51,wf52,wf53,wf54).setEmission(new Color(BLACK))
                .setMaterial(new Material().setKd(0.2).setKs(0.2).setnShininess(30).setKt(0.9).setKg(1)));

        //mirror
        Point m1 = new Point(200, -20, -590);
        Point m2 = new Point(-200, -20, -590);
        Point m3 = new Point(-200, -20, -588);
        Point m4 = new Point(200, -20, -588);
        scene1._geometries.addAll(new Wall(m1, m2, m3, m4, 130, mirrorColor, new Material().setKr(1).setKg(1))._parts);


        //right
        Point wrb1 = new Point(260, -80, 260);
        Point wrb2 = new Point(260, -80, -600);
        Point wrb3 = new Point(250, -80, -600);
        Point wrb4 = new Point(250, -80, 260);
        scene1._geometries.addAll(new Wall(wrb1, wrb2, wrb3, wrb4, 130, new Color(WHITE), new Material().setKd(0.7))._parts);

        Point wrr1 = new Point(260, 50, -500);
        Point wrr2 = new Point(260, 50, -600);
        Point wrr3 = new Point(250, 50, -600);
        Point wrr4 = new Point(250, 50, -500);
        scene1._geometries.addAll(new Wall(wrr1, wrr2, wrr3, wrr4, 165, new Color(WHITE), new Material().setKd(0.7))._parts);

        Point wrl1 = new Point(260, 50, 260);
        Point wrl2 = new Point(260, 50, 160);
        Point wrl3 = new Point(250, 50, 160);
        Point wrl4 = new Point(250, 50, 260);
        scene1._geometries.addAll(new Wall(wrl1, wrl2, wrl3, wrl4, 165, new Color(WHITE), new Material().setKd(0.7))._parts);

        Point wrt1 = new Point(260, 180, 160);
        Point wrt2 = new Point(260, 180, -500);
        Point wrt3 = new Point(250, 180, -500);
        Point wrt4 = new Point(250, 180, 160);
        scene1._geometries.addAll(new Wall(wrt1, wrt2, wrt3, wrt4, 35, new Color(WHITE), new Material().setKd(0.7))._parts);

        //window
        Point wrw1 = new Point(250, 50, 160);
        Point wrw2 = new Point(250, 50, -500);
        Point wrw3 = new Point(250, 180, -500);
        Point wrw4 = new Point(250, 180, 160);
        scene1._geometries.add(new Polygon(wrw1,wrw2,wrw3,wrw4).setEmission(new Color(BLACK))
                .setMaterial(new Material().setKd(0.2).setKs(0.2).setnShininess(30).setKt(0.9).setKg(1)));

        //up
        Point w1111 = new Point(260, 215, -600);
        Point w2222 = new Point(-260, 215, -600);
        Point w3333 = new Point(-260, 215, 260);
        Point w4444 = new Point(260, 215, 260);
        scene1._geometries.addAll(new Wall(w1111, w2222, w3333, w4444, 20, new Color(WHITE), new Material().setKd(0.7))._parts);

        //ground
        Point leftBack = new Point(-300, -80, 260);
        Material tileMaterial = new Material().setnShininess(20).setKd(0.7).setKs(0.3).setKr(0.7).setKg(1);

        for (int i = 1; i < 18; i++) {
            leftBack = leftBack.add(new Vector(0, 0, -50));
            for (int j = 1; j < 11; j++) {
                Tile tile;
                if (Math.abs(j - i) % 2 == 0) {
                    tile = new Tile(leftBack.add(new Vector(50 * j, 0, 0)),50, new Color(134,125,140),tileMaterial);
                } else {
                    tile = new Tile(leftBack.add(new Vector(50 * j, 0, 0)), 50, new Color(BLACK), tileMaterial);
                }
                scene1._geometries.add(tile._pol);
            }
        }



        scene1._lights.add(new SpotLight(new Color(WHITE), new Point(-80, 150, -200), new Vector(0, -1, 0)).set_shorten(0.1));
        scene1._lights.add(new SpotLight(new Color(WHITE), new Point(0, 150, -200), new Vector(0, -1, 0)).set_shorten(0.1));
        scene1._lights.add(new SpotLight(new Color(WHITE), new Point(80, 150, -200), new Vector(0, -1, 0)).set_shorten(0.1));
        scene1._lights.add(new SpotLight(new Color(WHITE), new Point(400, 100, -170), new Vector(-1, 0, 0)).set_shorten(0.2));


        ImageWriter imageWriter = new ImageWriter("wall5", 500, 500);
        camera1.setFocusField(1800);
        Camera camera1 = new Camera(new Point(0, 200, 4500), new Vector(0, -0.0425, -0.999), new Vector(0, 0.999, -0.0425)) //
                .setVPSize(150, 150) //
                .setVPDistance(1000);
        camera1.moveCamera(new Vector(660, 0, 0));
        camera1.rotateCamera(new Vector(0, 0.999, -0.0425), 8);
        camera1.setImageWriter(imageWriter) //
                .setRayTracer(new RayTracerBasic(scene1)) //
                .setAntiAliasing(true).renderImage2(); //
        camera1.writeToImage();//


    }

}
