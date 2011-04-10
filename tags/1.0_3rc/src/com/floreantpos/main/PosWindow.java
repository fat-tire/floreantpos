package com.floreantpos.main;

import javax.swing.JFrame;

import com.floreantpos.swing.GlassPane;

public class PosWindow extends JFrame {
	private GlassPane glassPane;
	
	public PosWindow() {
		glassPane = new GlassPane();
		glassPane.setOpacity(0.6f);
		setGlassPane(glassPane);
	}
	
	public void setGlassPaneVisible(boolean b) {
		glassPane.setVisible(b);
	}
}
