package com.floreantpos.ui.views.order;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.floreantpos.Messages;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.ui.dialog.ItemNumberSelectionDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class ItemSearchPanel extends JPanel {
	private JButton btnSearchButton;
	private JTextField txtSearchItem;

	public ItemSearchPanel() {

		setLayout(new BorderLayout(5, 5));

		txtSearchItem = new JTextField();
		btnSearchButton = new JButton("...");
		btnSearchButton.setPreferredSize(new Dimension(60, 40));

		add(txtSearchItem);
		add(btnSearchButton, BorderLayout.EAST);

		txtSearchItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (txtSearchItem.getText().equals("")) {
					POSMessageDialog.showMessage("Please enter item number ");
					return;
				}

				if (TerminalConfig.isShowItemByBarcode()) {
					setMenuItemByBarcode(txtSearchItem.getText());
				}
				else {
					setMenuItemByItemId(txtSearchItem.getText());
				}
				txtSearchItem.setText("");
			}
		});

		btnSearchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				//	ItemNumberSelectionDialog.takeIntInput("Search item by Barcode and Item No.");
				ItemNumberSelectionDialog dialog = new ItemNumberSelectionDialog(null);
				dialog.setTitle("Search item by Barcode and Item No.");
				dialog.pack();
				dialog.open();

				if (dialog.isBarcodeSelected()) {

					setMenuItemByBarcode(String.valueOf(dialog.getIntValue()));
				}
				else if (dialog.isItemSelected()) {
					setMenuItemByItemId(String.valueOf(dialog.getIntValue()));
				}
			}
		});
	}

	public void setMenuItemByItemId(String id) {

		Integer itemId = Integer.parseInt(id);

		MenuItem menuItem = MenuItemDAO.getInstance().get(itemId);
		if (menuItem == null) {
			POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("TicketView.45")); //$NON-NLS-1$
			return;
		}
		MenuItemDAO dao = new MenuItemDAO();
		menuItem = dao.initialize(menuItem);

		TicketItem ticketItem = menuItem.convertToTicketItem();
		OrderView.getInstance().getTicketView().addTicketItem(ticketItem);
	}

	public void setMenuItemByBarcode(String barcode) {

		MenuItemDAO dao = new MenuItemDAO();
		//MenuItem menuItem=dao.getMenuItemByBarcode("12345");

		MenuItem menuItem = dao.getMenuItemByBarcode(barcode);
		if (menuItem == null) {
			POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("TicketView.45")); //$NON-NLS-1$
			return;
		}
		menuItem = dao.initialize(menuItem);

		TicketItem ticketItem = menuItem.convertToTicketItem();
		OrderView.getInstance().getTicketView().addTicketItem(ticketItem);

	}

}
