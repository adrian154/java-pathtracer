package com.pathtracer.geometry;

import com.pathtracer.Hit;

public class Circle extends Plane {

	public double radius;

	public Circle(Vector normal, Vector point, double radius) {
		super(normal, point);
		this.radius = radius;
	}
	
	/*
	 * Ray-plane intersection.
	 */
	public Hit intersect(Ray ray) {
		
		Hit hit = super.intersect(ray);
		if(hit.hit) {

			if(hit.hitPoint.minus(this.point).lengthSquared() < this.radius * this.radius)
				return hit;
			else
				return Hit.MISS;
		
		}
		
		return Hit.MISS;
		
	}
	
}
