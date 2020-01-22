package com.pathtracer.geometry;

import com.pathtracer.Hit;
import com.pathtracer.Pathtracer;

/*
 * Infinite plane.
 */
public class Plane implements Shape {

	public Vector normal;
	public Vector point;
	
	public Plane(Vector normal, Vector point) {
		this.normal = normal;
		this.point = point;
	}
	
	/*
	 * Ray-plane intersection.
	 */
	public Hit intersect(Ray ray) {
		
		/* Check if equation has solution.*/
		double denom = ray.direction.dot(this.normal);
		if(denom == 0) {
			return Hit.MISS;
		}
		
		/* Solve for the distance. */
		double distance = this.point.minus(ray.origin).dot(this.normal) / denom;
		
		/* Make sure hit point is not behind ray. */
		if(distance < Pathtracer.MIN_DISTANCE) {
			return Hit.MISS;
		}
		
		Vector point = ray.point(distance);
		Vector u = normal.getOrthagonal();
		Vector v = normal.cross(u);
		
		return new Hit(true, point, distance, this.normal, new Vector(u.dot(point), v.dot(point), 0.0));
		
	}
	
}
