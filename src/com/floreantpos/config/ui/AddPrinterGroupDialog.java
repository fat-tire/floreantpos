package com.floreantpos.config.ui;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.gjt.sp.jedit.gui.JCheckBoxList;

import com.floreantpos.main.Application;
import com.floreantpos.model.Printer;
import com.floreantpos.model.PrinterGroup;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;

public class AddPrinterGroupDialog extends POSDialog {
	private FixedLengthTextField tfName = new FixedLengthTextField(60);
	JCheckBoxList printerList;

	public AddPrinterGroupDialog() throws HeadlessException {
		super(POSUtil.getBackOfficeWindow(), true);
		setTitle("Add/Edit Printer Group");
		
		init();
		
		//setMinimumSize(new Dimension(400, 200));
		//setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
	}

	public void init() {
		JPanel contentPane = (JPanel) getContentPane();
		contentPane.setLayout(new MigLayout("", "[][grow]", ""));
		
		add(new JLabel("Group name"));
		add(tfName, "grow");
		
		List<Printer> printers = Application.getPrinters().getKitchenPrinters();
		printerList = new JCheckBoxList(new Vector<Printer>(printers));
		
		JPanel listPanel = new JPanel(new BorderLayout());
		listPanel.setBorder(new TitledBorder("Printers"));
		listPanel.add(new JScrollPane(printerList));
		
		add(listPanel, "newline, span 2, grow");
		
		JPanel panel = new JPanel();
		contentPane.add(panel, "cell 0 4 3 1,grow");
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(StringUtils.isEmpty(tfName.getText())) {
					POSMessageDialog.showError(Application.getPosWindow(), "Name is empty");
					return;
				}
				
				Object[] checkedValues = printerList.getCheckedValues();
				if(checkedValues == null || checkedValues.length == 0) {
					POSMessageDialog.showError(Application.getPosWindow(), "Please select at least one printer");
					return;
				}
				
				setCanceled(false);
				dispose();
			}
		});
		panel.add(btnOk);
		
		JButton btnCancel = new JButton("CANCEL");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});
		panel.add(btnCancel);
	}

	public PrinterGroup getPrinterGroup() {
		PrinterGroup group = new PrinterGroup();
		group.setName(tfName.getText());
		
		Object[] checkedValues = printerList.getCheckedValues();
		if(checkedValues != null) {
			List<String> names = new ArrayList<String>();
			
			for (Object object : checkedValues) {
				Printer p = (Printer) object;
				names.add(p.getVirtualPrinter().getName());
			}
			
			group.setPrinterNames(names);
		}
		
		return group;
	}
}
