package com.floreantpos.ui.views.order;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.POSConstants;
import com.floreantpos.swing.PosButton;

public abstract class SelectionView extends JPanel {
	private static final Dimension buttonSize = new Dimension(85, 60);

	private JPanel buttonsPanel;

	private com.floreantpos.swing.PosButton btnBack;
	private com.floreantpos.swing.PosButton btnNext;
	private com.floreantpos.swing.PosButton btnPrev;
	private JScrollBar verticalScrollBar;

	private JScrollPane buttonScrollPane;

	public SelectionView(String title) {
		TitledBorder border = new TitledBorder(title);
		border.setTitleJustification(TitledBorder.CENTER);

		setBorder(border);

		setLayout(new BorderLayout(5, 5));

		MigLayout migLayout = new MigLayout("wrap 3", "fill,grow,shrink", "");
		buttonsPanel = new JPanel(migLayout);

		buttonScrollPane = new JScrollPane(buttonsPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		//JScrollPane scrollPane = new JScrollPane(buttonsPanel);
		buttonScrollPane.setBorder(null);
		buttonScrollPane.setOpaque(false);
		buttonScrollPane.getViewport().setOpaque(false);
		verticalScrollBar = buttonScrollPane.getVerticalScrollBar();
		verticalScrollBar.setBlockIncrement(250);
		add(buttonScrollPane);

		buttonsPanel.addComponentListener(new ComponentListener() {

			public void componentResized(ComponentEvent e) {
				int value = verticalScrollBar.getValue();
				int min = verticalScrollBar.getMinimum();
				int max = verticalScrollBar.getMaximum();
				int inc = verticalScrollBar.getBlockIncrement(0);

				if (value <= min) {
					btnPrev.setEnabled(false);
				}
				else {
					btnPrev.setEnabled(true);
				}
				if ((value + inc) >= max) {
					btnNext.setEnabled(false);
				}
				else {
					btnNext.setEnabled(true);
				}
			}

			public void componentMoved(ComponentEvent e) {
			}

			public void componentShown(ComponentEvent e) {
			}

			public void componentHidden(ComponentEvent e) {
			}

		});

		MigLayout migLayout2 = new MigLayout("fill,hidemode 3", "grow", "");
		JPanel southPanel = new JPanel(migLayout2);
		southPanel.add(new JSeparator(JSeparator.HORIZONTAL), "wrap, span, grow, gaptop 5");

		btnBack = new PosButton();
		btnBack.setText(POSConstants.CAPITAL_BACK);
		southPanel.add(btnBack, "grow,shrink, align center, height 50");

		btnPrev = new PosButton();
		btnPrev.setText(POSConstants.CAPITAL_PREV);
		southPanel.add(btnPrev, "grow, align center, height 50");

		btnNext = new PosButton();
		btnNext.setText(POSConstants.CAPITAL_NEXT);
		southPanel.add(btnNext, "grow, align center, height 50");

		add(southPanel, BorderLayout.SOUTH);

		ScrollAction action = new ScrollAction();
		btnBack.addActionListener(action);
		btnPrev.addActionListener(action);
		btnNext.addActionListener(action);
	}

	public void reset() {
		Component[] components = buttonsPanel.getComponents();
		for (int i = 0; i < components.length; i++) {
			Component c = components[i];
			if (c instanceof JButton) {
				JButton button = (JButton) c;
				//button.setIcon(null);
				button.setPreferredSize(null);

				ActionListener[] actionListeners = button.getActionListeners();
				if (actionListeners != null) {
					for (int j = 0; j < actionListeners.length; j++) {
						button.removeActionListener(actionListeners[j]);
					}
				}
			}
		}
		buttonsPanel.removeAll();
	}

	public void addButton(AbstractButton button) {
		button.setPreferredSize(buttonSize);
		button.setText("<html><body><center>" + button.getText() + "</center></body></html>");
		buttonsPanel.add(button, "height 60px");
	}

	public void addButton(JButton button, String text) {
		button.setText("<html><body><center>" + text + "</center></body></html>");
		button.setPreferredSize(buttonSize);
		buttonsPanel.add(button, "height 50px");
	}

	public void addSeparator(String text) {
		JLabel label = new JLabel(text);
		//label.setForeground(Color.RED);
		label.setFont(label.getFont().deriveFont(Font.BOLD));
		buttonsPanel.add(new JSeparator(), "newline, span, split 3, growx");
		buttonsPanel.add(label, "gapbottom 1");
		buttonsPanel.add(new JSeparator(), "gapleft rel, growx");
	}

	private int scrollByBlock(JScrollBar scrollbar, int direction) {
		// This method is called from BasicScrollPaneUI to implement wheel
		// scrolling, and also from scrollByBlock().
		int oldValue = scrollbar.getValue();
		int blockIncrement = scrollbar.getBlockIncrement();
		int delta = blockIncrement * ((direction > 0) ? +1 : -1);
		int newValue = oldValue + delta;

		// Check for overflow.
		if (delta > 0 && newValue < oldValue) {
			newValue = scrollbar.getMaximum();
		}
		else if (delta < 0 && newValue > oldValue) {
			newValue = scrollbar.getMinimum();
		}

		return newValue;

	}

	private void scrollDown() {
		int scrollUnit = scrollByBlock(verticalScrollBar, 1);
		verticalScrollBar.setValue(scrollUnit);
		//scrollUnit = scrollByBlock(verticalScrollBar, 1);

		int value = verticalScrollBar.getValue();
		int min = verticalScrollBar.getMinimum();

		if (value < scrollUnit) {
			btnNext.setEnabled(false);
		}
		else {
			btnNext.setEnabled(true);
		}

		if (value <= min) {
			btnPrev.setEnabled(false);
		}
		else {
			btnPrev.setEnabled(true);
		}
		/*
		 if ((value + inc) >= max) {
		 btnNext.setEnabled(false);
		 }
		 else {
		 btnNext.setEnabled(true);
		 }*/
	}

	private void scrollUp() {
		int scrollUnit = scrollByBlock(verticalScrollBar, 0);
		verticalScrollBar.setValue(scrollUnit);

		int value = verticalScrollBar.getValue();
		int min = verticalScrollBar.getMinimum();
		int max = verticalScrollBar.getMaximum();
		int inc = verticalScrollBar.getBlockIncrement(0);

		if (value <= min) {
			btnPrev.setEnabled(false);
		}
		else {
			btnPrev.setEnabled(true);
		}
		if ((value + inc) >= max) {
			btnNext.setEnabled(false);
		}
		else {
			btnNext.setEnabled(true);
		}
	}

	public void setBackEnable(boolean enable) {
		btnBack.setEnabled(enable);
	}
	public void setBackVisible(boolean enable) {
		btnBack.setVisible(enable);
	}

	public abstract void doGoBack();

	private class ScrollAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if (source == btnBack) {
				doGoBack();
			}
			else if (source == btnPrev) {
				scrollUp();
			}
			else if (source == btnNext) {
				scrollDown();
			}
		}

	}

	public JPanel getButtonsPanel() {
		return buttonsPanel;
	}

	public JScrollPane getButtonScrollPane() {
		return buttonScrollPane;
	}

	//	public static void main(String[] args) throws Exception {
	//		javax.swing.ImageIcon image = new javax.swing.ImageIcon(ButtonsView.class.getResource("/images/noModifier.png"));
	//		//BufferedImage image = ImageIO.read(ButtonsView.class.getResourceAsStream("/images/noModifier.png"));
	//		BufferedImage image2 = new BufferedImage(image.getIconWidth() / 2, image.getIconHeight() / 2, BufferedImage.TYPE_INT_ARGB);
	//		Graphics graphics = image2.getGraphics();
	//		//graphics.drawImage(image.getImage(),0,0,image.getIconWidth() / 2, image.getIconHeight() / 2, null);
	//		
	//		FileOutputStream out = new FileOutputStream("src/images/empty16.png");
	//		ImageIO.write(image2, "png", out);
	//		out.close();
	//	}
}
