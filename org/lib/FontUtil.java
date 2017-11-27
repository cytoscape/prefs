package org.lib;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.font.TextAttribute;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.logging.Log;

public class FontUtil //implements StringDefs
{
    public static final Font	dlog5	= new Font(Font.DIALOG, 0, 5);
    public static final Font	dlog8	= new Font(Font.DIALOG, 0, 8);
    public static final Font	dlog9	= new Font(Font.DIALOG, 0, 9);
    public static final Font	dlog10	= new Font(Font.DIALOG, 0, 10);
    public static final Font	dlog11	= new Font(Font.DIALOG, 0, 11);
    public static final Font	dlog12	= new Font(Font.DIALOG, 0, 12);
    public static final Font	dlog14	= new Font(Font.DIALOG, 0, 14);
    public static final Font	dlog16	= new Font(Font.DIALOG, 0, 16);
	public static final Font	dlogBI9= new Font(Font.DIALOG, Font.ITALIC + Font.BOLD, 9);
	public static final Font	dlogBI10= new Font(Font.DIALOG, Font.ITALIC + Font.BOLD, 10);
	public static final Font	dlogBI11= new Font(Font.DIALOG, Font.BOLD + Font.ITALIC, 11);
	public static final Font	dlogBI12= new Font(Font.DIALOG, Font.BOLD + Font.ITALIC, 12);
	public static final Font	BoldDialog11= new Font(Font.DIALOG, Font.BOLD, 11);
	public static final Font	BoldDialog12= new Font(Font.DIALOG, Font.BOLD, 12);
	public static final Font	dlogItal12= new Font(Font.DIALOG, Font.ITALIC, 12);
	public static final Font	dlogBold16= new Font(Font.DIALOG, Font.BOLD, 16);

	public static final Font	SansSerif9	= new Font(Font.SANS_SERIF, 0, 9);
	public static final Font	SansSerif10	= new Font(Font.SANS_SERIF, 0, 10);
	public static final Font	SansSerif11	= new Font(Font.SANS_SERIF, 0, 11);
	public static final Font	SansSerif12	= new Font(Font.SANS_SERIF, 0, 12);
	public static final Font	SansSerif14	= new Font(Font.SANS_SERIF, 0, 14);
	public static final Font	SansSerifBold10	= new Font(Font.SANS_SERIF, Font.BOLD, 10);
	public static final Font	SansSerifItal11	= new Font(Font.SANS_SERIF, Font.ITALIC, 11);
	public static final Font	SansSerifItal12	= new Font(Font.SANS_SERIF, Font.ITALIC, 12);

	public static final Font	BoldSerif12	= new Font(Font.SERIF, Font.BOLD, 12);
	public static final Font	BoldSerif14	= new Font(Font.SERIF, Font.BOLD, 14);
	public static final Font	SerifBold15	= new Font(Font.SERIF, Font.BOLD, 15);
	public static final Font	Serif10	= new Font(Font.SERIF, 0, 10);
	public static final Font	Serif11	= new Font(Font.SERIF, 0, 11);
	public static final Font	Serif12	= new Font(Font.SERIF, 0, 12);
	public static final Font	SerifItal12	= new Font(Font.SERIF, Font.ITALIC, 12);
	public static final Font	Serif14	= new Font(Font.SERIF, 0, 14);
	public static final Font	Serif16	= new Font(Font.SERIF, 0, 16);
	public static final Font	Serif18	= new Font(Font.SERIF, 0, 18);
	public static final Font	Serif24	= new Font(Font.SERIF, 0, 24);
	public static final Font	SerifBold18	= new Font(Font.SERIF, 0, 18);

	public static final Font	Arial14	= new Font("Arial Unicode MS", 0, 14);
	
//	public static final Font LiberationSansBoldBase 	= 	loadFontFromFile(Font.TRUETYPE_FONT, "/fonts/LiberationSans-Bold.ttf");
//	public static final Font PTSansNarrowBase		 	= 	loadFontFromFile(Font.TRUETYPE_FONT, "/fonts/PTN57F.ttf"); 
//	public static final Font PTSansBoldBase		 			= 	loadFontFromFile(Font.TRUETYPE_FONT, "/fonts/PTS75F.ttf"); 
	
//	public static Font 	PTSansNarrow16 			=	PTSansNarrowBase.deriveFont(Font.PLAIN, 16);
//	public static Font 	PTSansBold18 			=	PTSansBoldBase.deriveFont(Font.PLAIN, 18);
//	public static Font 	LiberationSansBold12 	=	LiberationSansBoldBase.deriveFont(Font.PLAIN, 12);

	public static final Font	MonoBold12	=   new Font(Font.MONOSPACED, Font.BOLD, 12);
	  
