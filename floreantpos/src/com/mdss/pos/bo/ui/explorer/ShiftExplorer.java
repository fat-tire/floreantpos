package com.mdss.pos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.mdss.pos.model.Shift;
import com.mdss.pos.model.User;
import com.mdss.pos.model.dao.ShiftDAO;
import com.mdss.pos.model.dao.UserDAO;
import com.mdss.pos.swing.MessageDialog;
import com.mdss.pos.swing.TransparentPanel;
import com.mdss.pos.ui.PosTableRenderer;
import com.mdss.pos.ui.dialog.ConfirmDeleteDialog;
import com.mdss.pos.ui.model.ShiftEntryDialog;
import com.mdss.pos.util.ShiftUtil;

public class ShiftExplorer extends TransparentPanel {
	
	private JTable table;
	private ShiftTableModel tableModel;
	
	public ShiftExplorer() {
		List<Shift> shifts = new ShiftDAO().findAll();
		
		tableModel = new ShiftTableModel(shifts);
		table = new JTable(tableModel);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		
		setLayout(new BorderLayout(5,5));
		add(new JScrollPane(table));
		
		JButton addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ShiftEntryDialog dialog = new ShiftEntryDialog();
					dialog.open();
					if (dialog.isCanceled())
						return;
					Shift shift = dialog.getShift();
					tableModel.addItem(shift);
				} catch (Exception x) {
					MessageDialog.showError("An error has occured, please restart the application", x);
				}
			}
			
		});
		
		JButton editButton = new JButton("Edit");
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					Shift shift = (Shift) tableModel.getRowData(index);
					ShiftEntryDialog dialog = new ShiftEntryDialog();
					dialog.setShift(shift);
					dialog.open();
					if (dialog.isCanceled())
						return;

					tableModel.updateItem(index);
				} catch (Throwable x) {
					MessageDialog.showError("An error has occured, please restart the application", x);
				}
			}
			
		});
		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					if (ConfirmDeleteDialog.showMessage(ShiftExplorer.this, "Sure Want to Delete?", "Delete") == ConfirmDeleteDialog.YES) {
						User user = (User) tableModel.getRowData(index);
						UserDAO.getInstance().delete(user);
						tableModel.deleteItem(index);
					}
				} catch (Exception x) {
					MessageDialog.showError("An error has occured, please restart the application", x);
				}
			}
			
		});

		TransparentPanel panel = new TransparentPanel();
		panel.add(addButton);
		panel.add(editButton);
		//panel.add(deleteButton);
		add(panel, BorderLayout.SOUTH);
	}
	
	class ShiftTableModel extends ListTableModel {
		
		ShiftTableModel(List list){
			super(new String[] {"ID", "Name", "Start Time", "End Time"}, list);
		}
		

		public Object getValueAt(int rowIndex, int columnIndex) {
			Shift shift = (Shift) rows.get(rowIndex);
			
			switch(columnIndex) {
				case 0:
					return String.valueOf(shift.getId());
					
				case 1:
					return shift.getName();
					
				case 2:
					return ShiftUtil.buildShiftTimeRepresentation(shift.getStartTime());
					
				case 3:
					return ShiftUtil.buildShiftTimeRepresentation(shift.getEndTime());
			}
			return null;
		}
	}
}
