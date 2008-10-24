/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.yoursway.swt.scrollbar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

/**
 * This class provides the layout for CoolScrolledComposite
 * 
 * @see CoolScrolledComposite
 */
class CoolScrolledCompositeLayout extends Layout {

	boolean inLayout = false;
	static final int DEFAULT_WIDTH = 64;
	static final int DEFAULT_HEIGHT = 64;

	protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
		CoolScrolledComposite sc = (CoolScrolledComposite) composite.getParent();
		Point size = new Point(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		if (sc.content != null) {
			Point preferredSize = sc.content.computeSize(wHint, hHint, flushCache);
			Point currentSize = sc.content.getSize();
			size.x = sc.getExpandHorizontal() ? preferredSize.x : currentSize.x;
			size.y = sc.getExpandVertical() ? preferredSize.y : currentSize.y;
		}
		size.x = Math.max(size.x, sc.minWidth);
		size.y = Math.max(size.y, sc.minHeight);
		if (wHint != SWT.DEFAULT)
			size.x = wHint;
		if (hHint != SWT.DEFAULT)
			size.y = hHint;
		return size;
	}

	protected boolean flushCache(Control control) {
		return true;
	}

	protected void layout(Composite composite, boolean flushCache) {
		if (inLayout)
			return;
		CoolScrolledComposite sc = (CoolScrolledComposite) composite.getParent();
		if (sc.content == null)
			return;

		CoolScrollBar horizontalBar = sc.horizontalBar;
		CoolScrollBar verticalBar = sc.verticalBar;

		if (horizontalBar.getSize().y >= sc.getSize().y) {
			return;
		}
		if (verticalBar.getSize().x >= sc.getSize().x) {
			return;
		}

		inLayout = true;
		Rectangle contentRect = sc.content.getBounds();

		Rectangle hostRect = composite.getClientArea();
		if (sc.expandHorizontal) {
			contentRect.width = Math.max(sc.minWidth, hostRect.width);
		}
		if (sc.expandVertical) {
			contentRect.height = Math.max(sc.minHeight, hostRect.height);
		}

		horizontalBar.setRunnerSize(contentRect.width, hostRect.width);
		float hPosition = horizontalBar.getPosition();
		if (hPosition > 0)
			contentRect.x = (int) -hPosition;

		verticalBar.setRunnerSize(contentRect.height, hostRect.height);
		float vPosition = verticalBar.getPosition();
		if (vPosition > 0)
			contentRect.x = (int) -vPosition;

		sc.content.setBounds(contentRect);
		inLayout = false;
	}
}
