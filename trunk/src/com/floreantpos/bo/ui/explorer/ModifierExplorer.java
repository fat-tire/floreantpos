package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.MenuModifierGroup;
import com.floreantpos.model.dao.MenuModifierGroupDAO;
import com.floreantpos.model.dao.ModifierDAO;
import com.floreantpos.swing.ListTableModel;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
import com.floreantpos.ui.model.MenuModifierForm;

public class ModifierExplorer extends TransparentPanel {

	private String currencySymbol;
	private JTable table;
	private ModifierExplorerModel tableModel;

	public ModifierExplorer() {
		setLayout(new BorderLayout(5, 5));
		
		currencySymbol = Application.getCurrencySymbol();
		tableModel = new ModifierExplorerModel();
		table = new JTable(tableModel);

		add(new JScrollPane(table));
		
		createActionButtons();
		add(buildSearchForm(), BorderLayout.NORTH);
		
		updateModifierList();
	}

	private void createActionButtons() {
		ExplorerButtonPanel explorerButtonPanel = new ExplorerButtonPanel();
		explorerButtonPanel.getAddButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					MenuModifierForm editor = new MenuModifierForm();
					BeanEditorDialog dialog = new BeanEditorDialog(editor);
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
		explorerButtonPanel.getEditButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;
					
					index = table.convertRowIndexToModel(index);
					MenuModifier modifier = (MenuModifier) tableModel.getRowData(index);

					MenuModifierForm editor = new MenuModifierForm(modifier);
					BeanEditorDialog dialog = new BeanEditorDialog(editor);
					dialog.open();
					if (dialog.isCanceled())
						return;

					table.repaint();
				} catch (Throwable x) {
				BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}
		});

		explorerButtonPanel.getDeleteButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;
					
					index = table.convertRowIndexToModel(index);
					
					if (ConfirmDeleteDialog.showMessage(ModifierExplorer.this, com.floreantpos.POSConstants.CONFIRM_DELETE, com.floreantpos.POSConstants.DELETE) != ConfirmDeleteDialog.NO) {
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
		
		add(explorerButtonPanel, BorderLayout.SOUTH);
	}

	private JPanel buildSearchForm() {
		List<MenuModifierGroup> grpName;
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("", "[][]30[][]30[]", "[]20[]"));

		JLabel nameLabel = new JLabel("Name : ");
		JLabel groupLabel = new JLabel("Group : ");
		final JTextField nameField = new JTextField(15);
		grpName = MenuModifierGroupDAO.getInstance().findAll();
		final JComboBox cbGroup = new JComboBox();
		cbGroup.addItem("FULL  LIST");
		for (MenuModifierGroup s : grpName) {
			cbGroup.addItem(s);
		}

		JButton searchBttn = new JButton("Search");
		panel.add(nameLabel, "align label");
		panel.add(nameField);
		panel.add(groupLabel);
		panel.add(cbGroup);
		panel.add(searchBttn);

		TitledBorder title;
		Border loweredetched;
		loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		title = BorderFactory.createTitledBorder(loweredetched, "Search");
		title.setTitleJustification(TitledBorder.LEFT);
		panel.setBorder(title);
		searchBttn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				List<MenuModifier> modifierList;
				String txName = nameField.getText();
				Object selectedItem = cbGroup.getSelectedItem();
				if (selectedItem instanceof MenuModifierGroup) {
					modifierList = ModifierDAO.getInstance().findModifier(txName, (MenuModifierGroup) selectedItem);
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
					com.floreantpos.POSConstants.PRICE + " (" + currencySymbol + ")", com.floreantpos.POSConstants.EXTRA_PRICE,
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

}