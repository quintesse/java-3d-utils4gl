package org.codejive.utils4gl;

import java.nio.ByteBuffer;

/*
 * @version $Revision: 101 $
 */
/** Object that encapsulates an image for use as a texture.
 */
public class Texture {
    private ByteBuffer m_pixels;
    private int m_nWidth;
    private int m_nHeight;

	/** Constructor for a new Texture object.
	 * @param _pixels A ByteBuffer containing the image data for the texture
	 * @param _nWidth The width of the image in pixels
	 * @param _nHeight The height of the image in pixels
	 */	
    public Texture(ByteBuffer _pixels, int _nWidth, int _nHeight) {
		m_pixels = _pixels;
		m_nWidth = _nWidth;
        m_nHeight = _nHeight;
    }

	/** Returns the texture's image data.
	 * @return A reference to the internal ByteBuffer containing the
	 * texture's image data.
	 */	
    public ByteBuffer getPixels() {
        return m_pixels;
    }

	/** Returns the width of the texture in pixels.
	 * @return Width in pixels
	 */	
    public int getWidth() {
        return m_nWidth;
    }

	/** Returns the height of the texture in pixels.
	 * @return Height in pixels
	 */	
	public int getHeight() {
		return m_nHeight;
	}
}

/*
 * $Log$
 * Revision 1.3  2003/11/20 16:23:27  tako
 * Updated Java docs.
 *
 * Revision 1.2  2003/11/17 10:49:59  tako
 * Added CVS macros for revision and log.
 *
 */
