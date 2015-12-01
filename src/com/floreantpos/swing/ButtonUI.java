/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
package com.floreantpos.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;

import com.jgoodies.looks.plastic.PlasticButtonUI;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.PlasticTheme;

public class ButtonUI extends PlasticButtonUI {

	public ButtonUI() {
		super();
	}
	static ButtonUI ui = new ButtonUI();
	
	public static ComponentUI createUI(JComponent b) {
        return ui;
    }

	public void update(Graphics g, JComponent c) {
		if (c.isOpaque()) {
			AbstractButton b = (AbstractButton) c;
			if (isToolBarButton(b)) {
				c.setOpaque(false);
			} else if (b.isContentAreaFilled()) {
				g.setColor(c.getBackground());
				g.fillRect(0, 0, c.getWidth(), c.getHeight());

				if (is3D(b)) {
					Rectangle r = new Rectangle(1, 1, c.getWidth() - 2, c.getHeight() - 1);
					add3DEffekt(g, r);
				}
			}
		}
		paint(g, c);
	}
	
	static void drawDark3DBorder(Graphics g, int x, int y, int w, int h) {
		drawFlush3DBorder(g, x, y, w, h);
		g.setColor(PlasticLookAndFeel.getControl());
		g.drawLine(x+1, y+1, 1, h - 3);
		g.drawLine(y+1, y+1, w - 3, 1);
	}


	static void drawDisabledBorder(Graphics g, int x, int y, int w, int h) {
		g.setColor(PlasticLookAndFeel.getControlShadow());
		drawRect(g, x, y, w - 1, h - 1);
	}


	/*
	 * Unlike <code>MetalUtils</code> we first draw with highlight then dark shadow
	 */
	static void drawFlush3DBorder(Graphics g, int x, int y, int w, int h) {
		g.translate(x, y);
		g.setColor(PlasticLookAndFeel.getControlHighlight());
		drawRect(g, 1, 1, w - 2, h - 2);
		g.drawLine(0, h - 1, 0, h - 1);
		g.drawLine(w - 1, 0, w - 1, 0);
		g.setColor(PlasticLookAndFeel.getControlDarkShadow());
		drawRect(g, 0, 0, w - 2, h - 2);
		g.translate(-x, -y);
	}


	/*
	 * Copied from <code>MetalUtils</code>.
	 */
	static void drawPressed3DBorder(Graphics g, int x, int y, int w, int h) {
		g.translate(x, y);
		drawFlush3DBorder(g, 0, 0, w, h);
		g.setColor(PlasticLookAndFeel.getControlShadow());
		g.drawLine(1, 1, 1, h - 3);
		g.drawLine(1, 1, w - 3, 1);
		g.translate(-x, -y);
	}


	/*
	 * Copied from <code>MetalUtils</code>.
	 */
    static void drawButtonBorder(Graphics g, int x, int y, int w, int h, boolean active) {
        if (active) {
            drawActiveButtonBorder(g, x, y, w, h);	    
        } else {
            drawFlush3DBorder(g, x, y, w, h);
		}
    }

	/*
	 * Copied from <code>MetalUtils</code>.
	 */
    static void drawActiveButtonBorder(Graphics g, int x, int y, int w, int h) {
        drawFlush3DBorder(g, x, y, w, h);
        g.setColor( PlasticLookAndFeel.getPrimaryControl() );
		g.drawLine( x+1, y+1, x+1, h-3 );
		g.drawLine( x+1, y+1, w-3, x+1 );
        g.setColor( PlasticLookAndFeel.getPrimaryControlDarkShadow() );
		g.drawLine( x+2, h-2, w-2, h-2 );
		g.drawLine( w-2, y+2, w-2, h-2 );
    }

