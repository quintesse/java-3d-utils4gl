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

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Tuple2f;
import javax.vecmath.Tuple3f;

/**
 * This class is a basic implementation of a VertexBuffer that can be used
 * for either interleaved or non-interleaved data.
 *  
 * @author Tako
 * @version $Revision: 215 $
 */
public class VertexBuffer {
	/**
	 * The vertex buffer contains coordinates
	 */
	public static final int COORDINATES = 0;
	/**
	 * The mask that can be used to test the mode flag for the COORDINATES bit
	 */
	public static final int MASK_COORDINATES = 0;

	/**
	 * The vertex buffer contains 2D texture coordinates
	 */
	public static final int TEXTURES_2D = 1;
	/**
	 * The vertex buffer contains 3D texture coordinates
	 */
	public static final int TEXTURES_3D = 2;
	/**
	 * The vertex buffer contains 4D texture coordinates
	 */
	public static final int TEXTURES_4D = 3;
	/**
	 * The mask that can be used to test the mode flag for the TEXTURES_ setting
	 */
	public static final int MASK_TEXTURES = 3;

	/**
	 * The vertex buffer contains RGB color information
	 */
	public static final int COLORS_RGB = 4;
	/**
	 * The vertex buffer contains RGBA color information
	 */
	public static final int COLORS_RGBA = 8;
	/**
	 * The mask that can be used to test the mode flag for the COLORS_ setting
	 */
	public static final int MASK_COLORS = 12;

	/**
	 * The vertex buffer contains vertex normals
	 */
	public static final int NORMALS = 16;
	/**
	 * The mask that can be used to test the mode flag for the NORMALS bit
	 */
	public static final int MASK_NORMALS = 16;

	/**
	 * The vertex buffer stores all data in one interleaved buffer
	 */
	public static final int INTERLEAVED = 32;
	/**
	 * The mask that can be used to test the mode flag for the INTERLEAVED bit
	 */
	public static final int MASK_INTERLEAVED = 32;

	/**
	 * The vertex buffer contains an index into the vertex data
	 */
	public static final int INDEXED = 64;
	/**
	 * The mask that can be used to test the mode flag for the INDEXED bit
	 */
	public static final int MASK_INDEXED = 64;

	/**
	 * The buffer is based on a Java array
	 */
	public static final int BUFFER_ARRAY = 0;
	/**
	 * The buffer is based on a NIO buffers
	 */
	public static final int BUFFER_NIO = 128;
	/**
	 * The buffer is read only
	 */
	public static final int BUFFER_READONLY = 256;
	/**
	 * The buffer uses NIO byref buffer
	 */
	public static final int BUFFER_BYREF = 384;
	/**
	 * The mask that can be used to test the mode flag for the BUFFER_ setting
	 */
	public static final int MASK_BUFFER = 384;

	private int m_nMaxVertexCount;
	private int m_nMaxIndexCount;
	private int m_nBufferFormat;
	private int m_nTextureType;
	private int m_nColorType;
	private int m_nNormalType;
	private boolean m_bInterleaved;
	private boolean m_bIndexed;
	
	private int m_nVertexCount;
	private int m_nIndexCount;
	private FloatBuffer m_vertices;
	private FloatBuffer m_textureCoords;
	private FloatBuffer m_colors;
	private FloatBuffer m_normals;
	private IntBuffer m_indices;
	private int m_nTextureOffset;
	private int m_nColorOffset;
	private int m_nNormalOffset;
	private int m_nVertexOffset;
	private int m_nTextureInfoSize;
	private int m_nColorInfoSize;
	private int m_nNormalInfoSize;
	private int m_nVertexInfoSize;
	
	private static final int SIZEOF_INT = 4;
	private static final int SIZEOF_FLOAT = 4;
	
	/**
	 * Constructor for a new VertexBuffer of the specified size
	 * 
	 * @param _nMaxVertexCount The number of vertices the buffer can contain
	 * @param _nBufferFormat The format determines which information can be stored (vertices, textures,
	 * colors, normals, etc) and how the stored data will be organized (interleaved or multiple buffers).
	 */
	public VertexBuffer(int _nMaxVertexCount, int _nBufferFormat) {
		this(_nMaxVertexCount, 0, _nBufferFormat);
	}

	/**
	 * Constructor for a new VertexBuffer of the specified size
	 * 
	 * @param _nMaxVertexCount The number of vertices the buffer can contain
	 * @param _nMaxIndexCount The number of vertex indices the buffer can contain
	 * @param _nBufferFormat The format determines which information can be stored (vertices, textures,
	 * colors, normals, etc) and how the stored data will be organized (interleaved or multiple buffers).
	 */
	public VertexBuffer(int _nMaxVertexCount, int _nMaxIndexCount, int _nBufferFormat) {
		// Just make sure that the INDEXED bit is set correctly
		if (_nMaxIndexCount > 0) {
			_nBufferFormat |= INDEXED;
		} else {
			_nBufferFormat &= ~INDEXED;
		}
		
		m_nBufferFormat = _nBufferFormat;
		m_nTextureType = _nBufferFormat & MASK_TEXTURES;
		m_nColorType = _nBufferFormat & MASK_COLORS;
		m_nNormalType = _nBufferFormat & MASK_NORMALS;
		m_bIndexed = ((_nBufferFormat & INDEXED) != 0);
		m_bInterleaved = ((_nBufferFormat & INTERLEAVED) != 0);
		
		m_nMaxVertexCount = _nMaxVertexCount;
		m_nMaxIndexCount = _nMaxIndexCount;
		m_nVertexCount = 0;
		m_nIndexCount = 0;
		
		calcInfoSizesAndOffsets();
		
		allocateBuffers();
	}

