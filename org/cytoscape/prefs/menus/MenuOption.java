package org.cytoscape.prefs.menus;

public class MenuOption {
	private String name;
	private String menu;
	private Double gravity;
	private Double toolbarGravity;
	private String accelerator;
	private boolean inToolbar;
	private boolean inMenu;
	private boolean enabled;
	private String actionClass;
	
	public MenuOption(String n, Double g, String a, String v, String act, Double tbGrav, boolean inMenus, boolean inTools, boolean isEnabled)	{ 
		name = n; 
		gravity = g; 
		accelerator = a; 
		menu = v;	
		inMenu = inMenus;	
		inToolbar = inTools;	
		toolbarGravity = tbGrav;	
		actionClass = act;	
		menu = v;	
		enabled = isEnabled;
	}
	
	public void setName(String a) 			{ name = a;  } 
	public String getName() 				{ return name; } 
	public void setMenu(String v) 			{ menu = v;} 
	public String getMenu() 				{ return menu; } 
	public void setGravity(Double d) 		{ gravity = d;  } 
	public void setGravity(String g) 		{ gravity = Double.valueOf(g);  } 
	public Double getGravity() 				{ return gravity; } 
	public void setAccelerator(String v) 	{ accelerator = v;} 
	public String getAccelerator() 			{ return accelerator; } 

	public void setActionClass(String ac) 	{ actionClass = ac;  } 
	public String getActionClass() 			{ return actionClass; } 
	public void setToolbarGravity(Double d)	{ toolbarGravity = d;  } 
	public void setToolbarGravity(String g)	{ toolbarGravity = Double.valueOf(g);  } 
	public Double getToolbarGravity() 		{ return toolbarGravity; } 

	public void setEnabled(boolean b) 		{ enabled = b;  } 
	public boolean isEnabled() 				{ return enabled; } 
	public void setInMenus(boolean b) 		{ inMenu = b;  } 
	public boolean isInMenus() 				{ return inMenu; } 
	public void setInToolbar(boolean b) 	{ inToolbar = b;  } 
	public boolean isInToolbar() 			{ return inToolbar; } 
	
	}
