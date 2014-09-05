/*
 * TicketView.java
 *
 * Created on August 4, 2006, 3:42 PM
 */

package com.floreantpos.ui.views.order;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.hibernate.StaleObjectStateException;

import com.floreantpos.POSConstants;
import com.floreantpos.PosException;
import com.floreantpos.main.Application;
import com.floreantpos.model.CookingInstruction;
import com.floreantpos.model.ITicketItem;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemCookingInstruction;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.dao.CookingInstructionDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.print.PosPrintService;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.CookingInstructionSelectionView;
import com.floreantpos.ui.views.SwitchboardView;
import com.floreantpos.ui.views.order.actions.OrderListener;
import com.floreantpos.util.NumberUtil;

/**
 *
 * @author  MShahriar
 */
public class TicketView extends JPanel {
	private java.util.Vector<OrderListener> orderListeners = new java.util.Vector<OrderListener>();
	private Ticket ticket;

	public final static String VIEW_NAME = "TICKET_VIEW";

	public TicketView() {
		initComponents();

		chkTaxExempt.setEnabled(false);
		btnAddCookingInstruction.setEnabled(false);
		btnIncreaseAmount.setEnabled(false);
		btnDecreaseAmount.setEnabled(false);

		ticketViewerTable.setRowHeight(35);
		ticketViewerTable.getRenderer().setInTicketScreen(true);
		ticketViewerTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					updateSelectionView();
				}
			}

		});

		ticketViewerTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {

				Object selected = ticketViewerTable.getSelected();
				if (!(selected instanceof ITicketItem)) {
					return;
				}

				ITicketItem item = (ITicketItem) selected;

				Boolean printedToKitchen = item.isPrintedToKitchen();

				btnAddCookingInstruction.setEnabled(item.canAddCookingInstruction());
				btnIncreaseAmount.setEnabled(!printedToKitchen);
				btnDecreaseAmount.setEnabled(!printedToKitchen);
			}

		});
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
	private void initComponents() {
		jPanel1 = new com.floreantpos.swing.TransparentPanel();
		ticketAmountPanel = new com.floreantpos.swing.TransparentPanel();
		controlPanel = new com.floreantpos.swing.TransparentPanel();
		controlPanel.setOpaque(true);
		btnPay = new com.floreantpos.swing.PosButton();
		btnCancel = new com.floreantpos.swing.PosButton();
		btnFinish = new com.floreantpos.swing.PosButton();
		scrollerPanel = new com.floreantpos.swing.TransparentPanel();
		btnIncreaseAmount = new com.floreantpos.swing.PosButton();
		btnDecreaseAmount = new com.floreantpos.swing.PosButton();
		btnScrollUp = new com.floreantpos.swing.PosButton();
		btnScrollDown = new com.floreantpos.swing.PosButton();
		jPanel2 = new com.floreantpos.swing.TransparentPanel();
		ticketViewerTable = new com.floreantpos.ui.ticket.TicketViewerTable();
		jScrollPane1 = new javax.swing.JScrollPane(ticketViewerTable);

		setBorder(javax.swing.BorderFactory.createTitledBorder(null, com.floreantpos.POSConstants.TICKET, javax.swing.border.TitledBorder.CENTER,
				javax.swing.border.TitledBorder.DEFAULT_POSITION));
		setPreferredSize(new java.awt.Dimension(420, 463));
		setLayout(new java.awt.BorderLayout(5, 5));
		jPanel1.setLayout(new BorderLayout(5, 5));
		ticketAmountPanel.setLayout(new MigLayout("alignx trailing,fill", "[grow][]", "[][][][][][][][]"));
		jLabel5 = new javax.swing.JLabel();

		jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12));
		jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel5.setText(com.floreantpos.POSConstants.SUBTOTAL + ":");
		ticketAmountPanel.add(jLabel5, "cell 0 1,growx,aligny center");
		tfSubtotal = new javax.swing.JTextField();
		tfSubtotal.setHorizontalAlignment(SwingConstants.TRAILING);

		tfSubtotal.setEditable(false);
		tfSubtotal.setFont(new java.awt.Font("Tahoma", 1, 12));
		ticketAmountPanel.add(tfSubtotal, "cell 1 1,growx,aligny center");
		jLabel1 = new javax.swing.JLabel();

		jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12));
		jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel1.setText(com.floreantpos.POSConstants.DISCOUNT + ":");
		ticketAmountPanel.add(jLabel1, "cell 0 2,growx,aligny center");
		tfDiscount = new javax.swing.JTextField();
		tfDiscount.setHorizontalAlignment(SwingConstants.TRAILING);

		tfDiscount.setEditable(false);
		tfDiscount.setFont(new java.awt.Font("Tahoma", 1, 12));
		ticketAmountPanel.add(tfDiscount, "cell 1 2,growx,aligny center");
		jLabel2 = new javax.swing.JLabel();

		jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12));
		jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel2.setText(com.floreantpos.POSConstants.TAX + ":");
		ticketAmountPanel.add(jLabel2, "cell 0 3,growx,aligny center");
		tfTax = new javax.swing.JTextField();
		tfTax.setHorizontalAlignment(SwingConstants.TRAILING);

		tfTax.setEditable(false);
		tfTax.setFont(new java.awt.Font("Tahoma", 1, 12));
		ticketAmountPanel.add(tfTax, "cell 1 3,growx,aligny center");

		lblServiceCharge = new JLabel();
		lblServiceCharge.setText("Service Charge:");
		lblServiceCharge.setHorizontalAlignment(SwingConstants.RIGHT);
		lblServiceCharge.setFont(new Font("Dialog", Font.BOLD, 12));
		ticketAmountPanel.add(lblServiceCharge, "cell 0 4,alignx trailing");

		tfServiceCharge = new JTextField();
		tfServiceCharge.setHorizontalAlignment(SwingConstants.TRAILING);
		tfServiceCharge.setEditable(false);
		ticketAmountPanel.add(tfServiceCharge, "cell 1 4,growx,aligny center");
		tfServiceCharge.setColumns(10);
		jLabel6 = new javax.swing.JLabel();

		jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12));
		jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel6.setText(com.floreantpos.POSConstants.TOTAL + ":");
		ticketAmountPanel.add(jLabel6, "cell 0 5,growx,aligny center");
		tfTotal = new javax.swing.JTextField();
		tfTotal.setHorizontalAlignment(SwingConstants.TRAILING);

		tfTotal.setEditable(false);
		tfTotal.setFont(new java.awt.Font("Tahoma", 1, 12));
		ticketAmountPanel.add(tfTotal, "cell 1 5,growx,aligny center");
		chkTaxExempt = new javax.swing.JCheckBox();

		chkTaxExempt.setFont(new java.awt.Font("Tahoma", 1, 12));
		chkTaxExempt.setText(com.floreantpos.POSConstants.TAX_EXEMPT);
		chkTaxExempt.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		chkTaxExempt.setFocusable(false);
		chkTaxExempt.setMargin(new java.awt.Insets(0, 0, 0, 0));
		ticketAmountPanel.add(chkTaxExempt, "cell 1 6,growx,aligny center");

		btnPay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pay_32.png")));
		btnPay.setText(com.floreantpos.POSConstants.PAY_NOW);
		btnPay.setPreferredSize(new java.awt.Dimension(76, 45));
		btnPay.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doPayNow(evt);
			}
		});
		controlPanel.setLayout(new MigLayout("insets 0", "[202px,grow][202px,grow]", "[45px][45px]"));
		controlPanel.add(btnPay, "cell 0 0 2 1,grow");

		btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cancel_32.png")));
		btnCancel.setText(com.floreantpos.POSConstants.CANCEL);
		btnCancel.setPreferredSize(new java.awt.Dimension(76, 45));
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doCancelOrder(evt);
			}
		});
		controlPanel.add(btnCancel, "cell 0 1,grow");

		btnFinish.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/finish_32.png")));
		btnFinish.setText(com.floreantpos.POSConstants.FINISH);
		btnFinish.setPreferredSize(new java.awt.Dimension(76, 45));
		btnFinish.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doFinishOrder(evt);
			}
		});
		controlPanel.add(btnFinish, "cell 1 1,grow");

		btnIncreaseAmount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add_user_32.png")));
		btnIncreaseAmount.setPreferredSize(new java.awt.Dimension(76, 45));
		btnIncreaseAmount.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doIncreaseAmount(evt);
			}
		});
		scrollerPanel.setLayout(new MigLayout("insets 0", "[133px,grow][133px,grow][133px,grow]", "[45px][45px]"));
		scrollerPanel.add(btnIncreaseAmount, "cell 0 0,grow");

		btnDecreaseAmount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/minus_32.png")));
		btnDecreaseAmount.setPreferredSize(new java.awt.Dimension(76, 45));
		btnDecreaseAmount.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doDecreaseAmount(evt);
			}
		});
		scrollerPanel.add(btnDecreaseAmount, "cell 1 0,grow");

		btnScrollUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/up_32.png")));
		btnScrollUp.setPreferredSize(new java.awt.Dimension(76, 45));
		btnScrollUp.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doScrollUp(evt);
			}
		});
		scrollerPanel.add(btnScrollUp, "cell 2 0,grow");

		btnScrollDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/down_32.png")));
		btnScrollDown.setPreferredSize(new java.awt.Dimension(76, 45));
		btnScrollDown.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doScrollDown(evt);
			}
		});
		btnDelete = new com.floreantpos.swing.PosButton();

		btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete_32.png")));
		btnDelete.setText(com.floreantpos.POSConstants.DELETE);
		btnDelete.setPreferredSize(new java.awt.Dimension(80, 17));
		btnDelete.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doDeleteSelection(evt);
			}
		});

		btnAddCookingInstruction = new PosButton();
		btnAddCookingInstruction.setEnabled(false);
		btnAddCookingInstruction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doAddCookingInstruction();
			}
		});
		btnAddCookingInstruction.setText("<html><center>ADD COOKING<br/>INSTRUCTION</center></html>");
		scrollerPanel.add(btnAddCookingInstruction, "cell 0 1,grow");
		scrollerPanel.add(btnDelete, "cell 1 1,grow");
		scrollerPanel.add(btnScrollDown, "cell 2 1,grow");

		JPanel amountPanelContainer= new JPanel(new BorderLayout(5, 5));
		amountPanelContainer.add(ticketAmountPanel);
		amountPanelContainer.add(scrollerPanel, BorderLayout.SOUTH);

		jPanel1.add(amountPanelContainer);
		jPanel1.add(controlPanel, BorderLayout.SOUTH);

		add(jPanel1, java.awt.BorderLayout.SOUTH);

		jPanel2.setLayout(new java.awt.BorderLayout());

		//		jScrollPane1.setBorder(null);
		jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		jScrollPane1.setPreferredSize(new java.awt.Dimension(180, 200));
		//jScrollPane1.setViewportView(ticketViewerTable);

		jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);

		add(jPanel2, java.awt.BorderLayout.CENTER);
		
	}// </editor-fold>//GEN-END:initComponents

	protected void doAddCookingInstruction() {

		try {
			Object object = ticketViewerTable.getSelected();
			if (!(object instanceof TicketItem)) {
				POSMessageDialog.showError("Please select and item");
				return;
			}

			TicketItem ticketItem = (TicketItem) object;

			if (ticketItem.isPrintedToKitchen()) {
				POSMessageDialog.showError("Cooking instruction cannot be added to item already printed to kitchen");
				return;
			}

			List<CookingInstruction> list = CookingInstructionDAO.getInstance().findAll();
			CookingInstructionSelectionView cookingInstructionSelectionView = new CookingInstructionSelectionView();
			BeanEditorDialog dialog = new BeanEditorDialog(cookingInstructionSelectionView, Application.getPosWindow(), true);
			dialog.setBean(list);
			dialog.setSize(450, 300);
			dialog.setLocationRelativeTo(Application.getPosWindow());
			dialog.setVisible(true);

			if (dialog.isCanceled()) {
				return;
			}

			List<TicketItemCookingInstruction> instructions = cookingInstructionSelectionView.getTicketItemCookingInstructions();
			ticketItem.addCookingInstructions(instructions);

			ticketViewerTable.updateView();
		} catch (Exception e) {
			e.printStackTrace();
			POSMessageDialog.showError(e.getMessage());
		}
	}

	private void doFinishOrder(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doFinishOrder
		try {

			updateModel();
			ticket.clearDeletedItems();
			
			OrderController.saveOrder(ticket);

			if (ticket.needsKitchenPrint()) {
				PosPrintService.printToKitchen(ticket);
			}
			
			//ticket.clearDeletedItems();
			//OrderController.saveOrder(ticket);

			RootView.getInstance().showView(SwitchboardView.VIEW_NAME);

		} catch (StaleObjectStateException e) {
			POSMessageDialog.showError("It seems the ticket has been modified by some other person or terminal. Save failed.");
			return;
		} catch (PosException x) {
			POSMessageDialog.showError(x.getMessage());
		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(), POSConstants.ERROR_MESSAGE, e);
		}
	}//GEN-LAST:event_doFinishOrder

	private void doCancelOrder(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doCancelOrder
		RootView.getInstance().showView(SwitchboardView.VIEW_NAME);
	}//GEN-LAST:event_doCancelOrder

	private void updateModel() {
		if (ticket.getTicketItems() == null || ticket.getTicketItems().size() == 0) {
			throw new PosException(com.floreantpos.POSConstants.TICKET_IS_EMPTY_);
		}

		ticket.calculatePrice();
	}

	private void doPayNow(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doPayNow
		try {
			updateModel();

			OrderController.saveOrder(ticket);
			
			firePayOrderSelected();
		} catch (PosException e) {
			POSMessageDialog.showError(e.getMessage());
		}
	}//GEN-LAST:event_doPayNow

	private void doDeleteSelection(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doDeleteSelection
		Object object = ticketViewerTable.deleteSelectedItem();
		if (object != null) {
			updateView();

			if (object instanceof TicketItemModifier) {
				ModifierView modifierView = OrderView.getInstance().getModifierView();
				if (modifierView.isVisible()) {
					modifierView.updateVisualRepresentation();
				}
			}
		}

	}//GEN-LAST:event_doDeleteSelection

	private void doIncreaseAmount(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doIncreaseAmount
		if (ticketViewerTable.increaseItemAmount()) {
			ModifierView modifierView = OrderView.getInstance().getModifierView();
			if (modifierView.isVisible()) {
				modifierView.updateVisualRepresentation();
			}
			updateView();
		}

	}//GEN-LAST:event_doIncreaseAmount

	private void doDecreaseAmount(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doDecreaseAmount
		if (ticketViewerTable.decreaseItemAmount()) {
			ModifierView modifierView = OrderView.getInstance().getModifierView();
			if (modifierView.isVisible()) {
				modifierView.updateVisualRepresentation();
			}
			updateView();
		}
	}//GEN-LAST:event_doDecreaseAmount

	private void doScrollDown(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doScrollDown
		ticketViewerTable.scrollDown();
	}//GEN-LAST:event_doScrollDown

	private void doScrollUp(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doScrollUp
		ticketViewerTable.scrollUp();
	}//GEN-LAST:event_doScrollUp

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private com.floreantpos.swing.TransparentPanel controlPanel;
	private com.floreantpos.swing.PosButton btnCancel;
	private com.floreantpos.swing.PosButton btnDecreaseAmount;
	private com.floreantpos.swing.PosButton btnDelete;
	private com.floreantpos.swing.PosButton btnFinish;
	private com.floreantpos.swing.PosButton btnIncreaseAmount;
	private com.floreantpos.swing.PosButton btnPay;
	private com.floreantpos.swing.PosButton btnScrollDown;
	private com.floreantpos.swing.PosButton btnScrollUp;
	private javax.swing.JCheckBox chkTaxExempt;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private com.floreantpos.swing.TransparentPanel jPanel1;
	private com.floreantpos.swing.TransparentPanel jPanel2;
	private com.floreantpos.swing.TransparentPanel ticketAmountPanel;
	private com.floreantpos.swing.TransparentPanel scrollerPanel;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTextField tfDiscount;
	private javax.swing.JTextField tfSubtotal;
	private javax.swing.JTextField tfTax;
	private javax.swing.JTextField tfTotal;
	private com.floreantpos.ui.ticket.TicketViewerTable ticketViewerTable;
	private JTextField tfServiceCharge;
	private JLabel lblServiceCharge;
	private PosButton btnAddCookingInstruction;

	// End of variables declaration//GEN-END:variables

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket _ticket) {
		this.ticket = _ticket;

		ticketViewerTable.setTicket(_ticket);

		updateView();
	}

	public void addTicketItem(TicketItem ticketItem) {
		ticketViewerTable.addTicketItem(ticketItem);
		updateView();
	}

	public void removeModifier(TicketItem parent, TicketItemModifier modifier) {
		ticketViewerTable.removeModifier(parent, modifier);
	}

	//	private NumberFormat numberFormat = new DecimalFormat("0.00");

	public void updateAllView() {
		ticketViewerTable.updateView();
		updateView();
	}

	public void selectRow(int rowIndex) {
		ticketViewerTable.selectRow(rowIndex);
	}

	public void updateView() {
		if (ticket == null || ticket.getTicketItems() == null || ticket.getTicketItems().size() <= 0) {
			tfSubtotal.setText("");
			tfDiscount.setText("");
			tfTax.setText("");
			tfServiceCharge.setText("");
			tfTotal.setText("");

			return;
		}
		

		ticket.calculatePrice();

		tfSubtotal.setText(NumberUtil.formatNumber(ticket.getSubtotalAmount()));
		tfDiscount.setText(NumberUtil.formatNumber(ticket.getDiscountAmount()));

		if(Application.getInstance().isPriceIncludesTax()) {
			tfTax.setText("INCLUDED");
		}
		else {
			tfTax.setText(NumberUtil.formatNumber(ticket.getTaxAmount()));
		}

		if (ticket.isTaxExempt()) {
			chkTaxExempt.setSelected(true);
		}
		else {
			chkTaxExempt.setSelected(false);
		}

		tfServiceCharge.setText(NumberUtil.formatNumber(ticket.getServiceCharge()));
		tfTotal.setText(NumberUtil.formatNumber(ticket.getTotalAmount()));
	}

	public void addOrderListener(OrderListener listenre) {
		orderListeners.add(listenre);
	}

	public void removeOrderListener(OrderListener listenre) {
		orderListeners.remove(listenre);
	}

	public void firePayOrderSelected() {
		for (OrderListener listener : orderListeners) {
			listener.payOrderSelected(getTicket());
		}
	}

	public void setControlsVisible(boolean visible) {
		if (visible) {
			controlPanel.setVisible(true);
			btnIncreaseAmount.setEnabled(true);
			btnDecreaseAmount.setEnabled(true);
			btnDelete.setEnabled(true);
		}
		else {
			controlPanel.setVisible(false);
			btnIncreaseAmount.setEnabled(false);
			btnDecreaseAmount.setEnabled(false);
			btnDelete.setEnabled(false);
		}
	}

	private void updateSelectionView() {
		Object selectedObject = ticketViewerTable.getSelected();

		OrderView orderView = OrderView.getInstance();

		TicketItem selectedItem = null;
		if (selectedObject instanceof TicketItem) {
			selectedItem = (TicketItem) selectedObject;
			MenuItemDAO dao = new MenuItemDAO();
			MenuItem menuItem = dao.get(selectedItem.getItemId());
			MenuGroup menuGroup = menuItem.getParent();
			MenuItemView itemView = OrderView.getInstance().getItemView();
			if (!menuGroup.equals(itemView.getMenuGroup())) {
				itemView.setMenuGroup(menuGroup);
			}
			orderView.showView(MenuItemView.VIEW_NAME);

			MenuCategory menuCategory = menuGroup.getParent();
			orderView.getCategoryView().setSelectedCategory(menuCategory);
			return;
		}
		else if (selectedObject instanceof TicketItemModifier) {
			selectedItem = ((TicketItemModifier) selectedObject).getParent().getParent();
		}
		if (selectedItem == null)
			return;

		ModifierView modifierView = orderView.getModifierView();

		if (selectedItem.isHasModifiers()) {
			MenuItemDAO dao = new MenuItemDAO();
			MenuItem menuItem = dao.get(selectedItem.getItemId());
			if (!menuItem.equals(modifierView.getMenuItem())) {
				menuItem = dao.initialize(menuItem);
				modifierView.setMenuItem(menuItem, selectedItem);
			}

			MenuCategory menuCategory = menuItem.getParent().getParent();
			orderView.getCategoryView().setSelectedCategory(menuCategory);

			orderView.showView(ModifierView.VIEW_NAME);
		}
	}
}
