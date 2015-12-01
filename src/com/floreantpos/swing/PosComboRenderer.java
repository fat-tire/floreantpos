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
package com.floreantpos.swing;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JList;

import org.jdesktop.swingx.renderer.DefaultListRenderer;

public class PosComboRenderer extends DefaultListRenderer {
	private boolean enableDefaultValueShowing = true;
	
	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		if(value == null && enableDefaultValueShowing) {
			value = "ALL"; //$NON-NLS-1$
		}
		
		Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		Dimension size = component.getPreferredSize();
		size.height = 40;
		component.setPreferredSize(size);
		
		return component;
	}

	public boolean isEnableDefaultValueShowing() {
		return enableDefaultValueShowing;
	}

	public void setEnableDefaultValueShowing(boolean enableDefaultValueShowing) {
		this.enableDefaultValueShowing = enableDefaultValueShowing;
	}
}
