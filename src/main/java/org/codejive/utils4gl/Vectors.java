/*
 * [utils4gl] OpenGL utilities library
 * 
 * Copyright (C) 2003 Tako Schotanus
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Created on 25-mrt-2003
 */
package org.codejive.utils4gl;

import javax.vecmath.*;

/** Helper class for Vectors. Has lots of useful constants and a
 * couple of static methods that make working with Vectors a bit
 * easier.
 * @author Tako
 * @version $Revision: 165 $
 */
public class Vectors {

	/** Point (0, 0, 0) using floats
	 */	
	static final public Point3f POSF_CENTER = new Point3f(0.0f, 0.0f, 0.0f);

	/** Point (0, 0, 0) using doubles
	 */	
	static final public Point3d POSD_CENTER = new Point3d(0.0d, 0.0d, 0.0d);

	/** Vector (0, 0, 0) using floats
	 */	
	static final public Vector3f VECTF_ZERO = new Vector3f(0.0f, 0.0f, 0.0f);
	/** Vector (0, 0, 0) using floats
	 */	
	static final public Vector3f VECTF_CENTER = VECTF_ZERO;
	/** Vector (1, 0, 0) using floats
	 */	
	static final public Vector3f VECTF_LEFT = new Vector3f(1.0f, 0.0f, 0.0f);
	/** Vector (-1, 0, 0) using floats
	 */	
	static final public Vector3f VECTF_RIGHT = new Vector3f(-1.0f, 0.0f, 0.0f);
	/** Vector (0, 1, 0) using floats
	 */	
	static final public Vector3f VECTF_UP = new Vector3f(0.0f, 1.0f, 0.0f);
	/** Vector (0, -1, 0) using floats
	 */	
	static final public Vector3f VECTF_DOWN = new Vector3f(0.0f, -1.0f, 0.0f);
	/** Vector (0, 0, 1) using floats
	 */	
	static final public Vector3f VECTF_OUT = new Vector3f(0.0f, 0.0f, 1.0f);
	/** Vector (0, 0, -1) using floats
	 */	
	static final public Vector3f VECTF_IN = new Vector3f(0.0f, 0.0f, -1.0f);

	/** Vector (0, 0, 0) using doubles
	 */	
	static final public Vector3d VECTD_ZERO = new Vector3d(0.0d, 0.0d, 0.0d);
	/** Vector (0, 0, 0) using doubles
	 */	
	static final public Vector3d VECTD_CENTER = VECTD_ZERO;
	/** Vector (1, 0, 0) using doubles
	 */	
	static final public Vector3d VECTD_LEFT = new Vector3d(1.0d, 0.0d, 0.0d);
	/** Vector (-1, 0, 0) using doubles
	 */	
	static final public Vector3d VECTD_RIGHT = new Vector3d(-1.0d, 0.0d, 0.0d);
	/** Vector (0, 1, 0) using doubles
	 */	
	static final public Vector3d VECTD_UP = new Vector3d(0.0d, 1.0d, 0.0d);
	/** Vector (0, -1, 0) using doubles
	 */	
	static final public Vector3d VECTD_DOWN = new Vector3d(0.0d, -1.0d, 0.0d);
	/** Vector (0, 0, 1) using doubles
	 */	
	static final public Vector3d VECTD_OUT = new Vector3d(0.0d, 0.0d, 1.0d);
	/** Vector (0, 0, -1) using doubles
	 */	
	static final public Vector3d VECTD_IN = new Vector3d(0.0d, 0.0d, -1.0d);

	/** A standard identity matrix using floats.
	 */	
	static final public Matrix3f MATRF_IDENTITY = new Matrix3f(1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f);
	
	/** A standard identity matrix using doubles.
	 */	
	static final public Matrix3d MATRD_IDENTITY = new Matrix3d(1.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d, 0.0d, 1.0d);

	/** Multilies the given vector by the given scalar.
	 * @param _v The Vector to scale
	 * @param _fScale The factor by which to multiply the vector
	 * @return A new Vector contaiing the scaled result
	 */	
	public static Vector3f getScaledVector(Vector3f _v, float _fScale) {
		return getScaledVector(_v, _fScale, new Vector3f());	
	}
	
	/** Multilies the given vector by the given scalar.
	 * @param _v The Vector to scale
	 * @param _fScale The factor by which to multiply the vector
	 * @param _result The Vector where the result should be stored
	 * @return A reference to the result vector
	 */	
	public static Vector3f getScaledVector(Vector3f _v, float _fScale, Vector3f _result) {
		_result.set(_v);
		_result.normalize();
		_result.scale(_fScale);
		return _result;	
	}
	
	/** Rotates a vector the given amount around the X, Y and Z axis.
	 * @param _v The Vector to rotate
	 * @param _fRotX The angle of rotation around the X axis 
	 * @param _fRotY The angle of rotation around the Y axis 
	 * @param _fRotZ The angle of rotation around the Z axis 
	 * @return A new Vector contaiing the rotated result
	 */	
	public static Vector3f rotateVector(Vector3f _v, float _fRotX, float _fRotY, float _fRotZ) {
		return rotateVector(_v, _fRotX, _fRotY, _fRotZ, new Vector3f());
	}
	
