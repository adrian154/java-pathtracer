package com.pathtracer.geometry;

/*
 * Octree node. Extension of bounding box.
 */
public class OctreeBoundingBox extends BoundingBox {

	/* Sub-boxes. */
	public OctreeBoundingBox subBoxes[];

	/* Triangles contained in box (if terminal); otherwise, null. */
	public int containedTriangles[];
	
	/* Is the box a terminal node? */
	public boolean isTerminal;
	
	public OctreeBoundingBox(Vector min, Vector max) {
		super(min, max);
	}
	
}
