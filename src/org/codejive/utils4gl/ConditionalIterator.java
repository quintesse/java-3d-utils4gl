/*
 * Created on Nov 10, 2003
 */
package org.codejive.utils4gl;

import java.util.Iterator;

/** An abstract iterator which can be used to create iterators that
 * function as a filtering wrapper for another iterator. The
 * filtering iterator will only return the allowed objects from
 * the filtered iterator. Only the method includeElement(Object)
 * needs to be implemented to make this possible.
 * @author tako
 * @version $Revision: 101 $
 */
public abstract class ConditionalIterator implements Iterator {
	private Iterator m_iter;
	private Object m_currItem;
	
	/** Constructor that takes the iterator that is going to be filtered
	 * @param _iter The iterator to be filtered
	 */	
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

	/** Determines if the given object should be included in the output
	 * of this iterator or not.
	 * @param _element The object to include or not
	 * @return Boolean indicating if the given object should be
	 * included or not
	 */	
	protected abstract boolean includeElement(Object _element);
}

/*
 * $Log$
 * Revision 1.3  2003/11/20 16:23:27  tako
 * Updated Java docs.
 *
 * Revision 1.2  2003/11/17 10:49:59  tako
 * Added CVS macros for revision and log.
 *
 */
