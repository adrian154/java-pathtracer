package com.pathtracer;

/*
 * Information about a cast ray.
 */
public class Hit {

	public boolean hit;
	public Vector hitPoint;
	public double distance;
	public Vector normal;
	
	/*
	 * Constant. Use to signal that the ray did not hit the target.
	 */
	public static final Hit MISS = new Hit(false, new Vector(), Double.POSITIVE_INFINITY, new Vector());
	
	public Hit(boolean hit, Vector hitPoint, double distance, Vector normal) {
		this.hit = hit;
		this.hitPoint = hitPoint;
		this.distance = distance;
		this.normal = normal;
	}
	
}
