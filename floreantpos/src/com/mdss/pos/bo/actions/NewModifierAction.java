package com.mdss.pos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import com.mdss.pos.main.Application;
import com.mdss.pos.swing.MessageDialog;
import com.mdss.pos.ui.dialog.BeanEditorDialog;
import com.mdss.pos.ui.model.MenuModifierForm;

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
			BeanEditorDialog dialog = new BeanEditorDialog(editor, Application.getInstance().getBackOfficeWindow(), true);
			dialog.open();
		} catch (Exception x) {
			MessageDialog.showError("An error has occured, please restart the application", x);
		}
	}

}
