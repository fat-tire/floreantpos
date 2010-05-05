package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import com.floreantpos.main.Application;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.model.MenuCategoryForm;

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
			MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
		}
	}

}
