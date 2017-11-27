package org.lib;

import java.awt.Font;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import javax.swing.text.StyleConstants;

public class ParseUtil
{
//----------------------------------------------------------------------------------------------
    public static void main(String[] args)
    {
    	String s1 = "0123&567\"9012'456<890>23";
    	String s2 = escapeXmlText(s1);
    	System.out.println(s1 + " escapes to " + s2);
    	String s3 = unescapeXmlText(s2);
    	System.out.println(s2 + " unescapes to " + s3);
    	if (s1.equals(s3)) System.out.println("Results were correct");
    	else System.out.println("Results were NOT correct");
    }

    private static final String[] sRawChars = 		{"&", 		"\"", 		"'", 		"<", 		">", 		"\n", 	System.getProperty("line.separator")};
    private static final String[] sEscapedChars = 	{"&amp;", 	"&quot;",	"&apos;", 	"&lt;", 	"&gt;", 	"&#xA;", "&#xA;" };


    private static boolean needsEscaping(String[] stringsToCheck, String theText)
    {
    	if (theText == null) return false;
    	for (int i = 0; i < stringsToCheck.length; i++)
			if (theText.contains(stringsToCheck[i])) return  true;
    	return false;
    }

    public static String unescapeXmlText(String text)
    {
    	if (text == null) return null;
    	if (!needsEscaping(sEscapedChars, text)) return text; // if no replacements are necessary, return original text
    	for (int i = 0; i < sEscapedChars.length; i++)
    		text = text.replaceAll(sEscapedChars[i], sRawChars[i]);
    	return text;
//        if (text.indexOf('<') >= 0) return text;			// dont double decode Note: this test has been moved to callers
//        return text.replaceAll("&quot;", "\"").replaceAll("&apos;", "'").replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&").replaceAll("&#xA;", System.getProperty("line.separator")) ; 
    }

    public static String escapeXmlText(String text)
    {
    	if (!needsEscaping(sRawChars, text)) return text; // if no replacements are necessary, return original text
    	StringWriter writer = new StringWriter(text.length() + 10);  // initialize a little larger
    	for (int i = 0; i < text.length(); i++)
		{
			char c = text.charAt(i);
			boolean foundCharToEscape = false;
			for (int j = 0; j < sRawChars.length; j++)
			{
				if (sRawChars[j].charAt(0) == c)
				{
					foundCharToEscape = true;
					writer.append(sEscapedChars[j]);
					break;
				}
			}
			if (!foundCharToEscape)
				writer.append(c);
		}
    	return writer.toString();
//        if (text.indexOf('<') < 0) return text;			// dont double encode Note: this test has been moved to callers
//        return text.replaceAll("&", "&amp;").replaceAll("\"", "&quot;").replaceAll("'", "&apos;").replaceAll("<", "&lt;").replaceAll("<", "&gt;").replaceAll(System.getProperty("line.separator"), "&#xA;");         
    }
    
    public static String escapeXmlAttribute(String attr)
    {
    	if (StringUtil.isEmpty(attr)) return "";
    	attr = attr.replaceAll("\"", "&quot;");
    	attr = attr.replaceAll("'", "&39;");		//NOTE AM 4/4/14 See http://stackoverflow.com/questions/2083754/why-shouldnt-apos-be-used-to-escape-single-quotes
    	return attr;
    }
  //----------------------------------------------------------------------------------------------
    public static int getNDecPlaces(String s)
    {
        String trimmed = s.trim();
        
        int ptLoc = s.indexOf(Intl.getInstanceDecimalFormatSymbols().getDecimalSeparator());
        return trimmed.length() - ptLoc - 1;
     }
    
    
    private static class EngineFormatterHolder extends ThreadLocal<NumberFormat>
    {
    	@Override public NumberFormat initialValue()
    	{
    		DecimalFormat fmt = new DecimalFormat("#.##########", new DecimalFormatSymbols(Locale.US));		//NOTE AM 4/10/12 review this -- this is saying xml doubles never get more than x decimal places. I think this is ok in most any of our use cases?
        	fmt.setGroupingUsed(false);
        	return fmt;
    	}
    }
    
