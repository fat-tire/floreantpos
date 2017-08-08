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
package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumnModel;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXTable;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.bo.ui.CustomCellRenderer;
import com.floreantpos.bo.ui.explorer.ExplorerButtonPanel;
import com.floreantpos.model.Tax;
import com.floreantpos.model.TaxGroup;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.swing.BeanTableModel;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.model.TaxForm;
import com.floreantpos.util.NumberUtil;
import com.floreantpos.util.POSUtil;

public class TaxSelectionDialog extends POSDialog {

	private JXTable table;
	private BeanTableModel<Tax> tableModel;
	private List<Tax> selectedTaxes;
	private TaxGroup taxGroup;

	public TaxSelectionDialog(TaxGroup taxGroup) {
		super(POSUtil.getBackOfficeWindow(), "");
		this.taxGroup = taxGroup;
		init();
		List<Tax> taxList = TaxDAO.getInstance().findAll();
		List<Tax> existingTaxs = null;
		if (taxGroup != null) {
			existingTaxs = taxGroup.getTaxes();
		}
		if (existingTaxs != null) {
			for (Tax tax : taxList) {
				if (existingTaxs.contains(tax)) {
					tax.setEnable(true);
				}
			}
		}
		tableModel.setRows(taxList);
		table.setDefaultRenderer(Object.class, new CustomCellRenderer());
	}

	private void init() {
		setLayout(new BorderLayout(5, 5));
		setTitle("Select Tax");
		TitlePanel titelpanel = new TitlePanel();
		titelpanel.setTitle("Tax Group: " + taxGroup.getName());

		add(titelpanel, BorderLayout.NORTH);
		tableModel = new BeanTableModel<Tax>(Tax.class) {
			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				if (columnIndex == 2) {
					Tax tax = tableModel.getRow(rowIndex);
					if (tax != null) {
						return NumberUtil.trimDecilamIfNotNeeded(tax.getRate()) + "%";
					}
				}
				return super.getValueAt(rowIndex, columnIndex);
			}
		};
		tableModel.addColumn("", "enable"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.NAME.toUpperCase(), "name"); //$NON-NLS-1$
		tableModel.addColumn("RATE", "rate"); //$NON-NLS-1$

		table = new JXTable(tableModel);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setTableHeader(null);
		table.setRowHeight(PosUIManager.getSize(30));
		table.setShowGrid(true, false);

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() == 2) {
					editSelectedRow();
				}
				else {
					selectItem();
				}
			}
		});

		JPanel contentPanel = new JPanel(new BorderLayout());
		JScrollPane scroll = new JScrollPane(table);
		contentPanel.add(scroll);

		add(contentPanel);
		resizeColumnWidth(table);
		add(createButtonPanel(), BorderLayout.SOUTH);
	}

	private TransparentPanel createButtonPanel() {
		ExplorerButtonPanel explorerButton = new ExplorerButtonPanel();
		JButton btnEdit = explorerButton.getEditButton();
		JButton btnAdd = explorerButton.getAddButton();

		JButton btnOk = new JButton("DONE");
		btnOk.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				doOk();
			}
		});

		JButton btnCancel = new JButton(POSConstants.CANCEL);
		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});
		btnAdd.setText("New Tax");
		btnEdit.setText(POSConstants.EDIT); //$NON-NLS-1$

		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editSelectedRow();
			}

		});

		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Tax tax = new Tax();
					TaxForm editor = new TaxForm(tax);
					BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), editor);
					dialog.open();

					if (dialog.isCanceled())
						return;

					Tax item = (Tax) editor.getBean();
					tableModel.addRow(item);
				} catch (Throwable x) {
					BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		btnOk.setBackground(Color.GREEN);

		TransparentPanel panel = new TransparentPanel(new MigLayout("center,ins 0 0 5 0", "sg,fill", ""));
		int h = PosUIManager.getSize(40);
		panel.add(btnAdd, "h " + h);
		//panel.add(btnEdit, "h " + h);
		panel.add(btnOk, "h " + h);
		panel.add(btnCancel, "h " + h);
		return panel;
	}

	public void resizeColumnWidth(JTable table) {
		final TableColumnModel columnModel = table.getColumnModel();
		for (int column = 0; column < table.getColumnCount(); column++) {
			columnModel.getColumn(column).setPreferredWidth((Integer) getColumnWidth().get(column));
		}
	}

	private List getColumnWidth() {
		List<Integer> columnWidth = new ArrayList();
		columnWidth.add(50);
		columnWidth.add(250);
		columnWidth.add(50);

		return columnWidth;
	}

	private void editSelectedRow() {
		try {
			int index = table.getSelectedRow();
			if (index < 0)
				return;

			index = table.convertRowIndexToModel(index);

			Tax tax = tableModel.getRow(index);
			tableModel.setRow(index, tax);

			TaxForm editor = new TaxForm(tax);
			BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), editor);
			dialog.open();
			if (dialog.isCanceled())
				return;

			table.repaint();
		} catch (Throwable x) {
			BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
		}
	}

	private void doOk() {
		try {
			selectedTaxes = new ArrayList<Tax>();
			List<Tax> items = tableModel.getRows();
			for (Iterator iterator = items.iterator(); iterator.hasNext();) {
				Tax tax = (Tax) iterator.next();
				if (tax.isEnable()) {
					selectedTaxes.add(tax);
				}
			}
			setCanceled(false);
			dispose();
		} catch (Throwable x) {
			BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
		}
	}

	public List<Tax> getSelectedTaxList() {
		return selectedTaxes;
	}

	private void selectItem() {
		if (table.getSelectedRow() < 0) {
			return;
		}
		int selectedRow = table.getSelectedRow();
		selectedRow = table.convertRowIndexToModel(selectedRow);
		Tax tax = tableModel.getRow(selectedRow);
		tax.setEnable(!tax.isEnable());
		table.repaint();
	}
}
