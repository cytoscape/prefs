package org.lib;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;


public class BoxSubPanel extends AntiAliasedPanel
{
	private boolean useSpacer = true;
	public BoxSubPanel() 				{ this(null, true);}
	public BoxSubPanel(String title) 	{ this(title, true);}
	public BoxSubPanel(String title, boolean spacer)
	{
		super();
		useSpacer = spacer;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentX(Component.LEFT_ALIGNMENT);
		if (title != null)
			setBorder(BorderFactory.createTitledBorder(title));
//			setBorder(makeSubpanelBorder2(title));
	}

	public void addLine(JComponent comp1, JComponent comp2)
	{
		Box line = Box.createHorizontalBox();
		line.setMaximumSize(new Dimension(300, 28));
		comp1.setAlignmentX(Component.LEFT_ALIGNMENT);
		line.add(comp1);
		if (comp2 != null)
		{
			comp2.setAlignmentX(Component.LEFT_ALIGNMENT);
			line.add(comp2);
		}
		if (useSpacer)
			addSpacer();
		super.add(line);
	}

	protected void add(JComponent comp)
	{
		comp.setAlignmentX(Component.LEFT_ALIGNMENT);
		if (useSpacer)
			addSpacer();
		super.add(comp);
	}
	
	public void addSpacer()	{super.add(Box.createRigidArea(BoxConstants.COMPONENT_SPACE));}
	public void addHalfSpacer()	{super.add(Box.createRigidArea(BoxConstants.HALF_SPACE));}
	public void addLeading()	{super.add(Box.createRigidArea(BoxConstants.LEADING));}
	
	//---------------------------------------------------------------------------------------------------
	public void equalizeHeight(BoxSubPanel other)
	{
		int myHeight = getPreferredSize().height;
		int otherHeight = other.getPreferredSize().height;
		if (otherHeight > myHeight)
		{
			setPreferredSize(new Dimension(getPreferredSize().width, otherHeight));
			setMinimumSize(getPreferredSize());
		}
		else 
		{
			other.setPreferredSize(new Dimension(other.getPreferredSize().width, myHeight));
			other.setMinimumSize(other.getPreferredSize());
		}
	}
	
	public void equalizeWidth(BoxSubPanel other)
	{
		int myWidth = getPreferredSize().width;
		int otherWidth = other.getPreferredSize().width;
		if (otherWidth > myWidth)
		{
			setPreferredSize(new Dimension(otherWidth, getPreferredSize().height));
			setMinimumSize(getPreferredSize());
		}
		else
		{
			other.setPreferredSize(new Dimension(myWidth, other.getPreferredSize().height));
			other.setMinimumSize(other.getPreferredSize());
		}
		other.invalidate();
		invalidate();
	}
	
	//---------------------------------------------------------------------------------------------------
	public JCheckBox addCheckBox(String text, String toolTip, Font f)
	{
		JCheckBox cb = makeCheckBox( text, toolTip, f);
		add(cb);
		return cb;
	}
	static public JCheckBox makeCheckBox(String text, String toolTip, Font f)
	{
		JCheckBox cb = new JCheckBox(text);
		cb.setToolTipText(toolTip);
		cb.setMaximumSize(new Dimension(Integer.MAX_VALUE, cb.getPreferredSize().height));
		if (f != null) cb.setFont(f);
		return cb;
	}

}
