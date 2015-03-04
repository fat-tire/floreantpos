package com.floreantpos.demo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.KitchenTicket;
import com.floreantpos.model.PosPrinters;
import com.floreantpos.model.Printer;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.dao.KitchenTicketDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosComboRenderer;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class KitchenDisplayView extends JPanel implements ActionListener {

	public final static String VIEW_NAME = "KD";

	public static final KitchenDisplayView instance = new KitchenDisplayView();

	private JComboBox<Printer> cbPrinters = new JComboBox<Printer>();
	private JComboBox<TicketType> cbTicketTypes = new JComboBox<TicketType>();

	JPanel ticketPanel = new JPanel(new MigLayout("filly"));

	private Timer viewUpdateTimer;

	private PosButton btnLogout;

	public KitchenDisplayView() {
		setLayout(new BorderLayout(5, 5));
		PosPrinters printers = Application.getPrinters();
		List<Printer> kitchenPrinters = printers.getKitchenPrinters();
		DefaultComboBoxModel<Printer> printerModel = new DefaultComboBoxModel<Printer>();
		printerModel.addElement(null);
		for (Printer printer : kitchenPrinters) {
			printerModel.addElement(printer);
		}

		Font font = getFont().deriveFont(18f);

		cbPrinters.setFont(font);
		cbPrinters.setRenderer(new PosComboRenderer());
		cbPrinters.setModel(printerModel);
		cbPrinters.addActionListener(this);

		JPanel topPanel = new JPanel(new MigLayout("", "[][][][][fill,grow][]", ""));
		topPanel.setBorder(BorderFactory.createTitledBorder("Filters"));
		JLabel label = new JLabel("Printer");
		label.setFont(font);
		topPanel.add(label);
		topPanel.add(cbPrinters);

		cbTicketTypes.setFont(font);
		cbTicketTypes.setRenderer(new PosComboRenderer());
		DefaultComboBoxModel<TicketType> ticketTypeModel = new DefaultComboBoxModel<TicketType>(TicketType.values());
		ticketTypeModel.insertElementAt(null, 0);
		cbTicketTypes.setModel(ticketTypeModel);
		cbTicketTypes.addActionListener(this);
		JLabel label2 = new JLabel("Order type");
		label2.setFont(font);

		topPanel.add(label2);
		topPanel.add(cbTicketTypes);

		if (TerminalConfig.isKitchenMode()) {
			btnLogout = new PosButton("LOG OUT");
			btnLogout.addActionListener(this);
			topPanel.add(new JLabel(), "grow");
			topPanel.add(btnLogout);
		}

		add(topPanel, BorderLayout.NORTH);

		ticketPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		JScrollPane scrollPane = new JScrollPane(ticketPanel);
		scrollPane.getHorizontalScrollBar().setSize(new Dimension(100, 60));
		scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(100, 60));
		add(scrollPane);

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

		TicketType selectedTicketType = (TicketType) cbTicketTypes.getSelectedItem();
		if (selectedTicketType != null && selectedTicketType != ticket.getType()) {
			return;
		}

		KitchenTicketView view = new KitchenTicketView(ticket);
		ticketPanel.add(view, "growy, width pref!");

		if (updateView) {
			ticketPanel.revalidate();
			ticketPanel.repaint();
		}
	}

	public void removeTicket(KitchenTicketView view) {
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

	public void cleanup() {
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
		if (e.getActionCommand() != null && e.getActionCommand().equalsIgnoreCase("log out")) {
			Application.getInstance().logout();
		}
		if (e.getSource() == viewUpdateTimer) {
			updateTicketView();
		}
		else {
			ticketPanel.removeAll();
			updateTicketView();
		}
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
					//kitchenTicketView.refreshTicket();
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
