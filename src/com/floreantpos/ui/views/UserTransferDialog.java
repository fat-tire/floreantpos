package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.floreantpos.Messages;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosScrollPane;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class UserTransferDialog extends POSDialog {

	private OrderInfoView view;
	private JList list;
	private TitlePanel titlePanel;
	private static Log logger = LogFactory.getLog(UserTransferDialog.class);

	public UserTransferDialog(OrderInfoView view) {
		this.view = view;
		createUI();
	}

	private void createUI() {
		setTitle(Messages.getString("UserTransferDialog.0")); //$NON-NLS-1$
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		titlePanel = new TitlePanel();
		titlePanel.setTitle(Messages.getString("UserTransferDialog.1")); //$NON-NLS-1$
		panel.add(titlePanel);
		add(panel, BorderLayout.NORTH);

		List<User> users = UserDAO.getInstance().findAll();

		DefaultListModel model = new DefaultListModel();
		list = new JList(model);
		list.setFixedCellHeight(PosUIManager.getSize(60));

		for (Iterator iter = users.iterator(); iter.hasNext();) {
			User user = (User) iter.next();
			model.addElement(user);
		}

		PosScrollPane scrollPane = new PosScrollPane(list);
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(scrollPane);

		JPanel footerPanel = new JPanel(new BorderLayout());
		footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		footerPanel.add(new JSeparator(), BorderLayout.NORTH);
		JPanel buttonPanel = new JPanel(new MigLayout("fill")); //$NON-NLS-1$
		footerPanel.add(buttonPanel);

		getContentPane().add(footerPanel, BorderLayout.SOUTH);
		PosButton btnOk = new PosButton();

		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				User selectedUser = (User) list.getSelectedValue();
				if (selectedUser == null) {
					POSMessageDialog.showError(UserTransferDialog.this, "Please select user.");
					return;
				}
				if (!selectedUser.isClockedIn()) {
					POSMessageDialog.showError(UserTransferDialog.this, "Selected user is not clocked in.");
					return;
				}
				List<Ticket> tickets = view.getTickets();
				for (Iterator iter = tickets.iterator(); iter.hasNext();) {
					Ticket ticket = (Ticket) iter.next();
					ticket.setOwner(selectedUser);
					TicketDAO.getInstance().saveOrUpdate(ticket);
				}

				try {
					view.getReportPanel().removeAll();
					view.createReport();
					view.revalidate();
					view.repaint();
					dispose();
					POSMessageDialog.showMessage(Messages.getString("UserTransferDialog.3")); //$NON-NLS-1$
				} catch (Exception e1) {
					POSMessageDialog.showError(Messages.getString("UserTransferDialog.4")); //$NON-NLS-1$
					logger.error(e1);
				}

			}

		});

		btnOk.setText(Messages.getString("UserTransferDialog.5")); //$NON-NLS-1$
		buttonPanel.add(btnOk, "split 2, align center"); //$NON-NLS-1$

		PosButton btnCancel = new PosButton();
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setText(Messages.getString("UserTransferDialog.7")); //$NON-NLS-1$
		buttonPanel.add(btnCancel);

	}
}
