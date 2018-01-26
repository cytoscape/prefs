package org.cytoscape.prefs;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import org.lib.GuiFactory;


/** The preferences for Cytoscape 3.7.
 *
 */
class PrefsDocument extends Object
{
	
}
public class Cy3PreferencesRoot extends PreferenceContainer implements ActionListener
{
	private static final long serialVersionUID = 1L;
	static int NPREFS = 15;				// TODO -- hard-coded assumption of 3 x 5
	static int FOOTER_HEIGHT = 10;
	public static double WINDOW_WIDTH = 680;
	public static double WINDOW_HEIGHT = WINDOW_WIDTH / 1.618;
	public static double ROW_HEIGHT = (WINDOW_HEIGHT - FOOTER_HEIGHT) / 3;

	//----------------------------------------------------------------------------------------------------
	// main:  we assume an Application and a PrefsDocument 
	//		We use the PrefsDocument to create a PreferencesRoot container.
	//		
	//----------------------------------------------------------------------------------------------------
	public static void main(String[] args) {
		try {
			String lf = UIManager.getSystemLookAndFeelClassName();
			UIManager.setLookAndFeel(lf);
			
			CyPrefsApplication application = new CyPrefsApplication(new String[] { "prefs"});
			application.getPrefs();
			PrefsDocument workspace = new PrefsDocument();
			Cy3PreferencesRoot pref = new Cy3PreferencesRoot(workspace);
			pref.showDlog();
		} catch (Throwable e)	
		{ 	
			System.err.println("Error caught in main();");
			e.printStackTrace(); 
		}
		
	}
	// the set of properties read in from files all put into one map
	Map<String, String> propertyMap =  new HashMap<String, String>();
	public Map<String, String> getPropertyMap()	{ return propertyMap; }

	//----------------------------------------------------------------------------------------------------
	// the layout is based around three rows of 5 buttons each.
	// those numbers should be flexible, but in fact things get thrown off if that's not the layout
	//----------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------
	public Cy3PreferencesRoot(PrefsDocument w) 
	{
		super("Preferences");
		InputStream istream = Cy3PreferencesRoot.class.getResourceAsStream("fontawesome-webfont.ttf");
		try
		{
	        Font font = Font.createFont(Font.TRUETYPE_FONT, istream);
	        font = font.deriveFont(Font.PLAIN, 48f);
			initUI();

			Prefs prefsCopy = Prefs.getPrefs();
			if (prefsCopy instanceof Cy3PrefsAPI)
				Cy3PrefsAPI.readProperties();
			else { System.err.println("Prefs mismatch");  }
			
			Dimension buttonSize = new Dimension(100, 25);

			homePanel = new JPanel();
			homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.PAGE_AXIS));
//			homePanel.setBorder(BorderFactory.createLineBorder(Color.red));
			contentsPanel.add(homePanel,  "home");
			for(int i = 0; i<PPanels.rows.length; i++) 
			{
				int rowLength = PPanels.rows[i];
				JButton[] buttons = new JButton[rowLength];
				JComponent[] components = getRowsComponents(i, rowLength);  			
				for (int j=0; j < rowLength; j++) 
				{
					PPanels panel = PPanels.values()[i * 5 + j];
					char iconName = panel.getIcon().charAt(0);
					Icon icon = new FontAwesomeIcon(iconName, 28);
					String name = panel.getDisplayName();
					buttons[j] = new JButton(name);  //GuiFactory.makeStackButton(name, name, "prefs/" + name, false);		//FJButton.ENABLED_ROLLOVER_DISABLED
					buttons[j].setSize(buttonSize);
					buttons[j].setIcon(icon);
					buttons[j].setToolTipText(panel.getTooltip());
					buttons[j].addActionListener(buttonListener);
					int wid = 140; 
					int hght = 100;   //  icon.getIconHeight() / 2;
					Dimension dim = new Dimension(wid, hght);
					GuiFactory.setSizes(buttons[j], dim);
					buttons[j].setHorizontalTextPosition(SwingConstants.CENTER);
					buttons[j].setVerticalTextPosition(SwingConstants.BOTTOM);
					components[j].setName(buttons[j].getText());
					System.out.println("adding: " + buttons[j].getText() + " -> " + components[j]);
					contentsPanel.add(components[j], buttons[j].getText());
				}
				addButtonRow(buttons, components, PPanels.rowNames[i]);
			}
		}
		catch (Exception e)		{	e.printStackTrace();	}
		
		install(Prefs.getPrefs());
	}
	//-------------------------------------------------------------------------
	private final AbstractPrefsPanel[] fPrefs = new AbstractPrefsPanel[NPREFS];

	// TODO -- hard-coded assumption of 3 x 5  -- keep in synch with PPanels

	private void initUI() {
			
			fPrefs[0] = new PrefsLayouts(this);				// row 1
			fPrefs[1] = new PrefsGroups(this);
			fPrefs[2] = new PrefsTables(this);
			fPrefs[3] = new PrefsLinks(this);
			fPrefs[4] = new PrefsStyles(this);

			fPrefs[5] = new PrefsBehavior(this);			// row 2
			fPrefs[6] = new PrefsEfficiency(this);
			fPrefs[7] = new PrefsSecurity(this);
			fPrefs[8] = new PrefsApps(this);						
			fPrefs[9] = new PrefsMenus(this);		

			fPrefs[10] = new PrefsLegends(this);			// row 3
			fPrefs[11] = new PrefsColors(this);
			fPrefs[12] = new PrefsText(this);
			fPrefs[13] = new PrefsImages(this);
			fPrefs[14] = new PrefsAdvanced(this);

			for (int i=0; i < NPREFS; i++)
				fPrefs[i].initUI();
		}
	// ----------------------------------------------------------------------------------------
