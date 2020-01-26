package com.pathtracer.material;

import com.pathtracer.geometry.Transforms;
import com.pathtracer.geometry.Vector;

/*
 * Material information.
 */
public interface Material {

	public Vector getColor(double u, double v);
	public Vector getEmission();
	public double getDiffuseness();
	public double getGlossiness();

	/*
	 * Static method. Get random diffuse vector.
	 */
	public static Vector getDiffuseVector(Vector normal) {
		
		/* Get random vector in hemisphere */
		Vector randomVector = Vector.cosineWeightedInHemisphere();
		
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
