package com.pathtracer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/*
 * Output: Wrapper class for BufferedImage.
 */
public class Output {
	
	public BufferedImage image;
	public int width;
	public int height;
	
	public Output(int width, int height) {

		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		this.width = width;
		this.height = height;
	
	}
	
	public void writePixel(int x, int y, Vector value) {

		/* Clamp value to 0 - 0xFF */
		int r = Math.min(0xFF, Math.max(0, (int)(value.x)));
		int g = Math.min(0xFF, Math.max(0, (int)(value.y)));
		int b = Math.min(0xFF, Math.max(0, (int)(value.z)));
		
		/* Create RGB int and write */
		int rgb = (r << 16) | (g << 8) | (b);
		image.setRGB(x, y, rgb);	
	
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
