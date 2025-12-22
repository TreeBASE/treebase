package org.cipres.treebase.auxdata;

import java.util.Iterator;

import junit.framework.TestCase;
import static org.junit.Assert.*;

public class LazyListTest extends TestCase {
	private class Gen implements Generator<Integer> {
		Integer i = 0;
		public Integer another() {
			i += 1;
			return i;
		}
	}
	
	private class GStop implements Generator<Integer> {
		Integer i = 0;
		public Integer another() {
			i += 1;
			if (i < 2) { return i; }
			else { return null; }
		}
	}
	
	Generator<Integer> g = new Gen(), gstop = new GStop();
	LazyList<Integer> a1 = new LazyList<Integer> (23, g);
	LazyList<Integer> a2 = new LazyList<Integer> (119, a1);
	LazyList<Integer> b1 = new LazyList<Integer> (17, gstop);
	
	public void testLazyListELazyListOfE() {
		assertNotNull(a2);
		assertEquals((Integer) 119, a2.head);
		assertSame(a1, a2.tail);
	}
	
	public void testLazyListELazyGeneratorOfE() {
		assertNotNull(a1);
		assertEquals((Integer) 23, a1.head);
		assertSame(g, a1.gen);
		assertNull(a1.tail);
	}

	public void testLazyListGeneratorOfE() {
		assertNotNull(a1);
		assertSame(g, a1.gen);
	}
	
	public void testLazyListArrayOfE() {
		Integer ar[] = { 1, 4, 2, 8, 5, 7 };
		LazyList<Integer> ll = new LazyList<Integer> (ar);
		assertNotNull(ll);
		for (Integer i : ar) {
			assertEquals(i, ll.head());
			ll = ll.tail();
		}
		assertNull(ll);
	}

	public void testCons() {
		LazyList<Integer> a3 = a1.cons(5);
		assertNotNull(a3);
		assertEquals((Integer) 5, a3.head());
		assertSame(a1, a3.tail());
	}

	public void testHead() {
		assertEquals((Integer) 23, a1.head());
		assertEquals((Integer) 119, a2.head());
	}

	public void testSetHead() {
		LazyList<Integer> a3 = a2.cons(3);
		a3.setHead(17);
		assertEquals((Integer) 17, a3.head());
	}

	public void testTail() {
		assertEquals(a1, a2.tail());
		LazyList<Integer> n1 = a1.tail();
		assertEquals((Integer) 1, n1.head());
		assertSame(n1, a1.tail());
		assertEquals((Integer) 1, a1.tail().head());
		
		LazyList<Integer> n2 = n1.tail();
		assertEquals((Integer) 2, n2.head());
		assertSame(n2, n1.tail());
		assertEquals((Integer) 2, n1.tail().head());
	
		LazyList<Integer> b = b1;
		assertEquals((Integer) 17, b.head());
		b = b.tail();
		assertEquals((Integer) 1, b.head());
		b = b.tail();
		assertNull(b);
	}

	public void testSetTail() {
		LazyList<Integer> a = a1.cons(13);
		a.setTail(a);
		assertSame(a, a.tail());
		assertEquals((Integer) 13, a.tail().tail().head());
	}

	public void testLastNode() {
		assertEquals(a1, a1.lastNode());
		assertEquals(a1, a2.lastNode());
		assertEquals(a1, a2.cons(1).lastNode());
	}

	public void testNconc() {
		LazyList<Integer> a = new LazyList<Integer> (30, a1);
		a.setTail(null);
		LazyList<Integer> b = a.cons(40);
		b.nconc(a2);
		assertSame(a2, b.tail().tail());
	}
	
	public void testIterator() {
		Iterator it = a2.iterator();
		Integer expected[] = { 119, 23, 1, 2, 3 };
		int i = 0;
		while (it.hasNext() && i <  expected.length) {
			assertEquals(expected[i], it.next());
			i += 1;
		}
	}
	
	public void testIteration() {
		Integer expected[] = { 119, 23, 1, 2, 3 };
		int i = 0;
		
		for (Integer a : a2) {
			assertEquals(expected[i], a);
			i += 1;
			if (i >= expected.length) break;
		}
	}

}
