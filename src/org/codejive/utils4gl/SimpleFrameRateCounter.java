/*
 * Created on Aug 31, 2003
 */
package org.codejive.world3d;

/**
 * @author tako
 */
public class SimpleFrameRateCounter implements FrameRateCounter {
	private long m_lFrameCount;
	private float m_fFrameRate;

	private long m_lLastUpdateTime;
	private long m_lLastFrameCount;
	
	public SimpleFrameRateCounter() {
		m_lFrameCount = 0;
		m_lLastUpdateTime = System.currentTimeMillis();
		m_lLastFrameCount = 0;
		m_fFrameRate = 0.0f;
	}
	
	public void addFrame() {
		m_lFrameCount++;
		long lCurrentTime = System.currentTimeMillis();
		long lTimeDiff = lCurrentTime - m_lLastUpdateTime;
		if (lTimeDiff >= 500) {
			float fRate = (m_lFrameCount - m_lLastFrameCount) / ((float)lTimeDiff / 1000);
			m_fFrameRate = fRate;
			m_lLastFrameCount = m_lFrameCount;
			m_lLastUpdateTime = lCurrentTime;
		}
	}
	
	public long getFrameCount() {
		return m_lFrameCount;
	}
	
	public float getFrameRate() {
		return m_fFrameRate;
	}
}
