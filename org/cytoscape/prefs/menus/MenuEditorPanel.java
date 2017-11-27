package org.cytoscape.prefs.menus;

import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableColumn;

import org.lib.Borders;

public class MenuEditorPanel extends JPanel {

	MenuOptionsPanel editor;
	MenuOptionsTable table = new MenuOptionsTable();
	JSplitPane splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	
	public MenuEditorPanel()
	{
		super();
		editor = new MenuOptionsPanel();
//		editor.setBorder(Borders.magenta);
		table = new MenuOptionsTable();
//		table.setBorder(Borders.cyan);
		JScrollPane scroller = new JScrollPane(table);
		splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scroller, editor);
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(ev -> { listSelectionChanged(ev); } ); 
		add(splitter);
	}
	
	private void listSelectionChanged(ListSelectionEvent ev) {
		if (ev.getValueIsAdjusting()) return;
		int row = ev.getFirstIndex();
		MenuOption record = table.getRow(row);
		editor.installRow(record);
	}

	private boolean getListOnLeft() {		return true;	}
	private double getListWidth() {		return 0.75;	}

	public void install(List<String> data) {
		
//		TableColumn col = table.getColumnModel().getColumn(0);
//		col.setPreferredWidth(160);
		table.install(data);
	}

	
}

