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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.model.KitchenTicket;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosUIManager;

public class KitchenTicketListPanel extends JPanel {
	private static int previousBlockIndex = -1;
	private static int currentBlockIndex = 0;
	private static int nextBlockIndex;

	private static int horizontalPanelCount;

	private PosButton btnNext = new PosButton();;
	private PosButton btnPrev = new PosButton();;

	private Set<KitchenTicket> existingTickets = new HashSet<KitchenTicket>();

	public KitchenTicketListPanel() {
		updatePanelCount();
		super.setLayout(new MigLayout("filly, wrap " + horizontalPanelCount, "sg, fill", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	public boolean addTicket(KitchenTicket ticket) {
		if (existingTickets.contains(ticket)) {
			return false;
		}

		existingTickets.add(ticket);
		updateButton();

		if (nextBlockIndex < existingTickets.size()) {
			return false;
		}
		super.add(new KitchenTicketView(ticket), "growy"); //$NON-NLS-1$ 

		return true;
	}

	protected void rendererKitchenTickets() {
		updatePanelCount();
		for (int i = currentBlockIndex; i < nextBlockIndex; i++) {
			if (i == existingTickets.size()) {
				break;
			}
			KitchenTicket item = (KitchenTicket) existingTickets.toArray()[i];
			super.add(new KitchenTicketView(item), "growy"); //$NON-NLS-1$ 
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

		btnPrev.setText(POSConstants.CAPITAL_PREV);
		btnPrev.setPreferredSize(PosUIManager.getSize(80, 30));
		southPanel.add(btnPrev, BorderLayout.WEST);

		btnNext.setPreferredSize(PosUIManager.getSize(80, 30));
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
		rendererKitchenTickets();
		repaint();
	}

	private void scrollUp() {
		currentBlockIndex = previousBlockIndex;
		super.removeAll();
		rendererKitchenTickets();
		repaint();
	}

	protected int getCount(int containerSize, int itemSize) {
		int panelCount = containerSize / itemSize;
		return panelCount;
	}

	private void updatePanelCount() {
		Dimension size = Application.getInstance().getRootView().getSize();

		horizontalPanelCount = getCount(size.width, 330);
		int verticalPanelCount = getCount(size.height, 280);

		int totalItem = horizontalPanelCount * verticalPanelCount;

		previousBlockIndex = currentBlockIndex - totalItem;
		nextBlockIndex = currentBlockIndex + totalItem;
	}

	@Override
	public void remove(Component comp) {
		if (comp instanceof KitchenTicketView) {
			KitchenTicketView view = (KitchenTicketView) comp;
			existingTickets.remove(view.getTicket());
		}
		super.remove(comp);
		super.removeAll();
		rendererKitchenTickets();
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
