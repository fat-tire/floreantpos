package com.floreantpos.demo;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.model.KitchenTicket;
import com.floreantpos.model.dao.KitchenTicketDAO;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class KitchenDisplay extends JFrame implements ActionListener {

	public static final KitchenDisplay instance = new KitchenDisplay();

	JPanel ticketPanel = new JPanel(new MigLayout("filly"));

	private Timer viewUpdateTimer;

	public KitchenDisplay() {
		setTitle("Kitchen Display");

		ticketPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(new JScrollPane(ticketPanel));

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		viewUpdateTimer = new Timer(30 * 1000, this);
		viewUpdateTimer.setRepeats(true);
	}

	public void addTicket(KitchenTicket ticket) {
		KitchenTicketView view = new KitchenTicketView(this, ticket);
		ticketPanel.add(view, "growy, width pref!");
		ticketPanel.revalidate();
		ticketPanel.repaint();
		setVisible(true);
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
			if (!viewUpdateTimer.isRunning()) {
				viewUpdateTimer.start();
			}
		}
		else {
			viewUpdateTimer.stop();
		}
	}

	@Override
	public void dispose() {
		viewUpdateTimer.stop();

		super.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		updateTicketView();
	}

	private void updateTicketView() {
		try {
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
					addTicket(kitchenTicket);
				}
			}
		} catch (Exception e2) {
			POSMessageDialog.showError(this, e2.getMessage(), e2);
		}
	}
}
