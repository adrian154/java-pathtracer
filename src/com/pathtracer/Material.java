package com.pathtracer;

public class Material {

	public Vector diffuseColor;
	public Vector emissiveColor;
	
	public Material() {
		
	}
	
	public Material(Vector diffuseColor, Vector emissiveColor) {
		this.diffuseColor = diffuseColor;
		this.emissiveColor = emissiveColor;
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
	
}
