/*
 * Created on Aug 31, 2003
 */
package org.codejive.world3d;

/**
 * @author tako
 */
public interface FrameRateCounter {
	public void addFrame();
	public long getFrameCount();
	public float getFrameRate();
}
