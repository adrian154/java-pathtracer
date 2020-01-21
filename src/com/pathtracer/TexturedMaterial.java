package com.pathtracer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TexturedMaterial implements Material {

	public BufferedImage texture;
	public Vector emission;
	public double diffuseness;
	public double glossiness;
	
	public TexturedMaterial(BufferedImage texture, Vector emission, double diffuseness, double glossiness) {
		this.texture = texture;
		this.emission = emission;
		this.diffuseness = diffuseness;
		this.glossiness = glossiness;
	}
	
	public Vector getColor(double u, double v) {
		
		int x = (int)Math.floor(u * texture.getWidth());
		int y = (int)Math.floor(v * texture.getHeight());
		int color =  texture.getRGB(x, y);
		int r = (color >> 16) & 0xFF;
		int g = (color >> 8) & 0xFF;
		int b = color & 0xFF;
		
		return new Vector(r / 255.0, g / 255.0, b / 255.0);
		
	}

	public Vector getEmission() {
		return emission;
	}

	public double getDiffuseness() {
		return diffuseness;
	}

	public double getGlossiness() {
		return glossiness;
	}

	public static BufferedImage loadTexture(String filename) {
		
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(new File(filename));
		} catch(IOException exception) {
			exception.printStackTrace();
			System.exit(1);
		}
		
		return image;
		
	}
	
}