    private static EngineFormatterHolder engineDoubleFormatters = new EngineFormatterHolder();		//AM thread-local formatters, as formatters are not thread-safe
    
    /** Avoids Double.toString() behavior of using 'E' notation on large numbers */
    public static String engineFormat(double d)
    {
    	return engineDoubleFormatters.get().format(d);			//TODO AM 4/10/12 test effeciency of this.
    }
    
//    public static String format(double value)
//    {
//        if (decimalFormat == null)
//        {
//            NumberFormat formatter = NumberFormat.getNumberInstance(Main.getFlowJoLocale());
//            decimalFormat = (DecimalFormat) formatter;
////              decimalFormat.applyPattern(decimalFormat.format(0.00000));
//        }
//        return decimalFormat.format(value);
//      }
    
    public static boolean getBool(String text) {
    	return getBool(text, false);
    }
    
    /**
     * getBool with null protected default value
     * @param text
     * @param deflt
     * @return
     */
    public static boolean getBool(String text, Boolean deflt)
    {
        if (text == null) {
        	if (deflt == null)
        		return false;
        	return deflt;
        }
        if ("1".equals(text))	return true;
        if (text.charAt(0) == 'T' || text.charAt(0) == 't')	return true;
        return false;     
    }

    public static boolean isInteger(String text)
    {
        if (text == null)            return false;
        try
        {
            Integer.parseInt(text);
            return true;
        }
        catch (NumberFormatException e)        {            return false;        }
    }
    public static int getHexInteger(String inString)
    {
        int result = 0;
        int val;
        if (inString != null)
        {
            inString = inString.trim().toUpperCase();
            for (char c : inString.toCharArray())
            {
               	if (c >= '0' && c <= '9')		val = c-'0';
              	else if (c >= 'A' && c <= 'F') 	val = 10 + c-'A';
               	else if (c >= 'A' && c <= 'F') 	val = 10 + c-'a';
               	else continue;
               	result = 16 * result + val;
            }
         }
        return result;
    }
    
    public static int getInteger(String inString)
    {
        int result = 0;
       if (inString != null)
        {
           if (inString.equals("4294967296"))        	return Integer.MAX_VALUE;
           inString = inString.trim();
            try
            {
                if (inString.length() > 0)
                    result = Integer.parseInt(inString);
            }
            catch (NumberFormatException ex)           
            {
            	return (int) getFloat(inString);
            }
        }
        return result;
    }
    
    public static int getInteger(String inString, int deflt)
    {
        int result = deflt;
        if (inString != null)
        {
            inString = inString.trim();
            try
            {
                if (inString.length() > 0)
                    result = Integer.parseInt(inString);
            }
            catch (NumberFormatException ex)            {}
        }
        return result;
    }
    public static long getLong(String inString)
    {
        long result = 0;
        if (inString != null)
        {
            inString = inString.trim();
            try
            {
                if (inString.length() > 0)
                    result = Long.parseLong(inString);
            }
            catch (NumberFormatException ex)
            {
            	try {
            		result = (long)Double.parseDouble(inString);
            	}
                catch (NumberFormatException e)        { result = 0;}
            }
        }
        return result;
    }

    public static boolean isNumber(String s)
    {
    	if ("0".equals(s)) 									return true;
    	if (s == null || "null".equals(s) || s.isEmpty())	return false;
    	try
    	{
    		s = s.trim();
    		if (s.endsWith("%"))
    			s = s.substring(0, s.length() - 1);

    		if (s.length() != 0)
    			Double.parseDouble(s);
    	}
    	catch (Exception e){	
    		try{
    			return !Double.isNaN(getNumber(s).doubleValue());
    		}
    		catch(Exception pe){
    			return false;
    		} 
    	}
    	return true;
    }

