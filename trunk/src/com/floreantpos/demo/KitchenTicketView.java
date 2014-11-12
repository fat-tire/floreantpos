package com.floreantpos.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.model.KitchenTicket;
import com.floreantpos.model.KitchenTicket.KitchenTicketStatus;
import com.floreantpos.model.KitchenTicketItem;
import com.floreantpos.model.VirtualPrinter;
import com.floreantpos.model.dao.KitchenTicketDAO;
import com.floreantpos.swing.ButtonColumn;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.TimerWatch;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class KitchenTicketView extends JPanel {
	KitchenTicket ticket;
	JLabel ticketId = new JLabel();
	KitchenTicketTableModel tableModel;
	JTable table;
	KitchenTicketStatusSelector statusSelector;
	private TimerWatch timerWatch;

	public KitchenTicketView(KitchenDisplay display, KitchenTicket ticket) {
		this.ticket = ticket;

		Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		setBorder(BorderFactory.createCompoundBorder(emptyBorder,
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), emptyBorder)));
		setLayout(new BorderLayout(5, 5));

		createHeader(ticket);

		createTable(ticket);

		createButtonPanel();

		statusSelector = new KitchenTicketStatusSelector((Frame) SwingUtilities.windowForComponent(this));
		statusSelector.pack();

		setPreferredSize(new Dimension(400, 200));

		timerWatch.start();
	}

	public void stopTimer() {
		timerWatch.stop();
	}

	private void createHeader(KitchenTicket ticket) {
		VirtualPrinter virtualPrinter = ticket.getPrinter().getVirtualPrinter();
		String printerName = virtualPrinter == null ? "" : virtualPrinter.getName();

		ticketId.setText("Ticket# " + ticket.getTicketId() + "-" + ticket.getId() + " [" + printerName + "]");
		ticketId.setFont(ticketId.getFont().deriveFont(Font.BOLD));

		timerWatch = new TimerWatch();

		JPanel headerPanel = new JPanel(new MigLayout("fill", "[fill, grow 100][]", ""));
		headerPanel.add(ticketId, "grow 100");
		headerPanel.add(timerWatch);

		add(headerPanel, BorderLayout.NORTH);
	}

	private void createTable(KitchenTicket ticket) {
		tableModel = new KitchenTicketTableModel(ticket.getTicketItems());
		table = new JTable(tableModel);
		table.setRowSelectionAllowed(false);
		table.setCellSelectionEnabled(false);
		table.setRowHeight(50);
		table.setTableHeader(null);
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				Component rendererComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				KitchenTicketItem ticketItem = tableModel.getRowData(row);
				if (ticketItem.getStatus().equalsIgnoreCase(KitchenTicketStatus.DONE.name())) {
					rendererComponent.setBackground(Color.green);
				}
				else if (ticketItem.getStatus().equalsIgnoreCase(KitchenTicketStatus.VOID.name())) {
					rendererComponent.setBackground(Color.red);
				}
				else {
					rendererComponent.setBackground(Color.white);
				}

				return rendererComponent;
			}
		});
		resizeTableColumns();

		AbstractAction action = new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int row = Integer.parseInt(e.getActionCommand());
				KitchenTicketItem ticketItem = tableModel.getRowData(row);
				statusSelector.setTicketItem(ticketItem);
				statusSelector.setLocationRelativeTo(KitchenTicketView.this);
				statusSelector.setVisible(true);
				table.repaint();
			}
		};

		new ButtonColumn(table, action, 2);
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane);
	}

	public void refreshTicket() {
		try {
			KitchenTicketDAO.getInstance().refresh(this.ticket);
			tableModel.setRows(this.ticket.getTicketItems());
		} catch (Exception e) {
			POSMessageDialog.showError(KitchenTicketView.this, e.getMessage(), e);
		}
	}

	private void createButtonPanel() {
		JPanel donePanel = new JPanel(new GridLayout(1, 0, 10, 10));

		PosButton btnDone = new PosButton("DONE");
		btnDone.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeTicket(KitchenTicketStatus.DONE);
			}
		});

		donePanel.add(btnDone);

		PosButton btnVoid = new PosButton("VOID");
		btnVoid.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeTicket(KitchenTicketStatus.VOID);
			}
		});
		donePanel.add(btnVoid);

		PosButton btnPrint = new PosButton("PRINT");
		btnPrint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//KitchenDisplay.instance.removeTicket(KitchenTicketView.this);
			}
		});
		donePanel.add(btnPrint);

		add(donePanel, BorderLayout.SOUTH);
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
			super(new String[] { "Name", "Qty", "" }, list);
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			if (columnIndex == 2) {
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

	public KitchenTicket getTicket() {
		return ticket;
	}

	public void setTicket(KitchenTicket ticket) {
		this.ticket = ticket;
	}

	private void closeTicket(KitchenTicketStatus status) {
		try {

			int option = JOptionPane.showConfirmDialog(KitchenTicketView.this, "Confirm " + status.name() + "?", "Confirm", JOptionPane.YES_NO_OPTION);
			if (option != JOptionPane.YES_OPTION) {
				return;
			}

			ticket.setStatus(status.name());
			ticket.setClosingDate(new Date());

			KitchenTicketDAO.getInstance().saveOrUpdate(ticket);
			KitchenDisplay.instance.removeTicket(KitchenTicketView.this);
		} catch (Exception e) {
			POSMessageDialog.showError(KitchenTicketView.this, e.getMessage(), e);
		}
	}
}
