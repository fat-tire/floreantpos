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
package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.JXTable;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.bo.ui.CustomCellRenderer;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.ModifierGroup;
import com.floreantpos.model.dao.ModifierDAO;
import com.floreantpos.model.dao.ModifierGroupDAO;
import com.floreantpos.swing.ListTableModel;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
import com.floreantpos.ui.model.MenuModifierForm;
import com.floreantpos.util.CurrencyUtil;
import com.floreantpos.util.POSUtil;

public class ModifierExplorer extends TransparentPanel {

	private String currencySymbol;
	private JXTable table;
	private ModifierExplorerModel tableModel;

	public ModifierExplorer() {
		setLayout(new BorderLayout(5, 5));

		currencySymbol = CurrencyUtil.getCurrencySymbol();
		tableModel = new ModifierExplorerModel();
		table = new JXTable(tableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setDefaultRenderer(Object.class, new CustomCellRenderer());
		add(new JScrollPane(table));

		createActionButtons();
		add(buildSearchForm(), BorderLayout.NORTH);

		updateModifierList();

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() == 2) {
					doEditSelectedMenuModifier();
				}
			}
		});
	}

	private void createActionButtons() {
		ExplorerButtonPanel explorerButtonPanel = new ExplorerButtonPanel();
		JButton editButton = explorerButtonPanel.getEditButton();
		JButton addButton = explorerButtonPanel.getAddButton();
		JButton deleteButton = explorerButtonPanel.getDeleteButton();
		JButton duplicateButton = new JButton(POSConstants.DUPLICATE);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					MenuModifierForm editor = new MenuModifierForm();
					BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), editor);
					dialog.open();
					if (dialog.isCanceled())
						return;
					MenuModifier modifier = (MenuModifier) editor.getBean();
					tableModel.addModifier(modifier);
				} catch (Throwable x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doEditSelectedMenuModifier();
			}
		});

		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					index = table.convertRowIndexToModel(index);

					if (ConfirmDeleteDialog
							.showMessage(ModifierExplorer.this, com.floreantpos.POSConstants.CONFIRM_DELETE, com.floreantpos.POSConstants.DELETE) != ConfirmDeleteDialog.NO) {
						MenuModifier category = (MenuModifier) tableModel.getRowData(index);
						ModifierDAO modifierDAO = new ModifierDAO();
						modifierDAO.delete(category);
						tableModel.deleteModifier(category, index);
					}
				} catch (Throwable x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}

			}

		});

		duplicateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					index = table.convertRowIndexToModel(index);

					MenuModifier existingModifier = (MenuModifier) tableModel.getRowData(index);

					MenuModifier newMenuModifier = new MenuModifier();
					PropertyUtils.copyProperties(newMenuModifier, existingModifier);
					newMenuModifier.setId(null);
					String newName = doDuplicateName(existingModifier);
					newMenuModifier.setName(newName);
					newMenuModifier.setModifierGroup(existingModifier.getModifierGroup());
					newMenuModifier.setSortOrder(existingModifier.getSortOrder());
					newMenuModifier.setTax(existingModifier.getTax());
					newMenuModifier.setButtonColor(existingModifier.getButtonColor());
					newMenuModifier.setTextColor(existingModifier.getTextColor());
					newMenuModifier.setShouldPrintToKitchen(existingModifier.isShouldPrintToKitchen());

					MenuModifierForm editor = new MenuModifierForm(newMenuModifier);
					BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), editor);
					dialog.open();
					if (dialog.isCanceled())
						return;

					MenuModifier menuModifier = (MenuModifier) editor.getBean();
					tableModel.addModifier(menuModifier);
					table.getSelectionModel().addSelectionInterval(tableModel.getRowCount() - 1, tableModel.getRowCount() - 1);
					table.scrollRowToVisible(tableModel.getRowCount() - 1);
				} catch (Throwable x) {
					BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
				}
			}
		});
		TransparentPanel panel = new TransparentPanel();
		panel.add(addButton);
		panel.add(editButton);
		panel.add(deleteButton);
		panel.add(duplicateButton);

		add(panel, BorderLayout.SOUTH);
	}

	private void doEditSelectedMenuModifier() {
		try {
			int index = table.getSelectedRow();
			if (index < 0)
				return;

			index = table.convertRowIndexToModel(index);
			MenuModifier modifier = (MenuModifier) tableModel.getRowData(index);

			MenuModifierForm editor = new MenuModifierForm(modifier);
			BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), editor);
			dialog.open();
			if (dialog.isCanceled())
				return;

			table.repaint();
		} catch (Throwable x) {
			BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
		}
	}

	private JPanel buildSearchForm() {
		List<ModifierGroup> grpName;
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("", "[][]30[][]30[]", "[]20[]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		JLabel nameLabel = new JLabel(Messages.getString("ModifierExplorer.3")); //$NON-NLS-1$
		JLabel groupLabel = new JLabel(Messages.getString("ModifierExplorer.4")); //$NON-NLS-1$
		final JTextField nameField = new JTextField(15);
		grpName = ModifierGroupDAO.getInstance().findAll();
		final JComboBox cbGroup = new JComboBox();
		cbGroup.addItem(Messages.getString("ModifierExplorer.5")); //$NON-NLS-1$
		for (ModifierGroup s : grpName) {
			cbGroup.addItem(s);
		}

		JButton searchBttn = new JButton(Messages.getString("ModifierExplorer.6")); //$NON-NLS-1$
		panel.add(nameLabel, "align label"); //$NON-NLS-1$
		panel.add(nameField);
		panel.add(groupLabel);
		panel.add(cbGroup);
		panel.add(searchBttn);

		TitledBorder title;
		Border loweredetched;
		loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		title = BorderFactory.createTitledBorder(loweredetched, Messages.getString("ModifierExplorer.8")); //$NON-NLS-1$
		title.setTitleJustification(TitledBorder.LEFT);
		panel.setBorder(title);
		searchBttn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				List<MenuModifier> modifierList;
				String txName = nameField.getText();
				Object selectedItem = cbGroup.getSelectedItem();
				if (selectedItem instanceof ModifierGroup) {
					modifierList = ModifierDAO.getInstance().findModifier(txName, (ModifierGroup) selectedItem);
				}
				else {
					modifierList = ModifierDAO.getInstance().findModifier(txName, null);
				}

				setModifierList(modifierList);
			}
		});
		return panel;
	}

	public synchronized void updateModifierList() {
		setModifierList(ModifierDAO.getInstance().findAll());

	}

	public void setModifierList(List<MenuModifier> modifierList) {
		tableModel.setRows(modifierList);

	}

	private class ModifierExplorerModel extends ListTableModel {

		public ModifierExplorerModel() {

			super(new String[] { com.floreantpos.POSConstants.ID, com.floreantpos.POSConstants.NAME, POSConstants.TRANSLATED_NAME,
					com.floreantpos.POSConstants.PRICE + " (" + currencySymbol + ")", com.floreantpos.POSConstants.EXTRA_PRICE, //$NON-NLS-1$ //$NON-NLS-2$
					com.floreantpos.POSConstants.TAX + "(%)", com.floreantpos.POSConstants.MODIFIER_GROUP, POSConstants.BUTTON_COLOR, POSConstants.SORT_ORDER }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			List<MenuModifier> modifierList = getRows();

			MenuModifier modifier = modifierList.get(rowIndex);

			switch (columnIndex) {
				case 0:
					return String.valueOf(modifier.getId());

				case 1:
					return modifier.getName();

				case 2:
					return modifier.getTranslatedName();

				case 3:
					return Double.valueOf(modifier.getPrice());

				case 4:
					return Double.valueOf(modifier.getExtraPrice());

				case 5:
					if (modifier.getTax() == null) {
						return ""; //$NON-NLS-1$
					}
					return Double.valueOf(modifier.getTax().getRate());

				case 6:
					if (modifier.getModifierGroup() == null) {
						return ""; //$NON-NLS-1$
					}
					return modifier.getModifierGroup().getName();

				case 7:
					if (modifier.getButtonColor() != null) {
						return new Color(modifier.getButtonColor());
					}

					return null;

				case 8:
					return modifier.getSortOrder();
			}
			return null;
		}

		public void addModifier(MenuModifier category) {
			int size = getRows().size();
			getRows().add(category);
			fireTableRowsInserted(size, size);

		}

		public void deleteModifier(MenuModifier category, int index) {
			getRows().remove(category);
			fireTableRowsDeleted(index, index);
		}

	}

	private String doDuplicateName(MenuModifier existingModifier) {
		String existingName = existingModifier.getName();
		String newName = new String();
		int lastIndexOf = existingName.lastIndexOf(" ");
		if (lastIndexOf == -1) {
			newName = existingName + " 1";
		}
		else {
			String processName = existingName.substring(lastIndexOf + 1, existingName.length());
			if (StringUtils.isNumeric(processName)) {
				Integer count = Integer.valueOf(processName);
				count += 1;
				newName = existingName.replace(processName, String.valueOf(count));
			}
			else {
				newName = existingName + " 1";
			}
		}
		return newName;
	}

}