package com.floreantpos.table;

import java.awt.Color;
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

import net.miginfocom.swing.MigLayout;

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

public class ShopTableForm extends BeanEditor<ShopTable> {

	private FixedLengthTextField tfTableDescription;
	private FixedLengthTextField tfTableCapacity;
	private FixedLengthTextField tfTableNo;
	private FixedLengthTextField tfTableName;
	private CheckBoxList tableTypeCBoxList;
	private JPanel statusPanel;
	private JRadioButton rbFree;
	private JRadioButton rbBooked;
	private JRadioButton rbDirty;
	private JRadioButton rbServing;
	private JRadioButton rbDisable;
	private JButton btnCapacityOne;
	private JButton btnCapacityTwo;
	private JButton btnCapacityFour;
	private JButton btnCapacitySix;
	private JButton btnCapacityEight;
	private JButton btnCapacityTen;
	private JButton btnCreateType;
	
	private Integer nextTableNumber;
	private String dupName;
	private  Integer dupCapacity;
	private String dupDescription;
	
	private boolean tmp;
	private boolean editMode;

	public ShopTableForm() {
		setPreferredSize(new Dimension(600, 800));
		setLayout(new MigLayout("", "[][grow]", "[][][][][][]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		setBorder(BorderFactory.createTitledBorder(Messages.getString("ShopTableForm.19")));
		tableTypeCBoxList = new CheckBoxList();
		tableTypeCBoxList.setModel(ShopTableTypeDAO.getInstance().findAll());
		JScrollPane tableTypeCheckBoxList = new JScrollPane(tableTypeCBoxList);
		tableTypeCBoxList.setEnabled(false);

		JLabel lblName = new JLabel(Messages.getString("ShopTableForm.0")); //$NON-NLS-1$
		add(lblName, "cell 0 0,alignx trailing,aligny center"); //$NON-NLS-1$

		tfTableNo = new FixedLengthTextField(6);
		add(tfTableNo, "cell 1 0,aligny top"); //$NON-NLS-1$

		tfTableNo.setEnabled(false);

		JLabel lblTableName = new JLabel(Messages.getString("ShopTableForm.1")); //$NON-NLS-1$
		//add(lblTableName, "cell 0 1,alignx trailing"); //$NON-NLS-1$

		tfTableName = new FixedLengthTextField();
		/*	tfTableName.setColumns(20);
		add(tfTableName, "cell 1 1,growx"); //$NON-NLS-1$
		*/
		JLabel lblAddress = new JLabel(Messages.getString("ShopTableForm.2")); //$NON-NLS-1$
		add(lblAddress, "cell 0 2,alignx trailing"); //$NON-NLS-1$

		tfTableDescription = new FixedLengthTextField();
		add(tfTableDescription, "cell 1 2,growx"); //$NON-NLS-1$

		JLabel lblCitytown = new JLabel(Messages.getString("ShopTableForm.3")); //$NON-NLS-1$
		add(lblCitytown, "cell 0 3,alignx trailing"); //$NON-NLS-1$

		tfTableCapacity = new FixedLengthTextField();
		tfTableCapacity.setPreferredSize(new Dimension(110, 52));
		tfTableCapacity.setText("4");
		add(tfTableCapacity, "flowx,cell 1 3"); //$NON-NLS-1$

		ActionListener action = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (e.getSource() == btnCapacityOne) {
					tfTableCapacity.setText("1");
				}
				else if (e.getSource() == btnCapacityTwo) {
					tfTableCapacity.setText("2");
				}
				else if (e.getSource() == btnCapacityFour) {
					tfTableCapacity.setText("4");
				}
				else if (e.getSource() == btnCapacitySix) {
					tfTableCapacity.setText("6");
				}
				else if (e.getSource() == btnCapacityEight) {
					tfTableCapacity.setText("8");
				}
				else if (e.getSource() == btnCapacityTen) {
					tfTableCapacity.setText("10");
				}
			}
		};

		btnCapacityOne = new PosButton("1");
		btnCapacityOne.setPreferredSize(new Dimension(52, 52));
		btnCapacityTwo = new PosButton("2");
		btnCapacityTwo.setPreferredSize(new Dimension(52, 52));
		btnCapacityFour = new PosButton("4");
		btnCapacityFour.setPreferredSize(new Dimension(52, 52));
		btnCapacitySix = new PosButton("6");
		btnCapacitySix.setPreferredSize(new Dimension(52, 52));
		btnCapacityEight = new PosButton("8");
		btnCapacityEight.setPreferredSize(new Dimension(52, 52));
		btnCapacityTen = new PosButton("10");
		btnCapacityTen.setPreferredSize(new Dimension(52, 52));

		btnCapacityOne.addActionListener(action);
		btnCapacityTwo.addActionListener(action);
		btnCapacityFour.addActionListener(action);
		btnCapacitySix.addActionListener(action);
		btnCapacityEight.addActionListener(action);
		btnCapacityTen.addActionListener(action);

		add(btnCapacityOne, "cell 1 3");
		add(btnCapacityTwo, "cell 1 3");
		add(btnCapacityFour, "cell 1 3");
		add(btnCapacitySix, "cell 1 3");
		add(btnCapacityEight, "cell 1 3");
		add(btnCapacityTen, "cell 1 3");

		statusPanel = new JPanel();
		statusPanel.setBorder(new TitledBorder(null, Messages.getString("ShopTableForm.4"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$
		add(statusPanel, "cell 1 4,grow"); //$NON-NLS-1$
		statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		rbFree = new JRadioButton(Messages.getString("ShopTableForm.5")); //$NON-NLS-1$
		statusPanel.add(rbFree);

		rbServing = new JRadioButton(Messages.getString("ShopTableForm.6")); //$NON-NLS-1$
		statusPanel.add(rbServing);

		rbBooked = new JRadioButton(Messages.getString("ShopTableForm.7")); //$NON-NLS-1$
		statusPanel.add(rbBooked);

		rbDirty = new JRadioButton(Messages.getString("ShopTableForm.8")); //$NON-NLS-1$
		statusPanel.add(rbDirty);

		rbDisable = new JRadioButton(Messages.getString("ShopTableForm.9")); //$NON-NLS-1$
		statusPanel.add(rbDisable);

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(rbFree);
		buttonGroup.add(rbServing);
		buttonGroup.add(rbBooked);
		buttonGroup.add(rbDirty);
		buttonGroup.add(rbDisable);
		
		add(new JLabel(), "grow,span");
		btnCreateType = new JButton("Create Table Type");

		final FloorLayoutPlugin floorLayoutPlugin = (FloorLayoutPlugin) ExtensionManager.getPlugin(FloorLayoutPlugin.class);

		if (floorLayoutPlugin != null) {

			add(new JLabel(Messages.getString("ShopTableForm.10")), "cell 0 5"); //$NON-NLS-1$ //$NON-NLS-2$
			//tableTypeCheckBoxList.setPreferredSize(new Dimension(0, 200));
			add(tableTypeCheckBoxList, "cell 1 5,wrap,grow"); //$NON-NLS-1$
			add(btnCreateType, "cell 1 6");

			btnCreateType.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					BeanEditorDialog dialog = new BeanEditorDialog(floorLayoutPlugin.getBeanEditor());
					dialog.open();
					tableTypeCBoxList.setModel(ShopTableTypeDAO.getInstance().findAll());
				}
			});
		}

	}

