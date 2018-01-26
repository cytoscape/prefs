package org.cytoscape.prefs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.lib.HBox;
import org.lib.VBox;

public class PrefsLayouts extends MasterDetailPanel
{

	    public PrefsLayouts(Cy3PreferencesRoot dlog)
    {
        super(dlog, "layout");
//	    editorPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
//	    setBorder(Borders.red);
    }
	
//     Dimension leading = new Dimension(12,12);
    @Override public void initUI()
    {
        super.initUI();
        makeLayoutList();
        splitter.setDividerLocation(0.3);
    }
    String base = "http://manual.cytoscape.org/en/stable/Navigation_and_Layout.html";

//	String[] layoutsx = {"attribute-circle", "attributes-layout", "circular", "cose", "degree-circle", "force-directed-cl", "force-directed", 
//			"fruchterman-rheingold", "grid", "hierarchical", "isom", "kamada-kawai", "stacked-node-layout"  };
//

private void makeLayoutList()
    {
    	addLayout("Grid", "grid", makeGridLayoutEditor(), "#grid-layout");
    	addLayout("Circular", "circular", makeCirleLayoutEditor(), "#yfiles-circular-layout");
    	addLayout("Attribute Circle", "attribute-circle", makeAttributeCirleLayoutEditor(), "#attribute-circle-layout");
    	addLayout("Degree Sorted Circle", "degree-circle", makeDegreeSortedCircleLayoutEditor());
    	addLayout("Group Attributes", "attributes-layout" , makeGroupLayoutEditor(), "#group-attributes-layout");
    	addLayout("Stacked Node", "stacked-node-layout",makeStackedNodeLayoutEditor());
    	addLayout("Hierarchical", "hierarchical",makeHierarchicalLayoutEditor(), "#yfiles-hierarchical-layout");
    	addLayout("Compound Spring Embedder", "cose", makeCoSELayoutEditor(), "#compound-spring-embedder-layout");
    	addLayout("Inverted Self Organizing Map", "isom", makeInvertedSOMLayoutEditor());
       	addLayout("Edge-weighted Spring-Embedded", "kamada-kawai", makeEdgeWeightedSpringEmbeddedLayoutEditor(), "#edge-weighted-spring-embedded-layout");
       	addLayout("Edge-weighted Force-Directed", "fruchterman-rheingold", makeEdgeWeightedForceLayoutEditor());
    	addLayout("Prefuse Force Directed", "force-directed", makePrefuseForceLayoutEditor(), "#prefuse-force-directed-layout");
    	addLayout("Prefuse Force Directed OpenCL", "force-directed-cl" , makePrefuseForceOpenCLLayoutEditor(), "#prefuse-force-directed-layout");
   }

    private void addLayout(String s, String namespace, NestedPrefPanel editor)
    {
    	addLayout(s, namespace, editor, null);
    }
    
    
    private void addLayout(String layoutName, String namespace, NestedPrefPanel editor, String url)
    {
    	masterListModel.addRecord(layoutName);			// add the layout to the model
    	editor.setName(layoutName);
    	detailPanel.add(layoutName, editor);			// put the card in the deck
    	map.put(layoutName, editor);					// be able to recall the editor
    	namespaceMap.put(layoutName, namespace);		// be able to get the ns from layout name
    	if (url != null)
    		editor.add(makeExampleLink(url));
    }
    static Font FONT = new Font("Dialog", Font.ITALIC, 9);
   
//    @Override public Map<String, String> extract()
//    {
//        int nRows = masterListModel.getRowCount();
//        for (int i=0; i<nRows; i++)
//        {
//        	String layoutName = masterListModel.getLayout(i);
//        	String nameSpace = namespaceMap.get(layoutName);
//        	NestedPrefPanel editor = map.get(layoutName);
////        	Map<String, String> props = editor.extract();
////        	writeLayoutPropFile(props);
//          }
//
//    }
    HBox makeExampleLink(String url)
    {
    	HBox line = new HBox();
    	String fullUrl = base + url;
    	JLabel label = new JLabel("Link");
    	label.setFont(FONT);
    	label.setForeground(Color.BLUE);
//    	label.addMouseListener(ev -> { openUrl(fullUrl);	});
    	return line;
    }
    
