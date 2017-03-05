package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.FontMetrics;

import javax.swing.JTabbedPane;

import com.floreantpos.swing.TransparentPanel;

public class PizzaExplorer extends TransparentPanel {

	private JTabbedPane mainTab;

	public PizzaExplorer() {
		initComponents();
	}

	private void initComponents() {
		mainTab = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		setLayout(new BorderLayout());
		mainTab.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
			@Override
			protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
				return 30;
			}

			@Override
			protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
				return 125;
			}

		});

		mainTab.addTab("Item", new PizzaItemExplorer());
		mainTab.addTab("Modifier", new PizzaModifierExplorer());
		mainTab.addTab("Size", new MenuItemSizeExplorer());
		mainTab.addTab("Crust", new PizzaCrustExplorer());
		mainTab.addTab("Modifier Group", new ModifierGroupExplorer());

		add(mainTab);
	}
}
