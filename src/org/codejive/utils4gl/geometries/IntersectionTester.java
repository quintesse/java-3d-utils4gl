/*
 * Created on 14-mrt-2003
 */
package org.codejive.utils4gl;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/*
 * @version $Revision: 187 $
 */
public class Intersections {

	/** A point that we use for working calculations (vertex transforms) */
	private Intersection m_workIntersect;
	private Vector3d m_workVector;

	/** Working vectors */
	private Vector3d m_tmpVector0;
	private Vector3d m_tmpVector1;
	private Vector3d m_normal;
	private Vector3d m_diffVector;

	/** The current 2D vertex list that we work from */
	private float[] m_working2dVertices;

	/** Temporay space for a single quad polygon */
	private float[] m_tmpPolygon;

	/** This class contains all information about an intersection. 
	 * It is returned by some of the functions in this class.
	 */
	 public static class Intersection {
		/** Indicates if an intersection was found. 
		 * The other members are only valid, if this field has a value of true.
		 */
		public boolean isIntersecting;
		/** The point at which an intersection was found
		 */
		public Point3d point = new Point3d();
		/** The normal at the which the vector intersects at the point found.
		 */
		public Vector3d normal = new Vector3d();
		/** The distance from the point passed in to the intersection point.
		 */
		public double distance = 0.0;
  
		public Intersection() {
			// No initialization necessary
		}

		public Intersection(Intersection _source) {
			set(_source);
		}
		
		public void set(Intersection _source) {
			isIntersecting = _source.isIntersecting;
			point = _source.point;
			normal = _source.normal;
			distance = _source.distance;
		}
	}

	/**
	 * Create a default instance of this class with no internal data
	 * structures allocated.
	 */
	public Intersections() {
		m_workIntersect = new Intersection();
		m_workVector = new Vector3d();
		m_tmpVector0 = new Vector3d();
		m_tmpVector1 = new Vector3d();
		m_normal = new Vector3d();
		m_diffVector = new Vector3d();
		m_tmpPolygon = new float[12];

		m_working2dVertices = new float[8];
	}

	/**
	 * Clear the current internal structures to reduce the amount of memory
	 * used. It is recommended you use this method with caution as then next
	 * time a user calls this class, all the internal structures will be
	 * reallocated. If this is running in a realtime environment, that could
	 * be very costly - both allocation and the garbage collection that
	 * results from calling this method
	 */
	public void clear() {
		m_working2dVertices = new float[8];
	}

	/** Test an array of triangles for intersection. Returns the closest
	 * intersection point to the origin of the picking ray. Assumes that the
	 * vertices are ordered as [Xn, Yn, Zn] and are translated into the same
	 * coordinate system that the the origin and direction are from.
	 *
	 * @param _origin The origin of the ray
	 * @param _direction The direction of the ray
	 * @param _fLength An optional length for to make the ray a segment. If the value is zero, it is ignored
	 * @param _vertices The vertices of the triangles
	 * @param _nTriangleCount The number of triangles to use from the array
	 * @param _bAnyIntersect True if we just want to know if any intersection occurs
	 * @param _intersection Will hold the details of any intersection found. The isIntersecting member will be false if no intersection was found.
	 * @return true if there was an intersection, false if not
	 */
	public boolean intersectTriangleArray(Point3d _origin, Vector3d _direction, float _fLength, float[] _vertices, int _nTriangleCount, boolean _bAnyIntersect, Intersection _intersection) {
		if (_vertices.length < _nTriangleCount * 9)
			throw new IllegalArgumentException("Not enough vertices for the given number of triangles");

		double shortest_length = -1;
		double this_length;

		// Make length squared because we do all comparisons using squared distances
		_fLength *= _fLength;
		
		for (int i = 0; i < _nTriangleCount; i++) {
			System.arraycopy(_vertices, i * 9, m_tmpPolygon, 0, 9);

			if (intersectPolygonChecked(_origin, _direction, 0.0f, m_tmpPolygon, 3, m_workIntersect)) {
				m_diffVector.sub(_origin, m_workIntersect.point);

				this_length = m_diffVector.lengthSquared();

				if (((_fLength == 0) || (this_length <= _fLength)) && ((shortest_length == -1) || (this_length < shortest_length))) {
					shortest_length = this_length;
					_intersection.set(m_workIntersect);
					if (_bAnyIntersect)
						break;
				}
			}
		}

		_intersection.isIntersecting = (shortest_length != -1);

		return _intersection.isIntersecting;
	}

