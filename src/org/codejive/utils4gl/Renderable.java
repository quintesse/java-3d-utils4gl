/*
 * Created on Jul 29, 2003
 */
package org.codejive.utils4gl;

/**
 * @author tako
 * @version $Revision: 48 $
 */
public interface Renderable {
	public boolean readyForRendering();
	public void initRendering(RenderContext _context);
	public void render(RenderContext _context);
}

/*
 * $Log$
 * Revision 1.3  2003/11/17 10:49:59  tako
 * Added CVS macros for revision and log.
 *
 */
