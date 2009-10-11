package com.mdss.pos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import com.mdss.pos.main.Application;
import com.mdss.pos.swing.MessageDialog;
import com.mdss.pos.ui.dialog.BeanEditorDialog;
import com.mdss.pos.ui.model.MenuItemForm;

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
			BeanEditorDialog dialog = new BeanEditorDialog(editor, Application.getInstance().getBackOfficeWindow(), true);
			dialog.open();
		} catch (Exception x) {
			MessageDialog.showError("An error has occured, please restart the application", x);
		}
	}

}
