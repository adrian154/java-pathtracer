package com.pathtracer;

import com.pathtracer.geometry.Vector;

/*
 * Camera and camera information.
 */
public class Camera {

	public double focalLength;
	public Vector position;
	
	public Vector lookingAt;
	public Vector up;
	
	public Camera(double FOV, Vector position, Vector lookingAt, Vector up) {
		this.focalLength = 0.5 / Math.tan(FOV * Math.PI / 360);
		this.position = position;
		this.lookingAt = lookingAt;
		this.up = up;
	}
	
}