	/*
	 * Modified edges.
	 */
    static void drawDefaultButtonBorder(Graphics g, int x, int y, int w, int h, boolean active) {
        drawButtonBorder(g, x+1, y+1, w-1, h-1, active);	    
        g.translate(x, y);
        g.setColor(PlasticLookAndFeel.getControlDarkShadow() );
		drawRect(g, 0, 0, w-3, h-3 );
		g.drawLine(w-2, 0, w-2, 0);
		g.drawLine(0, h-2, 0, h-2);
		g.setColor(PlasticLookAndFeel.getControl());
		g.drawLine(w-1, 0, w-1, 0);
		g.drawLine(0, h-1, 0, h-1);
        g.translate(-x, -y);
    }
    
    static void drawDefaultButtonPressedBorder(Graphics g, int x, int y, int w, int h) {
        drawPressed3DBorder(g, x + 1, y + 1, w - 1, h - 1);
        g.translate(x, y);
        g.setColor(PlasticLookAndFeel.getControlDarkShadow());
        drawRect(g, 0, 0, w - 3, h - 3);
        g.drawLine(w - 2, 0, w - 2, 0);
        g.drawLine(0, h - 2, 0, h - 2);
        g.setColor(PlasticLookAndFeel.getControl());
        g.drawLine(w - 1, 0, w - 1, 0);
        g.drawLine(0, h - 1, 0, h - 1);
        g.translate(-x, -y);
    }

	static void drawThinFlush3DBorder(Graphics g, int x, int y, int w, int h) {
		g.translate(x, y);
		g.setColor(PlasticLookAndFeel.getControlHighlight());
		g.drawLine(0, 0, w - 2, 0);
		g.drawLine(0, 0, 0, h - 2);
		g.setColor(PlasticLookAndFeel.getControlDarkShadow());
		g.drawLine(w - 1, 0, w - 1, h - 1);
		g.drawLine(0, h - 1, w - 1, h - 1);
		g.translate(-x, -y);
	}
	
	
	static void drawThinPressed3DBorder(Graphics g, int x, int y, int w, int h) {
		g.translate(x, y);
		g.setColor(PlasticLookAndFeel.getControlDarkShadow());
		g.drawLine(0, 0, w - 2, 0);
		g.drawLine(0, 0, 0, h - 2);
		g.setColor(PlasticLookAndFeel.getControlHighlight());
		g.drawLine(w - 1, 0, w - 1, h - 1);
		g.drawLine(0, h - 1, w - 1, h - 1);
		g.translate(-x, -y);
	}
	
	/*
	 * Convenience function for determining ComponentOrientation.  Helps us
	 * avoid having Munge directives throughout the code.
	 */
	static boolean isLeftToRight(Component c) {
		return c.getComponentOrientation().isLeftToRight();
	}
	
	
	// 3D Effects ***********************************************************************

	/**
	 * Checks and returns whether the specified component type has 3D effects.
     * 
     * @param keyPrefix    the prefix of the key used to lookup the setting
     * @return true if the component type shall be rendered with a 3D effect
     * @see #force3D(JComponent)
     * @see #forceFlat(JComponent)
	 */	
	static boolean is3D(String keyPrefix) {
		Object value = UIManager.get(keyPrefix + "is3DEnabled"); //$NON-NLS-1$
		return Boolean.TRUE.equals(value);
	}


	/**
	 * Checks and returns whether we have a custom hint that forces the 3D mode.
	 * 
     * @param c   the component to inspect
     * @return true if the given component has a 3D hint set
	 * @see #forceFlat(JComponent)
	 */
	static boolean force3D(JComponent c) {
		Object value = c.getClientProperty(PlasticLookAndFeel.IS_3D_KEY);
		return Boolean.TRUE.equals(value);
	}


	/**
	 * Checks and returns whether we have a custom hint that prevents the 3D mode.
	 * 
     * @param c   the component to inspect
     * @return true if the given component has a flat hint set
	 * @see #force3D(JComponent)
	 */
	static boolean forceFlat(JComponent c) {
		Object value = c.getClientProperty(PlasticLookAndFeel.IS_3D_KEY);
		return Boolean.FALSE.equals(value);
	}


	// Painting 3D Effects *************************************************************
	
	private static float FRACTION_3D = 0.5f;
	

