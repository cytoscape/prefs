package org.lib;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;


public class CheckBoxCell extends DefaultCellEditor implements TableCellRenderer {
	private static final long serialVersionUID = 1L;
	private JCheckBox checkbox;
	private boolean selColor = false;
	public CheckBoxCell() {
		super(new JCheckBox());
		checkbox = (JCheckBox) getComponent();
	}

	@Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		boolean b = (Boolean) value;
		checkbox.setSelected(b);
		checkbox.setBackground((isSelected && isSelectionColoringEnabled()) ? table.getSelectionBackground() : table.getBackground());
		return checkbox;
	}
	
	public JCheckBox getCheckbox()						{	return checkbox;	}
	public void setSelectionColoringEnabled(boolean b)	{		selColor = b;	}
	public boolean isSelectionColoringEnabled()			{		return selColor;	}
}