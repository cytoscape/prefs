package org.cytoscape.prefs;

import java.util.List;

import org.lib.ApplicationInfo;
import org.lib.SApplication;



public class CyPrefsApplication extends SApplication {
	Prefs prefs;
	
	public CyPrefsApplication(String[] args) {
		super(args);
		ApplicationInfo.setApplication(this);
	}
	public void earlyExit(String shutdownReason, int status) {	}

	public List<?> getCornerIconImages() {		return null;	}
	public String getCompany() {		return "Cytoscape Consortium";	}
	public String getAppName() {		return "Cytoscape Preferences";	}
	public String getVersion() {		return "-1.0";	}

	public void savePrefs() {			}
	public Prefs getPrefs() {
		if (prefs == null)
			prefs = new Cy3PrefsAPI();
		return prefs;
	}

}
