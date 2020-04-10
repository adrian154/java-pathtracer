package com.pathtracer.geometry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

import com.pathtracer.Hit;
import com.pathtracer.Pathtracer;

/*
 * Triangle mesh.
 */
public class Mesh implements Shape {

	/* Vertexes in the mesh. */
	public Vector[] vertexes;
	
	/* Triangles; contains indexes of vertexes. */
	public int[][] triangles;
	
	/* Octree, for optimization. */
	public OctreeBoundingBox octree;
	
	/* Static field - octree level, for mesh construction. */
	public static int OCTREE_LEVEL = 2;
	
	/* Constructor -  load mesh. */
	public Mesh(File file, double scale, Vector offset) {
		this(file, new Vector(1.0, 1.0, 1.0).times(scale), offset);
	}
	
	public Mesh(File file, Vector scale, Vector offset) {
		
		ArrayList<Vector> vertexes = new ArrayList<Vector>();
		ArrayList<int[]> triangles = new ArrayList<int[]>();

		/* Read mesh into arraylist */
		try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
			
			/* Read file line by line. */
			String line;
			int lineNum = 0;
			
			while((line = reader.readLine()) != null) {
				
				lineNum++;
				
				/* Avoid empty lines. */
				if(line.length() == 0) {
					continue;
				}
				
				/* Split line */
				String[] parts = line.split("\\s+");

				/* Ignore unsupported lines. */
				if(!parts[0].equals("v") && !parts[0].equals("f"))
					continue;
				
				/* Validate the line. */
				if(parts[0].equals("v") && parts.length != 4) {
					System.out.println("error in \"" + file.getName() + "\" at line " + lineNum + ": wrong number of parameters for vertex (expected 4 but got " + parts.length + ")");
					System.exit(1);
				} else if(parts[0].equals("f") && parts.length < 4) {
					System.out.println("error in \"" + file.getName() + "\" at line " + lineNum + ": too few parameters for face (expected at least 4 but got " + parts.length + ")");
					System.exit(1);
				}
				
				if(parts[0].equals("v")) {
					
					/* Load vertex. */
					double d1 = Double.parseDouble(parts[1]) * scale.x + offset.x;
					double d2 = Double.parseDouble(parts[2]) * scale.y + offset.y;
					double d3 = Double.parseDouble(parts[3]) * scale.z + offset.z;
					vertexes.add(new Vector(d1, d2, d3));
					
				} else {
					
					/* Load polygon */
					int indexes[] = new int[parts.length - 1];
		
					for(int vert = 0; vert < indexes.length; vert++) {
						int strindex = parts[vert + 1].indexOf('/');
						indexes[vert] = Integer.parseInt(strindex < 0 ? parts[vert + 1] : parts[vert + 1].substring(0, strindex));
					}
					
					if(indexes.length == 3) {
						int tri[] = new int[3];
						tri[0] = indexes[0];
						tri[1] = indexes[1];
						tri[2] = indexes[2];
						triangles.add(tri);
					} else if(indexes.length == 4) {
						int tri1[] = new int[3];
						tri1[0] = indexes[0];
						tri1[1] = indexes[1];
						tri1[2] = indexes[2];
						triangles.add(tri1);
						
						int tri2[] = new int[3];
						tri2[0] = indexes[0];
						tri2[1] = indexes[2];
						tri2[2] = indexes[3];
						triangles.add(tri2);
					}
					
					/*
					for(int vert = 0; vert < indexes.length - 2; vert++) {
						int tri[] = new int[3];
						tri[0] = indexes[vert];
						tri[1] = indexes[vert + 1];
						tri[2] = indexes[vert + 2];
						triangles.add(tri);
					}
					*/
					
				}
				
			}
			
		} catch(Exception exception) {
			exception.printStackTrace();
			System.exit(1);
		}
		
		/* Convert arraylists to array */
		this.vertexes = vertexes.toArray(new Vector[vertexes.size()]);
		this.triangles = triangles.toArray(new int[triangles.size()][3]);
		
