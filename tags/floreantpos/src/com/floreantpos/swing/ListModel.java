package com.floreantpos.swing;

import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListModel;

public class ListModel extends DefaultListModel {

	public ListModel() {
		super();
	}

	public ListModel(List list) {
		super();
		
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			addElement(iter.next());
		}
	}

}