	/** Test an array of triangles for intersection. Returns the closest
	 * intersection point to the origin of the picking ray. Assumes that the
	 * vertices are ordered as [Xn, Yn, Zn] and are translated into the same
	 * coordinate system that the the origin and direction are from.
	 *
	 * @param _origin The origin of the ray
	 * @param _direction The direction of the ray
	 * @param _fLength An optional length for to make the ray a segment. If the value is zero, it is ignored
	 * @param _vertices The vertices of the triangles
	 * @param _nTriangleCount The number of triangles to use from the array
	 * @param _bAnyIntersect True if we just want to know if any intersection occurs
	 * @return the details of any intersection found. The isIntersecting member will be false if no intersection was found. 
	 */
	public Intersection intersectTriangleArray(Point3d _origin, Vector3d _direction, float _fLength, float[] _vertices, int _nTriangleCount, boolean _bAnyIntersect) {
		Intersection intersection = new Intersection();
		intersectTriangleArray(_origin, _direction, _fLength, _vertices, _nTriangleCount, _bAnyIntersect, intersection);
		return intersection;
	}

	/** Test an array of quads for intersection. Returns the closest
	 * intersection point to the origin of the picking ray. Assumes that the
	 * vertices are ordered as [Xn, Yn, Zn] and are translated into the same
	 * coordinate system that the the origin and direction are from.
	 *
	 * @param _origin The origin of the ray
	 * @param _direction The direction of the ray
	 * @param _fLength An optional length for to make the ray a segment. If the value is zero, it is ignored
	 * @param _vertices The vertices of the quads
	 * @param _nQuadCount The number of quads to use from the array
	 * @param _bAnyIntersect True if we just want to know if any intersection occurs
	 * @param _intersection Will hold the details of any intersection found. The isIntersecting member will be false if no intersection was found.
	 * @return true if there was an intersection, false if not
	 */
	public boolean rayQuadArray(Point3d _origin, Vector3d _direction, float _fLength, float[] _vertices, int _nQuadCount, boolean _bAnyIntersect, Intersection _intersection) {
		if (_vertices.length < _nQuadCount * 12)
			throw new IllegalArgumentException("Not enough vertices for the given number of quads");

		double shortest_length = -1;
		double this_length;

		for (int i = 0; i < _nQuadCount; i++) {
			System.arraycopy(_vertices, i * 12, m_tmpPolygon, 0, 12);

			if (intersectPolygonChecked(_origin, _direction, _fLength, m_tmpPolygon, 4, m_workIntersect)) {
				m_diffVector.sub(_origin, m_workIntersect.point);

				this_length = m_diffVector.lengthSquared();

				if ((shortest_length == -1) || (this_length < shortest_length)) {
					shortest_length = this_length;
					_intersection.set(m_workIntersect);
					if (_bAnyIntersect)
						break;
				}
			}
		}

		_intersection.isIntersecting = (shortest_length != -1);

		return _intersection.isIntersecting;
	}

	/** Test an array of quads for intersection. Returns the closest
	 * intersection point to the origin of the picking ray. Assumes that the
	 * vertices are ordered as [Xn, Yn, Zn] and are translated into the same
	 * coordinate system that the the origin and direction are from.
	 *
	 * @param _origin The origin of the ray
	 * @param _direction The direction of the ray
	 * @param _fLength An optional length for to make the ray a segment. If the value is zero, it is ignored
	 * @param _vertices The vertices of the quads
	 * @param _nQuadCount The number of quads to use from the array
	 * @param _bAnyIntersect True if we just want to know if any intersection occurs
	 * @return the details of any intersection found. The isIntersecting member will be false if no intersection was found. 
	 */
	public Intersection rayQuadArray(Point3d _origin, Vector3d _direction, float _fLength, float[] _vertices, int _nQuadCount, boolean _bAnyIntersect) {
		Intersection intersection = new Intersection();
		rayQuadArray(_origin, _direction, _fLength, _vertices, _nQuadCount, _bAnyIntersect, intersection);
		return intersection;
	}
	
