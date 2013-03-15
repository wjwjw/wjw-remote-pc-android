package com.key;

/**
 * 3点指明一个空间向量
 * Stores 3 vectors representing a coordinate space.
 * 
 *
 */

public class PosCoordinateSpace {
	public PosPoint3D x;
	public PosPoint3D y;
	public PosPoint3D z;
	
	/**
	 * creates a default coordinaqte space
	 */
	
	public PosCoordinateSpace() {
		this.x = new PosPoint3D();
		this.x.x = 0;
		this.x.y = 1;
		this.x.z = 0;
		this.y = new PosPoint3D();
		this.y.x = 0;
		this.y.y = 0;
		this.y.z = 1;
		this.z = new PosPoint3D();
		this.z.x = 1;
		this.z.y = 0;
		this.z.z = 0;
	}
	
	/**
	 * creates a coordinate space from the given vectors. You're responsible for normalizing everything
	 * and making sure everything's in order.
	 * @param x
	 * @param y
	 * @param z
	 */
	
	public PosCoordinateSpace(PosPoint3D x, PosPoint3D y, PosPoint3D z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Creates a coordinate space from two vectors. You're responsible for making sure they're not the
	 * same vector, or that one's not just the reverse of the other.
	 * @param a
	 * @param m
	 */
	public PosCoordinateSpace(float[] a, float[] m) {
		PosPoint3D.copy(a, this.y = new PosPoint3D());
		this.z = new PosPoint3D();
		PosPoint3D.cross(a, m, this.z);
		this.x = new PosPoint3D();
		PosPoint3D.cross(this.z, this.y, this.x);
		// normalize everything
		PosPoint3D.normalize(this.x);
		PosPoint3D.normalize(this.y);
		PosPoint3D.normalize(this.z);
	}
	
	//
	
	public void setSpace(float[] a, float[] m) {
		//
		a[1] *= -1;
		//
		m[1] *= -1;
		m[2] *= -1;
		//
		PosPoint3D.copy(a, this.z);
		PosPoint3D.cross(a, m, this.y);
		PosPoint3D.cross(this.z, this.y, this.x);
		// normalize everything
		PosPoint3D.normalize(this.x);
		PosPoint3D.normalize(this.y);
		PosPoint3D.normalize(this.z);
	}
	
	public void setSpace(PosPoint3D a, PosPoint3D m) {
		// Make things match the screen space.
		a.y *= -1;
		//
		m.y *= -1;
		m.z *= -1;
		//
		PosPoint3D.copy(a, this.z);
		PosPoint3D.cross(a, m, this.y);
		PosPoint3D.cross(this.z, this.y, this.x);
		// normalize everything
		PosPoint3D.normalize(this.x);
		PosPoint3D.normalize(this.y);
		PosPoint3D.normalize(this.z);
		//
	}
	
	public void copy(PosCoordinateSpace c) {
		PosPoint3D.copy(c.x, this.x);
		PosPoint3D.copy(c.y, this.y);
		PosPoint3D.copy(c.z, this.z);
	}
}
