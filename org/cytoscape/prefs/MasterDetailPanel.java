package org.cytoscape.prefs;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

public class MasterDetailPanel extends AbstractPrefsPanel {
    protected JSplitPane splitter;
    protected JPanel detailPanel;
    protected MasterListTableModel masterListModel;
    protected JTable masterTable;
    
    Map<String, NestedPrefPanel> map = new HashMap<String, NestedPrefPanel>();
    Map<String, String> namespaceMap = new HashMap<String, String>();
    
    private void listSelectionChanged(ListSelectionEvent ev)
    {
    	int idx = masterTable.getSelectedRow();
    	String name = "" + masterListModel.getValueAt(idx, 0);
    	((CardLayout)detailPanel.getLayout()).show(detailPanel, name);
    }
    Dimension lineSpace = new Dimension(20,10);
    

	protected MasterDetailPanel(Cy3PreferencesRoot container, String nameSpace) {
		super(container, nameSpace);
    }
	private static final long serialVersionUID = 1L;


	public void  initUI() {
    	super.initUI();
		detailPanel = new JPanel(new CardLayout());
//		detailPanel.setBorder(Borders.cyan);
		masterListModel = new MasterListTableModel();
    	masterTable = new JTable(masterListModel);
        masterTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	masterTable.getSelectionModel().addListSelectionListener(ev -> { listSelectionChanged(ev); } ); 
    	Component left = getListOnLeft() ? masterTable : detailPanel;
    	Component right = getListOnLeft() ? detailPanel : masterTable;
    	masterTable.setMinimumSize(new Dimension(200,200));
    	splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left ,right);
    	splitter.setOneTouchExpandable(true);    
    	splitter.setDividerLocation(getListOnLeft() ? getListWidth() : 1- getListWidth());  
    	add(splitter);
	}
	protected double getListWidth() 		{		return 0.4; }
	protected boolean getListOnLeft() {			return true;	}
		//---------------------------------------------------------------------------------------------------------
    @Override public void install(Map<String, String> props)
    {
    	for (String name : map.keySet())
    	{
    		NestedPrefPanel panel = map.get(name);
    		panel.install(props);
    	}
     }
    
    @Override public Map<String, String> extract()
    {
    	Map<String, String> properties = getPropertyMap();
    	for (String name : map.keySet())
    	{
    		NestedPrefPanel panel = map.get(name);
    		properties.putAll(panel.extract());
    	}
    	return properties;
    }

}
