/*
 * Created on Aug 31, 2003
 */
package org.codejive.utils4gl;

/**
 * @author tako
 */
public interface FrameRateCounter {
	public void addFrame();
	public long getFrameCount();
	public float getFrameRate();
}
