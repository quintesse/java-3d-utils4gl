package org.codejive.utils4gl;

import java.nio.ByteBuffer;

/*
 * @version $Revision: 48 $
 */
public class Texture {
    private ByteBuffer m_pixels;
    private int m_nWidth;
    private int m_nHeight;

    public Texture(ByteBuffer _pixels, int _nWidth, int _nHeight) {
		m_pixels = _pixels;
		m_nWidth = _nWidth;
        m_nHeight = _nHeight;
    }

    public ByteBuffer getPixels() {
        return m_pixels;
    }

    public int getWidth() {
        return m_nWidth;
    }

	public int getHeight() {
		return m_nHeight;
	}
}

/*
 * $Log$
 * Revision 1.2  2003/11/17 10:49:59  tako
 * Added CVS macros for revision and log.
 *
 */
