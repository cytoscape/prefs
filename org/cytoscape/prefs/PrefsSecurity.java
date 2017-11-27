package org.cytoscape.prefs;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.lib.BoxComponent;
import org.lib.BoxSubPanel;
import org.lib.GuiFactory;
import org.lib.HBox;

public class PrefsSecurity extends AbstractPrefsPanel {

	protected PrefsSecurity(Cy3PreferencesRoot dlog) {
		super(dlog, "privacy");
//		namespace = "proxy";
	}
    String introTxt = "Your network administration may require a proxy server,\nan intermediate computer to filter Internet traffic.";

    @Override public void initUI()
    {
        super.initUI();
		setBorder(BorderFactory.createEmptyBorder(20,32,0,0));
		Box page = Box.createVerticalBox();
	    page.add(new HBox(true, true, new JLabel("Security")));
	    page.add(Box.createRigidArea(new Dimension(30,15)));
	    
		JTextArea intro = new JTextArea(introTxt );
		intro.setOpaque(false);
	    page.add(new HBox(true, true, intro));
	    page.add(Box.createRigidArea(new Dimension(30,15)));

	    page.add(new HBox(true, true, makeProxyPanel()));
	    page.add(Box.createRigidArea(new Dimension(30,15)));
	    page.add(new HBox(true, true, new JLabel("Privacy")));
	    page.add(Box.createRigidArea(new Dimension(30,15)));
	    page.add(makeCheckBoxLine("Check Version Info & News on Startup", "privacy.check.version", "tip"));
	  	page.add(makeCheckBoxLine("Check for App Store Updates", "privacy.check.appstore", "tip"));
	  	page.add(makeCheckBoxLine("Send telemetry reports to Cytoscape Team", "privacy.telemetry", "tip"));
	  	page.add(makeCheckBoxLine("Provide contact info", "user.contact", "provide an email address where we can reach you"));
	  	page.add(makeLabeledField("Email", "contact.email", "an address or any identifier goes here"));

//	  	JComponent box = components.get("contact.email");
//	  	if (box instanceof JPanel)
//	  		((JPanel)box).setEnabled(false);

		add(page);   
	}
    
	String[] proxyTypes = { "direct", "http", "socks"};

    private Component makeProxyPanel() {
		Box page = Box.createVerticalBox();
		page.add(makeRepositoryPanel());
		return page;
	}
	private JTextField fHost, fPort, fUsername; // proxy settings
	protected JPasswordField fPassword;

	static String[] strs = new String[] {"Host:", "Port:", "User Name:", "Password:"};
	BoxSubPanel makeRepositoryPanel()
	{
		BoxSubPanel panel = new BoxSubPanel("Proxy Settings", false);
		GuiFactory.setSizes(panel, new Dimension(550,80));
		fHost = new JTextField(25);
        fPort = new JTextField(5);
        fUsername = new JTextField(15);
        fPassword = new JPasswordField(15);
        
        components.put("proxy.host", fHost);
        components.put("proxy.port", fPort);
        components.put("proxy.username", fUsername);
        components.put("proxy.password", fPassword);
        
        JLabel[] labels = new JLabel[4];
        for (int i=0; i<4; i++)
        {
        	labels[i] = new JLabel(strs[i]);	
	        GuiFactory.setSizes(labels[i], new Dimension(89, 27));
        }
        Box lin1 = Box.createHorizontalBox();
        Box lin2 = Box.createHorizontalBox();
        lin1.add(new BoxComponent(labels[0], fHost));		
        lin1.add(new BoxComponent(labels[1], fPort));
        lin2.add(new BoxComponent(labels[2], fUsername));
        lin2.add(new BoxComponent(labels[3], fPassword));
        panel.add(lin1);
        panel.add(lin2);
		return panel;
	}
	
	// TODO
//	   @Override public void install(Map<String, String> props)
//	    {
////		   Map<String, String> map = getPropertyMap("cytoscape3.props");
//		   for (String fld : props.keySet())
//		   {
//			  String shortened = fld.replaceAll(" ", "");
//			  String squished = shortened.substring(0, 1).toLowerCase() + shortened.substring(1);
//			  String key = "cytoscape3." + squished;
//			  String value = props.get(key);
//			  if (value != null)
//			  {
//			   JComponent comp = components.get(key);
//			   if (comp == null) continue;	
//			   if (comp instanceof JCheckBox)
//			   {
//				   JCheckBox ck = (JCheckBox) comp;
//				   boolean checked = value.toLowerCase().startsWith("t");
//				   ck.setSelected(checked);
//			   }
//			   if (comp instanceof JTextField)
//				   ((JTextField) comp).setText(value);
//			  }
//		   }
//	    }
//	   
//	   @Override public Map<String,String> extract()
//	    {
//		   Map<String,String> attributes = new HashMap<String,String>();
//		   for (String fld : displayNames)
//		   {
//			   String shortened = fld.replaceAll(" ", "");
//			   String squished = shortened.substring(0, 1).toLowerCase() + shortened.substring(1);
//				JComponent comp = components.get("cytoscape3." + squished);
//			   if (comp == null) continue;	
//			   if (comp instanceof JCheckBox)
//			   {
//				   JCheckBox ck = (JCheckBox) comp;
//				   attributes.put(squished, boolState(ck));
//			   }
//		   }
//		   overwriteProperties("cytoscape3.props", attributes);
//		   return attributes;
//	    }	 
//	   


}