	@Override
	public void createNew() {
		tfTableNo.setEnabled(true);
		ShopTable bean2 = new ShopTable();
		bean2.setCapacity(4);

		try {
			int nxtTableNumber = ShopTableDAO.getInstance().getNextTableNumber();
			bean2.setId(nxtTableNumber + 1);
			bean2.setName("" + (nxtTableNumber + 1)); //$NON-NLS-1$
			bean2.setDescription("" + (nxtTableNumber + 1)); //$NON-NLS-1$

		} catch (Exception e) {
			BOMessageDialog.showError(this, Messages.getString("ShopTableForm.13")); //$NON-NLS-1$
		}

		setBean(bean2);
		setBorder(BorderFactory.createTitledBorder(Messages.getString("ShopTableForm.18")));
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
		setBorder(BorderFactory.createTitledBorder("Table Details"));
	}

	@Override
	public boolean delete() {
		try {
			ShopTable bean2 = getBean();
			if (bean2 == null)
				return false;

			int option = POSMessageDialog.showYesNoQuestionDialog(POSUtil.getBackOfficeWindow(),
					Messages.getString("ShopTableForm.14"), Messages.getString("ShopTableForm.15")); //$NON-NLS-1$ //$NON-NLS-2$
			if (option != JOptionPane.YES_OPTION) {
				return false;
			}
			//TableBookingInfoDAO.getInstance().
			ShopTableDAO.getInstance().delete(bean2);

			return true;
		} catch (Exception e) {
			POSMessageDialog.showError(POSUtil.getBackOfficeWindow(), e.getMessage(), e);
		}
		return false;
	}
	
