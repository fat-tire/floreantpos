package com.floreantpos.main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import com.floreantpos.swing.GlassPane;

public class PosWindow extends JFrame implements WindowListener {
	private GlassPane glassPane;
	
	public PosWindow() {
		setIconImage(Application.getApplicationIcon().getImage());
		
		glassPane = new GlassPane();
		glassPane.setOpacity(0.6f);
		setGlassPane(glassPane);
	}
	
	public void setGlassPaneVisible(boolean b) {
		glassPane.setVisible(b);
	}
	
	public void setGlassPaneMessage(String message) {
		glassPane.setMessage(message);
	}

	public void windowOpened(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		Application.getInstance().shutdownPOS();
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}
}
