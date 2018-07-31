/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
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

import com.floreantpos.Messages;
import com.floreantpos.PosException;
import com.floreantpos.PosLog;
import com.floreantpos.main.Application;
import com.floreantpos.model.TipsCashoutReport;
import com.floreantpos.model.TipsCashoutReportTableModel;
import com.floreantpos.print.PosPrintService;
import com.floreantpos.swing.PosButton;
import com.floreantpos.util.NumberUtil;
import com.floreantpos.util.POSUtil;

public class TipsCashoutReportDialog extends POSDialog implements ActionListener {
	private final TipsCashoutReport report;

	public TipsCashoutReportDialog(TipsCashoutReport report) {
		super();
		this.report = report;

		setTitle(com.floreantpos.POSConstants.SERVER_TIPS_REPORT);

		JPanel topPanel = new JPanel(new MigLayout("", "[fill]", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		topPanel.add(new JLabel(Messages.getString("TipsCashoutReportDialog.0"))); //$NON-NLS-1$
		topPanel.add(new JLabel(": " + report.getServer()), "wrap"); //$NON-NLS-1$ //$NON-NLS-2$
		topPanel.add(new JLabel(Messages.getString("TipsCashoutReportDialog.1"))); //$NON-NLS-1$
		topPanel.add(new JLabel(": " + Application.formatDate(report.getFromDate())), "wrap"); //$NON-NLS-1$ //$NON-NLS-2$
		topPanel.add(new JLabel(Messages.getString("TipsCashoutReportDialog.2"))); //$NON-NLS-1$
		topPanel.add(new JLabel(": " + Application.formatDate(report.getToDate())), "wrap"); //$NON-NLS-1$ //$NON-NLS-2$
		topPanel.add(new JLabel(Messages.getString("TipsCashoutReportDialog.3"))); //$NON-NLS-1$
		topPanel.add(new JLabel(": " + Application.formatDate(report.getReportTime())), "wrap"); //$NON-NLS-1$ //$NON-NLS-2$
		topPanel.add(new JLabel(Messages.getString("TipsCashoutReportDialog.4"))); //$NON-NLS-1$
		topPanel.add(new JLabel(": " + (report.getDatas() == null ? "0" : String.valueOf(report.getDatas().size()))), "wrap"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		topPanel.add(new JLabel(Messages.getString("TipsCashoutReportDialog.5"))); //$NON-NLS-1$
		topPanel.add(new JLabel(": " + NumberUtil.formatNumber(report.getCashTipsAmount())), "wrap"); //$NON-NLS-1$ //$NON-NLS-2$
		topPanel.add(new JLabel(Messages.getString("TipsCashoutReportDialog.6"))); //$NON-NLS-1$
		topPanel.add(new JLabel(": " + NumberUtil.formatNumber(report.getChargedTipsAmount())), "wrap"); //$NON-NLS-1$ //$NON-NLS-2$
		topPanel.add(new JLabel(Messages.getString("TipsCashoutReportDialog.7"))); //$NON-NLS-1$
		topPanel.add(new JLabel(": " + report.getTipsDue()), "wrap"); //$NON-NLS-1$ //$NON-NLS-2$

		add(topPanel, BorderLayout.NORTH);

		JPanel contentPanel = new JPanel(new MigLayout("fill"));
		JTable table = new JTable(new TipsCashoutReportTableModel(report.getDatas()));
		contentPanel.add(new JScrollPane(table));
		add(contentPanel);

		JPanel bottomPanel = new JPanel(new FlowLayout());
		PosButton print = new PosButton(Messages.getString("TipsCashoutReportDialog.28")); //$NON-NLS-1$
		print.setPreferredSize(new Dimension(120, 50));
		print.addActionListener(this);

		PosButton close = new PosButton(Messages.getString("TipsCashoutReportDialog.29")); //$NON-NLS-1$
		close.setPreferredSize(new Dimension(120, 50));
		close.addActionListener(this);

		bottomPanel.add(print);
		bottomPanel.add(close);
		add(bottomPanel, BorderLayout.SOUTH);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e) {
		if (Messages.getString("TipsCashoutReportDialog.30").equals(e.getActionCommand())) { //$NON-NLS-1$
			dispose();
		}
		else if (Messages.getString("TipsCashoutReportDialog.31").equals(e.getActionCommand())) { //$NON-NLS-1$
			try {
				PosPrintService.printServerTipsReport(report);
			} catch (PosException exception) {
				POSMessageDialog.showError(POSUtil.getFocusedWindow(), exception.getMessage());
			} catch (Exception x) {
				PosLog.error(getClass(), x);
				POSMessageDialog.showError(this, Messages.getString("TipsCashoutReportDialog.32") + x.getMessage()); //$NON-NLS-1$
			}
		}
	}
}
