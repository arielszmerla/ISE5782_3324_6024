package renderer;

import org.junit.jupiter.api.Test;

import lighting.AmbientLight;
import geometries.*;
import primitives.*;
import scene.Scene;


/**
 * Test rendering a basic image
 *
 * @author Dan
 */
public class RenderTests {

	/**
	 * Produce a scene with basic 3D model and render it into a png image with a
	 * grid
	 */



	/**
	 * Produce a scene with basic 3D model and render it into a jpeg image with a
	 * grid
	 */

	@Test
	public void basicRenderTwoColorTest() {
		Color redColor = new Color(100d, 0d, 0d);//red color
		Color yellowColor = new Color(255d, 255d, 0d);//yellow color
		Scene scene = new Scene.SceneBuilder("Test scene")//
			.setAmbientLight(new AmbientLight(redColor, //
						new Double3(1,1,1))) //
				.setBackground(yellowColor).build();

		scene._geometries.add(new Sphere( new Point(0, 0, -100),50),
				new Triangle(new Point(-100, 0, -100), new Point(0, 100, -100), new Point(-100, 100, -100)), // up
				// left
				new Triangle(new Point(-100, 0, -100), new Point(0, -100, -100), new Point(-100, -100, -100)), // down
				// left
				new Triangle(new Point(100, 0, -100), new Point(0, -100, -100), new Point(100, -100, -100))); // down

		//ImageWriter imageWriter = new ImageWriter("base render test", 1000, 1000);
		Camera camera = new Camera(Point.ZERO, new Vector(0, 0, -1), new Vector(0, 1, 0)) //
				.setVPDistance(100) //
				.setVPSize(500, 500)
				.setImageWriter(new ImageWriter("base render test", 1000, 1000))
		.setRayTracer(new RayTracerBasic(scene));

		camera.renderImage();
		camera.printGrid(100, new Color(java.awt.Color.YELLOW));
		camera.writeToImage();
	}

//	/**
//	 * Test for XML based scene - for bonus
//	 */
//	@Test
//	public void basicRenderXml() {
//		Scene scene = new Scene.SceneBuilder("XML Test scene").build();;
//		// enter XML file name and parse from XML file into scene object
//		// ...
//
//		Camera camera = new Camera(Point.ZERO, new Vector(0, 0, -1), new Vector(0, 1, 0)) //
//				.setVPDistance(100) //
//				.setVPSize(500, 500)
//				.setImageWriter(new ImageWriter("xml render test", 1000, 1000))
//				.setRayTracer(new RayTracerBasic(scene));
//		camera.renderImage();
//		camera.printGrid(100, new Color(java.awt.Color.YELLOW));
//		camera.writeToImage();
//	}
}
