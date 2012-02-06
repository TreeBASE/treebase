
package org.cipres.treebase;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;


/**
 * RangeExpression
 * 
 * <p>Parse and analyze numeric range expressions.  Transform them into 
 * Hibernate <tt>Criteria</tt> objects.</p>
 * 
 * <p>Legal syntax is any sequence of the following, separated by commas or semicolons:
 * <ul>
 * <li> "10..50"
 * <li> "10.."
 * <li> "..50"
 * <li> "37"
 * <li> (empty string) 
 * </ul>
 * And whitespace is ignored.</p>
 * 
 * <p>The current parser allows any number of dots in place of "..".  This 
 * should be construed as a bug.</p>
 * 
 * <p><b>TO DO:</b> "&lt; 50", "&lt;= 50", "&gt; 50", "&gt;= 50"
 * 
 * @author mjd 20081203
 *
 */
public class RangeExpression {
	String text;
	Criterion criterion = null;

	public class MalformedRangeExpression extends Exception {
		private static final long serialVersionUID = 1L;

		public MalformedRangeExpression(String string) {
			super(string);
		}

	}

	enum tokenType {
		Num, Range, End
	}

	public RangeExpression() {
		super();
	}	

	public RangeExpression(String pText) {
		this();
		text = pText;
	}

	/**
	 * <p>Construct a Hibernate <tt>Criterion</tt> object that will enforce
	 * that the specified property must lie in a certain range.</p>
	 * 
	 * @param propertyName
	 * @return the <tt>Criterion</tt> object
	 * @throws MalformedRangeExpression
	 */
	public Criterion getCriteria(String propertyName) throws MalformedRangeExpression {
		buildCriteria(propertyName);
		return criterion;
	}

	public void buildCriteria(String propertyName) throws MalformedRangeExpression {
		String[] clauses = getText().trim().split("[ \t]*[;,][ \t]*");
		Disjunction result = Restrictions.disjunction();
		for (String clause : clauses) {
			Criterion c = makeRestriction(propertyName, new Bounds(clause)); 
			if (c != null) result.add(c);
		}
		criterion = result;
	}

	private Criterion makeRestriction(String propertyName, Bounds bounds) {
		if (bounds.hasLow())
			if (bounds.hasHigh())
				if (bounds.equal()) 
					return Restrictions.eq(propertyName, bounds.getLow()); // "50" or "50..50"
				else 
					return Restrictions.and(Restrictions.ge(propertyName, bounds.getLow()),
							Restrictions.le(propertyName, bounds.getHigh())); // "10..50"
			else 
				return Restrictions.ge(propertyName, bounds.getLow()); // "10.."
		else
			if (bounds.hasHigh())
				return Restrictions.le(propertyName, bounds.getHigh()); // "..50"
			else 
				return null;
	}

	private void destroyCriteria() {
		criterion = null;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		destroyCriteria();
	}

	public class Bounds {
		/**
		 * <p>Analyze a single clause of the form <tt>"<var>x</var>..<var>y</var>"</tt> 
		 * (where <var>x</var> or <var>y</var>, but not both, might be missing)
		 * and figure out the lower and upper bounds of the range it represents.</p>
		 * 
		 * <p>This is the parser.</p>
		 */
		Integer low = null, high = null;

