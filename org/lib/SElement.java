package org.lib;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

@SuppressWarnings("unchecked")

public class SElement implements Element, Cloneable
{
	private static final long serialVersionUID = 1L;
	protected String fName;
	protected List<String> fAttributeNames;
	protected List<String> fAttributeValues;
	protected List<SElement> fContent = new ArrayList<SElement>();
	private SElement fParent;

	protected SElement()							{	init(10);	}
	public SElement(String name)					{	this();	fName = name;	}
	public SElement(String name, int capacity)		{	fName = name; 	init(capacity);	}
	public SElement(String name, String namespace)  {	this(name);	}

	private void init(int initialCapacity)
	{
		fAttributeNames = new ArrayList<String>(initialCapacity);
		fAttributeValues = new ArrayList<String>(initialCapacity);
	}
   	public List<String> getAttributeNamesCopy()	{	return new ArrayList<String>(fAttributeNames);	} // returning a copy because no one must be able to modify this list
   	public String getAttributeName(int index)	{	return (index < fAttributeNames.size()) ? fAttributeNames.get(index) : null; }
   	public String getAttributeValue(int index)	{	return (index < fAttributeValues.size()) ? fAttributeValues.get(index) : null; }
	public void setAttribute(String name, String inValue)
	{
		String value = inValue;
		if (name == null) return;
		if (value == null) value = "";
		int index = getIndexOfAttribute(name);
		if (index >= 0)
			fAttributeValues.set(index, value);
		else
		{
			fAttributeNames.add(name);
			fAttributeValues.add(value);
		}
	}
	public void setAttributes(List<AttributeValuePair> attrs)
	{
		for (AttributeValuePair avp : attrs)
			setAttribute(avp.getAttribute(), avp.getValue());
	}

	private int getIndexOfAttribute(String name)
	{
		if (name == null)	return -1;
		for (int i = 0; i < fAttributeNames.size(); i++)
			if (name.equals(fAttributeNames.get(i))) return i;
		return -1;
	}
	public List<String> getAttributeNames()		{ return fAttributeNames;	}
	public List<String> getAttributeValues()	{ return fAttributeValues;	}
	protected void setParent(SElement par)		{ fParent = par;	}
	public void addContent(SElement element)
	{
		if (element == null) return;
		if (element == this) 
			throw new RuntimeException("Cannot add SElement to itself");
		fContent.add(element);
		element.setParent(this);
	}

	public int getNumberOfAttributes()	{	return fAttributeNames.size();	}
	public String getAttributeValue(String key)
	{
		if (key == null)	return null;
		for (int i = 0; i < fAttributeNames.size(); i++)
			if (key.equals(fAttributeNames.get(i)))
				return fAttributeValues.get(i);
		return null;
	}

	public String getName()	{		return fName;	}

	public List<SElement> getChildren(String childName)
	{
		List<SElement> result = new ArrayList<SElement>();
		for (int i = 0; i < fContent.size(); i++)
		{
			SElement element = fContent.get(i);
			if (childName.equals(element.getName()))
				result.add(element);
		}
		return result;
	}

	public List<SElement> getChildren()	{		return fContent;	}

	public SElement(SElement e)
    {
        this();
        fName = e == null ? "NA" : e.getName();
        copy(e);
    }
   
    public SElement(String name, HashMap<String, String> attrs)
    {
        this(name);
		Set<String>	keys = attrs.keySet();
 		for (String key : keys)
		{
			String value = attrs.get(key);
			if (value != null)
				setKey(new AttributeValuePair(key, value));
		}		
    }

    public SElement(Image image)
	{
        this("Binary");
	    ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
		try
		{
			ObjectOutputStream oos = new ObjectOutputStream(bos);
		    oos.writeObject(new ImageIcon(image));
		    oos.flush(); 
		    oos.close(); 
		    bos.close();

			char[] chars = Base64Coder.encode(bos.toByteArray());
			setAttribute("dt:dt", "binary.base64");
			setAttribute("type", image.getClass().toString());
			setInt("width", image.getWidth(null));
			setInt("height", image.getHeight(null));
			setText(new String(chars));
		}
		catch(Exception e)	{ setText("Error: " + e.toString());	}
	}
    boolean confirmAttribute(String attr, String val)	{	return val != null && val.equals(getAttributeValue(attr)); }
    