// TODO -- hardcoded assumption of 3 x 5
	public JComponent[] getRowsComponents(int row, int rowlen)
		{
			JComponent[] components = new JComponent[rowlen];
			switch (row)
			{
				case 0:		for (int i=0; i < 5; i++)	components[i] = fPrefs[i];		break;
				case 1:		for (int i=0; i < 5; i++)	components[i] = fPrefs[i+5];	break;
				case 2:		for (int i=0; i < 5; i++)	components[i] = fPrefs[i+10];	break;
//				case 3:		for (int i=0; i < 4; i++)	components[i] = fPrefs[i+15];	break;
			}
			int width = (int) WINDOW_WIDTH - 48;  
			int height = (int) WINDOW_HEIGHT;
			Dimension size = new Dimension(width, height);
			for (JComponent c : components)
				GuiFactory.setSizes(c, size);
			GuiFactory.setSizes(homePanel, size);
			return components;
		}
	// ---------------------------------------------------------------------------------------------------------
		Prefs prefsCopy = new Cy3PrefsAPI();
		
		public Prefs getPrefs() { return prefsCopy;	}
	// ---------------------------------------------------------------------------------------------------------
		public void install(Prefs inPrefs)    
		{
			if (inPrefs == null)
			 inPrefs = Prefs.getPrefs();
			for (int i=0; i < NPREFS; i++)
				fPrefs[i].install(inPrefs);
		}

		public void extract()
		{
			Map<String,String> props = new HashMap<String, String>();
			for (int i=0; i < NPREFS; i++)
			{
				Map<String,String> subprops = fPrefs[i].extract();
				props.putAll(subprops);
			}
			writePrefsToConfig(props);
		}

		// ---------------------------------------------------------------------------------------------------------
		private void writePrefsToConfig(Map<String,String> props) {			// TODO
			
			Set<Entry<String, String>> entries = props.entrySet();
			Map<String, String> sorted = new TreeMap<String, String>(); 
			for (Entry<String, String> e : entries )
				sorted.put(e.getKey(), e.getValue());
			
			for (Entry<String, String> e : sorted.entrySet() )
				System.out.println(e.getKey() + " ==>  " + e.getValue());
		
		}
	// ---------------------------------------------------------------------------------------------------------
	@Override public void savePrefs()	{	extract();	}
	
}