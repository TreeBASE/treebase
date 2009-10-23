/*
 * Copyright 2009 CIPRES project. http://www.phylo.org/ All Rights Reserved.
 * 
 * Permission to use, copy, modify, and distribute this software and its documentation for
 * educational, research and non-profit purposes, without fee, and without a written agreement is
 * hereby granted, provided that the above copyright notice, this paragraph and the following two
 * paragraphs appear in all copies.
 * 
 * Permission to incorporate this software into commercial products may be obtained by contacting
 * us: http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event shall the CIPRES project
 * be liable to any party for direct, indirect, special, incidental, or consequential damages,
 * including lost profits, arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage. The CIPRES project
 * specifically disclaims any warranties, including, but not limited to, the implied warranties of
 * merchantability and fitness for a particular purpose. The CIPRES project has no obligations to
 * provide maintenance, support, updates, enhancements, or modifications.
 */

package org.cipres.treebase.util;

import java.io.IOException;
import java.io.Reader;
import java.util.Stack;

/**
 * Extension of Reader that supports un-reading and peeking ahead without reading.
 *
 * @author mjd 20090218
 *
 */
public class PutbackReader extends Reader implements Readable {

	Reader r;
	Stack<Character> stack = new Stack<Character> ();
	
	/**
	 * Manufacture a new <tt>PutbackReader</tt> whose underlying character source is the reader <var>r</var>.
	 * @param r
	 */
	public PutbackReader(Reader r) {
		this.r = r;
	}
	
	/* (non-Javadoc)
	 * @see java.io.Reader#close()
	 */
	@Override
	public void close() throws IOException {
		r.close();
		stack.removeAllElements();
	}
	
	/* (non-Javadoc)
	 * @see java.io.Reader#read()
	 */
	public int read() throws IOException {
		if (stack.isEmpty()) return r.read();
		else return stack.pop();
	}

	/* (non-Javadoc)
	 * @see java.io.Reader#read(char[], int, int)
	 */
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		int count = 0;   // number of characters read
		while (count < len) {
			int i = read();
			if (i == -1) break;
			cbuf[off++] = (char) read();
			count++;
		}
		return count;
	}
	
	/**
	 * Put a character back onto the input stream.
	 * 
	 * <p>The character so put back will be the first one read by the next call to {@see read}.</p>
	 * 
	 * @param c the character to un-read
	 * @author mjd 20090218
	 */
	public void putback(char c) {
		stack.push(c);
	}
	
	/**
	 * Peek ahead one character in the input.  Return the character that <i>would</i> be read by the next call to {@see read}.
	 * 
	 * <p>The method actually reads a value using {@see read()},  then puts it back with {@see putback} unless it is
	 * an end-of-file indication.</p>
	 * 
	 * <p>Note that the underlying {@see Reader} object <i>is</i> actually read, and so is unavailable to be re-read from
	 * that {@see Reader}.  But it <i>is</i> still available to be read from this object.</p>
	 * 
	 * @return The next value that will be returned by {@see read()}.
	 * @throws IOException
	 * @author mjd 20090218
	 */
	public int peek() throws IOException {
		if (! stack.isEmpty()) return stack.get(0);
		else {
			int i = read();
			if (i != -1) putback((char) i);
			return i;
		}
	}
}
