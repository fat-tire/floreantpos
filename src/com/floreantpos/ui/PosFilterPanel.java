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
package com.floreantpos.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXCollapsiblePane;

import com.floreantpos.POSConstants;
import com.floreantpos.swing.POSToggleButton;

public class PosFilterPanel extends JXCollapsiblePane {
	private List<String> filters;
	private PosFilterListener listener;

	public PosFilterPanel(List<String> filters) {
		this.filters = filters;
		getContentPane().setLayout(new MigLayout("inset 0", "grow", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		setCollapsed(true);
		createFilterPanel();
	}

	public void addFilterListener(PosFilterListener listener) {
		this.listener = listener;
	}

	private void createFilterPanel() {
		JPanel panel = new JPanel(new MigLayout("inset 0 0 5 0", "sg, fill", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		ButtonGroup group = new ButtonGroup();

		for (String filter : filters) {
			FilterButton btnFilter = new FilterButton(filter);
			if (filter.equals(POSConstants.ALL)) {
				btnFilter.setSelected(true);
			}
			group.add(btnFilter);
			panel.add(btnFilter);
		}

		getContentPane().add(panel);
	}

	private class FilterButton extends POSToggleButton implements ActionListener {

		public FilterButton(String name) {
			setText(name);
			addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			setSelected(true);
			String actionCommand = e.getActionCommand();
			listener.filterSelected(actionCommand);
		}
	}
}
