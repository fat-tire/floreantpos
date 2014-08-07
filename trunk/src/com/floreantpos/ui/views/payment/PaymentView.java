package com.floreantpos.ui.views.payment;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.model.Ticket;
import com.floreantpos.swing.POSToggleButton;
import com.floreantpos.swing.PosButton;
import com.floreantpos.util.NumberUtil;

public class PaymentView extends JPanel {
	protected SettleTicketView settleTicketView;

	private com.floreantpos.swing.PosButton btnCancel;
	private com.floreantpos.swing.PosButton btnFinish;
	private com.floreantpos.swing.TransparentPanel calcButtonPanel;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private com.floreantpos.swing.TransparentPanel jPanel4;
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
	private com.floreantpos.swing.FocusedTextField tfAmountTendered;
	private com.floreantpos.swing.FocusedTextField tfDueAmount;
	private com.floreantpos.swing.FocusedTextField tfGratuityAmount;
	private com.floreantpos.swing.TransparentPanel transparentPanel1;
	private POSToggleButton btnTaxExempt;
	private PosButton btnCoupon;
	private PosButton btnViewCoupons;
	private JLabel label;

	public PaymentView(SettleTicketView settleTicketView) {
		this.settleTicketView = settleTicketView;
		
		initComponents();

		tfAmountTendered.setText("0");
	}

