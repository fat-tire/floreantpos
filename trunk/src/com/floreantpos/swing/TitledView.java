package com.floreantpos.swing;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import com.floreantpos.ui.TitlePanel;

public class TitledView extends JPanel {
	private TitlePanel titlePanel;
	private JPanel contentPane;

	public TitledView() {
		this("");
	}

	public TitledView(String title) {
		this.titlePanel = new TitlePanel();
		this.contentPane = new JPanel();
		
		this.setLayout(new BorderLayout());

		this.add(titlePanel, BorderLayout.NORTH);
		this.add(contentPane);
		
		setTitle(title);
	}
	
	public void setTitle(String title) {
		this.titlePanel.setTitle(title);
	}
	
	public String getTitle() {
		return this.titlePanel.getTitle();
	}
	
	public void setTitlePaneVisible(boolean visible) {
		this.titlePanel.setVisible(visible);
	}
	
	public boolean isTitlePaneVisible() {
		return this.titlePanel.isVisible();
	}
	
	public JPanel getContentPane() {
		return this.contentPane;
	}
	
}