    private NestedPrefPanel makeAttributeCirleLayoutEditor()
    {
		Box page = Box.createVerticalBox();
		NestedPrefPanel panel =  new NestedPrefPanel("layout.attribute-circle", page, root);
	  	page.add(panel.makeLabeledField("Edge Attribute", "edgeAttribute", ""));
	  	page.add(panel.makeNumberField("Circle Size", "spacing", 1, 1, 100));
	  	page.add(Box.createRigidArea(lineSpace));
	  	page.add(panel.makeCheckBoxLine("Partition graph before layout", "singlePartition", "tip"));
	  	page.add(Box.createRigidArea(lineSpace));
		return panel;
	}
     //-------------------------------------------------------------------------------
    private NestedPrefPanel makeCirleLayoutEditor()
    {
		Box page = Box.createVerticalBox();
		NestedPrefPanel panel = new NestedPrefPanel("layout.circular", page, root);
	  	page.add(panel.makeSpacingBox());
	  	page.add(panel.makeMarginBox());
	  	page.add(Box.createRigidArea(lineSpace));
	  	page.add(prepartitionOption("circular"));
		return panel;
	}

    private NestedPrefPanel makeCoSELayoutEditor()
    {
    	
		Box page = Box.createVerticalBox();
		NestedPrefPanel panel = new NestedPrefPanel("layout.cose", page, root);
	  	page.add(panel.makeNumberField("Ideal Edge Length", "idealEdgeLength", 1, 1, 100));
	  	page.add(panel.makeNumberField("Spring Strength", "springStrength", 1, 1, 100));
	  	page.add(panel.makeNumberField("Repulsion Strength", "repulsionStrength", 1, 1, 100));
	  	page.add(panel.makeNumberField("Gravity Strength", "gravityStrength", 1, 1, 100));
	  	page.add(panel.makeNumberField("Compound Gravity Stength", "compoundGravityStrength", 1, 1, 100));
	  	page.add(Box.createRigidArea(lineSpace));
	  	page.add(panel.makeCheckBoxLine("Use smart edge length calculation", "smartEdgeLengthCalc", "tip"));
	  	page.add(panel.makeCheckBoxLine("Use smart repulsion range calculation", "smartRepulsionRangeCalc", "tip"));
	  	page.add(Box.createRigidArea(lineSpace));
		return panel;
	}

