/*
 * Created on 14-mrt-2003
 */
package org.codejive.utils4gl;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/*
 * @version $Revision: 181 $
 */
public class Intersections {

	/** A point that we use for working calculations (coord transforms) */
	private Point3d m_workPoint;
	private Vector3d m_workVector;
	private Vector3d m_workNormal;

	/** Working vectors */
	private Vector3d m_tmpVector0;
	private Vector3d m_tmpVector1;
	private Vector3d m_normal;
	private Vector3d m_diffVector;

	/** The current 2D coordinate list that we work from */
	private float[] m_working2dCoords;

	/** Temporay space for a single quad polygon */
	private float[] m_tmpPolygon;

	/** This class contains all information about an intersection. 
	 * It is returned by some of the functions in this class.
	 */
	public class Intersection {
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
	}

	/**
	 * Create a default instance of this class with no internal data
	 * structures allocated.
	 */
	public Intersections() {
		m_workPoint = new Point3d();
		m_workVector = new Vector3d();
		m_workNormal = new Vector3d();
		m_tmpVector0 = new Vector3d();
		m_tmpVector1 = new Vector3d();
		m_normal = new Vector3d();
		m_diffVector = new Vector3d();
		m_tmpPolygon = new float[12];

		m_working2dCoords = new float[8];
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
		m_working2dCoords = new float[8];
	}

	/** Test an array of triangles for intersection. Returns the closest
	 * intersection point to the origin of the picking ray. Assumes that the
	 * coordinates are ordered as [Xn, Yn, Zn] and are translated into the same
	 * coordinate system that the the origin and direction are from.
	 *
	 * @return true if there was an intersection, false if not
	 * @param normal The normal of the intersected triangle
	 * @param origin The origin of the ray
	 * @param direction The direction of the ray
	 * @param length An optional length for to make the ray a segment. If
	 *   the value is zero, it is ignored
	 * @param coords The coordinates of the triangles
	 * @param numTris The number of triangles to use from the array
	 * @param point The intersection point for returning
	 * @param intersectOnly true if we only want to know if we have a
	 *    intersection and don't really care which it is
	 */
	public boolean intersectTriangleArray(Point3d origin, Vector3d direction, float length, float[] coords, int numTris, Point3d point, Vector3d normal, boolean intersectOnly) {
		Intersection intersection = intersectTriangleArray(origin, direction, length, coords, numTris, intersectOnly);
		point.set(intersection.point);
		normal.set(intersection.normal);
		return intersection.isIntersecting;
	}

	/** Test an array of triangles for intersection. Returns the closest
	 * intersection point to the origin of the picking ray. Assumes that the
	 * coordinates are ordered as [Xn, Yn, Zn] and are translated into the same
	 * coordinate system that the the origin and direction are from.
	 *
	 * @return the details of any intersection found. The isIntersecting member will be false of no intersection was found. 
	 * @param origin The origin of the ray
	 * @param direction The direction of the ray
	 * @param length An optional length for to make the ray a segment. If
	 *   the value is zero, it is ignored
	 * @param coords The coordinates of the triangles
	 * @param numTris The number of triangles to use from the array
	 * @param intersectOnly true if we only want to know if we have a
	 *    intersection and don't really care which it is
	 */
	public Intersection intersectTriangleArray(Point3d origin, Vector3d direction, float length, float[] coords, int numTris, boolean intersectOnly) {
		Intersection intersection = new Intersection();
		if (coords.length < numTris * 9)
			throw new IllegalArgumentException("coords too small for numCoords");

		double shortest_length = -1;
		double this_length;

		// Make length squared because we do all comparisons using squared distances
		length *= length;
		
		for (int i = 0; i < numTris; i++) {
			System.arraycopy(coords, i * 9, m_tmpPolygon, 0, 9);

			if (intersectPolygonChecked(origin, direction, 0.0f, m_tmpPolygon, 3, m_workPoint, m_workNormal)) {
				m_diffVector.sub(origin, m_workPoint);

				this_length = m_diffVector.lengthSquared();

				if (((length == 0) || (this_length <= length)) && ((shortest_length == -1) || (this_length < shortest_length))) {
					shortest_length = this_length;
					intersection.point.set(m_workPoint);
					intersection.normal.set(m_workNormal);
					intersection.distance = this_length;

					if (intersectOnly)
						break;
				}
			}
		}

		return intersection;
	}

