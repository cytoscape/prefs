package org.cytoscape.prefs;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import org.cytoscape.prefs.lib.VBox;

public class NestedPrefPanel extends AbstractPrefsPanel {

	public NestedPrefPanel(String namespace, JComponent contents, Cy3PreferencesRoot container)
	{
		super(container, namespace);
		VBox box = new VBox(true, true, contents);
		box.setBorder(null);
		add(box);
		setBorder(BorderFactory.createLineBorder(Color.cyan));	
//		contents.setBorder(BorderFactory.createLineBorder(Color.magenta));	
	}
	public String toString()	{ return namespace;	}
}
