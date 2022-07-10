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
    private Color mirrorColor = new Color(155, 157, 167);

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
        camera1.setImageWriter(imageWriter).setDepthOfField(true).setNumOfRaysDepth(25).setApertureFieldRadius(1)//
                .setRayTracer(new RayTracerBasic(scene1)) //
                .renderImage2(); //
        camera1.writeToImage();//
    }

    @Test
    // A test for the table scene.
    public void table() {
        Color tableColor = new Color(new java.awt.Color(119, 79, 61));
        Material tableMaterial = new Material().setKd(0.25).setKs(0.25).setnShininess(20);
        Table table1 = new Table(100d, 80d, new Point(0, 0, -200), tableColor, tableMaterial);
//        //Table table2 = new Table(80d,80d,new Point(200, 0, -200) , tableColor, tableMaterial );
        //Table table3 = new Table(80d,80d,new Point(-200, 0, -200) , tableColor, tableMaterial );
        scene1._geometries.addAll(table1._parts);
        /*scene1._geometries.addAll(table2._parts);
        scene1._geometries.addAll(table3._parts);*/
        //Table table4 = new Table(80d,80d,new Point(0, 0, -500) , tableColor, tableMaterial );
        //Table table5 = new Table(80d,80d,new Point(200, 0, -400) , tableColor, tableMaterial );
        //Table table6 = new Table(80d,80d,new Point(-200, 0, -400) , tableColor, tableMaterial );
        //scene1._geometries.addAll(table4._parts);
        /*scene1._geometries.addAll(table5._parts);
        scene1._geometries.addAll(table6._parts);
        Table table7 = new Table(80d,80d,new Point(0, 0, 0) , tableColor, tableMaterial );
        Table table8 = new Table(80d,80d,new Point(200, 0, 0) , tableColor, tableMaterial );
        Table table9 = new Table(80d,80d,new Point(-200, 0, 0) , tableColor, tableMaterial );
        scene1._geometries.addAll(table7._parts);
        scene1._geometries.addAll(table8._parts);
        scene1._geometries.addAll(table9._parts);
        */

        scene1._geometries.add(
                new Sphere(new Point(-80, 150, -200), 15).setEmission(new Color(YELLOW)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(20).setKt(0.8).setKg(1)),
                new Sphere(new Point(0, 150, -200), 15).setEmission(new Color(YELLOW)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(20).setKt(0.8).setKg(1)),
                new Sphere(new Point(80, 150, -200), 15).setEmission(new Color(YELLOW)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(20).setKt(0.8).setKg(1)),
                new Cylinder(new Ray(new Point(-80, 165, -200), new Vector(0, 1, 0)), 1d, 50)
                        .setEmission(new Color(YELLOW)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(20).setKt(0.8).setKg(1)),
                new Cylinder(new Ray(new Point(0, 165, -200), new Vector(0, 1, 0)), 1d, 50)
                        .setEmission(new Color(YELLOW)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(20).setKt(0.5).setKg(1)),
                new Cylinder(new Ray(new Point(80, 165, -200), new Vector(0, 1, 0)), 1d, 50)
                        .setEmission(new Color(YELLOW)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(20).setKt(0.5).setKg(1)),
                new Cylinder(new Ray(new Point(-80, 215, -200), new Vector(1, 0, 0)), 3d, 160)
                        .setEmission(new Color(YELLOW)) //
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
//front1
        Point wf11 = new Point(-150, -80, 260);
        Point wf12 = new Point(-260, -80, 260);
        Point wf13 = new Point(-260, 215, 260);
        Point wf14 = new Point(-150, 215, 260);
        scene1._geometries.add(new Polygon(wf11,wf12,wf13,wf14).setEmission(new Color(BLACK))
                .setMaterial(new Material().setKd(0.2).setKs(0.2).setnShininess(30).setKt(0.9).setKg(0.2)));

        //front2
        Point wf21 = new Point(-50, -80, 260);
        Point wf22 = new Point(-150, -80, 260);
        Point wf23 = new Point(-150, 215, 260);
        Point wf24 = new Point(-50, 215, 260);
        scene1._geometries.add(new Polygon(wf21,wf22,wf23,wf24).setEmission(new Color(BLACK))
                .setMaterial(new Material().setKd(0.2).setKs(0.2).setnShininess(30).setKt(0.9).setKg(0.4)));

        //front3
        Point wf31 = new Point(50, -80, 260);
        Point wf32 = new Point(-50, -80, 260);
        Point wf33 = new Point(-50, 215, 260);
        Point wf34 = new Point(50, 215, 260);
        scene1._geometries.add(new Polygon(wf31,wf32,wf33,wf34).setEmission(new Color(BLACK))
                .setMaterial(new Material().setKd(0.2).setKs(0.2).setnShininess(30).setKt(0.9).setKg(0.6)));

        //front4
        Point wf41 = new Point(150, -80, 260);
        Point wf42 = new Point(50, -80, 260);
        Point wf43 = new Point(50, 215, 260);
        Point wf44 = new Point(150, 215, 260);
        scene1._geometries.add(new Polygon(wf41,wf42,wf43,wf44).setEmission(new Color(BLACK))
                .setMaterial(new Material().setKd(0.2).setKs(0.2).setnShininess(30).setKt(0.9).setKg(0.8)));

        //front5
        Point wf51 = new Point(260, -80, 260);
        Point wf52 = new Point(150, -80, 260);
        Point wf53 = new Point(150, 215, 260);
        Point wf54 = new Point(260, 215, 260);
        scene1._geometries.add(new Polygon(wf51,wf52,wf53,wf54).setEmission(new Color(BLACK))
                .setMaterial(new Material().setKd(0.2).setKs(0.2).setnShininess(30).setKt(0.9).setKg(1)));
        //mirror
        Point m1 = new Point(200, -20, -590);
        Point m2 = new Point(-200, -20, -590);
        Point m3 = new Point(-200, -20, -588);
        Point m4 = new Point(200, -20, -588);
        scene1._geometries.addAll(new Wall(m1, m2, m3, m4, 130, mirrorColor, new Material().setKr(1).setKg(0.99))._parts);


        //right
        Point w111 = new Point(260, -80, 260);
        Point w222 = new Point(260, -80, -600);
        Point w333 = new Point(250, -80, -600);
        Point w444 = new Point(250, -80, 260);
        scene1._geometries.addAll(new Wall(w111, w222, w333, w444, 295, new Color(WHITE), new Material().setKd(0.7))._parts);

        //up
        Point w1111 = new Point(260, 215, -600);
        Point w2222 = new Point(-260, 215, -600);
        Point w3333 = new Point(-260, 215, 260);
        Point w4444 = new Point(260, 215, 260);
        scene1._geometries.addAll(new Wall(w1111, w2222, w3333, w4444, 20, new Color(WHITE), new Material().setKd(0.7))._parts);


        Point leftBack = new Point(-300, -80, 260);
        Material redMaterial = new Material().setnShininess(30).setKd(0.5).setKs(0.5);
        Material blackMaterial = new Material().setnShininess(30).setKd(0.5).setKs(0.5);

        for (int i = 1; i < 18; i++) {
            leftBack = leftBack.add(new Vector(0, 0, -50));
            for (int j = 1; j < 11; j++) {
                Tile tile;
                if (Math.abs(j - i) % 2 == 0) {
                    tile = new Tile(leftBack.add(new Vector(50 * j, 0, 0)),50,new Color(WHITE),redMaterial);
                } else {
                    tile = new Tile(leftBack.add(new Vector(50 * j, 0, 0)), 50, new Color(BLACK), blackMaterial);
                }
                scene1._geometries.add(tile._pol);
            }
        }







            /*
            scene1._geometries.add( new Polygon(new Point(-160,-80,260),new Point(-160,-80,200-500),new Point(-160,300,200-500),new Point(-160,300,260))
                    .setEmission(new Color(PINK))
                    .setMaterial(new Material().setnShininess(30).setKd(0.5).setKs(0.5).setKr(0.8)));
            scene1._geometries.add( new Polygon(new Point(-160,-80,260-600),new Point(-160,-80,200-1200),new Point(-160,300,200-1200),new Point(-160,300,260-600))
                    .setEmission(new Color(PINK))
                    .setMaterial(new Material().setnShininess(30).setKd(0.5).setKs(0.5).setKr(0.8)));
            scene1._geometries.add( new Polygon(new Point(-160,-80,200-500),new Point(-160,-80,260-600),new Point(-160,100,260-600),new Point(-160,100,200-500))
                    .setEmission(new Color(PINK))
                    .setMaterial(new Material().setnShininess(30).setKd(0.5).setKs(0.5).setKr(0.8)));
            scene1._geometries.add( new Polygon(new Point(-160,180,200-500),new Point(-160,180,260-600),new Point(-160,300,260-600),new Point(-160,300,200-500))
                    .setEmission(new Color(PINK))
                    .setMaterial(new Material().setnShininess(80).setKd(0.5).setKs(0.8).setKr(0.5)));
            scene1._lights.add(new PointLight(new Color(YELLOW).reduce(10), new Point(-500, 330 , -400)));
            scene1._geometries.add( new Polygon(new Point(-160+315,-80,260),new Point(-160+315,-80,200-1200),new Point(-160+315,300,200-1200),new Point(-160+315,300,260))
                    .setEmission(new Color(PINK))
                    .setMaterial(new Material().setnShininess(80).setKd(0.5).setKs(0.8).setKr(0.5)));

        }
*/
        scene1._lights.add(new SpotLight(new Color(WHITE), new Point(-80, 150, -200), new Vector(0, -1, 0)).set_shorten(0.2));
        scene1._lights.add(new SpotLight(new Color(WHITE), new Point(0, 150, -200), new Vector(0, -1, 0)).set_shorten(0.2)
                .setKl(0.00001).setKq(0.000005));
        scene1._lights.add(new SpotLight(new Color(WHITE), new Point(80, 150, -200), new Vector(0, -1, 0)).set_shorten(0.2));
        //scene1._lights.add(new SpotLight(new Color(WHITE), new Point(0,100,4000), new Vector(0,0,-1)));
        //scene1._lights.add(new SpotLight(new Color(WHITE), new Point(-400, -400, 0), new Vector(0.2d, 0.5d, -1)));
        //scene1.lights.add(new DirectionalLight(new Color(GREEN), new Vector(1,1,1)));
        //scene1.lights.add(_directionalLight);
        //scene1.lights.add(_directionalLight1);
        //scene1.lights.add(new PointLight(spCL, spPL).setKl(0.001).setKq(0.0002));
        ImageWriter imageWriter = new ImageWriter("wall3", 500, 500);
        camera1.setFocusField(1800);
        Camera camera1 = new Camera(new Point(0, 200, 4500), new Vector(0, -0.0425, -0.999), new Vector(0, 0.999, -0.0425)) //
                .setVPSize(150, 150) //
                .setVPDistance(1000);
        camera1.moveCamera(new Vector(660, 0, 0));
//camera1.rotateCamera(new Vector(1,0,0),-2);
        camera1.rotateCamera(new Vector(0, 0.999, -0.0425), 8);
        camera1.setImageWriter(imageWriter) //
                .setRayTracer(new RayTracerBasic(scene1)) //
                .setMultithreading(3)//
                .setDebugPrint()//
                .renderImage(); //

        camera1.writeToImage();//


    }

}
