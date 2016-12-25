package com.floreantpos.table;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableModel;

import com.floreantpos.Messages;
import com.floreantpos.PosLog;
import com.floreantpos.bo.ui.Command;
import com.floreantpos.bo.ui.ModelBrowser;
import com.floreantpos.ui.BeanEditor;

public class ShopTableModelBrowser<E> extends ModelBrowser {

	private BeanEditor beanEditor;
	private JButton btnDuplicate = new JButton(Messages.getString("ShopTableModelBrowser.0")); //$NON-NLS-1$
	private JButton btnDeleteAll = new JButton(Messages.getString("ShopTableModelBrowser.1")); //$NON-NLS-1$

	public ShopTableModelBrowser(BeanEditor<E> beanEditor) {
		super(beanEditor);
		this.beanEditor = beanEditor;
	}

	@Override
	public void init(TableModel tableModel) {
		super.init(tableModel);
		buttonPanel.add(btnDuplicate);
		btnDuplicate.addActionListener(this);
		btnDuplicate.setEnabled(false);

		buttonPanel.add(btnDeleteAll);
		btnDeleteAll.addActionListener(this);
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
					btnDuplicate.setEnabled(false);
					break;

				case EDIT:
					beanEditor.edit();
					beanEditor.setFieldsEnable(true);
					btnNew.setEnabled(false);
					btnEdit.setEnabled(false);
					btnSave.setEnabled(true);
					btnDelete.setEnabled(false);
					btnCancel.setEnabled(true);
					btnDuplicate.setEnabled(false);
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
						customSelectedRow();
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
						btnDuplicate.setEnabled(false);
					}
					break;

				default:
					break;
			}

			handleAdditionaButtonActionIfApplicable(e);

			ShopTableForm form = (ShopTableForm) beanEditor;
			if (e.getSource() == btnDuplicate) {

				form.setDuplicate(true);
				form.createNew();
				form.save();
				refreshTable();
				customSelectedRow();
				btnSave.setEnabled(false);
				btnCancel.setEnabled(false);
			}
			else if (e.getSource() == btnDeleteAll) {

				if (!form.deleteAllTables())
					return;

				refreshTable();
				btnNew.setEnabled(true);
				btnEdit.setEnabled(false);
				btnSave.setEnabled(false);
				btnDelete.setEnabled(false);
				btnCancel.setEnabled(false);
				btnDuplicate.setEnabled(false);
			}

		} catch (Exception e2) {
			PosLog.error(getClass(), e2);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		super.valueChanged(e);
		btnDuplicate.setEnabled(true);
		btnDeleteAll.setEnabled(true);
	}

	@Override
	public void doCancelEditing() {
		super.doCancelEditing();
		if (browserTable.getSelectedRow() != -1) {
			btnDuplicate.setEnabled(true);
		}
	}

	private void customSelectedRow() {
		ShopTableForm form = (ShopTableForm) beanEditor;
		int x = getRowByValue(browserTable.getModel(), form.getNewTable());
		browserTable.setRowSelectionInterval(x, x);
	}

	private int getRowByValue(TableModel model, Object value) {
		for (int i = 0; i <= model.getRowCount(); i++) {
			if (model.getValueAt(i, 0).equals(value)) {
				return i;
			}
		}
		return -1;
	}
}