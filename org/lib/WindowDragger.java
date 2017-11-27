/*
 * @(#)WindowDragger.java
 *
 * Copyright (c) 2008 Jeremy Wood. All Rights Reserved.
 *
 * You have a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) You do not utilize the software in a manner
 * which is disparaging to the original author.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. THE AUTHOR SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL THE
 * AUTHOR BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF THE AUTHOR HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 */


package org.lib;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.event.*;

/** This listens to drag events and drags a Component's window as the user
 * drags the mouse.
 * <P>This is especially convenient for floating palettes, but it also
 * can be used for frames, too.
 * <P>In Mac OS 10.5 (Java 1.5) there is a system property
 * discussed <A HREF="http://developer.apple.com/technotes/tn2007/tn2196.html">here</A>
 * that achieves about the same goal.  To my knowledge they are the same in
 * function, but I haven't explored the subject in depth.
*/
public class WindowDragger extends MouseInputAdapter {
	private static boolean usingMac = (System.getProperties().getProperty("mrj.version")!=null);
	
	Point mouseLoc;
	boolean dragging;
	
	public void mousePressed(MouseEvent e) {
		mouseLoc = e.getPoint();
		dragging = true;
		SwingUtilities.convertPointToScreen(mouseLoc,(Component)e.getSource());
	}
	
	public void mouseReleased(MouseEvent e) {
		dragging = false;
		mouseLoc = null;
	}
	
	public void mouseDragged(MouseEvent e) {
		if(mouseLoc==null || dragging==false) {
			return;
		}
		synchronized(mouseLoc) {
			Point p = e.getPoint();
			SwingUtilities.convertPointToScreen(p,(Component)e.getSource());
			if(usingMac) p.y = Math.max(0,p.y);
			WindowDragger.translateWindow(p.x-mouseLoc.x,
					p.y-mouseLoc.y, 
					SwingUtilities.getWindowAncestor((Component)e.getSource()));
			
			mouseLoc.setLocation(p);
		}
	}
		

	public WindowDragger() {
		
	}
	public WindowDragger(Component c) {
		this(new Component[] {c});
	}
	
	public WindowDragger(Component[] c) {
		for(int a = 0; a<c.length; a++) {
			c[a].addMouseListener(this);
			c[a].addMouseMotionListener(this);
		}
	}
		
	/** Translates a window, after possibly adjusting dx and dy for
	 * OS-based restraints.
	 */
	protected static void translateWindow(int dx,int dy,Window window) {
		Point p = window.getLocation();
		p.x+=dx;
		p.y+=dy;
		if(usingMac) p.y = Math.max(0,p.y);
		window.setLocation(p);
	}
}