	private void calcInfoSizesAndOffsets() {
		m_nVertexInfoSize = 3;
			
		switch (m_nTextureType) {
			case TEXTURES_2D:
				m_nTextureInfoSize = 2;
				break;
			case TEXTURES_3D:
				m_nTextureInfoSize = 3;
				break;
			case TEXTURES_4D:
				m_nTextureInfoSize = 4;
				break;
			default:
				m_nTextureInfoSize = 0;
				break;
		}
			
		switch (m_nColorType) {
			case COLORS_RGB:
				m_nColorInfoSize = 3;
				break;
			case COLORS_RGBA:
				m_nColorInfoSize = 4;
				break;
			default:
				m_nColorInfoSize = 0;
				break;
		}
			
		switch (m_nNormalType) {
			case NORMALS:
				m_nNormalInfoSize = 3;
				break;
			default:
				m_nNormalInfoSize = 0;
				break;
		}
		
		if (m_bInterleaved) {
			int nOffset = 0;
			
			m_nVertexOffset = nOffset;
			nOffset += m_nVertexInfoSize;

			m_nTextureOffset = nOffset;
			nOffset += m_nTextureInfoSize;
			
			m_nColorOffset = nOffset;
			nOffset += m_nColorInfoSize;
			
			m_nNormalOffset = nOffset;
			
			int size = m_nVertexInfoSize + m_nTextureInfoSize + m_nColorInfoSize + m_nNormalInfoSize;
			m_nVertexInfoSize = m_nTextureInfoSize = m_nColorInfoSize = m_nNormalInfoSize = size;
		} else {
			m_nVertexOffset = m_nTextureOffset = m_nColorOffset = m_nNormalOffset = 0;
		}
	}

	private void allocateBuffers() {
		if (m_bInterleaved) {
			m_vertices = allocateFloatBuffer(m_nMaxVertexCount * m_nVertexInfoSize);
			m_textureCoords = m_colors = m_normals = m_vertices;
		} else {
			m_vertices = allocateFloatBuffer(m_nMaxVertexCount * 3);
			switch (m_nTextureType) {
				case TEXTURES_2D:
					m_textureCoords = allocateFloatBuffer(m_nMaxVertexCount * 2);
					break;
				case TEXTURES_3D:
					m_textureCoords = allocateFloatBuffer(m_nMaxVertexCount * 3);
					break;
				case TEXTURES_4D:
					m_textureCoords = allocateFloatBuffer(m_nMaxVertexCount * 4);
					break;
				default:
					m_textureCoords = null;
					break;
			}
			
			switch (m_nColorType) {
				case COLORS_RGB:
					m_colors = allocateFloatBuffer(m_nMaxVertexCount * 3);
					break;
				case COLORS_RGBA:
					m_colors = allocateFloatBuffer(m_nMaxVertexCount * 4);
					break;
				default:
					m_colors = null;
					break;
			}
			
			switch (m_nNormalType) {
				case NORMALS:
					m_normals = allocateFloatBuffer(m_nMaxVertexCount * 3);
					break;
				default:
					m_normals = null;
					break;
			}
		}
		if (m_bIndexed) {
			m_indices = allocateIntBuffer(m_nMaxIndexCount);
		}
	}
	
	private FloatBuffer allocateFloatBuffer(int _nSize) {
		FloatBuffer buf;

		if ((m_nBufferFormat & MASK_BUFFER) == BUFFER_NIO) {
			buf = ByteBuffer.allocateDirect(_nSize * SIZEOF_FLOAT).asFloatBuffer();
		} else {
			buf = FloatBuffer.allocate(_nSize);
		}
		
		return buf;
	}

	private IntBuffer allocateIntBuffer(int _nSize) {
		IntBuffer buf;

		if ((m_nBufferFormat & MASK_BUFFER) == BUFFER_NIO) {
			buf = ByteBuffer.allocateDirect(_nSize * SIZEOF_INT).asIntBuffer();
		} else {
			buf = IntBuffer.allocate(_nSize);
		}
		
		return buf;
	}

	/**
	 * Returns the maximum number of vertices the buffer can contain.
	 * If the buffer also holds texture coordinates, colors and/or
	 * normals room will be available in exactly the same quantity.
	 * 
	 * @return The maximum number of vertices the buffer can hold
	 */
	public int getMaxSize() {
		return (m_bIndexed) ? m_nMaxIndexCount : m_nMaxVertexCount;
	}

	/**
	 * Returns the number of vertices the buffer currently contains.
	 * If the buffer also holds texture coordinates, colors and/or
	 * normals they will be available in exactly the same quantity.
	 * 
	 * @return The number of vertices in the buffer
	 */
	public int getSize() {
		return (m_bIndexed) ? m_nIndexCount : m_nVertexCount;
	}

	/**
	 * Indicates if the buffer is empty or not.
	 * 
	 * @return A boolean indicating if the buffer is empty or not
	 */
	public boolean isEmpty() {
		return (getSize() == 0);
	}

	/**
	 * Indicates if the buffer is full or not.
	 * 
	 * @return A boolean indicating if the buffer is full or not
	 */
	public boolean isFull() {
		return (getSize() >= getMaxSize());
	}
	
