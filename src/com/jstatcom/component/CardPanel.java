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
package com.jstatcom.component;

// source:
// http://java.sun.com/products/jfc/tsc/articles/cardpanel/

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;

import javax.swing.JPanel;

/**
 * source:
 * http://java.sun.com/products/jfc/tsc/articles/cardpanel/
 * <p>
 * 
 * A simpler alternative to a JPanel with a CardLayout. The AWT CardLayout
 * layout manager can be inconvenient to use because the special "stack of
 * cards" operations it supports require a cast to use. For example to show the
 * card named "myCard" given a JPanel with a CardLayout one would write:
 * 
 * <pre>
 * ((CardLayout) (myJPanel.getLayout())).show(myJPanel, &quot;myCard&quot;);
 * </pre>
 * 
 * This doesn't work well with Swing - all of the CardLayout display operations,
 * like <code>show</code> call validate directly. Swing supports automatic
 * validation (see JComponent.revalidate()); this direct call to validate is
 * inefficient.
 * <p>
 * The CardPane JPanel subclass is intended to support a layout with a modest
 * number of cards, on the order of 100 or less. A cards name is it's component
 * name, as in java.awt.Component.getName(), which is set when the component is
 * added to the CardPanel:
 * 
 * <pre>
 * 
 * myCardPanel.add(myChild, &quot;MyChildName&quot;);
 * 
 * </pre>
 * 
 * As with CardLayout, the first child added to a CardPanel is made visible and
 * there's only one child visible at a time. The <code>showCard</code> method
 * accepts either a childs name or the child itself:
 * 
 * <pre>
 * myCardPanel.show(&quot;MyChildName&quot;);
 * myCardPanel.show(myChild);
 * </pre>
 * 
 * <p>
 * The CardPanel class doesn't support the vgap/hgap CardLayout properties since
 * one can add a Border, see JComponent.setBorder().
 * <p>
 * Originallly the <code>getVisibleChild()</code> method was private. I
 * changed it to protected. Alexander Benkwitz Feb 2002.
 */

public class CardPanel extends JPanel {
    private static class Layout implements LayoutManager {
        /**
         * Set the childs name (if non-null) and and make it visible iff it's
         * the only CardPanel child.
         * 
         * @see java.awt.Component#setName
         */
        public void addLayoutComponent(String name, Component child) {
            if (name != null) {
                child.setName(name);
            }
            child.setVisible(child.getParent().getComponentCount() == 1);
        }

        /**
         * If this child was visible, then make the first remaining child
         * visible.
         */
        public void removeLayoutComponent(Component child) {
            if (child.isVisible()) {
                Container parent = child.getParent();
                if (parent.getComponentCount() > 0) {
                    parent.getComponent(0).setVisible(true);
                }
            }
        }

        /**
         * @return the maximum preferred width/height + the parents insets
         */
        public Dimension preferredLayoutSize(Container parent) {
            int nChildren = parent.getComponentCount();
            Insets insets = parent.getInsets();
            int width = insets.left + insets.right;
            int height = insets.top + insets.bottom;

            for (int i = 0; i < nChildren; i++) {
                Dimension d = parent.getComponent(i).getPreferredSize();
                if (d.width > width) {
                    width = d.width;
                }
                if (d.height > height) {
                    height = d.height;
                }
            }
            return new Dimension(width, height);
        }

        /**
         * @return the maximum minimum width/height + the parents insets
         */
        public Dimension minimumLayoutSize(Container parent) {
            int nChildren = parent.getComponentCount();
            Insets insets = parent.getInsets();
            int width = insets.left + insets.right;
            int height = insets.top + insets.bottom;

            for (int i = 0; i < nChildren; i++) {
                Dimension d = parent.getComponent(i).getMinimumSize();
                if (d.width > width) {
                    width = d.width;
                }
                if (d.height > height) {
                    height = d.height;
                }
            }
            return new Dimension(width, height);
        }

        public void layoutContainer(Container parent) {
            int nChildren = parent.getComponentCount();
            Insets insets = parent.getInsets();
            for (int i = 0; i < nChildren; i++) {
                Component child = parent.getComponent(i);
                if (child.isVisible()) {
                    Rectangle r = parent.getBounds();
                    int width = r.width - insets.left + insets.right;
                    int height = r.height - insets.top + insets.bottom;
                    child.setBounds(insets.left, insets.top, width, height);
                    break;
                }
            }
        }
    }

    /**
     * Creates a <code>CardPanel</code>. Children, called "cards" in this
     * API, should be added with add(). The first child we be made visible,
     * subsequent children will be hidden. To show a card, use one of the
     * show*Card methods.
     */
    public CardPanel() {
        super(new Layout());
    }

    /**
     * Return the index of the first (and one would hope - only) visible child.
     * If a visible child can't be found, perhaps the caller has inexlicably
     * hidden all of the children, then return -1.
     */
    protected int getVisibleChildIndex() {
        int nChildren = getComponentCount();
        for (int i = 0; i < nChildren; i++) {
            Component child = getComponent(i);
            if (child.isVisible()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Hide the currently visible child "card" and show the specified card. If
     * the specified card isn't a child of the CardPanel then we add it here.
     */
    public void showCard(Component card) {
        if (card.getParent() != this) {
            add(card);
        }
        int index = getVisibleChildIndex();
        if (index != -1) {
            getComponent(index).setVisible(false);
        }
        card.setVisible(true);
        revalidate();
        repaint();
    }

    /**
     * Show the card with the specified name.
     * 
     * @see java.awt.Component#getName
     */
    public void showCard(String name) {
        int nChildren = getComponentCount();
        for (int i = 0; i < nChildren; i++) {
            Component child = getComponent(i);
            if (child.getName().equals(name)) {
                showCard(child);
                break;
            }
        }
    }

    /**
     * Show the first card that was added to this CardPanel.
     */
    public void showFirstCard() {
        if (getComponentCount() <= 0) {
            return;
        }
        showCard(getComponent(0));
    }

    /**
     * Show the last card that was added to this CardPanel.
     */
    public void showLastCard() {
        if (getComponentCount() <= 0) {
            return;
        }
        showCard(getComponent(getComponentCount() - 1));
    }

    /**
     * Show the card that was added to this CardPanel after the currently
     * visible card. If the currently visible card was added last, then show the
     * first card.
     */
    public void showNextCard() {
        if (getComponentCount() <= 0) {
            return;
        }
        int index = getVisibleChildIndex();
        if (index == -1) {
            showCard(getComponent(0));
        } else if (index == (getComponentCount() - 1)) {
            showCard(getComponent(0));
        } else {
            showCard(getComponent(index + 1));
        }
    }

    /**
     * Show the card that was added to this CardPanel before the currently
     * visible card. If the currently visible card was added first, then show
     * the last card.
     */
    public void showPreviousCard() {
        if (getComponentCount() <= 0) {
            return;
        }
        int index = getVisibleChildIndex();
        if (index == -1) {
            showCard(getComponent(0));
        } else if (index == 0) {
            showCard(getComponent(getComponentCount() - 1));
        } else {
            showCard(getComponent(index - 1));
        }
    }
}