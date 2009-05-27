package org.cipres.treebase.util;

import java.util.List;

import org.hibernate.SQLQuery;

public class Select extends AbstractStandalone {
	public static void main(String[] args) {
		Select me = new Select();
		me.setupContext();
		String sql = join(args, " ");
		
		SQLQuery q = me.getSession().createSQLQuery(sql);
		List results = q.list();
		if (results.isEmpty()) { System.out.println("Empty result."); }
		else {
			int n = results.size();
			String items = n == 1 ? "item" : "items";
			System.out.println("Result: " + n + " " + items + ".");
		}
	}
	
	static String join(String[] ss, String sep) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String s : ss) {
			if (! first) sb.append(sep);
			sb.append(s);
			first = false;
		}
		return sb.toString();
	}
}
