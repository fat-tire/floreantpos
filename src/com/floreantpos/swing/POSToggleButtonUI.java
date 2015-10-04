/**
 * 
 */
package com.floreantpos.swing;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;

import com.jgoodies.looks.plastic.PlasticToggleButtonUI;

public class POSToggleButtonUI extends PlasticToggleButtonUI {
	private static final POSToggleButtonUI INSTANCE = new POSToggleButtonUI();
	
	private final static Border pressedBorder = BorderFactory.createLineBorder(Color.black, 5);
	private final static Border defaultBorder = UIManager.getBorder("Button.border"); //$NON-NLS-1$

	public static ComponentUI createUI(JComponent b) {
		return INSTANCE;
	}

	public void update(Graphics g, JComponent c) {
		if (c.isOpaque()) {
			AbstractButton b = (AbstractButton) c;
			if (isToolBarButton(b)) {
				c.setOpaque(false);
			}
			else if (b.isContentAreaFilled()) {
				g.setColor(c.getBackground());
				g.fillRect(0, 0, c.getWidth(), c.getHeight());

				if (is3D(b)) {
					Color color1 = c.getBackground();// UIManager.getColor("control");
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
			}
		}
		c.setBorder(defaultBorder);
		paint(g, c);
	}

	protected void paintButtonPressed(Graphics g, AbstractButton b) {
		if (b.isContentAreaFilled()) {
			Color background = b.getBackground().darker();
			g.setColor(background);
			g.fillRect(0, 0, b.getWidth(), b.getHeight());
			//b.setBorder(pressedBorder);
		}
	}
}