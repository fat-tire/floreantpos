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
package com.floreantpos.bo.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXTable;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.swing.BeanTableModel;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.ui.SearchPanel;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class ModelBrowser<E> extends JPanel implements ActionListener, ListSelectionListener {

	protected JXTable browserTable;
	protected BeanEditor<E> beanEditor;
	protected SearchPanel<E> searchPanel;
	protected JPanel buttonPanel;
	protected JPanel browserPanel = new JPanel(new BorderLayout());
	private JPanel beanPanel = new JPanel(new BorderLayout());

	protected JButton btnNew = new JButton(Messages.getString("ModelBrowser.0")); //$NON-NLS-1$
	protected JButton btnEdit = new JButton(Messages.getString("ModelBrowser.1")); //$NON-NLS-1$
	protected JButton btnSave = new JButton(Messages.getString("ModelBrowser.2")); //$NON-NLS-1$
	protected JButton btnDelete = new JButton(Messages.getString("ModelBrowser.3")); //$NON-NLS-1$
	protected JButton btnCancel = new JButton(Messages.getString("ModelBrowser.4")); //$NON-NLS-1$

	public ModelBrowser() {
		this(null);
	}

	public ModelBrowser(BeanEditor<E> beanEditor) {
		super();
		this.beanEditor = beanEditor;

	}

	public ModelBrowser(BeanEditor<E> beanEditor, SearchPanel<E> searchPanel) {
		super();
		this.beanEditor = beanEditor;
		this.searchPanel = searchPanel;
	}
	
	


	public void init(TableModel tableModel) {
		browserTable = new JXTable();
		browserTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		browserTable.getSelectionModel().addListSelectionListener(this);
		browserTable.setDefaultRenderer(Date.class, new CustomCellRenderer());

		if (tableModel != null) {
			browserTable.setModel(tableModel);
		}

		setLayout(new BorderLayout(10, 10));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		if (searchPanel != null) {
			searchPanel.setModelBrowser(this);
			browserPanel.add(searchPanel, BorderLayout.NORTH);
		}
		browserPanel.add(new JScrollPane(browserTable));
		add(browserPanel);

		beanPanel.setBorder(BorderFactory.createEtchedBorder());
		JPanel beanEditorPanel = new JPanel(new MigLayout()); //$NON-NLS-1$
		beanEditorPanel.add(beanEditor);
		beanPanel.add(beanEditorPanel);

		buttonPanel = new JPanel();

		JButton additionalButton = getAdditionalButton();
		if (additionalButton != null) {
			buttonPanel.add(additionalButton);
			additionalButton.addActionListener(this);
		}

		buttonPanel.add(btnNew);
		buttonPanel.add(btnEdit);
		buttonPanel.add(btnSave);
		buttonPanel.add(btnDelete);
		buttonPanel.add(btnCancel);

		beanPanel.setPreferredSize(new Dimension(600, 400));
		beanPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(beanPanel, BorderLayout.EAST);

		btnNew.addActionListener(this);
		btnEdit.addActionListener(this);
		btnDelete.addActionListener(this);
		btnSave.addActionListener(this);
		btnCancel.addActionListener(this);

		btnNew.setEnabled(true);
		btnEdit.setEnabled(false);
		btnSave.setEnabled(false);
		btnDelete.setEnabled(false);
		btnCancel.setEnabled(false);

		beanEditor.clearFields();
		beanEditor.setFieldsEnable(false);

		refreshTable();
	}

	public void refreshTable() {
	}

	protected JButton getAdditionalButton() {
		return null;
	}

	protected void handleAdditionaButtonActionIfApplicable(ActionEvent e) {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Command command = Command.fromString(e.getActionCommand());

		try {
			switch (command) {
				case NEW:
					beanEditor.createNew();
					beanEditor.setFieldsEnable(true);
					btnNew.setEnabled(false);
					btnEdit.setEnabled(false);
					btnSave.setEnabled(true);
					btnDelete.setEnabled(false);
					btnCancel.setEnabled(true);
					browserTable.clearSelection();
					break;

				case EDIT:
					beanEditor.edit();
					beanEditor.setFieldsEnable(true);
					btnNew.setEnabled(false);
					btnEdit.setEnabled(false);
					btnSave.setEnabled(true);
					btnDelete.setEnabled(false);
					btnCancel.setEnabled(true);
					break;

				case CANCEL:
					doCancelEditing();
					break;

				case SAVE:
					if (beanEditor.save()) {
						beanEditor.setFieldsEnable(false);
						btnNew.setEnabled(true);
						btnEdit.setEnabled(false);
						btnSave.setEnabled(false);
						btnDelete.setEnabled(false);
						btnCancel.setEnabled(false);
						refreshTable();
					}
					break;

				case DELETE:
					if (beanEditor.delete()) {
						beanEditor.setBean(null);
						beanEditor.setFieldsEnable(false);
						btnNew.setEnabled(true);
						btnEdit.setEnabled(false);
						btnSave.setEnabled(false);
						btnDelete.setEnabled(false);
						btnCancel.setEnabled(false);
						refreshTable();
					}
					break;

				default:
					break;
			}

			handleAdditionaButtonActionIfApplicable(e);

		} catch (Exception e2) {
			POSMessageDialog.showError(Application.getPosWindow(), e2.getMessage(), e2);
		}
	}

	public void doCancelEditing() {

		if (browserTable.getSelectedRow() != -1) {
			beanEditor.setFieldsEnable(false);
			btnNew.setEnabled(true);
			btnEdit.setEnabled(true);
			btnSave.setEnabled(false);
			btnDelete.setEnabled(true);
			btnCancel.setEnabled(false);
			beanEditor.cancel();
		}
		else {
			beanEditor.cancel();
			beanEditor.clearFields();
			beanEditor.setBean(null);
			beanEditor.setFieldsEnable(false);
			btnNew.setEnabled(true);
			btnEdit.setEnabled(false);
			btnSave.setEnabled(false);
			btnDelete.setEnabled(false);
			btnCancel.setEnabled(false);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) {
			return;
		}

		BeanTableModel<E> model = (BeanTableModel<E>) browserTable.getModel();
		int selectedRow = browserTable.getSelectedRow();
		if (selectedRow < 0) {
			return;
		}
		selectedRow = browserTable.convertRowIndexToModel(selectedRow);

		if (selectedRow < 0)
			return;

		E data = (E) model.getRow(selectedRow);
		beanEditor.setBean(data);
		btnNew.setEnabled(true);
		btnEdit.setEnabled(true);
		btnSave.setEnabled(false);
		btnDelete.setEnabled(true);
		btnCancel.setEnabled(false);
		beanEditor.setFieldsEnable(false);

	}

	public void setModels(List<E> models) {
		BeanTableModel<E> tableModel = (BeanTableModel<E>) browserTable.getModel();
		tableModel.removeAll();
		tableModel.addRows(models);

	}

	public BeanEditor<E> getBeanEditor() {
		return beanEditor;
	}

	public SearchPanel<E> getSearchPanel() {
		return searchPanel;
	}
	
	public void refreshButtonPanel() {
		beanEditor.clearFields();
		btnNew.setEnabled(true);
		btnEdit.setEnabled(false);
		btnSave.setEnabled(false);
		btnDelete.setEnabled(false);
		btnCancel.setEnabled(false);
	}
}