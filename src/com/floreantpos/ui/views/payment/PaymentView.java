package com.floreantpos.ui.views.payment;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.IconFactory;
import com.floreantpos.POSConstants;
import com.floreantpos.model.Gratuity;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.NumberUtil;

public class PaymentView extends JPanel {
	private static final String ADD = "0";

	private static final String REMOVE = "1";

	protected SettleTicketDialog settleTicketView;

	private PosButton btnGratuity;
	private com.floreantpos.swing.PosButton btnCancel;
	private com.floreantpos.swing.PosButton btnFinish;
	private com.floreantpos.swing.TransparentPanel calcButtonPanel;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel6;
	private com.floreantpos.swing.TransparentPanel actionButtonPanel;
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
	private com.floreantpos.swing.TransparentPanel transparentPanel1;
//	private POSToggleButton btnTaxExempt;
	private PosButton btnCoupon;
	private PosButton btnViewCoupons;
	private JLabel label;

	public PaymentView(SettleTicketDialog settleTicketView) {
		this.settleTicketView = settleTicketView;

		initComponents();

		//		btnUseKalaId.setActionCommand(ADD);
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
		actionButtonPanel = new com.floreantpos.swing.TransparentPanel();
		transparentPanel1 = new com.floreantpos.swing.TransparentPanel();
		jLabel4 = new javax.swing.JLabel();
		jLabel6 = new javax.swing.JLabel();
		tfDueAmount = new com.floreantpos.swing.FocusedTextField();
		tfDueAmount.setFocusable(false);
		tfAmountTendered = new com.floreantpos.swing.FocusedTextField();

		setLayout(new MigLayout("", "[fill,grow]", "[73px][grow,fill][grow,fill][][shrink 0]"));

		calcButtonPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 5));
		calcButtonPanel.setLayout(new java.awt.GridLayout(0, 3, 5, 5));

		posButton1.setAction(calAction);
		posButton1.setIcon(IconFactory.getIcon("/ui_icons/", "7.png")); // NOI18N
		posButton1.setActionCommand("7");
		posButton1.setFocusable(false);
		calcButtonPanel.add(posButton1);

		posButton2.setAction(calAction);
		posButton2.setIcon(IconFactory.getIcon("/ui_icons/", "8.png")); // NOI18N
		posButton2.setActionCommand("8");
		posButton2.setFocusable(false);
		calcButtonPanel.add(posButton2);

		posButton3.setAction(calAction);
		posButton3.setIcon(IconFactory.getIcon("/ui_icons/", "9.png")); // NOI18N
		posButton3.setActionCommand("9");
		posButton3.setFocusable(false);
		calcButtonPanel.add(posButton3);

		posButton4.setAction(calAction);
		posButton4.setIcon(IconFactory.getIcon("/ui_icons/", "4.png")); // NOI18N
		posButton4.setActionCommand("4");
		posButton4.setFocusable(false);
		calcButtonPanel.add(posButton4);

		posButton5.setAction(calAction);
		posButton5.setIcon(IconFactory.getIcon("/ui_icons/", "5.png")); // NOI18N
		posButton5.setActionCommand("5");
		posButton5.setFocusable(false);
		calcButtonPanel.add(posButton5);

		posButton6.setAction(calAction);
		posButton6.setIcon(IconFactory.getIcon("/ui_icons/", "6.png")); // NOI18N
		posButton6.setActionCommand("6");
		posButton6.setFocusable(false);
		calcButtonPanel.add(posButton6);

		posButton9.setAction(calAction);
		posButton9.setIcon(IconFactory.getIcon("/ui_icons/", "1.png")); // NOI18N
		posButton9.setActionCommand(REMOVE);
		posButton9.setFocusable(false);
		calcButtonPanel.add(posButton9);

		posButton8.setAction(calAction);
		posButton8.setIcon(IconFactory.getIcon("/ui_icons/", "2.png")); // NOI18N
		posButton8.setActionCommand("2");
		posButton8.setFocusable(false);
		calcButtonPanel.add(posButton8);

		posButton7.setAction(calAction);
		posButton7.setIcon(IconFactory.getIcon("/ui_icons/", "3.png")); // NOI18N
		posButton7.setActionCommand("3");
		posButton7.setFocusable(false);
		calcButtonPanel.add(posButton7);

		posButton11.setAction(calAction);
		posButton11.setIcon(IconFactory.getIcon("/ui_icons/", "0.png")); // NOI18N
		posButton11.setActionCommand(ADD);
		posButton11.setFocusable(false);
		calcButtonPanel.add(posButton11);

		posButton10.setAction(calAction);
		posButton10.setIcon(IconFactory.getIcon("/ui_icons/", "dot.png")); // NOI18N
		posButton10.setActionCommand(".");
		posButton10.setFocusable(false);
		calcButtonPanel.add(posButton10);

		posButton12.setAction(calAction);
		posButton12.setIcon(IconFactory.getIcon("/ui_icons/", "clear.png")); // NOI18N
		posButton12.setText("CLEAR");
		posButton12.setFocusable(false);
		calcButtonPanel.add(posButton12);

		add(calcButtonPanel, "cell 0 1,grow, gapy 0 20px");
		actionButtonPanel.setLayout(new MigLayout("", "[fill,grow][fill,grow]", "[][][][]"));

