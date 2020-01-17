 package com.pathtracer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Pathtracer {

	public static double MIN_DISTANCE = 0.001;
	
	public static ObjectHit getHit(Ray ray, Scene scene) {
		
		ObjectHit nearestHit = new ObjectHit(new Hit(false, new Vector(), Double.POSITIVE_INFINITY, new Vector()), new Material());
		
		for(WorldObject object : scene.objects) {
			ObjectHit hit = object.intersect(ray);
			if(hit.hit.hit && hit.hit.distance < nearestHit.hit.distance) {
				nearestHit = hit;
			}
		}
		
		return nearestHit;
		
	}
	
	/*
	 * traceRay: Solves rendering equation numerically with Monte Carlo method.
	 * Returns color as vector.
	 */
	public static Vector traceRay(Ray ray, Scene scene, int bounces) {
		
		if(bounces > 2)
			return new Vector(0.0, 0.0, 0.0);
		
		ObjectHit hit = getHit(ray, scene);
		
		Vector color = new Vector(0.0, 0.0, 0.0);
		
		if(hit.hit.hit) {
			color = color.plus(hit.material.emissiveColor);
			
			Vector incoming = new Vector(0.0, 0.0, 0.0);
			
			for(int i = 0; i < 4; i++) {
				Vector newDirection = Material.getDiffuseVector(hit.hit.normal);
				Ray newRay = new Ray(hit.hit.hitPoint, newDirection);
				incoming = incoming.plus(traceRay(newRay, scene, bounces + 1)).times(newDirection.dot(hit.hit.normal));
			}
			
			incoming = incoming.divBy(4).times(hit.material.color);
		
			return color.plus(incoming);
		} else {
			return color;
		}
		
	}
	
	/*
	 * Render a scene.
	 */
	public static void renderSection(Camera camera, Scene scene, Output output, int start, int end) {
		
		for(int x = start; x < end; x++) {
			for(int y = 0; y < output.height; y++) {
				
				double worldX = ((double)x - output.width / 2.0) / output.width;
				double worldY = ((double)y - output.height / 2.0) / output.height;
						
				Vector direction = new Vector(worldX, worldY, 1.0);
				
				Ray primaryRay = new Ray(camera.position, direction);
				Vector color = new Vector(0.0, 0.0, 0.0);
				
				for(int i = 0; i < 4; i++) {
					color = color.plus(traceRay(primaryRay, scene, 0));
				}
				
				color.divBy(4);
				
				output.writePixel(x, y, color);
				
			}
			
			System.out.println("Now on line " + x);
		}
		
	}
		
}
