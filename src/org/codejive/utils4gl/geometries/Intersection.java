/*
 * Created on Jan 23, 2004
 */
package org.codejive.utils4gl.geometries;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;


/** This class contains all information about an intersection. 
 * It is returned by some of the functions in this class.
 * 
 * @author puf
 * @version $Revision: 216 $
 */
 public class Intersection {
	private boolean m_isIntersecting;
	private Point3d m_intersectionPoint;
	private Vector3d m_surfaceNormal;
	private double m_distanceSquared;

	/**
	 * Constructs a new Intersection
	 */
	public Intersection() {
		m_isIntersecting = false;
		m_intersectionPoint = new Point3d();
		m_surfaceNormal = new Vector3d();
		m_distanceSquared = 0.0;
	}

	/**
	 * Constructs a copy of the given Intersection
	 * @param _source The Intersection object to duplicate
	 */
	public Intersection(Intersection _source) {
		set(_source);
	}
	
	/** Indicates if an intersection was found. 
	 * The other members are only valid, if this field has a value of true.
	 * @return A boolean indicating if an intersection has been found
	 */
	public boolean isIntersecting() {
		return m_isIntersecting;
	}

	/** Indicates if an intersection was found. 
	 * The other members are only valid, if this field has a value of true.
	 * @param _isIntersecting A boolean indicating if an intersection has been found
	 */
	public void setIntersecting(boolean _isIntersecting) {
		m_isIntersecting = _isIntersecting;
	}

	/** Returns the point at which an intersection was found
	 * @return A point with the coordinates of the intersection
	 */
	public Point3d getPoint() {
		return m_intersectionPoint;
	}

	/** Sets the point at which an intersection was found
	 * @param _point A point with the coordinates of the intersection
	 */
	public void setPoint(Point3d _point) {
		m_intersectionPoint.set(_point);
	}

	/** Returns the surface normal at the point of intersection
	 * @return A vector holding a surfuce normal
	 */
	public Vector3d getNormal() {
		return m_surfaceNormal;
	}

	/** Sets the surface normal at the point of intersection
	 * @param _normal A vector holding a surfuce normal
	 */
	public void setNormal(Vector3d _normal) {
		m_surfaceNormal.set(_normal);
	}

	/** Returns the distance squared from the origin of the intersect ray to the intersection point.
	 * @return The distance squared to the intersection point
	 */
	public double getDistanceSquared() {
		return m_distanceSquared;
	}

	/** Sets the distance squared from the origin of the intersect ray to the intersection point.
	 * @param _distance The distance squared to the intersection point
	 */
	public void setDistanceSquared(double _distance) {
		m_distanceSquared = _distance;
	}

	/** Returns the distance from the origin of the intersect ray to the intersection point.
	 * @return The distance to the intersection point
	 */
	public double getDistance() {
		return Math.sqrt(m_distanceSquared);
	}
	
	/** Sets all members to the values specified in the given Intersection
	 * @param _source Intersection object to duplicate
	 */
	public void set(Intersection _source) {
		setIntersecting(_source.isIntersecting());
		setPoint(_source.getPoint());
		setNormal(_source.getNormal());
		setDistanceSquared(_source.getDistanceSquared());
	}
}

/*
 * $Log$
 * Revision 1.1  2004/03/07 17:34:10  tako
 * Introduced Geometries which make it possible to perform intersection and
 * collision detection without having to think about the underlying organization
 * of the data. Geometries have been implemented for the most used
 * vertex structures: quads, triangles, strips and fans. Each Geometry
 * implements a Polygon iterator making it possible to iterate over each
 * polygon (triangle or quad) in a Geometry again without knowing the
 * underlying organization of the data.
 *
 */