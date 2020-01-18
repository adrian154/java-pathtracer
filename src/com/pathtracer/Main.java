package com.pathtracer;

import java.io.File;

public class Main {

	public static Output output;
	
	public static void main(String[] args) {
		
		Mesh testMesh = new Mesh(new File("UtahTeapot.obj"), 1, new Vector(0.0, 0.0, 0.0));
		
		output = new Output(256, 256);
		Camera camera = new Camera(0.5, new Vector(0.0, 1.5, -10.0));
		Scene scene = new Scene();
		
		scene.objects.add(new WorldObject(testMesh, new Material(new Vector(1.0, 1.0, 1.0), new Vector(0.0, 0.0, 0.0), 0.5, 0.2)));
		
		// Light
		scene.objects.add(new WorldObject(new Sphere(new Vector(0.0, 105.0, 0.0), 100.02), new Material(new Vector(1.0, 1.0, 1.0), new Vector(100.0, 100.0, 100.0), 1.0, 0.0)));
		
		// Ceiling
		scene.objects.add(new WorldObject(new Plane(new Vector(0.0, -1.0, 0.0), new Vector(0.0, 5.0, 0.0)), new Material(new Vector(1.0, 1.0, 1.0), new Vector(0.0, 0.0, 0.0), 1.0, 0.0)));
		
		// Floor
		scene.objects.add(new WorldObject(new Plane(new Vector(0.0, 1.0, 0.0), new Vector(0.0, 0.0, 0.0)), new Material(new Vector(1.0, 0.0, 0.0), new Vector(0.0, 0.0, 0.0), 1.0, 0.0)));
		
		// Front
		scene.objects.add(new WorldObject(new Plane(new Vector(0.0, 0.0, -1.0), new Vector(0.0, 0.0, 5.0)), new Material(new Vector(1.0, 1.0, 1.0), new Vector(0.0, 0.0, 0.0), 1.0, 0.0)));
		
		// Rear
		scene.objects.add(new WorldObject(new Plane(new Vector(0.0, 0.0, 1.0), new Vector(0.0, 0.0, -11.0)), new Material(new Vector(1.0, 1.0, 1.0), new Vector(50.0, 50.0, 50.0), 1.0, 0.0)));
		
		// Light1 
		scene.objects.add(new WorldObject(new Sphere(new Vector(-1.5, 0.0, -2.0), 0.2), new Material(new Vector(0.0, 0.0, 0.0), new Vector(0.0, 255.0, 255.0), 0.0, 0.0)));
		
		// Light2 
		scene.objects.add(new WorldObject(new Sphere(new Vector(1.5, 0.0, -2.0), 0.2), new Material(new Vector(0.0, 0.0, 0.0), new Vector(255.0, 0.0, 255.0), 0.0, 0.0)));		
		
		LivePreviewFrame frame = new LivePreviewFrame();
		frame.setVisible(true);
		
		Renderer.render(camera, scene, output);
		
	}
	
}
