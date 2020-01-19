package com.pathtracer;

public class BoundingBox {

	public Vector min;
	public Vector max;
	
	public BoundingBox(Vector min, Vector max) {
		this.min = min;
		this.max = max;
	}
	
	/*
	 * Checks if a ray intersects with a bounding box.
	 */
	public double intersect(Ray ray) {
		
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
			return Double.POSITIVE_INFINITY;
		}
		
		/* Doesn't intersect */
		if(tmin > tmax) {
			return Double.POSITIVE_INFINITY;
		}
		
		return tmin;
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
	
	/*
	 * Checks if a point is contained in the box.
	 */
	public boolean contains(Vector vector) {
		if(vector.x > min.x  && vector.y > min.y && vector.z > min.z && vector.x < max.x && vector.y < max.y && vector.z < max.z)
			return true;
		else
			return false;
	}
	
	public double getWidth() {
		return max.x - min.x;
	}
	
	public double getHeight() {
		return max.y - min.y;
	}
	
	public double getDepth() {
		return max.z - min.z;
	}
	
}
