package org.cytoscape.prefs;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.AbstractBorder;
import javax.swing.border.TitledBorder;

import org.lib.Borders;
import org.lib.BoxSubPanel;
import org.lib.FileUtil;
import org.lib.FontManager;
import org.lib.GuiFactory;
import org.lib.HBox;
import org.lib.RangedIntegerTextField;

abstract public class AbstractPrefsPanel extends BoxSubPanel
{
	protected Cy3PreferencesRoot root;
//	private SElement fPrefsElement;
	protected Dimension dims = new Dimension(515, 400);
	protected String namespace;
	
	protected AbstractPrefsPanel(Cy3PreferencesRoot container, String nameSpace)
	{
		super();
		root = container;
		namespace = nameSpace;
	}

	public void initUI()	{	GuiFactory.setSizes(this, dims); }
	public void adjust()	{ }
//	public void install()	{	 }
//	public void extract(Prefs prefs)	{	 }
	

		
public static List<String> readPropertyFile(String fname) {
		List<String> lines = null;
		File propsFile = getPropertyFile(fname);
		try {
			lines = Files.readAllLines(propsFile.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}
	//---------------------------------------------------------------------------------------------
	public static File getPropertyFile(String fname)	
	{
		return new File(System.getProperty("user.home") + "/CytoscapeConfiguration/" + fname);
	}
	
	public static Map<String, String> getPropertyMap(String fname)	
	{
		return FileUtil.readMap(getPropertyFile(fname));
	}
	
	
	//---------------------------------------------------------------------------------------------
	
	public Map<String, String> getPropertyMap()	{ return root.getPropertyMap();	}
//	public Map<String, JComponent> getComponentMap()	{ return root.getComponentMap();	}
//	public void addComponentToMap(String s, JComponent com)	{ getComponentMap().put(s, com);  }
	public Dimension getPanelSize()		{ return dims;	}
    Dimension leading = new Dimension(12,12);
	//---------------------------------
    public void overwriteProperties(String fName, Map<String, String> attributes)
    {
    	Map<String, String> extant = getPropertyMap(fName);
    	for (String key : attributes.keySet())
    		extant.put(key,  attributes.get(key));
    	
    	try
    	{
    		FileUtil.writeMap(extant, getPropertyFile(fName));
    	}
    	catch (Exception e)
    	{
    		System.err.println("Cannot write property file: " + fName);
    		e.printStackTrace();
    	}
    }
	//---------------------------------------------------------------------------------------------
	public HBox makeLabeledField(String s, String propertyName, String deflt)
	{
		JLabel label = new JLabel(s);
		JLabel spacer = new JLabel("    " );
		JTextField field = new JTextField(deflt);
		HBox line = new HBox(true, true, label, spacer, field);
		components.put(namespace + "." + propertyName, field);
		setSizes(field, 320,30);
        return line;
	}
	//---------------------------------------------------------------------------------------------
	FocusListener focus = new FocusListener() {
	      public void focusGained(FocusEvent e) {
	        displayMessage("Focus gained", e);
	      }

	      public void focusLost(FocusEvent e) {
	        displayMessage("Focus lost", e);
	      }

	      void displayMessage(String prefix, FocusEvent e) {
	        System.out.println(prefix
	            + (e.isTemporary() ? " (temporary):" : ":")
	            + e.getComponent().getClass().getName()
	            + "; Opposite component: "
	            + (e.getOppositeComponent() != null ? e.getOppositeComponent().getClass().getName()
	                : "null"));
	      }
	    };
	    
	public  HBox makeNumberSliderRow(String s, String propName, int min, int max, int val) {
		
		JLabel label = new JLabel(s);
		setSizes(label, 150, 30);
        JSlider slider = new JSlider(JSlider.HORIZONTAL,min, max, val);
        slider.setName(propName);
//		label.setBorder(BorderFactory.createLineBorder(Color.green));
		RangedIntegerTextField fld = new RangedIntegerTextField(val, min, max);
		fld.addFocusListener(new FocusListener() {
		      public void focusGained(FocusEvent e) {      }
		      public void focusLost(FocusEvent e) 
		      {   
		    	  int val;
		    	  String s = fld.getText();
		    	 try
		    	 {
		    		 val = Integer.parseInt(s);
		    	 }
		    	 catch (Exception ex)
		    	 {
		    		 return;
		    	 }
		    	 System.out.println("setting slider " + slider.getName() + " to " + val);
		    	 slider.setValue(val);
		    }
		});
		setSizes(fld, 80,30);
        slider.addChangeListener(e -> { fld.setValue(slider.getValue());} );
        slider.setMajorTickSpacing(max - min);
        slider.setPaintLabels(true);
        slider.setMaximumSize(new Dimension(200, 36));
        slider.setValue(val);
    	fld.setHorizontalAlignment(SwingConstants.RIGHT);
		components.put(namespace + "." + propName, slider);
		Component spacer = Box.createHorizontalStrut(40);
		HBox line = new HBox(spacer, label, fld, slider);
 		return line;
	}
	//---------------------------------------------------------------------------------------------
	public JSlider makeSlider(String string) {
		JSlider slider = new JSlider(JSlider.VERTICAL,0, 50, 25);
		return slider;
	}
//	//---------------------------------------------------------------------------------------------
//	public HBox makeLabelledCombo(String s, String deflt)
//	{
//		JLabel label = new JLabel(s);
//		JLabel spacer = new JLabel("    " );
//		JComboBox<String> combo = new JComboBox<String>();
//		HBox line = new HBox();
//		line.add(label, spacer, combo);
//        return line;
//	}
	//---------------------------------------------------------------------------------------------
	public AbstractBorder makeSubpanelBorder(String name)
	{
		TitledBorder border = new TitledBorder(name);
		border.setTitleColor(Color.black);
		border.setTitleFont(FontManager.getRelativeFont(2));
		return border;
	}
	
	//---------------------------------
	//added to play with prefs antialiaspanel border testing
	protected AbstractBorder makeSubpanelBorder2(String name)
	{
		TitledBorder border = new TitledBorder(name);
		border.setTitleColor(Color.blue);
		border.setTitleFont(new Font("Dialog", Font.BOLD, 10));
		return border;
	}
	
	//---------------------------------
	protected RangedIntegerTextField numberField(int value, int max)
	{
		RangedIntegerTextField fld = new RangedIntegerTextField(value, 0, max);
    	setSizes(fld, 50,30 );
    	fld.setHorizontalAlignment(SwingConstants.RIGHT);
    	return fld;
	}
	
	//---------------------------------
	
	protected Box makeNumberField(String prompt, String propname, int value, int min, int max)
    {
    	Box line = Box.createHorizontalBox();
    	JLabel label = new JLabel(prompt);
    	line.add(label);
    	setSizes(label, 180,25);

    	RangedIntegerTextField fld = new RangedIntegerTextField(min, max);  
    	fld.setValue(value);
    	fld.setMaximumSize(new Dimension(80,30) );
    	line.add(fld);
    	fld.setHorizontalAlignment(SwingConstants.RIGHT);
  		line.setMaximumSize(new Dimension(500,25));
		components.put(namespace + "." + propname, fld);
  		return line;
     }
	//---------------------------------
	protected HBox makeNumberFieldShort(String prompt, int promptLen, String property, int value, int min, int max)
    {
    	JLabel label = new JLabel(prompt);
//    	line.add(label);
    	setSizes(label, promptLen, 25);

    	RangedIntegerTextField fld = new RangedIntegerTextField(min, max);  
    	setSizes(fld, 60, 30);
    	fld.setFont(smallFont);
    	fld.setHorizontalAlignment(SwingConstants.RIGHT);
		components.put(namespace+"." + property, fld);
		HBox line = new HBox(false, true, label, fld);
  		return line;
      }

	//---------------------------------
	protected Component makeLabeledPassworldField(String prompt, String fld) {
	  		Box line = Box.createHorizontalBox();
	  		JLabel lab = new JLabel(prompt);
	  		setSizes(lab, 150, 30);
	  		line.add(lab);
	  		JPasswordField choices = new JPasswordField();
	  		line.add(choices);
	  		line.setMaximumSize(new Dimension(500,30));
	  		return line;
	  	}
	//---------------------------------
		
//	protected JCheckBox makeCheckboxFromField(String prompt, String namespace, String fld)
//	{
//		JCheckBox box = new JCheckBox(prompt);
//		root.addFieldComponent(fld, box);
//		return box;
//	}
	//---------------------------------

	protected void setSizes(JComponent panel, int w, int h)
    {
    	Dimension d = new Dimension(w, h);
    	panel.setPreferredSize(d);
    	panel.setMaximumSize(d);
    	panel.setMinimumSize(d);
    	panel.setSize(d);
    }
	//---------------------------------
	protected Component makeLabeledCombo(String prompt, String propName, String[] options) {
			return makeLabeledCombo( prompt, propName, options, 30, 150, 130);
	}
	//---------------------------------
	protected Component makeLabeledCombo(String prompt, String propName, String[] options, int lineHeight, int labelWidth, int comboWidth)
	{
		Box line = Box.createHorizontalBox();
		JLabel lab = new JLabel(prompt);
		setSizes(lab, labelWidth, lineHeight);
		line.add(lab);
		JComboBox<String> choices = new JComboBox(options);
		line.add(choices);
		setSizes(choices, comboWidth, lineHeight);
		choices.setSelectedIndex(0);
		components.put(namespace+"." + propName, choices);
		return line;
	}

	//---------------------------------
	protected HBox prepartitionOption(String namespace)
    {
    	return makeCheckBoxLine(namespace, "Partition graph before layout", "singlePartition", defaultFont, "tip");
    }
//	//---------------------------------
//	protected JLabel makeLabelledCombo(String s, int x, int y, int w, int h, JComponent inParent)
//	{
//		JLabel label = new JLabel(s);
//		label.setBounds(new Rectangle(x, y, w, h));
//		inParent.add(label, null);
//        return label;
//	}
	//---------------------------------------------------------------------------------------------------------
	protected HBox makeCheckBoxLine(String namespace, String name, String propName, Font f, String tip)
    {
		HBox box = new HBox();
		JCheckBox cb = new JCheckBox(name);
    	cb.setToolTipText(tip);
    	cb.setFont(f);
    	box.add(cb);
		box.add(Box.createHorizontalGlue());
		components.put(namespace+"." + propName, cb);
    	return box;
    }
	
	Font defaultFont = new Font(Font.DIALOG, Font.PLAIN, 12);
	//---------------------------------------------------------------------------------------------------------
	protected HBox makeCheckBoxLine(String name, String propName, String tip)
	{
		return makeCheckBoxLine(namespace, name, propName, defaultFont , tip);
	}

//---------------------------------------------------------------------------------------------------------
//	protected JCheckBox makeCheckBox(String name, String tip)
//	{
//		JCheckBox cb = new JCheckBox(name);
//		cb.setToolTipText(tip);
//		return cb;
//	}
	//------------------------------------------------------------------
	static Font smallFont = new Font("Dialog", Font.BOLD, 10);
	    
	private String scoped(String method)
	{
		return namespace + "." + method;
	}
	//---------------------------------
	protected HBox makeSpacingBox() {
		Box page = Box.createHorizontalBox();
		HBox line = new HBox();
		JLabel hLabel = new JLabel("Horizontal");
		JLabel vLabel = new JLabel("Vertical");
		hLabel.setFont(smallFont);
		vLabel.setFont(smallFont);

		line.add(new JLabel("Spacing"), Box.createRigidArea(new Dimension(60, 5)), hLabel,
				makeIntegerBox(scoped("nodeHorizontalSpacing")), vLabel,
				makeIntegerBox(scoped("nodeVerticalSpacing")), Box.createRigidArea(new Dimension(45, 5)),
				Box.createHorizontalGlue());
		// page.add(line);
		// page.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		// setSizes(page, 300, 36);
		return line;

	}

	protected HBox makeMarginBox()
	    {
			HBox line = new HBox();
			JLabel topLabel = new JLabel("Top");
			JLabel leftLabel = new JLabel("Left");
			JLabel rightLabel = new JLabel("Right");
			topLabel.setFont(smallFont);
			leftLabel.setFont(smallFont);
			rightLabel.setFont(smallFont);
			line.add(new JLabel("Margin"), Box.createRigidArea(new Dimension(60, 5)), topLabel, 
					makeIntegerBox(scoped("topEdge")), Box.createRigidArea(new Dimension(10, 5)), leftLabel, 
					makeIntegerBox(scoped("leftEdge")), Box.createRigidArea(new Dimension(10, 5)), rightLabel, 
					makeIntegerBox(scoped("rightMargin")), Box.createHorizontalGlue());
		  	
			return line;
	    }
    
	protected Component makeIntegerBox(String propertyName, int min, int max) {
		RangedIntegerTextField fld = new RangedIntegerTextField(0,200);
		fld.setHorizontalAlignment(SwingConstants.RIGHT);
		setSizes(fld, 50, 30);
		components.put(propertyName, fld);
		return fld;
	}
    
	protected Component makeIntegerBox(String propertyName) {
		
		return makeIntegerBox(propertyName, 0, 200);
	}

//------------------------------------------------------------------
// Semantic Meat
// Map from key to displayName, to the component
// Attribute-Value-map 'props' is shared by all fields in this PrefsPanel
//------------------------------------------------------------------
	protected Map<String, JComponent> components = new HashMap<String, JComponent>();
	protected Map<String, String> displayNames = new HashMap<String, String>();
	protected Map<String, String> props = new HashMap<String, String>();
	//------------------------------------------------------------------
	public void install(Map<String, String> p)
	{
    	for (String name : components.keySet())
    	{
    		String property = props.get(name);
    		JComponent component = components.get(name);
    		if (property != null && component != null)
    			inject(property, component);
    	}
	}
	
	public Map<String, String> extract()
	{
    	Map<String, String> props = new HashMap<String, String>();
    	for (String name : components.keySet())
    	{
    		JComponent panel = components.get(name);
    		String value = scrape(panel);
    		String prefix = "";
    		if (!namespace.isEmpty() && !name.startsWith(namespace))
    			prefix = namespace + ".";
    		props.put(prefix+name, value);
    	}
    	return props;
	}
//------------------------------------------------------------------
// For each type of control we have used,
//	here we push a value into the control (inject).
//	and pull the value out (scrape)

	protected void inject(String value, JComponent control) {
		if (control instanceof JCheckBox) {
			JCheckBox ck = (JCheckBox) control;
			boolean val = value.toLowerCase().startsWith("t");
			ck.setSelected(val);
		}
		if (control instanceof JTextField) {
			JTextField fld = (JTextField) control;
			fld.setText(value);
		}
		if (control instanceof JComboBox) {
			JComboBox s = (JComboBox) control;
			s.setSelectedItem(value);
		}

		if (control instanceof JSlider) {
			JSlider s = (JSlider) control;
			try {
				int i = Integer.parseInt(value);
				s.setValue(i);
			} catch (NumberFormatException e) {
			}
		}
	}
	
	protected String scrape(JComponent control) {
		if (control instanceof JCheckBox) {
			JCheckBox ck = (JCheckBox) control;
			return ck.isSelected() ? "true" : "false";
		}
		if (control instanceof JTextField) {
			JTextField fld = (JTextField) control;
			return fld.getText();
		}
		if (control instanceof JSlider) {
			JSlider s = (JSlider) control;
			return "" + s.getValue();
		}
		if (control instanceof JComboBox) {
			JComboBox<?> s = (JComboBox<?>) control;
			return "" + s.getSelectedItem();
		}
		return "";
	}

};
