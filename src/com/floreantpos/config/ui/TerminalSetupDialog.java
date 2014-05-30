package com.floreantpos.config.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.main.Application;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.TitledView;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;

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
		panel.setLayout(new MigLayout("", "[64px][71px][][][][][][][][][][][][]", "[][41px]"));
		
		JSeparator separator = new JSeparator();
		panel.add(separator, "cell 0 0 14 1,growx");
		psbtnSave.setAction(saveAction);
		psbtnSave.setMargin(new Insets(10, 20, 10, 20));
		psbtnSave.setText("Save");
		panel.add(psbtnSave, "cell 12 1,alignx left,aligny top");
		
		PosButton psbtnClose = new PosButton();
		psbtnClose.setAction(closeAction);
		psbtnClose.setMargin(new Insets(10, 20, 10, 20));
		psbtnClose.setText("Close");
		panel.add(psbtnClose, "cell 13 1,alignx left,aligny top");
		
		TitledView titledView = new TitledView();
		titledView.setTitle("Setup this terminal");
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
			putValue(NAME, "SaveAction");
			putValue(SHORT_DESCRIPTION, "Save");
		}
		public void actionPerformed(ActionEvent e) {
			if(terminalConfigurationView.canSave()) {
				terminalConfigurationView.save();
			}
		}
	}
	private class SwingAction_1 extends AbstractAction {
		public SwingAction_1() {
			putValue(NAME, "CloseAction");
			putValue(SHORT_DESCRIPTION, "Close");
		}
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
}
