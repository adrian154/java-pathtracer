package com.pathtracer.geometry;

/*
 * Ray.
 */
public class Ray {

	public Vector direction;
	public Vector origin;
	
	public Ray() {
		
	}
	
	public Ray(Vector origin, Vector direction) {
		this.direction = direction.normalized();
		this.origin = origin;
	}
	
	public Vector point(double distance) {
		return origin.plus(direction.times(distance));
	}
	
}
