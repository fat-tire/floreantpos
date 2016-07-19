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
package com.floreantpos.ui.dialog;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.model.ShopTable;
import com.floreantpos.model.dao.ShopTableDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;

public class SeatSelectionDialog extends POSDialog implements ActionListener {
	private TitlePanel titlePanel;
	private Integer selectedSeat;

	private List<Integer> seats = new ArrayList<>();
	private List<Integer> orderSeats;

	public SeatSelectionDialog(List<Integer> tableNumbers, List<Integer> orderSeats) {
		List<ShopTable> tables = ShopTableDAO.getInstance().getByNumbers(tableNumbers);
		int seatNumber = 1;
		for (ShopTable shopTable : tables) {
			for (int i = 0; i < shopTable.getCapacity(); i++) {
				seats.add(seatNumber);
				seatNumber++;
			}
		}
		this.orderSeats = orderSeats;
		if (orderSeats != null) {
			for (Integer i = 0; i < orderSeats.size(); i++) {
				int orderSeatNumber = orderSeats.get(i);
				if (orderSeatNumber == 0)
					continue;
				if (!seats.contains(orderSeatNumber))
					seats.add(orderSeatNumber);
			}
		}
		init();
	}

	private void init() {
		setResizable(false);

		Container contentPane = getContentPane();

		MigLayout layout = new MigLayout("fillx,wrap 3", "[60px,fill][60px,fill][60px,fill]", "[][][][][]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		contentPane.setLayout(layout);

		titlePanel = new TitlePanel();
		contentPane.add(titlePanel, "spanx ,growy,height 60,wrap"); //$NON-NLS-1$

		for (Integer seat : seats) {
			PosButton posButton = new PosButton();
			posButton.setFocusable(false);
			posButton.setFont(posButton.getFont().deriveFont(Font.BOLD, 24));
			posButton.setText(String.valueOf(seat));
			if (orderSeats != null && orderSeats.contains(seat)) {
				posButton.setBackground(Color.GREEN);
			}
			posButton.addActionListener(this);
			String constraints = "grow, height 55"; //$NON-NLS-1$
			contentPane.add(posButton, constraints);
		}
		PosButton btnShared = new PosButton("Shared");
		btnShared.setFocusable(false);
		if (orderSeats != null && orderSeats.contains(0)) {
			btnShared.setBackground(Color.GREEN);
		}
		btnShared.addActionListener(this);
		contentPane.add(btnShared, "grow, height 55"); //$NON-NLS-1$

		PosButton btnCustom = new PosButton("Custom");
		btnCustom.setFocusable(false);
		btnCustom.addActionListener(this);
		contentPane.add(btnCustom, "grow, height 55"); //$NON-NLS-1$

		PosButton btnCancel = new PosButton("Cancel");
		btnCancel.setFocusable(false);
		btnCancel.addActionListener(this);
		contentPane.add(btnCancel, "newline, span,grow, height 55"); //$NON-NLS-1$
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();

		boolean canceled = false;
		if ("Shared".equalsIgnoreCase(actionCommand)) {
			selectedSeat = 0;
		}
		else if ("Custom".equalsIgnoreCase(actionCommand)) {
			selectedSeat = -1;
		}
		else if ("Cancel".equalsIgnoreCase(actionCommand)) {
			canceled = true;
		}
		else {
			selectedSeat = Integer.valueOf(actionCommand);
		}
		setCanceled(canceled);
		dispose();
	}

	public Integer getSeatNumber() {
		return selectedSeat;
	}

	public void setTitle(String title) {
		titlePanel.setTitle(title);

		super.setTitle(title);
	}

	public void setDialogTitle(String title) {
		super.setTitle(title);
	}
}
