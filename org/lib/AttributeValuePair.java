
package org.lib;

public class AttributeValuePair
{
    private String attribute, value;
    public AttributeValuePair(String a, String v)
    {
        attribute = a;
        value = v;
    }
    public AttributeValuePair(AttributeValuePair avp)
    {
        this (avp.getAttribute(), avp.getValue());
    }
    public AttributeValuePair(SElement element)
    {
    	stateFromElement(element);
    }
    
    public String getElementName()		{ return "Attribute"; }
    public String getAttribute()		{ return attribute;	}
    public String getValue()			{ return value;	}
    public void setAttribute(String a)	{ attribute = a;	}
    public void setValue(String a)		{ value = a;	}
    @Override public String toString()	
    {
    	if (value == null) return attribute;
    	return attribute + " = " + value;
    }
//    public String getXML() { return "<Keyword attribute=\"" + attribute + "\" value=" + value + "\" />"; }
    public String toXMLString()			{ return stateToElement().toString();	}

	@Override	public int hashCode()	{	return (getAttribute() + getValue()).hashCode();	}
	
	public SElement getElement()		{	return stateToElement();		}
    public SElement stateToElement()
	{
		SElement element = new SElement(getElementName());
		element.setAttribute("name", getAttribute());			//AM changed to name to maintain compatibility with previous FJML
		String value = getValue();
		if (value != null)
			element.setAttribute("value", value);
		return element;
	}
    
    private void stateFromElement(SElement element)
	{
    	String name = getElementName();
		if (element.getName().equals(name))
		{
			setAttribute(element.getAttributeValue("name"));
			setValue(element.getAttributeValue("value"));
		}
		else if (element.getName().equals("Keyword"))
		{
			String attr = element.getAttributeValue("attribute");
			if (attr == null)
				 attr = element.getAttributeValue("name");
			if (attr != null)
			{
				setAttribute(attr);
				setValue(element.getAttributeValue("value"));
			}

		}
		else	throw new IllegalArgumentException(String.format("Attempt to set a %s from an SElement named '%s' instead of '%s'", getClass().getName(), element.getName(), name));
	}
    

	@Override	public boolean equals(Object obj)
	{
		if ((obj != null) && (obj.getClass().equals(getClass())))
		{
			AttributeValuePair other = (AttributeValuePair)obj;
			return (StringUtil.areEqual( other.getAttribute(), getAttribute() ) &&
					StringUtil.areEqual( other.getValue(), getValue() ));
		}
		return false;
	}
}