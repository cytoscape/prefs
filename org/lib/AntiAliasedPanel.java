package org.lib;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class AntiAliasedPanel extends JPanel
{
	public AntiAliasedPanel()
	{
		super( );
	}
	public AntiAliasedPanel(LayoutManager inLayout)
    {
		super( inLayout);
    }
	@Override
	public void paint(final Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setBackground(Color.black);
		super.paint(g);
	}
}