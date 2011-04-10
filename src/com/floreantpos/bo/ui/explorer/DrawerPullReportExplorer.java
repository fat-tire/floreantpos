package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXDatePicker;

import com.floreantpos.POSConstants;
import com.floreantpos.model.DrawerPullReport;
import com.floreantpos.model.dao.DrawerPullReportDAO;
import com.floreantpos.model.util.DateUtil;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class DrawerPullReportExplorer extends TransparentPanel {
	private JXDatePicker fromDatePicker = new JXDatePicker();
	private JXDatePicker toDatePicker = new JXDatePicker();
	private JButton btnGo = new JButton(com.floreantpos.POSConstants.GO);
	private JButton btnEditActualAmount = new JButton(com.floreantpos.POSConstants.EDIT_ACTUAL_AMOUNT);
	
	private static SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd MMM, yyyy hh:mm a");

	private JTable table;

	public DrawerPullReportExplorer() {
		super(new BorderLayout());

		JPanel topPanel = new JPanel(new MigLayout());

		topPanel.add(new JLabel(com.floreantpos.POSConstants.FROM), "grow");
		topPanel.add(fromDatePicker, "wrap");
		topPanel.add(new JLabel(com.floreantpos.POSConstants.TO), "grow");
		topPanel.add(toDatePicker, "wrap");
		topPanel.add(btnGo, "skip 1, al right");
		add(topPanel, BorderLayout.NORTH);

		add(new JScrollPane(table = new JTable(new DrawerPullExplorerTableModel(null))));
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		bottomPanel.add(btnEditActualAmount);
		add(bottomPanel, BorderLayout.SOUTH);

		btnGo.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					viewReport();
				} catch (Exception e1) {
					POSMessageDialog.showError(DrawerPullReportExplorer.this, POSConstants.ERROR_MESSAGE, e1);
				}
			}

		});
		btnEditActualAmount.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try {
					int selectedRow = table.getSelectedRow();
					if(selectedRow < 0) {
						JOptionPane.showMessageDialog(DrawerPullReportExplorer.this, com.floreantpos.POSConstants.SELECT_DRAWER_PULL_TO_EDIT, com.floreantpos.POSConstants.MDS_POS, JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					String amountString = JOptionPane.showInputDialog(DrawerPullReportExplorer.this, com.floreantpos.POSConstants.ENTER_ACTUAL_AMOUNT + ":");
					if(amountString == null) {
						return;
					}
					double amount = 0;
					try {
						amount = Double.parseDouble(amountString);
					}catch(Exception x) {
						JOptionPane.showMessageDialog(DrawerPullReportExplorer.this, com.floreantpos.POSConstants.INVALID_AMOUNT, com.floreantpos.POSConstants.MDS_POS, JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					DrawerPullExplorerTableModel model = (DrawerPullExplorerTableModel) table.getModel();
					DrawerPullReport report = (DrawerPullReport) model.getRowData(selectedRow);
					report.setCashToDeposit(amount);
					
					DrawerPullReportDAO dao = new DrawerPullReportDAO();
					dao.saveOrUpdate(report);
					model.updateItem(selectedRow);
				} catch (Exception e1) {
					POSMessageDialog.showError(DrawerPullReportExplorer.this, POSConstants.ERROR_MESSAGE, e1);
				}
			}
			
		});
	}

	private void viewReport() {
		try {
			Date fromDate = fromDatePicker.getDate();
			Date toDate = toDatePicker.getDate();

			fromDate = DateUtil.startOfDay(fromDate);
			toDate = DateUtil.endOfDay(toDate);

			List<DrawerPullReport> list = new DrawerPullReportDAO().findReports(fromDate, toDate);
			table.setModel(new DrawerPullExplorerTableModel(list));
		} catch (Exception e) {
			POSMessageDialog.showError(this, POSConstants.ERROR_MESSAGE, e);
		}
	}

	class DrawerPullExplorerTableModel extends ListTableModel {
		String[] columnNames = { com.floreantpos.POSConstants.ID, com.floreantpos.POSConstants.TIME, com.floreantpos.POSConstants.DRAWER_PULL_AMOUNT, com.floreantpos.POSConstants.ACTUAL_AMOUNT };
		
		
		DrawerPullExplorerTableModel(List<DrawerPullReport> list) {
			setRows(list);
			setColumnNames(columnNames);
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			DrawerPullReport report = (DrawerPullReport) rows.get(rowIndex);

			switch (columnIndex) {
			case 0:
				return report.getId().toString();

			case 1:
				return dateTimeFormatter.format(report.getReportTime());

			case 2:
				return report.getDrawerAccountable();

			case 3:
				return report.getCashToDeposit();

			}
			return null;
		}
	}
}