    public static boolean inCurlyBrackets(String s)
    {
        s = s.trim();
        return s.charAt(0) == '{' && s.charAt(s.length()-1) == '}';
    }

    public static boolean inAngleBrackets(String s)
    {
        s = s.trim();
        return (s.charAt(0) == '<' && s.charAt(s.length() - 1) == '>');
    }

    //I18N parser
    private static Number getNumber(String inString) throws ParseException{
    	return Double.parseDouble(inString);				//ApplicationInfo.getApplication().getFormatter()
    }
    
    //Doubles
    public static double getDouble(String inString)   { return getDouble(inString, 0.0);	}  
    public static double getDouble(String inString, double deflt)     
    {
    	if (inString == null || inString.length() == 0 || "null".equals(inString))	return deflt;
    	inString = inString.trim();
    	if (inString.endsWith("%"))		return getDouble(inString.substring(0, inString.length() - 1)) / 100.0;
    	double result = deflt;
    	try
    	{    		result =   Double.parseDouble(inString);        }
    	catch (NumberFormatException e)
    	{
    		try{
    			result = getNumber(inString).doubleValue();
    		}
    		catch(ParseException pe){
    			return Double.NaN;  //pe.printStackTrace();
    		}
    	}
    	return result;
    }
    
    //Floats
    public static float getFloat(String inString)	{ return getFloat(inString, 0.0f);	}
    public static float getFloat(String inString, float deflt)
    {
    	float result = deflt;
    	if (inString != null && !("null".equals(inString))){
    		try
	    	{
	    			inString = inString.trim();
	    			if (inString.length() != 0)
	    				result = Float.parseFloat(inString); 
	    	}
	    	catch (NumberFormatException e)
	    	{
	    		try{
	    			result = getNumber(inString).floatValue();
	    		}
	    		catch(ParseException pe){
	    			return Float.NaN;  //pe.printStackTrace();		//ALERT AM 12/5/12 change to deflt?  NaN seems like wrong behavior.
	    		}
	    	}
    	}
    	return result;
    }
	public static float[] getFloatArray(String separator, String target) 
	{
		if (target == null || target.length() == 0) return new float[] { 1f };
		//split out the first and last characters if they are [ or ].
		if(target.charAt(0)=='[') target=target.substring(1);
		if(target.charAt(target.length()-1)==']') target=target.substring(0, target.length()-1);
//		System.out.println("target string="+target);
		String[] parts = target.split(separator);
		float[] floats = new float[parts.length];
		for (int i = 0; i < parts.length; i++) 
			floats[i] = ParseUtil.getFloat(parts[i]);
		return floats;
	}
  //  public static boolean getBooleanAttribute(Element elem, String attribute)    {         return toBool(elem.getAttributeValue(attribute));    }
     public static boolean toBool(String s)    {        return "1".equals(s) || "true".equals(s);    }
    static public String boolToString(boolean truth)    {        return (truth) ? "1" : "0";    }


    public static String getSuffixSizedString(double inVal)			// 31K, 2M, 6.3B   was getStringForDiVaValue
    {
    	double value = inVal;
    	if (value == 0) return "0";
    	double absVal = Math.abs(value);
    	if (absVal < 1) return String.format("%.3f", value);				//TODO INTL?
    	if (absVal < 2) return String.format("%.2f", value);
    	if (absVal < 10) return String.format("%.1f", value);
    	if (absVal < 100) return Integer.toString((int)(value+0.5));
        String suffix = "";
        if (absVal >= 1000000000)       {     value = value / 1000000000;    suffix = "G";     }
        else if (absVal >= 1000000)     {     value = value / 1000000;       suffix = "M";     }
        else if (absVal >= 1000)        {     value = value / 1000;          suffix = "K";      }
//        if (value % 1 == 0)
//        {
//    	return Integer.toString((int)(value+0.5)) + suffix;

        absVal = Math.abs(value);
        if (absVal < 10)    return String.format("%.1f%s", value, suffix);				//TODO INTL?
    	return String.format("%.0f%s", value, suffix);

//        }
//        return Double.toString(value) + suffix;
    }
    // private static final String DEFAULT_FONT = "TimesRoman";
    private static final int DEFAULT_SIZE = 12;

