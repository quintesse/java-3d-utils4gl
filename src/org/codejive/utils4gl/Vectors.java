/*
 * Created on 25-mrt-2003
 */
package org.codejive.utils4gl;

import javax.vecmath.*;

/**
 * @author Tako
 * @version $Revision: 48 $
 */
public class Vectors {

	static final public Point3f POSF_CENTER = new Point3f(0.0f, 0.0f, 0.0f);

	static final public Point3d POSD_CENTER = new Point3d(0.0d, 0.0d, 0.0d);

	static final public Vector3f VECTF_ZERO = new Vector3f(0.0f, 0.0f, 0.0f);
	static final public Vector3f VECTF_CENTER = VECTF_ZERO;
	static final public Vector3f VECTF_LEFT = new Vector3f(1.0f, 0.0f, 0.0f);
	static final public Vector3f VECTF_UP = new Vector3f(0.0f, 1.0f, 0.0f);
	static final public Vector3f VECTF_OUT = new Vector3f(0.0f, 0.0f, 1.0f);

	static final public Vector3d VECTD_ZERO = new Vector3d(0.0d, 0.0d, 0.0d);
	static final public Vector3d VECTD_CENTER = VECTD_ZERO;
	static final public Vector3d VECTD_LEFT = new Vector3d(1.0d, 0.0d, 0.0d);
	static final public Vector3d VECTD_UP = new Vector3d(0.0d, 1.0d, 0.0d);
	static final public Vector3d VECTD_OUT = new Vector3d(0.0d, 0.0d, 1.0d);

	static final public Matrix3f MATRF_IDENTITY = new Matrix3f(1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f);
	
	static final public Matrix3d MATRD_IDENTITY = new Matrix3d(1.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d, 0.0d, 1.0d);

	public static Vector3f getScaledVector(Vector3f _v, float _fScale) {
		Vector3f scaledv = (Vector3f)_v.clone();
		scaledv.normalize();
		scaledv.scale(_fScale);
		return scaledv;	
	}
	
	public static void lookAt(Matrix4d mat, Point3d eye, Point3d target) {
		// Determine lookVector
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

		// Determine rightVector by performing the cross product of the two other vectors
		double rightVectorX = upVectorY * lookVectorZ - lookVectorY * upVectorZ;
		double rightVectorY = upVectorZ * lookVectorX - upVectorX * lookVectorZ;
		double rightVectorZ = upVectorX * lookVectorY - upVectorY * lookVectorX;

		// Normalize rightVector
		dNormFact = 1.0 / Math.sqrt(rightVectorX * rightVectorX + rightVectorY * rightVectorY + rightVectorZ * rightVectorZ);
		rightVectorX *= dNormFact;
		rightVectorY *= dNormFact;
		rightVectorZ *= dNormFact;

		// Determine upVector (anew/again?) by performing the cross product of the other two vectors
		// (why is this done? the rightVector being used itself is calculated using the cross product of upVec en lookVec)
		upVectorX = lookVectorY * rightVectorZ - rightVectorY * lookVectorZ;
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
 * Revision 1.3  2003/11/17 10:49:59  tako
 * Added CVS macros for revision and log.
 *
 */
