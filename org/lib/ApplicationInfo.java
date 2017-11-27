package org.lib;

import java.io.File;

abstract public class ApplicationInfo 
{
//	public static final String TREESTAR_INC		= "TreeStar, Inc";
//	private static FormatKey FJVERSION = new FormatKey("fj.string.fjversion");

//	public static boolean isHeadless()				{		return hasApp() ? getApplication().isHeadless() : GraphicsEnvironment.isHeadless();	}
	public static boolean isPrerelease()			{		return isAlphaUser() || isBetaUser(); }//FJApplicationInfo.getVersion().contains("b")||FJApplicationInfo.getVersion().contains("a");	}

//	public static String getAppName()				{		return getApplication().getAppName();	}
	public static String getVersion()    			{        return ""; } // hasApp() ? getApplication().getVersion() : "-";    }
	static public boolean isAlphaUser()  			{        return getVersion().contains("a") ;     }

	static public boolean isBetaUser()   			{        return getVersion().contains("b");     }
	public static boolean allowMultiplePrograms()	{ 		return false;	} //PREFS OKreturn Prefs.getPrefs().get(PrefsFEML.License.allowMultipleFlowJoPrograms);
	static public boolean isDeveloper()				{		return (new File(".developer").isDirectory()  || ("true").equals(System.getenv().get("FLOWJO_DEVELOPER")));	}
	static public boolean is64bit()					{     	return System.getProperty("java.vm.name").contains("64-Bit");     }

	private static SApplication fInstance;
	public static SApplication getApplication() { return fInstance;	}
	public static void setApplication(SApplication app) { fInstance = app;	}
	public static boolean hasApp()	{	return fInstance != null;	}

}
