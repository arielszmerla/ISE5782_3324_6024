/**
 * 
 */
package renderer;

import org.junit.jupiter.api.Test;

import static java.awt.Color.*;

import lighting.*;
import geometries.*;
import primitives.*;
import renderer.scene.Scene;

import java.util.Random;


/**
 * Tests for reflection and transparency functionality, test for partial shadows
 * (with transparency)
 * 
 * @author dzilb
 */
public class ReflectionRefractionTests {
	private Scene scene = new Scene.SceneBuilder("Test scene").build();

	/**
	 * Produce a picture of a sphere lighted by a spot light
	 */
	@Test
	public void twoSpheres() {
		Camera camera = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
				.setVPSize(150, 150).setVPDistance(1000);

		scene._geometries.add( //
				new Sphere(new Point(0, 0, -50), 50d).setEmission(new Color(BLUE)) //
						.setMaterial(new Material().setKd(0.4).setKs(0.3).setnShininess(100).setKt(0.3).setKg(0.7)),
	            new Sphere(new Point(0, 0, -50), 25d).setEmission(new Color(RED)) //
						.setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(100)));
		scene._lights.add( //
				new SpotLight(new Color(1000, 600, 0), new Point(-100, -100, 500), new Vector(-1, -1, -2)) //
						.setKl(0.0004).setKq(0.0000006));

