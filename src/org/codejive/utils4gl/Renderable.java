/*
 * Created on Jul 29, 2003
 */
package org.codejive.utils4gl;

/**
 * @author tako
 */
public interface Renderable {
	public boolean readyForRendering();
	public void initRendering(RenderContext _context);
	public void render(RenderContext _context);
}
