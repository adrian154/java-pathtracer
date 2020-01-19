package com.pathtracer;

public class OctreeBoundingBox extends BoundingBox {

	public OctreeBoundingBox subBoxes[];
	public int containedTriangles[];
	public boolean isTerminal;
	
	public OctreeBoundingBox(Vector min, Vector max) {
		super(min, max);
	}
	
}