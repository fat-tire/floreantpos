package com.floreantpos.actions;

import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXDatePicker;

import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.model.TipsCashoutReport;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.dao.GratuityDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.swing.ListComboBoxModel;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.dialog.TipsCashoutReportDialog;
import com.floreantpos.ui.util.UiUtil;

public class ServerTipsAction extends PosAction {

	public ServerTipsAction() {
		super(POSConstants.SERVER_TIPS, UserPermission.DRAWER_PULL); //$NON-NLS-1$
	}

	@Override
	public void execute() {
		try {
			JPanel panel = new JPanel(new MigLayout());
			List<User> users = UserDAO.getInstance().findAll();

			JXDatePicker fromDatePicker = UiUtil.getCurrentMonthStart();
			JXDatePicker toDatePicker = UiUtil.getCurrentMonthEnd();

			panel.add(new JLabel(com.floreantpos.POSConstants.SELECT_USER + ":"), "grow");
			JComboBox userCombo = new JComboBox(new ListComboBoxModel(users));
			panel.add(userCombo, "grow, wrap");
			panel.add(new JLabel(com.floreantpos.POSConstants.FROM + ":"), "grow");
			panel.add(fromDatePicker, "wrap");
			panel.add(new JLabel(com.floreantpos.POSConstants.TO_), "grow");
			panel.add(toDatePicker);

			int option = JOptionPane.showOptionDialog(Application.getPosWindow(), panel, com.floreantpos.POSConstants.SELECT_CRIETERIA, JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, null, null);
			if (option != JOptionPane.OK_OPTION) {
				return;
			}

			GratuityDAO gratuityDAO = new GratuityDAO();
			TipsCashoutReport report = gratuityDAO.createReport(fromDatePicker.getDate(), toDatePicker.getDate(), (User) userCombo.getSelectedItem());

			TipsCashoutReportDialog dialog = new TipsCashoutReportDialog(report);
			dialog.setSize(400, 600);
			dialog.open();
		} catch (Exception e) {
			POSMessageDialog.showError(e.getMessage(), e);
		}
	}

}
