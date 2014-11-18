package com.floreantpos.ui.dialog;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.IconFactory;
import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.dao.ShopTableDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;

public class TableSelectionDialog extends POSDialog implements ActionListener {
	private int defaultValue;

	private TitlePanel titlePanel;
	private JTextField tfNumber;

	private PosButton posButton_1;
	
	private List<ShopTable> tables = new ArrayList<ShopTable>();

	public TableSelectionDialog() {
		this(Application.getPosWindow());
	}

	public TableSelectionDialog(Frame parent) {
		super(parent, true);
		init();
	}

	public TableSelectionDialog(Dialog parent) {
		super(parent, true);

		init();
	}

	private void init() {
		setResizable(false);

		Container contentPane = getContentPane();

		MigLayout layout = new MigLayout("fillx", "[60px,fill][60px,fill][60px,fill]", "[][][][][]");
		contentPane.setLayout(layout);

		titlePanel = new TitlePanel();
		titlePanel.setTitle("Enter table number");
		contentPane.add(titlePanel, "spanx ,growy,height 60,wrap");

		tfNumber = new JTextField();
		tfNumber.setText(String.valueOf(defaultValue));
		tfNumber.setFont(tfNumber.getFont().deriveFont(Font.BOLD, 24));
		tfNumber.setFocusable(true);
		tfNumber.requestFocus();
		tfNumber.setBackground(Color.WHITE);
		contentPane.add(tfNumber, "span 2, grow");

		PosButton posButton = new PosButton(POSConstants.CLEAR_ALL);
		posButton.setFocusable(false);
		posButton.setMinimumSize(new Dimension(25, 23));
		posButton.addActionListener(this);
		contentPane.add(posButton, "growy,height 55,wrap");

		String[][] numbers = { { "7", "8", "9" }, { "4", "5", "6" }, { "1", "2", "3" }, { "0", "CLEAR" } };
		String[][] iconNames = new String[][] { { "7_32.png", "8_32.png", "9_32.png" }, { "4_32.png", "5_32.png", "6_32.png" },
				{ "1_32.png", "2_32.png", "3_32.png" }, { "0_32.png", "clear_32.png" } };

		for (int i = 0; i < numbers.length; i++) {
			for (int j = 0; j < numbers[i].length; j++) {
				posButton = new PosButton();
				posButton.setFocusable(false);
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
				posButton.addActionListener(this);
				String constraints = "grow, height 55";
				if (j == numbers[i].length - 1) {
					constraints += ", wrap";
				}
				contentPane.add(posButton, constraints);
			}
		}
		contentPane.add(new JSeparator(), "newline,spanx ,growy,gapy 20");
		
		JPanel buttonPanel = new JPanel(new GridLayout(1, 0, 10, 10));
		
		posButton = new PosButton("NEXT");
		posButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(addTable()) {
					tfNumber.setText("");
				}
			}
		});
		buttonPanel.add(posButton, "grow");

		posButton = new PosButton(POSConstants.OK);
		posButton.setFocusable(false);
		posButton.addActionListener(this);
		buttonPanel.add(posButton, "skip 1,grow");

		posButton_1 = new PosButton(POSConstants.CANCEL);
		posButton_1.setFocusable(false);
		posButton_1.addActionListener(this);
		buttonPanel.add(posButton_1, "grow");

		contentPane.add(buttonPanel, "grow, span 3");
	}

	private void doOk() {
		if(addTable()) {
			setCanceled(false);
			dispose();
		}
	}

	private boolean addTable() {
		String tableNumber = tfNumber.getText();
		if(StringUtils.isEmpty(tableNumber)) {
			POSMessageDialog.showError(this, "Please insert table number");
			return false;
		}
		
		ShopTable shopTable = ShopTableDAO.getInstance().getByNumber(tableNumber);

		if(shopTable == null) {
			POSMessageDialog.showError(this, "Table number " + tableNumber + " does not exist");
			return false;
		}
		
		if(shopTable.isOccupied()) {
			POSMessageDialog.showError(this,  "Table number " + tableNumber + " is occupied");
			return false;
		}
		
		this.tables.add(shopTable);
		return true;
	}

	private void doCancel() {
		this.tables.clear();
		
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
		tfNumber.setText(number);
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
		return tables;
	}
}
