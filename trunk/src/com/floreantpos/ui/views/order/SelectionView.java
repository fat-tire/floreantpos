package com.floreantpos.ui.views.order;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXTitledSeparator;

import com.floreantpos.POSConstants;
import com.floreantpos.swing.PosButton;

public abstract class SelectionView extends JPanel implements ComponentListener {
	private final static int HORIZONTAL_GAP = 5;
	private final static int VERTICAL_GAP = 5;

	private Dimension buttonSize;

	private JPanel buttonsPanel;

	private List items;
	
	private int previousBlockIndex = -1;
	private int currentBlockIndex = 0;
	private int nextBlockIndex;

	private com.floreantpos.swing.PosButton btnBack;
	private com.floreantpos.swing.PosButton btnNext;
	private com.floreantpos.swing.PosButton btnPrev;

	public SelectionView(String title, int buttonWidth, int buttonHeight) {
		this.buttonSize = new Dimension(buttonWidth, buttonHeight);

		TitledBorder border = new TitledBorder(title);
		border.setTitleJustification(TitledBorder.CENTER);

		setBorder(border);

		setLayout(new BorderLayout(HORIZONTAL_GAP, VERTICAL_GAP));

		buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, HORIZONTAL_GAP, VERTICAL_GAP));
		buttonsPanel.addComponentListener(this);
		add(buttonsPanel);

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

	public SelectionView(String title) {
		this(title, 100, 100);
	}

	public void setItems(List items) {
		this.items = items;
		currentBlockIndex = 0;
		nextBlockIndex = 0;
		
		renderItems();
	}

	public List getItems() {
		return items;
	}

	public Dimension getButtonSize() {
		return buttonSize;
	}

	public void setButtonSize(Dimension buttonSize) {
		this.buttonSize = buttonSize;
	}

	protected abstract AbstractButton createItemButton(Object item);

	public void reset() {
		//btnBack.setEnabled(false);
		btnNext.setEnabled(false);
		btnPrev.setEnabled(false);
		
		Component[] components = buttonsPanel.getComponents();
		for (int i = 0; i < components.length; i++) {
			Component c = components[i];
			if (c instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) c;
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

	private void renderItems() {
		reset();

		if (this.items == null || items.size() == 0) {
			return;
		}

		Dimension size = buttonsPanel.getSize();
		Dimension itemButtonSize = getButtonSize();

		int horizontalButtonCount = getButtonCount(size.width, getButtonSize().width);
		int verticalButtonCount = getButtonCount(size.height, getButtonSize().height);
		
		buttonsPanel.setLayout(new MigLayout("alignx 50%, wrap " + horizontalButtonCount));
		
		//TODO: REVISE CODE
		int totalItem = horizontalButtonCount * verticalButtonCount;
		
		previousBlockIndex = currentBlockIndex - totalItem;
		nextBlockIndex = currentBlockIndex + totalItem;
		
		int separatorCount = getSeparatorCount();
		if(separatorCount > 0) {
			totalItem = totalItem - (separatorCount * horizontalButtonCount);
			previousBlockIndex = currentBlockIndex - totalItem;
			nextBlockIndex = currentBlockIndex + totalItem + separatorCount;
		}
		
		try {
			for (int i = currentBlockIndex; i < nextBlockIndex; i++) {

				Object item = items.get(i);

				if (item instanceof String) {
					addSeparator(item.toString());
					continue;
				}

				AbstractButton itemButton = createItemButton(item);
				buttonsPanel.add(itemButton, "width " + itemButtonSize.width + "!, height " + itemButtonSize.height + "!");

				if (i == items.size() - 1) {
					break;
				}
			}
		} catch (Exception e) {
			// TODO: fix it.
		}
		
		if(previousBlockIndex >= 0 && currentBlockIndex != 0) {
			btnPrev.setEnabled(true);
		}
		
		if(nextBlockIndex < items.size() - 1) {
			btnNext.setEnabled(true);
		}
		
		revalidate();
		repaint();
	}
	
	protected int getSeparatorCount() {
		if(!(this instanceof ModifierView)) {
			return 0;
		}
		
		int count = 0;
		for(int i = currentBlockIndex; i < nextBlockIndex; i++) {
			if(i == items.size() - 1) {
				break;
			}
			
			if(items.get(i) instanceof String) {
				++count;
			}
		}
		return count;
	}

	public void addButton(AbstractButton button) {
		button.setPreferredSize(buttonSize);
		button.setText("<html><body><center>" + button.getText() + "</center></body></html>");
		buttonsPanel.add(button);
	}

	public void addSeparator(String text) {
		JPanel pabel = new JPanel();
		TitledBorder titledBorder = BorderFactory.createTitledBorder(text);
		
		
		JLabel label = new JLabel(text);
		label.setFont(label.getFont().deriveFont(Font.BOLD));
		buttonsPanel.add(new JXTitledSeparator(text, JLabel.CENTER), "newline, span, growx, height 100!");
//		buttonsPanel.add(new JSeparator(), "newline, span, split 3, growx, height 100!");
//		buttonsPanel.add(label, "gapbottom 1, height 100!");
//		buttonsPanel.add(new JSeparator(), "gapleft rel, growx, height 100!");
	}

	private void scrollDown() {
		currentBlockIndex = nextBlockIndex;
		renderItems();
	}

	private void scrollUp() {
		currentBlockIndex = previousBlockIndex;
		renderItems();
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

	private int getButtonCount(int containerSize, int itemSize) {
		int buttonCount = containerSize / itemSize;
		buttonCount = (containerSize - ((containerSize / itemSize) * 5)) / itemSize;
		return buttonCount;
	}

	public void componentResized(ComponentEvent e) {
		renderItems();
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentShown(ComponentEvent e) {
	}

	public void componentHidden(ComponentEvent e) {
	}
}