	/** Test an array of quads for intersection. Returns the closest
	 * intersection point to the origin of the picking ray. Assumes that the
	 * coordinates are ordered as [Xn, Yn, Zn] and are translated into the same
	 * coordinate system that the the origin and direction are from.
	 *
	 * @return true if there was an intersection, false if not
	 * @param normal The normal of the intersected quad
	 * @param origin The origin of the ray
	 * @param direction The direction of the ray
	 * @param length An optional length for to make the ray a segment. If
	 *   the value is zero, it is ignored
	 * @param coords The coordinates of the quads
	 * @param numQuads The number of quads to use from the array
	 * @param point The intersection point for returning
	 * @param intersectOnly true if we only want to know if we have a
	 *    intersection and don't really care which it is
	 */
	public boolean rayQuadArray(Point3d origin, Vector3d direction, float length, float[] coords, int numQuads, Point3d point, Vector3d normal, boolean intersectOnly) {
		if (coords.length < numQuads * 12)
			throw new IllegalArgumentException("coords too small for numCoords");

		double shortest_length = -1;
		double this_length;

		for (int i = 0; i < numQuads; i++) {
			System.arraycopy(coords, i * 12, m_tmpPolygon, 0, 12);

			if (intersectPolygonChecked(origin, direction, length, m_tmpPolygon, 4, m_workPoint, m_workNormal)) {
				m_diffVector.sub(origin, m_workPoint);

				this_length = m_diffVector.lengthSquared();

				if ((shortest_length == -1) || (this_length < shortest_length)) {
					shortest_length = this_length;
					point.set(m_workPoint);
					normal.set(m_workNormal);

					if (intersectOnly)
						break;
				}
			}
		}

		return (shortest_length != -1);
	}

	/** Test an array of triangles strips for intersection. Returns the closest
	 * intersection point to the origin of the picking ray. Assumes that the
	 * coordinates are ordered as [Xn, Yn, Zn] and are translated into the same
	 * coordinate system that the the origin and direction are from.
	 *
	 * @return true if there was an intersection, false if not
	 * @param normal The normal of the intersected triangle
	 * @param origin The origin of the ray
	 * @param direction The direction of the ray
	 * @param length An optional length for to make the ray a segment. If
	 *   the value is zero, it is ignored
	 * @param coords The coordinates of the triangles
	 * @param stripCounts The number of polygons in each strip
	 * @param numStrips The number of strips to use from the array
	 * @param point The intersection point for returning
	 * @param intersectOnly true if we only want to know if we have a
	 *    intersection and don't really care which it is
	 */
	public boolean intersectTriangleStripArray(Point3d origin, Vector3d direction, float length, float[] coords, int[] stripCounts, int numStrips, Point3d point, Vector3d normal, boolean intersectOnly) {
		Intersection intersection = intersectTriangleStripArray(origin, direction, length, coords, stripCounts, numStrips, intersectOnly); 
		point.set(intersection.point);
		normal.set(intersection.normal);
		return (intersection.isIntersecting);
	}
	
