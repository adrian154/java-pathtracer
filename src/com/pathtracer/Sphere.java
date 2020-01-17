package com.pathtracer;

public class Sphere implements Shape {

	public Vector center;
	public double radius;
	
	public Sphere(Vector center, double radius) {
		this.center = center;
		this.radius = radius;
	}
	
	public Hit intersect(Ray ray) {
			
		/* Shift coordinate system so origin is on ray origin. *
		/* This makes calculations much easier. */
		Vector center = this.center.minus(ray.origin);
		
		/* Set up values */
		double a = ray.direction.dot(ray.direction);
		double b = 2 * ray.direction.dot(center);
		double c = center.dot(center) - radius * radius;
		
		/* Check if solution exists */
		double discrim = b * b - 4 * a * c;
		
		/* No solution. */
		if(discrim < 0) {
			return new Hit(false, new Vector(), Double.POSITIVE_INFINITY, new Vector());
		}
		
		System.out.println("ro: " + ray.origin.toString() + ", rd: " + ray.direction.toString());
		System.out.println(a + ", " + b + ", " + c);
		
		/* Solve quadratic */
		double t1 = (-b + Math.sqrt(discrim)) / (2 * a);
		double t2 = (-b - Math.sqrt(discrim)) / (2 * a);
		
		/* Exclude values that are negative (behind camera) */
		if(t1 < Pathtracer.MIN_DISTANCE && t2 < Pathtracer.MIN_DISTANCE) {
			return new Hit(false, new Vector(), Double.POSITIVE_INFINITY, new Vector());
		}
		
		double distance = (t1 > Pathtracer.MIN_DISTANCE && t2 < Pathtracer.MIN_DISTANCE) ? t1 : (t2 < Pathtracer.MIN_DISTANCE && t2 > Pathtracer.MIN_DISTANCE) ? t2 : (t1 < t2) ? t1 : t2;
		Vector point = ray.point(distance);
		Vector normal = point.minus(this.center).normalized();
		
		System.out.println("Success!");
		
 		return new Hit(true, point, distance, normal);
		
	}
	
}
