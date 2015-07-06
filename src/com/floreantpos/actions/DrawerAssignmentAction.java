package com.floreantpos.actions;

import java.util.Date;

import javax.swing.Action;
import javax.swing.JOptionPane;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.main.Application;
import com.floreantpos.model.DrawerAssignedHistory;
import com.floreantpos.model.DrawerPullReport;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.dao.TerminalDAO;
import com.floreantpos.print.DrawerpullReportService;
import com.floreantpos.print.PosPrintService;
import com.floreantpos.swing.UserListDialog;
import com.floreantpos.ui.dialog.NumberSelectionDialog2;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class DrawerAssignmentAction extends PosAction {

	public DrawerAssignmentAction() {
		super("ASSIGN DRAWER", UserPermission.DRAWER_ASSIGNMENT); //$NON-NLS-1$
		Terminal terminal = Application.getInstance().getTerminal();
		User assignedUser = terminal.getAssignedUser();
		
		if (assignedUser != null) {
			putValue(Action.NAME, "DRAWER CLOSE");
		}
		else {
			putValue(Action.NAME, "ASSIGN DRAWER");
		}
	}

	@Override
	public void execute() {
		try {
			Terminal terminal = Application.getInstance().getTerminal();
			User assignedUser = terminal.getAssignedUser();
			
			if (assignedUser != null) {
				int option = POSMessageDialog.showYesNoQuestionDialog(Application.getPosWindow(), "Drawer is assigned to " + assignedUser.getFullName()
						+ ". Do you want to close?", "Confirm");
				if (option != JOptionPane.YES_OPTION) {
					return;
				}

				performDrawerClose(terminal);
			}
			else {
				performAssignment(terminal);
			}

		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(),e.getMessage(), e);
		}
	}

	private void performAssignment(Terminal terminal) throws Exception {
		Session session = null;
		Transaction tx = null;

		try {
			UserListDialog dialog = new UserListDialog();
			dialog.pack();
			dialog.open();

			if (dialog.isCanceled())
				return;

			User user = dialog.getSelectedUser();
			
			double drawerBalance = NumberSelectionDialog2.takeDoubleInput("Please enter drawer initial balance", "Please enter drawer initial balance", terminal.getOpeningBalance());
	    	if(Double.isNaN(drawerBalance)) {
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

			tx.commit();

			POSMessageDialog.showMessage("Drawer is assigned to " + user.getFullName());

			putValue(Action.NAME, "CLOSE DRAWER");
			
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
			
			PosPrintService.printDrawerPullReport(report, terminal);
			
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

			POSMessageDialog.showMessage("Drawer is closed.");

			putValue(Action.NAME, "ASSIGN DRAWER");
			
		} catch (Exception e) {
			throw e;
		} 
	}
}
