package com.pathtracer;

/*
 * Object hit information. Contains material info.
 */
public class ObjectHit {

	public Hit hit;
	public Material material;
	
	public ObjectHit(Hit hit, Material material) {
		this.hit = hit;
		this.material = material;
	}
	
}
