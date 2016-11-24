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

import javax.vecmath.TexCoord4f;

/** Object that contains all the information necessary to use and render a texture.
 * 
 * @author tako
 * @version $Revision: 214 $
 */
public class SimpleTexture implements Texture {
    private TextureBuffer m_buffer;
    private TexCoord4f m_coords;

	/** Constructor for a new Texture object.
	 * @param _buffer A TextureBuffer containing the image data for the texture
	 */	
    public SimpleTexture(TextureBuffer _buffer) {
		m_buffer = _buffer;
		m_coords = new TexCoord4f(0.0f, 0.0f, 1.0f, 1.0f);
    }

	/** Constructor for a new Texture object.
	 * @param _buffer A TextureBuffer containing the image data for the texture
	 * @param _fS The texture S coordinate to use for this texture
	 * @param _fT The texture T coordinate to use for this texture
	 * @param _fU The texture U coordinate to use for this texture
	 * @param _fV The texture V coordinate to use for this texture
	 */	
	public SimpleTexture(TextureBuffer _buffer, float _fS, float _fT, float _fU, float _fV) {
		m_buffer = _buffer;
		m_coords = new TexCoord4f(_fS, _fT, _fU, _fV);
	}

	/** Constructor for a new Texture object.
	 * @param _buffer A TextureBuffer containing the image data for the texture
	 * @param _coords The texture coordinates to use for this texture
	 */	
	public SimpleTexture(TextureBuffer _buffer, TexCoord4f _coords) {
		m_buffer = _buffer;
		m_coords = new TexCoord4f(_coords);
	}

	/** Returns the texture buffer for this texture.
	 * @return A reference to the internal TextureBuffer containing the
	 * texture's image data.
	 */	
    public TextureBuffer getBuffer() {
        return m_buffer;
    }

	/** Returns the texture coordinates to use for this texture.
	 * @return A TexCoords4f object with the coordinates to use.
	 */	
	public TexCoord4f getTexCoords() {
		return m_coords;
	}
	
	/** Returns the S texture coordinate to use for this texture.
	 * @return The S coordinate
	 */	
	public float getS() {
		return m_coords.w;
	}
	
	/** Returns the T texture coordinate to use for this texture.
	 * @return The T coordinate
	 */	
	public float getT() {
		return m_coords.x;
	}
	
	/** Returns the U texture coordinate to use for this texture.
	 * @return The U coordinate
	 */	
	public float getU() {
		return m_coords.y;
	}
	
	/** Returns the V texture coordinate to use for this texture.
	 * @return The V coordinate
	 */	
	public float getV() {
		return m_coords.z;
	}
	
	/** Convenience method to quickly bind a texture
	 */
	public void bind() {
		getBuffer().bind();
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