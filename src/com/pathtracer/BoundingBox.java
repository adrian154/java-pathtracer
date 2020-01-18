package com.pathtracer;

public class BoundingBox {

	public Vector min;
	public Vector max;
	
	public BoundingBox(Vector min, Vector max) {
		this.min = min;
		this.max = max;
	}
	
	public boolean doesIntersect(Ray ray) {
		
		/* Intersections with all 6 AABB planes */
		double xmin = (min.x - ray.origin.x) / ray.direction.x;
		double xmax = (max.x - ray.origin.x) / ray.direction.x;
		
		double ymin = (min.y - ray.origin.y) / ray.direction.y;
		double ymax = (max.y - ray.origin.y) / ray.direction.y;
		
		double zmin = (min.z - ray.origin.z) / ray.direction.z;
		double zmax = (max.z - ray.origin.z) / ray.direction.z;

		double tmin = Math.max(Math.max(Math.min(xmin, xmax), Math.min(ymin, ymax)), Math.min(zmin, zmax));
		double tmax = Math.min(Math.min(Math.max(xmin, xmax), Math.max(ymin, ymax)), Math.max(zmin, zmax));
	
		/* AABB is behind ray */
		if(tmax < 0) {
			return false;
		}
		
		/* Doesn't intersect */
		if(tmin > tmax) {
			return false;
		}
		
		return true;
	}
	
}
