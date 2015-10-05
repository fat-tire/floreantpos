package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.IconFactory;
import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.ShopTableDAO;
import com.floreantpos.swing.IntegerTextField;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;

public class TableSelectionDialog extends POSDialog implements ActionListener {
	private int defaultValue;

	private TitlePanel titlePanel;
	private IntegerTextField tfNumber;

	private DefaultListModel<ShopTable> addedTableListModel = new DefaultListModel<ShopTable>();
	private JList<ShopTable> addedTableList = new JList<ShopTable>(addedTableListModel);

	public TableSelectionDialog() {
		init();
	}

	private void init() {
		setResizable(false);

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout(5, 5));

		renderTableList();

		titlePanel = new TitlePanel();
		titlePanel.setTitle(Messages.getString("TableSelectionDialog.0")); //$NON-NLS-1$
		contentPane.add(titlePanel, BorderLayout.NORTH);

		JPanel keypadPanel = new JPanel(new MigLayout("fill")); //$NON-NLS-1$

		tfNumber = new IntegerTextField();
		tfNumber.setText(String.valueOf(defaultValue));
		tfNumber.setFont(tfNumber.getFont().deriveFont(Font.BOLD, 24));
		tfNumber.setFocusable(true);
		tfNumber.requestFocus();
		tfNumber.setBackground(Color.WHITE);
		keypadPanel.add(tfNumber, "span 2, grow"); //$NON-NLS-1$

		PosButton posButton = new PosButton(POSConstants.CLEAR_ALL);
		posButton.setFocusable(false);
		posButton.setMinimumSize(new Dimension(25, 23));
		posButton.addActionListener(this);
		keypadPanel.add(posButton, "growy,height 55,wrap, w 100!"); //$NON-NLS-1$

		String[][] numbers = { { "7", "8", "9" }, { "4", "5", "6" }, { "1", "2", "3" }, { ".", "0", Messages.getString("TableSelectionDialog.15") } }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$
		String[][] iconNames = new String[][] { { "7.png", "8.png", "9.png" }, { "4.png", "5.png", "6.png" }, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
				{ "1.png", "2.png", "3.png" }, { "dot.png", "0.png", "clear.png" } }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

		for (int i = 0; i < numbers.length; i++) {
			for (int j = 0; j < numbers[i].length; j++) {
				posButton = new PosButton();
				ImageIcon icon = IconFactory.getIcon(iconNames[i][j]);
				String buttonText = String.valueOf(numbers[i][j]);

				if (icon == null) {
					posButton.setText(buttonText);
				}
				else {
					posButton.setIcon(icon);
					if (POSConstants.CLEAR.equals(buttonText)) {
						posButton.setText(buttonText);
					}
				}

				posButton.setActionCommand(buttonText);
				if(".".equals(buttonText)) { //$NON-NLS-1$
					posButton.setEnabled(false);
				}
				posButton.addActionListener(this);
				String constraints = "grow,w 100!"; //$NON-NLS-1$
				if (j == numbers[i].length - 1) {
					constraints += ", wrap"; //$NON-NLS-1$
				}
				keypadPanel.add(posButton, constraints);
			}
		}

		JPanel buttonPanel = new JPanel(new MigLayout("align 50% 50%")); //$NON-NLS-1$
		//buttonPanel.add(new JSeparator(JSeparator.HORIZONTAL), "span 4, grow, gaptop 5");

