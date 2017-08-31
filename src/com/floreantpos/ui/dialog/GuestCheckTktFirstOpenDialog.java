package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.config.AppProperties;
import com.floreantpos.model.GuestCheckPrint;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.GuestCheckPrintDAO;
import com.floreantpos.swing.BeanTableModel;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.TitlePanel;

public class GuestCheckTktFirstOpenDialog extends POSDialog {

	private BeanTableModel<Ticket> tableModel;

	public GuestCheckTktFirstOpenDialog() {
		initComponents();
	}

	private void initComponents() {
		setTitle(AppProperties.getAppName());
		setLayout(new MigLayout("fill"));
		JPanel mainPanel = new JPanel(new BorderLayout());
		add(mainPanel, "grow");

		TitlePanel titlePanel = new TitlePanel();
		titlePanel.setTitle("Time Since First Open");

		JTable table = new JTable();
		tableModel = new BeanTableModel<Ticket>(Ticket.class);
		tableModel.addColumn("Ticket Id", "id");
		tableModel.addColumn("Table #", "tableNumbers");
		tableModel.addColumn("Server", "owner");
		tableModel.addColumn("Create Time", "createDate");
		tableModel.addColumn("Elapsed Time", "diffWithCrntTime");

		table.setModel(tableModel);
		table.setRowHeight(60);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());

		table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (value instanceof Integer) {
					lbl.setHorizontalAlignment(JLabel.LEFT);
					return lbl;
				}

				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
		});

		table.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				if (value instanceof Date) {
					Date date = (Date) value;
					SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
					return super.getTableCellRendererComponent(table, dateFormat.format(date), isSelected, hasFocus, row, column);
				}
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
		});

		//		resizeColumnWidth(table);

		JPanel bottomPanel = new JPanel(new MigLayout("center"));
		PosButton btnDone = new PosButton("Done");
		btnDone.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		bottomPanel.add(btnDone);

		mainPanel.add(titlePanel, BorderLayout.NORTH);
		mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);
		mainPanel.add(bottomPanel, BorderLayout.SOUTH);

	}

	public void setData(List<Ticket> tickets) {
		if (tickets != null)
			tableModel.setRows(tickets);
	}

	public void resizeColumnWidth(JTable table) {
		final TableColumnModel columnModel = table.getColumnModel();
		for (int column = 0; column < table.getColumnCount(); column++) {
			columnModel.getColumn(column).setPreferredWidth((Integer) getColumnWidth().get(column));
		}
	}

	private List getColumnWidth() {
		List<Integer> columnWidth = new ArrayList();
		columnWidth.add(100);
		columnWidth.add(200);
		columnWidth.add(200);

		return columnWidth;
	}

}