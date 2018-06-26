package com.floreantpos.table;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import org.hibernate.StaleObjectStateException;

import com.floreantpos.Messages;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.extension.ExtensionManager;
import com.floreantpos.extension.FloorLayoutPlugin;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.ShopTableType;
import com.floreantpos.model.TableBookingInfo;
import com.floreantpos.model.dao.ShopTableDAO;
import com.floreantpos.model.dao.ShopTableTypeDAO;
import com.floreantpos.model.dao.TableBookingInfoDAO;
import com.floreantpos.model.util.IllegalModelStateException;
import com.floreantpos.swing.CheckBoxList;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.swing.IntegerTextField;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;

import net.miginfocom.swing.MigLayout;

public class ShopTableForm extends BeanEditor<ShopTable> {

	private FixedLengthTextField tfTableDescription;
	private IntegerTextField tfTableCapacity;
	private IntegerTextField tfTableNo;
	private FixedLengthTextField tfTableName;
	private CheckBoxList tableTypeCBoxList;
	private JPanel statusPanel;
	private JRadioButton rbFree;
	private JRadioButton rbDisable;
	private JButton btnCapacityOne;
	private JButton btnCapacityTwo;
	private JButton btnCapacityFour;
	private JButton btnCapacitySix;
	private JButton btnCapacityEight;
	private JButton btnCapacityTen;
	private JButton btnCreateType;
	private int newTable;

	private boolean dupTableEnable;
	private boolean dupTableDisable;
	private String dupName;
	private Integer dupCapacity;
	private String dupDescription;
	private List<ShopTableType> dupCheckValues;
	private int selectedTable;

	private boolean duplicate;

