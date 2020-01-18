package com.pathtracer;

public class Hit {

	public boolean hit;
	public Vector hitPoint;
	public double distance;
	public Vector normal;
	
	public static Hit MISS = new Hit(false, new Vector(), Double.POSITIVE_INFINITY, new Vector());
	
	public Hit(boolean hit, Vector hitPoint, double distance, Vector normal) {
		this.hit = hit;
		this.hitPoint = hitPoint;
		this.distance = distance;
		this.normal = normal;
	}
	
}
