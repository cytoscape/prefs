package org.lib;

import java.io.Serializable;

public class DictKey implements Serializable			//TextPresentable, FCMLable, S
{
	private String fKeyString;
	private String fCachedLookup = null;

	public DictKey(String inString)
	{
		fKeyString = inString;
//		Intl.getInstance().getLocaleChangeDelegate().add(
//				new FJEventListener<LocaleChangeEvent>() {
//					@Override public void fjEvent(LocaleChangeEvent event) {
//						fCachedLookup = null;
//					}
//				}, true);
	}
	static public String lookup(String pseudokey) 	{ 	return pseudokey;						}
	public String getKeyString() 					{	return fKeyString;									}
//	@Override public String toString()				{ 	return fKeyString + " = " + lookup();				}
	@Override public String toString()				{	return lookup();									}
	public boolean isDefined()						{	return !StringUtil.areEqual(getKeyString(), lookup());	}
	
	public boolean match(Object o)					{
		if (o instanceof DictKey) return equals((DictKey) o);
		return fKeyString.equals(o) || lookup().equals(o);
	}		// TODO this is generous
	public boolean equals(DictKey key)				{	return fKeyString.equals(key.getKeyString());		}
	public String lookup() 				
	{
		if (fCachedLookup == null)
			fCachedLookup = lookup(getKeyString());
		return fCachedLookup;
	}
	public String asPrompt()						{	return lookup() + ":";						}
	public DictKey tooltip()						{	return new DictKey(fKeyString + ".tooltip");	}
	
//	@Override public String getPresentationString() {	return lookup();		}
//	@Override public String toFCML()				{	return getKeyString();	}
}
