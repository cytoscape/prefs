package org.lib;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;

class LocalWindowAdapter extends WindowAdapter
{
    /** If the contentPane of the Frame is an IFramePanel, call exitAction(). */
    public void windowClosing(WindowEvent e)
    {
        if (e.getSource() instanceof JFrame)
        {
            JFrame frame = (JFrame) e.getSource();
            Container c;
//            if ((c = frame.getContentPane()) instanceof IFramePanel)
//            {
//                ((IFramePanel) c).exitAction();
//            }
//            else 
            	if (frame == WindowManager.getFrame())
                System.exit(0);
            else
                frame.setVisible(false);
        }
        else if (e.getSource() instanceof JDialog)
        {
            JDialog jdialog = (JDialog) e.getSource();
            Container c;
//            if ((c = jdialog.getContentPane()) instanceof IDialogPanel)
//            {
//                if (((IDialogPanel) c).exitAction())
//                    jdialog.setVisible(false);
//            }
//            else
                jdialog.setVisible(false);
        }
        else
            ((Component) e.getSource()).setVisible(false);
    }
}