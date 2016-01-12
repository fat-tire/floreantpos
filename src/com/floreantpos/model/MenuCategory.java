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

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.base.BaseMenuCategory;

@XmlRootElement(name = "menu-category")
public class MenuCategory extends BaseMenuCategory {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public MenuCategory () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public MenuCategory (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public MenuCategory (
		java.lang.Integer id,
		java.lang.String name) {

		super (
			id,
			name);
	}

	/*[CONSTRUCTOR MARKER END]*/

	private Color buttonColor;
	private Color textColor;

	@Override
	public Integer getSortOrder() {
		return sortOrder == null ? 9999 : sortOrder;
	}

	@XmlTransient
	public Color getButtonColor() {
		if (buttonColor != null) {
			return buttonColor;
		}

		if (getButtonColorCode() == null || getButtonColorCode() == 0) {
			return null;
		}

		return buttonColor = new Color(getButtonColorCode());
	}

	public void setButtonColor(Color buttonColor) {
		this.buttonColor = buttonColor;
	}

	@XmlTransient
	public Color getTextColor() {
		if (textColor != null) {
			return textColor;
		}

		if (getTextColorCode() == null || getTextColorCode() == 0) {
			return null;
		}

		return textColor = new Color(getTextColorCode());
	}

	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}

	public String getDisplayName() {
		if (TerminalConfig.isUseTranslatedName() && StringUtils.isNotEmpty(getTranslatedName())) {
			return getTranslatedName();
		}

		return super.getName();
	}

	@Override
	public String toString() {
		return getDisplayName();
	}

	public String getUniqueId() {
		return ("menu_category_" + getName() + "_" + getId()).replaceAll("\\s+", "_"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}
}