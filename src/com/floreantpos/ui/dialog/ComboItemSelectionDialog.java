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
 * MiscTicketItemDialog.java
 *
 * Created on September 8, 2006, 10:04 PM
 */

package com.floreantpos.ui.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.model.OrderType;
import com.floreantpos.swing.CheckBoxList;
import com.floreantpos.swing.ComboBoxModel;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosComboRenderer;
import com.floreantpos.util.POSUtil;

/**
 *
 * @author  MShahriar
 */
public class ComboItemSelectionDialog extends OkCancelOptionDialog {
	private javax.swing.JComboBox cbItems;
	private CheckBoxList chkItems;
	private JLabel lblItemText;
	private String title;
	private List items;
	private String lblText;
	private Object selectedObject;
	private PosButton btnNew = new PosButton("New");
	private JCheckBox chkSelectAll = new JCheckBox("Select All");
	private JPanel contentPane;
	private boolean newItem;
	private boolean allowMutipleSelection;
	private List selectedItems = new ArrayList<>();

	public ComboItemSelectionDialog(String title, String lblText, List items, boolean allowMutipleSelection) {
		super(POSUtil.getBackOfficeWindow(), true);
		this.title = title;
		this.lblText = lblText;
		this.items = items;
		this.allowMutipleSelection = allowMutipleSelection;
		setTitle(title); //$NON-NLS-1$
		initComponents();
	}

	public void setFirstItem(String firstItem) {

	}

	public void setVisibleNewButton(boolean b) {
		if (b) {
			btnNew.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					newItem = true;
					setCanceled(false);
					dispose();
				}
			});
			contentPane.add(btnNew, "h 40");
		}
	}

	private void initComponents() {
		contentPane = new JPanel(new MigLayout("inset 0, hidemode 3,fillx", "", ""));

		setTitle(title); //$NON-NLS-1$
		setTitlePaneText(title); //$NON-NLS-1$

		lblItemText = new JLabel(lblText);//$NON-NLS-2$
		if (!allowMutipleSelection)
			contentPane.add(lblItemText, "alignx trailing"); //$NON-NLS-1$ 

		PosComboRenderer comboRenderer = new PosComboRenderer();
		comboRenderer.setEnableDefaultValueShowing(false);

		cbItems = new JComboBox();
		chkItems = new CheckBoxList<>(items);

		if (allowMutipleSelection) {
			JPanel borderedPanel = new JPanel(new MigLayout("inset 0,fill"));
			borderedPanel.setBorder(new TitledBorder(lblText));
			chkSelectAll.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (chkSelectAll.isSelected()) {
						chkItems.selectAll();
					}
					else {
						chkItems.unCheckAll();
					}
				}
			});
			borderedPanel.add(chkSelectAll, "wrap");
			borderedPanel.add(chkItems, "grow");
			contentPane.add(borderedPanel, "w 200!,grow,split 2"); //$NON-NLS-1$
		}
		else {
			cbItems.setModel(new ComboBoxModel(items));
			cbItems.setRenderer(comboRenderer);
			contentPane.add(cbItems, "w 200!, h 40,split 2"); //$NON-NLS-1$
		}
		getContentPanel().add(contentPane);
	}

	public void doCancel() {
		setCanceled(true);
		dispose();
	}

	public void doOk() {
		setCanceled(false);
		if (!allowMutipleSelection) {
			selectedObject = cbItems.getSelectedItem();
		}
		else {
			selectedItems = chkItems.getCheckedValues();
		}
		dispose();
	}

	public Object getSelectedItem() {
		return selectedObject;
	}

	public List getSelectedItems() {
		return selectedItems;
	}

	public boolean isNewItem() {
		return newItem;
	}

	public void setSelectedItem(Object defaultValue) {
		cbItems.setSelectedItem(defaultValue);
	}

	public void setSelectedItems(List<OrderType> defaultValues) {
		chkItems.selectItems(defaultValues);
	}
}