	/** Test an array of triangles strips for intersection. Returns the closest
	 * intersection point to the origin of the picking ray. Assumes that the
	 * vertices are ordered as [Xn, Yn, Zn] and are translated into the same
	 * coordinate system that the the origin and direction are from.
	 *
	 * @param _origin The origin of the ray
	 * @param _direction The direction of the ray
	 * @param _fLength An optional length for to make the ray a segment. If the value is zero, it is ignored
	 * @param _vertices The vertices of the triangles
	 * @param _stripTriangleCount The number of polygons in each strip
	 * @param _nStripCount The number of strips to use from the array
	 * @param _bAnyIntersect True if we just want to know if any intersection occurs
	 * @param _intersection Will hold the details of any intersection found. The isIntersecting member will be false if no intersection was found.
	 * @return true if there was an intersection, false if not
	 */
	public boolean intersectTriangleStripArray(Point3d _origin, Vector3d _direction, float _fLength, float[] _vertices, int[] _stripTriangleCount, int _nStripCount, boolean _bAnyIntersect, Intersection _intersection) {
		// Add all the strip lengths up first
		int total_vertices = 0;

		for (int i = _nStripCount; --i >= 0;)
			total_vertices += _stripTriangleCount[i];

		if (_vertices.length < total_vertices * 3)
			throw new IllegalArgumentException("Not enough vertices for the given triangle strips");

		double shortest_length = -1;
		double this_length;
		int offset = 0;

		for (int i = 0; i < _nStripCount; i++) {
			offset = i * _stripTriangleCount[i] * 3;

			for (int j = 0; j < _stripTriangleCount[i] - 2; j++) {
				System.arraycopy(_vertices, offset + j * 3, m_tmpPolygon, 0, 9);

				if (intersectPolygonChecked(_origin, _direction, _fLength, m_tmpPolygon, 3, m_workIntersect)) {
					m_diffVector.sub(_origin, m_workIntersect.point);

					this_length = m_diffVector.lengthSquared();

					if ((shortest_length == -1) || (this_length < shortest_length)) {
						shortest_length = this_length;
						_intersection.set(m_workIntersect);
						if (_bAnyIntersect)
							break;
					}
				}
			}
		}

		_intersection.isIntersecting = (shortest_length != -1);

		return _intersection.isIntersecting;
	}

	/** Test an array of triangles strips for intersection. Returns the closest
	 * intersection point to the origin of the picking ray. Assumes that the
	 * vertices are ordered as [Xn, Yn, Zn] and are translated into the same
	 * coordinate system that the the origin and direction are from.
	 *
	 * @param _origin The origin of the ray
	 * @param _direction The direction of the ray
	 * @param _fLength An optional length for to make the ray a segment. If the value is zero, it is ignored
	 * @param _vertices The vertices of the triangles
	 * @param _stripTriangleCount The number of polygons in each strip
	 * @param _nStripCount The number of strips to use from the array
	 * @param _bAnyIntersect True if we just want to know if any intersection occurs
	 * @return the details of any intersection found. The isIntersecting member will be false if no intersection was found. 
	 */
	public Intersection intersectTriangleStripArray(Point3d _origin, Vector3d _direction, float _fLength, float[] _vertices, int[] _stripTriangleCount, int _nStripCount, boolean _bAnyIntersect) {
		Intersection intersection = new Intersection();
		intersectTriangleStripArray(_origin, _direction, _fLength, _vertices, _stripTriangleCount, _nStripCount, _bAnyIntersect, intersection);
		return intersection;
	}

