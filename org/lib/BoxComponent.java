package org.lib;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;


public class BoxComponent extends AntiAliasedPanel
{
	private static final long serialVersionUID = 1L;

	public BoxComponent(String label, JComponent comp) { this(new JLabel(label), comp);}
	
	public BoxComponent(JComponent ... components)
	{
		super();
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setAlignmentX(Component.LEFT_ALIGNMENT);
		
		for(JComponent comp : components)
		{
			comp.setAlignmentX(Component.LEFT_ALIGNMENT);
			comp.setAlignmentY(Component.CENTER_ALIGNMENT);
			if ((comp instanceof JTextField) && (((JTextField)comp).getColumns() > 0))
				comp.setMaximumSize(comp.getPreferredSize());
//			comp.setFont(new Font("Dialog", Font.PLAIN, 10));
			
			add(comp);
			add(Box.createRigidArea(BoxConstants.COMPONENT_SPACE));

		}
		add(Box.createHorizontalGlue());
		setMaximumSize(new Dimension(Integer.MAX_VALUE, getPreferredSize().height));
	}
}
