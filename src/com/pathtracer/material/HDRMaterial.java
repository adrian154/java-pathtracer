package com.pathtracer.material;

import java.io.File;
import java.io.IOException;

import com.JavaHDR.HDREncoder;
import com.JavaHDR.HDRImage;
import com.JavaHDR.HDRImageRGB;
import com.pathtracer.geometry.Vector;

public class HDRMaterial implements Material {

	public HDRImageRGB texture;
	public Vector emission;
	public double diffuseness;
	public double glossiness;
	
	public HDRMaterial(HDRImageRGB texture, Vector emission, double diffuseness, double glossiness) {
		this.texture = texture;
		this.emission = emission;
		this.diffuseness = diffuseness;
		this.glossiness = glossiness;
	}
	
	public Vector getColor(double u, double v) {
		
		int x = Math.max(Math.min((int)Math.round(u * texture.getWidth()), texture.getWidth() - 1), 0);
		int y = Math.max(Math.min(texture.getHeight() - (int)Math.round(Math.floor(v * texture.getHeight())), texture.getHeight() - 1), 0);

		return new Vector(texture.getPixelValue(x, y, 0) * 2, texture.getPixelValue(x, y, 1) * 2, texture.getPixelValue(x, y, 2) * 2);
		
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

	public static HDRImageRGB loadTexture(String filename) {
		
		HDRImage image = null;
		
		try {
			image = HDREncoder.readHDR(new File(filename), true);
		} catch(IOException exception) {
			exception.printStackTrace();
			System.exit(1);
		}
		
		return (HDRImageRGB)image;
		
	}
	
}
