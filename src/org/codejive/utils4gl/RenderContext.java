/*
 * Created on Sep 25, 2003
 */
package org.codejive.utils4gl;

import java.io.IOException;

import net.java.games.jogl.GL;
import net.java.games.jogl.GLU;
import net.java.games.jogl.util.GLUT;

/**
 * @author Tako
 */
public class RenderContext {
	private GL m_gl;
	private GLU m_glu;

	private int m_textures[];
		
	private static final GLUT m_glut = new GLUT();
	
	public RenderContext(GL _gl, GLU _glu) {
		m_gl = _gl;
		m_glu = _glu;

		// Just an arbitrary number for now until I decide how to handle this
		m_textures = new int[10];
		m_gl.glGenTextures(10, m_textures);
	}
	
	public GL getGl() {
		return m_gl;
	}
	
	public GLU getGlu() {
		return m_glu;
	}
	
	public GLUT getGlut() {
		return m_glut;
	}
	
	public void setTexture(int _nTextureIndex, String _sFileName) {
		Texture texture = null;
		try {
			texture = TextureReader.readTexture(_sFileName);
		} catch (IOException e) {
			System.err.println("Could not read texture because " + e.getMessage());
		}
		m_gl.glBindTexture(GL.GL_TEXTURE_2D, m_textures[_nTextureIndex]);
		if (texture != null) {
			makeRGBTexture(texture, GL.GL_TEXTURE_2D, true);
		}
	}

	public int getTextureHandle(int _nTextureId) {
		return m_textures[_nTextureId];
	}
    
	private void makeRGBTexture(Texture img, int target, boolean mipmapped) {
		if (mipmapped) {
			m_glu.gluBuild2DMipmaps(target, GL.GL_RGB8, img.getWidth(), img.getHeight(), GL.GL_RGB, GL.GL_UNSIGNED_BYTE, img.getPixels());
		} else {
			m_gl.glTexImage2D(target, 0, GL.GL_RGB, img.getWidth(), img.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, img.getPixels());
		}
	}
}
