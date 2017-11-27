package org.cytoscape.prefs;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import org.lib.HBox;

public class PrefsBehavior extends AbstractPrefsPanel {

	protected PrefsBehavior(Cy3PreferencesRoot dlog) {
		super(dlog, "cytoscape3");
	}
    @Override public void initUI()
    {
        super.initUI();
        
		Box col1 = Box.createVerticalBox();
		col1.add(Box.createRigidArea(new Dimension(20, 40)));
		for (int i=0; i< displayNames.length; i++)
		{
			String prompt =  displayNames[i];
			String shortened = prompt.replaceAll(" ", "");
			String fld  = shortened.substring(0, 1).toLowerCase() + shortened.substring(1);
			col1.add(makeCheckBoxLine(prompt, fld,  tips[i]));
		}
		col1.add(Box.createVerticalGlue());
		
		Box col2 = Box.createVerticalBox();
		col2.add(Box.createRigidArea(new Dimension(20, 40)));
		for (int i=0; i< futureFeatures.length; i++)
		{
			String prompt =  futureFeatures[i];
			if (prompt.trim().isEmpty()) continue;
			String shortened = prompt.replaceAll(" ", "");
			String fld  = shortened.substring(0, 1).toLowerCase() + shortened.substring(1);
			col2.add(makeCheckBoxLine(prompt, fld,  tips[i]));
		}
		col2.add(Box.createVerticalGlue());
		
		add(new HBox(true, true, Box.createRigidArea(new Dimension(20, 40)),col1, col2));   
	}
	
	
	String[] displayNames = { "Show Network Provenance Hierarchy", "Show Node Edge Count",  "Show QuickStart As Startup",  
							"Canonicalize Names",  "Hide Welcome Screen",  "Maximize View On Create"  };
		
	String[] tips = { "Show Network Provenance Hierarchy", "Show Node Edge Count",  "Show QuickStart As Startup",  
							"Canonicalize Names",  "Hide Welcome Screen",  "Maximize View On Create"  };
	   @Override public void install(Map<String, String> props)
	    {
//		   Map<String, String> map = getPropertyMap("cytoscape3.props");
		   for (String fld : displayNames)
		   {
			  String shortened = fld.replaceAll(" ", "");
			  String squished = shortened.substring(0, 1).toLowerCase() + shortened.substring(1);
			  String key = "cytoscape3." + squished;
			  String value = props.get(key);
			  if (value != null)
			  {
			   JComponent comp = components.get(key);
			   if (comp == null) continue;	
			   if (comp instanceof JCheckBox)
			   {
				   JCheckBox ck = (JCheckBox) comp;
				   boolean checked = value.toLowerCase().startsWith("t");
				   ck.setSelected(checked);
			   }
			   if (comp instanceof JTextField)
				   ((JTextField) comp).setText(value);
			  }
		   }
	    }
	   
	   @Override public Map<String,String> extract()
	   {
		    Map<String,String> map = extractFrom(displayNames);
		    map.putAll(extractFrom(futureFeatures));
		    return map;
	   }
	   
	   
	   private  Map<String,String> extractFrom(String[] names)
	   {
		   Map<String,String> attributes = new HashMap<String,String>();
		   for (String fld : names)
		   {
			   if (fld.isEmpty()) continue;
			   String shortened = fld.replaceAll(" ", "");
			   String squished = shortened.substring(0, 1).toLowerCase() + shortened.substring(1);
				JComponent comp = components.get("cytoscape3." + squished);
			   if (comp == null) continue;	
			   if (comp instanceof JCheckBox)
			   {
				   JCheckBox ck = (JCheckBox) comp;
				   attributes.put(squished, boolState(ck));
			   }
		   }
//		   overwriteProperties("cytoscape3.props", attributes);
		   return attributes;
	    }	 
		
	   
	   
	   
		String[] futureFeatures = { "Classic modifier key selection", 
				"Click in white space deselects all",  
				"",  
				"",  "",  ""  };
			
		String[] futureFeatureTips = { "", "",  "",  
								"",  "",  ""  };

	   
	   
	   
	   String boolState(JCheckBox ck )
	   {
		   return ck.isSelected() ? "true" : "false";
	   }
}
