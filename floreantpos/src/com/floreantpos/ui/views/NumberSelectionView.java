/*
 * NumberSelectionView.java
 *
 * Created on August 25, 2006, 7:56 PM
 */

package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.floreantpos.IconFactory;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.dialog.POSMessageDialog;

/**
 *
 * @author  MShahriar
 */
public class NumberSelectionView extends TransparentPanel implements ActionListener {
	private TitledBorder titledBorder;
	
	private boolean decimalAllowed;
	private JTextField tfNumber;
	
	/** Creates new form NumberSelectionView */
	public NumberSelectionView() {
		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout(5,5));
		
		tfNumber = new JTextField();
		tfNumber.setText("0");
		tfNumber.setFont(tfNumber.getFont().deriveFont(Font.BOLD, 24));
		tfNumber.setEditable(false);
		tfNumber.setBackground(Color.WHITE);
		tfNumber.setHorizontalAlignment(JTextField.RIGHT);
		
		JPanel northPanel = new JPanel(new BorderLayout(5,5));
		northPanel.add(tfNumber, BorderLayout.CENTER);
		
		PosButton btnClearAll = new PosButton();
		btnClearAll.setText(com.floreantpos.POSConstants.CLEAR_ALL);
		btnClearAll.setActionCommand(com.floreantpos.POSConstants.CLEAR_ALL);
		btnClearAll.setPreferredSize(new Dimension(90, 50));
		btnClearAll.addActionListener(this);
		northPanel.add(btnClearAll, BorderLayout.EAST);
		
		add(northPanel, BorderLayout.NORTH);
		
		String[][] numbers = {
        		{"7","8","9"},
        		{"4","5","6"},
        		{"1","2","3"},
        		{".","0",com.floreantpos.POSConstants.CLEAR}
        };
        String[][] iconNames = new String[][] { 
        		{ "7_32.png", "8_32.png", "9_32.png" }, 
        		{ "4_32.png", "5_32.png", "6_32.png" }, 
        		{ "1_32.png", "2_32.png", "3_32.png" }, 
        		{ "dot_32.png", "0_32.png", "clear_32.png" } 
        	};
        
        JPanel centerPanel = new JPanel(new GridLayout(4,3,5,5));
        Dimension preferredSize = new Dimension(90,80);
        
        for (int i = 0; i < numbers.length; i++) {
			for (int j = 0; j < numbers[i].length; j++) {
				PosButton posButton = new PosButton();
				ImageIcon icon = IconFactory.getIcon(iconNames[i][j]);
				String buttonText = String.valueOf(numbers[i][j]);

				if (icon == null) {
					posButton.setText(buttonText);
				}
				else {
					posButton.setIcon(icon);
					if (com.floreantpos.POSConstants.CLEAR.equals(buttonText)) {
						posButton.setText(buttonText);
					}
				}

				posButton.setActionCommand(buttonText);
				posButton.setPreferredSize(preferredSize);
				posButton.addActionListener(this);
				centerPanel.add(posButton);
			}
		}
		add(centerPanel, BorderLayout.CENTER);
		
		titledBorder = new TitledBorder("");
		titledBorder.setTitleJustification(TitledBorder.CENTER);
		
		setBorder(titledBorder);
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if(actionCommand.equals(com.floreantpos.POSConstants.CLEAR_ALL)) {
			tfNumber.setText("0");
		}
		else if(actionCommand.equals(com.floreantpos.POSConstants.CLEAR)) {
			String s = tfNumber.getText();
			if(s.length() > 1) {
				s = s.substring(0, s.length() - 1);
			}
			else {
				s = "0";
			}
			tfNumber.setText(s);
		} 
		else if (actionCommand.equals(".")) {
			if (isDecimalAllowed() && tfNumber.getText().indexOf('.') < 0) {
				String string = tfNumber.getText() + ".";
				if(!validate(string)) {
					POSMessageDialog.showError("Invalid number");
					return;
				}
				tfNumber.setText(string);
			}
		}
		else {
			String s = tfNumber.getText();
			if(s.equals("0")) {
				tfNumber.setText(actionCommand);
				return;
			}
			
			s = s + actionCommand;
			if(!validate(s)) {
				POSMessageDialog.showError("Invalid number");
				return;
			}
			tfNumber.setText(s);
		}
		
	}
	
	private boolean validate(String str) {
    	if(isDecimalAllowed()) {
    		try {
    			Double.parseDouble(str);
    		} catch (Exception x) {
    			return false;
    		}
    	}
    	else {
    		try {
    			Integer.parseInt(str);
    		} catch (Exception x) {
    			return false;
    		}
    	}
    	return true;
    }
	
	public void setTitle(String title) {
    	titledBorder.setTitle(title);
    }
	
	public double getValue() {
		return Double.parseDouble(tfNumber.getText());
	}

	public void setValue(double value) {
		if(isDecimalAllowed()) {
			tfNumber.setText(String.valueOf(value));
		}
		else {
			tfNumber.setText(String.valueOf( (int) value));
		}
	}


	public boolean isDecimalAllowed() {
		return decimalAllowed;
	}

	public void setDecimalAllowed(boolean decimalAllowed) {
		this.decimalAllowed = decimalAllowed;
	}
}
