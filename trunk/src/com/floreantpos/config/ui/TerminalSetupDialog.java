package com.floreantpos.config.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.TitledView;

public class TerminalSetupDialog extends JDialog {
	
	//private JTabbedPane tabbedPane;
	private final PosButton psbtnSave = new PosButton();
	private TerminalConfigurationView terminalConfigurationView;
	private final Action saveAction = new SwingAction();
	private final Action closeAction = new SwingAction_1();
	
	public TerminalSetupDialog() {
		super(Application.getPosWindow(), true);
		
		//tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		//getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		//tabbedPane.addTab("Terminal Setup", new TerminalConfigurationView());
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new MigLayout("", "[64px][71px][][][][][][][][][][][][]", "[][41px]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		JSeparator separator = new JSeparator();
		panel.add(separator, "cell 0 0 14 1,growx"); //$NON-NLS-1$
		psbtnSave.setAction(saveAction);
		psbtnSave.setMargin(new Insets(10, 20, 10, 20));
		psbtnSave.setText(Messages.getString("TerminalSetupDialog.4")); //$NON-NLS-1$
		panel.add(psbtnSave, "cell 12 1,alignx left,aligny top"); //$NON-NLS-1$
		
		PosButton psbtnClose = new PosButton();
		psbtnClose.setAction(closeAction);
		psbtnClose.setMargin(new Insets(10, 20, 10, 20));
		psbtnClose.setText(Messages.getString("TerminalSetupDialog.6")); //$NON-NLS-1$
		panel.add(psbtnClose, "cell 13 1,alignx left,aligny top"); //$NON-NLS-1$
		
		TitledView titledView = new TitledView();
		titledView.setTitle(Messages.getString("TerminalSetupDialog.0")); //$NON-NLS-1$
		getContentPane().add(titledView, BorderLayout.NORTH);
		
		terminalConfigurationView = new TerminalConfigurationView();
		getContentPane().add(terminalConfigurationView, BorderLayout.CENTER);
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TerminalSetupDialog dialog = new TerminalSetupDialog();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SaveAction"); //$NON-NLS-1$
			putValue(SHORT_DESCRIPTION, Messages.getString("TerminalSetupDialog.10")); //$NON-NLS-1$
		}
		public void actionPerformed(ActionEvent e) {
			if(terminalConfigurationView.canSave()) {
				terminalConfigurationView.save();
			}
		}
	}
	private class SwingAction_1 extends AbstractAction {
		public SwingAction_1() {
			putValue(NAME, "CloseAction"); //$NON-NLS-1$
			putValue(SHORT_DESCRIPTION, Messages.getString("TerminalSetupDialog.12")); //$NON-NLS-1$
		}
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
}
