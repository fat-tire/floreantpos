package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.model.MenuItemForm;

public class NewMenuItemAction extends AbstractAction {

	public NewMenuItemAction() {
		super();
	}

	public NewMenuItemAction(String name) {
		super(name);
	}

	public NewMenuItemAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		try {
			MenuItemForm editor = new MenuItemForm();
			BeanEditorDialog dialog = new BeanEditorDialog(editor);
			dialog.open();
		} catch (Exception x) {
			BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
		}
	}

}