	/**
	 * Returns the maximum number of actual vertices the buffer can
	 * contain regardless of the fact if the buffer is indexed or not.
	 * 
	 * @return The maximum number of vertices the buffer can hold
	 */
	public int getMaxVertexCount() {
		return m_nMaxVertexCount;
	}

	/**
	 * Returns the maximum number of actual vertices the buffer currently
	 * contains regardless of the fact if the buffer is indexed or not.
	 * 
	 * @return The number of vertices in the buffer
	 */
	public int getVertexCount() {
		return m_nVertexCount;
	}

	/**
	 * Returns the maximum number of vertex indices the buffer can contain.
	 * 
	 * @return The maximum number of vertex indices the buffer can hold
	 */
	public int getMaxIndexCount() {
		return m_nMaxIndexCount;
	}

	/**
	 * Returns the number of vertex indices the buffer currently contains.
	 * 
	 * @return The number of vertex indices in the buffer
	 */
	public int getIndexCount() {
		return m_nIndexCount;
	}

	/**
	 * Resets the content of the buffer effectively making it empty
	 */
	public void reset() {
		m_nVertexCount = 0;
	}

	/**
	 * Returns the format of the buffer which indicates which elements
	 * are contained by the buffer and what internal layout the buffer uses.
	 * @return An integer bit field 
	 */
	public int getFormat() {
		return m_nBufferFormat;
	}

	/**
	 * Determines if the vertex buffer contains an index into the vertex data
	 * @return A boolean indicating if an index exists
	 */
	public boolean isIndexed() {
		return m_bIndexed;
	}

	/**
	 * Determines if the data in the vertex buffer is stored in one large
	 * interleaved buffer or as separate buffers. 
	 * @return A boolean indicating if the data is stored in one buffer or not
	 */
	public boolean isInterleaved() {
		return m_bInterleaved;
	}

	/**
	 * Returns the buffer containing the texture coordinates.
	 * @return A FloatBuffer containing the vertex buffer's texture coordinates
	 */
	public FloatBuffer getTexCoords() {
		return m_textureCoords;
	}

	/**
	 * Returns the buffer containing the vertex colors.
	 * @return A FloatBuffer containing the vertex buffer's vertex colors
	 */
	public FloatBuffer getColors() {
		return m_colors;
	}

	/**
	 * Returns the buffer containing the vertex normals.
	 * @return A FloatBuffer containing the vertex buffer's vertex normals
	 */
	public FloatBuffer getNormals() {
		return m_normals;
	}

	/**
	 * Returns the buffer containing the vertices.
	 * @return A FloatBuffer containing the vertex buffer's vertices
	 */
	public FloatBuffer getVertices() {
		return m_vertices;
	}

	/**
	 * If the vertex buffer is interleaved this returns the buffer containing all the vertex data.
	 * @return A FloatBuffer containing all the vertex data
	 */
	public FloatBuffer getBuffer() {
		return m_vertices;
	}

	/**
	 * If the vertex buffer contains an index this returns the buffer containing all the indices.
	 * @return An IntBuffer containing all the indices
	 */
	public IntBuffer getIndices() {
		return m_indices;
	}

	/**
	 * Returns the offset of the texture coordinates for the n-th vertex in the texture buffer.  
	 * @param _nIndex The index of the vertex
	 * @return The offset where the requested information can be found in the buffer
	 */
	public int getTextureOffset(int _nIndex) {
		return m_nTextureOffset + _nIndex * m_nTextureInfoSize;
	}
	
	/**
	 * Returns the offset of the color information for the n-th vertex in the color buffer.  
	 * @param _nIndex The index of the vertex
	 * @return The offset where the requested information can be found in the buffer
	 */
	public int getColorOffset(int _nIndex) {
		return m_nColorOffset + _nIndex * m_nColorInfoSize;
	}
	
	/**
	 * Returns the offset of the vertex normals for the n-th vertex in the texture buffer.  
	 * @param _nIndex The index of the vertex
	 * @return The offset where the requested information can be found in the buffer
	 */
	public int getNormalOffset(int _nIndex) {
		return m_nNormalOffset + _nIndex * m_nNormalInfoSize;
	}
	
	/**
	 * Returns the offset of the n-th vertex in the vertex buffer.  
	 * @param _nIndex The index of the vertex
	 * @return The offset where the requested information can be found in the buffer
	 */
	public int getVertexOffset(int _nIndex) {
		return m_nVertexOffset + _nIndex * m_nVertexInfoSize;
	}
	
	protected int getVertexInfoSize() {
		return m_nVertexInfoSize;
	}
	
	/**
	 * Gets the specified vertex from the buffer
	 * 
	 * @param _nIndex The index of the vertex to retrieve
	 * @param _tuple Tuple3f object where the vertex coordinates will be stored
	 */
	public void getVertex(int _nIndex, Tuple3f _tuple) {
		int nIdx = getVertexOffset(_nIndex);
		_tuple.x = m_vertices.get(nIdx + 0);
		_tuple.y = m_vertices.get(nIdx + 1);
		_tuple.z = m_vertices.get(nIdx + 2);
	}

	/**
	 * Gets the specified vertices from the buffer
	 * 
	 * @param _nIndex The index of the first vertex to retrieve
	 * @param _coords Float array of at least 3 elements for each requested vertex
	 * @param _nOffset The offset into the buffer where the first coordinate should go
	 * @param _nCount The number of vertices to retrieve from the buffer
	 */
	public void getVertices(int _nIndex, float[] _coords, int _nOffset, int _nCount) {
		int nIdx = getVertexOffset(_nIndex);
		if (m_bInterleaved) {
			for (int i = 0; i < _nCount; i++) {
				m_vertices.position(nIdx);
				m_vertices.get(_coords, _nOffset, 3);
				nIdx += m_nVertexInfoSize;
				_nOffset += 3;
			}
		} else {
			m_vertices.position(nIdx);
			m_vertices.get(_coords, _nOffset, 3 * _nCount);
		}
	}

