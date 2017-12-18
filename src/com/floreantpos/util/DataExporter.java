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
package com.floreantpos.util;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.floreantpos.model.dao.MenuCategoryDAO;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.MenuModifierDAO;
import com.floreantpos.model.dao.ModifierGroupDAO;
import com.floreantpos.model.dao._RootDAO;

public class DataExporter {
	public static void main(String[] args) throws Exception {
		_RootDAO.initialize();
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("default-data.obj")); //$NON-NLS-1$

		write(ModifierGroupDAO.getInstance().findAll(), out);
		write(MenuModifierDAO.getInstance().findAll(), out);
		write(MenuCategoryDAO.getInstance().findAll(), out);
		write(MenuGroupDAO.getInstance().findAll(), out);
		write(MenuItemDAO.getInstance().findAll(), out);

		out.close();
	}

	private static void write(List list, ObjectOutputStream out) throws Exception {
		out.writeObject(list);
	}
}
