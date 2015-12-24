/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
/*
 * OrderView.java
 *
 * Created on August 4, 2006, 6:58 PM
 */

package com.floreantpos.ui.views.order.modifier;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JSeparator;

import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.MenuModifierGroup;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao._RootDAO;
import com.floreantpos.swing.POSToggleButton;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.dialog.POSDialog;

/**
 *
 * @author  MShahriar
 */
public class ModifierSelectionDialog extends POSDialog implements ModifierGroupSelectionListener {
	private HashMap<String, JComponent> views = new HashMap<String, JComponent>();

	private static ModifierSelectionDialog instance;

	private Ticket currentTicket;
	
	private TicketItem ticketItem;
	private MenuItem menuItem;
	
	private ModifierGroupView modifierGroupView;
	private ModifierView modifierView;
	
	private com.floreantpos.swing.TransparentPanel buttonPanel;
	
	public com.floreantpos.swing.POSToggleButton btnAddsOn;
	private com.floreantpos.swing.PosButton btnSave;
	private com.floreantpos.swing.PosButton btnCancel;
	
	//private TicketItemModifierView modifierViewer;

	ModifierSelectionDialog() {
		
		initComponents();

	}
	
	private void initComponents() {
		setTitle("Select Modifiers");
		setLayout(new java.awt.BorderLayout(10, 10));
		
		Dimension screenSize=Application.getPosWindow().getSize(); 
		
		modifierGroupView = new com.floreantpos.ui.views.order.modifier.ModifierGroupView();
		modifierView = new ModifierView();
		//ticketItemModifierView = new TicketItemModifierView();
		buttonPanel = new com.floreantpos.swing.TransparentPanel();
		buttonPanel.setLayout(new BorderLayout());

		add(modifierGroupView, java.awt.BorderLayout.EAST);
		add(modifierView);

		//ticketItemModifierView.setPreferredSize(new Dimension(350,0));
		//add(ticketItemModifierView, java.awt.BorderLayout.WEST);

		createButtonPanel();

		setSize(screenSize.width-200, screenSize.height-80); 
		
		modifierGroupView.addModifierGroupSelectionListener(this);
	}
	
	public void createButtonPanel(){
		
		TransparentPanel panel2=new TransparentPanel(new FlowLayout(FlowLayout.CENTER)); 

		btnAddsOn = new POSToggleButton("ADDS ON");
		btnAddsOn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				modifierView.setAddOnMode(btnAddsOn.isSelected());
			}
		});
		btnAddsOn.setPreferredSize(new Dimension(80,60));

		btnCancel = new com.floreantpos.swing.PosButton();
		btnSave = new com.floreantpos.swing.PosButton();

		btnCancel.setText(POSConstants.CANCEL_BUTTON_TEXT);
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				closeView(true);
			}
		});
		btnCancel.setPreferredSize(new Dimension(80,60));

		btnSave.setText("DONE");
		btnSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doFinishModifierSelection(evt);
			}
		});
		btnSave.setPreferredSize(new Dimension(80,60));
		
		panel2.add(btnAddsOn);
		panel2.add(btnSave);
		panel2.add(btnCancel);
		
		buttonPanel.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.NORTH);
		
		buttonPanel.add(panel2, BorderLayout.CENTER); 
		
		add(buttonPanel, java.awt.BorderLayout.SOUTH);
	}

	protected void openExtraModifiers(boolean selected) {
		if(selected){
			modifierGroupView.setModifierGroups(menuItem, ticketItem);
		}else{
			modifierGroupView.setModifierGroups(menuItem, ticketItem);
		}
	}

	public com.floreantpos.ui.views.order.modifier.ModifierGroupView getModifierGroupView() {
		return modifierGroupView;
	}

	public void setModifierGroupView(com.floreantpos.ui.views.order.modifier.ModifierGroupView modifierGroupView) {
		this.modifierGroupView = modifierGroupView;
	}

	public ModifierView getModifierView() {
		return modifierView;
	}

	public void setModifierView(ModifierView modifierView) {
		this.modifierView = modifierView;
	}
	
	public MenuItem getMenuItem() {
		return menuItem;
	}

	public TicketItem getTicketItem() {
		return ticketItem;
	}

	public void setTicketItem(MenuItem menuItem, TicketItem ticketItem) {
		this.menuItem = menuItem;
		this.ticketItem = ticketItem;
		modifierGroupView.setModifierGroups(menuItem, ticketItem);
		
	}
	
	public Ticket getCurrentTicket() {
		return currentTicket;
	}

	public void setCurrentTicket(Ticket currentTicket) {
		this.currentTicket=currentTicket; 

		//ticketItemModifierView.setTicket(currentTicket);
	}

	public synchronized static ModifierSelectionDialog getInstance() {
		if (instance == null) {
			instance = new ModifierSelectionDialog();
		}
		return instance;
	}
	
	private void closeView(boolean orderCanceled) {
		dispose();
	}

	private void doFinishModifierSelection(java.awt.event.ActionEvent evt) {
		
	}
	
	public static void main(String[] args) {
		_RootDAO.initialize();
		
		MenuItemDAO dao = new MenuItemDAO();
		MenuItem menuItem = dao.get(2);
		menuItem = dao.initialize(menuItem);
		
		ModifierSelectionDialog dialog = new ModifierSelectionDialog();
		dialog.setSize(1024, 600);
		dialog.setTicketItem(menuItem, null);
		dialog.open();
	}

	@Override
	public void modifierGroupSelected(MenuModifierGroup menuModifierGroup) {
		modifierView.setModifierGroup(menuModifierGroup);
	}

}
