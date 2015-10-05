package com.floreantpos.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.jdesktop.swingx.JXTable;

import com.floreantpos.Messages;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.swing.ListTableModel;
import com.floreantpos.swing.PosScrollPane;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class TransactionListView extends JPanel {
	private JXTable table;
	private TransactionListTableModel tableModel;

	public TransactionListView() {
		table = new TransactionListTable();
		table.setSortable(false);
		table.setModel(tableModel = new TransactionListTableModel());
		table.setRowHeight(40);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		table.setGridColor(Color.LIGHT_GRAY);
		
//		TableColumnModel columnModel = table.getColumnModel();
//		columnModel.getColumn(0).setPreferredWidth(20);
//		columnModel.getColumn(1).setPreferredWidth(20);
//		columnModel.getColumn(2).setPreferredWidth(200);
//		columnModel.getColumn(3).setPreferredWidth(100);

		PosScrollPane scrollPane = new PosScrollPane(table, PosScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, PosScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
		scrollBar.setPreferredSize(new Dimension(30, 60));

		setLayout(new BorderLayout());

		add(scrollPane);
	}

	public void setTransactions(List<PosTransaction> transactions) {
		tableModel.setRows(transactions);
	}

	public void addTransaction(PosTransaction transaction) {
		tableModel.addItem(transaction);
	}

	public PosTransaction getSelectedTransaction() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow < 0) {
			return null;
		}

		return tableModel.getRowData(selectedRow);
	}
	
	public List<PosTransaction> getAllTransactions() {
		return this.tableModel.getRows();
	}

	public List<PosTransaction> getSelectedTransactions() {
		int[] selectedRows = table.getSelectedRows();

		ArrayList<PosTransaction> transactions = new ArrayList<PosTransaction>(selectedRows.length);

		for (int i = 0; i < selectedRows.length; i++) {
			PosTransaction transaction = tableModel.getRowData(selectedRows[i]);
			transactions.add(transaction);
		}
		
		return transactions;
	}

	private class TransactionListTable extends JXTable {
		
		public TransactionListTable() {
			setColumnControlVisible(false);
		}
		
		@Override
		public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
			ListSelectionModel selectionModel = getSelectionModel();
			boolean selected = selectionModel.isSelectedIndex(rowIndex);
			if (selected) {
				selectionModel.removeSelectionInterval(rowIndex, rowIndex);
			}
			else {
				selectionModel.addSelectionInterval(rowIndex, rowIndex);
			}
		}
	}

	private class TransactionListTableModel extends ListTableModel<PosTransaction> {
		public TransactionListTableModel() {
			super(new String[] { Messages.getString("TransactionListView.0"), Messages.getString("TransactionListView.1"), Messages.getString("TransactionListView.2"), Messages.getString("TransactionListView.3"), Messages.getString("TransactionListView.4"), Messages.getString("TransactionListView.5"), Messages.getString("TransactionListView.6") }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			PosTransaction transaction = rows.get(rowIndex);

			switch (columnIndex) {
			case 0:
				return Integer.valueOf(transaction.getId());

			case 1:
				return transaction.getTicket().getId();
				
			case 2:
				return transaction.getTicket().getOwner().getFirstName();
				
			case 3:
				return transaction.getCardType();
				
			case 4:
				return transaction.getTipsAmount();

			case 5:
				return transaction.getAmount() - transaction.getTipsAmount();
				
			case 6:
				return transaction.getAmount();

			}

			return null;
		}

	}
	
	public PosTransaction getFirstSelectedTransaction() {
		List<PosTransaction> selectedTickets = getSelectedTransactions();

		if (selectedTickets.size() == 0 || selectedTickets.size() > 1) {
			POSMessageDialog.showMessage(Messages.getString("TransactionListView.7")); //$NON-NLS-1$
			return null;
		}

		PosTransaction t = selectedTickets.get(0);

		return t;
	}
	
	public JXTable getTable() {
		return table;
	}
}
