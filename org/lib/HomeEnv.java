package org.lib;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Hashtable;

import javax.swing.filechooser.FileSystemView;

// An instance describes how an application lives on a particular computer; its home directory, data directory etc.
public class HomeEnv 
{
	static HomeEnv instance;

	// private int editStatus;
	private static final String REGSTR_TOKEN = "REG_SZ";
	public static final String ENV_FILENAME = "env.var",APP_DIR = ".cytoscape";
	private String appName, systemAppFolderName;
	private String projectFolder; // if set, use instead of home for project
									// workspaces

	public static HomeEnv getInstance() {
		if (instance == null)
			instance = new HomeEnv();
		return instance;
	}

	Hashtable<String, Object> envHT;

	public HomeEnv() {
		envHT = new Hashtable<String, Object>();
	}

	private boolean debug = false;

	public void setDebug(boolean tv) {		debug = tv;	}

	public void put(String name, Object ov) {
		if (ov == null)			envHT.remove(name);
		else					envHT.put(name, ov);
		if (debug)		Debug.println("HomeEnv: size " + envHT.size() + " < " + name);
	}

	public Object get(String name) {		return envHT.get(name);	}

	public void setHomeFolder(String hd) {		put("home", hd);	}

	public String getHomeFolder() {
		Object ov = get("home");
		if (ov == null)
			return "";
		fUserHomeFolder = ov.toString();
		return fUserHomeFolder;
	}

	public String getDefaultFlowJoHomeDirectory() {		return getSystemAppFolderName();	}

	/**
	 * Return the Java system property "user.home". This is not cached in the
	 * hash table so that it doesn't get written out. If there is no property,
	 * return the cached home folder.
	 */
	public String getUserHomeFolder() 
	{
		if (fUserHomeFolder != null)			return fUserHomeFolder;
		fUserHomeFolder = System.getProperty("user.home");
		if (fUserHomeFolder != null && fUserHomeFolder.length() > 0)
			return fUserHomeFolder;
		return getHomeFolder();
	}

	public URI getHomeFolderURI() {		return new File(getUserHomeFolder()).toURI();	}
	
	public static String getUserName()
	{
		return System.getProperty("user.name");
	}

	/**
	 * Return the Java system property "java.io.tmpdir".
	 */
	public String getUserTempFolder() {
		if (fUserTempFolder != null)
			return fUserTempFolder;
		fUserTempFolder = System.getProperty("java.io.tmpdir");
		if (!(fUserTempFolder.endsWith("/") || fUserTempFolder.endsWith("\\")))
			fUserTempFolder = fUserTempFolder
					+ System.getProperty("file.separator");
		return fUserTempFolder;
	}

	public URI getUserTempFolderURI() {		return new File(getUserTempFolder()).toURI();	}

	private String fUserTempFolder = null;
	private String fUserDocFolder = null;
	private String fUserHomeFolder = null;
	private String fUserPrefsFolder = null;

	public String getUserDocumentsFolder() 
	{
		if (fUserDocFolder != null)			return fUserDocFolder;
		File defaultDirectory = FileSystemView.getFileSystemView().getDefaultDirectory();
		if (defaultDirectory != null && defaultDirectory.exists()) 
		{
			fUserDocFolder = defaultDirectory.getPath();
			return fUserDocFolder;
		}
		return getUserHomeFolder();
	}

	public String getUserPrefsFolder() {
		fUserPrefsFolder = getUserHomeFolder() 	+ File.separator;
				
				if (HomeEnv.isWindows())		fUserPrefsFolder +=	"Application Data" + File.separator + "FlowJo10";			// TODO INTL
				else if (HomeEnv.isMac())		fUserPrefsFolder += "Library" + File.separator+ "Preferences";
				else if (HomeEnv.isLinux())		fUserPrefsFolder +=  File.separator + ".FlowJo10";
				
				
		File prefsFolder = new File(fUserPrefsFolder);
		if (!prefsFolder.exists())
			prefsFolder.mkdirs();
		if (!prefsFolder.exists())
			fUserPrefsFolder = getUserDocumentsFolder();
		return fUserPrefsFolder;
	}

	public URI getUserPrefsURI() {
		try 
		{
			File f = new File(getUserPrefsFolder());
			return f.toURI();
		} catch (Exception e) {			e.printStackTrace();		}

		return null;
	}

