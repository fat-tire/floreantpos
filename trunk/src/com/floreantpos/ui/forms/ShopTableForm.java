package com.floreantpos.ui.forms;

import java.awt.FlowLayout;
import java.lang.ref.SoftReference;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.hibernate.StaleObjectStateException;

import com.floreantpos.Messages;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.dao.ShopTableDAO;
import com.floreantpos.model.util.IllegalModelStateException;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.swing.IntegerTextField;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;

public class ShopTableForm extends BeanEditor<ShopTable> {
	private FixedLengthTextField tfTableDescription;
	private IntegerTextField tfTableCapacity;
	private IntegerTextField tfTableNo;
	private FixedLengthTextField tfTableName;

	private JPanel statusPanel;
	private JRadioButton rbFree;
	private JRadioButton rbBooked;
	private JRadioButton rbDirty;
	private JRadioButton rbServing;
	private JRadioButton rbDisable;

	private SoftReference<ShopTableForm> instance;

	public ShopTableForm() {
		setLayout(new MigLayout("", "[][grow]", "[][][][][]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		JLabel lblName = new JLabel(Messages.getString("ShopTableForm.3")); //$NON-NLS-1$
		add(lblName, "cell 0 0,alignx trailing,aligny center"); //$NON-NLS-1$

		tfTableNo = new IntegerTextField(6);
		add(tfTableNo, "cell 1 0,aligny top"); //$NON-NLS-1$

		tfTableNo.setEnabled(false);

		JLabel lblPhone = new JLabel(Messages.getString("ShopTableForm.6")); //$NON-NLS-1$
		add(lblPhone, "cell 0 1,alignx trailing"); //$NON-NLS-1$

		tfTableName = new FixedLengthTextField(20);
		tfTableName.setColumns(20);
		add(tfTableName, "cell 1 1,growx"); //$NON-NLS-1$

		JLabel lblAddress = new JLabel(Messages.getString("ShopTableForm.9")); //$NON-NLS-1$
		add(lblAddress, "cell 0 2,alignx trailing"); //$NON-NLS-1$

		tfTableDescription = new FixedLengthTextField(60);
		tfTableDescription.setColumns(20);
		add(tfTableDescription, "cell 1 2,growx"); //$NON-NLS-1$

		JLabel lblCitytown = new JLabel(Messages.getString("ShopTableForm.12")); //$NON-NLS-1$
		add(lblCitytown, "cell 0 3,alignx trailing"); //$NON-NLS-1$

		tfTableCapacity = new IntegerTextField();
		tfTableCapacity.setColumns(6);
		add(tfTableCapacity, "flowx,cell 1 3"); //$NON-NLS-1$

		statusPanel = new JPanel();
		statusPanel.setBorder(new TitledBorder(null, Messages.getString("ShopTableForm.15"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$
		add(statusPanel, "cell 1 4,grow"); //$NON-NLS-1$
		statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		rbFree = new JRadioButton(Messages.getString("ShopTableForm.17")); //$NON-NLS-1$
		statusPanel.add(rbFree);

		rbServing = new JRadioButton(Messages.getString("ShopTableForm.18")); //$NON-NLS-1$
		statusPanel.add(rbServing);

		rbBooked = new JRadioButton(Messages.getString("ShopTableForm.19")); //$NON-NLS-1$
		statusPanel.add(rbBooked);

		rbDirty = new JRadioButton(Messages.getString("ShopTableForm.20")); //$NON-NLS-1$
		statusPanel.add(rbDirty);

		rbDisable = new JRadioButton(Messages.getString("ShopTableForm.21")); //$NON-NLS-1$
		statusPanel.add(rbDisable);

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(rbFree);
		buttonGroup.add(rbServing);
		buttonGroup.add(rbBooked);
		buttonGroup.add(rbDirty);
		buttonGroup.add(rbDisable);

	}

	@Override
	public void createNew() {
		ShopTable bean2 = new ShopTable();
		bean2.setTemporary(true);
		bean2.setCapacity(4);

		try {

			int nextTableNumber = ShopTableDAO.getInstance().getNextTableNumber();

			bean2.setId(nextTableNumber);
			bean2.setName("" + nextTableNumber); //$NON-NLS-1$
			bean2.setDescription("" + nextTableNumber); //$NON-NLS-1$

		} catch (Exception e) {
			BOMessageDialog.showError(this, Messages.getString("ShopTableForm.24")); //$NON-NLS-1$
		}

		setBean(bean2);
	}

	@Override
	public void cancel() {
		//		ShopTable table = (ShopTable) getBean();
		//		if(table == null) {
		//			return;
		//		}
		//		
		//		if(table.isTemporary()) {
		//			ShopTableDAO.getInstance().delete(table);
		//		}
		//		
		//		setBean(null);
	}

	@Override
	public boolean delete() {
		try {
			ShopTable bean2 = getBean();
			if (bean2 == null)
				return false;

			int option = POSMessageDialog.showYesNoQuestionDialog(POSUtil.getBackOfficeWindow(), Messages.getString("ShopTableForm.25"), Messages.getString("ShopTableForm.26")); //$NON-NLS-1$ //$NON-NLS-2$
			if (option != JOptionPane.YES_OPTION) {
				return false;
			}

			ShopTableDAO.getInstance().delete(bean2);

			return true;
		} catch (Exception e) {
			POSMessageDialog.showError(POSUtil.getBackOfficeWindow(), e.getMessage(), e);
		}

		return false;
	}

	public void setFieldsEditable(boolean editable) {
		tfTableName.setEditable(editable);
		tfTableDescription.setEditable(editable);
		tfTableCapacity.setEditable(editable);
	}

	@Override
	public void setFieldsEnable(boolean enable) {
		tfTableName.setEditable(enable);
		tfTableDescription.setEditable(enable);
		tfTableCapacity.setEditable(enable);

		rbFree.setEnabled(enable);
		rbServing.setEnabled(enable);
		rbBooked.setEnabled(enable);
		rbDirty.setEnabled(enable);
		rbDisable.setEnabled(enable);
	}

	public void setOnlyStatusEnable() {
		tfTableNo.setEditable(false);
		tfTableName.setEditable(false);
		tfTableDescription.setEditable(false);
		tfTableCapacity.setEditable(false);

		rbFree.setEnabled(true);
		rbServing.setEnabled(true);
		rbBooked.setEnabled(true);
		rbDirty.setEnabled(true);
		rbDisable.setEnabled(true);
	}

	@Override
	public boolean save() {
		try {
			if (!updateModel())
				return false;

			ShopTable table = (ShopTable) getBean();
			ShopTableDAO.getInstance().saveOrUpdate(table);
			table.setTemporary(false);

			updateView();
			return true;

		} catch (IllegalModelStateException e) {
		} catch (StaleObjectStateException e) {
			BOMessageDialog.showError(this, Messages.getString("ShopTableForm.27")); //$NON-NLS-1$
		}

		return false;
	}

	@Override
	protected void updateView() {
		ShopTable table = (ShopTable) getBean();

		if (table == null) {
			return;
		}

		tfTableNo.setText(String.valueOf(table.getTableNumber()));
		tfTableName.setText(table.getName());
		tfTableDescription.setText(table.getDescription());
		tfTableCapacity.setText(String.valueOf(table.getCapacity()));

		rbFree.setSelected(true);
		rbServing.setSelected(table.isServing());
		rbBooked.setSelected(table.isBooked());
		rbDirty.setSelected(table.isDirty());
		rbDisable.setSelected(table.isDisable());

	}

	@Override
	protected boolean updateModel() throws IllegalModelStateException {
		ShopTable table = (ShopTable) getBean();

		if (table == null) {
			table = new ShopTable();
			setBean(table, false);
		}

		if (table.isTemporary()) {
			table.setId(null);
		}

		table.setName(tfTableName.getText());
		table.setDescription(tfTableDescription.getText());
		table.setCapacity(tfTableCapacity.getInteger());

		if (rbFree.isSelected()) {
			table.setFree(true);
			table.setServing(false);
			table.setBooked(false);
			table.setDirty(false);
			table.setDisable(false);
		}
		else if (rbServing.isSelected()) {
			table.setFree(false);
			table.setServing(true);
			table.setBooked(false);
			table.setDirty(false);
			table.setDisable(false);
		}
		else if (rbBooked.isSelected()) {
			table.setFree(false);
			table.setServing(false);
			table.setBooked(true);
			table.setDirty(false);
			table.setDisable(false);
		}
		else if (rbDirty.isSelected()) {
			table.setFree(false);
			table.setServing(false);
			table.setBooked(false);
			table.setDirty(true);
			table.setDisable(false);
		}
		else if (rbDisable.isSelected()) {
			table.setFree(false);
			table.setServing(false);
			table.setBooked(false);
			table.setDirty(false);
			table.setDisable(true);
		}

		return true;
	}

	@Override
	public String getDisplayText() {
		if (editMode) {
			return Messages.getString("ShopTableForm.28"); //$NON-NLS-1$
		}
		return Messages.getString("ShopTableForm.29"); //$NON-NLS-1$
	}

	public ShopTableForm getInstance() {
		if (instance == null || instance.get() == null) {
			instance = new SoftReference<ShopTableForm>(new ShopTableForm());
		}

		return instance.get();
	}
}
