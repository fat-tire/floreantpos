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
package com.floreantpos.ui.views.order;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXTitledSeparator;

import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.swing.PosButton;

public abstract class SelectionView extends JPanel implements ComponentListener {
	private final static int HORIZONTAL_GAP = 5;
	private final static int VERTICAL_GAP = 5;

	protected List items;

	private Dimension buttonSize;

	protected CardLayout cardLayout = new CardLayout();
	private JPanel cardLayoutContainer = new JPanel(cardLayout);
	protected JPanel buttonPanelContainer = new JPanel(new BorderLayout());

	protected TitledBorder border;

	protected JPanel actionButtonPanel = new JPanel(new MigLayout("fill,hidemode 3, ins 2", "sg, fill", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	protected com.floreantpos.swing.PosButton btnNext;
	protected com.floreantpos.swing.PosButton btnPrev;

	private boolean initialized = true;

	@SuppressWarnings("unused")
	private String title;

	public SelectionView(String title, int buttonWidth, int buttonHeight) {
		this.title = title;
		this.buttonSize = new Dimension(buttonWidth, buttonHeight);

		border = new TitledBorder(title);
		border.setTitleJustification(TitledBorder.CENTER);
		setBorder(new CompoundBorder(border, new EmptyBorder(2, 2, 2, 2)));

		setLayout(new BorderLayout(HORIZONTAL_GAP, VERTICAL_GAP));

		buttonPanelContainer.add(cardLayoutContainer);
		add(buttonPanelContainer);

		btnPrev = new PosButton();
		btnPrev.setText(POSConstants.CAPITAL_PREV);
		actionButtonPanel.add(btnPrev, "grow, align center"); //$NON-NLS-1$

		btnNext = new PosButton();
		btnNext.setText(POSConstants.CAPITAL_NEXT);
		actionButtonPanel.add(btnNext, "grow, align center"); //$NON-NLS-1$

		add(actionButtonPanel, BorderLayout.SOUTH);

		ScrollAction action = new ScrollAction();
		btnPrev.addActionListener(action);
		btnNext.addActionListener(action);

		addComponentListener(this);

		btnNext.setVisible(false);
		btnPrev.setVisible(false);
	}

	public SelectionView(String title) {
		this(title, 120, TerminalConfig.getMenuItemButtonHeight());
	}

	public void setTitle(String title) {
		border.setTitle(title);
	}

	public void setItems(List items) {
		this.items = items;
		renderItems();
	}

	public List getItems() {
		return items;
	}

	public Dimension getButtonSize() {
		return buttonSize;
	}

	public void setButtonSize(Dimension buttonSize) {
		this.buttonSize = buttonSize;
	}

	protected abstract AbstractButton createItemButton(Object item);

	public void reset() {
		cardLayoutContainer.removeAll();
		//		btnNext.setEnabled(false);
		//		btnPrev.setEnabled(false);
		//		
		//		Component[] components = buttonsPanel.getComponents();
		//		for (int i = 0; i < components.length; i++) {
		//			Component c = components[i];
		//			if (c instanceof AbstractButton) {
		//				AbstractButton button = (AbstractButton) c;
		//				button.setPreferredSize(null);
		//
		//				ActionListener[] actionListeners = button.getActionListeners();
		//				if (actionListeners != null) {
		//					for (int j = 0; j < actionListeners.length; j++) {
		//						button.removeActionListener(actionListeners[j]);
		//					}
		//				}
		//			}
		//		}
		//		buttonsPanel.removeAll();
		//		buttonsPanel.revalidate();
		//		buttonsPanel.repaint();
	}

	protected int getHorizontalButtonCount() {
		Dimension size = buttonPanelContainer.getSize();
		Dimension itemButtonSize = getButtonSize();

		return getButtonCount(size.width, itemButtonSize.width);
	}

	protected int getFitableButtonCount() {
		Dimension size = buttonPanelContainer.getSize();
		Dimension itemButtonSize = getButtonSize();

		int horizontalButtonCount = getButtonCount(size.width, itemButtonSize.width);
		int verticalButtonCount = getButtonCount(size.height, itemButtonSize.height);

		int totalItem = horizontalButtonCount * verticalButtonCount;

		return totalItem;
	}

	protected void renderItems() {
		reset();

		if (this.items == null || items.size() == 0) {
			revalidate();
			repaint();
			return;
		}

		Dimension itemButtonSize = getButtonSize();

		//buttonsPanel.setLayout(new MigLayout("alignx 50%, wrap " + horizontalButtonCount)); //$NON-NLS-1$

		int totalItem = getFitableButtonCount();

		try {
			ButtonPanel buttonPanel = null;
			for (int i = 0; i < items.size(); i++) {
				if (i % totalItem == 0) {
					buttonPanel = new ButtonPanel("buttonpanel-" + i);
					buttonPanel.setLayout(createButtonPanelLayout());
					cardLayoutContainer.add(buttonPanel, buttonPanel.getName());
				}

				Object item = items.get(i);

				AbstractButton itemButton = createItemButton(item);
				if (itemButton == null) {
					continue;
				}

				itemButton.setPreferredSize(itemButtonSize);
				buttonPanel.add(itemButton);
			}
		} catch (Exception e) {
			initialized = false;
			// TODO: fix it.
		}

		cardLayout.first(cardLayoutContainer);
		if (cardLayoutContainer.getComponentCount() > 1) {
			btnPrev.setVisible(true);
			btnNext.setVisible(true);
		}
		else {
			btnPrev.setVisible(false);
			btnNext.setVisible(false);
		}

		revalidate();
		repaint();
	}

	protected LayoutManager createButtonPanelLayout() {
		return new FlowLayout(FlowLayout.CENTER);
	}

	public ButtonPanel getActivePanel() {
		Component[] components = cardLayoutContainer.getComponents();
		for (Component component : components) {
			if (component instanceof ButtonPanel && component.isVisible()) {
				return (ButtonPanel) component;
			}
		}

		return null;
	}

	public void addButton(AbstractButton button) {
		button.setPreferredSize(buttonSize);
		button.setText("<html><body><center>" + button.getText() + "</center></body></html>"); //$NON-NLS-1$ //$NON-NLS-2$
		cardLayoutContainer.add(button);
	}

	public void addSeparator(String text) {
		cardLayoutContainer.add(new JXTitledSeparator(text, JLabel.CENTER), "alignx 50%, newline, span, growx, height 30!"); //$NON-NLS-1$
	}

	private void scrollDown() {
		cardLayout.next(cardLayoutContainer);
	}

	private void scrollUp() {
		cardLayout.previous(cardLayoutContainer);
	}

	private class ScrollAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if (source == btnPrev) {
				scrollUp();
			}
			else if (source == btnNext) {
				scrollDown();
			}
		}

	}

	public JPanel getButtonsPanel() {
		return cardLayoutContainer;
	}

	public AbstractButton getFirstItemButton() {
		int componentCount = cardLayoutContainer.getComponentCount();
		if (componentCount == 0) {
			return null;
		}

		ButtonPanel buttonPanel = (ButtonPanel) cardLayoutContainer.getComponent(0);
		if (buttonPanel.getComponentCount() == 0) {
			return null;
		}

		return (AbstractButton) buttonPanel.getComponent(0);
	}

	protected int getButtonCount(int containerSize, int itemSize) {
		int buttonCount = containerSize / (itemSize + 5);
		//buttonCount = (containerSize - ((containerSize / itemSize) * 5)) / itemSize;
		return buttonCount;
	}

	public void componentResized(ComponentEvent e) {
		int totalItem = getFitableButtonCount();
		if (totalItem == cardLayoutContainer.getComponentCount()) {
			return;
		}

		renderItems();
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentShown(ComponentEvent e) {
	}

	public void componentHidden(ComponentEvent e) {
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	private class ButtonPanel extends JPanel {

		public ButtonPanel(String name) {
			setName(name);
			setBorder(null);
		}
	}
}
