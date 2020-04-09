package com.pathtracer;

import com.pathtracer.geometry.Circle;
import com.pathtracer.geometry.Mesh;
import com.pathtracer.geometry.Plane;
import com.pathtracer.geometry.Vector;
import com.pathtracer.material.BasicMaterial;
import com.pathtracer.material.Material;
import com.pathtracer.material.MaterialHelper;
import com.pathtracer.material.TexturedMaterial;

public class Main {

	/*
	 * Entry point.
	 */
	public static void main(String[] args) {
	
		boolean GUI = true;
		
		int numPrimaryRays = 100;
		int numSecondaryRays = 1;
		int numThreads = Runtime.getRuntime().availableProcessors();
		int numBounces = 6;
		
		Mesh.OCTREE_LEVEL = 3;
		
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
		Camera camera = new Camera(60.0, new Vector(0.0, 2.0, 0.0), new Vector(0.0, 0.0, 1.0), new Vector(0.0, 1.0, 0.0));
		Scene scene = new Scene();
		Pathtracer pathtracer = new Pathtracer(numPrimaryRays, numSecondaryRays, numBounces, scene, camera);
		
		/* Add objects to scene */
		Material matfloor = new TexturedMaterial(TexturedMaterial.loadTexture("mossybrick.png"), new Vector(0.0, 0.0, 0.0), 1.0, 0.0, false);
		Material matlwall = MaterialHelper.createDiffuse(new Vector(1.0, 1.0, 0.0));
		Material matrwall = MaterialHelper.createDiffuse(new Vector(0.0, 1.0, 1.0));
		Material matlight = new BasicMaterial(new Vector(1.0, 1.0, 1.0), new Vector(1.0, 1.0, 1.0).times(1000.0), 1.0, 0.0, false);
		Material matceiling = MaterialHelper.createDiffuse(new Vector(1.0, 1.0, 1.0));
		Material matfwall = MaterialHelper.createDiffuse(new Vector(1.0, 1.0, 1.0));
		
		final double WIDTH = 5.0;
		final double HEIGHT = 4.0;
		final double DEPTH = 30.0;
		Plane geomfloor = new Plane(new Vector(0.0, 1.0, 0.0), new Vector(0.0, 0.0, 0.0));
		Plane geomlwall = new Plane(new Vector(1.0, 0.0, 0.0), new Vector(-WIDTH / 2, 0.0, 0.0));
		Plane geomrwall = new Plane(new Vector(-1.0, 0.0, 0.0), new Vector(WIDTH / 2, 0.0, 0.0));
		Plane geomceiling = new Plane(new Vector(0.0, -1.0, 0.0), new Vector(0.0, HEIGHT, 0));
		Plane geomfwall = new Plane(new Vector(0.0, 0.0, -1.0), new Vector(0.0, 0.0, DEPTH / 2));
		Circle geomlight = new Circle(new Vector(0.0, -1.0, 0.0), new Vector(0.0, 3.0, 5.0), 1.0);
		
		scene.objects.add(new WorldObject(geomfloor, matfloor));
		scene.objects.add(new WorldObject(geomlwall, matlwall));
		scene.objects.add(new WorldObject(geomrwall, matrwall));
		scene.objects.add(new WorldObject(geomlight, matlight));
		scene.objects.add(new WorldObject(geomceiling, matceiling));
		scene.objects.add(new WorldObject(geomfwall, matfwall));
		
		pathtracer.skyMaterial = new BasicMaterial(new Vector(1.0, 1.0, 1.0).times(0.2), new Vector(0.0, 0.0, 0.0), 1.0, 0.0, false);
		
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
