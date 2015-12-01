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

import java.awt.LayoutManager;

import javax.swing.JPanel;




public class TransparentPanel extends JPanel {
	
	public TransparentPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		
		setOpaque(false);
	}

	public TransparentPanel(LayoutManager layout) {
		super(layout);
		
		setOpaque(false);
	}

	public TransparentPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		
		setOpaque(false);
	}

	public TransparentPanel() {
		super();
		setOpaque(false);
	}
}
