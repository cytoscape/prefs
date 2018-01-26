package org.cytoscape.prefs;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;


public class MenuOptionsTable extends JTable {
	public MenuOptionsTable()
	{
		super();
		MenuOptionsTableModel model = new MenuOptionsTableModel();
		setModel(model);
		sorter = new TableRowSorter<MenuOptionsTableModel>(model);
		setRowSorter(sorter);	
	    TableCellRenderer renderer = new EvenOddRenderer();
	    setDefaultRenderer(Object.class, renderer);
	}
	String[] columnNames = {"Name", "Parent Menu", "Gravity", "Accelerator"};
	TableRowSorter<MenuOptionsTableModel> sorter;
	
	List<MenuOption> menuOptionList = new ArrayList<MenuOption>();
		public void install(Map<String, String> settings)
	{
		String value, namespace="";
		TableModel model =  getModel();
		for (String key : settings.keySet())
		{
			value = settings.get(key);
			int idx = key.indexOf('.');
			if (idx > 0)
			{
				namespace = key.substring(0, idx);
				if ("layout".equals(namespace))
				{
					idx = key.indexOf('.', idx+1);
					namespace = key.substring(0, idx);
				}	
				key = key.substring(idx+1);
			}
			MenuOption row = new MenuOption(namespace, 1.0, key, value );
			if (model instanceof MenuOptionsTableModel)
			{		
				MenuOptionsTableModel m = (MenuOptionsTableModel) getModel();
				m.addRow(row);
			}
		}
	}
	public int getNColumns() {		return 4;	}

	//=========================
	

	class MenuOptionsTableModel extends AbstractTableModel {
	
		public List<MenuOption> getMenuOptionList() 	{ 	return menuOptionList;	}
	    public int getColumnCount() 			{   return columnNames.length;    }
	    public int getRowCount() 				{   return menuOptionList == null ? 0 : menuOptionList.size();    }
	    public String getColumnName(int col) 	{   return columnNames[col];    }
	    public void addRow(MenuOption newRow)		{ 	menuOptionList.add(newRow); }
	    public Object getValueAt(int row, int col) 
	    {      
	    	if (row < getRowCount()) return "";
	    	MenuOption av =  menuOptionList.get(row);  
	    	if (col == 0) return av.getName();
	    	 if (col == 1) return av.getMenu();
	    	 if (col == 2) return "" + av.getGravity();
	    	 return av.getAccelerator();
	    	 
	    }
	    public Class getColumnClass(int c) {        return getValueAt(0, c).getClass();    }
	
	    /*
	     * Don't need to implement this method unless your table's editable.
	     */
	    public boolean isCellEditable(int row, int col) { 	return col > 0;        }
	 
	    /*
	     * Don't need to implement this method unless your table's data can change.
	     */
	    public void setValueAt(String value, int row, int col) {
	    	MenuOption av =  menuOptionList.get(row);  
	    	if (col == 0)  av.setName(value);
	    	 if (col == 1)  av.setMenu(value);
	    	 if (col == 1)  av.setGravity(value);
	    	 if (col == 2)  av.setAccelerator(value);
//	    	if (col == 0) av.setAttribute(value);
//	    	else av.setValue(value);
	        fireTableCellUpdated(row, col);
	    }
	}

	  public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();
	    public final static Color offwhite     = new Color(250, 250, 250);

class EvenOddRenderer implements TableCellRenderer {


  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {
    Component renderer = DEFAULT_RENDERER.getTableCellRendererComponent(
        table, value, isSelected, hasFocus, row, column);
    ((JLabel) renderer).setOpaque(true);
    Color foreground, background;
    if (isSelected) {
      foreground = Color.LIGHT_GRAY;
      background = Color.black;
    } else {
      if (row % 2 == 0) {
    	  background = Color.white;
      } else {
        background = offwhite;
      }
    }
    renderer.setBackground(background);
    return renderer;
  }

}
}