    public static Font fontFromElement(SElement fontInfo)    {  	return fontFromElement(fontInfo, false);    }
    
   public static Font fontFromElement(SElement fontInfo, boolean forceItal)
    {
        if (fontInfo == null)
            return null;
        String font = fontInfo.getAttributeValue("font");
        String size = fontInfo.getAttributeValue("size");
      //  Color color = ColorUtil.colorFromString(fontInfo.getAttributeValue("color"));
        String style = fontInfo.getAttributeValue("style");
        // if (font != null && font.length() > 0) setFontFamily(font);
        int fontSize = DEFAULT_SIZE;
        if (size != null && size.length() > 0)
            fontSize = (Integer.parseInt(size));
        // if (color != null) setTextColor(color);
        int iStyle = ParseUtil.stringToStyle(style, forceItal);
        return new Font(font, iStyle, fontSize);
    }

    public static TextTraits fontToElement(Font inFont)
    {
    	TextTraits elem = new TextTraits();
        elem.setFontName(inFont.getFamily());
        elem.setSize(inFont.getSize());
        elem.setStyle(inFont.getStyle());
        return elem;
    }

    public static String styleToString(int style)
    {
      switch (style)
      {
            case Font.BOLD:                    return "bold";
            case Font.ITALIC:                  return "italic";
            case Font.BOLD + Font.ITALIC:      return "bold-italic";
       }
      return "plain";
    }
    
    

//    public static final int ALIGN_LEFT = 0, ALIGN_RIGHT = 1, ALIGN_CENTER = 2;

    public static String justificationToString(int inJust)
    {
        switch (inJust)
        {
            case StyleConstants.ALIGN_LEFT:              return "left";
            case StyleConstants.ALIGN_CENTER:            return "center";
            case StyleConstants.ALIGN_RIGHT:             return "right";
        }
        return "left";
    }

    public static String justificationToString(float inJust)
    {
        // if (inJust == Component.LEFT_ALIGNMENT)
        // return "left";
        if (inJust == StyleConstants.ALIGN_CENTER)          return "center";
        if (inJust == StyleConstants.ALIGN_RIGHT)           return "right";
        return "left";
    }

    public static int stringToAlignment(String inJust)
    {
        if ("right".equals(inJust))           return StyleConstants.ALIGN_RIGHT;
        if ("center".equals(inJust))          return StyleConstants.ALIGN_CENTER;
        return StyleConstants.ALIGN_LEFT;
    }
    public static int stringToStyle(DictKey inStyle, boolean forceItal)
    {
    	return stringToStyle(inStyle.getKeyString(), forceItal);
    }

    public static int stringToStyle(String inStyle, boolean forceItal)
    {
        int iStyle = forceItal ? Font.ITALIC: Font.PLAIN;
        if (inStyle == null || inStyle.length() == 0)    return iStyle;
        if (inStyle.indexOf("bold") >= 0)
            iStyle += Font.BOLD;
        if (inStyle.indexOf("italic") >= 0)			// || forceItal
            iStyle += Font.ITALIC;
        return iStyle;
    }
    //--------------------------------------------------------------------------------

    public static void dumpElement(SElement inElement)    {        dumpElement(inElement, " ");    }
    public static void identify(Object o)    {       System.err.println(o.getClass().getSimpleName() + "=" + o.hashCode());    }

