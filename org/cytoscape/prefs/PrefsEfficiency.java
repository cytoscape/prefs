package org.cytoscape.prefs;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;

import org.lib.HBox;

public class PrefsEfficiency extends AbstractPrefsPanel {

	protected PrefsEfficiency(Cy3PreferencesRoot dlog) {
		super(dlog,  "cytoscape3");
		setBorder(BorderFactory.createEmptyBorder(20,32,0,0));

	}
    @Override public void initUI()
    {
        super.initUI();
		Box col1 = Box.createVerticalBox();
		HBox firstLine = new HBox(new JLabel("Thresholds can be used to suppress drawing in large networks."), Box.createHorizontalGlue());
		HBox secondLine = new HBox(new JLabel("Several operations can be turned off to increase responsiveness."), Box.createHorizontalGlue());
		col1.add(firstLine);
		col1.add(secondLine);
		col1.add(Box.createRigidArea(new Dimension(20,14)));
	    col1.add( makeNumberSliderRow("Coarse Detail", "render.coarseDetailThreshold", 500, 50000, 1000));
	    col1.add(makeNumberSliderRow("Edge Arrow",  "render.edgeArrowThreshold", 500, 5000000, 1000));
	    col1.add(makeNumberSliderRow("Node Border",  "render.nodeBorderThreshold", 1000, 5000000, 5000));
	    col1.add(makeNumberSliderRow("Node Label",  "render.nodeLabelThreshold", 100, 50000, 500));
	    col1.add(makeNumberSliderRow("View Threshold", "viewThreshold",  1000, 5000000, 100000));
		col1.add(Box.createRigidArea(new Dimension(20,14)));
		HBox anotherLine = new HBox(new JLabel("Limiting the number of steps that can be undone can save memory."), Box.createHorizontalGlue());
		col1.add(anotherLine);
	    col1.add(makeNumberSliderRow("Undo Depth", "undo.limit",  1, 500, 10));

		add(col1);   
	}


}
