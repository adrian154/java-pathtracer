package com.pathtracer.geometry;

import java.text.DecimalFormat;

/*
 * Vector: Class for 3D vector.
 */
public class Vector {
	
	public double x, y, z;
	
	public Vector() {
		
	}
	
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector plus(Vector other) {
		return new Vector(this.x + other.x, this.y + other.y, this.z + other.z);
	}
	
	public Vector minus(Vector other) {
		return new Vector(this.x - other.x, this.y - other.y, this.z - other.z);
	}

	/* SHOULD ONLY BE USED ON COLORS */
	public Vector times(Vector other) {
		return new Vector(this.x * other.x, this.y * other.y, this.z * other.z);
	}
	
	public Vector times(double scalar) {
		return new Vector(this.x * scalar, this.y * scalar, this.z * scalar);
	}
	
	public Vector divBy(Vector other) {
		return new Vector(this.x / other.x, this.y / other.y, this.z / other.z);
	}
	
	public Vector divBy(double scalar) {
		return new Vector(this.x / scalar, this.y / scalar, this.z / scalar);
	}
	
	/* Faster than length. */
	public double lengthSquared() {
		return this.x * this.x + this.y * this.y + this.z * this.z;
	}
	
	public double length() {
		return Math.sqrt(this.lengthSquared());
	}
	
	public Vector normalized() {
		return this.times(1 / this.length());
	}
	
	public Vector cross(Vector other) {
		return new Vector(
			this.y * other.z - this.z * other.y,
			this.z * other.x - this.x * other.z,
			this.x * other.y - this.y * other.x
		);
	}
	
	public double dot(Vector other) {
		return this.x * other.x + this.y * other.y + this.z * other.z;
	}
	
	public Vector getOrthagonal() {
		Vector other;
		if(this.y != 0 || this.z != 0) {
			other = new Vector(1, 0, 0);
		} else {
			other = new Vector(0, 1, 0);
		}
		return this.cross(other);
	}
	
	public String toString() {
		return new String("(" + this.x + ", " + this.y + ", " + this.z + ")");
	}
	
	public static Vector uniformInHemisphere() {
		double theta = 2 * Math.PI * Math.random();
		double phi = Math.acos(1 - 2 * Math.random());
		return new Vector(
			Math.sin(phi) * Math.cos(theta),
			Math.abs(Math.cos(phi)),
			Math.sin(theta) * Math.sin(phi)
		);
	}
	
	public static Vector cosineWeightedInHemisphere() {
		double theta = 2 * Math.PI * Math.random();
		double phi = Math.random() * Math.PI / 2;
		return new Vector(
			Math.sin(phi) * Math.cos(theta),
			Math.abs(Math.cos(phi)),
			Math.sin(theta) * Math.sin(phi)
		);
	}
	
	public static Vector fromSpherical(double theta, double phi) {
		theta *= Math.PI / 180;
		phi *= Math.PI / 180;
		return new Vector(
				Math.sin(phi) * Math.cos(theta),
				Math.abs(Math.cos(phi)),
				Math.sin(theta) * Math.sin(phi)
			);
	}
	
}
