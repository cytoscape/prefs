package org.lib;

import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Intl
{
	private static Intl REF;
	public static synchronized Intl getInstance(){
		if(REF == null)	REF = new Intl();
	    return REF;
	}
	public static String lookup(String s) { return getInstance().lookupKey(s);	}
	
	protected Locale currentLocale;
	
	public static final Locale defaultLocale = new Locale("en", "US");
	private static final String[] availableLanguages = new String[]{ "en", "fr", "zh", "ja", "de", "es", "it"};
	private static final HashMap<String, String> defaultCountryLanguages = new HashMap<String, String>(){{
		put("en","US");
		put("fr","FR");
		put("zh","CN");
		put("ja","JP");
		put("de","DE");
		put("es","ES");
		put("it","IT");
	}};
	private final List<String> availableCountries;
	String[] nonMetricCountries = new String[] { "US", "LR", "MM" };	//usa, liberia, myanmar
	
//	private LinkedList<WeakReference<LocaleChangeListener>> fListenerList;
	private final EventDelegate<LocaleChangeEvent> fLocaleChangeDelegate;
	private Intl(){
		fLocaleChangeDelegate = new EventDelegate<LocaleChangeEvent>();
		currentLocale = defaultLocale;	// Returns en_US locale if Preference is set to not translate.
		availableCountries = new ArrayList<String>();
		for(String country : Locale.getISOCountries()){
			for(Locale loc : Locale.getAvailableLocales()){
				if(country.equals(loc.getCountry())){
					availableCountries.add(country);
					break; //only add each country once
				}
			}
		}
	}
	public Locale getCurrentLocale()							{	return currentLocale;														}
	public List<String> getAvailableLanguages() 				{	return Collections.unmodifiableList(Arrays.asList(availableLanguages));		}
	public List<String> getAvailableCountries() 				{	return Collections.unmodifiableList(availableCountries);					}
	public Map<String, String> getDefaultCountryLanguages()		{	return Collections.unmodifiableMap(defaultCountryLanguages);				}
	public String lookup(DictKey orig)							{ 	return lookup(orig);				}
//	public String lookup(ErrorKey err)							{ 	return getDictionaryMaster().lookup(err);				}
	public String lookupKey(String keyString)					{		return keyString;		}
		 
		
	 

	public String[] massLookup(DictKey[] keys)
	{
		int size = keys.length;
		String[] strs = new String[size];
		for (int i=0; i< size; i++)
			strs[i] = lookup(keys[i]);
		return strs;
	}
	
    
    public EventDelegate<LocaleChangeEvent> getLocaleChangeDelegate()	{	return fLocaleChangeDelegate;	}
	
	
	public boolean isUseMetric()
	{
		return !Arrays.asList(nonMetricCountries).contains(getCurrentLocale().getCountry());
	}
	

	/** Gets a locale for use with country specific localization. 
	 * @param inLoc
	 * @return first country-matching Locale from Java's available locales or default locale if not found
	 */
	private static Locale getCountrySpecificLocale(Locale inLoc){
		 String inCountry;
		 if (inLoc == null){
			 inCountry = Intl.getInstance().getCurrentLocale().getCountry();
		 }
		 else{
			 inCountry = inLoc.getCountry();
		 }
		 for(Locale locale : Locale.getAvailableLocales()){
			 if(locale.getCountry().equals(inCountry)){
				 return locale;
			 }
		 }
		 return Intl.defaultLocale;
	}

	/** Gets a DecimalFormatSymbols for use with country specific localization. 
	 * @see #getCountrySpecificLocale(Locale inLoc)
	 * @return DecimalFormatSymbols for first matching locale associated with current Country
	 */
	public static DecimalFormatSymbols getInstanceDecimalFormatSymbols(){
		return getInstanceDecimalFormatSymbols(Intl.getInstance().getCurrentLocale());
	}
	/** Gets a DecimalFormatSymbols for use with country specific localization. 
	 * @see #getCountrySpecificLocale(Locale inLoc)
	 * @return DecimalFormatSymbols for first matching locale associated with current Country
	 */
	public static DecimalFormatSymbols getInstanceDecimalFormatSymbols(Locale inLoc){
		return new DecimalFormatSymbols(getCountrySpecificLocale(inLoc));
	}
}
