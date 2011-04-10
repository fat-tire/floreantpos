package com.floreantpos.util;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class PanelTester {
	public static boolean pack;
	public static int width;
	public static int height;
	
	public static void test(JPanel panel) {
		JFrame frame = new JFrame();
		frame.setContentPane(panel);
		if(pack)
		frame.pack();
		else {
			frame.setSize(width, height);
		}
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
