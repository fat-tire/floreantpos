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
package com.floreantpos.ui;

import java.awt.Frame;
import java.awt.LayoutManager;

import com.floreantpos.model.util.IllegalModelStateException;
import com.floreantpos.ui.dialog.BeanEditorDialog;

public abstract class BeanEditor<E> extends com.floreantpos.swing.TransparentPanel {
	protected E bean;
	protected BeanEditorDialog editorDialog;

	public BeanEditor(LayoutManager layout) {
		super(layout);
	}

	public BeanEditor() {
		super();
	}

	public void createNew() {

	}

	public void clearFields() {

	}
	
	public void edit() {
		
	}
	
	public boolean delete() {
		return false;
	}
	
	public void setFieldsEnable(boolean enable) {
		
	}

	public abstract boolean save();
	
	public void cancel() {}

	protected abstract void updateView();

	protected abstract boolean updateModel() throws IllegalModelStateException;

	public abstract String getDisplayText();

	public E getBean() {
		return bean;
	}

	public void setBean(E bean) {
		setBean(bean, true);
	}

	public void setBean(E bean, boolean updateView) {
		this.bean = bean;

		if (bean == null) {
			clearFields();
		}
		else if (updateView) {
			updateView();
		}
	}

	public Frame getParentFrame() {
		return (Frame) editorDialog.getOwner();
	}

	public BeanEditorDialog getEditorDialog() {
		return editorDialog;
	}

	public void setEditorDialog(BeanEditorDialog editorDialog) {
		this.editorDialog = editorDialog;
	}
	
	
}
