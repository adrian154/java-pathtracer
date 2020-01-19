package com.pathtracer;

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
		
		/* Get intersection point; check if it is inside of the circle. */
		Vector point = ray.point(distance);
		if(point.minus(this.point).lengthSquared() < this.radius * this.radius)
			return new Hit(true, point, distance, this.normal);
		else
			return Hit.MISS;
		
		
	}
	
}
