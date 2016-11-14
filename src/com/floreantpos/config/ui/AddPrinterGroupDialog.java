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
package com.floreantpos.config.ui;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.model.PosPrinters;
import com.floreantpos.model.Printer;
import com.floreantpos.model.PrinterGroup;
import com.floreantpos.swing.CheckBoxList;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;

public class AddPrinterGroupDialog extends POSDialog {
	private FixedLengthTextField tfName = new FixedLengthTextField(60);
	private CheckBoxList<Printer> printerList;
	private JCheckBox chkDefault;

	private PrinterGroup printerGroup;
	private List<Printer> printers;

	public AddPrinterGroupDialog() throws HeadlessException {
		super(POSUtil.getBackOfficeWindow(), true);
		setTitle(Messages.getString("AddPrinterGroupDialog.0")); //$NON-NLS-1$

		init();

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
	}

	private void init() {
		JPanel contentPane = (JPanel) getContentPane();
		contentPane.setLayout(new MigLayout("", "[][grow]", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		add(new JLabel(Messages.getString("AddPrinterGroupDialog.4"))); //$NON-NLS-1$
		add(tfName, "grow, wrap"); //$NON-NLS-1$

		chkDefault = new JCheckBox(Messages.getString("AddPrinterGroupDialog.1")); //$NON-NLS-1$

		add(new JLabel(), "grow"); //$NON-NLS-1$
		add(chkDefault, "wrap"); //$NON-NLS-1$

		PosPrinters printersKitchen = PosPrinters.load();
		printers = printersKitchen.getKitchenPrinters();
		printerList = new CheckBoxList(new Vector<Printer>(printers));

		JPanel listPanel = new JPanel(new BorderLayout());
		listPanel.setBorder(new TitledBorder(Messages.getString("AddPrinterGroupDialog.6"))); //$NON-NLS-1$
		listPanel.add(new JScrollPane(printerList));

		add(listPanel, "newline, span 2, grow"); //$NON-NLS-1$

		JPanel panel = new JPanel();
		contentPane.add(panel, "cell 0 4 3 1,grow"); //$NON-NLS-1$

		JButton btnOk = new JButton(Messages.getString("AddPrinterGroupDialog.9")); //$NON-NLS-1$
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (StringUtils.isEmpty(tfName.getText())) {
					POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("AddPrinterGroupDialog.10")); //$NON-NLS-1$
					return;
				}

				List checkedValues = printerList.getCheckedValues();
				if (checkedValues == null || checkedValues.size() == 0) {
					POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("AddPrinterGroupDialog.11")); //$NON-NLS-1$
					return;
				}

				setCanceled(false);
				dispose();
			}
		});
		panel.add(btnOk);

		JButton btnCancel = new JButton(Messages.getString("AddPrinterGroupDialog.12")); //$NON-NLS-1$
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});
		panel.add(btnCancel);
	}

	public PrinterGroup getPrinterGroup() {
		if (this.printerGroup == null) {
			printerGroup = new PrinterGroup();
		}
		printerGroup.setIsDefault(chkDefault.isSelected());
		printerGroup.setName(tfName.getText());

		List checkedValues = printerList.getCheckedValues();
		if (checkedValues != null) {
			List<String> names = new ArrayList<String>();

			for (Object object : checkedValues) {
				Printer p = (Printer) object;
				names.add(p.getVirtualPrinter().getName());
			}

			printerGroup.setPrinterNames(names);
		}
		return printerGroup;
	}

	public void setPrinterGroup(PrinterGroup group) {
		this.printerGroup = group;
		tfName.setText(group.getName());

		chkDefault.setSelected(group.isIsDefault());

		Vector<Printer> selectedPrinters = new Vector<Printer>();
		for (Printer printer : printers) {
			if (printerGroup.getPrinterNames().contains(printer.getVirtualPrinter().getName())) {
				selectedPrinters.add(printer);
			}
		}
		printerList.selectItems(selectedPrinters);

	}
}