    public static void dumpElement(SElement inElement, String indent)
    {
       StringBuilder s = new StringBuilder("This recurses incorrectly in line + 14");
       if (inElement == null)        {    s.append(indent + "ParseUtil.dumpElement: null");   }
	   else
	   {
			s.append(indent + "<" + inElement.getName() + ">   [ ");
			List<AttributeValuePair> l = inElement.getAttributeList();
			for (AttributeValuePair attr : l)
				s.append(attr.getAttribute() + "=" + attr.getValue() + " ");
			s.append(']');
			List<SElement> children = inElement.getChildren();
			if (children.size() > 0)
			{
				s.append(indent + ": ");
				for (SElement e : children)
					dumpElement(e, indent + "  ");		// should return string not dump it
			}
			s.append(indent + "</" + inElement.getName() + ">");
	}
     System.out.println(s.toString());
    			
    }

    public static void dumpChildren(SElement inElement)    {        dumpChildren(inElement, "");    }

    private static void dumpChildren(SElement inElement, String indent)
    {
        System.err.println(indent + "<" + inElement.getName() + ">");
        List<SElement> l = inElement.getChildren();
        if (l.size() > 0)
        {
            for (int j = 0; j < l.size(); j++)
            {
                SElement e = l.get(j);
                dumpChildren(e, indent + "  ");
            }
        }
        System.err.println(indent + "</" + inElement.getName() + ">");
    }

    static public String cleanse(String s) // restore line breaks after xml streaming
    {
        if (s == null)
            return null;
        StringBuffer text = new StringBuffer(s);
        for (int i = 0; i < text.length(); i++)
        {
            if (isCommand(i, s))
            {
                for (int j = i + 1; j < text.length(); j++)
                {
                    if (text.charAt(j) == '>' && (j < text.length() - 1) && text.charAt(j + 1) == ' ')
                    {
                        text.setCharAt(j + 1, '\n');
                        i = j + 1;
                        break;
                    }
                }
            }
        }
        return text.toString();
    }

    private static boolean isCommand(int i, String text)
    {
        final String key = "CMD";  //FJML.KEY_CMD;
        final String stat = "STATISTIC"; //FJML.STATISTIC_CMD;
        boolean isCommand = false;
        if (text.charAt(i) == '<')
            if (text.regionMatches(i + 1, key, 0, key.length()) || text.regionMatches(i + 1, stat, 0, stat.length()) || isCustomCommand(i + 1, text))
                isCommand = true;
        return isCommand;
    }

    public static final String CMD_TERMINATOR = "/>";
    public static boolean isCustomCommand(int index, String text)
    {
        char c = text.charAt(index++);
        while ((index < text.length() - 1) && c != ' ' && c != '\n')
        {
            if (text.regionMatches(index, CMD_TERMINATOR, 0, CMD_TERMINATOR.length()))
              	return true;
             c = text.charAt(index++);
        }
        return false;
    }

    /** parse attributes of the form 'id="value"' into the returned HashMap */
    public static HashMap<String,String> parseAttributes(String command)
    {
        int start = command.indexOf(' ') + 1;  		// skip up to the first space
        HashMap<String,String> attributes = new HashMap<String,String>();
        String item = command.substring(start).trim();
        while (true)
        {
        	int v1 = item.indexOf('=', 0);
        	int v2 = item.indexOf('"', v1) + 1; // require a quoted string value
        	int v3 = item.indexOf('"', v2);
            if (0 > v1 || v1 > v2 || v2 > v3)      break;		// syntax error or termination
            String name = item.substring(0, v1);
            String value = item.substring(v2, v3);
            value = ParseUtil.unescapeXmlText(value);
            attributes.put(name, value);			//NOTE AM 4/27/11 May need UrlUtil.urlDecode() on attribute values (see toAttributeString()) //8/7/12 AM added escape/ParseUtil.unescapeXmlText()
            item = item.substring(v3 + 1, item.length()).trim();
        }
        return attributes;
    }
    
