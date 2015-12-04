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

import java.awt.BorderLayout;

import javax.swing.JPanel;

import com.floreantpos.ui.TitlePanel;

public class TitledView extends JPanel {
	private TitlePanel titlePanel;
	private JPanel contentPane;

	public TitledView() {
		this(""); //$NON-NLS-1$
	}

	public TitledView(String title) {
		this.titlePanel = new TitlePanel();
		this.contentPane = new JPanel();
		
		this.setLayout(new BorderLayout());

		this.add(titlePanel, BorderLayout.NORTH);
		this.add(contentPane);
		
		setTitle(title);
	}
	
	public void setTitle(String title) {
		this.titlePanel.setTitle(title);
	}
	
	public String getTitle() {
		return this.titlePanel.getTitle();
	}
	
	public void setTitlePaneVisible(boolean visible) {
		this.titlePanel.setVisible(visible);
	}
	
	public boolean isTitlePaneVisible() {
		return this.titlePanel.isVisible();
	}
	
	public JPanel getContentPane() {
		return this.contentPane;
	}
	
}
