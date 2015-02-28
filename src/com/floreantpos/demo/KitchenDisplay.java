package com.floreantpos.demo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.main.Application;
import com.floreantpos.model.KitchenTicket;
import com.floreantpos.model.PosPrinters;
import com.floreantpos.model.Printer;
import com.floreantpos.model.dao.KitchenTicketDAO;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class KitchenDisplay extends JFrame implements ActionListener {

	public static final KitchenDisplay instance = new KitchenDisplay();

	private JComboBox<Printer> cbPrinters = new JComboBox<Printer>();

	JPanel ticketPanel = new JPanel(new MigLayout("filly"));

	private Timer viewUpdateTimer;

	public KitchenDisplay() {
		setTitle("Kitchen Display");
		setIconImage(Application.getApplicationIcon().getImage());

		PosPrinters printers = Application.getPrinters();
		List<Printer> kitchenPrinters = printers.getKitchenPrinters();
		DefaultComboBoxModel<Printer> printerModel = new DefaultComboBoxModel<Printer>();
		printerModel.addElement(null);
		for (Printer printer : kitchenPrinters) {
			printerModel.addElement(printer);
		}

		cbPrinters.setModel(printerModel);
		cbPrinters.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ticketPanel.removeAll();
				updateTicketView();
			}
		});

		JPanel topPanel = new JPanel();
		topPanel.add(new JLabel("Printer"));
		topPanel.add(cbPrinters);

		add(topPanel, BorderLayout.NORTH);

		ticketPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		JScrollPane scrollPane = new JScrollPane(ticketPanel);
		scrollPane.getHorizontalScrollBar().setSize(new Dimension(100, 60));
		scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(100, 60));
		add(scrollPane);

		setSize(Toolkit.getDefaultToolkit().getScreenSize());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		viewUpdateTimer = new Timer(30 * 1000, this);
		viewUpdateTimer.setRepeats(true);
	}

	public void addTicket(KitchenTicket ticket) {
		addTicket(ticket, true);
	}

	private void addTicket(KitchenTicket ticket, boolean updateView) {
		if (!isShowing())
			return;

		Printer selectedPrinter = (Printer) cbPrinters.getSelectedItem();
		if (selectedPrinter != null && !selectedPrinter.equals(ticket.getPrinter())) {
			return;
		}

		KitchenTicketView view = new KitchenTicketView(this, ticket);
		ticketPanel.add(view, "growy, width pref!");

		if (updateView) {
			ticketPanel.revalidate();
			ticketPanel.repaint();
		}
	}

	public void removeTicket(KitchenTicketView view) {
		//view.stopTimer();
		ticketPanel.remove(view);
		ticketPanel.revalidate();
		ticketPanel.repaint();
	}

	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);

		if (b) {
			updateTicketView();

			if (!viewUpdateTimer.isRunning()) {
				viewUpdateTimer.start();
			}
		}
		else {
			cleanup();
		}
	}

	@Override
	public void dispose() {
		cleanup();

		super.dispose();
	}

	private void cleanup() {
		viewUpdateTimer.stop();

		Component[] components = ticketPanel.getComponents();
		for (Component component : components) {
			if (component instanceof KitchenTicketView) {
				KitchenTicketView kitchenTicketView = (KitchenTicketView) component;
				kitchenTicketView.stopTimer();
			}
		}

		ticketPanel.removeAll();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		updateTicketView();
	}

	private synchronized void updateTicketView() {
		try {
			viewUpdateTimer.stop();

			List<KitchenTicket> list = KitchenTicketDAO.getInstance().findAllOpen();
			List<KitchenTicket> existingList = new ArrayList<KitchenTicket>();

			Component[] components = ticketPanel.getComponents();
			for (Component component : components) {
				if (component instanceof KitchenTicketView) {
					KitchenTicketView kitchenTicketView = (KitchenTicketView) component;
					kitchenTicketView.refreshTicket();
					existingList.add(kitchenTicketView.getTicket());
				}
			}

			for (KitchenTicket kitchenTicket : list) {
				if (!existingList.contains(kitchenTicket)) {
					addTicket(kitchenTicket, false);
				}
			}
			
			ticketPanel.revalidate();
			ticketPanel.repaint();
			
		} catch (Exception e2) {
			POSMessageDialog.showError(this, e2.getMessage(), e2);
		} finally {
			viewUpdateTimer.restart();
		}
	}
}
