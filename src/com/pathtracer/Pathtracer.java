 package com.pathtracer;

import com.pathtracer.geometry.Ray;
import com.pathtracer.geometry.Transforms;
import com.pathtracer.geometry.Vector;
import com.pathtracer.material.BasicMaterial;
import com.pathtracer.material.Material;

public class Pathtracer {

	public static double MIN_DISTANCE = 0.0001;
	
	public int numPrimaryRays;
	public int numSecondaryRays;
	
	public Material skyMaterial;
	public Scene scene;
	public Camera camera;
	
	public Pathtracer(int numPrimaryRays, int numSecondaryRays, Scene scene, Camera camera) {
		this.numPrimaryRays = numPrimaryRays;
		this.numSecondaryRays = numSecondaryRays;
		this.scene = scene;
		this.camera = camera;
		this.skyMaterial = new BasicMaterial(new Vector(0.0, 0.0, 0.0), new Vector(0.0, 0.0, 0.0), 0.0, 0.0);
	}
	
	public Pathtracer(int numPrimaryRays, int numSecondaryRays, Scene scene, Camera camera, Material skyMaterial) {
		this.numPrimaryRays = numPrimaryRays;
		this.numSecondaryRays = numSecondaryRays;
		this.scene = scene;
		this.camera = camera;
		this.skyMaterial = skyMaterial;
	}
	
	/*
	 * Trace actual ray.
	 */
	public ObjectHit getHit(Ray ray) {
		
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
	public Vector traceRay(Ray ray, int bounces) {
		
		/* Terminate after too  many bounces. */
		if(bounces > 3)
			return new Vector(0.0, 0.0, 0.0);
		
		/* Trace ray. */
		ObjectHit hit = getHit(ray);

		if(hit.hit) {
			
			/* Light emitted by the hit location. */
			Vector color = hit.material.getEmission();
			
			/* Light going into the hit location. */
			Vector incoming = new Vector(0.0, 0.0, 0.0);
			
			/* Do secondary rays. */
			for(int i = 0; i < this.numSecondaryRays; i++) {
				Vector newDirection;
				
				if(Math.random() < hit.material.getDiffuseness()) {
					newDirection = Material.getDiffuseVector(hit.normal);
				} else {
					newDirection = Material.getReflectionVector(hit.normal, ray.direction, hit.material.getGlossiness());
				}
				
				Ray newRay = new Ray(hit.hitPoint, newDirection);
				
				incoming = incoming.plus(traceRay(newRay, bounces + 1)).times(newDirection.dot(hit.normal));
			}
			
			incoming = incoming.divBy(this.numSecondaryRays).times(hit.material.getColor(hit.textureCoordinates.x, hit.textureCoordinates.y));
			return color.plus(incoming);
			
		} else {
			
			/* If the ray missed return the ambient color. */
			Vector d = new Vector(0.0, 0.0, 0.0).minus(ray.direction);
			double u = 0.5 + Math.atan2(d.z, d.x) / (2 * Math.PI);
			double v = 0.5 - Math.asin(d.y) / Math.PI;
			
			return skyMaterial.getColor(u, v).times(255);
			
		}
		
	}
	
	/*
	 * Render a scene.
	 */
	public void renderSectionf(Output output, int start, int end) {
		
		double pixelWidth = 1.0 / output.width;
		double pixelHeight = 1.0 / output.height;
		
		for(int x = start; x < end; x++) {
			for(int y = 0; y < output.height; y++) {
				
				output.writePixel(x, y, new Vector(0.0, 255.0, 0.0));

				Vector color = new Vector(0.0, 0.0, 0.0);
				
				for(int i = 0; i < this.numPrimaryRays; i++) {

					double worldX = ((double)x - output.width / 2.0) / output.width + (Math.random() - 0.5) * pixelWidth;
					double worldY = ((double)y - output.height / 2.0) / output.height + (Math.random() - 0.5) * pixelHeight;
							
					Vector locDirection = new Vector(worldX, worldY, camera.focalLength);
					
					Vector w = camera.lookingAt;
					Vector u = camera.lookingAt.cross(camera.up);
					Vector v = camera.up;
					
					Vector direction = Transforms.localToWorldCoords(locDirection, u, v, w);

					Ray primaryRay = new Ray(camera.position, direction);

					color = color.plus(traceRay(primaryRay, 0));
				}
				
				color = color.divBy(this.numPrimaryRays);
	
				output.writePixel(x, y, color);
				
			}
			
		}
		
	}
	
	/*
	 * Test
	 */
	public void renderSection(Output output, int start, int end) {
		
		Vector point = new Vector(0.0, 3.0, -2.0);
		
		double pixelWidth = 1.0 / output.width;
		double pixelHeight = 1.0 / output.height;
		
		for(int x = start; x < end; x++) {
			for(int y = 0; y < output.height; y++) {
				
				output.writePixel(x, y, new Vector(0.0, 255.0, 0.0));
				
				Vector color = new Vector(0.0, 0.0, 0.0);
				
				for(int i = 0; i < 64; i++) {
					double worldX = ((double)x - output.width / 2.0) / output.width + (Math.random() - 0.5) * pixelWidth;
					double worldY = ((double)y - output.height / 2.0) / output.height + (Math.random() - 0.5) * pixelHeight;
							
					Vector locDirection = new Vector(worldX, worldY, camera.focalLength);
					
					Vector w = camera.lookingAt;
					Vector u = camera.lookingAt.cross(camera.up);
					Vector v = camera.up;
					
					Vector direction = Transforms.localToWorldCoords(locDirection, u, v, w);
					Ray primaryRay = new Ray(camera.position, direction);
					
					ObjectHit hit = getHit(primaryRay);
					if(hit.hit) {
						Vector vec = point.minus(hit.hitPoint).normalized();
						color = color.plus(hit.material.getColor(hit.textureCoordinates.x, hit.textureCoordinates.y).times(255.0 * vec.dot(hit.normal)));
					}
				}
			
				color = color.divBy(64.0);
				output.writePixel(x, y, color);
				
			}
			
		}
		
	}
		
}
