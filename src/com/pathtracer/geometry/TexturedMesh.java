package com.pathtracer.geometry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class TexturedMesh extends Mesh {
	
	/* Texture coordinates. */
	public Vector textureCoordinates[];
	
	/* Texture mappings. */
	public int textureMappings[][];
	
	/* Constructor -  load mesh. */
	public TexturedMesh(File file, double scale, Vector offset) {
		
		super(file, scale, offset);

		ArrayList<Vector> textureCoordinates = new ArrayList<Vector>();
		ArrayList<int[]> textureMappings = new ArrayList<int[]>();
		
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
				if(!parts[0].equals("vt") && !parts[0].equals("f"))
					continue;
				
				/* Validate the line. */
				//if(parts[0].equals("vt") && parts.length != 2) {
				//	System.out.println("error in \"" + file.getName() + "\" at line " + lineNum + ": wrong number of parameters for vertex (expected 2 but got " + parts.length + ")");
				//	System.exit(1);
				/*} else */ if(parts[0].equals("f") && parts.length < 4) {
					System.out.println("error in \"" + file.getName() + "\" at line " + lineNum + ": too few parameters for face (expected at least 4 but got " + parts.length + ")");
					System.exit(1);
				}
				
				if(parts[0].equals("vt")) {
					
					/* Load texture coordinate. */
					double d1 = Double.parseDouble(parts[1]);
					double d2 = Double.parseDouble(parts[2]);
					textureCoordinates.add(new Vector(d1, d2, 0.0));
					
				} else {
					
					/* Load polygon. */
					int indexes[] = new int[parts.length - 1];
		
					for(int vert = 0; vert < indexes.length; vert++) {
						int strIndex = parts[vert + 1].indexOf('/');
						
						int strIndex2 = parts[vert + 1].indexOf('/', strIndex + 1);
						strIndex2 = strIndex2 < 0 ? parts[vert + 1].length() : strIndex2;
						
						if(strIndex < 0) {
							System.out.println("error in \"" + file.getName() + "\" at line " + lineNum + ": no texture coordinates for face.");
							System.exit(1);
						}
						
						indexes[vert] = Integer.parseInt(parts[vert + 1].substring(strIndex + 1, strIndex2));
					}
					
					for(int vert = 0; vert < indexes.length - 2; vert++) {
						int mapping[] = new int[3];
						mapping[0] = indexes[vert];
						mapping[1] = indexes[vert + 1];
						mapping[2] = indexes[vert + 2];
						textureMappings.add(mapping);
					}
					
				}
				
			}
			
		} catch(Exception exception) {
			exception.printStackTrace();
			System.exit(1);
		}
		
		this.textureCoordinates = textureCoordinates.toArray(new Vector[textureCoordinates.size()]);
		this.textureMappings = textureMappings.toArray(new int[textureMappings.size()][3]);

	}
	
}
