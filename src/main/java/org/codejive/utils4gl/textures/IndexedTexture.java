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
 * Created on Jan 27, 2004
 */
package org.codejive.utils4gl.textures;

/**
 * An IndexedTexture is an extension on a standard Texture, a static image,
 * which allows a texture to have more than one state/frame An index attribute
 * determines which state/frame will currently be shown
 * .
 * @author Tako
 * @version $Revision: 214 $
 */
public interface IndexedTexture extends Texture {
	/**
	 * Returns the number of "frames" this texture has 
	 * @return A frame count
	 */
	public int getCount();
	
	/**
	 * Sets the current "frame" for this texture
	 * @param _nIndex The index of the frame to activate
	 */
	public void setIndex(int _nIndex);
}


/*
 * $Log$
 * Revision 1.1  2004/03/07 16:26:38  tako
 * Texture has been turned into an interface and textures itself are split into
 * TextureBuffers that hold the binary data while Textures determine which
 * part of the buffer to use. All existing code has been adjusted accordingly.
 *
 */