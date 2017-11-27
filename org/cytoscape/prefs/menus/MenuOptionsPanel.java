package org.cytoscape.prefs.menus;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.lib.HBox;
import org.lib.VBox;

public class MenuOptionsPanel extends JPanel {
		
	JCheckBox inMenu = new JCheckBox("Show in Menu Bar") ;
	JCheckBox inToolbar = new JCheckBox("Show in Tool Bar");
	JTextField accelerator = new JTextField("Accelerator");
	JLabel name = new JLabel("Name");

	public MenuOptionsPanel()
	{
		VBox page = new VBox();
		page.add(new HBox(true, true, name));
		page.add(new HBox(true, true, accelerator));
		page.add(new HBox(true, true, inMenu));
		page.add(new HBox(true, true, inToolbar));
		add(page);
	}

		public void installRow(MenuOption record) {
			
			inMenu.setSelected(record.isInMenus());
			inToolbar.setSelected(record.isInToolbar());
			accelerator.setText(record.getAccelerator());
			name.setText(record.getName());
		}
	}
