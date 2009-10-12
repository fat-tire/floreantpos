package com.floreantpos.swing;

import java.awt.LayoutManager;

import javax.swing.JPanel;




public class TransparentPanel extends JPanel {
	
	static {
		//UIManager.put("TransparentPanelUI", "com.floreantpos.swing.TransparentPanelUI");
	}

	public TransparentPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		
		setOpaque(false);
	}

	public TransparentPanel(LayoutManager layout) {
		super(layout);
		
		setOpaque(false);
	}

	public TransparentPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		
		setOpaque(false);
	}

	public TransparentPanel() {
		super();
		setOpaque(false);
	}
	
//	@Override
//	public String getUIClassID() {
//		return "TransparentPanelUI";
//	}
}
