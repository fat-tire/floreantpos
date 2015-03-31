package com.floreantpos.swing;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class PosScrollPane extends JScrollPane {

	public PosScrollPane() {
		JScrollBar scrollBar = getVerticalScrollBar();
		if(scrollBar != null) {
			scrollBar.setPreferredSize(new Dimension(40, 40));
		}
	}

	public PosScrollPane(Component view) {
		super(view);
		
		JScrollBar scrollBar = getVerticalScrollBar();
		if(scrollBar != null) {
			scrollBar.setPreferredSize(new Dimension(40, 40));
		}
	}

	public PosScrollPane(int vsbPolicy, int hsbPolicy) {
		super(vsbPolicy, hsbPolicy);
		
		JScrollBar scrollBar = getVerticalScrollBar();
		if(scrollBar != null) {
			scrollBar.setPreferredSize(new Dimension(40, 40));
		}
	}

	public PosScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
		super(view, vsbPolicy, hsbPolicy);
		
		JScrollBar scrollBar = getVerticalScrollBar();
		if(scrollBar != null) {
			scrollBar.setPreferredSize(new Dimension(40, 40));
		}
	}

}
