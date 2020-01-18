package com.pathtracer;

public class Material {

	public Vector color;
	public Vector emissiveColor;
	public double diffuseProb;
	public double glossiness;
	
	public Material() {
		
	}
	
	public Material(Vector color, Vector emissiveColor, double diffuseProb, double glossiness) {
		this.color = color;
		this.emissiveColor = emissiveColor;
		this.diffuseProb = diffuseProb;
		this.glossiness = glossiness;
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
	
	public static Vector getReflectionVector(Vector normal, Vector incident, double glossiness) {
		Vector reflect = incident.minus(normal.times(2 * incident.dot(normal)));
		
		if(glossiness > 0.0) {
			return reflect.plus(Material.getDiffuseVector(reflect).times(glossiness)).normalized();
		} else {
			return reflect;
		}
	}
	
}
