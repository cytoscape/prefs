package org.cytoscape.prefs;

import javax.swing.Box;
import javax.swing.JLabel;

import org.lib.Borders;
import org.lib.HBox;

public class PrefsImages extends MasterDetailPanel {

	protected PrefsImages(Cy3PreferencesRoot dlog) {
		super(dlog, "images");
	}
	static String introTxt1 = "    The existing Image Manager can be moved here,";
	static String introTxt2 = "    (optionally) taking it out of the Edit menu. ";
    @Override public void initUI()
    {
   		HBox intro = new HBox(true, new JLabel(introTxt1));
		HBox intro2 = new HBox(true, new JLabel(introTxt2));
		add(intro);
		add(intro2);
        super.initUI();
    	makeImageList();
        splitter.setDividerLocation(0.3);
     }

	private void makeImageList()
	    {
	    	addImage("Plant", "grid", makePlant(), "#grid-layout");
	    	addImage("Yeast", "circular", makeYeast(), "#yfiles-circular-layout");
	    	addImage("Pig", "Pig", makeMouse(), "#Pig");
	    }
	private void addImage(String imageName, String namespace, NestedPrefPanel editor, String url)
	{
//		super.addRecord(imageName, namespace, editor, url);
	}
	
	//--------------------------
	private NestedPrefPanel makeMouse() {
		NestedPrefPanel p = new NestedPrefPanel("Pig", detailPanel, root);
		p.setBorder(Borders.black );
		return p;
	}
	private NestedPrefPanel makeYeast() {
		NestedPrefPanel p = new NestedPrefPanel("Yeast", detailPanel, root);
		p.setBorder(Borders.ltGray);
		return p;
	}
	private NestedPrefPanel makePlant() {
		NestedPrefPanel p = new NestedPrefPanel("Plant", detailPanel, root);
		p.setBorder(Borders.blue);
		return p;
	}

}