    public Image getImage()
    {
		if (confirmAttribute("dt:dt", "binary.base64"))
		{
			String raw64 = fText.substring(1);
			byte[] bytes = Base64Coder.decode(raw64);
		    ByteArrayInputStream bis = new ByteArrayInputStream(bytes); 
			ObjectInputStream ois;
			try
			{
				ois = new ObjectInputStream(bis);
				ImageIcon t = (ImageIcon) ois.readObject ();  
			    return t.getImage();   
			} catch (IOException e)
			{
				e.printStackTrace();
			} catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		return null;	
    }
    
		
	public void replaceNameDeep(String oldName, String newName)
    {
        for (SElement channelElement : getChildren())
        {
            if (oldName.equals(channelElement.getAttributeValue("name")))
                 channelElement.setAttribute("name", newName);
           channelElement.replaceNameDeep(oldName, newName);
        }
    }
    
    public boolean getBool(String packageName, String inName)
    {
        SElement elem = getChild(packageName);
        if (elem != null)
        {
            String s = elem.getAttributeValue(inName);
            if (s != null)
                return "1".equals(s) || "true".equals(s);
        }
        return getBool(inName);
    }

    public boolean getBool(String inName)
    {
        String s = getAttributeValue(inName);
        return "1".equals(s) || "true".equals(s);
    }

    @Override public String toString()    {    return getXMLString();	} //  "[" + getName() + "]";    }

    public boolean getBool(String inName, boolean defaultIfAbsent)
    {
        String value = getAttributeValue(inName);
        if (value == null)
            return defaultIfAbsent;
        return "1".equals(value) || "true".equals(value);
    }

    public boolean getBool(String packageName, String inName, boolean defaultIfAbsent)
    {
        SElement elem = getChild(packageName);
        String value = getAttributeValue(inName);
        if (elem != null)
            value = elem.getAttributeValue(inName);
        if (value == null)
            value = getAttributeValue(inName);
        if (value == null)
            return defaultIfAbsent;
        return "1".equals(value) || "true".equals(value);
    }

    public void setBool(String packageName, String name, boolean value)    	{      if ( getChild(packageName) != null)   getChild(packageName).setBool(name, value);    }
    public void setBool(String name, boolean value)    	{        setAttribute(name, value ? "1" : "0");    }
    public void toggleAttribute(String name)    		{        setBool(name, !getBool(name));    }
    public int getInt(String inName)  					{ 		return getInt(inName, 0);	}
    public int getInt(String packageName, String inName){    	return getInt(packageName, inName, 0);    }
    public int getInt(String inName, int defaultIfAbsent)
    {
        String s = getAttributeValue(inName);
        try
        {              return (s == null || s.length() == 0) ? defaultIfAbsent : Integer.parseInt(s);        }
        catch (NumberFormatException ex)        {     	return (int) ParseUtil.getFloat(s);        }
    }

    public int getInt(String packageName, String inName, int defaultIfAbsent)
    {
        String s = null;
        try
        {
            SElement elem = getChild(packageName);
             if (elem != null)
                s = elem.getAttributeValue(inName);
            if (s == null)
                s = getAttributeValue(inName);
            return (s == null) ? defaultIfAbsent : Integer.parseInt(s);
        }
        catch (NumberFormatException ex)        {    	return (int) ParseUtil.getFloat(s);     }
    }

    public void setInt(String name, int value)    {        setAttribute(name, Integer.toString(value));    }
    public void setInt(String packageName, String inName, int value)
    {
        Element elem = getChild(packageName);
        if (elem != null)    elem.setAttribute(inName, "" + value);
        else				 setAttribute(inName, "" + value);
    }

//    public String getString(String packageName, String inName)
//    {
//            return getString(packageName, inName, "");
//    }
    
    public String getString(String packageName, String inName, String defaultIfAbsent)
    {
        SElement elem = packageName == null ? null : getChild(packageName);
        String s = getAttributeValue(inName);
        String s2 = null;
        if (elem != null)
            s2 = elem.getAttributeValue(inName);
        return (s2 == null) ? ((s == null) ? defaultIfAbsent : s) : s2;
    }

    public String getString(String inName)    {    	return getString(null, inName, "");    }

    static private Pattern nameReplacerPattern = Pattern.compile("[^\\p{Alnum}:-]");
    
    public Element setName(String inName)
    {
    	String newName = inName;
    	newName = SElement.nameReplacerPattern.matcher(newName).replaceAll("_");
    	if (! (Character.isLetter(newName.charAt(0)) || newName.charAt(0) == '_'))
    		newName = "_" + newName;
    	fName = newName;
    	return this;
    }

    public void setAttributeNS(String namespace, String name, String value)  {            setAttribute(name, value);    }
    public void setStringIf(String name, String inValue)
    {
         if (inValue != null)      
        	setString(name, inValue);
    }

    public void setString(String name, String inValue)
    {
    	String value = inValue;
        if (value == null)            value = ""; // avoid setting a null attribute value
        setAttribute(name, value);
    }

    public void setString(String packageName, String name, String inValue)
    {
    	String value = inValue;
        if (value == null)            value = ""; // avoid setting a null attribute value
        Element elem = getChild(packageName);
        if (elem != null)           elem.setAttribute(name, value);
        else            			setAttribute(name, value);
    }

    public float getFloat(String inName)
    {
        String s = getAttributeValue(inName);
        return (s == null) ? 0.0f : ParseUtil.getFloat(s);
    }

    public void setFloat(String packageName, String inName, float f)
    {
        Element elem = getChild(packageName);
        if (elem != null)   elem.setAttribute(inName, "" + f);
        else	            setAttribute(inName, "" + f);

    }
    public void setFloat(String inName, float f)    {        setAttribute(inName, "" + f);    }
    public void setDouble(String name, double d)    {        setAttribute(name, Double.toString(d));    }
    public float getFloat(String packageName, String inName)	{ return getFloat(packageName, inName, 0.0f);	}
    public float getFloat(String packageName, String inName, float defaultIfAbsent)
    {
        try
        {
            String s = null;
            SElement elem = getChild(packageName);
            if (elem != null)        s = elem.getAttributeValue(inName);
            if (s == null)           s = getAttributeValue(inName);
            return (s == null) ? defaultIfAbsent : ParseUtil.getFloat(s);
        }
        catch (NumberFormatException ex)        {            return defaultIfAbsent;        }
    }

    public float getFloat(String inName, float defaultIfAbsent)
    {
        try
        {
            String s = getAttributeValue(inName);
            return (s == null) ? defaultIfAbsent : ParseUtil.getFloat(s);
        }
        catch (NumberFormatException ex)        {            return defaultIfAbsent;        }
    }

    public double getDouble(String inName)
    {
        String s = getAttributeValue(inName);
        return (s == null) ? 0.0 : ParseUtil.getDouble(s);
    }

    public double getDouble(String packageName, String inName)    {        return getDouble(packageName, inName, 0.0);    }
    public double getDouble(String inName, double defaultIfAbsent)
    {
        try
        {
            String s = getAttributeValue(inName);
            return (s == null) ? defaultIfAbsent : ParseUtil.getDouble(s);
        }
        catch (NumberFormatException ex)        {            return defaultIfAbsent;        }
    }

    public double getDouble(String packageName, String inName, double defaultIfAbsent)
    {
        try
        {
            String s = null;
            SElement elem = getChild(packageName);
            if (elem != null)
                s = elem.getAttributeValue(inName);
            if (s == null)
                s = getAttributeValue(inName);
            return (s == null) ? defaultIfAbsent : ParseUtil.getDouble(s);
        }
        catch (NumberFormatException ex)        {            return defaultIfAbsent;        }
    }

     public void setDouble(String packageName, String inName, double value)
    {
        Element elem = getChild(packageName);
        if (elem != null)       elem.setAttribute(inName, Double.toString(value));
        else		            setAttribute(inName, Double.toString(value));
    }
     //----------------------------------------------------------------------------------------
     public Rectangle getRect()    		{        return new Rectangle(getInt("x"), getInt("y"), getInt("width"), getInt("height"));    }
     public void setRect(int x, int y, int w, int h)    {     setInt("x", x); setInt("y", y); setInt("width", w); setInt("height", h);    }
     public Point getPoint()    			{        return new Point(getInt("x"), getInt("y"));  }
     public void setPoint(Point pt) 	{      	setInt("x", pt.x); setInt("y", pt.y);  }
//     public ScalePoint getScalePoint() 	{     return new ScalePoint(getDouble("x"), getDouble("y"));  }
//     public void setScalePoint(ScalePoint pt) {   setDouble("x", pt.getX());  setDouble("y", pt.getY());  }
 
    static public SElement makeRect(String string, Rectangle bounds)
    {
        SElement newElem = new SElement(string);
        newElem.setInt("x", bounds.x);
        newElem.setInt("y", bounds.y);
        newElem.setInt("width", bounds.width);
        newElem.setInt("height", bounds.height);
        return newElem;
    }

    //----------------------------------------------------------------------------------------
   public Rectangle readWorkspaceBounds()
    {
        // new way
    	SElement child = getChild("WindowPosition");
        if (child != null)
        {
            SElement selem = new SElement(child);
            return selem.getRect();
        }
        // old way
        if (getAttributeValue("wswidth") != null && getAttributeValue("wsheight") != null
                && getAttributeValue("wsx") != null && getAttributeValue("wsy") != null)
        {
            SElement e = new SElement(this);
            int wswidth = e.getInt("wswidth");
            int wsheight = e.getInt("wsheight");
            int wsx = e.getInt("wsx");
            int wsy = e.getInt("wsy");
            return new Rectangle(wsx, wsy, wswidth, wsheight);
//            		Globals.putRectOnScreen(new Rectangle(wsx, wsy, wswidth, wsheight));
        }
        // neither format found, return some default
        return new Rectangle(50, 50, 400, 200);
    }

//   public WindowStateElement getWindowPosition()   {       return new WindowStateElement(getWindowState());   }
//   
//   public WindowState getWindowState()
//   {
//       SElement windowPositionElement = getChild("WindowPosition");
//       if (windowPositionElement == null)            return null;
//       Rectangle r = windowPositionElement.getRect();
////       if (r.width <= 0 || r.height <= 0) 			return null;// if not found the width and height are 0
//       boolean isDisplayed = windowPositionElement.getBool("displayed");
//       String panelState = windowPositionElement.getString("panelState");
//       return new WindowState(r, isDisplayed, panelState);
//   }
//   
   public Rectangle getWindowPositionBounds()
   {
       SElement windowPositionElement = getChild("WindowPosition");
       if (windowPositionElement == null)            return null;
       Rectangle r = windowPositionElement.getRect();
       if (r.width <= 0 || r.height <= 0) 			return null;// if not found the width and height are 0
       return r;
   }
   
    public boolean getWindowPositionOpen(String name)
    {
        SElement e = getChild(name);
        if (e == null)            return false;
        SElement windowPositionElement = e.getChild("WindowPosition");
         return windowPositionElement.getBool("displayed");
    }

    static public SElement makeWindowPosition(Rectangle bounds, boolean displayed)
    {
        SElement newElem = makeRect("WindowPosition", bounds);
        newElem.setBool("displayed", displayed);
        return newElem;
    }
    //----------------------------------------------------------------------------------------
//    public Color getColor(String inName)    {        return ColorUtil.colorFromString(getAttributeValue(inName));    }
//    public Color getColor(String inName, Color defaultColor)    {   return ColorUtil.colorFromString(getAttributeValue(inName), defaultColor);    }
//    public Color getColor(String packageName, String inName, Color defaultColor)    {   return ColorUtil.colorFromString(getString(packageName, inName, "000000"), defaultColor);    }

//    public Color getColor(String packageName, String inName)
//    {
//        SElement elem = getChild(packageName);
//        if (elem != null)
//            return ColorUtil.colorFromString(elem.getString(inName));
//        return ColorUtil.colorFromString(getAttributeValue(inName));
//    }

//    public void setColor(String inName, Color value)    {        setAttribute(inName, ColorUtil.colorToString(value));    }
//    public void setColor(String packageName, String inName, Color value)
//    {
//        Element elem = getChild(packageName);
//        String s = ColorUtil.colorToString(value);
//        if (elem != null)           elem.setAttribute(inName, s);
//        else			            setAttribute(inName, s);
//    }

   public void setKey(AttributeValuePair key)   	{   setAttribute(key.getAttribute(), key.getValue());   	}
   
   public void setAttributeConditional(String name, String inValue)
   {
        if (null == inValue)    removeAttribute(name);
        else 					setString(name, inValue);
   }

    public SElement setPackageAttribute(String packageName, String name, String inValue)
    {
    	String value = inValue;
        if (name == null)            return null;
        if (value == null)            value = "";
        SElement elem = getChild(packageName);
        if (elem != null)
            elem.setAttribute(name, value);
        return this;
    }
    //----------------------------------------------------------------------------------------
    public boolean equals(SElement inOrig)
    {
        // Is the text the same?
        if (!getText().equals(inOrig.getText()))     return false;
        // Are attributes the same?
        List<AttributeValuePair> origAttributes = inOrig.getAttributeList();
        List<AttributeValuePair> attributes = getAttributeList();
        int size = attributes.size();
        int origSize = origAttributes.size();
        if (size != origSize)			            return false;
        for (int i = 0; i < size; i++)
        {
            AttributeValuePair attr = attributes.get(i);
            boolean found = false;
            for (int j = 0; j < origSize && !found; j++)
            {
            	AttributeValuePair orig = origAttributes.get(j);
                found = attr.getAttribute().equals(orig.getAttribute()) && attr.getValue().equals(orig.getValue());
            }
            if (!found)                return false;
        }
        // Are child elements the same?
        List<SElement> subElements = getChildren();
        List<SElement> origSubElements = inOrig.getChildren();
        size = subElements.size();
        origSize = origSubElements.size();
        for (int i = 0; i < size; i++)
        {
            SElement elem = subElements.get(i);
            if (!equalElementInList(elem, origSubElements))
                return false;
        }
        return true;
    }

    private boolean equalElementInList(SElement elem, List<SElement> list)
    {
    	for (SElement origSub : list)
    		if (origSub.equals(elem))	return true;
        return false;
    }

    // Diffs the attribute values of this and otherElement, returning the differing attributes
    public List<AttributeValuePair> diffAttributeValues(SElement otherElement)
    {
        if (otherElement == null)            return null;
        List<AttributeValuePair> attributes = getAttributeList();
        List<AttributeValuePair> diffAttributes = new ArrayList<AttributeValuePair>();
        for (AttributeValuePair attrValPair : attributes)
        {
            String attr = attrValPair.getAttribute();
            if ("x".equals(attr) || "y".equals(attr)) continue;
            String val = attrValPair.getValue();
            String otherVal = otherElement.getAttribute(attr);
//            if (otherVal == null)                continue; // a reasonable alternative would be to add the attribute if it doesn't exist
            if (val.equals(otherVal)) 			continue;
            if (ParseUtil.isNumber(val) && ParseUtil.getDouble(val) != 0 && ParseUtil.getDouble(val) == ParseUtil.getDouble(otherVal)) continue;
            
        	 diffAttributes.add(new AttributeValuePair(attrValPair.getAttribute(), attrValPair.getValue()));
         }
        return diffAttributes;
    }
    
    
    public SElement diff(SElement other)			// TODO -- ignoring text section differences & assuming only one child with a given name per level!
    {
    	SElement output = null;
    	List<AttributeValuePair> attrDiffs = diffAttributeValues(other);
    	List<SElement> childDiffs = getChildDiffs(other);
 
    	if (attrDiffs.size() > 0 || childDiffs.size() > 0 && true)
    	{
    		output = new SElement(getName());
    		if (attrDiffs.size() > 0) 	output.setAttributes(attrDiffs);
    		if (childDiffs.size() > 0) 	output.addContent(childDiffs);
    	}
    	return output;
    }

    public List<SElement> getChildDiffs(SElement other)
    {
    	List<SElement> childDiffs = new ArrayList<SElement>();
    	for (SElement child : getChildren())
    	{
    		if ("DataLayer".equals(child.getName()) ) continue;
    		SElement subOther = other.getChild(child.getName());
    		List<SElement> multipleChoice = other.getChildren(child.getName());
    		if (multipleChoice.size() > 1)
    		{
    			for (SElement e : multipleChoice)			// if there are more than one element of the right tag, use OUR name attribute to discriminate
    			{
    				if (e.getString("name").equals(child.getString("name")))
    					subOther = e;
//    				if (e.getString("sampleID").equals(child.getString("sampleID")))
//    					subOther = e;
    			}
    		}
    		if (subOther != null)
    		{
    			SElement dif = child.diff(subOther);
    			if (dif != null)
    				childDiffs.add(dif);
    			
    		}
    	}
    	return childDiffs;
    }
    static String[] attrsToAvoid = new String[] { "x", "y", "width", "height" };
    
    public boolean skip(String inS)    
    {         
    	for (String s : attrsToAvoid)
    		if (s.equals(inS)) return true;
    	return false;
    }

    public void overwrite(SElement diffs)
    {
    	if (diffs == null || false) return;
    	List<String> attrs = diffs.getAttributeNames();
    		
    	for (String d : attrs)
    	{
    		if (skip(d) || (null == getAttributeValue(d))) continue;
    		setAttribute(d, diffs.getAttributeValue(d));
    	}
    	
    	for (SElement childDif : diffs.getChildren())
    	{
    		String name = childDif.getName();
    		List<SElement> options = getChildren(name);
    		SElement child = null;
    		if (options.size() > 1)
    		{
    			for (SElement e : options)
    			{
    				if (e.getString("name").equals(childDif.getString("name")))
    					child = e;
    			}
    		}
    		else child = getChild(name);
    		
    		SElement analog = child;
    		if (analog != null)					// don't add elements that don't correlate with an existing one
    			analog.overwrite(childDif);
    	}
    }

//    public void overwriteAttributes(List<AttributeValuePair> diffAttributes)
//    {
//        if (diffAttributes == null || diffAttributes.size() == 0)            return;
//        for (AttributeValuePair attr : diffAttributes)
//            setAttribute(attr.getAttribute(), attr.getValue());
//    }

	//-----------------------------------------------------------------------------------------------------------	
    public SElement getParentSElement()    {    	return fParent;    }

    public SElement getChild(String inName)
    {
    	if (inName == null)	return null;
		for (SElement element : fContent)
			if (inName.equals(element.getName()))
				return element;
		return null;
	}

    public SElement getChild(String[] inNames)
    {
     	int i = 0;
		int depth = inNames.length;
		SElement elem = this;
		while (i < depth)
		{
			elem = elem.getChild(inNames[i++]);
			if (elem == null) return null;
		}
		return elem;
	}

    public SElement getChild(String packageName, String inName)
    {
        if (inName == null)            return null;
        SElement pack = getChild(packageName);
        SElement child = (pack == null) ? getChild(inName) : pack.getChild(inName);
        return child;
    }

    public boolean hasAnyChildWithName(String[] names)
    {
    	if (names == null)	return false;
		for (SElement element : fContent)
		{
			for (String name : names)
				if (name.equals(element.getName()))
					return true;
		}
		return false;
	}
	//-----------------------------------------------------------------------------------------------------------	
	public List<AttributeValuePair> getAttributeList()
	{
		return getAttributeList(true);
	}
	/** This returns a list of attributes in this SElement.
	 * 
	 * @param parentNodesIncludeChildAttributes if this is true, then attributes
	 * are also listed for this node's immediate children.
	 * @return
	 */
	public List<AttributeValuePair> getAttributeList(boolean parentNodesIncludeChildAttributes)
	{
		List<AttributeValuePair> result = new ArrayList<AttributeValuePair>();
		for (int i = 0; i < fAttributeNames.size(); i++)
			result.add(new AttributeValuePair(fAttributeNames.get(i), fAttributeValues.get(i)));
		if(parentNodesIncludeChildAttributes) {
		for (SElement child : getChildren())
			{
				List<String> names = child.getAttributeNames();
				List<String> vals = child.getAttributeValues();
				for (int i = 0; i < child.getAttributeNames().size(); i++)
					result.add(new AttributeValuePair(names.get(i), vals.get(i)));
			}
		}
		return result;
	}

	public String getTextContent()		{		return getText();	}
//	private SElement getContent(int i)	{		return fContent.get(i);	}
	private int getContentSize()		{		return fContent.size() + (fText == null || fText.length() == 0 ? 0 : 1);	}

	// factory method that could return a subclass of SElement
	public static SElement construct(SElement elem)
	{
    	SElement kid = null;
		try
		{
			Constructor<SElement> constructor = (Constructor<SElement>) elem.getClass().getConstructor(String.class);
			kid = constructor.newInstance(elem.getName());
		} 
		catch (Exception e)	
		{
			kid = new SElement(elem.getName());
		} 
		if (kid != null)
            kid.copy(elem);
		return kid;
	}
	//-----------------------------------------------------------------------------------------------------------	
   public void copyChildren(SElement inOrig)
    {
        List<SElement> children = inOrig.getChildren();
        removeContent();
        for (SElement elem : children)
        {
            if (elem.getName() != null)
            {
            	SElement kid = construct(elem);
				addContent(kid);
            }
        }
    }

	public void copy(SElement inOrig)
    {
        if (inOrig == null)            return;
        if (inOrig.getName() != null && !inOrig.getName().equals(""))
            setName(inOrig.getName());
        copyAttributes(inOrig);
        copyChildren(inOrig);
        addContent(inOrig.getText());
    }

    public void copyAttributes(SElement inOrig)
    {
    	for (int i = 0; i < inOrig.getNumberOfAttributes(); i++)
    		setAttribute(inOrig.getAttributeName(i), inOrig.getAttributeValue(i));
    }

    public void addOrReplace(SElement inElem)
    {
        if (inElem == null) return;
        removeChildren(inElem.getName());
        addContent(inElem);
    }

 	//-----------------------------------------------------------------------------------------------------------	
    public void removeContent(SElement child)	{    fContent.remove(child);		}
    public SElement removeContent()	{		fContent.clear();		return this;	}
    
    public void removeAllChildren(String string)
	{
		List<SElement> kids = getChildren();
		for (SElement kid : kids)
			kid.removeAllChildren(string);
		removeChildren(string);
	}

    public void removeAllAttributes(String string)
	{
		List<SElement> kids = getChildren();
		for (SElement kid : kids)
			kid.removeAllAttributes(string);
		removeAttribute(string);
	}

	public void remove(SElement child)		{		fContent.remove(child);	}
	public void removeChild(String name)	{		remove(getChild(name));	}

	public void removeChildren(String string)
	{
		List<SElement> removeList = new ArrayList<SElement>();
		for (SElement elem : fContent)
			if (elem != null && elem.getName().equals(string))
				removeList.add(elem);
		for (SElement elem : removeList)
		{
			fContent.remove(elem);
			elem.setParent(null);
		}
	}

    public void clearAttributes()
    {
    	fAttributeNames.clear();
    	fAttributeValues.clear();
    }

	//-----------------------------------------------------------------------------------------------------------
    private static String mapEntry = "mapEntry";
    private static String mapKey = "mapKey";
    private static String mapVal = "mapVal";
	public void addHashMap(HashMap<String, String> referenceValues)
	{
		for (String s : referenceValues.keySet())
		{
			SElement e = new SElement(mapEntry);
			e.setString(mapKey, s);
			e.setString(mapVal, referenceValues.get(s));
			addContent(e); 
		}
	}

	public static HashMap<String, String> readHashMap(SElement child)
	{
		HashMap<String, String> map = new HashMap<String, String>();
		List<SElement> elems = child.getChildren(mapEntry);
		if (elems != null)
			for(SElement elem : elems)
				map.put(elem.getString(mapKey), elem.getString(mapVal));
		return map;
	}

	//-----------------------------------------------------------------------------------------------------------	
	public SElement findAnyChildWithName(String name)
	{
		if (name == null) return null;
		if (name.equals(getName())) return this;
		for (SElement elem : getChildren())
		{
			SElement foundElement = elem.findAnyChildWithName(name);
			if (foundElement != null) return foundElement;
		}
		return null;
	}
//	//-----------------------------------------------------------------------------------------------------------	
//	public void setLineWeight(LineWeight wt)  { setAttribute("lineWeight", StrokeFactory.lineWeightToString(wt));		}
//	public LineWeight getLineWeight()  { return StrokeFactory.stringToLineWeight(getString("lineWeight"));		}
//	public void setLineStyle(LineStyle st)  { setAttribute("lineStyle", StrokeFactory.lineStyleToString(st));		}
//	public LineStyle getLineStyle()  { return StrokeFactory.stringToLineStyle(getString("lineStyle"));		}

	public void setRect(Rectangle bounds)
	{
        setInt("x", bounds.x);
        setInt("y", bounds.y);
        setInt("width", bounds.width);
        setInt("height", bounds.height);
	}

	public void setRect(SElement e)
	{
        setAttribute("x", e.getAttributeValue("x"));
        setAttribute("y", e.getAttributeValue("y"));
        setAttribute("width", e.getAttributeValue("width"));
        setAttribute("height", e.getAttributeValue("height"));
	}
	public Font getFont()	{        return ParseUtil.fontFromElement(this);  	}
	//-----------------------------------------------------------------------------------------------------------	

//	 Rather than use the inherited JDOM functionality of managing text content, SElement will keep its own string field, thus circumventing lots of unnecessary character validation.
	private String fText = "";
	public SElement addContent(String str)	{		fText += str;		return this;	}
	public String getText()					{		return fText;	}
	public String getTextNormalize()		{		return fText;	}
	public String getTextTrim()				{		return fText;	}
	public Element setText(String text)		{		fText = text;	return this;	}
	public double getPercent(String string)				{		return getDouble(string, 100) / 100.;	}
	public void setPercent(String string, double val)	{		setDouble(string, ((int) (val * 1000)) / 10.0);	}

	@Override	public String getAttribute(String name)	{		return getAttributeValue(name);	}
	public void removeAttributes(String[] attributes) throws DOMException	{		for (String s : attributes)	removeAttribute(s);	}
	@Override	public void removeAttribute(String name) throws DOMException
	{
		int index = getIndexOfAttribute(name);
		if (index >= 0)
		{
			fAttributeNames.remove(index);
			fAttributeValues.remove(index);
		}
	}
	public Document getDocument()
	{
		SElement lastPar = this;
		SElement par = this;
		while (par != null)
		{
			lastPar = par;
			par = par.getParentSElement();
		}
		return (lastPar instanceof Document) ? (Document)lastPar : null;
	}

    @Override   public Object clone()
	{
		SElement cloned = null;
		try
		{
			cloned = (SElement)super.clone();
			cloned.fAttributeNames = new ArrayList<String>(fAttributeNames);
			cloned.fAttributeValues = new ArrayList<String>(fAttributeValues);
			cloned.fContent = new ArrayList<SElement>();
	        for (SElement elem : fContent)
	        	cloned.addContent((SElement)elem.clone());					
			cloned.setText(fText);
			cloned.setParent(null);
		} catch (CloneNotSupportedException e)		{			e.printStackTrace();		}
		return cloned;
	}

  //-----------------------------------------------------------------------------------------------------------	
    public void addContent(List<SElement> elements)	{		for (SElement elem : elements)	if (elem != null) addContent(elem);	}
	public void addContent(int i, SElement elem)	{		if (elem != null) fContent.add(i, elem);	}

//	public void addContent(int i, ArrayList<SElement> elements)
//	{
//		int j = i;
//		for (SElement elem : elements)
//		{
//			if (elem != null) fContent.add(j, elem);
//			j++;
//		}
//	}
	public void detach()	{		fParent = null;			}

	//-----------------------------------------------------------------------------------------------------------	
	public String getXMLString()
	{
		StringBuilder writer = new StringBuilder();
		toWriter(0, writer);
		return writer.toString();
	}
	
	public String dump(int indent)
	{
		StringBuffer indention = new StringBuffer();
		for(int i=0;i<indent;i++) indention.append("\t");
		
		StringBuffer str = new StringBuffer();
		str.append(indention);

		str.append("<"+getName());
		List<AttributeValuePair> attribList = getAttributeList();
		for(int i=0; i<attribList.size();i++)
			str.append(" "+attribList.get(i).getAttribute()).append("=\""+attribList.get(i).getValue()+"\"");
		str.append("/>\n");
		
		String text = getText();
		if(text!=null && text.length()>0) {
			str.append(indention+text+"\n");
		}

		if( getContentSize() > 0)
		{
			for(SElement e : getChildren())
				if (e != null)	str.append(e.dump(indent+1));		

			str.append(indention);
			str.append("</"+getName()+">");
			str.append("\n");
		}
	
		return str.toString();
	}
	//-----------------------------------------------------------------------------------------------------------	

	private static final String[] sIndents = { "", "  ", "    ", "      ", "        ", "          ", "            ", "              ", "                "};
	protected void writeIndent(int indent, StringBuilder writer)	{		writer.append(sIndents[Math.min(indent, sIndents.length-1)]);	}
	private String getSanitizedAttributeValue(int index)
	{
		String result = (index < fAttributeValues.size()) ? fAttributeValues.get(index) : null; 
		if (result == null || result.length() == 0)	return result;
//		if (result.contains("<") || result.contains("&"))
			result = ParseUtil.escapeXmlText(result);
		return result;
	}
	protected void toWriter(int indent, StringBuilder writer)
	{
		writeIndent(indent, writer);
		writer.append("<").append(fName).append(" ");
		for (int i = 0; i < fAttributeNames.size(); i++)
			writer.append(fAttributeNames.get(i)).append("=\"").append(getSanitizedAttributeValue(i)).append("\" ");
		if (fContent.isEmpty() && (fText == null || fText.length() == 0))
			writer.append(" />\n");
		else
		{
			writer.append(" >\n");
			for (SElement elem : fContent)
				elem.toWriter(indent+1, writer);
			if (fText != null)
				writer.append(fText.trim());
			writeIndent(indent, writer);
			writer.append("</").append(fName).append(" >\n");
		}
	}
//-----------------------------------------------------------------------------------------------------------	

	protected void takeOver(SElement other)
	{
		fAttributeNames = other.fAttributeNames;
		fAttributeValues = other.fAttributeValues;
		fContent = other.fContent;
		fText = other.fText;
	}

	public void renameAttribute(String oldName, String newName)
	{
		int index = fAttributeNames.indexOf(oldName);
		if (index >= 0)	fAttributeNames.set(index, newName);
	}
	//-----------------------------------------------------------------------------------------------------------	
	static final String UNIMPLEMENTED = "SElement method not implemented.";
	static RuntimeException ex = new RuntimeException(UNIMPLEMENTED);

	
	@Override	public String 		getAttributeNS(String namespaceURI, String localName) throws DOMException	{		throw ex;	}
	@Override	public Attr 		getAttributeNode(String name)												{		throw ex;	}
	@Override	public Attr 		getAttributeNodeNS(String namespaceURI, String localName) throws DOMException{		throw ex;	}
	@Override	public NodeList 	getElementsByTagName(String name)	{		throw ex;	}
	@Override	public NodeList 	getElementsByTagNameNS(String namespaceURI, String localName) throws DOMException	{		throw ex;	}
	@Override	public TypeInfo 	getSchemaTypeInfo()			{		throw ex;	}
	@Override	public String 		getTagName()				{		throw ex;	}
	@Override	public boolean 		hasAttribute(String name)	{		throw ex;	}
	@Override	public boolean 		hasAttributeNS(String namespaceURI, String localName) throws DOMException	{		throw ex;	}
	@Override	public void 		removeAttributeNS(String namespaceURI, String localName) throws DOMException	{		throw ex;	}
	@Override	public Attr 		removeAttributeNode(Attr oldAttr) throws DOMException	{		throw ex;	}
	@Override	public Attr 		setAttributeNode(Attr newAttr) throws DOMException		{		throw ex;	}
	@Override	public Attr 		setAttributeNodeNS(Attr newAttr) throws DOMException	{		throw ex;	}
	@Override	public void 		setIdAttribute(String name, boolean isId) throws DOMException{	throw ex;	}
	@Override	public void 		setIdAttributeNS(String namespaceURI, String localName, boolean isId) throws DOMException	{		throw ex;	}
	@Override	public void 		setIdAttributeNode(Attr idAttr, boolean isId) throws DOMException	{		throw ex;	}
	@Override	public Node 		appendChild(Node newChild) throws DOMException			{		throw ex;	}
	@Override	public Node 		cloneNode(boolean deep)	{	throw ex;	}
	@Override	public short 		compareDocumentPosition(Node other) throws DOMException	{		throw ex;	}
	@Override	public NamedNodeMap getAttributes()		{		throw ex;	}
	@Override	public String 		getBaseURI()		{		throw ex;	}
	@Override	public NodeList 	getChildNodes()		{		throw ex;	}
	@Override	public Object 		getFeature(String feature, String version)	{		throw ex;	}
	@Override	public Node 		getFirstChild()		{		throw ex;	}
	@Override	public Node 		getLastChild()		{		throw ex;	}
	@Override	public String 		getLocalName()		{		throw ex;	}
	@Override	public String 		getNamespaceURI()	{		throw ex;	}
	@Override	public Node 		getNextSibling()	{		throw ex;	}
	@Override	public String 		getNodeName()		{		throw ex;	}
	@Override	public short 		getNodeType()		{		throw ex;	}
	@Override	public String 		getNodeValue() throws DOMException			{		throw ex;	}
	@Override	public Document 	getOwnerDocument()	{		throw ex;	}
	@Override	public Node 		getParentNode()		{		throw ex;	}
	@Override	public String 		getPrefix()			{		throw ex;	}
	@Override	public Node 		getPreviousSibling(){		throw ex;	}
	@Override	public Object 		getUserData(String key)	{	throw ex;	}
	@Override	public boolean 		hasAttributes()		{		throw ex;	}
	@Override	public boolean 		hasChildNodes()		{		throw ex;	}
	@Override	public Node 		insertBefore(Node newChild, Node refChild) throws DOMException	{		throw ex;	}
	@Override	public boolean 		isDefaultNamespace(String namespaceURI)		{		throw ex;	}
	@Override	public boolean 		isEqualNode(Node arg)	{	throw ex;	}
	@Override	public boolean 		isSameNode(Node other)	{	throw ex;	}
	@Override	public boolean 		isSupported(String feature, String version)	{		throw ex;	}
	@Override	public String 		lookupNamespaceURI(String prefix)			{		throw ex;	}
	@Override	public String 		lookupPrefix(String namespaceURI)			{		throw ex;	}
	@Override	public void 		normalize()									{		throw ex;	}
	@Override	public Node 		removeChild(Node oldChild) throws DOMException					{		throw ex;	}
	@Override	public Node 		replaceChild(Node newChild, Node oldChild) throws DOMException	{		throw ex;	}
	@Override	public void 		setNodeValue(String nodeValue) throws DOMException				{		throw ex;	}
	@Override	public void 		setPrefix(String prefix) throws DOMException					{		throw ex;	}
	@Override	public void 		setTextContent(String textContent) throws DOMException			{		throw ex;	}
	@Override	public Object 		setUserData(String key, Object data, UserDataHandler handler)	{		throw ex;	}
	

}