/*
 * Created on Sep 11, 2003
 */
package org.codejive.utils4gl;

import java.awt.Color;

/**
 * @author Tako
 */
public class GLColor {
	private float m_fRed, m_fGreen, m_fBlue;
	private float m_fAlpha;
	
	float m_colorComponents[] = new float[4];

	public GLColor() {
		set(1.0f, 1.0f, 1.0f, 1.0f);
	}

	public GLColor(float _fRed, float _fGreen, float _fBlue) {
		set(_fRed, _fGreen, _fBlue);
	}

	public GLColor(float _fRed, float _fGreen, float _fBlue, float _fAlpha) {
		set(_fRed, _fGreen, _fBlue, _fAlpha);
	}

	public GLColor(GLColor _color) {
		set(_color);
	}

	public GLColor(Color _color) {
		set(_color);
	}

	public GLColor(Color _color, float _fAlpha) {
		set(_color, _fAlpha);
	}

	public void set(float _fRed, float _fGreen, float _fBlue, float _fAlpha) {
		m_fRed = _fRed;
		m_fGreen = _fGreen;
		m_fBlue = _fBlue;
		m_fAlpha = _fAlpha;
	}

	public void set(float _fRed, float _fGreen, float _fBlue) {
		set(_fRed, _fGreen, _fBlue, 1.0f);
	}

	public void set(GLColor _color) {
		set(_color.getRed(), _color.getGreen(), _color.getBlue(), _color.getAlpha());
	}

	public void set(Color _color) {
		set((float)_color.getRed() / 255, (float)_color.getGreen() / 255, (float)_color.getBlue() / 255, _color.getAlpha() / 255);
	}
	
	public void set(Color _color, float _fAlpha) {
		set((float)_color.getRed() / 255, (float)_color.getGreen() / 255, (float)_color.getBlue() / 255, _fAlpha);
	}
	
	public float getRed() {
		return m_fRed;
	}
	
	public void setRed(float _fRed) {
		m_fRed = _fRed;
	}
	
	public float getGreen() {
		return m_fGreen;
	}
	
	public void setGreen(float _fGreen) {
		m_fGreen = _fGreen;
	}
	
	public float getBlue() {
		return m_fBlue;
	}
	
	public void setBlue(float _fBlue) {
		m_fBlue = _fBlue;
	}
	
	public float getAlpha() {
		return m_fAlpha;
	}
	
	public void setAlpha(float _fAlpha) {
		m_fAlpha = _fAlpha;
	}
	
	public float[] toArray3f() {
		return toArray3f(m_colorComponents);
	}

	public float[] toArray3f(float colorComponents[]) {
		colorComponents[0] = m_fRed;
		colorComponents[1] = m_fGreen;
		colorComponents[2] = m_fBlue;
		return colorComponents;
	}

	public float[] toArray4f() {
		return toArray4f(m_colorComponents);
	}

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
