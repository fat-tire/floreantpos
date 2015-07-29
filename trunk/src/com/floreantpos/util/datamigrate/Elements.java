package com.floreantpos.util.datamigrate;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.MenuModifierGroup;
import com.floreantpos.model.Tax;

@XmlRootElement(name = "elements")
public class Elements {
	//	public final static Class[] classes = new Class[] { User.class, Tax.class, MenuCategory.class, MenuGroup.class, MenuModifier.class,
	//			MenuModifierGroup.class, MenuItem.class, MenuItemShift.class, Restaurant.class, UserType.class, UserPermission.class, Shift.class };

	List<Tax> taxes;
	List<MenuCategory> menuCategories;
	List<MenuGroup> menuGroups;
	List<MenuModifier> menuModifiers;
	List<MenuItemModifierGroup> menuItemModifierGroups;
	List<MenuModifierGroup> menuModifierGroups;
	List<MenuItem> menuItems;

	//List<User> users;

	//	List<MenuItemShift> menuItemShifts;
	//	List<Restaurant> restaurants;
	//	List<UserType> userTypes;
	//	List<UserPermission> userPermissions;
	//	List<Shift> shifts;

	public List<MenuCategory> getMenuCategories() {
		return menuCategories;
	}

	public void setMenuCategories(List<MenuCategory> menuCategories) {
		this.menuCategories = menuCategories;
	}

	public List<MenuGroup> getMenuGroups() {
		return menuGroups;
	}

	public void setMenuGroups(List<MenuGroup> menuGroups) {
		this.menuGroups = menuGroups;

		if (menuGroups == null) {
			return;
		}

		for (MenuGroup menuGroup : menuGroups) {
			MenuCategory parent = menuGroup.getParent();
			if(parent != null) {
				parent.setMenuGroups(null);
			}
		}
	}

	public List<MenuModifier> getMenuModifiers() {
		return menuModifiers;
	}

	public void setMenuModifiers(List<MenuModifier> menuModifiers) {
		this.menuModifiers = menuModifiers;

		if (menuModifiers == null)
			return;

		for (MenuModifier menuModifier : menuModifiers) {
			MenuModifierGroup modifierGroup = menuModifier.getModifierGroup();
			if(modifierGroup != null) {
				modifierGroup.setModifiers(null);
			}
		}
	}

	public List<MenuModifierGroup> getMenuModifierGroups() {
		return menuModifierGroups;
	}

	public void setMenuModifierGroups(List<MenuModifierGroup> menuModifierGroups) {
		this.menuModifierGroups = menuModifierGroups;
	}

	public List<MenuItem> getMenuItems() {
		return menuItems;
	}

	public void setMenuItems(List<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}

	//	public List<User> getUsers() {
	//		return users;
	//	}
	//
	//	public void setUsers(List<User> users) {
	//		this.users = users;
	//	}
	//
	public List<Tax> getTaxes() {
		return taxes;
	}

	public void setTaxes(List<Tax> taxes) {
		this.taxes = taxes;
	}

	public List<MenuItemModifierGroup> getMenuItemModifierGroups() {
		return menuItemModifierGroups;
	}

	public void setMenuItemModifierGroups(List<MenuItemModifierGroup> menuItemModifierGroups) {
		this.menuItemModifierGroups = menuItemModifierGroups;
	}

	//	public List<MenuItemShift> getMenuItemShifts() {
	//		return menuItemShifts;
	//	}
	//
	//	public void setMenuItemShifts(List<MenuItemShift> menuItemShifts) {
	//		this.menuItemShifts = menuItemShifts;
	//	}
	//
	//	public List<Restaurant> getRestaurants() {
	//		return restaurants;
	//	}
	//
	//	public void setRestaurants(List<Restaurant> restaurants) {
	//		this.restaurants = restaurants;
	//	}
	//
	//	public List<UserType> getUserTypes() {
	//		return userTypes;
	//	}
	//
	//	public void setUserTypes(List<UserType> userTypes) {
	//		this.userTypes = userTypes;
	//	}
	//
	//	public List<UserPermission> getUserPermissions() {
	//		return userPermissions;
	//	}
	//
	//	public void setUserPermissions(List<UserPermission> userPermissions) {
	//		this.userPermissions = userPermissions;
	//	}
	//
	//	public List<Shift> getShifts() {
	//		return shifts;
	//	}
	//
	//	public void setShifts(List<Shift> shifts) {
	//		this.shifts = shifts;
	//	}

}
