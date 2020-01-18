package com.pathtracer;

import java.io.File;

public class Main {

	public static Output output;
	public static int outputScale = 2;
	
	public static void main(String[] args) {
		
		/* Set up camera, scene, and output. */
		output = new Output(256, 256);
		Camera camera = new Camera(1.0, 1.8, new Vector(0.0, 0.0, -10.0));
		Scene scene = new Scene();
		
		/* Add objects to scene */
		Plane floor = new Plane(new Vector(0.0, 1.0, 0.0), new Vector(0.0, -3.0, 0.0));
		Plane ceiling = new Plane(new Vector(0.0, -1.0, 0.0), new Vector(0.0, 3.0, 0.0));
		Plane left = new Plane(new Vector(1.0, 0.0, 0.0), new Vector(-3.0, 0.0, 0.0));
		Plane right = new Plane(new Vector(-1.0, 0.0, 0.0), new Vector(3.0, 0.0, 0.0));
		Plane rear = new Plane(new Vector(0.0, 0.0, -1.5), new Vector(0.0, 0.0, 3.0));
		Plane back = new Plane(new Vector(0.0, 0.0, 1.0), new Vector(0.0, 0.0, -11.0));
		Mesh mesh = new Mesh(new File("StanfordBunny.obj"), 20, new Vector(0.5, -2.5, 0.0));
		
		Material red = new Material(new Vector(1.0, 0.0, 0.0), new Vector(0.0, 0.0, 0.0), 1.0, 0.0);
		Material green = new Material(new Vector(0.0, 1.0, 0.0), new Vector(0.0, 0.0, 0.0), 1.0, 0.0);
		Material blue = new Material(new Vector(0.0, 0.0, 1.0), new Vector(0.0, 0.0, 0.0), 1.0, 0.0);
		Material white = new Material(new Vector(1.0, 1.0, 1.0), new Vector(0.0, 0.0, 0.0), 1.0, 0.0);
		Material source = new Material(new Vector(1.0, 1.0, 1.0), new Vector(200.0, 200.0, 200.0), 1.0, 0.0);
		Material pot = new Material(new Vector(1.0, 1.0, 1.0), new Vector(0.0, 0.0, 0.0), 0.0, 0.3);
		
		scene.objects.add(new WorldObject(floor, red));
		scene.objects.add(new WorldObject(ceiling, source));
		scene.objects.add(new WorldObject(rear, green));
		scene.objects.add(new WorldObject(back, blue));
		scene.objects.add(new WorldObject(left, white));
		scene.objects.add(new WorldObject(right, white));
		scene.objects.add(new WorldObject(mesh, pot));
		
		/* Start live preview. */
		LivePreviewFrame frame = new LivePreviewFrame();
		frame.setVisible(true);
		
		/* Render. */
		Renderer.render(camera, scene, output);
		
	}
	
}
