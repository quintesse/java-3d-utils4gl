/*
 * Created on Aug 31, 2003
 */
package org.codejive.utils4gl;

/** Interface for objects that implement some kind of frame count and
 * average frame rate determination.
 * @author tako
 * @version $Revision: 101 $
 */
public interface FrameRateCounter {
	/** This method should be called each time a frame has been rendered.
	 */	
	public void addFrame();
	/** Returns the number of frames that have been counted since the
	 * object was created.
	 * @return The number of frames counted so far
	 */	
	public long getFrameCount();
	 /** Returns the average frame rate. Average should be taken loosly
	  * here because the exact mathematical function used is undetermined
	  * and depends on the implementing class.
	  * @return A float indicating some kind of frame rate.
	  */	 
	public float getFrameRate();
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
