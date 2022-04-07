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


import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.ParserConfigurationException;


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
		Scene scene = new Scene.SceneBuilder("Test scene")//
				.setAmbientLight(new AmbientLight(new Color(255, 191, 191), //
						new Double3(1, 1, 1))) //
				.setBackground(new Color(75, 127, 90)).build();

		scene._geometries.add(new Sphere(new Point(0, 0, -10), 5),
				new Triangle(new Point(-10, 0, -10), new Point(0, 10, -10), new Point(-10, 10, -10)), // up
				// left
				new Triangle(new Point(-10, 0, -10), new Point(0, -10, -10), new Point(-10, -10, -10)), // down
				// left
				new Triangle(new Point(10, 0, -10), new Point(0, -10, -10), new Point(10, -10, -10))); // down
		// right
		Camera camera = new Camera(Point.ZERO, new Vector(0, 0, -1), new Vector(0, 1, 0)) //
				.setVPDistance(100) //
				.setVPSize(500, 500) //
				.setImageWriter(new ImageWriter("base render test", 1000, 1000))
				.setRayTracer(new RayTracerBasic(scene));

		camera.renderImage();
		camera.printGrid(100, new Color(java.awt.Color.YELLOW));
		camera.writeToImage();
	}

	/**
	 * Test for XML based scene - for bonus
	 */
//	@Test
//	public void basicRenderXml() {
//		//Scene scene = new Scene.SceneBuilder("XML Test scene");
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


	/**
	 * Test for XML based scene - for bonus
	 */
	@Test
	public void basicRenderXml() throws ParserConfigurationException {
		//Scene scene = new Scene.SceneBuilder("C:\\Users\\ariel\\IdeaProjects\\ISE5782_3324_6024\\images\\basicRenderTestTwoColors.xml").build();
		;
		DalXml xml = new DalXml("C:/Users/ariel/IdeaProjects/ISE5782_3324_6024/images/basicRenderTestTwoColors");
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