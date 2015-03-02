package com.floreantpos.swing;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JList;

import org.jdesktop.swingx.renderer.DefaultListRenderer;

public class PosComboRenderer extends DefaultListRenderer {
	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		if(value == null) {
			value = "ALL";
		}
		
		Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		Dimension size = component.getPreferredSize();
		size.height = 40;
		component.setPreferredSize(size);
		
		return component;
	}
}
