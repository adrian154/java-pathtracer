package com.pathtracer;

public class Main {

	public static void main(String[] args) {
		
		Output output = new Output(256, 256);
		
		Camera camera = new Camera(0.5, new Vector(0.0, 0.0, 0.0));
		
		Scene scene = new Scene();
		scene.objects.add(new WorldObject(new Sphere(new Vector(0.0, -0.1, 3.0), 0.3), new Material(new Vector(1.0, 1.0, 1.0), new Vector(0.0, 0.0, 0.0))));
		scene.objects.add(new WorldObject(new Sphere(new Vector(0.0, 0.7, 3.0), 0.2), new Material(new Vector(1.0, 1.0, 1.0), new Vector(3000.0, 3000.0, 3000.0))));
		
		scene.objects.add(new WorldObject(new Plane(new Vector(0.0, 1.0, 0.0), new Vector(0.0, -1.3, 0.0)), new Material(new Vector(1.0, 1.0, 1.0), new Vector(0.0, 0.0, 0.0))));
		scene.objects.add(new WorldObject(new Plane(new Vector(0.0, -1.0, 0.0), new Vector(0.0, 1.3, 0.0)), new Material(new Vector(1.0, 1.0, 1.0), new Vector(0.0, 0.0, 0.0))));
		scene.objects.add(new WorldObject(new Plane(new Vector(0.0, 0.0, -1.0), new Vector(0.0, 0.0, 5.0)), new Material(new Vector(1.0, 0.0, 0.0), new Vector(0.0, 0.0, 0.0))));
		Renderer.render(camera, scene, output);
		
		//output.writeToFile("output.png");
		
	}
	
}