		camera.setImageWriter(new ImageWriter("refractionTwoSpheres", 500, 500)) //
				.setRayTracer(new RayTracerBasic(scene)) //
				.renderImage();
		camera.writeToImage();
	}

	/**
	 * Produce a picture of a sphere lighted by a spot light
	 */
	@Test
	public void twoSpheresOnMirrors() {
		Camera camera = new Camera(new Point(0, 0, 10000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
				.setVPSize(2500, 2500).setVPDistance(10000); //

		scene._ambientLight= new AmbientLight(new Color(255, 255, 255), new Double3(0.1));

		scene._geometries.add( //
				new Sphere(new Point(-950, -900, -1000), 400d).setEmission(new Color(0, 0, 100)) //
						.setMaterial(new Material().setKd(0.25).setKs(0.25).setnShininess(20).setKt(0.5)),
				new Sphere(new Point(-950, -900, -1000), 200d).setEmission(new Color(100, 20, 20)) //
						.setMaterial(new Material().setKd(0.25).setKs(0.25).setnShininess(20)),
				new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500), new Point(670, 670, 3000)) //
						.setEmission(new Color(20, 20, 20)) //
						.setMaterial(new Material().setKr(1).setKg(0.99)),
				new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500),
						new Point(-1500, -1500, -2000)) //
						.setEmission(new Color(20, 20, 20)) //
						.setMaterial(new Material().setKr(0.5).setKg(0.99)));

		scene._lights.add(new SpotLight(new Color(1020, 400, 400), new Point(-750, -750, -150), new Vector(-1, -1, -4)) //
				.setKl(0.00001).setKq(0.000005));

		ImageWriter imageWriter = new ImageWriter("reflectionTwoSpheresMirrored", 500, 500);
		camera.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene)) //
				.renderImage();
		camera.writeToImage();
	}

	/**
	 * Produce a picture of a two triangles lighted by a spot light with a partially
	 * transparent Sphere producing partial shadow
	 */
	@Test
	public void trianglesTransparentSphere() {
		Camera camera = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
				.setVPSize(200, 200).setVPDistance(1000);

		scene._ambientLight=new AmbientLight(new Color(WHITE), new Double3(0.15));

		scene._geometries.add( //
				new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135), new Point(75, 75, -150)) //
						.setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(60)), //
				new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140), new Point(75, 75, -150)) //
						.setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(60)), //
				new Sphere(new Point(60, 50, -50), 30d).setEmission(new Color(BLUE)) //
						.setMaterial(new Material().setKd(0.2).setKs(0.2).setnShininess(30).setKt(0.6).setKg(0.99)));

		scene._lights.add(new SpotLight(new Color(700, 400, 400), new Point(60, 50, 0), new Vector(0, 0, -1)) //
				.setKl(4E-5).setKq(2E-7));

		ImageWriter imageWriter = new ImageWriter("refractionShadow", 600, 600);
		camera.setImageWriter(imageWriter).setAntiAliasing(true) //
				.setRayTracer(new RayTracerBasic(scene)) //
				.renderImage2() ;
		camera.writeToImage();
	}
	/**
	 * Produce a picture of a two triangles lighted by a spot light with a partially
	 * transparent Sphere producing partial shadow
	 */
	@Test
	public void triangleCylinderTransparentSphere() {
		Camera camera = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
				.setVPSize(200, 200).setVPDistance(1000);

		scene._ambientLight=new AmbientLight(new Color(WHITE), new Double3(0.15));

		scene._geometries.add( //
				new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135), new Point(75, 75, -150)) //
						.setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(10)), //
				new Cylinder(new Ray(new Point(5, 5, -50),new Vector(50,-20,15)),5d,100d).setEmission(new Color(YELLOW)) //
						.setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(60).setKt(0.2)), //
				new Tube(new Ray(new Point(70, 60, -50),new Vector(-1,-11,40)),3d).setEmission(new Color(BLUE)) //
						.setMaterial(new Material().setKd(0.7).setKs(0.7).setnShininess(20).setKt(0.1)),
				new Tube(new Ray(new Point(70, 60, -50),new Vector(30,30,-60)),3d).setEmission(new Color(3,0,3)) //
						.setMaterial(new Material().setKd(0.5).setKs(0.9).setnShininess(20).setKt(0.8)),
				new Sphere(new Point(60, 20, -50), 30d).setEmission(new Color(BLUE)) //
						.setMaterial(new Material().setKd(0.2).setKs(0.2).setnShininess(30).setKt(0.6)));

		scene._lights.add(new SpotLight(new Color(700, 400, 400), new Point(60, 50, 0), new Vector(0, 0, -1),0.1) //
				.setKl(4E-5).setKq(2E-7));
		scene._lights.add(new PointLight(new Color(500, 200, 200),new Point(40,40,10)).setKq(2E-8).setKl(2E-4));

		ImageWriter imageWriter = new ImageWriter("refractionCylinderShadow", 600, 600);
		camera.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene)) //
				.renderImage() ;
		camera.writeToImage();
	}
	/**
	 * Produce a picture of a two triangles lighted by a spot light with a partially
	 * transparent Sphere producing partial shadow
	 */
	@Test
	public void trianglesTransparentSpheres() {
		Camera camera = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
				.setVPSize(200, 200).setVPDistance(1000);

		scene._ambientLight=new AmbientLight(new Color(WHITE), new Double3(0.15));

		scene._geometries.add( //
				new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135), new Point(75, 75, -150)) //
						.setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(60)), //
				new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140), new Point(75, 75, -150)) //
						.setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(60)), //
				new Sphere(new Point(40, 40, -50), 45d).setEmission(new Color(BLUE)) //
						.setMaterial(new Material().setKd(0.1).setKs(0.1).setnShininess(30).setKt(0.1)),
		new Sphere(new Point(28, 0, -50), 10d).setEmission(new Color(YELLOW)) //
				.setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(100).setKt(0.6)),
		new Sphere(new Point(52, 0, -50), 10d).setEmission(new Color(GREEN)) //
				.setMaterial(new Material().setKd(0.2).setKs(0.2).setnShininess(30).setKt(0.6)));

		scene._lights.add(new SpotLight(new Color(700, 400, 400), new Point(60, 50, 0), new Vector(0, 0, -1)) //
				.setKl(4E-5).setKq(2E-7));
		scene._lights.add(new PointLight(new Color(700, 400, 400), new Point(10, 10, 10)) //
				.setKl(4E-5).setKq(2E-7));

		ImageWriter imageWriter = new ImageWriter("refractionSpheresShadow", 600, 600);
		camera.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene)).setAntiAliasing(false) //
				.renderImage2() ;
		camera.writeToImage();
	}



	/**
	 * Produce a picture of a sphere lighted by a spot light
	 */
	@Test
	public void twoSpheresOnMirr() {
		Camera camera = new Camera(new Point(0, 0, 10000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
				.setVPSize(2500, 2500).setVPDistance(10000); //

		scene._ambientLight= new AmbientLight(new Color(255, 255, 255), new Double3(0.1));
		Cube myCube=new Cube(100, new Point(-230, -230, -115), new Point(-180, -230, -115),
				new Point(-180, -180, -115), new Point(-230, -180, -115));
		Random r= new Random();
		myCube.setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(30));
		for (Polygon  p: myCube.getPolygons()) {
			p.setMaterial(new Material().setKd(0.5).setKs(0.5).setnShininess(30)).setEmission(new Color(r.nextInt(255),r.nextInt(255),r.nextInt(255))).setEmission(new Color(BLUE));
		}
		scene._geometries.add(myCube,new Sphere(new Point(-130 , -130, -115),500).setMaterial(new Material().setKd(0.5).setKs(0.5).setKt(0.5).setnShininess(30))); //
				new Polygon(new Point(-200,-200,-100),new Point(200,-200,-100), new Point(200,-200,-230),new Point(-200, -200,-200)).setEmission(new Color(BLUE)).setMaterial(new Material().setKd(1).setKs(1).setnShininess(30));

		scene._lights.add(new SpotLight(new Color(1020, 400, 400), new Point(-750, -750, -150), new Vector(-1, -1, -4))//
				.setKl(0.00001).setKq(0.000005)); scene._lights.add(new PointLight(new Color(1020, 400, 400), new Point(-750, -750, -150)) //
				.setKl(0.00001).setKq(0.000005));

		ImageWriter imageWriter = new ImageWriter("reflec", 500, 500);
		camera.setImageWriter(imageWriter) //
				.setRayTracer(new RayTracerBasic(scene)) //
				.renderImage();
		camera.writeToImage();
	}
}
