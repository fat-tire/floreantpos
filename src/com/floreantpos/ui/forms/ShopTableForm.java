package com.floreantpos.ui.forms;

import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.hibernate.StaleObjectStateException;

import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.dao.ShopTableDAO;
import com.floreantpos.model.util.IllegalModelStateException;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.swing.IntegerTextField;
import com.floreantpos.ui.BeanEditor;

public class ShopTableForm extends BeanEditor<ShopTable> {
	private IntegerTextField tfX;
	private FixedLengthTextField tfTableDescription;
	private IntegerTextField tfTableCapacity;
	private IntegerTextField tfY;
	private FixedLengthTextField tfTableNo;
	private FixedLengthTextField tfTableName;
	private JPanel panel;

	private JPanel panel_1;
	private JRadioButton rbFree;
	private JRadioButton rbBooked;
	private JRadioButton rbDirty;
	private JRadioButton rbServing;
	private JRadioButton rbDisable;

	public ShopTableForm() {
		setLayout(new MigLayout("", "[][grow][][grow][]", "[][][][][][][]"));

		JLabel lblName = new JLabel("Table no");
		add(lblName, "cell 0 0,alignx trailing,aligny center");

		tfTableNo = new FixedLengthTextField(60);
		tfTableNo.setLength(60);
		add(tfTableNo, "cell 1 0,growx,aligny top");

		panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Status", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(panel_1, "cell 3 0 1 6,grow");
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));

		rbFree = new JRadioButton("Free");
		panel_1.add(rbFree);

		rbServing = new JRadioButton("Serving");
		panel_1.add(rbServing);

		rbBooked = new JRadioButton("Booked");
		panel_1.add(rbBooked);

		rbDirty = new JRadioButton("Dirty");
		panel_1.add(rbDirty);

		rbDisable = new JRadioButton("Disable");
		panel_1.add(rbDisable);

		JLabel lblPhone = new JLabel("Name");
		add(lblPhone, "cell 0 1,alignx trailing");

		tfTableName = new FixedLengthTextField(30);
		tfTableName.setLength(30);
		add(tfTableName, "cell 1 1,growx");

		JLabel lblAddress = new JLabel("Description");
		add(lblAddress, "cell 0 2,alignx trailing");

		tfTableDescription = new FixedLengthTextField(120);
		tfTableDescription.setLength(120);
		add(tfTableDescription, "cell 1 2,growx");

		JLabel lblCitytown = new JLabel("Capacity");
		add(lblCitytown, "cell 0 3,alignx trailing");

		tfTableCapacity = new IntegerTextField();
		add(tfTableCapacity, "flowx,cell 1 3,growx");

		JLabel lblLoyaltyNo = new JLabel("X");
		add(lblLoyaltyNo, "cell 0 4,alignx trailing");

		tfX = new IntegerTextField(6);
		add(tfX, "cell 1 4");

		JLabel lblCreditLimit = new JLabel("Y");
		add(lblCreditLimit, "cell 0 5,alignx trailing,aligny top");

		tfY = new IntegerTextField(6);
		add(tfY, "cell 1 5,aligny top");

		panel = new JPanel();
		add(panel, "cell 0 6 5 1,grow");

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(rbFree);
		buttonGroup.add(rbServing);
		buttonGroup.add(rbBooked);
		buttonGroup.add(rbDirty);
		buttonGroup.add(rbDisable);
	}

	public void setFieldsEditable(boolean editable) {
		tfTableNo.setEditable(editable);
		tfTableName.setEditable(editable);
		tfX.setEditable(editable);
		tfTableDescription.setEditable(editable);
		tfTableCapacity.setEditable(editable);
		tfY.setEditable(editable);
	}

	@Override
	public boolean save() {
		try {
			if (!updateModel())
				return false;

			ShopTable table = (ShopTable) getBean();
			ShopTableDAO.getInstance().saveOrUpdate(table);
			return true;
			
		} catch (IllegalModelStateException e) {
		} catch (StaleObjectStateException e) {
			BOMessageDialog.showError(this, "It seems this table is modified by some other person or terminal. Save failed.");
		}

		return false;
	}

	@Override
	protected void updateView() {
		ShopTable table = (ShopTable) getBean();

		if (table == null) {
			return;
		}

		tfTableNo.setText(table.getTableNumber());
		tfTableName.setText(table.getName());
		tfTableDescription.setText(table.getDescription());
		tfTableCapacity.setText(String.valueOf(table.getCapacity()));
		tfX.setText(String.valueOf(table.getX()));
		tfY.setText(String.valueOf(table.getY()));

		rbFree.setSelected(true);

		//rbFree.setSelected(table.isFree());
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

		table.setTableNumber(tfTableNo.getText());
		table.setName(tfTableName.getText());
		table.setDescription(tfTableDescription.getText());
		table.setCapacity(tfTableCapacity.getInteger());
		table.setX(tfX.getInteger());
		table.setY(tfY.getInteger());

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
			return "Edit table";
		}
		return "Create table";
	}
}
