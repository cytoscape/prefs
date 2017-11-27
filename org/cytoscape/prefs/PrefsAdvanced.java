package org.cytoscape.prefs;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.lib.AttrVal;
import org.lib.NamespaceKeyValueTable;

public class PrefsAdvanced extends AbstractPrefsPanel {

	protected PrefsAdvanced(Cy3PreferencesRoot dlog) {
		super(dlog, "literal");
	}
	
    @Override public void initUI()
    {
        super.initUI();
		Box page = Box.createVerticalBox();
		add(page);   
 	    page.add(new JLabel("Namespace Attribute Value View"));
	    page.add(new JLabel(""));
	    page.add(makeTable());
		add(page);   
	}
    
    private NamespaceKeyValueTable table;
	
    private Component makeTable() {
        Prefs data = root.getPrefs();
//		TableModel model = makePrefsTableModel(data);
		table = new NamespaceKeyValueTable();
		TableColumn col = table.getColumnModel().getColumn(table.getNColumns()-1);
		col.setPreferredWidth(260);
		table.setPreferredScrollableViewportSize(new Dimension(500, 270));
		
		table.setFillsViewportHeight(true);
		JScrollPane scroller = new JScrollPane(table);
		return scroller;
	}
    

	@Override public void install(Map<String, String> settings)
	{
		table.install(settings);
	}

}
