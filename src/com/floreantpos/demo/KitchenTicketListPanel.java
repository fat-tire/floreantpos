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
package com.floreantpos.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.POSConstants;
import com.floreantpos.model.KitchenTicket;
import com.floreantpos.swing.PosButton;

public class KitchenTicketListPanel extends JPanel {
	protected static int previousBlockIndex = -1;
	protected static int currentBlockIndex = 0;
	protected static int nextBlockIndex;

	static int horizontalPanelCount = 4;

	protected PosButton btnNext;
	protected PosButton btnPrev;

	public Set<KitchenTicket> existingTickets = new HashSet<KitchenTicket>();

	public KitchenTicketListPanel() {
		super(new MigLayout("filly, wrap " + horizontalPanelCount, "sg, fill", "")); //$NON-NLS-1$
	}

	public boolean addTicket(KitchenTicket ticket) {
		if (existingTickets.contains(ticket)) {
			return false;
		}

		Dimension size = getSize();
		Dimension panelSize = new Dimension();
		if (size.width == 0 && size.height == 0) {
			size = Toolkit.getDefaultToolkit().getScreenSize();
		}
		panelSize.width = 330;
		panelSize.height = 280;

		int horizontalPanelCount = getCount(size.width, panelSize.width);
		int verticalPanelCount = getCount(size.height, panelSize.height);

		int totalItem = horizontalPanelCount * verticalPanelCount;

		previousBlockIndex = currentBlockIndex - totalItem;
		nextBlockIndex = currentBlockIndex + totalItem;

		existingTickets.add(ticket);

		updateButton();

		if (nextBlockIndex < existingTickets.size()) {
			return false;
		}

		super.add(new KitchenTicketView(ticket), "growy"); //$NON-NLS-1$ 

		return true;
	}

	protected void updateView() {
		Dimension size = getSize();
		Dimension panelSize = new Dimension();
		if (size.width == 0 && size.height == 0) {
			size = Toolkit.getDefaultToolkit().getScreenSize();
		}
		panelSize.width = 330;
		panelSize.height = 280;

		int horizontalPanelCount = getCount(size.width, panelSize.width);
		int verticalPanelCount = getCount(size.height, panelSize.height);

		int totalItem = horizontalPanelCount * verticalPanelCount;

		previousBlockIndex = currentBlockIndex - totalItem;
		nextBlockIndex = currentBlockIndex + totalItem;

		for (int i = currentBlockIndex; i < nextBlockIndex; i++) {
			if (i == existingTickets.size()) {
				break;
			}
			KitchenTicket item = (KitchenTicket) existingTickets.toArray()[i];
			super.add(new KitchenTicketView(item), "growy"); //$NON-NLS-1$ 

			if (i == existingTickets.size() - 1) {
				break;
			}
		}
		updateButton();
	}

	private void updateButton() {
		if (previousBlockIndex >= 0 && currentBlockIndex != 0) {
			btnPrev.setEnabled(true);
		}
		else {
			btnPrev.setEnabled(false);
		}

		if (nextBlockIndex < existingTickets.size()) {
			btnNext.setEnabled(true);
		}
		else {
			btnNext.setEnabled(false);
		}
	}

	public JPanel getPaginationPanel() {
		JPanel southPanel = new JPanel(new BorderLayout(5, 5));
		southPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

		btnPrev = new PosButton();
		btnPrev.setText(POSConstants.CAPITAL_PREV);
		btnPrev.setPreferredSize(new Dimension(100, 30));
		southPanel.add(btnPrev, BorderLayout.WEST);

		btnNext = new PosButton();
		btnNext.setPreferredSize(new Dimension(100, 30));
		btnNext.setText(POSConstants.CAPITAL_NEXT);
		southPanel.add(btnNext, BorderLayout.EAST);

		ScrollAction action = new ScrollAction();
		btnPrev.addActionListener(action);
		btnNext.addActionListener(action);

		return southPanel;
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

	private void scrollDown() {
		currentBlockIndex = nextBlockIndex;
		super.removeAll();
		updateView();
		this.repaint();
	}

	private void scrollUp() {
		currentBlockIndex = previousBlockIndex;
		this.removeAll();
		updateView();
		this.repaint();
	}

	protected int getCount(int containerSize, int itemSize) {
		int panelCount = containerSize / itemSize;
		return panelCount;
	}

	@Override
	public void remove(Component comp) {
		if (comp instanceof KitchenTicketView) {
			KitchenTicketView view = (KitchenTicketView) comp;
			existingTickets.remove(view.getTicket());
		}

		super.remove(comp);
	}

	@Override
	public void removeAll() {
		existingTickets.clear();

		Component[] components = getComponents();
		for (Component component : components) {
			if (component instanceof KitchenTicketView) {
				KitchenTicketView kitchenTicketView = (KitchenTicketView) component;
				kitchenTicketView.stopTimer();
			}
		}

		super.removeAll();
	}
}
