package com.pathtracer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Mesh implements Shape {

	public Vector[] vertexes;
	public int[][] triangles;
	public OctreeBoundingBox octree;
	
	public Mesh(File file, double scale, Vector offset) {
		
		ArrayList<Vector> vertexes = new ArrayList<Vector>();
		ArrayList<int[]> triangles = new ArrayList<int[]>();
		
		/* Read mesh into arraylist */
		try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
			
			/* Read file line by line. */
			String line;
			int lineNum = 0;
			
			while((line = reader.readLine()) != null) {
				
				/* Avoid newlines */
				if(line.length() == 0) {
					continue;
				}
				
				/* Split line */
				String[] parts = line.split(" ");

				if(!parts[0].equals("v") && !parts[0].equals("f"))
					continue;
				
				/* Validate */
				if(parts.length != 4) {
					System.out.println("error in \"" + file.getName() + "\" at line " + lineNum + ": wrong number of parameters (expected 4)");
					return;
				}
				
				if(parts[0].equals("v")) {
					double d1 = Double.parseDouble(parts[1]) * scale + offset.x;
					double d2 = Double.parseDouble(parts[2]) * scale + offset.y;
					double d3 = Double.parseDouble(parts[3]) * scale + offset.z;
					vertexes.add(new Vector(d1, d2, d3));
				} else {
					int i1 = Integer.parseInt(parts[1]);
					int i2 = Integer.parseInt(parts[2]);
					int i3 = Integer.parseInt(parts[3]);
					
					int tri[] = new int[3];
					tri[0] = i1;
					tri[1] = i2;
					tri[2] = i3;
					triangles.add(tri);
				}
				
				lineNum++;
			}
			
		} catch(Exception exception) {
			exception.printStackTrace();
		}
		
		/* Convert arraylists to array */
		this.vertexes = vertexes.toArray(new Vector[vertexes.size()]);
		this.triangles = triangles.toArray(new int[triangles.size()][3]);
		
		this.computeOctree();
		
	}
	
	/*
	 * Get triangles in a bounding box. 	
	 */
	public int[] getTrianglesInBoundingBox(BoundingBox box) {
		
		/* ArrayList of triangles (convert to array later) */
		ArrayList<Integer> triangles = new ArrayList<Integer>();
		
		for(int i = 0; i < this.triangles.length; i++) {
			
			/* Check if part of the triangle are contained in the bounding box. */
			int triangle[] = this.triangles[i];
			Vector v1 = vertexes[triangle[0]];
			Vector v2 = vertexes[triangle[1]];
			Vector v3 = vertexes[triangle[3]];
			
			if(box.contains(v1) || box.contains(v2) || box.contains(v3)) {
				triangles.add(i);
			}
		}
		
		/* Convert to array. */
		return triangles.stream().mapToInt(Integer::intValue).toArray();
		
	}
	
	/*
	 * Create an octree for the mesh.
	 */
	public void computeOctree() {
		
		/* First, compute the bounding box of the mesh */
		double minX = Double.POSITIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		double minZ = Double.POSITIVE_INFINITY;
		
		double maxX = Double.NEGATIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;
		double maxZ = Double.NEGATIVE_INFINITY;
		
		for(int i = 0; i < vertexes.length; i++) {
			Vector vertex = vertexes[i];
			
			if(vertex.x < minX) minX = vertex.x;
			if(vertex.y < minY) minY = vertex.y;
			if(vertex.z < minZ) minZ = vertex.z;
			
			if(vertex.x > maxX) maxX = vertex.x;
			if(vertex.y > maxY) maxY = vertex.y;
			if(vertex.z > maxZ) maxZ = vertex.z;
		}
		
		Vector min = new Vector(minX, minY, minZ);
		Vector max = new Vector(maxX, maxY, maxZ);

		this.octree = new OctreeBoundingBox(min, max);
		splitBox(this.octree);
		
	}
	
	/*
	 * Split box into 8 subboxes.
	 */
	public void splitBox(OctreeBoundingBox box) {
		
		Vector min = box.min;
		Vector max = box.max;
		
		Vector middle = min.plus(max.minus(min).divBy(2.0));
		double width = max.x - min.x;
		double height = max.y - min.y;
		double depth = max.z - min.z;
		
		OctreeBoundingBox boxes[] = new OctreeBoundingBox[8];
		boxes[0] = new OctreeBoundingBox(min, middle);
		boxes[1] = new OctreeBoundingBox(min.plus(new Vector(0.0, 0.0, depth / 2)), middle.plus(new Vector(0.0, 0.0, depth / 2)));
		boxes[2] = new OctreeBoundingBox(min.plus(new Vector(width / 2, 0.0, 0.0)), middle.plus(new Vector(width / 2, 0.0, 0.0)));
		boxes[3] = new OctreeBoundingBox(min.plus(new Vector(width / 2, 0.0, depth / 2)), middle.plus(new Vector(width / 2, 0.0, depth / 2)));
		boxes[4] = new OctreeBoundingBox(min.plus(new Vector(0.0, height / 2, 0.0)), middle.plus(new Vector(0.0, height / 2, 0.0)));
		boxes[5] = new OctreeBoundingBox(min.plus(new Vector(0.0, height / 2, depth / 2)), middle.plus(new Vector(0.0, height / 2, depth / 2)));
		boxes[6] = new OctreeBoundingBox(min.plus(new Vector(width / 2, height / 2, 0.0)), middle.plus(new Vector(width / 2, 0.0, 0.0)));
		boxes[7] = new OctreeBoundingBox(middle, max);
		
		/* Kind of a kludge - remove unfilled boxes. */
		int numUnfilledBoxes = 0;
		
		
	}
	
	/* Util functions for checking if a point is in a triangle */
	public boolean sameSide(Vector p1, Vector p2, Vector a, Vector b) {
		Vector cp1 = b.minus(a).cross(p1.minus(a));
		Vector cp2 = b.minus(a).cross(p2.minus(a));
		if(cp1.dot(cp2) >= 0)
			return true;
		else
			return false;
	}
	
	public Hit intersect(Ray ray) {
		
		if(!this.octree.doesIntersect(ray)) {
			return Hit.MISS;
		}
		
		Hit nearestHit = Hit.MISS;
		
		/* Loop through triangles */
		for(int i = 0; i < triangles.length; i++) {
			Vector p1 = vertexes[triangles[i][0] - 1];
			Vector p2 = vertexes[triangles[i][1] - 1];
			Vector p3 = vertexes[triangles[i][2] - 1];
			
			Vector normal = p2.minus(p1).cross(p3.minus(p2)).normalized();
			Plane plane = new Plane(normal, p1);
			Hit hit = plane.intersect(ray);
			
			/* Check if point is inside triangle */
			if(hit.hit) {
				if(sameSide(hit.hitPoint, p1, p2, p3) && sameSide(hit.hitPoint, p2, p1, p3) && sameSide(hit.hitPoint, p3, p1, p2)) {
					if(hit.distance < nearestHit.distance) {
						nearestHit = hit;
					}
				}
			}
			
		}
		
		if(nearestHit.hit) 
			return nearestHit;
		
		return Hit.MISS;
		
	}
	

}
