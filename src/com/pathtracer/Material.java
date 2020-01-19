package com.pathtracer;

/*
 * Material information.
 */
public class Material {

	public Vector color;
	public Vector emissiveColor;
	public double diffuseness;
	public double glossiness;
	
	public Material() {
		
	}
	
	public Material(Vector color, Vector emissiveColor, double diffuseness, double glossiness) {
		this.color = color;
		this.emissiveColor = emissiveColor;
		this.diffuseness = diffuseness;
		this.glossiness = glossiness;
	}
	
	/*
	 * Static method. Get random diffuse vector.
	 */
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
	
	/*
	 * Static method. Get random reflection vector.
	 */
	public static Vector getReflectionVector(Vector normal, Vector incident, double glossiness) {
		Vector reflect = incident.minus(normal.times(2 * incident.dot(normal)));
		
		if(glossiness > 0.0) {
			return reflect.plus(Material.getDiffuseVector(reflect).times(glossiness)).normalized();
		} else {
			return reflect;
		}
	}
	
}
