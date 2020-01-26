package com.pathtracer;

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
		
		int numPrimaryRays = 200;
		int numSecondaryRays = 3;
		int numThreads = Runtime.getRuntime().availableProcessors();
		int numBounces = 6;
		
		/* Reads command-line arguments */
		for(int i = 0; i < args.length; i++) {
			if(args[i].equals("--primary-rays") || args[i].equals("-p") && i++ < args.length) {
				numPrimaryRays = Integer.parseInt(args[i]);
			} else if(args[i].equals("--secondary-rays") || args[i].equals("-s") && i++ < args.length) {
				numSecondaryRays = Integer.parseInt(args[i]);
			} else if(args[i].equals("--noGUI") || args[i].equals("-n")) {
				GUI = false;
			} else if(args[i].equals("--threads") || args[i].equals("-t") && i++ < args.length) {
				numThreads = Integer.parseInt(args[i]);
			} else if(args[i].equals("--bounces") || args[i].equals("-b") && i++ < args.length) {
				numBounces = Integer.parseInt(args[i]);
			}
		}
		
		/* Set up camera, scene, and output. */
		Output output = new Output(256, 256);
		Camera camera = new Camera(60.0, new Vector(0.0, 0.0, -10.0), new Vector(0.0, 0.0, 1.0), new Vector(0.0, 1.0, 0.0));
		Scene scene = new Scene();
		Pathtracer pathtracer = new Pathtracer(numPrimaryRays, numSecondaryRays, numBounces, scene, camera);
		
		/* Add objects to scene */	
		Material sphereMat = new TexturedMaterial(TexturedMaterial.loadTexture("earth.jpg"), new Vector(0.0, 0.0, 0.0), 1.0, 0.0);
		Sphere sphereGeom = new Sphere(new Vector(0.0, -0.3, 0.0), 1.7);
		
		Material lightMat = new BasicMaterial(new Vector(1.0, 1.0, 1.0), new Vector(1.0, 1.0, 1.0).times(14000.0), 1.0, 0.0);
		Circle lightGeom = new Circle(new Vector(0.0, -1.0, 0.0), new Vector(0.0, 3.5, 0.0), 2.0);
		
		Material floorMat = new TexturedMaterial(TexturedMaterial.loadTexture("oakplanks.png"), new Vector(0.0, 0.0, 0.0), 1.0, 0.0);
		Plane floorGeom = new Plane(new Vector(0.0, 1.0, 0.0), new Vector(0.0, -2.0, 0.0), 1.0);
		
		pathtracer.skyMaterial = new TexturedMaterial(TexturedMaterial.loadTexture("milkyway.jpg"), new Vector(0.0, 0.0, 0.0), 0.0, 0.0);

		scene.objects.add(new WorldObject(lightGeom, lightMat));
		scene.objects.add(new WorldObject(floorGeom, floorMat));
		scene.objects.add(new WorldObject(sphereGeom, sphereMat));
		
		/* Start live preview. */
		if(GUI) {
			LivePreviewFrame frame = new LivePreviewFrame(output.image, 2);
			frame.setVisible(true);
		}
		
		/* Render. */
		Renderer renderer = new Renderer(pathtracer, output);
		renderer.startRender();
		
	}
	
}
