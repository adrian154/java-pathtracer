package com.pathtracer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import com.pathtracer.geometry.Vector;

/*
 * Output: Wrapper class for BufferedImage.
 */
public class Output {
	
	public Vector values[][];
	public BufferedImage image;
	public int width;
	public int height;
	
	public Output(int width, int height, boolean progressive) {

		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		this.width = width;
		this.height = height;
		
		if(progressive) {
			values = new Vector[width][height];
			for(int i = 0; i < width; i++) {
				for(int j = 0; j < height; j++) {
					values[i][j] = new Vector(0.0, 0.0, 0.0);
				}
			}
		}
	
	}
	
	/* Write pixel to location. */
	public void writePixel(int x, int y, Vector value) {

		/* Clamp value to 0 - 0xFF */
		int r = Math.min(0xFF, Math.max(0, (int)(value.x)));
		int g = Math.min(0xFF, Math.max(0, (int)(value.y)));
		int b = Math.min(0xFF, Math.max(0, (int)(value.z)));
		
		/* Create RGB int and write */
		int rgb = (r << 16) | (g << 8) | (b);
		image.setRGB(x, this.height - (y + 1), rgb);	
	
	}
	
	/* Write image to file. */
	public void writeToFile(String filename) {
	
		File imageFile = new File(filename);
		
		try {
			ImageIO.write(image, "png", imageFile);
		} catch(IOException exception) {
			exception.printStackTrace();
		}
		
	}

}
