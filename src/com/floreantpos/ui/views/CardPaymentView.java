/*
 * CardView.java
 *
 * Created on August 25, 2006, 1:39 PM
 */

package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.main.Application;
import com.floreantpos.model.CreditCardTransaction;
import com.floreantpos.model.DebitCardTransaction;
import com.floreantpos.swing.DoubleDocument;
import com.floreantpos.swing.FocusedTextField;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.order.RootView;

/**
 *
 * @author  MShahriar
 */
public class CardPaymentView extends PaymentView {
	public final static int CARD_TYPE_CREDIT = 0;
	public final static int CARD_TYPE_DEBIT = 1;

	public final static String MASTER_CARD = "MASTER_CARD";
	public final static String VISA_CARD = "VISA_CARD";
	public final static String EMEX_CARD = "EMEX_CARD";
	public final static String DISCOVER_CARD = "DISCOVER_CARD";

	private int cardType = CARD_TYPE_CREDIT;
	private String whichCard = MASTER_CARD;

	private double gratuityAmount;

	/** Creates new form CardView */
	public CardPaymentView() {
		initComponents();

		DoubleDocument gratuityDocument = new DoubleDocument();
		gratuityDocument.addDocumentListener(new DocumentListener() {
			void updatePayAmount() {
				double cardAmount = 0;
				double gratuity = 0;

				try {
					gratuity = Double.parseDouble(tfGratuityAmount.getText());
				} catch (Exception x) {
				}
				cardAmount = getDueAmount();
				tfCardAmount.setText(Application.formatNumber(cardAmount + gratuity));
			}

			public void insertUpdate(DocumentEvent e) {
				updatePayAmount();
			}

			public void removeUpdate(DocumentEvent e) {
				updatePayAmount();
			}

			public void changedUpdate(DocumentEvent e) {
				updatePayAmount();
			}

		});
		
		lblCSymbol3 = new JLabel("");
		transparentPanel.add(lblCSymbol3, "cell 1 0,alignx trailing");
		
		tfDueAmount = new FocusedTextField();
		tfDueAmount.setHorizontalAlignment(SwingConstants.RIGHT);
		tfDueAmount.setFont(new Font("Dialog", Font.BOLD, 12));
		tfDueAmount.setEditable(false);
		transparentPanel.add(tfDueAmount, "cell 2 0,growx");
		jLabel1 = new javax.swing.JLabel();
		jLabel1.setFont(new Font("Dialog", Font.BOLD, 12));
		transparentPanel.add(jLabel1, "cell 0 1,alignx right");
		jLabel1.setText("GRATUITY AMOUNT");
		lblCSymbol1 = new javax.swing.JLabel();
		transparentPanel.add(lblCSymbol1, "cell 1 1");
		
				
				tfGratuityAmount = new FocusedTextField();
				tfGratuityAmount.setHorizontalAlignment(SwingConstants.RIGHT);
				tfGratuityAmount.setColumns(20);
				transparentPanel.add(tfGratuityAmount, "cell 2 1,growx");
				
						tfGratuityAmount.setFont(new Font("Dialog", Font.BOLD, 12));
						tfGratuityAmount.setText("0");
						jLabel2 = new javax.swing.JLabel();
						jLabel2.setFont(new Font("Dialog", Font.BOLD, 12));
						transparentPanel.add(jLabel2, "cell 0 2,alignx right");
						jLabel2.setText("AMOUNT TO BE CHARGED");
						lblCSymbol2 = new javax.swing.JLabel();
						transparentPanel.add(lblCSymbol2, "cell 1 2");
						
		tfCardAmount = new FocusedTextField();
		tfCardAmount.setHorizontalAlignment(SwingConstants.RIGHT);
		tfCardAmount.setColumns(20);
		transparentPanel.add(tfCardAmount, "cell 2 2,growx");
		
				tfCardAmount.setFont(new Font("Dialog", Font.BOLD, 12));
				tfCardAmount.setText("0");
				jLabel3 = new javax.swing.JLabel();
				jLabel3.setFont(new Font("Dialog", Font.BOLD, 12));
				transparentPanel.add(jLabel3, "cell 0 3,alignx right");
				jLabel3.setText("AUTHORIZATION CODE");
				tfAuthorizationCode = new FocusedTextField(false);
				tfAuthorizationCode.setHorizontalAlignment(SwingConstants.RIGHT);
				tfAuthorizationCode.setColumns(20);
				transparentPanel.add(tfAuthorizationCode, "cell 2 3,alignx center");
				
						tfAuthorizationCode.setFont(new Font("Dialog", Font.BOLD, 12));
						tfAuthorizationCode.setText("0");
						
						TransparentPanel transparentPanel_1 = new TransparentPanel();
						transparentPanel8.add(transparentPanel_1);
						transparentPanel_1.setLayout(new BorderLayout(5, 5));
						transparentPanel10 = new com.floreantpos.swing.TransparentPanel();
						transparentPanel_1.add(transparentPanel10, BorderLayout.WEST);
						btnMasterCard = new com.floreantpos.swing.POSToggleButton();
						btnVisa = new com.floreantpos.swing.POSToggleButton();
						btnEmEx = new com.floreantpos.swing.POSToggleButton();
						btnDiscover = new com.floreantpos.swing.POSToggleButton();
						
								transparentPanel10.setLayout(new java.awt.GridLayout(0, 1, 5, 5));
								
										buttonGroup1.add(btnMasterCard);
										btnMasterCard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/master_card.PNG")));
										btnMasterCard.setSelected(true);
										btnMasterCard.setActionCommand("MASTER_CARD");
										btnMasterCard.setPreferredSize(new java.awt.Dimension(90, 0));
										transparentPanel10.add(btnMasterCard);
										
												buttonGroup1.add(btnVisa);
												btnVisa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/visa_card.PNG")));
												btnVisa.setActionCommand("VISA_CARD");
												btnVisa.setPreferredSize(new java.awt.Dimension(90, 0));
												transparentPanel10.add(btnVisa);
												
														buttonGroup1.add(btnEmEx);
														btnEmEx.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/am_ex_card.PNG")));
														btnEmEx.setActionCommand("EMEX_CARD");
														btnEmEx.setPreferredSize(new java.awt.Dimension(90, 0));
														transparentPanel10.add(btnEmEx);
														
																buttonGroup1.add(btnDiscover);
																btnDiscover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/discover_card.PNG")));
																btnDiscover.setActionCommand("DISCOVER_CARD");
																btnDiscover.setPreferredSize(new java.awt.Dimension(90, 0));
																transparentPanel10.add(btnDiscover);
																transparentPanel9 = new com.floreantpos.swing.TransparentPanel();
																transparentPanel_1.add(transparentPanel9, BorderLayout.CENTER);
																posButton1 = new com.floreantpos.swing.PosButton();
																posButton2 = new com.floreantpos.swing.PosButton();
																posButton3 = new com.floreantpos.swing.PosButton();
																posButton4 = new com.floreantpos.swing.PosButton();
																posButton5 = new com.floreantpos.swing.PosButton();
																posButton6 = new com.floreantpos.swing.PosButton();
																posButton9 = new com.floreantpos.swing.PosButton();
																posButton8 = new com.floreantpos.swing.PosButton();
																posButton7 = new com.floreantpos.swing.PosButton();
																posButton10 = new com.floreantpos.swing.PosButton();
																posButton11 = new com.floreantpos.swing.PosButton();
																posButton12 = new com.floreantpos.swing.PosButton();
																
																		transparentPanel9.setLayout(new java.awt.GridLayout(0, 3, 5, 5));
																		
																				posButton1.setAction(calAction);
																				posButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/7_32.png")));
																				posButton1.setActionCommand("7");
																				posButton1.setFocusable(false);
																				transparentPanel9.add(posButton1);
																				
																						posButton2.setAction(calAction);
																						posButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/8_32.png")));
																						posButton2.setActionCommand("8");
																						posButton2.setFocusable(false);
																						transparentPanel9.add(posButton2);
																						
																								posButton3.setAction(calAction);
																								posButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/9_32.png")));
																								posButton3.setActionCommand("9");
																								posButton3.setFocusable(false);
																								transparentPanel9.add(posButton3);
																								
																										posButton4.setAction(calAction);
																										posButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/4_32.png")));
																										posButton4.setActionCommand("4");
																										posButton4.setFocusable(false);
																										transparentPanel9.add(posButton4);
																										
																												posButton5.setAction(calAction);
																												posButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/5_32.png")));
																												posButton5.setActionCommand("5");
																												posButton5.setFocusable(false);
																												transparentPanel9.add(posButton5);
																												
																														posButton6.setAction(calAction);
																														posButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/6_32.png")));
																														posButton6.setActionCommand("6");
																														posButton6.setFocusable(false);
																														transparentPanel9.add(posButton6);
																														
																																posButton9.setAction(calAction);
																																posButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/1_32.png")));
																																posButton9.setActionCommand("1");
																																posButton9.setFocusable(false);
																																transparentPanel9.add(posButton9);
																																
																																		posButton8.setAction(calAction);
																																		posButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/2_32.png")));
																																		posButton8.setActionCommand("2");
																																		posButton8.setFocusable(false);
																																		transparentPanel9.add(posButton8);
																																		
																																				posButton7.setAction(calAction);
																																				posButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/3_32.png")));
																																				posButton7.setActionCommand("3");
																																				posButton7.setFocusable(false);
																																				transparentPanel9.add(posButton7);
																																				
																																						posButton10.setAction(calAction);
																																						posButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/dot_32.png")));
																																						posButton10.setActionCommand(".");
																																						posButton10.setFocusable(false);
																																						transparentPanel9.add(posButton10);
																																						
																																								posButton11.setAction(calAction);
																																								posButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/0_32.png")));
																																								posButton11.setActionCommand("0");
																																								posButton11.setFocusable(false);
																																								transparentPanel9.add(posButton11);
																																								
																																										posButton12.setAction(calAction);
																																										posButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/clear_32.png")));
																																										posButton12.setText(com.floreantpos.POSConstants.CLEAR);
																																										posButton12.setFocusable(false);
																																										transparentPanel9.add(posButton12);
	}

