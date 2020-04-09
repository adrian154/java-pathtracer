package com.pathtracer.material;

import com.pathtracer.geometry.Vector;

public class MaterialHelper {

	public static Material createDiffuse(Vector color) {
		return new BasicMaterial(color, new Vector(0.0, 0.0, 0.0), 1.0, 0.0, false);
	}
	
}
