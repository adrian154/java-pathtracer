 package com.pathtracer;

import com.pathtracer.geometry.Circle;
import com.pathtracer.geometry.Ray;
import com.pathtracer.geometry.Sphere;
import com.pathtracer.geometry.Transforms;
import com.pathtracer.geometry.Vector;
import com.pathtracer.material.BasicMaterial;
import com.pathtracer.material.Material;

public class Pathtracer {

	public static double MIN_DISTANCE = 0.000000001;
	
	public int numPrimaryRays;
	public int numSecondaryRays;
	public int numBounces;
	
	public Material skyMaterial;
	public Scene scene;
	public Camera camera;
	
	public Pathtracer(int numPrimaryRays, int numSecondaryRays, int numBounces, Scene scene, Camera camera) {
		this.numPrimaryRays = numPrimaryRays;
		this.numSecondaryRays = numSecondaryRays;
		this.numBounces = numBounces;
		this.scene = scene;
		this.camera = camera;
		this.skyMaterial = new BasicMaterial(new Vector(0.0, 0.0, 0.0), new Vector(0.0, 0.0, 0.0), 0.0, 0.0, false);
	}
	
	public Pathtracer(int numPrimaryRays, int numSecondaryRays, int numBounces, Scene scene, Camera camera, Material skyMaterial) {
		this.numPrimaryRays = numPrimaryRays;
		this.numSecondaryRays = numSecondaryRays;
		this.numBounces = numBounces;
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
	 * Check if there is a path between a point and an object.
	 */
	public boolean hasPathTo(Ray ray, WorldObject object) {
		
		Hit objHit = object.intersect(ray);
		
		for(int i = 0; i < scene.objects.size(); i++) {
			WorldObject curObject = scene.objects.get(i);
			if(curObject == object)
				continue;
			
			Hit hit = curObject.intersect(ray);
			if(hit.distance < objHit.distance)
				return false;
			
		}
		
		return true;
		
	}
	
	/*
	 * traceRay: Solves rendering equation numerically with Monte Carlo method.
	 * Returns color as vector.
	 */
	public Vector traceRay(Ray ray, int bounces) {
		
		/* Terminate after too  many bounces. */
		if(bounces > this.numBounces)
			return new Vector(0.0, 0.0, 0.0);
		
		/* Trace ray. */
		ObjectHit hit = getHit(ray);

		if(hit.hit) {
			
			/* Light emitted by the hit location. */
			Vector color = hit.material.getEmission(hit.textureCoordinates.x, hit.textureCoordinates.y);
			
			/* Light going into the hit location. */
			Vector incoming = new Vector(0.0, 0.0, 0.0);
			
			/* Do secondary rays. */
			Vector selfColor = hit.material.getColor(hit.textureCoordinates.x, hit.textureCoordinates.y);
			double diffuseness = hit.material.getDiffuseness();
			
			for(int i = 0; i < this.numSecondaryRays; i++) {

				Ray newRay = new Ray(hit.hitPoint, new Vector(0.0, 0.0, 0.0));
						
				Vector diffuseSample = new Vector(0.0, 0.0, 0.0);
				Vector specularSample = new Vector(0.0, 0.0, 0.0);
				
				if(diffuseness > 0.0) {
					Vector diffuseVector = Material.getDiffuseVector(hit.normal);
					newRay.direction = diffuseVector;
					diffuseSample = traceRay(newRay, bounces + 1);
					diffuseSample = diffuseSample.times(diffuseVector.dot(hit.normal)).times(selfColor);
				}
				
				if(diffuseness < 1.0) {
					Vector specularVector = Material.getReflectionVector(hit.normal, ray.direction, hit.material.getGlossiness());
					newRay.direction = specularVector;
					specularSample = traceRay(newRay, bounces + 1);
					
					if(!hit.material.isPlastic())
						specularSample = specularSample.times(selfColor);
				}
				
				Vector total = diffuseSample.times(hit.material.getDiffuseness()).plus(specularSample.times(1 - hit.material.getDiffuseness()));
				incoming = incoming.plus(total);
			}
			
			incoming = incoming.divBy(this.numSecondaryRays);
			return color.plus(incoming);
			
		} else {
			
			/* If the ray missed return the ambient color. */
			Vector d = new Vector(0.0, 0.0, 0.0).minus(ray.direction);
			double u = 0.5 + Math.atan2(d.z, d.x) / (2 * Math.PI);
			double v = 0.5 - Math.asin(d.y) / Math.PI;
			
			return skyMaterial.getColor(u, v).times(255).plus(skyMaterial.getEmission(u, v));
			
		}
		
	}
	
	public Vector correct(Vector in, double gamma) {
		return new Vector(
			Math.pow(in.x / 255, gamma) * 255,
			Math.pow(in.y / 255, gamma) * 255,
			Math.pow(in.z / 255, gamma) * 255
		);
	}
	
	/*
	 * Render a scene.
	 */
	public void renderSectionF(Output output, int start, int end) {
		
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
				
				color = correct(color.divBy(this.numPrimaryRays), 0.8);
	
				output.writePixel(x, y, color);
				
			}
			
		}
		
	}
	
	/*
	 * Test; render with flat shading
	 */
	public void renderSection(Output output, int start, int end) {
		
		//Vector point = camera.position;
		Vector point = new Vector(0.0, 3.8, 8.0);
		Vector point2 = camera.position;

		for(int x = start; x < end; x++) {
			for(int y = 0; y < output.height; y++) {
				
				output.writePixel(x, y, new Vector(0.0, 255.0, 0.0));
				
				double worldX = ((double)x - output.width / 2.0) / output.width;
				double worldY = ((double)y - output.height / 2.0) / output.width;
						
				Vector locDirection = new Vector(worldX, worldY, camera.focalLength);
				
				Vector w = camera.lookingAt;
				Vector u = camera.lookingAt.cross(camera.up);
				Vector v = camera.up;
				
				Vector direction = Transforms.localToWorldCoords(locDirection, u, v, w);
				Ray primaryRay = new Ray(camera.position, direction);
				
				Vector color = new Vector(0.0, 0.0, 0.0);
				
				ObjectHit hit = getHit(primaryRay);
				
				if(hit.hit) {
					Vector vec = point.minus(hit.hitPoint).normalized();
					Vector vec2 = point2.minus(hit.hitPoint).normalized();
					
					double factor = 255 * vec.dot(hit.normal) + 255 * vec2.dot(hit.normal);
					
					//color = color.plus(hit.material.getColor(hit.textureCoordinates.x, hit.textureCoordinates.y).times(factor));
					//color = new Vector(hit.textureCoordinates.x, hit.textureCoordinates.y, 1 - hit.textureCoordinates.x - hit.textureCoordinates.y).times(255);
					color = new Vector(255.0, 255.0, 255.0);
				}
			
				output.writePixel(x, y, color);
				
			}
			
		}
		
	}
		
}
