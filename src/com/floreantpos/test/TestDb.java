package com.floreantpos.test;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;

import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXFrame;

import com.floreantpos.Messages;

public class TestDb {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		//test
		JXCollapsiblePane cp = new JXCollapsiblePane();

		 // JXCollapsiblePane can be used like any other container
		 cp.setLayout(new BorderLayout());
		 
		 // the Controls panel with a textfield to filter the tree
		 JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
		 controls.add(new JLabel(Messages.getString("TestDb.0"))); //$NON-NLS-1$
		 controls.add(new JTextField(10));    
		 controls.add(new JButton(Messages.getString("TestDb.1"))); //$NON-NLS-1$
		 controls.setBorder(new TitledBorder(Messages.getString("TestDb.2"))); //$NON-NLS-1$
		 cp.add(Messages.getString("TestDb.3"), controls); //$NON-NLS-1$
		   
		 JXFrame frame = new JXFrame();
		 frame.setLayout(new BorderLayout());
		  
		 // Put the "Controls" first
		 frame.add(Messages.getString("TestDb.4"), cp); //$NON-NLS-1$
		    
		 // Then the tree - we assume the Controls would somehow filter the tree
		 JScrollPane scroll = new JScrollPane(new JTree());
		 frame.add(Messages.getString("TestDb.5"), scroll); //$NON-NLS-1$

		 // Show/hide the "Controls"
		 
		 Action a = cp.getActionMap().get("toggle"); //$NON-NLS-1$
		JButton toggle = new JButton(a);
		 frame.add(Messages.getString("TestDb.7"), toggle); //$NON-NLS-1$

		 frame.pack();
		 frame.setVisible(true);

	}



}