	/** Test an array of triangles strips for intersection. Returns the closest
	 * intersection point to the origin of the picking ray. Assumes that the
	 * coordinates are ordered as [Xn, Yn, Zn] and are translated into the same
	 * coordinate system that the the origin and direction are from.
	 *
	 * @return the details of any intersection found. The isIntersecting member will be false of no intersection was found. 
	 * @param origin The origin of the ray
	 * @param direction The direction of the ray
	 * @param length An optional length for to make the ray a segment. If
	 *   the value is zero, it is ignored
	 * @param coords The coordinates of the triangles
	 * @param stripCounts The number of polygons in each strip
	 * @param numStrips The number of strips to use from the array
	 * @param intersectOnly true if we only want to know if we have a
	 *    intersection and don't really care which it is
	 */
	public Intersection intersectTriangleStripArray(Point3d origin, Vector3d direction, float length, float[] coords, int[] stripCounts, int numStrips, boolean intersectOnly) {
		Intersection intersection = new Intersection();
		
		// Add all the strip lengths up first
		int total_coords = 0;

		for (int i = numStrips; --i >= 0;)
			total_coords += stripCounts[i];

		if (coords.length < total_coords * 3)
			throw new IllegalArgumentException("coords too small for numCoords");

		double shortest_length = -1;
		double this_length;
		int offset = 0;

		for (int i = 0; i < numStrips; i++) {
			offset = i * stripCounts[i] * 3;

			for (int j = 0; j < stripCounts[i] - 2; j++) {
				System.arraycopy(coords, offset + j * 3, m_tmpPolygon, 0, 9);
				//System.out.println("Copied strip "+i+" from coords["+(offset + j * 3)+",+9]");

				if (intersectPolygonChecked(origin, direction, length, m_tmpPolygon, 3, m_workPoint, m_workNormal)) {
					m_diffVector.sub(origin, m_workPoint);

					this_length = m_diffVector.lengthSquared();

					if ((shortest_length == -1) || (this_length < shortest_length)) {
						shortest_length = this_length;
						intersection.point.set(m_workPoint);
						intersection.normal.set(m_workNormal);
						intersection.distance = this_length;
						if (intersectOnly)
							break;
					}
				}
			}
		}

		intersection.isIntersecting = (shortest_length != -1);

		return intersection;
	}

	/** Test an array of triangle fans for intersection. Returns the closest
	 * intersection point to the origin of the picking ray. Assumes that the
	 * coordinates are ordered as [Xn, Yn, Zn] and are translated into the same
	 * coordinate system that the the origin and direction are from.
	 *
	 * @return true if there was an intersection, false if not
	 * @param normal The normal of the intersected triangle
	 * @param origin The origin of the ray
	 * @param direction The direction of the ray
	 * @param length An optional length for to make the ray a segment. If
	 *   the value is zero, it is ignored
	 * @param coords The coordinates of the triangles
	 * @param stripCounts The number of polygons in each fan
	 * @param numStrips The number of strips to use from the array
	 * @param point The intersection point for returning
	 * @param intersectOnly true if we only want to know if we have a
	 *    intersection and don't really care which it is
	 */
	public boolean intersectTriangleFanArray(Point3d origin, Vector3d direction, float length, float[] coords, int[] stripCounts, int numStrips, Point3d point, Vector3d normal, boolean intersectOnly) {
		// Add all the strip lengths up first
		int total_coords = 0;

		for (int i = numStrips; --i >= 0;)
			total_coords += stripCounts[i];

		if (coords.length < total_coords * 3)
			throw new IllegalArgumentException("coords too small for numCoords");

		double shortest_length = -1;
		double this_length;
		int offset = 0;

		for (int i = 0; i < numStrips; i++) {
			offset = i * stripCounts[i] * 3;

			// setup the constant first position
			m_tmpPolygon[0] = coords[offset];
			m_tmpPolygon[1] = coords[offset + 1];
			m_tmpPolygon[2] = coords[offset + 2];

			for (int j = 1; j < stripCounts[i] - 2; j++) {
				m_tmpPolygon[3] = coords[offset + j * 3];
				m_tmpPolygon[4] = coords[offset + j * 3 + 1];
				m_tmpPolygon[5] = coords[offset + j * 3 + 2];

				m_tmpPolygon[6] = coords[offset + j * 3 + 3];
				m_tmpPolygon[7] = coords[offset + j * 3 + 4];
				m_tmpPolygon[8] = coords[offset + j * 3 + 5];

				// Now the rest of the polygon
				if (intersectPolygonChecked(origin, direction, length, m_tmpPolygon, 3, m_workPoint, m_workNormal)) {
					m_diffVector.sub(origin, m_workPoint);

					this_length = m_diffVector.lengthSquared();

					if ((shortest_length == -1) || (this_length < shortest_length)) {
						shortest_length = this_length;
						point.set(m_workPoint);
						normal.set(m_workNormal);

						if (intersectOnly)
							break;
					}
				}
			}
		}

		return (shortest_length != -1);
	}

