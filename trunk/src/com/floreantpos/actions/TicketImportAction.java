package com.floreantpos.actions;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

import org.hibernate.Session;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import com.floreantpos.ITicketList;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.MenuModifierGroup;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.TicketItemModifierGroup;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.MenuModifierDAO;
import com.floreantpos.model.dao.TicketDAO;

public class TicketImportAction extends AbstractAction {
	private TicketImporter TICKET_IMPORTER = new TicketImporter();
	private Component parentComponent;

	JTextArea ta = new JTextArea();

	Timer timer = new Timer(60*1000, TICKET_IMPORTER);

	public TicketImportAction(Component parentComponent) {
		super("ONLINE TICKETS");

		this.parentComponent = parentComponent;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFrame frame = new JFrame("Online tickt importer") {
			@Override
			public void setVisible(boolean b) {
				super.setVisible(b);
				
				if(b) {
					timer.start();
				}
				else {
					timer.stop();
				}
			}
			
			@Override
			public void dispose() {
				timer.stop();
				super.dispose();
			}
		};
		//ta.setEditable(false);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(new JScrollPane(ta));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setVisible(true);
		TICKET_IMPORTER.actionPerformed(null);
	}

	class TicketImporter implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				TicketDAO ticketDAO = TicketDAO.getInstance();
				Session session = ticketDAO.createNewSession();

				Ticket ticket = new Ticket();
				ticket.setPriceIncludesTax(Application.getInstance().isPriceIncludesTax());
				ticket.setType(TicketType.TAKE_OUT);
				ticket.setTerminal(Application.getInstance().getTerminal());
				ticket.setOwner(Application.getCurrentUser());
				ticket.setShift(Application.getInstance().getCurrentShift());

				Calendar currentTime = Calendar.getInstance();
				ticket.setCreateDate(currentTime.getTime());
				ticket.setCreationHour(currentTime.get(Calendar.HOUR_OF_DAY));

				SAXBuilder jdomBuilder = new SAXBuilder();
				Document document = jdomBuilder.build(new URL("http://cloud.floreantpos.org/webstore/go.php?id=11&item=1"));

				XPathFactory xFactory = XPathFactory.instance();
				XPathExpression<Element> xPathExpression = xFactory.compile("//ticketItem", Filters.element());
				List<Element> list = xPathExpression.evaluate(document);

				for (Element element : list) {
					String id = element.getChildText("id");
					MenuItemDAO dao = MenuItemDAO.getInstance();
					MenuItem menuItem = dao.get(Integer.parseInt(id), session);

					TicketItem ticketItem = menuItem.convertToTicketItem();
					ticketItem.setTicket(ticket);
					ticket.addToticketItems(ticketItem);

					XPathExpression<Element> xPathExpression2 = xFactory.compile("//modifier", Filters.element());
					List<Element> list2 = xPathExpression2.evaluate(element);

					for (Element modifierElement : list2) {
						MenuModifierDAO menuModifierDAO = MenuModifierDAO.getInstance();
						MenuModifier menuModifier = menuModifierDAO.get(Integer.parseInt(modifierElement.getChildText("id")), session);
						List<MenuItemModifierGroup> menuItemModiferGroups = menuItem.getMenuItemModiferGroups();

						for (MenuItemModifierGroup menuItemModifierGroup : menuItemModiferGroups) {
							MenuModifierGroup modifierGroup = menuItemModifierGroup.getModifierGroup();
							if (modifierGroup.equals(menuModifier.getModifierGroup())) {
								menuModifier.setMenuItemModifierGroup(menuItemModifierGroup);
								break;
							}
						}

						TicketItemModifierGroup modifierGroup = ticketItem.findTicketItemModifierGroup(menuModifier, true);
						modifierGroup.addTicketItemModifier(menuModifier, TicketItemModifier.NORMAL_MODIFIER);
					}
				}
				
				ticketDAO.save(ticket);
				
				ta.append("\nImported ticket with id: " + ticket.getId());
				if(parentComponent instanceof ITicketList) {
					((ITicketList) parentComponent).updateTicketList();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}

}
