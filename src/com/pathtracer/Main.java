package com.pathtracer;

import java.io.File;

public class Main {

	public static void main(String[] args) {
		
		Mesh testMesh = new Mesh(new File("UtahTeapot.obj"));
		
		Output output = new Output(256, 256);
		Camera camera = new Camera(0.5, new Vector(0.0, 1.5, -10.0));
		Scene scene = new Scene();
		
		scene.objects.add(new WorldObject(testMesh, new Material(new Vector(1.0, 1.0, 1.0), new Vector(0.0, 0.0, 0.0))));
		
		// Sky
		scene.objects.add(new WorldObject(new Plane(new Vector(0.0, -1.0, 0.0), new Vector(0.0, 5.0, 0.0)), new Material(new Vector(0.0, 0.0, 0.0), new Vector(255.0, 255.0, 255.0))));
		
		// Floor
		scene.objects.add(new WorldObject(new Plane(new Vector(0.0, 1.0, 0.0), new Vector(0.0, 0.0, 0.0)), new Material(new Vector(1.0, 0.0, 0.0), new Vector(0.0, 0.0, 0.0))));
		
		// Rear
		scene.objects.add(new WorldObject(new Plane(new Vector(0.0, 0.0, -1.0), new Vector(0.0, 0.0, 5.0)), new Material(new Vector(1.0, 1.0, 1.0), new Vector(0.0, 0.0, 0.0))));
		
		Renderer.render(camera, scene, output);
		
	}
	
}
