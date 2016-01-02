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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXTitledSeparator;

import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.swing.PosButton;

public abstract class SelectionView extends JPanel implements ComponentListener {
	private final static int HORIZONTAL_GAP = 15;
	private final static int VERTICAL_GAP = 15;

	private Dimension buttonSize;

	protected final JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

	protected List items;
	
	protected int previousBlockIndex = -1;
	protected int currentBlockIndex = 0;
	protected int nextBlockIndex;
	
	protected TitledBorder border;
	
	protected JPanel actionButtonPanel = new JPanel(new MigLayout("fill,hidemode 3, ins 2", "sg, fill", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	protected com.floreantpos.swing.PosButton btnBack;
	protected com.floreantpos.swing.PosButton btnNext;
	protected com.floreantpos.swing.PosButton btnPrev;
	
	@SuppressWarnings("unused")
	private String title;

	public SelectionView(String title, int buttonWidth, int buttonHeight) {
		this.title = title;
		this.buttonSize = new Dimension(buttonWidth, buttonHeight);

		border = new TitledBorder(title);
		border.setTitleJustification(TitledBorder.CENTER);

		setBorder(border);

		setLayout(new BorderLayout(HORIZONTAL_GAP, VERTICAL_GAP));

		add(buttonsPanel);

		btnBack = new PosButton();
		btnBack.setText(POSConstants.CAPITAL_BACK);
		actionButtonPanel.add(btnBack, "grow,shrink, align center"); //$NON-NLS-1$

		btnPrev = new PosButton();
		btnPrev.setText(POSConstants.CAPITAL_PREV);
		actionButtonPanel.add(btnPrev, "grow, align center"); //$NON-NLS-1$

		btnNext = new PosButton();
		btnNext.setText(POSConstants.CAPITAL_NEXT);
		actionButtonPanel.add(btnNext, "grow, align center"); //$NON-NLS-1$

		add(actionButtonPanel, BorderLayout.SOUTH);

		ScrollAction action = new ScrollAction();
		btnBack.addActionListener(action);
		btnPrev.addActionListener(action);
		btnNext.addActionListener(action);

		addComponentListener(this);
	}

	public SelectionView(String title) {
		this(title, 120, TerminalConfig.getMenuItemButtonHeight());
	}
	
	public void setTitle(String title) {
		border.setTitle(title);
	}

	public void setItems(List items) {
		this.items = items;
		currentBlockIndex = 0;
		nextBlockIndex = 0;
		
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
		//btnBack.setEnabled(false);
		btnNext.setEnabled(false);
		btnPrev.setEnabled(false);
		
		Component[] components = buttonsPanel.getComponents();
		for (int i = 0; i < components.length; i++) {
			Component c = components[i];
			if (c instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) c;
				button.setPreferredSize(null);

				ActionListener[] actionListeners = button.getActionListeners();
				if (actionListeners != null) {
					for (int j = 0; j < actionListeners.length; j++) {
						button.removeActionListener(actionListeners[j]);
					}
				}
			}
		}
		buttonsPanel.removeAll();
		buttonsPanel.revalidate();
		buttonsPanel.repaint();
	}
	
	protected int getFitableButtonCount() {
		Dimension size = buttonsPanel.getSize();
		Dimension itemButtonSize = getButtonSize();

		int horizontalButtonCount = getButtonCount(size.width, itemButtonSize.width);
		int verticalButtonCount = getButtonCount(size.height, itemButtonSize.height);
		
		int totalItem = horizontalButtonCount * verticalButtonCount;
		
		return totalItem;
	}

	protected void renderItems() {
		reset();

		if (this.items == null || items.size() == 0) {
			return;
		}

		Dimension itemButtonSize = getButtonSize();

		//buttonsPanel.setLayout(new MigLayout("alignx 50%, wrap " + horizontalButtonCount)); //$NON-NLS-1$
		
		int totalItem = getFitableButtonCount();
		
		previousBlockIndex = currentBlockIndex - totalItem;
		nextBlockIndex = currentBlockIndex + totalItem;
		
		try {
			for (int i = currentBlockIndex; i < nextBlockIndex; i++) {

				Object item = items.get(i);

				AbstractButton itemButton = createItemButton(item);
				if(itemButton == null) {
					continue;
				}
				itemButton.setPreferredSize(itemButtonSize);
				buttonsPanel.add(itemButton);//, "width " + itemButtonSize.width + "!, height " + itemButtonSize.height + "!"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

				if (i == items.size() - 1) {
					break;
				}
			}
		} catch (Exception e) {
			// TODO: fix it.
		}
		
		if(previousBlockIndex >= 0 && currentBlockIndex != 0) {
			btnPrev.setEnabled(true);
		}
		
		if(nextBlockIndex < items.size()) {
			btnNext.setEnabled(true);
		}
		
		revalidate();
		repaint();
	}
	
	public void addButton(AbstractButton button) {
		button.setPreferredSize(buttonSize);
		button.setText("<html><body><center>" + button.getText() + "</center></body></html>"); //$NON-NLS-1$ //$NON-NLS-2$
		buttonsPanel.add(button);
	}

	public void addSeparator(String text) {
		buttonsPanel.add(new JXTitledSeparator(text, JLabel.CENTER), "alignx 50%, newline, span, growx, height 30!"); //$NON-NLS-1$
	}

	private void scrollDown() {
		currentBlockIndex = nextBlockIndex;
		renderItems();
	}

	private void scrollUp() {
		currentBlockIndex = previousBlockIndex;
		renderItems();
	}

	public void setBackEnable(boolean enable) {
		btnBack.setEnabled(enable);
	}

	public void setBackVisible(boolean enable) {
		btnBack.setVisible(enable);
	}

	public abstract void doGoBack();

	private class ScrollAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if (source == btnBack) {
				doGoBack();
			}
			else if (source == btnPrev) {
				scrollUp();
			}
			else if (source == btnNext) {
				scrollDown();
			}
		}

	}

	public JPanel getButtonsPanel() {
		return buttonsPanel;
	}

	protected int getButtonCount(int containerSize, int itemSize) {
		int buttonCount = containerSize / (itemSize + 5);
		//buttonCount = (containerSize - ((containerSize / itemSize) * 5)) / itemSize;
		return buttonCount;
	}

	public void componentResized(ComponentEvent e) {
		int totalItem = getFitableButtonCount();
		if(totalItem == buttonsPanel.getComponentCount()) {
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
}
