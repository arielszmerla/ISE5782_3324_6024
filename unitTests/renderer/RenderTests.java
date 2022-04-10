package renderer;

import org.junit.jupiter.api.Test;

import lighting.AmbientLight;
import geometries.*;
import primitives.*;
import scene.Scene;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jdk.jshell.spi.ExecutionControl;
import xml.DalXml;

import static java.awt.Color.*;


/**
 * Test rendering a basic image
 *
 * @author Dan
 */
public class RenderTests {


	/**
	 * Produce a scene with basic 3D model and render it into a jpeg image with a
	 * grid
	 */

	@Test
	public void basicRenderTwoColorTest() {
		Scene scene = new Scene.SceneBuilder("Test scene")//
				.setAmbientLight(new AmbientLight(new Color(100, 100,100 ), //
						new Double3( 1,1,1))) //
				.setBackground(new Color(75, 127, 90)).build();

		scene._geometries.add(new Sphere( new Point(0, 0, -100),1).setEmission(new Color(BLUE)),
				new Triangle(new Point(-100, 0, -100), new Point(0, 100, -100), new Point(-100, 100, -100)), // up
				// left
				new Triangle(new Point(-100, 0, -100), new Point(0, -100, -100), new Point(-100, -100, -100)), // down
				// left
				new Triangle(new Point(100, 0, -100), new Point(0, -100, -100), new Point(100, -100, -100))); // down

		Camera camera = new Camera(Point.ZERO, new Vector(0, 0, -1), new Vector(0, 1, 0)) //
				.setVPDistance(100) //
				.setVPSize(500, 500) //
				.setImageWriter(new ImageWriter("base render test", 1000, 1000))
				.setRayTracer(new RayTracerBasic(scene));

		camera.renderImage();
		camera.printGrid(100, new Color(java.awt.Color.YELLOW));
		camera.writeToImage();
	}


// For stage 6 - please disregard in stage 5
	/**
	 * Produce a scene with basic 3D model - including individual lights of the
	 * bodies and render it into a png image with a grid
	 */
	@Test
	public void basicRenderMultiColorTest() {
		Scene scene = new Scene.SceneBuilder("Test scene")//
				.setAmbientLight(new AmbientLight(new Color(WHITE), new Double3(1,1,1))).build(); //

		scene._geometries.add( //
				new Sphere(new Point(0, 0, -100), 50),
				// up left
				new Triangle(new Point(-100, 0, -100), new Point(0, 100, -100), new Point(-100, 100, -100))
						.setEmission(new Color(GREEN)),
				// down left
				new Triangle(new Point(-100, 0, -100), new Point(0, -100, -100), new Point(-100, -100, -100))
						.setEmission(new Color(RED)),
				// down right
				new Triangle(new Point(100, 0, -100), new Point(0, -100, -100), new Point(100, -100, -100))
						.setEmission(new Color(BLUE)));

		Camera camera = new Camera(Point.ZERO, new Vector(0, 0, -1), new Vector(0, 1, 0)) //
				.setVPDistance(100) //
				.setVPSize(500, 500) //
				.setImageWriter(new ImageWriter("color render test", 1000, 1000))
				.setRayTracer(new RayTracerBasic(scene));

		camera.renderImage();
		camera.printGrid(100, new Color(WHITE));
		camera.writeToImage();
	}

	/**
	 * Test for XML based scene - for bonus
	 */
	@Test
	public void basicRenderXml() throws ParserConfigurationException {
		DalXml xml = new DalXml("images/basicRenderTestTwoColors");
		Scene scene = xml.getSceneFromXML();

		// enter XML file name and parse from XML file into scene object
		// ...

		Camera camera = new Camera(Point.ZERO, new Vector(0, 0, -1), new Vector(0, 1, 0)) //
				.setVPDistance(100) //
				.setVPSize(500, 500)
				.setImageWriter(new ImageWriter("xml render test", 1000, 1000))
				.setRayTracer(new RayTracerBasic(scene));
		camera.renderImage();
		camera.printGrid(100, new Color(java.awt.Color.YELLOW));
		camera.writeToImage();
	}


}