//		btnTaxExempt = new POSToggleButton(com.floreantpos.POSConstants.TAX_EXEMPT);
//		btnTaxExempt.setText("TAX EXEMPT");
//		btnTaxExempt.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				settleTicketView.doTaxExempt(btnTaxExempt.isSelected());
//			}
//		});


//		btnMyKalaDiscount = new PosButton();
//		btnMyKalaDiscount.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				settleTicketView.submitMyKalaDiscount();
//			}
//		});
//
//		btnMyKalaDiscount.setText("LOYALTY DISCOUNT");
//		jPanel4.add(btnMyKalaDiscount, "cell 1 0,growx");
		
		btnFinish = new com.floreantpos.swing.PosButton(POSConstants.PAY.toUpperCase());
		actionButtonPanel.add(btnFinish, "span 2, growx, wrap");
		btnFinish.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doFinish(evt);
			}
		});
		
		btnGratuity = new PosButton(com.floreantpos.POSConstants.ADD_GRATUITY_TEXT);
		actionButtonPanel.add(btnGratuity, "growx");
		btnGratuity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSetGratuity();
			}
		});
		
		btnCoupon = new PosButton(com.floreantpos.POSConstants.COUPON_DISCOUNT);
		actionButtonPanel.add(btnCoupon, "growx, wrap");
		btnCoupon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				settleTicketView.doApplyCoupon();
			}
		});

		btnViewCoupons = new PosButton(com.floreantpos.POSConstants.VIEW_DISCOUNTS);
		actionButtonPanel.add(btnViewCoupons, "growx");
		btnViewCoupons.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				settleTicketView.doViewDiscounts();
			}
		});

		btnCancel = new com.floreantpos.swing.PosButton(POSConstants.CANCEL.toUpperCase());
		actionButtonPanel.add(btnCancel, "growx");
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnCancelActionPerformed(evt);
			}
		});

		label = new JLabel("");
		add(label, "cell 0 2,growy");

		add(actionButtonPanel, "cell 0 4,grow");

		transparentPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 15, 15, 15));

		jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
		jLabel4.setText("DUE AMOUNT:");

		jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
		jLabel6.setText("TENDERED AMOUNT:");

		tfDueAmount.setEditable(false);
		tfDueAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

		tfAmountTendered.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

		add(transparentPanel1, "cell 0 0,growx,aligny top");

		transparentPanel1.setLayout(new MigLayout("", "[][grow,fill]", "[19px][][19px]"));
		transparentPanel1.add(jLabel4, "cell 0 0,alignx right,aligny center");

		transparentPanel1.add(jLabel6, "cell 0 2,alignx left,aligny center");
		transparentPanel1.add(tfDueAmount, "cell 1 0,growx,aligny top");
		transparentPanel1.add(tfAmountTendered, "cell 1 2,growx,aligny top");
	}// </editor-fold>//GEN-END:initComponents

	protected void removeKalaId() {
		Ticket ticket = settleTicketView.getTicket();
		ticket.getProperties().remove(SettleTicketDialog.LOYALTY_ID);
		TicketDAO.getInstance().saveOrUpdate(ticket);

		POSMessageDialog.showMessage("Loyalty Id removed");
	}

	public void addMyKalaId() {
		String loyaltyid = JOptionPane.showInputDialog("Enter loyalty id:");

		if (StringUtils.isEmpty(loyaltyid)) {
			return;
		}

		Ticket ticket = settleTicketView.getTicket();
		ticket.addProperty(SettleTicketDialog.LOYALTY_ID, loyaltyid);
		TicketDAO.getInstance().saveOrUpdate(ticket);

		POSMessageDialog.showMessage("Loyalty id set.");
	}

	protected void doSetGratuity() {
		settleTicketView.doSetGratuity();
	}

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

	Action calAction = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			JTextField textField = tfAmountTendered;

			PosButton button = (PosButton) e.getSource();
			String s = button.getActionCommand();
			if (s.equals("CLEAR")) {
				textField.setText(ADD);
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
//	private PosButton btnMyKalaDiscount;

	public void updateView() {
//		btnTaxExempt.setEnabled(true);
//		btnTaxExempt.setSelected(settleTicketView.getTicket().isTaxExempt());

		double dueAmount = getDueAmount();

		tfDueAmount.setText(NumberUtil.formatNumber(dueAmount));
		tfAmountTendered.setText(NumberUtil.formatNumber(dueAmount));
	}

	public double getTenderedAmount() throws ParseException {
		double doubleValue = NumberUtil.parse(tfAmountTendered.getText()).doubleValue();
		return doubleValue;
	}

	public SettleTicketDialog getSettleTicketView() {
		return settleTicketView;
	}

	public void setSettleTicketView(SettleTicketDialog settleTicketView) {
		this.settleTicketView = settleTicketView;
	}

	protected double getPaidAmount() {
		return settleTicketView.getTicket().getPaidAmount();
	}

	protected double getDueAmount() {
		return settleTicketView.getTicket().getDueAmount();
	}

	protected double getAdvanceAmount() {
		Gratuity gratuity = settleTicketView.getTicket().getGratuity();
		return gratuity != null ? gratuity.getAmount() : 0;
	}

	protected double getTotalGratuity() {
		return settleTicketView.getTicket().getPaidAmount();
	}

	public void setDefaultFocus() {
		tfAmountTendered.requestFocus();
	}

}
