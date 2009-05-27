package org.cipres.treebase.util;

import junit.framework.TestCase;

public class UnixOptionsTest extends TestCase {
	String[] args;
	GetOpts<UnixOptions> go;
	UnixOptions opts;
	
	public void testSimple() {
		go = new GetOpts<UnixOptions>(new UnixOptions (""));
		opts = go.getOpts("foo");
		args = go.getArgs();
		assertEquals(1, args.length);
	}
	
	public void testBool() {
		go = new GetOpts<UnixOptions>(new UnixOptions ("b"));		
		opts = go.getOpts("foo");
		args = go.getArgs();
		assertEquals(1, args.length);
		assertFalse(opts.getBoolOpt("b"));
		
		opts = go.getOpts("-b", "foo");
		args = go.getArgs();
		assertEquals(1, args.length);
		assertTrue(opts.getBoolOpt("b"));
		
		opts = go.getOpts("foo", "-b");
		args = go.getArgs();
		assertEquals(2, args.length);
		assertFalse(opts.getBoolOpt("b"));
	}
	
	public void testBasic() {
		go = new GetOpts<UnixOptions>(new UnixOptions ("Bs:i=I=S:b"));
		opts = go.getOpts("-s", "foo", "-b", "-i", "119", "blah", "-B", "blah");
		args = go.getArgs();
		
		assertEquals(3, args.length);
		assertTrue(opts.getBoolOpt("b"));
		assertFalse(opts.getBoolOpt("B"));
		assertEquals("foo", opts.getStringOpt("s"));
		assertEquals("", opts.getStringOpt("S"));
		assertEquals(119, opts.getIntOpt("i"));
		assertEquals(0, opts.getIntOpt("I"));
	}

	public void testDashDash() {
		GetOpts<UnixOptions> go = new GetOpts<UnixOptions>(new UnixOptions ("bS:s:"));
		opts = go.getOpts("-s", "foo", "--", "-b", "-S", "bar", "baz");
		args = go.getArgs();
		
		assertEquals(4, args.length);
		assertEquals("foo", opts.getStringOpt("s"));
		assertEquals("", opts.getStringOpt("S"));
		assertFalse(opts.getBoolOpt("b"));
	}
	
	public void testRegression1() {
		go = new GetOpts<UnixOptions>(new UnixOptions ("nl:"));
		opts = go.getOpts("-n", "blah");
		args = go.getArgs();
	
		assertEquals(args.length, 1);
		assertEquals(args[0], "blah");
		assertTrue(opts.getBoolOpt("n"));
		assertTrue(opts.acceptsOpt("l"));
		assertFalse(opts.acceptsOpt(":"));
		assertFalse(opts.hasOpt("l"));
		assertEquals(opts.getOptWithDefault("l", "def"), "def");
	}
	
	public void testRegression2( ){
		go = new GetOpts<UnixOptions>(new UnixOptions ("a:l:"));
		opts = go.getOpts("-a", "foo", "bar");
		assertTrue(opts.hasOpt("a"));
		assertFalse(opts.hasOpt("l"));
		assertEquals("foo", opts.getStringOpt("a"));
		assertEquals("foo", opts.getOptWithDefault("a", "snonk"));
		assertEquals("bonk", opts.getOptWithDefault("l", "bonk"));
	}

}		