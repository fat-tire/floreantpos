package com.floreantpos.config.ui;

import java.awt.Component;

import javax.print.PrintService;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

public class PrintServiceComboRenderer extends DefaultListCellRenderer {
	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		JLabel listCellRendererComponent = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		PrintService printService = (PrintService) value;

		if (printService != null) {
			listCellRendererComponent.setText(printService.getName());
		}

		return listCellRendererComponent;
	}
}
