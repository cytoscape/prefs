package org.lib;

///RibesExport

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowAdapter;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * This class contains static methods for managing JFrames and JDialogs. In any particular GUI app, there only needs to be one
 * JFrame and one JDialog. These can be re-used for various screens by setting their content panes. Note that the static "mainFrame"
 * variable here goes crazy with swing 1.1 and JDK 1.17; it gets allocated more than once!
 */
public class WindowManager
{
    public static JFrame mainFrame;
    // public static Frame mainFrame;
    static LocalWindowAdapter localWindowAdapter;

    /**
     * A GUI application may have only a single Frame. Allocate that Frame if necessary, and return it.
     */
    public static JFrame getFrame()
    {
        if (mainFrame == null)
            mainFrame = frameFactory();
        return mainFrame;
    }
    
    public static Frame getFrame(Component c) 	
    {
    	Window w = getWindow(c);
    	return w instanceof Frame ? (Frame) w : null;
    }
    
    public static Window getWindow(Component c) 	
    {
    	if(c==null) return null;
    	if (c instanceof Window) return (Window)c;
    	Window w = SwingUtilities.getWindowAncestor(c);
    	while(w!=null) 
    	{
    		if(w instanceof Window)   			return (Window)w;
    		w = w.getOwner();
    	}
    	return null;
    }
    
    public static int getWindowHash(Component c)
    {
    	Window w = getWindow(c);
    	return w == null ? -1 : w.hashCode();
    }

    public static JFrame frameFactory()
    {
        JFrame jf;
        setCloseOperation(jf = new JFrame());
        return jf;
    }

    public static void setCloseOperation(JFrame jf)
    {
        jf.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        jf.addWindowListener(getClosingWindowAdapter());
    }

    public static WindowAdapter getClosingWindowAdapter()
    {
        if (localWindowAdapter == null)
            localWindowAdapter = new LocalWindowAdapter();
        return localWindowAdapter;
    }

    public static void setFramePanel(final JFrame jframe, final JPanel panel, final int x, final int y)
    {
    	SwingUtilities.invokeLater(new Runnable(){
    		public void run(){
    			// if MenuBar must be set, do it in activatePanel:
    			//        jframe.setJMenuBar(null);
    			jframe.setContentPane(panel);
    			if (jframe.getTitle() == null || jframe.getTitle().equals(""))
    				jframe.setTitle(panel.getName());
    			// Note that JDialog is similar to JFrame but has no ancestor or interface in common
//    			if (panel instanceof IFramePanel)
//    				((IFramePanel) panel).activatePanel(jframe);

    			jframe.pack();
    			panel.setVisible(true);
    			if (x == 0 && y == 0)        centerWindow(jframe);
    			else if (x >= 0)            jframe.setLocation(x, y);
    			jframe.setVisible(true);
    		}
    	});
     }

    public static void centerWindow(Window win)
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height; // Determine the new location of the JFrame
        int w = win.getSize().width;
        int h = win.getSize().height;
        int x = (screenWidth - w) / 2; // center horizontally
        int y = (screenHeight - h) / 3; // upper third vertically
        win.setLocation(x, y);   // Set the location of the JFrame
    }
   
    public static void centerOnFrame(Window win, Frame frame)
    {
        Rectangle frameRect = frame == null ? getDisplayBounds() : frame.getBounds();
        int frameWidth = frameRect.width;
        int w = win.getSize().width;
        int x = frameRect.x + (frameWidth - w) / 2;  // center horizontally
        int y = frameRect.y + 20; 					// just below top vertically
        win.setLocation(x, y);   					// Set the location of the JFrame
    }

    static public Rectangle sDisplayBounds = null;
	public synchronized static Rectangle getDisplayBounds()
	{
		if (sDisplayBounds == null)
		{
			sDisplayBounds = new Rectangle();
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice[] gs = ge.getScreenDevices();
			for (int j = 0; j < gs.length; j++)
			{
				GraphicsDevice gd = gs[j];
				GraphicsConfiguration[] gc = gd.getConfigurations();
				for (int i = 0; i < gc.length; i++)
					sDisplayBounds = sDisplayBounds.union(gc[i].getBounds());
			}
		}
		return sDisplayBounds;
	}

    public static JDialog dialog;
    
    public static JFrame findJFrame(Component comp)
    {
    	return findParent(comp, JFrame.class);
//        Container target = comp.getParent();
//        while (target != null)
//        {
//            if (target instanceof JFrame)
//                return (JFrame) target;
//            target = target.getParent();
//        }
//        return null;
     }
    
    public static Window findWindow(Component comp)
    {
    	return findParent(comp, Window.class);
    }
    
    public static <T> T findParent(Component comp, Class<T> type)
    {
    	if (comp == null) return null;
        Container target = comp.getParent();
        while (target != null)
        {
            if (type.isInstance(target))
                return type.cast(target);
            target = target.getParent();
        }
        return null;
     }
    
    
    public static Point getShoulderLocation(JFrame win)
    {
        if (win == null) return new Point(0,0);	
        final Point location = win.getLocation();
        Dimension size = win.getSize();
        final int extra = 50;  // require this much to be onscreen
        if (location.getX() + size.width + extra < WindowManager.getDisplayBounds().getWidth())
            location.translate(size.width + 8, 0);
        return location;
    }
    
//    public static boolean show(AbstractController c)
//    {
//    	if (c == null) return false;
//    	return show(c.getJFrame());
//    }

    public static boolean show(JFrame frame)
    {
		if (frame == null) return false;
		frame.setVisible(true);
		if (frame.getExtendedState() == Frame.ICONIFIED)
			frame.setExtendedState(Frame.NORMAL);
		frame.toFront();
		return true;
    }

}
