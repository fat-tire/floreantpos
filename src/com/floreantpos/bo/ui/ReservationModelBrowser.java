package com.floreantpos.bo.ui;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableModel;

import com.floreantpos.ui.BookingBeanEditor;
import com.floreantpos.ui.SearchPanel;

public class ReservationModelBrowser<E> extends ModelBrowser {

	BookingBeanEditor beanEditor;
	private JButton btnSeat = new JButton("SEAT");
	private JButton btnNoAppear = new JButton("NO APR");
	private JButton btnDelay = new JButton("DELAY");
	private JButton btnBookingCancel = new JButton("CANCEL BK");

	public ReservationModelBrowser(BookingBeanEditor<E> beanEditor, SearchPanel<E> searchPanel) {
		super(beanEditor, searchPanel);
		this.beanEditor = beanEditor;
		btnSeat.setEnabled(false);
		btnNoAppear.setEnabled(false);
		btnDelay.setEnabled(false);
		btnBookingCancel.setEnabled(false);
		btnDelete.setText("DEL");
	}

	@Override
	public void init(TableModel tableModel) {
		super.init(tableModel);
		buttonPanel.add(btnSeat);
		buttonPanel.add(btnDelay);
		buttonPanel.add(btnNoAppear);
		buttonPanel.add(btnBookingCancel);

		btnSeat.addActionListener(this);
		btnDelay.addActionListener(this);
		btnNoAppear.addActionListener(this);
		btnBookingCancel.addActionListener(this);
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
					btnSeat.setEnabled(false);
					btnBookingCancel.setEnabled(false);
					btnDelay.setEnabled(false);
					btnNoAppear.setEnabled(false);
					break;

				case EDIT:
					beanEditor.edit();
					beanEditor.setFieldsEnable(true);
					btnNew.setEnabled(false);
					btnEdit.setEnabled(false);
					btnSave.setEnabled(true);
					btnDelete.setEnabled(false);
					btnCancel.setEnabled(true);
					btnSeat.setEnabled(false);
					btnBookingCancel.setEnabled(false);
					btnDelay.setEnabled(false);
					btnNoAppear.setEnabled(false);
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
						btnSeat.setEnabled(false);
						btnBookingCancel.setEnabled(false);
						btnDelay.setEnabled(false);
						btnNoAppear.setEnabled(false);
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
						btnSeat.setEnabled(false);
						btnBookingCancel.setEnabled(false);
						btnDelay.setEnabled(false);
						btnNoAppear.setEnabled(false);
						refreshTable();
					}
					break;

				default:
					break;
			}

			handleAdditionaButtonActionIfApplicable(e);

			if (e.getSource() == btnSeat) {
				beanEditor.bookingStatus("seat");
			}
			else if (e.getSource() == btnBookingCancel) {
				beanEditor.bookingStatus("cancel");
			}
			else if (e.getSource() == btnDelay) {
				beanEditor.bookingStatus("delay");
			}
			else if (e.getSource() == btnNoAppear) {
				beanEditor.bookingStatus("no appear");
			}

		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		super.valueChanged(e);
		btnSeat.setEnabled(true);
		btnNoAppear.setEnabled(true);
		btnDelay.setEnabled(true);
		btnBookingCancel.setEnabled(true);
	}

	@Override
	public void doCancelEditing() {
		super.doCancelEditing();
		btnSeat.setEnabled(false);
		btnBookingCancel.setEnabled(false);
		btnDelay.setEnabled(false);
		btnNoAppear.setEnabled(false);

	}
}
