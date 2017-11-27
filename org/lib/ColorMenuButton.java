package org.lib;

/**
 ColorMenuButton
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.border.AbstractBorder;

public class ColorMenuButton extends JButton implements MouseListener, ColorMenu.ColorMenuListener //, ActionListener
{
	private static final long	serialVersionUID	= 1L;
	boolean fUseNoFill = true; 
	public ColorMenuButton(boolean useNoFill)
    {
        super();
        fUseNoFill = useNoFill;
        addMouseListener(this);
        setBorder(new BorderPainter());
        GuiFactory.setSizes(this, new Dimension(16, 16));
    }
	public ColorMenuButton()
	{
		this(true);
	}
   	
	
	public ColorMenuButton(Dimension dim, boolean useNoFill)
	{
		this(useNoFill);
	    GuiFactory.setSizes(this, dim);
	}
   	
   	public ColorMenuButton(String name, boolean useNoFill)
	{
   		this(useNoFill);
	    setText(name);
	    setToolTipText(name + ".tooltip");
	}
   	
   	public ColorMenuButton(String name, Color c, boolean useNoFill)
	{
   		this(name, useNoFill);
	    setColor(c);
	}

   	public ColorMenuButton(boolean useNoFill, Color c)
    {
        this(useNoFill);
        setColor(c);
    }

    @Override public void paintComponent(Graphics g)
    {
    	g.drawRect(0,0,35,54);
		super.paintComponent(g);
 		boolean noFill = Colors.kUnfilled.equals(fColor);
  		if (noFill) {
			g.setColor(Color.black);
			g.drawRect(0,0,35,54);
		}
    }
    //--------------------------------------------------------------------------------------
    class BorderPainter extends AbstractBorder
	{
    	private static final long serialVersionUID = 1L;

		// private ColorMenu fMenu;
		public BorderPainter()		{		}		//  ColorMenu menu  fMenu = menu;	

		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
		{
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setColor(getColor());
			g2.fillRect(0, 0, c.getWidth(), c.getHeight());
			g2.setColor(Color.BLACK);
			g2.drawRect(0, 0, c.getWidth() - 1, c.getHeight() - 1);
			boolean noFill = Colors.kUnfilled.equals(fColor);
		  	if (noFill) {
				g.setColor(Color.black);
				g.setFont(FontUtil.BoldSerif12);
				g.drawString("N", (getWidth() / 2 - 4), getHeight() / 2 + 3);
			}
		}
	}
    //--------------------------------------------------------------------------------------
    public static final String COLOR_CHANGED = "Color Changed";
    public void colorSelected()			// called as a result of the color being set
    {
        setColor(ColorMenu.getInstance().getColor());
        fireActionPerformed(new ActionEvent(this, 0, COLOR_CHANGED));
    }
    //--------------------------------------------------------------------------------------
    private Color fColor = null; 
    public Color getColor()    {        return fColor;    }

    public void setColor(Color c)
    {
        if (c == null)    	c = Color.red;
        fColor = c;
        setBackground(c);
        setForeground(c);
    }

	@Override	public void mouseClicked(MouseEvent arg0) {}
	@Override	public void mouseEntered(MouseEvent arg0) {}
	@Override	public void mouseExited(MouseEvent arg0) {}
	@Override	public void mousePressed(MouseEvent arg0) 
	{
		if (isEnabled()) {
			ColorMenu cm = ColorMenu.getInstance();
			cm.setOwner(this);
			cm.setColor(getBackground());
			cm.setUseNoFill(fUseNoFill);
			Point location = cm.getSelectedLocation();
			cm.show(this, -location.x, -location.y);
		}
	}
	@Override	public void mouseReleased(MouseEvent arg0) {}
}