package com.pathtracer;

import java.io.File;

import com.pathtracer.geometry.Circle;
import com.pathtracer.geometry.Plane;
import com.pathtracer.geometry.Sphere;
import com.pathtracer.geometry.Vector;
import com.pathtracer.material.BasicMaterial;
import com.pathtracer.material.Material;
import com.pathtracer.material.TexturedMaterial;

public class Main {

	/*
	 * Entry point.
	 */
	public static void main(String[] args) {
	
		boolean GUI = true;
		
		for(int i = 0; i < args.length; i++) {
			if(args[i].equals("--primary-rays") || args[i].equals("-p") && i++ < args.length) {
				Pathtracer.NUM_PRIMARY_RAYS = Integer.parseInt(args[i]);
			} else if(args[i].equals("--secondary-rays") || args[i].equals("-s") && i++ < args.length) {
				Pathtracer.NUM_SECONDARY_RAYS = Integer.parseInt(args[i]);
			} else if(args[i].equals("--noGUI") || args[i].equals("-n")) {
				GUI = false;
			} else if(args[i].equals("--threads") || args[i].equals("t") && i++ < args.length) {
				Renderer.numThreads = Integer.parseInt(args[i]);
			}
		}
		
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
		TexturedMaterial mat = new TexturedMaterial(TexturedMaterial.loadTexture("earth2.jpg"), new Vector(0.0, 0.0, 0.0), 1.0, 0.0);
		
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
		if(GUI) {
			LivePreviewFrame frame = new LivePreviewFrame(output.image, 2);
			frame.setVisible(true);
		}
		
		/* Render. */
		Renderer.startRender(camera, scene, output);
		
	}
	
}
