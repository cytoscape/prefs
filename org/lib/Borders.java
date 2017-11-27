package org.lib;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class Borders
{
	public static Border red = BorderFactory.createLineBorder(Color.red);
	public static Border green = BorderFactory.createLineBorder(Color.green);
	public static Border blue = BorderFactory.createLineBorder(Color.blue);
	public static Border cyan = BorderFactory.createLineBorder(Color.cyan);
	public static Border orange = BorderFactory.createLineBorder(Color.orange);
	public static Border pink = BorderFactory.createLineBorder(Color.pink);
	public static Border magenta = BorderFactory.createLineBorder(Color.magenta);
	public static Border white = BorderFactory.createLineBorder(Color.white);
	public static Border gray = BorderFactory.createLineBorder(Color.gray);
	public static Border ltGray = BorderFactory.createLineBorder(Color.lightGray);
	public static Border yellow = BorderFactory.createLineBorder(Color.yellow);
	public static Border etched = BorderFactory.createEtchedBorder();
	public static Border black = BorderFactory.createLineBorder(Color.black);
//	public static Border dashed = new DashedBorder(Color.black);
		
	public static Border doubleRaised = BorderFactory.createCompoundBorder( BorderFactory.createRaisedBevelBorder(), BorderFactory.createRaisedBevelBorder());
	public static Border tripleRaised = BorderFactory.createCompoundBorder(
			BorderFactory.createCompoundBorder( BorderFactory.createRaisedBevelBorder(), BorderFactory.createRaisedBevelBorder()),
			BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(),BorderFactory.createRaisedBevelBorder()));

	public static Border etchedRaised = BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.darkGray, Color.lightGray);
	public static Border etchedLowered = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, Color.black, Color.gray);
	public static Border etched1 = BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.yellow, Color.blue);
	public static Border etched2 = BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.blue, Color.green);
	public static Border etched3 = BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.yellow, Color.cyan);
	public static Border etched4 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, Color.yellow, Color.pink);
	public static Border bevel1 = BorderFactory.createBevelBorder(EtchedBorder.LOWERED, Color.gray, Color.cyan);
	public static Border bevel2 = BorderFactory.createBevelBorder(EtchedBorder.RAISED, Color.gray, Color.yellow);
	public static Border bevel3 = BorderFactory.createBevelBorder(EtchedBorder.RAISED, Color.red, Color.white);
	public static Border bevel4 = BorderFactory.createBevelBorder(EtchedBorder.RAISED, Color.gray, Color.green);
	public static Border blankMedium = BorderFactory.createEmptyBorder(8,8,8,8);
	public static Border blankSmall = BorderFactory.createEmptyBorder(5, 5, 5, 5);
	public static Border blank15 = BorderFactory.createEmptyBorder(15, 15, 15, 15);
	public static Border blank30 = BorderFactory.createEmptyBorder(30, 30, 30, 30);
	public static Border dialogFooter = BorderFactory.createEmptyBorder(5, 5, 5, 5);
	public static Border stdTableHeader = BorderFactory.createEmptyBorder(1, 3, 1, 3);
	public static Border empty1 = BorderFactory.createEmptyBorder(1, 1, 1, 1);

	//---------------------------------------------------------------------------------------------------------
	/**
	 * An border used for a pulsating animated effect
	 * http://www.java2s.com/Code/Java/Swing-Components/PulseAnimationField.htm
	 * @author Seth
	 *
	 */
	public static PulsatingBorder makePulsatingBorder(JComponent comp, Color color, float thickness, Insets insets){
		return new PulsatingBorder(comp,color, thickness, insets);
	}
	public static PulsatingBorder makePulsatingBorder(JComponent comp, Color color){
		return makePulsatingBorder(comp,color, 2.0f, new Insets(2,2,2,2));
	}
	public static class PulsatingBorder implements Border {
        private float thicknessFactor = 0.0f;
        private float thickness;
        private JComponent comp;
        private Color color;
        private Insets insets;
        
        public PulsatingBorder(JComponent comp, Color color, float thickness, Insets insets) {
            this.comp = comp;
            this.color = color;
            this.thickness = thickness;
            this.insets = insets;
        }
        
        public void paintBorder(Component c, Graphics g,
                int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            
            Rectangle2D r = new Rectangle2D.Double(x, y, width - 1, height - 1);
            g2.setStroke(new BasicStroke(thickness * getThicknessFactor()));
            g2.setComposite(AlphaComposite.SrcOver.derive(getThicknessFactor()));
            g2.setColor(color);
            g2.draw(r);
        }

        public Insets getBorderInsets(Component c) 		{        	return insets;        }
        public boolean isBorderOpaque() 				{           return false;        }
        public float getThicknessFactor() 				{           return thicknessFactor;        }
        public void setThicknessFactor(float thickness) {           this.thicknessFactor = thickness;            comp.repaint();        }
    }
	//---------------------------------------------------------------------------------------------------------
	public static void spiffyBorder(JComponent comp, String title)
	{
		comp.setBorder(BorderFactory.createCompoundBorder( BorderFactory .createEmptyBorder(5, 5, 5, 5), BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(), title)));
	}
	
	public static GradientBorder makeGradientBorder(Color colorLeft, Color colorRight, int thickness){
		return new GradientBorder(colorLeft, colorRight, thickness, thickness, thickness, thickness);
	}
	public static class GradientBorder implements Border
	{
	    private Insets margin;
		private Color c1;
		private Color c2;
	    public GradientBorder (Color c1, Color c2, int top, int left, int bottom, int right )
	    {
	        super ();
	        this.c1 = c1;
	        this.c2 = c2;
	        margin = new Insets ( top, left, bottom, right );
	    }
	    public void paintBorder ( Component c, Graphics g, int x, int y, int width, int height )
	    {
	        Graphics2D g2d = ( Graphics2D ) g;
	        g2d.setPaint ( new GradientPaint ( x, y, c1, x + width, y,c2 ) );
	        Area border = new Area ( new Rectangle ( x, y, width, height ) );
	        border.subtract ( new Area ( new Rectangle ( x + margin.left, y + margin.top,
	                width - margin.left - margin.right, height - margin.top - margin.bottom ) ) );
	        g2d.fill ( border );
	    }
	    public Insets getBorderInsets ( Component c ) 	{	        return margin;	    }
	    public boolean isBorderOpaque ()		   		{	        return true;	    }
	}

}
