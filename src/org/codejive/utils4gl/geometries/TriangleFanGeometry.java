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

import java.nio.FloatBuffer;
import java.util.Iterator;



/**
 * 
 * IMPORTANT: The polygon iterator methods are NOT thread-safe!
 * 
 * @author tako
 * @version $Revision: 216 $
 */
public class TriangleFanGeometry extends GeometryBase {
	PolygonIterator m_polyIter;
	
	public TriangleFanGeometry(VertexBuffer _buffer) {
		super(_buffer);
		m_polyIter = this.new PolygonIterator(getBuffer().getFormat());
	}
	
	public Iterator polygonIterator() {
		m_polyIter.reset();
		return m_polyIter;
	}
	
	public Iterator polygonIterator(int _nFormat) {
		m_polyIter.reset();
		return m_polyIter;
	}
	
	private class PolygonIterator implements Iterator {
		private Polygon m_polygon;
		private int m_nIndex;
		
		public PolygonIterator(int _nFormat) {
			m_polygon = new Polygon(3, _nFormat);
			m_nIndex = 1;
		}
		
		public boolean hasNext() {
			return ((getBuffer().getSize() - m_nIndex + 1) >= 3);
		}

		public Object next() {
			m_polygon.reset();
			
			int nFormat = getBuffer().getFormat();
			FloatBuffer buf;
			VertexBuffer vbuf = getBuffer();
			boolean bIndexed = vbuf.isIndexed();
			int nIdx;
			if (bIndexed) {
				nIdx = vbuf.getIndex(0);
			} else {
				nIdx = 0;
			}
			if ((nFormat & VertexBuffer.MASK_TEXTURES) > 0) {
				buf = vbuf.getTexCoords();
				m_polygon.addTexCoords2f(buf.array(), buf.arrayOffset() + vbuf.getTextureOffset(nIdx), 1);
			}
			if ((nFormat & VertexBuffer.MASK_COLORS) > 0) {
				buf = vbuf.getColors();
				m_polygon.addColors3f(buf.array(), buf.arrayOffset() + vbuf.getColorOffset(nIdx), 1);
			}
			if ((nFormat & VertexBuffer.MASK_NORMALS) > 0) {
				buf = vbuf.getNormals();
				m_polygon.addNormals(buf.array(), buf.arrayOffset() + vbuf.getNormalOffset(nIdx), 1);
			}
			buf = vbuf.getVertices();
			m_polygon.addVertices(buf.array(), buf.arrayOffset() + vbuf.getVertexOffset(nIdx), 1);
			for (int i = 0; i < 2; i++) {
				if (bIndexed) {
					nIdx = vbuf.getIndex(m_nIndex + i);
				} else {
					nIdx = m_nIndex + i;
				}
				if ((nFormat & VertexBuffer.MASK_TEXTURES) > 0) {
					buf = vbuf.getTexCoords();
					m_polygon.addTexCoords2f(buf.array(), buf.arrayOffset() + vbuf.getTextureOffset(nIdx), 1);
				}
				if ((nFormat & VertexBuffer.MASK_COLORS) > 0) {
					buf = vbuf.getColors();
					m_polygon.addColors3f(buf.array(), buf.arrayOffset() + vbuf.getColorOffset(nIdx), 1);
				}
				if ((nFormat & VertexBuffer.MASK_NORMALS) > 0) {
					buf = vbuf.getNormals();
					m_polygon.addNormals(buf.array(), buf.arrayOffset() + vbuf.getNormalOffset(nIdx), 1);
				}
				buf = vbuf.getVertices();
				m_polygon.addVertices(buf.array(), buf.arrayOffset() + vbuf.getVertexOffset(nIdx), 1);
			}
			m_nIndex++;
			return m_polygon;
		}

		public void remove() {
			// We don't support removing
		}
		
		public void reset() {
			m_nIndex = 0;
		}
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