	private void initComponents() {
		calcButtonPanel = new com.floreantpos.swing.TransparentPanel();
		posButton1 = new com.floreantpos.swing.PosButton();
		posButton2 = new com.floreantpos.swing.PosButton();
		posButton3 = new com.floreantpos.swing.PosButton();
		posButton4 = new com.floreantpos.swing.PosButton();
		posButton5 = new com.floreantpos.swing.PosButton();
		posButton6 = new com.floreantpos.swing.PosButton();
		posButton9 = new com.floreantpos.swing.PosButton();
		posButton8 = new com.floreantpos.swing.PosButton();
		posButton7 = new com.floreantpos.swing.PosButton();
		posButton11 = new com.floreantpos.swing.PosButton();
		posButton10 = new com.floreantpos.swing.PosButton();
		posButton12 = new com.floreantpos.swing.PosButton();
		jPanel4 = new com.floreantpos.swing.TransparentPanel();
		transparentPanel1 = new com.floreantpos.swing.TransparentPanel();
		jLabel4 = new javax.swing.JLabel();
		jLabel5 = new javax.swing.JLabel();
		jLabel6 = new javax.swing.JLabel();
		tfDueAmount = new com.floreantpos.swing.FocusedTextField();
		tfGratuityAmount = new com.floreantpos.swing.FocusedTextField();
		tfAmountTendered = new com.floreantpos.swing.FocusedTextField();

		setLayout(new MigLayout("", "[456px]", "[73px][251px][grow,fill][][shrink 0]"));

		calcButtonPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 5));
		calcButtonPanel.setLayout(new java.awt.GridLayout(0, 3, 5, 5));

		posButton1.setAction(calAction);
		posButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/7_32.png"))); // NOI18N
		posButton1.setActionCommand("7");
		posButton1.setFocusable(false);
		calcButtonPanel.add(posButton1);

		posButton2.setAction(calAction);
		posButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/8_32.png"))); // NOI18N
		posButton2.setActionCommand("8");
		posButton2.setFocusable(false);
		calcButtonPanel.add(posButton2);

		posButton3.setAction(calAction);
		posButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/9_32.png"))); // NOI18N
		posButton3.setActionCommand("9");
		posButton3.setFocusable(false);
		calcButtonPanel.add(posButton3);

		posButton4.setAction(calAction);
		posButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/4_32.png"))); // NOI18N
		posButton4.setActionCommand("4");
		posButton4.setFocusable(false);
		calcButtonPanel.add(posButton4);

		posButton5.setAction(calAction);
		posButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/5_32.png"))); // NOI18N
		posButton5.setActionCommand("5");
		posButton5.setFocusable(false);
		calcButtonPanel.add(posButton5);

		posButton6.setAction(calAction);
		posButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/6_32.png"))); // NOI18N
		posButton6.setActionCommand("6");
		posButton6.setFocusable(false);
		calcButtonPanel.add(posButton6);

		posButton9.setAction(calAction);
		posButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/1_32.png"))); // NOI18N
		posButton9.setActionCommand("1");
		posButton9.setFocusable(false);
		calcButtonPanel.add(posButton9);

		posButton8.setAction(calAction);
		posButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/2_32.png"))); // NOI18N
		posButton8.setActionCommand("2");
		posButton8.setFocusable(false);
		calcButtonPanel.add(posButton8);

		posButton7.setAction(calAction);
		posButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/3_32.png"))); // NOI18N
		posButton7.setActionCommand("3");
		posButton7.setFocusable(false);
		calcButtonPanel.add(posButton7);

		posButton11.setAction(calAction);
		posButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/0_32.png"))); // NOI18N
		posButton11.setActionCommand("0");
		posButton11.setFocusable(false);
		calcButtonPanel.add(posButton11);

		posButton10.setAction(calAction);
		posButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/dot_32.png"))); // NOI18N
		posButton10.setActionCommand(".");
		posButton10.setFocusable(false);
		calcButtonPanel.add(posButton10);

		posButton12.setAction(calAction);
		posButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/clear_32.png"))); // NOI18N
		posButton12.setText("CLEAR");
		posButton12.setFocusable(false);
		calcButtonPanel.add(posButton12);

		add(calcButtonPanel, "cell 0 1,grow");
		jPanel4.setLayout(new MigLayout("", "[222px][222px]", "[][][]"));

		btnTaxExempt = new POSToggleButton(com.floreantpos.POSConstants.TAX_EXEMPT);
		btnTaxExempt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				settleTicketView.doTaxExempt(btnTaxExempt.isSelected());
			}
		});
		jPanel4.add(btnTaxExempt, "cell 0 0 2 1,grow");

		btnCoupon = new PosButton(com.floreantpos.POSConstants.COUPON_DISCOUNT);
		btnCoupon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				settleTicketView.doApplyCoupon();
			}
		});
		jPanel4.add(btnCoupon, "cell 0 1,grow");

		btnViewCoupons = new PosButton(com.floreantpos.POSConstants.VIEW_DISCOUNTS);
		btnViewCoupons.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				settleTicketView.doViewDiscounts();
			}
		});
		jPanel4.add(btnViewCoupons, "cell 1 1,grow");

		btnFinish = new com.floreantpos.swing.PosButton();
		jPanel4.add(btnFinish, "cell 1 2,grow");
		btnFinish.setText("PAY");
		btnCancel = new com.floreantpos.swing.PosButton();
		jPanel4.add(btnCancel, "cell 0 2,grow");
		btnCancel.setText("CANCEL");
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnCancelActionPerformed(evt);
			}
		});
		btnFinish.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doFinish(evt);
			}
		});

		label = new JLabel("");
		add(label, "cell 0 2,growy");

		add(jPanel4, "cell 0 4,grow");

		transparentPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 5),
				javax.swing.BorderFactory.createTitledBorder("")));

		jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
		jLabel4.setText("DUE AMOUNT:");

		jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
		jLabel5.setText("GRATUITY AMOUNT:");

		jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
		jLabel6.setText("TENDERED AMOUNT:");

		tfDueAmount.setEditable(false);
		tfDueAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
		tfDueAmount.setText("focusedTextField1");
		tfDueAmount.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

		tfGratuityAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
		tfGratuityAmount.setText("focusedTextField2");
		tfGratuityAmount.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

		tfAmountTendered.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
		tfAmountTendered.setText("focusedTextField3");
		tfAmountTendered.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

		javax.swing.GroupLayout transparentPanel1Layout = new javax.swing.GroupLayout(transparentPanel1);
		transparentPanel1.setLayout(transparentPanel1Layout);
		transparentPanel1Layout.setHorizontalGroup(transparentPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				transparentPanel1Layout
						.createSequentialGroup()
						.addGroup(
								transparentPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
										.addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
										.addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(
								transparentPanel1Layout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(tfDueAmount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(tfGratuityAmount, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
										.addComponent(tfAmountTendered, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE))));
		transparentPanel1Layout.setVerticalGroup(transparentPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				transparentPanel1Layout
						.createSequentialGroup()
						.addGroup(
								transparentPanel1Layout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel4)
										.addComponent(tfDueAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(
								transparentPanel1Layout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel5)
										.addComponent(tfGratuityAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(
								transparentPanel1Layout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel6)
										.addComponent(tfAmountTendered, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE))));

		add(transparentPanel1, "cell 0 0,growx,aligny top");
	}// </editor-fold>//GEN-END:initComponents

	protected void doTaxExempt() {
	}

	private void doFinish(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doFinish
		settleTicketView.doSettle();
	}//GEN-LAST:event_doFinish

	private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
		settleTicketView.setCanceled(true);
		settleTicketView.dispose();
	}//GEN-LAST:event_btnCancelActionPerformed

	// End of variables declaration//GEN-END:variables

	public void resetView() {
		tfAmountTendered.setText("0");
	}

	Action calAction = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			JTextField textField = tfAmountTendered;

			if (tfGratuityAmount.hasFocus()) {
				textField = tfGratuityAmount;
			}

			PosButton button = (PosButton) e.getSource();
			String s = button.getActionCommand();
			if (s.equals("CLEAR")) {
				textField.setText("0");
			}
			else if (s.equals(".")) {
				if (textField.getText().indexOf('.') < 0) {
					textField.setText(textField.getText() + ".");
				}
			}
			else {
				String string = textField.getText();
				int index = string.indexOf('.');
				if (index < 0) {
					double value = 0;
					try {
						value = Double.parseDouble(string);
					} catch (NumberFormatException x) {
						Toolkit.getDefaultToolkit().beep();
					}
					if (value == 0) {
						textField.setText(s);
					}
					else {
						textField.setText(string + s);
					}
				}
				else {
					textField.setText(string + s);
				}
			}
		}
	};

	public void updateView() {
		List<Ticket> tickets = settleTicketView.getTicketsToSettle();
		if (tickets.size() == 1) {
			btnTaxExempt.setEnabled(true);
			btnTaxExempt.setSelected(tickets.get(0).isTaxExempt());
		}
		else {
			btnTaxExempt.setEnabled(false);
		}

		double dueAmount = getDueAmount();
		double tips = calculateGratuity();

		tfDueAmount.setText(NumberUtil.formatNumber(dueAmount));
		tfGratuityAmount.setText(NumberUtil.formatNumber(tips));
		tfAmountTendered.setText(NumberUtil.formatNumber(dueAmount + tips));
		tfAmountTendered.requestFocus();
	}

	public double getTenderedAmount() throws ParseException {
		NumberFormat numberFormat = NumberFormat.getInstance();
		double doubleValue = numberFormat.parse(tfAmountTendered.getText()).doubleValue();
		return doubleValue;

		//return Double.parseDouble(tfAmountTendered.getText());
	}

	public double getGratuityAmount() {
		try {
			NumberFormat numberFormat = NumberFormat.getInstance();
			return numberFormat.parse(tfGratuityAmount.getText()).doubleValue();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		//return Double.parseDouble(tfGratuityAmount.getText());
	}

	public SettleTicketView getSettleTicketView() {
		return settleTicketView;
	}

	public void setSettleTicketView(SettleTicketView settleTicketView) {
		this.settleTicketView = settleTicketView;
	}

	protected double getPaidAmount() {
		List<Ticket> ticketsToSettle = settleTicketView.getTicketsToSettle();
		if (ticketsToSettle == null) {
			return 0;
		}

		double total = 0;
		for (Ticket ticket : ticketsToSettle) {
			total += ticket.getPaidAmount();
		}
		return total;
	}

	protected double getDueAmount() {
		List<Ticket> ticketsToSettle = settleTicketView.getTicketsToSettle();
		if (ticketsToSettle == null) {
			return 0;
		}

		double total = 0;
		for (Ticket ticket : ticketsToSettle) {
			total += ticket.getDueAmount();
		}
		return total;
	}

	protected double getTotalGratuity() {
		List<Ticket> ticketsToSettle = settleTicketView.getTicketsToSettle();
		if (ticketsToSettle == null) {
			return 0;
		}

		double total = 0;
		for (Ticket ticket : ticketsToSettle) {
			if (ticket.getGratuity() != null) {
				total += ticket.getGratuity().getAmount();
			}
		}
		return total;
	}

	protected double calculateGratuity() {
		List<Ticket> ticketsToSettle = settleTicketView.getTicketsToSettle();
		if (ticketsToSettle == null) {
			return 0;
		}

		Ticket ticket = ticketsToSettle.get(0);

		if (!Ticket.DINE_IN.equals(ticket.getTicketType())) {
			return 0.0;
		}

		double dueAmount = getDueAmount();
		double tips = getTotalGratuity();
		if (tips == 0) {
			tips = dueAmount * 0.15;
		}

		return tips;
	}

	
}
