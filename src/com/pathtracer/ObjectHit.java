package com.pathtracer;

/*
 * Object hit information. Contains material info.
 */
public class ObjectHit extends Hit {

	public Material material;
	
	public ObjectHit(Hit hit, Material material) {
		super(hit.hit, hit.hitPoint, hit.distance, hit.normal, hit.textureCoordinates);
		this.material = material;
	}
	
}
