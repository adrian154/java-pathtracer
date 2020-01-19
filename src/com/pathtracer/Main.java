package com.pathtracer;

import java.io.File;

public class Main {

	/*
	 * Entry point.
	 */
	public static void main(String[] args) {
		
		/* Set up camera, scene, and output. */
		Output output = new Output(256, 256);
		Camera camera = new Camera(1.0, 2.0, new Vector(0.0, 0.0, -10.0), new Vector(0.0, 0.0, 1.0), new Vector(0.0, 1.0, 0.0));
		Scene scene = new Scene();
		
		/* Add objects to scene */
		Mesh mesh = new Mesh(new File("garfield.obj"), 0.05, new Vector(0.0, -3.0, 5.0));
		Plane plane = new Plane(new Vector(0.0, 1.0, 0.0), new Vector(0.0, -3.0, 0.0));
		
		Material diff = new Material(new Vector(1.0, 1.0, 1.0), new Vector(0.0, 0.0, 0.0), 1.0, 0.0);
		Material mat = new Material(new Vector(1.0, 1.0, 1.0), new Vector(0.0, 0.0, 0.0), 1.0, 0.0);
		
		scene.objects.add(new WorldObject(mesh, mat));
		//scene.objects.add(new WorldObject(plane, diff));
		
		/* Start live preview. */
		LivePreviewFrame frame = new LivePreviewFrame(output.image, 2);
		frame.setVisible(true);
		
		/* Render. */
		Renderer.render(camera, scene, output);
		
	}
	
}
