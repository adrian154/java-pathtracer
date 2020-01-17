package com.pathtracer;

public class Main {

	public static void main(String[] args) {
		
		Output output = new Output(256, 256);
		
		Camera camera = new Camera(0.5, new Vector(0.0, 0.0, 0.0));
		
		Scene scene = new Scene();
		scene.objects.add(new WorldObject(new Sphere(new Vector(0.0, 0.0, 2.0), 0.5), new Material(new Vector(1.0, 1.0, 1.0), new Vector(0.0, 0.0, 0.0))));
		scene.objects.add(new WorldObject(new Sphere(new Vector(0.0, 0.5, 2.0), 0.2), new Material(new Vector(1.0, 1.0, 1.0), new Vector(100.0, 1000.0, 1000.0))));
		//scene.objects.add(new WorldObject(new Plane(new Vector(0.0, 1.0, 0.0), new Vector(0.0, -1.0, 0.0)), new Material(new Vector(1.0, 0.0, 0.0), new Vector(200.0, 0.0, 0.0))));
		Pathtracer.render(camera, scene, output);
		
		output.writeToFile("output.png");
	
	}
	
}