	/**
	 * Adds the specified vertex to the buffer
	 * 
	 * @param _fX The vertex x coordinate
	 * @param _fY The vertex y coordinate
	 * @param _fZ The vertex z coordinate
	 */
	public void addVertex(float _fX, float _fY, float _fZ) {
		int nIdx = getVertexOffset(m_nVertexCount);
		m_vertices.put(nIdx + 0, _fX);
		m_vertices.put(nIdx + 1, _fY);
		m_vertices.put(nIdx + 2, _fZ);
		m_nVertexCount++;
	}

	/**
	 * Adds the specified vertex to the buffer
	 * 
	 * @param _tuple Tuple3f object to add to the vertex buffer
	 */
	public void addVertex(Tuple3f _tuple) {
		int nIdx = getVertexOffset(m_nVertexCount);
		m_vertices.put(nIdx + 0, _tuple.x);
		m_vertices.put(nIdx + 1, _tuple.y);
		m_vertices.put(nIdx + 2, _tuple.z);
		m_nVertexCount++;
	}

	/**
	 * Adds the specified vertices from the buffer
	 * 
	 * @param _coords Float array of at least 3 elements for each requested vertex
	 * @param _nOffset The offset into the buffer where the first coordinate should come from
	 * @param _nCount The number of vertices to store in the buffer
	 */
	public void addVertices(float[] _coords, int _nOffset, int _nCount) {
		int nIdx = getVertexOffset(m_nVertexCount);
		for (int i = 0; i < _nCount; i++) {
			m_vertices.put(nIdx + 0, _coords[_nOffset + 0]);
			m_vertices.put(nIdx + 1, _coords[_nOffset + 1]);
			m_vertices.put(nIdx + 2, _coords[_nOffset + 2]);
			nIdx += m_nVertexInfoSize;
			_nOffset += 3;
		}
		m_nVertexCount += _nCount;
	}

	/**
	 * Changes the specified vertex in the buffer
	 * 
	 * @param _nIndex The index of the vertex to change
	 * @param _fX The vertex x coordinate
	 * @param _fY The vertex y coordinate
	 * @param _fZ The vertex z coordinate
	 */
	public void setVertex(int _nIndex, float _fX, float _fY, float _fZ) {
		int nIdx = getVertexOffset(_nIndex);
		m_vertices.put(nIdx + 0, _fX);
		m_vertices.put(nIdx + 1, _fY);
		m_vertices.put(nIdx + 2, _fZ);
	}

	/**
	 * Changes the specified vertex in the buffer
	 * 
	 * @param _nIndex The index of the vertex to change
	 * @param _tuple Tuple3f object to use to change the vertex buffer
	 */
	public void setVertex(int _nIndex, Tuple3f _tuple) {
		int nIdx = getVertexOffset(_nIndex);
		m_vertices.put(nIdx + 0, _tuple.x);
		m_vertices.put(nIdx + 1, _tuple.y);
		m_vertices.put(nIdx + 2, _tuple.z);
	}

	/**
	 * Changes the specified vertices in the buffer
	 * 
	 * @param _nIndex The index of the first vertex to change
	 * @param _coords Float array of at least 3 elements for each requested vertex
	 * @param _nOffset The offset into the buffer where the first coordinate should come from
	 * @param _nCount The number of vertices to store in the buffer
	 */
	public void setVertices(int _nIndex, float[] _coords, int _nOffset, int _nCount) {
		int nIdx = getVertexOffset(_nIndex);
		for (int i = 0; i < _nCount; i++) {
			m_vertices.put(nIdx + 0, _coords[_nOffset + 0]);
			m_vertices.put(nIdx + 1, _coords[_nOffset + 1]);
			m_vertices.put(nIdx + 2, _coords[_nOffset + 2]);
			nIdx += m_nVertexInfoSize;
			_nOffset += 3;
		}
	}

	/**
	 * Gets the specified texture coordinate tuple from the buffer
	 * 
	 * @param _nIndex The index of the texture coordinates to change
	 * @param _tuple Tuple2f object where the texture coordinates will be stored
	 */
	public void getTexCoord(int _nIndex, Tuple2f _tuple) {
		int nIdx = getTextureOffset(_nIndex);
		_tuple.x = m_textureCoords.get(nIdx + 0);
		_tuple.y = m_textureCoords.get(nIdx + 1);
	}

	/**
	 * Gets the specified texture coordinate tuples from the buffer
	 * 
	 * @param _nIndex The index of the first texture coordinate tuple to retrieve
	 * @param _coords Float array of at least 2 elements for each requested texture coordinate tuple
	 * @param _nOffset The offset into the buffer where the first coordinate should go
	 * @param _nCount The number of texture coordinate tuples to retrieve from the buffer
	 */
	public void getTexCoords2f(int _nIndex, float[] _coords, int _nOffset, int _nCount) {
		int nIdx = getTextureOffset(_nIndex);
		if (m_bInterleaved) {
			for (int i = 0; i < _nCount; i++) {
				m_textureCoords.position(nIdx);
				m_textureCoords.get(_coords, _nOffset, 2);
				nIdx += m_nVertexInfoSize;
				_nOffset += 2;
			}
		} else {
			m_textureCoords.position(nIdx);
			m_textureCoords.get(_coords, _nOffset, 2 * _nCount);
		}
	}

