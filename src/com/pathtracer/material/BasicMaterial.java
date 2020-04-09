package com.pathtracer.material;

import com.pathtracer.geometry.Vector;

public class BasicMaterial implements Material {

	public Vector color;
	public Vector emission;
	public double diffuseness;
	public double glossiness;
	public boolean plastic;
	
	public BasicMaterial(Vector color, Vector emission, double diffuseness, double glossiness, boolean plastic) {
		this.color = color;
		this.emission = emission;
		this.diffuseness = diffuseness;
		this.glossiness = glossiness;
		this.plastic = plastic;
	}
	
	public Vector getColor(double u, double v) {
		return color;
	}

	public Vector getEmission(double u, double v) {
		return emission;
	}

	public double getDiffuseness() {
		return diffuseness;
	}

	public double getGlossiness() {
		return glossiness;
	}
	
	public boolean isPlastic() {
		return plastic;
	}

}
