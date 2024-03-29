package renderer;

import org.junit.jupiter.api.Test;

import lighting.*;
import geometries.*;
import primitives.*;
import renderer.scene.Scene;

import java.util.Random;

import static java.awt.Color.*;

/**
 * Test rendering a basic image
 * 
 * @author Dan
 */
public class LightsTests {
	private Scene scene1 = new Scene.SceneBuilder("Test scene").build();
	private Scene scene2 = new Scene.SceneBuilder("Test scene") //
			.setAmbientLight(new AmbientLight(new Color(WHITE), new Double3(0.15))).build();
	private Camera camera1 = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
			.setVPSize(150, 150) //
			.setVPDistance(1000);
	private Camera camera2 = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
			.setVPSize(200, 200) //
			.setVPDistance(1000);

	private Point[] p = { // The Triangles' vertices:
			new Point(-110, -110, -150), // the shared left-bottom
			new Point(95, 100, -150), // the shared right-top
			new Point(110, -110, -150), // the right-bottom
			new Point(-75, 85, 0), // the left-top
	new Point(-75,100,-150)};

	private Point trPL = new Point(50, 30, -100); // Triangles test Position of Light
	private Point spPL = new Point(-50, -50, 25); // Sphere test Position of Light
	private Color trCL = new Color(800, 500, 250); // Triangles test Color of Light
	private Color spCL = new Color(800, 500, 0); // Sphere test Color of Light
	private Vector trDL = new Vector(-2, 2, -2); // Triangles test Direction of Light
	private Material material = new Material().setKd(0.5).setKs(0.5).setnShininess(300).setKg(0.9);
	private Geometry triangle1 = new Triangle(p[0], p[1], p[2]).setMaterial(material);
	private Geometry triangle2 = new Triangle(p[0], p[1], p[3]).setMaterial(material);
	private Geometry sphere = new Sphere(new Point(0, 0, -50), 50d) //
			.setEmission(new Color(BLUE).reduce(2)) //
			.setMaterial(material);
	private Geometry sphere2 = new Sphere(new Point(4, 8, -20), 20d) //
			.setEmission(new Color(YELLOW).reduce(2)) //
			.setMaterial(material);
	/**
	 * Produce a picture of a sphere lighted by a directional light
	 */
	@Test
	public void sphereDirectional() {
		scene1._geometries.add(sphere);
		scene1._lights.add(new DirectionalLight(spCL, new Vector(1, 1, -0.5)));

		ImageWriter imageWriter = new ImageWriter("lightSphereDirectional", 500, 500);
		camera1.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene1)) //
				.renderImage(); //
		camera1.writeToImage();//
	}

	/**
	 * Produce a picture of a sphere lighted by a point light
	 */
	@Test
	public void spherePoint() {
		scene1._geometries.add(sphere);
		scene1._lights.add(new PointLight(spCL, spPL).setKl(0.001).setKq(0.0002));

		ImageWriter imageWriter = new ImageWriter("lightSpherePoint", 500, 500);
		camera1.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene1)) //
				.renderImage(); //
		camera1.writeToImage();
	}

	/**
	 * Produce a picture of a sphere lighted by a spot light
	 */
	@Test
	public void sphereSpot() {
		scene1._geometries.add(sphere);
		scene1._geometries.add(new Sphere(new Point(-20, -20, 400), 20d) .setMaterial(material)//
				.setEmission(new Color(RED).reduce(2)));
		scene1._geometries.add(new Sphere(new Point(60, 30, -600), 30d) .setMaterial(material)//
				.setEmission(new Color(MAGENTA).reduce(2)));

		scene1._lights.add(new SpotLight(spCL, spPL, new Vector(1, 1, -0.5)).setKl(0.001).setKq(0.0001));

		ImageWriter imageWriter = new ImageWriter("lightSphereSpot", 500, 500);
		camera1.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene1)) //
				.renderImage() ; //
		camera1.writeToImage();//
	}

	/**
	 * Produce a picture of a two triangles lighted by a directional light
	 */
	@Test
	public void trianglesDirectional() {
		scene2._geometries.add(triangle1, triangle2);
		scene2._lights.add(new DirectionalLight(trCL, trDL));

		ImageWriter imageWriter = new ImageWriter("lightTrianglesDirectional", 500, 500);
		camera2.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene2)) //
				.renderImage() ; //
		camera2.writeToImage();
	}

	/**
	 * Produce a picture of a two triangles lighted by a point light
	 */
	@Test
	public void trianglesPoint() {
		scene2._geometries.add(triangle1, triangle2);
		scene2._lights.add(new PointLight(trCL, trPL).setKl(0.001).setKq(0.0002));

		ImageWriter imageWriter = new ImageWriter("lightTrianglesPoint", 500, 500);
		camera2.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene2)) //
				.renderImage() ; //
		camera2.writeToImage();
	}

	/**
	 * Produce a picture of a two triangles lighted by a spot light
	 */
	@Test
	public void trianglesSpot() {
		scene2._geometries.add(triangle1, triangle2);
		scene2._lights.add(new SpotLight(trCL, trPL, trDL).setKl(0.001).setKq(0.0001));

		ImageWriter imageWriter = new ImageWriter("lightTrianglesSpot", 500, 500);
		camera2.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene2)) //
				.renderImage() ; //
		camera2.writeToImage();
	}
	/**
	 * Produce a picture of two triangles lighted by a spotlight
	 */
	@Test
	public void trianglesSpotShortened() {
		scene2._geometries.add(triangle1, triangle2);
		scene2._lights.add(new SpotLight(trCL, trPL, trDL,0.5).setKl(0.001).setKq(0.0001));

		ImageWriter imageWriter = new ImageWriter("lightTrianglesSpotShortened", 500, 500);
		camera2.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene2)) //
				.renderImage() ; //
		camera2.writeToImage();
	}
	/**
	 * Produce a picture of a sphere lighted by a spot light
	 */
	@Test
	public void sphereSpotShortened() {
		scene1._geometries.add(sphere);
		scene1._lights.add(new SpotLight(spCL, spPL, new Vector(1, 1, -0.5),0.1).setKl(0.001).setKq(0.0001));

		ImageWriter imageWriter = new ImageWriter("lightSphereSpotShortened", 500, 500);
		camera1.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene1)) //
				.renderImage() ; //
		camera1.writeToImage();//
	}
	/**
	 * Produce a picture of a sphere lighted by a spot light
	 */
	@Test
	public void mySphereSpotShortened() {
		scene1._geometries.add(sphere,sphere2);
		scene1._lights.add(new SpotLight(spCL, spPL, new Vector(1, 1, -0.5),0.1).setKl(0.001).setKq(0.0001));
		scene1._lights.add(new PointLight(new Color (555,0,500), new Point(30, 60, 100)).setKl(0.002).setKq(0.0002));
		ImageWriter imageWriter = new ImageWriter("myLightSphereSpotShortened", 500, 500);
		camera1.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene1)) //
				.renderImage() ; //
		camera1.writeToImage();//
	}
	private Point[] square = { // The Triangles' vertices:
			new Point(-98, -110, -145), // the shared left-bottom
			new Point(97, 100, -145), // the shared right-top
			new Point(110, -110, -145), // the right-bottom
			new Point(-88,100,-145)}; // the left-top
	private  Point[] leftBlueStrip ={
			new Point(-70, -110, -140), // the shared left-bottom
			new Point(-55, 100, -140), // the shared right-top
			new Point(-35, 100, -140), // the left-top
			new Point(-50, -110, -140), // the right-bottom

	};
	private  Point[] rightBlueStrip={
			new Point(90, -110, -140), // the shared left-bottom
			new Point(55, 100, -140), // the shared right-top
			new Point(70, -110, -140), // the right-bottom
			new Point(72, 100, -140), // the left-top
	};
	private  Point[] downTriangle={
			new Point(-30, 42, -140), // the  left-top
			new Point(10, -46, -140), // the bottom
			new Point(50, 42, -140), // the right-top
	};
	private  Point[] topTriangle={
			new Point(-32, -21, -140), // the  left-bottom
			new Point(10, 71, -140), // the top
			new Point(53, -21, -140), // the right-bottom
	};
	private  Point[] internalPolygon={
			new Point(3, -17, -139.99), // the bottom left
			new Point(17, -17, -139.99), // the bottom right
			new Point(36, 14, -139.99), // the middle right
			new Point(17, 38, -139.99), // the top right
			new Point(3, 38, -139.99), // the top left
			new Point(-12, 14, -139.99), //  the middle left
	};
	private Geometry sphereLeft = new Sphere(new Point(-52, 95, -140), 24d) //
			.setEmission(new Color(YELLOW).reduce(2)) //
			.setMaterial(material);
	private Geometry sphereRight = new Sphere(new Point(57, 95, -140), 24d) //
			.setEmission(new Color(ORANGE).reduce(2)) //
			.setMaterial(material);
     private Geometry leftCylinder = new Cylinder(new Ray(new Point(-80, 5, -160),new Vector(0.3,1,0.42)),1.5d,100d).setEmission(new Color(black)) //
			.setMaterial(new Material().setKd(0.1).setKs(0.1).setnShininess(60).setKt(0.2));//
	private Geometry rightCylinder = new Cylinder(new Ray(new Point(67, 5, -150),new Vector(-0.1,1,0.32)),1.5d,100d).setEmission(new Color(black)) //
			.setMaterial(new Material().setKd(0.1).setKs(0.1).setnShininess(60).setKt(0.2));//
	private Scene scene3 = new Scene.SceneBuilder("Test scene") //
			.setAmbientLight(new AmbientLight(new Color(WHITE), new Double3(0.005))).build();
	private Camera camera3 = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
			.setVPSize(200, 200) //
			.setVPDistance(1000).setImageWriter( new ImageWriter("israel", 500 ,500));
	/**
	 * Produce a picture of a sphere lighted by a spot light
	 */
	@Test
	public void israelTest() {
		scene3._geometries.add(new Polygon(square[3],square[1],square[2],square[0]).setEmission(new Color(WHITE)).setMaterial(material.setKd(0.5).setKs(0.5).setnShininess(100))
				.setMaterial(material),new Polygon(leftBlueStrip[3], leftBlueStrip[2], leftBlueStrip[1], leftBlueStrip[0]).setEmission(new Color(BLUE).reduce(2)).setMaterial(material),
				new Polygon(rightBlueStrip[3],rightBlueStrip[1],rightBlueStrip[2],rightBlueStrip[0]).setEmission(new Color(BLUE).reduce(2)).setMaterial(material),
				new Triangle(downTriangle[0],downTriangle[1],downTriangle[2]).setEmission(new Color(BLUE).reduce(2)).setMaterial(material),
				new Polygon(internalPolygon[0],internalPolygon[1],internalPolygon[2],internalPolygon[3],internalPolygon[4],internalPolygon[5]).setEmission(new Color(white).reduce(2)).setMaterial(material.setKr(0.5)),
				new Triangle(topTriangle[0],topTriangle[1],topTriangle[2]).setEmission(new Color(BLUE).reduce(2)).setMaterial(material),
		sphereLeft,sphereRight,leftCylinder,rightCylinder);
		scene3._lights.add(new PointLight(new Color(WHITE), trDL));
		scene3._lights.add(new SpotLight(trCL, trPL, trDL,0.5).setKl(0.001).setKq(0.001));
		ImageWriter imageWriter = new ImageWriter("israelFlag", 500, 500);
		camera3.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene3)) //
				.renderImage() ; //
		camera3.writeToImage();//
		imageWriter = new ImageWriter("israelFlagRightMoveRotateLeft", 500, 500);
		camera3.moveCamera(new Vector(1100,0,-300));
		camera3.rotateCamera(new Vector(0,1,0),50);
		camera3.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene3)) //
				.renderImage() ; //
		camera3.writeToImage();//
	}



	/**
	 * Produce a picture of snow man
	 */
	@Test
	public void snowMan() {
		Sphere sphere1 = (Sphere) new Sphere(new Point(0,-90,-500), 60)
				.setMaterial(new Material().setKd(0.8).setKs(0.2).setKt(0.2).setnShininess(30));
		sphere1.setEmission(new Color(DARK_GRAY));
		Sphere sphere2 = (Sphere) new Sphere(new Point(0,0,-500), 50)
				.setMaterial(new Material().setKd(0.8).setKs(0.2).setKt(0.2).setnShininess(30));
		sphere2.setEmission(new Color(DARK_GRAY));
		Sphere sphere3 = new Sphere(new Point(0,70,-500), 40);
		sphere3.setEmission(new Color(DARK_GRAY))
				.setMaterial(new Material().setKd(0.8).setKs(0.2).setKt(0.2).setnShininess(30));
		Sphere sphere4 = new Sphere(new Point(-12,80,-463), 3);
		sphere4.setEmission(new Color(0,100,0));
		Sphere sphere5 = new Sphere(new Point(12,80,-463), 3);
		sphere5.setEmission(new Color(0,100,0));
		Sphere sphere6 = (Sphere) new Sphere(new Point(0,0,-450), 4).setEmission(new Color(BLACK));
		Sphere sphere7 = (Sphere) new Sphere(new Point(0,12,-452), 4).setEmission(new Color(BLACK));
		Sphere sphere8 = (Sphere) new Sphere(new Point(0,-12,-452), 4).setEmission(new Color(BLACK));
		Triangle triangle1 = (Triangle) new Triangle(
				new Point(-3,70,-459),
				new Point(3,70,-459),
				new Point(0,60,-440)
		).setEmission(new Color(238,125,0));
		Cylinder cylinder1 = (Cylinder) new Cylinder(new Ray(new Point(35,9,-500),new Vector(1,0.7d,1)), 3, 40)
				.setEmission(new Color(50,16,0));
		Cylinder cylinder2 = (Cylinder) new Cylinder(new Ray(new Point(-35,9,-500),new Vector(-1,0.7d,-1)), 3, 40)
				.setEmission(new Color(50,16,0));
		Cylinder cylinder3 = (Cylinder) new Cylinder(new Ray(new Point(90,11,-500),new Vector(-1,0.7d,-1)), 3, 40)
				.setEmission(new Color(50,16,0));
		Cylinder cylinder4 = (Cylinder) new Cylinder(new Ray(new Point(-85,8,-500),new Vector(1,0.7d,1)), 3, 40)
				.setEmission(new Color(50,16,0));

		Triangle polygon1 = (Triangle) new Triangle(
				new Point(-200,-200,-560),
				new Point(200,-200,-560),
				new Point(200,200,-560)
		).setEmission(new Color(DARK_GRAY)).setMaterial(new Material().setKs(0.8).setnShininess(60))//
				;

		Triangle polygon2 = (Triangle) new Triangle(
				new Point(-200,-200,-560),
				new Point(-200,200,-560),
				new Point(200,200,-560)
		).setEmission(new Color(DARK_GRAY)).setMaterial(new Material().setKs(0.8).setnShininess(60))//
				;


		Polygon pl1 = (Polygon) new Polygon(
				new Point(-30,-65,-440),
				new Point(30,-65,-440),
				new Point(30,-60,-440),
				new Point(-30,-60,-440))
				.setEmission(new Color(BLUE));

		Polygon pl2 = (Polygon) new Polygon(
				new Point(-30,-105,-440),
				new Point(30,-105,-440),
				new Point(30,-100,-440),
				new Point(-30,-100,-440))
				.setEmission(new Color(BLUE));

		Triangle tr1 = (Triangle) new Triangle(
				new Point(0,-68,-440),
				new Point(-15,-90,-440),
				new Point(15,-90,-440)
		).setEmission(new Color(BLUE))
				.setMaterial(new Material().setKs(0.8).setnShininess(60));

		Triangle tr2 = (Triangle) new Triangle(
				new Point(0,-97,-440),
				new Point(-15,-75,-440),
				new Point(15,-75,-440)
		).setEmission(new Color(BLUE))
				.setMaterial(new Material().setKs(0.8).setnShininess(60));
		;



		scene2._geometries.add(sphere1, sphere2, sphere3, sphere4, sphere5, triangle1, cylinder1, cylinder2, cylinder3,
				cylinder4, polygon1, polygon2, sphere6, sphere7, sphere8,
				pl1, pl2, tr1, tr2
		);
		//	scene2.lights.add( //
		//			new SpotLight(new Color(WHITE), new Point(0, 100, -450), new Vector(0, 0, -1)) //
		//					.setKl(4E-4).setKq(2E-5));



		scene2._lights.add( //
				new SpotLight(new Color(WHITE), new Point(0, -85, -380), new Vector(0, 0, -2)) //
						.setKl(0.0004).setKq(0.0000006));
		scene2._lights.add( //
				new SpotLight(new Color(LIGHT_GRAY), new Point(-200, 0, 0), new Vector(1, 1, -2)) //
						.setKl(0.0004).setKq(0.0000006));
		scene2._lights.add( //
				new PointLight(new Color(GRAY), new Point(-50, 0, 0)));
		scene2._lights.add( //
				new PointLight(new Color(GRAY), new Point(50, 0, 0)));

		scene2._background = new Color(89,108,143);
		ImageWriter imageWriter = new ImageWriter("snowMan", 500, 500);
		camera2.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene2)) //
				.renderImage() ; //
		camera2.writeToImage();
	}

	/**
	 * Produce a picture of snow man
	 */
	@Test
	public void garden() {

		Cylinder cylinder1 = (Cylinder) new Cylinder(new Ray(new Point(0,-100,-50), new Vector(0,1,0)), 10, 100)
				.setEmission(new Color(50,16,10))
						.setMaterial(new Material().setKd(0.9).setKs(0.1));

		Polygon polygon = (Polygon) new Polygon(
				new Point(-110,-110, -50),
				new Point(110,-110, -50),
				new Point(110,110, -50),
				new Point(-110,110, -50)
		).setEmission(new Color(186, 255, 255))
				.setMaterial(new Material().setKd(0.9).setKs(0.1));
		scene2._geometries.add(cylinder1, polygon);
		for(int i = 0; i<200; i++){
			Random rand = new Random();
			Point p1 = new Point(rand.nextInt(100) - 50, rand.nextInt(100) - 15 , 0 );
			scene2._geometries.add(new Sphere(p1, rand.nextInt(20)+1).setEmission(new Color(GREEN))
					.setMaterial(new Material().setKs(0.001).setKd(0.9)));
		}

		for(int i = 0; i<50; i++){
			Random rand = new Random();
			Point p1 = new Point(rand.nextInt(90) - 50, rand.nextInt(90) - 15 , 20 );
			scene2._geometries.add(new Sphere(p1, 2).setEmission(new Color(RED)));
		}
		scene2._lights.add( //
				new SpotLight(new Color(WHITE), new Point(0, 500, 700), new Vector(0, -400, -1)) //
						.setKl(0.0004).setKq(0.0000006));
/*
		scene2.lights.add( //
				new SpotLight(new Color(WHITE), new Point(-200, 0, 0), new Vector(1, 1, -2)) //
						.setKl(0.0004).setKq(0.0000006));
		scene2.lights.add( //
				new PointLight(new Color(WHITE), new Point(0, 0, 10000)));
						*/

		scene2._background = new Color(89,108,143);
		ImageWriter imageWriter = new ImageWriter("garden", 500, 500);
		camera2.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene2)) //
				.renderImage() ; //
		camera2.writeToImage();
	}
}
