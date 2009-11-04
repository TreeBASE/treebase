


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