    //NOTE AM 4/27/11 May need UrlUtil.urlEncode() on attribute values
    public static String toAttributeString(Map<String, String> attributes) {
    	StringBuffer str = new StringBuffer();
    	
    	for (Entry<String, String> entry : attributes.entrySet()) {
    		str.append(entry.getKey());
    		str.append("=\"");
    		str.append(escapeXmlText(entry.getValue()));
    		str.append("\" ");
    	}
    	
    	return str.toString();
    }
    
 

	/** Encode any characters greater than 256 as &#xNNNN; **/
    public static String convertToNCR(String s)
    {
    	// first make a quick pass to see if all chars are < 256
    	boolean needsNCR = false;
    	for (int i = 0; i < s.length(); i++)
    		if (s.codePointAt(i) >= 256) { needsNCR = true; break; }
    	if (!needsNCR) return s; // return original string, no numerical character reference needed

    	StringWriter newS = new StringWriter();
        for (int i = 0; i < s.length(); i++)
        {
        	int cpoint = s.codePointAt(i);
        	if (cpoint < 256) newS.append(s.charAt(i));
        	else newS.append("&#x").append(Integer.toHexString(cpoint)).append(';');
        }
        return newS.toString();
    }

    public static String convertFromNCR(String s)
    {
    	int index = s.indexOf("&#x");
    	if (index < 0) return s; // return original string, no numerical character reference found

    	int start = 0;
    	StringWriter newS = new StringWriter();
    	while (index > 0)
    	{
    		newS.append(s.substring(start, index)); // append leading up to &#x
    		start = index+3; // point to hex number after &#x
    		index = s.indexOf(";", start);
    		if (index > 0)
    		{
    			String ncr = s.substring(start, index);
    			String n = new String(Character.toChars(Integer.parseInt(ncr, 16)));
    			newS.append(n);
    			start = index+1;
    		}
    		index = s.indexOf("&#x", start);
    	}
    	newS.append(s.substring(start, s.length()));
        return newS.toString();
    }
//    public static String[] getPnEValues(String pneString)
//    {
//        String decadesString = "0";
//        // parse out the decades and the offset from the combined string
//        String offsetString = "1";
//        if (pneString != null && (pneString.length() > 0))
//        {
//            int commaIndex = pneString.indexOf(",");
//            // have to leave room for parameter before and after the comma
//            if (commaIndex < 1 || commaIndex == (pneString.length() - 1))
//            {
//                System.err.println("bad decadesOffsetString " + pneString);
//                decadesString = offsetString = "0";
//            }
//            else
//            {
//                decadesString = pneString.substring(0, commaIndex);
//                offsetString = pneString.substring(commaIndex + 1);
//            }
//        }
//        String[] result = { decadesString, offsetString };
//        return result;
//    }


    public static String byteCountToString(int bytes) {
    	double value;
    	String suffix;
    	
	    if (bytes < 1e3) {
	    	value = bytes; suffix = "B";
	    } else if (bytes < 1e6) {
    		value = bytes/1e3; suffix = "KB";
	    } else if (bytes < 1e9) {
	    	value = bytes/1e6; suffix = "MB";
	    } else {
	    	value = bytes/1e9; suffix = "GB";
	    }
	    
	    return String.format("%.2f%s", value, suffix);
    }
    public static int[] stringToIntArray(String string, int[]values) 
	{
		StringTokenizer tokenizer = new StringTokenizer(string);
		int i = 0;
		while (tokenizer.hasMoreTokens() && i < values.length)
			values[i++] = Integer.parseInt(tokenizer.nextToken());
		return values;
	}
    public static String intArrayToString(int[] values)
	{
		StringWriter writer = new StringWriter();
		for (int i = 0; i < values.length; i++)
			writer.append("" + values[i]).append(' ');
		return writer.toString();
	}
	public static boolean isFCSFloatKeyword(String key) 
	{
		if (key == null || key.length() == 0) return false;
		if (key.contains("$TIMESTEP") || key.contains("FJ_FCS_VERSION") || (key.startsWith("$P") && key.endsWith("G"))) return true;
		return false;
	}