    private NestedPrefPanel makeDegreeSortedCircleLayoutEditor()
    {
		Box page = Box.createVerticalBox();
		NestedPrefPanel panel =  new NestedPrefPanel("layout.degree-circle", page, root);
	  	page.add(prepartitionOption("degree-circle"));
		return panel;
	}

 
    private NestedPrefPanel makeEdgeWeightedForceLayoutEditor()
    {
		Box page = Box.createVerticalBox();
		NestedPrefPanel panel =  new NestedPrefPanel("layout.fruchterman-rheingold", page, root);
	  	page.add(makeEdgeWeightLine());
	  	
	  	page.add(new HBox(false, true, new JLabel("Multipliers")));
	  	HBox lineA = new HBox(panel.makeNumberFieldShort("Attraction", 70, "attraction_multiplier", 1, 1, 100));
	  	HBox lineB = new HBox(panel.makeNumberFieldShort("Repulsion", 80, "repulsion_multiplier", 1, 1, 100));
	  	HBox lineC = new HBox(panel.makeNumberFieldShort("Gravity", 60, "gravity_multiplier", 1, 1, 100));
	  	HBox lineC2 = new HBox(panel.makeNumberFieldShort("Compound Gravity", 130, "gravity_multiplier", 1, 1, 100));
	  	page.add(new HBox(false, true, lineA, lineB));
	  	page.add(new HBox(false, true, lineC, lineC2));
//	  	
	  	HBox lineM = new HBox(panel.makeNumberFieldShort("Conflict Avoidance", 120, "conflict_avoidance", 1, 1, 100));
	  	HBox lineN = new HBox(panel.makeNumberFieldShort("Temperature", 80, "temperature", 1, 1, 100));
	  	page.add(new HBox(false, true, lineM, lineN));
  	
	  	HBox lineQ = new HBox(panel.makeNumberFieldShort("Repulsion Range", 120, "repulsion_multiplier", 1, 1, 100));
	  	HBox lineR = new HBox(panel.makeNumberFieldShort("Extra room", 80, "repulsion_multiplier", 1, 1, 100));
	  	
	  	page.add(new HBox(false, true, lineQ, lineR));

	  	
	  	HBox lineD = new HBox(panel.makeNumberFieldShort("#Iterations", 90, "nIterations", 1, 1, 100));
	  	HBox lineE = new HBox(panel.makeNumberFieldShort("Refresh Every", 100, "update_iterations", 1, 1, 100));
	  	page.add(new HBox(false, true, lineD, lineE));

	  	page.add(Box.createRigidArea(lineSpace));
	  	page.add(prepartitionOption("fruchterman-rheingold"));
	  	page.add(panel.makeCheckBoxLine("Randomize graph before layout", "", "tip"));
	  	page.add(panel.makeCheckBoxLine("Use 3D layout", "layout3D", "tip"));
	  	page.add(Box.createRigidArea(lineSpace));
		return panel;
	}

    String[] interpretations = {  "Heuristic", "-Log(value)", "1 - normalized value", "normalized value" } ;
    private NestedPrefPanel makeEdgeWeightedSpringEmbeddedLayoutEditor()
    {
    	
		Box page = Box.createVerticalBox();
		NestedPrefPanel panel= new NestedPrefPanel("layout.kamada-kawai", page, root);
		page.add(makeEdgeWeightLine());
	  	HBox aLine = panel.makeNumberFieldShort("Spring strength", 140, "m_nodeDistanceStrengthConstant", 1, 1, 100);
	  	aLine.add(panel.makeNumberFieldShort("rest length", 70, "m_nodeDistanceRestLengthConstant", 1, 1, 100));
//	  	HBox sum = new HBox(true, true, aLine, a2Line);
//	  	sum.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
//	  	page.add(sum);
	  	
	  	HBox bLine = panel.makeNumberFieldShort("Discon. strength", 140, "m_disconnectedNodeDistanceSpringStrength", 1, 1, 100);
	  	bLine.add(panel.makeNumberFieldShort("rest length", 70, "m_disconnectedNodeDistanceSpringRestLength", 1, 1, 100));
	  	page.add(aLine);
	  	page.add(bLine);
	  	
	  	
	  	page.add(panel.makeNumberField("Collision avoidance strength", "m_anticollisionSpringStrength", 1, 1, 100));
	  	page.add(panel.makeNumberField("Number of layout passes", "m_layoutPass", 1, 1, 100));
	  	page.add(panel.makeNumberField("Refresh every", "m_averageIterationsPerNode", 1, 1, 100));

	  	page.add(Box.createRigidArea(lineSpace));
	  	page.add(panel.prepartitionOption("kamada-kawai"));
	  	page.add(panel.makeCheckBoxLine("Use unweighted edges", "unweighted", "tip"));
	  	page.add(panel.makeCheckBoxLine("Randomize graph before layout", "randomize", "tip"));
	  	page.add(Box.createRigidArea(lineSpace));
		return panel;
	}
  

    private NestedPrefPanel makeGridLayoutEditor()
    {
    	
		Box page = Box.createVerticalBox();
		NestedPrefPanel panel=  new NestedPrefPanel("layout.grid", page, root);
	  	page.add(panel.makeSpacingBox());
	  	page.add(Box.createRigidArea(lineSpace));
		return panel;
	}   
    
//TODOspacingy=400.0    spacingx=400.0

