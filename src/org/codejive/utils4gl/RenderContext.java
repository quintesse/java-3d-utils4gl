/*
 * [utils4gl] OpenGL utilities library
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
 * Created on Sep 25, 2003
 */
package org.codejive.utils4gl;

import java.io.IOException;

import net.java.games.jogl.GL;
import net.java.games.jogl.GLU;
import net.java.games.jogl.util.GLUT;

/** This object is used to pass references to the OpenGL GL, GLU and
 * GLUT objects to those objects that need them. This saves having
 * to pass all of them around all of the time.
 * The object is also used to set and retrieve any managed textures.
 * @author Tako
 * @version $Revision: 164 $
 */
public class RenderContext {
	private GL m_gl;
	private GLU m_glu;

	private int m_textures[];
		
	private static final GLUT m_glut = new GLUT();
	
	/** Constructs a RenderContext using the given GL and GLU objects.
	 * @param _gl A reference to a valid GL object
	 * @param _glu A reference to a valid GLU object
	 */	
	public RenderContext(GL _gl, GLU _glu) {
		m_gl = _gl;
		m_glu = _glu;

		// Just an arbitrary number for now until I decide how to handle this
		m_textures = new int[10];
		m_gl.glGenTextures(10, m_textures);
	}
	
	/** Returns the reference to the GL object
	 * @return A reference to the internal GL object
	 */	
	public GL getGl() {
		return m_gl;
	}
	
	/** Returns the reference to the GLU object
	 * @return A reference to the internal GLU object
	 */	
	public GLU getGlu() {
		return m_glu;
	}
	
	/** Returns the reference to a GLUT object
	 * @return A reference to the internal GLUT object
	 */	
	public GLUT getGlut() {
		return m_glut;
	}
	
	public void setTexture(int _nTextureIndex, String _sFileName) {
		Texture texture = null;
		try {
			texture = TextureReader.readTexture(_sFileName);
		} catch (IOException e) {
			System.err.println("Could not read texture because " + e.getMessage());
		}
		m_gl.glBindTexture(GL.GL_TEXTURE_2D, m_textures[_nTextureIndex]);
		if (texture != null) {
			makeRGBTexture(texture, GL.GL_TEXTURE_2D, true);
		}
	}

	public int getTextureHandle(int _nTextureId) {
		return m_textures[_nTextureId];
	}
    
	private void makeRGBTexture(Texture img, int target, boolean mipmapped) {
		if (mipmapped) {
			m_glu.gluBuild2DMipmaps(target, GL.GL_RGB8, img.getWidth(), img.getHeight(), GL.GL_RGB, GL.GL_UNSIGNED_BYTE, img.getPixels());
		} else {
			m_gl.glTexImage2D(target, 0, GL.GL_RGB, img.getWidth(), img.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, img.getPixels());
		}
	}
}

/*
 * $Log$
 * Revision 1.5  2003/12/01 22:34:37  tako
 * All code is now subject to the Lesser GPL.
 *
 * Revision 1.4  2003/11/20 16:23:27  tako
 * Updated Java docs.
 *
 * Revision 1.3  2003/11/17 10:49:59  tako
 * Added CVS macros for revision and log.
 *
 */
