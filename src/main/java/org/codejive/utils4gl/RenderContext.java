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

import java.awt.Rectangle;
import java.util.Stack;

import org.codejive.utils4gl.textures.*;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import com.jogamp.opengl.util.gl2.GLUT;


/** This object is used to pass references to the OpenGL GL, GLU and
 * GLUT objects to those objects that need them. This saves having
 * to pass all of them around all of the time.
 * The object is also used to set and retrieve any managed textures.
 * @author Tako
 * @version $Revision: 357 $
 */
public class RenderContext {
	private GL2 m_gl;

	private Texture m_textures[];
	private Stack<Rectangle> m_clippingRegions;
		
	private static final GLU m_glu = new GLUgl2();
	private static final GLUT m_glut = new GLUT();
	
	/** Constructs a RenderContext using the given GL and GLU objects.
	 * @param _gl A reference to a valid GL object
	 * @param _glu A reference to a valid GLU object
	 */	
	public RenderContext(GL2 _gl) {
		m_gl = _gl;

		// Just an arbitrary number for now until I decide how to handle this
		m_textures = new Texture[10];
		
		m_clippingRegions = new Stack<Rectangle>();
	}
	
	/** Returns the reference to the GL object
	 * @return A reference to the internal GL object
	 */	
	public GL2 getGl() {
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
	
	/**
	 * Adds a texture to the context's texture manager.
	 * @param _nTextureId The Id chosen for the texture
	 * @param _texture The texture to register with the texture manager
	 */
	public void addTexture(int _nTextureId, Texture _texture) {
		m_textures[_nTextureId] = _texture;
	}

	/**
	 * Retrieves a texture from the texture manager
	 * @param _nTextureId The Id of the texture to retrieve
	 * @return The requested texture
	 */
	public Texture getTexture(int _nTextureId) {
		return m_textures[_nTextureId];
	}
	
	/**
	 * Sets the current clipping region using glScissor() while
	 * maintaining a stact of those regions so that calling
	 * popClippingRegion() restores the previous setting.
	 * @param _clipRect The new region to clip against
	 */
	public void pushClippingRegion(Rectangle _clipRect) {
		Rectangle newRect = new Rectangle(_clipRect);
		m_clippingRegions.push(newRect);
		setClippingRegion(newRect);
	}
	
	/**
	 * Pops the current clipping region from the stack and restores
	 * the previous region. Clipping will be turned of completely
	 * if no more regions remain in the stack.
	 * @return The region that was current and has been popped or
	 * null if no clipping region was currently active.
	 */
	public Rectangle popClippingRegion() {
		Rectangle rect = null;
		if (!m_clippingRegions.isEmpty()) {
			rect = m_clippingRegions.pop();
			Rectangle newRect = null;
			if (!m_clippingRegions.isEmpty()) {
				newRect = m_clippingRegions.peek();
			}
			setClippingRegion(newRect);
		}
		return rect;
	}
	
	/**
	 * Returns the currently active clipping region or null if no
	 * region was active.
	 * @return The current clipping region or null if none was active
	 */
	public Rectangle getClippingRegion() {
		Rectangle rect = null;
		if (!m_clippingRegions.isEmpty()) {
			rect = m_clippingRegions.peek();
		}
		return rect;
	}

	private void setClippingRegion(Rectangle _clipRect) {
		if (_clipRect != null) {
			m_gl.glScissor(_clipRect.x, _clipRect.y, _clipRect.width, _clipRect.height);
			m_gl.glEnable(GL3.GL_SCISSOR_TEST);
		} else {
			m_gl.glDisable(GL3.GL_SCISSOR_TEST);
		}
	}
}

/*
 * $Log$
 * Revision 1.8  2004/05/04 21:52:42  tako
 * Added support for clipping and maintaining a stack of active clipping regions.
 *
 * Revision 1.7  2004/03/07 16:05:55  tako
 * Fixed faulty CVS checkin comments and added javadoc.
 *
 * Revision 1.6  2004/03/07 15:54:23  tako
 * Changed existing texture handling methods to just do very simple
 * texture bookkeeping.
 *
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