	public void deleteAllTables() {
		
		int option = POSMessageDialog.showYesNoQuestionDialog(POSUtil.getBackOfficeWindow(),
				"This will Remove all your tables. Are you sure?", "Confirm"); //$NON-NLS-1$ //$NON-NLS-2$
		if (option != JOptionPane.YES_OPTION) {
			return;
		}
		
		List<ShopTable> list=ShopTableDAO.getInstance().findAll();
				
		List<TableBookingInfo> bookingList=TableBookingInfoDAO.getInstance().findAll();
		
		for(TableBookingInfo info: bookingList) {
			info.setTables(null);
			TableBookingInfoDAO.getInstance().saveOrUpdate(info);
		}
		
		for (ShopTable table : list) {
			table.setFloor(null);
			table.setTypes(null);
			ShopTableDAO.getInstance().delete(table);
		}
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

		tfTableName.setEditable(enable);
		tfTableDescription.setEditable(enable);
		tfTableCapacity.setEditable(enable);

		btnCapacityOne.setEnabled(enable);
		btnCapacityTwo.setEnabled(enable);
		btnCapacityFour.setEnabled(enable);
		btnCapacitySix.setEnabled(enable);
		btnCapacityEight.setEnabled(enable);
		btnCapacityTen.setEnabled(enable);
		btnCreateType.setEnabled(enable);

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

			if (!isEditMode()) {

				ShopTable shopTable = ShopTableDAO.getInstance().get(table.getId());

				if (shopTable != null) {
					POSMessageDialog.showError(POSUtil.getBackOfficeWindow(), "This number already assigned, please choose another one");
					return false;
				}
			}
			ShopTableDAO.getInstance().saveOrUpdate(table);
			updateView();
			setEditMode(false);
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
		rbServing.setSelected(table.isServing());
		rbBooked.setSelected(table.isBooked());
		rbDirty.setSelected(table.isDirty());
		rbDisable.setSelected(table.isDisable());
		
		nextTableNumber=table.getTableNumber();
		System.out.println("ss"+table.getTableNumber());
		dupName=table.getName();
		dupCapacity=table.getCapacity();
		dupDescription=table.getDescription();
System.out.println(dupDescription);
	}

	@Override
	protected boolean updateModel() throws IllegalModelStateException {

		ShopTable table = (ShopTable) getBean();

		if (table == null) {
			table = new ShopTable();
			setBean(table, false);
		}

		if (isTmp()) {
			System.out.println("in");
			nextTableNumber = nextTableNumber + 1;
			table.setId(nextTableNumber);
			table.setCapacity(dupCapacity);
			table.setDescription(dupDescription);
			table.setName(dupName);
			setTmp(false);
		}
		else {
			System.out.println("else");
			
			nextTableNumber = Integer.parseInt(tfTableNo.getText());
			table.setId(nextTableNumber);
			table.setName(tfTableName.getText());
			table.setDescription(tfTableDescription.getText());
			table.setCapacity(Integer.parseInt(tfTableCapacity.getText()));
		}

		List<ShopTableType> checkValues = tableTypeCBoxList.getCheckedValues();
		table.setTypes(checkValues);

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
	public void edit() {
		setBorder(BorderFactory.createTitledBorder(Messages.getString("ShopTableForm.17")));
		tfTableNo.setEnabled(false);
		setEditMode(true);
	}

	@Override
	public String getDisplayText() {
		return Messages.getString("ShopTableForm.18"); //$NON-NLS-1$
	}

	public void setTableTypeCBoxListEnable(boolean enable) {
		this.tableTypeCBoxList.setEnabled(enable);
	}

	public boolean isTmp() {
		return tmp;
	}

	public void setTmp(boolean tmp) {
		this.tmp = tmp;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}
}
