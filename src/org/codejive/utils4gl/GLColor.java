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
 * Created on Sep 11, 2003
 */
package org.codejive.utils4gl;

import java.awt.Color;

/** A simple color class that contains some useful methods to make
 * it easy to use them with OpenGL.
 * @author Tako
 * @version $Revision: 164 $
 */
public class GLColor {
	private float m_fRed, m_fGreen, m_fBlue;
	private float m_fAlpha;
	
	float m_colorComponents[] = new float[4];

	/** Constructs an GLColor object with the color white and alpha
	 * set to 1.0f.
	 */	
	public GLColor() {
		set(1.0f, 1.0f, 1.0f, 1.0f);
	}

	/** Constructs a GLColor object with the given R, G and B values and
	 * alpha set to 1.0f.
	 * @param _fRed Red part of the color (0.0f - 1.0f)
	 * @param _fGreen Green part of the color (0.0f - 1.0f)
	 * @param _fBlue Blue part of the color (0.0f - 1.0f)
	 */	
	public GLColor(float _fRed, float _fGreen, float _fBlue) {
		set(_fRed, _fGreen, _fBlue);
	}

	/** Constructs a GLColor object with the given R, G, B and alpha values.
	 * @param _fRed Red part of the color (0.0f - 1.0f)
	 * @param _fGreen Green part of the color (0.0f - 1.0f)
	 * @param _fBlue Blue part of the color (0.0f - 1.0f)
	 * @param _fAlpha Alpha part of the color
	 */	
	public GLColor(float _fRed, float _fGreen, float _fBlue, float _fAlpha) {
		set(_fRed, _fGreen, _fBlue, _fAlpha);
	}

	/** Constructs a GLColor using the same color information as
	 * available in the given GLColor object.
	 * @param _color A GLColor object
	 */	
	public GLColor(GLColor _color) {
		set(_color);
	}

	/** Constructs a GLColor using the same color information as
	 * available in the given AWT Color object.
	 * @param _color An AWT Color object
	 */	
	public GLColor(Color _color) {
		set(_color);
	}

	/** Constructs a GLColor using the same color information as
	 * available in the given AWT Color object, but using the given
	 * alpha value.
	 * @param _color An AWT Color object
	 * @param _fAlpha The alpha value to use instead of the one defined by
	 * the given Color object
	 */	
	public GLColor(Color _color, float _fAlpha) {
		set(_color, _fAlpha);
	}

	/** Updates a GLColor object with the given R, G, B and alpha values.
	 * @param _fRed Red part of the color (0.0f - 1.0f)
	 * @param _fGreen Green part of the color (0.0f - 1.0f)
	 * @param _fBlue Blue part of the color (0.0f - 1.0f)
	 * @param _fAlpha Alpha part of the color
	 */	
	public void set(float _fRed, float _fGreen, float _fBlue, float _fAlpha) {
		m_fRed = _fRed;
		m_fGreen = _fGreen;
		m_fBlue = _fBlue;
		m_fAlpha = _fAlpha;
	}

	/** Updates a GLColor object with the given R, G and B values and
	 * alpha set to 1.0f.
	 * @param _fRed Red part of the color (0.0f - 1.0f)
	 * @param _fGreen Green part of the color (0.0f - 1.0f)
	 * @param _fBlue Blue part of the color (0.0f - 1.0f)
	 */	
	public void set(float _fRed, float _fGreen, float _fBlue) {
		set(_fRed, _fGreen, _fBlue, 1.0f);
	}

	/** Updates a GLColor using the same color information as
	 * available in the given GLColor object.
	 * @param _color A GLColor object
	 */	
	public void set(GLColor _color) {
		set(_color.getRed(), _color.getGreen(), _color.getBlue(), _color.getAlpha());
	}

	/** Updates a GLColor using the same color information as
	 * available in the given AWT Color object.
	 * @param _color An AWT Color object
	 */	
	public void set(Color _color) {
		set((float)_color.getRed() / 255, (float)_color.getGreen() / 255, (float)_color.getBlue() / 255, _color.getAlpha() / 255);
	}
	
	/** Updates a GLColor using the same color information as
	 * available in the given AWT Color object, but using the given
	 * alpha value.
	 * @param _color An AWT Color object
	 * @param _fAlpha The alpha value to use instead of the one defined by
	 */	
	public void set(Color _color, float _fAlpha) {
		set((float)_color.getRed() / 255, (float)_color.getGreen() / 255, (float)_color.getBlue() / 255, _fAlpha);
	}
	
	/** Returns the red component of the color
	 * @return Red component (0.0f - 1.0f)
	 */	
	public float getRed() {
		return m_fRed;
	}
	
	/** Sets the red component of the color
	 * @param _fRed The red component (0.0f - 1.0f)
	 */	
	public void setRed(float _fRed) {
		m_fRed = _fRed;
	}
	
	/** Returns the green component of the color
	 * @return Green component (0.0f - 1.0f)
	 */	
	public float getGreen() {
		return m_fGreen;
	}
	
	/** Sets the green component of the color
	 * @param _fGreen The green component (0.0f - 1.0f)
	 */	
	public void setGreen(float _fGreen) {
		m_fGreen = _fGreen;
	}
	
	/** Returns the blue component of the color
	 * @return Blue component (0.0f - 1.0f)
	 */	
	public float getBlue() {
		return m_fBlue;
	}
	
	/** Sets the blue component of the color
	 * @param _fBlue The blue component (0.0f - 1.0f)
	 */	
	public void setBlue(float _fBlue) {
		m_fBlue = _fBlue;
	}
	
	/** Returns the alpha component of the color
	 * @return Alpha component (0.0f - 1.0f)
	 */	
	public float getAlpha() {
		return m_fAlpha;
	}
	
	/** Sets the alpha component of the color
	 * @param _fAlpha The alpha component (0.0f - 1.0f)
	 */	
	public void setAlpha(float _fAlpha) {
		m_fAlpha = _fAlpha;
	}
	
	/** Returns a float array containing the R, G and B values of the color
	 * @return A float array with RGB values
	 */	
	public float[] toArray3f() {
		return toArray3f(m_colorComponents);
	}

	/** Updates a float array with the R, G and B values of the color
	 * @param colorComponents Array of floats with at least 3 elements
	 * @return A float array with RGB values
	 */	
	public float[] toArray3f(float colorComponents[]) {
		colorComponents[0] = m_fRed;
		colorComponents[1] = m_fGreen;
		colorComponents[2] = m_fBlue;
		return colorComponents;
	}

	/** Returns a float array containing the RGB and A values of the color
	 * @return A float array with RGBA values
	 */	
	public float[] toArray4f() {
		return toArray4f(m_colorComponents);
	}

	/** Updates a float array with the RGB and A values of the color
	 * @param colorComponents Array of floats with at least 4 elements
	 * @return A float array with RGB values
	 */	
	public float[] toArray4f(float colorComponents[]) {
		colorComponents[0] = m_fRed;
		colorComponents[1] = m_fGreen;
		colorComponents[2] = m_fBlue;
		colorComponents[3] = m_fAlpha;
		return colorComponents;
	}
	
	public String toString() {
		return "(R:" + m_fRed + ",G:" + m_fGreen + ",B:" + m_fBlue + ",alpha:" + m_fAlpha + ")";
	}
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
