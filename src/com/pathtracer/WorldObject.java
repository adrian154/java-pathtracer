package com.pathtracer;

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
