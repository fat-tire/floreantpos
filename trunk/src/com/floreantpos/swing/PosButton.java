package com.floreantpos.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.floreantpos.config.UIConfig;

public class PosButton extends JButton {
	public static Border border = new LineBorder(Color.BLACK, 1);
	static Insets margin = new Insets(0, 0, 0, 0);

	static POSButtonUI ui = new POSButtonUI();

	static {
		UIManager.put("PosButtonUI", "com.floreantpos.swing.POSButtonUI");
	}

	public PosButton() {
		this(null);
	}

	public PosButton(String text) {
		super(text);
		setFont(UIConfig.getButtonFont());

		setFocusPainted(false);
		setMargin(margin);
		setPreferredSize(new Dimension(60, 45));
	}

	@Override
	public String getUIClassID() {
		return "PosButtonUI";
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		final PosButton button = new PosButton("TEST");
		frame.add(button, BorderLayout.NORTH);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(button.getPreferredSize());
				System.out.println(button.getSize());
			}
		});

		frame.setSize(600, 400);
		frame.setVisible(true);
	}
}
