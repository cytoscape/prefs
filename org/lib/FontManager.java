
package org.lib;

///RibesExport

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Hashtable;

import javax.swing.text.StyleConstants;

/**
 * This class contains static methods for caching Fonts. Once defaultFontSize has been set, fonts may be referred to by a size relative to defaultFontSize.
 */
public class FontManager
{
//
//	/** The font name used by getFont/2 */
	public static String defaultFontName = "dialog";
	public static int defaultFontSize = 12;
	static Hashtable<String, Font> fontCacheHT = new Hashtable<String, Font>();
//
//	public static void setDefaultFontName(String face)	{		defaultFontName = face;	}
//	public static String getDefaultFontName()			{		return defaultFontName;	}
//
//	public static void setDefaultFontSize(int z)		{		defaultFontSize = z;	}
//	public static int getDefaultFontSize()				{		return defaultFontSize;	}
//
//	public static Font getAbsoluteFont(int style, int size)				{		return getFont(defaultFontName, style, size);	}
	public static Font getRelativeFont(int style, int sizeIncrement)	{		return getFont(defaultFontName, style, defaultFontSize + sizeIncrement);	}
	public static Font getRelativeFont(int sizeIncrement)				{		return getFont(defaultFontName, Font.PLAIN, defaultFontSize + sizeIncrement);	}
//	public static Font getRelativeFont(String face, int style, int sizeIncrement)	{		return getFont(face, style, defaultFontSize + sizeIncrement);	}
//	public static Font getRelativeFont(Font xfont, int sizeIncrement)	{		return getFont(xfont.getName(), xfont.getStyle(), xfont.getSize() + sizeIncrement);	}
//
//	public static Font getFont(int style, int size)						{		return getFont(defaultFontName, style, size);	}
//	public static Font getFont(SElement inElem)
//	{
//		if (inElem == null) return getRelativeFont(0);
//		String font = defaultFontName;
//		String s = inElem.getAttributeValue("font");
//		if (s != null) font = s;
//		s = inElem.getAttributeValue("size");
//		int size = defaultFontSize;
//		if (s != null) size = Integer.parseInt(s);
//		int style = Font.PLAIN;
//		s = inElem.getAttributeValue("style");
//		if (s != null) style = Integer.parseInt(s);
//		return getFont(font, style, size);
//	}
//
	/**
	 * Get a font with the default fontName, size and style. This is a font caching mechanism.
	 */
	public static Font getFont(String face, int style, int size)
	{
		String id = style + "-" + size + face;
		Font font;
		if ((font = fontCacheHT.get(id)) == null)
		{
			font = getNewFont(face, style, size);
			fontCacheHT.put(id, font);
			// System.out.println("font: " + font +"\n style: " +style +" z: " + size +" face: " + face);
		}
		return font;
	}

	static Hashtable<String, Font> derivedHT = new Hashtable<String, Font>();

	static Font getNewFont(String name, int style, int size)
	{
		Font baseFont, font;
		if ((baseFont = derivedHT.get(name)) == null)
		{
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			Font fa[] = ge.getAllFonts();
			for (int i = 0; i < fa.length; i++)
			{
				if (fa[i].getFontName().equals(name))
				{
					baseFont = fa[i];
					break;
				}
			}
			if (baseFont == null)
			{
				baseFont = new Font("dialog", style, size);
				// System.out.println("can't find " + name + " using " + baseFont);
			}
			derivedHT.put(name, baseFont);
		}
		font = baseFont.deriveFont(style, size);
		return font;
	}
//
//	public static void dumpAllFonts()
//	{
//		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//		Font fa[] = ge.getAllFonts();
//		System.out.println("all fonts: ");
//		for (int i = 0; i < fa.length; i++)
//			System.out.println(i + " " + fa[i].getFontName());
//	}
//
//	public static TextTraits getDefaultFontElement()
//	{
//		return new TextTraits("", defaultFontSize, Font.PLAIN, ColorUtil.kColorOfNoFill, Color.black, StyleConstants.ALIGN_LEFT);
//	}
}
