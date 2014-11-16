package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.Blob;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import org.apache.commons.io.FileUtils;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.lob.BlobImpl;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.config.ui.ConfigurationView;
import com.floreantpos.model.ShopFloor;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.dao.ShopFloorDAO;
import com.floreantpos.swing.ImageComponent;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.dialog.NumberSelectionDialog2;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class FloorConfigurationView extends ConfigurationView {

	private ShopFloor floor;

	TitlePanel titlePanel = new TitlePanel();
	private JLayeredPane floorPanel = new JLayeredPane();
	JPanel buttonPanel = new JPanel();

	public FloorConfigurationView() {
		setLayout(new BorderLayout(5, 5));

		floorPanel.setBorder(new CompoundBorder(new EtchedBorder(), new EmptyBorder(20, 20, 20, 20)));
		floorPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				NumberSelectionDialog2 dialog = new NumberSelectionDialog2((Dialog) SwingUtilities.getWindowAncestor(FloorConfigurationView.this));
				dialog.setTitle("Enter table number");
				dialog.setFloatingPoint(false);
				dialog.pack();
				dialog.open();

				if (dialog.isCanceled()) {
					return;
				}

				int tableNumber = (int) dialog.getValue();
				ShopTable table = new ShopTable();
				table.setNumber(tableNumber);
				table.setX(e.getX());
				table.setY(e.getY());
				floor.addTotables(table);
				
				JButton button = new JButton(table.getNumber() + "");
				button.setBounds(table.getX() - 20, table.getY() - 20, 40, 40);
				floorPanel.add(button);
				floorPanel.moveToFront(button);
				floorPanel.revalidate();
				floorPanel.repaint();
			}
		});

		floorPanel.setPreferredSize(new Dimension(600, 400));
		JPanel floorPanelContainer = new JPanel();
		floorPanelContainer.add(floorPanel);

		add(floorPanelContainer);

		createButtonPanel();

		try {
			renderFloor();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private void createButtonPanel() {
		JButton btnAdd = new JButton("SET FLOOR IMAGE");
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectImageFromFile();
			}
		});

		buttonPanel.add(btnAdd);

		JButton clearButton = new JButton("REMOVE TABLES");
		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (floor == null) {
					return;
				}

				if (floor.getTables() == null)
					return;

				floor.getTables().clear();

				try {
					renderFloor();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		buttonPanel.add(clearButton);

		add(buttonPanel, BorderLayout.SOUTH);
	}

	private void renderFloor() throws Exception {
		floorPanel.removeAll();

		if (floor == null) {
			return;
		}

		byte[] imageData = floor.getImageData();
		if (imageData == null)
			return;

		ImageIcon imageIcon = new ImageIcon(imageData);
		ImageComponent imageComponent = new ImageComponent(imageIcon.getImage());
		imageComponent.setBounds(floorPanel.getVisibleRect());
		floorPanel.add(imageComponent);
		//floorPanel.setComponentZOrder(imageComponent, 1);

		Set<ShopTable> tables = floor.getTables();
		if (tables != null) {
			for (ShopTable shopTable : tables) {
				JButton button = new JButton(shopTable.getNumber() + "");
				button.setBounds(shopTable.getX() - 20, shopTable.getY() - 20, 40, 40);
				floorPanel.add(button);
				//floorPanel.setComponentZOrder(button, 2);
			}
		}

		floorPanel.moveToBack(imageComponent);
		floorPanel.revalidate();
		floorPanel.repaint();
	}

	@Override
	public boolean save() throws Exception {
		ShopFloorDAO.getInstance().save(floor);
		return true;
	}

	@Override
	public void initialize() throws Exception {
		List<ShopFloor> floors = ShopFloorDAO.getInstance().findAll();
		if (floors == null || floors.size() == 0) {
			this.floor = new ShopFloor();
			setInitialized(true);
			return;
		}

		setFloor(floors.get(0));
		setInitialized(true);
	}

	@Override
	public String getName() {
		return "Tables";
	}

	public ShopFloor getFloor() {
		return floor;
	}

	public void setFloor(ShopFloor floor) {
		this.floor = floor;
		try {
			renderFloor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectImageFromFile() {
		JFileChooser fileChooser = new JFileChooser();
		int option = fileChooser.showOpenDialog(BackOfficeWindow.getInstance());
		if (option != JFileChooser.APPROVE_OPTION) {
			return;
		}

		File file = fileChooser.getSelectedFile();
		try {
			byte[] imageData = FileUtils.readFileToByteArray(file);

			ShopFloorDAO dao = ShopFloorDAO.getInstance();
			//Session session = dao.getSession();
			//BlobImpl blob = new BlobImpl(imageData);

			floor.setImageData(imageData);
			renderFloor();
		} catch (Exception e1) {
			e1.printStackTrace();

			POSMessageDialog.showMessage(BackOfficeWindow.getInstance(), e1.getMessage());
		}
	}

}
