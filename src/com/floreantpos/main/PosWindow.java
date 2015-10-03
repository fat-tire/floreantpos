package com.floreantpos.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.jdesktop.swingx.JXStatusBar;

import com.floreantpos.config.AppConfig;
import com.floreantpos.swing.GlassPane;

public class PosWindow extends JFrame implements WindowListener {
	private static final String EXTENDEDSTATE = "extendedstate"; //$NON-NLS-1$
	private static final String WLOCY = "wlocy"; //$NON-NLS-1$
	private static final String WLOCX = "wlocx"; //$NON-NLS-1$
	private static final String WHEIGHT = "wheight"; //$NON-NLS-1$
	private static final String WWIDTH = "wwidth"; //$NON-NLS-1$
	
	private GlassPane glassPane;
	private JXStatusBar statusBar;
	private JLabel statusLabel;
	
	public PosWindow() {
		setIconImage(Application.getApplicationIcon().getImage());
		
		addWindowListener(this);
		
		glassPane = new GlassPane();
		glassPane.setOpacity(0.6f);
		setGlassPane(glassPane);
		
		statusBar = new JXStatusBar();
		getContentPane().add(statusBar, BorderLayout.SOUTH);
		
		statusLabel = new JLabel(""); //$NON-NLS-1$
		statusBar.add(statusLabel, JXStatusBar.Constraint.ResizeBehavior.FILL);
	}
	
	public void setStatus(String status) {
		statusLabel.setText(status);
	}
	
	public void setupSizeAndLocation() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(AppConfig.getInt(WWIDTH, (int) screenSize.getWidth()), AppConfig.getInt(WHEIGHT, (int) screenSize.getHeight()));
		
		setLocation(AppConfig.getInt(WLOCX, ((screenSize.width - getWidth()) >> 1)), AppConfig.getInt(WLOCY, ((screenSize.height - getHeight()) >> 1)));
		setMinimumSize(new Dimension(800, 600));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		int extendedState = AppConfig.getInt(EXTENDEDSTATE, -1);
		if(extendedState != -1) {
			setExtendedState(extendedState);
		}
	}

	public void enterFullScreenMode() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize);
		setExtendedState(MAXIMIZED_BOTH);
		setUndecorated(true);
		setLocation(0, 0);
		//GraphicsDevice window = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
		//window.setFullScreenWindow(this);
	}
	
	public void leaveFullScreenMode() {
		GraphicsDevice window = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
		setUndecorated(false);
		window.setFullScreenWindow(null);
	}
	
	public void saveSizeAndLocation() {
		int width = getWidth();
		int height = getHeight();
		AppConfig.putInt(WWIDTH, width);
		AppConfig.putInt(WHEIGHT, height);

		Point locationOnScreen = getLocationOnScreen();
		AppConfig.putInt(WLOCX, locationOnScreen.x);
		AppConfig.putInt(WLOCY, locationOnScreen.y);
		
		AppConfig.putInt(EXTENDEDSTATE, getExtendedState());
	}
	
	public void setGlassPaneVisible(boolean b) {
		glassPane.setVisible(b);
	}
	
	/*public void setGlassPaneMessage(String message) {
		glassPane.setMessage(message);
	}*/

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