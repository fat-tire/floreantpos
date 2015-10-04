package com.floreantpos.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
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
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.Messages;
import com.floreantpos.model.KitchenTicket;
import com.floreantpos.model.KitchenTicket.KitchenTicketStatus;
import com.floreantpos.model.KitchenTicketItem;
import com.floreantpos.model.dao.KitchenTicketDAO;
import com.floreantpos.swing.ButtonColumn;
import com.floreantpos.swing.ListTableModel;
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
	private JScrollPane scrollPane;

	public KitchenTicketView(KitchenTicket ticket) {
		this.ticket = ticket;

		Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		setBorder(BorderFactory.createCompoundBorder(emptyBorder,
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), emptyBorder)));
		setLayout(new BorderLayout(5, 5));

		createHeader(ticket);

		createTable(ticket);

		createButtonPanel();

		statusSelector = new KitchenTicketStatusSelector((Frame) SwingUtilities.getWindowAncestor(this));
		statusSelector.pack();

		setPreferredSize(new Dimension(400, 200));

		timerWatch.start();

		addAncestorListener(new AncestorListener() {
			@Override
			public void ancestorRemoved(AncestorEvent event) {
				timerWatch.stop();
			}

			@Override
			public void ancestorMoved(AncestorEvent event) {
			}

			@Override
			public void ancestorAdded(AncestorEvent event) {
			}
		});
	}

	public void stopTimer() {
		timerWatch.stop();
	}

	private void createHeader(KitchenTicket ticket) {
		String printerName = ticket.getPrinters().toString();

		String ticketInfo = "Ticket# " + ticket.getTicketId() + "-" + ticket.getId() + " " + printerName + ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		if (ticket.getTableNumbers() != null && ticket.getTableNumbers().size() > 0) {
			ticketInfo += "<br/>Table " + ticket.getTableNumbers(); //$NON-NLS-1$
		}
		ticketId.setText("<html>" + ticketInfo + "</html>"); //$NON-NLS-1$ //$NON-NLS-2$
		ticketId.setFont(ticketId.getFont().deriveFont(Font.BOLD));

		timerWatch = new TimerWatch(ticket.getCreateDate());

		JPanel headerPanel = new JPanel(new MigLayout("fill", "[fill, grow 100][]", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		headerPanel.add(ticketId, "grow 100"); //$NON-NLS-1$
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

				if (ticketItem != null && ticketItem.getStatus() != null) {
					if (ticketItem.getStatus().equalsIgnoreCase(KitchenTicketStatus.DONE.name())) {
						rendererComponent.setBackground(Color.green);
					}
					else if (ticketItem.getStatus().equalsIgnoreCase(KitchenTicketStatus.VOID.name())) {
						rendererComponent.setBackground(Color.red);
					}
					else {
						rendererComponent.setBackground(Color.white);
					}
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
				if(!ticketItem.isCookable()) {
					return;
				}
				statusSelector.setTicketItem(ticketItem);
				statusSelector.setLocationRelativeTo(KitchenTicketView.this);
				statusSelector.setVisible(true);
				table.repaint();
			}
		};

		new ButtonColumn(table, action, 2);
		scrollPane = new JScrollPane(table);
		add(scrollPane);
	}

	private void createButtonPanel() {
		JPanel buttonPanel = new JPanel(new GridLayout(1, 0, 5, 5));

		PosButton btnDone = new PosButton(Messages.getString("KitchenTicketView.11")); //$NON-NLS-1$
		btnDone.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeTicket(KitchenTicketStatus.DONE);
			}
		});

		buttonPanel.add(btnDone);

		PosButton btnVoid = new PosButton(Messages.getString("KitchenTicketView.12")); //$NON-NLS-1$
		btnVoid.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeTicket(KitchenTicketStatus.VOID);
			}
		});
		buttonPanel.add(btnVoid);

		//		PosButton btnPrint = new PosButton("PRINT");
		//		btnPrint.addActionListener(new ActionListener() {
		//			@Override
		//			public void actionPerformed(ActionEvent e) {
		//				//KitchenDisplay.instance.removeTicket(KitchenTicketView.this);
		//			}
		//		});
		//		buttonPanel.add(btnPrint);
		//		
		//		PosButton btnUp = new PosButton("UP");
		//		btnUp.addActionListener(new ActionListener() {
		//			@Override
		//			public void actionPerformed(ActionEvent e) {
		//				JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
		//				scrollBar.setValue(scrollBar.getValue() - 50);
		//			}
		//		});
		//		buttonPanel.add(btnUp);
		//		
		//		PosButton btnDown = new PosButton("DOWN");
		//		btnDown.addActionListener(new ActionListener() {
		//			@Override
		//			public void actionPerformed(ActionEvent e) {
		//				JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
		//				scrollBar.setValue(scrollBar.getValue() + 50);
		//			}
		//		});
		//		buttonPanel.add(btnDown);

		add(buttonPanel, BorderLayout.SOUTH);
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
			super(new String[] { Messages.getString("KitchenTicketView.13"), Messages.getString("KitchenTicketView.14"), "" }, list); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
			stopTimer();

			int option = JOptionPane.showConfirmDialog(KitchenTicketView.this, Messages.getString("KitchenTicketView.16") + status.name() + "?", Messages.getString("KitchenTicketView.18"), JOptionPane.YES_NO_OPTION); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			if (option != JOptionPane.YES_OPTION) {
				return;
			}

			ticket.setStatus(status.name());
			ticket.setClosingDate(new Date());

			KitchenTicketDAO.getInstance().saveOrUpdate(ticket);
			Container parent = this.getParent();
			parent.remove(this);
			parent.revalidate();
			parent.repaint();
		} catch (Exception e) {
			POSMessageDialog.showError(KitchenTicketView.this, e.getMessage(), e);
		}
	}
}
