package org.cytoscape.prefs.menus;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
//import javax.swing.event.ListSelectionEvent;
//import javax.swing.event.ListSelectionListener;		use this for master detail interface
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;




public class MenuOptionsTable extends JTable {
	private static final long serialVersionUID = 1L;

	public MenuOptionsTable()
	{
		super();
		MenuOptionsTableModel model = new MenuOptionsTableModel();
		setModel(model);
		sorter = new TableRowSorter<MenuOptionsTableModel>(model);
		setRowSorter(sorter);	
	    TableCellRenderer renderer = new EvenOddRenderer();
	    setDefaultRenderer(Object.class, renderer);

	    boolean alwaysEdit = true;			// put editor in a side bar
	    if (alwaysEdit)
	    getSelectionModel().addListSelectionListener(listSelList);
	    boolean useDoubleClickToEdit = false;
	    if (useDoubleClickToEdit)			// put editor in a modal dialog
	    	addMouseListener(doubleClickHandler);  
	    
	   }
  
	public MenuOption getRow(int row) {		return getMOModel().getRow(row);	}	
	   //====== example of using ListSelectionListener for click/arrow handling

	private ListSelectionListener listSelList = new ListSelectionListener() {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting()) return;
			System.out.println("set the editor's current row to " + 
					getSelectionModel().getMinSelectionIndex());
		}
    };

    //=======  example of using MouseAdapter for double click handling

    
    private     MouseAdapter doubleClickHandler =  new MouseAdapter() {
        public void mousePressed(MouseEvent mouseEvent) {
            JTable table =(JTable) mouseEvent.getSource();
            Point point = mouseEvent.getPoint();
            int row = table.rowAtPoint(point);
            if (mouseEvent.getClickCount() == 2) {
                showDialog(row); 
            }
        }

		private void showDialog(int rowIndex) {
			
			System.out.println("show the dialog");
//			MenuOptionsTableModel model = (MenuOptionsTableModel) getModel();
//			MenuOption option = model.getRow(rowIndex);
		}
    };
    
    //===================================
	String[] columnNames = {"Parent Menu", "Name"};  //, "inMenuBar", , "Accelerator", "Parent Menu", "Gravity", "inToolBar", "Toolbar Gravity", "Enabled", "Action"
	TableRowSorter<MenuOptionsTableModel> sorter;
	
	List<MenuOption> menuOptionList = new ArrayList<MenuOption>();
	final String TAB = "\t";
	
	MenuOptionsTableModel getMOModel()	{ return (MenuOptionsTableModel) super.getModel(); }
	
	public void install(List<String> lines)
	{
		if (!(getModel() instanceof MenuOptionsTableModel))
		 return;
		
		MenuOptionsTableModel m = getMOModel();
		for (String line : lines)
		{
			String[] tokens = line.split(TAB);
			if (tokens.length != 9) continue;
			
			String name = tokens[0];
			String accel = tokens[1];
			boolean inMenu = tokens[2].startsWith("T");
			String menu = tokens[3];
			Double menuGrav = Double.parseDouble(tokens[4]);
			boolean inTools = tokens[5].startsWith("T");;
			Double toolGrav = Double.parseDouble(tokens[6]);
			boolean enabled = tokens[7].startsWith("T");
			String action = tokens[8];
			MenuOption row = new MenuOption(name, menuGrav, accel, menu, action, toolGrav, inMenu, inTools, enabled );
			m.addRow(row);
		}
	}

	//=========================
	

	class MenuOptionsTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;

		public List<MenuOption> getMenuOptionList() { 	return menuOptionList;	}
	    public int getColumnCount() 				{   return columnNames.length;    }
	    public int getRowCount() 					{   return menuOptionList == null ? 0 : menuOptionList.size();    }
	    public String getColumnName(int col) 		{   return columnNames[col];    }
	    public void addRow(MenuOption newRow)		{ 	menuOptionList.add(newRow); }
	    public MenuOption getRow(int i) 			{   return menuOptionList.get(i);      }

	    public Object getValueAt(int row, int col) 
	    {      
	    	if (row > getRowCount()) return "";
	    	MenuOption av =  menuOptionList.get(row);  
	    	 if (col == 0) return av.getMenu();
	    	if (col == 1) return av.getName();
	    	 if (col == 2) return av.getAccelerator();
	    	 if (col == 3) return av.isInMenus() ? "T" : "F";
	    	 if (col == 4) return "" + av.getGravity();
	    	 if (col == 5) return av.isInToolbar() ? "T" : "F";
	    	 if (col == 6) return "" + av.getToolbarGravity();
	    	 if (col == 7) return av.isEnabled()  ? "T" : "F";
	    	 if (col == 8) return av.getActionClass();
	    	 return av.getAccelerator();
	    	 
	    }
	    public Class<?> getColumnClass(int c) {        return getValueAt(0, c).getClass();    }
	
	    /*
	     * Don't need to implement this method unless your table's editable.
	     */
//	    public boolean isCellEditable(int row, int col) { 	return col > 0;        }
	 
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
    Color background;
    if (isSelected) {
//      foreground = Color.LIGHT_GRAY;
      background = Color.black;
    } else  background = (row % 2 == 0) ? Color.white : offwhite;
    
    renderer.setBackground(background);
    return renderer;
  }

}


}
