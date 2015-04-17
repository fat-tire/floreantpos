package com.floreantpos.ui.views.order;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXTitledSeparator;

import com.floreantpos.POSConstants;
import com.floreantpos.swing.PosButton;
import com.jstatcom.component.CardPanel;

public abstract class SelectionView extends JPanel implements ComponentListener {
	private final static int HORIZONTAL_GAP = 15;
	private final static int VERTICAL_GAP = 15;

	private Dimension buttonSize;

	protected CardPanel cardPanel = new CardPanel();
	protected JPanel buttonPanel = new JPanel();
	protected JScrollPane scrollPane = new JScrollPane(buttonPanel);
	
	protected int scrollSize;

	protected List items;

	protected com.floreantpos.swing.PosButton btnBack;
	protected com.floreantpos.swing.PosButton btnNext;
	protected com.floreantpos.swing.PosButton btnPrev;

	@SuppressWarnings("unused")
	private String title;

	public SelectionView(String title, int buttonWidth, int buttonHeight) {
		this.title = title;
		this.buttonSize = new Dimension(buttonWidth, buttonHeight);

		TitledBorder border = new TitledBorder(title);
		border.setTitleJustification(TitledBorder.CENTER);

		setBorder(border);

		setLayout(new BorderLayout(HORIZONTAL_GAP, VERTICAL_GAP));

		scrollPane.setBorder(null);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane);

		MigLayout migLayout2 = new MigLayout("fill,hidemode 3", "grow", "");
		JPanel southPanel = new JPanel(migLayout2);

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
		
		addComponentListener(this);

	}

	public SelectionView(String title) {
		this(title, 120, 120);
	}

	public void setItems(List items) {
		this.items = items;
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
//		btnNext.setEnabled(false);
//		btnPrev.setEnabled(false);
		buttonPanel.removeAll();
		cardPanel.removeAll();
	}

	protected void renderItems() {
		reset();

		if (this.items == null || items.size() == 0) {
			return;
		}

		Dimension size = scrollPane.getSize();
		Dimension itemButtonSize = getButtonSize();

		int horizontalButtonCount = getButtonCount(size.width, getButtonSize().width);
		int verticalButtonCount = getButtonCount(size.height, getButtonSize().height);

		scrollSize = verticalButtonCount * itemButtonSize.height;
		
		int itemPerPage = horizontalButtonCount * verticalButtonCount;
		int numPages = (int) Math.ceil((double) items.size() / (double) itemPerPage);

		buttonPanel.setLayout(new MigLayout("alignx 50%, wrap " + horizontalButtonCount));
		
		int itemIndex = 0;
		for (int i = 0; i < items.size(); i++) {
//			JPanel panel = new JPanel(new MigLayout("alignx 50%, wrap " + horizontalButtonCount));
//			for (int j = 0; j < itemPerPage; j++) {
//				if(itemIndex >= items.size()) {
//					break;
//				}
			Object item = items.get(itemIndex++);
			if (item instanceof String) {
				//addSeparator(item.toString());
				buttonPanel.add(new JXTitledSeparator(item.toString(), JLabel.CENTER), "alignx 50%, newline, span, growx, height 30!");
				continue;
			}
			
				AbstractButton itemButton = createItemButton(item);
				buttonPanel.add(itemButton, "width " + itemButtonSize.width + "!, height " + itemButtonSize.height + "!");
//			}
//			
//			cardPanel.add(panel);
		}
		
		repaint();
	}

	private void scrollDown() {
		//cardPanel.showNextCard();
		JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
		scrollBar.setValue(scrollBar.getValue() + scrollSize);
	}

	private void scrollUp() {
		//cardPanel.showPreviousCard();
		scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getValue() - scrollSize);
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

	protected int getButtonCount(int containerSize, int itemSize) {
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
