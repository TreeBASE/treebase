


package org.cipres.treebase.auxdata;

import java.util.Iterator;

/**
 * Lazy list class for use by parsers
 * 
 * <p>The intent of this class is to represent an input source as a lazily-populated
 * list of tokens.  The basic representation is as a linked list, with one token
 * per node.  The list also has a {@see Generator} member which is used to extend the
 * list if necessary.</p>
 * 
 * <p>An object of this class represents an entire list, but implements only a single
 * node from the list.  The remainder of the list is represented by the {@see #tail} member.</p>
 * 
 * 
 * @author mjd 20071203
 *
 * @param <E> the type of values contained in the list
 */
public class LazyList<E> implements Iterable<E> {
	/**
	 * The value in the head of the list.
	 */
	E head;
	/**
	 * The remainder of the list.  
	 * 
	 * <p>This might be null under two different circumstances:
	 * <ol>
	 * <li>This is the last node in the list
	 * <li>The rest of the list is unknown
	 * </ol>
	 * 
	 * In the former case, {@see #gen} will also be null.
	 * In the latter case, {@see #gen} can be invoked to return
	 * the rest of the list (which, if this <i>is</i> the last node, will be null.)
	 * </p>
	 */
	LazyList<E> tail;
	/**
	 * A source of values which can be consulted to recover the rest of the list
	 * 
	 * <p>If null, then the rest of the list can be found in {@see #tail}.
	 */
	Generator<E> gen; 
	
	
	/**
	 * Build a list node with specified head and tail
	 * 
	 * @param h the head value
	 * @param t the rest of the list
	 */
	public LazyList(E h, LazyList<E> t) {
		head = h;
		tail = t;
	}
	
	
	/**
	 * Build a list node with specified head value and generator
	 * 
	 * <p>The tail of the list will be generated if it is ever examined.</p>
	 * @param h the head value
	 * @param g the generator {@see Generator}
	 */
	public LazyList(E h, Generator<E> g) {
		head = h;
		gen = g;
		tail = null;
	}
	
	/* This isn't quite right, because g might fail to produce
	 * even one token, and then we ought to return an empty list.
	 * But I'm using null to represent an empty list, and 
	 * constructors may not return null in Java.
	 * TODO: fix this.
	 */
	
	/**
	 * Produce the list of values generated by the {@see Generator} <var>g</var>
	 * 
	 * <p>This function has a bug in dealing with a generator that might generate an empty seqeunce.
	 * This class represents an empty list with a simple null value.  If the generator
	 * returns null on its first invocation, this indicates that the generator represents an empty list, 
	 * so the constructor should return null.  However, it returns a list of length 1 
	 * whose first ellinkement is null.</p>
	 * 
	 * @param g
	 */
	public LazyList(Generator<E> g) {
		head = g.another();
		gen = g;
		tail = null;
	}
			
	
	/**
	 * Append a new element to the font of this list
	 * <p>Manufactures a new list whose head value is <var>newhead</var>
	 * and whose tail is this list, and returns the new list.
	 * The new and old lists share structure.</p>
	 * 
	 * @param newhead
	 * @return the new list
	 */
	public LazyList<E> cons(E newhead) {
		LazyList<E> newlist = new LazyList<E>(newhead, this);
		return newlist;
	}
	
	/**
	 *  Convert an array of values to a lazy list 
	 *  
	 *  <p>Constructs a new, finite list whose elements are taken from the
	 *  specified array of values.  The new list is not constructed lazily.</p>
	 * @param ar an array of values
	 */
	public LazyList(E ar[]) {
		int i = ar.length - 1;
		LazyList<E> cur = null;
		while (i >= 0) {
			cur = new LazyList<E>(ar[i], cur);
			i -= 1;
		}
		head = cur.head;
		tail = cur.tail;
	}
	
	/**
	 * Get the value at the head of the list (<tt>car</tt>)
	 * 
	 * @return the head value
	 */
	public E head() {
		return head;
	}
	/**
	 * Modify the value at the head of the list (<tt>rplaca</tt>)
	 * 
	 * @param newhead the new value
	 */
	public void setHead(E newhead) {
		head = newhead;
	}
	
	/**
	 * Return the tail of a lazy list
	 * 
	 * <p>Returns the tail of the list; that is, a list containing every element except the 
	 * head element.</p>
	 * <p>If the list has length 1, then this method returns null.  If the 
	 * rest of the list has been promised but not yet computed, this method forces its
	 * computation and caches the result.<p>
	 * 
	 * @return the tail
	 * @author mjd
	 */
	public LazyList<E> tail() {
		if (tail == null && gen != null) {
			E newhead = gen.another();
			if (newhead != null) { 
				tail = new LazyList<E>(newhead, this.gen);	
			}
		}
		return tail;
	}
	
	/**
	 * Modify the tail of the list (<tt>rplacd</tt>)
	 * 
	 * <p>Replaces the tail of the list with the argument list</p>
	 * 
	 * @param newtail the new tail for the list
	 * @author mjd
	 */
	public void setTail(LazyList<E> newtail) {
		tail = newtail;
	}
	
	/**
	 * Return an {@see Iterator} that iterates the elements of the list
	 * 
	 * @author mjd
	 * @return The {@see Iterator} object
	 */
	public LazyListIterator<E> iterator() {
		return new LazyListIterator<E>(this);
	}
	
	
	/**
	 * Return the last node in the list
	 * 
	 * <p>Forces computation of the entire list, caching it in memory, and returns the last node,
	 * which is a list of length 1.</p>
	 * 
	 * <p>If invoked on an infinite list, this function will never return.</p>
	 * 
	 * @return the last node in the list
	 * @author mjd
	 */
	public LazyList<E> lastNode() {
		LazyList<E> p = this;
		while (p.tail != null) {
			p = p.tail;
		}
		return p;
	}
	
	/**
	 * Destructively concatenate two lists
	 * 
	 * <p>Destructively concatenate list <var>b</var> to the end of this one.  The argument
	 * list <var>b</var> is unmodified.  Note that this function forces the computation of 
	 * this entire list, and  so if this is an infinite list, will never return.</p>
	 * 
	 * @param b the list to append to this one
	 * @author mjd
	 */
	public void nconc(LazyList<E> b) {
		this.lastNode().setTail(b);
	}
	

	private class LazyListIterator<T> implements Iterator {
		LazyList<T> theList;
		
		public LazyListIterator(LazyList<T> pList) {
			theList = pList;
		}
		
		public boolean hasNext() {
			return theList != null;
		}
		public T next() {
			T nextval = theList.head();
			theList = theList.tail();
			return nextval;
		}
		public void remove() {
			throw new java.lang.UnsupportedOperationException();
		}
	}
}
