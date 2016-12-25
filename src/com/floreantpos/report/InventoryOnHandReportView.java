
package com.floreantpos.report;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JRViewer;

import org.jdesktop.swingx.JXDatePicker;

import com.floreantpos.PosLog;
import com.floreantpos.main.Application;
import com.floreantpos.model.InventoryItem;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.dao.InventoryItemDAO;
import com.floreantpos.model.dao.TerminalDAO;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.util.UiUtil;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

public class InventoryOnHandReportView extends TransparentPanel {
	private JButton btnGo;
	//private JComboBox cbTerminal;
	private JXDatePicker fromDatePicker;
	private JXDatePicker toDatePicker;
	private JPanel reportPanel;
	private JPanel contentPane;

	//private JComboBox cbUserType;

	public InventoryOnHandReportView() {
		//cbUserType.setModel(new DefaultComboBoxModel(new String[]{com.floreantpos.POSConstants.ALL, com.floreantpos.POSConstants.SERVER, com.floreantpos.POSConstants.CASHIER, com.floreantpos.POSConstants.MANAGER}));

		TerminalDAO terminalDAO = new TerminalDAO();
		List terminals = terminalDAO.findAll();
		terminals.add(0, com.floreantpos.POSConstants.ALL);
		// cbTerminal.setModel(new ListComboBoxModel(terminals));

		setLayout(new BorderLayout());
		add(contentPane);

		btnGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InventoryItemDAO dao = new InventoryItemDAO();
				List<InventoryItem> findPayroll = dao.findAll();
				viewReport(findPayroll);
			}
		});
	}

	public InventoryOnHandReportView(final List<InventoryItem> inventoryList) {
		//cbUserType.setModel(new DefaultComboBoxModel(new String[]{com.floreantpos.POSConstants.ALL, com.floreantpos.POSConstants.SERVER, com.floreantpos.POSConstants.CASHIER, com.floreantpos.POSConstants.MANAGER}));

		TerminalDAO terminalDAO = new TerminalDAO();
		List terminals = terminalDAO.findAll();
		terminals.add(0, com.floreantpos.POSConstants.ALL);
		// cbTerminal.setModel(new ListComboBoxModel(terminals));

		setLayout(new BorderLayout());
		add(contentPane);
		viewReport(inventoryList);

		/*btnGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});*/
	}

	private void viewReport(List<InventoryItem> inventList) {
		Date fromDate = fromDatePicker.getDate();
		Date toDate = toDatePicker.getDate();

		if (fromDate.after(toDate)) {
			POSMessageDialog.showError(com.floreantpos.util.POSUtil.getFocusedWindow(), com.floreantpos.POSConstants.FROM_DATE_CANNOT_BE_GREATER_THAN_TO_DATE_);
			return;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.clear();

		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(fromDate);

		calendar.set(Calendar.YEAR, calendar2.get(Calendar.YEAR));
		calendar.set(Calendar.MONTH, calendar2.get(Calendar.MONTH));
		calendar.set(Calendar.DATE, calendar2.get(Calendar.DATE));
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		fromDate = calendar.getTime();

		calendar.clear();
		calendar2.setTime(toDate);
		calendar.set(Calendar.YEAR, calendar2.get(Calendar.YEAR));
		calendar.set(Calendar.MONTH, calendar2.get(Calendar.MONTH));
		calendar.set(Calendar.DATE, calendar2.get(Calendar.DATE));
		calendar.set(Calendar.HOUR, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		toDate = calendar.getTime();

		try {
			JasperReport report = ReportUtil.getReport("inventoryOnHandReport"); //$NON-NLS-1$

			HashMap properties = new HashMap();
			ReportUtil.populateRestaurantProperties(properties);
			properties.put("fromDate", fromDate); //$NON-NLS-1$
			properties.put("toDate", toDate); //$NON-NLS-1$
			properties.put("reportDate", new Date()); //$NON-NLS-1$
			properties.put("reportTitle", "Purchase Order");

			Restaurant restaurant = Application.getInstance().getRestaurant();

			properties.put("companyName", restaurant.getName());
			properties.put("address", restaurant.getAddressLine1());
			properties.put("city", restaurant.getAddressLine2());
			properties.put("phone", restaurant.getTelephone());
			properties.put("fax", restaurant.getZipCode());
			properties.put("email", restaurant.getAddressLine3());


			InventoryOnHandReportModel reportModel = new InventoryOnHandReportModel();
			reportModel.setRows(inventList);

			JasperPrint print = JasperFillManager.fillReport(report, properties, new JRTableModelDataSource(reportModel));

			JRViewer viewer = new JRViewer(print);
			reportPanel.removeAll();
			reportPanel.add(viewer);
			reportPanel.revalidate();
		} catch (JRException e) {
			PosLog.error(getClass(), e);
		}
	}

	{
		// GUI initializer generated by IntelliJ IDEA GUI Designer
		// >>> IMPORTANT!! <<<
		// DO NOT EDIT OR ADD ANY CODE HERE!
		$$$setupUI$$$();
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		contentPane = new JPanel();
		contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayoutManager(2, 7, new Insets(10, 10, 10, 10), 10, 10));
		contentPane.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK
				| GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
		final JLabel label1 = new JLabel();
		label1.setText(com.floreantpos.POSConstants.FROM + ":"); //$NON-NLS-1$
		panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED,
				GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label2 = new JLabel();
		label2.setText(com.floreantpos.POSConstants.TO + ":"); //$NON-NLS-1$
		panel1.add(label2, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED,
				GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		//final JLabel label3 = new JLabel();
		//label3.setText(com.floreantpos.POSConstants.TERMINAL);
		//panel1.add(label3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		fromDatePicker = UiUtil.getCurrentMonthStart();
		panel1.add(fromDatePicker, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
				GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK
						| GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(147, 24), null, 0, false));
		toDatePicker = UiUtil.getCurrentMonthEnd();
		panel1.add(toDatePicker, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK
				| GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null,
				new Dimension(147, 24), null, 0, false));
		final Spacer spacer1 = new Spacer();
		panel1.add(spacer1, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
				GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
		//cbTerminal = new JComboBox();
		//panel1.add(cbTerminal, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(147, 22), null, 0, false));
		btnGo = new JButton();
		btnGo.setText(com.floreantpos.POSConstants.GO);
		panel1.add(btnGo, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_FIXED, null,
				new Dimension(147, 23), null, 0, false));
		//final JLabel label4 = new JLabel();
		//label4.setText(com.floreantpos.POSConstants.USER_TYPE_);
		//panel1.add(label4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		//cbUserType = new JComboBox();
		//panel1.add(cbUserType, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(147, 22), null, 0, false));
		final JSeparator separator1 = new JSeparator();
		panel1.add(separator1, new GridConstraints(1, 0, 1, 7, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW,
				GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		reportPanel = new JPanel();
		reportPanel.setLayout(new BorderLayout(0, 0));
		contentPane.add(reportPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
				GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK
						| GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return contentPane;
	}

}
