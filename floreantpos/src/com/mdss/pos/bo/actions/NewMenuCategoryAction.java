package com.mdss.pos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import com.mdss.pos.main.Application;
import com.mdss.pos.swing.MessageDialog;
import com.mdss.pos.ui.dialog.BeanEditorDialog;
import com.mdss.pos.ui.model.MenuCategoryForm;

public class NewMenuCategoryAction extends AbstractAction {

	public NewMenuCategoryAction() {
		super();
	}

	public NewMenuCategoryAction(String name) {
		super(name);
	}

	public NewMenuCategoryAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		try {
			MenuCategoryForm editor = new MenuCategoryForm();
			BeanEditorDialog dialog = new BeanEditorDialog(editor, Application.getInstance().getBackOfficeWindow(), true);
			dialog.open();
		} catch (Exception x) {
			MessageDialog.showError("An error has occured, please restart the application", x);
		}
	}

}
