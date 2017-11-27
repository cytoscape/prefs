package org.cytoscape.prefs;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.lib.FileUtil;
import org.lib.ProxyConfig;

public class Cy3PrefsAPI extends Prefs {

	public Cy3PrefsAPI()
	{
		instance = this;
	}
	public ProxyConfig getProxy() {		return null;	}
	public void setProxyConfig(ProxyConfig config) {}
//	@Override	public IntlFormatter getFormatter() {		return null;	}
	@Override	public File getPrefsFile() {		return null;	}
	@Override	public boolean canSetPrefs() {		return false;	}
				public String getLastPath() {	return null;	}

				public void addPanel(String suid,  JPanel userPref)
				{
					
				}
				public void removePanel(String suid)
				{
					
				}
	
//	private static Map<String, String> properties  = new HashMap<String, String>();
	
	public static void readProperties()
	{
		String startPath = System.getProperty("user.home") + "/CytoscapeConfiguration";
		File cytoDir = new File(startPath);
		if (cytoDir.exists())
		{
			List<File> propFiles = FileUtil.collectFiles(startPath, ".props");
			for (File f : propFiles)
			{
				String namespace = f.getName();
				namespace = namespace.substring(0, namespace.length() - 6);  // trim .props
				Map<String, String> list = AbstractPrefsPanel.getPropertyMap(f.getName());
				if (!list.isEmpty())
				{
					for (String key : list.keySet())
						instance.put(namespace + "." + key, list.get(key));		
				}
				System.out.println(f.getName() + " " + list.entrySet().size());
			}
		}
		
	}
	
	
}