    private NestedPrefPanel makeGroupLayoutEditor()
    {
    	
		Box page = Box.createVerticalBox();
		NestedPrefPanel panel =  new NestedPrefPanel("layout.attributes-layout", page, root);
	  	page.add(panel.makeLabeledField("Edge Weight Attribute ", "edgeAttribute", ""));
	  	page.add(Box.createRigidArea(lineSpace));
	  	page.add(panel.makeSpacingBox());
	  	page.add(Box.createRigidArea(lineSpace));
	  	page.add(panel.makeNumberField("Maximum Row Width", "maxwidth", 1, 5000, 10000));
	  	page.add(panel.makeNumberField("Minimum Partition Width", "minrad", 1, 100, 1000));
	  	page.add(panel.makeNumberField("Scale of the Partition Radius", "radmult", 1, 50, 100));
	  	page.add(Box.createRigidArea(lineSpace));
		return panel;
	}   
    
 	private NestedPrefPanel makeHierarchicalLayoutEditor()
    {
    	
		Box page = Box.createVerticalBox();
		NestedPrefPanel panel =  new NestedPrefPanel("layout.hierarchical", page, root);
	  	page.add(panel.makeSpacingBox());
	  	page.add(panel.makeNumberField("Component Spacing", "spacing", 1, 1, 100));
	  	page.add(panel.makeNumberField("Band gap", "bandGap", 1, 1, 100));
	  	page.add(panel.makeMarginBox());
	  	page.add(Box.createRigidArea(lineSpace));
		return panel;
	}   
    
    
    private NestedPrefPanel makeInvertedSOMLayoutEditor()
    {
		Box page = Box.createVerticalBox();
		NestedPrefPanel panel = new NestedPrefPanel("layout.isom", page, root); 	
		page.add(makeNumberField("Radius", "radius", 1, 1, 100));

	  	HBox lineN = new HBox(panel.makeNumberFieldShort("Constant", 70, "radiusConstantTime", 1, 1, 100));
	  	HBox lineM = new HBox(panel.makeNumberFieldShort("Minumum Radius", 120, "minRadius", 1, 1, 100));
	  	page.add(new HBox(false, true, lineM, lineN));
 
	  	HBox lineP = new HBox(panel.makeNumberFieldShort("Initial Adaptation", 120, "initialAdaptation", 1, 1, 100));
	  	HBox lineQ = new HBox(panel.makeNumberFieldShort("Minumum ", 70, "minAdaptation", 1, 1, 100));
	  	page.add(new HBox(false, true, lineP, lineQ));

	  	HBox lineR = new HBox(panel.makeNumberFieldShort("Size Factor", 80, "sizeFactor", 1, 1, 100));
	  	HBox lineS = new HBox(panel.makeNumberFieldShort("Cooling Factor ",100, "coolingFactor", 1, 1, 100));
	  	page.add(new HBox(false, true, lineR, lineS));

 	  	page.add(panel.makeNumberField("Number of Iterations", "maxEpoch", 1, 5000, 5000));
	  	page.add(Box.createRigidArea(lineSpace));
	  	page.add(panel.makeCheckBoxLine("Partition graph before layout", "singlePartition", "tip"));
		return panel;
	}   

    private NestedPrefPanel makePrefuseForceLayoutEditor()
    {
    	
		Box page = Box.createVerticalBox();
		NestedPrefPanel panel = new NestedPrefPanel("layout.force-directed", page, root);
	  	page.add(makeEdgeWeightLine());

	  	HBox lineD = new HBox(panel.makeNumberFieldShort("Spring Coefficient", 130, "nIterations", 1, 1, 100));
	  	HBox lineE = new HBox(panel.makeNumberFieldShort("Length", 100, "update_iterations", 1, 1, 100));
	  	page.add(new HBox(false, true, lineD, lineE));
	  	page.add(panel.makeNumberField("Node Mass", "defaultNodeMass", 1, 3, 100));
	  	page.add(Box.createRigidArea(lineSpace));
	  	page.add(panel.makeCheckBoxLine("Force Deterministic Layouts (slower)", "isDeterministic", "tip"));
	  	page.add(panel.prepartitionOption("force-directed"));
	  	page.add(panel.makeCheckBoxLine("Randomize graph before layout", "randomize", "tip"));
	  	page.add(panel.makeCheckBoxLine("Use 3D layout", "use3d", "tip"));
	  	page.add(Box.createRigidArea(lineSpace));
		return panel;
	}
  
