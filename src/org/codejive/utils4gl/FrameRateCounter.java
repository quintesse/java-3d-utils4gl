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
 * Created on Aug 31, 2003
 */
package org.codejive.utils4gl;

/** Interface for objects that implement some kind of frame count and
 * average frame rate determination.
 * @author tako
 * @version $Revision: 164 $
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
 * Revision 1.5  2003/12/01 22:35:03  tako
 * All code is now subject to the Lesser GPL.
 *
 * Revision 1.4  2003/11/20 16:23:27  tako
 * Updated Java docs.
 *
 * Revision 1.3  2003/11/17 10:49:59  tako
 * Added CVS macros for revision and log.
 *
 */
