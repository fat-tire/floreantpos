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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.Timer;

import com.floreantpos.actions.ActionCommand;


public class PosBlinkButton extends PosButton implements ActionListener {
	private boolean blinking = false;
	private boolean blinked = false;
	private Color originalColor = getBackground();
	
	private Timer blinkTimer = new Timer(500, this);
	
	public PosBlinkButton() {
		super();
	}

	public PosBlinkButton(Action a) {
		super(a);
	}

	public PosBlinkButton(ActionCommand command, ActionListener listener) {
		super(command, listener);
	}

	public PosBlinkButton(ActionCommand command) {
		super(command);
	}

	public PosBlinkButton(ImageIcon imageIcon) {
		super(imageIcon);
	}

	public PosBlinkButton(String text, Action action) {
		super(text, action);
	}

	public PosBlinkButton(String text, ActionCommand command) {
		super(text, command);
	}

	public PosBlinkButton(String text) {
		super(text);
	}

	public boolean isBlinking() {
		return blinking;
	}

	public void setBlinking(boolean blinking) {
		this.blinking = blinking;
		
		if(blinking) {
			blinkTimer.start();
		}
		else {
			blinkTimer.stop();
			setBackground(originalColor);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		blinked = !blinked;
		
		if(blinked) {
			setBackground(Color.YELLOW.darker());
		}
		else {
			setBackground(Color.YELLOW);
		}
	}
}