	@Override	public String toString() {	return "HomeEnv(" + appName + ") homeFolder: '" + getHomeFolder() + "'";	}

	public void initApp(String name) {
		appName = name;
		makeAbsolutePath(getSystemAppFolderName());
		loadEnv();
	}

	private static String fComputerName;
    public static String getComputerName()
    {
    	if (fComputerName == null)
    	{
    		try {
    			fComputerName = InetAddress.getLocalHost().getHostName();}
    		catch(Exception x)
    		{
    			fComputerName = "unknown";
    		}
    	}
    	return fComputerName;
    }
	
	
	public String getAppName() {		return appName;	}

	public void setAppName(String name) {		appName = name;	}

	public String getSystemAppFolderName() {
		if (isMac()){
			String s =  getUserHomeFolder() + File.separator + "Library" + File.separator  + "Preferences" + File.separatorChar + appName + "Init";
			return s;
		}
		
		if (isLinux())
			return getUserHomeFolder() + File.separatorChar + appName + "Init";
		
		
		if (systemAppFolderName == null) {
			String homeName = null;
			File homeDir;
			for (int i = 0; i < 2; i++) {
				switch (i) 
				{
					case 0:					homeName = getUserHomeFolder();	break;
					case 1:					homeName = "";					break;
				}
				homeDir = new File(homeName);
				if (homeDir.exists() && homeDir.isDirectory() && homeDir.canWrite()) 
				{
					systemAppFolderName = homeName + File.separatorChar + appName + "Init";
					break;
				}
				Debug.println(i + " cannot use homeName '" + homeName + "'");
			}
			if (systemAppFolderName == null)
				systemAppFolderName = appName + "Init"; // relative
		}
		return systemAppFolderName;
	}

	public String getSystemAppEnvironmentFileName() {	return getSystemAppFolderName() + File.separatorChar + ENV_FILENAME;	}

