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
 * Created on Nov 17, 2003
 */
package org.codejive.utils4gl.textures;

import javax.vecmath.TexCoord4f;

/**
 * @author tako
 * @version $Revision: 214 $
 */
public interface Texture {
	/** Returns the texture buffer for this texture.
	 * @return A reference to the internal TextureBuffer containing the
	 * texture's image data.
	 */
	public abstract TextureBuffer getBuffer();
	/** Returns the texture coordinates to use for this texture.
	 * @return A TexCoords4f object with the coordinates to use.
	 */
	public abstract TexCoord4f getTexCoords();
	/** Returns the S texture coordinate to use for this texture.
	 * @return The S coordinate
	 */
	public abstract float getS();
	/** Returns the T texture coordinate to use for this texture.
	 * @return The T coordinate
	 */
	public abstract float getT();
	/** Returns the U texture coordinate to use for this texture.
	 * @return The U coordinate
	 */
	public abstract float getU();
	/** Returns the V texture coordinate to use for this texture.
	 * @return The V coordinate
	 */
	public abstract float getV();
	/** Convenience method to quickly bind a texture
	 */
	public abstract void bind();
}

/*
 * $Log$
 * Revision 1.4  2004/03/07 16:26:38  tako
 * Texture has been turned into an interface and textures itself are split into
 * TextureBuffers that hold the binary data while Textures determine which
 * part of the buffer to use. All existing code has been adjusted accordingly.
 *
 * Revision 1.3  2003/11/20 16:23:27  tako
 * Updated Java docs.
 *
 * Revision 1.2  2003/11/17 10:49:59  tako
 * Added CVS macros for revision and log.
 *
 */
