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
package com.floreantpos.actions;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.swing.Action;
import javax.swing.JOptionPane;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.Messages;
import com.floreantpos.PosException;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.CashDrawer;
import com.floreantpos.model.Currency;
import com.floreantpos.model.CurrencyBalance;
import com.floreantpos.model.DrawerAssignedHistory;
import com.floreantpos.model.DrawerPullReport;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.dao.CashDrawerDAO;
import com.floreantpos.model.dao.TerminalDAO;
import com.floreantpos.print.DrawerpullReportService;
import com.floreantpos.print.PosPrintService;
import com.floreantpos.swing.UserListDialog;
import com.floreantpos.ui.dialog.MultiCurrencyAssignDrawerDialog;
import com.floreantpos.ui.dialog.NumberSelectionDialog2;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.CurrencyUtil;
import com.floreantpos.util.POSUtil;

public class DrawerAssignmentAction extends PosAction {

	public DrawerAssignmentAction() {
		super(Messages.getString("DrawerAssignmentAction.0"), UserPermission.DRAWER_ASSIGNMENT); //$NON-NLS-1$
		updateActionText();
	}

	public void updateActionText() {
		Terminal terminal = Application.getInstance().getTerminal();
		User assignedUser = terminal.getAssignedUser();

		if (assignedUser != null) {
			putValue(Action.NAME, Messages.getString("DrawerAssignmentAction.1")); //$NON-NLS-1$
		}
		else {
			putValue(Action.NAME, Messages.getString("DrawerAssignmentAction.2")); //$NON-NLS-1$
		}
	}

	@Override
	public void execute() {
		try {
			Terminal terminal = Application.getInstance().getTerminal();
			User assignedUser = terminal.getAssignedUser();

			if (assignedUser != null) {
				int option = POSMessageDialog.showYesNoQuestionDialog(Application.getPosWindow(),
						Messages.getString("DrawerAssignmentAction.3") + assignedUser.getFullName() //$NON-NLS-1$
								+ Messages.getString("DrawerAssignmentAction.4"), //$NON-NLS-1$
						Messages.getString("DrawerAssignmentAction.5")); //$NON-NLS-1$
				if (option != JOptionPane.YES_OPTION) {
					return;
				}

				performDrawerClose(terminal);
			}
			else {
				performAssignment(terminal);
			}

		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(), e.getMessage(), e);
		}
	}

	private void performAssignment(Terminal terminal) throws Exception {
		Session session = null;
		Transaction tx = null;

		try {
			UserListDialog dialog = new UserListDialog();
			dialog.pack();
			dialog.open();

			if (dialog.isCanceled()) {
				return;
			}

			User user = dialog.getSelectedUser();
			if (!user.isClockedIn()) {
				POSMessageDialog.showError("Can't assign drawer. Selected user is not clocked in.");
				return;
			}

			double drawerBalance = 0;
			CashDrawer cashDrawer = null;
			if (TerminalConfig.isEnabledMultiCurrency()) {
				List<Currency> currencyList = CurrencyUtil.getAllCurrency();

				if (currencyList.size() > 1) {
					MultiCurrencyAssignDrawerDialog multiCurrencyDialog = new MultiCurrencyAssignDrawerDialog(500, currencyList);
					multiCurrencyDialog.pack();
					multiCurrencyDialog.open();

					if (multiCurrencyDialog.isCanceled()) {
						return;
					}
					cashDrawer = multiCurrencyDialog.getCashDrawer();
					drawerBalance = multiCurrencyDialog.getTotalAmount();
				}
			}
			else {
				drawerBalance = NumberSelectionDialog2.takeDoubleInput(Messages.getString("DrawerAssignmentAction.6"), //$NON-NLS-1$
						Messages.getString("DrawerAssignmentAction.7"), 500); //$NON-NLS-1$
			}
			if (Double.isNaN(drawerBalance)) {
				return;
			}

			terminal.setAssignedUser(user);
			terminal.setCurrentBalance(drawerBalance);

			DrawerAssignedHistory history = new DrawerAssignedHistory();
			history.setTime(new Date());
			history.setOperation(DrawerAssignedHistory.ASSIGNMENT_OPERATION);
			history.setUser(user);

			session = TerminalDAO.getInstance().createNewSession();
			tx = session.beginTransaction();

			session.saveOrUpdate(terminal);
			session.save(history);

			if (cashDrawer != null) {
				session.saveOrUpdate(cashDrawer);
			}

			tx.commit();

			POSMessageDialog.showMessage(Messages.getString("DrawerAssignmentAction.8") + user.getFullName()); //$NON-NLS-1$

			putValue(Action.NAME, Messages.getString("DrawerAssignmentAction.9")); //$NON-NLS-1$

		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}

			throw e;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	private void performDrawerClose(Terminal terminal) throws Exception {
		try {
			User user = terminal.getAssignedUser();

			DrawerPullReport report = DrawerpullReportService.buildDrawerPullReport();
			report.setAssignedUser(user);

			TerminalDAO dao = new TerminalDAO();
			dao.resetCashDrawer(report, terminal, user, 0);

			if (TerminalConfig.isEnabledMultiCurrency()) {
				CashDrawer cashDrawer = CashDrawerDAO.getInstance().findByTerminal(terminal);
				if (cashDrawer != null) {
					Set<CurrencyBalance> currencyBalances = cashDrawer.getCurrencyBalanceList();

					if (currencyBalances != null) {
						for (CurrencyBalance currencyBalance : currencyBalances) {
							currencyBalance.setBalance(0.0);
						}
					}

					CashDrawerDAO.getInstance().saveOrUpdate(cashDrawer);
				}
			}

			try {
				PosPrintService.printDrawerPullReport(report, terminal);
			} catch (PosException exception) {
				POSMessageDialog.showError(POSUtil.getFocusedWindow(), exception.getMessage());
			}

			//			DrawerAssignedHistory history = new DrawerAssignedHistory();
			//			history.setTime(new Date());
			//			history.setOperation(DrawerAssignedHistory.DEASSIGNMENT_OPERATION);
			//			history.setUser(user);
			//
			//			session = TerminalDAO.getInstance().createNewSession();
			//			tx = session.beginTransaction();
			//
			//			session.saveOrUpdate(terminal);
			//			session.save(history);
			//
			//			tx.commit();

			POSMessageDialog.showMessage(Messages.getString("DrawerAssignmentAction.10")); //$NON-NLS-1$

			putValue(Action.NAME, Messages.getString("DrawerAssignmentAction.11")); //$NON-NLS-1$

		} catch (Exception e) {
			throw e;
		}
	}
}
