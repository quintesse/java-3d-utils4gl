/*
 * Created on Jan 23, 2004
 */
package org.codejive.utils4gl.geometries;

import java.util.Iterator;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;



/**
 * Geometry wraps a VertexBuffer thereby giving it "shape".
 * This because a VertexBuffer is nothing more than a list of vertices
 * without any information about the shape they define. A Geometry
 * defines how vertices should be grouped together to form polygons.
 * Geometries support iteration over and intersection determination
 * of the polygons they contain.
 * 
 * @author Tako
 * @version $Revision: 356 $
 */
public interface Geometry {

	/**
	 * Returns the VertexBuffer associated with this Geometry
	 * @return The Geometry's VertexBuffer
	 */
	public VertexBuffer getBuffer();
		
	/**
	 * Returns an iterator that iterates over the polygons in the buffer.
	 * The iterator returns an initialized Polygon object for each
	 * iteration that will contain the vertices and possibly the texture
	 * coordinates, colors and normals for one polygon.
	 * 
	 * @return An Iterator object
	 */
	public Iterator<Polygon> polygonIterator();
	
	/**
	 * Returns an iterator that iterates over the polygons in the buffer.
	 * The iterator returns an initialized Polygon object for each
	 * iteration that will contain the vertices and possibly the texture
	 * coordinates, colors and normals for one polygon.
	 * 
	 * @param _nFormat The format determines which information the polygon iterator will gather
	 * @return An Iterator object
	 */
	public Iterator<Polygon> polygonIterator(int _nFormat);
	
	/** Test the geometry for intersection by "shooting" a ray of infinite length
	 * from the given origin towards the given direction. The first intersection
	 * found will be returned.
	 *
	 * @param _origin The origin of the ray
	 * @param _direction The direction of the ray
	 * @return Intersection object containing all the pertinent information about any intersection found
	 */
	public Intersection intersectAny(Point3d _origin, Vector3d _direction);
	
	/** Test the geometry for intersection by "shooting" a ray of infinite length
	 * from the given origin towards the given direction. The first intersection
	 * found will be returned.
	 *
	 * @param _origin The origin of the ray
	 * @param _direction The direction of the ray
	 * @return Intersection object containing all the pertinent information about any intersection found
	 */
	public Intersection intersectClosest(Point3d _origin, Vector3d _direction);
	
	/** Test the geometry for intersection by "shooting" a ray with the specified length
	 * from the given origin towards the given direction. Depending on the value of bAnyIntersect
	 * it will either return the first intersection found or the one closest to the given origin.
	 *
	 * @param _origin The origin of the ray
	 * @param _direction The direction of the ray
	 * @param _fLength An optional length for to make the ray a segment. If the value is zero, it is ignored
	 * @return Intersection object containing all the pertinent information about any intersection found
	 */
	public Intersection intersectAny(Point3d _origin, Vector3d _direction, float _fLength);
	
	/** Test the geometry for intersection by "shooting" a ray with the specified length
	 * from the given origin towards the given direction. Depending on the value of bAnyIntersect
	 * it will either return the first intersection found or the one closest to the given origin.
	 *
	 * @param _origin The origin of the ray
	 * @param _direction The direction of the ray
	 * @param _fLength An optional length for to make the ray a segment. If the value is zero, it is ignored
	 * @return Intersection object containing all the pertinent information about any intersection found
	 */
	public Intersection intersectClosest(Point3d _origin, Vector3d _direction, float _fLength);
	
	/** Test the geometry for intersection by "shooting" a ray of infinite length
	 * from the given origin towards the given direction. The first intersection
	 * found will be returned.
	 *
	 * @param _origin The origin of the ray
	 * @param _direction The direction of the ray
	 * @param _intersection Will hold the details of any intersection found. The isIntersecting member will be false if no intersection was found.
	 * @return true if there was an intersection, false if not
	 */
	public boolean intersectAny(Point3d _origin, Vector3d _direction, Intersection _intersection);
	
	/** Test the geometry for intersection by "shooting" a ray of infinite length
	 * from the given origin towards the given direction. The first intersection
	 * found will be returned.
	 *
	 * @param _origin The origin of the ray
	 * @param _direction The direction of the ray
	 * @param _intersection Will hold the details of any intersection found. The isIntersecting member will be false if no intersection was found.
	 * @return true if there was an intersection, false if not
	 */
	public boolean intersectClosest(Point3d _origin, Vector3d _direction, Intersection _intersection);
	
	/** Test the geometry for intersection by "shooting" a ray with the specified length
	 * from the given origin towards the given direction. Depending on the value of bAnyIntersect
	 * it will either return the first intersection found or the one closest to the given origin.
	 *
	 * @param _origin The origin of the ray
	 * @param _direction The direction of the ray
	 * @param _fLength An optional length for to make the ray a segment. If the value is zero, it is ignored
	 * @param _intersection Will hold the details of any intersection found. The isIntersecting member will be false if no intersection was found.
	 * @return true if there was an intersection, false if not
	 */
	public boolean intersectAny(Point3d _origin, Vector3d _direction, float _fLength, Intersection _intersection);
	
	/** Test the geometry for intersection by "shooting" a ray with the specified length
	 * from the given origin towards the given direction. Depending on the value of bAnyIntersect
	 * it will either return the first intersection found or the one closest to the given origin.
	 *
	 * @param _origin The origin of the ray
	 * @param _direction The direction of the ray
	 * @param _fLength An optional length for to make the ray a segment. If the value is zero, it is ignored
	 * @param _intersection Will hold the details of any intersection found. The isIntersecting member will be false if no intersection was found.
	 * @return true if there was an intersection, false if not
	 */
	public boolean intersectClosest(Point3d _origin, Vector3d _direction, float _fLength, Intersection _intersection);
}


/*
 * $Log$
 * Revision 1.2  2004/03/07 18:01:31  tako
 * Completed all required javadoc comments.
 *
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