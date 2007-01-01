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
 * @version $Revision: 357 $
 */
public abstract class ConditionalIterator<E> implements Iterator<E> {
	private Iterator<E> m_iter;
	private E m_currItem;
	
	/** Constructor that takes the iterator that is going to be filtered
	 * @param _iter The iterator to be filtered
	 */	
	public ConditionalIterator(Iterator<E> _iter) {
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
	public E next() {
		findNext();
		E element = m_currItem;
		m_currItem = null;
		return element;
	}

	private void findNext() {
		boolean bFound = (m_currItem != null);
		while (!bFound && m_iter.hasNext()) {
			E element = m_iter.next();
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
	protected abstract boolean includeElement(E _element);
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
