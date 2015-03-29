package com.floreantpos.ui.views.order;

import java.awt.Component;
import java.awt.LayoutManager;

import javax.swing.JPanel;

import com.floreantpos.ui.views.IView;

public abstract class ViewPanel extends JPanel implements IView {

	public ViewPanel() {
		super();
	}

	public ViewPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	public ViewPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	public ViewPanel(LayoutManager layout) {
		super(layout);
	}

	@Override
	public Component getViewComponent() {
		return this;
	}

}
