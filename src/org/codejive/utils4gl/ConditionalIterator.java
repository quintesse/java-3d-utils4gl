/*
 * Created on Nov 10, 2003
 */
package org.codejive.utils4gl;

import java.util.Iterator;

/**
 * @author tako
 * @version $Revision: 48 $
 */
public abstract class ConditionalIterator implements Iterator {
	private Iterator m_iter;
	private Object m_currItem;
	
	public ConditionalIterator(Iterator _iter) {
		m_iter = _iter;
		m_currItem = null;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		m_iter.remove();
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		findNext();
		return (m_currItem != null);
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	public Object next() {
		findNext();
		Object element = m_currItem;
		m_currItem = null;
		return element;
	}

	private void findNext() {
		boolean bFound = (m_currItem != null);
		while (!bFound && m_iter.hasNext()) {
			Object element = m_iter.next();
			if (includeElement(element)) {
				m_currItem = element;
			}
		}
	}

	protected abstract boolean includeElement(Object _element);
}

/*
 * $Log$
 * Revision 1.2  2003/11/17 10:49:59  tako
 * Added CVS macros for revision and log.
 *
 */
