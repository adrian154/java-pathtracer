package com.pathtracer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Mesh implements Shape {

	public Vector[] vertexes;
	public int[][] triangles;
	
	public Mesh(File file) {
		
		try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
			
			/* Read file line by line. */
			String line;
			int lineNum = 0;
			
			while((line = reader.readLine()) != null) {
			
				/* Skip comments */
				if(line.substring(0, 1).equals("#")) {
					continue;
				}
				
				/* Split line */
				String[] parts = line.split(" ");
				
				/* Skip unsupported types (UVs, normals, etc) */
				if(parts[0] != "v" && parts[0] != "f") {
					continue;
				}
				
				/* Validate */
				if(parts.length != 4) {
					System.out.println("error in \"" + file.getName() + "\" at line " + lineNum + ": wrong number of parameters (expected 4)");
					return;
				}
				
				
				
				lineNum++;
-			}
			
		} catch(Exception exception) {
			exception.printStackTrace();
		}
		
	}
	
	@Override
	public Hit intersect(Ray ray) {
		
		
		
		return null;
	}

}