	/** Test an array of indexed triangles for intersection. Returns the
	 * closest intersection point to the origin of the picking ray. Assumes
	 * that the coordinates are ordered as [Xn, Yn, Zn] and are translated
	 * into the same coordinate system that the the origin and direction are
	 * from.
	 *
	 * @return true if there was an intersection, false if not
	 * @param normal The normal of the intersected triangle
	 * @param origin The origin of the ray
	 * @param direction The direction of the ray
	 * @param length An optional length for to make the ray a segment. If
	 *   the value is zero, it is ignored
	 * @param coords The coordinates of the triangles
	 * @param indexes The list of indexes to use to construct triangles
	 * @param numIndex The number of indexes to use from the array
	 * @param point The intersection point for returning
	 * @param intersectOnly true if we only want to know if we have a
	 *    intersection and don't really care which it is
	 */
	public boolean intersectIndexedTriangleArray(Point3d origin, Vector3d direction, float length, float[] coords, int[] indexes, int numIndex, Point3d point, Vector3d normal, boolean intersectOnly) {
		double shortest_length = -1;
		double this_length;
		int i0, i1, i2;

		for (int i = 0; i < numIndex * 3;) {
			i0 = indexes[i++];
			i1 = indexes[i++];
			i2 = indexes[i++];

			m_tmpPolygon[0] = coords[i0++];
			m_tmpPolygon[1] = coords[i0++];
			m_tmpPolygon[2] = coords[i0];

			m_tmpPolygon[3] = coords[i1++];
			m_tmpPolygon[4] = coords[i1++];
			m_tmpPolygon[5] = coords[i1];

			m_tmpPolygon[6] = coords[i2++];
			m_tmpPolygon[7] = coords[i2++];
			m_tmpPolygon[8] = coords[i2];

			if (intersectPolygonChecked(origin, direction, length, m_tmpPolygon, 3, m_workPoint, m_workNormal)) {
				m_diffVector.sub(origin, m_workPoint);

				this_length = m_diffVector.lengthSquared();

				if ((shortest_length == -1) || (this_length < shortest_length)) {
					shortest_length = this_length;
					point.set(m_workPoint);
					normal.set(m_workNormal);

					if (intersectOnly)
						break;
				}
			}
		}

		return (shortest_length != -1);
	}

