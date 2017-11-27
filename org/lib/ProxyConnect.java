package org.lib;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.cytoscape.prefs.Prefs;

import com.btr.proxy.selector.pac.JavaxPacScriptParser;
import com.btr.proxy.selector.pac.UrlPacScriptSource;

public class ProxyConnect 
{
    public static final String NETWORK_CONNECTION_ERROR = "There was a problem connecting to your network.";				// TODO INTL
    public static final String NETWORK_RESTART_ERROR = "Please restart the panel wizard after making proxy setting changes.";

	private static final String	HOST = "Host";
	private static final String	PORT = "Port";
	private static final String 	userLabelText = "User Name";
	private static final String 	passLabelText = "Password";
	private static final String 	pacLabelText = "Proxy Configuration File";
	private static final String 	PAC = "Proxy Configuration File Settings";
	private static final String 	PROXY = "Proxy Settings";
	private static final String 	PROXYINFO = "Proxy Information";
	private static final String DB_URL = null;
    public static String pHost = "";
    public static String pPort = "";
    public static String pUser = "";
    public static String pPass = "";
    public static String pPac = "";
    public static SecurePrompt prompt;
    public static boolean connected = false;
    public static boolean option = false;
    private static JDialog proxyDialog;
    private static JTextField fProxyHost;
    private static JTextField fProxyPort;
    private static JTextField fProxyUsername;
    private static JPasswordField fProxyPassword;
    private static JTextField fPacURL;
    public static JDialog pFrame;
    public static String error;
	public static void checkProxy(ProxyConfig config,JDialog frame, boolean headless)
	{
		if (config != null)
			checkProxy(config.getHost(), config.getPort(), config.getUsername(), config.getPassword(), config.getPac(), frame,  headless);
		else
			checkProxy("","","","","", frame,  headless);
	}
	public static void checkProxy(SElement elem,JDialog frame, boolean headless)
	{
		String host = elem.getString("proxyHost");
		String port = elem.getString("proxyPort");
		String pacURL = elem.getString("pac");
		String pw = elem.getString("proxyPassword");
		String user = elem.getString("proxyUsername");
		checkProxy(host, port, user, pw, pacURL, frame,  headless);
	}
	public static void checkProxy(final String host, final String port, final String username, final String encryptedPassword, final String pacURL,JDialog frame, boolean headless)
	{
		option = false;
		connected = false;
		prompt = headless ? null : new SecurePrompt(frame);
		System.setProperty("http.proxyHost", "");
	    System.setProperty("http.proxyPort", "");
		final String password = encryptedPassword == null ? null : Base64Coder.decodeString(encryptedPassword);
	    if (host != null && port != null) 
	    {
			System.setProperty("java.net.useSystemProxies", "true");
			try
		 	{
		 		if(host != null && !host.equals("") && port != null)
		 		{
		 			pHost = host;
					pPort = port;
					System.setProperty("http.proxyHost", pHost);
	        	    System.setProperty("http.proxyPort", "" + pPort);
	        	    pUser = username;
	         		pPass = password;
	        	    Authenticator.setDefault(new ProxyAuthenticator());
	        	    final URL url = new URL("http://offsite.treestar.com");
	    		 	final HttpURLConnection con = (HttpURLConnection) url.openConnection();
	    		 	con.setConnectTimeout(5000);
	    		 	con.connect();
	    	        con.disconnect();
	    	        connected = true;
	    	        return;
		 		}
		 	}
			catch (final Exception e){
				e.printStackTrace();
			}
	    }
		try
	 	{
	 		if(pacURL != null && !pacURL.equals(""))
	 		{
	 			pPac = pacURL;
   				final JavaxPacScriptParser pacScriptParser = new JavaxPacScriptParser(new UrlPacScriptSource(pPac));
   				final String pacString = pacScriptParser.evaluate(pPac,DB_URL);
   				if(pacString.startsWith("PROXY"))
   				{
   					pHost = pacString.substring(6, pacString.indexOf(":"));
   					pPort = pacString.substring(pacString.indexOf(":")+1, pacString.length());
   					System.setProperty("http.proxyHost", pHost);
            	    System.setProperty("http.proxyPort", pPort);
            	    pUser = username;
             		pPass = password;
            	    Authenticator.setDefault(new ProxyAuthenticator());
            	    final URL url = new URL("http://offsite.treestar.com");
       			 	final HttpURLConnection con = (HttpURLConnection) url.openConnection();
       			 	con.setConnectTimeout(5000);
       			 	con.connect();
       		        con.disconnect();
       		        connected = true;
       		        return;
   				}
           	}
	 	}
		catch (final Exception e){e.printStackTrace();}
	    try 
		{	     
			final List<Proxy> l = ProxySelector.getDefault().select(new URI(DB_URL));
		    for (final Iterator<Proxy> iter = l.iterator(); iter.hasNext(); ) 
            {
               final Proxy temp = iter.next();
               if(temp.type() == Proxy.Type.HTTP)
               {
            	    final InetSocketAddress addr = (InetSocketAddress)temp.address();
            	    if (addr == null)
            	    	break;
            	    if(host == null || host.equals(""))            	pHost = addr.getHostName();
            	    if(port == null || port.equals(""))           	pPort = Integer.toString(addr.getPort());
            	    System.setProperty("http.proxyHost", pHost);
            	    System.setProperty("http.proxyPort", pPort);
            	    pUser = username;
             		pPass = password;
            	    Authenticator.setDefault(new ProxyAuthenticator());
    	            break;
               }
		    }
            final URL url = new URL("http://offsite.treestar.com");
		    final HttpURLConnection con = (HttpURLConnection) url.openConnection();
		    con.setConnectTimeout(5000);
		    con.connect();
	        con.disconnect();
	        connected = true;
	        return;
        } 
 		catch(final Exception e)	{		error = NETWORK_CONNECTION_ERROR+"  ("+e.getMessage()+")"; 	}
	}
	
