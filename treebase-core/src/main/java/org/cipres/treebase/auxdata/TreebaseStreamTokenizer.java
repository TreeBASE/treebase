


package org.cipres.treebase.auxdata;

import java.io.IOException;
import java.io.Reader;

import org.cipres.treebase.util.PutbackReader;

class TreebaseStreamTokenizer {

	private enum CharType { WORD, NUM, QUOTE, SPACE, EOL, EOF, BACKSLASH, OTHER };
	
	PutbackReader in;


	public TreebaseStreamTokenizer(Reader r) {
		in = new PutbackReader(r);
	}
	
	private void skipSpaces() throws IOException {
		while (nextCharType() == CharType.SPACE) 
			discard();
	}
	
	private String readQuotedString() throws IOException {
		StringBuilder s = new StringBuilder("");
		while (nextCharType() != CharType.QUOTE) {
			if (nextCharType() == CharType.BACKSLASH)
				discard(); // Discard backslash, append following character instead
			int i = in.read();
			if (i == -1) throw new IOException("End of file inside quoted string"
					+ (s.equals("") ? "" : " '" + s.toString() + "'"));
			s.append((char) i);
		}
		discard(); // Trailing quote
		 
		return s.toString();
	}
	
	private String readWord() throws IOException {
		StringBuilder s = new StringBuilder("");
		while (nextCharType() == CharType.WORD || nextCharType() == CharType.NUM) 
			s.append((char) in.read());
		return s.toString();
	}
	
	// Actually only reads unsigned integers
	private int readNumber() throws IOException {
		StringBuilder s = new StringBuilder("");
		while (nextCharType() == CharType.NUM) 
			s.append((char) in.read());
		return Integer.decode(s.toString());
	}
	
	public Token nextTokenObject() throws java.io.IOException {
		skipSpaces();

		Token rv;
		switch (charType(in.peek())) {
		case NUM:   rv = new Token((double) readNumber()); break;
		case WORD:  rv = new Token(readWord()); break;
		case EOF:   return null;
		case QUOTE: discard(); rv = new Token(readQuotedString()); break;
		case SPACE: throw new Error("Spaces not all skipped?");
		case EOL: // Handle with default
		default:  rv = new Token(in.read());
		}
		return rv;
	}
	
	private CharType nextCharType() throws IOException {
		return charType(in.peek());
	}

	// Discard the upcoming character
	private void discard() throws IOException {
		in.read();
	}
	
	private CharType charType(char c) {
		if (c == '\'') return CharType.QUOTE;
		else if (c == '\\') return CharType.BACKSLASH;
		else if (c == '\n') return CharType.EOL;
		else if (c == '_') return CharType.WORD;
		else if (Character.isWhitespace(c)) return CharType.SPACE;
		else if (Character.isDigit(c)) return CharType.NUM;
		else if (Character.isLetter(c)) return CharType.WORD;
		else return CharType.OTHER;		
	}
	
	private CharType charType(int i) {
		if (i == -1) return CharType.EOF;
		else return charType((char) i);
	}
}