	/** Test an array of indexed quads for intersection. Returns the
	 * closest intersection point to the origin of the picking ray. Assumes
	 * that the coordinates are ordered as [Xn, Yn, Zn] and are translated
	 * into the same coordinate system that the the origin and direction are
	 * from.
	 *
	 * @return true if there was an intersection, false if not
	 * @param normal The normal of the intersected quad
	 * @param origin The origin of the ray
	 * @param direction The direction of the ray
	 * @param length An optional length for to make the ray a segment. If
	 *   the value is zero, it is ignored
	 * @param coords The coordinates of the triangles
	 * @param indexes The list of indexes to use to construct triangles
	 * @param numIndex The number of indexes to use from the array
	 * @param point The intersection point for returning
	 * @param intersectOnly true if we only want to know if we have a
	 *    intersection and don't really care which it is
	 */
	public boolean intersectIndexedQuadArray(Point3d origin, Vector3d direction, float length, float[] coords, int[] indexes, int numIndex, Point3d point, Vector3d normal, boolean intersectOnly) {
		double shortest_length = -1;
		double this_length;
		int i0, i1, i2, i3;

		for (int i = 0; i < numIndex * 3;) {
			i0 = indexes[i++];
			i1 = indexes[i++];
			i2 = indexes[i++];
			i3 = indexes[i++];

			m_tmpPolygon[0] = coords[i0++];
			m_tmpPolygon[1] = coords[i0++];
			m_tmpPolygon[2] = coords[i0];

			m_tmpPolygon[3] = coords[i1++];
			m_tmpPolygon[4] = coords[i1++];
			m_tmpPolygon[5] = coords[i1];

			m_tmpPolygon[6] = coords[i2++];
			m_tmpPolygon[7] = coords[i2++];
			m_tmpPolygon[8] = coords[i2];

			m_tmpPolygon[9] = coords[i3++];
			m_tmpPolygon[10] = coords[i3++];
			m_tmpPolygon[11] = coords[i3];

			if (intersectPolygonChecked(origin, direction, length, m_tmpPolygon, 4, m_workPoint, m_workNormal)) {
				m_diffVector.sub(origin, m_workPoint);

				this_length = m_diffVector.lengthSquared();

				if ((shortest_length == -1) || (this_length < shortest_length)) {
					shortest_length = this_length;
					point.set(m_workPoint);
					normal.set(m_workNormal);
					
					if (intersectOnly)
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
	 * coordinates are ordered as [Xn, Yn, Zn]. The algorithm assumes that
	 * the points are co-planar. If they are not, the results may not be
	 * accurate. The normal is calculated based on the first 3 points of the
	 * polygon. We don't do any testing for less than 3 points.
	 *
	 * @return true if there was an intersection, false if not
	 * @param normal The normal of the intersected polygon
	 * @param origin The origin of the ray
	 * @param direction The direction of the ray
	 * @param length An optional length for to make the ray a segment. If
	 *   the value is zero, it is ignored
	 * @param coords The coordinates of the polygon
	 * @param numCoords The number of coordinates to use from the array
	 * @param point The intersection point for returning
	 */
	public boolean intersectPolygon(Point3d origin, Vector3d direction, float length, float[] coords, int numCoords, Point3d point, Vector3d normal) {
		if (coords.length < numCoords * 2)
			throw new IllegalArgumentException("coords too small for numCoords");

		if ((m_working2dCoords == null) || (m_working2dCoords.length < numCoords * 2))
			m_working2dCoords = new float[numCoords * 2];

		return intersectPolygonChecked(origin, direction, length, coords, numCoords, point, normal);
	}

	/** Private version of the ray - Polygon intersection test that does not
	 * do any bounds checking on arrays and assumes everything is correct.
	 * Allows fast calls to this method for internal use as well as more
	 * expensive calls with checks for the public interfaces.
	 * <p>
	 * This method does not use m_workPoint.
	 *
	 * @return true if there was an intersection, false if not
	 * @param normal The normal of the intersected polygon
	 * @param origin The origin of the ray
	 * @param direction The direction of the ray
	 * @param length An optional length for to make the ray a segment. If
	 *   the value is zero, it is ignored
	 * @param coords The coordinates of the polygon
	 * @param numCoords The number of coordinates to use from the array
	 * @param point The intersection point for returning
	 */
	private boolean intersectPolygonChecked(Point3d origin, Vector3d direction, float length, float[] coords, int numCoords, Point3d point, Vector3d normal) {
		int i, j;

		m_tmpVector0.x = coords[3] - coords[0];
		m_tmpVector0.y = coords[4] - coords[1];
		m_tmpVector0.z = coords[5] - coords[2];

		m_tmpVector1.x = coords[6] - coords[3];
		m_tmpVector1.y = coords[7] - coords[4];
		m_tmpVector1.z = coords[8] - coords[5];

		m_normal.cross(m_tmpVector0, m_tmpVector1);

		// degenerate polygon?
		if (m_normal.lengthSquared() == 0)
			return false;

		double n_dot_dir = m_normal.dot(direction);

		// ray and plane parallel?
		if (n_dot_dir == 0)
			return false;

		m_workVector.x = coords[0];
		m_workVector.y = coords[1];
		m_workVector.z = coords[2];
		double d = m_normal.dot(m_workVector);

		m_workVector.set(origin);
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
		point.x = origin.x + direction.x * t;
		point.y = origin.y + direction.y * t;
		point.z = origin.z + direction.z * t;
		normal.set(m_normal);
		normal.normalize();
		
		// Intersection point after the end of the segment?
		if ((length != 0) && (origin.distance(point) > length))
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

		// Map all the coordinates to the 2D plane. The u and v coordinates
		// are interleaved as u == even indicies and v = odd indicies

		// Steps 1 & 2 combined
		// 1. For NV vertices [Xn Yn Zn] where n = 0..Nv-1, project polygon
		// vertices [Xn Yn Zn] onto dominant coordinate plane (Un Vn).
		// 2. Translate (U, V) polygon so intersection point is origin from
		// (Un', Vn').
		j = 2 * numCoords - 1;

		switch (dom_axis) {
			case 0 :
				for (i = numCoords; --i >= 0;) {
					m_working2dCoords[j--] = coords[i * 3 + 2] - (float)point.z;
					m_working2dCoords[j--] = coords[i * 3 + 1] - (float)point.y;
				}
				break;

			case 1 :
				for (i = numCoords; --i >= 0;) {
					m_working2dCoords[j--] = coords[i * 3 + 2] - (float)point.z;
					m_working2dCoords[j--] = coords[i * 3] - (float)point.x;
				}
				break;

			case 2 :
				for (i = numCoords; --i >= 0;) {
					m_working2dCoords[j--] = coords[i * 3 + 1] - (float)point.y;
					m_working2dCoords[j--] = coords[i * 3] - (float)point.x;
				}
				break;
		}

		int sh; // current sign holder
		int nsh; // next sign holder
		float dist;
		int crossings = 0;

		// Step 4.
		// Set sign holder as f(V' o) ( V' value of 1st vertex of 1st edge)
		if (m_working2dCoords[1] < 0.0)
			sh = -1;
		else
			sh = 1;

		for (i = 0; i < numCoords; i++) {
			// Step 5.
			// For each edge of polygon (Ua' V a') -> (Ub', Vb') where
			// a = 0..Nv-1 and b = (a + 1) mod Nv

			// b = (a + 1) mod Nv
			j = (i + 1) % numCoords;

			int i_u = i * 2; // index of Ua'
			int j_u = j * 2; // index of Ub'
			int i_v = i * 2 + 1; // index of Va'
			int j_v = j * 2 + 1; // index of Vb'

			// Set next sign holder (Nsh) as f(Vb')
			// Nsh = -1 if Vb' < 0
			// Nsh = +1 if Vb' >= 0
			if (m_working2dCoords[j_v] < 0.0)
				nsh = -1;
			else
				nsh = 1;

			// If Sh <> NSH then if = then edge doesn't cross +U axis so no
			// ray intersection and ignore

			if (sh != nsh) {
				// if Ua' > 0 and Ub' > 0 then edge crosses + U' so Nc = Nc + 1
				if ((m_working2dCoords[i_u] > 0.0) && (m_working2dCoords[j_u] > 0.0)) {
					crossings++;
				} else if ((m_working2dCoords[i_u] > 0.0) || (m_working2dCoords[j_u] > 0.0)) {
					// if either Ua' or U b' > 0 then line might cross +U, so
					// compute intersection of edge with U' axis
					dist = m_working2dCoords[i_u] - (m_working2dCoords[i_v] * (m_working2dCoords[j_u] - m_working2dCoords[i_u])) / (m_working2dCoords[j_v] - m_working2dCoords[i_v]);

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
