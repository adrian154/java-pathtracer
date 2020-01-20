package com.pathtracer;

import java.io.File;

public class Main {

	/*
	 * Entry point.
	 */
	public static void main(String[] args) {
	
		/* Set up camera, scene, and output. */
		Output output = new Output(256, 256);
		
		Camera camera = new Camera(1.0, 1.0, new Vector(0.0, 0.0, -1.0), new Vector(0.0, 0.0, 1.0), new Vector(0.0, 1.0, 0.0));

		Scene scene = new Scene();
		
		/* Add objects to scene */
		Plane circle = new Plane(new Vector(0.0, 1.0, 0.0), new Vector(0.0, -3.0, 0.0));
		Sphere sphere1 = new Sphere(new Vector(-2.0, 0.0, 3.0), 0.3);
		Sphere sphere2 = new Sphere(new Vector(2.0, 0.0, 3.0), 0.3);
		Mesh mesh = new Mesh(new File("StanfordBunny.obj"), 20, new Vector(0.35, -2.0, 5.0));
		
		Material mat = new Material(new Vector(1.0, 1.0, 1.0), new Vector(0.0, 0.0, 0.0), 1.0, 0.0);
		Material src1 = new Material(new Vector(1.0, 1.0, 1.0), new Vector(1000.0, 500.0, 0.0), 1.0, 0.0);
		Material src2 = new Material(new Vector(1.0, 1.0, 1.0), new Vector(0.0, 500.0, 1000.0), 1.0, 0.0);
		Material mirr = new Material(new Vector(1.0, 1.0, 1.0), new Vector(0.0, 0.0, 0.0), 1.0, 0.0);
		
		scene.objects.add(new WorldObject(sphere1, src1));
		scene.objects.add(new WorldObject(sphere2, src2));
		scene.objects.add(new WorldObject(circle, mat));
		scene.objects.add(new WorldObject(mesh, mirr));
		
		/* Start live preview. */
		LivePreviewFrame frame = new LivePreviewFrame(output.image, 2);
		frame.setVisible(true);
		
		/* Render. */
		Renderer.startRender(camera, scene, output);
		
	}
	
}
