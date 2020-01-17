package com.pathtracer;

public class Plane implements Shape {

	public Vector normal;
	public Vector point;
	
	public Plane(Vector normal, Vector point) {
		this.normal = normal;
		this.point = point;
	}
	
	public Hit intersect(Ray ray) {
		
		double denom = ray.direction.dot(this.normal);
		if(denom == 0) {
			return new Hit(false, new Vector(), Double.POSITIVE_INFINITY, new Vector());
		}
		
		double distance = this.point.minus(ray.origin).dot(this.normal) / denom;
		
		if(distance < Pathtracer.MIN_DISTANCE) {
			return new Hit(false, new Vector(), Double.POSITIVE_INFINITY, new Vector());
		}
		
		Vector point = ray.point(distance);
		return new Hit(true, point, distance, this.normal);
		
	}
	
}
