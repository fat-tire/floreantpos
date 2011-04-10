package com.floreantpos.ui;

import java.awt.Frame;
import java.awt.LayoutManager;

import com.floreantpos.model.util.IllegalModelStateException;
import com.floreantpos.ui.dialog.BeanEditorDialog;

public abstract class BeanEditor extends com.floreantpos.swing.TransparentPanel {
	protected Object bean;
	protected BeanEditorDialog editorDialog;
	
	public BeanEditor(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	public BeanEditor(LayoutManager layout) {
		super(layout);
	}

	public BeanEditor(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	public BeanEditor() {
		super();
	}
	
	public abstract boolean save();
	public abstract void dispose();
	protected abstract void updateView();
	protected abstract boolean updateModel() throws IllegalModelStateException;
	public abstract String getDisplayText();

	public Object getBean() {
		return bean;
	}

	public void setBean(Object bean) {
		this.bean = bean;
		updateView();
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
