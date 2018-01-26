package org.cytoscape.prefs;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumn;

import org.lib.NamespaceKeyValueTable;

public class PrefsMenus extends AbstractPrefsPanel {

	protected PrefsMenus(Cy3PreferencesRoot dlog) {
		super(dlog, "menu");
	}
	   @Override public void initUI()
	    {
	        super.initUI();
			Box page = Box.createVerticalBox();
		    page.add(new JLabel("Menu Definitions"));
		    page.add(new JLabel("Table with columns for accelearator, preferred menu, gravity etc."));
		    page.add(makeTable());
		    add(page);   
		    setBorder(new EmptyBorder(5, 22, 0, 30));
		}
      
    @Override public void install(Map<String, String> p)
    {
    	super.install(p);
    	Map<String, String> data = getPropertyMap("");
    	table.install(data);
		TableColumn col = table.getColumnModel().getColumn(2);
		col.setPreferredWidth(260);
    }

//    List<String> filterStringBy(Map<String, String> inMap, String prefix)
//    {
//    	List<String> data = new ArrayList<String>();
//    	for (String s : inMap.keySet())
//    		if (s.startsWith(prefix))
//    			data.add(s.substring(prefix.length()) + "=" + inMap.get(s));
//    	return data;
//    }
    private MenuOptionsTable table;
	
    private Component makeTable() {
		table = new MenuOptionsTable();
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);
		JScrollPane scroller = new JScrollPane(table);
		return scroller;
	}

}