	/**
	 * Adds a new 2D texture coordinate tuple to the buffer
	 * 
	 * @param _fS The texture s coordinate
	 * @param _fT The texture t coordinate
	 */
	public void addTexCoord(float _fS, float _fT) {
		int nIdx = getTextureOffset(m_nVertexCount);
		m_textureCoords.put(nIdx + 0, _fS);
		m_textureCoords.put(nIdx + 1, _fT);
	}

	/**
	 * Adds a new 2D texture coordinate tuple to the buffer
	 * 
	 * @param _tuple Tuple2f object to add to the vertex buffer
	 */
	public void addTexCoord(Tuple2f _tuple) {
		int nIdx = getTextureOffset(m_nVertexCount);
		m_textureCoords.put(nIdx + 0, _tuple.x);
		m_textureCoords.put(nIdx + 1, _tuple.y);
	}

	/**
	 * Adds the specified texture coordinate tuples to the buffer
	 * 
	 * @param _coords Float array of at least 2 elements for each texture coordinate tuple
	 * @param _nOffset The offset into the buffer where the first coordinate should come from
	 * @param _nCount The number of texture coordinate tuples to store in the buffer
	 */
	public void addTexCoords2f(float[] _coords, int _nOffset, int _nCount) {
		int nIdx = getTextureOffset(m_nVertexCount);
		if (isInterleaved()) {
			for (int i = 0; i < _nCount; i++) {
				m_textureCoords.position(nIdx);
				m_textureCoords.put(_coords, _nOffset, 2);
				nIdx += m_nVertexInfoSize;
				_nOffset += 2;
			}
		} else {
			m_textureCoords.position(nIdx);
			m_textureCoords.put(_coords, _nOffset, 2 * _nCount);
		}
	}

	/**
	 * Changes the specified texture coordinate tuple in the buffer
	 * 
	 * @param _nIndex The index of the texture coordinates to change
	 * @param _fS The vertex x coordinate
	 * @param _fT The vertex y coordinate
	 */
	public void setTexCoord(int _nIndex, float _fS, float _fT) {
		int nIdx = getTextureOffset(_nIndex);
		m_textureCoords.put(nIdx + 0, _fS);
		m_textureCoords.put(nIdx + 1, _fT);
	}

	/**
	 * Changes the specified vertex in the buffer
	 * 
	 * @param _nIndex The index of the texture coordinates to change
	 * @param _tuple Tuple2f object to use to change the vertex buffer
	 */
	public void setTexCoord(int _nIndex, Tuple2f _tuple) {
		int nIdx = getTextureOffset(_nIndex);
		m_textureCoords.put(nIdx + 0, _tuple.x);
		m_textureCoords.put(nIdx + 1, _tuple.y);
	}

	/**
	 * Changes the specified texture coordinate tuples in the buffer
	 * 
	 * @param _nIndex The index of the first texture coordinate tuple to change
	 * @param _coords Float array of at least 2 elements for each texture coordinate tuple
	 * @param _nOffset The offset into the buffer where the first coordinate should come from
	 * @param _nCount The number of texture coordinate tuples to store in the buffer
	 */
	public void setTexCoords2f(int _nIndex, float[] _coords, int _nOffset, int _nCount) {
		int nIdx = getTextureOffset(_nIndex);
		if (isInterleaved()) {
			for (int i = 0; i < _nCount; i++) {
				m_textureCoords.position(nIdx);
				m_textureCoords.put(_coords, _nOffset, 2);
				nIdx += m_nVertexInfoSize;
				_nOffset += 2;
			}
		} else {
			m_textureCoords.position(nIdx);
			m_textureCoords.put(_coords, _nOffset, 2 * _nCount);
		}
	}

	/**
	 * Gets an RGB color from the buffer
	 * 
	 * @param _nIndex The index of the texture coordinates to change
	 * @param _color Color3f object where the texture coordinates will be stored
	 */
	public void getColor(int _nIndex, Color3f _color) {
		int nIdx = _nIndex * m_nVertexInfoSize + m_nColorOffset;
		_color.x = m_colors.get(nIdx + 0);
		_color.y = m_colors.get(nIdx + 1);
		_color.z = m_colors.get(nIdx + 2);
	}

	/**
	 * Gets an RGB color from the buffer
	 * 
	 * @param _nIndex The index of the firsdt color to retrieve
	 * @param _coords Float array of at least 3 elements for each requested color
	 * @param _nOffset The offset into the buffer where the first color element should go
	 * @param _nCount The number of colors to retrieve from the buffer
	 */
	public void getColors3f(int _nIndex, float[] _coords, int _nOffset, int _nCount) {
		int nIdx = getColorOffset(_nIndex);
		if (m_bInterleaved) {
			for (int i = 0; i < _nCount; i++) {
				m_colors.position(nIdx);
				m_colors.get(_coords, _nOffset, 3);
				nIdx += m_nVertexInfoSize;
				_nOffset += 3;
			}
		} else {
			m_colors.position(nIdx);
			m_colors.get(_coords, _nOffset, 3 * _nCount);
		}
	}