	/** Test an array of triangle fans for intersection. Returns the closest
	 * intersection point to the origin of the picking ray. Assumes that the
	 * vertices are ordered as [Xn, Yn, Zn] and are translated into the same
	 * coordinate system that the the origin and direction are from.
	 *
	 * @param _origin The origin of the ray
	 * @param _direction The direction of the ray
	 * @param _fLength An optional length for to make the ray a segment. If the value is zero, it is ignored
	 * @param _vertices The vertices of the triangles
	 * @param _fanTriangleCounts The number of polygons in each fan
	 * @param _nFanCount The number of fans to use from the array
	 * @param _bAnyIntersect True if we just want to know if any intersection occurs
	 * @param _intersection Will hold the details of any intersection found. The isIntersecting member will be false if no intersection was found.
	 * @return true if there was an intersection, false if not
	 */
	public boolean intersectTriangleFanArray(Point3d _origin, Vector3d _direction, float _fLength, float[] _vertices, int[] _fanTriangleCounts, int _nFanCount, boolean _bAnyIntersect, Intersection _intersection) {
		// Add all the strip lengths up first
		int total_vertices = 0;

		for (int i = _nFanCount; --i >= 0;)
			total_vertices += _fanTriangleCounts[i];

		if (_vertices.length < total_vertices * 3)
			throw new IllegalArgumentException("Not enough vertices for the given triangle fans");

		double shortest_length = -1;
		double this_length;
		int offset = 0;

		for (int i = 0; i < _nFanCount; i++) {
			offset = i * _fanTriangleCounts[i] * 3;

			// setup the constant first position
			m_tmpPolygon[0] = _vertices[offset];
			m_tmpPolygon[1] = _vertices[offset + 1];
			m_tmpPolygon[2] = _vertices[offset + 2];

			for (int j = 1; j < _fanTriangleCounts[i] - 2; j++) {
				m_tmpPolygon[3] = _vertices[offset + j * 3];
				m_tmpPolygon[4] = _vertices[offset + j * 3 + 1];
				m_tmpPolygon[5] = _vertices[offset + j * 3 + 2];

				m_tmpPolygon[6] = _vertices[offset + j * 3 + 3];
				m_tmpPolygon[7] = _vertices[offset + j * 3 + 4];
				m_tmpPolygon[8] = _vertices[offset + j * 3 + 5];

				// Now the rest of the polygon
				if (intersectPolygonChecked(_origin, _direction, _fLength, m_tmpPolygon, 3, m_workIntersect)) {
					m_diffVector.sub(_origin, m_workIntersect.point);

					this_length = m_diffVector.lengthSquared();

					if ((shortest_length == -1) || (this_length < shortest_length)) {
						shortest_length = this_length;
						_intersection.set(m_workIntersect);
						if (_bAnyIntersect)
							break;
					}
				}
			}
		}

		_intersection.isIntersecting = (shortest_length != -1);

		return _intersection.isIntersecting;
	}

	/** Test an array of triangle fans for intersection. Returns the closest
	 * intersection point to the origin of the picking ray. Assumes that the
	 * vertices are ordered as [Xn, Yn, Zn] and are translated into the same
	 * coordinate system that the the origin and direction are from.
	 *
	 * @param _origin The origin of the ray
	 * @param _direction The direction of the ray
	 * @param _fLength An optional length for to make the ray a segment. If the value is zero, it is ignored
	 * @param _vertices The vertices of the triangles
	 * @param _fanTriangleCounts The number of polygons in each fan
	 * @param _nFanCount The number of fans to use from the array
	 * @param _bAnyIntersect True if we just want to know if any intersection occurs
	 * @return the details of any intersection found. The isIntersecting member will be false if no intersection was found. 
	 */
	public Intersection intersectTriangleFanArray(Point3d _origin, Vector3d _direction, float _fLength, float[] _vertices, int[] _fanTriangleCounts, int _nFanCount, boolean _bAnyIntersect) {
		Intersection intersection = new Intersection();
		intersectTriangleStripArray(_origin, _direction, _fLength, _vertices, _fanTriangleCounts, _nFanCount, _bAnyIntersect, intersection);
		return intersection;
	}
	
