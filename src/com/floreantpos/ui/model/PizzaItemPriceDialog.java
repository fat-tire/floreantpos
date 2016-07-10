package com.floreantpos.ui.model;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.combobox.ListComboBoxModel;

import com.floreantpos.Messages;
import com.floreantpos.model.MenuItemSize;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.PizzaCrust;
import com.floreantpos.model.PizzaPrice;
import com.floreantpos.model.dao.MenuItemSizeDAO;
import com.floreantpos.model.dao.OrderTypeDAO;
import com.floreantpos.model.dao.PizzaCrustDAO;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class PizzaItemPriceDialog extends POSDialog {
	private JPanel contentPane;
	private JButton btnOK;
	private JButton btnCancel;
	// private JComboBox cbOrderTypes;
	private JComboBox cbCrust;
	private JComboBox cbSize;
	private JTextField tfPrice;

	private PizzaPrice pizzaPrice;
	List<PizzaPrice> pizzaPriceList;

	public PizzaItemPriceDialog(Frame owner, PizzaPrice pizzaPrice, List<PizzaPrice> pizzaPriceList) {
		super(owner, true);
		this.pizzaPrice = pizzaPrice;
		this.pizzaPriceList = pizzaPriceList;
		init();
		updateView();
	}

	private void init() {
		createView();

		setModal(true);
		getRootPane().setDefaultButton(btnOK);

		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onOK();
			}
		});

		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		});

		// call onCancel() when cross is clicked
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});

		// call onCancel() on ESCAPE
		contentPane.registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

	}

	private void onOK() {
		if (!updateModel())
			return;

		try {
			setCanceled(false);
			dispose();
		} catch (Exception e) {
			POSMessageDialog.showError(this, com.floreantpos.POSConstants.ERROR_MESSAGE, e);
		}
	}

	private void onCancel() {
		setCanceled(true);
		dispose();
	}

	private void updateView() {
		if (pizzaPrice == null) {
			return;
		}

		cbSize.setSelectedItem(pizzaPrice.getSize());
		cbCrust.setSelectedItem(pizzaPrice.getCrust());
		// cbOrderTypes.setSelectedItem(pizzaPrice.getOrderType());
		tfPrice.setText(String.valueOf(pizzaPrice.getPrice()));
	}

	public boolean updateModel() {
		if (pizzaPrice == null) {
			pizzaPrice = new PizzaPrice();
		}

		if (cbSize.getSelectedItem() == null) {
			POSMessageDialog.showError(this, "Please Select Size!");
			return false;
		}
		if (cbCrust.getSelectedItem() == null) {
			POSMessageDialog.showError(this, "Please Select Crust!");
			return false;
		}
		if (tfPrice.getText() == null) {
			POSMessageDialog.showError(this, "Price Cannot Be Empty!");
			return false;
		}

		double price = 0;
		try {
			price = Double.parseDouble(tfPrice.getText());
		} catch (Exception x) {
			POSMessageDialog.showError(this, com.floreantpos.POSConstants.PRICE_IS_NOT_VALID_);
			return false;
		}

		if (pizzaPriceList != null) {
			for (PizzaPrice pc : pizzaPriceList) {
				if (pc.getSize().equals(cbSize.getSelectedItem()) && pc.getCrust().equals(cbCrust.getSelectedItem())) {
					if (pc != this.pizzaPrice) {
						POSMessageDialog.showMessage(this, "Duplicate item cannot be entered");
						return false;
					}
				}
			}
		}

		pizzaPrice.setSize((MenuItemSize) cbSize.getSelectedItem());
		pizzaPrice.setCrust((PizzaCrust) cbCrust.getSelectedItem());
		// pizzaPrice.setOrderType((OrderType) cbOrderTypes.getSelectedItem());
		pizzaPrice.setPrice(Double.valueOf(price));
		return true;
	}

	private void createView() {
		contentPane = new JPanel(new BorderLayout());

		List<MenuItemSize> menuItemSizeList = MenuItemSizeDAO.getInstance().findAll();
		List<PizzaCrust> crustList = PizzaCrustDAO.getInstance().findAll();
		List<OrderType> orderTypeList = OrderTypeDAO.getInstance().findAll();
		orderTypeList.add(0, null);

		final JLabel sizeLabel = new JLabel();
		sizeLabel.setText("Size");
		cbSize = new JComboBox(new ListComboBoxModel<MenuItemSize>(menuItemSizeList));

		final JLabel crustLabel = new JLabel();
		crustLabel.setText("Crust");
		cbCrust = new JComboBox(new ListComboBoxModel<PizzaCrust>(crustList));
		// new DefaultComboBoxModel(TaxDAO.getInstance().findAll().toArray())

		// final JLabel orderTypeLabel = new JLabel();
		// orderTypeLabel.setText("OrderType");
		// cbOrderTypes = new JComboBox(new
		// ListComboBoxModel<OrderType>(orderTypeList));

		final JLabel priceLabel = new JLabel();
		priceLabel.setText(com.floreantpos.POSConstants.PRICE + ":"); //$NON-NLS-1$
		tfPrice = new JTextField();

		JPanel panel = new JPanel(new MigLayout("", "grow", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		panel.add(sizeLabel, "right"); //$NON-NLS-1$
		panel.add(cbSize, "grow,wrap"); //$NON-NLS-1$
		panel.add(crustLabel, "right"); //$NON-NLS-1$
		panel.add(cbCrust, "grow,wrap"); //$NON-NLS-1$
		//		panel.add(orderTypeLabel, "right"); //$NON-NLS-1$
		//		panel.add(cbOrderTypes, "grow,wrap"); //$NON-NLS-1$
		panel.add(priceLabel, "right"); //$NON-NLS-1$
		panel.add(tfPrice, "grow"); //$NON-NLS-1$

		contentPane.add(panel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new MigLayout("al center center", "sg", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		btnOK = new JButton(Messages.getString("MenuItemPriceByOrderTypeDialog.0")); //$NON-NLS-1$
		btnCancel = new JButton(Messages.getString("MenuItemPriceByOrderTypeDialog.21")); //$NON-NLS-1$

		buttonPanel.add(btnOK, "grow"); //$NON-NLS-1$
		buttonPanel.add(btnCancel, "grow"); //$NON-NLS-1$
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		add(contentPane);
	}

	public PizzaPrice getPizzaPrice() {
		return pizzaPrice;
	}
}