	/**
	 * Gets an RGBA color from the buffer
	 * 
	 * @param _nIndex The index of the texture coordinates to change
	 * @param _color Color4f object where the texture coordinates will be stored
	 */
	public void getColor(int _nIndex, Color4f _color) {
		int nIdx = getColorOffset(_nIndex);
		_color.x = m_colors.get(nIdx + 0);
		_color.y = m_colors.get(nIdx + 1);
		_color.z = m_colors.get(nIdx + 2);
		_color.w = m_colors.get(nIdx + 3);
	}

	/**
	 * Gets an RGBA color from the buffer
	 * 
	 * @param _nIndex The index of the firsdt color to retrieve
	 * @param _coords Float array of at least 4 elements for each requested color
	 * @param _nOffset The offset into the buffer where the first color element should go
	 * @param _nCount The number of colors to retrieve from the buffer
	 */
	public void getColors4f(int _nIndex, float[] _coords, int _nOffset, int _nCount) {
		int nIdx = getColorOffset(_nIndex);
		if (m_bInterleaved) {
			for (int i = 0; i < _nCount; i++) {
				m_colors.position(nIdx);
				m_colors.get(_coords, _nOffset, 4);
				nIdx += m_nVertexInfoSize;
				_nOffset += 4;
			}
		} else {
			m_colors.position(nIdx);
			m_colors.get(_coords, _nOffset, 4 * _nCount);
		}
	}

	/**
	 * Adds a new RGB color to the buffer
	 * 
	 * @param _fR The color's red component
	 * @param _fG The color's green component
	 * @param _fB The color's blue component
	 */
	public void addColor(float _fR, float _fG, float _fB) {
		int nIdx = getColorOffset(m_nVertexCount);
		m_colors.put(nIdx + 0, _fR);
		m_colors.put(nIdx + 1, _fG);
		m_colors.put(nIdx + 2, _fB);
	}

	/**
	 * Adds a new RGB color to the buffer
	 * 
	 * @param _color Color3f object to add to the vertex buffer
	 */
	public void addColor(Color3f _color) {
		int nIdx = getColorOffset(m_nVertexCount);
		m_colors.put(nIdx + 0, _color.x);
		m_colors.put(nIdx + 1, _color.y);
		m_colors.put(nIdx + 2, _color.z);
	}

	/**
	 * Adds an RGB color to the buffer
	 * 
	 * @param _coords Float array of at least 3 elements for each color to store
	 * @param _nOffset The offset into the buffer where the first color element should come from
	 * @param _nCount The number of colors to store in the buffer
	 */
	public void addColors3f(float[] _coords, int _nOffset, int _nCount) {
		int nIdx = getColorOffset(m_nVertexCount);
		if (isInterleaved()) {
			for (int i = 0; i < _nCount; i++) {
				m_colors.position(nIdx);
				m_colors.put(_coords, _nOffset, 3);
				nIdx += m_nVertexInfoSize;
				_nOffset += 3;
			}
		} else {
			m_colors.position(nIdx);
			m_colors.put(_coords, _nOffset, 3 * _nCount);
		}
	}

	/**
	 * Changes the specified RGB color in the buffer
	 * 
	 * @param _nIndex The index of the color to change
	 * @param _fR The color's red component
	 * @param _fG The color's green component
	 * @param _fB The color's blue component
	 */
	public void setColor(int _nIndex, float _fR, float _fG, float _fB) {
		int nIdx = getColorOffset(_nIndex);
		m_colors.put(nIdx + 0, _fR);
		m_colors.put(nIdx + 1, _fG);
		m_colors.put(nIdx + 2, _fB);
	}

	/**
	 * Changes the specified RGB color in the buffer
	 * 
	 * @param _nIndex The index of the color to change
	 * @param _color Color3f object to use to change the vertex buffer
	 */
	public void setColor(int _nIndex, Color3f _color) {
		int nIdx = getColorOffset(_nIndex);
		m_colors.put(nIdx + 0, _color.x);
		m_colors.put(nIdx + 1, _color.y);
		m_colors.put(nIdx + 2, _color.z);
	}

	/**
	 * Changes the specified RGB colors in the buffer
	 * 
	 * @param _nIndex The index of the first color to change
	 * @param _coords Float array of at least 3 elements for each color to store
	 * @param _nOffset The offset into the buffer where the first color element should come from
	 * @param _nCount The number of colors to store in the buffer
	 */
	public void setColors3f(int _nIndex, float[] _coords, int _nOffset, int _nCount) {
		int nIdx = getColorOffset(_nIndex);
		if (isInterleaved()) {
			for (int i = 0; i < _nCount; i++) {
				m_colors.position(nIdx);
				m_colors.put(_coords, _nOffset, 3);
				nIdx += m_nVertexInfoSize;
				_nOffset += 3;
			}
		} else {
			m_colors.position(nIdx);
			m_colors.put(_coords, _nOffset, 3 * _nCount);
		}
	}

	/**
	 * Adds a new RGB color to the buffer
	 * 
	 * @param _fR The color's red component
	 * @param _fG The color's green component
	 * @param _fB The color's blue component
	 * @param _fA The color's alpha component
	 */
	public void addColor(float _fR, float _fG, float _fB, float _fA) {
		int nIdx = getColorOffset(m_nVertexCount);
		m_colors.put(nIdx + 0, _fR);
		m_colors.put(nIdx + 1, _fG);
		m_colors.put(nIdx + 2, _fB);
		m_colors.put(nIdx + 3, _fA);
	}

