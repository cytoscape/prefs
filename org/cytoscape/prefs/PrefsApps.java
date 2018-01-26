package org.cytoscape.prefs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;

import org.lib.HBox;
import org.lib.VBox;

import ch.randelshofer.quaqua.util.SequentialDispatcher;

public class PrefsApps extends MasterDetailPanel {

	protected PrefsApps(Cy3PreferencesRoot dlog) {
		super(dlog, "apps");
		setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
	}
     @Override public void initUI()
    {
        super.initUI();
        makeAppList();
        splitter.setDividerLocation(0.9);
    }
    String base = "http://manual.cytoscape.org/en/stable/Navigation_and_Layout.html";

    private void makeAppList()
    {
    	addApp("ID Mapper", "idmapper", makeIDMapperEditor(), "#idmapper");
    	addApp("CyBrowser", "Browser", makeBrowserEditor(), "#Browser");
    	addApp("WikiPathways", "wikipathways", makeWPEditor(), "#wikipathways");
   }

    protected double getListWidth() 			{			return 0.15;}
	protected boolean getListOnLeft() 		{			return false;	}
   private void addLayout(String s, String namespace, NestedPrefPanel editor)
    {
    	addApp(s, namespace, editor, null);
    }
    
    
    private void addApp(String layoutName, String namespace, NestedPrefPanel editor, String url)
    {
    	masterListModel.addRecord(layoutName);			// add the layout to the model
    	editor.setName(layoutName);
    	detailPanel.add(layoutName, editor);			// put the card in the deck
    	map.put(layoutName, editor);					// be able to recall the editor
    	namespaceMap.put(layoutName, namespace);		// be able to get the ns from layout name
    	if (url != null)
    		editor.add(makeExampleLink(url));
    }
    static Font FONT = new Font("Dialog", Font.ITALIC, 9);
   
    private void extractVlid()
    {
        int nRows = masterListModel.getRowCount();
        for (int i=0; i<nRows; i++)
        {
        	String layoutName = masterListModel.getLayout(i);
        	String nameSpace = namespaceMap.get(layoutName);
        	NestedPrefPanel editor = map.get(layoutName);
//        	Map<String, String> props = editor.extract();
//        	writeLayoutPropFile(props);
          }

    }
    HBox makeExampleLink(String url)
    {
    	HBox line = new HBox();
    	String fullUrl = base + url;
    	JLabel label = new JLabel("Link");
    	label.setFont(FONT);
    	label.setForeground(Color.BLUE);
//    	label.addMouseListener(ev -> { openUrl(fullUrl);	});
    	return line;
    }
    
    private NestedPrefPanel makeIDMapperEditor()
    {
		Box page = Box.createVerticalBox();
		NestedPrefPanel panel =  new NestedPrefPanel("IDMapper", page, root);
	  	page.add(panel.makeLabeledField("Server", "edgeAttribute", ""));
	  	page.add(panel.makeNumberField("Port", "spacing", 1, 1, 100));
	  	page.add(Box.createRigidArea(lineSpace));
	  	page.add(panel.makeCheckBoxLine("Report outages", "singlePartition", "tip"));
	  	page.add(Box.createRigidArea(lineSpace));
		return panel;
	}
    
    private NestedPrefPanel makeWPEditor()
    {
		Box page = Box.createVerticalBox();
		NestedPrefPanel panel =  new NestedPrefPanel("attribute-circle", page, root);
	  	page.add(panel.makeLabeledField("WikiPathways", "edgeAttribute", ""));
	  	page.add(panel.makeNumberField("ID of Pathway", "spacing", 1, 1, 100));
	  	page.add(Box.createRigidArea(lineSpace));
	  	page.add(panel.makeCheckBoxLine("Use species specific speficications", "singlePartition", "tip"));
	  	page.add(Box.createRigidArea(lineSpace));
		return panel;
	}
    
    private NestedPrefPanel makeBrowserEditor()
    {
		Box page = Box.createVerticalBox();
		NestedPrefPanel panel =  new NestedPrefPanel("attribute-circle", page, root);
	  	page.add(panel.makeLabeledField("Edge Attribute", "edgeAttribute", ""));
	  	page.add(panel.makeNumberField("sdfsd Size", "spacing", 1, 1, 100));
	  	page.add(Box.createRigidArea(lineSpace));
	  	page.add(panel.makeCheckBoxLine("Show Button Bar", "showButtons", "tip"));
	  	page.add(panel.makeCheckBoxLine("Show Search Bar", "showSearchBar", "tip"));
	  	page.add(Box.createRigidArea(lineSpace));
		return panel;
	}

}
