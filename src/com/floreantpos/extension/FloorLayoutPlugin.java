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
package com.floreantpos.extension;

import java.awt.Dimension;
import java.util.List;

import com.floreantpos.model.ShopTable;
import com.floreantpos.model.Ticket;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.ui.tableselection.TableSelector;

public abstract class FloorLayoutPlugin extends AbstractFloreantPlugin {

	public final static Dimension defaultFloorImageSize = new Dimension(400, 400);

	public abstract void initialize();

	public abstract void openTicketsAndTablesDisplay();

	public abstract TableSelector createTableSelector();

	public abstract void updateView();

	public abstract List<ShopTable> captureTableNumbers(Ticket ticket);

	public abstract BeanEditor getBeanEditor();
}
