package org.cytoscape.prefs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.lib.VBox;

public class ClickableBox extends VBox implements MouseListener {

	ClickableBox(boolean useSpacing, boolean useGlue)
	{
		super(useSpacing, useGlue);
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("Set Selected");

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
