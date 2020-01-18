package com.pathtracer;

public class Camera {

	public double sensorSize;
	public double focalLength;
	public Vector position;
	
	public Camera(double sensorSize, double focalLength, Vector position) {
		this.sensorSize = sensorSize;
		this.focalLength = focalLength;
		this.position = position;
	}
	
}
