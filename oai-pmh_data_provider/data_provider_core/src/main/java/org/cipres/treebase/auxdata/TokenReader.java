/*
 * Copyright 2008 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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



package org.cipres.treebase.auxdata;

import java.io.Reader;
import java.util.LinkedList;
import java.util.Stack;

public class TokenReader implements Generator<Token> {
   private Stack<Token> tokenStack = new Stack<Token> ();
    private TreebaseStreamTokenizer tokenStream; 
    
    public TokenReader(Reader source) {
    	tokenStream = new TreebaseStreamTokenizer(source);
    }
    
    public TokenReader(TreebaseStreamTokenizer tst) {
    	tokenStream = tst;
    }
    
    public Token another() { 
    	try { return nextToken(); }
    	catch (java.io.IOException e) {
    		return null;
    	}
    } 

    public Token nextToken() throws java.io.IOException { 
    	if (tokenStack.empty()) {
    		return tokenStream.nextTokenObject();
    	} else {
    		return tokenStack.pop();
    	}
    }
    public Token TryToken(int ttype) throws java.io.IOException {
    	Token t = nextToken();
    	if (t.type == ttype) {
    		return t;
    	} else {
    		unreadToken(t);
    		return null;
    	}
    }
    
    public Token[] TryTokenSequence(int ttype[]) throws java.io.IOException {
    	LinkedList<Token> q = new LinkedList<Token>();
    	LinkedList<Token> r = new LinkedList<Token>();
    	Token _d[] = null;  // dummy variable for .toArray call
    	
    	for (int t : ttype) {
    	   Token tok = TryToken(t);
    	   if (tok == null) {  // put them all back
    		   while (! q.isEmpty()) {
    			   unreadToken(q.removeLast());
    		   }
    		   return null;
    	   } else { 
    		   r.add(tok);
    	   }
    	}
    	return r.toArray(_d);    // Is this really the right way to do this?
    }
    
    public void unreadToken(Token t) {
    	tokenStack.push(t);
    }
}