	public void loadEnv()
	{
		File file = new File(getSystemAppEnvironmentFileName());
		if (!file.exists()) {		Debug.println(file.getPath() + "  does not exist!");		return;	}
		envHT.clear();
		if (debug)
			Debug.println(getAppName() + " HomeEnv: loading '" + file.getName()	+ "'");
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			loadEnv(fis, envHT);
		} catch (Exception ex) {Debug.println("loadEnv/0 ex: " + ex);} 
		finally {
			if (fis != null) 
				try {	fis.close();	} catch (Exception ex) {}
		}
	}

	public static InputStream getResourceInputStream(String name) {
		ClassLoader cl = HomeEnv.class.getClassLoader();
		InputStream is = null;
		try {
			is = cl.getResourceAsStream(name);
		} catch (Exception ex) {
			Debug.println("getRIS ex: " + ex);
		}
		return is;
	}

	public static void loadEnv(InputStream is, Hashtable<String, Object> ht) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is));
			String line, front, back;
			int x;
			while ((line = reader.readLine()) != null) {
				x = line.indexOf('=');
				if (x > 0) {
					front = line.substring(0, x);
					back = line.substring(x + 1);
					ht.put(front, back);
					// if (debug) Debug.println(" '" + front +"' = '" + back
					// +"'");
				}
			}
		} catch (Exception ex) {
			Debug.println("loadEnv/2:  ex: " + ex);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception ex) {
				}
			}
		}
	}

	public void saveEnv() {
		PrintWriter out = null;
		try {
			String fn = getSystemAppEnvironmentFileName();
			out = new PrintWriter(new BufferedOutputStream(
					new FileOutputStream(fn)));
			for (String key : envHT.keySet())
				out.println(key + "=" + envHT.get(key).toString());
		} catch (Exception ex) {
			Debug.println("!HomeEnv.save: ex: " + ex);
			throw new RuntimeException("HomeEnv.save " + ex);
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (Exception ex) {
			}
		}
		// editStatus = 0;
	}

	public String makeFilePath(String dir) {
		if (dir != null && dir.length() == 0)
			dir = null;
		if (dir != null && !dir.endsWith(File.separator))
			dir += File.separator;
		return dir;
	}

	public String makeFilePath(String path, String filename) {
		int separators = 0;
		StringBuffer result = new StringBuffer();
		result.append(path);
		if (path.endsWith(File.separator))			separators++;
		if (filename.startsWith(File.separator))	separators++;
		switch (separators) 
		{
			case 0: result.append(File.separator); // and fall thru
			case 1: result.append(filename);			break;
			case 2: result.append(filename.substring(1, filename.length()));
		}
		return result.toString();
	}

	public static void makeAbsolutePath(String dir) {
		File file = new File(dir);
		try {
			if (!file.exists())
				file.mkdirs();
		} catch (Exception ex) {
			Debug.println("makeAbsPath: file: " + file + " ex: " + ex);
		}
	}

	public void setProjectFolder(String dir) {
		projectFolder = makeFilePath(dir);
		put("project", projectFolder);
	}

	public String getProjectFolder() {
		if (projectFolder != null)
			return projectFolder;
		Object ov = get("project");
		if (ov != null)
			projectFolder = ov.toString();
		String s = (projectFolder != null) ? projectFolder
				: getUserHomeFolder(); // getHomeFolder(); to fix bug #66
		makeAbsolutePath(s);
		return s;
	}

	public String makeProjectPath(String filename) {
		return makeFilePath(getProjectFolder(), filename);
	}

	public String getResourcePath() {
		return System.getProperty("user.home") + File.separator + APP_DIR;
	}

	public String getAbsProjectFolder() {
		String sv = (projectFolder != null) ? projectFolder : getHomeFolder();
		makeAbsolutePath(sv);
		return sv;
	}

	// public void savePreferences(boolean explicitSave)
	// {
	// for (IPreferenceClient client : clientSet)
	// client.savePreferences(explicitSave);
	// if (editStatus > 0)
	// saveEnv();
	// }

	// public void restorePreferences()
	// {
	// hasRestoredFlag = true;
	// for (IPreferenceClient client : clientSet)
	// client.restorePreferences();
	// }

	boolean hasRestoredFlag = false;

	public boolean hasRestored() {
		return hasRestoredFlag;
	}
	
	public static void addClasspathURL(String url) throws Exception{
		URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		File f = new File(url);
		URL u = f.toURL();
		URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		Class urlClass = URLClassLoader.class;
		Method method = urlClass.getDeclaredMethod("addURL", new Class[]{URL.class});
		method.setAccessible(true);
		method.invoke(urlClassLoader, new Object[]{u});
	}

	// Vector<IPreferenceClient> clientSet = new Vector<IPreferenceClient>();
	// public void registerClient(IPreferenceClient client)
	// {
	// if (clientSet.indexOf(client) >= 0) return;
	// if (debug) Debug.println("Pref:register " + client);
	// clientSet.addElement(client);
	// }

	static public String getOS() 			{		return System.getProperty("os.name").toLowerCase();	}
	static public boolean isLinux() 		{		return getOS().contains("linux");	}
	static public boolean isWindows2000() 	{		return isWindows() && getOS().contains("2000");	}
	static public boolean isMac() 			{		return (getOS().contains("mac") || getOS().contains("os x")) ;	}
	static public boolean isWindows7() 		{		return getOS().contains("windows 7");	}
	static public boolean isVista() 		{		return getOS().contains("vista");	}
	static public boolean isWindows() 		{		return getOS().contains("windows");	}
//	
//	static public int getBitness()			{
//		if(isWindows()){
//			String arch = System.getenv("PROCESSOR_ARCHITECTURE");
//			String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");
//
//			int realArch = arch.endsWith("64")
//			                  || wow64Arch != null && wow64Arch.endsWith("64")
//			                      ? 64 : 32;
//			return realArch;
//		}
//		String arch = System.getProperty("os.arch");
//		return arch.endsWith("64") ? 64 : 32;
//	}
//	

	// -----------------------------------------------------------------------------------------
	public static class StreamPumper extends Thread 
	{
		private final InputStream is;
		private final StringWriter sw;

		public StreamPumper(InputStream ins) {
			is = ins;
			sw = new StringWriter();
		}

		@Override public void run() {
			try 
			{
				int c;
				while ((c = is.read()) != -1)
					sw.write(c);
			} catch (IOException e) {	}
		}

		public String getResult() {		return sw.toString();		}
	}


}