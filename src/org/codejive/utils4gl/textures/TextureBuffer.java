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
 * Created on Dec 15, 2003
 */
package org.codejive.utils4gl.textures;

import java.nio.ByteBuffer;

import org.codejive.utils4gl.RenderContext;

import net.java.games.jogl.GL;

/** Buffer object that holds the image to use for a single texture or for multiple textures.
 * 
 * @author tako
 * @version $Revision: 228 $
 */
public class TextureBuffer {
	private RenderContext m_context;
	private ByteBuffer m_pixels;
	private int m_nWidth;
	private int m_nHeight;
	private boolean m_bHasAlphaChannel;

	private int m_nHandle;
	private boolean m_bMipMapped;
	private boolean m_bUnbound;
	

	/** Constructor for a new Texture object.
	 * @param _context Render context
	 * @param _pixels A ByteBuffer containing the image data for the texture
	 * @param _nWidth The width of the image in pixels
	 * @param _nHeight The height of the image in pixels
	 * @param _bHasAlphaChannel Determines if the image data contains alpha channel information
	 */	
	public TextureBuffer(RenderContext _context, ByteBuffer _pixels, int _nWidth, int _nHeight, boolean _bHasAlphaChannel) {
		m_context = _context;
		m_pixels = _pixels;
		m_nWidth = _nWidth;
		m_nHeight = _nHeight;
		m_bHasAlphaChannel = _bHasAlphaChannel;
		
		int textures[] = new int[1];
		_context.getGl().glGenTextures(1, textures);
		m_nHandle = textures[0];
		m_bMipMapped = true; // We turn mip-mapping on by default
		m_bUnbound = true;
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
	
	/** Returns the fact if the texture contains an alpha channel
	 * @return A boolean indicating the existence of an alpha channel
	 */	
	public boolean hasAlphaChannel() {
		return m_bHasAlphaChannel;
	}
	
	/** Returns the fact if the texture is/should be mip-mapped
	 * @return A boolean indicating if the texture is/should be mip-mapped
	 */	
	public boolean isMipMapped() {
		return m_bMipMapped;
	}
	
	/** Sets the fact if the texture should be mip-mapped
	 * @param _bMipMapped A boolean indicating if the texture should be mip-mapped
	 */
	public void setMipMapped(boolean _bMipMapped) {
		m_bMipMapped = _bMipMapped;
	}
	
	/** Returns the "handle" for this texture that is used for GL calls like glBindTexture().
	 * @return Texture handle
	 */	
	public int getHandle() {
		return m_nHandle;
	}
	
	/**
	 * Binds the texture making it ready for use
	 */
	public void bind() {
		m_context.getGl().glBindTexture(GL.GL_TEXTURE_2D, getHandle());
		if (m_bUnbound) {
			makeTexture();
			m_bUnbound = false;
		}
	}
	
	/**
	 * Binds the texture image in the requested format
	 */
	private void makeTexture() {
		if (m_bMipMapped) {
			makeMipmappedTexture();
		} else {
			makePlainTexture();
		}
	}
	
	private void makePlainTexture() {
		if (hasAlphaChannel()) {
			m_context.getGl().glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, getWidth(), getHeight(), 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, getPixels());
		} else {
			m_context.getGl().glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB, getWidth(), getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, getPixels());
		}
	}
	
	private void makeMipmappedTexture() {
		if (hasAlphaChannel()) {
			m_context.getGlu().gluBuild2DMipmaps(GL.GL_TEXTURE_2D, GL.GL_RGBA8, getWidth(), getHeight(), GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, getPixels());
		} else {
			m_context.getGlu().gluBuild2DMipmaps(GL.GL_TEXTURE_2D, GL.GL_RGB8, getWidth(), getHeight(), GL.GL_RGB, GL.GL_UNSIGNED_BYTE, getPixels());
		}
	}
}

/*
 * $Log$
 * Revision 1.2  2004/03/17 00:39:39  tako
 * Now remembers if the image data contains alpha channel information.
 * Textures are now automatically generated the first time bind() is called.
 * Mip-mapping is now an option of the object (true by default and can only
 * be changed before the first call to bind()).
 *
 * Revision 1.1  2004/03/07 16:26:38  tako
 * Texture has been turned into an interface and textures itself are split into
 * TextureBuffers that hold the binary data while Textures determine which
 * part of the buffer to use. All existing code has been adjusted accordingly.
 *
 */