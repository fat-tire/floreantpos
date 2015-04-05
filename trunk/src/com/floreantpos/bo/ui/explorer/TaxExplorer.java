package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.model.Tax;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
import com.floreantpos.ui.model.TaxForm;

public class TaxExplorer extends TransparentPanel {
	private List<Tax> taxList;
	
	private JTable table;

	private TaxExplorerTableModel tableModel;
	
	public TaxExplorer() {
		taxList = TaxDAO.getInstance().findAll();
		
		tableModel = new TaxExplorerTableModel();
		table = new JTable(tableModel);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		
		setLayout(new BorderLayout(5,5));
		add(new JScrollPane(table));
		
		JButton addButton = new JButton(com.floreantpos.POSConstants.ADD);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					TaxForm editor = new TaxForm();
					BeanEditorDialog dialog = new BeanEditorDialog(editor);
					dialog.open();
					if (dialog.isCanceled())
						return;

					tableModel.addTax((Tax) editor.getBean());
				} catch (Exception x) {
				BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}
			
		});
		
		JButton editButton = new JButton(com.floreantpos.POSConstants.EDIT);
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					Tax tax = taxList.get(index);

					BeanEditorDialog dialog = new BeanEditorDialog(new TaxForm(tax));
					dialog.open();
					if (dialog.isCanceled())
						return;

					table.repaint();
				} catch (Throwable x) {
				BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}
			
		});
		JButton deleteButton = new JButton(com.floreantpos.POSConstants.DELETE);
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					if (ConfirmDeleteDialog.showMessage(TaxExplorer.this, com.floreantpos.POSConstants.CONFIRM_DELETE, com.floreantpos.POSConstants.DELETE) == ConfirmDeleteDialog.YES) {
						Tax tax = taxList.get(index);
						TaxDAO.getInstance().delete(tax);
						tableModel.deleteTax(tax, index);
					}
				} catch (Exception x) {
				BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}
			
		});

		TransparentPanel panel = new TransparentPanel();
		panel.add(addButton);
		panel.add(editButton);
		panel.add(deleteButton);
		add(panel, BorderLayout.SOUTH);
	}
	
	class TaxExplorerTableModel extends AbstractTableModel {
		String[] columnNames = {POSConstants.ID, POSConstants.NAME, POSConstants.RATE};
		
		public int getRowCount() {
			if(taxList == null) {
				return 0;
			}
			return taxList.size();
		}

		public int getColumnCount() {
			return columnNames.length;
		}
		
		@Override
		public String getColumnName(int column) {
			return columnNames[column];
		}
		
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			if(taxList == null)
				return ""; //$NON-NLS-1$
			
			Tax tax = taxList.get(rowIndex);
			
			switch(columnIndex) {
				case 0:
					return String.valueOf(tax.getId());
					
				case 1:
					return tax.getName();
					
				case 2:
					return tax.getRate();
					
			}

			return null;
		}

		public void addTax(Tax tax) {
			int size = taxList.size();
			taxList.add(tax);
			fireTableRowsInserted(size, size);
		}
		
		public void deleteTax(Tax tax, int index) {
			taxList.remove(tax);
			fireTableRowsDeleted(index, index);
		}
	}
}
