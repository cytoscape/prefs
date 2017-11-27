package org.lib;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class SecurePrompt extends JDialog 
{
	private static final long serialVersionUID = 1L;
	private boolean frameSizeAdjusted = false;
	private JLabel label1 = new JLabel();
	private JLabel label2 = new JLabel();
	private JTextField _uid = new JTextField();
	private JButton _ok = new JButton();
	private JPasswordField _pwd = new JPasswordField();
	private JButton _cancel = new JButton();

	public SecurePrompt(JDialog parent) 
	  {
	    super(parent, true);
	    setTitle("Proxy Authentication Required");
	    getContentPane().setLayout(null);
	    setSize(403, 129);
	    setVisible(false);
	    label1.setText("User ID:");
	    label1.setBounds(12, 12, 48, 24);
	    label2.setText("Password:");
	    label2.setBounds(12, 48, 72, 24);
	    _uid.setText("");

	    getContentPane().add(label1);
	    getContentPane().add(label2);
	    
	    getContentPane().add(_uid);
	    _uid.setBounds(72, 12, 324, 24);
	    _ok.setText("OK");
	    
	    InputMap im = _ok.getInputMap();
	    im.put( KeyStroke.getKeyStroke( "ENTER" ), "pressed" );
	    im.put( KeyStroke.getKeyStroke( "released ENTER" ), "released" );
	    getContentPane().add(_ok);
	    _ok.setBounds(60, 84, 84, 24);
	    
	    getContentPane().add(_pwd);
	    _pwd.setBounds(72, 48, 324, 24);
	    
	    _cancel.setText("Cancel");
	    getContentPane().add(_cancel);
	    _cancel.setBounds(264, 84, 84, 24);
	    
	    SymAction lSymAction = new SymAction();
	    _ok.addActionListener(lSymAction);
	    _cancel.addActionListener(lSymAction);
	  }

	  public void setVisible(boolean b) 
	  {
	    if (b)
	    {
		    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	        Dimension frameSize = getSize();
	        if (frameSize.height > screenSize.height)
	            frameSize.height = screenSize.height;
	        if (frameSize.width > screenSize.width)
	            frameSize.width = screenSize.width;
	        setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
	    }
	    super.setVisible(b);
	  }

	  public void addNotify() 
	  {
	    Dimension size = getSize();
	    super.addNotify();
	    if (frameSizeAdjusted)	      return;
	    frameSizeAdjusted = true;
	    Insets insets = getInsets();
	    setSize(insets.left + insets.right + size.width, insets.top  + insets.bottom + size.height);
	  }
	  
	  public String getUserID()	  {		  return _uid.getText();	  }
	  public char[] getPassword()	  {		  return _pwd.getPassword();	  }
	  
	  class SymAction implements ActionListener 
	  {
	    public void actionPerformed(ActionEvent event) 
	    {
	      Object object = event.getSource();
	      if (object == _ok)	        Ok_actionPerformed(event);
	      else if (object == _cancel)	 Cancel_actionPerformed(event);
	    }
	  }

	  void Ok_actionPerformed(ActionEvent event) 	  {	    setVisible(false);	  }
	  void Cancel_actionPerformed(java.awt.event.ActionEvent event) 	  {		setVisible(false);		  }
	}