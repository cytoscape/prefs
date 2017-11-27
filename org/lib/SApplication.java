package org.lib;

import org.cytoscape.prefs.Prefs;

/**
 * This is the mother of all workspaces. She can pick and open new ones, and keep the list of what's open
 */
abstract public class SApplication  //<T extends SWorkspace>  
{	
	public Prefs getPrefs()	{ return null;	}
    public SApplication(String[] args)
    {
//    	VersionInfo version = new VersionInfo(getVersion());
//    	if (!version.isValid() && !ApplicationInfo.isDeveloper())
//    		SDialogs.notify(String.format("Application version string \"%s\" is improper", getVersion()));		// TODO INTL
//    	setStartupCommands(makeStartupCommands(args));
    }
    //----------------------------------------------------------------------------------------------------
 
}