 package com.pathtracer;

public class Pathtracer {

	public static double MIN_DISTANCE = 0.001;
	
	public static int NUM_PRIMARY_RAYS = 5;
	public static int NUM_SECONDARY_RAYS = 2;

	public static Vector ambient = new Vector(0.0, 0.0, 0.0);
	
	/*
	 * Trace actual ray.
	 */
	public static ObjectHit getHit(Ray ray, Scene scene) {
		
		ObjectHit nearestHit = new ObjectHit(Hit.MISS, null);
		
		for(int i = 0; i < scene.objects.size(); i++) {
			WorldObject object = scene.objects.get(i);
			
			ObjectHit hit = new ObjectHit(object.intersect(ray), object.material);
			
			if(hit.hit && hit.distance < nearestHit.distance) {
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
		
		/* Terminate after too  many bounces. */
		if(bounces > 3)
			return new Vector(0.0, 0.0, 0.0);
		
		/* Trace ray. */
		ObjectHit hit = getHit(ray, scene);

		if(hit.hit) {
			
			/* Light emitted by the hit location. */
			Vector color = hit.material.getEmission();
			
			/* Light going into the hit location. */
			Vector incoming = new Vector(0.0, 0.0, 0.0);
			
			/* Do secondary rays. */
			for(int i = 0; i < NUM_SECONDARY_RAYS; i++) {
				Vector newDirection;
				
				if(Math.random() < hit.material.getDiffuseness()) {
					newDirection = Material.getDiffuseVector(hit.normal);
				} else {
					newDirection = Material.getReflectionVector(hit.normal, ray.direction, hit.material.getGlossiness());
				}
				
				Ray newRay = new Ray(hit.hitPoint, newDirection);
				
				incoming = incoming.plus(traceRay(newRay, scene, bounces + 1)).times(newDirection.dot(hit.normal));
			}
			
			incoming = incoming.divBy(NUM_SECONDARY_RAYS).times(hit.material.getColor(hit.textureCoordinates.x, hit.textureCoordinates.y));
			return color.plus(incoming);
			
		} else {
			
			/* If the ray missed return the ambient color. */
			return ambient;
		}
		
	}
	
	/*
	 * Render a scene.
	 */
	public static void renderSection(Camera camera, Scene scene, Output output, int start, int end) {
		
		double pixelWidth = 1.0 / output.width;
		double pixelHeight = 1.0 / output.height;
		
		for(int x = start; x < end; x++) {
			for(int y = 0; y < output.height; y++) {
				
				output.writePixel(x, y, new Vector(0.0, 255.0, 0.0));

				Vector color = new Vector(0.0, 0.0, 0.0);
				
				for(int i = 0; i < NUM_PRIMARY_RAYS; i++) {

					double worldX = ((double)x - output.width / 2.0) / output.width + (Math.random() - 0.5) * pixelWidth;
					double worldY = ((double)y - output.height / 2.0) / output.height + (Math.random() - 0.5) * pixelHeight;
							
					Vector locDirection = new Vector(worldX, worldY, camera.focalLength);
					
					Vector w = camera.lookingAt;
					Vector u = camera.lookingAt.cross(camera.up);
					Vector v = camera.up;
					
					Vector direction = Transforms.localToWorldCoords(locDirection, u, v, w);

					Ray primaryRay = new Ray(camera.position, direction);

					color = color.plus(traceRay(primaryRay, scene, 0));
				}
				
				color.divBy(NUM_PRIMARY_RAYS);
				
				output.writePixel(x, y, color);
				
			}
			
		}
		
	}
		
}
