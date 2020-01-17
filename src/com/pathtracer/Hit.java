package com.pathtracer;

public class Hit {

	public boolean hit;
	public Vector hitPoint;
	public double distance;
	public Vector normal;
	
	public Hit(boolean hit, Vector hitPoint, double distance, Vector normal) {
		this.hit = hit;
		this.hitPoint = hitPoint;
		this.distance = distance;
		this.normal = normal;
	}
	
}
