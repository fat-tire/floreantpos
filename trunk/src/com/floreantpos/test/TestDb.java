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

public class TestDb {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		JXCollapsiblePane cp = new JXCollapsiblePane();

		 // JXCollapsiblePane can be used like any other container
		 cp.setLayout(new BorderLayout());
		 
		 // the Controls panel with a textfield to filter the tree
		 JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
		 controls.add(new JLabel("Search:"));
		 controls.add(new JTextField(10));    
		 controls.add(new JButton("Refresh"));
		 controls.setBorder(new TitledBorder("Filters"));
		 cp.add("Center", controls);
		   
		 JXFrame frame = new JXFrame();
		 frame.setLayout(new BorderLayout());
		  
		 // Put the "Controls" first
		 frame.add("North", cp);
		    
		 // Then the tree - we assume the Controls would somehow filter the tree
		 JScrollPane scroll = new JScrollPane(new JTree());
		 frame.add("Center", scroll);

		 // Show/hide the "Controls"
		 
		 Action a = cp.getActionMap().get("toggle");
		JButton toggle = new JButton(a);
		 frame.add("South", toggle);

		 frame.pack();
		 frame.setVisible(true);

	}



}
