package com.pathtracer;

public class Material {

	public Vector color;
	public Vector emissiveColor;
	public double diffuseProb;

	public Material() {
		
	}
	
	public Material(Vector color, Vector emissiveColor, double diffuseProb) {
		this.color = color;
		this.emissiveColor = emissiveColor;
		this.diffuseProb = diffuseProb;
	}
	
	public static Vector getDiffuseVector(Vector normal) {
		
		/* Get random vector in hemisphere */
		Vector randomVector = Vector.randomInHemisphere();
		
		/* Create basis vectors for new coordinate system where Y is normal */
		Vector bvy = normal;
		Vector bvx = bvy.getOrthagonal();
		Vector bvz = bvy.cross(bvx);
				
		/* Convert to world coords */
		return Transforms.localToWorldCoords(randomVector, bvx, bvy, bvz);
		
	}
	
	public static Vector getReflectionVector(Vector normal, Vector incident) {
		return incident.minus(normal.times(2 * incident.dot(normal)));
	}
	
}
