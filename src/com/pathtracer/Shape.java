package com.pathtracer;

/*
 * Shape; interface for geometric primitives/mesh objects
 */
public interface Shape {

	public Hit intersect(Ray ray);
	
}