	/**
	 * Adds a new RGBA color to the buffer
	 * 
	 * @param _color Color4f object to add to the vertex buffer
	 */
	public void addColor(Color4f _color) {
		int nIdx = getColorOffset(m_nVertexCount);
		m_colors.put(nIdx + 0, _color.x);
		m_colors.put(nIdx + 1, _color.y);
		m_colors.put(nIdx + 2, _color.z);
		m_colors.put(nIdx + 3, _color.w);
	}

	/**
	 * Adds an RGBA color to the buffer
	 * 
	 * @param _coords Float array of at least 4 elements for each color to store
	 * @param _nOffset The offset into the buffer where the first color element should come from
	 * @param _nCount The number of colors to store in the buffer
	 */
	public void addColors4f(float[] _coords, int _nOffset, int _nCount) {
		int nIdx = getColorOffset(m_nVertexCount);
		if (isInterleaved()) {
			for (int i = 0; i < _nCount; i++) {
				m_colors.position(nIdx);
				m_colors.put(_coords, _nOffset, 4);
				nIdx += m_nVertexInfoSize;
				_nOffset += 4;
			}
		} else {
			m_colors.position(nIdx);
			m_colors.put(_coords, _nOffset, 4 * _nCount);
		}
	}

	/**
	 * Changes the specified RGBA color in the buffer
	 * 
	 * @param _nIndex The index of the color to change
	 * @param _fR The color's red component
	 * @param _fG The color's green component
	 * @param _fB The color's blue component
	 * @param _fA The color's alpha component
	 */
	public void setColor(int _nIndex, float _fR, float _fG, float _fB, float _fA) {
		int nIdx = _nIndex * m_nVertexInfoSize + m_nColorOffset;
		m_colors.put(nIdx + 0, _fR);
		m_colors.put(nIdx + 1, _fG);
		m_colors.put(nIdx + 2, _fB);
		m_colors.put(nIdx + 3, _fA);
	}

	/**
	 * Changes the specified RGBA color in the buffer
	 * 
	 * @param _nIndex The index of the color to change
	 * @param _color Color4f object to use to change the vertex buffer
	 */
	public void setColor(int _nIndex, Color4f _color) {
		int nIdx = getColorOffset(_nIndex);
		m_colors.put(nIdx + 0, _color.x);
		m_colors.put(nIdx + 1, _color.y);
		m_colors.put(nIdx + 2, _color.z);
		m_colors.put(nIdx + 3, _color.w);
	}

	/**
	 * Changes the specified RGB colors in the buffer
	 * 
	 * @param _nIndex The index of the first color to change
	 * @param _coords Float array of at least 4 elements for each color to store
	 * @param _nOffset The offset into the buffer where the first color element should come from
	 * @param _nCount The number of colors to store in the buffer
	 */
	public void setColors4f(int _nIndex, float[] _coords, int _nOffset, int _nCount) {
		int nIdx = getColorOffset(_nIndex);
		if (isInterleaved()) {
			for (int i = 0; i < _nCount; i++) {
				m_colors.position(nIdx);
				m_colors.put(_coords, _nOffset, 4);
				nIdx += m_nVertexInfoSize;
				_nOffset += 4;
			}
		} else {
			m_colors.position(nIdx);
			m_colors.put(_coords, _nOffset, 4 * _nCount);
		}
	}

	/**
	 * Gets the specified normal from the buffer
	 * 
	 * @param _nIndex The index of the normal to retrieve
	 * @param _tuple Tuple3f object where the normal coordinates will be stored
	 */
	public void getNormal(int _nIndex, Tuple3f _tuple) {
		int nIdx = _nIndex * m_nVertexInfoSize + m_nNormalOffset;
		_tuple.x = m_normals.get(nIdx + 0);
		_tuple.y = m_normals.get(nIdx + 1);
		_tuple.z = m_normals.get(nIdx + 2);
	}

	/**
	 * Gets the specified normals from the buffer
	 * 
	 * @param _nIndex The index of the first normal to retrieve
	 * @param _coords Float array of at least 3 elements for each normal to retrieve
	 * @param _nOffset The offset into the buffer where the first coordinate should go
	 * @param _nCount The number of normales to retrieve from the buffer
	 */
	public void getNormals(int _nIndex, float[] _coords, int _nOffset, int _nCount) {
		int nIdx = getNormalOffset(_nIndex);
		if (m_bInterleaved) {
			for (int i = 0; i < _nCount; i++) {
				m_normals.position(nIdx);
				m_normals.get(_coords, _nOffset, 3);
				nIdx += m_nVertexInfoSize;
				_nOffset += 3;
			}
		} else {
			m_normals.position(nIdx);
			m_normals.get(_coords, _nOffset, 3 * _nCount);
		}
	}

	/**
	 * Adds a new normal to the buffer
	 * 
	 * @param _fX The normal x coordinate
	 * @param _fY The normal y coordinate
	 * @param _fZ The normal z coordinate
	 */
	public void addNormal(float _fX, float _fY, float _fZ) {
		int nIdx = getNormalOffset(m_nVertexCount);
		m_normals.put(nIdx + 0, _fX);
		m_normals.put(nIdx + 1, _fY);
		m_normals.put(nIdx + 2, _fZ);
	}

	/**
	 * Adds a new normal to the buffer
	 * 
	 * @param _tuple Tuple3f object to add to the normal buffer
	 */
	public void addNormal(Tuple3f _tuple) {
		int nIdx = getNormalOffset(m_nVertexCount);
		m_normals.put(nIdx + 0, _tuple.x);
		m_normals.put(nIdx + 1, _tuple.y);
		m_normals.put(nIdx + 2, _tuple.z);
	}