	/** Test an array of indexed triangles for intersection. Returns the
	 * closest intersection point to the origin of the picking ray. Assumes
	 * that the vertices are ordered as [Xn, Yn, Zn] and are translated
	 * into the same coordinate system that the the origin and direction are
	 * from.
	 *
	 * @param _origin The origin of the ray
	 * @param _direction The direction of the ray
	 * @param _fLength An optional length for to make the ray a segment. If the value is zero, it is ignored
	 * @param _vertices The vertices of the triangles
	 * @param _indices The list of indexes used to construct triangles
	 * @param _nIndexCount The number of strips to use from the array
	 * @param _bAnyIntersect True if we just want to know if any intersection occurs
	 * @param _intersection Will hold the details of any intersection found. The isIntersecting member will be false if no intersection was found.
	 * @return true if there was an intersection, false if not
	 */
	public boolean intersectIndexedTriangleArray(Point3d _origin, Vector3d _direction, float _fLength, float[] _vertices, int[] _indices, int _nIndexCount, boolean _bAnyIntersect, Intersection _intersection) {
		double shortest_length = -1;
		double this_length;
		int i0, i1, i2;

		for (int i = 0; i < _nIndexCount * 3;) {
			i0 = _indices[i++];
			i1 = _indices[i++];
			i2 = _indices[i++];

			m_tmpPolygon[0] = _vertices[i0++];
			m_tmpPolygon[1] = _vertices[i0++];
			m_tmpPolygon[2] = _vertices[i0];

			m_tmpPolygon[3] = _vertices[i1++];
			m_tmpPolygon[4] = _vertices[i1++];
			m_tmpPolygon[5] = _vertices[i1];

			m_tmpPolygon[6] = _vertices[i2++];
			m_tmpPolygon[7] = _vertices[i2++];
			m_tmpPolygon[8] = _vertices[i2];

			if (intersectPolygonChecked(_origin, _direction, _fLength, m_tmpPolygon, 3, m_workIntersect)) {
				m_diffVector.sub(_origin, m_workIntersect.point);

				this_length = m_diffVector.lengthSquared();

				if ((shortest_length == -1) || (this_length < shortest_length)) {
					shortest_length = this_length;
					_intersection.set(m_workIntersect);
					if (_bAnyIntersect)
						break;
				}
			}
		}

		return (shortest_length != -1);
	}

	/** Test an array of indexed triangles for intersection. Returns the
	 * closest intersection point to the origin of the picking ray. Assumes
	 * that the vertices are ordered as [Xn, Yn, Zn] and are translated
	 * into the same coordinate system that the the origin and direction are
	 * from.
	 *
	 * @param _origin The origin of the ray
	 * @param _direction The direction of the ray
	 * @param _fLength An optional length for to make the ray a segment. If the value is zero, it is ignored
	 * @param _vertices The vertices of the triangles
	 * @param _indices The list of indexes used to construct triangles
	 * @param _nIndexCount The number of strips to use from the array
	 * @param _bAnyIntersect True if we just want to know if any intersection occurs
	 * @return the details of any intersection found. The isIntersecting member will be false if no intersection was found. 
	 */
	public Intersection intersectIndexedTriangleArray(Point3d _origin, Vector3d _direction, float _fLength, float[] _vertices, int[] _indices, int _nIndexCount, boolean _bAnyIntersect) {
		Intersection intersection = new Intersection();
		intersectIndexedTriangleArray(_origin, _direction, _fLength, _vertices, _indices, _nIndexCount, _bAnyIntersect, intersection);
		return intersection;
	}
	
