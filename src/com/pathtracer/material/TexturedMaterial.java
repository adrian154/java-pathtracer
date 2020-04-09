package com.pathtracer.material;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.pathtracer.geometry.Vector;

public class TexturedMaterial implements Material {

	public BufferedImage texture;
	public Vector emission;
	public double diffuseness;
	public double glossiness;
	public boolean plastic;
	
	public TexturedMaterial(BufferedImage texture, Vector emission, double diffuseness, double glossiness, boolean plastic) {
		this.texture = texture;
		this.emission = emission;
		this.diffuseness = diffuseness;
		this.glossiness = glossiness;
		this.plastic = plastic;
	}
	
	public Vector getColor(double u, double v) {
		
		int x = Math.max(Math.min((int)Math.round(u * texture.getWidth()), texture.getWidth() - 1), 0);
		int y = Math.max(Math.min(texture.getHeight() - (int)Math.round(Math.floor(v * texture.getHeight())), texture.getHeight() - 1), 0);
		int color =  texture.getRGB(x, y);
		int r = (color >> 16) & 0xFF;
		int g = (color >> 8) & 0xFF;
		int b = color & 0xFF;
		
		return new Vector(r / 255.0, g / 255.0, b / 255.0);
		
	}

	public Vector getEmission(double u, double v) {
		return emission.times(getColor(u, v));
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
	
	public boolean isPlastic() {
		return plastic;
	}
	
}
