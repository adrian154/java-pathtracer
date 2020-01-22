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
		Output output = new Output(512, 512);
		Camera camera = new Camera(60.0, new Vector(0.0, 0.0, -1.0), new Vector(0.0, 0.0, 1.0), new Vector(0.0, 1.0, 0.0));
		Scene scene = new Scene();
		
		/* Add objects to scene */
		Plane floor = new Plane(new Vector(0.0, 1.0, 0.0), new Vector(0.0, -1.0, 0.0));
		TexturedMaterial mat = new TexturedMaterial(TexturedMaterial.loadTexture("obama.jpg"), new Vector(0.0, 0.0, 0.0), 1.0, 0.0);
		
		Pathtracer.skyMaterial = new TexturedMaterial(TexturedMaterial.loadTexture("sky3.jpg"), new Vector(50.0, 50.0, 50.0), 1.0, 0.0);
		
		Sphere earth = new Sphere(new Vector(-0.5, 0.0, 2.0), 1.0);
		TexturedMaterial earthmat = new TexturedMaterial(TexturedMaterial.loadTexture("earth3.jpg"), new Vector(0.0, 0.0, 0.0), 1.0, 0.0);
		
		Sphere mars = new Sphere(new Vector(0.5, -0.5, 2.0), 0.5);
		TexturedMaterial marsmat = new TexturedMaterial(TexturedMaterial.loadTexture("mars2.jpg"), new Vector(0.0, 0.0, 0.0), 1.0, 0.0);

		Material white = new BasicMaterial(new Vector(1.0, 1.0, 1.0), new Vector(0.0, 0.0, 0.0), 1.0, 0.0);
		
		scene.objects.add(new WorldObject(floor, white));
		scene.objects.add(new WorldObject(earth, earthmat));
		scene.objects.add(new WorldObject(mars, marsmat));
		
		/* Start live preview. */
		if(GUI) {
			LivePreviewFrame frame = new LivePreviewFrame(output.image, 2);
			frame.setVisible(true);
		}
		
		/* Render. */
		Renderer.startRender(camera, scene, output);
		
	}
	
}
