package com.floreantpos.actions;

import java.util.Date;

import javax.swing.Action;
import javax.swing.JOptionPane;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.Messages;
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
		super(Messages.getString("DrawerAssignmentAction.0"), UserPermission.DRAWER_ASSIGNMENT); //$NON-NLS-1$
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
				int option = POSMessageDialog.showYesNoQuestionDialog(Application.getPosWindow(), Messages.getString("DrawerAssignmentAction.3") + assignedUser.getFullName() //$NON-NLS-1$
						+ Messages.getString("DrawerAssignmentAction.4"), Messages.getString("DrawerAssignmentAction.5")); //$NON-NLS-1$ //$NON-NLS-2$
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
			
			double drawerBalance = NumberSelectionDialog2.takeDoubleInput(Messages.getString("DrawerAssignmentAction.6"), Messages.getString("DrawerAssignmentAction.7"), terminal.getOpeningBalance()); //$NON-NLS-1$ //$NON-NLS-2$
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

			POSMessageDialog.showMessage(Messages.getString("DrawerAssignmentAction.10")); //$NON-NLS-1$

			putValue(Action.NAME, Messages.getString("DrawerAssignmentAction.11")); //$NON-NLS-1$
			
		} catch (Exception e) {
			throw e;
		} 
	}
}
