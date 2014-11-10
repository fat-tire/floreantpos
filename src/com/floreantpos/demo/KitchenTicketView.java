package com.floreantpos.demo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.model.KitchenTicket;
import com.floreantpos.model.KitchenTicketItem;
import com.floreantpos.swing.ButtonColumn;
import com.floreantpos.swing.PosButton;

public class KitchenTicketView extends JPanel {
	
	JLabel ticketId = new JLabel();
	KitchenTicketTableModel tableModel;
	JTable table;
	KitchenTicketStatusSelector statusSelector;
	
	public KitchenTicketView(KitchenDisplay display, KitchenTicket ticket) {
		
//		setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		setLayout(new BorderLayout(5, 5));
		
		ticketId.setText("Ticket# " + ticket.getTicketId() + " [" + ticket.getPrinter().getVirtualPrinter().getName() + "]");
		ticketId.setFont(ticketId.getFont().deriveFont(Font.BOLD));
		ticketId.setHorizontalAlignment(JLabel.CENTER);
		add(ticketId, BorderLayout.NORTH);
		
		tableModel = new KitchenTicketTableModel(ticket.getTicketItems());
		table = new JTable(tableModel);
		table.setRowSelectionAllowed(false);
		table.setCellSelectionEnabled(false);
		table.setRowHeight(50);
		table.getTableHeader().setVisible(false);
		resizeTableColumns();
		
		AbstractAction action = new AbstractAction("DONE") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = Integer.parseInt(e.getActionCommand());
				KitchenTicketItem ticketItem = tableModel.getRowData(row);
				statusSelector.setTicketItem(ticketItem);
				statusSelector.setVisible(true);
			}
		};
		
		new ButtonColumn(table, action, 2);
		add(new JScrollPane(table));
		
		PosButton btnDone = new PosButton("DONE");
		btnDone.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				KitchenDisplay.instance.removeTicket(KitchenTicketView.this);
			}
		});
		JPanel donePanel = new JPanel();
		donePanel.add(btnDone);
		add(donePanel, BorderLayout.SOUTH);
		
		statusSelector = new KitchenTicketStatusSelector((Frame) SwingUtilities.windowForComponent(this));
		statusSelector.setLocationRelativeTo(KitchenTicketView.this);
		statusSelector.pack();
		
		setPreferredSize(new Dimension(400, 200));
	}
	
	private void resizeTableColumns() {
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		setColumnWidth(1, 40);
		setColumnWidth(2, 100);
	}

	private void setColumnWidth(int columnNumber, int width) {
		TableColumn column = table.getColumnModel().getColumn(columnNumber);

		column.setPreferredWidth(width);
		column.setMaxWidth(width);
		column.setMinWidth(width);
	}

	class KitchenTicketTableModel extends ListTableModel<KitchenTicketItem> {
		
		KitchenTicketTableModel(List<KitchenTicketItem> list) {
			super(new String[] {"Name", "Qty", ""}, list);
		}
		
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			if(columnIndex == 2) {
				return true;
			}
			
			return false;
		}
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			KitchenTicketItem ticketItem = getRowData(rowIndex);
			
			switch (columnIndex) {
				case 0:
					return ticketItem.getMenuItemName();
					
				case 1:
					return String.valueOf(ticketItem.getQuantity());
					
				case 2:
					return ticketItem.getStatus();
			}
			
			return null;
		}
		
	}
}
