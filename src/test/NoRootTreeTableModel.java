package test;

import java.util.ArrayList;
import java.util.List;

import org.apache.ecs.xhtml.object;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

import com.floreantpos.model.ITicketItem;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.TicketItemModifierGroup;

public class NoRootTreeTableModel extends AbstractTreeTableModel {
	private final static String[] COLUMN_NAMES = { "ITEM", "U/PRICE", "UNIT", "PRICE" };

	private List<Department> departmentList;
	private Ticket ticket;

	public NoRootTreeTableModel(Ticket ticket) {
		super(ticket);
		this.ticket = ticket;
	}

	@Override
	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}

	@Override
	public String getColumnName(int column) {
		return COLUMN_NAMES[column];
	}

	@Override
	public boolean isCellEditable(Object node, int column) {
		return false;
	}

	@Override
	public boolean isLeaf(Object node) {
		if (node instanceof Ticket) {
			Ticket ticket = (Ticket) node;
			return ticket.getTicketItems() == null || ticket.getTicketItems().size() == 0;
		}
		
		if (node instanceof TicketItem) {
			TicketItem ticketItem = (TicketItem) node;
			return !ticketItem.isHasModifiers();
		}
		
		return true;
	}

	@Override
	public int getChildCount(Object parent) {
		System.out.println(parent.getClass());
		if (parent instanceof Ticket) {
			return ((Ticket) parent).getTicketItems().size();
		}
		
		if (parent instanceof TicketItem) {
			TicketItem ticketItem = (TicketItem) parent;
			if (ticketItem.isHasModifiers()) {
				List<TicketItemModifierGroup> modifierGroups = ticketItem.getTicketItemModifierGroups();
				int size = 0;
				for (TicketItemModifierGroup ticketItemModifierGroup : modifierGroups) {
					size += ticketItemModifierGroup.getTicketItemModifiers().size();
				}
				return size;
			}

			return 0;
		}
		
		return -1;
	}

	@Override
	public Object getChild(Object parent, int index) {
		
		if (parent instanceof Ticket) {
			return ((Ticket) parent).getTicketItems().get(index);
		}
		if (parent instanceof TicketItem) {
			TicketItem ticketItem = (TicketItem) parent;
			List<TicketItemModifierGroup> modifierGroups = ticketItem.getTicketItemModifierGroups();
			List<TicketItemModifier> modifiers = new ArrayList<TicketItemModifier>();
			for (TicketItemModifierGroup modifierGroup : modifierGroups) {
				List<TicketItemModifier> itemModifiers = modifierGroup.getTicketItemModifiers();
				for (TicketItemModifier ticketItemModifier : itemModifiers) {
					modifiers.add(ticketItemModifier);
				}
			}
			
			return modifiers.get(index);
		}
		
		return -1;
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		if (parent instanceof Ticket) {
			Ticket ticket = (Ticket) parent;
			return ticket.getTicketItems().indexOf(child);
		}
		
		if (parent instanceof TicketItem) {
			TicketItem ticketItem = (TicketItem) parent;
			List<TicketItemModifierGroup> modifierGroups = ticketItem.getTicketItemModifierGroups();
			return modifierGroups.indexOf(child);
		}
		
		return -1;
	}

	@Override
	public Object getValueAt(Object node, int column) {
		
//		if (node instanceof TicketItem) {
//			TicketItem ticketItem = (TicketItem) node;
//			switch (column) {
//			case 0:
//				return ticketItem.getNameDisplay();
//			case 1:
//				return ticketItem.getUnitPriceDisplay();
//			}
//		} else if (node instanceof TicketItemModifier) {
//			Employee emp = (Employee) node;
//			switch (column) {
//			case 0:
//				return emp.getId();
//			case 1:
//				return emp.getName();
//			case 2:
//				return emp.getDoj();
//			case 3:
//				return emp.getPhoto();
//			}
//		}
		
		if(!(node instanceof ITicketItem)) {
			return null;
		}
		
		ITicketItem ticketItem = (ITicketItem) node;
		
		switch (column) {
		case 0:
			return ticketItem.getNameDisplay();
		}
		
		return null;
	}

}