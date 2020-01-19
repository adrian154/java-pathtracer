package com.pathtracer;

public class OctreeBoundingBox extends BoundingBox {

	public OctreeBoundingBox subBoxes[];
	
	public OctreeBoundingBox(Vector min, Vector max) {
		super(min, max);
	}
	
}