	private static void add3DEffekt(Graphics g, Rectangle r, boolean isHorizontal,
		Color startC0, Color stopC0, Color startC1, Color stopC1) {
			
		Graphics2D g2 = (Graphics2D) g;
		int xb0, yb0, xb1, yb1, xd0, yd0, xd1, yd1, width, height;
		if (isHorizontal) {
			width = r.width;
			height = (int) (r.height * FRACTION_3D);
			xb0 = r.x;
			yb0 = r.y;
			xb1 = xb0;
			yb1 = yb0 + height;
			xd0 = xb1;
			yd0 = yb1;
			xd1 = xd0;
			yd1 = r.y + r.height;
		} else {
			width = (int) (r.width * FRACTION_3D);
			height = r.height;
			xb0 = r.x;
			yb0 = r.y;
			xb1 = xb0 + width;
			yb1 = yb0;
			xd0 = xb1;
			yd0 = yb0;
			xd1 = r.x + r.width;
			yd1 = yd0;
		}
		g2.setPaint(new GradientPaint(xb0, yb0, stopC0, xb1, yb1, startC0));
		g2.fillRect(r.x, r.y, width, height);
		g2.setPaint(new GradientPaint(xd0, yd0, startC1, xd1, yd1, stopC1));
		g2.fillRect(xd0, yd0, width, height);
	}


	static void add3DEffekt(Graphics g, Rectangle r) {
		Color brightenStop = UIManager.getColor("Plastic.brightenStop"); //$NON-NLS-1$
		if (null == brightenStop)
			brightenStop = PlasticTheme.BRIGHTEN_STOP;

		// Add round sides
		Graphics2D g2 = (Graphics2D) g;
		int border = 10;
		g2.setPaint(new GradientPaint(r.x, r.y, brightenStop, r.x + border, r.y, PlasticTheme.BRIGHTEN_START));
		g2.fillRect(r.x, r.y, border, r.height);
		int x = r.x + r.width -border;
		int y = r.y;
		g2.setPaint(new GradientPaint(x, y, PlasticTheme.DARKEN_START, x + border, y, PlasticTheme.LT_DARKEN_STOP));
		g2.fillRect(x, y, border, r.height);

		add3DEffekt(g, r, true, PlasticTheme.BRIGHTEN_START, brightenStop, PlasticTheme.DARKEN_START, PlasticTheme.LT_DARKEN_STOP);
	}


	static void addLight3DEffekt(Graphics g, Rectangle r, boolean isHorizontal) {
		Color ltBrightenStop = UIManager.getColor("Plastic.ltBrightenStop"); //$NON-NLS-1$
		if (null == ltBrightenStop)
			ltBrightenStop = PlasticTheme.LT_BRIGHTEN_STOP;

		add3DEffekt(g, r, isHorizontal, PlasticTheme.BRIGHTEN_START, ltBrightenStop, PlasticTheme.DARKEN_START, PlasticTheme.LT_DARKEN_STOP);
	}
	
	
	/*
	 * FillerUI.
	 */
	public static void addLight3DEffekt(Graphics g, Rectangle r) {
		Color ltBrightenStop = UIManager.getColor("Plastic.ltBrightenStop"); //$NON-NLS-1$
		if (null == ltBrightenStop)
			ltBrightenStop = PlasticTheme.LT_BRIGHTEN_STOP;

		add3DEffekt(g, r, true, PlasticTheme.DARKEN_START, PlasticTheme.LT_DARKEN_STOP, PlasticTheme.BRIGHTEN_START, ltBrightenStop);
	}
    

    // Low level graphics ***************************************************

    /*
     * An optimized version of Graphics.drawRect.
     */
    private static void drawRect(Graphics g, int x, int y, int w, int h) {
        g.fillRect(x,   y,   w+1, 1);
        g.fillRect(x,   y+1, 1,   h);
        g.fillRect(x+1, y+h, w,   1);
        g.fillRect(x+w, y+1, 1,   h);
    }

}
