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
 * Created on Dec 8, 2003
 */
package org.codejive.utils4gl.geometries;

/**
 * A Polygon is just an extremely thin wrapper around a VertexBuffer used to hold
 * the vertex information of just a single polygon (3 or 4 vertices).
 * 
 * @author tako
 * @version $Revision: 217 $
 */
public class Polygon extends VertexBuffer {

	/**
	 * Creates a new Polygon buffer with the given amount of vertices.
	 * @param _nMaxVertexCount The maximum number of vertices in the polygon (either 3 or 4)
	 * @param _nBufferFormat The format of the buffer indicating which information will be stored
	 */
	public Polygon(int _nMaxVertexCount, int _nBufferFormat) {
		super(_nMaxVertexCount, (_nBufferFormat & (MASK_COORDINATES | MASK_TEXTURES | MASK_COLORS | MASK_NORMALS)) | BUFFER_BYREF);
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