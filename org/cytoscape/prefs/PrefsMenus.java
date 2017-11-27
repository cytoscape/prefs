package org.cytoscape.prefs;

import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumn;

import org.cytoscape.prefs.menus.MenuEditorPanel;

public class PrefsMenus extends AbstractPrefsPanel {

	Cy3PreferencesRoot root;
	MenuEditorPanel editorPanel;
	
	
	protected PrefsMenus(Cy3PreferencesRoot dlog) {
		super(dlog, "menu");
		root = dlog;
	}
	   @Override public void initUI()
	    {
	        super.initUI();
			Box page = Box.createVerticalBox();
		    page.add(new JLabel("Menu Definitions"));
		    page.add(new JLabel("Table with columns for accelearator, preferred menu, gravity etc."));
		    page.add(new JLabel("Select an action to edit its hot keys or position."));
		    page.add(new JLabel("[Excess columns need to be eliminated here, meta-pressed needs iconic form]"));
//		    page.add(makeTable());
		    editorPanel = new MenuEditorPanel();
		    page.add(editorPanel);
		    add(page);   
//		    setBorder(new EmptyBorder(5, 22, 10, 30));
		}
      
    @Override public void install(Map<String, String> p)
    {
    	super.install(p);
    	List<String> data = readPropertyFile("menuActions.txt");  // TODO put in a RESOURCE
    	editorPanel.install(data);
    }

//    List<String> filterStringBy(Map<String, String> inMap, String prefix)
//    {
//    	List<String> data = new ArrayList<String>();
//    	for (String s : inMap.keySet())
//    		if (s.startsWith(prefix))
//    			data.add(s.substring(prefix.length()) + "=" + inMap.get(s));
//    	return data;
//    }
//    private MenuOptionsTable table;
//	
//    private Component makeTable() {
//		table = new MenuOptionsTable();
//		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
//		table.setFillsViewportHeight(true);
//		JScrollPane scroller = new JScrollPane(table);
//		return scroller;
//	}
//
}
