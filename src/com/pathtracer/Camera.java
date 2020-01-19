package com.pathtracer;

/*
 * Camera and camera information.
 */
public class Camera {

	public double sensorSize;
	public double focalLength;
	public Vector position;
	
	public Vector lookingAt;
	public Vector up;
	
	public Camera(double sensorSize, double focalLength, Vector position, Vector lookingAt, Vector up) {
		this.sensorSize = sensorSize;
		this.focalLength = focalLength;
		this.position = position;
		this.lookingAt = lookingAt;
		this.up = up;
	}
	
}