	public static final DictKey		Bold = new DictKey("string.font.bold");
    public static final DictKey		Italic = new DictKey("string.font.italic");
    public static final DictKey		BoldItalic = new DictKey("string.font.bolditalic");
    public static final DictKey		Plain = new DictKey("string.font.plain");
    public static final DictKey[] 	styles = new DictKey[] { Plain, Bold, Italic, BoldItalic };
    
	public static final Font	Futura12	= new Font("Futura",Font.PLAIN, 12);

	public static final Color INFO_COLOR		= new Color(0x00aa00);
	public static final Color WARNING_COLOR		= new Color(0xaa5500);
	public static final Color ERROR_COLOR		= new Color(0xaa0000);
	

	
    public enum FontStyle {
    	BOLD("bold", "string.font.bold", Font.BOLD),
    	ITALIC("italic", "string.font.italic", Font.ITALIC),
    	BOLDITALIC("bold-italic", "string.font.bolditalic", Font.BOLD + Font.ITALIC),
    	PLAIN("plain", "string.font.plain", Font.PLAIN);
    	
    	private String fFCML;
		private int fStyle;
		private String fKey;
		private FontStyle(String fcml, String key, int style)	 {	fFCML = fcml;	fStyle = style;	fKey = key;	}
		public int getStyle()			 	{	return fStyle;		}
		public String getKey()				{	return fKey;		}
		
		/**
		 * Generous matcher compensates for lowercase and uses contains? to return type specific style 
		 * @param type.contains(styleString)
		 * @return FontStyle PLAIN (default), BOLD, ITALIC, BOLD-ITALIC
		 */
		public static FontStyle fromFCML(String styleString)	 {
			if (styleString == null || styleString.length() == 0) 
				return PLAIN;
			for (FontStyle type : FontStyle.values()){
				if (type.toString().toLowerCase().equals(styleString.toLowerCase()))
					return type;
			}
			return PLAIN;		
		}
    }
    public static Font fontStyleFromString(final String style)
    {
        int istyle  = Font.PLAIN;
        String lcaseStyle = style.toLowerCase();
        if (lcaseStyle.contains("bold"))          istyle |= Font.BOLD;
        if (lcaseStyle.contains("italic"))          istyle |= Font.ITALIC;
//        else if (lcaseStyle.equals(FontCommand.BOLDITALIC))      istyle = Font.BOLD | Font.ITALIC;
        return FontManager.getRelativeFont(istyle, 0);
    }
    public static Font fontStyleFromKey(final DictKey key)    { return key == null ? dlog12 : fontStyleFromString(key.getKeyString());}

    public static String fontStyleToString(final Font f)
    {
        String str = null;
        final int istyle = f.getStyle();
        if (istyle == Font.BOLD)                     str = "bold";
        else if (istyle == Font.ITALIC)              str = "italic";
        else if (istyle == (Font.BOLD | Font.ITALIC)) str = "bold-italic";
        else                                         str = "plain";
        return str;
    }
    
	private static TreeSet<String> sFontFamilies = null;
	public static TreeSet<String> getFontFamilies()
	{
	
	if (sFontFamilies == null)
		initFonts();
	return sFontFamilies;
	}
	
	static public void initFonts()
	{
		sFontFamilies = new TreeSet<String>();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Font fa[] = ge.getAllFonts();
		for (int i = 0; i < fa.length; i++){
			if(fa[i].canDisplay('a'))  //check to see if system actually can display font so you don't get garbage for characters
				sFontFamilies.add(fa[i].getFamily());
		}
	}
	
	public static Dimension getTextSize(Graphics g, String s)
	{
		FontMetrics fm = g.getFontMetrics();
		int width = fm.stringWidth(s);
		int height = (int) fm.getLineMetrics(s, g).getHeight();
		return new Dimension(width, height);
	}

	public static Font scale(Font f, double scale)
	{
		return new Font(f.getName(), f.getStyle(), (int) (f.getSize() * scale));
	}
	
	public static Font resize(Font f, int size)
	{
		return new Font(f.getName(), f.getStyle(), size);
	}
	
	public static Font withStyle(Font f, int style)
	{
		return new Font(f.getFamily(), style, f.getSize());
	}
	
	public static Font strike(Font f)
	{
		Map<TextAttribute, Object> attrs = (Map<TextAttribute, Object>) f.getAttributes();
		attrs.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
		return new Font(attrs);
	}
	
	public static Font loadFontFromFile(int fontFormat, String filepath){
		Font font = dlog12;
		try {
			InputStream fontStream = ClassLoader.class.getResourceAsStream(filepath);
			font = Font.createFont(fontFormat, fontStream);
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
		} catch (Exception e) {
//			Log.debug("Font load exception: " + (String) e.getMessage());
		}
		return font;
	}

}