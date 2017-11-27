package org.cytoscape.prefs;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

import org.lib.Borders;
import org.lib.ColorBrewer;
import org.lib.ColorSwatch;
import org.lib.HBox;
import org.lib.VBox;

//   http://colorbrewer2.org

public class PrefsColors extends AbstractPrefsPanel {

	HBox lineOfPalettes;			// this is the box that gets populated dynamically
    Integer[] paletteSizes = { 1,2,3,4,5,6,7,8,9 };
	JComboBox<Integer> nColorsCombo = new JComboBox<Integer>(paletteSizes);
	
	public ActionListener ctrlListener = new ActionListener() { public void actionPerformed(ActionEvent e)  { populate(); } };
    ButtonGroup radioButtonGroup = new ButtonGroup();
    ButtonGroup paletteButtonGroup = new ButtonGroup();
	
	protected PrefsColors(Cy3PreferencesRoot dlog) {
		super(dlog, "color");
		lineOfPalettes = new HBox();
		nColorsCombo.setMaximumRowCount(10);	}
   @Override public void initUI()
    {
        super.initUI();
		Box page = Box.createVerticalBox();
//	    page.add(Box.createRigidArea(new Dimension(20,10)));
//	    page.add(new JLabel("Reserved for Colors"));
	    page.add(Box.createRigidArea(new Dimension(20,10)));
	    String s = "The Brewer Colors* are a set of color palettes that are appropriate for visualizations.\n" +
	    "The proper choice of palette is dependent on the type of attribute being mapped, \n" +
	    		"and may be further constrained by how the results will be handled downstream. \n"+
	
	  "";
	    JTextArea text = new JTextArea(s);
	    text.setOpaque(false);
//	    text.setMargin(new Insets(0,20,0, 10));
	    page.add(text);
//	    text.setBorder(Borders.blue);
	    text.setPreferredSize(new Dimension(535, 60));
	    text.setMaximumSize(new Dimension(535, 60));
	    
	    VBox controlBox = new VBox("controls");
	    controlBox.setBorder(Borders.doubleRaised);
	    JRadioButton a = makeRadioButton("Sequential"); 
	    JRadioButton b = makeRadioButton("Divergent"); 
	    JRadioButton c = makeRadioButton("Qualitative");
	    a.setSelected(true);
	    
	    controlBox.add(new HBox(true, true, a));
	    controlBox.add(new HBox(true, true, b));
	    controlBox.add(new HBox(true, true, c));
	    
	    controlBox.add(Box.createVerticalStrut(20));
	    colorBlindSafeCheckBox = makeCheckBox("Color blind safe");
	    controlBox.add(new HBox(true, true, colorBlindSafeCheckBox));
//	    controlBox.add(new HBox(true, true, makeCheckBox("Print friendly")));
//	    controlBox.add(new HBox(true, true, makeCheckBox("Photocopy safe")));
	    controlBox.add(Box.createVerticalStrut(10));
//	    controlBox.setPreferredSize(new Dimension(160, 350));
//	    controlBox.setMaximumSize(new Dimension(160, 350));
	    setSizes(controlBox, 160, 270);
    
	    JLabel label = new JLabel("Categories");
	    nColorsCombo.setSelectedItem(5);
	    nColorsCombo.addActionListener(ctrlListener);
	    HBox combobox = new HBox(true, true, Box.createRigidArea(new Dimension(5,10)), label, Box.createRigidArea(new Dimension(5,10)), nColorsCombo);
	    controlBox.add(combobox);
	    controlBox.add(Box.createVerticalGlue());

	    HBox combined = new HBox(true, false, Box.createHorizontalStrut(18), controlBox,  
	    		Box.createHorizontalStrut(30), makeColorSchemePicker(), Box.createHorizontalStrut(30));
//	    combined.setBorder(Borders.red);
	    page.add(combined);
//	    combined.setBorder(Borders.red);
	    populate();
	    page.setSize(dims);
		add(page);   
	}
   
   
   VBox makeColorSchemePicker()
   {
   		String name = "Pick a color scheme";
	    JLabel label = new JLabel(name);
	    HBox prompt = new HBox(Box.createRigidArea(new Dimension(32,17)), label);
	    
	    VBox box = new VBox(true, true, prompt, lineOfPalettes);
	    setSizes(box, 400, 270);
	    box.setAlignmentX(CENTER_ALIGNMENT);
	    box.setBorder(Borders.doubleRaised);
	    return box;
   }
  
    boolean colorBlindSafe = true;
    JCheckBox colorBlindSafeCheckBox = makeCheckBox("Color blind safe");
    int count = 0;
    int offset = 0;
    
	public void populate()
	{
		String state = radioButtonGroup.getSelection().getActionCommand();
		lineOfPalettes.removeAll();
		ColorBrewer[] brewer = null;
		if ("Divergent".equals(state))  
			brewer = ColorBrewer.getDivergingColorPalettes(colorBlindSafe);
		if ("Sequential".equals(state)) 
			brewer = ColorBrewer.getSequentialColorPalettes(colorBlindSafe);
		if ("Qualitative".equals(state)) 
			brewer = ColorBrewer.getQualitativeColorPalettes(colorBlindSafe);
	    
	    buildPalettes(state, brewer);
	    lineOfPalettes.setVisible(false);
	    lineOfPalettes.setVisible(true);			// TODO force a refresh
    }
	    
	void buildPalettes(String state, ColorBrewer[] brewers)
	{
		int len = brewers.length;
		int nColors = (Integer) nColorsCombo.getSelectedItem();
	
		if (len > 17)  len = 17;
		
		for (int i=0; i<len; i++)
		{
			ColorBrewer brew = brewers[i];
			if ("Sequential".equals(state))
			{
				if (i==3 || i==4  || i==6  || i==8 || i==13 ) continue;  // edit out near duplicates
			}
			Color [] colors = brew.getColorPalette(nColors);
			lineOfPalettes.add(makeColorColumn(colors));
		    lineOfPalettes.add(Box.createRigidArea(new Dimension(8,4)));
		}
	}
	 
	public void deselectAllPalettes()
	{
		for (Component c : lineOfPalettes.getComponents())
		{
			if (c instanceof ClickableBox)
				((ClickableBox)c).setSelect(false);
		}
	}
	   //
	   private  JRadioButton makeRadioButton(String s)
	   {
		   JRadioButton btn = new JRadioButton(s);
		   btn.addActionListener(ctrlListener);
		   btn.setActionCommand(s);
		   radioButtonGroup.add(btn); 
		   return btn;
	   }
	   private JCheckBox makeCheckBox(String s)
	   {
		   JCheckBox box = new JCheckBox(s);
		   box.addActionListener(ctrlListener);
		   return box;
	   }
		

	   
	ClickableBox makeColorColumn(Color[] colors)
	{
		ClickableBox box = new ClickableBox(false, false);
		box.addMouseListener(box);
		int row = 0;
		for (Color color : colors)
		{
			ColorSwatch c = new ColorSwatch(row++, 0, color, null, true);
			box.add(c);
		}
		box.setBorder(ClickableBox.borderOff);
		return box;
	}

//
//    VBox makeColorColumn(int start)
//    {
//    	int nColors = (Integer) nColorsCombo.getSelectedItem();
//    	ClickableBox box = new ClickableBox(false, false);
//		for (int row = 0; row < nColors; row++)
//		{
//			Color color = Colors.colorFromIndex(start + row);
//			ColorPane c = new ColorPane(row, 0, color, null);
//			box.add(c);
//		}
//		box.setBorder(BorderFactory.createLineBorder(Color.orange, 3));
//    	return box;
// }
    
    
    
}