	/** Rotates a vector the given amount around the X, Y and Z axis.
	 * @param _v The Vector to rotate
	 * @param _fRotX The angle of rotation around the X axis 
	 * @param _fRotY The angle of rotation around the Y axis 
	 * @param _fRotZ The angle of rotation around the Z axis 
	 * @param _result The Vector where the result should be stored
	 * @return A reference to the result vector
	 */	
	public static Vector3f rotateVector(Vector3f _v, float _fRotX, float _fRotY, float _fRotZ, Vector3f _result) {
		Matrix4f rm = new Matrix4f();
		Matrix4f m = new Matrix4f();
		rm.setIdentity();
//		m.rotZ(_orientation.z * (float)GLUtils.DEG2RAD);
//		rm.mul(m);
		m.rotY(_fRotY * (float)GLUtils.DEG2RAD);
		rm.mul(m);
		m.rotX(_fRotX * (float)GLUtils.DEG2RAD);
		rm.mul(m);
		rm.transform(_v, _result);
		return _result;
	}
	
	/**
	 */	
	public static void lookAt(Matrix4d mat, Point3d eye, Point3d target) {
		// Determine lookVector which will be the new negative Z axis of transformed object
		double lookVectorX = eye.x - target.x;
		double lookVectorY = eye.y - target.y;
		double lookVectorZ = eye.z - target.z;

		// Normalize lookVector
		double dNormFact = 1.0 / Math.sqrt(lookVectorX * lookVectorX + lookVectorY * lookVectorY + lookVectorZ * lookVectorZ);
		lookVectorX *= dNormFact;
		lookVectorY *= dNormFact;
		lookVectorZ *= dNormFact;

		double upVectorX;
		double upVectorY;
		double upVectorZ;
		
		//First try at making a View Up vector: Use Y axis default  (0,1,0)
		upVectorX = 0 - lookVectorY * lookVectorX;
		upVectorY = 1 - lookVectorY * lookVectorY;
		upVectorZ = 0 - lookVectorY * lookVectorZ;

		// Check for validity:
		double UpMagnitude = lookVectorX * lookVectorX + lookVectorY * lookVectorY + lookVectorZ * lookVectorZ;

		if (UpMagnitude < .0000001) {
			//Final try at making a View Up vector: Use Z axis default  (0,0,1)
			upVectorX = 0 - lookVectorZ * lookVectorX;
			upVectorY = 0 - lookVectorZ * lookVectorY;
			upVectorZ = 1 - lookVectorZ * lookVectorZ;

			// Check for validity:
			UpMagnitude = lookVectorX * lookVectorX + lookVectorY * lookVectorY + lookVectorZ * lookVectorZ;

			if (UpMagnitude < .0000001)
				return;
		}

		// Normalize upVector
		dNormFact = 1.0 / Math.sqrt(upVectorX * upVectorX + upVectorY * upVectorY + upVectorZ * upVectorZ);
		upVectorX = upVectorX * dNormFact;
		upVectorY = upVectorY * dNormFact;
		upVectorZ = upVectorZ * dNormFact;

		// Determine rightVector which will be the new X axis of our transformed object
		// by performing the cross product of the two other vectors
		double rightVectorX = upVectorY * lookVectorZ - upVectorZ * lookVectorY;
		double rightVectorY = upVectorZ * lookVectorX - upVectorX * lookVectorZ;
		double rightVectorZ = upVectorX * lookVectorY - upVectorY * lookVectorX;

		// Normalize rightVector
		dNormFact = 1.0 / Math.sqrt(rightVectorX * rightVectorX + rightVectorY * rightVectorY + rightVectorZ * rightVectorZ);
		rightVectorX *= dNormFact;
		rightVectorY *= dNormFact;
		rightVectorZ *= dNormFact;

		// Determine the new upVector which will be the new Y axis of our transformed object
		// by performing the cross product of the other two vectors
		upVectorX = lookVectorY * rightVectorZ - lookVectorZ * rightVectorY;
		upVectorY = lookVectorZ * rightVectorX - lookVectorX * rightVectorZ;
		upVectorZ = lookVectorX * rightVectorY - lookVectorY * rightVectorX;

		mat.m00 = rightVectorX;
		mat.m01 = rightVectorY;
		mat.m02 = rightVectorZ;
		mat.m10 = upVectorX;
		mat.m11 = upVectorY;
		mat.m12 = upVectorZ;
		mat.m20 = lookVectorX;
		mat.m21 = lookVectorY;
		mat.m22 = lookVectorZ;
		mat.m03 = -eye.x * mat.m00 + -eye.y * mat.m01 + -eye.z * mat.m02;
		mat.m13 = -eye.x * mat.m10 + -eye.y * mat.m11 + -eye.z * mat.m12;
		mat.m23 = -eye.x * mat.m20 + -eye.y * mat.m21 + -eye.z * mat.m22;
		mat.m30 = mat.m31 = mat.m32 = 0.0;
		mat.m33 = 1.0;
	}
}

/*
 * $Log$
 * Revision 1.5  2003/12/01 22:36:56  tako
 * All code is now subject to the Lesser GPL.
 * Added opposite vector constants.
 * Added rotateVector() methods.
 *
 * Revision 1.4  2003/11/20 16:23:27  tako
 * Updated Java docs.
 *
 * Revision 1.3  2003/11/17 10:49:59  tako
 * Added CVS macros for revision and log.
 *
 */