	Dimension preferredSize = new Dimension(380, 100);

	public Dimension getPreferredSize() {
		return preferredSize;
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		buttonGroup1 = new javax.swing.ButtonGroup();
		transparentPanel5 = new com.floreantpos.swing.TransparentPanel();
		btnChangePMethod = new com.floreantpos.swing.PosButton();
		btnCancel = new com.floreantpos.swing.PosButton();
		btnSettle = new com.floreantpos.swing.PosButton();
		transparentPanel8 = new com.floreantpos.swing.TransparentPanel();

		setLayout(new java.awt.BorderLayout(0, 5));

		setBorder(javax.swing.BorderFactory.createTitledBorder(null, com.floreantpos.POSConstants.TITLE, javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

		transparentPanel5.setLayout(new java.awt.GridLayout(1, 0, 5, 5));

		transparentPanel5.setPreferredSize(new java.awt.Dimension(100, 60));
		btnChangePMethod.setText("<html><body><p align='center'>CHANGE PAYMENT METHOD</p></body></html>");
		btnChangePMethod.setFocusable(false);
		btnChangePMethod.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnChangePMethodActionPerformed(evt);
			}
		});

		transparentPanel5.add(btnChangePMethod);

		btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cancel_32.png")));
		btnCancel.setText(com.floreantpos.POSConstants.CANCEL);
		btnCancel.setFocusable(false);
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnCancelActionPerformed(evt);
			}
		});

		transparentPanel5.add(btnCancel);

		btnSettle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/settle_ticket_32.png")));
		btnSettle.setText(com.floreantpos.POSConstants.SETTLE);
		btnSettle.setFocusable(false);
		btnSettle.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnSettleActionPerformed(evt);
			}
		});

		transparentPanel5.add(btnSettle);

		add(transparentPanel5, java.awt.BorderLayout.SOUTH);
		transparentPanel8.setLayout(new BorderLayout(10, 10));
		
		transparentPanel = new TransparentPanel();
		transparentPanel8.add(transparentPanel, BorderLayout.NORTH);
		transparentPanel.setLayout(new MigLayout("", "[][grow][]", "[][][][]"));
		
		lblTotal = new JLabel("DUE AMOUNT");
		lblTotal.setFont(new Font("Dialog", Font.BOLD, 12));
		transparentPanel.add(lblTotal, "cell 0 0,alignx right");

		add(transparentPanel8, java.awt.BorderLayout.CENTER);

	}// </editor-fold>//GEN-END:initComponents

	private void btnSettleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSettleActionPerformed
		try {
			try {
				String gratuity = tfGratuityAmount.getText();
				if (gratuity == null || gratuity.trim().equals("")) {
					gratuityAmount = 0;
				}
				else {
					gratuityAmount = Double.parseDouble(gratuity);
				}
			} catch (NumberFormatException x) {
				POSMessageDialog.showError("Gratuity amount is not valid");
				return;
			}
			double tenderedAmount = 0;
			try {
				tenderedAmount = Double.parseDouble(tfCardAmount.getText());
			} catch (NumberFormatException x) {
				POSMessageDialog.showError("Amount is not valid");
				return;
			}

			if (tenderedAmount < 0) {
				POSMessageDialog.showError("Insufficient amount");
				return;
			}

			String authorizationCode = tfAuthorizationCode.getText();
			if (getCardType() == CARD_TYPE_CREDIT) {
				
				settleTickets(tenderedAmount, gratuityAmount, new CreditCardTransaction(), getWhichCard(), authorizationCode);
			}
			if (getCardType() == CARD_TYPE_DEBIT) {
				settleTickets(tenderedAmount, gratuityAmount, new DebitCardTransaction(), getWhichCard(), authorizationCode);
			}
		} catch (Exception e) {
			POSMessageDialog.showError("An unexpected error has occured, you may need to restart the application", e);
		}
	}//GEN-LAST:event_btnSettleActionPerformed

	private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
		RootView.getInstance().showView(SwitchboardView.VIEW_NAME);
	}//GEN-LAST:event_btnCancelActionPerformed

	private void btnChangePMethodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangePMethodActionPerformed
		changePaymentMethod();
	}//GEN-LAST:event_btnChangePMethodActionPerformed

	private JTextField getFocusedTextField() {
		if (tfGratuityAmount.hasFocus()) {
			return tfGratuityAmount;
		}
		if (tfAuthorizationCode.hasFocus()) {
			return tfAuthorizationCode;
		}
		if (tfCardAmount.hasFocus()) {
			return tfCardAmount;
		}
		tfGratuityAmount.requestFocus();
		return tfGratuityAmount;
	}

	private Action calAction = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			JTextField tf = getFocusedTextField();

			String s = e.getActionCommand();
			if (s.equals(com.floreantpos.POSConstants.CLEAR)) {
				tf.setText("0");
			}
			else if (s.equals(".")) {
				if (tf == tfAuthorizationCode) {
					return;
				}
				if (tf.getText().indexOf('.') < 0) {
					tf.setText(tf.getText() + ".");
				}
			}
			else {
				String string = tf.getText();
				int index = string.indexOf('.');
				if (index < 0) {
					double value = 0;
					try {
						value = Double.parseDouble(string);
					} catch (NumberFormatException x) {
						Toolkit.getDefaultToolkit().beep();
					}
					if (value == 0) {
						tf.setText(s);
					}
					else {
						tf.setText(string + s);
					}
				}
				else {
					tf.setText(string + s);
				}
			}
			if (tf == tfGratuityAmount) {
				double gAmount = 0;
				double amount = getDueAmount();
				try {
					gAmount = Double.parseDouble(tfGratuityAmount.getText());
				} catch (NumberFormatException x) {
					Toolkit.getDefaultToolkit().beep();
					return;
				}
				tfCardAmount.setText(Application.formatNumber(amount + gAmount));
			}
		}
	};

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private com.floreantpos.swing.PosButton btnCancel;
	private com.floreantpos.swing.PosButton btnChangePMethod;
	private com.floreantpos.swing.POSToggleButton btnDiscover;
	private com.floreantpos.swing.POSToggleButton btnEmEx;
	private com.floreantpos.swing.POSToggleButton btnMasterCard;
	private com.floreantpos.swing.PosButton btnSettle;
	private com.floreantpos.swing.POSToggleButton btnVisa;
	private javax.swing.ButtonGroup buttonGroup1;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel lblCSymbol1;
	private javax.swing.JLabel lblCSymbol2;
	private com.floreantpos.swing.PosButton posButton1;
	private com.floreantpos.swing.PosButton posButton10;
	private com.floreantpos.swing.PosButton posButton11;
	private com.floreantpos.swing.PosButton posButton12;
	private com.floreantpos.swing.PosButton posButton2;
	private com.floreantpos.swing.PosButton posButton3;
	private com.floreantpos.swing.PosButton posButton4;
	private com.floreantpos.swing.PosButton posButton5;
	private com.floreantpos.swing.PosButton posButton6;
	private com.floreantpos.swing.PosButton posButton7;
	private com.floreantpos.swing.PosButton posButton8;
	private com.floreantpos.swing.PosButton posButton9;
	private FocusedTextField tfAuthorizationCode;
	private FocusedTextField tfCardAmount;
	private FocusedTextField tfGratuityAmount;
	private com.floreantpos.swing.TransparentPanel transparentPanel10;
	private com.floreantpos.swing.TransparentPanel transparentPanel5;
	private com.floreantpos.swing.TransparentPanel transparentPanel8;
	private com.floreantpos.swing.TransparentPanel transparentPanel9;
	private TransparentPanel transparentPanel;
	private JLabel lblTotal;
	private FocusedTextField tfDueAmount;
	private JLabel lblCSymbol3;

	// End of variables declaration//GEN-END:variables

	public int getCardType() {
		return cardType;
	}

	public void setCardType(int cardType) {
		this.cardType = cardType;

		if (cardType == CARD_TYPE_DEBIT) {
			btnEmEx.setVisible(false);
			btnDiscover.setVisible(false);
			TitledBorder titledBorder = new TitledBorder("Debit Card Transaction");
			titledBorder.setTitleJustification(TitledBorder.CENTER);
			setBorder(titledBorder);
		}
		else {
			btnEmEx.setVisible(true);
			btnDiscover.setVisible(true);
			TitledBorder titledBorder = new TitledBorder("Credit Card Transaction");
			titledBorder.setTitleJustification(TitledBorder.CENTER);
			setBorder(titledBorder);
		}
	}

	public String getWhichCard() {
		return whichCard;
	}

	public void setWhichCard(String whichCard) {
		this.whichCard = whichCard;
	}

	@Override
	public void updateView() {
		String currencySymbol = Application.getCurrencySymbol();
		lblCSymbol1.setText(currencySymbol);
		lblCSymbol2.setText(currencySymbol);
		lblCSymbol3.setText(currencySymbol);
		
		double dueAmount = getDueAmount();
		double tips = calculateGratuity();
		
		tfDueAmount.setText(Application.formatNumber(dueAmount));
		tfGratuityAmount.setText(Application.formatNumber(tips));
		tfCardAmount.setText(Application.formatNumber(dueAmount + tips));
		
	}
}