		/* Compute octree for the mesh */
		System.out.println("Building " + Mesh.OCTREE_LEVEL + "-level octree for mesh " + file.getName());
		long startTime = System.currentTimeMillis();
		this.computeOctree();
		long elapsed = System.currentTimeMillis() - startTime;
		System.out.println("Finished building octree in " + elapsed + " milliseconds.");
		
	}
	
	/* 
	 * Check if bounding box contains parts of mesh.
	 */
	public boolean hasTrianglesInBoundingBox(BoundingBox box) {
		
		for(int i = 0; i < this.triangles.length; i++) {
			int triangle[] = this.triangles[i];
			Vector v1 = vertexes[triangle[0] - 1];
			Vector v2 = vertexes[triangle[1] - 1];
			Vector v3 = vertexes[triangle[2] - 1];
			
			if(box.contains(v1) || box.contains(v2) || box.contains(v3)) 
				return true;
		}
		
		return false;
		
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
			Vector v1 = vertexes[triangle[0] - 1];
			Vector v2 = vertexes[triangle[1] - 1];
			Vector v3 = vertexes[triangle[2] - 1];
			
			if(box.contains(v1) || box.contains(v2) || box.contains(v3)) {
				
				/* If the box contains one of the triangle's vertices, right off the bat it can be added. */
				triangles.add(i);
				
			} else {
				
				/* Otherwise, check each edge. */
				Vector edge1 = v2.minus(v1);			/* v1 to v2 */
				Vector edge2 = v3.minus(v2);			/* v2 to v3 */
				Vector edge3 = v1.minus(v3);			/* V3 to v1 */
				double l1 = box.intersectDistance(new Ray(v1, edge1));
				double l2 = box.intersectDistance(new Ray(v2, edge2));
				double l3 = box.intersectDistance(new Ray(v3, edge3));
				
				if(l1 * l1 < edge1.lengthSquared() || l2 * l2 < edge2.lengthSquared() || l3 * l3 < edge3.lengthSquared()) {
					triangles.add(i);
				}
				
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

		/* Split the bounding box into sub-boxes. */
		this.octree = new OctreeBoundingBox(min, max);
		splitBox(this.octree, 0);
		
	}
	
	/*
	 * Split box into 8 subboxes.
	 */
	public void splitBox(OctreeBoundingBox box, int level) {

		/* Bottom left and top right corners */
		Vector min = box.min;
		Vector max = box.max;
		
		Vector middle = min.plus(max.minus(min).divBy(2.0));
		double width = max.x - min.x;
		double height = max.y - min.y;
		double depth = max.z - min.z;
		
		/* Split box into 8 subboxes. */
		OctreeBoundingBox boxes[] = new OctreeBoundingBox[8];
		
		/* Bottom half */
		/* Left */
		boxes[0] = new OctreeBoundingBox(min, middle);
		boxes[1] = new OctreeBoundingBox(min.plus(new Vector(0.0, 0.0, depth / 2)), middle.plus(new Vector(0.0, 0.0, depth / 2)));
		/* Right */
		boxes[2] = new OctreeBoundingBox(min.plus(new Vector(width / 2, 0.0, 0.0)), middle.plus(new Vector(width / 2, 0.0, 0.0)));
		boxes[3] = new OctreeBoundingBox(min.plus(new Vector(width / 2, 0.0, depth / 2)), middle.plus(new Vector(width / 2, 0.0, depth / 2)));
		
		/* Top half */
		/* Left */
		boxes[4] = new OctreeBoundingBox(min.plus(new Vector(0.0, height / 2, 0.0)), middle.plus(new Vector(0.0, height / 2, 0.0)));
		boxes[5] = new OctreeBoundingBox(min.plus(new Vector(0.0, height / 2, depth / 2)), middle.plus(new Vector(0.0, height / 2, depth / 2)));
		/* Right */
		boxes[6] = new OctreeBoundingBox(min.plus(new Vector(width / 2, height / 2, 0.0)), middle.plus(new Vector(width / 2, height / 2, 0.0)));
		boxes[7] = new OctreeBoundingBox(middle, max);
		
		/* Store filled boxes. */
		OctreeBoundingBox filledBoxes[] = new OctreeBoundingBox[8];
		int numFilledBoxes = 0;
		
		/* Loop through sub-boxes. */
		for(int i = 0; i < 8; i++) {
			
			OctreeBoundingBox subbox = boxes[i];

			/* If there are triangles in the box, */
			int containedTriangles[] = getTrianglesInBoundingBox(subbox);
			if(containedTriangles.length > 0) {
				
				/* If it's a terminal node attach the triangles. */
				if(level == Mesh.OCTREE_LEVEL) {
					subbox.containedTriangles = containedTriangles;
					subbox.isTerminal = true;
				} else {
					subbox.isTerminal = false;
				}

				/* Add box to filledBoxes array. */
				filledBoxes[numFilledBoxes] = subbox;
				numFilledBoxes++;
				
			}
			
		}

		/* Attach filled subboxes. */
		box.subBoxes = Arrays.copyOfRange(filledBoxes, 0, numFilledBoxes);
		
		/* Recurse if level is not too high */
		if(level < Mesh.OCTREE_LEVEL) {
			
			for(int i = 0; i < box.subBoxes.length; i++) {
				splitBox(box.subBoxes[i], level + 1);
			}
			
		}
		
	}
	
	/*
	 * Check ray-mesh intersection.
	 */
	public Hit intersect(Ray ray) {
		
		/* Walk octree and */
		return intersect(ray, octree);

	}
	
	/*
	 * Recursive; check if ray intersects with octree.
	 */
	public Hit intersect(Ray ray, OctreeBoundingBox box) {
		
		/* Miss: don't recurse. */
		if(!box.doesIntersect(ray)) {
			return Hit.MISS;
		}
		
		/* Terminal box: iterate through triangles. */
		if(box.isTerminal) {
			return intersect(ray, box.containedTriangles);
			//return box.intersect(ray);
			
		}
		
		/* Otherwise: recurse, find nearest box. */
		Hit nearestHit = Hit.MISS;
		for(int i = 0; i < box.subBoxes.length; i++) {
			Hit hit = intersect(ray, box.subBoxes[i]);
			if(hit.hit && hit.distance < nearestHit.distance) {
				nearestHit = hit;
			}
		}
		
		if(nearestHit.hit)
			return nearestHit;
		else
			return Hit.MISS;
		
	}
	
	/*
	 * Ray intersection with list of triangles.
	 */
	public Hit intersect(Ray ray, int triangles[]) {

		Hit nearestHit = Hit.MISS;
		
		/* Loop through triangles */
		for(int i = 0; i < triangles.length; i++) {
			
			Vector p0 = vertexes[this.triangles[triangles[i]][0] - 1];
			Vector p1 = vertexes[this.triangles[triangles[i]][1] - 1];
			Vector p2 = vertexes[this.triangles[triangles[i]][2] - 1];
			
			/* Moller-Trumbore intersection algorithm */
			Vector edge1, edge2, h, s, q;
			double a, f, u, v;
			edge1 = p1.minus(p0);
			edge2 = p2.minus(p0);
			h = ray.direction.cross(edge2);
			a = edge1.dot(h);
			if(a > -Pathtracer.MIN_DISTANCE && a < Pathtracer.MIN_DISTANCE) {
				continue;
			}
			
			f = 1 / a;
			s = ray.origin.minus(p0);
			u = f * s.dot(h);
			if(u < 0.0 || u > 1.0) {
				continue;
			}
			
			q = s.cross(edge1);
			v = f * ray.direction.dot(q);
			
			if(v < 0.0 || u + v > 1.0) {
				continue;
			}
			
			double t = f * edge2.dot(q);
			if(t > Pathtracer.MIN_DISTANCE) {
				
				/* Isolate hit point. */
				Vector point = ray.point(t);
				
				/* Get surface normal. If it's on the wrong side, flip to ray side. */
				Vector normal = edge2.cross(edge1).normalized();
				if(ray.origin.minus(p0).dot(normal) < 0) {
					normal = normal.times(-1);
				}

				Hit hit = new Hit(true, point, t, normal, new Vector(u, v, 0.0));
				if(hit.distance < nearestHit.distance) {
					nearestHit = hit;
				}
				
			}
			
		}
		
		if(nearestHit.hit) 
			return nearestHit;
		
		return Hit.MISS;
		
	}
	
}
