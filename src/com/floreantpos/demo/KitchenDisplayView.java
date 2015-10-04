package com.floreantpos.demo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import com.floreantpos.Messages;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.KitchenTicket;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.PosPrinters;
import com.floreantpos.model.Printer;
import com.floreantpos.model.dao.KitchenTicketDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosComboRenderer;
import com.floreantpos.ui.HeaderPanel;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.order.ViewPanel;

public class KitchenDisplayView extends ViewPanel implements ActionListener {

	public final static String VIEW_NAME = "KD"; //$NON-NLS-1$

	private JComboBox<Printer> cbPrinters = new JComboBox<Printer>();
	private JComboBox<OrderType> cbTicketTypes = new JComboBox<OrderType>();

	HeaderPanel headerPanel;
	
	KitchenTicketListPanel ticketPanel = new KitchenTicketListPanel();

	private Timer viewUpdateTimer;

	private PosButton btnLogout;

	public KitchenDisplayView(boolean showHeader) {
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
		
		JPanel firstTopPanel = new JPanel(new BorderLayout(5, 5));
		if(showHeader) {
			headerPanel = new HeaderPanel();
			firstTopPanel.add(headerPanel, BorderLayout.NORTH);
		}

		JPanel topPanel = new JPanel(new MigLayout("", "[][][][][fill,grow][]", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		topPanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("KitchenDisplayView.4"))); //$NON-NLS-1$
		JLabel label = new JLabel(Messages.getString("KitchenDisplayView.5")); //$NON-NLS-1$
		label.setFont(font);
		topPanel.add(label);
		topPanel.add(cbPrinters);

		cbTicketTypes.setFont(font);
		cbTicketTypes.setRenderer(new PosComboRenderer());
		DefaultComboBoxModel<OrderType> ticketTypeModel = new DefaultComboBoxModel<OrderType>(OrderType.values());
		ticketTypeModel.insertElementAt(null, 0);
		cbTicketTypes.setModel(ticketTypeModel);
		cbTicketTypes.setSelectedIndex(0);
		cbTicketTypes.addActionListener(this);
		JLabel label2 = new JLabel(Messages.getString("KitchenDisplayView.6")); //$NON-NLS-1$
		label2.setFont(font);

		topPanel.add(label2);
		topPanel.add(cbTicketTypes);

		if (TerminalConfig.isKitchenMode()) {
			btnLogout = new PosButton(Messages.getString("KitchenDisplayView.7")); //$NON-NLS-1$
			btnLogout.addActionListener(this);
			topPanel.add(new JLabel(), "grow"); //$NON-NLS-1$
			topPanel.add(btnLogout);
		}

		firstTopPanel.add(topPanel);
		add(firstTopPanel, BorderLayout.NORTH);

		ticketPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		JScrollPane scrollPane = new JScrollPane(ticketPanel);
		scrollPane.getHorizontalScrollBar().setSize(new Dimension(100, 60));
		scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(100, 60));
		add(scrollPane);

		viewUpdateTimer = new Timer(5 * 1000, this);
		viewUpdateTimer.setRepeats(true);
	}

	public void addTicket(KitchenTicket ticket) {
		addTicket(ticket, true);
	}

	private synchronized void addTicket(KitchenTicket ticket, boolean updateView) {
		if (!isShowing())
			return;

		Printer selectedPrinter = (Printer) cbPrinters.getSelectedItem();
		if (selectedPrinter != null && !selectedPrinter.equals(ticket.getPrinters())) {
			return;
		}

		OrderType selectedTicketType = (OrderType) cbTicketTypes.getSelectedItem();
		if (selectedTicketType != null && selectedTicketType != ticket.getType()) {
			return;
		}

		if (ticketPanel.addTicket(ticket)) {
			if (updateView) {
				//ticketPanel.revalidate();
				ticketPanel.repaint();
			}
		}
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

	public synchronized void cleanup() {
		viewUpdateTimer.stop();
		ticketPanel.removeAll();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() != null && e.getActionCommand().equalsIgnoreCase("log out")) { //$NON-NLS-1$
			Application.getInstance().doLogout();
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

			for (KitchenTicket kitchenTicket : list) {
				addTicket(kitchenTicket, false);
			}

			//ticketPanel.revalidate();
			ticketPanel.repaint();

		} catch (Exception e2) {
			POSMessageDialog.showError(this, e2.getMessage(), e2);
		} finally {
			viewUpdateTimer.restart();
		}
	}

	@Override
	public String getViewName() {
		return VIEW_NAME;
	}
}
