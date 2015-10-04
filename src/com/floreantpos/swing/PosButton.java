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

import com.floreantpos.actions.ActionCommand;
import com.floreantpos.actions.PosAction;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.config.UIConfig;

public class PosButton extends JButton {
	public static Border border = new LineBorder(Color.BLACK, 1);
	static Insets margin = new Insets(0, 0, 0, 0);

	static POSButtonUI ui = new POSButtonUI();

	static {
		UIManager.put("PosButtonUI", "com.floreantpos.swing.POSButtonUI"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public PosButton() {
		this(""); //$NON-NLS-1$
	}

	public PosButton(String text) {
		super(text);
		setFont(UIConfig.getButtonFont());
		
		setFocusable(false);
		setFocusPainted(false);
		setMargin(margin);
	}
	public PosButton(String text, Action action) {
		super(action);
		setText(text);
		
		setFont(UIConfig.getButtonFont());
		
		setFocusable(false);
		setFocusPainted(false);
		setMargin(margin);
	}
	
	public PosButton(Action a) {
		super(a);
		
		setFont(UIConfig.getButtonFont());

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
		Dimension size = super.getPreferredSize();

		if (isPreferredSizeSet()) {
			return size;
		}
		
		if (ui != null) {
			size = ui.getPreferredSize(this);
		}
		
		if(size != null) {
			size.setSize(size.width + 20, TerminalConfig.getTouchScreenButtonHeight());
		}
		
		
		return (size != null) ? size : super.getPreferredSize();
	}
	
	@Override
	public void setAction(Action a) {
		super.setAction(a);
		
		if(a instanceof PosAction) {
			PosAction action = (PosAction) a;
			setVisible(action.isVisible());
		}
	}
}
