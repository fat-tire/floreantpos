package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.main.Application;
import com.floreantpos.model.TipsCashoutReport;
import com.floreantpos.print.PosPrintService;
import com.floreantpos.swing.PosButton;

public class TipsCashoutReportDialog extends POSDialog implements ActionListener {
	private final TipsCashoutReport report;

	public TipsCashoutReportDialog(TipsCashoutReport report, java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		this.report = report;
		
		setTitle(com.floreantpos.POSConstants.SERVER_TIPS_REPORT);
		
		JPanel topPanel = new JPanel(new MigLayout("","[fill]",""));
		topPanel.add(new JLabel("Server"));
		topPanel.add(new JLabel(": " + report.getServer()), "wrap");
		topPanel.add(new JLabel("From"));
		topPanel.add(new JLabel(": " + Application.formatDate(report.getFromDate())), "wrap");
		topPanel.add(new JLabel("To"));
		topPanel.add(new JLabel(": " + Application.formatDate(report.getToDate())), "wrap");
		topPanel.add(new JLabel("Time"));
		topPanel.add(new JLabel(": " + Application.formatDate(report.getReportTime())), "wrap");
		topPanel.add(new JLabel("Transaction Count"));
		topPanel.add(new JLabel(": " + (report.getDatas() == null ? "0" : String.valueOf(report.getDatas().size()))), "wrap");
		topPanel.add(new JLabel("Cash Tips"));
		topPanel.add(new JLabel(": " + Application.formatNumber(report.getCashTipsAmount())), "wrap");
		topPanel.add(new JLabel("Charged Tips"));
		topPanel.add(new JLabel(": " + Application.formatNumber(report.getChargedTipsAmount())), "wrap");
		topPanel.add(new JLabel("Tips Due"));
		topPanel.add(new JLabel(": " + report.getTipsDue()), "wrap");
		
		add(topPanel, BorderLayout.NORTH);
		
		JTable table = new JTable(new TipsCashoutReport.TipsCashoutReportTableModel(report.getDatas()));
		add(new JScrollPane(table));
		
		JPanel bottomPanel = new JPanel(new FlowLayout());
		PosButton print = new PosButton("PRINT");
		print.setPreferredSize(new Dimension(120,50));
		print.addActionListener(this);
		
		PosButton close = new PosButton("CLOSE");
		close.setPreferredSize(new Dimension(120,50));
		close.addActionListener(this);
		
		bottomPanel.add(print);
		bottomPanel.add(close);
		add(bottomPanel, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e) {
		if("CLOSE".equals(e.getActionCommand())) {
			dispose();
		}
		else if("PRINT".equals(e.getActionCommand())) {
			try {
				PosPrintService.printServerTipsReport(report);
			}catch (Exception x) {
				POSMessageDialog.showError(this, "Could not print\n" + x.getMessage());
			}
		}
	}
}
