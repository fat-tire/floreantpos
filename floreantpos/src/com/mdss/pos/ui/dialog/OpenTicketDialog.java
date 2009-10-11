/*
 * OpenTicketDialog.java
 *
 * Created on August 23, 2006, 11:59 PM
 */

package com.mdss.pos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;

import org.hibernate.Session;

import com.mdss.pos.IconFactory;
import com.mdss.pos.model.Ticket;
import com.mdss.pos.model.dao.TicketDAO;
import com.mdss.pos.swing.PosButton;
import com.mdss.pos.swing.TransparentPanel;

/**
 *
 * @author  MShahriar
 */
public class OpenTicketDialog extends POSDialog implements ActionListener {
	private CardLayout cardLayout = new CardLayout();
	private Dimension buttonDimension = new Dimension(100, 60);
	private Ticket selectedTicket;

	/** Creates new form OpenTicketDialog */
	public OpenTicketDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal, true);

		TransparentPanel contentPane = new TransparentPanel(new BorderLayout(5, 5));
		contentPane.setOpaque(true);
		setContentPane(contentPane);

		initComponents();

		String[][] numbers = { { "7", "8", "9" }, { "4", "5", "6" }, { "1", "2", "3" }, { "0", "GO" } };
		int[][] gridwidths = { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 }, { 1, 2 } };

		String[][] iconNames = new String[][] { { "7_32.png", "8_32.png", "9_32.png" }, { "4_32.png", "5_32.png", "6_32.png" }, { "1_32.png", "2_32.png", "3_32.png" }, { "0_32.png", "finish_32.png" } };

		ticketNumberPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.insets = new Insets(3, 3, 3, 3);
		gbc.fill = GridBagConstraints.BOTH;

		Dimension preferredSize = new Dimension(60, 70);

		for (int i = 0; i < numbers.length; i++) {
			gbc.gridy = i;
			for (int j = 0; j < numbers[i].length; j++) {
				gbc.gridx = j;
				gbc.gridwidth = gridwidths[i][j];

				PosButton posButton = new PosButton();
				ImageIcon icon = IconFactory.getIcon(iconNames[i][j]);
				String buttonText = String.valueOf(numbers[i][j]);

				if (icon == null) {
					posButton.setText(buttonText);
				}
				else {
					posButton.setIcon(icon);
					if ("GO".equals(buttonText)) {
						posButton.setText(buttonText);
					}
				}

				posButton.setActionCommand(buttonText);
				posButton.setPreferredSize(preferredSize);
				posButton.addActionListener(this);
				ticketNumberPanel.add(posButton, gbc);
			}
		}
		btnClear.setIcon(IconFactory.getIcon("clear_32.png"));
		tfTicketNumber.setBackground(Color.WHITE);

		btnPrev.setIcon(IconFactory.getIcon("previous_32.png"));
		btnPrev.setText("");
		btnPrev.setActionCommand("PREV");
		btnNext.setIcon(IconFactory.getIcon("next_32.png"));
		btnNext.setText("");
		btnNext.setActionCommand("NEXT");
		btnCancel.setIcon(IconFactory.getIcon("cancel_32.png"));

		gbc.weighty = 1;
		gbc.gridx = 0;
		gbc.gridy = GridBagConstraints.RELATIVE;
		gbc.gridwidth = 3;

		TransparentPanel transparentPanel = new TransparentPanel();
		ticketNumberPanel.add(transparentPanel, gbc);

		ticketListPanel.setLayout(cardLayout);

		loadTickets();

		titlePanel.setTitle("SELECT A TICKET");
		setTitle("MDSS-POS");

		setSize(794, 575);
		setResizable(false);
		
		titlePanel.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1 && e.isControlDown()) {
					Dimension size = getSize();
					size.width += 1;
					setSize(size);
				}
				else if(e.getButton() == MouseEvent.BUTTON1 && e.isAltDown()) {
					Dimension size = getSize();
					size.height += 1;
					setSize(size);
				}
				else if(e.getButton() == MouseEvent.BUTTON3 && e.isControlDown()) {
					Dimension size = getSize();
					size.width -= 1;
					setSize(size);
				}
				else if(e.getButton() == MouseEvent.BUTTON3 && e.isAltDown()) {
					Dimension size = getSize();
					size.height -= 1;
					setSize(size);
				}
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}
			
		});
	}

	public void loadTickets() {
		try {
			TicketDAO dao = new TicketDAO();
			List<Ticket> openTickets = dao.findOpenTickets();

			TransparentPanel ticketPanel = null;
			int count = 0;
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.weightx = 1;
			gbc.gridx = 0;
			gbc.gridy = GridBagConstraints.RELATIVE;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.insets = new Insets(3, 3, 3, 3);

			for (Iterator iter = openTickets.iterator(); iter.hasNext(); count++) {
				Ticket ticket = (Ticket) iter.next();
				if (count % 6 == 0) {
					ticketPanel = new TransparentPanel(new GridBagLayout());
					ticketListPanel.add(ticketPanel, String.valueOf(count));
				}
				gbc.weighty = 0;
				ticketPanel.add(new TicketButton(ticket), gbc);

				if (!iter.hasNext()) {
					gbc.weighty = 1;
					ticketPanel.add(new TransparentPanel(), gbc);
				}
			}

			//openTickets.clear();
		} catch (Exception e) {
			POSMessageDialog.showMessage(e.getMessage());
		}
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
	private void initComponents() {
		titlePanel = new com.mdss.pos.ui.TitlePanel();
		ticketNumberOuterPanel = new com.mdss.pos.swing.TransparentPanel();
		transparentPanel1 = new com.mdss.pos.swing.TransparentPanel();
		btnClear = new com.mdss.pos.swing.PosButton();
		tfTicketNumber = new javax.swing.JTextField();
		ticketNumberPanel = new com.mdss.pos.swing.TransparentPanel();
		openTicketsPanel = new com.mdss.pos.swing.TransparentPanel();
		buttonPanel = new com.mdss.pos.swing.TransparentPanel();
		btnPrev = new com.mdss.pos.swing.PosButton();
		btnNext = new com.mdss.pos.swing.PosButton();
		btnCancel = new com.mdss.pos.swing.PosButton();
		ticketListPanel = new com.mdss.pos.swing.TransparentPanel();

		getContentPane().setLayout(new java.awt.BorderLayout(5, 5));

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		getContentPane().add(titlePanel, java.awt.BorderLayout.NORTH);

		ticketNumberOuterPanel.setLayout(new java.awt.BorderLayout(5, 5));

		ticketNumberOuterPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Enter Ticket Number", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
		ticketNumberOuterPanel.setPreferredSize(new java.awt.Dimension(250, 93));
		transparentPanel1.setLayout(new java.awt.BorderLayout(5, 5));

		transparentPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 3, 1, 3));
		btnClear.setText("CLEAR");
		btnClear.setPreferredSize(new java.awt.Dimension(90, 40));
		btnClear.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnCleardoClearTicketNumber(evt);
			}
		});

		transparentPanel1.add(btnClear, java.awt.BorderLayout.EAST);

		tfTicketNumber.setColumns(5);
		tfTicketNumber.setEditable(false);
		tfTicketNumber.setFont(new java.awt.Font("Tahoma", 1, 24));
		transparentPanel1.add(tfTicketNumber, java.awt.BorderLayout.CENTER);

		ticketNumberOuterPanel.add(transparentPanel1, java.awt.BorderLayout.NORTH);

		org.jdesktop.layout.GroupLayout ticketNumberPanelLayout = new org.jdesktop.layout.GroupLayout(ticketNumberPanel);
		ticketNumberPanel.setLayout(ticketNumberPanelLayout);
		ticketNumberPanelLayout.setHorizontalGroup(ticketNumberPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 238, Short.MAX_VALUE));
		ticketNumberPanelLayout.setVerticalGroup(ticketNumberPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 385, Short.MAX_VALUE));
		ticketNumberOuterPanel.add(ticketNumberPanel, java.awt.BorderLayout.CENTER);

		getContentPane().add(ticketNumberOuterPanel, java.awt.BorderLayout.WEST);

		openTicketsPanel.setLayout(new java.awt.BorderLayout(5, 5));

		openTicketsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Open Tickets", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
		buttonPanel.setLayout(new java.awt.GridLayout(1, 0, 5, 0));

		buttonPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 3, 1, 3));
		buttonPanel.setPreferredSize(new java.awt.Dimension(100, 50));
		btnPrev.setText("PREV");
		btnPrev.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnPrevdoGoPrev(evt);
			}
		});

		buttonPanel.add(btnPrev);

		btnNext.setText("NEXT");
		btnNext.setPreferredSize(new java.awt.Dimension(100, 50));
		btnNext.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnNextdoGoNext(evt);
			}
		});

		buttonPanel.add(btnNext);

		btnCancel.setText("CANCEL");
		btnCancel.setPreferredSize(new java.awt.Dimension(220, 50));
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnCanceldoCancel(evt);
			}
		});

		buttonPanel.add(btnCancel);

		openTicketsPanel.add(buttonPanel, java.awt.BorderLayout.SOUTH);

		org.jdesktop.layout.GroupLayout ticketListPanelLayout = new org.jdesktop.layout.GroupLayout(ticketListPanel);
		ticketListPanel.setLayout(ticketListPanelLayout);
		ticketListPanelLayout.setHorizontalGroup(ticketListPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 534, Short.MAX_VALUE));
		ticketListPanelLayout.setVerticalGroup(ticketListPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 377, Short.MAX_VALUE));
		openTicketsPanel.add(ticketListPanel, java.awt.BorderLayout.CENTER);

		getContentPane().add(openTicketsPanel, java.awt.BorderLayout.CENTER);

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void btnCanceldoCancel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanceldoCancel
		selectedTicket = null;
		canceled = true;
		setVisible(false);
		dispose();
	}//GEN-LAST:event_btnCanceldoCancel

	private void btnNextdoGoNext(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextdoGoNext
		cardLayout.next(ticketListPanel);
	}//GEN-LAST:event_btnNextdoGoNext

	private void btnPrevdoGoPrev(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevdoGoPrev
		cardLayout.previous(ticketListPanel);
	}//GEN-LAST:event_btnPrevdoGoPrev

	private void btnCleardoClearTicketNumber(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCleardoClearTicketNumber
		tfTicketNumber.setText("");
	}//GEN-LAST:event_btnCleardoClearTicketNumber

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();

		if (actionCommand.equals("GO")) {
			Session session = null;
			try {
				String ticketNum = tfTicketNumber.getText();
				if (ticketNum == null || ticketNum.trim().equals("")) {
					return;
				}

				int ticketId = 0;
				try {
					ticketId = Integer.parseInt(tfTicketNumber.getText());
				} catch (NumberFormatException x) {
					return;
				}
				TicketDAO dao = new TicketDAO();
				session = dao.getSession();
				Ticket ticket = dao.get(ticketId, session);
				if (ticket == null) {
					POSMessageDialog.showError("Ticket not found");
					return;
				}
				ticket = dao.initializeTicket(ticket);
				selectedTicket = ticket;
				canceled = false;
				setVisible(false);
				dispose();
			} catch (Exception ex) {
				POSMessageDialog.showError(ex.getMessage());
			} finally {
				if (session != null) {
					session.close();
				}
			}
		}

		else {
			tfTicketNumber.setText(tfTicketNumber.getText() + actionCommand);
		}
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private com.mdss.pos.swing.PosButton btnCancel;
	private com.mdss.pos.swing.PosButton btnClear;
	private com.mdss.pos.swing.PosButton btnNext;
	private com.mdss.pos.swing.PosButton btnPrev;
	private com.mdss.pos.swing.TransparentPanel buttonPanel;
	private com.mdss.pos.swing.TransparentPanel openTicketsPanel;
	private javax.swing.JTextField tfTicketNumber;
	private com.mdss.pos.swing.TransparentPanel ticketListPanel;
	private com.mdss.pos.swing.TransparentPanel ticketNumberOuterPanel;
	private com.mdss.pos.swing.TransparentPanel ticketNumberPanel;
	private com.mdss.pos.ui.TitlePanel titlePanel;
	private com.mdss.pos.swing.TransparentPanel transparentPanel1;

	// End of variables declaration//GEN-END:variables

	class TicketButton extends PosButton implements ActionListener {
		Ticket ticket;

		public TicketButton(Ticket t) {
			this.ticket = t;

			setText(ticket.getTitle());
			setPreferredSize(buttonDimension);
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				ticket = TicketDAO.getInstance().initializeTicket(ticket);
				selectedTicket = ticket;
			} catch (Exception ex) {
				POSMessageDialog.showError(ex.getMessage());
				return;
			} 
			canceled = false;
			setVisible(false);
			dispose();
		}

		@Override
		protected void finalize() throws Throwable {
			ticket = null;
			removeActionListener(this);
			super.finalize();
		}
	}

	public Ticket getSelectedTicket() {
		return selectedTicket;
	}

	public void setSelectedTicket(Ticket selectedTicket) {
		this.selectedTicket = selectedTicket;
	}
}