		Bounds() { super(); }
		Bounds(Integer low, Integer high) { this.low = low; this.high = high; }
		Bounds(String clause) throws MalformedRangeExpression {
			ClauseTokener tokener = new ClauseTokener(clause);
			Token t1 = tokener.getNextToken();
			switch (t1.getType()) {
			case End: return;
			case Num: low = t1.getInteger(); break;
			case Range: 
				Token t2 = tokener.getNextToken();
				switch (t2.getType()) {

				case End: throw new MalformedRangeExpression("Range with missing numbers");

				case Num: // "..50"
					if (tokener.getNextToken().getType() != tokenType.End)
						throw new MalformedRangeExpression("Range symbol following upper bound");
					else {
						high = t2.getInteger();
						return;
					}

				case Range: throw new MalformedRangeExpression("Two consecutive range symbols");
				default: throw new MalformedRangeExpression("Mysterious token type '" + t2.getType() + "'");
				}
			default: throw new MalformedRangeExpression("Mysterious token type '" + t1.getType() + "'");
			}

			// first token was the low end of the range
			Token t2 = tokener.getNextToken();
			switch (t2.getType()) {
			case End: high = low; return; // "50"
			case Num: throw new MalformedRangeExpression("Two consecutive numbers");
			case Range: 
				Token t3 = tokener.getNextToken();
				switch (t3.getType()) {
				case End: return;  // "10.."
				case Num: // "10..50"
					if (tokener.getNextToken().getType() != tokenType.End)
						throw new MalformedRangeExpression("Range symbol following upper bound");
					else {
						high = t3.getInteger(); 
						return;
					}
				case Range: throw new MalformedRangeExpression("Two consecutive range symbols");
				default: throw new MalformedRangeExpression("Mysterious token type '" + t3.getType() + "'");
				}
			default: throw new MalformedRangeExpression("Mysterious token type '" + t2.getType() + "'");
			}
		}
		
		public boolean equal() {
			return hasHigh() && hasLow() && high == low;
		}

		public Integer getLow() {
			return low;
		}
		public boolean hasLow() {
			return low != null;
		}
		public void setLow(Integer low) {
			this.low = low;
		}
		public Integer getHigh() {
			return high;
		}
		public boolean hasHigh() {
			return high != null;
		}
		public void setHigh(Integer high) {
			this.high = high;
		} 	
	}

	private static class Token {
		String token;
		tokenType type;

		Token(String s, tokenType t) {
			token = s;
			type = t;
		}

		public Integer getInteger() {
			return type == tokenType.Num ? new Integer(token) : null;
		}

		public tokenType getType() {
			return type;
		}
	}

	private enum charType {
		NumChar, DotChar, BlankChar, BadChar, End
	}

	/**
	 * Look, Mom!  I'm programming in C!
	 * @author mjd 20081203
	 */
	private class ClauseTokener {
		String s;
		int pos = 0;
		Token curToken;

		public ClauseTokener(String clause) {
			s = clause;        // Merry Christmas
		}

		public char curChar() {
			return s.charAt(pos);
		}

		public void findNextToken() throws MalformedRangeExpression {
			int start = pos;
			charType ct = curCharType();

			if (ct == charType.End) {
				curToken = new Token(null, tokenType.End);
				return;
			}

			while (curCharType() == ct) pos++;

			if (ct == charType.BlankChar) // This one is not interesting
				findNextToken();          // So get the *next* next token instead
			else {
				String tok = s.substring(start, pos);
				curToken = new Token(tok, whatTokenType(tok));
			}
		}

		public Token getNextToken() throws MalformedRangeExpression {
			findNextToken();
			return getCurToken();
		}

		public Token getCurToken() {
			return curToken;
		}

		public charType curCharType() {
			if (pos >= s.length()) return charType.End;
			else return whatCharType(curChar());
		}

		public charType whatCharType(char c) {
			switch (c) {
			case ' ': 
			case '\t': 
				return charType.BlankChar;

			case '.':
				return charType.DotChar;

			case '0': case '1': case '2': case '3': case '4':
			case '5': case '6': case '7': case '8': case '9':
				return charType.NumChar;

			default:
				return charType.BadChar;
			}
		}

		public tokenType whatTokenType(String tok) throws MalformedRangeExpression {
			switch (whatCharType(tok.charAt(0))) {
			case BlankChar: throw new MalformedRangeExpression("Mysterious blank token!?");  // Should not happen
			case DotChar: return tokenType.Range;
			case NumChar: return tokenType.Num;
			case End: return tokenType.End;
			default: throw new MalformedRangeExpression("Mysterious unrecognized character type!?");
			}
		}
	}
}
