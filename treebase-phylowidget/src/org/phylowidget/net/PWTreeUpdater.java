/***************************************************************************************************
 * Copyright (c) 2007, 2008 Gregory Jordan
 * 
 * This file is part of PhyloWidget.
 * 
 * PhyloWidget is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 * 
 * PhyloWidget is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with PhyloWidget. If not,
 * see <http://www.gnu.org/licenses/>.
 */
package org.phylowidget.net;

import java.util.regex.Pattern;

import org.andrewberman.unsorted.DelayedAction;
import org.phylowidget.PhyloWidget;
import org.phylowidget.tree.TreeIO;
import org.phylowidget.ui.PhyloTree;

/**
 * A utility class to update PhyloWidget's internal tree when triggered via JavaScript. For the
 * Java->JavaScript communication, see the JSTreeUpdater.
 * 
 * @author Greg
 * 
 */
public class PWTreeUpdater extends DelayedAction {

	String parseMe;
	public static final String REGEX = "@"; // This is a new addition--Madhu

	/**
	 * @author Madhu
	 * 
	 * This method is a rewrite of the previous method with the same name written by Greg Jordan.
	 * Earlier method did not have a way to treat treeid.
	 * @param s contains treeid and newick string
	 */
	public void triggerUpdate(String s) {

		System.out.println("STRING: " + s);

		boolean showEditableMenu = false;
		if (s.indexOf(REGEX) >= 0) {

			Pattern p = Pattern.compile(REGEX);
			String[] items = p.split(s);

			System.out.println("TREE ID :" + items[0]);
			System.out.println("PARSE ME :" + items[1]);
			parseMe = items[1];
			if (items.length > 2 && items[2].equalsIgnoreCase("T")) {
				showEditableMenu = true;
			}
			TreeIO.processTreeWithOption(items[0], showEditableMenu);

		} else {
			TreeIO.processTreeWithOption(null, showEditableMenu);
			parseMe = s;
		}
		trigger(200);
	}

	// public void triggerUpdate(String s)
	// {
	// parseMe = s;
	// trigger(200);
	// }

	public void run() {
		PhyloTree t = new PhyloTree();
		TreeIO.setOldTree(PhyloWidget.trees.getTree());// In this method newick String for uploaded
		// tree (oldnewick String) is saved ..
		// Madhu..
		PhyloWidget.trees.setTree(TreeIO.parseNewickString(t, parseMe));
	}
}
