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
package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.TableColumnExt;
import org.jdesktop.swingx.table.TableColumnModelExt;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.PosException;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.DrawerPullReport;
import com.floreantpos.model.dao.DrawerPullReportDAO;
import com.floreantpos.model.util.DateUtil;
import com.floreantpos.print.PosPrintService;
import com.floreantpos.swing.ListTableModel;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.util.UiUtil;
import com.floreantpos.util.POSUtil;

public class DrawerPullReportExplorer extends TransparentPanel {
	private JXDatePicker fromDatePicker = UiUtil.getCurrentMonthStart();
	private JXDatePicker toDatePicker = UiUtil.getCurrentMonthEnd();
	private JButton btnGo = new JButton(com.floreantpos.POSConstants.GO);
	private JButton btnEditActualAmount = new JButton(com.floreantpos.POSConstants.EDIT_ACTUAL_AMOUNT);
	private JButton btnPrint = new JButton(Messages.getString("DrawerPullReportExplorer.0")); //$NON-NLS-1$
	private static SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd MMM, yyyy hh:mm a"); //$NON-NLS-1$
	private TableColumnModelExt columnModel;
	private JXTable table;

	public DrawerPullReportExplorer() {
		super(new BorderLayout());
		add(new JScrollPane(table = new JXTable(new DrawerPullExplorerTableModel(null))));
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setColumnControlVisible(true);

		resizeColumnWidth(table);

		restoreTableColumnsVisibility();
		addTableColumnListener();
		JPanel topPanel = new JPanel(new MigLayout());

		topPanel.add(new JLabel(com.floreantpos.POSConstants.FROM), "grow"); //$NON-NLS-1$
		topPanel.add(fromDatePicker, "wrap"); //$NON-NLS-1$
		topPanel.add(new JLabel(com.floreantpos.POSConstants.TO), "grow"); //$NON-NLS-1$
		topPanel.add(toDatePicker, "wrap"); //$NON-NLS-1$
		topPanel.add(btnGo, "skip 1, al right"); //$NON-NLS-1$
		add(topPanel, BorderLayout.NORTH);

		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		bottomPanel.add(btnEditActualAmount);
		bottomPanel.add(btnPrint);
		add(bottomPanel, BorderLayout.SOUTH);

		btnPrint.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
				int selectedRow = table.getSelectedRow();
				if (selectedRow < 0) {
					BOMessageDialog.showError(DrawerPullReportExplorer.this, Messages.getString("DrawerPullReportExplorer.1")); //$NON-NLS-1$
					return;
				}
				DrawerPullExplorerTableModel model = (DrawerPullExplorerTableModel) table.getModel();
				DrawerPullReport report = (DrawerPullReport) model.getRowData(selectedRow);

				PosPrintService.printDrawerPullReport(report, report.getTerminal());
				}catch (PosException exception) {
					POSMessageDialog.showError(POSUtil.getFocusedWindow(), exception.getMessage());
				}
			}
		});

		btnGo.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					viewReport();
					resizeColumnWidth(table);
				} catch (Exception e1) {
					BOMessageDialog.showError(DrawerPullReportExplorer.this, POSConstants.ERROR_MESSAGE, e1);
				}
			}

		});
		btnEditActualAmount.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					int selectedRow = table.getSelectedRow();
					if (selectedRow < 0) {
						BOMessageDialog.showError(DrawerPullReportExplorer.this, com.floreantpos.POSConstants.SELECT_DRAWER_PULL_TO_EDIT);
						return;
					}

					String amountString = JOptionPane.showInputDialog(DrawerPullReportExplorer.this, com.floreantpos.POSConstants.ENTER_ACTUAL_AMOUNT + ":"); //$NON-NLS-1$
					if (amountString == null) {
						return;
					}
					double amount = 0;
					try {
						amount = Double.parseDouble(amountString);
					} catch (Exception x) {
						BOMessageDialog.showError(DrawerPullReportExplorer.this, com.floreantpos.POSConstants.INVALID_AMOUNT);
						return;
					}

					DrawerPullExplorerTableModel model = (DrawerPullExplorerTableModel) table.getModel();
					DrawerPullReport report = (DrawerPullReport) model.getRowData(selectedRow);
					report.setCashToDeposit(amount);

					DrawerPullReportDAO dao = new DrawerPullReportDAO();
					dao.saveOrUpdate(report);
					model.updateItem(selectedRow);
				} catch (Exception e1) {
					BOMessageDialog.showError(DrawerPullReportExplorer.this, POSConstants.ERROR_MESSAGE, e1);
				}
			}

		});
	}

	private void restoreTableColumnsVisibility() {
		String recordedSelectedColumns = TerminalConfig.getDrawerPullReportHiddenColumns();
		TableColumnModelExt columnModel = (TableColumnModelExt) table.getColumnModel();

		if (recordedSelectedColumns.isEmpty()) {
			return;
		}
		String str[] = recordedSelectedColumns.split("\\*"); //$NON-NLS-1$
		for (int i = 0; i < str.length; i++) {
			Integer columnIndex = Integer.parseInt(str[i]);
			columnModel.getColumnExt((columnIndex - i)).setVisible(false);
		}
	}

	private void viewReport() {
		try {
			Date fromDate = fromDatePicker.getDate();
			Date toDate = toDatePicker.getDate();

			fromDate = DateUtil.startOfDay(fromDate);
			toDate = DateUtil.endOfDay(toDate);

			List<DrawerPullReport> list = new DrawerPullReportDAO().findReports(fromDate, toDate);
			DrawerPullExplorerTableModel model = (DrawerPullExplorerTableModel) table.getModel();
			model.setRows(list);
		} catch (Exception e) {
			BOMessageDialog.showError(this, POSConstants.ERROR_MESSAGE, e);
		}
	}

	class DrawerPullExplorerTableModel extends ListTableModel {
		String[] columnNames = { com.floreantpos.POSConstants.ID, com.floreantpos.POSConstants.TIME, "TICKET COUNT", //$NON-NLS-1$
				com.floreantpos.POSConstants.DRAWER_PULL_AMOUNT, "USER ID", "TERMINAL ID", "BEGIN CASH", "NET SALES", "SALES TAX", "CASH TAX", "TOTAL REVENUE", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
				"GROSS RECEIPTS", "GIFT CERT RETURN COUNT", "GIFT CERT RETURN AMOUNT", "GIFT CERT CHARGE AMOUNT", "CASH RECEIPT NO", "CASH RECEIPT AMOUNT", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
				"CREDIT CARD RECEIPT NO", "CREDIT CARD RECEIPT AMOUNT", "DEBIT CARD RECEIPT NO", "DEBIT CARD RECEIPT AMOUNT", "REFUND RECEIPT COUNT", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				"REFUND AMOUNT", "RECEIPT DIFFERENTIAL", "CASH BACK", "CASH TIPS", "CHARGED TIPS", "TIPS PAID", "TIPS DIFFERENTIAL", "PAY OUT NO", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
				"PAY OUT AMOUNT", "DRAWER BLEED NO", "DRAWER BLEED AMOUNT", com.floreantpos.POSConstants.ACTUAL_AMOUNT, "VARIANCE", "TOTAL VOIDWST", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				"TOTAL VOID", "TOTAL DISCOUNT COUNT", "TOTAL DISCOUNT AMOUNT", "TOTAL DISCOUNT SALES", "TOTAL DISCOUNT GUEST", "TOTAL DISCOUNT PARTY SIZE", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
				"TOTAL DISCOUNT CHECK SIZE", "TOTAL DISCOUNT PERCENTAGE", "TOTAL DISCOUNT RATIO" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

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
					return report.getTicketCount();

				case 3:
					return report.getDrawerAccountable();

				case 4:
					return report.getAssignedUser().getUserId();

				case 5:
					return report.getTerminal().getId();

				case 6:
					return report.getBeginCash();

				case 7:
					return report.getNetSales();

				case 8:
					return report.getSalesTax();

				case 9:
					return report.getCashTax();

				case 10:
					return report.getTotalRevenue();

				case 11:
					return report.getGrossReceipts();

				case 12:
					return report.getGiftCertReturnCount();

				case 13:
					return report.getGiftCertReturnAmount();

				case 14:
					return report.getGiftCertChangeAmount();

				case 15:
					return report.getCashReceiptNumber();

				case 16:
					return report.getCashReceiptAmount();

				case 17:
					return report.getCreditCardReceiptNumber();

				case 18:
					return report.getCreditCardReceiptAmount();

				case 19:
					return report.getDebitCardReceiptNumber();

				case 20:
					return report.getDebitCardReceiptAmount();

				case 21:
					return report.getRefundReceiptCount();

				case 22:
					return report.getRefundAmount();

				case 23:
					return report.getReceiptDifferential();

				case 24:
					return report.getCashBack();

				case 25:
					return report.getCashTips();

				case 26:
					return report.getChargedTips();

				case 27:
					return report.getTipsPaid();

				case 28:
					return report.getTipsDifferential();

				case 29:
					return report.getPayOutNumber();

				case 30:
					return report.getPayOutAmount();

				case 31:
					return report.getDrawerBleedNumber();

				case 32:
					return report.getDrawerBleedAmount();

				case 33:
					return report.getCashToDeposit();

				case 34:
					return report.getVariance();

				case 35:
					return report.getTotalVoidWst();

				case 36:
					return report.getTotalVoid();

				case 37:
					return report.getTotalDiscountCount();

				case 38:
					return report.getTotalDiscountAmount();

				case 39:
					return report.getTotalDiscountSales();

				case 40:
					return report.getTotalDiscountGuest();

				case 41:
					return report.getTotalDiscountPartySize();

				case 42:
					return report.getTotalDiscountCheckSize();

				case 43:
					return report.getTotalDiscountPercentage();

				case 44:
					return report.getTotalDiscountRatio();

			}
			return null;
		}
	}

	public void resizeColumnWidth(JTable table) {
		final TableColumnModel columnModel = table.getColumnModel();
		for (int column = 0; column < table.getColumnCount(); column++) {

			int columnWidthByComponent = getColumnWidthByComponentLenght(table, column);
			int columnWidthByHeader = getColumnWidthByHeaderLenght(table, column);
			if (columnWidthByComponent > columnWidthByHeader) {
				columnModel.getColumn(column).setPreferredWidth(columnWidthByComponent);
			}
			else {
				columnModel.getColumn(column).setPreferredWidth(columnWidthByHeader);
			}
		}
	}

	private int getColumnWidthByHeaderLenght(JTable table, int column) {
		int width = 50;
		TableColumn tcolumn = table.getColumnModel().getColumn(column);
		TableCellRenderer headerRenderer = tcolumn.getHeaderRenderer();

		if (headerRenderer == null) {
			headerRenderer = table.getTableHeader().getDefaultRenderer();
		}
		Object headerValue = tcolumn.getHeaderValue();
		Component headerComp = headerRenderer.getTableCellRendererComponent(table, headerValue, false, false, 0, column);

		width = Math.max(width, headerComp.getPreferredSize().width);

		return width + 20;
	}

	private int getColumnWidthByComponentLenght(JTable table, int column) {
		int width = 50;
		for (int row = 0; row < table.getRowCount(); row++) {
			TableCellRenderer renderer = table.getCellRenderer(row, column);
			Component comp = table.prepareRenderer(renderer, row, column);
			width = Math.max(comp.getPreferredSize().width + 1, width);
		}
		return width + 20;
	}

	private void saveHiddenColumns() {
		List<TableColumn> columns = columnModel.getColumns(true);
		List<Integer> indices = new ArrayList<Integer>();
		for (TableColumn tableColumn : columns) {
			TableColumnExt c = (TableColumnExt) tableColumn;
			if (!c.isVisible()) {
				indices.add(c.getModelIndex());
			}
		}
		saveTableColumnsVisibility(indices);
	}

	private void saveTableColumnsVisibility(List indices) {
		String selectedColumns = ""; //$NON-NLS-1$
		for (Iterator iterator = indices.iterator(); iterator.hasNext();) {
			String newSelectedColumn = String.valueOf(iterator.next());
			selectedColumns += newSelectedColumn;

			if (iterator.hasNext()) {
				selectedColumns += "*"; //$NON-NLS-1$
			}
		}
		TerminalConfig.setDrawerPullReportHiddenColumns(selectedColumns);
	}

	private void addTableColumnListener() {
		columnModel = (TableColumnModelExt) table.getColumnModel();
		columnModel.addColumnModelListener(new TableColumnModelListener() {

			@Override
			public void columnSelectionChanged(ListSelectionEvent e) {
			}

			@Override
			public void columnRemoved(TableColumnModelEvent e) {
				saveHiddenColumns();
			}

			@Override
			public void columnMoved(TableColumnModelEvent e) {

			}

			@Override
			public void columnMarginChanged(ChangeEvent e) {
			}

			@Override
			public void columnAdded(TableColumnModelEvent e) {
				saveHiddenColumns();
			}
		});

	}
}
