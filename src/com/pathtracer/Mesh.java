package com.pathtracer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Mesh implements Shape {

	public Vector[] vertexes;
	public int[][] triangles;
	
	public Mesh(File file) {
		
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

				/* Validate */
				if(parts.length != 4) {
					System.out.println("error in \"" + file.getName() + "\" at line " + lineNum + ": wrong number of parameters (expected 4)");
					return;
				}
				
				if(parts[0].equals("v")) {
					double d1 = Double.parseDouble(parts[1]);
					double d2 = Double.parseDouble(parts[2]);
					double d3 = Double.parseDouble(parts[3]);
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
	
	@Override
	public Hit intersect(Ray ray) {

		Hit nearestHit = new Hit(false, new Vector(), Double.POSITIVE_INFINITY, new Vector());
		
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
		
		return new Hit(false, new Vector(), Double.POSITIVE_INFINITY, new Vector());
		
	}

}