	public ShopTableForm() {
		setPreferredSize(new Dimension(600, 800));
		setLayout(new MigLayout("", "[][grow]", "[][][][][][][][]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		setBorder(BorderFactory.createTitledBorder(Messages.getString("ShopTableForm.19"))); //$NON-NLS-1$
		tableTypeCBoxList = new CheckBoxList();
		tableTypeCBoxList.setModel(ShopTableTypeDAO.getInstance().findAll());
		JScrollPane tableTypeCheckBoxList = new JScrollPane(tableTypeCBoxList);
		tableTypeCheckBoxList.setPreferredSize(new Dimension(0, 350));

		tfTableName = new FixedLengthTextField();

		JLabel lblTableNo = new JLabel(Messages.getString("ShopTableForm.0")); //$NON-NLS-1$
		add(lblTableNo, "cell 0 0,alignx trailing,aligny center"); //$NON-NLS-1$

		tfTableNo = new IntegerTextField(6);
		add(tfTableNo, "cell 1 0,aligny top"); //$NON-NLS-1$

		JLabel lblDescription = new JLabel(Messages.getString("ShopTableForm.2")); //$NON-NLS-1$
		add(lblDescription, "cell 0 2,alignx trailing"); //$NON-NLS-1$

		tfTableDescription = new FixedLengthTextField();
		add(tfTableDescription, "cell 1 2,growx"); //$NON-NLS-1$

		JLabel lblCapacity = new JLabel(Messages.getString("ShopTableForm.3")); //$NON-NLS-1$
		add(lblCapacity, "cell 0 3,alignx trailing,aligny center"); //$NON-NLS-1$

		tfTableCapacity = new IntegerTextField(6);
		add(tfTableCapacity, "flowx,grow,cell 1 3"); //$NON-NLS-1$

		ActionListener action = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (e.getSource() == btnCapacityOne) {
					tfTableCapacity.setText("1"); //$NON-NLS-1$
				}
				else if (e.getSource() == btnCapacityTwo) {
					tfTableCapacity.setText("2"); //$NON-NLS-1$
				}
				else if (e.getSource() == btnCapacityFour) {
					tfTableCapacity.setText("4"); //$NON-NLS-1$
				}
				else if (e.getSource() == btnCapacitySix) {
					tfTableCapacity.setText("6"); //$NON-NLS-1$
				}
				else if (e.getSource() == btnCapacityEight) {
					tfTableCapacity.setText("8"); //$NON-NLS-1$
				}
				else if (e.getSource() == btnCapacityTen) {
					tfTableCapacity.setText("10"); //$NON-NLS-1$
				}
			}
		};

		btnCapacityOne = new PosButton("1"); //$NON-NLS-1$
		btnCapacityOne.setPreferredSize(new Dimension(52, 52));
		btnCapacityTwo = new PosButton("2"); //$NON-NLS-1$
		btnCapacityTwo.setPreferredSize(new Dimension(52, 52));
		btnCapacityFour = new PosButton("4"); //$NON-NLS-1$
		btnCapacityFour.setPreferredSize(new Dimension(52, 52));
		btnCapacitySix = new PosButton("6"); //$NON-NLS-1$
		btnCapacitySix.setPreferredSize(new Dimension(52, 52));
		btnCapacityEight = new PosButton("8"); //$NON-NLS-1$
		btnCapacityEight.setPreferredSize(new Dimension(52, 52));
		btnCapacityTen = new PosButton("10"); //$NON-NLS-1$
		btnCapacityTen.setPreferredSize(new Dimension(52, 52));

		btnCapacityOne.addActionListener(action);
		btnCapacityTwo.addActionListener(action);
		btnCapacityFour.addActionListener(action);
		btnCapacitySix.addActionListener(action);
		btnCapacityEight.addActionListener(action);
		btnCapacityTen.addActionListener(action);

		add(btnCapacityOne, "cell 1 3"); //$NON-NLS-1$
		add(btnCapacityTwo, "cell 1 3"); //$NON-NLS-1$
		add(btnCapacityFour, "cell 1 3"); //$NON-NLS-1$
		add(btnCapacitySix, "cell 1 3"); //$NON-NLS-1$
		add(btnCapacityEight, "cell 1 3"); //$NON-NLS-1$
		add(btnCapacityTen, "cell 1 3"); //$NON-NLS-1$

		statusPanel = new JPanel();
		statusPanel.setBorder(new TitledBorder(null, Messages.getString("ShopTableForm.4"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$
		add(statusPanel, "cell 1 4,grow"); //$NON-NLS-1$
		statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		rbFree = new JRadioButton(Messages.getString("ShopTableForm.5")); //$NON-NLS-1$
		statusPanel.add(rbFree);

		rbDisable = new JRadioButton(Messages.getString("ShopTableForm.9")); //$NON-NLS-1$
		statusPanel.add(rbDisable);

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(rbFree);
		buttonGroup.add(rbDisable);

		add(new JLabel(), "grow,span"); //$NON-NLS-1$

		final FloorLayoutPlugin floorLayoutPlugin = (FloorLayoutPlugin) ExtensionManager.getPlugin(FloorLayoutPlugin.class);

		if (floorLayoutPlugin != null) {
			btnCreateType = new JButton(Messages.getString("ShopTableForm.40")); //$NON-NLS-1$
			add(new JLabel(Messages.getString("ShopTableForm.10")), "cell 0 5"); //$NON-NLS-1$ //$NON-NLS-2$
			add(tableTypeCheckBoxList, "cell 1 5,wrap,grow"); //$NON-NLS-1$
			add(btnCreateType, "cell 1 6"); //$NON-NLS-1$

			btnCreateType.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), floorLayoutPlugin.getBeanEditor());
					dialog.open();
					tableTypeCBoxList.setModel(ShopTableTypeDAO.getInstance().findAll());
				}
			});
		}
	}

	@Override
	public void createNew() {
		ShopTable bean2 = new ShopTable();
		setBean(bean2);

		int nxtTableNumber = ShopTableDAO.getInstance().getNextTableNumber();
		if (nxtTableNumber == 0) {
			nxtTableNumber = 1;
		}

		for (int i = 1; i <= nxtTableNumber + 1; i++) {
			ShopTable shopTable = ShopTableDAO.getInstance().get(i);
			if (shopTable == null) {
				tfTableNo.setText(String.valueOf(i));
				break;
			}
		}
		tfTableCapacity.setText("4"); //$NON-NLS-1$
		tfTableDescription.setText(""); //$NON-NLS-1$
		tfTableName.setText(""); //$NON-NLS-1$
		setBorder(BorderFactory.createTitledBorder(Messages.getString("ShopTableForm.18"))); //$NON-NLS-1$
	}

	@Override
	public void cancel() {
		setBorder(BorderFactory.createTitledBorder(Messages.getString("ShopTableForm.46"))); //$NON-NLS-1$
		updateView();
	}

	@Override
	public void clearFields() {
		tfTableNo.setText("");//$NON-NLS-1$
		tfTableCapacity.setText(""); //$NON-NLS-1$
		tfTableDescription.setText(""); //$NON-NLS-1$
		tfTableName.setText(""); //$NON-NLS-1$
		tableTypeCBoxList.unCheckAll();
		rbFree.setSelected(false);
		rbDisable.setSelected(false);
	}

	@Override
	public boolean delete() {
		try {
			ShopTable bean2 = getBean();
			if (bean2 == null)
				return false;

			int option = POSMessageDialog.showYesNoQuestionDialog(POSUtil.getBackOfficeWindow(), Messages.getString("ShopTableForm.14"), //$NON-NLS-1$
					Messages.getString("ShopTableForm.15")); //$NON-NLS-1$
			if (option != JOptionPane.YES_OPTION) {
				return false;
			}

			List<TableBookingInfo> bookingList = TableBookingInfoDAO.getInstance().findAll();

			for (TableBookingInfo info : bookingList) {
				List<ShopTable> tableList = info.getTables();
				for (ShopTable shopTable : tableList) {
					if (shopTable.getId().equals(bean2.getId())) {
						tableList.remove(shopTable);
						info.setTables(tableList);
						TableBookingInfoDAO.getInstance().saveOrUpdate(info);
						break;
					}
				}
			}

			ShopTableDAO.getInstance().delete(bean2);

			tfTableCapacity.setText(""); //$NON-NLS-1$
			tfTableDescription.setText(""); //$NON-NLS-1$
			tfTableName.setText(""); //$NON-NLS-1$
			tfTableNo.setText(""); //$NON-NLS-1$
			tableTypeCBoxList.unCheckAll();

			return true;
		} catch (Exception e) {
			POSMessageDialog.showError(POSUtil.getBackOfficeWindow(), e.getMessage(), e);
		}
		return false;
	}

	public boolean deleteAllTables() {

		List<ShopTable> list = ShopTableDAO.getInstance().findAll();

		if (list.isEmpty()) {
			POSMessageDialog.showError(POSUtil.getBackOfficeWindow(), Messages.getString("ShopTableForm.51")); //$NON-NLS-1$
			return false;
		}

		int option = POSMessageDialog.showYesNoQuestionDialog(POSUtil.getBackOfficeWindow(), Messages.getString("ShopTableForm.20"), //$NON-NLS-1$
				Messages.getString("ShopTableForm.21")); //$NON-NLS-1$
		if (option != JOptionPane.YES_OPTION) {
			return false;
		}

		List<TableBookingInfo> bookingList = TableBookingInfoDAO.getInstance().findAll();

		for (TableBookingInfo info : bookingList) {
			info.setTables(null);
			TableBookingInfoDAO.getInstance().saveOrUpdate(info);
		}

		for (ShopTable table : list) {
			table.setFloor(null);
			table.setTypes(null);
			ShopTableDAO.getInstance().delete(table);
		}

		tfTableNo.setText(""); //$NON-NLS-1$
		tfTableCapacity.setText(""); //$NON-NLS-1$
		tfTableDescription.setText(""); //$NON-NLS-1$
		tfTableName.setText(""); //$NON-NLS-1$
		tableTypeCBoxList.unCheckAll();

		return true;
	}

	public void setFieldsEditable(boolean editable) {

		tfTableName.setEditable(editable);
		tfTableDescription.setEditable(editable);
		tfTableCapacity.setEditable(editable);
	}

	@Override
	public void setFieldsEnable(boolean enable) {

		tableTypeCBoxList.setEnabled(enable);
		tableTypeCBoxList.clearSelection();

		tfTableNo.setEnabled(enable);
		tfTableName.setEnabled(enable);
		tfTableDescription.setEnabled(enable);
		tfTableCapacity.setEnabled(enable);

		btnCapacityOne.setEnabled(enable);
		btnCapacityTwo.setEnabled(enable);
		btnCapacityFour.setEnabled(enable);
		btnCapacitySix.setEnabled(enable);
		btnCapacityEight.setEnabled(enable);
		btnCapacityTen.setEnabled(enable);

		if (btnCreateType != null) {
			btnCreateType.setEnabled(enable);
		}

		rbFree.setEnabled(enable);
		rbDisable.setEnabled(enable);
	}

	public void setOnlyStatusEnable() {
		tfTableNo.setEditable(false);
		tfTableName.setEditable(false);
		tfTableDescription.setEditable(false);
		tfTableCapacity.setEditable(false);

		btnCapacityOne.setVisible(false);
		btnCapacityTwo.setVisible(false);
		btnCapacityFour.setVisible(false);
		btnCapacitySix.setVisible(false);
		btnCapacityEight.setVisible(false);
		btnCapacityTen.setVisible(false);

		tableTypeCBoxList.setEnabled(false);

		if (btnCreateType != null) {
			btnCreateType.setVisible(false);
		}

		rbFree.setEnabled(true);
		rbDisable.setEnabled(true);
	}

	@Override
	public boolean save() {
		try {
			if (!updateModel())
				return false;

			ShopTable table = (ShopTable) getBean();
			ShopTableDAO.getInstance().saveOrUpdate(table);
			updateView();
			return true;

		} catch (IllegalModelStateException e) {
		} catch (StaleObjectStateException e) {
			BOMessageDialog.showError(this, Messages.getString("ShopTableForm.16")); //$NON-NLS-1$
		}
		return false;
	}

	@Override
	protected void updateView() {
		ShopTable table = (ShopTable) getBean();

		if (table == null) {
			return;
		}

		tableTypeCBoxList.setModel(ShopTableTypeDAO.getInstance().findAll());
		tableTypeCBoxList.selectItems(table.getTypes());

		tfTableNo.setText(String.valueOf(table.getTableNumber()));
		tfTableName.setText(table.getName());
		tfTableDescription.setText(table.getDescription());
		tfTableCapacity.setText(String.valueOf(table.getCapacity()));

		rbFree.setSelected(true);
		rbDisable.setSelected(table.isDisable());

		if (table.getTableNumber() != null) {
			selectedTable = table.getTableNumber();
		}

		if (!isDuplicateOn()) {
			List<ShopTableType> checkValues = tableTypeCBoxList.getCheckedValues();
			dupCheckValues = checkValues;
			dupCapacity = table.getCapacity();
			dupDescription = table.getDescription();
			dupName = table.getName();
			dupTableEnable = rbFree.isSelected();
			dupTableDisable = rbDisable.isSelected();
		}

		setBorder(BorderFactory.createTitledBorder(Messages.getString("ShopTableForm.56"))); //$NON-NLS-1$
	}

	@Override
	protected boolean updateModel() throws IllegalModelStateException {

		ShopTable table = (ShopTable) getBean();

		if (table == null) {
			table = new ShopTable();
			setBean(table, false);
		}

		if (!isDuplicateOn() && tfTableNo.getInteger() == 0) {
			POSMessageDialog.showError(null, Messages.getString("ShopTableForm.57")); //$NON-NLS-1$
			return false;
		}

		ShopTable tableTocheck = ShopTableDAO.getInstance().get(tfTableNo.getInteger());

		if (tableTocheck != null && selectedTable != tableTocheck.getId()) {
			POSMessageDialog.showError(POSUtil.getBackOfficeWindow(), Messages.getString("ShopTableForm.58")); //$NON-NLS-1$
			return false;
		}

		if (isDuplicateOn()) {

			int nxtTableNumber = ShopTableDAO.getInstance().getNextTableNumber();

			for (int i = 1; i <= nxtTableNumber; i++) {
				ShopTable shopTable = ShopTableDAO.getInstance().get(i);
				if (shopTable == null) {
					table.setId(i);
					break;
				}
			}

			if (table.getId() == null) {
				table.setId(nxtTableNumber + 1);
			}
			table.setTypes(dupCheckValues);
			table.setCapacity(dupCapacity);
			table.setDescription(dupDescription);
			table.setName(dupName);
			table.setFree(dupTableEnable);
			table.setDisable(dupTableDisable);

			setDuplicate(false);
		}
		else {

			table.setId(tfTableNo.getInteger());
			table.setName(tfTableName.getText());
			table.setDescription(tfTableDescription.getText());
			table.setCapacity(tfTableCapacity.getInteger());

			List<ShopTableType> checkValues = tableTypeCBoxList.getCheckedValues();
			table.setTypes(checkValues);

			if (rbFree.isSelected()) {
				table.setFree(true);
				table.setServing(false);
				table.setBooked(false);
				table.setDirty(false);
				table.setDisable(false);
			}
			else if (rbDisable.isSelected()) {
				table.setFree(false);
				table.setServing(false);
				table.setBooked(false);
				table.setDirty(false);
				table.setDisable(true);
			}
		}

		setNewTable(table.getId());

		return true;
	}

	@Override
	public void edit() {
		setBorder(BorderFactory.createTitledBorder(Messages.getString("ShopTableForm.17"))); //$NON-NLS-1$
	}

	@Override
	public String getDisplayText() {
		return Messages.getString("ShopTableForm.18"); //$NON-NLS-1$
	}

	public void setTableTypeCBoxListEnable(boolean enable) {
		this.tableTypeCBoxList.setEnabled(enable);
	}

	public boolean isDuplicateOn() {
		return duplicate;
	}

	public void setDuplicate(boolean duplicate) {
		this.duplicate = duplicate;
	}

	/**
	 * @return the newTable
	 */
	public int getNewTable() {
		return newTable;
	}

	/**
	 * @param newTable the newTable to set
	 */
	public void setNewTable(int newTable) {
		this.newTable = newTable;
	}

}