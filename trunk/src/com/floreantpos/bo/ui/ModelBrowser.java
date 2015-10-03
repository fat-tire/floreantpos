package com.floreantpos.bo.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXTable;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.swing.BeanTableModel;
import com.floreantpos.swing.ListTableModel;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class ModelBrowser<E> extends JPanel implements ActionListener, ListSelectionListener {
	
	protected JXTable browserTable;
	protected BeanEditor<E> beanEditor;
	
	protected JPanel browserPanel = new JPanel(new BorderLayout());
	private JPanel beanPanel = new JPanel(new BorderLayout());
	
	private JButton btnNew = new JButton(Messages.getString("ModelBrowser.0")); //$NON-NLS-1$
	private JButton btnEdit = new JButton(Messages.getString("ModelBrowser.1")); //$NON-NLS-1$
	private JButton btnSave = new JButton(Messages.getString("ModelBrowser.2")); //$NON-NLS-1$
	private JButton btnDelete = new JButton(Messages.getString("ModelBrowser.3")); //$NON-NLS-1$
	private JButton btnCancel = new JButton(Messages.getString("ModelBrowser.4")); //$NON-NLS-1$
	
	public ModelBrowser() {
		this(null);	
	}

	public ModelBrowser(BeanEditor<E> beanEditor) {
		super();
		this.beanEditor = beanEditor;
	}

	public void init(TableModel tableModel) {
		browserTable = new JXTable();
		browserTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		browserTable.getSelectionModel().addListSelectionListener(this);
		
		if(tableModel != null) {
			browserTable.setModel(tableModel);
		}
		
		setLayout(new BorderLayout(10, 10));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		browserPanel.add(new JScrollPane(browserTable));
		
		JPanel searchPanel = createSearchPanel();
		if(searchPanel != null) {
			browserPanel.add(searchPanel, BorderLayout.NORTH);
		}
		
		add(browserPanel);
		
		beanPanel.setBorder(BorderFactory.createEtchedBorder());
		JPanel beanEditorPanel = new JPanel(new MigLayout("align 50% 50%")); //$NON-NLS-1$
		beanEditorPanel.add(beanEditor);
		beanPanel.add(beanEditorPanel);
		
		JPanel buttonPanel = new JPanel();
		
		JButton additionalButton = getAdditionalButton();
		if(additionalButton != null) {
			buttonPanel.add(additionalButton);
			additionalButton.addActionListener(this);
		}
		
		buttonPanel.add(btnNew);
		buttonPanel.add(btnEdit);
		buttonPanel.add(btnSave);
		buttonPanel.add(btnDelete);
		buttonPanel.add(btnCancel);
		beanPanel.setPreferredSize(new Dimension(500, 400));
		beanPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		add(beanPanel, BorderLayout.EAST);
		
		btnNew.addActionListener(this);
		btnEdit.addActionListener(this);
		btnDelete.addActionListener(this);
		btnSave.addActionListener(this);
		btnCancel.addActionListener(this);
		
		btnNew.setEnabled(true);
		btnEdit.setEnabled(false);
		btnSave.setEnabled(false);
		btnDelete.setEnabled(false);
		btnCancel.setEnabled(false);
		
		beanEditor.clearFields();
		beanEditor.setFieldsEnable(false);
		
		refreshTable();
	}
	
	public void refreshTable() {
		
	}
	
	public JPanel createSearchPanel() {
		return null;
	}
	
	protected JButton getAdditionalButton() {
		return null;
	}
	
	protected void handleAdditionaButtonActionIfApplicable(ActionEvent e) {
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Command command = Command.fromString(e.getActionCommand());
		
		try {
			switch (command) {
				case NEW:
					beanEditor.createNew();
					beanEditor.setFieldsEnable(true);
					btnNew.setEnabled(false);
					btnEdit.setEnabled(false);
					btnSave.setEnabled(true);
					btnDelete.setEnabled(false);
					btnCancel.setEnabled(true);
					break;

				case EDIT:
					beanEditor.setFieldsEnable(true);
					btnNew.setEnabled(false);
					btnEdit.setEnabled(false);
					btnSave.setEnabled(true);
					btnDelete.setEnabled(false);
					btnCancel.setEnabled(true);
					break;

				case CANCEL:
					beanEditor.cancel();
					beanEditor.setBean(null);
					beanEditor.setFieldsEnable(false);
					btnNew.setEnabled(true);
					btnEdit.setEnabled(false);
					btnSave.setEnabled(false);
					btnDelete.setEnabled(false);
					btnCancel.setEnabled(false);
					break;

				case SAVE:
					if (beanEditor.save()) {
						beanEditor.setFieldsEnable(false);
						btnNew.setEnabled(true);
						btnEdit.setEnabled(false);
						btnSave.setEnabled(false);
						btnDelete.setEnabled(false);
						btnCancel.setEnabled(false);

						refreshTable();
					}
					break;

				case DELETE:
					if (beanEditor.delete()) {
						beanEditor.setBean(null);
						beanEditor.setFieldsEnable(false);
						btnNew.setEnabled(true);
						btnEdit.setEnabled(false);
						btnSave.setEnabled(false);
						btnDelete.setEnabled(false);
						btnCancel.setEnabled(false);
						refreshTable();
					}
					break;

				default:
					break;
			}
			
			handleAdditionaButtonActionIfApplicable(e);
			
		} catch (Exception e2) {
			POSMessageDialog.showError(Application.getPosWindow(), e2.getMessage(), e2);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(e.getValueIsAdjusting()) {
			return;
		}
		
		BeanTableModel<E> model = (BeanTableModel<E>) browserTable.getModel();
		int selectedRow = browserTable.getSelectedRow();
		if(selectedRow < 0) {
			return;
		}
		
		selectedRow = browserTable.convertRowIndexToModel(selectedRow);
		
		if(selectedRow < 0) return;
		
		E data = (E) model.getRow(selectedRow);
		beanEditor.setBean(data);
		
		btnNew.setEnabled(true);
		btnEdit.setEnabled(true);
		btnSave.setEnabled(false);
		btnDelete.setEnabled(true);
		btnCancel.setEnabled(false);
	}
	
	public void setModels(List<E> models) {
		ListTableModel tableModel = (ListTableModel) browserTable.getModel();
		tableModel.setRows(models);
	}
}
