package com.floreantpos.table;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableModel;

import com.floreantpos.Messages;
import com.floreantpos.bo.ui.Command;
import com.floreantpos.bo.ui.ModelBrowser;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.TableBookingInfo;
import com.floreantpos.model.dao.ShopTableDAO;
import com.floreantpos.model.dao.TableBookingInfoDAO;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;

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
						btnDuplicate.setEnabled(true);
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
				
				form.setTmp(true);
				form.createNew();
				form.save();
				refreshTable();
			}else if(e.getSource()==btnDeleteAll) {
				form.deleteAllTables();
				refreshTable();
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
	}
}