	public static boolean isFCSIntKeyword(String key) 
	{
		if (key == null || key.length() == 0) return false;
		if ("$TOT".equals(key) || "$PAR".equals(key)) return true;
		if (key.startsWith("$P"))
			return  key.endsWith("R") || key.endsWith("B");
		return false;
	}
	
	public static String base64Encode(String s) {		return base64Encode(s.getBytes());	}
	public static String base64Encode(byte[] bytes) {		return Base64.getEncoder().encodeToString(bytes);	}
	public static byte[] base64Decode(String s) throws IOException {		return Base64.getDecoder().decode(s);	}
	
	public static String toBinary(int val) {
		StringBuilder sb = new StringBuilder();
		for (int i=31; i>=0; i--)
			sb.append(((val >> i) & 1) > 0 ? '1' : '0');
		return sb.toString();
	}
//	
//	public static final Map<Character, Character> SUPERSCRIPT_MAP = MapUtil.map(
//				'0', '\u2070',
//				'1', '\u00b9',
//				'2', '\u00b2',
//				'3', '\u00b3',
//				'4', '\u2074',
//				'5', '\u2075',
//				'6', '\u2076',
//				'7', '\u2077',
//				'8', '\u2078',
//				'9', '\u2079'
//			);
//	
//	public static String asSuperscript(int n)
//	{
//		char[] instr = String.valueOf(n).toCharArray();
//		char[] outstr = new char[instr.length];
//		for (int i=0; i<instr.length; i++)
//			outstr[i] = SUPERSCRIPT_MAP.get(instr[i]);
//		return new String(outstr);
//	}
//	
    public static void cleanupWorkspaceElement(SElement elem)
	{
		if (elem.getName().equals("Content"))//FJML.Content
		{
			String txt = elem.getText();
			if (!txt.isEmpty() && txt.contains("<"))
				elem.setText(ParseUtil.escapeXmlText(txt));
		}
		for (SElement child : elem.getChildren())
			cleanupWorkspaceElement(child);
	}
    