	static class ProxyAuthenticator extends Authenticator
	{
		 @Override  protected PasswordAuthentication getPasswordAuthentication()
         {
         	if(option == false && (pUser == null || pUser.equals("") || pPass == null || pPass.equals("")))
         	{
         		if(prompt != null && (prompt.getUserID() == null || prompt.getUserID().equals("")))
         		{
         			prompt.setVisible(true);
         			pUser = prompt.getUserID();
         			pPass = String.valueOf(prompt.getPassword());
         			if(pUser == null || pUser.equals("") || pPass == null || pPass.equals(""))
                 	{
         				createProxyPrefsPDialog(false);
         				option = true;
                 	}
         			prompt.dispose();
         		}
         	}
            return new PasswordAuthentication(pUser, pPass.toCharArray());
         }
	}
	
	public static void createProxyPrefsPDialog(boolean forPrefs) 
	{
		// the boolean forPrefs distinguishes between startup version, and the prefs dialog version.
		
		proxyDialog = new JDialog(pFrame,"Proxy Settings", true);
		proxyDialog.setResizable(false);
		Dimension dStandAlone = new Dimension(580, 380);
		Dimension dForPrefs = new Dimension(580, 270);
		if (forPrefs)
			proxyDialog.setSize(dForPrefs);
		else
			proxyDialog.setSize(dStandAlone);
		
		proxyDialog.setLocationRelativeTo(pFrame);
		proxyDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		proxyDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		VBox main = new VBox(getOfflineStartupBox(), getHTTPProxySettingsPanel(), getPACProxySettingsPanel(), getButtonBox());
		if (forPrefs)
		 main = new VBox( getHTTPProxySettingsPanel(), getPACProxySettingsPanel(), getButtonBox());
		
		
		main.setBorder(BorderFactory.createEtchedBorder());
	 
		proxyDialog.add(main);
		
		proxyDialog.setVisible(true);
	}
	
	public static VBox getOfflineStartupBox(){
		
		VBox offlineBox = new VBox(PROXYINFO);
		
		Dimension d = new Dimension(515, 100);
		Dimension dd = new Dimension(450, 20);
		GuiFactory.setSizes(offlineBox, d);
		
		Font proxyFont = FontUtil.dlog14;
		
		JLabel infoLabel = new JLabel("Could not access the Internet during startup. ");			// TODO INTL
		infoLabel.setFont(proxyFont);
		JLabel infoLabel2 = new JLabel(" If you use a Proxy server, try entering that now.");
		infoLabel2.setFont(proxyFont);
		JLabel infoLabel3 = new JLabel( "If you are disconnected from the internet, hit 'Cancel' below.");
		infoLabel3.setFont(proxyFont);
		
		HBox h0 = new HBox( Box.createRigidArea(BoxConstants.LINE5),  infoLabel); GuiFactory.setSizes(h0, dd);
		HBox h1 = new HBox( Box.createRigidArea(BoxConstants.LINE5),  infoLabel2);GuiFactory.setSizes(h1, dd);
		HBox h2 = new HBox( Box.createRigidArea(BoxConstants.LINE5),  infoLabel3);GuiFactory.setSizes(h2, dd);
		offlineBox.add(new VBox( h0, h1, h2));
		
		return offlineBox;
	}
	
	public static Box getButtonBox()
	{
		Box buttonBox = Box.createHorizontalBox();
		
		JButton cancel = new JButton("Cancel"); 	cancel.addActionListener(new buttonAction());
		JButton ok = new JButton("OK");    			ok.addActionListener(new buttonAction());
		JButton reset = new JButton("Reset");   	reset.addActionListener(new buttonAction());

		buttonBox.add(reset);
		buttonBox.add(Box.createHorizontalGlue());
		buttonBox.add(cancel);
		buttonBox.add(ok);
		return buttonBox;
	}
	
