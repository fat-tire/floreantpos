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
package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.model.MenuModifierForm;
import com.floreantpos.util.POSUtil;

public class NewModifierAction extends AbstractAction {

	public NewModifierAction() {
		super();
	}

	public NewModifierAction(String name) {
		super(name);
	}

	public NewModifierAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		try {
			MenuModifierForm editor = new MenuModifierForm();
			BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), editor);
			dialog.open();
		} catch (Exception x) {
			BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
		}
	}

}
