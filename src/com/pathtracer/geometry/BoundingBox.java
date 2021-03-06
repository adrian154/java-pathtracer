package com.pathtracer.geometry;

import com.pathtracer.Hit;
import com.pathtracer.Pathtracer;

/*
 * 3D axis aligned bounding box.
 */
public class BoundingBox implements Shape {

	/*
	 * Minimum and maximum points of the box.
	 */
	public Vector min;
	public Vector max;
	
	public BoundingBox(Vector min, Vector max) {
		this.min = min;
		this.max = max;
	}

	/*
	 * Checks if a ray intersects with the bounding box.
	 */
	public boolean doesIntersect(Ray ray) {
		return intersectDistance(ray) == Double.POSITIVE_INFINITY ? false : true;
	}
	
	public double intersectDistance(Ray ray) {
		
		/* Intersections with all 6 AABB planes */
		double xmin = (min.x - ray.origin.x) / ray.direction.x;
		double xmax = (max.x - ray.origin.x) / ray.direction.x;
		
		double ymin = (min.y - ray.origin.y) / ray.direction.y;
		double ymax = (max.y - ray.origin.y) / ray.direction.y;
		
		double zmin = (min.z - ray.origin.z) / ray.direction.z;
		double zmax = (max.z - ray.origin.z) / ray.direction.z;

		/* Minimum and maximum intersection distances. */
		double tmin = Math.max(Math.max(Math.min(xmin, xmax), Math.min(ymin, ymax)), Math.min(zmin, zmax));
		double tmax = Math.min(Math.min(Math.max(xmin, xmax), Math.max(ymin, ymax)), Math.max(zmin, zmax));
	
		/* Negative: AABB is behind the ray. */
		if(tmax < Pathtracer.MIN_DISTANCE) {
			return Double.POSITIVE_INFINITY;
		}
		
		/* Minimum distance greater than maximum: No intersection. */
		if(tmin > tmax) {
			return Double.POSITIVE_INFINITY;
		}
		
		/* Ray intersects with the box. */
		return tmin;
	}
	
	/*
	 * Proper intersection
	 */
	public Hit intersect(Ray ray) {
		
		double distance = intersectDistance(ray);
		if(distance == Double.POSITIVE_INFINITY)
			return Hit.MISS;
		
		Vector hitPoint = ray.point(distance);

		Vector middle = min.plus(max.minus(min).divBy(2.0));
		double deltaX = hitPoint.x - middle.x;
		double deltaY = hitPoint.y - middle.y;
		double deltaZ = hitPoint.z - middle.z;
		
		Vector normal;
		if(Math.abs(Math.abs(deltaX) - this.getWidth() / 2) < Pathtracer.MIN_DISTANCE) {
			normal = new Vector(Math.signum(deltaX), 0.0, 0.0);
		} else if(Math.abs(Math.abs(deltaY) - this.getHeight() / 2) < Pathtracer.MIN_DISTANCE) {
			normal = new Vector(0.0, Math.signum(deltaY), 0.0);
		} else {
			normal = new Vector(0.0, 0.0, Math.signum(deltaZ));
		}
		
		return new Hit(true, hitPoint, distance, normal, new Vector(0.0, 0.0, 0.0));
		
	}
	
	/*
	 * Checks if a point is contained in the box.
	 */
	public boolean contains(Vector vector) {
		if(vector.x >= min.x  && vector.y >= min.y && vector.z >= min.z && vector.x <= max.x && vector.y <= max.y && vector.z <= max.z)
			return true;
		else
			return false;
	}
	
	/*
	 * Get width, height, and depth.
	 */
	public double getWidth() { return max.x - min.x; }
	public double getHeight() { return max.y - min.y; }
	public double getDepth() { return max.z - min.z; }
	
}
