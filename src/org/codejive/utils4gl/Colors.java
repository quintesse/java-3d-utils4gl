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
 * Created on 4-apr-2003
 */
package org.codejive.utils4gl;

/** Simple class containing color constants.
 * @author Tako
 * @version $Revision: 164 $
 */
public class Colors {

	/** The color black
	 */	
	public static final GLColor BLACK = new GLColor(0.0f, 0.0f, 0.0f); 
	/** The color red
	 */	
	public static final GLColor RED = new GLColor(1.0f, 0.0f, 0.0f); 
	/** The color green
	 */	
	public static final GLColor GREEN = new GLColor(0.0f, 1.0f, 0.0f); 
	/** The color blue
	 */	
	public static final GLColor BLUE = new GLColor(0.0f, 0.0f, 1.0f); 
	/** The color white
	 */	
	public static final GLColor WHITE = new GLColor(1.0f, 1.0f, 1.0f); 
}

/*
 * $Log$
 * Revision 1.4  2003/12/01 22:35:03  tako
 * All code is now subject to the Lesser GPL.
 *
 * Revision 1.3  2003/11/20 16:23:27  tako
 * Updated Java docs.
 *
 * Revision 1.2  2003/11/17 10:49:59  tako
 * Added CVS macros for revision and log.
 *
 */
