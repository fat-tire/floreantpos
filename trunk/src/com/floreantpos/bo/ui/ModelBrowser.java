package com.floreantpos.bo.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.swingx.JXTable;

import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.ui.BeanEditor;

public class ModelBrowser<E> extends JPanel implements ActionListener, ListSelectionListener {
	
	protected JXTable browserTable;
	private BeanEditor<E> beanEditor;
	
	private JPanel browserPanel = new JPanel(new BorderLayout());
	private JPanel beanPanel = new JPanel(new BorderLayout());
	
	private JButton btnNew = new JButton("NEW");
	private JButton btnEdit = new JButton("EDIT");
	private JButton btnSave = new JButton("SAVE");
	private JButton btnDelete = new JButton("DELETE");
	private JButton btnCancel = new JButton("CANCEL");
	
	public ModelBrowser() {
		this(null, null);	
	}

	public ModelBrowser(ListTableModel<E> tableModel, BeanEditor<E> beanEditor) {
		super();
		this.beanEditor = beanEditor;
		
		browserTable = new JXTable();
		browserTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		browserTable.getSelectionModel().addListSelectionListener(this);
		
		if(tableModel != null) {
			browserTable.setModel(tableModel);
		}
		
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		browserPanel.add(new JScrollPane(browserTable));
		browserTable.setBorder(BorderFactory.createEtchedBorder());
		
		JPanel searchPanel = createSearchPanel();
		if(searchPanel != null) {
			browserPanel.add(searchPanel, BorderLayout.NORTH);
		}
		
		add(browserPanel);
		
		beanPanel.setBorder(BorderFactory.createEtchedBorder());
		beanPanel.add(beanEditor);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(btnNew);
		buttonPanel.add(btnEdit);
		buttonPanel.add(btnSave);
		buttonPanel.add(btnDelete);
		buttonPanel.add(btnCancel);
		beanPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		add(beanPanel, BorderLayout.EAST);
		
		btnNew.addActionListener(this);
		btnEdit.addActionListener(this);
		btnDelete.addActionListener(this);
		btnSave.addActionListener(this);
		btnCancel.addActionListener(this);
	}
	
	public JPanel createSearchPanel() {
		return null;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Command command = Command.fromString(e.getActionCommand());
		
		switch (command) {
			case NEW:
			case EDIT:
				beanEditor.createNew();
				
				btnNew.setEnabled(false);
				btnEdit.setEnabled(false);
				btnSave.setEnabled(true);
				btnDelete.setEnabled(false);
				btnCancel.setEnabled(true);
				break;
				
			case CANCEL:
				E bean = beanEditor.getBean();
				//if(bean.)
				
			

			default:
				break;
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		ListTableModel model = (ListTableModel) browserTable.getModel();
		int selectedRow = browserTable.getSelectedRow();
		
		if(selectedRow < 0) return;
		
		E data = (E) model.getRowData(selectedRow);
		beanEditor.setBean(data);
		
		btnNew.setEnabled(true);
		btnEdit.setEnabled(true);
		btnSave.setEnabled(false);
		btnDelete.setEnabled(true);
		btnCancel.setEnabled(false);
	}
}
