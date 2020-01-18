package com.pathtracer;

public class Sphere implements Shape {

	public Vector center;
	public double radius;
	
	public Sphere(Vector center, double radius) {
		this.center = center;
		this.radius = radius;
	}
	
	public Hit intersect(Ray ray) {
			
		/* Normalize direction. */
		Vector direction = ray.direction.normalized();
		
		/* Shift coordinate system so origin is on ray origin. */
		/* This makes calculations much easier. */
		Vector center = this.center.minus(ray.origin);
		
		/* Set up values */
		double a = direction.x * direction.x + direction.y * direction.y + direction.z * direction.z;
		double b = -2 * (direction.x * center.x + direction.y * center.y + direction.z * center.z);
		double c = center.x * center.x + center.y * center.y + center.z * center.z - radius * radius;
		
		/* Check if solution exists */
		double discrim = b * b - 4 * a * c;
		
		/* No solution. */
		if(discrim < 0) {
			return Hit.MISS;
		}
		
		/* Solve quadratic */
		double t1 = (-b + Math.sqrt(discrim)) / (2 * a);
		double t2 = (-b - Math.sqrt(discrim)) / (2 * a);
		
		/* Exclude values that are negative (behind camera) */
		if(t1 < Pathtracer.MIN_DISTANCE && t2 < Pathtracer.MIN_DISTANCE) {
			return Hit.MISS;
		}
		
		double distance = (t1 > Pathtracer.MIN_DISTANCE && t2 < Pathtracer.MIN_DISTANCE) ? t1 : ((t1 < Pathtracer.MIN_DISTANCE && t2 > Pathtracer.MIN_DISTANCE) ? t2 : ((t1 < t2) ? t1 : t2));
		
		Vector point = ray.point(distance);
		Vector normal = point.minus(this.center).normalized();
		
 		return new Hit(true, point, distance, normal);
		
	}
	
}
