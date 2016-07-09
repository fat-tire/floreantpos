package test;

import java.util.*;
import javax.swing.*;
import org.jdesktop.swingx.JXTreeTable;

import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao._RootDAO;

public class TreeTableTest extends JFrame {

	private JXTreeTable treeTable;

	public TreeTableTest() {
		_RootDAO.initialize();
		
		Ticket ticket = TicketDAO.getInstance().loadFullTicket(1);
		
		NoRootTreeTableModel model = new NoRootTreeTableModel(ticket);
		treeTable = new JXTreeTable(model);
		treeTable.setRootVisible(false); // hide the root
		treeTable.setShowsRootHandles(false);
		treeTable.setLeafIcon(null);
		treeTable.setOpenIcon(null);
		treeTable.expandAll();
		add(new JScrollPane(treeTable));

		setTitle("JXTreeTable Example");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new TreeTableTest();
			}
		});
	}
}