    // unused but useful code
//  private static DecimalFormat decimalFormat = InternationallyAwareNumberFormattingMethods.decimalFormatter();
    
//  // Encodes the given text by performing the following (returns XML safe string):
//  //   1.  generate a 31-bit random seed using Math.random()
//  //   2.  generate a 2496 byte "one-time pad" (really a two time pad here ;) from the seed using the Mersenne Twister algorithm
//  //   3.  XOR the given text's bytes with the pad (creating additional pads if necessary)
//  //   4.  create a low security 'magic' pad
//  //   5.  use the magic pad to XOR a byte array containing the seed and the Mersenne XOR'd bytes
//  //   6.  return the BASE64 encoded byte array
//  // The end result is totally random, but the function could be reverse engineered with moderate effort
//  public static String encodeText(String text)
//  {
//      int seed = (int) (Math.random() * Integer.MAX_VALUE);
//      byte[] decodedBytes = text.getBytes();
//      byte[] encodedBytes = new byte[decodedBytes.length + 4];
//      ByteBuffer.wrap(encodedBytes, 0, 4).putInt(seed);
//      
//      byte[] mt = getMersenneTwisterBytes(seed);
//      for (int i = 0; i < decodedBytes.length; i++)
//      {
//          if (i % mt.length == 0 && i > 0)
//              mt = getMersenneTwisterBytes(mt[mt.length - 1] * seed);
//          encodedBytes[i + 4] = (byte) (decodedBytes[i] ^ mt[i % mt.length]);
//      }
//      
//      int padA = 0x434451F5;
//      int padB = 0xB473A410;
//      int padC = padA * padB;
//      byte p1 = (byte) padC; 
//      byte p2 = (byte) (padC >> 8); 
//      byte p3 = (byte) (padC >> 16); 
//      byte p4 = (byte) (padC >> 24); 
//      for (int i = 0; i < encodedBytes.length; i++)
//      {
//          switch (i % 4)
//          {
//              case 3 : encodedBytes[i] ^= p1; break;
//              case 2 : encodedBytes[i] ^= p2; break;
//              case 1 : encodedBytes[i] ^= p3; break;
//              case 0 : encodedBytes[i] ^= p4;
//          }
//      }
//      return new BASE64Encoder().encode(encodedBytes);
//  }
  
//  // used to decode text encoded with ParseUtil.encodeText(String).  See that function for explaination of encryption algorithm
//  public static String decodeText(String text)
//  {
//      byte[] encodedBytes;
//      try
//      {
//          encodedBytes = new BASE64Decoder().decodeBuffer(text);
//      }
//      catch (IOException ex)
//      {
//          ex.printStackTrace();
//          return "";
//      }
//      if (encodedBytes.length < 5)
//          return "";
//      byte[] decodedBytes = new byte[encodedBytes.length - 4];
//      int padA = 0x434451F5;
//      int padB = 0xB473A410;
//      int padC = padA * padB;
//      byte p1 = (byte) padC; 
//      byte p2 = (byte) (padC >> 8); 
//      byte p3 = (byte) (padC >> 16); 
//      byte p4 = (byte) (padC >> 24); 
//      for (int i = 0; i < encodedBytes.length; i++)
//      {
//          switch (i % 4)
//          {
//              case 3 : encodedBytes[i] ^= p1; break;
//              case 2 : encodedBytes[i] ^= p2; break;
//              case 1 : encodedBytes[i] ^= p3; break;
//              case 0 : encodedBytes[i] ^= p4;
//          }
//      }
//
//      int seed = ByteBuffer.wrap(encodedBytes, 0, 4).getInt();
//      
//      byte[] mt = getMersenneTwisterBytes(seed);
//      for (int i = 0; i < encodedBytes.length - 4; i++)
//      {
//          if (i % mt.length == 0 && i > 0)
//              mt = getMersenneTwisterBytes(mt[mt.length - 1] * seed);
//          decodedBytes[i] = (byte) (encodedBytes[i + 4] ^ mt[i % mt.length]);
//      }
//      
//      return new String(decodedBytes);
//  }    
  
//  // returns an array of 2496 random bytes from a given seed.  This function's utility comes from the fact that
//  // the random bytes will be exactly the same for any given seed. 
//  public static byte[] getMersenneTwisterBytes(int seed)
//  {
//      final int M = 397;
//      final int N = 624;
//      final int UM = 0x80000000;
//      final int LM = 0x7FFFFFFF;
//      int[] mt = new int[N];
//      int[] mag01 = new int[] { 0, 0x9908B0DF};
//
//      mt[0] = seed & 0xFFFFFFFF;        
//      for (int i = 1; i < N; i++)
//          mt[i] = (int) (69069 * (long) mt[i - 1]) & 0xFFFFFFFF;
//      
//      int y;
//      int kk;
//      for (kk = 0; kk < N - M; kk++)
//      {
//          y = (mt[kk] & UM) | (mt[kk + 1] & LM);
//          mt[kk] = mt[kk + M] ^ (y >>> 1) ^ mag01[y & 0x1]; 
//      }
//      for (; kk < N - 1; kk++)
//      {
//          y = (mt[kk] & UM) | (mt[kk + 1] & LM);
//          mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ mag01[y & 0x1];
//      }
//      y = (mt[N - 1] & UM) | (mt[0] & LM);
//      mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ mag01[y & 0x1];
//      
//      // returning untempered results as we create a new (potentially identical) array each time
//      ByteBuffer byteBuf = ByteBuffer.allocate(4 * N);
//      // we through in some magic XORing just to throw off cracking with a traditional Mersenne Twister
//      int padA = 0x190890CC;
//      int padB = 0xE25B1A01;
//      int padC = padA ^ padB;
//      for (int i = 0; i < N; i++)
//          byteBuf.putInt(mt[i] ^ padC);
//      return byteBuf.array();
//  }

}
