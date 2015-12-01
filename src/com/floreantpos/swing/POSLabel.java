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

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.floreantpos.config.UIConfig;

public class POSLabel extends JLabel {

	public POSLabel(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		
		setFont(UIConfig.largeFont);
	}

	public POSLabel(String text, int horizontalAlignment) {
		this(text,null, horizontalAlignment);
	}

	public POSLabel(String text) {
		this(text, null, JLabel.LEFT);
	}

	public POSLabel(Icon image, int horizontalAlignment) {
		this("", image, horizontalAlignment); //$NON-NLS-1$
	}

	public POSLabel(Icon image) {
		this("", image, SwingConstants.LEFT); //$NON-NLS-1$
	}

	public POSLabel() {
		this("", null, SwingConstants.LEFT); //$NON-NLS-1$
	}

}
