
package org.codejive.utils4gl;

/**
 * This class encapsulates a buffer of vertices making it easy to fill
 * it one vertex at a time.
 *  
 * @author Tako
 * @version $Revision: 48 $
 */
public class VertexBuffer {
	private float[] m_vfCoordinates;
	private int m_nVertexCount;

	/**
	 * Constructor for a new VertexBuffer of the specified size
	 * 
	 * @param _nMaxVertexCount The size of the buffer defined by the number of vertices it can contain
	 */
	public VertexBuffer(int _nMaxVertexCount) {
		m_nVertexCount = 0;
		m_vfCoordinates = new float[_nMaxVertexCount * 3];
	}

	/**
	 * Returns a reference to the internal array of floats containing all the
	 * vertices previously added using addVertex(). The internal array consists
	 * of a number of float triplets specifying the x, y and z coordinates
	 * for each vertex.
	 * NB: The size of the array is only an indication of the maximum number
	 * of vertices it may contain, it does not mean it actually contains that
	 * many. Always use getVertexCount() to determine how many vertices the
	 * buffer contains.
	 * 
	 * @return An array of floats
	 */
	public float[] getCoordinates() {
		return m_vfCoordinates;
	}

	/**
	 * Adds a new vertex to the buffer
	 * 
	 * @param _fX The vertex x coordinate
	 * @param _fY The vertex y coordinate
	 * @param _fZ The vertex z coordinate
	 */
	public void addVertex(float _fX, float _fY, float _fZ) {
		int nIdx = m_nVertexCount * 3;
		m_vfCoordinates[nIdx] = _fX;
		m_vfCoordinates[nIdx + 1] = _fY;
		m_vfCoordinates[nIdx + 2] = _fZ;
		m_nVertexCount++;
	}

	/**
	 * Returns the number of vertices the buffer currently contains
	 * 
	 * @return The number of vertices in the buffer
	 */
	public int getVertexCount() {
		return m_nVertexCount;
	}

	/**
	 * Resets the content of the buffer effectively making it empty
	 */
	public void reset() {
		m_nVertexCount = 0;
	}
}

/*
 * $Log$
 * Revision 1.2  2003/11/17 10:49:59  tako
 * Added CVS macros for revision and log.
 *
 */
