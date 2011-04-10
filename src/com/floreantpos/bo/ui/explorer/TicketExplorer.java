package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.JXTable;

import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;

public class TicketExplorer extends TransparentPanel {
	private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy, h:m a");
	DecimalFormat numberFormat = new DecimalFormat("0.00");
	
	JXTable explorerTable;
	private List<Ticket> tickets;
	
	public TicketExplorer() {
		setLayout(new BorderLayout());
		
		explorerTable = new JXTable(new TicketExplorerTableModel()) {
			PosTableRenderer renderer = new PosTableRenderer();
			
			@Override
			public TableCellRenderer getCellRenderer(int row, int column) {
				return renderer;
			}
		};
		explorerTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		explorerTable.setColumnControlVisible(true);
		add(new JScrollPane(explorerTable));
	}
	
	public void init() {
		try {
			TicketDAO dao = new TicketDAO();
			tickets = dao.findAll();
			explorerTable.packAll();
			explorerTable.repaint();
		} catch (Exception e) {
			MessageDialog.showError(e);
		}
	}
	
	class TicketExplorerTableModel extends AbstractTableModel {
		String[] columnNames = {com.floreantpos.POSConstants.ID, com.floreantpos.POSConstants.CREATED_BY, com.floreantpos.POSConstants.CREATE_TIME, com.floreantpos.POSConstants.SETTLE_TIME, 
								com.floreantpos.POSConstants.SUBTOTAL, com.floreantpos.POSConstants.DISCOUNT, com.floreantpos.POSConstants.TAX, com.floreantpos.POSConstants.TOTAL, 
								com.floreantpos.POSConstants.PAID, com.floreantpos.POSConstants.VOID
		};
		
		public int getRowCount() {
			if(tickets == null) {
				return 0;
			}
			return tickets.size();
		}

		public int getColumnCount() {
			return 11;
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
			if(tickets == null)
				return "";
			
			Ticket ticket = tickets.get(rowIndex);
			
			switch(columnIndex) {
				case 0:
					return String.valueOf(ticket.getId());
					
				case 1:
					return ticket.getOwner().toString();
					
				case 2:
					return dateFormat.format(ticket.getCreateDate());
					
				case 3:
					if(ticket.getClosingDate() != null) {
						return dateFormat.format(ticket.getClosingDate());
					}
					return "";
					
				case 4:
					return Double.valueOf(ticket.getSubtotalAmount());
					
				case 5:
					return Double.valueOf(ticket.getDiscountAmount());
					
				case 6:
					return Double.valueOf(ticket.getTaxAmount());
					
				case 7:
					return Double.valueOf(ticket.getTotalAmount());
					
				case 8:
					return Boolean.valueOf(ticket.isPaid());
					
				case 9:
					return Boolean.valueOf(ticket.isVoided());
			}
			return null;
		}
	}
}
