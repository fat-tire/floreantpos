package com.mdss.pos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import com.mdss.pos.main.Application;
import com.mdss.pos.ui.dialog.BeanEditorDialog;
import com.mdss.pos.ui.model.MenuGroupForm;

public class NewMenuGroupAction extends AbstractAction {

	public NewMenuGroupAction() {
		super();
	}

	public NewMenuGroupAction(String name) {
		super(name);
	}

	public NewMenuGroupAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		MenuGroupForm editor = new MenuGroupForm();
		BeanEditorDialog dialog = new BeanEditorDialog(editor, Application.getInstance().getBackOfficeWindow(), true);
		dialog.open();
	}

}