	/** Test an array of indexed quads for intersection. Returns the
	 * closest intersection point to the origin of the picking ray. Assumes
	 * that the vertices are ordered as [Xn, Yn, Zn] and are translated
	 * into the same coordinate system that the the origin and direction are
	 * from.
	 *
	 * @param _origin The origin of the ray
	 * @param _direction The direction of the ray
	 * @param _fLength An optional length for to make the ray a segment. If the value is zero, it is ignored
	 * @param _vertices The vertices of the triangles
	 * @param _indices The list of indexes used to construct quads
	 * @param _nIndexCount The number of indices
	 * @param _bAnyIntersect True if we just want to know if any intersection occurs
	 * @param _intersection Will hold the details of any intersection found. The isIntersecting member will be false if no intersection was found.
	 * @return true if there was an intersection, false if not
	 */
	public boolean intersectIndexedQuadArray(Point3d _origin, Vector3d _direction, float _fLength, float[] _vertices, int[] _indices, int _nIndexCount, boolean _bAnyIntersect, Intersection _intersection) {
		double shortest_length = -1;
		double this_length;
		int i0, i1, i2, i3;

		for (int i = 0; i < _nIndexCount * 3;) {
			i0 = _indices[i++];
			i1 = _indices[i++];
			i2 = _indices[i++];
			i3 = _indices[i++];

			m_tmpPolygon[0] = _vertices[i0++];
			m_tmpPolygon[1] = _vertices[i0++];
			m_tmpPolygon[2] = _vertices[i0];

			m_tmpPolygon[3] = _vertices[i1++];
			m_tmpPolygon[4] = _vertices[i1++];
			m_tmpPolygon[5] = _vertices[i1];

			m_tmpPolygon[6] = _vertices[i2++];
			m_tmpPolygon[7] = _vertices[i2++];
			m_tmpPolygon[8] = _vertices[i2];

			m_tmpPolygon[9] = _vertices[i3++];
			m_tmpPolygon[10] = _vertices[i3++];
			m_tmpPolygon[11] = _vertices[i3];

			if (intersectPolygonChecked(_origin, _direction, _fLength, m_tmpPolygon, 4, m_workIntersect)) {
				m_diffVector.sub(_origin, m_workIntersect.point);

				this_length = m_diffVector.lengthSquared();

				if ((shortest_length == -1) || (this_length < shortest_length)) {
					shortest_length = this_length;
					_intersection.set(m_workIntersect);
					if (_bAnyIntersect)
						break;
				}
			}
		}

		return (shortest_length != -1);
	}

	//----------------------------------------------------------
	// Lower level methods for individual polygons
	//----------------------------------------------------------

	/** Test to see if the polygon intersects with the given ray. The
	 * vertices are ordered as [Xn, Yn, Zn]. The algorithm assumes that
	 * the points are co-planar. If they are not, the results may not be
	 * accurate. The normal is calculated based on the first 3 points of the
	 * polygon. We don't do any testing for less than 3 points.
	 *
	 * @param _origin The origin of the ray
	 * @param _direction The direction of the ray
	 * @param _fLength An optional length for to make the ray a segment. If the value is zero, it is ignored
	 * @param _vertices The vertices of the triangles
	 * @param _nVertexCount The number of vertices to use from the array
	 * @param _intersection Will hold the details of any intersection found. The isIntersecting member will be false if no intersection was found.
	 * @return true if there was an intersection, false if not
	 */
	public boolean intersectPolygon(Point3d _origin, Vector3d _direction, float _fLength, float[] _vertices, int _nVertexCount, Intersection _intersection) {
		if (_vertices.length < _nVertexCount * 2)
			throw new IllegalArgumentException("Not enough vertices in the buffer for the given vertex count");

		if ((m_working2dVertices == null) || (m_working2dVertices.length < _nVertexCount * 2))
			m_working2dVertices = new float[_nVertexCount * 2];

		return intersectPolygonChecked(_origin, _direction, _fLength, _vertices, _nVertexCount, _intersection);
	}

	/** Test to see if the polygon intersects with the given ray. The
	 * vertices are ordered as [Xn, Yn, Zn]. The algorithm assumes that
	 * the points are co-planar. If they are not, the results may not be
	 * accurate. The normal is calculated based on the first 3 points of the
	 * polygon. We don't do any testing for less than 3 points.
	 *
	 * @param _origin The origin of the ray
	 * @param _direction The direction of the ray
	 * @param _fLength An optional length for to make the ray a segment. If the value is zero, it is ignored
	 * @param _vertices The vertices of the triangles
	 * @param _nVertexCount The number of vertices to use from the array
	 * @return the details of any intersection found. The isIntersecting member will be false if no intersection was found. 
	 */
	public Intersection intersectPolygon(Point3d _origin, Vector3d _direction, float _fLength, float[] _vertices, int _nVertexCount) {
		if (_vertices.length < _nVertexCount * 2)
			throw new IllegalArgumentException("Not enough vertices in the buffer for the given vertex count");

		if ((m_working2dVertices == null) || (m_working2dVertices.length < _nVertexCount * 2))
			m_working2dVertices = new float[_nVertexCount * 2];

		Intersection intersection = new Intersection();
		intersectPolygonChecked(_origin, _direction, _fLength, _vertices, _nVertexCount, intersection);
		return intersection;
	}

