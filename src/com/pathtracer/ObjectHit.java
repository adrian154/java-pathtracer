package com.pathtracer;

public class ObjectHit {

	public Hit hit;
	public Material material;
	
	public ObjectHit(Hit hit, Material material) {
		this.hit = hit;
		this.material = material;
	}
	
}
