/*
 * Created on Sep 25, 2003
 */
package org.codejive.utils4gl;

import java.io.IOException;

import org.codejive.utils4gl.*;

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

		m_textures = new int[1];
		_gl.glGenTextures(1, m_textures);
		Texture texture = null;
		try {
			texture = TextureReader.readTexture("games/batoru/textures/grass_03.jpg");
		} catch (IOException e) {
			System.err.println("Could not read texture because " + e.getMessage());
		}
		_gl.glBindTexture(GL.GL_TEXTURE_2D, m_textures[0]);
		if (texture != null) {
			makeRGBTexture(texture, GL.GL_TEXTURE_2D, true);
		}
//		_gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_NEAREST);
//		_gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_NEAREST);
//		_gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
//		_gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		_gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_NEAREST);
		_gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
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
	
	public int getTextureHandle(int _nTextureId) {
		return m_textures[_nTextureId];
	}
    
	private void makeRGBTexture(Texture img, int target, boolean mipmapped)
	{
	  if (mipmapped)
	  {
		m_glu.gluBuild2DMipmaps(target, GL.GL_RGB8, img.getWidth(), img.getHeight(), GL.GL_RGB, GL.GL_UNSIGNED_BYTE, img.getPixels());
	  }
	  else
	  {
		m_gl.glTexImage2D(target, 0, GL.GL_RGB, img.getWidth(), img.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, img.getPixels());
	  }
	}
}
