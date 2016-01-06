/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
package com.floreantpos.model;

import java.awt.Color;
import java.awt.Image;
import java.util.List;

import javax.swing.ImageIcon;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.base.BaseMenuItem;

@XmlRootElement(name = "menu-item")
public class MenuItem extends BaseMenuItem {

	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public MenuItem () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public MenuItem (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public MenuItem (
		java.lang.Integer id,
		java.lang.String name,
		java.lang.Double buyPrice,
		java.lang.Double price) {

		super (
			id,
			name,
			buyPrice,
			price);
	}

	/*[CONSTRUCTOR MARKER END]*/
	
	private Color buttonColor;
	private Color textColor;
	
	private ImageIcon image;

	@XmlTransient
	public ImageIcon getImage() {
		if(image != null) {
			return image;
		}
		
		int width = 100;
		int height = 100;
		byte[] imageData = getImageData();
		if (imageData != null) {
			image = new ImageIcon(imageData);
			image = new ImageIcon(image.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
		}
		return image;
	}

	public void setImage(ImageIcon icon) {

	}

	@Override
	public Integer getSortOrder() {
		return sortOrder == null ? 9999 : sortOrder;
	}

	@XmlTransient
	public Color getButtonColor() {
		if(buttonColor != null) {
			return buttonColor;
		}
		
		if(getButtonColorCode() == null) {
			return null;
		}
		
		return buttonColor = new Color(getButtonColorCode());
	}
	
	public void setButtonColor(Color buttonColor) {
		this.buttonColor = buttonColor;
	}

	@XmlTransient
	public Color getTextColor() {
		if(textColor != null) {
			return textColor;
		}
		
		if(getTextColorCode() == null) {
			return null;
		}
		
		return textColor = new Color(getTextColorCode());
	}
	
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}
	
	@XmlTransient
	public String getDisplayName() {
		if (TerminalConfig.isUseTranslatedName() && StringUtils.isNotEmpty(getTranslatedName())) {
			return getTranslatedName();
		}

		return super.getName();
	}

	public double getPrice(Shift currentShift) {
		List<MenuItemShift> shifts = getShifts();
		double price = super.getPrice();

		if (currentShift == null) {
			return price;
		}
		if (shifts == null || shifts.size() == 0) {
			return price;
		}

		//		Date formattedTicketTime = ShiftUtil.formatShiftTime(ticketCreateTime);
		//		Calendar calendar = Calendar.getInstance();
		//		calendar.setTime(formattedTicketTime);
		//		formattedTicketTime = calendar.getTime();
		//		
		for (MenuItemShift shift : shifts) {
			if (shift.getShift().equals(currentShift)) {
				return shift.getShiftPrice();
			}
			//			Date startTime = shift.getShift().getStartTime();
			//			Date endTime = shift.getShift().getEndTime();
			//			if(startTime.after(currentShift.getStartTime()) && endTime.before(currentShift.getEndTime())) {
			//				return shift.getShiftPrice();
			//			}
		}
		return price;
	}

	@Override
	public String toString() {
		return getName();
	}

	public String getUniqueId() {
		return ("menu_item_" + getName() + "_" + getId()).replaceAll("\\s+", "_"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	public TicketItem convertToTicketItem() {
		TicketItem ticketItem = new TicketItem();
		ticketItem.setItemId(this.getId());
		ticketItem.setItemCount(1);
		ticketItem.setName(this.getDisplayName());
		ticketItem.setGroupName(this.getParent().getDisplayName());
		ticketItem.setCategoryName(this.getParent().getParent().getDisplayName());
		ticketItem.setUnitPrice(this.getPrice(Application.getInstance().getCurrentShift()));
		ticketItem.setTaxRate(this.getTax() == null ? 0 : this.getTax().getRate());
		ticketItem.setHasModifiers(hasModifiers());
		if (this.getParent().getParent().isBeverage()) {
			ticketItem.setBeverage(true);
			ticketItem.setShouldPrintToKitchen(false);
		} else {
			ticketItem.setBeverage(false);
			ticketItem.setShouldPrintToKitchen(true);
		}
		ticketItem.setPrinterGroup(this.getPrinterGroup());

		Recepie recepie = getRecepie();
		if (recepie != null) {
			List<RecepieItem> recepieItems = recepie.getRecepieItems();
			for (RecepieItem recepieItem : recepieItems) {
				InventoryItem inventoryItem = recepieItem.getInventoryItem();
				Double recepieUnits = inventoryItem.getTotalRecepieUnits();
				//Double percentage = recepieItem.getPercentage();
				--recepieUnits;

			}

		}

		return ticketItem;
	}

	public boolean hasModifiers() {
		return (this.getMenuItemModiferGroups() != null && this.getMenuItemModiferGroups().size() > 0);
	}

	public ImageIcon getScaledImage(int width, int height) {
		ImageIcon icon = new ImageIcon(getImageData());
		Image scaledInstance = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return new ImageIcon(scaledInstance);
	}

}