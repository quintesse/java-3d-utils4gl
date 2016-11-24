/*
 * [utils4gl] OpenGL game-oriented GUI library
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
 * Created on Feb 18, 2004
 */
package org.codejive.utils4gl.geometries;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;



/**
 * The base implementation of a Geometry object.
 * 
 * IMPORTANT: The intersection methods are NOT thread-safe!
 * 
 * @author Tako
 * @version $Revision: 217 $
 */
public abstract class GeometryBase implements Geometry {
	private VertexBuffer m_buffer;
	private IntersectionTester intersections;
	
	/**
	 * Creates a Geometry using the given VertexBuffer for its vertex data.
	 * The data is assumed to be arranged exactly according to the expectations
	 * of the implementing Geometry.
	 * @param _buffer The VertexBuffer containing the raw geometric information
	 */
	public GeometryBase(VertexBuffer _buffer) {
		m_buffer = _buffer;
		intersections = new IntersectionTester();
	}
	
	public VertexBuffer getBuffer() {
		return m_buffer;
	}
	
	/* (non-Javadoc)
	 * @see org.codejive.utils4gl.geometries.Geometry#intersectAny(javax.vecmath.Point3d, javax.vecmath.Vector3d)
	 */
	public Intersection intersectAny(Point3d _origin, Vector3d _direction) {
		return intersectAny(_origin, _direction, 0.0f);
	}
	
	/* (non-Javadoc)
	 * @see org.codejive.utils4gl.geometries.Geometry#intersectClosest(javax.vecmath.Point3d, javax.vecmath.Vector3d)
	 */
	public Intersection intersectClosest(Point3d _origin, Vector3d _direction) {
		return intersectClosest(_origin, _direction, 0.0f);
	}
	
	/* (non-Javadoc)
	 * @see org.codejive.utils4gl.geometries.Geometry#intersectAny(javax.vecmath.Point3d, javax.vecmath.Vector3d, float)
	 */
	public Intersection intersectAny(Point3d _origin, Vector3d _direction, float _fLength) {
		Intersection intersection = new Intersection();
		intersections.intersect(_origin, _direction, _fLength, this, true, intersection);
		return intersection;
	}

	/* (non-Javadoc)
	 * @see org.codejive.utils4gl.geometries.Geometry#intersectAny(javax.vecmath.Point3d, javax.vecmath.Vector3d, float)
	 */
	public Intersection intersectClosest(Point3d _origin, Vector3d _direction, float _fLength) {
		Intersection intersection = new Intersection();
		intersections.intersect(_origin, _direction, _fLength, this, false, intersection);
		return intersection;
	}

	/* (non-Javadoc)
	 * @see org.codejive.utils4gl.geometries.Geometry#intersectAny(javax.vecmath.Point3d, javax.vecmath.Vector3d, org.codejive.utils4gl.Intersection)
	 */
	public boolean intersectAny(Point3d _origin, Vector3d _direction, Intersection _intersection) {
		return intersectAny(_origin, _direction, 0.0f, _intersection);
	}

	/* (non-Javadoc)
	 * @see org.codejive.utils4gl.geometries.Geometry#intersectClosest(javax.vecmath.Point3d, javax.vecmath.Vector3d, org.codejive.utils4gl.Intersection)
	 */
	public boolean intersectClosest(Point3d _origin, Vector3d _direction, Intersection _intersection) {
		return intersectClosest(_origin, _direction, 0.0f, _intersection);
	}

	/* (non-Javadoc)
	 * @see org.codejive.utils4gl.geometries.Geometry#intersectAny(javax.vecmath.Point3d, javax.vecmath.Vector3d, float, org.codejive.utils4gl.Intersection)
	 */
	public boolean intersectAny(Point3d _origin, Vector3d _direction, float _fLength, Intersection _intersection) {
		return intersections.intersect(_origin, _direction, _fLength, this, true, _intersection);
	}

	/* (non-Javadoc)
	 * @see org.codejive.utils4gl.geometries.Geometry#intersectClosest(javax.vecmath.Point3d, javax.vecmath.Vector3d, float, org.codejive.utils4gl.Intersection)
	 */
	public boolean intersectClosest(Point3d _origin, Vector3d _direction, float _fLength, Intersection _intersection) {
		return intersections.intersect(_origin, _direction, _fLength, this, false, _intersection);
	}
	
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