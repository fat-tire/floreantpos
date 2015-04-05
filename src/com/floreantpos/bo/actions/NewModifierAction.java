package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.model.MenuModifierForm;

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
			BeanEditorDialog dialog = new BeanEditorDialog(editor);
			dialog.open();
		} catch (Exception x) {
			BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
		}
	}

}
