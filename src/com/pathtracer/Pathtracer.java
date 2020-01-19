 package com.pathtracer;

public class Pathtracer {

	public static double MIN_DISTANCE = 0.001;
	
	public static int NUM_PRIMARY_RAYS = 10;
	public static int NUM_SECONDARY_RAYS = 3;

	public static Vector ambient = new Vector(10.0, 15.0, 25.0);
	
	/*
	 * Trace actual ray.
	 */
	public static ObjectHit getHit(Ray ray, Scene scene) {
		
		ObjectHit nearestHit = new ObjectHit(Hit.MISS, new Material());
		
		for(int i = 0; i < scene.objects.size(); i++) {
			WorldObject object = scene.objects.get(i);
			
			ObjectHit hit = object.intersect(ray);
			if(hit.hit.hit && hit.hit.distance < nearestHit.hit.distance) {
				if(i == 0) {
					double texX = hit.hit.hitPoint.x;
					double texZ = hit.hit.hitPoint.z;
					boolean a = Math.sin(texX) > 0.0 ? true : false;
					boolean b = Math.sin(texZ) > 0.0 ? true : false;
					Vector color = a ^ b ? new Vector(1.0, 1.0, 1.0) : new Vector(0.0, 0.0, 0.0);
					hit.material.color = color;
				}
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
		if(bounces > 5)
			return new Vector(0.0, 0.0, 0.0);
		
		/* Trace ray. */
		ObjectHit hit = getHit(ray, scene);
		
		if(hit.hit.hit) {
			
			/* Light emitted by the hit location. */
			Vector color = hit.material.emissiveColor;
			
			/* Light going into the hit location. */
			Vector incoming = new Vector(0.0, 0.0, 0.0);
			
			/* Do secondary rays. */
			for(int i = 0; i < NUM_SECONDARY_RAYS; i++) {
				Vector newDirection;
				
				if(Math.random() < hit.material.diffuseness) {
					newDirection = Material.getDiffuseVector(hit.hit.normal);
				} else {
					newDirection = Material.getReflectionVector(hit.hit.normal, ray.direction, hit.material.glossiness);
				}
				
				Ray newRay = new Ray(hit.hit.hitPoint, newDirection);
				incoming = incoming.plus(traceRay(newRay, scene, bounces + 1)).times(newDirection.dot(hit.hit.normal));
			}
			
			incoming = incoming.divBy(NUM_SECONDARY_RAYS).times(hit.material.color);
		
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
		
		double pixelWidth = camera.sensorSize / output.width;
		double pixelHeight = camera.sensorSize / output.height;
		
		for(int x = start; x < end; x++) {
			for(int y = 0; y < output.height; y++) {
				
				output.writePixel(x, y, new Vector(0.0, 255.0, 0.0));
				
				double worldX = ((double)x - output.width / 2.0) / output.width * camera.sensorSize;// + Math.random() * pixelWidth;
				double worldY = ((double)y - output.height / 2.0) / output.height * camera.sensorSize;// + Math.random() * pixelHeight;
						
				Vector locDirection = new Vector(worldX, worldY, camera.focalLength);
				
				Vector w = camera.lookingAt;
				Vector u = camera.lookingAt.cross(camera.up);
				Vector v = camera.up;
				
				Vector direction = Transforms.localToWorldCoords(locDirection, u, v, w);

				Ray primaryRay = new Ray(camera.position, direction);
				Vector color = new Vector(0.0, 0.0, 0.0);
				
				for(int i = 0; i < NUM_PRIMARY_RAYS; i++) {
					color = color.plus(traceRay(primaryRay, scene, 0));
				}
				
				color.divBy(NUM_PRIMARY_RAYS);
				
				output.writePixel(x, y, color);
				
			}
			
			//System.out.println("Now on line " + x);
		}
		
	}
		
}
