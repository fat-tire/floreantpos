package com.floreantpos.swing;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;

public class TransparentPanelUI extends BasicPanelUI {
	
	public TransparentPanelUI() {
		super();
	}

	static TransparentPanelUI ui = new TransparentPanelUI();

	public static ComponentUI createUI(JComponent b) {
		return ui;
	}

	public void update(Graphics g, JComponent c) {
		if (c.isOpaque()) {
			Color color1 = UIManager.getColor("control");
			Color color2 = color1.brighter();

			int x = 0;
			int y = 0;
			int width = c.getWidth();
			int height = c.getHeight();
			GradientPaint gp = new GradientPaint(x, y, color2, width - 2, height - 2, color1, true);
			Graphics2D g2 = (Graphics2D) g;
			g2.setPaint(gp);
			g2.fillRect(x, y, width, height);
		}
		paint(g, c);
	}

}
