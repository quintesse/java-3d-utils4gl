/*
 * Created on Aug 31, 2003
 */
package org.codejive.utils4gl;

/**
 * @author tako
 * @version $Revision: 48 $
 */
public interface FrameRateCounter {
	public void addFrame();
	public long getFrameCount();
	public float getFrameRate();
}

/*
 * $Log$
 * Revision 1.3  2003/11/17 10:49:59  tako
 * Added CVS macros for revision and log.
 *
 */
