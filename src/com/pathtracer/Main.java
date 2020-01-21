package com.pathtracer;

import java.io.File;

public class Main {

	/*
	 * Entry point.
	 */
	public static void main(String[] args) {
	
		/* Set up camera, scene, and output. */
		Output output = new Output(256, 256);
		Camera camera = new Camera(60.0, new Vector(0.0, 0.0, -1.0), new Vector(0.0, 0.0, 1.0), new Vector(0.0, 1.0, 0.0));
		Scene scene = new Scene();
		
		/* Add objects to scene */
		Plane front = new Plane(new Vector(0.0, 0.0, -1.0), new Vector(0.0, 0.0, 3.0));
		Plane rear = new Plane(new Vector(0.0, 0.0, 1.0), new Vector(0.0, 0.0, -3.0));
		Plane ceiling = new Plane(new Vector(0.0, -1.0, 0.0), new Vector(0.0, 1.5, 0.0));
		Plane floor = new Plane(new Vector(0.0, 1.0, 0.0), new Vector(0.0, -1.5, 0.0));
		Plane left = new Plane(new Vector(1.0, 0.0, 0.0), new Vector(-1.0, 0.0, 0.0));
		Plane right = new Plane(new Vector(-1.0, 0.0, 0.0), new Vector(1.0, 0.0, 0.0));
		
		Sphere sph = new Sphere(new Vector(0.0, 0.0, 1.5), 0.5);
		TexturedMaterial mat = new TexturedMaterial(TexturedMaterial.loadTexture("earth.jpg"), new Vector(0.0, 0.0, 0.0), 0.3, 0.5);
		
		Circle circle = new Circle(new Vector(0.0, -1.0, 0.0), new Vector(0.0, 1.4, 0.0), 0.75);
		
		Material white = new BasicMaterial(new Vector(1.0, 1.0, 1.0), new Vector(0.0, 0.0, 0.0), 1.0, 0.0);
		Material source = new BasicMaterial(new Vector(1.0, 1.0, 1.0), new Vector(1000.0, 1000.0, 1000.0), 1.0, 0.0);
		
		scene.objects.add(new WorldObject(front, white));
		scene.objects.add(new WorldObject(rear, white));
		scene.objects.add(new WorldObject(ceiling, white));
		scene.objects.add(new WorldObject(floor, white));
		scene.objects.add(new WorldObject(left, white));
		scene.objects.add(new WorldObject(right, white));
		scene.objects.add(new WorldObject(sph, mat));
		
		scene.objects.add(new WorldObject(circle, source));
		
		/* Start live preview. */
		LivePreviewFrame frame = new LivePreviewFrame(output.image, 2);
		frame.setVisible(true);
		
		/* Render. */
		Renderer.startRender(camera, scene, output);
		
	}
	
}
