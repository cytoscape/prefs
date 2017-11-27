package org.cytoscape.prefs;

import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import org.lib.VBox;

public class ClickableBox extends VBox implements MouseListener {


	public static Border borderOn = BorderFactory.createLineBorder(Color.orange, 4);
	public static Border borderOff = BorderFactory.createLineBorder(Color.lightGray, 4);
	public static Border borderSelect = BorderFactory.createLineBorder(Color.red, 4);
	
	ClickableBox(boolean useSpacing, boolean useGlue)
	{
		super(useSpacing, useGlue);
	}

	
	boolean selected = false;
	public void setSelect(boolean b)	
	{ 
//		if (b != selected)
		{
			selected = b;	
			setBorder(selected ? borderSelect : borderOff);
		}
	}
	public boolean isSelected()			{ return selected;	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("mouseClicked");
		setBorder(borderSelect);
		Container parent = getParent();
		while (parent != null && !(parent instanceof PrefsColors))
			parent = parent.getParent();
		if (parent != null)
			((PrefsColors) parent).deselectAllPalettes();
		setSelect(true);
	}

	@Override
	public void mousePressed(MouseEvent e) {
//		System.out.println("mousePressed");

	}

	@Override
	public void mouseReleased(MouseEvent e) {
//		System.out.println("mouseReleased");

	}

	@Override
	public void mouseEntered(MouseEvent e) {
//		System.out.println("mouseEntered");
		setBorder(selected ? borderSelect : borderOn);

	}

	@Override
	public void mouseExited(MouseEvent e) {
		
		int x = e.getX();
		int y = e.getY();
		Rectangle bounds = getBounds();
		if (x > 0 && x < bounds.getWidth() && y > 0 && y < bounds.getHeight())
			return;

//		System.out.println("mouseExited: (" + x + ", " + y + " )");
		setBorder(selected ? borderSelect : borderOff);

	}

}
