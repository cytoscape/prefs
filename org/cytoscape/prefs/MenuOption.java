package org.cytoscape.prefs;

public class MenuOption {
	private String name;
	private String menu;
	private Double gravity;
	private String accelerator;
	
	
	public MenuOption(String n, Double g, String a, String v)	{ 
		name = n; 
		gravity = g; 
		name = a; 
		menu = v;	
	}
	
	public void setName(String a) 			{ name = a;  } 
	public String getName() 				{ return name; } 
	public void setMenu(String v) 			{ menu = v;} 
	public String getMenu() 				{ return menu; } 
	public void setGravity(String g) 		{ gravity = Double.valueOf(g);  } 
	public Double getGravity() 				{ return gravity; } 
	public void setAccelerator(String v) 	{ accelerator = v;} 
	public String getAccelerator() 			{ return accelerator; } 
}
