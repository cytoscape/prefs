package org.lib;
/**
	ColorMenu
*/
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPopupMenu;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;


public class ColorMenu extends JPopupMenu  implements ActionListener
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	public interface ColorMenuListener
	{
		public void colorSelected();
	}

	protected Border fUnselectedBorder, fSelectedBorder, fActiveBorder;
//	protected Hashtable<Color, ColorPane> fPanes;
	protected ColorPane fSelected, fCurrent;
	protected ColorMenuListener fOwner;
	public void setOwner(ColorMenuListener cml)	{ fOwner = cml;	}
	static private ColorMenu sColorMenu = new ColorMenu();			// one copy for getInstance() to return	
	public static ColorMenu getInstance()	{ return sColorMenu;	}
	private static int rows = 13, cols = 10;
	private ColorMenu()
	{
		fUnselectedBorder = new MatteBorder(1, 1, 1, 1, Color.gray);
		fSelectedBorder = new MatteBorder(1, 1, 1, 1, Color.red);
		fActiveBorder = new MatteBorder(1, 1, 1, 1, Color.white);
		setBorder(new EmptyBorder(0, 1, 1, 1));
		int rows = 13, cols = 10;
		setLayout(new GridLayout(rows, cols));
		makeColorPanes();
	}
	static private ColorPane[] panes = null;

	private void makeColorPanes()
	{
		int i = 0;
		panes = new ColorPane[rows*cols];
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < cols; c++)
			{
				Color color = Colors.colorFromIndex(i);
				panes[i] = new ColorPane(r, c, color, this);
				add(panes[i]);
				i++;
			}

	}
//	private ColorMenu(ColorMenuListener owner)
//	{
//		this();
//		fOwner = owner;
//	}
	public void setUseNoFill(boolean use)
	{
		if (panes == null) makeColorPanes();
		ColorPane pane0 = panes[0];
		pane0.setColor(use ? Colors.kUnfilled : Color.white);
	}
//----------------------------------------------------------------------------
	public void setColor(Color c)
	{
        if (c == null)            return;
//		ColorPane pane = fPanes.get(c);
//		if (pane == null)
		ColorPane	pane = findClosestPane(c);
		if (fSelected != null)
			fSelected.setSelected(false);
		fSelected = fCurrent = pane;
		if (fSelected != null) fSelected.setSelected(true);
	}
	
	//---------------------------------------------------------------------
	float COLOR_THRESHOLD = 0.00001f;
	private ColorPane findClosestPane(Color c)
	{
		if (panes == null) makeColorPanes();
		//For each pane in fPanes, get the color and compute the color distance.
		float[] targetColors = c.getRGBColorComponents(null);
		ColorPane minPane = null;
		float minDistance = -1;
		float[] sampleColors = new float[3];
		for (ColorPane currPane : panes)
		{
			Color color = currPane.getColor();
			color.getRGBColorComponents(sampleColors);
			float distance = 0;
			for (int i = 0; i < 3; i++)
			{
				float diff = targetColors[i] - sampleColors[i];
				distance += (diff * diff);
			}
			if (distance < COLOR_THRESHOLD) return currPane;		
			if (minPane == null || distance < minDistance)
			{
				minPane = currPane;
				minDistance = distance;
			}
		}
		return minPane;
	}
	//---------------------------------------------------------------------
	public Color getColor()
	{
		if (fCurrent != null)
		{
			if (!fCurrent.isFilled())	return Colors.kUnfilled;
			return fCurrent.getColor();
		}
		return (fSelected != null) ? fSelected.getColor() : null;
	}

	public Point getSelectedLocation()	{		return (fSelected == null)  ? null : fSelected.getLocation();	}
	
	public void actionPerformed(ActionEvent e)
	{
		ColorPane pn = (ColorPane) e.getSource();
		if (fSelected != null)
			fSelected.setSelected(false);
		fSelected = pn;
		pn.setSelected(true);
		if (fOwner == null)		System.err.println("Owner not assigned to ColorMenu");
		else  {
			fOwner.colorSelected();
			fOwner = null;
		}
	}
}

