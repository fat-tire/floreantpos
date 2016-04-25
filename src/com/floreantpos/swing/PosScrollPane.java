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

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class PosScrollPane extends JScrollPane {

	public PosScrollPane() {
		JScrollBar scrollBar = getVerticalScrollBar();
		if (scrollBar != null) {
			scrollBar.setPreferredSize(PosUIManager.getSize(40,40));
		}
	}

	public PosScrollPane(Component view) {
		super(view);

		JScrollBar scrollBar = getVerticalScrollBar();
		if (scrollBar != null) {
			scrollBar.setPreferredSize(PosUIManager.getSize(40,40));
		}
	}

	public PosScrollPane(int vsbPolicy, int hsbPolicy) {
		super(vsbPolicy, hsbPolicy);

		JScrollBar scrollBar = getVerticalScrollBar();
		if (scrollBar != null) {
			scrollBar.setPreferredSize(PosUIManager.getSize(40,40));
		}
	}

	public PosScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
		super(view, vsbPolicy, hsbPolicy);

		JScrollBar scrollBar = getVerticalScrollBar();
		if (scrollBar != null) {
			scrollBar.setPreferredSize(PosUIManager.getSize(40,40));
		}
	}

}
