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
package org.phylowidget;

import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.lang.reflect.Method;

import org.andrewberman.ui.Color;
import org.andrewberman.ui.FontLoader;
import org.phylowidget.net.PWClipUpdater;
import org.phylowidget.net.PWTreeUpdater;
import org.phylowidget.tree.TreeManager;
import org.phylowidget.ui.PhyloUI;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphicsJava2D;

public class PhyloWidget extends PApplet {
	private static final long serialVersionUID = -7096870051293017660L;

	public static PhyloWidget p;
	public static TreeManager trees;
	public static PhyloUI ui;
	// public static Properties props;

	// public static int WIDTH = 400;
	// public static int HEIGHT = 400;

	public static float FRAMERATE = 40f;
	public static float TWEEN_FACTOR = 30f / FRAMERATE;

	public static boolean usingNativeFonts;
	public static boolean openGL;
	public static boolean isOutputting;

	private PWTreeUpdater treeUpdater;
	private PWClipUpdater clipUpdater;

	private String messageString = new String();

	public PhyloWidget() {
		super();
	}

	@Override
	public void setup() {
		if (frame != null) {
			/*
			 * We're in standalone / application mode.
			 */
			frame.setResizable(true);
			size(400, 400);
		} else {
			size(width, height);
		}

		frameRate(FRAMERATE);

		p = this;

		// Creates, manages, and renders trees.
		trees = new TreeManager(this);
		// Creates and manages UI elements.
		ui = new PhyloUI(this);
		treeUpdater = new PWTreeUpdater();
		clipUpdater = new PWClipUpdater();

		ui.setup();
		trees.setup();

		bgColor = Color.parseColor(PhyloWidget.ui.background).getRGB();
	}

	int bgColor;

	@Override
	public void draw() {
		background(bgColor);
		trees.update();
		if (messageString.length() != 0) drawMessage();
		drawNumLeaves();
		drawFrameRate();
	}

	@Override
	public void stop() {
		super.stop();
	}

	void drawFrameRate() {
		textAlign(PConstants.LEFT);
		textFont(FontLoader.instance.vera);
		textSize(10);
		fill(255, 0, 0);
		text(String.valueOf(round(frameRate * 10) / 10.0), width - 40, height - 10);
		// Uncomment to print out the number of leaves.
		// RootedTree t = trees.getTree();
		// int numLeaves = t.getNumEnclosedLeaves(t.getRoot());
		// text(numLeaves,5,height-10);
	}

	void drawNumLeaves() {
		int leaves = trees.getRenderer().getTree().getNumEnclosedLeaves(
			trees.getRenderer().getTree().getRoot());
		String nleaves = String.valueOf(leaves);
		textAlign(PConstants.LEFT);
		textFont(FontLoader.instance.vera);
		textSize(10);
		fill(255, 0, 0);
		text(nleaves, width - 100, height - 10);
	}

	void drawMessage() {
		textAlign(PConstants.LEFT);
		textFont(FontLoader.instance.vera);
		textSize(10);
		fill(255, 0, 0);
		text(messageString, 5, height - 10);
	}

	public void setMessage(String s) {
		messageString = s;
	}

	@Override
	public void size(int w, int h) {
		if (width != w || h != h) size(w, h, JAVA2D);
		// size(w,h,P3D);
		// size(w,h,OPENGL);
		if (g.getClass() == PGraphicsJava2D.class) {
			PGraphicsJava2D pg = (PGraphicsJava2D) g;
			hint(PConstants.ENABLE_NATIVE_FONTS); // Native fonts are nice!
			usingNativeFonts = true;
			pg.g2.setRenderingHint(
				RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);
			// pg.g2.setRenderingHint(RenderingHints.KEY_RENDERING,
			// RenderingHints.VALUE_RENDER_SPEED);
			pg.g2.setRenderingHint(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			// pg.g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
			// RenderingHints.VALUE_STROKE_PURE);
			// p.smooth();
		} else if (g.getClass().getName().equals("OPENGL")) {
			openGL = true;
		}
	}

	public void updateTree(String s) {
		treeUpdater.triggerUpdate(s);
	}

	public void updateClip(String s) {
		clipUpdater.triggerUpdate(s);
	}

	public void callFunction(String s, String p) {
		try {
			Method m = ui.getClass().getMethod(s, new Class[] {String.class});
			m.invoke(ui, new Object[] {p});
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	@Override
	public void keyPressed() {
		super.keyPressed();
		if (key == KeyEvent.VK_ESCAPE) key = 0;
	}

	static public void main(String args[]) {
		PApplet.main(new String[] {"org.phylowidget.PhyloWidget"});
	}
}