	static class buttonAction implements ActionListener 
	{ 
		  private static final long serialVersionUID = 1L;

	    @Override
		public void actionPerformed(ActionEvent e) 
	    { 
			String item = e.getActionCommand(); 
			if (item.equals("Reset")) 				resetAllFields();
			else if (item.equals("Cancel"))  		proxyDialog.dispose();
			else if(item.equals("OK"))
			{
				updateProxyInfo();
				proxyDialog.dispose();
			}
	    } 
	    
		private void resetAllFields()
		{
			fProxyHost.setText("");
			fProxyPort.setText("");
			fProxyUsername.setText("");
			fProxyPassword.setText("");
			fPacURL.setText("");
		}
		
		private void updateProxyInfo()
		{
			pHost = fProxyHost.getText();
			pPort = fProxyPort.getText();
			pUser = fProxyUsername.getText();
			pPass = String.valueOf(fProxyPassword.getPassword());
			pPac = fPacURL.getText();
			String password = "";
			if(pPass != null && !pPass.equals(""))
				password = 	Base64Coder.encodeString(ProxyConnect.pPass);
			checkProxy(pHost, pPort, pUser, password, pPac, pFrame, false);
		}
	} 
	
	public static VBox getHTTPProxySettingsPanel()
	{
		VBox proxySettingsPanel = new VBox(PROXY);
		Dimension d = new Dimension(515, 100);
		GuiFactory.setSizes(proxySettingsPanel, d);
		fProxyHost = 		new JTextField(pHost,25);		GuiFactory.setSizes(fProxyHost, 	BoxConstants.COMBO230);
		fProxyPort = 		new JTextField(pPort,5);		GuiFactory.setSizes(fProxyPort, 	BoxConstants.COMBO140);
		fProxyUsername = 	new JTextField(pUser,15);		GuiFactory.setSizes(fProxyUsername, BoxConstants.COMBO140);
		fProxyPassword = 	new JPasswordField(pPass,15);	GuiFactory.setSizes(fProxyPassword, BoxConstants.COMBO50);
		
		HBox h0 = new HBox( Box.createRigidArea(BoxConstants.LINE5), new JLabel(HOST), fProxyHost, Box.createHorizontalGlue(), new JLabel(PORT), fProxyPort );
		HBox h1 = new HBox( Box.createRigidArea(BoxConstants.LINE5), new JLabel(userLabelText), fProxyUsername, Box.createHorizontalGlue(), new JLabel(passLabelText),  fProxyPassword );
		
		proxySettingsPanel.add(new VBox( h0, h1));
		return proxySettingsPanel;
	}
	static String CHOOSE = "Choose";
	public static VBox getPACProxySettingsPanel()
	{		
		VBox PACSettingsPanel = new VBox(PAC);
		Dimension d = new Dimension(515, 110);
		GuiFactory.setSizes(PACSettingsPanel, d);
		fPacURL = 		new JTextField(pPac,25);		GuiFactory.setSizes(fPacURL, 	BoxConstants.COMBO230);
		JButton choose = new JButton(CHOOSE);
		choose.addActionListener(new ActionListener()
		{
			@Override public void actionPerformed(ActionEvent arg0) 
			{
				JFileChooser fc = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter("Proxy Configuration File", new String[] {"pac"});
			    fc.setFileFilter(filter);
			    int returnVal = fc.showOpenDialog(pFrame);
			    if(returnVal == JFileChooser.APPROVE_OPTION)
			    {
			    	File panelFile = fc.getSelectedFile();
			    	fPacURL.setText(panelFile.getAbsolutePath());
			    }
			}
		});
//		HBox h0 = new HBox( Box.createRigidArea(BoxConstants.LINE5), new JLabel(pacLabelText));
		HBox h1 = new HBox( Box.createRigidArea(BoxConstants.LINE5), new JLabel("URL:"), fPacURL, Box.createRigidArea(BoxConstants.LINE5), new JLabel(" or "), choose);
		final JCheckBox cbox = new JCheckBox(NO_PROXY_DIALOG);
		cbox.addActionListener(new ActionListener()
		{
			@Override public void actionPerformed(ActionEvent arg0) 
			{
//				Prefs.getPrefs().set("Workspace.noProxyDialog", (cbox.isSelected() ? "true" : "false"));
				Prefs.getPrefs().savePrefs();
			}
		});
		HBox h2 = new HBox( Box.createRigidArea(BoxConstants.LINE5), cbox);
		PACSettingsPanel.add(new VBox(h1, Box.createRigidArea(BoxConstants.LINE5), h2));			//h0, h1, h2
		return PACSettingsPanel;
	}
	static String NO_PROXY_DIALOG = "dlog.license.no.proxydialog";
}
