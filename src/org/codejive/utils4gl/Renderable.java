/*
 * Created on Jul 29, 2003
 */
package org.codejive.utils4gl;

/** Interface for objects that can render themselves using OpenGL.
 * @author tako
 * @version $Revision: 101 $
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
	/** Used to make the object render itself in 3D.
	 * @param _context A reference to a valid RenderContext object
	 */	
	public void render(RenderContext _context);
}

/*
 * $Log$
 * Revision 1.4  2003/11/20 16:23:27  tako
 * Updated Java docs.
 *
 * Revision 1.3  2003/11/17 10:49:59  tako
 * Added CVS macros for revision and log.
 *
 */
