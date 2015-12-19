package com.floreantpos.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

public class ScrollableFlowPanel extends JPanel implements Scrollable {
	private ScrollableFlowLayout layout;
	private JPanel contentPane;
	
	public ScrollableFlowPanel() {
		this(FlowLayout.CENTER);
	}
	
	public ScrollableFlowPanel(int alignment) {
		super(new BorderLayout());
		
		layout = new ScrollableFlowLayout(alignment);
		contentPane = new JPanel(layout);
		
		super.add(contentPane);
	}
	
	@Override
	public Component add(Component comp) {
		return contentPane.add(comp);
	}
	
	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}
	
	@Override
	public Dimension getPreferredSize() {
		Dimension preferredSize = super.getPreferredSize();
		preferredSize.height = layout.getLayoutHeight();
		
		return preferredSize;
	}
	
	public JPanel getContentPane() {
		return contentPane;
	}

	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 10;
	}

	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return ((orientation == SwingConstants.VERTICAL) ? visibleRect.height : visibleRect.width) - 10;
	}

	public boolean getScrollableTracksViewportWidth() {
		return true;
	}

	public boolean getScrollableTracksViewportHeight() {
		return false;
	}
	
	class ScrollableFlowLayout extends FlowLayout {
		private int layoutHeight;
		
		public ScrollableFlowLayout() {
		}

		public ScrollableFlowLayout(int align) {
			super(align);
		}

		public ScrollableFlowLayout(int align, int hgap, int vgap) {
			super(align, hgap, vgap);
		}

		/**
		 * Centers the elements in the specified row, if there is any slack.
		 * @param target the component which needs to be moved
		 * @param x the x coordinate
		 * @param y the y coordinate
		 * @param width the width dimensions
		 * @param height the height dimensions
		 * @param rowStart the beginning of the row
		 * @param rowEnd the the ending of the row
		 * @param useBaseline Whether or not to align on baseline.
		 * @param ascent Ascent for the components. This is only valid if
		 *               useBaseline is true.
		 * @param descent Ascent for the components. This is only valid if
		 *               useBaseline is true.
		 * @return actual row height
		 */
		private int moveComponents(Container target, int x, int y, int width, int height, int rowStart, int rowEnd, boolean ltr, boolean useBaseline, int[] ascent,
				int[] descent) {
			switch (getAlignment()) {
				case LEFT:
					x += ltr ? 0 : width;
					break;
				case CENTER:
					x += width / 2;
					break;
				case RIGHT:
					x += ltr ? width : 0;
					break;
				case LEADING:
					break;
				case TRAILING:
					x += width;
					break;
			}
			int maxAscent = 0;
			int nonbaselineHeight = 0;
			int baselineOffset = 0;
			if (useBaseline) {
				int maxDescent = 0;
				for (int i = rowStart; i < rowEnd; i++) {
					Component m = target.getComponent(i);
					if (m.isVisible()) {
						if (ascent[i] >= 0) {
							maxAscent = Math.max(maxAscent, ascent[i]);
							maxDescent = Math.max(maxDescent, descent[i]);
						}
						else {
							nonbaselineHeight = Math.max(m.getHeight(), nonbaselineHeight);
						}
					}
				}
				height = Math.max(maxAscent + maxDescent, nonbaselineHeight);
				baselineOffset = (height - maxAscent - maxDescent) / 2;
			}
			for (int i = rowStart; i < rowEnd; i++) {
				Component m = target.getComponent(i);
				if (m.isVisible()) {
					int cy;
					if (useBaseline && ascent[i] >= 0) {
						cy = y + baselineOffset + maxAscent - ascent[i];
					}
					else {
						cy = y + (height - m.getSize().height) / 2;
					}
					if (ltr) {
						m.setLocation(x, cy);
					}
					else {
						m.setLocation(target.getSize().width - x - m.getSize().width, cy);
					}
					x += m.getSize().width + getHgap();
				}
			}
			return height;
		}

		/**
		 * Lays out the container. This method lets each
		 * <i>visible</i> component take
		 * its preferred size by reshaping the components in the
		 * target container in order to satisfy the alignment of
		 * this <code>FlowLayout</code> object.
		 *
		 * @param target the specified component being laid out
		 * @see Container
		 * @see       java.awt.Container#doLayout
		 */
		public void layoutContainer(Container target) {
			synchronized (target.getTreeLock()) {
				layoutHeight = 0;
				Insets insets = target.getInsets();
				int maxwidth = target.getSize().width - (insets.left + insets.right + getHgap() * 2);
				int nmembers = target.getComponentCount();
				int x = 0, y = insets.top + getVgap();
				int rowh = 0, start = 0;

				boolean ltr = target.getComponentOrientation().isLeftToRight();

				boolean useBaseline = getAlignOnBaseline();
				int[] ascent = null;
				int[] descent = null;

				if (useBaseline) {
					ascent = new int[nmembers];
					descent = new int[nmembers];
				}

				for (int i = 0; i < nmembers; i++) {
					Component m = target.getComponent(i);
					if (m.isVisible()) {
						Dimension d = m.getPreferredSize();
						m.setSize(d.width, d.height);

						if (useBaseline) {
							int baseline = m.getBaseline(d.width, d.height);
							if (baseline >= 0) {
								ascent[i] = baseline;
								descent[i] = d.height - baseline;
							}
							else {
								ascent[i] = -1;
							}
						}
						if ((x == 0) || ((x + d.width) <= maxwidth)) {
							if (x > 0) {
								x += getHgap();
							}
							x += d.width;
							rowh = Math.max(rowh, d.height);
						}
						else {
							rowh = moveComponents(target, insets.left + getHgap(), y, maxwidth - x, rowh, start, i, ltr, useBaseline, ascent, descent);
							x = d.width;
							y += getVgap() + rowh;
							rowh = d.height;
							start = i;
							layoutHeight += rowh + getVgap();
						}
					}
				}
				layoutHeight += moveComponents(target, insets.left + getHgap(), y, maxwidth - x, rowh, start, nmembers, ltr, useBaseline, ascent, descent);
				layoutHeight += getVgap() * 2;
			}
		}

		public int getLayoutHeight() {
			return layoutHeight;
		}

	}
}