    private NestedPrefPanel makePrefuseForceOpenCLLayoutEditor()
    {
    	
		Box page = Box.createVerticalBox();
		NestedPrefPanel panel = new NestedPrefPanel("layout.force-directed-cl", page, root);;
	  	page.add(makeEdgeWeightLine());
	  	
//	  	page.add(makeNumberField("Spring Coefficient", "force-directed-cl", "", 1, 1, 100));
//	  	page.add(makeNumberField("Spring Length", "force-directed-cl", "", 1, 1, 100));

	  	HBox lineD = new HBox(panel.makeNumberFieldShort("Spring Coefficient", 130, "springCoeff", 1, 1, 100));
	  	HBox lineE = new HBox(panel.makeNumberFieldShort("Length", 80, "springLength", 1, 1, 100));
	  	page.add(new HBox(false, true, lineD, lineE));
	  	page.add(panel.makeNumberFieldShort("Node Mass", 115, "", 1, 1, 100));

	  	HBox lineF = new HBox(panel.makeNumberFieldShort("Number of Iterations", 140, "nIterations", 1, 1, 100));
	  	HBox lineG = new HBox(panel.makeNumberField("Edge-repulsive Iterations", "edgeRepelIterations", 1, 1, 100));
	  	page.add(lineF);
	  	page.add(lineG);

	  	page.add(Box.createRigidArea(lineSpace));
	  	page.add(panel.makeCheckBoxLine("Force Deterministic Layouts (slower)", "deterministic", "tip"));
	  	page.add(panel.makeCheckBoxLine("Start from scratch",  "fromScratch","tip"));
	  	page.add(panel.makeCheckBoxLine("Use 3D layout",  "use3d", "tip"));
	  	page.add(Box.createRigidArea(lineSpace));
		return panel;
	}

    private NestedPrefPanel makeStackedNodeLayoutEditor()
    {
		Box page = Box.createVerticalBox();
		NestedPrefPanel panel = new NestedPrefPanel("layout.stacked-node-layout", page, root);;
	  	page.add(panel.makeNumberField("X Position","x_position", 1, 1, 100));
	  	page.add(panel.makeNumberField("Y Position", "y_position", 1, 1, 100));
	  	page.add(Box.createRigidArea(lineSpace));
		return panel;
	}   
	//---------------------------------------------------------------------------------------------------------
   
 	HBox makeEdgeWeightLine() {
 		JLabel label = new JLabel("Attribute");
 		JTextField attrName = new JTextField();
 		JLabel spacer = new JLabel("    ");
 		HBox line0 = new HBox();
 		line0.add(label, spacer, attrName);
 		setSizes(line0, 300, 30);

 		JComboBox<String> combo = new JComboBox<String>(interpretations);
 		setSizes(combo, 200, 30);

 		HBox lineA = new HBox(true, true, new JLabel("Distribution"), combo);
 		HBox line1 = new HBox(true, true, new JLabel("Edge weight"));

 		HBox line2 = new HBox(true, true, new JLabel("Min:"), numberField(1), new JLabel("Max:"), numberField(1000));
 		line2.add(new JLabel("Default:"));
 		line2.add(numberField(100));
 		setSizes(line2, 300, 30);
 		VBox couplet = new VBox(false, false, line1, line0, lineA, line2);
 		setSizes(couplet, 310, 120);
// 		couplet.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
 		return new HBox(true, true, couplet);
 	}
 	
	//---------------------------------------------------------------------------------------------------------

}