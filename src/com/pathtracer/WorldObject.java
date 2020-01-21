package com.pathtracer;

import com.pathtracer.geometry.Ray;
import com.pathtracer.geometry.Shape;
import com.pathtracer.material.Material;

/*
 * Object in the world. Has geometry and material.
 */
public class WorldObject {

	public Shape shape;
	public Material material;
	
	public WorldObject(Shape shape, Material material) {
		this.shape = shape;
		this.material = material;
	}
	
	public ObjectHit intersect(Ray ray) {
		return new ObjectHit(this.shape.intersect(ray), material);
	}
	
}
