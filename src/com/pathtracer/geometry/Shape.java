package com.pathtracer.geometry;

import com.pathtracer.Hit;

/*
 * Shape; interface for geometric primitives/mesh objects
 */
public interface Shape {

	public Hit intersect(Ray ray);
	
}