	/** Private version of the ray - Polygon intersection test that does not
	 * do any bounds checking on arrays and assumes everything is correct.
	 * Allows fast calls to this method for internal use as well as more
	 * expensive calls with checks for the public interfaces.
	 * <p>
	 * This method does not use m_workPoint.
	 *
	 * @param _origin The origin of the ray
	 * @param _direction The direction of the ray
	 * @param _fLength An optional length for to make the ray a segment. If the value is zero, it is ignored
	 * @param _vertices The vertices of the triangles
	 * @param _nVertexCount The number of vertices to use from the array
	 * @param _intersection Will hold the details of any intersection found. The isIntersecting member will be false if no intersection was found.
	 * @return true if there was an intersection, false if not
	 */
	private boolean intersectPolygonChecked(Point3d _origin, Vector3d _direction, float _fLength, float[] _vertices, int _nVertexCount, Intersection _intersection) {
		int i, j;

		m_tmpVector0.x = _vertices[3] - _vertices[0];
		m_tmpVector0.y = _vertices[4] - _vertices[1];
		m_tmpVector0.z = _vertices[5] - _vertices[2];

		m_tmpVector1.x = _vertices[6] - _vertices[3];
		m_tmpVector1.y = _vertices[7] - _vertices[4];
		m_tmpVector1.z = _vertices[8] - _vertices[5];

		m_normal.cross(m_tmpVector0, m_tmpVector1);

		// degenerate polygon?
		if (m_normal.lengthSquared() == 0)
			return false;

		double n_dot_dir = m_normal.dot(_direction);

		// ray and plane parallel?
		if (n_dot_dir == 0)
			return false;

		m_workVector.x = _vertices[0];
		m_workVector.y = _vertices[1];
		m_workVector.z = _vertices[2];
		double d = m_normal.dot(m_workVector);

		m_workVector.set(_origin);
		double n_dot_o = m_normal.dot(m_workVector);

		// t = (d - N.O) / N.D
		double t = (d - n_dot_o) / n_dot_dir;

		// intersection before the origin
		if (t < 0)
			return false;

		// So we have an intersection with the plane of the polygon and the
		// segment/ray. Using the winding rule to see if inside or outside
		// First store the exact intersection point anyway, regardless of
		// whether this is an intersection or not.
		_intersection.point.x = _origin.x + _direction.x * t;
		_intersection.point.y = _origin.y + _direction.y * t;
		_intersection.point.z = _origin.z + _direction.z * t;
		_intersection.normal.set(m_normal);
		_intersection.normal.normalize();
		
		// Intersection point after the end of the segment?
		if ((_fLength != 0) && (_origin.distance(_intersection.point) > _fLength))
			return false;

		// bounds check

		// find the dominant axis to resolve to a 2 axis system
		double abs_nrm_x = (m_normal.x >= 0) ? m_normal.x : -m_normal.x;
		double abs_nrm_y = (m_normal.y >= 0) ? m_normal.y : -m_normal.y;
		double abs_nrm_z = (m_normal.z >= 0) ? m_normal.z : -m_normal.z;

		int dom_axis;

		if (abs_nrm_x > abs_nrm_y)
			dom_axis = 0;
		else
			dom_axis = 1;

		if (dom_axis == 0) {
			if (abs_nrm_x < abs_nrm_z)
				dom_axis = 2;
		} else if (abs_nrm_y < abs_nrm_z) {
			dom_axis = 2;
		}

		// Map all the vertices to the 2D plane. The u and v vertices
		// are interleaved as u == even indicies and v = odd indicies

		// Steps 1 & 2 combined
		// 1. For NV vertices [Xn Yn Zn] where n = 0..Nv-1, project polygon
		// vertices [Xn Yn Zn] onto dominant coordinate plane (Un Vn).
		// 2. Translate (U, V) polygon so intersection point is origin from
		// (Un', Vn').
		j = 2 * _nVertexCount - 1;

		switch (dom_axis) {
			case 0 :
				for (i = _nVertexCount; --i >= 0;) {
					m_working2dVertices[j--] = _vertices[i * 3 + 2] - (float)_intersection.point.z;
					m_working2dVertices[j--] = _vertices[i * 3 + 1] - (float)_intersection.point.y;
				}
				break;

			case 1 :
				for (i = _nVertexCount; --i >= 0;) {
					m_working2dVertices[j--] = _vertices[i * 3 + 2] - (float)_intersection.point.z;
					m_working2dVertices[j--] = _vertices[i * 3] - (float)_intersection.point.x;
				}
				break;

			case 2 :
				for (i = _nVertexCount; --i >= 0;) {
					m_working2dVertices[j--] = _vertices[i * 3 + 1] - (float)_intersection.point.y;
					m_working2dVertices[j--] = _vertices[i * 3] - (float)_intersection.point.x;
				}
				break;
		}

		int sh; // current sign holder
		int nsh; // next sign holder
		float dist;
		int crossings = 0;

		// Step 4.
		// Set sign holder as f(V' o) ( V' value of 1st vertex of 1st edge)
		if (m_working2dVertices[1] < 0.0)
			sh = -1;
		else
			sh = 1;

		for (i = 0; i < _nVertexCount; i++) {
			// Step 5.
			// For each edge of polygon (Ua' V a') -> (Ub', Vb') where
			// a = 0..Nv-1 and b = (a + 1) mod Nv

			// b = (a + 1) mod Nv
			j = (i + 1) % _nVertexCount;

			int i_u = i * 2; // index of Ua'
			int j_u = j * 2; // index of Ub'
			int i_v = i * 2 + 1; // index of Va'
			int j_v = j * 2 + 1; // index of Vb'

			// Set next sign holder (Nsh) as f(Vb')
			// Nsh = -1 if Vb' < 0
			// Nsh = +1 if Vb' >= 0
			if (m_working2dVertices[j_v] < 0.0)
				nsh = -1;
			else
				nsh = 1;

			// If Sh <> NSH then if = then edge doesn't cross +U axis so no
			// ray intersection and ignore

			if (sh != nsh) {
				// if Ua' > 0 and Ub' > 0 then edge crosses + U' so Nc = Nc + 1
				if ((m_working2dVertices[i_u] > 0.0) && (m_working2dVertices[j_u] > 0.0)) {
					crossings++;
				} else if ((m_working2dVertices[i_u] > 0.0) || (m_working2dVertices[j_u] > 0.0)) {
					// if either Ua' or U b' > 0 then line might cross +U, so
					// compute intersection of edge with U' axis
					dist = m_working2dVertices[i_u] - (m_working2dVertices[i_v] * (m_working2dVertices[j_u] - m_working2dVertices[i_u])) / (m_working2dVertices[j_v] - m_working2dVertices[i_v]);

					// if intersection point is > 0 then must cross,
					// so Nc = Nc + 1
					if (dist > 0)
						crossings++;
				}

				// Set SH = Nsh and process the next edge
				sh = nsh;
			}
		}

		// Step 6. If Nc odd, point inside else point outside.
		// Note that we have already stored the intersection point way back up
		// the start.
		return ((crossings % 2) == 1);
	}
}

/*
 * $Log$
 * Revision 1.7  2003/12/05 15:24:25  tako
 * Removed all old-style intersection methods.
 * All methods now use an Intersection object and all methods have a
 * companion method that creates and returns the result in a newly
 * created Intersection object.
 * Variables and arguments now adhere to our naming conventions.
 *
 * Revision 1.6  2003/12/02 18:51:44  tako
 * Fixed bug in intersectTriangleArray() where intersection would not be flagged.
 *
 * Revision 1.5  2003/12/02 10:16:07  tako
 * Applied puf's patch that adds several methods that return Intersection
 * instances instead of relying on setting the values of several initialized
 * objects that the caller has to pass.
 *
 * Revision 1.4  2003/11/20 16:23:27  tako
 * Updated Java docs.
 *
 * Revision 1.3  2003/11/17 10:49:59  tako
 * Added CVS macros for revision and log.
 *
 */
