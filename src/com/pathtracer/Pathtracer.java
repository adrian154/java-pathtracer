 package com.pathtracer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Pathtracer {

	public static double MIN_DISTANCE = 0.001;
	
	public static int NUM_PRIMARY_RAYS = 4;
	public static int NUM_SECONDARY_RAYS = 1;
	
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
		
		if(hit.hit.hit) {
			Vector color = hit.material.emissiveColor;
			
			Vector incoming = new Vector(0.0, 0.0, 0.0);
			
			for(int i = 0; i < NUM_SECONDARY_RAYS; i++) {
				Vector newDirection = Material.getDiffuseVector(hit.hit.normal);
				Ray newRay = new Ray(hit.hit.hitPoint, newDirection);
				incoming = incoming.plus(traceRay(newRay, scene, bounces + 1)).times(newDirection.dot(hit.hit.normal));
			}
			
			incoming = incoming.divBy(NUM_SECONDARY_RAYS).times(hit.material.color);
		
			return color.plus(incoming);
		} else {
			return new Vector(0.0, 0.0, 0.0);
		}
		
	}
	
	/*
	 * Test function. Traces ray
	 */
	public static Vector traceRayNorm(Ray ray, Scene scene, int bounces) {
		
		ObjectHit hit = getHit(ray, scene);
		if(hit.hit.hit) {
			return (new Vector(0.5, 0.5, 0.5).plus(hit.hit.normal.normalized().times(0.5))).times(100);
		} else {
			return new Vector(0.0, 0.0, 0.0);
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
				
				for(int i = 0; i < NUM_PRIMARY_RAYS; i++) {
					color = color.plus(traceRay(primaryRay, scene, 0));
				}
				
				color.divBy(NUM_PRIMARY_RAYS);
				
				output.writePixel(x, y, color);
				
			}
			
			System.out.println("Now on line " + x);
		}
		
	}
		
}
