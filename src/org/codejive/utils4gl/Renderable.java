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
 * Created on Jul 29, 2003
 */
package org.codejive.utils4gl;

/** Interface for objects that can render themselves using OpenGL.
 * @author tako
 * @version $Revision: 164 $
 */
public interface Renderable {
	/** Indicates that the object has been initialized (its initRendering()
	 * method was called) and is ready to render.
	 * @return Boolean indicating the object is ready to render.
	 */	
	public boolean readyForRendering();
	/** Will be called before the first call to render() so the object
	 * can do any initialization it needs to get ready to render.
	 * @param _context A reference to a valid RenderContext object.
	 */	
	public void initRendering(RenderContext _context);
	/** Will be called when something in the context change that might
	 * necesitate the object to redo (part of) its initialization
	 * (a possible example is when the render window gets resized).
	 * @param _context A reference to a valid RenderContext object.
	 */	
	public void updateRendering(RenderContext _context);
	/** Used to make the object render itself in 3D.
	 * @param _context A reference to a valid RenderContext object
	 */	
	public void render(RenderContext _context);
}

/*
 * $Log$
 * Revision 1.6  2003/12/01 22:34:37  tako
 * All code is now subject to the Lesser GPL.
 *
 * Revision 1.5  2003/11/24 16:44:38  tako
 * Added updateRendering(). The only reason for this (for now at least) is
 * to support resizing the rendering canvas. Re-using initRendering() for
 * this would make it impossible to distinguish between the two.
 *
 * Revision 1.4  2003/11/20 16:23:27  tako
 * Updated Java docs.
 *
 * Revision 1.3  2003/11/17 10:49:59  tako
 * Added CVS macros for revision and log.
 *
 */
