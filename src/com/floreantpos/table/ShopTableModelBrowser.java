package com.floreantpos.table;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableModel;

import com.floreantpos.bo.ui.Command;
import com.floreantpos.bo.ui.ModelBrowser;
import com.floreantpos.ui.BeanEditor;

public class ShopTableModelBrowser<E> extends ModelBrowser {

	private BeanEditor beanEditor;
	private JButton btnDuplicate = new JButton("DUP");
	private JButton btnDeleteAll = new JButton("DEL ALL");

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
						btnDuplicate.setEnabled(false);
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
						btnDuplicate.setEnabled(false);
						refreshTable();
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
				btnEdit.setEnabled(false);
				btnSave.setEnabled(false);
				btnDelete.setEnabled(false);
				btnCancel.setEnabled(false);
			}
			else if (e.getSource() == btnDeleteAll) {

				form.deleteAllTables();
				refreshTable();
				btnNew.setEnabled(true);
				btnEdit.setEnabled(false);
				btnSave.setEnabled(false);
				btnDelete.setEnabled(false);
				btnCancel.setEnabled(false);
				btnDuplicate.setEnabled(false);
			}

		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		super.valueChanged(e);
		btnDuplicate.setEnabled(true);
	}

	@Override
	public void doCancelEditing() {
		super.doCancelEditing();
		btnDuplicate.setEnabled(false);
	}
}