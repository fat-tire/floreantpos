package com.floreantpos.util;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.floreantpos.model.dao.MenuCategoryDAO;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.MenuModifierDAO;
import com.floreantpos.model.dao.MenuModifierGroupDAO;
import com.floreantpos.model.dao._RootDAO;

public class DataExporter {
	public static void main(String[] args) throws Exception {
		_RootDAO.initialize();
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("default-data.obj"));
		
		write(MenuModifierGroupDAO.getInstance().findAll(), out);
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