		posButton = new PosButton(Messages.getString("TableSelectionDialog.32")); //$NON-NLS-1$
		posButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (addTable()) {
					tfNumber.setText(""); //$NON-NLS-1$
				}
			}
		});
		buttonPanel.add(posButton, "newline, w 80"); //$NON-NLS-1$

		posButton = new PosButton(POSConstants.OK);
		posButton.setFocusable(false);
		posButton.addActionListener(this);
		buttonPanel.add(posButton, "w 80!"); //$NON-NLS-1$

		PosButton btnCancel = new PosButton(POSConstants.CANCEL);
		btnCancel.setFocusable(false);
		btnCancel.addActionListener(this);
		buttonPanel.add(btnCancel, " w 80!"); //$NON-NLS-1$

		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		contentPane.add(keypadPanel);
	}

	private void renderTableList() {
		JPanel tableListPanel = new JPanel(new BorderLayout(5, 5));
		tableListPanel.setPreferredSize(new Dimension(150, 100));
		tableListPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5),
				BorderFactory.createTitledBorder(Messages.getString("TableSelectionDialog.37")))); //$NON-NLS-1$

		PosButton btnRemoveTable = new PosButton(Messages.getString("TableSelectionDialog.38")); //$NON-NLS-1$
		btnRemoveTable.setFocusable(false);
		btnRemoveTable.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ShopTable selectedValue = addedTableList.getSelectedValue();
				if (selectedValue != null) {
					selectedValue.setServing(false);
					addedTableListModel.removeElement(selectedValue);
				}
			}
		});
		tableListPanel.add(btnRemoveTable, BorderLayout.SOUTH);
		DefaultListCellRenderer renderer = new DefaultListCellRenderer() {
			Dimension preferredSize = new Dimension(60, 40);

			public java.awt.Component getListCellRendererComponent(javax.swing.JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel rendererComponent = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				rendererComponent.setHorizontalAlignment(JLabel.CENTER);
				rendererComponent.setPreferredSize(preferredSize);

				return rendererComponent;
			};
		};
		addedTableList.setCellRenderer(renderer);
		tableListPanel.add(new JScrollPane(addedTableList));

		getContentPane().add(tableListPanel, BorderLayout.WEST);
	}

	private void doOk() {
		setCanceled(false);
		dispose();
	}

	private boolean addTable() {
		int tableNumber = tfNumber.getInteger();
		
		if (tableNumber <= 0) {
			POSMessageDialog.showError(this, Messages.getString("TableSelectionDialog.39")); //$NON-NLS-1$
			return false;
		}

		ShopTable shopTable = ShopTableDAO.getInstance().get(tableNumber);

		if (shopTable == null) {
			POSMessageDialog.showError(this, Messages.getString("TableSelectionDialog.40")); //$NON-NLS-1$
			return false;
		}

		if (shopTable.isServing()) {
			POSMessageDialog.showError(this, Messages.getString("TableSelectionDialog.41") + tableNumber + " is occupied"); //$NON-NLS-1$ //$NON-NLS-2$
			return false;
		}

		if (shopTable.isBooked()) {
			POSMessageDialog.showError(this, Messages.getString("TableSelectionDialog.43") + tableNumber + " is booked"); //$NON-NLS-1$ //$NON-NLS-2$
			return false;
		}

		addedTableListModel.addElement(shopTable);
		return true;
	}

	private void doCancel() {
		addedTableListModel.removeAllElements();

		setCanceled(true);
		dispose();
	}

	private void doClearAll() {
		tfNumber.setText(String.valueOf(defaultValue));
	}

	private void doClear() {
		String s = tfNumber.getText();
		if (s.length() > 1) {
			s = s.substring(0, s.length() - 1);
		}
		else {
			s = String.valueOf(defaultValue);
		}
		tfNumber.setText(s);
	}

	private void doInsertNumber(String number) {
		String s = tfNumber.getText();
		
		if (s.equals("0")) { //$NON-NLS-1$
			tfNumber.setText(number);
			return;
		}

		s = s + number;
		
		tfNumber.setText(s);
		tfNumber.requestFocus();
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();

		if (POSConstants.CANCEL.equalsIgnoreCase(actionCommand)) {
			doCancel();
		}
		else if (POSConstants.OK.equalsIgnoreCase(actionCommand)) {
			doOk();
		}
		else if (actionCommand.equals(POSConstants.CLEAR_ALL)) {
			doClearAll();
		}
		else if (actionCommand.equals(POSConstants.CLEAR)) {
			doClear();
		}
		else {
			doInsertNumber(actionCommand);
		}

	}

	public void setTitle(String title) {
		titlePanel.setTitle(title);

		super.setTitle(title);
	}

	public void setDialogTitle(String title) {
		super.setTitle(title);
	}

	public List<ShopTable> getTables() {
		Enumeration<ShopTable> elements = this.addedTableListModel.elements();
		List<ShopTable> tables = new ArrayList<ShopTable>();

		while (elements.hasMoreElements()) {
			tables.add(elements.nextElement());
		}

		return tables;
	}
	
	public void setTicket(Ticket ticket) {
		if(ticket == null) {
			return;
		}
		
		List<ShopTable> tables = ShopTableDAO.getInstance().getTables(ticket);
		if(tables == null) return;
		
		for (ShopTable shopTable : tables) {
			addedTableListModel.addElement(shopTable);
		}
	}
}
