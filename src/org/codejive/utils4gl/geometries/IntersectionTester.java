/*
 * Created on 14-mrt-2003
 */
package org.codejive.utils4gl.geometries;

import java.util.Iterator;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;


/**
 * This class can be used to determine intersections on any geometry contained in a VertexBuffer.
 * For performance reasons it's best to re-use an object of this class as much as possible,
 * but it has to be taken into account that the code is _not_ thread-safe!
 * 
 * @author Tako
 * @version $Revision: 356 $
 */
public class IntersectionTester {
	
	/** A point that we use for working calculations (vertex transforms) */
	private Intersection m_workIntersect;
	private Vector3d m_workVector;

	/** Working vectors */
	private Vector3f m_polyVector0;
	private Vector3f m_polyVector1;
	private Vector3f m_polyVector2;
	private Vector3d m_tmpVector0;
	private Vector3d m_tmpVector1;
	private Vector3d m_normal;

	/** The current 2D vertex list that we work from */
	private float[] m_working2dVertices;

	/**
	 * Create a default instance of this class with no internal data
	 * structures allocated.
	 */
	public IntersectionTester() {
		m_workIntersect = new Intersection();
		m_workVector = new Vector3d();
		m_polyVector0 = new Vector3f();
		m_polyVector1 = new Vector3f();
		m_polyVector2 = new Vector3f();
		m_tmpVector0 = new Vector3d();
		m_tmpVector1 = new Vector3d();
		m_normal = new Vector3d();

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

	/** Performs a Ray - Geometry intersection test. Returns the closest
	 * intersection point to the origin of the picking ray.
	 *
	 * @param _origin The origin of the ray
	 * @param _direction The direction of the ray
	 * @param _fLength An optional length for to make the ray a segment. If the value is zero, it is ignored
	 * @param _geometry The geometry to perform intersection on
	 * @param _bAnyIntersect True if we just want to know if any intersection occurs
	 * @param _intersection Will hold the details of any intersection found. The isIntersecting member will be false if no intersection was found.
	 * @return true if there was an intersection, false if not
	 */
	public boolean intersect(Point3d _origin, Vector3d _direction, float _fLength, Geometry _geometry, boolean _bAnyIntersect, Intersection _intersection) {
		double shortest_length = -1;

		Iterator<Polygon> i = _geometry.polygonIterator(VertexBuffer.COORDINATES);
		while (i.hasNext()) {
			Polygon p = i.next();
			if (intersectPolygonChecked(_origin, _direction, _fLength, p, m_workIntersect)) {
				if ((shortest_length == -1) || (m_workIntersect.getDistance() < shortest_length)) {
					shortest_length = m_workIntersect.getDistance();
					_intersection.set(m_workIntersect);
					if (_bAnyIntersect) {
						break;
					}
				}
			}
		}

		_intersection.setIntersecting((shortest_length != -1));

		return _intersection.isIntersecting();
	}

	/** Private version of the Ray - Polygon intersection test that does not
	 * do any bounds checking on arrays and assumes everything is correct.
	 * Allows fast calls to this method for internal use as well as more
	 * expensive calls with checks for the public interfaces.
	 * <p>
	 * This method does not use m_workPoint.
	 *
	 * @param _origin The origin of the ray
	 * @param _direction The direction of the ray
	 * @param _fLength An optional length for to make the ray a segment. If the value is zero, it is ignored
	 * @param _polygon The polygon to check for intersection
	 * @param _intersection Will hold the details of any intersection found. The isIntersecting member will be false if no intersection was found.
	 * @return true if there was an intersection, false if not
	 */
	private boolean intersectPolygonChecked(Point3d _origin, Vector3d _direction, float _fLength, Polygon _polygon, Intersection _intersection) {
		int i, j;

		// Make length squared because we do all comparisons using squared distances
		float _fLengthSquared = _fLength * _fLength;
		
		_polygon.getVertex(0, m_polyVector0);
		_polygon.getVertex(1, m_polyVector1);
		_polygon.getVertex(2, m_polyVector2);
		
		m_tmpVector0.x = m_polyVector1.x - m_polyVector0.x;
		m_tmpVector0.y = m_polyVector1.y - m_polyVector0.y;
		m_tmpVector0.z = m_polyVector1.z - m_polyVector0.z;

		m_tmpVector1.x = m_polyVector2.x - m_polyVector1.x;
		m_tmpVector1.y = m_polyVector2.y - m_polyVector1.y;
		m_tmpVector1.z = m_polyVector2.z - m_polyVector1.z;

		m_normal.cross(m_tmpVector0, m_tmpVector1);

		// degenerate polygon?
		if (m_normal.lengthSquared() == 0)
			return false;

		double n_dot_dir = m_normal.dot(_direction);

		// ray and plane parallel?
		if (n_dot_dir == 0)
			return false;

		m_workVector.set(m_polyVector0);
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
		_intersection.getPoint().x = _origin.x + _direction.x * t;
		_intersection.getPoint().y = _origin.y + _direction.y * t;
		_intersection.getPoint().z = _origin.z + _direction.z * t;
		_intersection.setDistanceSquared(_origin.distanceSquared(_intersection.getPoint()));
		_intersection.getNormal().set(m_normal);
		_intersection.getNormal().normalize();
		
		// Intersection point after the end of the segment?
		if ((_fLength != 0) && (_intersection.getDistanceSquared() > _fLengthSquared))
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
		j = 2 * _polygon.getVertexCount() - 1;

		switch (dom_axis) {
			case 0 :
				for (i = _polygon.getVertexCount(); --i >= 0;) {
					_polygon.getVertex(i, m_polyVector0);
					m_working2dVertices[j--] = m_polyVector0.z - (float)_intersection.getPoint().z;
					m_working2dVertices[j--] = m_polyVector0.y - (float)_intersection.getPoint().y;
				}
				break;

			case 1 :
				for (i = _polygon.getVertexCount(); --i >= 0;) {
					_polygon.getVertex(i, m_polyVector0);
					m_working2dVertices[j--] = m_polyVector0.z - (float)_intersection.getPoint().z;
					m_working2dVertices[j--] = m_polyVector0.x - (float)_intersection.getPoint().x;
				}
				break;

			case 2 :
				for (i = _polygon.getVertexCount(); --i >= 0;) {
					_polygon.getVertex(i, m_polyVector0);
					m_working2dVertices[j--] = m_polyVector0.y - (float)_intersection.getPoint().y;
					m_working2dVertices[j--] = m_polyVector0.x - (float)_intersection.getPoint().x;
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

		for (i = 0; i < _polygon.getVertexCount(); i++) {
			// Step 5.
			// For each edge of polygon (Ua' V a') -> (Ub', Vb') where
			// a = 0..Nv-1 and b = (a + 1) mod Nv

			// b = (a + 1) mod Nv
			j = (i + 1) % _polygon.getVertexCount();

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
 * Revision 1.9  2004/03/07 17:34:10  tako
 * Introduced Geometries which make it possible to perform intersection and
 * collision detection without having to think about the underlying organization
 * of the data. Geometries have been implemented for the most used
 * vertex structures: quads, triangles, strips and fans. Each Geometry
 * implements a Polygon iterator making it possible to iterate over each
 * polygon (triangle or quad) in a Geometry again without knowing the
 * underlying organization of the data.
 *
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
