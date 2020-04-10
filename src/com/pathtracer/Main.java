package com.pathtracer;

import java.io.File;

import com.pathtracer.geometry.BoundingBox;
import com.pathtracer.geometry.Circle;
import com.pathtracer.geometry.Mesh;
import com.pathtracer.geometry.Plane;
import com.pathtracer.geometry.Sphere;
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
		
		int numPrimaryRays = 5;
		int numSecondaryRays = 1;
		int numThreads = Runtime.getRuntime().availableProcessors();
		int numBounces = 3;
		
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
		Output output = new Output(720, 720, true);
		Camera camera = new Camera(60.0, new Vector(0.0, 1.8, 0.0), new Vector(0.0, 0.0, 1.0), new Vector(0.0, 1.0, 0.0));
		Scene scene = new Scene();
		Pathtracer pathtracer = new Pathtracer(numPrimaryRays, numSecondaryRays, numBounces, scene, camera, true);
		
		/* Add objects to scene */
		Material matfloor = new TexturedMaterial(TexturedMaterial.loadTexture("oakplanks.png"), new Vector(0.0, 0.0, 0.0), 1.0, 0.0, false);
		Material matlight1 = new BasicMaterial(new Vector(1.0, 1.0, 1.0), new Vector(1.0, 1.0, 0.95).times(3000.0), 1.0, 0.0, false);
		Material matlight2 = new BasicMaterial(new Vector(1.0, 1.0, 1.0), new Vector(0.95, 1.0, 1.0).times(3000.0), 1.0, 0.0, false);
		Material matceiling = MaterialHelper.createDiffuse(new Vector(1.0, 1.0, 1.0));
		//Material matfwall = new TexturedMaterial(TexturedMaterial.loadTexture("wallpaper.png"), new Vector(0.0, 0.0, 0.0), 1.0, 0.0, false);
		Material matwall = MaterialHelper.createDiffuse(new Vector(0x95/255.0, 0xC9/255.0, 0xDE/255.0));
		Material WHITE = MaterialHelper.createDiffuse(new Vector(1.0, 1.0, 1.0));
		Material matmesh = new BasicMaterial(new Vector(1.0, 1.0, 0.95), new Vector(0.0, 0.0, 0.0), 1.0, 0.0, true);
		Material matvishnu = new BasicMaterial(new Vector(1.0, 1.0, 0.0), new Vector(0.0, 0.0, 0.0), 0.7, 0.2, false);
		
		final double WIDTH = 5.0;
		final double HEIGHT = 4.0;
		final double DEPTH = 18.0;
		Plane geomfloor = new Plane(new Vector(0.0, 1.0, 0.0), new Vector(0.0, 0.0, 0.0));
		Plane geomlwall = new Plane(new Vector(1.0, 0.0, 0.0), new Vector(-WIDTH / 2, 0.0, 0.0));
		Plane geomrwall = new Plane(new Vector(-1.0, 0.0, 0.0), new Vector(WIDTH / 2, 0.0, 0.0));
		Plane geomceiling = new Plane(new Vector(0.0, -1.0, 0.0), new Vector(0.0, HEIGHT, 0));
		Plane geomfwall = new Plane(new Vector(0.0, 0.0, -1.0), new Vector(0.0, 0.0, DEPTH / 2), 4.0);
		BoundingBox geombox = new BoundingBox(new Vector(0.5, 0.0, 4.0), new Vector(1.5, 1.0, 5.0));
		//Circle geomlight = new Circle(new Vector(0.0, -1.0, 0.0), new Vector(0.0, HEIGHT - 0.2, 5.0), 1.0);
		Circle l1 = new Circle(new Vector(0.0, -1.0, 0.0), new Vector(-1.0, HEIGHT - 0.1, 7.0), 0.4);
		Circle l2 = new Circle(new Vector(0.0, -1.0, 0.0), new Vector(1.0, HEIGHT - 0.1, 7.0), 0.4);
		Circle l3 = new Circle(new Vector(0.0, -1.0, 0.0), new Vector(-1.0, HEIGHT - 0.1, 5.0), 0.4);
		Circle l4 = new Circle(new Vector(0.0, -1.0, 0.0), new Vector(1.0, HEIGHT - 0.1, 5.0), 0.4);
		
		Mesh mesh = new Mesh(new File("ObamaHead.obj"), new Vector(1.0, 1.0, 1.0).times(0.06), new Vector(1.0, 1.0, 4.5));
		Mesh gvishnu = new Mesh(new File("Vishnu.obj"), new Vector(1.0, 1.0, 1.0).times(0.002), new Vector(-0.8, 0.0, 6.5));
		
		scene.objects.add(new WorldObject(geomfloor, matfloor));
		scene.objects.add(new WorldObject(geomlwall, matwall));
		scene.objects.add(new WorldObject(geomrwall, matwall));
		scene.objects.add(new WorldObject(l1, matlight1));
		scene.objects.add(new WorldObject(l2, matlight2));
		scene.objects.add(new WorldObject(l3, matlight1));
		scene.objects.add(new WorldObject(l4, matlight2));
		scene.objects.add(new WorldObject(geomceiling, matceiling));
		scene.objects.add(new WorldObject(geomfwall, matwall));
		scene.objects.add(new WorldObject(geombox, WHITE));
		scene.objects.add(new WorldObject(mesh, matmesh));
		scene.objects.add(new WorldObject(gvishnu, matvishnu));
		
		pathtracer.skyMaterial = new BasicMaterial(new Vector(1.0, 1.0, 1.0).times(0.2), new Vector(0.0, 0.0, 0.0), 1.0, 0.0, false);
		
		/* Start live preview. */
		if(GUI) {
			LivePreviewFrame frame = new LivePreviewFrame(output.image, 1);
			frame.setVisible(true);
		}
		
		/* Render. */
		Renderer renderer = new Renderer(pathtracer, output);
		renderer.startRender();
		
	}
	
}
