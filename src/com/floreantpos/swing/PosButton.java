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
package com.floreantpos.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.floreantpos.POSConstants;
import com.floreantpos.actions.ActionCommand;
import com.floreantpos.actions.PosAction;

public class PosButton extends JButton {
	public static Border border = new LineBorder(Color.BLACK, 1);
	static Insets margin = new Insets(1, 5, 1, 5);

	static POSButtonUI ui = new POSButtonUI();

	static {
		UIManager.put("PosButtonUI", "com.floreantpos.swing.POSButtonUI"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public PosButton() {
		this(""); //$NON-NLS-1$
	}

	public PosButton(String text) {
		super(text);
		//setFont(UIConfig.getButtonFont());

		setFocusable(false);
		setFocusPainted(false);
		setMargin(margin);
	}

	public PosButton(String text, Action action) {
		super(action);
		setText(text);

		//setFont(UIConfig.getButtonFont());

		setFocusable(false);
		setFocusPainted(false);
		setMargin(margin);
	}

	public PosButton(Action a) {
		super(a);

		//setFont(UIConfig.getButtonFont());

		setFocusable(false);
		setFocusPainted(false);
		setMargin(margin);
	}

	public PosButton(ActionCommand command) {
		this(command.toString());

		setActionCommand(command.name());
	}

	public PosButton(String text, ActionCommand command) {
		this(text);

		setActionCommand(command.name());
	}

	public PosButton(ActionCommand command, ActionListener listener) {
		this(command.toString());

		setActionCommand(command.name());
		addActionListener(listener);
	}

	public PosButton(ImageIcon imageIcon) {
		super(imageIcon);
		setFocusable(false);
		setFocusPainted(false);
	}

	@Override
	public String getUIClassID() {
		return "PosButtonUI"; //$NON-NLS-1$
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension size = null;

		if (isPreferredSizeSet()) {
			return super.getPreferredSize();
		}
		else if (ui != null) {
			size = ui.getPreferredSize(this);
		}

		if (size == null) {
			size = new Dimension(PosUIManager.getSize(POSConstants.BUTTON_DEFAULT_WIDTH, POSConstants.BUTTON_DEFAULT_HEIGHT));
		}
		else {
			int width = size.width < POSConstants.BUTTON_DEFAULT_WIDTH ? POSConstants.BUTTON_DEFAULT_WIDTH : size.width;
			int height = size.height < POSConstants.BUTTON_DEFAULT_HEIGHT ? POSConstants.BUTTON_DEFAULT_HEIGHT : size.height;
			size.setSize(PosUIManager.getSize(width, height));
		}

		return size;
	}

	@Override
	public void setAction(Action a) {
		super.setAction(a);

		if (a instanceof PosAction) {
			PosAction action = (PosAction) a;
			setVisible(action.isVisible());
		}
	}
}