	/**
	 * Adds the specified normals to the buffer
	 * 
	 * @param _coords Float array of at least 3 elements for each normal to store
	 * @param _nOffset The offset into the buffer where the first coordinate should come from
	 * @param _nCount The number of normals to store in the buffer
	 */
	public void addNormals(float[] _coords, int _nOffset, int _nCount) {
		int nIdx = getNormalOffset(m_nVertexCount);
		if (isInterleaved()) {
			for (int i = 0; i < _nCount; i++) {
				m_normals.position(nIdx);
				m_normals.put(_coords, _nOffset, 3);
				nIdx += m_nVertexInfoSize;
				_nOffset += 3;
			}
		} else {
			m_normals.position(nIdx);
			m_normals.put(_coords, _nOffset, 3 * _nCount);
		}
	}

	/**
	 * Changes the specified normal in the buffer
	 * 
	 * @param _nIndex The index of the normal to change
	 * @param _fX The normal x coordinate
	 * @param _fY The normal y coordinate
	 * @param _fZ The normal z coordinate
	 */
	public void setNormal(int _nIndex, float _fX, float _fY, float _fZ) {
		int nIdx = getNormalOffset(_nIndex);
		m_normals.put(nIdx + 0, _fX);
		m_normals.put(nIdx + 1, _fY);
		m_normals.put(nIdx + 2, _fZ);
	}

	/**
	 * Changes the specified normal in the buffer
	 * 
	 * @param _nIndex The index of the normal to change
	 * @param _tuple Tuple3f object to use to change the normal buffer
	 */
	public void setNormal(int _nIndex, Tuple3f _tuple) {
		int nIdx = getNormalOffset(_nIndex);
		m_normals.put(nIdx + 0, _tuple.x);
		m_normals.put(nIdx + 1, _tuple.y);
		m_normals.put(nIdx + 2, _tuple.z);
	}

	/**
	 * Changes the specified normals in the buffer
	 * 
	 * @param _nIndex The index of the first normal to store
	 * @param _coords Float array of at least 3 elements for each normal to store
	 * @param _nOffset The offset into the buffer where the first coordinate should come from
	 * @param _nCount The number of normals to store in the buffer
	 */
	public void setNormals(int _nIndex, float[] _coords, int _nOffset, int _nCount) {
		int nIdx = getNormalOffset(_nIndex);
		if (isInterleaved()) {
			for (int i = 0; i < _nCount; i++) {
				m_normals.position(nIdx);
				m_normals.put(_coords, _nOffset, 3);
				nIdx += m_nVertexInfoSize;
				_nOffset += 3;
			}
		} else {
			m_normals.position(nIdx);
			m_normals.put(_coords, _nOffset, 3 * _nCount);
		}
	}

	/**
	 * Gets the specified vertex index from the buffer
	 * 
	 * @param _nIndex The index of the vertex index to retrieve
	 * @return The requested vertex index
	 */
	public int getIndex(int _nIndex) {
		return m_indices.get(_nIndex);
	}

	/**
	 * Gets the specified vertix indices from the buffer
	 * 
	 * @param _nIndex The index of the first vertex index to retrieve
	 * @param _indices Int array of at least 1 element for each requested vertex index
	 * @param _nOffset The offset into the buffer where the first index should go
	 * @param _nCount The number of indices to retrieve from the buffer
	 */
	public void getIndices(int _nIndex, int[] _indices, int _nOffset, int _nCount) {
		m_indices.position(_nIndex);
		m_indices.get(_indices, _nOffset, _nCount);
	}

	/**
	 * Adds the specified vertex index to the buffer
	 * 
	 * @param _nVertexIndex The vertex x coordinate
	 */
	public void addIndex(int _nVertexIndex) {
		m_indices.put(m_nIndexCount++, _nVertexIndex);
	}

	/**
	 * Adds the specified vertex indices to the buffer
	 * 
	 * @param _indices Int array of at least 1 element for each requested vertex index
	 * @param _nOffset The offset into the buffer where the first index should come from
	 * @param _nCount The number of indices to store in the buffer
	 */
	public void addIndices(int[] _indices, int _nOffset, int _nCount) {
		m_indices.put(_indices, _nOffset, _nCount);
	}

	/**
	 * Changes the specified vertex in the buffer
	 * 
	 * @param _nIndex The index of the vertex to change
	 * @param _nVertexIndex The vertex x coordinate
	 */
	public void setIndex(int _nIndex, int _nVertexIndex) {
		m_indices.put(_nIndex, _nVertexIndex);
	}

	/**
	 * Changes the specified vertices in the buffer
	 * 
	 * @param _nIndex The index of the first vertex to change
	 * @param _indices Int array of at least 1 element for each requested vertex index
	 * @param _nOffset The offset into the buffer where the first coordinate should come from
	 * @param _nCount The number of vertices to store in the buffer
	 */
	public void setIndices(int _nIndex, int[] _indices, int _nOffset, int _nCount) {
		m_normals.position(_nIndex);
		m_indices.put(_indices, _nOffset, _nCount);
	}
}

/*
 * $Log$
 * Revision 1.4  2004/03/07 17:27:29  tako
 * Moved VertexBuffer from utils4gl to utils4gl/geometries.
 * The old VertexBuffer has been changed and extended to not only hold
 * vertices but also all kinds of other information like colors and textures.
 * NIO buffers are now used to store all this information which makes it
 * possible to use VertexBuffer in glVertexPointer() calls.
 *
 */
