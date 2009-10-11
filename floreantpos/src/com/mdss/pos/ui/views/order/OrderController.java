package com.mdss.pos.ui.views.order;

import com.mdss.pos.main.Application;
import com.mdss.pos.model.MenuCategory;
import com.mdss.pos.model.MenuGroup;
import com.mdss.pos.model.MenuItem;
import com.mdss.pos.model.MenuModifier;
import com.mdss.pos.model.Ticket;
import com.mdss.pos.model.TicketItem;
import com.mdss.pos.model.dao.MenuItemDAO;
import com.mdss.pos.ui.dialog.PaymentTypeSelectionDialog;
import com.mdss.pos.ui.views.SettleTicketView;
import com.mdss.pos.ui.views.order.actions.CategorySelectionListener;
import com.mdss.pos.ui.views.order.actions.GroupSelectionListener;
import com.mdss.pos.ui.views.order.actions.ItemSelectionListener;
import com.mdss.pos.ui.views.order.actions.ModifierSelectionListener;
import com.mdss.pos.ui.views.order.actions.OrderListener;

public class OrderController implements OrderListener, CategorySelectionListener, GroupSelectionListener, ItemSelectionListener, ModifierSelectionListener {
	private OrderView orderView;

	public OrderController(OrderView orderView) {
		this.orderView = orderView;

		orderView.getCategoryView().addCategorySelectionListener(this);
		orderView.getGroupView().addGroupSelectionListener(this);
		orderView.getItemView().addItemSelectionListener(this);
		orderView.getModifierView().addModifierSelectionListener(this);
		orderView.getTicketView().addOrderListener(this);
	}

	public void categorySelected(MenuCategory foodCategory) {
		orderView.getGroupView().setMenuCategory(foodCategory);
		orderView.showView(GroupView.VIEW_NAME);
	}

	public void groupSelected(MenuGroup foodGroup) {
		orderView.getItemView().setMenuGroup(foodGroup);
		orderView.showView(MenuItemView.VIEW_NAME);
		
//		ItemView itemView = new ItemView();
//		itemView.setMenuGroup(foodGroup);
//		PanelTester.width = 600;
//		PanelTester.height = 600;
//		PanelTester.test(itemView);
	}

	public void itemSelected(MenuItem menuItem) {
		MenuItemDAO dao = new MenuItemDAO();
		menuItem = dao.initialize(menuItem);
		
		boolean hasModifiers = (menuItem.getMenuItemModiferGroups() != null && menuItem.getMenuItemModiferGroups().size() > 0);
		
		TicketItem ticketItem = new TicketItem();
		ticketItem.setItemId(menuItem.getId());
		ticketItem.setItemCount(1);
		ticketItem.setName(menuItem.getName());
		ticketItem.setGroupName(menuItem.getParent().getName());
		ticketItem.setCategoryName(menuItem.getParent().getParent().getName());
		ticketItem.setUnitPrice(menuItem.getPrice(Application.getInstance().getCurrentShift()));
		ticketItem.setDiscountRate(menuItem.getDiscountRate());
		ticketItem.setTaxRate(menuItem.getTax() == null ? 0: menuItem.getTax().getRate());
		ticketItem.setHasModifiers(hasModifiers);
		if(menuItem.getParent().getParent().isBeverage()) {
			ticketItem.setBeverage(true);
			ticketItem.setShouldPrintToKitchen(false);
		}
		else {
			ticketItem.setBeverage(false);
			ticketItem.setShouldPrintToKitchen(true);
		}
		orderView.getTicketView().addTicketItem(ticketItem);

		if (hasModifiers) {
			ModifierView modifierView = orderView.getModifierView();
			modifierView.setMenuItem(menuItem, ticketItem);
			orderView.showView(ModifierView.VIEW_NAME);
			
//			ModifierView modifierView = new ModifierView();
//			modifierView.setMenuItem(menuItem, ticketItem);
//			PanelTester.width = 800;
//			PanelTester.height = 600;
//			PanelTester.test(modifierView);
		}
	}

	public void modifierSelected(MenuItem parent, MenuModifier modifier) {
//		TicketItemModifier itemModifier = new TicketItemModifier();
//		itemModifier.setItemId(modifier.getId());
//		itemModifier.setName(modifier.getName());
//		itemModifier.setPrice(modifier.getPrice());
//		itemModifier.setExtraPrice(modifier.getExtraPrice());
//		itemModifier.setMinQuantity(modifier.getMinQuantity());
//		itemModifier.setMaxQuantity(modifier.getMaxQuantity());
//		itemModifier.setTaxRate(modifier.getTax() == null ? 0 : modifier.getTax().getRate());
//		
//		orderView.getTicketView().addModifier(itemModifier);
	}

	public void itemSelectionFinished(MenuGroup parent) {
		MenuCategory menuCategory = parent.getParent();
		GroupView groupView = orderView.getGroupView();
		if(!menuCategory.equals(groupView.getMenuCategory())) {
			groupView.setMenuCategory(menuCategory);
		}
		orderView.showView(GroupView.VIEW_NAME);
	}

	public void modifierSelectionFiniched(MenuItem parent) {
		MenuGroup menuGroup = parent.getParent();
		MenuItemView itemView = orderView.getItemView();
		if(!menuGroup.equals(itemView.getMenuGroup())) {
			itemView.setMenuGroup(menuGroup);
		}
		orderView.showView(MenuItemView.VIEW_NAME);
	}

	public void payOrderSelected(Ticket ticket) {
		PaymentTypeSelectionDialog dialog = new PaymentTypeSelectionDialog();
		dialog.setSize(250, 400);
		dialog.open();
		
		if (!dialog.isCanceled()) {
			SettleTicketView view = SettleTicketView.getInstance();
			view.setPaymentView(dialog.getSelectedPaymentView());
			view.setCurrentTicket(ticket);
			RootView.getInstance().showView(SettleTicketView.VIEW_NAME);
		}
	}
}
