package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.model.MenuModifierGroupForm;

public class NewModifierGroupAction extends AbstractAction {

	public NewModifierGroupAction() {
		super();
	}

	public NewModifierGroupAction(String name) {
		super(name);
	}

	public NewModifierGroupAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		try {
			MenuModifierGroupForm editor = new MenuModifierGroupForm();
			BeanEditorDialog dialog = new BeanEditorDialog(editor);
			dialog.open();
		} catch (Exception x) {
			BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
		}
	}

}
