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
 * @version $Revision: 214 $
 */
public class TextureBuffer {
	private RenderContext m_context;
	private ByteBuffer m_pixels;
	private int m_nWidth;
	private int m_nHeight;
	
	private int m_nHandle;

	/** Constructor for a new Texture object.
	 * @param _context Render context
	 * @param _pixels A ByteBuffer containing the image data for the texture
	 * @param _nWidth The width of the image in pixels
	 * @param _nHeight The height of the image in pixels
	 */	
	public TextureBuffer(RenderContext _context, ByteBuffer _pixels, int _nWidth, int _nHeight) {
		m_context = _context;
		m_pixels = _pixels;
		m_nWidth = _nWidth;
		m_nHeight = _nHeight;
		
		int textures[] = new int[1];
		_context.getGl().glGenTextures(1, textures);
		m_nHandle = textures[0];
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
	}
	
	/**
	 * Binds the texture and generates the necessary texture image
	 */
	public void makeTexture() {
		bind();
		m_context.getGl().glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB, getWidth(), getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, getPixels());
	}
	
	/**
	 * Binds the texture and generates mip-mapped texture data
	 */
	public void makeMipmappedTexture() {
		bind();
		m_context.getGlu().gluBuild2DMipmaps(GL.GL_TEXTURE_2D, GL.GL_RGB8, getWidth(), getHeight(), GL.GL_RGB, GL.GL_UNSIGNED_BYTE, getPixels());
	}
}

/*
 * $Log$
 * Revision 1.1  2004/03/07 16:26:38  tako
 * Texture has been turned into an interface and textures itself are split into
 * TextureBuffers that hold the binary data while Textures determine which
 * part of the buffer to use. All existing code has been adjusted accordingly.
 *
 */