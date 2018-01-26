package org.cytoscape.prefs;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

public class PrefsLegends extends AbstractPrefsPanel {

	protected PrefsLegends(Cy3PreferencesRoot dlog) {
		super(dlog, "legend");
	}

    @Override public void initUI()
    {
        super.initUI();
		Box page = Box.createVerticalBox();
	    page.add(Box.createRigidArea(new Dimension(20,50)));
	    page.add(new JLabel("Reserved for Legend Definition"));
	    page.add(Box.createRigidArea(new Dimension(20,30)));
	    page.add(new JCheckBox("Show Edge Categories"));
	    page.add(new JCheckBox("Show Node Colors"));
	    page.add(new JCheckBox("Show Summary Network Analysis"));
	    page.add(new JCheckBox("Gradient Edtior"));
	    page.add(Box.createVerticalGlue());
	    add(page);
	}
}
