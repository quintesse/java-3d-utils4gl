/*
 * Created on Aug 31, 2003
 */
package org.codejive.utils4gl;

import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

/**
 * @author tako
 * @version $Revision: 166 $
 */
public class GLUtils {
	
	public static final double DEG2RAD = 2.0d * Math.PI / 360.0d;
	public static final double RAD2DEG = 360.0d / 2.0d * Math.PI;
	
	public static float[] getGLMatrix(Matrix3f _matrix) {
		Vector3f vec = new Vector3f();
		Matrix4f matrix = new Matrix4f(_matrix, vec, 1.0f);
		return getGLMatrix(matrix);
	}
	
	public static float[] getGLMatrix(Matrix4f _matrix) {
		float mat[] = new float[16];
		mat[0] = _matrix.m00;
		mat[1] = _matrix.m01;
		mat[2] = _matrix.m02;
		mat[3] = _matrix.m03;
		mat[4] = _matrix.m10;
		mat[5] = _matrix.m11;
		mat[6] = _matrix.m12;
		mat[7] = _matrix.m13;
		mat[8] = _matrix.m20;
		mat[9] = _matrix.m21;
		mat[10] = _matrix.m22;
		mat[11] = _matrix.m23;
		mat[12] = _matrix.m30;
		mat[13] = _matrix.m31;
		mat[14] = _matrix.m32;
		mat[15] = _matrix.m33;
		
		return mat;
	}
}

/*
 * $Log$
 * Revision 1.3  2003/12/01 22:37:51  tako
 * Added factors that convert degrees to radians and vise versa.
 *
 * Revision 1.2  2003/11/17 10:49:59  tako
 * Added CVS macros for revision and log.
 *
 */
