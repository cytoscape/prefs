package org.lib;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;

/**
 * TextTrait objects are used to store text formatting information. They can be stored into XML using getAsElement().
 */
public final class TextTraits
{
    public interface Defaults
    {
        final Color  BACK_COLOR           = Color.WHITE;
        final Color  FORE_COLOR           = Color.BLACK;
        final int    FONT_SIZE            = 12;
        final int    FONT_STYLE           = Font.PLAIN;
        final String FONT_NAME            = "SansSerif";
//        final String DFLT_NAME            = "";
        final String FONT_STYLE_STRING    = "plain";
        final String JUSTIFICATION_STRING = "left";
    }

    static public String[] AVAILABLE_FONT_NAMES;
    {
        AVAILABLE_FONT_NAMES = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        Arrays.sort(AVAILABLE_FONT_NAMES);
    }
    private Color       backColor = Defaults.BACK_COLOR, foreColor = Defaults.FORE_COLOR;
    private int       	justification;
    private String      name = "";
    private String 		fontName = Defaults.FONT_NAME;
    private int 		fontSize = Defaults.FONT_SIZE;
    private int 		fontStyle = Defaults.FONT_STYLE;

    public String getTTName()    		{        return name;    }
    public void setTTName(String s)    	{        if (s != null) name = s;    }
    
    public TextTraits(String context)
    {
        this();
        if (context != null) setTTName(context);
//        SElement fontPref = Prefs.getPrefs().getTextTraits(context);
//        if (fontPref != null) backColor = fontPref.getColor("background");
    }

    public TextTraits()    {    }
    public static TextTraits makeDefault()	{ return new TextTraits(Defaults.FONT_NAME,Defaults.FONT_SIZE,Defaults.FONT_STYLE,Defaults.BACK_COLOR, Defaults.FORE_COLOR, 0);	}
    public TextTraits(String inName, int inSize, int inStyle, Color bg, Color fg, int just)
    {
    	this();
        backColor =  (bg == null) ? Defaults.BACK_COLOR : bg;
        foreColor = (fg == null) ?  Defaults.FORE_COLOR : fg;
        fontName = inName;
        fontSize = inSize;
        fontStyle = inStyle;
//        justification = ParseUtil.JUSTIFY_LEFT;
//        if (just == ParseUtil.JUSTIFY_RIGHT || just == ParseUtil.JUSTIFY_CENTER)
//            justification = just;
        name = inName;
    }

//    public TextTraits(SElement element)    	{        setFromElement(element);    }

	public Font getFont()    				{        return new Font(fontName,fontStyle, fontSize);       }
    public Color getColor()  				{        return foreColor;       }
    @Override public Object clone()    		{        return new TextTraits(fontName,fontSize,fontStyle, backColor, foreColor, justification);    }
//
//    public SElement getAsElement()
//    {
//        SElement element = new SElement("TextTraits");
//        element.setString("font", fontName);
//        element.setInt("size", getSize());
//        element.setString("name", getTTName());
//        element.setString("style", ParseUtil.styleToString(fontStyle));
//        element.setColor("color", foreColor);
//        element.setColor("background", backColor);
//        element.setString("just", ParseUtil.justificationToString(justification));
//        return element;
//    }
    @Override public String toString()		{	return	getFontName() + "(" + getSize() + ", " + getStyleString() + ")"	;		}

//    public Font getFont(boolean ctrl)	{ 		return ParseUtil.fontFromElement(getAsElement(), ctrl);	}
    public String getFontName()			{		return fontName;	}
    public void setFontName(String fn)	{		fontName = fn;		}
    public int getSize()    			{        return fontSize;    }
    public int getLineHeight()    		{        return (int) (1.4 * fontSize);    }
    public void setSize(int inSize)    	{        fontSize = inSize;    }
    public void setStyle(int inStyle)	{		fontStyle = inStyle;	}
    public String getStyleString()		{		return ParseUtil.styleToString(fontStyle);		}
    public String getJustificationString()	{	return ParseUtil.justificationToString(justification);	}
//    public void setJustification(String j)	{	justification = ParseUtil.stringToJustification(j);	}
    public void setColor(Color c)		{		foreColor = c;	}
    public void setBackColor(Color c)	{		backColor = c;	}

//    public void setFromElement(SElement element)
//    {
//    	if (element == null)	return;
//        String s = Defaults.FONT_NAME;
//        String str = element.getString("font");
//        int pos = str == null ? -1 : Arrays.binarySearch(AVAILABLE_FONT_NAMES, str);
//        if (str != null && (pos >= 0))
//            s = AVAILABLE_FONT_NAMES[pos];
//        setSize(element.getInt("size", Defaults.FONT_SIZE));
//        int style = ParseUtil.stringToStyle(element.getString("style"), false);
//        fontName = s;
//        fontStyle = style;
//  
//        foreColor = element.getColor("color", Defaults.FORE_COLOR);
//        backColor = element.getColor("background", Defaults.BACK_COLOR);
////        justification = ParseUtil.stringToJustification(element.getString("just"));
//        setTTName(element.getAttributeValue("name"));
//    }
//    
}
